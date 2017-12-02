package de.deeps.postman.game.model;

import de.deeps.postman.game.model.data.DeliveryService;
import de.deeps.postman.game.model.data.DeliveryShape;
import de.deeps.postman.game.model.data.DeliveryShapeBuilder;
import de.deeps.postman.game.model.data.Priority;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParcelTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Delivery delivery, otherDelivery;


    @BeforeEach
    void setUp() {
        setDelivery(new Delivery(DeliveryService.AMAZON, DeliveryShapeBuilder.buildShapeByName(DeliveryShape
                .SQUARE_SHAPE), 5, 0, 5, Priority.NORMAL));
        getDelivery().getBasicParcelSize().bind(new SimpleIntegerProperty(40));
        setOtherDelivery(getDelivery().createGhost());
    }

    @Test
    void wouldCollideWith() {
        assert getDelivery().getParcels().get(0).wouldCollideWith(getDelivery());
        assert !getDelivery().getParcels().get(0).wouldCollideWith(getOtherDelivery());
    }

}