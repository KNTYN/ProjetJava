package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;

public class SplashMenu extends Menu {

    private float timer = 0f;
    private boolean blink = true;
    private float blinkTimer = 0f;

    public SplashMenu(MenuController controller) {
        super(TypeMenu.SPLASH, controller);
    }

    @Override
    protected void build() {
        // Fond noir
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Image blackBg = new Image(new Texture(pixmap));
        pixmap.dispose();
        blackBg.setFillParent(true);
        stage.addActor(blackBg);

        // Image de fond
        setBackground("placeholder/BG_MainMenu.jpg");

        // Titre
        addLabel("TASTY CROUSTY", WIDTH / 2f, HEIGHT * 0.65f, 72, OR_PALE);

        // Texte clignotant
        addMenuOption("APPUYEZ SUR UNE TOUCHE", WIDTH / 2f, HEIGHT * 0.35f, controller::goMain);

        // N'importe quelle touche
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (clickSound != null) clickSound.play(volumeMaster * volumeEffets);
                controller.goMain();
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (clickSound != null) clickSound.play(volumeMaster * volumeEffets);
                controller.goMain();
                return true;
            }
        });
    }
}
