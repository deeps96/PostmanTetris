package de.deeps.postman.game.model.data;

import de.deeps.postman.utils.JsonConverter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class GameParameters {

    @Getter @Setter private boolean enablePriority, enableBalance;
    @Getter @Setter private double balanceMaxDifferenceInPercent;
    @Getter @Setter private int deliveryWeightLimit, gridWidth, gridHeight, pauseBetweenFallInMS, userActionTimeoutInMS,
            deliveryDroppedScore, parcelRemovedScore, balanceMinParcelCount;

    //initialization
    public static GameParameters loadFromJsonFile(String jsonFilePath){
        try {
            GameParameters gameParameters = JsonConverter.jsonInputStreamToObject(GameParameters.class.getClassLoader()
                    .getResourceAsStream(jsonFilePath), GameParameters.class);
            if (!isValid(gameParameters)){
                throw new NullPointerException();
            }
            return gameParameters;
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    //convenience
    public void triggerEnablePriority(){
        setEnablePriority(!isEnablePriority());
    }

    public void triggerEnableBalance(){
        setEnableBalance(!isEnableBalance());
    }

    //conditionals
    private static boolean isValid(GameParameters gameParameters) {
        return isValidGridWidth(gameParameters.getGridWidth()) && isValidGridHeight(gameParameters.getGridHeight());
    }

    private static boolean isValidGridWidth(int gridWidth) {
        return gridWidth > 0;
    }

    private static boolean isValidGridHeight(int gridHeight) {
        return gridHeight > 0;
    }
}
