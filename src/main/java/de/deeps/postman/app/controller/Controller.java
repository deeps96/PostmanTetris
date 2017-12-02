package de.deeps.postman.app.controller;

import de.deeps.postman.Main;
import de.deeps.postman.app.model.Keyboard;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Controller implements EventHandler<KeyEvent> {

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PRIVATE) private Keyboard keyboard;
    @Getter(AccessLevel.PROTECTED) Main main;

    //initialization
    protected Controller() {
        setKeyboard(new Keyboard());
    }

    protected abstract void initialize();

    //conditionals
    private boolean isMainSet(){
        return main != null;
    }

    //accessing
    private void setBasicMain(Main main){
        this.main = main;
    }

    //event handling
    @Override
    public void handle(KeyEvent event) {
        getKeyboard().updateKeys(event);
    }

    //convenience
    public void setMain(Main main){
        setBasicMain(main);
        if (isMainSet()) {
            initialize();
        }
    }

}
