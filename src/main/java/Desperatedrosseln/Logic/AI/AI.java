package Desperatedrosseln.Logic.AI;

import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;

public class AI {
    int activePhase;
    int currentPlayer;
    int ID;
    Robot robot;
    ArrayList<String> cardsInHand;
    public AI(int ai_id) {
        ID = ai_id;
    }

    public void setActivePhase(int activePhase) {
        this.activePhase = activePhase;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void setCardsInHand(ArrayList<String> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public ArrayList<String> getCardsInHand() {
        return cardsInHand;
    }

    public String getRandomCard() {
        if(cardsInHand.size()==0||cardsInHand == null){
            return "error";
        }
        int index = (int)Math.floor(Math.random()*(cardsInHand.size()));
        if(index != cardsInHand.size()){
            String card = cardsInHand.get(index);
            cardsInHand.remove(index);
            return card;
        }else{
            return getRandomCard();
        }
    }
}
