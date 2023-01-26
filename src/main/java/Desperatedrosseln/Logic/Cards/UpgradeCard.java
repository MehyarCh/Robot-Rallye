package Desperatedrosseln.Logic.Cards;

public class UpgradeCard extends Card{

    /*public UpgradeCards(String info) {
        super(info);
    }*/
    transient boolean isPassive;
    transient int cost;
    public UpgradeCard(boolean isPassive, int cost) {
        this.isPassive = isPassive;;
        this.cost = cost;
    }

    public boolean isPassive() {
        return isPassive;
    }

    public int getCost() {
        return cost;
    }

    public void playCard(){

    }

    @Override
    public String toString(){
        return "UpgradeCard";
    }
}

