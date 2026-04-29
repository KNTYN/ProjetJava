package fr.github.tcgame.view.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.view.menu.Menu;

public class GameView extends Menu {

    private static final float CARD_W = 80f;
    private static final float CARD_H = 112f;

    private Texture cardTex;
    private Texture activeCardTex;

    public GameView(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        setBackground("placeholder/BG_GameView.png");
        buildBoardZones();

        cardTex = new Texture("placeholder/Card_Template.png");
        activeCardTex = new Texture("placeholder/activeCard_Template.png");

        // --- 📊 INTERFACES ---
        addLabel("CRISTAL ADVERSE", 180, HEIGHT - 40, 24, Color.RED);
        addLabel("100 PV", 180, HEIGHT - 75, 20, Color.WHITE);

        addLabel("MON CRISTAL", WIDTH - 180, 85, 24, Color.GREEN);
        addLabel("100 PV", WIDTH - 180, 50, 20, Color.WHITE);

        addLabel("⚡ ENERGIE : 1", 150, 85, 22, OR_PALE);
        addLabel("💰 PIECES : 0", 150, 50, 22, Color.GOLD);

        // --- 🃏 DESSIN DES ZONES AVEC LABELS ---
        drawOpponentMain();
        drawOpponentBench();
        drawOpponentActiveCard();

        drawPlayerMain();
        drawPlayerBench();
        drawPlayerActiveCard();

        keyIsPressed(Input.Keys.ESCAPE, controller::goJouer);
    }

    private void drawOpponentMain() {
        float mainY = HEIGHT - 52f - (CARD_H / 2f);

        for (int i = 0; i < 5; i++) {
            addPlaceholderAt(cardTex, WIDTH / 2f - 180 + (i * 90), mainY, CARD_W, CARD_H);
        }
    }

    private void drawOpponentBench() {
        float benchY = HEIGHT / 2f + 90f;
        float espaceEntreCartes = 15f;
        float debutZoneG = 300;
        float finZoneG = WIDTH / 2f;
        float largeurZoneG = finZoneG - debutZoneG;
        float largeurBanc = (3 * CARD_W) + (2 * espaceEntreCartes);
        float startX = debutZoneG + (largeurZoneG - largeurBanc) / 2f + (CARD_W / 2f);

        addLabel("BANC ADVERSE", startX + (CARD_W + espaceEntreCartes), benchY - (CARD_H / 2f) - 35, 18, Color.BLACK);

        for (int i = 0; i < 3; i++) {
            addPlaceholderAt(cardTex, startX + (i * (CARD_W + espaceEntreCartes)), benchY, CARD_W, CARD_H);
        }
    }

    private void drawOpponentActiveCard() {
        float activeX = WIDTH - 550;
        float activeY = HEIGHT / 2f + 80f;

        addPlaceholderAt(activeCardTex, activeX, activeY, CARD_W * 1.1f, CARD_H * 1.1f);
    }

    private void drawPlayerBench() {
        float benchY = HEIGHT / 2f - 90f;
        float espaceEntreCartes = 15f;
        float debutZoneG = 300f;
        float finZoneG = WIDTH / 2f;
        float largeurZoneG = finZoneG - debutZoneG;
        float largeurBanc = (3 * CARD_W) + (2 * espaceEntreCartes);
        float startX = debutZoneG + (largeurZoneG - largeurBanc) / 2f + (CARD_W / 2f);

        addLabel("MON BANC", startX + (CARD_W + espaceEntreCartes), benchY - (CARD_H / 2f) - 30, 18, Color.BLACK);

        for (int i = 0; i < 3; i++) {
            addPlaceholderAt(cardTex, startX + (i * (CARD_W + espaceEntreCartes)), benchY, CARD_W, CARD_H);
        }
    }

    private void drawPlayerActiveCard() {
        float activeX = WIDTH - 550;
        float activeY = HEIGHT / 2f - 80f;
        addPlaceholderAt(activeCardTex, activeX, activeY, CARD_W * 1.1f, CARD_H * 1.1f);
    }

    private void drawPlayerMain() {
        float mainY = 42f + (CARD_H / 2f) + 10f;

        for (int i = 0; i < 5; i++) {
            addPlaceholderAt(cardTex, WIDTH / 2f - 180 + (i * 90), mainY, CARD_W, CARD_H);
        }
    }

    private void addPlaceholderAt(Texture tex, float x, float y, float w, float h) {
        if (tex != null) {
            Image img = new Image(tex);
            img.setSize(w, h);
            img.setPosition(x - w / 2f, y - h / 2f);
            stage.addActor(img);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (cardTex != null) cardTex.dispose();
        if (activeCardTex != null) activeCardTex.dispose();
    }
}
