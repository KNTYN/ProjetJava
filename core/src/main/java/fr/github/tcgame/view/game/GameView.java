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
import com.badlogic.gdx.utils.Align;

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
    private Card selectedAttacker = null;
    private int selectedAttackType = 1; // 1=normale, 2=spéciale
    private String message = "";
    private boolean waitingForTarget = false;

    public GameView(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        playGameOst();
        setBackground("placeholder/BG_GameView.png");

        gameModel = new GameModel("Joueur", "IA");
        gameModel.init();
        gameModel.startTurn();

        buildUI();
        refreshDisplay();
    }

    // ==================== CONSTRUCTION UI ====================

    private void buildUI() {
        // Boutons d'action (colonne de gauche)
        addButton("1. Déployer", 20, HEIGHT - 120, () -> deployFromHand());
        addButton("2. Activer", 20, HEIGHT - 165, () -> activateFromBench());
        addButton("3. Échanger", 20, HEIGHT - 210, () -> swapCards());
        addButton("4. Attaquer", 20, HEIGHT - 255, () -> attackMode());
        addButton("5. Sort", 20, HEIGHT - 300, () -> spellMode());
        addButton("6. Défausser", 20, HEIGHT - 345, () -> discardMode());
        addButton("7. Passer tour", 20, HEIGHT - 390, () -> endTurn());

        // Message
        message = "Tour 1 - Choisissez une action";
    }

    private void addButton(String text, float x, float y, Runnable action) {
        Pixmap px = new Pixmap(150, 35, Pixmap.Format.RGBA8888);
        px.setColor(0.2f, 0.2f, 0.3f, 1f);
        px.fill();
        px.setColor(Color.GOLD);
        px.drawRectangle(0, 0, 150, 35);
        Image btn = new Image(new Texture(px));
        px.dispose();
        btn.setPosition(x, y);
        btn.setSize(150, 35);
        stage.addActor(btn);

        Label lbl = new Label(text, new Label.LabelStyle(cinzel(14, Color.WHITE), Color.WHITE));
        lbl.setPosition(x + 10, y + 7);
        stage.addActor(lbl);

        btn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float mx, float my) { action.run(); }
        });
    }

    // ==================== AFFICHAGE ====================

    private void refreshDisplay() {
        // Supprimer anciennes cartes
        for (var a : stage.getActors()) {
            if (a.getName() != null && a.getName().startsWith("dc_")) a.remove();
        }

        if (gameModel == null || gameModel.isOver()) return;

        Player p1 = gameModel.getPlayer1();
        Player p2 = gameModel.getPlayer2();

        // Titres
        addText("=== IA ===   💎" + p2.getCrystal().getCurrentHp() + "   🔵" + p2.getMana(), 200, HEIGHT - 40, Color.RED);
        addText("=== VOUS === 💎" + p1.getCrystal().getCurrentHp() + "   🔵" + p1.getMana(), 200, HEIGHT/2f + 60, Color.GREEN);
        addText("Tour " + gameModel.getTurnNumber() + " (+" + gameModel.getCurrentManaGain() + " mana) | Pioche: " + gameModel.getDeck().size(), WIDTH/2f, HEIGHT - 20, Color.GOLD);
        addText(message, WIDTH/2f, HEIGHT - 60, Color.YELLOW);

        // Terrain IA
        addText("Terrain IA:", 200, HEIGHT - 80, Color.LIGHT_GRAY);
        drawCardRow(p2.getFieldCards(), 200, HEIGHT - 280, false);

        // Banc IA
        addText("Banc IA:", 200, HEIGHT - 300, Color.LIGHT_GRAY);
        drawCardRow(p2.getBench(), 200, HEIGHT - 380, false);

        // Terrain Joueur
        addText("Terrain VOUS:", 200, HEIGHT/2f + 20, Color.LIGHT_GRAY);
        drawCardRow(p1.getFieldCards(), 200, HEIGHT/2f - 180, true);

        // Banc Joueur
        addText("Banc VOUS:", 200, HEIGHT/2f - 200, Color.LIGHT_GRAY);
        drawCardRow(p1.getBench(), 200, HEIGHT/2f - 280, true);

        // Main Joueur
        addText("Main (" + p1.getHand().size() + "/" + GameModel.MAX_HAND_SIZE + "):", 200, 120, Color.LIGHT_GRAY);
        drawCardRow(p1.getHand(), 200, 20, true);

        // Message
        addText(message, WIDTH/2f, HEIGHT - 60, Color.YELLOW);
    }

    private void drawCardRow(List<Card> cards, float x, float y, boolean showInfo) {
        float cw = 80, ch = 50, pad = 8;
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            float cx = x + i * (cw + pad);

            // Fond carte
            Pixmap px = new Pixmap((int)cw, (int)ch, Pixmap.Format.RGBA8888);
            px.setColor(c == selectedAttacker ? Color.GOLD : new Color(0.1f, 0.1f, 0.15f, 1f));
            px.fill();
            px.setColor(Color.WHITE);
            px.drawRectangle(0, 0, (int)cw, (int)ch);
            Image cardImg = new Image(new Texture(px));
            px.dispose();
            cardImg.setName("dc_card");
            cardImg.setPosition(cx, y);
            cardImg.setSize(cw, ch);
            stage.addActor(cardImg);

            // Nom
            Label name = new Label(c.getName(), new Label.LabelStyle(cinzel(9, Color.WHITE), Color.WHITE));
            name.setName("dc_lbl");
            name.setPosition(cx + 3, y + ch - 16);
            stage.addActor(name);

            if (showInfo && c.getHp() > 0) {
                Label hp = new Label(c.getCurrentHp() + "/" + c.getHp(), new Label.LabelStyle(cinzel(9, c.isBelowHalfHp() ? Color.RED : Color.GREEN), c.isBelowHalfHp() ? Color.RED : Color.GREEN));
                hp.setName("dc_lbl");
                hp.setPosition(cx + 3, y + ch - 32);
                stage.addActor(hp);
            }

            // Clic sur les cartes du joueur
            final Card card = c;
            final boolean isHand = cards == gameModel.getPlayer1().getHand();
            final boolean isField = gameModel.getPlayer1().getFieldCards().contains(c);
            final boolean isBench = gameModel.getPlayer1().getBench().contains(c);

            cardImg.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float mx, float my) {
                    if (waitingForTarget && gameModel.getOpponent(gameModel.getPlayer1()).getFieldCards().contains(card)) {
                        // C'est une cible ennemie
                        doAttack(selectedAttacker, card);
                        waitingForTarget = false;
                        selectedAttacker = null;
                        refreshDisplay();
                    } else if (waitingForTarget && card == null) {
                        // Attaque cristal
                        doAttack(selectedAttacker, null);
                        waitingForTarget = false;
                        selectedAttacker = null;
                        refreshDisplay();
                    }
                }
            });
        }
    }

    private void addText(String text, float x, float y, Color color) {
        Label lbl = new Label(text, new Label.LabelStyle(cinzel(12, color), color));
        lbl.setName("dc_lbl");
        lbl.setPosition(x, y);
        stage.addActor(lbl);
    }

    // ==================== ACTIONS ====================

    private void deployFromHand() {
        Player p = gameModel.getPlayer1();
        if (p.getHand().isEmpty()) { message = "Main vide !"; refreshDisplay(); return; }
        if (p.getBench().size() >= Player.MAX_BENCH_SIZE) { message = "Banc plein !"; refreshDisplay(); return; }

        // Déployer la première carte jouable
        for (Card c : p.getHand()) {
            if (p.getMana() >= c.getCost()) {
                gameModel.deployToBench(p, c);
                message = c.getName() + " déployé sur le banc";
                saveState();
                refreshDisplay();
                return;
            }
        }
        message = "Pas assez de mana !";
        refreshDisplay();
    }

    private void activateFromBench() {
        Player p = gameModel.getPlayer1();
        if (p.getBench().isEmpty()) { message = "Banc vide !"; refreshDisplay(); return; }
        if (p.getFieldCards().size() >= Player.MAX_FIELD_SIZE) { message = "Terrain plein !"; refreshDisplay(); return; }

        for (Card c : p.getBench()) {
            if (!p.isNewlyDeployed(c)) {
                gameModel.playFromBenchToField(p, c);
                message = c.getName() + " activé sur le terrain";
                saveState();
                refreshDisplay();
                return;
            }
        }
        message = "Cartes en attente du prochain tour";
        refreshDisplay();
    }

    private void swapCards() {
        Player p = gameModel.getPlayer1();
        if (p.getMana() < 2) { message = "Pas assez de mana (2 requis)"; refreshDisplay(); return; }
        if (p.getFieldCards().isEmpty() || p.getBench().isEmpty()) { message = "Besoin d'une carte sur le terrain ET sur le banc"; refreshDisplay(); return; }

        for (Card bench : p.getBench()) {
            if (!p.isNewlyDeployed(bench)) {
                Card field = p.getFieldCards().get(0);
                gameModel.swapCard(p, field, bench);
                message = "Échange : " + field.getName() + " ↔ " + bench.getName();
                saveState();
                refreshDisplay();
                return;
            }
        }
        message = "Pas de carte échangeable sur le banc";
        refreshDisplay();
    }

    private void attackMode() {
        Player p = gameModel.getPlayer1();
        if (!p.hasActiveCard()) { message = "Pas de carte sur le terrain !"; refreshDisplay(); return; }

        // Choisir automatiquement le premier attaquant disponible
        for (Card c : p.getFieldCards()) {
            if (!alreadyAttacked.contains(c) && p.getMana() >= c.getNormalAtk().getCostMana()) {
                selectedAttacker = c;
                waitingForTarget = true;
                message = "Attaque avec " + c.getName() + " - Cliquez sur une cible ennemie";
                refreshDisplay();

                // Si pas de cible ennemie, attaque directe cristal
                if (gameModel.getOpponent(p).getFieldCards().isEmpty()) {
                    doAttack(c, null);
                    waitingForTarget = false;
                    selectedAttacker = null;
                }
                return;
            }
        }
        message = "Aucune carte ne peut attaquer !";
        refreshDisplay();
    }

    private void doAttack(Card attacker, Card target) {
        String result = gameModel.resolveAttack(gameModel.getPlayer1(), attacker, target, false);
        alreadyAttacked.add(attacker);
        message = result;
        saveState();
        gameModel.checkEndCondition();
        refreshDisplay();
    }

    private void spellMode() {
        Player p = gameModel.getPlayer1();
        for (Card c : p.getHand()) {
            if (c.getFamily() == Card.Family.SORT || c.getType() == Card.Type.EVENT) {
                if (p.getMana() >= c.getNormalAtk().getCostMana()) {
                    String result = gameModel.resolveSpell(p, c, false, null);
                    message = result;
                    saveState();
                    refreshDisplay();
                    return;
                }
            }
        }
        message = "Pas de sort jouable !";
        refreshDisplay();
    }

    private void discardMode() {
        Player p = gameModel.getPlayer1();
        if (!p.getHand().isEmpty()) {
            gameModel.discardFromHand(p, 0);
            message = "Carte défaussée";
            saveState();
        } else {
            message = "Main vide";
        }
        refreshDisplay();
    }

    private void endTurn() {
        alreadyAttacked.clear();
        selectedAttacker = null;
        waitingForTarget = false;

        // IA
        Player ia = gameModel.getPlayer2();
        for (Card c : new ArrayList<>(ia.getHand())) {
            if (c.getFamily() != Card.Family.SORT && ia.getBench().size() < Player.MAX_BENCH_SIZE && ia.getMana() >= c.getCost())
            { gameModel.deployToBench(ia, c); break; }
        }
        for (Card c : new ArrayList<>(ia.getBench())) {
            if (!ia.isNewlyDeployed(c) && ia.getFieldCards().size() < Player.MAX_FIELD_SIZE)
                gameModel.playFromBenchToField(ia, c);
        }
        for (Card attacker : ia.getFieldCards()) {
            if (ia.getMana() >= attacker.getNormalAtk().getCostMana())
                gameModel.resolveAttackRandom(ia, new java.util.Random().nextBoolean());
            if (gameModel.isOver()) break;
        }

        gameModel.checkEndCondition();
        if (!gameModel.isOver()) gameModel.startTurn();
        message = "Tour " + gameModel.getTurnNumber() + " - À vous de jouer !";
        saveState();
        refreshDisplay();
    }

    private void saveState() {
        // Sauvegarde JSON pour déco
        try {
            Player p1 = gameModel.getPlayer1();
            Player p2 = gameModel.getPlayer2();
            StringBuilder json = new StringBuilder();
            json.append("{\"turn\":").append(gameModel.getTurnNumber())
                .append(",\"p1_cristal\":").append(p1.getCrystal().getCurrentHp())
                .append(",\"p2_cristal\":").append(p2.getCrystal().getCurrentHp())
                .append(",\"gameover\":").append(gameModel.isOver()).append("}");
            java.io.FileWriter fw = new java.io.FileWriter("/tmp/tcgame_state.json");
            fw.write(json.toString());
            fw.close();
        } catch (Exception ex) {}
    }

    @Override
    public void draw() {
        super.draw();
        if (gameModel != null && gameModel.isOver()) {
            message = gameModel.getWinner() == gameModel.getPlayer1() ? "🎉 VICTOIRE ! 🎉" : "💀 DÉFAITE ! 💀";
            refreshDisplay();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
