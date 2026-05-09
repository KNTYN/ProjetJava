package fr.github.tcgame.model.player;

import fr.github.tcgame.model.card.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    public static final int MAX_BENCH_SIZE = 3;
    public static final int MAX_FIELD_SIZE = 6;
    public static final int MAX_MANA       = 15;

    private final String name;
    private final Crystal crystal;

    private final List<Card> hand;
    private final List<Card> bench;
    private final List<Card> field;

    // Cartes qui viennent d'arriver sur le banc ce tour (ne peuvent pas être activées)
    private final List<Card> newlyDeployed;

    private int mana;

    private boolean cannotDraw;
    private int reducedDeployCost;

    public Player(String name) {
        this.name = name;
        this.crystal = new Crystal();
        this.hand = new ArrayList<>();
        this.bench = new ArrayList<>();
        this.field = new ArrayList<>();
        this.newlyDeployed = new ArrayList<>();
        this.mana = 3;
        this.cannotDraw = false;
        this.reducedDeployCost = 0;
    }

    public void addtoHand(Card card) { hand.add(card); }
    public boolean removeFromHand(Card card) { return hand.remove(card); }

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
        newlyDeployed.add(card);  // Marquer comme "vient d'arriver"
        System.out.println("[DEPLOY] " + name + " déploie " + card.getName() + " sur le banc (mana restant: " + mana + ")");
        return true;
    }

    public void clearNewlyDeployed() {
        newlyDeployed.clear();
    }

    public boolean isNewlyDeployed(Card card) {
        return newlyDeployed.contains(card);
    }

    public boolean removeFromBench(Card card) { return bench.remove(card); }

    public boolean playFromBenchToField(Card card) {
        if (!bench.contains(card)) return false;
        if (isNewlyDeployed(card)) {
            System.out.println("[PLAY] " + card.getName() + " vient d'arriver sur le banc, doit attendre le prochain tour !");
            return false;
        }
        if (field.size() >= MAX_FIELD_SIZE) {
            System.out.println("[PLAY] Terrain plein pour " + name + " (" + MAX_FIELD_SIZE + " max)");
            return false;
        }
        bench.remove(card);
        field.add(card);
        System.out.println("[PLAY] " + name + " met " + card.getName() + " sur le terrain (gratuit)");
        return true;
    }

    public boolean swapCard(Card fromField, Card fromBench) {
        if (!field.contains(fromField) || !bench.contains(fromBench)) return false;
        if (isNewlyDeployed(fromBench)) {
            System.out.println("[SWAP] " + fromBench.getName() + " vient d'arriver sur le banc, doit attendre le prochain tour !");
            return false;
        }
        if (mana < 2) {
            System.out.println("[SWAP] Pas assez de mana pour échanger (" + name + ")");
            return false;
        }
        mana -= 2;
        field.remove(fromField);
        bench.remove(fromBench);
        bench.add(fromField);
        field.add(fromBench);
        System.out.println("[SWAP] " + name + " échange " + fromField.getName() + " ↔ " + fromBench.getName());
        return true;
    }

    public void removeFromField(Card card) {
        field.remove(card);
        card.clearAllStatuses();
    }

    public List<Card> getFieldCards() { return field; }
    public Card getActiveCard() { return field.isEmpty() ? null : field.get(0); }
    public void setActiveCard(Card card) {
        if (card == null) field.clear();
        else if (!field.contains(card)) {
            if (field.size() >= MAX_FIELD_SIZE) field.remove(0);
            field.add(card);
        }
    }
    public boolean hasActiveCard() { return !field.isEmpty(); }

    public void gainMana(int amount) { mana = Math.min(MAX_MANA, mana + amount); }
    public void setCrystalHp(int hp) { crystal.setHp(hp); }
    public boolean spendMana(int amount) {
        if (mana < amount) return false;
        mana -= amount;
        return true;
    }

    public void setCannotDraw(boolean value)         { this.cannotDraw = value; }
    public boolean cannotDraw()                      { return cannotDraw; }
    public void applyReduceDeployCost(int reduction) { this.reducedDeployCost = reduction; }

    public String getName()        { return name; }
    public Crystal getCrystal()    { return crystal; }
    public List<Card> getHand()    { return hand; }
    public List<Card> getBench()   { return bench; }
    public int getMana()           { return mana; }

    @Override
    public String toString() {
        return String.format("%s | %s | Mana:%d/%d | Banc:%d | Terrain:%d | Main:%d",
            name, crystal, mana, MAX_MANA, bench.size(), field.size(), hand.size());
    }
}
