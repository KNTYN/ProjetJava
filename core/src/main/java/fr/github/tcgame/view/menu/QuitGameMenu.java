package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;

public class QuitGameMenu extends Menu {

    public QuitGameMenu(MenuController controller) {
        super(TypeMenu.QUIT_GAME, controller);
    }

    @Override
    protected void build() {
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(0f, 0f, 0f, 0.85f);
        px.fill();
        Image fond = new Image(new Texture(px));
        px.dispose();
        fond.setFillParent(true);
        stage.addActor(fond);

        addLabel("VRAIMENT QUITTER ?", WIDTH / 2f, HEIGHT * 0.65f, 36, OR_PALE);

        float centreX = WIDTH / 2f;
        float optY    = HEIGHT * 0.48f;
        float gap     = HEIGHT * 0.07f;

        addMenuOption("VERS LE MENU PRINCIPAL", centreX, optY, () -> {
            playMenuOst();
            controller.goMain();
        });
        addMenuOption("VERS LE BUREAU", centreX, optY - gap, () -> Gdx.app.exit());
        addMenuOption("ANNULER",        centreX, optY - gap * 2, controller::goPause);

        keyIsPressed(Input.Keys.ESCAPE, controller::goPause);
    }
}
