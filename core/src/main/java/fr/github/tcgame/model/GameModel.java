package fr.github.tcgame.model;

import fr.github.tcgame.model.attack.Attack;
import fr.github.tcgame.model.attack.AttackEffect;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.card.CardFactory;
import fr.github.tcgame.model.deck.Deck;
import fr.github.tcgame.model.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameModel {

    public static final int MANA_GAIN_BASE         = 1;
    public static final int MANA_GAIN_INCREMENT    = 1;
    public static final int TURNS_BEFORE_INCREMENT = 5;
    public static final int INITIAL_HAND_SIZE      = 4;

    public enum Status { EN_COURS, TERMINE }

    private final Player player1;
    private final Player player2;
    private final Deck deck;
    private final Random random;

    private int turnNumber;
    private Status status;
    private Player winner;

    public GameModel(String nameP1, String nameP2) {
        this.player1 = new Player(nameP1);
        this.player2 = new Player(nameP2);
        this.deck = new Deck();
        this.random = new Random();
        this.turnNumber = 0;
        this.status = Status.EN_COURS;
        this.winner = null;
    }

    // --- Init ---

    public void init() {
        List<Card> allCards = CardFactory.createAllCards();
        Collections.shuffle(allCards);
        for (Card c : allCards) deck.addCard(c);

        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            Card c1 = deck.draw();
            Card c2 = deck.draw();
            if (c1 != null) player1.addtoHand(c1);
            if (c2 != null) player2.addtoHand(c2);
        }
        System.out.println("[INIT] Partie initialisée. Main Joueur: " + player1.getHand().size() + " cartes, Main IA: " + player2.getHand().size() + " cartes");
    }

    // --- Tour ---

    public void startTurn() {
        turnNumber++;
        int manaGain = getCurrentManaGain();
        System.out.println("\n=== DEBUT TOUR " + turnNumber + " (gain mana: " + manaGain + ") ===");

        player1.gainMana(manaGain);
        player2.gainMana(manaGain);

        drawForPlayer(player1);
        drawForPlayer(player2);

        applyPoisonDamage(player1, player2);
        applyPoisonDamage(player2, player1);
    }

    private void drawForPlayer(Player player) {
        if (player.cannotDraw()) {
            System.out.println("[PIOCHE] " + player.getName() + " ne peut pas piocher ce tour.");
            player.setCannotDraw(false);
            return;
        }
        Card drawn = deck.draw();
        if (drawn != null) {
            player.addtoHand(drawn);
            System.out.println("[PIOCHE] " + player.getName() + " pioche " + drawn.getName());
        } else {
            System.out.println("[PIOCHE] Pioche vide !");
        }
    }

    private void applyPoisonDamage(Player poisoned, Player opponent) {
        Card active = poisoned.getActiveCard();
        if (active == null || active.getPoisonDamage() <= 0) return;
        Card opActive = opponent.getActiveCard();
        if (opActive != null) {
            System.out.println("[POISON] " + active.getName() + " empoisonne " + opActive.getName() + " (" + active.getPoisonDamage() + " dégâts)");
            opActive.takeDamage(active.getPoisonDamage());
            checkCardDeath(opActive, opponent, poisoned);
        }
    }

    public int getCurrentManaGain() {
        int increments = (turnNumber - 1) / TURNS_BEFORE_INCREMENT;
        return MANA_GAIN_BASE + increments * MANA_GAIN_INCREMENT;
    }

    // --- Déploiement ---

    public boolean deployToBench(Player player, Card card) {
        return player.deployToBench(card);
    }

    public boolean playFromBenchFree(Player player, Card card) {
        return player.playFromBenchFree(card);
    }

    public boolean swapActiveCard(Player player, Card card) {
        // Vérifie le piège TNT de l'adversaire avant d'échanger
        Player opponent = getOpponent(player);
        Card opActive = opponent.getActiveCard();
        if (opActive != null && opActive.hasTrap()) {
            System.out.println("[PIEGE] TNT déclenché ! " + opActive.getName() + " inflige " + opActive.getSpecialAtk().getDamage() + " dégâts au cristal de " + player.getName());
            opActive.setTrap(false);
            player.getCrystal().takeDamage(opActive.getSpecialAtk().getDamage());
            checkEndCondition();
        }
        return player.swapActiveCard(card);
    }

    // --- Combat ---

    public void resolveAttack(Player attacker, boolean useSpecial) {
        if (status == Status.TERMINE) return;

        Player defender = getOpponent(attacker);
        Card atkCard = attacker.getActiveCard();
        Card defCard = defender.getActiveCard();

        if (atkCard == null) {
            System.out.println("[ATTAQUE] " + attacker.getName() + " n'a pas de carte active !");
            return;
        }

        // Carte gelée
        if (atkCard.isFrozen()) {
            System.out.println("[ATTAQUE] " + atkCard.getName() + " est gelé, ne peut pas attaquer !");
            atkCard.clearFreeze();
            return;
        }

        Attack attack = useSpecial ? atkCard.getSpecialAtk() : atkCard.getNormalAtk();
        if (attack == null) {
            System.out.println("[ATTAQUE] Pas d'attaque disponible !");
            return;
        }

        // Vérification mana
        if (!attacker.spendMana(attack.getCostMana())) {
            System.out.println("[ATTAQUE] " + attacker.getName() + " n'a pas assez de mana pour " + attack.getName() + " (besoin " + attack.getCostMana() + ")");
            return;
        }

        // FINAL_SENTENCE : uniquement si cible < 50% PV
        if (attack.getEffect() == AttackEffect.FINAL_SENTENCE && defCard != null && !defCard.isBelowHalfHp()) {
            System.out.println("[ATTAQUE] Sentence Finale : l'ennemi n'est pas assez affaibli !");
            attacker.spendMana(-attack.getCostMana()); // remboursement
            return;
        }

        // Miss chance
        if (atkCard.hasAttackMiss() && random.nextInt(100) < atkCard.getMissChance()) {
            System.out.println("[ATTAQUE] " + atkCard.getName() + " rate son attaque !");
            atkCard.clearMissChance();
            return;
        }
        if (atkCard.hasAttackMiss()) atkCard.clearMissChance();

        // --- Résolution des dégâts ---
        if (defCard != null) {
            // Il y a une carte ennemie active
            System.out.println("[ATTAQUE] " + attacker.getName() + " : " + atkCard.getName()
                + " utilise " + attack.getName() + " contre " + defCard.getName()
                + " (" + attack.getDamage() + " dégâts)");
            defCard.takeDamage(attack.getDamage());
            System.out.println("[ETAT] " + defCard.getName() + " -> " + defCard.getCurrentHp() + "/" + defCard.getHp() + " PV");
        } else {
            // Aucune carte ennemie : dégâts directs au cristal
            System.out.println("[ATTAQUE] " + attacker.getName() + " : " + atkCard.getName()
                + " attaque directement le cristal de " + defender.getName()
                + " (" + attack.getDamage() + " dégâts)");
            defender.getCrystal().takeDamage(attack.getDamage());
            System.out.println("[CRISTAL] " + defender.getName() + " -> " + defender.getCrystal().getCurrentHp() + "/" + defender.getCrystal().getMaxHp() + " PV");
        }

        // Effets spéciaux
        if (attack.hasEffect() && defCard != null) {
            applyEffect(attack, attacker, defender, atkCard, defCard);
        }

        if (defCard != null) checkCardDeath(defCard, defender, attacker);
        checkEndCondition();

        // EXTRA_ATTACK : rejouer une attaque normale automatiquement
        if (attack.getEffect() == AttackEffect.EXTRA_ATTACK) {
            System.out.println("[EXTRA] " + atkCard.getName() + " attaque une deuxième fois !");
            resolveAttack(attacker, false);
        }
    }

    private void applyEffect(Attack attack, Player attacker, Player defender,
                             Card atkCard, Card defCard) {
        int val = attack.getEffectValue();
        System.out.println("[EFFET] Application de " + attack.getEffect() + " (valeur: " + val + ")");
        switch (attack.getEffect()) {
            case DEAL_CRYSTAL_DMG:
                defender.getCrystal().takeDamage(val);
                atkCard.takeDamage(val);
                System.out.println("[EFFET] Dégâts directs au cristal de " + defender.getName() + " (-" + val + " PV). " + atkCard.getName() + " s'inflige aussi " + val + " PV.");
                break;
            case SELF_DMG:
                atkCard.takeDamage(val);
                System.out.println("[EFFET] " + atkCard.getName() + " s'inflige " + val + " PV.");
                break;
            case STEAL_COINS:
                // Reconverti en vol de mana
                if (defender.spendMana(val)) {
                    attacker.gainMana(val);
                    System.out.println("[EFFET] " + atkCard.getName() + " vole " + val + " mana à " + defender.getName());
                }
                break;
            case MISS_CHANCE:
                defCard.applyMissChance(val);
                System.out.println("[EFFET] " + defCard.getName() + " a " + val + "% de rater sa prochaine attaque.");
                break;
            case FORCE_SWAP:
                List<Card> bench = defender.getBench();
                if (!bench.isEmpty()) {
                    Card randomCard = bench.get(random.nextInt(bench.size()));
                    defender.swapActiveCard(randomCard);
                    System.out.println("[EFFET] " + defender.getName() + " est forcé d'échanger pour " + randomCard.getName());
                } else {
                    System.out.println("[EFFET] " + defender.getName() + " n'a pas d'autre carte sur le banc.");
                }
                break;
            case TRAP:
                atkCard.setTrap(true);
                System.out.println("[EFFET] " + atkCard.getName() + " pose un piège TNT !");
                break;
            case POISON:
                atkCard.applyPoison(val);
                System.out.println("[EFFET] " + atkCard.getName() + " empoisonne " + defCard.getName() + " (" + val + " dégâts/tour).");
                break;
            case PROTECT_CRYSTAL:
                atkCard.applyProtectCrystal();
                System.out.println("[EFFET] " + atkCard.getName() + " active son bouclier de cristal !");
                break;
            case REDUCE_DEPLOY_COST:
                attacker.applyReduceDeployCost(val);
                System.out.println("[EFFET] Prochain déploiement de " + attacker.getName() + " coûte " + val + " mana de moins.");
                break;
            default:
                break;
        }
    }

    private void checkCardDeath(Card card, Player owner, Player killer) {
        if (!card.isDead()) return;

        System.out.println("[MORT] " + card.getName() + " de " + owner.getName() + " est éliminée !");

        if (!card.isProtectingCrystal()) {
            System.out.println("[CRISTAL] Les dégâts de la mort de " + card.getName() + " n'atteignent pas le cristal (c'est normal, les dégâts viennent des attaques).");
        }
        card.clearProtectCrystal();
        card.clearAllStatuses();
        deck.putBack(card);
        owner.setActiveCard(null);
        System.out.println("[PIOCHE] " + card.getName() + " retourne dans la pioche.");
    }

    // --- Fin de partie ---

    public void checkEndCondition() {
        if (player1.getCrystal().isDestroyed()) {
            status = Status.TERMINE;
            winner = player2;
            System.out.println("[FIN] Le cristal de " + player1.getName() + " est détruit ! " + player2.getName() + " gagne !");
        } else if (player2.getCrystal().isDestroyed()) {
            status = Status.TERMINE;
            winner = player1;
            System.out.println("[FIN] Le cristal de " + player2.getName() + " est détruit ! " + player1.getName() + " gagne !");
        }
    }

    public boolean isOver() { return status == Status.TERMINE; }

    // --- Utilitaires ---

    public Player getOpponent(Player player) {
        return player == player1 ? player2 : player1;
    }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Deck getDeck()      { return deck; }
    public int getTurnNumber() { return turnNumber; }
    public Status getStatus()  { return status; }
    public Player getWinner()  { return winner; }

    @Override
    public String toString() {
        return String.format("=== Tour %d | %s ===\n  %s\n  %s\n  %s",
            turnNumber, status, deck, player1, player2);
    }
}
