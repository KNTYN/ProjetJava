package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;

import fr.github.tcgame.control.MenuController;

public class MainMenu extends Menu {

    public MainMenu(MenuController controller) {
        super(TypeMenu.MAIN, controller);
    }

    @Override
    protected void build() {
        setBackground("placeholder/BG_MainMenu.jpg");

        addButton("placeholder/bouton.png", 50, 650, 0.2f, controller::goSelection); // jouer
        addButton("placeholder/bouton.png", 50, 400, 0.2f, controller::goCardList); // bibliotheque
        addButton("placeholder/bouton.png", 50, 250, 0.2f, controller::goSettings); // parametres
        addButton("placeholder/bouton.png", 50, 100, 0.2f, controller::quit); // quitter

        keyIsPressed(Input.Keys.ESCAPE, controller::quit);
    }
}
