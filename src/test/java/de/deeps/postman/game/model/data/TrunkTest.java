package de.deeps.postman.game.model.data;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.Trunk;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrunkTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Game game;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Trunk trunk;

    @BeforeEach
    void setUp(){
        setGame(new Game(GameParameters.loadFromJsonFile("gameParameters.json"), KeyboardSettings.loadFromJsonFile("defaultKeymap.json")));
        setTrunk(new Trunk(getGame(), getGame().getGameParameters()));
    }

    @Test
    void getAllDeliveriesIntersectingBounds() {
        getGame().getDeliveries().addAll(
                createDeliveryAt(0, 0),
                createDeliveryAt(0, 3)
        );
        assertArrayEquals(
                getGame().getDeliveries().get().toArray(),
                getTrunk().getAllDeliveriesIntersectingBounds(new Rectangle(0, 0, 300, 100)).toArray());

        assertEquals(
                new LinkedList<>(Collections.singletonList(getGame().getDeliveries().get(0))),
                getTrunk().getAllDeliveriesIntersectingBounds(new Rectangle(0, 0, 100, 10)));

        assertEquals(
                0,
                getTrunk().getAllDeliveriesIntersectingBounds(new Rectangle(0, 120, 200, 100)).size());
    }

    private Delivery createDeliveryAt(int row, int column){
        Delivery delivery = new Delivery(DeliveryService.AMAZON, DeliveryShapeBuilder.buildShapeByName(DeliveryShape
                .L_SHAPE), 5, row, column, Priority.NORMAL);
        delivery.getBasicParcelSize().bind(new SimpleIntegerProperty(40));
        return delivery;
    }

}