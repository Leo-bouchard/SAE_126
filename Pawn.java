import java.io.*;
import java.util.*;

public class Pawn {

    private int[] pos;
    private Boolean is_alive;
    private Boolean white;

    public Pawn() {
        this.pos = "";
        this.is_alive = "";
        this.white = "";
    }

    public Pawn(int[] pos, Boolean is_alive, Boolean white) {
        this.pos = pos;
        this.is_alive = is_alive;
        this.white = white;
    }

    public int[] getPos() {
        return this.pos;
    }

    public Boolean getIs_alive() {
        return this.is_alive;
    }

    public Boolean getWhite() {
        return this.white;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public void setIs_alive(Boolean is_alive) {
        this.is_alive = is_alive;
    }

    public void setWhite(Boolean white) {
        this.white = white;
    }

    public movePawn(int[] posi){
	this.pos = posi;
    }

    public eatPawn(){
	this.is_alive = false; 
   }

    @Override
    public String toString() {
        return "Pawn{" +
            "pos=" + pos +
            ", is_alive=" + is_alive +
            ", white=" + white +
            "}";
    }

}
