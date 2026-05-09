package fr.github.tcgame.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.model.GameModel;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.player.Player;
import fr.github.tcgame.view.menu.Menu;

import java.util.ArrayList;
import java.util.List;

public class GameView extends Menu {

    private GameModel gameModel;
    private List<Card> alreadyAttacked = new ArrayList<>();
    private Card selectedCard = null;
    private String actionMode = "";
    private boolean useSpecialAttack = false;
    private String infoText = "";
    private String logText = "";
    private BitmapFont font;
    private boolean gameOver = false;
    private Texture gearTex;

    public GameView(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        font = cinzel(11, Color.WHITE);
        gearTex = new Texture(Gdx.files.internal("ui/gear.png"));
        gameModel = new GameModel("Joueur", "IA");
        gameModel.init();
        gameModel.startTurn();
        infoText = "Tour " + gameModel.getTurnNumber() + " - Choisissez une action";
        logText = "Partie commencee";
        refreshAll();
    }

    private void refreshAll() {
        stage.clear();
        drawBackground();
        drawGear();
        drawTopBar();
        drawLeftPanel();
        drawIAZone();
        drawPlayerZone();
        drawLog();
        if (gameOver) drawGameOver();
    }

    private void drawBackground() {
        Pixmap bg = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bg.setColor(0.08f, 0.08f, 0.12f, 1f); bg.fill();
        Image bgImg = new Image(new Texture(bg)); bg.dispose();
        bgImg.setFillParent(true); stage.addActor(bgImg);
    }

