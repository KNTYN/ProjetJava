package fr.github.tcgame.model;

import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.card.CardFactory;
import fr.github.tcgame.model.deck.Deck;
import fr.github.tcgame.model.player.Player;
import fr.github.tcgame.model.attack.Attack;
import fr.github.tcgame.model.attack.AttackEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameModel {

    //CONSTANTES
    public static final int MANA_GAIN_BASE = 1;
    public static final int MANA_GAIN_INCREMENT = 1;
    public static final int TURNS_BEFORE_INCREMENT = 5;
    public static final int INITIAL_HAND_SIZE = 4;

    public enum Status {EN_COURS, TERMINE }

    //ETAT DE LA PARTIE
    private final Player player1;
    private final Player player2;
    private final Deck deck;
    private final Random random;

    private int turnNumber;
    private Status status;
    private Player winner;

    // CONSTRUCTEUR
    public GameModel(String nameP1, String nameP2){
        this.player1 = new Player(nameP1);
        this.player2 = new Player(nameP2);
        this.deck = new Deck();
        this.random = new Random();
        this.turnNumber = 0;
        this.status = Status.EN_COURS;
        this.winner = null;
    }

    // INITIALISATION
    public void init(){
        //création et mélange de toutes les cartes
        List<Card> allCards = CardFactory.createAllCards();
        Collections.shuffle(allCards);
        for(Card c : allCards) deck.addCard(c);

        //Distribuer la main de depart
        for (int i = 0; i<INITIAL_HAND_SIZE; i++){
            Card c1 = deck.draw();
            Card c2 = deck.draw();
            if (c1 != null) player1.addtoHand(c1);
            if (c1 != null) player2.addtoHand(c1);
        }
    }

    //DEROULEMENT D'UN TOUR
    /**
     * Demarre un nouveau tour :
     * - incremente le compteur
     * - donne du mana aux deux joueurs
     * - chaque joueur pioche 1 carte (sauf si bloque par Panzoli)
     */

    public void startTurn(){
        turnNumber++;
        int manaGain = getCurrentManaGain();

        player1.gainMana(manaGain);
        player2.gainMana(manaGain);

        drawForPlayer(player1);
        drawForPlayer(player2);

        //applique le poison sur les cartes
        //applyPoisonDamage(player1, player2);
        //applyPoisonDamage(player2, player1);
    }

    private void drawForPlayer(Player player){
        if (player.cannotDraw()){
            player.setCannotDraw((false)); //reset pour le tour suivant
            return;
        }
        Card drawn = deck.draw();
        if (drawn != null) player.addtoHand(drawn);
    }

    public int getCurrentManaGain(){
        int increments = (turnNumber - 1) / TURNS_BEFORE_INCREMENT;
        return MANA_GAIN_BASE + increments * MANA_GAIN_INCREMENT;
    }

    // DEPLOIEMENT DES CARTES

    //Main -> Banc : coute des pieces
    public boolean deployToBench(Player player, Card card) {
        return player.deployToBench(card);
    }

    //Banc -> En jeu : gratuit si aucune carte active
    public boolean playFromBenchFree(Player player, Card card) {
        return player.playFromBenchFree(card);
    }

    //A CONTINUER








}
