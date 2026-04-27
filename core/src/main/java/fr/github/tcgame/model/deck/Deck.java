package fr.github.tcgame.model.deck;

import fr.github.tcgame.model.card.Card;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Pioche commune aux deux joueurs.
 * Fonctionne comme une file FIFO :
 *   - on pioche par le debut
 *   - les cartes mortes retournent en fin de file
 */

public class Deck {

    public static final int TOTAL_CARDS = 60;

    private final Deque<Card> cards;

    public Deck(){
        this.cards=new ArrayDeque<>();
    }

    //Ajoute carte en fin de pioche (init)
    public void addCard(Card card){
        cards.addLast(card);
    }

    //Pioche la premiere carte
    public Card draw(){
        return cards.pollFirst();
    }

    public void putBack(Card card) {
        cards.addLast(card);
    }

    public void shuffle(){
        List<Card> list = new ArrayList<>(cards);
        Collections.shuffle(list);
        cards.clear();
        cards.addAll(list);
    }

    public boolean isEmpty() { return cards.isEmpty(); }
    public int size()        { return cards.size(); }

    @Override
    public String toString() {
        return String.format("Pioche [%d cartes restantes]", cards.size());
    }
}
