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
import fr.github.tcgame.model.player.Player;
import fr.github.tcgame.view.menu.Menu;

import java.util.List;

public class GameView extends Menu {

    // ── Constantes layout ──────────────────────────────────────────────────────
    private static final float COUNTDOWN_MAX = 10f;

    // Cartes terrain
    private static final float FIELD_CARD_W   = 100f;
    private static final float FIELD_CARD_H   = 140f;
    private static final float FIELD_CARD_PAD = 20f;

    // Cartes main
    private static final float HAND_CARD_W   = 90f;
    private static final float HAND_CARD_H   = 126f;
    private static final float HAND_CARD_PAD = 14f;

    // Zones Y
    private static final float IA_FIELD_Y     = HEIGHT * 0.56f;
    private static final float JOUEUR_FIELD_Y = HEIGHT * 0.22f;
    private static final float HAND_Y         = 10f;

    // ── Textures / modèle ─────────────────────────────────────────────────────
    private Texture gearTex;
    private Texture crystalIATex;
    private Texture crystalJoueurTex;

    private GameModel gameModel;
    private Player player;
    private Player ia;

    // ── Labels état ───────────────────────────────────────────────────────────
    private Label labelCristalJoueur;
    private Label labelCristalIA;
    private Label labelManaJoueur;
    private Label labelManaIA;
    private Label labelTour;
    private Label labelPhase;
    private Label labelFinPartie;
    private Label labelCountdown;

    // ── État de jeu ───────────────────────────────────────────────────────────
    private boolean gameOver       = false;
    private boolean soundPlayed    = false;
    private float   countdownTimer = COUNTDOWN_MAX;

    // ── Phase de jeu du joueur humain ─────────────────────────────────────────
    // PHASE 1 : déployer une carte depuis la main vers le banc
    // PHASE 2 : choisir la carte active depuis le banc (si pas encore active)
    // PHASE 3 : attaquer (normal / spécial)
    // PHASE 4 : tour IA puis recommencer
    private enum Phase { DEPLOY, SET_ACTIVE, ATTACK, IA_TURN }
    private Phase phase = Phase.DEPLOY;

    private Card selectedHandCard   = null; // carte sélectionnée dans la main
    private Card selectedBenchCard  = null; // carte sélectionnée sur le banc

    // ── Groupes d'acteurs pour le rendu dynamique ─────────────────────────────
    // On les recrée à chaque redraw pour rester simple
    private final com.badlogic.gdx.scenes.scene2d.Group handGroup  = new com.badlogic.gdx.scenes.scene2d.Group();
    private final com.badlogic.gdx.scenes.scene2d.Group fieldGroup = new com.badlogic.gdx.scenes.scene2d.Group();

    public GameView(MenuController controller) {
        super(TypeMenu.GAME, controller);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // BUILD
    // ═══════════════════════════════════════════════════════════════════════════

    @Override
    protected void build() {
        playGameOst();

        setBackground("placeholder/BG_GameView.png");
        buildBoardZones();

        gearTex          = new Texture(Gdx.files.internal("ui/gear.png"));
        crystalIATex     = new Texture(Gdx.files.internal("ui/crystal_opponent.png"));
        crystalJoueurTex = new Texture(Gdx.files.internal("ui/crystal_player.png"));

        gameModel = new GameModel("Joueur", "IA");
        gameModel.init();
        player = gameModel.getPlayer1();
        ia     = gameModel.getPlayer2();

        // Démarre le premier tour (pioche + mana)
        gameModel.startTurn();

        buildStaticUI();

        // Groupes dynamiques
        stage.addActor(fieldGroup);
        stage.addActor(handGroup);

        // Fin de partie (cachés)
        labelFinPartie = mkLabel("", WIDTH / 2f, HEIGHT / 2f, 72, Color.WHITE);
        labelFinPartie.setVisible(false);
        labelCountdown = mkLabel("", WIDTH / 2f, HEIGHT / 2f - 80f, 20, Color.WHITE);
        labelCountdown.setVisible(false);

        // Engrenage (pause) tout en haut
        Image gear = new Image(gearTex);
        gear.setSize(40f, 40f);
        gear.setPosition(15f, HEIGHT - 55f);
        gear.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { controller.goPause(); }
        });
        stage.addActor(gear);

        // Touche ESC = pause
        keyIsPressed(Input.Keys.ESCAPE, controller::goPause);

        // Refresh initial
        refreshDynamic();

