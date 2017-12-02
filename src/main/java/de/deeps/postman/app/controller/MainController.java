package de.deeps.postman.app.controller;

import com.jfoenix.controls.JFXButton;
import de.deeps.postman.app.model.FXMLDictionary;
import javafx.fxml.FXML;
import lombok.AccessLevel;
import lombok.Getter;

public class MainController extends Controller{

    @FXML
    @Getter(AccessLevel.PRIVATE) private JFXButton startNewGameButton, settingsButton, exitButton, aboutButton;

    //initialization
    @Override
    protected void initialize() {
        addStartNewGameListener();
        addSettingsListener();
        addExitListener();
        addAboutListener();
    }

    //event handling
    private void addExitListener() {
        getExitButton().setOnAction(event -> System.exit(0));
    }

    private void addSettingsListener() {
        getSettingsButton().setOnAction(event -> getMain().showScene(FXMLDictionary.Layout.SETTINGS));
    }

    private void addStartNewGameListener() {
        getStartNewGameButton().setOnAction(event -> startNewGame());
    }

    private void startNewGame(){
        getMain().getModel().createNewGame();
        getMain().showScene(FXMLDictionary.Layout.GAME);
        getMain().getModel().getCurrentGame().get().startGame();
    }

    private void addAboutListener() {
        getAboutButton().setOnAction(event -> getMain().showScene(FXMLDictionary.Layout.ABOUT));
    }

}
