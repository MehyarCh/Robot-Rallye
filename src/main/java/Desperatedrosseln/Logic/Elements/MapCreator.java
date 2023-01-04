package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Local.Protocols.HelloClient;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Local.Protocols.MessageAdapter;
import Desperatedrosseln.Logic.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

public class MapCreator {

    public static void main(String args[]) throws IOException {


        //List<?> x = new ArrayList<>();
        Empty e = new Empty();



        Moshi moshi = new Moshi.Builder().add(new MessageAdapter()).build();
        JsonAdapter<Message> messageAdapter = moshi.adapter(Message.class);
        JsonAdapter<HelloClient> helloCLientAdapter = moshi.adapter(HelloClient.class);

        HelloClient hc = new HelloClient("0.1");
        String x = helloCLientAdapter.toJson(hc);
        String json = helloCLientAdapter.toJson(hc);

        Message msg = new Message("HelloClient",x);

        String Y = messageAdapter.toJson(msg);



        Position antennaPos = new Position(1, 7);
       // Tile bb = new Antenna(Direction.UP, antennaPos);
        GameMap map = new GameMap();
       // map.setTile(antennaPos, new Tile());
        for(int i= 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(map.getGameMap().get(i).get(j));
            }
            System.out.println(); }

    }

    void jsonReader(String json){
        json = "[ [[{\"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"},{\"type\": \"Antenna\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"orientations\": [\"right\"]}],[{\"type\": \"StartPoint\",\n" +
                "            \"isOnBoard\": \"Start A\"}]] , [] , [] ]";

        json = json.replaceAll("\\s+","");
        StringBuilder s = new StringBuilder(json);

        int i =0;
        while (i<json.length()){
            ++i;                   //check
            if(json.charAt(i) == '['){
                while (json.charAt(i) != ']'){
                    ++i;
                    if(json.charAt(i) == '['){
                        while (json.charAt(i) != ']'){
                            ++i;
                            if(json.charAt(i) == '{'){
                                StringBuilder t = s;
                                String temp = t.substring(i);
                                while (json.charAt(i) != '}'){
                                    ++i;
                                    System.out.println(getType(temp));
                                    //TODO:


                                }
                            }

                        }
                    }

                }
            }

        }

    }

    String getType(String temp){
        StringBuilder t = new StringBuilder(temp);
        String s = t.substring(7);
        if(temp.startsWith("\"type\":")){
            if(s.startsWith("\"Empty\"")){
                return "Empty";
            } else if (s.startsWith("\"StartPoint\"")) {
                return "StartPoint";
            }else if (s.startsWith("\"Antenna\"")) {
                return "Antenna";
            }
        }
        System.out.println("doesnt start with the given types");
        return "";
    }


}

