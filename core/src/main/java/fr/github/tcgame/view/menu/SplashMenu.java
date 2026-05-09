package fr.github.tcgame.view.menu;

import fr.github.tcgame.control.MenuController;

public class SplashMenu extends Menu{
    public SplashMenu(MenuController controller) {
        super(TypeMenu.SPLASH, controller);
    }

    @Override
    protected void build() {
        setBlackBackground();
        addSplashText("coubeh", 400, 300, 2.0f, controller::goMain);
    }
}
