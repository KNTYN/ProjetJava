package fr.github.tcgame.view.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CrystalView {

    private static CrystalView instance;
    private Stage stage;

    private Image crystalImageP1;
    private Image crystalImageP2;
    private Label crystalLabelP1;
    private Label crystalLabelP2;

    public CrystalView() {}

    public static CrystalView getInstance() {
        if (instance == null) instance = new CrystalView();
        return instance;
    }

    public void setStage(Stage stage) { this.stage = stage; }
    public Stage getStage() { return stage; }

    public void displayCrystal(int idP, int hp) {
        if (stage == null) {
            System.out.println("⚠️ Stage pas encore prêt, skip displayCrystal");
            return;
        }

        // Position selon le joueur
        float x = (idP == 1) ? 100 : 1300;
        float y = (idP == 1) ? 400 : 400;

        // Texture selon les HP (10 états : 100→90→...→0)
        int step = Math.min(9, (100 - hp) / 10); // 0 = plein, 9 = mort
        String path = "crystal/crystal__000" + step + "_crystal_1.png";

        // Image du cristal
        Image crystal = new Image(new TextureRegionDrawable(new Texture(path)));
        crystal.setPosition(x, y);
        crystal.setTouchable(Touchable.disabled);


        // Label HP
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.font.getData().setScale(1.5f);
        Label label = new Label(hp + " HP", style);
        label.setPosition(x + 10, y - 25);

        // Remplace l'ancien affichage
        if (idP == 1) {
            if (crystalImageP1 != null) crystalImageP1.remove();
            if (crystalLabelP1 != null) crystalLabelP1.remove();
            crystalImageP1 = crystal;
            crystalLabelP1 = label;
        } else {
            if (crystalImageP2 != null) crystalImageP2.remove();
            if (crystalLabelP2 != null) crystalLabelP2.remove();
            crystalImageP2 = crystal;
            crystalLabelP2 = label;
        }

        stage.addActor(crystal);
        stage.addActor(label);
    }
}
