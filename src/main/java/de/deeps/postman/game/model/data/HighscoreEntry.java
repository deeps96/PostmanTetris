package de.deeps.postman.game.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleStringProperty;

public class HighscoreEntry {

    private SimpleStringProperty name, time, score;

    private HighscoreEntry() {
        setBasicName(new SimpleStringProperty());
        setBasicTime(new SimpleStringProperty());
        setBasicScore(new SimpleStringProperty());
    }

    public HighscoreEntry(String name, String time, String score) {
        this();
        setName(name);
        setTime(time);
        setScore(score);
    }

    //accessing
    @JsonIgnore
    public SimpleStringProperty getBasicName() {
        return name;
    }

    @JsonIgnore
    private void setBasicName(SimpleStringProperty name) {
        this.name = name;
    }

    @JsonIgnore
    public SimpleStringProperty getBasicTime() {
        return time;
    }

    @JsonIgnore
    private void setBasicTime(SimpleStringProperty time) {
        this.time = time;
    }

    @JsonIgnore
    public SimpleStringProperty getBasicScore() {
        return score;
    }

    @JsonIgnore
    private void setBasicScore(SimpleStringProperty score) {
        this.score = score;
    }

    public String getName() {
        return getBasicName().get();
    }

    public void setName(String name) {
        getBasicName().set(name);
    }

    public String getTime() {
        return getBasicTime().get();
    }

    public void setTime(String time) {
        getBasicTime().set(time);
    }

    public String getScore() {
        return getBasicScore().get();
    }

    public void setScore(String score) {
        getBasicScore().set(score);
    }
}
