package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import fr.github.tcgame.control.MenuController;

public class SelectionMenu extends Menu {
    public SelectionMenu(MenuController controller) {
        super(TypeMenu.SELECTION, controller);
    }

    @Override
    protected void build() {
        setBackground("test/test.jpg");

        addButton("placeholder/bouton.png", 450, 320, 0.35f, controller::playMusic);
        addLabel("Musique", 530f, 350f, 20, Color.WHITE);

        addButton("placeholder/bouton.png", 450, 185, 0.35f, controller::goMain);
        addLabel("Retour", 530f, 215f, 20, Color.WHITE);

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
        keyIsPressed(Input.Keys.UP, controller::playMusic);
    }
}
