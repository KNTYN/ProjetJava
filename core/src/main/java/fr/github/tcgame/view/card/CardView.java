package fr.github.tcgame.view.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import fr.github.tcgame.model.card.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import static fr.github.tcgame.model.GameModel.*;

public class CardView {
    private static CardView instance;
    private Stage stage;

    // Listes séparées par joueur ET par zone (prio 1)
    private final List<Group> handCardsP1   = new ArrayList<>();
    private final List<Group> benchCardsP1  = new ArrayList<>();
    private final List<Group> activeCardsP1 = new ArrayList<>();

    private final List<Group> handCardsP2   = new ArrayList<>();
    private final List<Group> benchCardsP2  = new ArrayList<>();
    private final List<Group> activeCardsP2 = new ArrayList<>();

    // Animations (prio 2)
    private final List<Card> alreadyAnimatedHandCards = new ArrayList<>();
    private final Map<Card, float[]> lastCardPositions = new HashMap<>();
    private final Map<Card, String> lastCardZones = new HashMap<>();
    private final Set<Card> dyingCards = new HashSet<>();

    public CardView() {}

    public static CardView getInstance() {
        if (instance == null) instance = new CardView();
        return instance;
    }

    public void setStage(Stage stage) { this.stage = stage; }
    public Stage getStage() { return stage; }

    public void setTouchable(Touchable t) {
        for (List<Group> list : allLists())
            for (Group g : list)
                g.setTouchable(t);
    }

    public void clearHandCards(int idP)   { clear(idP == 1 ? handCardsP1   : handCardsP2);  }
    public void clearBenchCards(int idP)  { clear(idP == 1 ? benchCardsP1  : benchCardsP2); }
    public void clearActiveCards(int idP) { clear(idP == 1 ? activeCardsP1 : activeCardsP2); }

    public void clearAllCards() {
        for (List<Group> list : allLists()) clear(list);
        alreadyAnimatedHandCards.clear();
    }

    private void clear(List<Group> list) {
        List<Group> toRemove = new ArrayList<>();

        for (Group g : list) {
            Card card = (Card) g.getUserObject();

            if (card != null && dyingCards.contains(card)) {
                continue;
            }

            if (card != null && card.isDying()) {
                animateDeath(card);
                card.setDying(false);
                continue;
            }

            rememberCardPosition(g, card, (String) g.getName());
            g.remove();
            toRemove.add(g);
        }

        list.removeAll(toRemove);
    }

    private List<List<Group>> allLists() {
        return List.of(handCardsP1, benchCardsP1, activeCardsP1,
            handCardsP2, benchCardsP2, activeCardsP2);
    }

