package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.data.Rectangle;

/**
 * This component checks, if the active delivery has ended it´s journey in the last loop, if the top of the
 * trunk has been reached. If there is a parcel, blocking the next delivery from spawning then the game is over.
 * For "blocking" the parcel only has to be located in the top row (not important on which column)
 */
public class CheckGameOver extends PipeComponent{

    public CheckGameOver(Game game) {
        super(game);
    }

    @Override
    public void handle() {
        if (!isActiveDeliverySet() && doesFirstRowIntersectsAPackage()){
            getGame().endGame();
        }
    }

    //conditionals
    private boolean doesFirstRowIntersectsAPackage() {
        Rectangle firstRow = new Rectangle(0, 0, getTrunk().getCanvasWidth().get(), getTrunk().getParcelSize()
                .doubleValue());
        return !getTrunk().getAllDeliveriesIntersectingBounds(firstRow).isEmpty();
    }
}
