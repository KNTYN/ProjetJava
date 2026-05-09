package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

public class CardListMenu extends Menu {
    public CardListMenu(MenuController controller) {
        super(TypeMenu.CARDLIST, controller);
    }

    @Override
    protected void build() {
        setBackground("test/test.jpg");

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }
}
