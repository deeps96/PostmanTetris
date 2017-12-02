package de.deeps.postman.app.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class Keyboard {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) HashMap<KeyCode, Boolean> pressedKeys;

    //initialization
    public Keyboard() {
        setPressedKeys(new HashMap<>());
    }

    //convenience
    public void updateKeys(KeyEvent event){
        if (isKeyTypedEvent(event)){
            return;
        }
        getPressedKeys().put(event.getCode(), isKeyPressEvent(event));
    }

    public boolean isKeyPressed(KeyCode keyCode){
        return getPressedKeys().containsKey(keyCode) && getPressedKeys().get(keyCode);
    }

    //conditionals
    private boolean isKeyPressEvent(KeyEvent event){
        return event.getEventType().equals(KeyEvent.KEY_PRESSED);
    }
    private boolean isKeyTypedEvent(KeyEvent event){
        return event.getEventType().equals(KeyEvent.KEY_TYPED);
    }
}
