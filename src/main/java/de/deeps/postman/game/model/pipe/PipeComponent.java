package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.Trunk;
import de.deeps.postman.game.model.data.GameParameters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic component of the game pipe. Offers several helper functions for the extending child components.
 */
public abstract class PipeComponent {

    @Getter @Setter(AccessLevel.PRIVATE) private boolean isNewActiveDeliveryRequested;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PRIVATE) private Delivery activeDelivery;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PRIVATE) private Game game;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PRIVATE) private Long now;

    PipeComponent(Game game) {
        setGame(game);
    }

    public void handle(long now, Delivery activeDelivery){
        setNewActiveDeliveryRequested(false);
        setActiveDelivery(activeDelivery);
        setNow(now);
        handle();
    }

    protected abstract void handle();

    public void start(){ }

    //----- helpers for children classes -----
    GameParameters getGameParameters(){
        return getGame().getGameParameters();
    }

    Trunk getTrunk() { return getGame().getTrunk(); }

    void requestNewActiveDelivery(){
        setNewActiveDeliveryRequested(true);
    }

    //conditionals
    boolean isActiveDeliverySet() { return getActiveDelivery() != null; }

    boolean isNewDeliveryRequested(){
        return getGame().doesAPipeComponentRequestNewActiveDelivery();
    }

}
