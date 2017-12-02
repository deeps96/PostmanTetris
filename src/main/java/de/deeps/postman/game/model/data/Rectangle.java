package de.deeps.postman.game.model.data;

import javafx.geometry.Bounds;

public class Rectangle extends javafx.scene.shape.Rectangle {

    public Rectangle() { super(); }

    public Rectangle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * Intersection of single border (lets call this scenario a "touch") will result in false.
     * Example: This Rectangle is located directly to the left of the other rectangle (this.maxX == other.minX) -> return false;
     * But if all borders overlap, true is returned.
     * @param rectangle The other rectangle to check for intersection.
     * @return If the two rectangles intersect.
     */
    //convenience
    public boolean intersects(Rectangle rectangle){
        return intersects(rectangle.getLayoutBounds());
    }

    @Override
    public boolean intersects(Bounds otherBounds) {
        return hasSameBounds(otherBounds) || (isXOverlapping(otherBounds) && isYOverlapping(otherBounds));
    }

    /**
     * Returns, if this rectangle includes the other.
     * If the other rectangle has the same boundaries as this one, its still considered as contained.
     * @param otherRectangle - the rectangle, which should be checked to be within this one
     * @return true, if this rectangle contains the other one
     */
    public boolean contains(Rectangle otherRectangle){
        return contains(otherRectangle.getLayoutBounds());
    }

    private boolean contains(Bounds otherBounds){
        return isContainedHorizontally(otherBounds) && isContainedVertically(otherBounds);
    }

    //conditionals
    private boolean isXOverlapping(Bounds otherBounds){
        return otherBounds.getMinX() < getLayoutBounds().getMaxX() && otherBounds.getMaxX() > getLayoutBounds().getMinX();
    }

    private boolean isYOverlapping(Bounds otherBounds){
        return otherBounds.getMinY() < getLayoutBounds().getMaxY() && otherBounds.getMaxY() > getLayoutBounds().getMinY();
    }

    private boolean isContainedHorizontally(Bounds otherBounds){
        return otherBounds.getMinX() >= getLayoutBounds().getMinX() && otherBounds.getMaxX() <= getLayoutBounds().getMaxX();
    }

    private boolean isContainedVertically(Bounds otherBounds) {
        return otherBounds.getMinY() >= getLayoutBounds().getMinY() && otherBounds.getMaxY() <= getLayoutBounds().getMaxY();
    }

    private boolean hasSameBounds(Bounds otherBounds) {
        return getLayoutBounds().equals(otherBounds);
    }
}
