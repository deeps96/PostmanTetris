package de.deeps.postman.game.controller;

import com.jfoenix.controls.JFXButton;
import de.deeps.postman.app.model.FXMLDictionary;
import de.deeps.postman.game.model.data.GameState;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;

public class GamePausedController extends AbstractGameController {

    @FXML
    @Getter(AccessLevel.PRIVATE) private JFXButton resumeButton, backToMenuButton;
    @FXML
    @Getter(AccessLevel.PRIVATE) private VBox rootVBox;

    //initialize
    @Override
    protected void initialize() {
        super.initialize();
        getRootVBox().setVisible(false);
        getResumeButton().setOnAction(event -> resume());
        getBackToMenuButton().setOnAction(event -> backToMenu());
    }

    @Override
    protected void updateGameBindings() {
        getGame().getGameState().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue) && GameState.PAUSED.equals(newValue)){
                showPauseMenu();
            }
        });
    }

    //actions
    private void showPauseMenu() {
        getRootVBox().setVisible(true);
    }

    private void backToMenu() {
        getGame().endGame();
        getRootVBox().setVisible(false);
        getMain().showScene(FXMLDictionary.Layout.MAIN);
    }

    private void resume() {
        getRootVBox().setVisible(false);
        getGame().resumeGame();
    }

}
