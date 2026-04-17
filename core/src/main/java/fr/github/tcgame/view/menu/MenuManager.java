package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;

public class MenuManager {
    private Menu actualMenu;
    private MainMenu mainMenu;
    //private Une instance de chaque menu

    public void changeMenu(Menu.TypeMenu typeMenu) {
        if (actualMenu != null && typeMenu == actualMenu.typeMenu) {
            return; // On est déjà sur ce menu, on ne fait rien
        }
        switch (typeMenu) {
            case MAIN -> {
                if (mainMenu == null) {
                    mainMenu = new MainMenu();
                }
                actualMenu = mainMenu;
            }/*
        case SELECTION -> {

        }
        */
        }
        if (actualMenu != null) {
            Gdx.input.setInputProcessor(actualMenu.getStage());
        }
    }


    public void render(){
        if (actualMenu!=null){ actualMenu.draw(); }
    }
    public Menu getActualMenu() {
        return actualMenu;
    }

    public void resize(int width, int height) {
        if (actualMenu != null) {
            actualMenu.resize(width, height);
        }
    }
}
