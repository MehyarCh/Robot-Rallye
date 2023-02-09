package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;

public class PowerUp extends Programmingcard {
    private final int moves = 0;
    @Override
    public String toString() {
        return "PowerUp";
    }

    @Override
    public int getMoves(){
        return moves;
    }
}
