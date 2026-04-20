package fr.github.tcgame.model.player;

public class Crystal {

    public static final int MAX_HP = 100;

    private int currentHp;

    public Crystal(){
        this.currentHp = MAX_HP;
    }

    public void takeDamage(int amount){
        currentHp = Math.max(0,currentHp-amount);
    }

    public void heal(int amount){
        currentHp=Math.min(MAX_HP,currentHp+amount);
    }

    public boolean isDestroyed(){
        return currentHp<=0;
    }

    public int getCurrentHp(){return currentHp;}
    public int getMaxHp(){return MAX_HP;}

    @Override
    public String toString(){
        return String.format("Cristal [%d/%d PV]", currentHp, MAX_HP);
    }

}
