package de.deeps.postman.game.view;

import de.deeps.postman.game.model.data.DeliveryService;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.EnumMap;

public abstract class AbstractDeliveryCanvas extends Canvas {

    @Getter(AccessLevel.PRIVATE) private static final String DELIVERY_SERVICE_IMAGE_DIR = "images/delivery-services";

    @Getter(AccessLevel.PROTECTED) private final EnumMap<DeliveryService, Image> logos = loadLogosIntoMap();

    private EnumMap<DeliveryService, Image> loadLogosIntoMap(){
        EnumMap<DeliveryService, Image> loadedLogos = new EnumMap<>(DeliveryService.class);
        for (DeliveryService deliveryService : DeliveryService.values()){
            loadedLogos.put(deliveryService, loadDeliverServiceImage(deliveryService));
        }
        return loadedLogos;
    }

    private Image loadDeliverServiceImage(DeliveryService deliveryService){
        return loadImageFromClassPath(getDELIVERY_SERVICE_IMAGE_DIR() + "/" + deliveryService.toString().toLowerCase
                () + ".png");
    }

    private Image loadImageFromClassPath(String classPath){
        return new Image(getClass().getClassLoader().getResourceAsStream(classPath));
    }

    //convenience
    public abstract void redraw();
}
