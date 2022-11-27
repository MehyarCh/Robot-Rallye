package Server;

abstract class Card {

    private String info;

    /*Konstruktor?
    public Card(String info) {
        this.info = info;
    }*/

    public abstract void playCard();

    public abstract String toString();
}
