package de.deeps.postman.game.model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractDeliveryShapeTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private AbstractDeliveryShape shape;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int trueValueCounter;

    @BeforeEach
    void setUp(){
        setShape(DeliveryShapeBuilder.buildShapeByName(DeliveryShape.L_SHAPE));
    }

    @Test
    void rotateClockwise() {
        rotationTest(() -> { getShape().rotateClockwise(); }, getClockwiseRotatedLShape());
    }

    @Test
    void rotateAntiClockwise() { rotationTest(() -> { getShape().rotateAntiClockwise(); }, getAntiClockwiseRotatedLShape()); }

    void rotationTest(Runnable rotationAction, boolean[][] rotatedShape){
        rotationAction.run();
        setTrueValueCounter(0);
        getShape().forEachEnabledCellDo((row, column) -> {
            incrementTrueValuesCounter();
            assert rotatedShape[row][column];
        });
        assertEquals(4, getTrueValueCounter());
    }

    //helpers
    private boolean[][] getClockwiseRotatedLShape() {
        return new boolean[][] {
                {
                        false, false, false
                },
                {
                        true, true, true
                },
                {
                        true, false, false
                }
        };
    }

    private void incrementTrueValuesCounter(){
        setTrueValueCounter(getTrueValueCounter() + 1);
    }

    private boolean[][] getAntiClockwiseRotatedLShape() {
        return new boolean[][] {
                {
                        false, false, true
                },
                {
                        true, true, true
                },
                {
                        false, false, false
                }
        };
    }


}