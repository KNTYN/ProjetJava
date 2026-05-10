package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import fr.github.tcgame.control.MenuController;

import java.util.function.Consumer;

import static fr.github.tcgame.model.GameModel.*;
import static fr.github.tcgame.model.player.Player.WIN;
import static fr.github.tcgame.view.MainGame.AUDIOSETTINGS;
import static fr.github.tcgame.view.MainGame.CARDV;

public class GameMenu extends Menu {

    private boolean paused = false;
    private Group pauseOverlay;

    private Label coinLabelP1;
    private Label manaLabelP1;
    private Label coinLabelP2;
    private Label manaLabelP2;
    private Label turnLabel;

    private Texture coinTex;
    private Texture manaTex;

    public GameMenu(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_game.png");

        coinTex = new Texture("items/coin.png");
        manaTex = new Texture("items/mana.png");

        // Boutons de jeu avec vérification de l'état pause
        addButton("background/bench.png", "background/bench.png", 300, 300, 1f,
            () -> { if (!paused && playerTurn == 1) controller.moveCardToBench(1); });

        addButton("background/card_back.png", "background/card_back.png", 900, 300, 1f,
            () -> { if (!paused && playerTurn == 1) controller.deployToActive(1); });

        addButton("button/attaque.png", "button/attaque_pressed.png", 1200, 300, 1f,
            () -> { if (!paused && playerTurn == 1) controller.attack(1); }, ButtonMode.PRESSED);

        addButton("button/speciale.png", "button/speciale_pressed.png", 1200, 250, 1f,
            () -> { if (!paused && playerTurn == 1) controller.attackSpecial(1); }, ButtonMode.PRESSED);

        addButton("button/passer.png", "button/passer_pressed.png", 1200, 200, 1f,
            () -> { if (!paused && playerTurn == 1) controller.passed(); }, ButtonMode.PRESSED);

        addCoinDisplay();
        addManaDisplay();
        addTurnDisplay();

        // L'overlay est créé ici mais sera géré dans le draw() pour le Z-Index
        createPauseOverlay();

        CARDV.setStage(stage);

        keyIsPressed(Input.Keys.A, () -> { controller.debug(); });

        keyIsPressed(Input.Keys.ESCAPE, () -> {
            paused = !paused;
            pauseOverlay.setVisible(paused);

            // Désactive les interactions avec les cartes si en pause
            if (paused) {
                CARDV.setTouchable(Touchable.disabled);
            } else {
                CARDV.setTouchable(Touchable.enabled);
            }
        });
    }

    private void createPauseOverlay() {
        pauseOverlay = new Group();

        // Background de pause (assombri pour mieux voir l'UI)
        Image bg = new Image(new TextureRegionDrawable(new Texture("background/BG_MainMenu.png")));
        bg.setSize(WIDTH, HEIGHT);
        bg.setColor(1, 1, 1, 1f); // Optionnel : légère transparence
        pauseOverlay.addActor(bg);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(3f);

        Label title = new Label("PAUSE", style);
        title.setPosition(WIDTH / 2f - 60, HEIGHT - 100);
        pauseOverlay.addActor(title);

        addPauseSlider(WIDTH / 2f - 200, 590, AUDIOSETTINGS.getGlobalVolume(), AUDIOSETTINGS::setGlobalVolume);
        addPauseSlider(WIDTH / 2f - 200, 425, AUDIOSETTINGS.getMusicVolume(), AUDIOSETTINGS::setMusicVolume);
        addPauseSlider(WIDTH / 2f - 200, 270, AUDIOSETTINGS.getSfxVolume(), AUDIOSETTINGS::setSfxVolume);

        pauseOverlay.setVisible(false);
        stage.addActor(pauseOverlay);
    }

    private void addPauseSlider(float x, float y, float value, Consumer<Float> onChange) {
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = new TextureRegionDrawable(new Texture("slider/bar_slider.png"));
        style.knob = new TextureRegionDrawable(new Texture("slider/point_slider.png"));

        Slider slider = new Slider(0f, 1f, 0.01f, false, style);
        slider.setPosition(x, y);
        slider.setSize(400, 20);
        slider.setValue(value);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChange.accept(slider.getValue());
            }
        });
        pauseOverlay.addActor(slider);
    }

    @Override
    public void draw() {
        if (paused) {
            pauseOverlay.setVisible(true);
            // CRUCIAL : On force l'overlay à passer devant tout le reste (y compris les cartes)
            pauseOverlay.toFront();

            stage.act(0);
            stage.draw();
            return;
        }

        pauseOverlay.setVisible(false);

        // Update UI Game
        if (coinLabelP1 != null) coinLabelP1.setText("x" + P1.piece);
        if (manaLabelP1 != null) manaLabelP1.setText("x" + P1.mana);
        if (coinLabelP2 != null) coinLabelP2.setText("x" + P2.piece);
        if (manaLabelP2 != null) manaLabelP2.setText("x" + P2.mana);
        if (turnLabel != null) turnLabel.setText("Tour " + TURN + " - Joueur " + playerTurn);

        if (WIN) {
            controller.goWin();
            return;
        }

        super.draw();
    }

    // Méthodes UI inchangées pour la clarté...
    private void addCoinDisplay() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(2f);
        Table p1 = new Table(); p1.setPosition(50, 200);
        coinLabelP1 = new Label("", style);
        p1.add(new Image(coinTex)).size(40, 40).padRight(10);
        p1.add(coinLabelP1);
        stage.addActor(p1);

        Table p2 = new Table(); p2.setPosition(WIDTH - 220, HEIGHT - 80);
        coinLabelP2 = new Label("", style);
        p2.add(new Image(coinTex)).size(40, 40).padRight(10);
        p2.add(coinLabelP2);
        stage.addActor(p2);
    }

    private void addManaDisplay() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(2f);
        Table p1 = new Table(); p1.setPosition(50, 140);
        manaLabelP1 = new Label("", style);
        p1.add(new Image(manaTex)).size(40, 40).padRight(10);
        p1.add(manaLabelP1);
        stage.addActor(p1);

        Table p2 = new Table(); p2.setPosition(WIDTH - 220, HEIGHT - 140);
        manaLabelP2 = new Label("", style);
        p2.add(new Image(manaTex)).size(40, 40).padRight(10);
        p2.add(manaLabelP2);
        stage.addActor(p2);
    }

    private void addTurnDisplay() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(2f);
        turnLabel = new Label("", style);
        turnLabel.setPosition(50, HEIGHT - 100);
        stage.addActor(turnLabel);
    }
}
