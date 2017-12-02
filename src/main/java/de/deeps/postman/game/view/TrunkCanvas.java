package de.deeps.postman.game.view;

import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.Parcel;
import de.deeps.postman.game.model.data.Priority;
import de.deeps.postman.game.model.data.Rectangle;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public class TrunkCanvas extends AbstractDeliveryCanvas {

    //constants
    @Getter(AccessLevel.PRIVATE) private static final Color
            DEFAULT_GRID_COLOR = Color.web("#fff", 0.1),
            DEFAULT_GHOST_COLOR = Color.web("#ffc107"),
            DEFAULT_PRIORITY_BACKGROUND_COLOR = Color.rgb(231, 76, 60, 0.75),
            DEFAULT_WEIGHT_COLOR = Color.web("#ecf0f1");
    @Getter(AccessLevel.PRIVATE) private static final Font DEFAULT_WEIGHT_FONT = new Font("Roboto", 20);

    @Getter(AccessLevel.PRIVATE) @Setter private boolean drawWeight;
    @Getter(AccessLevel.PRIVATE) @Setter private Color gridColor, ghostColor, priorityBackgroundColor, weightColor;
    @Getter(AccessLevel.PRIVATE) @Setter private Font weightFont;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleListProperty<Delivery> deliveries;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleObjectProperty<Delivery> ghostDelivery;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleObjectProperty<Game> game;

    public TrunkCanvas() {
        setGridColor(getDEFAULT_GRID_COLOR());
        setGhostColor(getDEFAULT_GHOST_COLOR());
        setWeightColor(getDEFAULT_WEIGHT_COLOR());
        setWeightFont(getDEFAULT_WEIGHT_FONT());
        setPriorityBackgroundColor(getDEFAULT_PRIORITY_BACKGROUND_COLOR());
        setDeliveries(new SimpleListProperty<>(FXCollections.observableArrayList(new LinkedList<>())));
        setGame(new SimpleObjectProperty<>());
        setGhostDelivery(new SimpleObjectProperty<>());
    }

    //actions
    private Rectangle getInvertedBounds(Priority priority, Rectangle priorityBounds) {
        if (Priority.LOW.equals(priority)){
            return invertLeftToRight(priorityBounds);
        } else if (Priority.HIGH.equals(priority)){
            return invertRightToLeft(priorityBounds);
        }
        return priorityBounds;
    }

    private Rectangle invertRightToLeft(Rectangle priorityBounds) {
        Bounds bounds = priorityBounds.getLayoutBounds();
        return new Rectangle(0, bounds.getMinY(), bounds.getMinX(), bounds.getHeight());
    }

    private Rectangle invertLeftToRight(Rectangle priorityBounds) {
        Bounds bounds = priorityBounds.getLayoutBounds();
        return new Rectangle(bounds.getMaxX(), bounds.getMinY(), getWidth() - bounds.getWidth(), bounds.getHeight());
    }

    //drawing
    @Override
    public void redraw(){
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        drawDeliveries();
        drawGhost();
        drawPriorityFrame();
    }

    private void drawPriorityFrame() {
        Priority priority = getGhostDelivery().get().getPriority();
        if (Priority.NORMAL.equals(priority)){
            return;
        }
        getGraphicsContext2D().setFill(getPriorityBackgroundColor());
        Rectangle priorityBounds = getGame().get().getTrunk().getAllowedBoundsForPriority(priority);
        Rectangle bounds = getInvertedBounds(priority, priorityBounds);
        getGraphicsContext2D().fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    private void drawDeliveries() {
        for (Delivery delivery : getDeliveries()){
            drawDelivery(delivery);
        }
    }

    private void drawDelivery(Delivery delivery) {
        for (Parcel parcel : delivery.getParcels()){
            drawDeliveryLogo(parcel);
            if (isDrawWeight()) {
                drawWeight(parcel);
            }
            drawGridSurroundingParcel(parcel, getGridColor());
        }
    }

    private void drawWeight(Parcel parcel) {
        getGraphicsContext2D().setFont(getWeightFont());
        getGraphicsContext2D().setFill(getWeightColor());
        Text text = new Text(Integer.toString(parcel.getWeight()));
        text.setFont(getWeightFont());
        getGraphicsContext2D().fillText(
                text.getText(),
                parcel.getX() + (parcel.getWidth() - text.getBoundsInLocal().getWidth()) / 2.0,
                parcel.getY() + parcel.getHeight() * 0.75);
    }

    private void drawGridSurroundingParcel(Parcel parcel, Color gridColor) {
        getGraphicsContext2D().setStroke(gridColor);
        getGraphicsContext2D().strokeRect(parcel.getX(), parcel.getY(), parcel.getWidth(), parcel.getHeight());
    }

    private void drawDeliveryLogo(Parcel parcel){
        getGraphicsContext2D().drawImage(
                getLogos().get(parcel.getDeliveryService()),
                parcel.getX(), parcel.getY(), parcel.getWidth(), parcel.getHeight());
    }

    private void drawGhost() {
        if (!isGhostSet()){
            return;
        }
        for (Parcel parcel: getGhostDelivery().get().getParcels()){
            drawGridSurroundingParcel(parcel, getGhostColor());
        }
    }

    //conditionals
    private boolean isGhostSet(){
        return getGhostDelivery().isNotNull().get();
    }

}