    public void displayCard(Card card, int playerOwner, float x, float y,
                            float width, float height, String zone) {

        Group cardGroup = new Group();
        cardGroup.setPosition(x, y);
        cardGroup.setSize(width, height);
        cardGroup.setUserObject(card); // pour rememberCardPosition
        cardGroup.setName(zone);       // pour retrouver la zone au clear

        // ── ImageButton ───────────────────────────────────────────────────────
        ImageButton cardButton = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(card.getCardTexture()))
        );
        cardButton.setSize(width, height);
        cardButton.setPosition(0, 0);
        cardButton.setOrigin(width / 2f, height / 2f);

        // ── Animation slide ───────────────────────────────────────────────────
        boolean comesFromPreviousZone =
            lastCardPositions.containsKey(card) && (
                ("hand".equals(lastCardZones.get(card))  && (zone.equals("bench") || zone.equals("active"))) ||
                    ("bench".equals(lastCardZones.get(card)) && zone.equals("active"))
            );

        if (comesFromPreviousZone) {
            float[] prev = lastCardPositions.get(card);
            cardGroup.setPosition(prev[0], prev[1]);
            cardGroup.addAction(Actions.moveTo(x, y, 0.35f, Interpolation.smooth));
        } else if (zone.equals("hand") && !alreadyAnimatedHandCards.contains(card)) {
            cardGroup.setPosition(-width - 50, y);
            alreadyAnimatedHandCards.add(card);
            cardGroup.addAction(Actions.moveTo(x, y, 0.35f, Interpolation.smooth));
        }

        // ── Hover / click listeners ───────────────────────────────────────────
        cardButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float px, float py, int pointer, Actor fromActor) {
                if (cardGroup.getTouchable() == Touchable.enabled && playerOwner == playerTurn) {
                    cardButton.clearActions();
                    cardButton.addAction(Actions.parallel(
                        Actions.scaleTo(1.08f, 1.08f, 0.12f, Interpolation.smooth),
                        Actions.moveTo(-6, 10, 0.12f, Interpolation.smooth)
                    ));
                }
            }

            @Override
            public void exit(InputEvent event, float px, float py, int pointer, Actor toActor) {
                if (cardGroup.getTouchable() == Touchable.enabled && playerOwner == playerTurn) {
                    cardButton.clearActions();
                    cardButton.addAction(Actions.parallel(
                        Actions.scaleTo(1f, 1f, 0.12f, Interpolation.smooth),
                        Actions.moveTo(0, 0, 0.12f, Interpolation.smooth)
                    ));
                }
            }

            @Override
            public void clicked(InputEvent event, float px, float py) {
                if (cardGroup.getTouchable() != Touchable.enabled) return;
                if (playerOwner != playerTurn) return;
                System.out.println("✅ CARTE CLIQUÉE : " + card + " (zone: " + zone + ", P" + playerOwner + ")");
                setSelectedCard(card, playerOwner);
                cardButton.clearActions();
                cardButton.addAction(Actions.sequence(
                    Actions.scaleTo(1.15f, 1.15f, 0.08f, Interpolation.smooth),
                    Actions.scaleTo(1.08f, 1.08f, 0.08f, Interpolation.smooth)
                ));
            }
        });

        // ── Label HP ──────────────────────────────────────────────────────────
        Label.LabelStyle hpStyle = new Label.LabelStyle();
        hpStyle.font = new BitmapFont();
        hpStyle.font.getData().setScale(1.1f);
        hpStyle.fontColor = Color.GREEN;
        Label hpLabel = new Label("", hpStyle) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText(card.getHp() + " HP");
            }
        };
        hpLabel.setPosition(5, 5);
        hpLabel.setTouchable(Touchable.disabled);

        cardGroup.addActor(cardButton);
        cardGroup.addActor(hpLabel);
        stage.addActor(cardGroup);

        List<Group> target = switch (zone) {
            case "hand"   -> playerOwner == 1 ? handCardsP1   : handCardsP2;
            case "bench"  -> playerOwner == 1 ? benchCardsP1  : benchCardsP2;
            case "active" -> playerOwner == 1 ? activeCardsP1 : activeCardsP2;
            default -> throw new IllegalArgumentException("Zone inconnue : " + zone);
        };
        target.add(cardGroup);
    }

    private void rememberCardPosition(Group g, Card card, String zone) {
        if (card == null || zone == null) return;
        lastCardPositions.put(card, new float[]{ g.getX(), g.getY() });
        lastCardZones.put(card, zone);
    }

    private void playDeathAnimation(Group g) {
        g.setTouchable(Touchable.disabled);
        g.clearActions();
        g.toFront();

        g.addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.fadeOut(0.6f),
                    Actions.scaleTo(0.15f, 0.15f, 0.6f, Interpolation.pow2Out),
                    Actions.rotateBy(25f, 0.6f)
                ),
                Actions.removeActor()
            )
        );
    }

    public void animateDeath(Card card) {
        Group g = findGroupByCard(card);

        if (g == null) {
            return;
        }

        dyingCards.add(card);

        g.setTouchable(Touchable.disabled);
        g.clearActions();
        g.toFront();

        g.addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.fadeOut(0.6f),
                    Actions.scaleTo(0.15f, 0.15f, 0.6f, Interpolation.pow2Out),
                    Actions.rotateBy(25f, 0.6f)
                ),
                Actions.run(() -> {
                    g.remove();
                    removeGroupFromAllLists(g);
                    dyingCards.remove(card);
                })
            )
        );
    }

    private Group findGroupByCard(Card card) {
        for (List<Group> list : allLists()) {
            for (Group g : list) {
                if (g.getUserObject() == card) {
                    return g;
                }
            }
        }

        return null;
    }

    private void removeGroupFromAllLists(Group group) {
        for (List<Group> list : allLists()) {
            list.remove(group);
        }
    }
}
