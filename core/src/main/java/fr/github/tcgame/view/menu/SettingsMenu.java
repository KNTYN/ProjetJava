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
        setBackground("background/BG_settings.png");

        addButton("button/arrow_return.png", "button/arrow_return.png", 50, 650, 1.5f, controller::goMain);

        addSlider(300, 500, 400, AUDIOSETTINGS.getGlobalVolume(),value -> AUDIOSETTINGS.setGlobalVolume(value)); // global
        addSlider(300, 400, 400, AUDIOSETTINGS.getMusicVolume(),value -> AUDIOSETTINGS.setMusicVolume(value)); // music
        addSlider(300, 300, 400, AUDIOSETTINGS.getSfxVolume(),value -> AUDIOSETTINGS.setSfxVolume(value)); // sfx

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }
}
