package de.deeps.postman.game.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.deeps.postman.utils.JsonConverter;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Highscore {

    private SimpleListProperty<HighscoreEntry> scoreboard;

    private Highscore() {
        setBasicScoreboard(new SimpleListProperty<>(FXCollections.observableArrayList()));
    }

    public static Highscore loadFromJson(String filePath) {
        Highscore highscore = null;
        if (new File(filePath).exists()) {
            try {
                highscore = JsonConverter.jsonFileToObject(filePath, Highscore.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (highscore == null) ? new Highscore() : highscore;
    }

    public void saveAsJson(String jsonFilePath) throws IOException {
        JsonConverter.writeObjectToJsonFile(self(), jsonFilePath);
    }

    public void addScore(HighscoreEntry score) {
        getBasicScoreboard().add(score);
    }

    //accessing
    private Highscore self() {
        return this;
    }

    @JsonIgnore
    public SimpleListProperty<HighscoreEntry> getBasicScoreboard() {
        return scoreboard;
    }

    @JsonIgnore
    private void setBasicScoreboard(SimpleListProperty<HighscoreEntry> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public List<HighscoreEntry> getScoreboard() {
        return getBasicScoreboard().get();
    }

    public void setScoreboard(List<HighscoreEntry> scoreboard) {
        getBasicScoreboard().set(FXCollections.observableArrayList(scoreboard));
    }
}
