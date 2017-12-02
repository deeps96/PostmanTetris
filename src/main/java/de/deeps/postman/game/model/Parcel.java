package de.deeps.postman.game.model;

import de.deeps.postman.game.model.data.DeliveryData;
import de.deeps.postman.game.model.data.DeliveryService;
import de.deeps.postman.game.model.data.ParcelData;
import de.deeps.postman.game.model.data.Rectangle;

public class Parcel extends ParcelData {

    //initialization
    public Parcel(DeliveryData delivery, int row, int column, int weight) {
        super(delivery, row, column, weight);
    }

    //conditionals
    private boolean wouldCollideWith(Parcel parcel){
        return getBounds().intersects(parcel.getBounds());
    }

    //convenience
    boolean wouldCollideWith(Delivery delivery){
        for (Parcel otherParcel : delivery.getParcels()){
            if (wouldCollideWith(otherParcel)){
                return true;
            }
        }
        return false;
    }

    public DeliveryService getDeliveryService(){
        return getParentDelivery().getDeliveryService();
    }

    boolean isContainedInBounds(Rectangle bounds){
        return bounds.contains(getBounds());
    }

}
