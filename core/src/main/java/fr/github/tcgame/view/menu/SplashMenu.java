package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import fr.github.tcgame.control.MenuController;

public class SplashMenu extends Menu {

    public SplashMenu(MenuController controller) {
        super(TypeMenu.SPLASH, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_splashMenu.png");
        addMainTitle();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(2f);
        Label pressKey = new Label("Appuyez sur une touche pour continuer", style);
        pressKey.setPosition(WIDTH/2-250, 80);
        pressKey.getColor().a = 0f;
        stage.addActor(pressKey);

        mainTitle.getColor().a = 0f;
        mainTitle.addAction(Actions.sequence(
            Actions.fadeIn(2.6f),
            Actions.delay(1f),
            Actions.targeting(pressKey, Actions.fadeIn(0.5f)),
            Actions.run(() -> addSplash(controller::goMain))
        ));
    }
}
