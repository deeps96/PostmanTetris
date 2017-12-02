package de.deeps.postman.app.model;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

class FXMLLayout {

    @Getter @Setter(AccessLevel.PRIVATE) private Node rootNode;
    @Getter @Setter(AccessLevel.PRIVATE) private Object controller;
    @Getter @Setter(AccessLevel.PRIVATE) private Scene sceneOfRootNode;

    FXMLLayout(Parent rootNode, Object controller) {
        setRootNode(rootNode);
        setController(controller);
        setSceneOfRootNode(new Scene(rootNode));
    }
}
