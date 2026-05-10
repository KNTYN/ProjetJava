package fr.github.tcgame.view.card;

import fr.github.tcgame.model.card.Card;

import static fr.github.tcgame.model.GameModel.P1;
import static fr.github.tcgame.model.GameModel.P2;
import static fr.github.tcgame.view.MainGame.CARDV;

public class ActiveView {

    public ActiveView() {}

    public void displayActive(int idP) {
        if (CARDV.getStage() == null) {
            System.out.println("⚠️ Stage pas encore prêt, skip displayActive");
            return;
        }

        CARDV.clearActiveCards(); // ← Clear seulement l'active slot

        Card activeCard = (idP == 1) ? P1.getActiveCard() : P2.getActiveCard();

        if (activeCard != null) {
            float x = 1089; // Position centrale pour l'active
            float y = 291;
            CARDV.displayCard(activeCard, idP, x, y, 150, 210, "active"); // ← flag "active"
        }
    }
}
