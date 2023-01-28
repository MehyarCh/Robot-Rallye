package Desperatedrosseln.Local.Protocols;


import java.util.List;

public class RefillShop {
    List<String> cards;

    public RefillShop(List<String> cards) {
        this.cards = cards;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }
}
