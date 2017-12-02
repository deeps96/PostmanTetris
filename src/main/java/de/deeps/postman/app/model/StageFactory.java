package de.deeps.postman.app.model;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * This factory is used to configure the appearance of the applications stage.
 */
public class StageFactory {

    //constants
    @Getter(AccessLevel.PRIVATE) private final String DEFAULT_ICON_PATH = "images/icon.png";
    @Getter(AccessLevel.PRIVATE) final int DEFAULT_WIDTH = 845, DEFAULT_HEIGHT = 964;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Stage stage;

    //initialization
    public StageFactory(Stage stage) {
        setStage(stage);
        applyDefaults();
    }

    private void applyDefaults() {
        withApplicationTitle(SingleLocale.get()
                .getString("applicationTitle"));
        withDisabledResize();
        withProgramIcon(getDEFAULT_ICON_PATH());
        withDimensions(getDEFAULT_WIDTH(), getDEFAULT_HEIGHT());
    }

    //convencience
    public StageFactory withDimensions(int width, int height){
        getStage().setMaxWidth(width);
        getStage().setMaxHeight(height);
        return self();
    }

    public StageFactory withProgramIcon(String iconFilePath){
        getStage().getIcons().add(
                new Image(getClass().getClassLoader()
                        .getResourceAsStream(iconFilePath)));
        return self();
    }

    public StageFactory withApplicationTitle(String applicationTitle){
        getStage().setTitle(applicationTitle);
        return self();
    }

    public StageFactory withDisabledResize(){
        getStage().setResizable(false);
        return self();
    }

    public Stage get(){
        return getStage();
    }

    //accesssing
    private StageFactory self(){
        return this;
    }
}
