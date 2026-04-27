package fr.github.tcgame.model.player;

import fr.github.tcgame.model.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final int MAX_BENCH_SIZE = 3;
    public static final int MAX_MANA       = 10;
    public static final int SWAP_COINS_COST = 2; // cout en coins pour echanger la carte en jeu

    private final String name;
    private final Crystal crystal;

    private final List<Card> hand;
    private final List<Card> bench;
    private Card activeCard;

    //Monnaies
    private int mana;
    private int coins;

    //Effets temporaires sur le joueur
    private boolean cannotDraw;      // ne peut pas piocher au prochain tour (Panzoli passif)
    private int reducedDeployCost;   // reduction du prochain deploiement (Actionnaire)

    public Player(String name){
        this.name = name;
        this.crystal = new Crystal();
        this.hand = new ArrayList<>();
        this.bench = new ArrayList<>();
        this.activeCard = null;
        this.mana = 0;
        this.coins = 0;
        this.cannotDraw = false;
        this.reducedDeployCost = 0;
    }

    //Methodes Main

    public void addtoHand(Card card){
        hand.add(card);
    }

    public boolean removeFromHand(Card card){
        return hand.remove(card);
    }

    //Methodes Banc

    //Transition Main->Banc (coute des pièces)
    public boolean deployToBench(Card card){
        if (bench.size()>=MAX_BENCH_SIZE) return false;

        int cost = Math.max(0,card.getCost()-reducedDeployCost);
        if (coins<cost) return false;

        coins -= cost;
        reducedDeployCost=0;
        hand.remove(card);
        bench.add(card);
        return true;
    }

    public boolean removeFromBench(Card card){
        return bench.remove(card);
    }

    //Methodes de la carte en jeu

    //Transition Banc->En jeu (gratuit si aucune carte active)
    public boolean playFromBenchFree(Card card){
        if (!bench.contains(card)) return false;
        bench.remove(card);
        activeCard = card;
        return true;
    }

    //Echange la carte en jeu avec une carte du banc (coute des coins)
    public boolean swapActiveCard(Card card){
        if (!bench.contains(card)) return false;
        if (coins<SWAP_COINS_COST) return false;

        coins -= SWAP_COINS_COST;
        bench.add(activeCard);
        bench.remove(card);
        activeCard=card;
        return true;
    }

    // Setter activeCard (utilise par GameModel pour remettre a null apres mort)
    public void setActiveCard(Card card) { this.activeCard = card; }

    //Methodes Mana

    public void gainMana(int amount){
        mana = Math.min(MAX_MANA, mana+amount);
    }

    public boolean spendMana(int amount){
        if (mana<amount) return false;
        mana -= amount;
        return true;
    }

    public void halveMana() {
        mana = mana / 2;
    }

    //Methodes Coins

    public void gainCoins(int amount){
        coins += amount;
    }

    public boolean spendCoins(int amount){
        if(coins < amount) return false;
        coins -= amount;
        return true;
    }

    public void loseHalfCoins(){
        coins = coins / 2;
    }

    //Shuffle
    public void clearAll() {
        hand.clear();
        bench.clear();
        activeCard = null;
    }

    //Methodes des effets temporaires
    public void setCannotDraw(boolean value)        { this.cannotDraw = value; }
    public boolean cannotDraw()                     { return cannotDraw; }

    public void applyReduceDeployCost(int reduction){ this.reducedDeployCost = reduction; }

    // --- Getters ---

    public String getName()        { return name; }
    public Crystal getCrystal()    { return crystal; }
    public List<Card> getHand()    { return hand; }
    public List<Card> getBench()   { return bench; }
    public Card getActiveCard()    { return activeCard; }
    public boolean hasActiveCard() { return activeCard != null; }
    public int getMana()           { return mana; }
    public int getCoins()          { return coins; }

    @Override
    public String toString() {
        return String.format("%s | %s | Mana:%d/%d | Pieces:%d | Banc:%d | Main:%d cartes",
            name, crystal, mana, MAX_MANA, coins, bench.size(), hand.size());
    }


}
