package de.deeps.postman.game.model.data;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.Parcel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds the data field and basic data methods for the Delivery object.
 */
public class DeliveryData {

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private AbstractDeliveryShape deliveryShape;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private boolean isDeliveryShapeValid;
    @Getter @Setter(AccessLevel.PROTECTED) private Delivery isGhostFor;
    @Getter @Setter(AccessLevel.PROTECTED) private DeliveryService deliveryService;
    @Getter(AccessLevel.PROTECTED) @Setter private Game game;
    @Getter @Setter(AccessLevel.PROTECTED) private int column, row, weight;
    @Getter @Setter(AccessLevel.PROTECTED) private Priority priority;
    @Getter private SimpleIntegerProperty x, y;
    @Setter(AccessLevel.PROTECTED) private SimpleIntegerProperty parcelSize;
    @Getter @Setter(AccessLevel.PROTECTED) private SimpleListProperty<Parcel> parcels;

    //initialization
    public DeliveryData(DeliveryService deliveryService, AbstractDeliveryShape deliveryShape, int weightPerParcel,
                        int row, int column, Delivery isGhostFor, Priority priority) {
        setPriority(priority);
        setIsGhostFor(isGhostFor);
        setDeliveryShapeValid(true);
        setRow(row);
        setColumn(column);
        setBasicX(new SimpleIntegerProperty());
        setBasicY(new SimpleIntegerProperty());
        setParcelSize(new SimpleIntegerProperty());
        setDeliveryService(deliveryService);
        setDeliveryShape(deliveryShape);
        addChangeListenerForParcelSize();
        initializeParcels(weightPerParcel);
    }

    private void initializeParcels(int weightPerParcel) {
        setParcels(new SimpleListProperty<>(FXCollections.observableArrayList()));
        addParcelsChangeListener();
        createParcels(weightPerParcel);
    }

    protected void createParcels(int weightPerParcel) {
        getParcels().clear();
        getDeliveryShape().forEachEnabledCellDo((iRow, iColumn) -> getParcels().add(new Parcel(self(), iRow, iColumn,
                weightPerParcel)));
    }

    //beans
    private void addParcelsChangeListener() {
        getParcels().addListener((ListChangeListener<Parcel>) c -> updateWeight());
    }

    private void addChangeListenerForParcelSize() {
        getBasicParcelSize().addListener((observable, oldValue, newValue) -> updatePosition());
    }

    //action
    private void updateWeight() {
        int newWeight = 0;
        for (Parcel parcel : getParcels()){
            newWeight += parcel.getWeight();
        }
        setWeight(newWeight);
    }

    private void updatePosition() {
        updateX();
        updateY();
    }

    protected void updateY() {
        setY(getRow() * getParcelSize());
    }

    protected void updateX(){
        setX(getColumn() * getParcelSize());
    }

    //accessing
    private void setBasicX(SimpleIntegerProperty x){
        this.x = x;
    }

    private void setBasicY(SimpleIntegerProperty y){
        this.y = y;
    }

    protected boolean isGameSet(){
        return getGame() != null;
    }

    private DeliveryData self(){
        return this;
    }

    private int getParcelSize(){
        return getBasicParcelSize().intValue();
    }

    //convenience
    public void setX(int x){
        getX().set(x);
    }

    public void setY(int y){
        getY().set(y);
    }

    public SimpleIntegerProperty getBasicParcelSize(){
        return parcelSize;
    }

    protected boolean isGhost(){
        return getIsGhostFor() != null;
    }

    public void remove(Parcel parcel) {
        setDeliveryShapeValid(false);
        getParcels().remove(parcel);
    }

    public boolean hasParcels() {
        return !getParcels().isEmpty();
    }

    protected Parcel getParcelAt(int row, int column){
        for (Parcel parcel : getParcels()){
            if (parcel.getRow() == row && parcel.getColumn() == column){
                return parcel;
            }
        }
        return null;
    }


}
