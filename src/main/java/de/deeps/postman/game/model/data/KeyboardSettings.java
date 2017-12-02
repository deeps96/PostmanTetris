package de.deeps.postman.game.model.data;

import de.deeps.postman.utils.JsonConverter;
import javafx.scene.input.KeyCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class hold all keyboard values which can get changed by the user through the settings scene.
 * The values are stored in (and loaded from) a JSON-File.
 * Assumption: 1:1 assignment between userAction and keyCode.
 */
public class KeyboardSettings {

    @Getter @Setter private HashMap<UserAction, KeyCode> keyMap;

    //convenience
    public static KeyboardSettings loadFromJsonFile(String jsonFilePath){
        return JsonConverter.jsonInputStreamToObject(jsonFilePath, KeyboardSettings.class);
    }

    public void saveAsJson(String jsonFilePath) throws IOException {
        JsonConverter.writeObjectToJsonFile(self(), jsonFilePath);
    }

    public KeyCode getKeyCodeForUserAction(UserAction userAction){
        //containsKey-check not required -> returns null per default
        return getKeyMap().get(userAction);
    }

    public boolean changeKeyCodeForUserAction(UserAction userAction, KeyCode keyCode){
        if (!isKeyCodeValid(keyCode)){
            return false;
        }
        getKeyMap().put(userAction, keyCode);
        return true;
    }

    public UserAction getUserActionForKeyCode(KeyCode keyCode){
        for (Map.Entry<UserAction, KeyCode> entry : getKeyMap().entrySet()){
            if (entry.getValue().equals(keyCode)){
                return entry.getKey();
            }
        }
        return null;
    }

    //conditionals
    private boolean isKeyCodeValid(KeyCode keyCode){
        return keyCode != null;
    }

    //accessing
    private KeyboardSettings self(){
        return this;
    }

}
