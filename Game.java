import java.io.*;
import java.util.*;

public class Game {

    private Pawn[][] board;
    private Date time_start;
    private Date time_end;
    private Player p1;
    private Player p2;

    public Game() {
        this.board[5][5]= new Pawn();
        this.time_start = "";
        this.time_end = "";
        this.p1 = "";
        this.p2 = "";
    }

    public Game(Pawn[][] board, Date time_start, Date time_end, Player p1, Player p2) {
        this.board = board;
        this.time_start = time_start;
        this.time_end = time_end;
        this.p1 = p1;
        this.p2 = p2;
    }

    public Pawn[][] getBoard() {
        return this.board;
    }

    public Date getTime_start() {
        return this.time_start;
    }

    public Date getTime_end() {
        return this.time_end;
    }

    public Player getP1() {
        return this.p1;
    }

    public Player getP2() {
        return this.p2;
    }

    public void setBoard(Pawn[][] board) {
        this.board = board;
    }

    public void setTime_start(Date time_start) {
        this.time_start = time_start;
    }

    public void setTime_end(Date time_end) {
        this.time_end = time_end;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public init_game(Player p1, Player p2){
	Game(
    }

    @Override
    public String toString() {
        return "Game{" +
            "board=" + board +
            ", time_start=" + time_start +
            ", time_end=" + time_end +
            ", p1=" + p1 +
            ", p2=" + p2 +
            "}";
    }

}
