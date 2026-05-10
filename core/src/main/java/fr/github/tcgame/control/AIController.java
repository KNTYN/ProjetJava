package fr.github.tcgame.control;

import fr.github.tcgame.model.GameModel;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.player.Player;

import static fr.github.tcgame.model.GameModel.*;
import static fr.github.tcgame.model.player.Player.WIN;

public class AIController {

    private final MenuController controller;

    public AIController(MenuController controller) {
        this.controller = controller;
    }

    public void playTurn() {
        if (WIN) return;

        Player ai = P2;

        // 1. Poser autant de cartes que possible sur le bench
        boolean moved = true;
        while (moved) {
            moved = false;
            for (Card c : ai.getHand()) {
                if (c != null && ai.hasSlot(ai.getBench()) && ai.enoughPiece(c.getCost())) {
                    ai.cardHandToBench(c);
                    refreshP2Views();
                    moved = true;
                    break; // On recommence depuis le début après chaque pose
                }
            }
        }

        // 2. Déployer une carte en active si vide
        if (ai.getActiveCard() == null) {
            for (Card c : ai.getBench()) {
                if (c != null) {
                    ai.deployActiveCard(c);
                    refreshP2Views();
                    break;
                }
            }
        }

        // 3. Attaquer si possible (spéciale en priorité, sinon normale, sinon passer)
        if (ai.getActiveCard() != null) {
            if (ai.enoughMana(ai.getActiveCard().getManaCost())) {
                controller.attackSpecial(2);
            } else {
                controller.attack(2);
            }
        } else {
            controller.passed();
        }
    }

    private void refreshP2Views() {
        Player.BENCHV2.displayBench(2);
        Player.ACTIVEV2.displayActive(2);
    }
}
