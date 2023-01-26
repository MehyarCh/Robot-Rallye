package Desperatedrosseln.Local.Protocols;

import java.util.List;

public class ExchangeShop {
    List<String> cards;

    public ExchangeShop(List<String> cards) {
        this.cards = cards;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }
}
