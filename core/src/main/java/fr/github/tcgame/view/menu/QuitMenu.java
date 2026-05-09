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
        setBackground("placeholder/BG_QuitMenu.jpg");

        addButton("placeholder/bouton.png", 150, 50, 0.35f, () -> {Gdx.app.exit();}); // oui - quitter
        addButton("placeholder/bouton.png", 650, 50, 0.35f, controller::goMain); // non - retour vers Main menu

        keyIsPressed(Input.Keys.ESCAPE, () -> {Gdx.app.exit();});
    }

}
