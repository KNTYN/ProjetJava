package fr.github.tcgame.model;

import fr.github.tcgame.model.attack.Attack;
import fr.github.tcgame.model.attack.AttackEffect;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.player.Crystal;
import fr.github.tcgame.model.player.Player;

import java.util.Random;

public class GameModel {

    public enum Status { EN_COURS, TERMINE }

    private final Player player1;
    private final Player player2;
    private final Random random;
    private Status status;
    private Player winner;

    public GameModel() {
        this.player1 = new Player("Joueur");
        this.player2 = new Player("IA");
        this.random  = new Random();
        this.status  = Status.EN_COURS;
        this.winner  = null;
        init();
    }

    private void init() {
        // 4 cartes disponibles
        Card[] cartes = {
            createStonks(),
            createGanon(),
            createAirFryer(),
            createPanzoli()
        };

        // Joueur prend Stonks, IA prend Ganon
        player1.setActiveCard(cartes[0]);
        player2.setActiveCard(cartes[1]);

        System.out.println("[INIT] Joueur : " + player1.getActiveCard().getName());
        System.out.println("[INIT] IA : " + player2.getActiveCard().getName());
    }

    // --- Création des 4 cartes ---

    private Card createStonks() {
        Attack normal  = new Attack("Spéculation", "Attaque standard.", 3, 0, AttackEffect.NONE, 0);
        Attack special = new Attack("To the Moon", "Dégâts doublés.", 6, 0, AttackEffect.NONE, 0);
        return new Card("Stonks", "Capitaliste rusé.", Card.Type.RARE, Card.Family.CAPITALISTE, 15, 0, normal, special);
    }

    private Card createGanon() {
        Attack normal  = new Attack("Poing de ténèbres", "Attaque standard.", 5, 0, AttackEffect.NONE, 0);
        Attack special = new Attack("Calamité", "Grosse attaque.", 8, 0, AttackEffect.NONE, 0);
        return new Card("Ganon", "Seigneur des ténèbres.", Card.Type.RARE, Card.Family.MAUDIT, 20, 0, normal, special);
    }

    private Card createAirFryer() {
        Attack normal  = new Attack("Rotation agressive", "Attaque standard.", 2, 0, AttackEffect.NONE, 0);
        Attack special = new Attack("Mode frite", "Attaque légère.", 4, 0, AttackEffect.NONE, 0);
        return new Card("Air Fryer", "Électroménager de combat.", Card.Type.COMMUN, Card.Family.GOOFY, 10, 0, normal, special);
    }

    private Card createPanzoli() {
        Attack normal  = new Attack("TP d'archi", "Attaque standard.", 4, 0, AttackEffect.NONE, 0);
        Attack special = new Attack("Chaos Immersif", "Attaque puissante.", 6, 0, AttackEffect.NONE, 0);
        return new Card("Mr Panzoli", "Légendaire et redoutable.", Card.Type.LEGENDAIRE, Card.Family.PRODIGE, 18, 0, normal, special);
    }

    // --- Combat ---

    public String attackPlayer(boolean useSpecial) {
        if (status == Status.TERMINE) return "Partie terminée !";

        Card pCard = player1.getActiveCard();
        Card iCard = player2.getActiveCard();
        if (pCard == null || iCard == null) return "Pas de carte active !";

        Attack atk = useSpecial ? pCard.getSpecialAtk() : pCard.getNormalAtk();
        if (atk == null) return "Pas d'attaque !";

        // Joueur attaque IA
        iCard.takeDamage(atk.getDamage());
        String log = "Joueur : " + atk.getName() + " -> IA perd " + atk.getDamage() + " PV";
        System.out.println("[COMBAT] " + log);

        // Si carte IA morte
        if (iCard.isDead()) {
            player2.getCrystal().takeDamage(iCard.getHp());
            System.out.println("[MORT] Carte IA morte ! Cristal IA -" + iCard.getHp() + " PV");
            player2.setActiveCard(null);
            checkEnd();
            if (status == Status.TERMINE) return log;
            // IA pioche une nouvelle carte
            player2.setActiveCard(createRandomCard());
            System.out.println("[IA] Nouvelle carte : " + player2.getActiveCard().getName());
        }

        if (status != Status.TERMINE) {
            // IA contre-attaque
            String iaLog = attackIA();
            return log + "\n" + iaLog;
        }

        return log;
    }

    private String attackIA() {
        Card iCard = player2.getActiveCard();
        Card pCard = player1.getActiveCard();
        if (iCard == null || pCard == null) return "";

        // IA choisit son attaque au hasard
        boolean useSpecial = random.nextBoolean() && iCard.getSpecialAtk() != null;
        Attack atk = useSpecial ? iCard.getSpecialAtk() : iCard.getNormalAtk();
        if (atk == null) return "";

        pCard.takeDamage(atk.getDamage());
        String log = "IA : " + atk.getName() + " -> Joueur perd " + atk.getDamage() + " PV";
        System.out.println("[COMBAT] " + log);

        // Si carte joueur morte
        if (pCard.isDead()) {
            player1.getCrystal().takeDamage(pCard.getHp());
            System.out.println("[MORT] Carte Joueur morte ! Cristal Joueur -" + pCard.getHp() + " PV");
            player1.setActiveCard(null);
            checkEnd();
            if (status != Status.TERMINE) {
                player1.setActiveCard(createRandomCard());
                System.out.println("[JOUEUR] Nouvelle carte : " + player1.getActiveCard().getName());
            }
        }

        return log;
    }

    private Card createRandomCard() {
        int r = random.nextInt(4);
        return switch (r) {
            case 0 -> createStonks();
            case 1 -> createGanon();
            case 2 -> createAirFryer();
            default -> createPanzoli();
        };
    }

    private void checkEnd() {
        if (player2.getCrystal().isDestroyed()) {
            status = Status.TERMINE;
            winner = player1;
            System.out.println("[FIN] Victoire Joueur !");
        } else if (player1.getCrystal().isDestroyed()) {
            status = Status.TERMINE;
            winner = player2;
            System.out.println("[FIN] Victoire IA !");
        }
    }

    // --- Getters ---

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Status getStatus()  { return status; }
    public Player getWinner()  { return winner; }
    public boolean isOver()    { return status == Status.TERMINE; }
}
