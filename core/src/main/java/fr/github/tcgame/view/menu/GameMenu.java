package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import fr.github.tcgame.control.MenuController;

import static fr.github.tcgame.model.GameModel.*;
import static fr.github.tcgame.model.player.Player.WIN;
import static fr.github.tcgame.view.MainGame.CARDV;

public class GameMenu extends Menu {


    private boolean paused = false;
    private Group pauseOverlay;

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
            if (playerTurn == 1)
                controller.moveCardToBench(1);
        });

        // =========================
        // BOUTON DEPLOY ACTIVE
        // =========================
        addButton("background/card_back.png", "background/card_back.png", 900, 300, 1f, () -> {
            if (playerTurn == 1)
                controller.deployToActive(1);
        });

        // =========================
        // BOUTON ATTAQUE
        // =========================
        addButton("button/attaque.png", "button/attaque_pressed.png", 1200, 300, 1f, () -> {
            if (playerTurn == 1)
                controller.attack(1);
        }, ButtonMode.PRESSED);

        // =========================
        // BOUTON SPECIALE
        // =========================
        addButton("button/speciale.png", "button/speciale_pressed.png", 1200, 250, 1f, () -> {
            if (playerTurn == 1)
                controller.attackSpecial(1);
        }, ButtonMode.PRESSED);

        // =========================
        // BOUTON PASSER
        // =========================
        addButton("button/passer.png", "button/passer_pressed.png", 1200, 200, 1f, () -> {
            if (playerTurn == 1)
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
        // DEBUG BOT SKIP
        // =========================
        keyIsPressed(Input.Keys.ESCAPE, () -> {
            System.out.println("Debug Touch");
            controller.debug();
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
        // PLAYER 1 (BOTTOM LEFT)
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
        // PLAYER 2 (TOP RIGHT)
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
        // PLAYER 1 (BOTTOM LEFT)
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
        // PLAYER 2 (TOP RIGHT)
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
    // DRAW
    // =========================================================
    @Override
    public void draw() {

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
