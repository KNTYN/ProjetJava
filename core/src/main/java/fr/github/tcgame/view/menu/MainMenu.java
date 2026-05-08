package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;

public class MainMenu extends Menu {

    public MainMenu(MenuController controller) {
        super(TypeMenu.MAIN, controller);
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

        addLabel("TASTY CROUSTY", WIDTH / 2f, HEIGHT * 0.65f, 72, OR_PALE);

        float centreX = WIDTH / 2f;
        float optY    = HEIGHT * 0.45f;
        float gap     = HEIGHT * 0.04f;

        addMenuOption("JOUER",      centreX, optY,           controller::goJouer);
        addMenuOption("COLLECTION", centreX, optY - gap,     controller::goCollection);
        addMenuOption("PARAMETRES", centreX, optY - gap * 2, controller::goParametres);
        addMenuOption("QUITTER",    centreX, optY - gap * 3, controller::quit);

        addLabel("ONLINE",   WIDTH - 80f, 35f, 14, GRIS_MENU);
        addLabel("Ver. 1.0", WIDTH - 80f, 18f, 12, GRIS_MENU);

        keyIsPressed(Input.Keys.ESCAPE, controller::quit);
    }
}
