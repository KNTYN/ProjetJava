package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import fr.github.tcgame.control.MenuController;
import java.util.function.Consumer;

public abstract class Menu {
    public static final float WIDTH  = 1280;
    public static final float HEIGHT = 720;

    protected Group overlay;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected Table root;
    protected MenuController controller;

    public enum TypeMenu {SPLASH, MAIN, SELECTION, QUIT, JOUER, PARAMETRES, AUDIO, CONTROLES, COLLECTION}
    public TypeMenu typeMenu;

    protected static final Color OR_PALE   = new Color(0.76f, 0.67f, 0.49f, 1f);
    protected static final Color GRIS_MENU = new Color(0.63f, 0.63f, 0.63f, 1f);
    protected static final Color OR_HOVER  = new Color(0.95f, 0.82f, 0.40f, 1f);

    protected static Sound clickSound;
    protected static Music ostMain;

    protected static float volumeMaster  = 1f;
    protected static float volumeMusique = 1f;
    protected static float volumeEffets  = 1f;

    public Menu(TypeMenu typeMenu, MenuController controller) {
        this.typeMenu   = typeMenu;
        this.controller = controller;
        camera   = new OrthographicCamera();
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        stage    = new Stage(viewport);
        root     = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        overlay = new Group();
        overlay.setVisible(false);
        stage.addActor(overlay);

        if (clickSound == null)
            clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click.mp3"));
        if (ostMain == null) {
            ostMain = Gdx.audio.newMusic(Gdx.files.internal("audio/ost_main.mp3"));
            ostMain.setLooping(true);
            ostMain.setVolume(volumeMaster * volumeMusique);
            ostMain.play();
        }

        build();
    }

    protected static void updateVolumes() {
        if (ostMain != null) ostMain.setVolume(volumeMaster * volumeMusique);
    }

    protected BitmapFont cinzel(int size, Color color) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/Cinzel-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size      = size;
        param.color     = color;
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = gen.generateFont(param);
        gen.dispose();
        return font;
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

    public void dispose() { stage.dispose(); }

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
        button.setSize(texture.getWidth() * size, texture.getHeight() * size);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action != null) action.run();
            }
        });
        stage.addActor(button);
    }

    public void addLabel(String text, float x, float y, int size, Color color) {
        BitmapFont font = cinzel(size, color);
        Label.LabelStyle style = new Label.LabelStyle(font, color);
        Label label = new Label(text, style);
        label.setPosition(x - label.getPrefWidth() / 2f, y);
        stage.addActor(label);
    }

    private Texture makeGlowBar() {
        int w = 4, h = 40;
        Pixmap px = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        for (int y = 0; y < h; y++) {
            float alpha = 1f - Math.abs((y - h / 2f) / (h / 2f));
            px.setColor(1f, 0.82f, 0.3f, alpha * 0.9f);
            px.drawLine(0, y, w - 1, y);
        }
        Texture t = new Texture(px);
        px.dispose();
        return t;
    }

    public void addMenuOption(String text, float x, float y, Runnable action) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/Cinzel-Regular.ttf")
        );

        FreeTypeFontGenerator.FreeTypeFontParameter paramNormal = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramNormal.size      = 22;
        paramNormal.color     = GRIS_MENU;
        paramNormal.minFilter = Texture.TextureFilter.Linear;
        paramNormal.magFilter = Texture.TextureFilter.Linear;
        BitmapFont fontNormal = gen.generateFont(paramNormal);

        FreeTypeFontGenerator.FreeTypeFontParameter paramHover = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramHover.size      = 22;
        paramHover.color     = OR_HOVER;
        paramHover.minFilter = Texture.TextureFilter.Linear;
        paramHover.magFilter = Texture.TextureFilter.Linear;
        BitmapFont fontHover = gen.generateFont(paramHover);

        gen.dispose();

        Label.LabelStyle normalStyle = new Label.LabelStyle(fontNormal, GRIS_MENU);
        Label.LabelStyle hoverStyle  = new Label.LabelStyle(fontHover, OR_HOVER);

        Label label = new Label(text, normalStyle);
        float labelW = label.getPrefWidth();
        float labelX = x - labelW / 2f;
        label.setPosition(labelX, y);
        label.setSize(labelW, label.getPrefHeight());

        Texture barTex = makeGlowBar();
        Image bar = new Image(barTex);
        float barH = 36f, barW = 4f;
        bar.setSize(barW, barH);
        bar.setPosition(labelX - 18f, y + label.getPrefHeight() / 2f - barH / 2f);
        bar.setVisible(false);
        stage.addActor(bar);

        label.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float mx, float my, int pointer, Actor fromActor) {
                label.setStyle(hoverStyle);
                bar.setVisible(true);
            }
            @Override
            public void exit(InputEvent event, float mx, float my, int pointer, Actor toActor) {
                label.setStyle(normalStyle);
                bar.setVisible(false);
            }
            @Override
            public void clicked(InputEvent event, float mx, float my) {
                if (action != null) action.run();
            }
        });

        stage.addActor(label);
    }

    public void keyIsPressed(int key, Runnable action) {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == key) { action.run(); return true; }
                return false;
            }
        });
    }

    public void addSearchBar(int maxChar, float x, float y, float width, float height, Consumer<String> onChange) {
        TextField field = new TextField("",
            new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("ui/uiskin.json")));
        field.setSize(width, height);
        field.setPosition(x, y);
        field.setMaxLength(maxChar);
        field.setTextFieldListener((textField, c) -> {
            if (onChange != null) onChange.accept(textField.getText());
        });
        stage.addActor(field);
    }
}
