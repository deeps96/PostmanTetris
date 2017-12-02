package de.deeps.postman.game.model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class ParcelData {

    @Getter @Setter(AccessLevel.PROTECTED) private DeliveryData parentDelivery;
    @Getter private int row, column;
    @Getter @Setter(AccessLevel.PROTECTED) private int weight;
    @Getter @Setter(AccessLevel.PROTECTED) private Rectangle bounds;

    //initialization
    public ParcelData(DeliveryData delivery, int row, int column, int weight) {
        setParentDelivery(delivery);
        setBasicRow(row);
        setBasicColumn(column);
        setWeight(weight);

        setBounds(new Rectangle());
        addChangeListenersForBeans();
    }

    //beans
    private void addChangeListenersForBeans() {
        addChangeListenerForSizeProperty();
        addChangeListenerForDeliveryX();
        addChangeListenerForDeliveryY();
    }

    private void addChangeListenerForDeliveryY() {
        updateY();
        getParentDelivery().getY().addListener((observable, oldValue, newValue) -> updateY(newValue.intValue()));
    }

    private void addChangeListenerForDeliveryX() {
        updateX();
        getParentDelivery().getX().addListener((observable, oldValue, newValue) -> updateX(newValue.intValue()));
    }

    private void addChangeListenerForSizeProperty() {
        updateSize(getParentDelivery().getBasicParcelSize().intValue());
        getParentDelivery().getBasicParcelSize().addListener((observable, oldValue, newValue) -> updateSize(newValue.intValue()));
    }

    //actions
    private void updateSize(int size) {
        setWidth(size);
        setHeight(size);
        updateX();
        updateY();
    }

    private void updateY() {
        updateY(getParentDelivery().getY().get());
    }

    private void updateX(){
        updateX(getParentDelivery().getX().get());
    }

    private void updateX(int parentDeliveryX){
        setX(parentDeliveryX + getColumn() * getWidth());
    }

    private void updateY(int parentDeliveryY){
        setY(parentDeliveryY + getRow() * getHeight());
    }

    // accessors
    private void setX(int x){
        getBounds().setX(x);
    }

    private void setY(int y){
        getBounds().setY(y);
    }

    private void setWidth(int width){
        getBounds().setWidth(width);
    }

    private void setHeight(int height){
        getBounds().setHeight(height);
    }

    private void setBasicRow(int row){
        this.row = row;
    }

    private void setBasicColumn(int column){
        this.column = column;
    }

    //convenience
    public int getX(){
        return (int) getBounds().getX();
    }

    public int getY(){
        return (int) getBounds().getY();
    }

    public int getWidth(){
        return (int) getBounds().getWidth();
    }

    public int getHeight(){
        return (int) getBounds().getHeight();
    }

    public void setRow(int row){
        setBasicRow(row);
        updateY();
    }

}
