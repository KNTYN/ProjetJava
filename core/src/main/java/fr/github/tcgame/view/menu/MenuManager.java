package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.view.game.GameView;

public class MenuManager {
    private Menu actualMenu;
    private SplashMenu splashMenu;
    private MainMenu mainMenu;
    private SelectionMenu selectionMenu;
    private JouerMenu jouerMenu;
    private ParametresMenu parametresMenu;
    private AudioMenu audioMenu;
    private ControlesMenu controlesMenu;
    private QuitMenu quitMenu;
    private CollectionMenu collectionMenu;
    private MenuController controller;
    private GameView gameView;

    public MenuManager() {
        controller = new MenuController(this);
        changeMenu(Menu.TypeMenu.SPLASH);
    }

    public void changeMenu(Menu.TypeMenu typeMenu) {
        if (actualMenu != null && actualMenu.typeMenu == typeMenu) return;
        switch (typeMenu) {
            case SPLASH -> {
                if (splashMenu == null) { splashMenu = new SplashMenu(controller); }
                actualMenu = splashMenu;
            }
            case MAIN -> {
                if (mainMenu == null) { mainMenu = new MainMenu(controller); }
                actualMenu = mainMenu;
            }
            case SELECTION -> {
                if (selectionMenu == null) { selectionMenu = new SelectionMenu(controller); }
                actualMenu = selectionMenu;
            }
            case JOUER -> {
                if (jouerMenu == null) { jouerMenu = new JouerMenu(controller); }
                actualMenu = jouerMenu;
            }
            case PARAMETRES -> {
                if (parametresMenu == null) { parametresMenu = new ParametresMenu(controller); }
                actualMenu = parametresMenu;
            }
            case AUDIO -> {
                if (audioMenu == null) { audioMenu = new AudioMenu(controller); }
                actualMenu = audioMenu;
            }
            case CONTROLES -> {
                if (controlesMenu == null) { controlesMenu = new ControlesMenu(controller); }
                actualMenu = controlesMenu;
            }
            case QUIT -> {
                if (quitMenu == null) { quitMenu = new QuitMenu(controller); }
                actualMenu = quitMenu;
            }
            case COLLECTION -> {
                if (collectionMenu == null) { collectionMenu = new CollectionMenu(controller); }
                actualMenu = collectionMenu;
            }
            case GAME -> {
                if (gameView == null) { gameView = new GameView(controller); }
                actualMenu = gameView;
            }
        }
        if (actualMenu != null) {
            Gdx.input.setInputProcessor(actualMenu.getStage());
        }
    }

    public void render() {
        if (actualMenu != null) { actualMenu.draw(); }
    }

    public void resize(int width, int height) {
        if (actualMenu != null) {
            actualMenu.resize(width, height);
        }
    }

    public void dispose() {
        if (splashMenu != null) splashMenu.dispose();
        if (mainMenu != null) mainMenu.dispose();
        if (selectionMenu != null) selectionMenu.dispose();
        if (jouerMenu != null) jouerMenu.dispose();
        if (parametresMenu != null) parametresMenu.dispose();
        if (audioMenu != null) audioMenu.dispose();
        if (controlesMenu != null) controlesMenu.dispose();
        if (quitMenu != null) quitMenu.dispose();
        if (collectionMenu != null) collectionMenu.dispose();
    }
}
