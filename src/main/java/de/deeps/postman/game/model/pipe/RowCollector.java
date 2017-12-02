package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.Parcel;
import de.deeps.postman.game.model.data.AbstractDeliveryShape;
import de.deeps.postman.game.model.data.Rectangle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public class RowCollector extends PipeComponent {

    @Getter @Setter(AccessLevel.PRIVATE) private int removedParcels;

    public RowCollector(Game game) {
        super(game);
    }

    @Override
    protected void handle() {
        setRemovedParcels(0);
        if (isActiveDeliverySet() && isNewDeliveryRequested()){
            checkIfRowIsComplete();
        }
    }

    /**
     * After the activeDelivery has been dropped (cant fall any longer) this methods checks, if a row have been
     * completed by the activeDelivery.
     */
    private void checkIfRowIsComplete() {
        for (Rectangle row : getRowBoundsOfActiveDeliveryForCheck()){
            List<Delivery> deliveries = getTrunk().getAllDeliveriesIntersectingBounds(row);
            if (isParcelCountSameAsGridWidth(row, deliveries)){
                removeParcelsFromDeliveriesContainedInRow(deliveries, row);
                letDeliveriesAboveFall(row);
            }
        }
    }

    /**
     * Returns three (Matrix-Size) row bounds. Each one describes a row, where the activeDelivery landed.
     * @return The three rows, where parcels of the active delivery are located for the active delivery
     */
    private List<Rectangle> getRowBoundsOfActiveDeliveryForCheck() {
        List<Rectangle> rowBoundsToCheck = new LinkedList<>();
        int lowerRow = getActiveDelivery().getRow() + AbstractDeliveryShape.getMATRIX_SIZE();
        int size = getActiveDelivery().getBasicParcelSize().get();
        for (int iRow = getActiveDelivery().getRow(); iRow < lowerRow; iRow++){
            rowBoundsToCheck.add(new Rectangle(0, iRow * size, getTrunk().getCanvasWidth().get(), size));
        }
        return rowBoundsToCheck;
    }

    /**
     * Let all delivery above the specified row fall one unit, since one row got cleared.
     * @param row The bottom row/ border - dividing the deliveries into "toFall" and "notToFall"
     */
    private void letDeliveriesAboveFall(Rectangle row) {
        Rectangle aboveRow = getTrunk().getBoundsForRowAbove(row);
        for (Delivery deliveries : getTrunk().getAllDeliveriesContainedInBounds(aboveRow)){
            deliveries.fall();
        }
    }

    /**
     * Remove all parcels, which are contained in the row and belong to a delivery in the list. If all parcels have
     * been removed from a delivery, the delivery gets removed from the game.
     * @param deliveries Acting as filter
     * @param row - the row, where a parcel has to be located in
     */
    private void removeParcelsFromDeliveriesContainedInRow(List<Delivery> deliveries, Rectangle row) {
        for (Delivery delivery : deliveries){
            removeParcelsContainedInRectangleOfDelivery(delivery, row);
            if (!delivery.hasParcels()){
                removeDelivery(delivery);
            }
        }
    }

    private void removeDelivery(Delivery delivery) {
        getGame().getDeliveries().remove(delivery);
    }

    /**
     * Removes the parcels of a specific delivery, which are contained in a row. After deleting the last one (being
     * in the row, not the last of the delivery), the delivery gets compressed - meaning that the parcels above the
     * deleted row fall one unit.
     * @param delivery - the delivery, where the parcels have to belong to
     * @param row - the row, where the parcel have to be located in
     */
    private void removeParcelsContainedInRectangleOfDelivery(Delivery delivery, Rectangle row) {
        List<Parcel> parcels = getTrunk().getAllParcelsOfDeliveriesContainedInBounds(row, delivery);
        for (int iParcel = 0; iParcel < parcels.size(); iParcel++){
            delivery.remove(parcels.get(iParcel));
            incrementRemovedParcelCount();
            if (iParcel == parcels.size() - 1){
                delivery.compress(parcels.get(iParcel));
            }
        }
    }

    //conditionals
    private boolean isParcelCountSameAsGridWidth(Rectangle row, List<Delivery> deliveries) {
        return getTrunk().getAllParcelsOfDeliveriesContainedInBounds(row, deliveries).size() == getGameParameters()
                .getGridWidth();
    }

    //accessing
    private void incrementRemovedParcelCount(){
        setRemovedParcels(getRemovedParcels() + 1);
    }

}
