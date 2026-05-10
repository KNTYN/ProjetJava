package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import fr.github.tcgame.control.MenuController;

import static fr.github.tcgame.model.GameModel.*;
import static fr.github.tcgame.model.player.Player.WIN;
import static fr.github.tcgame.view.MainGame.AUDIOSETTINGS;
import static fr.github.tcgame.view.MainGame.CARDV;

public class GameMenu extends Menu {

    // =========================
    // LABELS PLAYER 1
    // =========================
    private Label coinLabelP1;
    private Label manaLabelP1;

    // =========================
    // LABELS PLAYER 2
    // =========================
    private Label coinLabelP2;
    private Label manaLabelP2;

    // =========================
    // TURN LABEL
    // =========================
    private Label turnLabel;

    // =========================
    // PAUSE
    // =========================
    private boolean paused = false;
    private Group pauseOverlay;

    public GameMenu(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {

        setBackground("background/BG_game.png");

        // =========================
        // BOUTON BENCH
        // =========================
        addButton("background/bench.png", "background/bench.png", 300, 300, 1f, () -> {
            if (!paused && playerTurn == 1)
                controller.moveCardToBench(1);
        });

        // =========================
        // BOUTON DEPLOY ACTIVE
        // =========================
        addButton("background/card_back.png", "background/card_back.png", 900, 300, 1f, () -> {
            if (!paused && playerTurn == 1)
                controller.deployToActive(1);
        });

        // =========================
        // BOUTON ATTAQUE
        // =========================
        addButton("button/attaque.png", "button/attaque_pressed.png", 1200, 300, 1f, () -> {
            if (!paused && playerTurn == 1)
                controller.attack(1);
        }, ButtonMode.PRESSED);

        // =========================
        // BOUTON SPECIALE
        // =========================
        addButton("button/speciale.png", "button/speciale_pressed.png", 1200, 250, 1f, () -> {
            if (!paused && playerTurn == 1)
                controller.attackSpecial(1);
        }, ButtonMode.PRESSED);

        // =========================
        // BOUTON PASSER
        // =========================
        addButton("button/passer.png", "button/passer_pressed.png", 1200, 200, 1f, () -> {
            if (!paused && playerTurn == 1)
                controller.passed();
        }, ButtonMode.PRESSED);

        // =========================
        // UI
        // =========================
        addCoinDisplay();
        addManaDisplay();
        addTurnDisplay();

        CARDV.setStage(stage);

        // =========================
        // PAUSE OVERLAY
        // =========================
        buildPauseOverlay();

        // =========================
        // ESCAPE = PAUSE
        // =========================
        keyIsPressed(Input.Keys.ESCAPE, () -> {

            paused = !paused;

            pauseOverlay.setVisible(paused);

            System.out.println(paused ? "⏸️ Pause" : "▶️ Reprise");
        });
    }

    // =========================================================
    // COINS DISPLAY
    // =========================================================
    private void addCoinDisplay() {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.getData().setScale(2f);

        Texture coinTexture = new Texture("items/coin.png");

        // =========================
        // PLAYER 1
        // =========================
        Table coinTableP1 = new Table();
        coinTableP1.setPosition(50, 200);

        Image coinIconP1 = new Image(coinTexture);
        coinIconP1.setSize(40, 40);

        coinLabelP1 = new Label("", labelStyle);

        coinTableP1.add(coinIconP1).size(40, 40).padRight(10);
        coinTableP1.add(coinLabelP1);

        stage.addActor(coinTableP1);

        // =========================
        // PLAYER 2
        // =========================
        Table coinTableP2 = new Table();
        coinTableP2.setPosition(WIDTH - 220, HEIGHT - 80);

        Image coinIconP2 = new Image(coinTexture);
        coinIconP2.setSize(40, 40);

        coinLabelP2 = new Label("", labelStyle);

        coinTableP2.add(coinIconP2).size(40, 40).padRight(10);
        coinTableP2.add(coinLabelP2);

        stage.addActor(coinTableP2);
    }

    // =========================================================
    // MANA DISPLAY
    // =========================================================
    private void addManaDisplay() {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.getData().setScale(2f);

        Texture manaTexture = new Texture("items/mana.png");

        // =========================
        // PLAYER 1
        // =========================
        Table manaTableP1 = new Table();
        manaTableP1.setPosition(50, 140);

        Image manaIconP1 = new Image(manaTexture);
        manaIconP1.setSize(40, 40);

        manaLabelP1 = new Label("", labelStyle);

        manaTableP1.add(manaIconP1).size(40, 40).padRight(10);
        manaTableP1.add(manaLabelP1);

        stage.addActor(manaTableP1);

        // =========================
        // PLAYER 2
        // =========================
        Table manaTableP2 = new Table();
        manaTableP2.setPosition(WIDTH - 220, HEIGHT - 140);

        Image manaIconP2 = new Image(manaTexture);
        manaIconP2.setSize(40, 40);

        manaLabelP2 = new Label("", labelStyle);

        manaTableP2.add(manaIconP2).size(40, 40).padRight(10);
        manaTableP2.add(manaLabelP2);

        stage.addActor(manaTableP2);
    }

    // =========================================================
    // TURN DISPLAY
    // =========================================================
    private void addTurnDisplay() {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.getData().setScale(2f);

        turnLabel = new Label("", labelStyle);

        turnLabel.setPosition(50, HEIGHT - 100);

        stage.addActor(turnLabel);
    }

    // =========================================================
    // PAUSE OVERLAY
    // =========================================================
    private void buildPauseOverlay() {

        pauseOverlay = new Group();

        // =========================
        // DARK BACKGROUND
        // =========================
        Image darkBg = new Image(
            skin.newDrawable(
                "white",
                new Color(0, 0, 0, 0.7f)
            )
        );

        darkBg.setSize(WIDTH, HEIGHT);

        pauseOverlay.addActor(darkBg);

        // =========================
        // TITLE
        // =========================
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(3f);

        Label pauseLabel = new Label("PAUSE", style);

        pauseLabel.setPosition(WIDTH / 2f - 100, HEIGHT - 250);

        pauseOverlay.addActor(pauseLabel);

        // =========================
        // SLIDERS
        // =========================
        addPauseSlider(
            WIDTH / 2f - 200,
            590,
            AUDIOSETTINGS.getGlobalVolume(),
            value -> AUDIOSETTINGS.setGlobalVolume(value)
        );

        addPauseSlider(
            WIDTH / 2f - 200,
            425,
            AUDIOSETTINGS.getMusicVolume(),
            value -> AUDIOSETTINGS.setMusicVolume(value)
        );

        addPauseSlider(
            WIDTH / 2f - 200,
            270,
            AUDIOSETTINGS.getSfxVolume(),
            value -> AUDIOSETTINGS.setSfxVolume(value)
        );

        pauseOverlay.setVisible(false);

        stage.addActor(pauseOverlay);
    }

    // =========================================================
    // PAUSE SLIDER
    // =========================================================
    private void addPauseSlider(float x,
                                float y,
                                float initialValue,
                                java.util.function.Consumer<Float> onChange) {

        Slider.SliderStyle style = new Slider.SliderStyle();

        Texture slice = new Texture("slider/bar_slider.png");
        Texture point = new Texture("slider/point_slider.png");

        style.background =
            new TextureRegionDrawable(new TextureRegion(slice));

        style.knob =
            new TextureRegionDrawable(new TextureRegion(point));

        Slider slider = new Slider(0f, 1f, 0.01f, false, style);

        slider.setPosition(x, y);
        slider.setSize(400, 20);

        slider.setValue(initialValue);

        slider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChange.accept(slider.getValue());
            }
        });

        pauseOverlay.addActor(slider);
    }

    // =========================================================
    // DRAW
    // =========================================================
    @Override
    public void draw() {

        // =========================
        // PAUSE
        // =========================
        if (paused) {
            stage.act();
            stage.draw();
            return;
        }

        // =========================
        // PLAYER 1
        // =========================
        if (coinLabelP1 != null) {
            coinLabelP1.setText("x" + P1.piece);
        }

        if (manaLabelP1 != null) {
            manaLabelP1.setText("x" + P1.mana);
        }

        // =========================
        // PLAYER 2
        // =========================
        if (coinLabelP2 != null) {
            coinLabelP2.setText("x" + P2.piece);
        }

        if (manaLabelP2 != null) {
            manaLabelP2.setText("x" + P2.mana);
        }

        // =========================
        // TURN
        // =========================
        if (turnLabel != null) {
            turnLabel.setText("Tour " + TURN + " - Joueur " + playerTurn);
        }

        // =========================
        // WIN
        // =========================
        if (WIN) {
            System.out.println("🏆 VICTOIRE DÉTECTÉE ! Changement vers WinMenu...");
            controller.goWin();
            return;
        }

        super.draw();
    }
}
