package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

public class CardListMenu extends Menu {
    public CardListMenu(MenuController controller) {
        super(TypeMenu.CARDLIST, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_biblio.png");
        addButton("button/arrow_return.png", "button/arrow_return_gold.png", 50, 890, 1.5f, controller::goMain);

        // barre de recherche
        // + les 4 boutons


        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }
}
