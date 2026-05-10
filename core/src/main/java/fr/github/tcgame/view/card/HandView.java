package fr.github.tcgame.view.card;

import fr.github.tcgame.model.card.Card;

import static fr.github.tcgame.model.GameModel.P1;
import static fr.github.tcgame.view.MainGame.CARDV;

public class HandView {
    private static HandView instance;

    public HandView() {}

    public static HandView getInstance() {
        if (instance == null) {
            instance = new HandView();
        }
        return instance;
    }

    public void displayHand(int idP) {
        float spacing = 170; // Espacement entre les cartes
        Card[] hand = P1.getHand();
        if (idP==1) {
            for (int i = 0; i < hand.length; i++) {
                if (hand[i] != null) {
                    float x = 300 + (i * spacing);
                    CARDV.displayCard(hand[i], idP, x, 100, 150, 210);
                }
            }
        }
    }

}
