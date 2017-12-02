package de.deeps.postman.app.model;

import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.data.GameParameters;
import de.deeps.postman.game.model.data.Highscore;
import de.deeps.postman.game.model.data.KeyboardSettings;
import javafx.beans.property.SimpleObjectProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class Model {

    //constants
    @Getter(AccessLevel.PRIVATE) private final String
            KEYMAPPING_JSON = "defaultKeymap.json",
            GAME_PARAMETERS_JSON = "gameParameters.json",
            HIGHSCORE_JSON = "highscore.json";

    @Getter @Setter(AccessLevel.PRIVATE) private SimpleObjectProperty<Game> currentGame;
    @Getter @Setter(AccessLevel.PRIVATE) private GameParameters gameParameters;
    @Getter @Setter(AccessLevel.PRIVATE) private Highscore highscore;
    @Getter @Setter(AccessLevel.PRIVATE) private KeyboardSettings keyboardSettings;

    //initialization
    public Model() {
        setCurrentGame(new SimpleObjectProperty<>());
        loadHighscore();
        loadKeymapping();
        loadDefaultGameParameters();
    }

    private void loadHighscore() {
        setHighscore(Highscore.loadFromJson(getHIGHSCORE_JSON()));
    }

    private void loadDefaultGameParameters() {
        setGameParameters(GameParameters.loadFromJsonFile(getGAME_PARAMETERS_JSON()));
    }

    private void loadKeymapping() {
        setKeyboardSettings(KeyboardSettings.loadFromJsonFile(getKEYMAPPING_JSON()));
    }

    //convenience
    public void saveHighscore() throws IOException {
        getHighscore().saveAsJson(getHIGHSCORE_JSON());
    }

    public void saveKeymapping() throws IOException {
        getKeyboardSettings().saveAsJson(getKEYMAPPING_JSON());
    }

    public void createNewGame() {
        getCurrentGame().set(new Game(getGameParameters(), getKeyboardSettings()));
    }

    public boolean isGameSet(){
        return getCurrentGame().isNotNull().get();
    }
}
