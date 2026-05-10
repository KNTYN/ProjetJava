package fr.github.tcgame.view.card;

import fr.github.tcgame.model.card.Card;

import static fr.github.tcgame.model.GameModel.P1;
import static fr.github.tcgame.model.GameModel.P2;
import static fr.github.tcgame.view.MainGame.CARDV;

public class BenchView {

    public BenchView() {}

    public void displayBench(int idP) {
        if (CARDV.getStage() == null) {
            System.out.println("⚠️ Stage pas encore prêt, skip displayBench");
            return;
        }

        CARDV.clearBenchCards(idP); // ← idP passé

        float spacing = 170;
        Card[] bench = (idP == 1) ? P1.getBench() : P2.getBench();

        for (int i = 0; i < bench.length; i++) {
            if (bench[i] != null) {
                float x = 400 + (i * spacing);
                float y = (idP == 1) ? 300 : 600; // bench P2 plus haut
                CARDV.displayCard(bench[i], idP, x, y, 150, 210, "bench");
            }
        }
    }
}