    private void drawGear() {
        Image gear = new Image(gearTex);
        gear.setSize(35, 35); gear.setPosition(10, HEIGHT - 45);
        gear.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { controller.goPause(); }
        });
        stage.addActor(gear);
    }

    private void drawTopBar() {
        Player p1 = gameModel.getPlayer1();
        Player p2 = gameModel.getPlayer2();
        addText("IA  Cristal: " + p2.getCrystal().getCurrentHp() + "/" + GameModel.CRISTAL_MAX_HP + "  Mana: " + p2.getMana() + "/" + Player.MAX_MANA + "  Main: " + p2.getHand().size(), WIDTH - 400, HEIGHT - 25, Color.RED);
        addText("TOUR " + gameModel.getTurnNumber() + " (+" + gameModel.getCurrentManaGain() + " mana) | Pioche: " + gameModel.getDeck().size(), WIDTH/2f - 100, HEIGHT - 25, Color.GOLD);
        Color mc = infoText.contains("impossible") || infoText.contains("Pas assez") ? Color.RED : (infoText.contains("OK") || infoText.contains("active") ? Color.GREEN : Color.YELLOW);
        addText(infoText, WIDTH/2f - 100, HEIGHT - 50, mc);
    }

    private void drawLeftPanel() {
        float x = 10, y = HEIGHT - 100, gap = 42;
        Player p1 = gameModel.getPlayer1();
        addLeftButton("DEPLOYER", x, y, () -> setMode("deploy", "Cliquez sur une carte de votre main"));
        addLeftButton("ACTIVER", x, y - gap, () -> setMode("activate", "Cliquez sur une carte * de votre banc"));
        addLeftButton("ECHANGER", x, y - gap*2, () -> setMode("swap_field", "Cliquez sur une carte du terrain"));
        addLeftButton("ATK NORMALE", x, y - gap*3, () -> setMode("chooseAttacker", "ATTAQUE NORMALE - Cliquez sur votre attaquant", false));
        addLeftButton("ATK SPECIALE", x, y - gap*4, () -> setMode("chooseAttacker", "ATTAQUE SPECIALE - Cliquez sur votre attaquant", true));
        addLeftButton("SORT NORMAL", x, y - gap*5, () -> setMode("spell", "SORT NORMAL - Cliquez sur un sort dans votre main", false));
        addLeftButton("SORT SPECIAL", x, y - gap*6, () -> setMode("spell", "SORT SPECIAL - Cliquez sur un sort dans votre main", true));
        addLeftButton("DEFAUSSER", x, y - gap*7, () -> setMode("discard", "Cliquez sur une carte a defausser"));
        addLeftButton("PASSER", x, y - gap*8, () -> endTurn());
        addText("Cristal: " + p1.getCrystal().getCurrentHp() + "/" + GameModel.CRISTAL_MAX_HP, x + 5, 80, Color.GREEN);
        addText("Mana: " + p1.getMana() + "/" + Player.MAX_MANA, x + 5, 60, Color.CYAN);
    }

    private void addLeftButton(String text, float x, float y, Runnable action) {
        float w = 135, h = 36;
        Pixmap px = new Pixmap((int)w, (int)h, Pixmap.Format.RGBA8888);
        px.setColor(new Color(0.15f, 0.15f, 0.25f, 1f)); px.fill();
        px.setColor(Color.GRAY); px.drawRectangle(0, 0, (int)w, (int)h);
        Image btn = new Image(new Texture(px)); px.dispose();
        btn.setPosition(x, y); btn.setSize(w, h); stage.addActor(btn);
        Label lbl = new Label(text, new Label.LabelStyle(font, Color.WHITE));
        lbl.setPosition(x + 5, y + 9); stage.addActor(lbl);
        btn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float mx, float my) { action.run(); }
        });
    }

    private void drawIAZone() {
        addText("TERRAIN IA", 180, HEIGHT - 80, Color.LIGHT_GRAY);
        drawCardSlots(gameModel.getPlayer2().getFieldCards(), 180, HEIGHT - 220, 6, false);
        addText("BANC IA", 180, HEIGHT - 260, Color.LIGHT_GRAY);
        drawCardSlots(gameModel.getPlayer2().getBench(), 180, HEIGHT - 320, 3, false);
        Pixmap line = new Pixmap((int)WIDTH, 2, Pixmap.Format.RGBA8888);
        line.setColor(Color.DARK_GRAY); line.fill();
        Image li = new Image(new Texture(line)); line.dispose();
        li.setPosition(0, HEIGHT/2f + 20); li.setSize(WIDTH, 2); stage.addActor(li);
    }

    private void drawPlayerZone() {
        Player p1 = gameModel.getPlayer1();
        addText("TERRAIN VOUS", 180, HEIGHT/2f - 20, Color.LIGHT_GRAY);
        drawCardSlots(p1.getFieldCards(), 180, HEIGHT/2f - 160, 6, true);
        addText("BANC VOUS", 180, 140, Color.LIGHT_GRAY);
        drawCardSlots(p1.getBench(), 180, 80, 3, true);
        addText("MAIN (" + p1.getHand().size() + "/" + GameModel.MAX_HAND_SIZE + ")", 180, 55, Color.LIGHT_GRAY);
        drawCardSlots(p1.getHand(), 180, 10, 7, true);
    }

    private void drawCardSlots(List<Card> cards, float x, float y, int maxSlots, boolean isPlayer) {
        float sw = 90, sh = 55, gap = 6;
        for (int i = 0; i < maxSlots; i++) {
            float sx = x + i * (sw + gap);
            Pixmap sb = new Pixmap((int)sw, (int)sh, Pixmap.Format.RGBA8888);
            sb.setColor(new Color(0.1f, 0.1f, 0.15f, 0.8f)); sb.fill();
            sb.setColor(Color.DARK_GRAY); sb.drawRectangle(0, 0, (int)sw, (int)sh);
            Image si = new Image(new Texture(sb)); sb.dispose();
            si.setPosition(sx, y); si.setSize(sw, sh); stage.addActor(si);

            if (i < cards.size()) {
                Card c = cards.get(i);
                boolean sel = (c == selectedCard);
                Pixmap cb = new Pixmap((int)sw-4, (int)sh-4, Pixmap.Format.RGBA8888);
                cb.setColor(sel ? new Color(0.4f, 0.35f, 0.1f, 1f) : new Color(0.15f, 0.12f, 0.08f, 1f)); cb.fill();
                cb.setColor(sel ? Color.GOLD : Color.GRAY); cb.drawRectangle(0, 0, (int)sw-4, (int)sh-4);
                Image ci = new Image(new Texture(cb)); cb.dispose();
                ci.setPosition(sx+2, y+2); ci.setSize(sw-4, sh-4); stage.addActor(ci);

                addText(c.getName(), sx+4, y+34, Color.WHITE);
                if (isPlayer && c.getHp() > 0) addText(c.getCurrentHp()+"/"+c.getHp(), sx+4, y+18, c.isBelowHalfHp()?Color.RED:Color.GREEN);
                if (isPlayer && c.getCost() > 0) addText("Mana:"+c.getCost(), sx+sw-50, y+4, Color.CYAN);
                if (isPlayer && alreadyAttacked != null && alreadyAttacked.contains(c)) addText("X", sx+sw/2f-5, y+sh/2f-8, Color.RED);
                if (isPlayer && gameModel.getPlayer1().getBench().contains(c) && gameModel.getPlayer1().isNewlyDeployed(c)) addText("(attente)", sx+4, y+4, Color.ORANGE);

                final Card card = c;
                ci.addListener(new ClickListener() {
                    @Override public void clicked(InputEvent e, float mx, float my) { handleClick(card); }
                });
            }
        }
    }

    private void drawLog() {
        addText("LOG: " + logText, WIDTH - 350, 10, Color.LIGHT_GRAY);
    }

    // ==================== LOGIQUE ====================

    private void setMode(String mode, String msg) { setMode(mode, msg, false); }
    private void setMode(String mode, String msg, boolean special) {
        actionMode = mode; selectedCard = null; infoText = msg;
        if (mode.equals("chooseAttacker") || mode.equals("spell")) useSpecialAttack = special;
        refreshAll();
    }

    private void handleClick(Card card) {
        Player p1 = gameModel.getPlayer1();
        Player p2 = gameModel.getPlayer2();
        boolean isHand = p1.getHand().contains(card);
        boolean isBench = p1.getBench().contains(card);
        boolean isField = p1.getFieldCards().contains(card);
        boolean isEnemy = p2.getFieldCards().contains(card);

        switch (actionMode) {
            case "deploy":
                if (isHand && p1.getBench().size() < Player.MAX_BENCH_SIZE && p1.getMana() >= card.getCost()) {
                    gameModel.deployToBench(p1, card);
                    logText = card.getName() + " deployee"; infoText = "OK";
                } else infoText = "Impossible";
                actionMode = ""; selectedCard = null; break;

            case "activate":
                if (isBench && !p1.isNewlyDeployed(card) && p1.getFieldCards().size() < Player.MAX_FIELD_SIZE) {
                    gameModel.playFromBenchToField(p1, card);
                    logText = card.getName() + " activee"; infoText = "OK";
                } else infoText = "Impossible";
                actionMode = ""; selectedCard = null; break;

            case "swap_field":
                if (isField) { selectedCard = card; actionMode = "swap_bench"; infoText = "Cliquez sur une carte du banc"; }
                break;
            case "swap_bench":
                if (isBench && selectedCard != null && !p1.isNewlyDeployed(card) && p1.getMana() >= 2) {
                    gameModel.swapCard(p1, selectedCard, card);
                    logText = "Echange: " + selectedCard.getName() + " <-> " + card.getName(); infoText = "OK";
                } else infoText = "Impossible";
                actionMode = ""; selectedCard = null; break;

            case "chooseAttacker":
                if (isField && !alreadyAttacked.contains(card)) {
                    int cost = useSpecialAttack ? card.getSpecialAtk().getCostMana() : card.getNormalAtk().getCostMana();
                    if (p1.getMana() >= cost) {
                        selectedCard = card; actionMode = "chooseTarget";
                        infoText = "Cliquez sur une cible ennemie";
                    } else { infoText = "Pas assez de mana !"; actionMode = ""; }
                }
                break;

            case "chooseTarget":
                if (selectedCard != null && (isEnemy || p2.getFieldCards().isEmpty())) {
                    String result = gameModel.resolveAttack(p1, selectedCard, isEnemy ? card : null, useSpecialAttack);
                    alreadyAttacked.add(selectedCard);
                    logText = result; infoText = "Attaque effectuee !";
                    actionMode = ""; selectedCard = null; useSpecialAttack = false;
                }
                break;

            case "spell":
                if (isHand && (card.getFamily() == Card.Family.SORT || card.getType() == Card.Type.EVENT)) {
                    String result = gameModel.resolveSpell(p1, card, useSpecialAttack, null);
                    logText = result;
                    infoText = (useSpecialAttack ? "Sort special lance !" : "Sort normal lance !");
                    actionMode = ""; useSpecialAttack = false;
                }
                break;

            case "discard":
                if (isHand) {
                    gameModel.discardFromHand(p1, 0);
                    logText = "Carte defaussee"; infoText = "OK";
                    actionMode = "";
                }
                break;
        }

        gameModel.checkEndCondition();
        if (gameModel.isOver()) { gameOver = true; logText = "Partie terminee !"; }
        refreshAll();
    }

    // ==================== FIN DE TOUR ====================

    private void endTurn() {
        alreadyAttacked.clear(); actionMode = ""; selectedCard = null;
        Player ia = gameModel.getPlayer2();
        for (Card c : new ArrayList<>(ia.getHand())) {
            if (c.getFamily() != Card.Family.SORT && ia.getBench().size() < Player.MAX_BENCH_SIZE && ia.getMana() >= c.getCost())
            { gameModel.deployToBench(ia, c); break; }
        }
        for (Card c : new ArrayList<>(ia.getBench())) {
            if (!ia.isNewlyDeployed(c) && ia.getFieldCards().size() < Player.MAX_FIELD_SIZE)
                gameModel.playFromBenchToField(ia, c);
        }
        for (Card a : ia.getFieldCards()) {
            if (ia.getMana() >= a.getNormalAtk().getCostMana()) gameModel.resolveAttackRandom(ia, new java.util.Random().nextBoolean());
            if (gameModel.isOver()) break;
        }
        gameModel.checkEndCondition();
        if (gameModel.isOver()) { gameOver = true; logText = "Partie terminee !"; }
        else gameModel.startTurn();
        infoText = "Tour " + gameModel.getTurnNumber() + " - A vous !";
        logText = "Debut du tour " + gameModel.getTurnNumber();
        refreshAll();
    }

    // ==================== FIN DE PARTIE ====================

    private void drawGameOver() {
        Pixmap ov = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        ov.setColor(0f, 0f, 0f, 0.7f); ov.fill();
        Image oi = new Image(new Texture(ov)); ov.dispose();
        oi.setFillParent(true); stage.addActor(oi);
        boolean v = gameModel.getWinner() == gameModel.getPlayer1();
        String t = v ? "VICTOIRE !" : "DEFAITE...";
        Color c = v ? new Color(0.95f, 0.82f, 0.40f, 1f) : Color.RED;
        Label vl = new Label(t, new Label.LabelStyle(cinzel(72, c), c));
        vl.setPosition(WIDTH/2f - 180, HEIGHT/2f); stage.addActor(vl);
        addTempButton("RETOUR AU MENU", WIDTH/2f - 110, HEIGHT/2f - 80, () -> { playMenuOst(); controller.goMain(); });
    }

    private void addTempButton(String text, float x, float y, Runnable action) {
        Pixmap px = new Pixmap(220, 35, Pixmap.Format.RGBA8888);
        px.setColor(new Color(0.2f, 0.3f, 0.2f, 1f)); px.fill();
        px.setColor(Color.GREEN); px.drawRectangle(0, 0, 220, 35);
        Image btn = new Image(new Texture(px)); px.dispose();
        btn.setPosition(x, y); btn.setSize(220, 35); stage.addActor(btn);
        Label lbl = new Label(text, new Label.LabelStyle(font, Color.WHITE));
        lbl.setPosition(x + 5, y + 10); stage.addActor(lbl);
        btn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float mx, float my) { action.run(); }
        });
    }

    private void addText(String text, float x, float y, Color color) {
        Label lbl = new Label(text, new Label.LabelStyle(font, color));
        lbl.setPosition(x, y); stage.addActor(lbl);
    }

    @Override public void dispose() { super.dispose(); if (font != null) font.dispose(); if (gearTex != null) gearTex.dispose(); }
}
