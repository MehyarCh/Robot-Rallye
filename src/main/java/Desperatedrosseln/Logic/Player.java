package Desperatedrosseln.Logic;

import Desperatedrosseln.Logic.Cards.Card;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private Robot robot;
    private List<Card> deck = new ArrayList<>(20);
    private List<Card> hand = new ArrayList<>(9);
    private Card[] register = new Card[5];
    //private Date birthdate;
    private List <Card> discarded;

    public Player(Robot robot /*ToDO: add Date*/) {

        this.robot = robot;
    }

    public void playPhaseOne(){

    }

    public void playPhaseTwo(){

    }

    public void buyCard (Card card){

    }
}
