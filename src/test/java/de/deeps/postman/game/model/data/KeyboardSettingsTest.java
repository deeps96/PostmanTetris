package de.deeps.postman.game.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyboardSettingsTest {

    /**
     * This test ensures, that for each useraction a default key is specified.
     */
    @Test
    void loadFromJsonFile() {
        KeyboardSettings settings = KeyboardSettings.loadFromJsonFile("defaultKeymap.json");
        for (UserAction userAction : UserAction.values()){
            assertNotNull(settings.getKeyCodeForUserAction(userAction));
        }
    }

}