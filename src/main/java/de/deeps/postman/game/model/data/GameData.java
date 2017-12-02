package de.deeps.postman.game.model.data;

import de.deeps.postman.app.model.Keyboard;
import de.deeps.postman.game.model.Delivery;
import de.deeps.postman.game.model.Parcel;
import de.deeps.postman.game.model.Trunk;
import de.deeps.postman.game.model.pipe.DeliverySpawn;
import de.deeps.postman.game.model.pipe.PipeComponent;
import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public abstract class GameData extends AnimationTimer {

    @Getter(AccessLevel.PROTECTED) @Setter private Delivery activeDelivery;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private DeliverySpawn deliverySpawn;
    @Getter @Setter(AccessLevel.PRIVATE) private GameParameters gameParameters;
    @Getter @Setter private Keyboard keyboard;
    @Getter @Setter(AccessLevel.PRIVATE) private KeyboardSettings keyboardSettings;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PRIVATE) private List<PipeComponent> pipe;

    @Getter @Setter(AccessLevel.PRIVATE) private SimpleBooleanProperty balanceMinimumReached;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleDoubleProperty balance;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleIntegerProperty score;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleListProperty<Delivery> deliveries;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleLongProperty guiUpdate, previewUpdate, runningTime;
    @Getter private SimpleObjectProperty<Delivery> ghostForActiveDelivery;
    @Getter private SimpleObjectProperty<GameState> gameState;
    @Getter @Setter(AccessLevel.PRIVATE) private Trunk trunk;

    //initialization
    public GameData(@NonNull GameParameters gameParameters, @NonNull KeyboardSettings keyboardSettings) {
        setGameParameters(gameParameters);
        setKeyboardSettings(keyboardSettings);
        setScore(new SimpleIntegerProperty());
        setRunningTime(new SimpleLongProperty());
        setBalance(new SimpleDoubleProperty());
        setBalanceMinimumReached(new SimpleBooleanProperty());
        setTrunk(new Trunk(self(), getGameParameters()));

        setDeliveries(new SimpleListProperty<>(FXCollections.observableArrayList()));
        setBasicGhostForActiveDelivery(new SimpleObjectProperty<>());

        setPipe(new LinkedList<>());
        fillPipe();
        setGuiUpdate(new SimpleLongProperty());
        setPreviewUpdate(new SimpleLongProperty());
        setBasicGameState(new SimpleObjectProperty<>(GameState.READY));
    }

    //abstract
    protected abstract void fillPipe();

    //accessing
    private void setBasicGhostForActiveDelivery(SimpleObjectProperty<Delivery> ghostForActiveDelivery) {
        this.ghostForActiveDelivery = ghostForActiveDelivery;
    }

    protected void unsetActiveDelivery() {
        setActiveDelivery(null);
    }

    protected void setGameState(GameState gameState) {
        getGameState().set(gameState);
    }

    private void setBasicGameState(SimpleObjectProperty<GameState> gameState) {
        this.gameState = gameState;
    }

    private GameData self() {
        return this;
    }

    protected void addPipeComponent(PipeComponent pipeComponent){
        getPipe().add(pipeComponent);
    }

    //convenience
    public void setGhostForActiveDelivery(Delivery ghost) {
        getGhostForActiveDelivery().set(ghost);
    }

    public Delivery getPreview(int index) {
        return getDeliverySpawn().getDeliveryQueue().get(index);
    }

    public boolean doesAPipeComponentRequestNewActiveDelivery() {
        for (PipeComponent component : getPipe()) {
            if (component.isNewActiveDeliveryRequested()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameRunning() {
        return getGameState().get().equals(GameState.RUNNING);
    }

    public List<Parcel> getAllParcels(){
        List<Parcel> parcels = new LinkedList<>();
        for (Delivery delivery : getDeliveries()){
            parcels.addAll(delivery.getParcels());
        }
        return parcels;
    }

    //conditionals
    protected boolean isGameReady() {
        return getGameState().get().equals(GameState.READY);
    }

    protected boolean isGamePaused() {
        return getGameState().get().equals(GameState.PAUSED);
    }

    protected boolean isSameDelivery(Delivery delivery, Delivery otherDelivery) {
        Delivery toCompare = delivery.isGhost() ? delivery.getIsGhostFor() : delivery;
        return toCompare == otherDelivery;
    }

}
