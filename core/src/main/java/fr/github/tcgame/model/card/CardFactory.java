package fr.github.tcgame.model.card;

import fr.github.tcgame.model.attack.Attack;
import fr.github.tcgame.model.attack.AttackEffect;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {

    public static List<Card> createAllCards() {
        List<Card> cards = new ArrayList<>();

        // === CAPITALISTES ===
        cards.add(new Card("Stonks",
            "Spécule sur tout, même sur ta défaite.",
            Card.Type.RARE, Card.Family.CAPITALISTE, 15, 4,
            new Attack("Spéculation", "Attaque standard.",3, 2,AttackEffect.NONE,0),
            new Attack("To the Moon", "Si kill, rapporte 3 pièces au lieu d'1.", 6, 4,
                AttackEffect.BONUS_COINS, 3)));



        // === SORTS ===
        cards.add(new Card("Shuffle",       "Rase tout le terrain, remelange tout.",             Card.Type.OBJET, Card.Family.SORT, 0, 2, null, null));
        cards.add(new Card("Boost",         "Boost temporaire PV/ATK pendant 1 tour.",           Card.Type.OBJET, Card.Family.SORT, 0, 2, null, null));
        cards.add(new Card("Freeze",        "Empêche une carte d'attaquer au prochain tour.",    Card.Type.OBJET, Card.Family.SORT, 0, 2, null, null));
        cards.add(new Card("Soin",          "Soigne le cristal de 15 PV.",                       Card.Type.OBJET, Card.Family.SORT, 0, 2, null, null));
        cards.add(new Card("Burst",         "Dégâts aléatoires entre les deux cristaux.",        Card.Type.OBJET, Card.Family.SORT, 0, 3, null, null));
        cards.add(new Card("Coupe Energie", "Divise le mana par 2 des deux joueurs.",            Card.Type.OBJET, Card.Family.SORT, 0, 2, null, null));
        cards.add(new Card("Boomerang",     "Renvoie la carte active adverse au banc.",          Card.Type.OBJET, Card.Family.SORT, 0, 2, null, null));
        cards.add(new Card("Raffinerie",    "Convertit 3 mana en 2 pièces.",                     Card.Type.OBJET, Card.Family.SORT, 0, 3, null, null));
        cards.add(new Card("Pioche",        "Pioche 2 cartes supplémentaires.",                  Card.Type.OBJET, Card.Family.SORT, 0, 1, null, null));
        cards.add(new Card("Urssaf",        "L'adversaire perd 50% de ses pièces.",              Card.Type.RARE,  Card.Family.SORT, 0, 4, null, null));

        return cards;
    }
}
