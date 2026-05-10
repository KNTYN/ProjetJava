package fr.github.tcgame.model.player;

import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.view.card.HandView;

import java.util.List;

import static fr.github.tcgame.model.GameModel.MAX_CARD_BENCH;
import static fr.github.tcgame.model.GameModel.MAX_CARD_HAND;
import static java.lang.Math.max;

public class Player {
    private Crystal crystal;

    public static HandView HANDV1 = new HandView();
    public static HandView HANDV2 = new HandView();

    public Card[] hand = new Card[MAX_CARD_HAND];
    public Card[] bench = new Card[MAX_CARD_BENCH];
    public Card activeCard;

    public Card selectedCard;

    public Player(int idP){
        this.crystal=new Crystal(idP);
    }

    public Card getSelectedCard() { return selectedCard; } // derniere carte selectionnée
    public void resetSelectionCard(){ this.selectedCard=null; } // reset la selection de carte

    public Card getActiveCard() { return activeCard; } // getter activeCard

    public void deployActiveCard(Card c){ // deployer une carte sur le slot actif
        if (this.activeCard==null){
            this.activeCard=c;
            removeCardFromBench(c);
        }
        else { errorEvent(); }
    }

    public void removeActiveCard(){this.activeCard=null; } // remove l'active card

    public boolean hasSlot(Card[] tab){ // s'il y a un slot pour une nouvelle carte dans la hand ou bench
        int currentItems=0;
        for (Card c : tab) { if (c != null) { currentItems++; } }
        return currentItems<tab.length;
    }

    public boolean contains(Card[] tab, Card c){ // boolean true si la hand ou le bench possède la carte passée en param
        boolean check=false;
        for (int i=0;i<tab.length;i++) {
            if(tab[i]==c){ check=true; }
        }
        return check;
    }

    public void removeCard(Card[] tab, Card c){ // supprime une carte du bench ou de la hand
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] == c) { tab[i] = null; }
        }
    }

    public void addCardToHand(Card c){ // ajoute une carte à la main dans le premier slot vide
        if (hasSlot(this.hand)) {
            for (int i=0;i<MAX_CARD_HAND;i++) {
                if (this.hand[i]==null){
                    this.hand[i]=c;
                    break;
                }
            }
        } else { errorEvent(); }
    }

    public void removeCardFromHand(Card c){ // supprime une carte de la main - avec checkout et error exit
        if (contains(this.hand,c)){ removeCard(this.hand,c); }
        else {
            errorEvent();
        }
    }

    public void addCardToBench(Card c){ // ajoute une carte au bench
        if (hasSlot(this.bench)) {
            for (int i = 0; i < MAX_CARD_BENCH; i++) {
                if (this.bench[i] == null) {
                    this.bench[i] = c;
                    break;
                }
            }
        } else { errorEvent(); }
    }

    public void cardHandToBench(Card c){ // deplace une carte de la hand vers le bench - avec checkout et error exit
        if (hasSlot(this.bench)){
            addCardToBench(c);
            removeCardFromHand(c);
        }
        else { errorEvent(); }
    }

    public void removeCardFromBench(Card c){ // supprime une carte du bench
        if (contains(this.bench,c)){ removeCard(this.bench,c); }
        else { errorEvent(); }
    }

    public void errorEvent(){ // error exit - a update
        System.out.println("[Debug] - Vous ne pouvez pas faire ça !");
    }

    public Card[] getHand() { return this.hand; }
    public Card[] getBench() { return this.bench; }

    public int getHp(){ return this.crystal.currentHp; }

    public void takeDamage(int dmg){
        this.crystal.currentHp=max(0,this.crystal.currentHp-dmg);
        this.crystal.updateTexture();

        checkDeath(); // faire un truc avec quand on fera le gestion des morts
    }

    public boolean checkDeath(){
        return this.crystal.currentHp==0;
    }
}
