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
            Card.Type.RARE, Card.Family.CAPITALISTE, 15, 3,
            new Attack("Spéculation", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
            new Attack("To the Moon", "Dégâts x2 si l'ennemi a moins de 50% PV.", 6, 4, AttackEffect.FINAL_SENTENCE, 0)));

        cards.add(new Card("Huissier",
            "Vient chercher ce qui lui appartient.",
            Card.Type.COMMUN, Card.Family.CAPITALISTE, 12, 2,
            new Attack("Mise en demeure", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
            new Attack("Sommation", "Dégâts + vole 2 mana à l'adversaire.", 4, 3, AttackEffect.STEAL_COINS, 2)));

        cards.add(new Card("Actionnaire",
            "Investit dans la destruction.",
            Card.Type.COMMUN, Card.Family.CAPITALISTE, 10, 2,
            new Attack("Dividendes", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
            new Attack("Plus-value", "Réduit le coût du prochain déploiement de 2.", 3, 3, AttackEffect.REDUCE_DEPLOY_COST, 2)));

        // === MAUDITS ===
        cards.add(new Card("Corbeau",
            "Augure de mauvais présage.",
            Card.Type.COMMUN, Card.Family.MAUDIT, 8, 2,
            new Attack("Bec acéré", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
            new Attack("Augure", "La prochaine attaque ennemie a 50% de rater.", 1, 3, AttackEffect.MISS_CHANCE, 50)));

        cards.add(new Card("Modo",
            "Silencieux, mais venimeux.",
            Card.Type.RARE, Card.Family.MAUDIT, 18, 4,
            new Attack("Griffure", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
            new Attack("Venin", "Inflige 2 dégâts par tour tant qu'il est en jeu.", 2, 4, AttackEffect.POISON, 2)));

        cards.add(new Card("Bourreau",
            "Execute les ennemis affaiblis.",
            Card.Type.RARE, Card.Family.MAUDIT, 14, 3,
            new Attack("Lame", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
            new Attack("Sentence Finale", "8 dégâts, uniquement si l'ennemi a moins de 50% PV.", 8, 3, AttackEffect.FINAL_SENTENCE, 0)));

        // === PRODIGES ===
        cards.add(new Card("Electricien",
            "Court-circuite ses propres neurones.",
            Card.Type.COMMUN, Card.Family.PRODIGE, 10, 2,
            new Attack("Décharge", "Attaque standard.", 3, 1, AttackEffect.NONE, 0),
            new Attack("Court-circuit", "5 dégâts, mais s'inflige 2 dégâts.", 5, 3, AttackEffect.SELF_DMG, 2)));

        cards.add(new Card("Assureur",
            "Si il meurt, le cristal ne prend pas les dégâts.",
            Card.Type.RARE, Card.Family.PRODIGE, 16, 4,
            new Attack("Contrat", "Attaque standard.", 2, 2, AttackEffect.NONE, 0),
            new Attack("Clause protectrice", "Active un bouclier : si meurt, le cristal ne prend pas les dégâts.", 0, 3, AttackEffect.PROTECT_CRYSTAL, 0)));

        cards.add(new Card("Ganon",
            "Calamité ambulante.",
            Card.Type.LEGENDAIRE, Card.Family.PRODIGE, 25, 5,
            new Attack("Bras de fer", "Attaque standard.", 4, 2, AttackEffect.NONE, 0),
            new Attack("Calamité", "Inflige 3 dégâts directs au cristal ennemi, et s'inflige 3 PV.", 3, 5, AttackEffect.DEAL_CRYSTAL_DMG, 3)));

        // === GOOFYS ===
        cards.add(new Card("Steve",
            "Il pose des TNT partout.",
            Card.Type.COMMUN, Card.Family.GOOFY, 10, 2,
            new Attack("Pioche", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
            new Attack("TNT", "Piège : explose si l'adversaire change de carte (dégâts = ATK spé).", 4, 3, AttackEffect.TRAP, 0)));

        cards.add(new Card("Panzoli",
            "Crée le chaos, prospère dedans.",
            Card.Type.RARE, Card.Family.GOOFY, 13, 3,
            new Attack("Bousculade", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
            new Attack("Chaos Immersif", "Force l'adversaire à changer de carte active.", 2, 4, AttackEffect.FORCE_SWAP, 0)));

        cards.add(new Card("Leclerc",
            "Pas de stratégie, juste foncer.",
            Card.Type.COMMUN, Card.Family.GOOFY, 11, 2,
            new Attack("Charge", "Attaque standard.", 3, 1, AttackEffect.NONE, 0),
            new Attack("Pas de stratégie", "Attaque deux fois de suite (attaque normale x2).", 3, 3, AttackEffect.EXTRA_ATTACK, 0)));

        return cards;
    }
}
