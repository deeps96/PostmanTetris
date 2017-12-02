package de.deeps.postman.game.view;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Parcel;
import de.deeps.postman.game.model.data.Rectangle;
import javafx.beans.property.SimpleObjectProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class PreviewCanvas extends AbstractDeliveryCanvas {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int parcelSize, offsetX, offsetY;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Rectangle innerBounds;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleObjectProperty<Delivery> previewDelivery;

    public PreviewCanvas() {
        setPreviewDelivery(new SimpleObjectProperty<>());
        addSizeChangedListener();
        addPreviewDeliveryChangedListener();
    }

    //bean
    private void addSizeChangedListener() {
        updateSize();
        widthProperty().addListener((observable, oldValue, newValue) -> updateSize());
    }

    private void addPreviewDeliveryChangedListener() {
        getPreviewDelivery().addListener((observable, oldValue, newValue) -> { updateSize(); redraw(); });
    }

    //drawing
    @Override
    public void redraw() {
        if (!isDeliveryPackageSet()){
            return;
        }
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        for (Parcel parcel : getPreviewDelivery().get().getParcels()){
            getGraphicsContext2D().drawImage(
                    getLogos().get(parcel.getDeliveryService()),
                    getOffsetX() + (parcel.getColumn() - getInnerBounds().getX()) * getParcelSize(),
                    getOffsetY() + (parcel.getRow() - getInnerBounds().getY()) * getParcelSize(),
                    getParcelSize(), getParcelSize());
        }
    }

    //calculations
    private void updateSize(){
        if (!isDeliveryPackageSet()){
            return;
        }
        setInnerBounds(getInnerBoundsOfDelivery(getPreviewDelivery().get()));
        setParcelSize(getParcelPixelSize(getInnerBounds()));
        setOffsetX(getOffsetX(getParcelSize(), getInnerBounds()));
        setOffsetY(getOffsetY(getParcelSize(), getInnerBounds()));
    }

    private int getOffsetY(int parcelSize, Rectangle innerBounds) {
        return (int) (getHeight() - innerBounds.getHeight() * parcelSize) / 2;
    }

    private int getOffsetX(int parcelSize, Rectangle innerBounds) {
        return (int) (getWidth() - innerBounds.getWidth() * parcelSize) / 2;
    }

    private int getParcelPixelSize(Rectangle innerBounds){
        return (int) Math.min(getWidth() / innerBounds.getWidth(), getHeight() / innerBounds.getHeight());
    }

    private Rectangle getInnerBoundsOfDelivery(Delivery delivery) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Parcel parcel : delivery.getParcels()){
            minX = Math.min(minX, parcel.getColumn());
            minY = Math.min(minY, parcel.getRow()) ;
            maxX = Math.max(maxX, parcel.getColumn());
            maxY = Math.max(maxY, parcel.getRow());
        }
        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    //accessing
    private boolean isDeliveryPackageSet(){
        return getPreviewDelivery().isNotNull().get();
    }

}
