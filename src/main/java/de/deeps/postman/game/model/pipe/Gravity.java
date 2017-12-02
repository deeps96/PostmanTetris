package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Game;
import de.deeps.postman.utils.UnitConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Responsible for letting the active delivery fall one unit per specified time interval.
 */
public class Gravity extends PipeComponent{

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private long lastFallStartTimestamp, lastFallEndTimestamp;

    public Gravity(Game game) {
        super(game);
    }

    @Override
    public void handle() {
        if (isTimeToLetActivePackageFall()){
            checkFall();
        }
    }

    @Override
    public void start() {
        super.start();
        setLastFallEndTimestamp(System.nanoTime() - UnitConverter.convertMSIntoNS(getGameParameters().getPauseBetweenFallInMS()));
    }

    private void checkFall() {
        setLastFallStartTimestamp(getLastFallEndTimestamp() + UnitConverter.convertMSIntoNS(getGameParameters().getPauseBetweenFallInMS()));
        setLastFallEndTimestamp(getNow());
        if (!getActiveDelivery().fall()){
            requestNewActiveDelivery();
        }
    }

    //conditionals
    private boolean isTimeToLetActivePackageFall(){
        return getNow() - getLastFallEndTimestamp() >= UnitConverter.convertMSIntoNS(getGameParameters().getPauseBetweenFallInMS());
    }
}
