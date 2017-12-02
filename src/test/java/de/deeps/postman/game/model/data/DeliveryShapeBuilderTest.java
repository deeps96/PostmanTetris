package de.deeps.postman.game.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryShapeBuilderTest {

    /**
     * Verify that for each DeliverShape there is a matching instance of AbstractDeliveryShape.
     */
    @Test
    void getShapeByName() {
        for (DeliveryShape deliveryShape : DeliveryShape.values()){
            assertNotNull(DeliveryShapeBuilder.buildShapeByName(deliveryShape));
        }
    }

}