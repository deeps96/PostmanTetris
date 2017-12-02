package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.data.GameState;
import javafx.beans.property.SimpleLongProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Timer extends PipeComponent {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private long previousTimestamp;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private GameState previousGameState;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleLongProperty time;

    public Timer(Game game) {
        super(game);
        setTime(new SimpleLongProperty());
    }

    @Override
    protected void handle() {
        if (isGameRunning() && wasGameAlreadyRunning()) {
            incrementTime(getNow() - getPreviousTimestamp());
        }
        setPreviousGameState(getGame().getGameState().get());
        setPreviousTimestamp(getNow());
    }

    //accessing
    private void incrementTime(long increment) {
        getTime().set(getTime().get() + increment);
    }

    //conditionals
    private boolean isGameRunning() {
        return GameState.RUNNING.equals(getGame().getGameState().get());
    }

    private boolean wasGameAlreadyRunning() {
        return getPreviousGameState() != null && getPreviousGameState().equals(getGame().getGameState().get());
    }

}
