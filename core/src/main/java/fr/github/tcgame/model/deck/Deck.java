package fr.github.tcgame.model.deck;

import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.player.Player;

import java.util.List;

import static fr.github.tcgame.model.card.CardFactory.createAllCards;
import static fr.github.tcgame.view.MainGame.CARDV;
import static fr.github.tcgame.view.MainGame.GM;

public class Deck {
    private List<Card> deck;

    public void createDeck(){ // créer un deck
        deck=createAllCards();
    }

    public List<Card> getDeck(){ // getter
        return this.deck;
    }

    public void draws(Player p, int nmb) { // pioche nmb carte(s) pour la main du joueur p
        for (int i = 0; i < nmb; i++) {
            Card c = deck.remove(0);
            p.addCardToHand(c);
            deck.add(deck.size()-1,c); // on remet la carte à la fin du deck pour avoir un "deck infini"

            //CARDV.displayCard(c, p == GM.P1 ? 1 : 2, 500, 400, 150, 210);
        }
        // FAUT AFFICHER LA CARTE AVEC CARDVIEW
    }


}
