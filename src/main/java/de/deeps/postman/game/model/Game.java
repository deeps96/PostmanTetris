package de.deeps.postman.game.model;

import de.deeps.postman.game.model.data.GameData;
import de.deeps.postman.game.model.data.GameParameters;
import de.deeps.postman.game.model.data.GameState;
import de.deeps.postman.game.model.data.KeyboardSettings;
import de.deeps.postman.game.model.pipe.*;

public class Game extends GameData {

    //initialization
    public Game(GameParameters gameParameters, KeyboardSettings keyboardSettings) {
        super(gameParameters, keyboardSettings);
    }

    @Override
    protected void fillPipe() {
        //pre-creation for references
        GhostUpdater ghostUpdater = new GhostUpdater(self());
        RowCollector rowCollector = new RowCollector(self());
        Score score = new Score(self(), rowCollector);
        Timer timer = new Timer(self());
        BalanceChecker balanceChecker = new BalanceChecker(self());

        //assign object field
        getScore().bind(score.getScore());
        getRunningTime().bind(timer.getTime());
        setDeliverySpawn(new DeliverySpawn(self(), ghostUpdater));
        getBalance().bind(balanceChecker.getBalance());
        getBalanceMinimumReached().bind(balanceChecker.getBalanceMinimumReached());

        //order of the pipe components matters
        addPipeComponent(new CheckGameOver(self()));
        addPipeComponent(getDeliverySpawn());
        addPipeComponent(new Gravity(self()));
        addPipeComponent(new KeyHandler(self(), ghostUpdater));
        addPipeComponent(rowCollector);
        addPipeComponent(score); //after key handler and gravity
        addPipeComponent(timer);
        addPipeComponent(balanceChecker);
        addPipeComponent(ghostUpdater); //last
    }

    /**
     * This is the main game loop. It´s responsible for updating the package position accordingly (reacting to fall,
     * user input, game events) and for adding/ removing packages (and their tiles) from the loadArea.
     * The method is implemented using a pipe, where the activeDelivery gets passed through.
     * The method gets called max. 60 times per second
     */
    @Override
    public void handle(long now) {
        if (!isGameRunning()) {
            return;
        }
        for (PipeComponent pipeComponent : getPipe()) {
            pipeComponent.handle(now, getActiveDelivery());
        }
        if (doesAPipeComponentRequestNewActiveDelivery()) {
            unsetActiveDelivery();
        }
        if (getDeliverySpawn().isSpawnedNewDelivery()) {
            updatePreview(now);
        }
        updateGUI(now);
    }

    //accessing
    private Game self(){
        return this;
    }

    //drawing
    private void updateGUI(long now) {
        getGuiUpdate().set(now);
    }

    private void updatePreview(long now) {
        getPreviewUpdate().set(now);
    }

    //actions
    private void startAllPipeComponents() {
        for (PipeComponent component : getPipe()) {
            component.start();
        }
    }

    //convenience
    public void startGame() {
        if (!isGameReady()) {
            return;
        }
        setGameState(GameState.RUNNING);
        startAllPipeComponents();
        start();
    }

    public void endGame() {
        setGameState(GameState.OVER);
        stop();
    }

    public void pauseGame() {
        if (isGameRunning()) {
            setGameState(GameState.PAUSED);
            stop();
        }
    }

    public void resumeGame() {
        if (isGamePaused()) {
            setGameState(GameState.RUNNING);
            start();
        }
    }

    boolean wouldCollideAtCurrentPosition(Delivery delivery) {
        for (Delivery otherDelivery : getDeliveries()) {
            if (!isSameDelivery(delivery, otherDelivery) && delivery.wouldCollideWith(otherDelivery)) {
                return true;
            }
        }
        return false;
    }

    boolean doesPriorityMatchLocation(Delivery delivery) {
        return delivery.isContainedInBounds(getTrunk().getAllowedBoundsForPriority(delivery.getPriority()));
    }


}
