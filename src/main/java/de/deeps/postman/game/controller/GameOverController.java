package de.deeps.postman.game.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.deeps.postman.app.model.FXMLDictionary;
import de.deeps.postman.app.model.SingleLocale;
import de.deeps.postman.app.view.JFXDialogFactory;
import de.deeps.postman.game.model.data.GameState;
import de.deeps.postman.game.model.data.HighscoreEntry;
import de.deeps.postman.utils.UnitConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class GameOverController extends AbstractGameController {

    @FXML
    @Getter(AccessLevel.PRIVATE) private JFXButton backToMenuButton;
    @FXML
    @Getter(AccessLevel.PRIVATE) private JFXTextField nameTextField;
    @FXML
    @Getter(AccessLevel.PRIVATE) private Label scoreLabel, timeLabel;
    @FXML
    @Getter(AccessLevel.PRIVATE) private StackPane rootStackPane;
    @FXML
    @Getter(AccessLevel.PRIVATE) private TableColumn<HighscoreEntry, Integer> scoreTableColumn;
    @FXML
    @Getter(AccessLevel.PRIVATE) private TableColumn<HighscoreEntry, String> nameTableColumn, timerTableColumn;
    @FXML
    @Getter(AccessLevel.PRIVATE) private TableView<HighscoreEntry> highscoreTableView;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private HighscoreEntry entry;

    //initialization
    @Override
    protected void initialize() {
        super.initialize();
        getRootStackPane().setVisible(false);
        initializeNameTextField();
        setTableCellFactories();
        getHighscoreTableView().setItems(getMain().getModel().getHighscore().getBasicScoreboard());
        addBackToMenuButtonListener();
    }

    private void addBackToMenuButtonListener() {
        getBackToMenuButton().setOnAction(event -> {
            String name = getNameTextField().getText();
            if (isNameValid(name)) {
                saveHighscoreEntryWithName();
            }
            getMain().showScene(FXMLDictionary.Layout.MAIN);
        });
    }

    private void setTableCellFactories() {
        getScoreTableColumn().setCellValueFactory(new PropertyValueFactory<>("score"));
        getNameTableColumn().setCellValueFactory(new PropertyValueFactory<>("name"));
        getTimerTableColumn().setCellValueFactory(new PropertyValueFactory<>("time"));
        getScoreTableColumn().setSortType(TableColumn.SortType.DESCENDING);
        getScoreTableColumn().setSortable(true);
    }

    private void initializeNameTextField() {
        getNameTextField().textProperty().addListener((observable, oldValue, newValue) -> updateTable());
    }

    //bindings
    @Override
    protected void updateGameBindings() {
        getGame().getGameState().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue) && GameState.OVER.equals(newValue)) {
                showGameOverScreen();
            } else if (getRootStackPane().isVisible()) {
                getRootStackPane().setVisible(false);
            }
        });
    }

    //actions
    private void showGameOverScreen() {
        removePreviousNameTextFieldBinding();
        updateEntry();
        getMain().getModel().getHighscore().addScore(getEntry());
        updateView();
        getRootStackPane().setVisible(true);
    }

    private void updateView() {
        getNameTextField().textProperty().bindBidirectional(getEntry().getBasicName());
        getTimeLabel().setText(getEntry().getTime());
        getScoreLabel().setText(getEntry().getScore());
    }

    private void updateEntry() {
        setEntry(new HighscoreEntry(
                SingleLocale.get().getString("anonymPlayer"),
                UnitConverter.convertNSToString(getGame().getRunningTime().get()),
                Integer.toString(getGame().getScore().get())));
    }

    private void removePreviousNameTextFieldBinding() {
        if (isHighscoreEntrySet()){
            getNameTextField().textProperty().unbindBidirectional(getEntry().getBasicName());
        }
    }

    private void saveHighscoreEntryWithName() {
        try {
            getMain().getModel().saveHighscore();
        } catch (IOException e) {
            String error = SingleLocale.get().getString("errorSavingChanges");
            error = error.replace("#errormsg#", e.getLocalizedMessage());
            new JFXDialogFactory()
                    .withStackPane(getRootStackPane())
                    .withDismiss(SingleLocale.get().getString("ok"))
                    .withTitle(SingleLocale.get().getString("error"))
                    .withContent(error)
                    .show();
        }
    }

    private void updateTable() {
        //workaround since table doesnt update the content
        getHighscoreTableView().getColumns().get(0).setVisible(false);
        getHighscoreTableView().getColumns().get(0).setVisible(true);
    }

    //conditionals
    private boolean isNameValid(String name) {
        return name.length() > 0;
    }

    private boolean isHighscoreEntrySet(){
        return getEntry() != null;
    }
}
