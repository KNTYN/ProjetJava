package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

import static fr.github.tcgame.view.MainGame.AUDIOSETTINGS;

public class SettingsMenu extends Menu{
    public SettingsMenu(MenuController controller) {
        super(TypeMenu.SETTINGS, controller);
    }

    @Override
    protected void build() {
        setBlackBackground();

        addSlider(300, 500, 400, AUDIOSETTINGS.getGlobalVolume(),value -> AUDIOSETTINGS.setGlobalVolume(value)); // global
        addSlider(300, 400, 400, AUDIOSETTINGS.getMusicVolume(),value -> AUDIOSETTINGS.setMusicVolume(value)); // music
        addSlider(300, 300, 400, AUDIOSETTINGS.getSfxVolume(),value -> AUDIOSETTINGS.setSfxVolume(value)); // sfx

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }
}
