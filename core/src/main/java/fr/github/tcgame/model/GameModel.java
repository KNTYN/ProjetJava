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

    public static final int MANA_GAIN_BASE         = 2;
    public static final int MANA_GAIN_INCREMENT    = 1;
    public static final int TURNS_BEFORE_INCREMENT = 4;
    public static final int INITIAL_HAND_SIZE      = 5;
    public static final int MAX_HAND_SIZE          = 8;
    public static final int CRISTAL_MAX_HP         = 80;

    public enum Status { EN_COURS, TERMINE }

    private final Player player1;
    private final Player player2;
    private final Deck deck;
    private final Random random;

    private int turnNumber;
    private Status status;
    private Player winner;

    private int player1CardsKilled = 0;
    private int player2CardsKilled = 0;
    private int player1DamageDealt = 0;
    private int player2DamageDealt = 0;

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
        player1.setCrystalHp(CRISTAL_MAX_HP);
        player2.setCrystalHp(CRISTAL_MAX_HP);
        System.out.println("[INIT] Partie initialisée. Main Joueur: " + player1.getHand().size() + " cartes, Main IA: " + player2.getHand().size() + " cartes");
    }

    // --- Tour ---

    public void startTurn() {
        turnNumber++;
        int manaGain = getCurrentManaGain();
        System.out.println("\n=== DEBUT TOUR " + turnNumber + " (gain mana: " + manaGain + ") ===");

        player1.clearNewlyDeployed();
        player2.clearNewlyDeployed();

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
        for (Card active : poisoned.getFieldCards()) {
            if (active.getPoisonDamage() <= 0) continue;
            List<Card> enemyField = opponent.getFieldCards();
            if (!enemyField.isEmpty()) {
                Card target = enemyField.get(random.nextInt(enemyField.size()));
                System.out.println("[POISON] " + active.getName() + " empoisonne " + target.getName() + " (" + active.getPoisonDamage() + " dégâts)");
                target.takeDamage(active.getPoisonDamage());
                checkCardDeath(target, opponent);
            }
        }
    }

    public int getCurrentManaGain() {
        int increments = (turnNumber - 1) / TURNS_BEFORE_INCREMENT;
        return MANA_GAIN_BASE + increments * MANA_GAIN_INCREMENT;
    }

    // --- Défausse ---

    public boolean discardFromHand(Player player, int index) {
        if (index < 0 || index >= player.getHand().size()) return false;
        Card card = player.getHand().remove(index);
        deck.putBack(card);
        System.out.println("[DEFAUSSE] " + player.getName() + " défausse " + card.getName());
        return true;
    }

    // --- Soin ---

    public void healCrystal(Player player, int amount) {
        player.getCrystal().heal(amount);
        System.out.println("[SOIN] " + player.getName() + " soigne son cristal de " + amount + " PV → " + player.getCrystal().getCurrentHp() + "/" + player.getCrystal().getMaxHp());
    }

    // --- Déploiement ---

    public boolean deployToBench(Player player, Card card) {
        return player.deployToBench(card);
    }

    public boolean playFromBenchToField(Player player, Card card) {
        return player.playFromBenchToField(card);
    }

    public boolean swapCard(Player player, Card fromField, Card fromBench) {
        Player opponent = getOpponent(player);
        for (Card c : opponent.getFieldCards()) {
            if (c.hasTrap()) {
                System.out.println("[PIEGE] TNT déclenché ! " + c.getName() + " inflige " + c.getSpecialAtk().getDamage() + " dégâts au cristal de " + player.getName());
                c.setTrap(false);
                player.getCrystal().takeDamage(c.getSpecialAtk().getDamage());
                checkEndCondition();
            }
        }
        return player.swapCard(fromField, fromBench);
    }

    // --- Combat ---

    public String resolveAttack(Player attacker, Card atkCard, Card defCard, boolean useSpecial) {
        if (status == Status.TERMINE) return "Partie terminée";

        if (!attacker.getFieldCards().contains(atkCard)) {
            return "Cette carte n'est pas sur votre terrain !";
        }

        if (atkCard.isFrozen()) {
            atkCard.clearFreeze();
            return atkCard.getName() + " est gelé et ne peut pas attaquer !";
        }

        Attack attack = useSpecial ? atkCard.getSpecialAtk() : atkCard.getNormalAtk();
        if (attack == null) return "Pas d'attaque disponible";

        if (!attacker.spendMana(attack.getCostMana())) {
            return "Pas assez de mana ! Besoin: " + attack.getCostMana() + ", Disponible: " + attacker.getMana();
        }

        // FINAL_SENTENCE : uniquement si cible < 50% PV
        if (attack.getEffect() == AttackEffect.FINAL_SENTENCE && defCard != null && !defCard.isBelowHalfHp()) {
            attacker.gainMana(attack.getCostMana()); // remboursement
            return "L'ennemi n'est pas assez affaibli pour Sentence Finale !";
        }

        // Miss chance
        if (atkCard.hasAttackMiss() && random.nextInt(100) < atkCard.getMissChance()) {
            atkCard.clearMissChance();
            return atkCard.getName() + " rate son attaque !";
        }
        if (atkCard.hasAttackMiss()) atkCard.clearMissChance();

        // Stats
        if (attacker == player1) player1DamageDealt += attack.getDamage();
        else player2DamageDealt += attack.getDamage();

        StringBuilder log = new StringBuilder();
        log.append(attacker.getName()).append(" utilise ").append(attack.getName()).append(" ! ");

        if (defCard != null && getOpponent(attacker).getFieldCards().contains(defCard)) {
            // Attaque une carte ennemie
            defCard.takeDamage(attack.getDamage());
            log.append("Inflige ").append(attack.getDamage()).append(" dégâts à ").append(defCard.getName())
                .append(" (").append(defCard.getCurrentHp()).append("/").append(defCard.getHp()).append(" PV)");

            if (attack.hasEffect()) {
                applyEffect(attack, attacker, getOpponent(attacker), atkCard, defCard);
            }

            if (defCard.isDead()) {
                log.append(" → 💀 ").append(defCard.getName()).append(" est MORT !");
                if (attacker == player1) player1CardsKilled++;
                else player2CardsKilled++;
                killCard(defCard, getOpponent(attacker));
            }
        } else if (defCard == null) {
            // Attaque directe au cristal
            Player defender = getOpponent(attacker);
            defender.getCrystal().takeDamage(attack.getDamage());
            log.append("Attaque directe au cristal ! ").append(attack.getDamage()).append(" dégâts")
                .append(" (").append(defender.getCrystal().getCurrentHp()).append("/").append(defender.getCrystal().getMaxHp()).append(" PV)");
        } else {
            return "Cible invalide !";
        }

        checkEndCondition();

        // EXTRA_ATTACK : rejouer une attaque normale
        if (attack.getEffect() == AttackEffect.EXTRA_ATTACK && !isOver()) {
            log.append("\n⚡ Attaque bonus !");
            String bonusLog = resolveAttack(attacker, atkCard, defCard, false);
            log.append("\n").append(bonusLog);
        }

        return log.toString();
    }

    public String resolveAttackRandom(Player attacker, boolean useSpecial) {
        if (!attacker.hasActiveCard()) return "Pas de carte active";
        Card atkCard = attacker.getFieldCards().get(random.nextInt(attacker.getFieldCards().size()));
        Player defender = getOpponent(attacker);
        Card defCard = null;
        if (!defender.getFieldCards().isEmpty()) {
            defCard = defender.getFieldCards().get(random.nextInt(defender.getFieldCards().size()));
        }
        return resolveAttack(attacker, atkCard, defCard, useSpecial);
    }

    // --- Sorts ---

    public String resolveSpell(Player caster, Card spellCard, boolean useSpecial, Card targetCard) {
        if (status == Status.TERMINE) return "Partie terminée";

        // ==================== TASTY CROUSTY ====================
        if (spellCard.getName().equals("Tasty Crousty")) {
            Attack spell = useSpecial ? spellCard.getSpecialAtk() : spellCard.getNormalAtk();
            if (!caster.spendMana(spell.getCostMana())) {
                return "Pas assez de mana ! (besoin: " + spell.getCostMana() + ")";
            }

            if (!useSpecial && caster.getFieldCards().size() >= 6) {
                // Appel du Crousty : 6 créatures sur VOTRE terrain
                status = Status.TERMINE;
                winner = caster;
                caster.getHand().remove(spellCard);
                deck.putBack(spellCard);
                System.out.println("[TASTY CROUSTY] " + caster.getName() + " a 6 créatures sur son terrain !");
                return "🎉 TASTY CROUSTY ! " + caster.getName() + " a 6 créatures et remporte la partie !";
            } else if (useSpecial && getOpponent(caster).getFieldCards().size() >= 6) {
                // Festin Suprême : 6 créatures sur le terrain ADVERSE
                status = Status.TERMINE;
                winner = caster;
                caster.getHand().remove(spellCard);
                deck.putBack(spellCard);
                System.out.println("[FESTIN SUPREME] " + caster.getName() + " profite des 6 créatures adverses !");
                return "🎉 FESTIN SUPRÊME ! " + caster.getName() + " profite des 6 créatures adverses et gagne !";
            } else {
                caster.gainMana(spell.getCostMana()); // Remboursement
                return "❌ Pas assez de créatures sur le terrain ! (il faut 6)";
            }
        }

        // ==================== AUTRES SORTS ====================
        Attack spell = useSpecial ? spellCard.getSpecialAtk() : spellCard.getNormalAtk();
        if (!caster.spendMana(spell.getCostMana())) {
            return "Pas assez de mana !";
        }

        String spellName = spell.getName();
        StringBuilder log = new StringBuilder();
        log.append(spellName).append(" ! ");

        // Soin
        if (spellName.contains("Soin")) {
            int healAmount = spellName.contains("Grand") ? 20 : 10;
            healCrystal(caster, healAmount);
            log.append("Soigne ").append(healAmount).append(" PV au cristal");
        }
        // Boule de feu
        else if (spellName.contains("Flamme") || spellName.contains("Inferno")) {
            if (targetCard != null && getOpponent(caster).getFieldCards().contains(targetCard)) {
                targetCard.takeDamage(spell.getDamage());
                log.append(spell.getDamage()).append(" dégâts à ").append(targetCard.getName());
                if (targetCard.isDead()) {
                    killCard(targetCard, getOpponent(caster));
                }
            } else if (targetCard == null) {
                getOpponent(caster).getCrystal().takeDamage(spell.getDamage());
                log.append(spell.getDamage()).append(" dégâts directs au cristal !");
            } else {
                return "Cible invalide !";
            }
        }
        // Freeze
        else if (spellName.contains("Gelure") || spellName.contains("Blizzard")) {
            if (spellName.contains("Blizzard")) {
                for (Card c : getOpponent(caster).getFieldCards()) c.applyFreeze();
                log.append("Toutes les cartes ennemies sont gelées !");
            } else if (targetCard != null && getOpponent(caster).getFieldCards().contains(targetCard)) {
                targetCard.applyFreeze();
                log.append(targetCard.getName()).append(" est gelé !");
            } else {
                return "Cible invalide !";
            }
        }
        // Shuffle
        else if (spellName.contains("Tourbillon") || spellName.contains("Tornade")) {
            Player opponent = getOpponent(caster);
            int swaps = spellName.contains("Tornade") ? 2 : 1;
            int done = 0;
            for (int i = 0; i < swaps; i++) {
                if (!opponent.getBench().isEmpty() && !opponent.getFieldCards().isEmpty()) {
                    Card fromBench = opponent.getBench().get(random.nextInt(opponent.getBench().size()));
                    Card fromField = opponent.getFieldCards().get(random.nextInt(opponent.getFieldCards().size()));
                    if (opponent.swapCard(fromField, fromBench)) done++;
                }
            }
            log.append(done).append(" échange(s) forcé(s) !");
        }
        // Boost
        else if (spellName.contains("Encouragement") || spellName.contains("Adrénaline")) {
            log.append("Effet de boost temporaire !");
        }

        caster.getHand().remove(spellCard);
        deck.putBack(spellCard);
        checkEndCondition();
        return log.toString();
    }

    // --- Effets ---

    private void applyEffect(Attack attack, Player attacker, Player defender, Card atkCard, Card defCard) {
        int val = attack.getEffectValue();
        System.out.println("[EFFET] Application de " + attack.getEffect() + " (valeur: " + val + ")");
        switch (attack.getEffect()) {
            case DEAL_CRYSTAL_DMG:
                defender.getCrystal().takeDamage(val);
                atkCard.takeDamage(val);
                System.out.println("[EFFET] Dégâts directs au cristal de " + defender.getName() + " (-" + val + " PV). " + atkCard.getName() + " s'inflige aussi " + val + " PV.");
                checkCardDeath(atkCard, attacker);
                break;
            case SELF_DMG:
                atkCard.takeDamage(val);
                System.out.println("[EFFET] " + atkCard.getName() + " s'inflige " + val + " PV.");
                checkCardDeath(atkCard, attacker);
                break;
            case STEAL_COINS:
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
                if (!defender.getBench().isEmpty() && !defender.getFieldCards().isEmpty()) {
                    Card randomBench = defender.getBench().get(random.nextInt(defender.getBench().size()));
                    Card randomField = defender.getFieldCards().get(random.nextInt(defender.getFieldCards().size()));
                    defender.swapCard(randomField, randomBench);
                    System.out.println("[EFFET] " + defender.getName() + " est forcé d'échanger pour " + randomBench.getName());
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

    // --- Mort ---

    private void killCard(Card card, Player owner) {
        System.out.println("[MORT] " + card.getName() + " de " + owner.getName() + " est éliminée !");
        if (!card.isProtectingCrystal()) {
            int damage = card.getHp();
            owner.getCrystal().takeDamage(damage);
            System.out.println("[CRISTAL] " + owner.getName() + " perd " + damage + " PV ! (" + owner.getCrystal().getCurrentHp() + "/" + owner.getCrystal().getMaxHp() + ")");
        } else {
            System.out.println("[CRISTAL] Bouclier activé ! " + owner.getName() + " ne perd pas de PV.");
        }
        card.clearProtectCrystal();
        card.clearAllStatuses();
        deck.putBack(card);
        owner.removeFromField(card);
        System.out.println("[PIOCHE] " + card.getName() + " retourne dans la pioche.");
    }

    private void checkCardDeath(Card card, Player owner) {
        if (!card.isDead()) return;
        killCard(card, owner);
        checkEndCondition();
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

    // --- Getters ---

    public int getPlayer1CardsKilled() { return player1CardsKilled; }
    public int getPlayer2CardsKilled() { return player2CardsKilled; }
    public int getPlayer1DamageDealt() { return player1DamageDealt; }
    public int getPlayer2DamageDealt() { return player2DamageDealt; }

    public boolean isOver() { return status == Status.TERMINE; }
    public Player getOpponent(Player player) { return player == player1 ? player2 : player1; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Deck getDeck()      { return deck; }
    public int getTurnNumber() { return turnNumber; }
    public Status getStatus()  { return status; }
    public Player getWinner()  { return winner; }
}
