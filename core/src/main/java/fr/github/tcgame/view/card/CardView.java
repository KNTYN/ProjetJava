package fr.github.tcgame.view.card;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import fr.github.tcgame.model.card.Card;

import static fr.github.tcgame.model.GameModel.setSelectedCard;

public class CardView{
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void displayCard(Card card, int playerOwner, float x, float y, float width, float height) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(
            new TextureRegion(card.getCardTexture())
        );

        ImageButton cardButton = new ImageButton(drawable);
        cardButton.setPosition(x, y);
        cardButton.setSize(width, height);

        cardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("✅ CARTE CLIQUÉE : " + card);
                setSelectedCard(card, playerOwner);
            }
        });

        stage.addActor(cardButton);
    }
}
