package fr.github.tcgame.model.card;

import fr.github.tcgame.model.attack.Attack;

public class Card {
    public enum Type{COMMUN,RARE,LEGENDAIRE,TASTY_CROUSTY,OBJET,EVENT}
    public enum Family { CAPITALISTE, MAUDIT, PRODIGE, GOOFY, SORT, EVENT }
    private String name;
    private String description;
    private Type type;
    private Family family;
    private int hp;
    private int currentHp;
    private int cost;

    //Effets carte
    private boolean frozen;
    private boolean hasAttackMiss;
    private int missChance;
    private boolean protectCrystal;
    private boolean hasTrap;
    private int poisonDamage;

    private Attack normalAtk;
    private Attack specialAtk;

    private String bgCardTexturePath;
    private String charTexturePath;

    public Card(String name, String description, Type type, Family family,
                int hp, int cost, Attack normalAtk, Attack specialAtk) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.family = family;
        this.hp = hp;
        this.currentHp = hp;
        this.cost = cost;
        this.normalAtk = normalAtk;
        this.specialAtk = specialAtk;
    }

    public void takeDamage(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    public void heal(int amount) {
        currentHp = Math.min(hp, currentHp + amount);
    }

    public boolean isDead() {
        return currentHp <= 0;
    }

    public boolean isBelowHalfHp() {
        return currentHp < hp / 2.0;
    }

    //Getters
    public String getName()             { return name; }
    public String getDescription()      { return description; }
    public Type getType()               { return type; }
    public Family getFamily()           { return family; }
    public int getHp()                  { return hp; }
    public int getCurrentHp()          { return currentHp; }
    public int getCost()                { return cost; }
    public Attack getNormalAtk()        { return normalAtk; }
    public Attack getSpecialAtk()       { return specialAtk; }
    public String getBgCardTexturePath(){ return bgCardTexturePath; }
    public String getCharTexturePath()  { return charTexturePath; }



    //Methodes effets

    public void applyFreeze()             { this.frozen = true; }
    public void clearFreeze()             { this.frozen = false; }
    public boolean isFrozen()             { return frozen; }

    public void applyMissChance(int pct)  { this.hasAttackMiss = true; this.missChance = pct; }
    public void clearMissChance()         { this.hasAttackMiss = false; this.missChance = 0; }
    public boolean hasAttackMiss()        { return hasAttackMiss; }
    public int getMissChance()            { return missChance; }

    public void applyProtectCrystal()     { this.protectCrystal = true; }
    public void clearProtectCrystal()     { this.protectCrystal = false; }
    public boolean isProtectingCrystal()  { return protectCrystal; }

    public void setTrap(boolean trap)     { this.hasTrap = trap; }
    public boolean hasTrap()              { return hasTrap; }

    public void applyPoison(int dmgPerTurn) { this.poisonDamage = dmgPerTurn; }
    public void clearPoison()               { this.poisonDamage = 0; }
    public int getPoisonDamage()            { return poisonDamage; }

    public void clearAllStatuses() {
        clearFreeze();
        clearMissChance();
        clearProtectCrystal();
        setTrap(false);
        clearPoison();
    }


    //Setters textures

    public void setBgCardTexturePath(String path) { this.bgCardTexturePath = path; }
    public void setCharTexturePath(String path)   { this.charTexturePath = path; }

    @Override
    public String toString() {
        return String.format("[%s/%s] %s  HP:%d/%d  Coût:%d",
            type, family, name, currentHp, hp, cost);
    }
}
