package de.deeps.postman.game.controller;

import de.deeps.postman.game.model.data.GameState;
import de.deeps.postman.game.view.BalanceCanvas;
import de.deeps.postman.game.view.PreviewCanvas;
import de.deeps.postman.game.view.TrunkCanvas;
import de.deeps.postman.utils.UnitConverter;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;

public class GameController extends AbstractGameController {

    @FXML
    @Getter(AccessLevel.PRIVATE) private BalanceCanvas balanceCanvas;
    @FXML
    @Getter(AccessLevel.PRIVATE) private Label timeLabel, scoreLabel;
    @FXML
    @Getter(AccessLevel.PRIVATE) private PreviewCanvas previewCanvas1, previewCanvas2, previewCanvas3;
    @FXML
    @Getter(AccessLevel.PRIVATE) private StackPane rootStackPane;
    @FXML
    @Getter(AccessLevel.PRIVATE) private TrunkCanvas trunkCanvas;
    @FXML
    @Getter(AccessLevel.PRIVATE) private VBox gameVBox;

    //initialization
    @Override
    protected void initialize() {
        super.initialize();
        getTrunkCanvas().getGame().bind(getBasicGame());
    }

    protected void updateGameBindings() {
        updateBalanceCanvas();
        getGame().setKeyboard(getKeyboard());
        updateTrunkBindings();
        updateTrunkCanvasBindings();
        getGame().getGameState().addListener((observable, oldValue, newValue) -> blurEnabled(GameState.PAUSED.equals
                (newValue) || GameState.OVER.equals(newValue)));
        getScoreLabel().textProperty().bind(getGame().getScore().asString());
        addTimeUpdateListener();
        addGuiUpdateListener();
        addPreviewUpdateListener();
    }

    private void updateBalanceCanvas() {
        if (!getGame().getGameParameters().isEnableBalance()){
            getBalanceCanvas().setVisible(false);
        } else {
            getBalanceCanvas().setVisible(true);
            getBalanceCanvas().setMax(getGame().getGameParameters().getBalanceMaxDifferenceInPercent());
            getBalanceCanvas().getBalanceMinimumReached().bind(getGame().getBalanceMinimumReached());
            getBalanceCanvas().getBalance().bind(getGame().getBalance());
        }
    }

    private void updateTrunkCanvasBindings() {
        getTrunkCanvas().setDrawWeight(getGame().getGameParameters().isEnableBalance());
        getTrunkCanvas().getDeliveries().bind(getGame().getDeliveries());
        getTrunkCanvas().getGhostDelivery().bind(getGame().getGhostForActiveDelivery());
    }

    private void updateTrunkBindings() {
        getGame().getTrunk().getCanvasWidth().bind(getTrunkCanvas().widthProperty());
        getGame().getTrunk().getCanvasHeight().bind(getTrunkCanvas().heightProperty());
    }

    private void addTimeUpdateListener() {
        getGame().getRunningTime().addListener((observable, oldValue, newValue) -> getTimeLabel().setText
                (UnitConverter.convertNSToString(newValue.longValue())));
    }

    private void addPreviewUpdateListener() {
        getGame().getPreviewUpdate().addListener((observable, oldValue, newValue) -> updatePreview());
    }

    private void addGuiUpdateListener() {
        getGame().getGuiUpdate().addListener((observable, oldValue, newValue) -> updateGUI());
    }

    //actions
    private void updateGUI() {
        getTrunkCanvas().redraw();
        getBalanceCanvas().redraw();
    }

    private void updatePreview() {
        getPreviewCanvas1().getPreviewDelivery().set(getGame().getPreview(0));
        getPreviewCanvas2().getPreviewDelivery().set(getGame().getPreview(1));
        getPreviewCanvas3().getPreviewDelivery().set(getGame().getPreview(2));
    }

    private void blurEnabled(boolean isEnabled) {
        getGameVBox().setEffect((isEnabled) ? new GaussianBlur() : null);
    }

    //convenience
    public void addNodeToStackPane(Node childNode) {
        getRootStackPane().getChildren().add(childNode);
    }
}
