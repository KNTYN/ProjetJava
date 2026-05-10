package fr.github.tcgame.model.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Card {

    public enum Family { CAPITALISTE, MAUDIT, PRODIGE, GOOFY, SORT, EVENT }

    private String name;
    private Family family;

    private int hp;
    private int currentHp;

    private int cost;

    private int normalAtk;
    private int specialAtk;
    private int mana;

    private Texture cardTexture; // on initie la texture dans le constructeur directement puis il sera récupérer dans le CardView

    public enum Zone { OFFF, HAND, BENCH, ACTIVE}

    private Zone zone;


    public Card(String name, Family family, int hp, int cost, int normalAtk, int specialAtk, int mana, String pathCardTexture) {
        this.name = name;
        this.family = family;

        this.hp = hp;
        this.currentHp = hp;

        this.cost = cost;

        this.normalAtk = normalAtk;
        this.specialAtk = specialAtk;
        this.mana = mana;

        this.cardTexture=new Texture((Gdx.files.internal(pathCardTexture)));

        this.zone=Zone.OFFF;
    }


    // getters necessaires pour les class hors du package |model| :
    public Texture getCardTexture() { return cardTexture; }

    @Override
    public String toString() {
        return this.name;
    }

    public Family getFamily() { return family; }
    public String getName() { return name; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public int getCost(){ return this.cost; }

    public int getNormalAtk() { return this.normalAtk; }
    public int getSpecialAtk() { return this.specialAtk; }
    public int getManaCost() { return this.mana; }

    public int getDamage(int dmg) {
        int absorbed = min(dmg, this.hp); // dégâts que la carte absorbe
        this.hp = max(0, this.hp - dmg);
        if (this.hp == 0) { deathCard(); }
        return dmg - absorbed; // surplus transmis au cristal
    }

    public void deathCard(){
        this.zone=Zone.OFFF;
        // reset completement de l'affichage
    }
}
