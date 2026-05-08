package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.github.tcgame.control.MenuController;

public class QuitMenu extends Menu {

    public QuitMenu(MenuController controller) {
        super(TypeMenu.QUIT, controller);
    }

    @Override
    protected void build() {
        setBackground("placeholder/BG_QuitMenu.jpg");

        addMenuOption("QUITTER", 370f, 298f, () -> { Gdx.app.exit(); });
        addMenuOption("RETOUR",  370f, 440f, controller::goMain);

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }
}
