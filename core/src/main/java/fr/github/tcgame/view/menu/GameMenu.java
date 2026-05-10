package fr.github.tcgame.view.menu;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import fr.github.tcgame.control.MenuController;


import static fr.github.tcgame.model.GameModel.P1;
import static fr.github.tcgame.view.MainGame.CARDV;


public class GameMenu extends Menu{
    private Label coinLabel;

    public GameMenu(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        setBackground("background/BG_game.png");
        addButton("background/bench.png", "background/bench.png", 300, 300, 1f,() -> {
            controller.moveCardToBench(1);
        });
        addButton("background/card_back.png", "background/card_back.png", 900, 300, 1f,() -> {
            controller.deployToActive(1);
        });

        addButton("button/attaque.png", "button/attaque_pressed.png", 1200, 300, 1f,() -> {

        }, ButtonMode.PRESSED);

        addCoinDisplay();
        CARDV.setStage(stage);

        keyIsPressed(Input.Keys.ESCAPE, () -> {
            System.out.println("Debug Touch");
        });
    }

    private void addCoinDisplay() {

        Table coinTable = new Table();
        coinTable.setPosition(50, 200);

        Texture coinTexture = new Texture("items/coin.png");
        Image coinIcon = new Image(coinTexture);
        coinIcon.setSize(40, 40);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(); // Police par défaut
        labelStyle.font.getData().setScale(2f);

        coinLabel = new Label("x" + P1.piece, labelStyle);

        coinTable.add(coinIcon).size(40, 40).padRight(10);
        coinTable.add(coinLabel);
        stage.addActor(coinTable);
    }

    @Override
    public void draw() {
        if (coinLabel != null) { coinLabel.setText("x" + P1.piece); }
        super.draw();
    }
}
