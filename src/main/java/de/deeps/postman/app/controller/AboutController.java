package de.deeps.postman.app.controller;

import com.jfoenix.controls.JFXButton;
import de.deeps.postman.app.model.FXMLDictionary;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import lombok.AccessLevel;
import lombok.Getter;

import java.net.URL;

public class AboutController extends Controller {

    //constants
    @Getter(AccessLevel.PRIVATE) private static final String CONTENT_HTML = "about.html";

    @FXML
    @Getter(AccessLevel.PRIVATE) private JFXButton backButton;
    @FXML
    @Getter(AccessLevel.PRIVATE) private WebView contentWebView;

    //initialization
    @Override
    protected void initialize() {
        addBackButtonListener();
        loadContent();
    }

    private void loadContent() {
        URL aboutUrl = getClass().getClassLoader().getResource(getCONTENT_HTML());
        if (isAboutUrlValid(aboutUrl)) {
            getContentWebView().getEngine().load(aboutUrl.toExternalForm());
        }
    }

    //event handling
    private void addBackButtonListener() {
        getBackButton().setOnAction(event -> getMain().showScene(FXMLDictionary.Layout.MAIN));
    }

    //conditionals
    private boolean isAboutUrlValid(URL aboutUrl) {
        return aboutUrl != null;
    }
}
