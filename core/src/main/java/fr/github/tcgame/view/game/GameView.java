package fr.github.tcgame.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.github.tcgame.control.MenuController;
import fr.github.tcgame.model.GameModel;
import fr.github.tcgame.model.card.Card;
import fr.github.tcgame.model.player.Crystal;
import fr.github.tcgame.view.menu.Menu;

public class GameView extends Menu {

    private static final float COUNTDOWN_MAX = 10f;
    private static final float CARD_W        = 80f;
    private static final float CARD_H        = 112f;

    private Texture gearTex;
    private Texture crystalIATex;
    private Texture crystalJoueurTex;
    private Texture cardTex;

    private GameModel gameModel;

    private Label labelCristalJoueur;
    private Label labelCristalIA;
    private Label labelCarteJoueur;
    private Label labelCarteIA;
    private Label labelHpJoueur;
    private Label labelHpIA;
    private Label labelAtk1;
    private Label labelAtk2;
    private Label labelLog;
    private Label labelFinPartie;
    private Label labelCountdown;

    private boolean gameOver       = false;
    private boolean soundPlayed    = false;
    private float   countdownTimer = COUNTDOWN_MAX;

    public GameView(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    @Override
    protected void build() {
        playGameOst();

        setBackground("placeholder/BG_GameView.png");
        buildBoardZones();

        gearTex          = new Texture(Gdx.files.internal("ui/gear.png"));
        crystalIATex     = new Texture(Gdx.files.internal("ui/crystal_opponent.png"));
        crystalJoueurTex = new Texture(Gdx.files.internal("ui/crystal_player.png"));
        cardTex          = new Texture(Gdx.files.internal("placeholder/Card_Template.png"));

        gameModel = new GameModel();

        float scaleIA     = 0.22f;
        float scaleJoueur = 0.22f;
        float cristalX    = 10f;
        float cristalIAW  = crystalIATex.getWidth() * scaleIA;
        float cristalIAH  = crystalIATex.getHeight() * scaleIA;
        float cristalJW   = crystalJoueurTex.getWidth() * scaleJoueur;
        float cristalJH   = crystalJoueurTex.getHeight() * scaleJoueur;

        // Cristal IA
        Image crystalIA = new Image(crystalIATex);
        crystalIA.setSize(cristalIAW, cristalIAH);
        crystalIA.setPosition(cristalX, HEIGHT - cristalIAH - 30f);
        stage.addActor(crystalIA);
        labelCristalIA = mkLabel("100 / 100 PV", cristalX + cristalIAW / 2f, HEIGHT - cristalIAH - 55f, 16, Color.RED);

        // Cristal Joueur
        Image crystalJoueur = new Image(crystalJoueurTex);
        crystalJoueur.setSize(cristalJW, cristalJH);
        crystalJoueur.setPosition(cristalX, 30f);
        stage.addActor(crystalJoueur);
        labelCristalJoueur = mkLabel("100 / 100 PV", cristalX + cristalJW / 2f, 30f + cristalJH + 5f, 16, Color.GREEN);

        // Pioche au centre gauche
        Image pioche = new Image(cardTex);
        pioche.setSize(CARD_W, CARD_H);
        pioche.setPosition(cristalX + cristalJW / 2f - CARD_W / 2f, HEIGHT / 2f - CARD_H / 2f);
        stage.addActor(pioche);

        // Carte IA — en haut au centre
        addLabel("CARTE IA", WIDTH / 2f, HEIGHT * 0.75f, 14, Color.RED);
        labelCarteIA  = mkLabel("", WIDTH / 2f, HEIGHT * 0.68f, 18, Color.RED);
        labelHpIA     = mkLabel("", WIDTH / 2f, HEIGHT * 0.63f, 14, Color.WHITE);

        // Carte Joueur — en bas au centre
        addLabel("MA CARTE", WIDTH / 2f, HEIGHT * 0.35f, 14, Color.GREEN);
        labelCarteJoueur = mkLabel("", WIDTH / 2f, HEIGHT * 0.28f, 18, Color.GREEN);
        labelHpJoueur    = mkLabel("", WIDTH / 2f, HEIGHT * 0.23f, 14, Color.WHITE);

        // Attaques disponibles
        labelAtk1 = mkLabel("", WIDTH / 2f, HEIGHT * 0.16f, 13, OR_PALE);
        labelAtk2 = mkLabel("", WIDTH / 2f, HEIGHT * 0.11f, 13, OR_PALE);

        // Log dernière action
        labelLog = mkLabel("", WIDTH / 2f, HEIGHT * 0.05f, 12, Color.YELLOW);

        // Boutons ATTAQUE et SPECIAL
        addBtn("ATTAQUE",  WIDTH - 220f, HEIGHT / 2f + 20f, new Color(0.6f, 0.15f, 0.15f, 1f), () -> doAttack(false));
        addBtn("SPECIAL",  WIDTH - 220f, HEIGHT / 2f - 30f, new Color(0.15f, 0.15f, 0.6f, 1f), () -> doAttack(true));

        // Labels fin de partie cachés
        labelFinPartie = mkLabel("", WIDTH / 2f, HEIGHT / 2f, 72, Color.WHITE);
        labelFinPartie.setVisible(false);
        labelCountdown = mkLabel("", WIDTH / 2f, HEIGHT / 2f - 80f, 20, Color.WHITE);
        labelCountdown.setVisible(false);

        // Touches de test
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                System.out.println("[KEY] keycode=" + keycode);
                if (keycode == Input.Keys.V) {
                    while (!gameModel.getPlayer2().getCrystal().isDestroyed())
                        gameModel.getPlayer2().getCrystal().takeDamage(10);
                    System.out.println("[TEST] Victoire forcee");
                    updateUI();
                }
                if (keycode == Input.Keys.B) {
                    while (!gameModel.getPlayer1().getCrystal().isDestroyed())
                        gameModel.getPlayer1().getCrystal().takeDamage(10);
                    System.out.println("[TEST] Defaite forcee");
                    updateUI();
                }
                return true;
            }
        });

        keyIsPressed(Input.Keys.ESCAPE, controller::goPause);

        // Engrenage EN DERNIER
        Image gear = new Image(gearTex);
        gear.setSize(40f, 40f);
        gear.setPosition(15f, HEIGHT - 55f);
        gear.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.goPause();
            }
        });
        stage.addActor(gear);

        updateUI();
    }

    private void addBtn(String text, float x, float y, Color color, Runnable action) {
        Pixmap px = new Pixmap(140, 35, Pixmap.Format.RGBA8888);
        px.setColor(color);
        px.fill();
        px.setColor(OR_PALE);
        px.drawRectangle(0, 0, 140, 35);
        Image btn = new Image(new Texture(px));
        px.dispose();
        btn.setPosition(x, y);
        btn.setSize(140, 35);
        stage.addActor(btn);

        Label lbl = new Label(text, new Label.LabelStyle(cinzel(13, Color.WHITE), Color.WHITE));
        lbl.setPosition(x + 70f - lbl.getPrefWidth() / 2f, y + 8f);
        stage.addActor(lbl);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float mx, float my) {
                if (action != null) action.run();
            }
        });
    }

    private Label mkLabel(String text, float x, float y, int size, Color color) {
        Label lbl = new Label(text, new Label.LabelStyle(cinzel(size, color), color));
        lbl.setPosition(x - lbl.getPrefWidth() / 2f, y);
        stage.addActor(lbl);
        return lbl;
    }

    private void doAttack(boolean useSpecial) {
        if (gameOver) return;
        String log = gameModel.attackPlayer(useSpecial);
        labelLog.setText(log.contains("\n") ? log.split("\n")[0] : log);
        updateUI();
    }

    private void updateUI() {
        if (gameModel == null) return;

        Crystal cJ = gameModel.getPlayer1().getCrystal();
        Crystal cI = gameModel.getPlayer2().getCrystal();

        labelCristalJoueur.setText(cJ.getCurrentHp() + " / " + Crystal.MAX_HP + " PV");
        labelCristalIA.setText(cI.getCurrentHp() + " / " + Crystal.MAX_HP + " PV");

        Card pCard = gameModel.getPlayer1().getActiveCard();
        if (pCard != null) {
            labelCarteJoueur.setText(pCard.getName());
            labelHpJoueur.setText("HP : " + pCard.getCurrentHp() + " / " + pCard.getHp());
            labelAtk1.setText("► " + (pCard.getNormalAtk() != null ? pCard.getNormalAtk().getName() + " (" + pCard.getNormalAtk().getDamage() + " dmg)" : "—"));
            labelAtk2.setText("★ " + (pCard.getSpecialAtk() != null ? pCard.getSpecialAtk().getName() + " (" + pCard.getSpecialAtk().getDamage() + " dmg)" : "—"));
        } else {
            labelCarteJoueur.setText("Aucune carte");
            labelHpJoueur.setText("");
            labelAtk1.setText("");
            labelAtk2.setText("");
        }

        Card iCard = gameModel.getPlayer2().getActiveCard();
        if (iCard != null) {
            labelCarteIA.setText(iCard.getName());
            labelHpIA.setText("HP : " + iCard.getCurrentHp() + " / " + iCard.getHp());
        } else {
            labelCarteIA.setText("Aucune carte");
            labelHpIA.setText("");
        }

        checkGameEnd(cJ, cI);
    }

    private void checkGameEnd(Crystal cJoueur, Crystal cIA) {
        if (gameOver) return;
        if (!cJoueur.isDestroyed() && !cIA.isDestroyed()) return;

        gameOver = true;
        labelFinPartie.setVisible(true);
        labelCountdown.setVisible(true);

        if (!soundPlayed) {
            if (cIA.isDestroyed() && !cJoueur.isDestroyed()) {
                showResultat("VICTOIRE", new Color(0.95f, 0.82f, 0.40f, 1f));
                if (victorySound != null) victorySound.play(volumeMaster * volumeEffets);
            } else {
                showResultat("VOUS ETES MORT", Color.RED);
                if (defeatSound != null) defeatSound.play(volumeMaster * volumeEffets);
            }
            soundPlayed = true;
            if (ostGame != null) ostGame.stop();
        }
    }

    private void showResultat(String texte, Color couleur) {
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(0f, 0f, 0f, 0.6f);
        px.fill();
        Image fond = new Image(new Texture(px));
        px.dispose();
        fond.setFillParent(true);
        stage.addActor(fond);

        Label.LabelStyle style = new Label.LabelStyle(cinzel(72, couleur), couleur);
        labelFinPartie.setStyle(style);
        labelFinPartie.setText(texte);
        labelFinPartie.setPosition(WIDTH / 2f - labelFinPartie.getPrefWidth() / 2f, HEIGHT / 2f);
        stage.addActor(labelFinPartie);
        labelCountdown.setPosition(WIDTH / 2f - 150f, HEIGHT / 2f - 80f);
        stage.addActor(labelCountdown);
    }

    @Override
    public void draw() {
        if (ostGame != null && !ostGame.isPlaying() && !gameOver) playGameOst();
        super.draw();

        if (gameOver) {
            countdownTimer -= Gdx.graphics.getDeltaTime();
            int secondes = Math.max(0, (int) Math.ceil(countdownTimer));
            labelCountdown.setText("Retour au menu dans " + secondes + "s...");
            if (countdownTimer <= 0) {
                playMenuOst();
                controller.goMain();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (gearTex != null)          gearTex.dispose();
        if (crystalIATex != null)     crystalIATex.dispose();
        if (crystalJoueurTex != null) crystalJoueurTex.dispose();
        if (cardTex != null)          cardTex.dispose();
    }
}
