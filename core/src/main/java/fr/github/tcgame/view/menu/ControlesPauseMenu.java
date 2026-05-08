package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;

public class ControlesPauseMenu extends Menu {

    public ControlesPauseMenu(MenuController controller) {
        super(TypeMenu.CONTROLES_PAUSE, controller);
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

        addLabel("CONTROLES", WIDTH / 2f, HEIGHT * 0.72f, 56, OR_PALE);

        float centreX = WIDTH / 2f;
        float optY    = HEIGHT * 0.55f;
        float gap     = HEIGHT * 0.07f;

        addMenuOption("SAUVEGARDER", centreX, optY,         controller::playMusic);
        addMenuOption("DEFAULT",     centreX, optY - gap,   controller::playMusic);
        addMenuOption("RETOUR",      centreX, optY - gap*2, controller::goParametresPause);

        keyIsPressed(Input.Keys.ESCAPE, controller::goParametresPause);
    }
}
