package fr.github.tcgame.view;

import com.badlogic.gdx.ApplicationAdapter;

import fr.github.tcgame.view.menu.Menu;
import fr.github.tcgame.view.menu.MenuManager;

public class MainGame

    extends ApplicationAdapter {
    private MenuManager menuManager;
    @Override
    public void create() {
        menuManager = new MenuManager();
        menuManager.changeMenu(Menu.TypeMenu.MAIN);
    }

    @Override
    public void render() {
        menuManager.render();
    }

    @Override
    public void dispose() {
        menuManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        menuManager.resize(width, height);
    }

}
