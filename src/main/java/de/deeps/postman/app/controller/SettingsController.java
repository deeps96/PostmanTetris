package de.deeps.postman.app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import de.deeps.postman.app.model.FXMLDictionary;
import de.deeps.postman.app.model.SingleLocale;
import de.deeps.postman.app.view.JFXDialogFactory;
import de.deeps.postman.game.model.data.KeyboardSettings;
import de.deeps.postman.game.model.data.UserAction;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsController extends Controller {

    @FXML
    @Getter(AccessLevel.PRIVATE) private JFXButton backButton, moveLeftButton, moveRightButton, rotateButton, dropButton, pauseButton;
    @FXML
    @Getter(AccessLevel.PRIVATE) private JFXToggleButton enablePriorityToggleButton, enableBalanceToggleButton;
    @FXML
    @Getter(AccessLevel.PRIVATE) private StackPane rootStackPane;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private HashMap<UserAction, JFXButton> buttonMap;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private KeyboardSettings settings;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private UserAction requestKeyUpdate;

    //initialization
    @Override
    protected void initialize() {
        mapButtons();
        setSettings(getMain().getModel().getKeyboardSettings());
        updateButtonLabels();
        addBackButtonListener();
        addKeyChangeListeners();
        addPriorityChangeListener();
        addBalanceChangeListener();
    }

    private void addBalanceChangeListener() {
        getEnableBalanceToggleButton().setSelected(getMain().getModel().getGameParameters().isEnableBalance());
        getEnableBalanceToggleButton().setOnAction(event -> getMain().getModel().getGameParameters().triggerEnableBalance());
    }

    private void mapButtons() {
        setButtonMap(new HashMap<>());
        getButtonMap().put(UserAction.MOVE_LEFT, getMoveLeftButton());
        getButtonMap().put(UserAction.MOVE_RIGHT, getMoveRightButton());
        getButtonMap().put(UserAction.ROTATE, getRotateButton());
        getButtonMap().put(UserAction.DROP, getDropButton());
        getButtonMap().put(UserAction.PAUSE, getPauseButton());
    }

    //actions
    private void addPriorityChangeListener() {
        getEnablePriorityToggleButton().setSelected(getMain().getModel().getGameParameters().isEnablePriority());
        getEnablePriorityToggleButton().setOnAction(event -> getMain().getModel().getGameParameters().triggerEnablePriority());
    }

    private void updateButtonLabels() {
        for (Map.Entry<UserAction, JFXButton> entry : getButtonMap().entrySet()){
            updateButtonLabel(entry.getValue(), getSettings().getKeyCodeForUserAction(entry.getKey()));
        }
    }

    /**
     * This method updates the text of a button using the following form: ButtonText [KeyCode]
     * Example: Move Left [LEFT]
     * @param button - The button the update function should be applied.
     * @param keyCode - The new keycode for the button.
     */
    private void updateButtonLabel(JFXButton button, KeyCode keyCode){
        button.setText(getBasicButtonText(button) + " [" + keyCode.toString() + "]");
    }

    private String getBasicButtonText(JFXButton button){
        String basicText = button.getText();
        if (basicText.contains("[")){
            basicText = basicText.split(" \\[")[0];
        }
        return basicText;
    }

    private void requestKeyUpdateForUserAction(UserAction userAction){
        JFXButton button = getButtonMap().get(userAction);
        button.setText(getBasicButtonText(button) + " [" + SingleLocale.get().getString("pressNewKey") + "]");
        getRootStackPane().requestFocus();
        setRequestKeyUpdate(userAction);
    }

    private void updateKeyForUserAction(UserAction requestKeyUpdate, KeyCode code) {
        if (isKeyAlreadyAssigned(requestKeyUpdate, code)){
            showKeyAlreadyAssignedDialog(code);
            return;
        }
        if (!getSettings().changeKeyCodeForUserAction(requestKeyUpdate, code)){
            showSaveErrorDialog(SingleLocale.get().getString("errorKeyCodeInvalid"));
        }
        try {
            getMain().getModel().saveKeymapping();
        } catch (IOException e) {
            showSaveErrorDialog(e.getLocalizedMessage());
        }
        updateButtonLabel(getButtonMap().get(requestKeyUpdate), code);
        getButtonMap().get(requestKeyUpdate).requestFocus();
        unsetRequestKeyUpdate();
    }

    private void showKeyAlreadyAssignedDialog(KeyCode code) {
        String title = SingleLocale.get().getString("error");
        String dismiss = SingleLocale.get().getString("ok");
        String content = SingleLocale.get().getString("keyAlreadyAssigned");
        UserAction userAction = getSettings().getUserActionForKeyCode(code);
        if (!isUserActionSet(userAction)){
            throw new NullPointerException();
        }
        content = content.replace("#key#", code.toString());
        content = content.replace("#action#", userAction.toString());

        new JFXDialogFactory()
                .withTitle(title)
                .withContent(content)
                .withDismiss(dismiss)
                .withStackPane(getRootStackPane())
                .show();
    }

    private void showSaveErrorDialog(String localizedMessage) {
        String title = SingleLocale.get().getString("error");
        String dismiss = SingleLocale.get().getString("ok");
        String content = SingleLocale.get().getString("errorSavingChanges");
        content = content.replace("#errormsg#", localizedMessage);

        new JFXDialogFactory()
                .withTitle(title)
                .withContent(content)
                .withDismiss(dismiss)
                .withStackPane(getRootStackPane())
                .show();
    }

    private void abortKeyUpdateRequest(){
        updateKeyForUserAction(getRequestKeyUpdate(), getSettings().getKeyCodeForUserAction(getRequestKeyUpdate()));
    }

    private void back(){
        if (isRequestingKeyUpdate()){
            abortKeyUpdateRequest();
        }
        getMain().showScene(FXMLDictionary.Layout.MAIN);
    }

    //accessing
    private void unsetRequestKeyUpdate(){
        setRequestKeyUpdate(null);
    }

    //event handling
    private void addBackButtonListener() {
        getBackButton().setOnAction(event -> back());
    }

    private void addKeyChangeListeners() {
        for (Map.Entry<UserAction, JFXButton> entry : getButtonMap().entrySet()){
            addKeyButtonListener(entry.getValue(), entry.getKey());
        }
    }

    private void addKeyButtonListener(JFXButton button, UserAction userAction){
        button.setOnAction(event -> {
            if (isRequestingKeyUpdate()){
                abortKeyUpdateRequest();
            }
            requestKeyUpdateForUserAction(userAction);
        });
    }

    @Override
    public void handle(KeyEvent event) {
        super.handle(event);
        if (isRequestingKeyUpdate() && isKeyPressed(event)){
            updateKeyForUserAction(getRequestKeyUpdate(), event.getCode());
        }
    }

    //conditionals
    private boolean isKeyPressed(KeyEvent event){
        return event.getEventType().equals(KeyEvent.KEY_PRESSED);
    }

    private boolean isRequestingKeyUpdate(){
        return getRequestKeyUpdate() != null;
    }

    private boolean isKeyAlreadyAssigned(UserAction requestKeyUpdate, KeyCode code) {
        return getSettings().getKeyMap().values().contains(code) &&
                !getSettings().getKeyMap().get(requestKeyUpdate).equals(code);
    }

    private boolean isUserActionSet(UserAction userAction){
        return userAction != null;
    }
}