        System.out.println("[GAMEVIEW] Build terminé. Phase: DEPLOY");
    }

    // ── UI statique (cristaux, labels) ────────────────────────────────────────

    private void buildStaticUI() {
        float scaleIA     = 0.22f;
        float scaleJoueur = 0.22f;
        float cristalX    = 10f;
        float cristalIAW  = crystalIATex.getWidth()  * scaleIA;
        float cristalIAH  = crystalIATex.getHeight() * scaleIA;
        float cristalJW   = crystalJoueurTex.getWidth()  * scaleJoueur;
        float cristalJH   = crystalJoueurTex.getHeight() * scaleJoueur;

        // Cristal IA
        Image crystalIA = new Image(crystalIATex);
        crystalIA.setSize(cristalIAW, cristalIAH);
        crystalIA.setPosition(cristalX, HEIGHT - cristalIAH - 30f);
        stage.addActor(crystalIA);
        labelCristalIA = mkLabel("100 / 100 PV", cristalX + cristalIAW / 2f, HEIGHT - cristalIAH - 55f, 14, Color.RED);
        labelManaIA    = mkLabel("Mana: 0/10",   cristalX + cristalIAW / 2f, HEIGHT - cristalIAH - 75f, 12, new Color(0.4f, 0.8f, 1f, 1f));

        // Cristal Joueur
        Image crystalJoueur = new Image(crystalJoueurTex);
        crystalJoueur.setSize(cristalJW, cristalJH);
        crystalJoueur.setPosition(cristalX, 160f);
        stage.addActor(crystalJoueur);
        labelCristalJoueur = mkLabel("100 / 100 PV", cristalX + cristalJW / 2f, 140f, 14, Color.GREEN);
        labelManaJoueur    = mkLabel("Mana: 0/10",   cristalX + cristalJW / 2f, 120f, 12, new Color(0.4f, 0.8f, 1f, 1f));

        // Tour + phase
        labelTour  = mkLabel("Tour 1", WIDTH / 2f, HEIGHT - 30f, 16, OR_PALE);
        labelPhase = mkLabel("Phase: DEPLOYER", WIDTH / 2f, HEIGHT - 55f, 13, GRIS_MENU);

        // Bouton "Fin de tour" (passer à l'IA si on a fini)
        addMenuOption("FIN DE TOUR", WIDTH - 100f, 80f, this::endPlayerTurn);

        // Bouton "Attaque normale" et "Attaque spéciale"
        addMenuOption("ATK NORMALE",  WIDTH - 110f, HEIGHT * 0.38f,        () -> playerAttack(false));
        addMenuOption("ATK SPECIALE", WIDTH - 110f, HEIGHT * 0.38f - 40f,  () -> playerAttack(true));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // REFRESH DYNAMIQUE (main + terrain)
    // ═══════════════════════════════════════════════════════════════════════════

    private void refreshDynamic() {
        handGroup.clearChildren();
        fieldGroup.clearChildren();
        buildHandCards();
        buildFieldCards();
        updateLabels();
    }

    // ── Main du joueur ────────────────────────────────────────────────────────

    private void buildHandCards() {
        List<Card> hand = player.getHand();
        float totalW = hand.size() * (HAND_CARD_W + HAND_CARD_PAD) - HAND_CARD_PAD;
        float startX = WIDTH / 2f - totalW / 2f;

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            float x = startX + i * (HAND_CARD_W + HAND_CARD_PAD);
            buildHandCardActor(card, x, HAND_Y, i);
        }
    }

    private void buildHandCardActor(Card card, float x, float y, int idx) {
        boolean selected = card == selectedHandCard;
        Color bg = selected
            ? new Color(0.5f, 0.4f, 0.1f, 0.95f)
            : new Color(0.12f, 0.10f, 0.08f, 0.92f);

        Image cardImg = makeCardRect((int) HAND_CARD_W, (int) HAND_CARD_H, bg, OR_PALE);
        cardImg.setPosition(x, selected ? y + 10f : y);
        handGroup.addActor(cardImg);

        // Texte nom
        addGroupLabel(handGroup, card.getName(), x + 5f, y + HAND_CARD_H - 18f + (selected ? 10f : 0f), 10, getTypeColor(card.getType()));
        // Famille
        addGroupLabel(handGroup, card.getFamily().name(), x + 5f, y + HAND_CARD_H - 32f + (selected ? 10f : 0f), 9, GRIS_MENU);
        // HP
        addGroupLabel(handGroup, "HP: " + card.getHp(), x + 5f, y + HAND_CARD_H - 47f + (selected ? 10f : 0f), 9, GRIS_MENU);
        // Coût
        addGroupLabel(handGroup, "Cout: " + card.getCost(), x + 5f, y + HAND_CARD_H - 60f + (selected ? 10f : 0f), 9, new Color(0.4f, 0.8f, 1f, 1f));
        // Attaque normale
        if (card.getNormalAtk() != null)
            addGroupLabel(handGroup, card.getNormalAtk().getName() + " " + card.getNormalAtk().getDamage() + "dmg",
                x + 5f, y + HAND_CARD_H - 76f + (selected ? 10f : 0f), 8, Color.WHITE);
        // Attaque spéciale
        if (card.getSpecialAtk() != null)
            addGroupLabel(handGroup, card.getSpecialAtk().getName() + " " + card.getSpecialAtk().getDamage() + "dmg",
                x + 5f, y + HAND_CARD_H - 90f + (selected ? 10f : 0f), 8, new Color(0.9f, 0.5f, 0.9f, 1f));

        // Clic : sélectionner cette carte pour la déployer
        cardImg.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float mx, float my) {
                onHandCardClicked(card);
            }
        });
    }

    // ── Terrain (banc + carte active) des deux joueurs ────────────────────────

    private void buildFieldCards() {
        // IA (haut de l'écran)
        buildPlayerField(ia,     IA_FIELD_Y,     false);
        // Joueur (milieu bas)
        buildPlayerField(player, JOUEUR_FIELD_Y, true);
    }

    private void buildPlayerField(Player p, float baseY, boolean isHuman) {
        // Carte active
        Card active = p.getActiveCard();
        if (active != null) {
            float x = WIDTH / 2f - FIELD_CARD_W / 2f;
            buildFieldCardActor(active, x, baseY, isHuman, true);
        }

        // Banc
        List<Card> bench = p.getBench();
        float benchStartX = WIDTH / 2f - ((Player.MAX_BENCH_SIZE) * (FIELD_CARD_W + FIELD_CARD_PAD)) / 2f;
        for (int i = 0; i < bench.size(); i++) {
            float bx = benchStartX + i * (FIELD_CARD_W + FIELD_CARD_PAD);
            float by = isHuman ? baseY - FIELD_CARD_H - 10f : baseY + FIELD_CARD_H + 10f;
            buildFieldCardActor(bench.get(i), bx, by, isHuman, false);
        }

        // Label "ACTIF" / "BANC"
        addGroupLabel(fieldGroup, isHuman ? "— VOS CARTES —" : "— IA —",
            WIDTH / 2f - 50f,
            isHuman ? baseY + FIELD_CARD_H + 5f : baseY - 20f,
            11, isHuman ? OR_PALE : Color.RED);
    }

    private void buildFieldCardActor(Card card, float x, float y, boolean isHuman, boolean isActive) {
        Color border = isActive
            ? (isHuman ? new Color(0.95f, 0.82f, 0.40f, 1f) : Color.RED)
            : GRIS_MENU;
        boolean selectedForSwap = card == selectedBenchCard;
        Color bg = selectedForSwap
            ? new Color(0.4f, 0.3f, 0.05f, 0.95f)
            : new Color(0.10f, 0.08f, 0.06f, 0.93f);

        Image cardImg = makeCardRect((int) FIELD_CARD_W, (int) FIELD_CARD_H, bg, border);
        cardImg.setPosition(x, y);
        fieldGroup.addActor(cardImg);

        float ty = y + FIELD_CARD_H - 18f;
        addGroupLabel(fieldGroup, card.getName(),           x + 5f, ty,      11, getTypeColor(card.getType()));
        addGroupLabel(fieldGroup, card.getFamily().name(),  x + 5f, ty - 16f, 9, GRIS_MENU);
        addGroupLabel(fieldGroup, "HP: " + card.getCurrentHp() + "/" + card.getHp(), x + 5f, ty - 30f, 9,
            card.isBelowHalfHp() ? Color.RED : Color.GREEN);
        if (card.getNormalAtk() != null)
            addGroupLabel(fieldGroup, card.getNormalAtk().getName() + " " + card.getNormalAtk().getDamage() + "dmg",
                x + 5f, ty - 44f, 8, Color.WHITE);
        if (card.getSpecialAtk() != null)
            addGroupLabel(fieldGroup, card.getSpecialAtk().getName() + " " + card.getSpecialAtk().getDamage() + "dmg",
                x + 5f, ty - 56f, 8, new Color(0.9f, 0.5f, 0.9f, 1f));

        // Statuts
        if (card.isFrozen())            addGroupLabel(fieldGroup, "[GELE]",   x + 5f, ty - 70f, 8, Color.CYAN);
        if (card.hasTrap())             addGroupLabel(fieldGroup, "[TNT]",    x + 5f, ty - 80f, 8, Color.ORANGE);
        if (card.getPoisonDamage() > 0) addGroupLabel(fieldGroup, "[POISON]", x + 5f, ty - 90f, 8, Color.GREEN);

        // IsActive marker
        addGroupLabel(fieldGroup, isActive ? "[ACTIF]" : "[BANC]", x + 5f, y + 5f, 8, border);

        // Clic sur carte du banc du joueur = sélectionner pour swap
        if (isHuman && !isActive) {
            cardImg.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float mx, float my) {
                    onBenchCardClicked(card);
                }
            });
        }
    }

    // ── Mise à jour des labels état ───────────────────────────────────────────

    private void updateLabels() {
        Crystal cJ = player.getCrystal();
        Crystal cI = ia.getCrystal();

        labelCristalJoueur.setText(cJ.getCurrentHp() + " / " + Crystal.MAX_HP + " PV");
        labelCristalIA.setText(cI.getCurrentHp() + " / " + Crystal.MAX_HP + " PV");
        labelManaJoueur.setText("Mana: " + player.getMana() + "/" + Player.MAX_MANA);
        labelManaIA.setText("Mana: " + ia.getMana() + "/" + Player.MAX_MANA);
        labelTour.setText("Tour " + gameModel.getTurnNumber());

        String phaseStr = switch (phase) {
            case DEPLOY      -> "Déployez une carte de votre main (cliquez dessus)";
            case SET_ACTIVE  -> "Choisissez la carte active parmi votre banc (cliquez sur le banc)";
            case ATTACK      -> "Choisissez votre attaque ou passez le tour";
            case IA_TURN     -> "Tour de l'IA...";
        };
        labelPhase.setText(phaseStr);
        labelPhase.setPosition(WIDTH / 2f - labelPhase.getPrefWidth() / 2f, HEIGHT - 55f);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LOGIQUE JOUEUR
    // ═══════════════════════════════════════════════════════════════════════════

    private void onHandCardClicked(Card card) {
        if (phase != Phase.DEPLOY && phase != Phase.SET_ACTIVE) {
            System.out.println("[CLIC] Pas en phase DEPLOY/SET_ACTIVE, ignoré.");
            return;
        }
        if (phase == Phase.DEPLOY) {
            // Déployer sur le banc
            if (gameModel.deployToBench(player, card)) {
                selectedHandCard = null;
                System.out.println("[JOUEUR] Carte déployée sur le banc.");
                // Si pas encore de carte active, passer en SET_ACTIVE
                if (!player.hasActiveCard()) {
                    phase = Phase.SET_ACTIVE;
                    System.out.println("[PHASE] -> SET_ACTIVE");
                } else {
                    phase = Phase.ATTACK;
                    System.out.println("[PHASE] -> ATTACK");
                }
            } else {
                // Sélection visuelle
                selectedHandCard = (selectedHandCard == card) ? null : card;
                System.out.println("[JOUEUR] Sélection: " + (selectedHandCard != null ? selectedHandCard.getName() : "aucune"));
            }
            refreshDynamic();
        }
    }

    private void onBenchCardClicked(Card card) {
        if (phase == Phase.SET_ACTIVE) {
            // Mettre la carte en jeu (gratuit)
            if (gameModel.playFromBenchFree(player, card)) {
                selectedBenchCard = null;
                phase = Phase.ATTACK;
                System.out.println("[JOUEUR] Carte mise en jeu (gratuit). Phase -> ATTACK");
                refreshDynamic();
            }
        } else if (phase == Phase.ATTACK) {
            // Echanger la carte active (coûte 2 mana)
            if (gameModel.swapActiveCard(player, card)) {
                selectedBenchCard = null;
                System.out.println("[JOUEUR] Echange effectué.");
                refreshDynamic();
            } else {
                selectedBenchCard = (selectedBenchCard == card) ? null : card;
                refreshDynamic();
            }
        }
    }

    private void playerAttack(boolean useSpecial) {
        if (phase != Phase.ATTACK) {
            System.out.println("[ATTAQUE] Pas en phase ATTACK, ignoré.");
            return;
        }
        if (!player.hasActiveCard()) {
            System.out.println("[ATTAQUE] Pas de carte active !");
            return;
        }
        gameModel.resolveAttack(player, useSpecial);
        refreshDynamic();

        if (gameModel.isOver()) { handleGameOver(); return; }

        // Après l'attaque, on passe directement au tour IA
        System.out.println("[PHASE] -> IA_TURN");
        phase = Phase.IA_TURN;
        refreshDynamic();
    }

    private void endPlayerTurn() {
        if (phase == Phase.IA_TURN) return; // déjà passé
        System.out.println("[JOUEUR] Fin de tour volontaire.");
        phase = Phase.IA_TURN;
        refreshDynamic();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LOGIQUE IA (très simple : déploie si possible, puis attaque)
    // ═══════════════════════════════════════════════════════════════════════════

    private float iaDelay = 0f;
    private static final float IA_THINK_TIME = 1.2f; // secondes de "réflexion"

    private void runIaTurn() {
        System.out.println("\n[IA] === Tour de l'IA ===");

        // 1. Déployer une carte si banc non plein
        List<Card> iaHand = ia.getHand();
        for (Card c : iaHand) {
            if (gameModel.deployToBench(ia, c)) break; // une seule par tour
        }

        // 2. Mettre une carte en jeu si pas de carte active
        if (!ia.hasActiveCard() && !ia.getBench().isEmpty()) {
            gameModel.playFromBenchFree(ia, ia.getBench().get(0));
        }

        // 3. Attaquer (préfère la spéciale si assez de mana)
        if (ia.hasActiveCard()) {
            Card iac = ia.getActiveCard();
            boolean useSpecial = iac.getSpecialAtk() != null && ia.getMana() >= iac.getSpecialAtk().getCostMana();
            gameModel.resolveAttack(ia, useSpecial);
        }

        System.out.println("[IA] Fin de tour IA.");

        if (gameModel.isOver()) { handleGameOver(); return; }

        // Nouveau tour
        gameModel.startTurn();
        phase = Phase.DEPLOY;

        // Si le joueur n'a pas de carte active, forcer SET_ACTIVE
        if (!player.hasActiveCard() && !player.getBench().isEmpty()) {
            phase = Phase.SET_ACTIVE;
        }

        System.out.println("[PHASE] Nouveau tour -> " + phase);
        refreshDynamic();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // FIN DE PARTIE
    // ═══════════════════════════════════════════════════════════════════════════

    private void handleGameOver() {
        gameOver = true;
        labelFinPartie.setVisible(true);
        labelCountdown.setVisible(true);
        if (!soundPlayed) {
            boolean joueurGagne = gameModel.getWinner() == player;
            showResultat(joueurGagne ? "VICTOIRE" : "VOUS ETES MORT",
                joueurGagne ? new Color(0.95f, 0.82f, 0.40f, 1f) : Color.RED);
            if (joueurGagne) { if (victorySound != null) victorySound.play(volumeMaster * volumeEffets); }
            else             { if (defeatSound  != null) defeatSound.play(volumeMaster * volumeEffets); }
            soundPlayed = true;
            if (ostGame != null) ostGame.stop();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DRAW (appelé chaque frame)
    // ═══════════════════════════════════════════════════════════════════════════

    @Override
    public void draw() {
        if (ostGame != null && !ostGame.isPlaying() && !gameOver) playGameOst();

        // Délai IA
        if (phase == Phase.IA_TURN && !gameOver) {
            iaDelay += Gdx.graphics.getDeltaTime();
            if (iaDelay >= IA_THINK_TIME) {
                iaDelay = 0f;
                runIaTurn();
            }
        }

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

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    private Label mkLabel(String text, float x, float y, int size, Color color) {
        Label lbl = new Label(text, new Label.LabelStyle(cinzel(size, color), color));
        lbl.setPosition(x - lbl.getPrefWidth() / 2f, y);
        stage.addActor(lbl);
        return lbl;
    }

    private void addGroupLabel(com.badlogic.gdx.scenes.scene2d.Group group,
                               String text, float x, float y, int size, Color color) {
        Label lbl = new Label(text, new Label.LabelStyle(cinzel(size, color), color));
        lbl.setPosition(x, y);
        group.addActor(lbl);
    }

    private Image makeCardRect(int w, int h, Color bg, Color border) {
        Pixmap px = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        px.setColor(bg);
        px.fill();
        px.setColor(border);
        px.drawRectangle(0, 0, w, h);
        Image img = new Image(new Texture(px));
        px.dispose();
        img.setSize(w, h);
        return img;
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
    public void dispose() {
        super.dispose();
        if (gearTex != null)          gearTex.dispose();
        if (crystalIATex != null)     crystalIATex.dispose();
        if (crystalJoueurTex != null) crystalJoueurTex.dispose();
    }
}
