package fr.github.tcgame.view.card;

import fr.github.tcgame.model.card.Card;

import static fr.github.tcgame.model.GameModel.P1;
import static fr.github.tcgame.model.GameModel.P2;
import static fr.github.tcgame.view.MainGame.CARDV;

public class HandView {

    public HandView() {}

    public void displayHand(int idP) {
        if (CARDV.getStage() == null) {
            System.out.println("⚠️ Stage pas encore prêt, skip displayHand");
            return;
        }

        CARDV.clearHandCards();

        float spacing = 170;
        Card[] hand = (idP == 1) ? P1.getHand() : P2.getHand();

        for (int i = 0; i < hand.length; i++) {
            if (hand[i] != null) {
                float x = 500 + (i * spacing);
                CARDV.displayCard(hand[i], idP, x, 75, 150, 210, "hand"); // ← "hand"
            }
        }
    }
}
