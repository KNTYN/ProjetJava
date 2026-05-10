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

        addButton("button/arrow_return.png", "button/arrow_return_gold.png", 50, 890    , 1.5f, controller::goMain);

        addSlider(WIDTH/2 - 200, 590, 400, AUDIOSETTINGS.getGlobalVolume(),value -> AUDIOSETTINGS.setGlobalVolume(value)); // global
        addSlider(WIDTH/2 - 200, 425, 400, AUDIOSETTINGS.getMusicVolume(),value -> AUDIOSETTINGS.setMusicVolume(value)); // music
        addSlider(WIDTH/2 - 200, 270, 400, AUDIOSETTINGS.getSfxVolume(),value -> AUDIOSETTINGS.setSfxVolume(value)); // sfx

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }
}
