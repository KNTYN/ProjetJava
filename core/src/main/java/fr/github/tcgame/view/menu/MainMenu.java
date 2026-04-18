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

        addButton("placeholder/bouton.png", 50, 500, 0.35f, controller::goSelection);
        addButton("placeholder/bouton.png", 50, 300, 0.35f, controller::playMusic);
        addButton("placeholder/bouton.png", 50, 100, 0.35f, controller::quit);
        /*
        addSearchBar(30,600,300,400, 60, text -> {
            System.out.println(text);
        });
        */
        keyIsPressed(Input.Keys.ESCAPE, controller::quit);
    }
}
