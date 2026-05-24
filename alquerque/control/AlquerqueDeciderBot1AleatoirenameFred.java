package alquerque.control;

import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlquerqueDeciderBot1AleatoirenameFred extends Decider {

    private static final Random rand = new Random();

    public AlquerqueDeciderBot1AleatoirenameFred(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int botColor = model.getIdPlayer();

        // build a simulated matrix to track captures during the chain
        int[][] matrix = boardToMatrix(board);

        // ALQUERQUE RULE: if any capture is possible, we MUST capture
        List<int[]> allCaptures = findAllCaptures(matrix, botColor);

        if (!allCaptures.isEmpty()) {
            // pick a random capture
            int[] firstCapture = allCaptures.get(rand.nextInt(allCaptures.size()));

            // get the pawn ONCE from the real board, then reuse for the whole chain
            AlquerquePawn capturingPawn = (AlquerquePawn) board.getElement(firstCapture[0], firstCapture[1]);

            // start building the action list
            ActionList actions = new ActionList();
            addCaptureToActionList(actions, board, capturingPawn, firstCapture);
            applyMoveOnMatrix(matrix, firstCapture);

            // try to chain captures from the new position
            int currentRow = firstCapture[2];
            int currentCol = firstCapture[3];

            while (true) {
                List<int[]> nextCaptures = findCapturesFromPosition(matrix, currentRow, currentCol, botColor);
                if (nextCaptures.isEmpty()) break;

                // pick the next capture at random
                int[] next = nextCaptures.get(rand.nextInt(nextCaptures.size()));

                // reuse the SAME pawn (it's the one that's moving)
                addCaptureToActionList(actions, board, capturingPawn, next);
                applyMoveOnMatrix(matrix, next);
                currentRow = next[2];
                currentCol = next[3];
            }

            actions.setDoEndOfTurn(true);
            return actions;
        }

        // no capture available, do a simple random move
        List<int[]> simpleMoves = findAllSimpleMoves(matrix, botColor);
        if (simpleMoves.isEmpty()) {
            ActionList empty = new ActionList();
            empty.setDoEndOfTurn(true);
            return empty;
        }
        int[] move = simpleMoves.get(rand.nextInt(simpleMoves.size()));
        return buildSimpleMoveAction(board, move);
    }


    private void addCaptureToActionList(ActionList actions, AlquerqueBoard board, AlquerquePawn pawn, int[] capture) {
        AlquerquePawn captured = (AlquerquePawn) board.getElement(capture[4], capture[5]);
        ActionList move = ActionFactory.generateMoveWithinContainer(model, pawn, capture[2], capture[3]);
        ActionList remove = ActionFactory.generateRemoveFromStage(model, captured);
        actions.addAll(move);
        actions.addAll(remove);
    }

    private ActionList buildSimpleMoveAction(AlquerqueBoard board, int[] move) {
        AlquerquePawn pawn = (AlquerquePawn) board.getElement(move[0], move[1]);
        ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, move[2], move[3]);
        actions.setDoEndOfTurn(true);
        return actions;
    }

    public List<int[]> findAllCaptures(int[][] matrix, int color) {
        List<int[]> captures = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[row][col] != color) continue;
                captures.addAll(findCapturesFromPosition(matrix, row, col, color));
            }
        }
        return captures;
    }

    public List<int[]> findCapturesFromPosition(int[][] matrix, int row, int col, int color) {
        List<int[]> captures = new ArrayList<>();
        if (matrix[row][col] != color) return captures;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                boolean isDiagonal = (i != 0 && j != 0);
                if (isDiagonal && (row + col) % 2 != 0) continue;

                int midRow = row + i;
                int midCol = col + j;
                if (midRow < 0 || midRow >= 5 || midCol < 0 || midCol >= 5) continue;
                if (matrix[midRow][midCol] == -1) continue;
                if (matrix[midRow][midCol] == color) continue;
                if (isDiagonal && (midRow + midCol) % 2 != 0) continue;

                int endRow = row + 2 * i;
                int endCol = col + 2 * j;
                if (endRow < 0 || endRow >= 5 || endCol < 0 || endCol >= 5) continue;
                if (matrix[endRow][endCol] != -1) continue;

                captures.add(new int[]{row, col, endRow, endCol, midRow, midCol});
            }
        }
        return captures;
    }

    public List<int[]> findAllSimpleMoves(int[][] matrix, int color) {
        List<int[]> moves = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[row][col] != color) continue;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i == 0 && j == 0) continue;
                        boolean isDiagonal = (i != 0 && j != 0);
                        if (isDiagonal && (row + col) % 2 != 0) continue;

                        int newRow = row + i;
                        int newCol = col + j;
                        if (newRow < 0 || newRow >= 5 || newCol < 0 || newCol >= 5) continue;
                        if (matrix[newRow][newCol] != -1) continue;

                        moves.add(new int[]{row, col, newRow, newCol});
                    }
                }
            }
        }
        return moves;
    }

    public int[][] boardToMatrix(AlquerqueBoard board) {
        int[][] matrix = new int[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Object e = board.getElement(row, col);
                if (e == null) matrix[row][col] = -1;
                else matrix[row][col] = ((AlquerquePawn) e).getColor();
            }
        }
        return matrix;
    }

    public void applyMoveOnMatrix(int[][] matrix, int[] move) {
        int color = matrix[move[0]][move[1]];
        matrix[move[0]][move[1]] = -1;
        matrix[move[2]][move[3]] = color;
        if (move.length >= 6) matrix[move[4]][move[5]] = -1;
    }
}