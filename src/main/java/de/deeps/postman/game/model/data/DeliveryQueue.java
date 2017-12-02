package de.deeps.postman.game.model.data;

import de.deeps.postman.game.model.Delivery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DeliveryQueue {

    @Getter(AccessLevel.PRIVATE) private static final int DEFAULT_QUEUE_LENGTH = 3;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private boolean enablePriority;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int length, weightLimit, startRow, startColumn;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Queue<Delivery> deliveryQueue;


    public DeliveryQueue(int weightLimit, int startRow, int startColumn, boolean enablePriority) {
        this(getDEFAULT_QUEUE_LENGTH(), weightLimit, startRow, startColumn, enablePriority);
    }

    private DeliveryQueue(int length, int weightLimit, int startRow, int startColumn, boolean enablePriority) {
        setLength(length);
        setWeightLimit(weightLimit);
        setStartRow(startRow);
        setStartColumn(startColumn);
        setDeliveryQueue(new LinkedList<>());
        setEnablePriority(enablePriority);
        generateDeliveries();
    }

    private void generateDeliveries() {
        for (int iDelivery = getDeliveryQueue().size(); iDelivery < getLength(); iDelivery++){
            generateNewDelivery();
        }
    }

    private void generateNewDelivery(){
        getDeliveryQueue().add(  DeliveryBuilder.generateRandomDelivery(getWeightLimit(), getStartRow(),
                getStartColumn(), isEnablePriority()));
    }

    public Delivery pollNext(){
        Delivery nextDelivery = getDeliveryQueue().poll();
        generateNewDelivery();
        return nextDelivery;
    }

    public Delivery get(int index){
        return new ArrayList<>(getDeliveryQueue()).get(index);
    }

}
