package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fr.github.tcgame.control.MenuController;

public class AudioMenu extends Menu {

    public AudioMenu(MenuController controller) {
        super(TypeMenu.AUDIO, controller);
    }

    @Override
    protected void build() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTex = new Texture(pixmap);
        pixmap.dispose();
        Image blackBg = new Image(blackTex);
        blackBg.setFillParent(true);
        stage.addActor(blackBg);

        setBackground("placeholder/BG_MainMenu.jpg");

        addLabel("AUDIO", WIDTH / 2f, HEIGHT * 0.78f, 56, OR_PALE);

        float labelX  = WIDTH / 2f - 250f;
        float sliderX = WIDTH / 2f - 100f;
        float startY  = HEIGHT * 0.58f;
        float gap     = HEIGHT * 0.10f;

        addSlider("VOLUME GENERAL",  labelX, sliderX, startY,          volumeMaster,  v -> { volumeMaster  = v; updateVolumes(); });
        addSlider("MUSIQUE",         labelX, sliderX, startY - gap,     volumeMusique, v -> { volumeMusique = v; updateVolumes(); });
        addSlider("EFFETS SONORES",  labelX, sliderX, startY - gap * 2, volumeEffets,  v -> { volumeEffets  = v; });

        addMenuOption("RETOUR", WIDTH / 2f, startY - gap * 3, controller::goParametres);

        keyIsPressed(Input.Keys.ESCAPE, controller::goParametres);
    }

    private void addSlider(String labelText, float labelX, float sliderX, float y, float initialValue, java.util.function.Consumer<Float> onChange) {
        // Label nom
        BitmapFont font = cinzel(16, OR_PALE);
        Label nameLabel = new Label(labelText, new Label.LabelStyle(font, OR_PALE));
        nameLabel.setPosition(labelX, y + 5f);
        stage.addActor(nameLabel);

        // Style slider
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();

        Pixmap bgPx = new Pixmap(300, 6, Pixmap.Format.RGBA8888);
        bgPx.setColor(GRIS_MENU);
        bgPx.fill();
        sliderStyle.background = new Image(new Texture(bgPx)).getDrawable();
        bgPx.dispose();

        Pixmap knobPx = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        knobPx.setColor(OR_HOVER);
        knobPx.fillCircle(8, 8, 8);
        sliderStyle.knob = new Image(new Texture(knobPx)).getDrawable();
        knobPx.dispose();

        Slider slider = new Slider(0f, 1f, 0.01f, false, sliderStyle);
        slider.setValue(initialValue);
        slider.setSize(300f, 20f);
        slider.setPosition(sliderX, y);
        stage.addActor(slider);

        // Pourcentage
        BitmapFont fontPct = cinzel(14, GRIS_MENU);
        Label pctLabel = new Label((int)(initialValue * 100) + "%", new Label.LabelStyle(fontPct, GRIS_MENU));
        pctLabel.setPosition(sliderX + 315f, y + 2f);
        stage.addActor(pctLabel);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float val = slider.getValue();
                pctLabel.setText((int)(val * 100) + "%");
                if (onChange != null) onChange.accept(val);
            }
        });
    }
}
