package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;

import fr.github.tcgame.control.MenuController;

public class MenuManager {
    private Menu actualMenu;
    private MainMenu mainMenu;
    private SelectionMenu selectionMenu;
    private MenuController controller;
    private QuitMenu quitMenu;

    public MenuManager() {
        controller = new MenuController(this);
    }

    public void changeMenu(Menu.TypeMenu typeMenu) {
        if (actualMenu != null && actualMenu.typeMenu == typeMenu) return;
        switch (typeMenu) {
            case MAIN -> {
                if (mainMenu == null) { mainMenu = new MainMenu(controller); }
                actualMenu = mainMenu;
            }
            case SELECTION -> {
                if (selectionMenu == null) { selectionMenu = new SelectionMenu(controller); }
                actualMenu = selectionMenu;
            }
            case QUIT -> {
                if (quitMenu == null) { quitMenu = new QuitMenu(controller); }
                actualMenu = quitMenu;
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
    public void dispose(){
        mainMenu.dispose();
        selectionMenu.dispose();
        quitMenu.dispose();
    }
}
