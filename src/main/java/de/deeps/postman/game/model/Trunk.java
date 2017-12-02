package de.deeps.postman.game.model;

import de.deeps.postman.game.model.data.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds information about the game field and calculates the size for the parcels.
 * It additionally has several methods to do bounds-checking.
 */
public class Trunk {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private GameData game;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private GameParameters gameParameters;
    @Getter @Setter(AccessLevel.PRIVATE) private NumberBinding parcelSize;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleIntegerProperty canvasWidth, canvasHeight;

    public Trunk(GameData game, GameParameters gameParameters) {
        setGame(game);
        setGameParameters(gameParameters);
        setCanvasWidth(new SimpleIntegerProperty());
        setCanvasHeight(new SimpleIntegerProperty());
        setParcelSizeBean();
    }

    //bean
    private void setParcelSizeBean() {
        setParcelSize(Bindings.min(
                getCanvasWidth().divide(getGameParameters().getGridWidth()),
                getCanvasHeight().divide(getGameParameters().getGridHeight())));
    }

    /**
     * Calculates the bounds of the trunk. This value is the boundary of the inner representation of the field.
     * It´s not necessarily the same as canvasWidth or canvasHeight. (Could be slightly smaller due rounding of numbers)
     * @return The real field boundaries
     */
    //action
    private Rectangle getFieldBounds() {
        return new Rectangle(
                0, -(AbstractDeliveryShape.getMATRIX_SIZE() * getParcelSize().intValue()),
                getGameParameters().getGridWidth() * getParcelSize().intValue(),
                (AbstractDeliveryShape.getMATRIX_SIZE() + getGameParameters().getGridHeight()) * getParcelSize().intValue());
    }

    private int getWidthForPriorityBounds(){
        int border = getGameParameters().getGridWidth() / 2 + 2;
        return border * getParcelSize().intValue();
    }

    /**
     * The height of the trunk is extended, so it additionally contains some space above the trunk.
     * @param delivery - The delivery to check, whether its outside of the trunk or not.
     * @return - True, if its outside of the trunk.
     */
    //convenience
    boolean wouldFallOutTrunk(Delivery delivery) {
        return !delivery.isContainedInBounds(getFieldBounds());
    }

    /**
     * @param priority The priority level of the delivery
     * @return Returns the restricted bounds, where the prioritized delivery can be located.
     */
    public Rectangle getAllowedBoundsForPriority(Priority priority){
        switch (priority){
            case LOW:
                return getLeftFieldBounds();
            case NORMAL:
                return getFieldBounds();
            case HIGH:
                return getRightFieldBounds();
            default:
                return null;
        }
    }

    public List<Delivery> getAllDeliveriesIntersectingBounds(Rectangle bounds) {
        List<Delivery> deliveries = new LinkedList<>();
        for (Delivery delivery : getGame().getDeliveries()){
            if (delivery.isIntersectingBounds(bounds)){
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    public List<Parcel> getAllParcelsOfDeliveriesContainedInBounds(Rectangle bounds, Delivery delivery) {
        return getAllParcelsOfDeliveriesContainedInBounds(bounds, new LinkedList<>(Collections.singletonList
                (delivery)));
    }

    /**
     * @param bounds - The row, where all parcels have to be located in
     * @param deliveries - Acts like a filter, to speed up this method.
     * @return All parcels contained in the specified row, which are part of a delivery specified in deliveries.
     */
    public List<Parcel> getAllParcelsOfDeliveriesContainedInBounds(Rectangle bounds, List<Delivery> deliveries) {
        List<Parcel> parcels = new LinkedList<>();
        for (Delivery delivery : deliveries){
            for (Parcel parcel : delivery.getParcels()){
                if (parcel.isContainedInBounds(bounds)){
                    parcels.add(parcel);
                }
            }
        }
        return parcels;
    }

    /**
     * @param row The bounds of the current row.
     * @return The bounds for the row above.
     */
    public Rectangle getBoundsForRowAbove(Rectangle row){
        return new Rectangle(0, 0, getCanvasWidth().get(), row.getLayoutBounds().getMaxY());
    }

    public List<Parcel> getAllParcelsContainedInBounds(Rectangle bounds){
        return getAllParcelsOfDeliveriesContainedInBounds(bounds, getGame().getDeliveries());
    }

    public List<Delivery> getAllDeliveriesContainedInBounds(Rectangle bounds){
        List<Delivery> deliveries = new LinkedList<>();
        for (Delivery delivery : getGame().getDeliveries()){
            if (delivery.isContainedInBounds(bounds)){
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    /**
     * Calculates the boundaries for the right part of the field, including the middle of the trunk.
     * @return boundaries for the right part of the trunk
     */
    public Rectangle getRightFieldBounds() {
        Rectangle fieldBounds = getFieldBounds();
        int width = getWidthForPriorityBounds();
        int leftBorder = (int) fieldBounds.getWidth() - width;

        fieldBounds.setX(leftBorder);
        fieldBounds.setWidth(width);
        return fieldBounds;
    }

    /**
     * Calculates the boundaries for the left part of the field, including the middle of the trunk.
     * @return boundaries for the left part of the trunk
     */
    public Rectangle getLeftFieldBounds() {
        Rectangle fieldBounds = getFieldBounds();
        fieldBounds.setX(0);
        fieldBounds.setWidth(getWidthForPriorityBounds());
        return fieldBounds;
    }

}
