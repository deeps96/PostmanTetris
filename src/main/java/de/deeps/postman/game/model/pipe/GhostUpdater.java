package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Updates the ghost for the active delivery on request.
 */
public class GhostUpdater extends PipeComponent {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private boolean requestUpdate;

    public GhostUpdater(Game game) {
        super(game);
        setRequestUpdate(false);
    }

    @Override
    public void handle() {
        if (isActiveDeliverySet() && isRequestUpdate()){
            updateGhost();
        }
    }

    //actions
    private void updateGhost() {
        Delivery ghost = getActiveDelivery().createGhost();
        ghost.setGame(getGame());
        ghost.getBasicParcelSize().bind(getTrunk().getParcelSize());
        while (ghost.fall()){} //fall until collision
        getGame().setGhostForActiveDelivery(ghost);
        setRequestUpdate(false);
    }

    //convenience
    void requestUpdate(){
        setRequestUpdate(true);
    }

}
