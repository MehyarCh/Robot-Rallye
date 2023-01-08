package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {


    private String name;
    private int ID;
    private Robot robot;
    private List<Card> deck = new ArrayList<>(20);
    private List<Card> hand = new ArrayList<>(9);
    private Card[] register = new Card[5];
    //private Date birthdate;
    private List <Card> discarded;

    public Player() {

    }

    public Robot getRobot() {
        return robot;
    }
    public Player(Robot robot /*ToDO: add Date*/) {

        this.robot = robot;
    }

    public Player(String name){
        this.name = name;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void playPhaseOne(){

    }

    public void playPhaseTwo(){

    }

    public void buyCard (Card card){

    }
}
