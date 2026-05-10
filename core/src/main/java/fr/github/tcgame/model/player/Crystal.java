package fr.github.tcgame.model.player;

import fr.github.tcgame.view.player.CrystalView;

public class Crystal {
    final public static int MAX_HP=100;
    public int currentHp=MAX_HP;
    public int idPlayer;

    public Crystal(int idP){ this.idPlayer=idP; }

    // draw method avec CrystalView

    public int getHealth(){ return this.currentHp; }

    public void updateTexture() {
        CrystalView.getInstance().displayCrystal(idPlayer, currentHp);
    }
}
