package fr.github.tcgame.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import fr.github.tcgame.model.player.Crystal;
import fr.github.tcgame.model.player.Player;

public class GameHUD extends Group {
    private static final float WIDTH = 1200f;
    private static final float HEIGHT = 800f;

    private static final Color OR_PALE = new Color(0.76f, 0.67f, 0.49f, 1f);
    private static final Color GRIS_HUD  = new Color(0.63f, 0.63f, 0.63f, 1f);
    private static final Color CRYSTAL_COLOR = new Color(0.4f, 0.8f, 1f, 1f);


    private final Player player;
    private final Player opponent;

    private BitmapFont fontMedium;
    private BitmapFont fontSmall;
    private BitmapFont fontTour;

    private int turnNumber;

    private Texture barBg;
    private Texture manaFull;
    private Texture manaEmpty;

    //Constructeur
    public GameHUD(Player player, Player opponent){
        this.player=player;
        this.opponent=opponent;
        this.turnNumber=1;
        loadFonts();
        buildStaticTextures();
    }

    private void loadFonts(){
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Cinzel-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter pm = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pm.size =18; pm.color = OR_PALE;
        fontMedium = gen.generateFont(pm);

        FreeTypeFontGenerator.FreeTypeFontParameter ps = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ps.size = 13; ps.color = GRIS_HUD;
        fontSmall = gen.generateFont(ps);

        FreeTypeFontGenerator.FreeTypeFontParameter pt = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pt.size = 13; ps.color = OR_PALE;
        fontTour=gen.generateFont(pt);

        gen.dispose();
    }

    private void buildStaticTextures(){

    }




}
