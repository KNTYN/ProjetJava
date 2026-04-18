package fr.github.tcgame.model.attack;

public class Attack {
    private String name;
    private String description;
    private int damage;
    private int costMana;
    private AttackEffect effect;
    private int effectValue;

    public Attack(String name, String description, int damage, int costMana, AttackEffect effect, int effectValue){
        this.name=name;
        this.description=description;
        this.damage=damage;
        this.costMana=costMana;
        this.effect=effect;
        this.effectValue=effectValue;
    }

    public boolean hasEffect(){
        return effect != AttackEffect.NONE;
    }

    //Getters
    public String getName()          { return name; }
    public String getDescription()   { return description; }
    public int getDamage()           { return damage; }
    public int getCostMana()         { return costMana; }
    public AttackEffect getEffect()  { return effect; }
    public int getEffectValue()      { return effectValue; }


    @Override
    public String toString() {
        return String.format("%s (dmg:%d, mana:%d%s)",
            name, damage, costMana,
            hasEffect() ? ", effet:" + effect + "(" + effectValue + ")" : "");
    }
}
