package fr.github.tcgame.terminal;

import fr.github.tcgame.model.GameModel;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.player.Crystal;
import fr.github.tcgame.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameTerminal {

    private GameModel gameModel;
    private Scanner scanner;

    public static void main(String[] args) {
        new GameTerminal().play();
    }

    public void play() {
        scanner = new Scanner(System.in);
        gameModel = new GameModel("Joueur", "IA");
        gameModel.init();

        log("============================================================");
        log("          🔥 TASTY CROUSTY - VERSION COMPLÈTE 🔥");
        log("   6 slots · Sorts · Stats · Défausse · Limite main 8");
        log("============================================================");
        log("");
        log("RÈGLES :");
        log("  • Une carte posée sur le banc doit attendre 1 tour");
        log("  • +2 mana/tour (+1 supplémentaire tous les 4 tours)");
        log("  • Chaque carte du terrain peut attaquer une fois par tour");
        log("  • Main limitée à 8 cartes, défausse obligatoire si plus");
        log("  • Les sorts se lancent depuis la main");
        log("  • 6 slots de terrain pour des combats épiques !");
        log("");

        while (!gameModel.isOver()) {
            gameModel.startTurn();
            if (gameModel.isOver()) break;

            displayGameState();

            boolean turnEnded = false;
            String lastError = null;
            List<Card> alreadyAttacked = new ArrayList<>();

            while (!turnEnded && !gameModel.isOver()) {
                if (lastError != null) {
                    log("\n  ❌ " + lastError);
                    log("  Appuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                    lastError = null;
                    displayGameState();
                }

                if (gameModel.getPlayer1().getHand().size() > GameModel.MAX_HAND_SIZE) {
                    log("\n  ⚠️  Votre main est pleine (" + gameModel.getPlayer1().getHand().size() + "/" + GameModel.MAX_HAND_SIZE + ") !");
                    log("  Vous devez défausser une carte avant de continuer.");
                    discardPhase(gameModel.getPlayer1());
                    displayGameState();
                }

                System.out.println("\n🎮 --- ACTIONS ---");
                System.out.println("  1. Déployer (main → banc)");
                System.out.println("  2. Activer (banc → terrain) [GRATUIT]");
                System.out.println("  3. Échanger (terrain ↔ banc) [2 mana]");
                System.out.println("  4. ATTAQUER (choisir attaquant + cible)");
                System.out.println("  5. LANCER UN SORT (depuis la main)");
                System.out.println("  6. Défausser une carte");
                System.out.println("  7. Passer le tour");
                System.out.print("  Choix : ");

                int choice = readInt();

                switch (choice) {
                    case 1: lastError = deployCard(gameModel.getPlayer1()); break;
                    case 2: lastError = activateCard(gameModel.getPlayer1()); break;
                    case 3: lastError = swapCard(gameModel.getPlayer1()); break;
                    case 4:
                        AttackResult ar = attackPhase(gameModel.getPlayer1(), alreadyAttacked);
                        if (ar.error != null) lastError = ar.error;
                        else if (ar.attacked) log("\n  💡 Vous pouvez attaquer avec vos autres cartes ou passer le tour (option 7)");
                        break;
                    case 5: lastError = spellPhase(gameModel.getPlayer1()); break;
                    case 6: discardPhase(gameModel.getPlayer1()); displayGameState(); break;
                    case 7: log("\n  ⏩ Vous passez votre tour."); turnEnded = true; break;
                    default: lastError = "Choix invalide (1-7)";
                }

                if (gameModel.isOver()) break;
            }

            if (gameModel.isOver()) break;

            iaTurn();
            gameModel.checkEndCondition();
        }

        displayResult();
        scanner.close();
    }

    private void log(String msg) { System.out.println(msg); }

    private String deployCard(Player player) {
        if (player.getHand().isEmpty()) return "Main vide !";
        if (player.getBench().size() >= Player.MAX_BENCH_SIZE) return "Banc plein (" + Player.MAX_BENCH_SIZE + " max) !";
        System.out.print("  Carte (0-" + (player.getHand().size()-1) + ", -1=annuler) : ");
        int idx = readInt();
        if (idx < 0 || idx >= player.getHand().size()) return idx < 0 ? null : "Numéro invalide";
        Card c = player.getHand().get(idx);
        if (player.getMana() < c.getCost()) return "Pas assez de mana ! Besoin: " + c.getCost() + ", Vous avez: " + player.getMana();
        gameModel.deployToBench(player, c);
        log("  ✅ " + c.getName() + " déployé sur le banc (jouable au prochain tour)");
        displayGameState();
        return null;
    }

    private String activateCard(Player player) {
        if (player.getBench().isEmpty()) return "Banc vide !";
        if (player.getFieldCards().size() >= Player.MAX_FIELD_SIZE) return "Terrain plein (" + Player.MAX_FIELD_SIZE + " max) !";
        boolean hasPlayable = false;
        for (Card c : player.getBench()) if (!player.isNewlyDeployed(c)) { hasPlayable = true; break; }
        if (!hasPlayable) return "Toutes les cartes du banc viennent d'arriver ! Attendez le prochain tour";
        System.out.println("  Banc (★ = jouable) :");
        for (int i = 0; i < player.getBench().size(); i++) {
            Card c = player.getBench().get(i);
            System.out.println("    [" + i + "] " + (player.isNewlyDeployed(c) ? "⏳" : "★") + " " + formatCard(c));
        }
        System.out.print("  Activer (-1=annuler) : ");
        int idx = readInt();
        if (idx < 0 || idx >= player.getBench().size()) return idx < 0 ? null : "Numéro invalide";
        if (player.isNewlyDeployed(player.getBench().get(idx))) return "Cette carte vient d'arriver !";
        Card c = player.getBench().get(idx);
        gameModel.playFromBenchToField(player, c);
        log("  🚀 " + c.getName() + " activé sur le terrain !");
        displayGameState();
        return null;
    }

    private String swapCard(Player player) {
        if (player.getFieldCards().isEmpty()) return "Pas de carte sur le terrain !";
        if (player.getBench().isEmpty()) return "Banc vide !";
        if (player.getMana() < 2) return "Pas assez de mana (2 requis)";
        List<Card> field = player.getFieldCards();
        System.out.println("  Terrain :");
        for (int i = 0; i < field.size(); i++) System.out.println("    [" + i + "] " + formatCard(field.get(i)));
        System.out.print("  Échanger (-1=annuler) : ");
        int fi = readInt();
        if (fi < 0 || fi >= field.size()) return fi < 0 ? null : "Invalide";
        System.out.println("  Banc :");
        for (int i = 0; i < player.getBench().size(); i++) {
            Card c = player.getBench().get(i);
            System.out.println("    [" + i + "] " + (player.isNewlyDeployed(c) ? "⏳" : "★") + " " + formatCard(c));
        }
        System.out.print("  Avec (-1=annuler) : ");
        int bi = readInt();
        if (bi < 0 || bi >= player.getBench().size()) return bi < 0 ? null : "Invalide";
        if (player.isNewlyDeployed(player.getBench().get(bi))) return "Cette carte du banc vient d'arriver !";
        gameModel.swapCard(player, field.get(fi), player.getBench().get(bi));
        log("  🔄 Échange effectué !");
        displayGameState();
        return null;
    }

    private AttackResult attackPhase(Player player, List<Card> alreadyAttacked) {
        if (!player.hasActiveCard()) return new AttackResult("Aucune carte sur le terrain !", false);
        Player opponent = gameModel.getOpponent(player);
        List<Card> myField = player.getFieldCards();
        List<Card> enemyField = opponent.getFieldCards();

        System.out.println("\n  ⚔️  Cartes disponibles :");
        int available = 0;
        for (int i = 0; i < myField.size(); i++) {
            Card c = myField.get(i);
            if (alreadyAttacked.contains(c)) System.out.println("    [" + i + "] ❌ " + formatCard(c) + " (déjà attaqué)");
            else if (player.getMana() < c.getNormalAtk().getCostMana() && player.getMana() < c.getSpecialAtk().getCostMana())
                System.out.println("    [" + i + "] 🔒 " + formatCard(c) + " (pas assez de mana)");
            else { System.out.println("    [" + i + "] ✅ " + formatCard(c)); available++; }
        }
        if (available == 0) return new AttackResult("Aucune carte ne peut attaquer !", false);

        System.out.print("  Attaquant (-1=annuler) : ");
        int atkIdx = readInt();
        if (atkIdx < 0 || atkIdx >= myField.size()) return atkIdx < 0 ? new AttackResult(null, false) : new AttackResult("Invalide", false);
        Card attacker = myField.get(atkIdx);
        if (alreadyAttacked.contains(attacker)) return new AttackResult("Déjà attaqué !", false);

        System.out.println("  [1] Normale (" + attacker.getNormalAtk().getDamage() + " dmg, " + attacker.getNormalAtk().getCostMana() + " mana)");
        System.out.println("  [2] Spéciale (" + attacker.getSpecialAtk().getDamage() + " dmg, " + attacker.getSpecialAtk().getCostMana() + " mana)");
        System.out.print("  Type (-1=annuler) : ");
        int type = readInt();
        if (type != 1 && type != 2) return new AttackResult(null, false);

        Card target = null;
        if (!enemyField.isEmpty()) {
            for (int i = 0; i < enemyField.size(); i++) System.out.println("    [" + i + "] " + formatCard(enemyField.get(i)));
            System.out.println("    [" + enemyField.size() + "] 💎 Cristal");
            System.out.print("  Cible (-1=annuler) : ");
            int tIdx = readInt();
            if (tIdx < 0) return new AttackResult(null, false);
            if (tIdx < enemyField.size()) target = enemyField.get(tIdx);
        }

        String result = gameModel.resolveAttack(player, attacker, target, type == 2);
        alreadyAttacked.add(attacker);
        log("\n  ⚔️  " + result);
        scanner.nextLine();
        displayGameState();
        return new AttackResult(null, true);
    }

    private String spellPhase(Player player) {
        List<Integer> spells = new ArrayList<>();
        for (int i = 0; i < player.getHand().size(); i++) {
            Card c = player.getHand().get(i);
            if (c.getFamily() == Card.Family.SORT || c.getType() == Card.Type.EVENT) spells.add(i);
        }
        if (spells.isEmpty()) return "Aucun sort dans votre main !";
        for (int i = 0; i < spells.size(); i++) {
            Card s = player.getHand().get(spells.get(i));
            System.out.println("  [" + i + "] " + s.getName() + " | " + s.getNormalAtk().getName() + " (" + s.getNormalAtk().getCostMana() + " mana)");
        }
        System.out.print("  Sort (-1=annuler) : ");
        int idx = readInt();
        if (idx < 0 || idx >= spells.size()) return null;
        Card spell = player.getHand().get(spells.get(idx));
        System.out.println("  [1] " + spell.getNormalAtk().getName());
        System.out.println("  [2] " + spell.getSpecialAtk().getName());
        System.out.print("  Version : ");
        int v = readInt();
        if (v != 1 && v != 2) return null;
        String result = gameModel.resolveSpell(player, spell, v == 2, null);
        log("\n  🪄 " + result);
        scanner.nextLine();
        displayGameState();
        return null;
    }

    private void discardPhase(Player player) {
        if (player.getHand().isEmpty()) { log("  Main vide."); return; }
        for (int i = 0; i < player.getHand().size(); i++) System.out.println("  [" + i + "] " + player.getHand().get(i).getName());
        System.out.print("  Défausser (-1=annuler) : ");
        int idx = readInt();
        if (idx >= 0 && idx < player.getHand().size()) { gameModel.discardFromHand(player, idx); log("  ✅ Défaussé."); }
    }

    private void iaTurn() {
        Player ia = gameModel.getPlayer2();
        Player joueur = gameModel.getPlayer1();
        log("\n🤖 === TOUR DE L'IA ===");

        // 1. Défausse si main pleine
        while (ia.getHand().size() > GameModel.MAX_HAND_SIZE) gameModel.discardFromHand(ia, 0);

        // 2. Déployer la carte la plus forte
        Card bestCard = null;
        int bestScore = -1;
        for (Card c : new ArrayList<>(ia.getHand())) {
            if (c.getFamily() != Card.Family.SORT) {
                int score = c.getHp() + c.getNormalAtk().getDamage() * 2;
                if (score > bestScore && ia.getBench().size() < Player.MAX_BENCH_SIZE && ia.getMana() >= c.getCost()) {
                    bestScore = score;
                    bestCard = c;
                }
            }
        }
        if (bestCard != null) {
            log("  📥 IA déploie " + bestCard.getName());
            gameModel.deployToBench(ia, bestCard);
        }

        // 3. Activer les cartes disponibles
        for (Card c : new ArrayList<>(ia.getBench())) {
            if (!ia.isNewlyDeployed(c) && ia.getFieldCards().size() < Player.MAX_FIELD_SIZE) {
                log("  🚀 IA active " + c.getName());
                gameModel.playFromBenchToField(ia, c);
            }
        }

        // 4. Lancer des sorts offensifs si le joueur a des créatures
        if (!joueur.getFieldCards().isEmpty()) {
            for (Card c : new ArrayList<>(ia.getHand())) {
                if ((c.getFamily() == Card.Family.SORT || c.getType() == Card.Type.EVENT)
                    && ia.getMana() >= c.getNormalAtk().getCostMana()
                    && (c.getName().contains("Feu") || c.getName().contains("Freeze"))) {
                    log("  🪄 IA lance " + c.getName());
                    gameModel.resolveSpell(ia, c, false, joueur.getFieldCards().get(0));
                    break;
                }
            }
        }

        // 5. Soin si cristal < 50%
        if (ia.getCrystal().getCurrentHp() < GameModel.CRISTAL_MAX_HP / 2) {
            for (Card c : new ArrayList<>(ia.getHand())) {
                if (c.getName().contains("Soin") && ia.getMana() >= c.getNormalAtk().getCostMana()) {
                    log("  🪄 IA se soigne");
                    gameModel.resolveSpell(ia, c, false, null);
                    break;
                }
            }
        }

        // 6. ATTAQUES INTELLIGENTES : priorité aux créatures du joueur
        List<Card> iaAttacked = new ArrayList<>();
        for (Card attacker : ia.getFieldCards()) {
            if (iaAttacked.contains(attacker)) continue;
            if (ia.getMana() < attacker.getNormalAtk().getCostMana()) continue;

            // Choisir la cible intelligemment
            Card target = null;

            // Priorité 1 : créature joueur avec le moins de PV
            if (!joueur.getFieldCards().isEmpty()) {
                Card weakest = joueur.getFieldCards().get(0);
                for (Card c : joueur.getFieldCards()) {
                    if (c.getCurrentHp() < weakest.getCurrentHp()) weakest = c;
                }

                // Si on peut tuer, on tue !
                if (weakest.getCurrentHp() <= attacker.getNormalAtk().getDamage()) {
                    target = weakest;
                }
                // Sinon on tape la plus faible
                else {
                    target = weakest;
                }
            }
            // Si pas de créature, attaque directe au cristal
            // target reste null = attaque cristal

            boolean useSpecial = ia.getMana() >= attacker.getSpecialAtk().getCostMana()
                && new java.util.Random().nextInt(3) == 0; // 33% de chance d'utiliser spéciale

            String result = gameModel.resolveAttack(ia, attacker, target, useSpecial);
            log("  ⚔️  " + result);
            iaAttacked.add(attacker);

            if (gameModel.isOver()) return;
        }

        log("🤖 ================");
    }

    private String formatCard(Card c) { return c.getName() + " [" + c.getFamily() + "] " + c.getCurrentHp() + "/" + c.getHp(); }
    private String formatList(List<Card> cards) { return cards.isEmpty() ? "Vide" : cards.stream().map(c -> c.getName() + "(" + c.getCurrentHp() + "/" + c.getHp() + ")").reduce((a,b)->a+" "+b).get(); }

    private void displayGameState() {
        Player p1 = gameModel.getPlayer1(), p2 = gameModel.getPlayer2();
        System.out.println("\n======================================================================");
        System.out.println("          ⚡ TOUR " + gameModel.getTurnNumber() + " | Mana +" + gameModel.getCurrentManaGain() + " | Pioche: " + gameModel.getDeck().size());
        System.out.println("======================================================================");
        System.out.println("\n🤖 IA  💎" + p2.getCrystal().getCurrentHp() + "/" + GameModel.CRISTAL_MAX_HP + "  🔵" + p2.getMana() + "/" + Player.MAX_MANA + "  🎴" + p2.getHand().size());
        System.out.println("  🃏 Terrain: " + formatList(p2.getFieldCards()));
        System.out.println("  🪑 Banc:    " + formatList(p2.getBench()));
        System.out.println("\n🧑 VOUS  💎" + p1.getCrystal().getCurrentHp() + "/" + GameModel.CRISTAL_MAX_HP + "  🔵" + p1.getMana() + "/" + Player.MAX_MANA + "  🎴" + p1.getHand().size() + "/" + GameModel.MAX_HAND_SIZE);
        System.out.println("  🃏 Terrain: " + formatList(p1.getFieldCards()));
        System.out.println("  🪑 Banc:    " + formatList(p1.getBench()));
        System.out.print("  🎴 Main:    ");
        for (int i = 0; i < p1.getHand().size(); i++) {
            Card c = p1.getHand().get(i);
            String m = (c.getFamily() == Card.Family.SORT || c.getType() == Card.Type.EVENT) ? "🪄" : "";
            System.out.print("[" + i + "]" + m + c.getName() + "(" + c.getCost() + " mana) ");
        }
        System.out.println("\n======================================================================");
    }

    private void displayResult() {
        System.out.println("\n======================================================================");
        System.out.println(gameModel.getWinner() == gameModel.getPlayer1() ? "🎉 VICTOIRE ! 🎉" : "💀 DÉFAITE ! 💀");
        System.out.println("======================================================================");
        System.out.println("\n📊 STATS : Cartes tuées: " + gameModel.getPlayer1CardsKilled() + " | Dégâts: " + gameModel.getPlayer1DamageDealt() + " | Tours: " + gameModel.getTurnNumber());
    }

    private int readInt() { try { return Integer.parseInt(scanner.nextLine()); } catch (Exception e) { return -1; } }

    private static class AttackResult {
        String error; boolean attacked;
        AttackResult(String e, boolean a) { error = e; attacked = a; }
    }
}
