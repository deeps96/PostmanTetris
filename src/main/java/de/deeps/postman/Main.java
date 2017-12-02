package de.deeps.postman;

import de.deeps.postman.app.model.FXMLDictionary;
import de.deeps.postman.app.model.Model;
import de.deeps.postman.app.model.SingleLocale;
import de.deeps.postman.app.model.StageFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is the main application.
 * It´s responsible for managing the appearance (layout) off the app and implements the applications life cycle.
 */
public class Main extends Application  {

    //constants
    @Getter(AccessLevel.PRIVATE)
    private static final FXMLDictionary.Layout START_SCENE_LAYOUT = FXMLDictionary.Layout.MAIN;
    @Getter(AccessLevel.PRIVATE) private static final String LOCALIZATION_PATH = "localization.loc";

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private FXMLDictionary fxmlDictionary;
    @Getter @Setter(AccessLevel.PRIVATE) private Model model;
    @Getter @Setter(AccessLevel.PRIVATE) private Stage stage;

    //initialization
    private void initialize(Stage primaryStage) throws IOException {
        loadDefaultLocale();

        setModel(new Model());
        setFxmlDictionary(new FXMLDictionary(self()));
        initializeStage(primaryStage);
        assignLayoutsToGameLayout();
        showScene(getSTART_SCENE_LAYOUT());
        getStage().show();
    }

    private void assignLayoutsToGameLayout() {
        getFxmlDictionary().getGameController().addNodeToStackPane(
                getFxmlDictionary().getRootNodeOfLayout(FXMLDictionary.Layout.GAME_PAUSED));
        getFxmlDictionary().getGameController().addNodeToStackPane(
                getFxmlDictionary().getRootNodeOfLayout(FXMLDictionary.Layout.GAME_OVER));
    }

    private void initializeStage(Stage primaryStage) {
        setStage(new StageFactory(primaryStage).get()); //let factory just apply defaults for now
    }

    private void loadDefaultLocale() {
        ResourceBundle locale = ResourceBundle.getBundle(getLOCALIZATION_PATH(), Locale.getDefault());
        SingleLocale.set(locale);
    }

    //lifecycle
    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(self()::shutdown));
        initialize(primaryStage);
    }

    private void shutdown(){
        if (getModel().isGameSet() && getModel().getCurrentGame().get().isGameRunning()){
            getModel().getCurrentGame().get().endGame();
        }
    }

    //convenience
    public void showScene(FXMLDictionary.Layout layout){
        getStage().setScene(getFxmlDictionary().getSceneForLayout(layout));
    }

    //accessing
    private Main self(){
        return this;
    }

}
