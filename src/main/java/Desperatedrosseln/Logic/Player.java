package Desperatedrosseln.Logic;

import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Elements.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int ID;
    private int next_checkpoint = 1;
    private Robot robot;
    private List<Card> deck = new ArrayList<>(20);
    private boolean ready = false;

    public List<Card> getHand() {
        return hand;
    }

    private int energyReserve = 5;
    private List<Card> hand = new ArrayList<>();
    private Card[] register = new Card[5];
    private int registerTrack = 0;
    private List<Card> discarded = new ArrayList<>();
    private ArrayList<String> cardsYouGotNow = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger();
    ArrayList<Card> upgrades = new ArrayList<>();

    public Player() {

    }

    private int distance;

    public Card[] getRegister() {
        return register;
    }

    public int getRegisterTrack() {
        return registerTrack;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public Card getRegisterIndex(int index) {
        return register[index];
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

    public Player(String name) {
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
    public Card drawCardFromDeck() {
        Card card = deck.get((deck.size() - 1));
        deck.remove(deck.size() - 1);
        return card;
    }

    /**
     * draws the hand of the player during the programming phase
     *
     * @return 1 if discarded pile wasn't needed, or 2 otherwise
     */
    public int programmingPhase() {
        DeckHelper.buildDeck(deck);
        if (deck.size() >= 9) {
            for (int i = 0; i < 9; i++) {
                hand.add(drawCardFromDeck());
            }
            return 1;
        } else {
            for (int i = 0; i < deck.size(); i++) {
                hand.add(drawCardFromDeck());
            }
            //discarded = CardUtils.shuffle(discarded);
            for (int j = 0; j < 20; j++) {
                deck.add(takeCardFromDiscarded());
            }
            shuffleDeck();
            while (hand.size() <= 9) {
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

    private Card takeCardFromDiscarded() {
        //assert size >0
        Card card = discarded.get((discarded.size() - 1));
        discarded.remove(discarded.size() - 1);
        return card;
    }

    void shuffleDeck() {
        DeckHelper.shuffleCards(deck);
    }

    public void buyCard(Card card) {

    }

    public int getNextCheckPoint() {
        return next_checkpoint;
    }

    public void setNextCheckPoint() {
        next_checkpoint++;
    }

    public void chooseProgrammingCards(Card[] cards) {
        //assert cards size == 5
        register = cards;
    }

    /**
     * sets a specified register slot with a card from the hand and deletes it from the hand
     * replacing it with an empty slot
     *
     * @param number   the position of the card in the hand
     * @param register the register to put the card in
     */
    public void selectCard(int number, int register) {
        this.register[register] = hand.get(number);
        hand.add(number, null);
        hand.remove(number + 1);
    }

    /**
     * @param register the slot of the register to reset. Must start by 0
     */
    public void resetRegisterCard(int register) {
        int i = 0;
        logger.warn("The Hand before the Card was put back: " + hand);
        while (i < hand.size()) {
            if (hand.get(i) == null) {
                hand.set(i, this.register[register]);
                break;
            } else {
                i++;
            }
        }
        logger.warn("The Hand after the Card was put back: " + hand);
    }

    /**
     * removes all remaining programming cards from hand and into the discarded pile
     */
    public void discardRestOfHand() {
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (card instanceof Programmingcard) {
                discarded.add(card);
                hand.remove(i);
            }
        }
    }

    /**
     * @param cardString (can be null or any other cardtype): is the type of card to be added to the ...
     * @param register   <-
     *                   also increments registerTrack
     */

    public void addToRegister(String cardString, int register) {
        Card card;

        if (cardString.equals("null")) {
            card = null;
            //new
            resetRegisterCard(register);
        } else {
            card = getCardFromHand(cardString);
        }
        this.register[register] = card;
        int cardsnotnull = 0;

        for (Card cardae : this.register) {
            if (cardae != null) {
                cardsnotnull++;
            }
        }
        registerTrack = cardsnotnull;
        logger.info(" registertrack: " + registerTrack + " ,ID: " + ID);
    }

    public int getRegisterSize() {
        return register.length;
    }

    /**
     * @param type the type of the card to look for within the hand
     * @return the first occurence of the card from that type to put into the register
     */
    private Card getCardFromHand(String type) {
        Card card = null;
        int i = 0;
        while (i < hand.size()) {
            if (hand.get(i) != null && hand.get(i).toString().equals(type)) {
                card = hand.get(i);
                hand.set(i, null);
                break;
            } else {
                i++;
            }
        }
        logger.debug("Karte die von der Hand genommen wurde: " + card);
        return card;
    }

    public void removeCardFromHand(String type) {
        ///ToDo: set card to null
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).toString().equals(type)) {
                discarded.add(hand.get(i));
                hand.set(i,null);
                return;
            }
        }


    }

    public ArrayList<String> getHandAsStrings() {
        ArrayList<String> cards = new ArrayList<>();

        for (Card card :
                hand) {
            if(card != null){
                cards.add(card.toString());
            }
        }
        return cards;
    }

    public boolean checkHandContainsCard(String cardString) {
        for (Card card :
                hand) {
            if(card != null){
                if (card.toString().equals(cardString)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkRegisterContainsCard(String cardString) {
        for (Card card :
                register) {
            if(card != null){
                if (card.toString().equals(cardString)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setReady(boolean status) {
        this.ready = status;
    }

    public boolean isReady() {
        return this.ready;
    }

    @Override
    public String toString() {
        return "[" + name + "|" + ID + "]";
    }

    public void addUpgrade(Card upgrade) {
        upgrades.add(upgrade);
    }

    public ArrayList<Card> getUpgrades() {
        return upgrades;
    }

    public void removeUpgrade(String card) {
        for (Card curr : upgrades) {
            UpgradeCard upgradeCard = (UpgradeCard) curr;
            if (upgradeCard.toString().equals(card)) {
                upgrades.remove(curr);
                return;
            }
        }

        System.out.println("cannot remove upgrade card");
    }

    public int getEnergyReserve() {
        return energyReserve;
    }

    public void setEnergyReserve(int energyReserve) {
        this.energyReserve = energyReserve;
    }

    public void addToEnergyReserve(int i) {
        this.energyReserve += i;
    }

    public boolean checkUpgrade(String upgrade) {
        for (Card curr : upgrades) {
            UpgradeCard upgradeCard = (UpgradeCard) curr;
            if (upgradeCard.toString().equals(upgrade)) {
                return true;
            }
        }
        return false;
    }

    public int calculateDistance(Position pos1, Position pos2) {
        distance = Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY());
        return distance;
    }

    public void resetRound() {

        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i) != null){
                discarded.add(hand.get(i));
                hand.set(i,null);
            }
        }
        registerTrack = 0;
        for (int i = 0; i < register.length; i++) {
            if(register[i] != null){
                discarded.add(register[i]);
                register[i] = null;
            }
        }

    }
}
