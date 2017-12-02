package de.deeps.postman.game.model;

import de.deeps.postman.game.model.data.*;

/**
 * Implements the action methods used by the game for the delivery.
 */
public class Delivery extends DeliveryData{

    //initialization
    public Delivery(DeliveryService deliveryService, AbstractDeliveryShape deliveryShape, int weightPerParcel,
                    Priority priority) {
        this(deliveryService, deliveryShape, weightPerParcel, 0, 0, priority);
    }

    public Delivery(DeliveryService deliveryService, AbstractDeliveryShape deliveryShape, int weightPerParcel, int
            row, int column, Priority priority) {
        this(deliveryService, deliveryShape, weightPerParcel, row, column, null, priority);
    }

    public Delivery(DeliveryService deliveryService, AbstractDeliveryShape deliveryShape, int weightPerParcel, int
            row, int column, Delivery isGhostFor, Priority priority){
        super(deliveryService, deliveryShape, weightPerParcel, row, column, isGhostFor, priority);
    }

    //action
    private boolean move(Runnable moveAction, Runnable undoMoveAction){
        moveAction.run();
        if (!isValidPosition()){
            undoMoveAction.run();
            return false;
        }
        return true;
    }

    private void basicFall(){
        setRow(getRow() + 1);
        updateY();
    }

    private void basicReverseFall(){
        setRow(getRow() - 1);
        updateY();
    }

    private void basicMoveLeft(){
        setColumn(getColumn() - 1);
        updateX();
    }

    private void basicMoveRight(){
        setColumn(getColumn() + 1);
        updateX();
    }

    private void basicRotateClockwise(){
        rotate(getDeliveryShape()::rotateClockwise);
    }

    private void basicRotateAntiClockwise(){
        rotate(getDeliveryShape()::rotateAntiClockwise);
    }

    private void rotate(Runnable rotationMethod){
        //this method would generate new tiles and result into unexpected behavior -> do shapeValid check
        if (!isDeliveryShapeValid() || getParcels().isEmpty()){
            return;
        }
        rotationMethod.run();
        createParcels(getParcels().get(0).getWeight());
    }

    //accessing
    private Delivery self(){
        return this;
    }

    //conditionals
    private boolean isValidPosition(){
        return !isGameSet() || !getGame().wouldCollideAtCurrentPosition(self()) && !getGame().getTrunk().wouldFallOutTrunk
                (self
                ()) && getGame().doesPriorityMatchLocation(self());
    }

    //convenience
    public Delivery createGhost(){
        return new Delivery(null, getDeliveryShape(), 0, getRow(), getColumn(), self(),
                getPriority());
    }

    boolean wouldCollideWith(Delivery delivery){
        for (Parcel parcel : getParcels()){
            if (parcel.wouldCollideWith(delivery)){
                return true;
            }
        }
        return false;
    }

    public boolean fall(){
        return move(this::basicFall, this::basicReverseFall);
    }

    public boolean moveLeft(){
        return move(this::basicMoveLeft, this::basicMoveRight);
    }

    public boolean moveRight(){
        return move(this::basicMoveRight, this::basicMoveLeft);
    }

    public boolean rotate() { return move(this::basicRotateClockwise, this::basicRotateAntiClockwise); }

    boolean isIntersectingBounds(Rectangle bounds){
        for (Parcel parcel : getParcels()){
            if (parcel.getBounds().intersects(bounds)){
                return true;
            }
        }
        return false;
    }

    boolean isContainedInBounds(Rectangle bounds) {
        for (Parcel parcel : getParcels()){
            if (!bounds.contains(parcel.getBounds())){
                return false;
            }
        }
        return true;
    }

    public void compress(Parcel removedParcel) {
        for (int iRow = removedParcel.getRow() - 1; iRow >= 0; iRow--){
            for (int iColumn = 0; iColumn < AbstractDeliveryShape.getMATRIX_SIZE(); iColumn++) {
                Parcel parcel = getParcelAt(iRow, iColumn);
                if (parcel != null){
                    parcel.setRow(iRow + 1);
                }
            }
        }
    }
}
