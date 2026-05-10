package fr.github.tcgame.model;

import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.card.CardFactory;
import fr.github.tcgame.model.deck.Deck;
import fr.github.tcgame.model.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static fr.github.tcgame.model.player.Player.HANDV1;

public class GameModel {
    // CONSTANTES
    public static final int MANA_GAIN_BASE=1;
    public static final int MANA_GAIN_INCREMENT=2;
    public static final int TURNS_BEFORE_INCREMENT=5;
    public static final int INITIAL_HAND_SIZE=4;

    public static final int MAX_CARD_HAND=5;
    public static final int MAX_CARD_BENCH=3;

    public enum Status { OFF, ON }

    // ETAT DE LA PARTIE
    public static Player P1;
    public static Player P2;
    private final Deck deck;
    //private final Random random;

    public static int TURN;
    private Status status;
    private Player winner;


    // CONSTRUCTEUR
    public GameModel() {
        P1 = new Player(1);
        P2 = new Player(2);
        this.deck = new Deck();
        //this.random = new Random();
        this.TURN = 0;
        this.status = Status.ON;
        this.winner = null;
    }

    // INITIALISATION
    public void init() {
        this.deck.createDeck();
        this.deck.draws(P1,3);
        System.out.println("P1 HAND : " + Arrays.toString(P1.getHand()));
        HANDV1.displayHand(1);
        // afficher LA CARTE via DECK

        P1.cardHandToBench(P1.getHand()[2]);
        HANDV1.displayHand(1);
    }


    public static void setSelectedCard(Card card, int p) {
        System.out.println("selected card");
        if (p==1){
            P1.selectedCard=card;
            System.out.println("P1");
        }
        else {
            P2.selectedCard=card;
            System.out.println("P2");
        }
    }

}
