package fr.github.tcgame.model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardFactory {

    // Juste pour créer les cartes directement, plus tard faire un système avec yml ou csv pour load de manière plus opti

    public static List<Card> createAllCards() {
        List<Card> cards = new ArrayList<>();
        //Prodiges
        Card c = new Card("Verstappen", Card.Family.PRODIGE, 10, 3, 3, 6, 2, "cards/prodiges/verstappen.png");
        cards.add(c);
        cards.add(new Card("Steve", Card.Family.PRODIGE, 9, 4, 4, 7, 3, "cards/prodiges/steve.png"));
        cards.add(new Card("Panzoli", Card.Family.PRODIGE, 15, 5, 8, 11, 2, "cards/prodiges/panzoli.png"));
        cards.add(new Card("Link", Card.Family.PRODIGE, 8, 2, 2, 4, 2, "cards/prodiges/link.png"));
        cards.add(new Card("Leon", Card.Family.PRODIGE, 11, 3, 2, 5, 2, "cards/prodiges/leon.png"));
        //Maudits
        cards.add(new Card("Bourreau", Card.Family.MAUDIT, 8, 3, 3, 5, 2, "cards/maudits/bourreau.png"));
        cards.add(new Card("Corbeau", Card.Family.MAUDIT, 6, 2, 2, 4, 1, "cards/maudits/corbeau.png"));
        cards.add(new Card("Ganon", Card.Family.MAUDIT, 10, 4, 4, 7, 3, "cards/maudits/ganon.png"));
        cards.add(new Card("Leclerc", Card.Family.MAUDIT, 11, 3, 3, 6, 3, "cards/maudits/leclerc.png"));
        //Goofys
        cards.add(new Card("AirFryer", Card.Family.GOOFY, 5, 2, 1, 3, 1, "cards/goofys/airfryer.png"));
        cards.add(new Card("DemandeInstagram", Card.Family.GOOFY, 4, 1, 1, 4, 2, "cards/goofys/demande_instagram.png"));
        cards.add(new Card("Facture", Card.Family.GOOFY, 4, 1, 2, 3, 1, "cards/goofys/facture.png"));
        cards.add(new Card("JJ35", Card.Family.GOOFY, 6, 2, 2, 3, 1, "cards/goofys/jj35.png"));
        cards.add(new Card("John", Card.Family.GOOFY, 5, 1, 1, 2, 1, "cards/goofys/john.png"));
        cards.add(new Card("Multipla", Card.Family.GOOFY, 5, 1, 1, 3, 1, "cards/goofys/multipla.png"));
        cards.add(new Card("Multiprise", Card.Family.GOOFY, 6, 2, 3, 5, 3, "cards/goofys/multiprise.png"));
        //Capitalistes
        cards.add(new Card("Actionnaire", Card.Family.GOOFY, 7, 2, 2, 6, 3, "cards/capitalistes/actionnaire.png"));
        cards.add(new Card("Assureur", Card.Family.GOOFY, 6, 2, 3, 5, 3, "cards/capitalistes/assureur.png"));
        cards.add(new Card("Banquier", Card.Family.GOOFY, 5, 1, 2, 3, 1, "cards/capitalistes/banquier.png"));
        cards.add(new Card("Huissier", Card.Family.GOOFY, 5, 1, 1, 4, 1, "cards/capitalistes/huissier.png"));
        cards.add(new Card("Stonks", Card.Family.GOOFY, 7, 3, 2, 4, 2, "cards/capitalistes/stonks.png"));
        cards.add(new Card("Trump", Card.Family.GOOFY, 10, 3, 3, 8, 3, "cards/capitalistes/trump.png"));


        Collections.shuffle(cards);
        return cards;
    }
}
