package Desperatedrosseln.Local.Protocols;

public class BuyUpgrade {
    boolean isBuying;
    String card;

    public BuyUpgrade(boolean isBuying, String card) {
        this.isBuying = isBuying;
        this.card = card;
    }

    public boolean isBuying() {
        return isBuying;
    }

    public String getCard() {
        return card;
    }
}
