package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.model.audio.AudioSettings;

import static fr.github.tcgame.model.player.Player.WIN;
import static fr.github.tcgame.view.MainGame.AUDIOSETTINGS;

public class WinMenu extends Menu{
    public WinMenu( MenuController controller) {
        super(TypeMenu.WIN, controller);
    }
    @Override
    protected void build() {
        setBackground("background/BG_win.jpg");
        addMainTitle();

        keyIsPressed(Input.Keys.ESCAPE, () ->{
            AUDIOSETTINGS.playMusic(AudioSettings.TypeMusic.MENU);
            WIN=false;
            // il faut reset le jeu
            Gdx.app.exit();
        });
    }
}
