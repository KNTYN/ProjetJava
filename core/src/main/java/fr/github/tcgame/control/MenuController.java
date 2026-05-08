package fr.github.tcgame.control;

import fr.github.tcgame.view.menu.Menu;
import fr.github.tcgame.view.menu.MenuManager;

public class MenuController {
    private MenuManager menuManager;

    public MenuController(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void goSplash()           { menuManager.changeMenu(Menu.TypeMenu.SPLASH); }
    public void goMain()             { menuManager.changeMenu(Menu.TypeMenu.MAIN); }
    public void goSelection()        { menuManager.changeMenu(Menu.TypeMenu.SELECTION); }
    public void goJouer()            { menuManager.changeMenu(Menu.TypeMenu.JOUER); }
    public void goSolo()             { menuManager.changeMenu(Menu.TypeMenu.GAME); }
    public void goDuel()             { System.out.println("Duel !"); }
    public void goParametres()       { menuManager.changeMenu(Menu.TypeMenu.PARAMETRES); }
    public void goAudio()            { menuManager.changeMenu(Menu.TypeMenu.AUDIO); }
    public void goControles()        { menuManager.changeMenu(Menu.TypeMenu.CONTROLES); }
    public void goCollection()       { menuManager.changeMenu(Menu.TypeMenu.COLLECTION); }
    public void goGame()             { menuManager.changeMenu(Menu.TypeMenu.GAME); }
    public void goPause()            { menuManager.changeMenu(Menu.TypeMenu.PAUSE); }
    public void goQuitGame()         { menuManager.changeMenu(Menu.TypeMenu.QUIT_GAME); }
    public void goParametresPause()  { menuManager.changeMenu(Menu.TypeMenu.PARAMETRES_PAUSE); }
    public void goAudioPause()       { menuManager.changeMenu(Menu.TypeMenu.AUDIO_PAUSE); }
    public void goControlesPause()   { menuManager.changeMenu(Menu.TypeMenu.CONTROLES_PAUSE); }
    public void quit()               { menuManager.changeMenu(Menu.TypeMenu.QUIT); }
    public void playMusic()          { System.out.println("MESSAGE TEST"); }
}
