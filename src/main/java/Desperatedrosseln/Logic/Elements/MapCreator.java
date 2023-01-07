package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Local.Protocols.HelloClient;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Logic.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

public class MapCreator {

    public static void main(String args[]) throws IOException {

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Message> messageAdapter = moshi.adapter(Message.class);
        JsonAdapter<HelloClient> helloCLientAdapter = moshi.adapter(HelloClient.class);

        HelloClient hc = new HelloClient("0.1");
        String x = helloCLientAdapter.toJson(hc);

        Message msg = new Message("HelloClient",x);

        String Y = messageAdapter.toJson(msg);

        /////////////////////////////////////
        Message newMsg = messageAdapter.fromJson(Y);

        if(newMsg.getMessageType().equals("HelloClient")){
            System.out.println("HelloClient msg received");

            HelloClient newHc = helloCLientAdapter.fromJson(newMsg.getMessageBody());

            System.out.println(newHc.getProtocol());
        }

        Position antennaPos = new Position(1, 7);
        BoardElement bb = new Antenna(DIRECTION.UP, antennaPos);
        GameMap map = new GameMap();
        map.setTile(antennaPos, new BoardElement());
        for(int i= 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(map.getGameMap().get(i).get(j));
            }
            System.out.println(); }

    }


}

