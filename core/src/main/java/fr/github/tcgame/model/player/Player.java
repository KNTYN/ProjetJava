package fr.github.tcgame.model.player;

import fr.github.tcgame.model.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final int MAX_BENCH_SIZE = 3;
    public static final int MAX_MANA       = 10;

    private final String name;
    private final Crystal crystal;

    private final List<Card> hand;
    private final List<Card> bench;
    private Card activeCard;

    private int mana;

    // Effets temporaires
    private boolean cannotDraw;
    private int reducedDeployCost; // gardé pour l'effet REDUCE_DEPLOY_COST mais basé sur mana

    public Player(String name) {
        this.name = name;
        this.crystal = new Crystal();
        this.hand = new ArrayList<>();
        this.bench = new ArrayList<>();
        this.activeCard = null;
        this.mana = 0;
        this.cannotDraw = false;
        this.reducedDeployCost = 0;
    }

    // --- Main ---

    public void addtoHand(Card card) { hand.add(card); }

    public boolean removeFromHand(Card card) { return hand.remove(card); }

    // --- Déploiement sur le banc (coûte du mana) ---

    public boolean deployToBench(Card card) {
        if (bench.size() >= MAX_BENCH_SIZE) {
            System.out.println("[DEPLOY] Banc plein pour " + name);
            return false;
        }
        int cost = Math.max(0, card.getCost() - reducedDeployCost);
        if (mana < cost) {
            System.out.println("[DEPLOY] Pas assez de mana pour " + name + " (besoin " + cost + ", a " + mana + ")");
            return false;
        }
        mana -= cost;
        reducedDeployCost = 0;
        hand.remove(card);
        bench.add(card);
        System.out.println("[DEPLOY] " + name + " déploie " + card.getName() + " sur le banc (mana restant: " + mana + ")");
        return true;
    }

    public boolean removeFromBench(Card card) { return bench.remove(card); }

    // --- Carte active ---

    // Banc -> En jeu (gratuit si aucune carte active)
    public boolean playFromBenchFree(Card card) {
        if (!bench.contains(card)) return false;
        bench.remove(card);
        activeCard = card;
        System.out.println("[PLAY] " + name + " met " + card.getName() + " en jeu (gratuit)");
        return true;
    }

    // Échange la carte active avec une du banc (coûte 2 mana)
    public boolean swapActiveCard(Card card) {
        if (!bench.contains(card)) return false;
        if (mana < 2) {
            System.out.println("[SWAP] Pas assez de mana pour échanger (" + name + ")");
            return false;
        }
        mana -= 2;
        if (activeCard != null) bench.add(activeCard);
        bench.remove(card);
        activeCard = card;
        System.out.println("[SWAP] " + name + " échange pour " + card.getName() + " (mana restant: " + mana + ")");
        return true;
    }

    public void setActiveCard(Card card) { this.activeCard = card; }

    // --- Mana ---

    public void gainMana(int amount) {
        mana = Math.min(MAX_MANA, mana + amount);
        System.out.println("[MANA] " + name + " gagne " + amount + " mana -> " + mana + "/" + MAX_MANA);
    }

    public boolean spendMana(int amount) {
        if (mana < amount) return false;
        mana -= amount;
        return true;
    }

    public void halveMana() { mana = mana / 2; }

    // --- Effets temporaires ---

    public void setCannotDraw(boolean value)         { this.cannotDraw = value; }
    public boolean cannotDraw()                      { return cannotDraw; }
    public void applyReduceDeployCost(int reduction) { this.reducedDeployCost = reduction; }

    // Shuffle
    public void clearAll() {
        hand.clear();
        bench.clear();
        activeCard = null;
    }

    // --- Getters ---

    public String getName()        { return name; }
    public Crystal getCrystal()    { return crystal; }
    public List<Card> getHand()    { return hand; }
    public List<Card> getBench()   { return bench; }
    public Card getActiveCard()    { return activeCard; }
    public boolean hasActiveCard() { return activeCard != null; }
    public int getMana()           { return mana; }

    @Override
    public String toString() {
        return String.format("%s | %s | Mana:%d/%d | Banc:%d | Main:%d cartes",
            name, crystal, mana, MAX_MANA, bench.size(), hand.size());
    }
}
