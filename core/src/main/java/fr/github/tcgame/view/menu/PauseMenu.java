package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;

public class PauseMenu extends Menu {

    public PauseMenu(MenuController controller) {
        super(TypeMenu.PAUSE, controller);
    }

    @Override
    protected void build() {
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(0f, 0f, 0f, 0.75f);
        px.fill();
        Image fond = new Image(new Texture(px));
        px.dispose();
        fond.setFillParent(true);
        stage.addActor(fond);

        addLabel("PAUSE", WIDTH / 2f, HEIGHT * 0.72f, 56, OR_PALE);

        float centreX = WIDTH / 2f;
        float optY    = HEIGHT * 0.55f;
        float gap     = HEIGHT * 0.07f;

        addMenuOption("REPRENDRE",  centreX, optY,           controller::goGame);
        addMenuOption("PARAMETRES", centreX, optY - gap,     controller::goParametresPause);
        addMenuOption("QUITTER",    centreX, optY - gap * 2, controller::goQuitGame);

        keyIsPressed(Input.Keys.ESCAPE, controller::goGame);
    }
}
