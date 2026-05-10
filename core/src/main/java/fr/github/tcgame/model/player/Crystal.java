package fr.github.tcgame.model.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Crystal {
    final public static int MAX_HP=100;
    public int currentHp=MAX_HP;
    public int idPlayer;

    public Crystal(int idP){ this.idPlayer=idP; }

    // draw method avec CrystalView

    public int getHealth(){ return this.currentHp; }

    // quand on se prend un dégât, update la texture
    public void updateTexture(){

    }
}
