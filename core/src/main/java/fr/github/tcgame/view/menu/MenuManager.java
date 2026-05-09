package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;

import fr.github.tcgame.control.MenuController;

public class MenuManager {
    private MenuController controller;
    private Menu actualMenu;

    private SplashMenu splashMenu;
    private MainMenu mainMenu;
    private SelectionMenu selectionMenu;
    private CardListMenu cardListMenu;
    private SettingsMenu settingsMenu;
    private QuitMenu quitMenu;


    public MenuManager(){}

    public void render() {
        if (actualMenu != null) { actualMenu.draw(); }
    }

    public void resize(int width, int height) {
        if (actualMenu != null) {
            actualMenu.resize(width, height);
        }
    }
    public void dispose(){
        splashMenu.dispose();
        mainMenu.dispose();
        selectionMenu.dispose();
        cardListMenu.dispose();
        settingsMenu.dispose();
        quitMenu.dispose();
    }

    public void initMenu() {
        this.controller=new MenuController(this);
        splashMenu = new SplashMenu(controller);
        mainMenu = new MainMenu(controller);
        selectionMenu = new SelectionMenu(controller);
        cardListMenu = new CardListMenu(controller);
        settingsMenu = new SettingsMenu(controller);
        quitMenu = new QuitMenu(controller);
    }

    public void changeMenu(Menu.TypeMenu typeMenu) {
        if (actualMenu != null && actualMenu.typeMenu == typeMenu) return;
        System.out.println(typeMenu);
        switch (typeMenu) {
            case MAIN -> actualMenu = mainMenu;

            case SELECTION -> actualMenu = selectionMenu;

            case QUIT -> actualMenu = quitMenu;

            case SPLASH -> actualMenu = splashMenu;

            case SETTINGS -> actualMenu = settingsMenu;

            case CARDLIST -> actualMenu = cardListMenu;
        }
        if (actualMenu != null) {
            Gdx.input.setInputProcessor(actualMenu.getStage());
        }
    }
}
