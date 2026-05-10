package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;

import fr.github.tcgame.control.MenuController;

public class MainMenu extends Menu {

    public MainMenu(MenuController controller) {
        super(TypeMenu.MAIN, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_MainMenu.png");
        //addMainTitle();

        // remplacer les - 50 par  - 1/2 de width de la texture
        // Il faut que toutes les valeurs dependent de width et height
        addButton("button/button_0007_JOUER.png", "button/button_0006_JOUER2.png", WIDTH/2 - 100, 600, 1.5f, controller::goSelection); // jouer
        addButton("button/button_0005_BIBLIO.png", "button/button_0004_BIBLIO2.png", WIDTH/2 - 215, 500, 1.5f, controller::goCardList); // bibliotheque
        addButton("button/button_0003_PARAMETRES.png", "button/button_0002_PARAMETRES2.png", WIDTH/2 - 215, 400, 1.5f, controller::goSettings); // parametres
        addButton("button/button_0001_QUITTER.png", "button/button_0000_QUITTER2.png", WIDTH/2 - 135, 300, 1.5f, controller::quit); // quitter

        keyIsPressed(Input.Keys.ESCAPE, controller::quit);
    }
}
