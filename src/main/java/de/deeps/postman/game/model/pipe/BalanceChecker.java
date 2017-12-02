package de.deeps.postman.game.model.pipe;

import de.deeps.postman.game.model.Game;
import de.deeps.postman.game.model.Parcel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BalanceChecker extends PipeComponent {

    @Getter @Setter(AccessLevel.PRIVATE) private SimpleBooleanProperty balanceMinimumReached;
    @Getter @Setter(AccessLevel.PRIVATE) private SimpleDoubleProperty balance;

    public BalanceChecker(Game game) {
        super(game);
        setBalance(new SimpleDoubleProperty());
        setBalanceMinimumReached(new SimpleBooleanProperty());
    }

    @Override
    protected void handle() {
        if (isNewDeliveryRequested() && getGameParameters().isEnableBalance()){
            updateBalance();
            updateBalanceMinimumReached();
            checkIfLimitExceeded();
        }
    }

    private void checkIfLimitExceeded() {
        if (getBalanceMinimumReached().get() && Math.abs(getBalance().get()) > getGameParameters()
                .getBalanceMaxDifferenceInPercent()){
            getGame().endGame();
        }
    }

    private void updateBalanceMinimumReached() {
        getBalanceMinimumReached().set(getGame().getAllParcels().size() >= getGameParameters()
                .getBalanceMinParcelCount());
    }

    private void updateBalance() {
        double leftWeight = getWeightOfParcels(getLeftParcels());
        double rightWeight = getWeightOfParcels(getRightParcels());
        double newBalance;
        if (Math.max(leftWeight, rightWeight) > 0) {
            newBalance = (rightWeight - leftWeight) / Math.min(leftWeight, rightWeight);
        } else {
            newBalance = 0.0;
        }
        getBalance().set(newBalance);
    }

    private int getWeightOfParcels(List<Parcel> parcels) {
        int weight = 0;
        for (Parcel parcel : parcels){
            weight += parcel.getWeight();
        }
        return weight;
    }

    private List<Parcel> getLeftParcels(){
        return getTrunk().getAllParcelsContainedInBounds(getTrunk().getLeftFieldBounds());
    }

    private List<Parcel> getRightParcels(){
        return getTrunk().getAllParcelsContainedInBounds(getTrunk().getRightFieldBounds());
    }


}
