package de.deeps.postman.game.model;

import de.deeps.postman.game.model.data.DeliveryService;
import de.deeps.postman.game.model.data.DeliveryShape;
import de.deeps.postman.game.model.data.DeliveryShapeBuilder;
import de.deeps.postman.game.model.data.Priority;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Rectangle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryTest {
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Delivery delivery;

    @BeforeEach
    void setUp() {
        setDelivery(new Delivery(DeliveryService.AMAZON, DeliveryShapeBuilder.buildShapeByName(DeliveryShape.L_SHAPE)
                , 5, 5, 2, Priority.NORMAL));
        getDelivery().getBasicParcelSize().bind(new SimpleIntegerProperty(40));
    }

    @Test
    void weightSum() {
        assertEquals(20, getDelivery().getWeight());
    }

    @Test
    void isPositionCorrectForRowAndColumn(){
        assertEquals(80, getDelivery().getX().get());
        assertEquals(200, getDelivery().getY().get());
    }

    @Test
    void parcelBounds() {
        List<Rectangle> parcelBounds = new LinkedList();
        parcelBounds.add(new Rectangle(120, 200, 40, 40));
        parcelBounds.add(new Rectangle(120, 240, 40, 40));
        parcelBounds.add(new Rectangle(120, 280, 40, 40));
        parcelBounds.add(new Rectangle(160, 280, 40, 40));

        assertParcelPositions(parcelBounds, getDelivery().getParcels());
    }

    @Test
    void wouldCollideWith() {
        Delivery otherDelivery = new Delivery(DeliveryService.DHL, DeliveryShapeBuilder.buildShapeByName
                (DeliveryShape.SQUARE_SHAPE), 5, Priority.NORMAL);
        otherDelivery.getBasicParcelSize().bind(new SimpleIntegerProperty(40));
        otherDelivery.setX(160);
        otherDelivery.setY(200);
        assert !getDelivery().wouldCollideWith(otherDelivery);
        otherDelivery.setX(120);
        assert getDelivery().wouldCollideWith(otherDelivery);
    }

    @Test
    void fall() {
        getDelivery().fall();
        assertEquals(240, getDelivery().getY().get());

        List<Rectangle> parcelBounds = new LinkedList();
        parcelBounds.add(new Rectangle(120, 240, 40, 40));
        parcelBounds.add(new Rectangle(120, 280, 40, 40));
        parcelBounds.add(new Rectangle(120, 320, 40, 40));
        parcelBounds.add(new Rectangle(160, 320, 40, 40));

        assertParcelPositions(parcelBounds, getDelivery().getParcels());

    }

    @Test
    void moveLeft() {
        getDelivery().moveLeft();
        List<Rectangle> parcelBounds = new LinkedList();
        parcelBounds.add(new Rectangle(80, 200, 40, 40));
        parcelBounds.add(new Rectangle(80, 240, 40, 40));
        parcelBounds.add(new Rectangle(80, 280, 40, 40));
        parcelBounds.add(new Rectangle(120, 280, 40, 40));

        assertParcelPositions(parcelBounds, getDelivery().getParcels());
    }

    @Test
    void moveRight() {
        getDelivery().moveRight();
        List<Rectangle> parcelBounds = new LinkedList();
        parcelBounds.add(new Rectangle(160, 200, 40, 40));
        parcelBounds.add(new Rectangle(160, 240, 40, 40));
        parcelBounds.add(new Rectangle(160, 280, 40, 40));
        parcelBounds.add(new Rectangle(200, 280, 40, 40));

        assertParcelPositions(parcelBounds, getDelivery().getParcels());
    }

    @Test
    void rotateClockwise() {
        getDelivery().rotate();
        List<Rectangle> parcelBounds = new LinkedList();
        parcelBounds.add(new Rectangle(80, 240, 40, 40));
        parcelBounds.add(new Rectangle(120, 240, 40, 40));
        parcelBounds.add(new Rectangle(160, 240, 40, 40));
        parcelBounds.add(new Rectangle(80, 280, 40, 40));

        assertParcelPositions(parcelBounds, getDelivery().getParcels());
    }

    private void assertParcelPositions(List<Rectangle> extpectedParcelPositions, List<Parcel> actualParcels){
        Parcel parcel;
        for (int iParcel = 0; iParcel < extpectedParcelPositions.size(); iParcel++) {
            parcel = actualParcels.get(iParcel);
            assertEquals(extpectedParcelPositions.get(iParcel).getX(), parcel.getBounds().getX());
            assertEquals(extpectedParcelPositions.get(iParcel).getY(), parcel.getBounds().getY());
            assertEquals(extpectedParcelPositions.get(iParcel).getWidth(), parcel.getBounds().getWidth());
            assertEquals(extpectedParcelPositions.get(iParcel).getHeight(), parcel.getBounds().getHeight());
        }
    }

}