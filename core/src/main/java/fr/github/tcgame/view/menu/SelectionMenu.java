package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

public class SelectionMenu extends Menu{
    public SelectionMenu(MenuController controller) {
        super(TypeMenu.SELECTION, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_MainMenu.png");
        // addMainTitle();

        addButton("button/button_0012_SOLO.png","button/button_0009_SOLO2.png", WIDTH/2 - 80, 600, 1.5f, controller::startGame); // solo
        addButton("button/button_0011_LOCAL.png","button/button_0010_LOCAL2.png", WIDTH/2 - 100, 500, 1.5f, controller::goMain); // local
        addButton("button/button_0008_RETOUR.png", "button/button_0007_RETOUR2.png", WIDTH/2 - 120, 400, 1.5f, controller::goMain); // retour

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }
}
