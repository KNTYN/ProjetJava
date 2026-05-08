package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;

public class ControlesMenu extends Menu {

    public ControlesMenu(MenuController controller) {
        super(TypeMenu.CONTROLES, controller);
    }

    @Override
    protected void build() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTex = new Texture(pixmap);
        pixmap.dispose();
        Image blackBg = new Image(blackTex);
        blackBg.setFillParent(true);
        stage.addActor(blackBg);

        setBackground("placeholder/BG_MainMenu.jpg");

        addLabel("CONTROLES", WIDTH / 2f, HEIGHT * 0.65f, 72, OR_PALE);

        float centreX = WIDTH / 2f;
        float optY    = HEIGHT * 0.45f;
        float gap     = HEIGHT * 0.04f;

        addMenuOption("SAUVEGARDER", centreX, optY,         controller::playMusic);
        addMenuOption("DEFAULT",     centreX, optY - gap,   controller::playMusic);
        addMenuOption("RETOUR",      centreX, optY - gap*2, controller::goParametres);

        keyIsPressed(Input.Keys.ESCAPE, controller::goParametres);
    }
}
