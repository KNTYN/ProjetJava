package fr.github.tcgame.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.card.CardFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CollectionMenu extends Menu {

    private List<Card> allCards;
    private List<Card> filteredCards;
    private String searchText = "";
    private Card.Family selectedFamily = null;

    private static final Color CARD_BG    = new Color(0.12f, 0.10f, 0.08f, 0.95f);
    private static final Color CARD_BORDER = new Color(0.76f, 0.67f, 0.49f, 1f);
    private static final Color FILTRE_ON  = new Color(0.95f, 0.82f, 0.40f, 1f);
    private static final Color FILTRE_OFF = new Color(0.63f, 0.63f, 0.63f, 1f);

    public CollectionMenu(MenuController controller) {
        super(TypeMenu.COLLECTION, controller);
    }

    @Override
    protected void build() {
        // Fond noir
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTex = new Texture(pixmap);
        pixmap.dispose();
        Image blackBg = new Image(blackTex);
        blackBg.setFillParent(true);
        stage.addActor(blackBg);

        setBackground("placeholder/BG_MainMenu.jpg");

        // Charger les cartes
        allCards = CardFactory.createAllCards();
        filteredCards = allCards;

        // Cartes en premier (derrière)
        buildCards();

        // Titre par dessus
        addLabel("COLLECTION", WIDTH / 2f, HEIGHT - 55f, 48, OR_PALE);

        // Barre de recherche
        buildSearchBar();

        // Filtres familles
        buildFamilyFilters();

        // Bouton retour par dessus tout
        addMenuOption("RETOUR", 80f, 30f, controller::goMain);

        keyIsPressed(Input.Keys.ESCAPE, controller::goMain);
    }

    private void buildSearchBar() {
        BitmapFont font = cinzel(14, Color.WHITE);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font      = font;
        style.fontColor = Color.WHITE;

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(0.15f, 0.13f, 0.10f, 1f);
        px.fill();
        Texture bgTex = new Texture(px);
        px.dispose();
        style.background = new Image(bgTex).getDrawable();

        // Curseur
        Pixmap cursorPx = new Pixmap(2, 20, Pixmap.Format.RGBA8888);
        cursorPx.setColor(OR_PALE);
        cursorPx.fill();
        style.cursor = new Image(new Texture(cursorPx)).getDrawable();

        TextField searchBar = new TextField("", style);
        searchBar.setSize(280f, 30f);
        searchBar.setPosition(WIDTH / 2f - 140f, HEIGHT - 100f);
        searchBar.setMessageText("  Rechercher...");

        searchBar.setTextFieldListener((field, c) -> {
            searchText = field.getText().toLowerCase();
            refreshCards();
        });

        stage.addActor(searchBar);
    }

    private void buildFamilyFilters() {
        Card.Family[] families = Card.Family.values();
        float totalW = families.length * 130f;
        float startX = WIDTH / 2f - totalW / 2f;
        float y = HEIGHT - 130f;

        for (int i = 0; i < families.length; i++) {
            Card.Family family = families[i];
            float x = startX + i * 130f;
            addFamilyButton(family.name(), x, y, family);
        }
    }

    private void addFamilyButton(String text, float x, float y, Card.Family family) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/Cinzel-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 12;
        p.color = FILTRE_OFF;
        BitmapFont fontOff = gen.generateFont(p);
        p.color = FILTRE_ON;
        BitmapFont fontOn = gen.generateFont(p);
        gen.dispose();

        Label.LabelStyle styleOff = new Label.LabelStyle(fontOff, FILTRE_OFF);
        Label.LabelStyle styleOn  = new Label.LabelStyle(fontOn,  FILTRE_ON);

        Label label = new Label(text, styleOff);
        label.setPosition(x, y);

        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float mx, float my) {
                if (selectedFamily == family) {
                    selectedFamily = null;
                    label.setStyle(styleOff);
                } else {
                    selectedFamily = family;
                    label.setStyle(styleOn);
                }
                refreshCards();
            }
        });

        stage.addActor(label);
    }

    private void refreshCards() {
        filteredCards = allCards.stream()
            .filter(c -> searchText.isEmpty() || c.getName().toLowerCase().contains(searchText))
            .filter(c -> selectedFamily == null || c.getFamily() == selectedFamily)
            .collect(Collectors.toList());
        buildCards();
    }

    private void buildCards() {
        // Supprimer anciennes cartes
        com.badlogic.gdx.utils.Array<Actor> toRemove = new com.badlogic.gdx.utils.Array<>();
        for (Actor a : stage.getActors()) {
            if ("card".equals(a.getName())) toRemove.add(a);
        }
        for (Actor a : toRemove) a.remove();

        float cardW  = 190f;
        float cardH  = 130f;
        float padX   = 15f;
        float padY   = 15f;
        float startX = 30f;
        float startY = HEIGHT - 400f;
        int   cols   = 6;

        for (int i = 0; i < filteredCards.size(); i++) {
            Card card = filteredCards.get(i);
            int col = i % cols;
            int row = i / cols;
            float x = startX + col * (cardW + padX);
            float y = startY - row * (cardH + padY);
            buildCardActor(card, x, y, cardW, cardH);
        }
    }

    private void buildCardActor(Card card, float x, float y, float w, float h) {
        // Fond carte
        Pixmap px = new Pixmap((int) w, (int) h, Pixmap.Format.RGBA8888);
        px.setColor(CARD_BG);
        px.fill();
        px.setColor(CARD_BORDER);
        px.drawRectangle(0, 0, (int) w, (int) h);
        Image cardImg = new Image(new Texture(px));
        px.dispose();
        cardImg.setName("card");
        cardImg.setPosition(x, y);
        cardImg.setSize(w, h);
        stage.addActor(cardImg);

        Color typeColor = getTypeColor(card.getType());

        // Nom
        BitmapFont fontName = cinzel(12, typeColor);
        Label nameLabel = new Label(card.getName(), new Label.LabelStyle(fontName, typeColor));
        nameLabel.setName("card");
        nameLabel.setPosition(x + 8f, y + h - 20f);
        stage.addActor(nameLabel);

        // Famille
        BitmapFont fontInfo = cinzel(10, GRIS_MENU);
        Label.LabelStyle infoStyle = new Label.LabelStyle(fontInfo, GRIS_MENU);

        Label familyLabel = new Label(card.getFamily().name(), infoStyle);
        familyLabel.setName("card");
        familyLabel.setPosition(x + 8f, y + h - 36f);
        stage.addActor(familyLabel);

        if (card.getHp() > 0) {
            Label hpLabel = new Label("HP: " + card.getHp(), infoStyle);
            hpLabel.setName("card");
            hpLabel.setPosition(x + 8f, y + h - 52f);
            stage.addActor(hpLabel);

            Label coutLabel = new Label("Cout: " + card.getCost(), infoStyle);
            coutLabel.setName("card");
            coutLabel.setPosition(x + 8f, y + h - 68f);
            stage.addActor(coutLabel);
        } else {
            BitmapFont fontDesc = cinzel(9, GRIS_MENU);
            Label descLabel = new Label(card.getDescription(), new Label.LabelStyle(fontDesc, GRIS_MENU));
            descLabel.setName("card");
            descLabel.setWrap(true);
            descLabel.setSize(w - 16f, h - 40f);
            descLabel.setPosition(x + 8f, y + 8f);
            stage.addActor(descLabel);
        }

        // Clic pour détails
        cardImg.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float mx, float my) {
                showCardDetails(card);
            }
        });
    }

    private Color getTypeColor(Card.Type type) {
        return switch (type) {
            case LEGENDAIRE    -> new Color(1f, 0.84f, 0f, 1f);
            case RARE          -> new Color(0.4f, 0.6f, 1f, 1f);
            case TASTY_CROUSTY -> new Color(1f, 0.3f, 0.3f, 1f);
            case OBJET         -> new Color(0.6f, 0.9f, 0.6f, 1f);
            case EVENT         -> new Color(0.9f, 0.5f, 0.9f, 1f);
            default            -> new Color(0.85f, 0.85f, 0.85f, 1f);
        };
    }

    private void showCardDetails(Card card) {
        overlay.clearChildren();
        overlay.setVisible(true);

        Pixmap px = new Pixmap(500, 360, Pixmap.Format.RGBA8888);
        px.setColor(0.08f, 0.06f, 0.04f, 0.97f);
        px.fill();
        px.setColor(CARD_BORDER);
        px.drawRectangle(0, 0, 500, 360);
        Image bg = new Image(new Texture(px));
        px.dispose();
        bg.setSize(500, 360);
        bg.setPosition(WIDTH / 2f - 250f, HEIGHT / 2f - 180f);
        overlay.addActor(bg);

        float ox = WIDTH / 2f - 230f;
        float oy = HEIGHT / 2f + 145f;

        addOverlayLabel(card.getName(), ox, oy, 20, getTypeColor(card.getType()));
        addOverlayLabel(card.getFamily().name() + " — " + card.getType().name(), ox, oy - 30f, 12, GRIS_MENU);
        addOverlayLabel(card.getDescription(), ox, oy - 60f, 12, Color.WHITE);

        if (card.getHp() > 0) {
            addOverlayLabel("HP : "   + card.getHp(),   ox, oy - 100f, 13, Color.WHITE);
            addOverlayLabel("Cout : " + card.getCost(), ox, oy - 125f, 13, Color.WHITE);
            if (card.getNormalAtk() != null)
                addOverlayLabel("Attaque : " + card.getNormalAtk().getName(), ox, oy - 155f, 12, Color.WHITE);
            if (card.getSpecialAtk() != null)
                addOverlayLabel("Special : " + card.getSpecialAtk().getName(), ox, oy - 178f, 12, Color.WHITE);
        }

        BitmapFont font = cinzel(14, OR_HOVER);
        Label close = new Label("[ FERMER ]", new Label.LabelStyle(font, OR_HOVER));
        close.setPosition(WIDTH / 2f - 45f, HEIGHT / 2f - 160f);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float mx, float my) {
                overlay.setVisible(false);
            }
        });
        overlay.addActor(close);
    }

    private void addOverlayLabel(String text, float x, float y, int size, Color color) {
        BitmapFont font = cinzel(size, color);
        Label label = new Label(text, new Label.LabelStyle(font, color));
        label.setPosition(x, y);
        overlay.addActor(label);
    }
}
