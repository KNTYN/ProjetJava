package fr.github.tcgame.model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardFactory {

    // Juste pour créer les cartes directement, plus tard faire un système avec yml ou csv pour load de manière plus opti

    public static List<Card> createAllCards() {
        List<Card> cards = new ArrayList<>();
        for (int i=0; i<40;i++) {
            Card c = new Card("Verstappen", Card.Family.PRODIGE, 12, 3, 3, 8, 3, "cards/prodiges/verstappen.png");
            cards.add(c);
        }
        Collections.shuffle(cards);
        return cards;
    }
}
