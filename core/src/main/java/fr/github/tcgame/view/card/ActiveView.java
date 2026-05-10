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

        CARDV.clearActiveCards(idP); // ← idP passé, n'efface que la sienne

        Card activeCard = (idP == 1) ? P1.getActiveCard() : P2.getActiveCard();

        if (activeCard != null) {
            float x = 1089;
            float y = (idP == 1) ? 291 : 591; // P2 au-dessus de P1
            CARDV.displayCard(activeCard, idP, x, y, 150, 210, "active");
        }
    }
}
