package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Game;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Score extends PipeComponent {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private RowCollector rowCollector;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleIntegerProperty score;


    public Score(Game game, RowCollector rowCollector) {
        super(game);
        setScore(new SimpleIntegerProperty());
        setRowCollector(rowCollector);
    }

    @Override
    protected void handle() {
        incrementScore(getRowCollector().getRemovedParcels() * getGameParameters().getParcelRemovedScore());
        if (getGame().doesAPipeComponentRequestNewActiveDelivery()) {
            incrementScore(getGameParameters().getDeliveryDroppedScore());
        }
    }

    //accessing
    private void incrementScore(int increment) {
        getScore().set(getScore().get() + increment);
    }
}
