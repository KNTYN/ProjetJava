package fr.github.tcgame.model.attack;

public class Attack {
    private String name;
    private String description;
    private int damage;
    private int costMana;
    public Attack(String name, String description, int damage, int costMana){
        this.name=name;
        this.description=description;
        this.damage=damage;
        this.costMana=costMana;
    }
}
