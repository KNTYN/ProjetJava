package fr.github.tcgame.view.card;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import fr.github.tcgame.model.card.Card;

import java.util.ArrayList;
import java.util.List;

import static fr.github.tcgame.model.GameModel.*;

public class CardView {
    private static CardView instance;
    private Stage stage;

    // Listes séparées par joueur ET par zone
    private final List<ImageButton> handCardsP1  = new ArrayList<>();
    private final List<ImageButton> benchCardsP1 = new ArrayList<>();
    private final List<ImageButton> activeCardsP1 = new ArrayList<>();

    private final List<ImageButton> handCardsP2  = new ArrayList<>();
    private final List<ImageButton> benchCardsP2 = new ArrayList<>();
    private final List<ImageButton> activeCardsP2 = new ArrayList<>();

    public CardView() {}

    public static CardView getInstance() {
        if (instance == null) instance = new CardView();
        return instance;
    }

    public void setStage(Stage stage) { this.stage = stage; }
    public Stage getStage() { return stage; }

    // ── Touchable global (pause) ──────────────────────────────────────────────
    public void setTouchable(Touchable t) {
        for (List<ImageButton> list : allLists()) {
            for (ImageButton b : list) b.setTouchable(t);
        }
    }

    // ── Clear par joueur + zone ───────────────────────────────────────────────
    public void clearHandCards(int idP)   { clear(idP == 1 ? handCardsP1  : handCardsP2);  }
    public void clearBenchCards(int idP)  { clear(idP == 1 ? benchCardsP1 : benchCardsP2); }
    public void clearActiveCards(int idP) { clear(idP == 1 ? activeCardsP1 : activeCardsP2); }

    public void clearAllCards() {
        for (List<ImageButton> list : allLists()) clear(list);
    }

    private void clear(List<ImageButton> list) {
        for (ImageButton b : list) b.remove();
        list.clear();
    }

    @SuppressWarnings("unchecked")
    private List<List<ImageButton>> allLists() {
        return List.of(handCardsP1, benchCardsP1, activeCardsP1,
            handCardsP2, benchCardsP2, activeCardsP2);
    }

    // ── Affichage ─────────────────────────────────────────────────────────────
    public void displayCard(Card card, int playerOwner, float x, float y,
                            float width, float height, String zone) {

        TextureRegionDrawable drawable = new TextureRegionDrawable(
            new TextureRegion(card.getCardTexture())
        );

        ImageButton cardButton = new ImageButton(drawable);
        cardButton.setPosition(x, y);
        cardButton.setSize(width, height);

        cardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Seul le joueur dont c'est le tour peut sélectionner SES propres cartes
                if (cardButton.getTouchable() != Touchable.enabled) return;
                if (playerOwner != playerTurn) return;

                System.out.println("✅ CARTE CLIQUÉE : " + card + " (zone: " + zone + ", P" + playerOwner + ")");
                setSelectedCard(card, playerOwner);
            }
        });

        stage.addActor(cardButton);

        // Ajout dans la bonne liste
        List<ImageButton> target = switch (zone) {
            case "hand"   -> playerOwner == 1 ? handCardsP1  : handCardsP2;
            case "bench"  -> playerOwner == 1 ? benchCardsP1 : benchCardsP2;
            case "active" -> playerOwner == 1 ? activeCardsP1 : activeCardsP2;
            default -> throw new IllegalArgumentException("Zone inconnue : " + zone);
        };
        target.add(cardButton);
    }
}
