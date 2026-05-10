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

import static fr.github.tcgame.model.GameModel.setSelectedCard;

public class CardView {
    private static CardView instance;
    private Stage stage;

    private List<ImageButton> handCards = new ArrayList<>();
    private List<ImageButton> benchCards = new ArrayList<>();
    private List<ImageButton> activeCards = new ArrayList<>();

    public CardView() {}

    public static CardView getInstance() {
        if (instance == null) {
            instance = new CardView();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Permet d'activer ou de désactiver les clics sur TOUTES les cartes.
     * Très utile pour le menu pause.
     */
    public void setTouchable(Touchable touchable) {
        for (ImageButton card : handCards) card.setTouchable(touchable);
        for (ImageButton card : benchCards) card.setTouchable(touchable);
        for (ImageButton card : activeCards) card.setTouchable(touchable);
    }

    public void clearHandCards() {
        for (ImageButton card : handCards) {
            card.remove();
        }
        handCards.clear();
    }

    public void clearBenchCards() {
        for (ImageButton card : benchCards) {
            card.remove();
        }
        benchCards.clear();
    }

    public void clearActiveCards() {
        for (ImageButton card : activeCards) {
            card.remove();
        }
        activeCards.clear();
    }

    public void clearAllCards() {
        clearHandCards();
        clearBenchCards();
        clearActiveCards();
    }

    public void displayCard(Card card, int playerOwner, float x, float y, float width, float height, String zone) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(
            new TextureRegion(card.getCardTexture())
        );

        ImageButton cardButton = new ImageButton(drawable);
        cardButton.setPosition(x, y);
        cardButton.setSize(width, height);

        cardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // On vérifie si le bouton est touchable (sécurité supplémentaire)
                if (cardButton.getTouchable() == Touchable.enabled) {
                    System.out.println("✅ CARTE CLIQUÉE : " + card + " (zone: " + zone + ")");
                    setSelectedCard(card, playerOwner);
                }
            }
        });

        stage.addActor(cardButton);

        // Ajouter à la bonne liste
        switch (zone) {
            case "hand" -> handCards.add(cardButton);
            case "bench" -> benchCards.add(cardButton);
            case "active" -> activeCards.add(cardButton);
        }
    }
}
