package fr.github.tcgame.control;

import fr.github.tcgame.model.audio.AudioSettings;
import fr.github.tcgame.model.player.Player;
import fr.github.tcgame.view.menu.Menu;
import fr.github.tcgame.view.menu.MenuManager;

import static fr.github.tcgame.model.GameModel.*;
import static fr.github.tcgame.model.player.Player.WIN;
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


    public void attack(int idP) {
        Player attacker = idP == 1 ? P1 : P2;
        Player victim   = idP == 1 ? P2 : P1;

        if (attacker.activeCard == null) { attacker.errorEvent(); return; }

        int dmg = attacker.activeCard.getNormalAtk();

        // Si la victime a une carte active, elle absorbe les dégâts en premier
        if (victim.activeCard != null) {
            int overflow = victim.activeCard.getDamage(dmg);
            if (overflow > 0) {
                victim.takeDamage(overflow); // surplus au cristal
                // La carte est morte, on la retire
                victim.removeActiveCard();
                refreshViews(idP == 1 ? 2 : 1);
            }
        } else {
            // Pas de carte active : dégâts directs au cristal
            victim.takeDamage(dmg);
        }

        System.out.println("💥 Cristal P" + (idP == 1 ? 2 : 1) + " : " + victim.getHp() + " HP");
        GM.checkVictory();
        if (!WIN) GM.nextTurn();
    }

    public void attackSpecial(int idP) {
        Player attacker = idP == 1 ? P1 : P2;
        Player victim   = idP == 1 ? P2 : P1;

        if (attacker.activeCard == null) { attacker.errorEvent(); return; }
        if (!attacker.enoughMana(attacker.getActiveCard().getManaCost())) { attacker.errorEvent(); return; }

        attacker.costMana(attacker.getActiveCard().getManaCost());
        int dmg = attacker.getActiveCard().getSpecialAtk();

        if (victim.activeCard != null) {
            int overflow = victim.activeCard.getDamage(dmg);
            if (overflow > 0) {
                victim.takeDamage(overflow);
                victim.removeActiveCard();
                refreshViews(idP == 1 ? 2 : 1);
            }
        } else {
            victim.takeDamage(dmg);
        }

        System.out.println("✨ Spéciale ! Cristal P" + (idP == 1 ? 2 : 1) + " : " + victim.getHp() + " HP");
        GM.checkVictory();
        if (!WIN) GM.nextTurn();
    }

    // Helper : rafraîchit bench + active du joueur dont la carte vient de mourir
    private void refreshViews(int idP) {
        if (idP == 1) {
            Player.BENCHV1.displayBench(1);
            Player.ACTIVEV1.displayActive(1);
        } else {
            Player.BENCHV2.displayBench(2);
            Player.ACTIVEV2.displayActive(2);
        }
    }

    public void passed(){
        GM.nextTurn();
    }

    public void debug(){
        System.out.println("debug");
        passed();
    }

}
