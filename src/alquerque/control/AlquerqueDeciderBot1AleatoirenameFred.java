package alquerque.control;

import boardifier.control.Controller;
import boardifier.model.Model;

import java.util.List;
import java.util.Random;

public class AlquerqueDeciderBot1AleatoirenameFred extends AlquerqueDeciderBase {

    private static final Random rand = new Random();

    public AlquerqueDeciderBot1AleatoirenameFred(Model model, Controller control) {
        super(model, control);
    }

    @Override
    protected int[] chooseFirstCapture(List<int[]> captures, int[][] matrix, int botColor) {
        return captures.get(rand.nextInt(captures.size()));
    }

    @Override
    protected int[] chooseNextCapture(List<int[]> captures, int[][] matrix, int botColor) {
        return captures.get(rand.nextInt(captures.size()));
    }

    @Override
    protected int[] chooseSimpleMove(List<int[]> moves, int[][] matrix, int botColor) {
        return moves.get(rand.nextInt(moves.size()));
    }
}
