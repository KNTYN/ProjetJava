package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.badlogic.gdx.utils.viewport.Viewport;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.model.audio.AudioSettings;

import static fr.github.tcgame.view.MainGame.AUDIOSETTINGS;


public abstract class Menu {
    public static final float WIDTH = 1280;
    public static final float HEIGHT = 720;

    protected Group overlay;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected Table root;
    protected MenuController controller;
    protected com.badlogic.gdx.scenes.scene2d.ui.Skin skin;

    public enum TypeMenu {SPLASH, MAIN, SELECTION, CARDLIST, SETTINGS, QUIT}
    public TypeMenu typeMenu;

    public Menu(TypeMenu typeMenu, MenuController controller) {
        this.typeMenu = typeMenu;
        this.controller = controller;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WIDTH, HEIGHT, camera);

        stage = new Stage(viewport);
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);

        skin = new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("ui/uiskin.json"));

        overlay = new Group();
        overlay.setVisible(false);
        stage.addActor(overlay);

        build();
    }

    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public Stage getStage() { return stage; }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
    }

    public void dispose() {
        stage.dispose();
        this.skin.dispose();
    }

    protected abstract void build();

    public void setBackground(String texturePath) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        Image bg = new Image(texture);
        bg.setFillParent(true);
        stage.addActor(bg);
    }

    public void addButton(String texturePath, float x, float y, float size, Runnable action) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));

        button.setPosition(x, y);
        button.setSize(texture.getWidth()*size,texture.getHeight()*size);

        com.badlogic.gdx.audio.Sound sfx = Gdx.audio.newSound(Gdx.files.internal("sfx/placeholder_button.mp3"));

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action != null) {
                    sfx.play(AUDIOSETTINGS.getSfxVolume());
                    action.run();
                }
            }
        });

        stage.addActor(button);
    }

    public void keyIsPressed(int key, Runnable action) {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == key) {
                    action.run();
                    return true;
                }
                return false;
            }
        });
    }

    public void addSplashText(String text, float x, float y, float scale, Runnable action) {
        Label label = new Label(text,this.skin);
        label.setPosition(x, y);
        label.setFontScale(scale);
        label.setTouchable(Touchable.disabled);

        stage.addActor(label);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                action.run();
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                action.run();
                return true;
            }
        });
    }

    public void setBlackBackground() {
        Image bg = new Image(this.skin.newDrawable("white", com.badlogic.gdx.graphics.Color.BLACK));
        bg.setFillParent(true);
        stage.addActor(bg);
    }

    public void addSlider(float x, float y, float width, float initialValue, Consumer<Float> onChange) {
        Slider.SliderStyle style = new Slider.SliderStyle();

        Texture slice = new Texture(Gdx.files.internal("slider/bar_slider.png"));
        Texture point = new Texture(Gdx.files.internal("slider/point_slider.png"));

        style.background = new TextureRegionDrawable(new TextureRegion(slice));
        style.knob = new TextureRegionDrawable(new TextureRegion(point));

        Slider slider = new Slider(0f, 1f, 0.01f, false, style);

        slider.setPosition(x, y);
        slider.setSize(width, 20);
        slider.setValue(initialValue);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = slider.getValue();
                if (onChange != null) {
                    onChange.accept(value);
                }
            }
        });
        stage.addActor(slider);
    }

}
