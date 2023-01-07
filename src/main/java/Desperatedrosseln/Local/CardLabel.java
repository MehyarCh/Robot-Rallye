package Desperatedrosseln.Local;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

public abstract class CardLabel {
    private String cardName;
    private Label cardLabel;

    public CardLabel(String cardName, String styleClass, EventHandler<MouseEvent> event) {
            this.cardName = cardName;
            this.cardLabel = new Label();
            cardLabel.setId("card");
            cardLabel.setOnMouseClicked(event);
            cardLabel.getStyleClass().add(styleClass);
        }
    public String getCardName() {
        return cardName;
    }
    public Label getCardLabel() {
        return cardLabel;
    }

}

