package Desperatedrosseln.Local.Protocols;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageAdapter {

    @ToJson
    public String toJson(Message message){
        return "{\"messageType\": \""+message.getMessageType()+"\",\"messageBody\": "+message.getMessageBody()+"}";
    }

    @FromJson
    public Message fromJson(String message) {
        Pattern pattern = Pattern.compile("\\{\"messageType\":\s\"(.*)\",\"messageBody\":\s(.*)}");
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? new Message(matcher.group(1), matcher.group(2)) : null;
    }



}