package alquerque.control;

import boardifier.control.Controller;
import boardifier.model.Model;

import java.util.Collections;
import java.util.List;

public class AlquerqueDeciderBot2Jesus extends AlquerqueDeciderBase {

    public AlquerqueDeciderBot2Jesus(Model model, Controller control) {
        super(model, control);
    }

    @Override
    protected int[] chooseFirstCapture(List<int[]> captures, int[][] matrix, int botColor) {
        return bestByScore(captures);
    }

    @Override
    protected int[] chooseNextCapture(List<int[]> captures, int[][] matrix, int botColor) {
        return bestByScore(captures);
    }

    @Override
    protected int[] chooseSimpleMove(List<int[]> moves, int[][] matrix, int botColor) {
        Collections.shuffle(moves);
        int[] best = moves.get(0);
        int bestScore = Integer.MIN_VALUE;
        for (int[] move : moves) {
            int score = scoreSimpleMove(move);
            if (score > bestScore) {
                bestScore = score;
                best = move;
            }
        }
        return best;
    }

    private int[] bestByScore(List<int[]> captures) {
        Collections.shuffle(captures);
        int[] best = captures.get(0);
        int bestScore = Integer.MIN_VALUE;
        for (int[] c : captures) {
            int score = scoreCapture(c);
            if (score > bestScore) {
                bestScore = score;
                best = c;
            }
        }
        return best;
    }

    private int scoreCapture(int[] capture) {
        int score = 20;
        if (edgeBonus(capture[2], capture[3]) > edgeBonus(capture[0], capture[1])) score += 2;
        return score;
    }

    private int scoreSimpleMove(int[] move) {
        int score = 0;
        if (edgeBonus(move[2], move[3]) > edgeBonus(move[0], move[1])) score += 1;
        return score;
    }

    private int edgeBonus(int row, int col) {
        boolean onEdgeRow = (row == 0 || row == 4);
        boolean onEdgeCol = (col == 0 || col == 4);
        if (onEdgeRow && onEdgeCol) return 3;
        if (onEdgeRow || onEdgeCol) return 2;
        return 0;
    }
}
