package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.*;
import Desperatedrosseln.Logic.Cards.Programmingcard;
import javafx.scene.control.*;
import javafx.scene.image.*;

import java.util.*;

public class MoveOne extends Programmingcard {
    Image cardOneImage;
    String cardName;
    public void Card(){
        cardOneImage  = new Image("file:cardtest2.png");
        cardName = "moveOne";
    }
    @Override
    public void playCard(Player player) {
        player.getRobot().move(1);
    }

    public boolean isDamageCard(){
        return false;
    }
    public String getCardName() {
        return cardName;
    }
    public Image getCardOneImage() {
        return cardOneImage;
    }
}

