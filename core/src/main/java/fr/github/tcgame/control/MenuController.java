package fr.github.tcgame.control;

import fr.github.tcgame.view.menu.Menu;
import fr.github.tcgame.view.menu.MenuManager;

public class MenuController {

    private MenuManager menuManager;
    public MenuController(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void goMain() {
        menuManager.changeMenu(Menu.TypeMenu.MAIN);
    }
    public void goCardList() {
        menuManager.changeMenu(Menu.TypeMenu.CARDLIST);
    }
    public void goSelection() {
        menuManager.changeMenu(Menu.TypeMenu.SELECTION);
    }
    public void goSettings() {
        menuManager.changeMenu(Menu.TypeMenu.SETTINGS);
    }

    public void quit() {
        menuManager.changeMenu(Menu.TypeMenu.QUIT);
    }
    public void startGame() { System.out.println("START GAME"); }
    // public void playMusic(){ System.out.println("MESSAGE TEST"); }



}
