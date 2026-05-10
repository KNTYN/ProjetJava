package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import fr.github.tcgame.control.MenuController;

import static fr.github.tcgame.model.GameModel.*;
import static fr.github.tcgame.model.player.Player.WIN;
import static fr.github.tcgame.view.MainGame.CARDV;

public class GameMenu extends Menu {
    private Label coinLabel;
    private Label manaLabel;
    private Label turnLabel; // ← Afficher le numéro de tour

    public GameMenu(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_game.png");

        addButton("background/bench.png", "background/bench.png", 300, 300, 1f, () -> {
            if (playerTurn == 1) controller.moveCardToBench(1);
        });

        addButton("background/card_back.png", "background/card_back.png", 900, 300, 1f, () -> {
            if (playerTurn == 1) controller.deployToActive(1);
        });

        addButton("button/attaque.png", "button/attaque_pressed.png", 1200, 300, 1f, () -> {
            if (playerTurn == 1) controller.attack(1);
        }, ButtonMode.PRESSED,"sfx/Click_stereo.ogg.mp3");

        addButton("button/speciale.png", "button/speciale_pressed.png", 1200, 250, 1f, () -> {
            if (playerTurn == 1) controller.attackSpecial(1);
        }, ButtonMode.PRESSED,"sfx/Click_stereo.ogg.mp3");

        addButton("button/passer.png", "button/passer_pressed.png", 1200, 200, 1f, () -> {
            if (playerTurn == 1) controller.passed();
        }, ButtonMode.PRESSED,"sfx/Click_stereo.ogg.mp3");

        addCoinDisplay();
        addManaDisplay();
        addTurnDisplay(); // ← Afficher le tour

        CARDV.setStage(stage);

        keyIsPressed(Input.Keys.ESCAPE, () -> {
            System.out.println("Debug Touch");
            controller.debug();
        });
    }

    private void addCoinDisplay() {
        int x;
        int y;
        if (playerTurn == 1) {
            x=50;
            y=200;
        } else {
            x=50;
            y=400;
        }
        Table coinTable = new Table();
        coinTable.setPosition(x, y);

        Texture coinTexture = new Texture("items/coin.png");
        Image coinIcon = new Image(coinTexture);
        coinIcon.setSize(40, 40);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.getData().setScale(2f);

        coinLabel = new Label("", labelStyle);

        coinTable.add(coinIcon).size(40, 40).padRight(10);
        coinTable.add(coinLabel);
        stage.addActor(coinTable);
    }

    private void addManaDisplay() {
        int x;
        int y;
        if (playerTurn == 1) {
            x=50;
            y=140;
        } else {
            x=50;
            y=360;
        }
        Table manaTable = new Table();
        manaTable.setPosition(x, y);

        Texture manaTexture = new Texture("items/mana.png");
        Image manaIcon = new Image(manaTexture);
        manaIcon.setSize(40, 40);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.getData().setScale(2f);

        manaLabel = new Label("", labelStyle);

        manaTable.add(manaIcon).size(40, 40).padRight(10);
        manaTable.add(manaLabel);
        stage.addActor(manaTable);
    }

    private void addTurnDisplay() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.getData().setScale(2f);

        turnLabel = new Label("", labelStyle);
        turnLabel.setPosition(50, HEIGHT - 100);
        stage.addActor(turnLabel);
    }

    @Override
    public void draw() {
        // Afficher les ressources du joueur actif
        if (coinLabel != null) {
            coinLabel.setText("x" + (playerTurn == 1 ? P1.piece : P2.piece));
        }
        if (manaLabel != null) {
            manaLabel.setText("x" + (playerTurn == 1 ? P1.mana : P2.mana));
        }
        if (turnLabel != null) {
            turnLabel.setText("Tour " + TURN + " - Joueur " + playerTurn);
        }

        // Vérifier victoire
        if (WIN) {
            System.out.println("🏆 VICTOIRE DÉTECTÉE ! Changement vers WinMenu...");
            controller.goWin();
            return;
        }

        super.draw();
    }
}
