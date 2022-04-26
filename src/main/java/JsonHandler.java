import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonHandler {
    Operations jsonToConnnection(String filename) {
        Gson gson = new Gson();
        try {
            FileReader f = new FileReader(filename);
            Operations operations = gson.fromJson(f, Operations.class);
            System.out.println(operations.toString());
            return operations;
        } catch (FileNotFoundException | JsonIOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        JsonHandler jsonHandler = new JsonHandler();
        Operations c = jsonHandler.jsonToConnnection("esempio_json.json");
        System.out.println(c.toString());
    }
}
