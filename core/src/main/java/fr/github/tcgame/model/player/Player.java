package fr.github.tcgame.model.player;

import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.view.card.ActiveView;
import fr.github.tcgame.view.card.BenchView;
import fr.github.tcgame.view.card.HandView;

import static fr.github.tcgame.model.GameModel.MAX_CARD_BENCH;
import static fr.github.tcgame.model.GameModel.MAX_CARD_HAND;
import static java.lang.Math.max;

public class Player {
    public static boolean WIN = false;
    public static int WINNER_ID = 0; // 0 = aucun, 1 = P1, 2 = P2

    public Crystal crystal;

    public static HandView HANDV1 = new HandView();
    public static BenchView BENCHV1 = new BenchView();

    public static HandView HANDV2 = new HandView();
    public static BenchView BENCHV2 = new BenchView();

    public static ActiveView ACTIVEV1 = new ActiveView();
    public static ActiveView ACTIVEV2 = new ActiveView();

    public Card[] hand = new Card[MAX_CARD_HAND];
    public Card[] bench = new Card[MAX_CARD_BENCH];
    public Card activeCard;

    public Card selectedCard;

    public static int MAX_MANA = 10;

    public int mana = 3;
    public int piece = 6;
    private int idPlayer; // ← Pour savoir qui a gagné

    public Player(int idP) {
        this.crystal = new Crystal(idP);
        this.idPlayer = idP;
    }

    public boolean enoughMana(int mana) {
        return this.mana >= mana;
    }

    public void addMana(int mana) {
        this.mana += mana;
    }

    public void costMana(int mana) {
        this.mana -= mana;
    }

    public boolean enoughPiece(int piece) {
        return this.piece >= piece;
    }

    public void addPiece(int piece) {
        this.piece += piece;
    }

    public void costPiece(int piece) {
        this.piece -= piece;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void resetSelectionCard() {
        this.selectedCard = null;
    }

    public Card getActiveCard() {
        return activeCard;
    }

    public void deployActiveCard(Card c) {
        if (this.activeCard == null && c.getZone().equals(Card.Zone.BENCH)) {
            this.activeCard = c;
            removeCardFromBench(c);
            c.setZone(Card.Zone.ACTIVE);
        } else {
            errorEvent();
        }
    }

    public void removeActiveCard() {
        this.activeCard = null;
    }

    public boolean hasSlot(Card[] tab) {
        int currentItems = 0;
        for (Card c : tab) {
            if (c != null) {
                currentItems++;
            }
        }
        return currentItems < tab.length;
    }

    public boolean contains(Card[] tab, Card c) {
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] == c) {
                return true;
            }
        }
        return false;
    }

    public void removeCard(Card[] tab, Card c) {
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] == c) {
                tab[i] = null;
            }
        }
    }

    public void addCardToHand(Card c) {
        if (hasSlot(this.hand)) {
            for (int i = 0; i < MAX_CARD_HAND; i++) {
                if (this.hand[i] == null) {
                    this.hand[i] = c;
                    c.setZone(Card.Zone.HAND);
                    break;
                }
            }
        } else {
            errorEvent();
        }
    }

    public void removeCardFromHand(Card c) {
        if (contains(this.hand, c)) {
            removeCard(this.hand, c);
        } else {
            errorEvent();
        }
    }

    public void addCardToBench(Card c) {
        if (hasSlot(this.bench)) {
            for (int i = 0; i < MAX_CARD_BENCH; i++) {
                if (this.bench[i] == null) {
                    this.bench[i] = c;
                    c.setZone(Card.Zone.BENCH);
                    break;
                }
            }
        } else {
            errorEvent();
        }
    }

    public void cardHandToBench(Card c) {
        System.out.println("🔍 cardHandToBench appelé avec : " + c);
        System.out.println("🔍 hasSlot(bench) = " + hasSlot(this.bench));

        // FIX: vérifier enoughPiece au lieu de enoughMana
        if (hasSlot(this.bench) && c != null && c.getZone().equals(Card.Zone.HAND) && enoughPiece(c.getCost())) {
            System.out.println("✅ Slot disponible, ajout au bench...");
            addCardToBench(c);
            System.out.println("✅ Carte ajoutée au bench");
            removeCardFromHand(c);
            System.out.println("✅ Carte retirée de la main");
            costPiece(c.getCost());
            System.out.println("✅ Retrait de pièces : (" + this.piece + ")");
        } else {
            errorEvent();
        }
    }

    public void removeCardFromBench(Card c) {
        if (contains(this.bench, c)) {
            removeCard(this.bench, c);
        } else {
            errorEvent();
        }
    }

    public void errorEvent() {
        System.out.println("[Debug] - Vous ne pouvez pas faire ça !");
    }

    public Card[] getHand() {
        return this.hand;
    }

    public Card[] getBench() {
        return this.bench;
    }

    public int getHp() {
        return this.crystal.currentHp;
    }

    public void takeDamage(int dmg) {
        this.crystal.currentHp = max(0, this.crystal.currentHp - dmg);
        this.crystal.updateTexture();
        checkVictory(); // ← Vérifier automatiquement
    }

    public boolean checkDeath() {
        return this.crystal.currentHp == 0;
    }

    public void checkVictory() {
        if (checkDeath()) {
            WIN = true;
            WINNER_ID = (this.idPlayer == 1) ? 2 : 1; // L'adversaire gagne
            System.out.println("💀 Joueur " + this.idPlayer + " mort ! Joueur " + WINNER_ID + " gagne !");
        }
    }
}
