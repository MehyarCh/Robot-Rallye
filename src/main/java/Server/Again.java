package Server;

import java.util.List;

public class Again extends Programmingcards{

    @Override
    public void playCard() {

    }

    public static class ConnectionHandler {

        public void connectClient(){

        }

        public void closeConnection(){

        }
    }

    public static class Game {

        private List<Player> players;
        private int phase;
        private Player playing;
        private int distance;

        public void runStep(){

        }

        private int calculateDistance(Position pos1, Position pos2){
            return distance;
        }

        private void activateElements(){

        }

        public Player getNextPlayer(){
            return playing; //ToDo: change to next player
        }

        public void start(){

        }
    }
}
