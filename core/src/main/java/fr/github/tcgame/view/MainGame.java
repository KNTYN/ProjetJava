package fr.github.tcgame.view;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Game;
import fr.github.tcgame.model.GameModel;
import fr.github.tcgame.model.audio.AudioSettings;
import fr.github.tcgame.view.card.CardView;
import fr.github.tcgame.view.menu.Menu;
import fr.github.tcgame.view.menu.MenuManager;
import fr.github.tcgame.view.player.CrystalView;

public class MainGame

    extends ApplicationAdapter {
    private MenuManager menuManager = new MenuManager();
    public static AudioSettings AUDIOSETTINGS;
    public static GameModel GM = new GameModel();
    public static CrystalView CRYSTALV = new CrystalView();
    public static CardView CARDV = new CardView();

    @Override
    public void create() {
        AUDIOSETTINGS=new AudioSettings();
        menuManager.initMenu();
        menuManager.changeMenu(Menu.TypeMenu.SPLASH);
        AUDIOSETTINGS.initMusic();
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
