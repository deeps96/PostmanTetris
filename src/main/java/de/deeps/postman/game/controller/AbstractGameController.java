package de.deeps.postman.game.controller;

import de.deeps.postman.app.controller.Controller;
import de.deeps.postman.game.model.Game;
import javafx.beans.property.SimpleObjectProperty;
import lombok.AccessLevel;
import lombok.Setter;

public abstract class AbstractGameController extends Controller {

    @Setter(AccessLevel.PRIVATE) private SimpleObjectProperty<Game> game;

    AbstractGameController() {
        setGame(new SimpleObjectProperty<>());
    }

    @Override
    protected void initialize() {
        addGameChangeListener();
        bindGame();
    }

    private void addGameChangeListener() {
        getBasicGame().addListener((observable, oldValue, newValue) -> updateGameBindings());
    }

    //abstract
    protected abstract void updateGameBindings();

    //beans
    private void bindGame() {
        getBasicGame().bind(getMain().getModel().getCurrentGame());
    }

    //accessing
    protected Game getGame() {
        return getBasicGame().get();
    }

    SimpleObjectProperty<Game> getBasicGame() {
        return game;
    }
}
