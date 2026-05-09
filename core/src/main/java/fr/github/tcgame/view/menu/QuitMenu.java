package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

public class QuitMenu extends Menu{
    public QuitMenu( MenuController controller) {
        super(TypeMenu.QUIT, controller);
    }
    @Override
    protected void build() {
        setBackground("background/BG_MainMenu.png");
        addMainTitle();

        addButton("button/button_0001_QUITTER.png", "button/button_0000_QUITTER2.png", 150, 50, 1.5f, () -> {Gdx.app.exit();}); // quitter def
        addButton("button/button_0008_RETOUR.png", "button/button_0007_RETOUR2.png", 650, 50, 1.5f, controller::goMain); // non - retour vers Main menu

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }

}
