package de.deeps.postman.game.model.data;

public class DeliveryShapeBuilder {

    private DeliveryShapeBuilder() {
        throw new IllegalStateException();
    }

    /**
     * Maps the DeliveryShape-Enum to a new matching AbstractDeliveryShape instance.
     * @param deliveryShape - The delivery shape enum, naming the shape.
     * @return The delivery shape representing the form.
     */
    public static AbstractDeliveryShape buildShapeByName(DeliveryShape deliveryShape){
        switch (deliveryShape){
            case L_SHAPE:
                return new LShape();
            case INVERTED_L_SHAPE:
                return new InvertedLShape();
            case Z_SHAPE:
                return new ZShape();
            case INVERTED_Z_SHAPE:
                return new InvertedZShape();
            case SPIKE_SHAPE:
                return new SpikeShape();
            case SQUARE_SHAPE:
            default:
                return new SquareShape();
        }
    }

    /**
     *  -#-
     *  -#-
     *  -##
     */
    public static class LShape extends AbstractDeliveryShape {
        protected void formShape() {
            enableTile(0, 1);
            enableTile(1, 1);
            enableTile(2, 1);
            enableTile(2, 2);
        }
    }

    /**
     *  -#-
     *  -#-
     *  ##-
     */
    public static class InvertedLShape extends AbstractDeliveryShape {
        protected void formShape() {
            enableTile(0, 1);
            enableTile(1, 1);
            enableTile(2, 1);
            enableTile(2, 0);
        }
    }

    /**
     *  ##-
     *  -##
     *  ---
     */
    public static class ZShape extends AbstractDeliveryShape {
        protected void formShape() {
            enableTile(0, 0);
            enableTile(0, 1);
            enableTile(1, 1);
            enableTile(1, 2);
        }
    }

    /**
     *  -##
     *  ##-
     *  ---
     */
    public static class InvertedZShape extends AbstractDeliveryShape {
        protected void formShape() {
            enableTile(0, 1);
            enableTile(0, 2);
            enableTile(1, 0);
            enableTile(1, 1);
        }
    }

    /**
     *  -#-
     *  ###
     *  ---
     */
    public static class SpikeShape extends AbstractDeliveryShape {
        protected void formShape() {
            enableTile(0, 1);
            enableTile(1, 0);
            enableTile(1, 1);
            enableTile(1, 2);
        }
    }

    /**
     *  ##-
     *  ##-
     *  ---
     */
    public static class SquareShape extends AbstractDeliveryShape {
        protected void formShape() {
            enableTile(0, 0);
            enableTile(0, 1);
            enableTile(1, 0);
            enableTile(1, 1);
        }

        @Override
        public void rotateClockwise() { /* rotation not required for square shape */ }
    }
}
