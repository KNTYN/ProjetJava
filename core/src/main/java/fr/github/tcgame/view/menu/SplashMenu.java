package fr.github.tcgame.view.menu;

import fr.github.tcgame.control.MenuController;

public class SplashMenu extends Menu{
    public SplashMenu(MenuController controller) {
        super(TypeMenu.SPLASH, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_splashMenu.png");
        //addMainTitle();

        addSplash(controller::goMain);
    }
}
