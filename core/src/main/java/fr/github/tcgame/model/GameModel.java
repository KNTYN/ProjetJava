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

    // CONSTANTES
    public static final int MANA_GAIN_BASE         = 1;
    public static final int MANA_GAIN_INCREMENT    = 1;
    public static final int TURNS_BEFORE_INCREMENT = 5;
    public static final int INITIAL_HAND_SIZE      = 4;

    public enum Status { EN_COURS, TERMINE }

    // ETAT DE LA PARTIE
    private final Player player1;
    private final Player player2;
    private final Deck deck;
    private final Random random;

    private int turnNumber;
    private Status status;
    private Player winner;

    // CONSTRUCTEUR
    public GameModel(String nameP1, String nameP2) {
        this.player1 = new Player(nameP1);
        this.player2 = new Player(nameP2);
        this.deck = new Deck();
        this.random = new Random();
        this.turnNumber = 0;
        this.status = Status.EN_COURS;
        this.winner = null;
    }


    // INITIALISATION

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
    }


    // DEROULEMENT D'UN TOUR


    public void startTurn() {
        turnNumber++;
        int manaGain = getCurrentManaGain();

        player1.gainMana(manaGain);
        player2.gainMana(manaGain);

        drawForPlayer(player1);
        drawForPlayer(player2);

        applyPoisonDamage(player1, player2);
        applyPoisonDamage(player2, player1);
    }

    private void drawForPlayer(Player player) {
        if (player.cannotDraw()) {
            player.setCannotDraw(false);
            return;
        }
        Card drawn = deck.draw();
        if (drawn != null) player.addtoHand(drawn);
    }

    private void applyPoisonDamage(Player poisoned, Player opponent) {
        Card active = poisoned.getActiveCard();
        if (active == null || active.getPoisonDamage() <= 0) return;
        Card opActive = opponent.getActiveCard();
        if (opActive != null) {
            opActive.takeDamage(active.getPoisonDamage());
            checkCardDeath(opActive, opponent, poisoned);
        }
    }

    public int getCurrentManaGain() {
        int increments = (turnNumber - 1) / TURNS_BEFORE_INCREMENT;
        return MANA_GAIN_BASE + increments * MANA_GAIN_INCREMENT;
    }


    // DEPLOIEMENT


    public boolean deployToBench(Player player, Card card) {
        return player.deployToBench(card);
    }

    public boolean playFromBenchFree(Player player, Card card) {
        return player.playFromBenchFree(card);
    }

    public boolean swapActiveCard(Player player, Card card) {
        // Verifie le piege TNT de l'adversaire avant d'echanger
        Player opponent = getOpponent(player);
        Card opActive = opponent.getActiveCard();
        if (opActive != null && opActive.hasTrap()) {
            opActive.setTrap(false);
            player.getCrystal().takeDamage(opActive.getSpecialAtk().getDamage());
            checkEndCondition();
        }
        return player.swapActiveCard(card);
    }


    // COMBAT

    public void resolveAttack(Player attacker, boolean useSpecial) {
        if (status == Status.TERMINE) return;

        Player defender = getOpponent(attacker);
        Card atkCard = attacker.getActiveCard();
        Card defCard = defender.getActiveCard();
        if (atkCard == null || defCard == null) return;

        // Carte gelee : ne peut pas attaquer
        if (atkCard.isFrozen()) {
            atkCard.clearFreeze();
            return;
        }

        Attack attack = useSpecial ? atkCard.getSpecialAtk() : atkCard.getNormalAtk();
        if (attack == null) return;

        // Verification du cout en mana
        if (!attacker.spendMana(attack.getCostMana())) return;

        // FINAL_SENTENCE : uniquement si cible < 50% PV
        if (attack.getEffect() == AttackEffect.FINAL_SENTENCE && !defCard.isBelowHalfHp()) return;

        // Miss chance sur la carte attaquante
        if (atkCard.hasAttackMiss() && random.nextInt(100) < atkCard.getMissChance()) {
            atkCard.clearMissChance();
            return;
        }
        if (atkCard.hasAttackMiss()) atkCard.clearMissChance();

        // Inflige les degats
        defCard.takeDamage(attack.getDamage());

        // Applique l'effet special
        if (attack.hasEffect()) {
            applyEffect(attack, attacker, defender, atkCard, defCard);
        }

        // Verifie mort et fin de partie
        checkCardDeath(defCard, defender, attacker);
        checkEndCondition();
    }

    private void applyEffect(Attack attack, Player attacker, Player defender,
                             Card atkCard, Card defCard) {
        int val = attack.getEffectValue();
        switch (attack.getEffect()) {
            case DEAL_CRYSTAL_DMG:
                // Ganon : 2 degats directs au cristal adverse, Ganon perd 2 PV
                defender.getCrystal().takeDamage(val);
                atkCard.takeDamage(val);
                break;
            case SELF_DMG:
                atkCard.takeDamage(val);
                break;
            case STEAL_COINS:
                if (defender.spendCoins(val)) attacker.gainCoins(val);
                break;
            case BONUS_COINS:
                // Gere dans checkCardDeath si c'est un kill
                break;
            case MISS_CHANCE:
                defCard.applyMissChance(val);
                break;
            case EXTRA_ATTACK:
                // Leclerc : le GameController peut appeler resolveAttack(attacker, false) une 2e fois
                break;
            case FORCE_SWAP:
                List<Card> bench = defender.getBench();
                if (!bench.isEmpty()) {
                    Card randomCard = bench.get(random.nextInt(bench.size()));
                    defender.swapActiveCard(randomCard);
                }
                break;
            case TRAP:
                atkCard.setTrap(true);
                break;
            case POISON:
                atkCard.applyPoison(val);
                break;
            case PROTECT_CRYSTAL:
                atkCard.applyProtectCrystal();
                break;
            case REDUCE_DEPLOY_COST:
                attacker.applyReduceDeployCost(val);
                break;
            case NO_DRAW:
                defender.setCannotDraw(true);
                break;
            default:
                break;
        }
    }

    private void checkCardDeath(Card card, Player owner, Player killer) {
        if (!card.isDead()) return;

        // Degats au cristal = PV restants de la carte (0 si deja a 0)
        if (!card.isProtectingCrystal()) {
            // Les PV sont a 0, pas de degats supplementaires au cristal
        }
        card.clearProtectCrystal();

        // Recompense : 1 piece par kill, ou bonus si effet BONUS_COINS
        Attack special = card.getSpecialAtk();
        if (special != null && special.getEffect() == AttackEffect.BONUS_COINS) {
            killer.gainCoins(special.getEffectValue());
        } else {
            killer.gainCoins(1);
        }

        // Nettoie les statuts et remet en fin de pioche
        card.clearAllStatuses();
        deck.putBack(card);
        owner.setActiveCard(null);
    }

    // SORTS


    public void playSpell(Player caster, Card spellCard) {
        if (!caster.getHand().contains(spellCard)) return;
        if (!caster.spendCoins(spellCard.getCost())) return;

        Player opponent = getOpponent(caster);
        caster.removeFromHand(spellCard);

        switch (spellCard.getName()) {
            case "Shuffle":
                caster.clearAll();
                opponent.clearAll();
                deck.shuffle();
                break;
            case "Boost":
                // +3 PV temporaire sur la carte active (a gerer cote vue/controller)
                if (caster.getActiveCard() != null) caster.getActiveCard().heal(3);
                break;
            case "Freeze":
                if (opponent.getActiveCard() != null)
                    opponent.getActiveCard().applyFreeze();
                break;
            case "Soin":
                caster.getCrystal().heal(15);
                break;
            case "Burst":
                int dmg = random.nextInt(8) + 1;
                if (random.nextBoolean()) caster.getCrystal().takeDamage(dmg);
                else opponent.getCrystal().takeDamage(dmg);
                break;
            case "Coupe Energie":
                caster.halveMana();
                opponent.halveMana();
                break;
            case "Boomerang":
                Card opActive = opponent.getActiveCard();
                if (opActive != null) {
                    opponent.setActiveCard(null);
                    opponent.getBench().add(opActive);
                }
                break;
            case "Raffinerie":
                if (caster.spendMana(3)) caster.gainCoins(2);
                break;
            case "Pioche":
                for (int i = 0; i < 2; i++) {
                    Card c = deck.draw();
                    if (c != null) caster.addtoHand(c);
                }
                break;
            case "Urssaf":
                opponent.loseHalfCoins();
                break;
            default:
                break;
        }

        deck.putBack(spellCard);
        checkEndCondition();
    }


    // FIN DE PARTIE


    public void checkEndCondition() {
        if (player1.getCrystal().isDestroyed()) {
            status = Status.TERMINE;
            winner = player2;
        } else if (player2.getCrystal().isDestroyed()) {
            status = Status.TERMINE;
            winner = player1;
        }
    }

    public boolean isOver() { return status == Status.TERMINE; }

    // =========================================================
    // UTILITAIRES
    // =========================================================

    public Player getOpponent(Player player) {
        return player == player1 ? player2 : player1;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public Player getPlayer1()    { return player1; }
    public Player getPlayer2()    { return player2; }
    public Deck getDeck()         { return deck; }
    public int getTurnNumber()    { return turnNumber; }
    public Status getStatus()     { return status; }
    public Player getWinner()     { return winner; }

    @Override
    public String toString() {
        return String.format("=== Tour %d | %s ===\n  %s\n  %s\n  %s",
            turnNumber, status, deck, player1, player2);
    }
}
