package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.badlogic.gdx.utils.viewport.Viewport;
import fr.github.tcgame.control.MenuController;


public abstract class Menu {
    public static final float WIDTH = 1280;
    public static final float HEIGHT = 720;

    protected Group overlay;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected Table root;
    protected MenuController controller;
    private Map<String, Music> musics = new HashMap<>();

    public enum TypeMenu {MAIN, SELECTION, QUIT}
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
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action != null) { action.run(); }
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

    public void addSearchBar(int maxChar,float x, float y, float width, float height, Consumer<String> onChange) {
        TextField field = new TextField("",
                new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("ui/uiskin.json")));

        field.setSize(width, height);
        field.setPosition(x,y);
        field.setMaxLength(maxChar);
        field.setTextFieldListener(
        (textField, c) -> {
            if (onChange != null) {
                onChange.accept(textField.getText());
            }
        });

        stage.addActor(field);
    }

}
