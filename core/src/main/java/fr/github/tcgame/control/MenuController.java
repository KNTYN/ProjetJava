package fr.github.tcgame.control;

import fr.github.tcgame.model.GameModel;
import fr.github.tcgame.model.audio.AudioSettings;
import fr.github.tcgame.model.player.Player;
import fr.github.tcgame.view.card.HandView;
import fr.github.tcgame.view.menu.Menu;
import fr.github.tcgame.view.menu.MenuManager;

import static fr.github.tcgame.model.GameModel.*;
import static fr.github.tcgame.model.player.Player.HANDV1;
import static fr.github.tcgame.view.MainGame.*;

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
    public void goWin() { menuManager.changeMenu(Menu.TypeMenu.WIN); }

    public void quit() {
        menuManager.changeMenu(Menu.TypeMenu.QUIT);
    }
    public void startGame() {
        System.out.println("START GAME");
        menuManager.changeMenu(Menu.TypeMenu.GAME);
        AUDIOSETTINGS.playMusic(AudioSettings.TypeMusic.GAME);
        GM.init();
    }

    public void moveCardToBench(int idP) {
        Player p = (idP == 1) ? GM.P1 : P2;
        if (p.getSelectedCard() != null) {
            System.out.println("🔄 Déplacement de " + p.getSelectedCard() + " vers le bench");
            p.cardHandToBench(p.getSelectedCard());

            // Rafraîchir l'affichage
            if (idP == 1) {
                Player.HANDV1.displayHand(1);
                Player.BENCHV1.displayBench(1);
            } else {
                Player.HANDV2.displayHand(2);
                Player.BENCHV2.displayBench(2);
            }

            p.resetSelectionCard();
        } else {
            System.out.println("⚠️ Aucune carte sélectionnée");
        }
    }

    public void deployToActive(int idP) {
        Player p = (idP == 1) ? GM.P1 : P2;

        if (p.getSelectedCard() != null) {
            System.out.println("⚡ Déploiement de " + p.getSelectedCard() + " en active");
            p.deployActiveCard(p.getSelectedCard());

            // Rafraîchir l'affichage
            if (idP == 1) {
                Player.BENCHV1.displayBench(1);
                Player.ACTIVEV1.displayActive(1);
            } else {
                Player.BENCHV2.displayBench(2);
                Player.ACTIVEV2.displayActive(2);
            }

            p.resetSelectionCard();
        } else {
            System.out.println("⚠️ Aucune carte sélectionnée");
        }
    }


    public void attack(int idP){ // c'est celui qui attaque et non celui qui subit l'attaque
        Player attacker = idP==1 ? P1 : P2;
        Player victim = idP==1 ? P2 : P1;
        if (attacker.activeCard!=null){
            victim.takeDamage(attacker.activeCard.getNormalAtk());
            System.out.println(victim.getHp());
            GM.checkVictory();
        } else { attacker.errorEvent(); }

    }


    public void attackSpecial(int idP){ // c'est celui qui attaque et non celui qui subit l'attaque
        Player attacker = idP==1 ? P1 : P2;
        Player victim = idP==1 ? P2 : P1;
        if (attacker.activeCard!=null){
            if (attacker.enoughMana(attacker.getActiveCard().getManaCost())){
                attacker.costMana(attacker.getActiveCard().getManaCost());
                victim.takeDamage(attacker.getActiveCard().getSpecialAtk());
                System.out.println(victim.getHp());
                GM.checkVictory();
            }
        } else { attacker.errorEvent(); }
    }

    public void passed(){
        GM.nextTurn();
    }

    public void debug(){
        GM.nextTurn();
    }

}
