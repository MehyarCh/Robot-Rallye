package Desperatedrosseln.Logic;

import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int ID;
    private int next_checkpoint=1;
    private Robot robot;
    private List<Card> deck = new ArrayList<>(20);
    public List<Card> getHand() {
        return hand;
    }

    private List<Card> hand = new ArrayList<>(9);
    private Card[] registers = new Card[5];
    private int registerTrack =0;
    private List <Card> discarded = new ArrayList<>();
    private ArrayList<String> cardsYouGotNow = new ArrayList<>();


    public Player() {

    }

    public Card[] getRegisters() {
        return registers;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public Card getRegisterIndex(int index) {
        return registers[index];
    }

    public List<Card> getDiscarded() {
        return discarded;
    }

    public Robot getRobot() {
        return robot;
    }
    public Player(Robot robot) {
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

    /**
     * draws card from deck and disposes it from the deck
     */
    public Card drawCardFromDeck(){
        Card card = deck.get((deck.size()-1));
        deck.remove(deck.size()-1);
        return card;
    }

    /**
     * draws the hand of the player during the programming phase
     * @return 1 if discarded pile wasn't needed, or 2 otherwise
     */
    public int programmingPhase(){
        DeckHelper.buildDeck(deck);
        if(deck.size()>=9){
            for(int i=0; i<9; i++){
                hand.add(drawCardFromDeck());
            }
            return 1;
        }else{
            for(int i=0; i<deck.size(); i++){
                hand.add(drawCardFromDeck());
            }
            //discarded = CardUtils.shuffle(discarded);
            for(int j=0; j<20; j++){
                deck.add(takeCardFromDiscarded());
            }
            shuffleDeck();
            while(hand.size()<= 9){
                Card card = drawCardFromDeck();
                hand.add(card);
                cardsYouGotNow.add(card.toString());
            }

            return 2;
        }
    }

    public ArrayList<String> getCardsYouGotNow() {
        return cardsYouGotNow;
    }

    private Card takeCardFromDiscarded(){
        //assert size >0
        Card card = discarded.get((discarded.size()-1));
        discarded.remove(discarded.size()-1);
        return card;
    }

    void shuffleDeck(){
        DeckHelper.shuffleCards(deck);
    }

    public void buyCard (Card card){

    }
    public int getNextCheckPoint(){
        return next_checkpoint;
    }
    public void setNextCheckPoint(){
        next_checkpoint++;
    }

    public void chooseProgrammingCards(Card[] cards) {
        //assert cards size == 5
        registers = cards;
    }

    /**
     * sets a specified register slot with a card from the hand and deletes it from the hand
     * replacing it with an empty slot
     * @param number the position of the card in the hand
     * @param register the register to put the card in
     */
    public void selectCard(int number, int register){
        this.registers[register] = hand.get(number);
        hand.add(number, null);
        hand.remove(number+1);
    }

    /**
     *
     * @param register the slot of the register to reset. Must start by 0
     */
    public void resetRegisterCard(int register){
        int i=0;
        while(i<hand.size()){
            if(hand.get(i)== null){
                hand.add(i, registers[register]);
                hand.remove(i+1);
            }
            i++;
        }
        registers[register] = null;
    }

    /**
     * removes all remaining programming cards from hand and into the discarded pile
     */
    public void discardRestOfHand() {
        for(int i=0; i<hand.size();i++){
            Card card = hand.get(i);
            if(card instanceof Programmingcard){
                discarded.add(card);
                hand.remove(i);
            }
        }
    }
    //also increments registerTrack
    public void addToRegister(String cardString){
        if(registerTrack == 5){
            System.out.println("register full for " + name);
            return;
        }

        Card card = getCardFromHand(cardString);
        registers[registerTrack++] = card;
        if(registerTrack==4){
            for(Card cardsae : registers){
                System.out.println(cardsae);
            }
        }
    }
    public int getRegisterSize(){
        return registerTrack;
    }

    /**
     *
     * @param type the type of the card to look for within the hand
     * @return the first occurence of the card from that type to put into the register
     */
    private Card getCardFromHand(String type){
        Card card = null;
        int i=0;
        while(i<hand.size()){
            if(hand.get(i).toString().equals(type)){
                card = hand.get(i);
                hand.remove(i);
            }
            i++;
        }
        return card;
    }

    public ArrayList<String> getHandasStrings() {
        ArrayList<String> cards = new ArrayList<>();

        for (Card card:
                hand) {
            cards.add(card.toString());
        }
        return cards;
    }
}
