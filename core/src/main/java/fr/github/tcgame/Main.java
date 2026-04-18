package fr.github.tcgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.github.tcgame.view.menu.Menu;
import fr.github.tcgame.view.menu.MenuManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private MenuManager menuManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        menuManager = new MenuManager();
        menuManager.changeMenu(Menu.TypeMenu.MAIN);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
        menuManager.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        menuManager.getActualMenu().dispose();
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0,0,width,height);
        menuManager.resize(width, height);
    }
}
