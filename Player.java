import java.io.*;
import java.util.*;

public class Player {

    private String name;
    private int score;
    private Boolean white;
    private int pawn_left;

    public Player() {
        this.name = "";
        this.score = 0;
        this.white = false;
        this.pawn_left = 0;
    }

    public Player(String name, int score, Boolean white, int pawn_left) {
        this.name = name;
        this.score = score;
        this.white = white;
        this.pawn_left = pawn_left;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public Boolean getWhite() {
        return this.white;
    }

    public int getPawn_left() {
        return this.pawn_left;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setWhite(Boolean white) {
        this.white = white;
    }

    public void setPawn_left(int pawn_left) {
        this.pawn_left = pawn_left;
    }

    @Override
    public String toString() {
        return "Player{" +
            "name=" + "\"" + name + "\"" +
            ", score=" + score +
            ", white=" + white +
            ", pawn_left=" + pawn_left +
            "}";
    }

}
