package de.deeps.postman.game.model.data;

import de.deeps.postman.game.model.Delivery;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class DeliveryBuilder {

    //constants
    @Getter(AccessLevel.PRIVATE) private static final List<DeliveryService> DELIVERY_SERVICES =
            Collections.unmodifiableList(Arrays.asList(DeliveryService.values()));
    @Getter(AccessLevel.PRIVATE) private static final List<DeliveryShape> DELIVERY_SHAPES =
            Collections.unmodifiableList(Arrays.asList(DeliveryShape.values()));
    @Getter(AccessLevel.PRIVATE) private static final List<Priority> PRIORITIES =
            Collections.unmodifiableList(Arrays.asList(Priority.values()));
    @Getter(AccessLevel.PRIVATE) private static final Random RANDOM = new Random();

    private DeliveryBuilder() {
        throw new IllegalStateException();
    }

    //convenience
    static Delivery generateRandomDelivery(int weightLimit, int startRow, int startColumn, boolean
            enablePriority){
        AbstractDeliveryShape randomShape = DeliveryShapeBuilder.buildShapeByName(getRandomDeliveryShape());
        for (int iRotation = 0; iRotation < getRandomRotationCount(); iRotation++){
            randomShape.rotateClockwise();
        }
        return new Delivery(getRandomDeliveryService(), randomShape, getRandomWeight(weightLimit), startRow,
                startColumn, getRandomPriority(enablePriority));
    }

    //actions
    private static DeliveryShape getRandomDeliveryShape(){
        return getDELIVERY_SHAPES().get(getRANDOM().nextInt(getDELIVERY_SHAPES().size()));
    }

    private static DeliveryService getRandomDeliveryService(){
        return getDELIVERY_SERVICES().get(getRANDOM().nextInt(getDELIVERY_SERVICES().size()));
    }

    private static int getRandomRotationCount(){
        return getRANDOM().nextInt(AbstractDeliveryShape.getROTATIONS_UNTIL_ORIGIN());
    }

    private static int getRandomWeight(int weightLimit){
        return getRANDOM().nextInt(weightLimit + 1);
    }

    private static Priority getRandomPriority(boolean enablePriority) {
        if (!enablePriority){
            return Priority.NORMAL;
        }
        return getPRIORITIES().get(getRANDOM().nextInt(getPRIORITIES().size()));
    }
}