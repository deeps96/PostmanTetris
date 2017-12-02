package de.deeps.postman.game.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class BalanceCanvas extends Canvas {

    //constants
    @Getter(AccessLevel.PRIVATE) private static final Color DEFAULT_BACKGROUND_COLOR = Color.web("#ECF0F1"),
    DEFAULT_DISABLED_COLOR = Color.rgb(184, 195, 199, 0.8);

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Color backgroundColor, disabledColor;
    @Getter(AccessLevel.PRIVATE) @Setter private double max;
    @Getter @Setter(AccessLevel.PRIVATE) SimpleBooleanProperty balanceMinimumReached;
    @Getter @Setter(AccessLevel.PRIVATE) SimpleDoubleProperty balance;

    //initialize
    public BalanceCanvas() {
        setMax(1);
        setBackgroundColor(getDEFAULT_BACKGROUND_COLOR());
        setDisabledColor(getDEFAULT_DISABLED_COLOR());
        setBalance(new SimpleDoubleProperty());
        setBalanceMinimumReached(new SimpleBooleanProperty());
        redraw();
    }

    //drawing
    public void redraw(){
        if (!isVisible()){
            return;
        }
        getGraphicsContext2D().clearRect(0,0, getWidth(), getHeight());
        drawBackground();
        drawBalance();
        if (!getBalanceMinimumReached().get()){
            drawDisabledOverlay();
        }
    }

    private void drawDisabledOverlay() {
        getGraphicsContext2D().setFill(getDisabledColor());
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawBalance() {
        double value = getBalance().get();
        if (Double.isInfinite(value)){
            return;
        }
        getGraphicsContext2D().setFill(Color.rgb(getRed(value, getMax()), getGreen(value, getMax()), 0));
        int progressWidth = getProgressWidth(value, getMax());
        if (value > 0){
            getGraphicsContext2D().fillRect(getWidth() / 2, 0, progressWidth, getHeight());
        } else if (value < 0){
            getGraphicsContext2D().fillRect(getWidth() / 2 - progressWidth, 0, progressWidth, getHeight());
        }

    }

    private int getGreen(double value, double max){
        double absValue = Math.abs(value);
        double absMax = Math.abs(max);
        absValue -= absMax / 2;
        return (absValue <= 0) ? 255 : (int) Math.min(((absValue / absMax / 2) * 255.0), 255);
    }

    private int getRed(double value, double max){
        double absValue = Math.abs(value);
        double absMax = Math.abs(max);
        return (absValue >= absMax / 2) ? 255 : (int) Math.min(((absValue / absMax / 2) * 255.0), 255);
    }

    private int getProgressWidth(double value, double max){
        return (int) (Math.abs(value) / max * getWidth() / 2.0);
    }

    private void drawBackground() {
        getGraphicsContext2D().setFill(getBackgroundColor());
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }
}
