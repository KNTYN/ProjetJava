package fr.github.tcgame.model;

import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.deck.Deck;
import fr.github.tcgame.model.player.Player;

import java.util.Arrays;

import static fr.github.tcgame.model.player.Player.HANDV1;
import static fr.github.tcgame.model.player.Player.WIN;

public class GameModel {
    // CONSTANTES
    public static final int TURNS_BEFORE_INCREMENT = 5;

    public static int TURN=1;

    public static final int MAX_CARD_HAND = 5;
    public static final int MAX_CARD_BENCH = 3;

    public enum Status { OFF, ON }

    public static int playerTurn = 1; // ← FIX: initialiser à 1 pour que P1 commence

    // ETAT DE LA PARTIE
    public static Player P1;
    public static Player P2;
    private final Deck deck;
    private Status status;
    private Player winner;

    // CONSTRUCTEUR
    public GameModel() {
        P1 = new Player(1);
        P2 = new Player(2);
        this.deck = new Deck();

        TURN = 1; // ← le jeu commence au tour 1
        playerTurn = 1; // ← joueur 1 commence

        this.status = Status.ON;
        this.winner = null;
    }

    // INITIALISATION
    public void init() {
        this.deck.createDeck();
        deck.draws(P1, 1);
        deck.draws(P2, 1);

        System.out.println("P1 HAND : " + Arrays.toString(P1.getHand()));
        System.out.println("P2 HAND : " + Arrays.toString(P2.getHand()));
        HANDV1.displayHand(1);
    }

    public int getIdPlayerTurn(){ return playerTurn; }
    public Player getPlayerTurn(){ return playerTurn==1 ? P1 : P2; }

    public void nextTurn(){
        playerTurn = (playerTurn == 1) ? 2 : 1;

        TURN++;

        System.out.println("🔄 Tour " + TURN + " - C'est au tour du joueur " + playerTurn);


        Player currentPlayer = getPlayerTurn();
        if (playerTurn==1){
            this.deck.draws(currentPlayer, 1);
            currentPlayer.addMana(TURN >= 5 ? 2 : 1);
            currentPlayer.addPiece(1);
            HANDV1.displayHand(playerTurn);
        }

        System.out.println("🃏 Pioche effectuée");
        System.out.println("💧 Mana : " + currentPlayer.mana);
        System.out.println("🪙 Pièces : " + currentPlayer.piece);
    }

    public static void setSelectedCard(Card card, int p) {
        System.out.println("selected card");
        if (p == 1) {
            P1.selectedCard = card;
            System.out.println("P1");
        } else {
            P2.selectedCard = card;
            System.out.println("P2");
        }
    }

    public void checkVictory() {
        if (P1.checkDeath()) {
            WIN = true;
            Player.WINNER_ID = 2;
        }
        if (P2.checkDeath()) {
            WIN = true;
            Player.WINNER_ID = 1;
        }
    }
}
