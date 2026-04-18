package fr.github.tcgame.model.card;

import fr.github.tcgame.model.attack.Attack;

public class Card {
    public enum Type{COMMUN,RARE,LEGENDAIRE,TASTY_CROUSTY,OBJET,EVENT}
    private String name;
    private String description;
    private Type type;
    private int hp;
    private int cost;

    private Attack normalAtk;
    //private Attack specialAtk;

    private String bgCardTexturePath;
    private String charTexturePath;

    public Card(String name, String description, Type type, int hp, int cost, Attack normalAtk) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.hp = hp;
        this.cost = cost;
        this.normalAtk = normalAtk;
       // this.specialAtk = specialAtk;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getCost() { return cost;}
}
