package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

public class SelectionMenu extends Menu{
    public SelectionMenu(MenuController controller) {
        super(TypeMenu.SELECTION, controller);
    }

    @Override
    protected void build() {
        setBackground("test/test.jpg");
        addButton("placeholder/bouton.png", 450, 320, 0.35f, controller::playMusic);
        addButton("placeholder/bouton.png", 450, 185, 0.35f, controller::goMain);

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
        keyIsPressed(Input.Keys.UP, controller::playMusic);
    }
}
