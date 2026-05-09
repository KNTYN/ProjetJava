package fr.github.tcgame.model.card;

import fr.github.tcgame.model.attack.Attack;
import fr.github.tcgame.model.attack.AttackEffect;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {

    public static List<Card> createAllCards() {
        List<Card> cards = new ArrayList<>();

        // === CAPITALISTES (6 uniques × 3 = 18) ===
        for (int i = 0; i < 3; i++) {
            cards.add(new Card("Stonks", "Spécule sur tout, même sur ta défaite.", Card.Type.RARE, Card.Family.CAPITALISTE, 15, 3,
                new Attack("Spéculation", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
                new Attack("To the Moon", "+3 mana si kill.", 6, 4, AttackEffect.STEAL_COINS, 3)));

            cards.add(new Card("Huissier", "Vient chercher ce qui lui appartient.", Card.Type.COMMUN, Card.Family.CAPITALISTE, 12, 3,
                new Attack("Mise en demeure", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
                new Attack("Sommation", "Vole 2 mana à l'adversaire.", 4, 3, AttackEffect.STEAL_COINS, 2)));

            cards.add(new Card("Donald Trump", "Make TCG Great Again.", Card.Type.RARE, Card.Family.CAPITALISTE, 18, 5,
                new Attack("Tweet impulsif", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
                new Attack("Coup médiatique", "Dégâts massifs + réduit coût déploiement.", 6, 4, AttackEffect.REDUCE_DEPLOY_COST, 1)));

            cards.add(new Card("Actionnaire", "Investit dans la destruction.", Card.Type.COMMUN, Card.Family.CAPITALISTE, 10, 2,
                new Attack("Dividendes", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
                new Attack("Plus-value", "Réduit le coût du prochain déploiement de 2.", 3, 3, AttackEffect.REDUCE_DEPLOY_COST, 2)));

            cards.add(new Card("Banquier", "L'argent ne dort jamais.", Card.Type.COMMUN, Card.Family.CAPITALISTE, 12, 4,
                new Attack("Intérêts composés", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
                new Attack("Placement risqué", "Gagne 3 mana.", 2, 3, AttackEffect.STEAL_COINS, 3)));

            cards.add(new Card("Assureur", "Protège votre cristal en cas de décès.", Card.Type.RARE, Card.Family.CAPITALISTE, 14, 4,
                new Attack("Contrat", "Attaque standard.", 2, 2, AttackEffect.NONE, 0),
                new Attack("Clause protectrice", "Protège le cristal si cette carte meurt.", 0, 3, AttackEffect.PROTECT_CRYSTAL, 0)));
        }

        // === MAUDITS (5 uniques × 2 = 10) ===
        for (int i = 0; i < 2; i++) {
            cards.add(new Card("Ganon", "Calamité ambulante.", Card.Type.LEGENDAIRE, Card.Family.MAUDIT, 25, 6,
                new Attack("Bras de fer", "Attaque standard.", 5, 3, AttackEffect.NONE, 0),
                new Attack("Calamité", "Dégâts au cristal ET s'inflige 3 PV.", 4, 5, AttackEffect.DEAL_CRYSTAL_DMG, 4)));

            cards.add(new Card("Bourreau", "Exécute les ennemis affaiblis.", Card.Type.RARE, Card.Family.MAUDIT, 14, 3,
                new Attack("Lame", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
                new Attack("Sentence Finale", "8 dégâts si ennemi < 50% PV.", 8, 3, AttackEffect.FINAL_SENTENCE, 0)));

            cards.add(new Card("Corbeau", "Augure de mauvais présage.", Card.Type.COMMUN, Card.Family.MAUDIT, 8, 2,
                new Attack("Bec acéré", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
                new Attack("Augure", "Prochaine attaque ennemie : 50% de rater.", 1, 3, AttackEffect.MISS_CHANCE, 50)));

            cards.add(new Card("Leclerc", "Pas de stratégie, juste foncer.", Card.Type.RARE, Card.Family.MAUDIT, 14, 4,
                new Attack("Charge", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
                new Attack("Pas de stratégie", "Attaque deux fois de suite.", 4, 3, AttackEffect.EXTRA_ATTACK, 0)));

            cards.add(new Card("Modo Discord", "Silencieux mais venimeux.", Card.Type.RARE, Card.Family.MAUDIT, 16, 4,
                new Attack("Griffure", "Attaque standard.", 2, 2, AttackEffect.NONE, 0),
                new Attack("Venin", "Empoisonne la cible (2 dégâts/tour).", 2, 4, AttackEffect.POISON, 2)));
        }

        // === PRODIGES (5 uniques × 2 = 10) ===
        for (int i = 0; i < 2; i++) {
            cards.add(new Card("Panzoli", "Crée le chaos, prospère dedans.", Card.Type.LEGENDAIRE, Card.Family.PRODIGE, 18, 6,
                new Attack("Bousculade", "Attaque standard.", 4, 2, AttackEffect.NONE, 0),
                new Attack("Chaos Immersif", "Force l'adversaire à échanger sa carte.", 3, 4, AttackEffect.FORCE_SWAP, 0)));

            cards.add(new Card("Steve", "Il pose des TNT partout.", Card.Type.COMMUN, Card.Family.PRODIGE, 12, 3,
                new Attack("Pioche", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
                new Attack("TNT", "Piège : explose si l'adversaire échange.", 5, 3, AttackEffect.TRAP, 0)));

            cards.add(new Card("Link", "Le héros du temps.", Card.Type.LEGENDAIRE, Card.Family.PRODIGE, 20, 5,
                new Attack("Coup d'épée", "Attaque standard.", 4, 2, AttackEffect.NONE, 0),
                new Attack("Flèche de lumière", "Dégâts puissants.", 7, 4, AttackEffect.NONE, 0)));

            cards.add(new Card("Max Verstappen", "Pilote d'élite.", Card.Type.RARE, Card.Family.PRODIGE, 16, 4,
                new Attack("Dépassement", "Attaque standard.", 4, 2, AttackEffect.NONE, 0),
                new Attack("Tour éclair", "Dégâts massifs.", 7, 4, AttackEffect.NONE, 0)));

            cards.add(new Card("Leon Kennedy", "Agent spécial.", Card.Type.RARE, Card.Family.PRODIGE, 17, 4,
                new Attack("Tir précis", "Attaque standard.", 4, 2, AttackEffect.NONE, 0),
                new Attack("Lance-roquettes", "Dégâts énormes.", 8, 5, AttackEffect.NONE, 0)));
        }

        // === GOOFYS (4 uniques × 3 = 12) ===
        for (int i = 0; i < 3; i++) {
            cards.add(new Card("Air Fryer", "Électroménager de combat.", Card.Type.COMMUN, Card.Family.GOOFY, 10, 1,
                new Attack("Rotation", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
                new Attack("Mode frite", "Dégâts légers.", 4, 2, AttackEffect.NONE, 0)));

            cards.add(new Card("Électricien", "Court-circuite ses propres neurones.", Card.Type.COMMUN, Card.Family.GOOFY, 10, 2,
                new Attack("Décharge", "Attaque standard.", 4, 1, AttackEffect.NONE, 0),
                new Attack("Court-circuit", "6 dégâts mais s'inflige 2 PV.", 6, 3, AttackEffect.SELF_DMG, 2)));

            cards.add(new Card("Multiprise", "Toujours plus de connexions.", Card.Type.COMMUN, Card.Family.GOOFY, 11, 2,
                new Attack("Surcharge", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
                new Attack("Court-jus", "Dégâts moyens.", 5, 3, AttackEffect.NONE, 0)));

            cards.add(new Card("Fiat Multipla", "La légende sur roues.", Card.Type.RARE, Card.Family.GOOFY, 16, 3,
                new Attack("Accélération", "Attaque standard.", 3, 2, AttackEffect.NONE, 0),
                new Attack("Klaxon assourdissant", "Dégâts puissants.", 6, 4, AttackEffect.NONE, 0)));
        }

        // === SORTS (10) ===
        cards.add(new Card("Soin", "Soigne votre cristal.", Card.Type.EVENT, Card.Family.SORT, 0, 2,
            new Attack("Soin léger", "Soigne 10 PV.", 0, 2, AttackEffect.NONE, 0),
            new Attack("Grand soin", "Soigne 20 PV.", 0, 4, AttackEffect.NONE, 0)));
        cards.add(new Card("Soin", "Soigne votre cristal.", Card.Type.EVENT, Card.Family.SORT, 0, 2,
            new Attack("Soin léger", "Soigne 10 PV.", 0, 2, AttackEffect.NONE, 0),
            new Attack("Grand soin", "Soigne 20 PV.", 0, 4, AttackEffect.NONE, 0)));
        cards.add(new Card("Boule de feu", "Dégâts directs.", Card.Type.EVENT, Card.Family.SORT, 0, 3,
            new Attack("Flamme", "5 dégâts à une carte.", 5, 3, AttackEffect.NONE, 0),
            new Attack("Inferno", "8 dégâts à une carte.", 8, 5, AttackEffect.NONE, 0)));
        cards.add(new Card("Boule de feu", "Dégâts directs.", Card.Type.EVENT, Card.Family.SORT, 0, 3,
            new Attack("Flamme", "5 dégâts à une carte.", 5, 3, AttackEffect.NONE, 0),
            new Attack("Inferno", "8 dégâts à une carte.", 8, 5, AttackEffect.NONE, 0)));
        cards.add(new Card("Freeze", "Gèle une carte ennemie.", Card.Type.EVENT, Card.Family.SORT, 0, 2,
            new Attack("Gelure", "L'ennemi ne peut pas attaquer.", 0, 2, AttackEffect.NONE, 0),
            new Attack("Blizzard", "Gèle toutes les cartes.", 0, 4, AttackEffect.NONE, 0)));
        cards.add(new Card("Freeze", "Gèle une carte ennemie.", Card.Type.EVENT, Card.Family.SORT, 0, 2,
            new Attack("Gelure", "L'ennemi ne peut pas attaquer.", 0, 2, AttackEffect.NONE, 0),
            new Attack("Blizzard", "Gèle toutes les cartes.", 0, 4, AttackEffect.NONE, 0)));
        cards.add(new Card("Boost", "Boost temporaire.", Card.Type.EVENT, Card.Family.SORT, 0, 1,
            new Attack("Encouragement", "+2 dégâts ce tour.", 0, 1, AttackEffect.NONE, 0),
            new Attack("Adrénaline", "+4 dégâts ce tour.", 0, 3, AttackEffect.NONE, 0)));
        cards.add(new Card("Boost", "Boost temporaire.", Card.Type.EVENT, Card.Family.SORT, 0, 1,
            new Attack("Encouragement", "+2 dégâts ce tour.", 0, 1, AttackEffect.NONE, 0),
            new Attack("Adrénaline", "+4 dégâts ce tour.", 0, 3, AttackEffect.NONE, 0)));
        cards.add(new Card("Shuffle", "Mélange le terrain.", Card.Type.EVENT, Card.Family.SORT, 0, 3,
            new Attack("Tourbillon", "Force un échange.", 0, 3, AttackEffect.FORCE_SWAP, 0),
            new Attack("Tornade", "Force 2 échanges.", 0, 5, AttackEffect.FORCE_SWAP, 0)));
        cards.add(new Card("Shuffle", "Mélange le terrain.", Card.Type.EVENT, Card.Family.SORT, 0, 3,
            new Attack("Tourbillon", "Force un échange.", 0, 3, AttackEffect.FORCE_SWAP, 0),
            new Attack("Tornade", "Force 2 échanges.", 0, 5, AttackEffect.FORCE_SWAP, 0)));

        // === TASTY CROUSTY (4 exemplaires) ===
        for (int i = 0; i < 4; i++) {
            cards.add(new Card("Tasty Crousty",
                "Si vous avez 6 créatures sur le terrain, vous gagnez !",
                Card.Type.TASTY_CROUSTY, Card.Family.EVENT, 0, 8,
                new Attack("Appel du Crousty", "6 créatures sur VOTRE terrain = VICTOIRE", 0, 8, AttackEffect.NONE, 0),
                new Attack("Festin Suprême", "6 créatures sur le terrain ADVERSE = VICTOIRE", 0, 10, AttackEffect.NONE, 0)));
        }

        // Ajuster à 60 cartes
        while (cards.size() > 60) cards.remove(cards.size() - 1);
        while (cards.size() < 60) {
            cards.add(new Card("Facture", "Personne n'aime les recevoir.", Card.Type.COMMUN, Card.Family.GOOFY, 6, 1,
                new Attack("Échéance", "Attaque standard.", 2, 1, AttackEffect.NONE, 0),
                new Attack("Rappel", "Attaque légère.", 3, 2, AttackEffect.NONE, 0)));
        }

        return cards;
    }
}
