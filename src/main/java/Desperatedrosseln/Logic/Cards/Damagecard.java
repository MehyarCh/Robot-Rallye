package Desperatedrosseln.Logic.Cards;

public class Damagecard extends Card {

    /*public Damagecards(String info) {
        super(info);
    }*/

    public void playCard() {

    }

    @Override
    public String toString() {
        return "DamageCard";
    }

    @Override
    public boolean isDamageCard() {
        return true;
    }
}

