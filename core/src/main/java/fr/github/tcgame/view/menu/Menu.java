package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;


import java.util.Objects;
import java.util.function.Consumer;

import com.badlogic.gdx.utils.viewport.Viewport;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.model.card.Card;

import static fr.github.tcgame.view.MainGame.AUDIOSETTINGS;

//QUENTIN
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public abstract class Menu {
    public static final float WIDTH = 1920; // 1280
    public static final float HEIGHT = 1080; // 720

    private Texture mainTitleTexture;
    private Image mainTitle;

    protected Group overlay;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected Table root;
    protected MenuController controller;
    protected com.badlogic.gdx.scenes.scene2d.ui.Skin skin;

    //QUENTIN (permet de pas surcharger VRAM)
    protected final java.util.List<Texture> loadedTextures = new java.util.ArrayList<>();
    //Pour la bibliothèque
    protected Table cardsTable;
    protected ScrollPane cardsScrollPane;
    protected List<FileHandle> currentCardFiles = new ArrayList<>();
    protected final List<String> currentFamilyFilters = new ArrayList<>();

    protected float libraryCardWidth;
    protected float libraryCardHeight;
    protected float libraryGapX;
    protected float libraryGapY;
    protected int libraryCardsPerRow;

    public enum TypeMenu {SPLASH, MAIN, SELECTION, CARDLIST, SETTINGS, QUIT, GAME}
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
        this.mainTitleTexture=new Texture(Gdx.files.internal("background/main_title.png"));
        this.mainTitle=new Image(mainTitleTexture);

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
        //Quentin
        for (Texture texture : loadedTextures) {
            texture.dispose();
        }
        loadedTextures.clear();
        //
        stage.dispose();
        this.skin.dispose();
        if (mainTitleTexture != null) {
            mainTitleTexture.dispose();
        }
    }

    protected abstract void build();

    public void setBackground(String texturePath) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        Image bg = new Image(texture);
        bg.setFillParent(true);
        stage.addActor(bg);
        bg.toBack(); //J'ajoute ca pour que le background soit toujours au fond (Micka me frappe pas)
    }


    public void addMainTitle() {
        mainTitle.setPosition(WIDTH*0.005f, HEIGHT*0.05f);
        mainTitle.setSize(WIDTH,HEIGHT);
        stage.addActor(mainTitle);
    }

    public void addButton(String texturePath, String texturePathHover,
                          float x, float y, float size, Runnable action) {

        Texture normalTex = new Texture(Gdx.files.internal(texturePath));
        Texture hoverTex = new Texture(Gdx.files.internal(texturePathHover));

        TextureRegionDrawable normalDrawable = new TextureRegionDrawable(new TextureRegion(normalTex));
        TextureRegionDrawable hoverDrawable = new TextureRegionDrawable(new TextureRegion(hoverTex));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = normalDrawable;
        style.imageOver = hoverDrawable;

        ImageButton button = new ImageButton(style);

        button.setPosition(x, y);
        button.setSize(normalTex.getWidth() * size, normalTex.getHeight() * size);

        com.badlogic.gdx.audio.Sound sfx =
            Gdx.audio.newSound(Gdx.files.internal("sfx/placeholder_button.mp3"));

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            if (action != null) {
                sfx.play(AUDIOSETTINGS.getEffectiveSfxVolume());
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

    public void addSplash(Runnable action) {
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


    //QUENTIN
    //  METHODES BIBLIOTHEQUE

    public void addSearch(float x, float y, float width, Consumer<String> onSearch){
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();

        Texture texBg = new Texture(Gdx.files.internal("button/search/searchbar_bg_native.png"));
        Texture texFocused = new Texture(Gdx.files.internal("button/search/searchbar_bg_focused.png"));
        Texture texCursor = new Texture(Gdx.files.internal("button/search/cursor.png"));

        float height = texBg.getHeight();

        style.font = new BitmapFont(Gdx.files.internal("ui/PixelGameFont_24.fnt"));
        style.fontColor = com.badlogic.gdx.graphics.Color.WHITE;
        style.focusedFontColor= com.badlogic.gdx.graphics.Color.valueOf("C8A84B");

        style.background = new TextureRegionDrawable(new TextureRegion(texBg));
        style.focusedBackground = new TextureRegionDrawable(new TextureRegion(texFocused));

        style.cursor = new TextureRegionDrawable((new TextureRegion(texCursor)));
        style.selection = skin.newDrawable("white", new com.badlogic.gdx.graphics.Color(0.55f, 0.42f, 0.18f, 0.45f));

        TextField searchField = new TextField("", style);
        searchField.setMessageText("Rechercher une carte...");
        searchField.setPosition(x, y);
        searchField.setSize(width,height);

        searchField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (onSearch!=null){
                    onSearch.accept(searchField.getText().toLowerCase().trim());
                }
            }
        });

        stage.addActor(searchField);
        searchField.toFront();
    }



    public void addAllCards(String folderPath,
                            float x,
                            float y,
                            float viewportWidth,
                            float viewportHeight,
                            float cardWidth,
                            float cardHeight,
                            float gapX,
                            float gapY,
                            int cardsPerRow) {

        FileHandle rootFolder = Gdx.files.internal("assets/" + folderPath);

        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            System.out.println("Dossier de cartes introuvable : " + rootFolder.path());
            return;
        }

        currentCardFiles.clear();
        collectCardImages(rootFolder, currentCardFiles);
        currentCardFiles.sort(Comparator.comparing(FileHandle::path));

        libraryCardWidth = cardWidth;
        libraryCardHeight = cardHeight;
        libraryGapX = gapX;
        libraryGapY = gapY;
        libraryCardsPerRow = cardsPerRow;

        cardsTable = new Table();
        cardsTable.top().left();

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        cardsScrollPane = new ScrollPane(cardsTable, style);

        cardsScrollPane.setPosition(x, y);
        cardsScrollPane.setSize(viewportWidth, viewportHeight);
        cardsScrollPane.setScrollingDisabled(true, false);
        cardsScrollPane.setForceScroll(false, true);
        cardsScrollPane.setFadeScrollBars(false);
        cardsScrollPane.setOverscroll(false, false);
        cardsScrollPane.setScrollbarsVisible(true);

        stage.addActor(cardsScrollPane);
        cardsScrollPane.toFront();

        stage.setScrollFocus(cardsScrollPane);

        cardsScrollPane.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                stage.setScrollFocus(cardsScrollPane);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                cardsScrollPane.setScrollY(cardsScrollPane.getScrollY() + amountY * 80f);
                return true;
            }
        });

        refreshCards("");
    }

    private void collectCardImages(FileHandle folder, List<FileHandle> cardFiles) {
        for (FileHandle file : folder.list()) {
            if (file.isDirectory()) {
                collectCardImages(file, cardFiles);
            } else if (file.extension().equalsIgnoreCase("png")) {
                cardFiles.add(file);
            }
        }
    }

    public void refreshCards(String query) {
        if (cardsTable == null) {
            return;
        }

        cardsTable.clearChildren();

        String cleanQuery = query == null ? "" : query.toLowerCase().trim();

        int displayedCards = 0;

        for (FileHandle file : currentCardFiles) {
            String cardName = file.nameWithoutExtension().toLowerCase();
            String familyName = file.parent().name().toLowerCase();

            boolean matchesSearch = cleanQuery.isEmpty() || cardName.contains(cleanQuery);
            boolean matchesFamily = currentFamilyFilters.isEmpty() || currentFamilyFilters.contains(familyName);

            if (!matchesSearch || !matchesFamily) {
                continue;
            }

            Texture texture = new Texture(file);
            loadedTextures.add(texture);

            Image cardImage = new Image(texture);

            cardsTable.add(cardImage)
                .width(libraryCardWidth)
                .height(libraryCardHeight)
                .padRight(libraryGapX)
                .padBottom(libraryGapY);

            displayedCards++;

            if (displayedCards % libraryCardsPerRow == 0) {
                cardsTable.row();
            }
        }

        cardsTable.pack();

        if (cardsScrollPane != null) {
            cardsScrollPane.setScrollY(0);
            cardsScrollPane.layout();
        }
    }

    public void toggleFamilyFilter(String familyName) {
        if (familyName == null) {
            return;
        }

        String cleanFamilyName = familyName.toLowerCase().trim();

        if (currentFamilyFilters.contains(cleanFamilyName)) {
            currentFamilyFilters.remove(cleanFamilyName);
        } else {
            currentFamilyFilters.add(cleanFamilyName);
        }

        refreshCards("");
    }




}
