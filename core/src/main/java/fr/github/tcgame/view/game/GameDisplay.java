
package fr.github.tcgame.view.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import fr.github.tcgame.model.GameModel;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.player.Player;

import java.util.List;

public class GameDisplay extends ApplicationAdapter {

    private GameModel gameModel;
    private SpriteBatch batch;
    private BitmapFont font;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Texture cardBg;

    public GameDisplay(GameModel model) {
        this.gameModel = model;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Cinzel-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 14;
        font = gen.generateFont(p);
        gen.dispose();

        cardBg = new Texture(Gdx.files.internal("placeholder/Card_Template.png"));
    }

    @Override
    public void render() {
        // Fond sombre
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (gameModel == null) {
            font.draw(batch, "En attente...", 600, 360);
            batch.end();
            return;
        }

        Player p1 = gameModel.getPlayer1();
        Player p2 = gameModel.getPlayer2();

        // IA (haut)
        font.setColor(Color.RED);
        font.draw(batch, "IA | Cristal: " + p2.getCrystal().getCurrentHp() + " | Mana: " + p2.getMana(), 20, 700);
        drawCards(p2.getFieldCards(), 20, 580, batch, "TERRAIN IA");
        drawCards(p2.getBench(), 20, 500, batch, "BANC IA");

        // Joueur (bas)
        font.setColor(Color.GREEN);
        font.draw(batch, "VOUS | Cristal: " + p1.getCrystal().getCurrentHp() + " | Mana: " + p1.getMana(), 20, 400);
        drawCards(p1.getFieldCards(), 20, 280, batch, "TERRAIN");
        drawCards(p1.getBench(), 20, 200, batch, "BANC");
        drawCards(p1.getHand(), 20, 100, batch, "MAIN");

        // Tour
        font.setColor(Color.GOLD);
        font.draw(batch, "Tour " + gameModel.getTurnNumber() + " | Pioche: " + gameModel.getDeck().size(), 1000, 700);

        batch.end();
    }

    private void drawCards(List<Card> cards, float x, float y, SpriteBatch batch, String label) {
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, label + ":", x, y + 15);

        if (cards.isEmpty()) {
            font.draw(batch, "  Vide", x + 80, y + 15);
            return;
        }

        float cardX = x + 80;
        for (Card c : cards) {
            if (cardBg != null) {
                batch.draw(cardBg, cardX, y - 40, 60, 85);
            }
            font.setColor(Color.WHITE);
            font.draw(batch, c.getName(), cardX + 5, y + 55);
            if (c.getHp() > 0) {
                font.setColor(c.isBelowHalfHp() ? Color.RED : Color.GREEN);
                font.draw(batch, c.getCurrentHp() + "/" + c.getHp(), cardX + 5, y + 40);
            }
            cardX += 70;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (cardBg != null) cardBg.dispose();
    }
}
