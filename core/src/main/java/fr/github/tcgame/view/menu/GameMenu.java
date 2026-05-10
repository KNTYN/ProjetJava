package fr.github.tcgame.view.menu;


import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

import static fr.github.tcgame.view.MainGame.CARDV;

public class GameMenu extends Menu{
    public GameMenu(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_game.png");
        // temporaire :

        addButton("background/bench.png", "background/bench.png", 300, 300, 1f,() -> {
            controller.moveCardToBench(1);
        });

        addButton("background/card_back.png", "background/card_back.png", 900, 300, 1f,() -> {
            controller.deployToActive(1);

        });

        CARDV.setStage(stage);

        keyIsPressed(Input.Keys.ESCAPE, () -> {
            System.out.println("Debug Touch");
        });
    }
}
