package fr.github.tcgame.view.menu;


import fr.github.tcgame.control.MenuController;


public class GameMenu extends Menu{
    public GameMenu(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        setBlackBackground();

    }
}
