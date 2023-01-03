package Desperatedrosseln.Logic.Cards;

import Desperatedrosseln.Logic.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

public abstract class Card {

    private String cardName;

    private Label cardLabel;

    public Card(String cardName, String styleClass, EventHandler<MouseEvent> event) {
        this.cardName = cardName;
        this.cardLabel = new Label();
        cardLabel.setId("card");
        cardLabel.setOnMouseClicked(event);
        cardLabel.getStyleClass().add(styleClass);
    }
    public Card(String cardName, String styleClass){}


    public void playCard(Player player){

    }

    public abstract void playCard();

    public String toString(){
        return cardName;
    }

    public boolean isAllowedMove(){
        //conditions in which a move is allowed/not allowed (moving from a pit == false)
        return true;
    }
    public String getCardName() {
        return cardName;
    }
    public Label getCardLabel() {
        return cardLabel;
    }

}

