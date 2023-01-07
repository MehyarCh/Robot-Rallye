package Desperatedrosseln.Json.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JsonFileReader {

    public String readFile(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
