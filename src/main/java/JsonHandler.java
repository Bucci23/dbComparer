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
            return gson.fromJson(f, Operations.class);
        } catch (FileNotFoundException | JsonIOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
