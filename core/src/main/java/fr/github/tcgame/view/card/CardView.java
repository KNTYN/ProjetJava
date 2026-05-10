package fr.github.tcgame.view.card;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import fr.github.tcgame.model.card.Card;
//Animations
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;

import static fr.github.tcgame.model.GameModel.setSelectedCard;

public class CardView {
    private static CardView instance;
    private Stage stage;

    private List<ImageButton> handCards = new ArrayList<>();
    private List<ImageButton> benchCards = new ArrayList<>();
    private List<ImageButton> activeCards = new ArrayList<>();

    //Animations
    private List<Card> alreadyAnimatedHandCards = new ArrayList<>();
    private Map<Card, float[]> lastCardPositions = new HashMap<>();
    private Map<Card, String> lastCardZones = new HashMap<>();

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
        for (ImageButton cardButton : handCards) {
            Card card = (Card) cardButton.getUserObject();
            rememberCardPosition(cardButton, card, "hand");
            cardButton.remove();
        }
        handCards.clear();
    }

    public void clearBenchCards() {
        for (ImageButton cardButton : benchCards) {
            Card card = (Card) cardButton.getUserObject();
            rememberCardPosition(cardButton, card, "bench");
            cardButton.remove();
        }
        benchCards.clear();
    }

    public void clearActiveCards() {
        for (ImageButton cardButton : activeCards) {
            Card card = (Card) cardButton.getUserObject();
            rememberCardPosition(cardButton, card, "active");
            cardButton.remove();
        }
        activeCards.clear();
    }

    public void clearAllCards() {
        clearHandCards();
        clearBenchCards();
        clearActiveCards();
        alreadyAnimatedHandCards.clear();
    }

    public void displayCard(Card card, int playerOwner, float x, float y, float width, float height, String zone) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(
            new TextureRegion(card.getCardTexture())
        );

        ImageButton cardButton = new ImageButton(drawable);
        cardButton.setUserObject(card);

        cardButton.setSize(width, height);

        cardButton.setOrigin(width / 2f, height / 2f);

        //Slide
        // Slide depuis ancienne zone ou depuis la pioche
        boolean comesFromPreviousZone =
            lastCardPositions.containsKey(card)
                && (
                ("hand".equals(lastCardZones.get(card)) && (zone.equals("bench") || zone.equals("active")))
                    || ("bench".equals(lastCardZones.get(card)) && zone.equals("active"))
            );

        if (comesFromPreviousZone) {
            float[] previousPosition = lastCardPositions.get(card);

            cardButton.setPosition(previousPosition[0], previousPosition[1]);

            cardButton.addAction(
                Actions.moveTo(x, y, 0.35f, Interpolation.smooth)
            );

        } else {
            boolean shouldSlideFromLeft =
                zone.equals("hand")
                    && !alreadyAnimatedHandCards.contains(card);

            if (shouldSlideFromLeft) {
                cardButton.setPosition(-width - 50, y);
                alreadyAnimatedHandCards.add(card);

                cardButton.addAction(
                    Actions.moveTo(x, y, 0.35f, Interpolation.smooth)
                );

            } else {
                cardButton.setPosition(x, y);
            }
        }

        final float baseX = x;
        final float baseY = y;

        cardButton.addListener(new ClickListener() {

            //Eleve la carte lorsque l'on est dessus
            @Override
            public void enter(InputEvent event, float px, float py, int pointer, Actor fromActor) {
                if (cardButton.getTouchable() == Touchable.enabled) {
                    cardButton.clearActions();

                    cardButton.addAction(
                        Actions.parallel(
                            Actions.scaleTo(1.08f, 1.08f, 0.12f, Interpolation.smooth),
                            Actions.moveTo(baseX - 6, baseY + 10, 0.12f, Interpolation.smooth)
                        )
                    );
                }
            }

            //La carte se baisse lorsque l'on est plus dessus
            @Override
            public void exit(InputEvent event, float px, float py, int pointer, Actor toActor) {
                if (cardButton.getTouchable() == Touchable.enabled) {
                    cardButton.clearActions();

                    cardButton.addAction(
                        Actions.parallel(
                            Actions.scaleTo(1f, 1f, 0.12f, Interpolation.smooth),
                            Actions.moveTo(baseX, baseY, 0.12f, Interpolation.smooth)
                        )
                    );
                }
            }

            @Override
            public void clicked(InputEvent event, float px, float py) {
                if (cardButton.getTouchable() == Touchable.enabled) {
                    System.out.println("✅ CARTE CLIQUÉE : " + card + " (zone: " + zone + ")");
                    setSelectedCard(card, playerOwner);

                    cardButton.clearActions();

                    cardButton.addAction(
                        Actions.sequence(
                            Actions.scaleTo(1.15f, 1.15f, 0.08f, Interpolation.smooth),
                            Actions.scaleTo(1.08f, 1.08f, 0.08f, Interpolation.smooth)
                        )
                    );
                }
            }
        });

        stage.addActor(cardButton);

        switch (zone) {
            case "hand" -> handCards.add(cardButton);
            case "bench" -> benchCards.add(cardButton);
            case "active" -> activeCards.add(cardButton);
        }
    }


    private void rememberCardPosition(ImageButton cardButton, Card card, String zone) {
        lastCardPositions.put(card, new float[] {
            cardButton.getX(),
            cardButton.getY()
        });

        lastCardZones.put(card, zone);
    }
}
