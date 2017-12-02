package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.data.AbstractDeliveryShape;
import de.deeps.postman.game.model.data.DeliveryQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Spawns a new random delivery if none is that and the game running.
 */
public class DeliverySpawn extends PipeComponent {

    @Getter(AccessLevel.PRIVATE) private static final int START_ROW = -3;

    @Getter @Setter(AccessLevel.PRIVATE) private boolean spawnedNewDelivery;
    @Getter @Setter(AccessLevel.PRIVATE) private DeliveryQueue deliveryQueue;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private GhostUpdater ghostUpdater;

    public DeliverySpawn(Game game, GhostUpdater ghostUpdater) {
        super(game);
        setGhostUpdater(ghostUpdater);
        setDeliveryQueue(new DeliveryQueue(
                getGameParameters().getDeliveryWeightLimit(),
                getSTART_ROW(),
                Math.round(getGameParameters().getGridWidth() / 2.0f - AbstractDeliveryShape.getMATRIX_SIZE() /
                        2.0f),
                getGameParameters().isEnablePriority()));
    }

    @Override
    public void handle() {
        setSpawnedNewDelivery(false);
        if (!isActiveDeliverySet()){
            spawnNewDelivery();
        }
    }

    private void spawnNewDelivery() {
        Delivery randomDelivery = getDeliveryQueue().pollNext();
        randomDelivery.setGame(getGame());
        randomDelivery.getBasicParcelSize().bind(getTrunk().getParcelSize());
        getGame().setActiveDelivery(randomDelivery);
        getGame().getDeliveries().add(randomDelivery);
        getGhostUpdater().requestUpdate();
        setSpawnedNewDelivery(true);
    }

}
