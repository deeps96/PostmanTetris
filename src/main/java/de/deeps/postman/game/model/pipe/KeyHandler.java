package de.deeps.postman.game.model.pipe;

import de.deeps.postman.app.model.Keyboard;
import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.data.KeyboardSettings;
import de.deeps.postman.game.model.data.UserAction;
import javafx.scene.input.KeyCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.function.BooleanSupplier;

public class KeyHandler extends PipeComponent {
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private GhostUpdater ghostUpdater;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    private HashMap<UserAction, Long> lastUserActionTimestamps;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private KeyboardSettings keyboardSettings;

    public KeyHandler(Game game, GhostUpdater ghostUpdater) {
        super(game);
        setGhostUpdater(ghostUpdater);
        setKeyboardSettings(getGame().getKeyboardSettings());
        setLastUserActionTimestamps(new HashMap<>());
    }

    @Override
    public void handle() {
        if (isActiveDeliverySet()) {
            reactOnMoveUserAction(UserAction.MOVE_LEFT, getActiveDelivery()::moveLeft);
            reactOnMoveUserAction(UserAction.MOVE_RIGHT, getActiveDelivery()::moveRight);
            reactOnMoveUserAction(UserAction.DROP, self()::fall);
            reactOnMoveUserAction(UserAction.ROTATE, getActiveDelivery()::rotate);
        }
        reactOnPause();
    }

    //action

    private boolean fall() {
        boolean success = getActiveDelivery().fall();
        if (!success) {
            requestNewActiveDelivery();
        }
        return success;
    }

    private void reactOnPause() {
        if (isKeyForUserActionPressed(UserAction.PAUSE)) {
            getGame().pauseGame();
        }
    }

    private void reactOnMoveUserAction(UserAction userAction, BooleanSupplier doUserAction) {
        if (isKeyForUserActionPressed(userAction) && isUserActionTimeoutOver(userAction) && isActiveDeliverySet()) {
            getLastUserActionTimestamps().put(userAction, System.currentTimeMillis());
            boolean success = doUserAction.getAsBoolean();
            if (!isDropUserAction(userAction) && success) {
                getGhostUpdater().requestUpdate();
            }
        }
    }

    private boolean isKeyForUserActionPressed(UserAction userAction) {
        return isKeyPressed(getKeyboardSettings().getKeyCodeForUserAction(userAction));
    }

    //accessing
    private Keyboard getKeyboard() {
        return getGame().getKeyboard();
    }

    //accesssing
    private KeyHandler self() {
        return this;
    }

    //conditionals
    private boolean isUserActionTimeoutOver(UserAction userAction) {
        return !getLastUserActionTimestamps().containsKey(userAction) || System.currentTimeMillis() -
                getLastUserActionTimestamps().get(userAction) >= getGameParameters().getUserActionTimeoutInMS();
    }

    private boolean isKeyPressed(KeyCode keyCode) {
        return isKeyboardSet() && getKeyboard().isKeyPressed(keyCode);
    }

    private boolean isKeyboardSet() {
        return getKeyboard() != null;
    }

    private boolean isDropUserAction(UserAction userAction) {
        return userAction.equals(UserAction.DROP);
    }

}
