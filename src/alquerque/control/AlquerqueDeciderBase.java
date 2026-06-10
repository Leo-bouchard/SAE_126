package src.alquerque.control;

import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.control.ActionFactory;
import src.boardifier.control.Controller;
import src.boardifier.control.Decider;
import src.boardifier.model.Model;
import src.boardifier.model.action.ActionList;

import java.util.ArrayList;
import java.util.List;

public abstract class AlquerqueDeciderBase extends Decider {

    public AlquerqueDeciderBase(Model model, Controller control) {
        super(model, control);
    }

    protected abstract int[] chooseFirstCapture(List<int[]> captures, int[][] matrix, int botColor);

    protected abstract int[] chooseNextCapture(List<int[]> captures, int[][] matrix, int botColor);

    protected abstract int[] chooseSimpleMove(List<int[]> moves, int[][] matrix, int botColor);

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int botColor = model.getIdPlayer();

        int[][] matrix = boardToMatrix(board);

        List<int[]> allCaptures = findAllCaptures(matrix, botColor);

        if (!allCaptures.isEmpty()) {
            int[] firstCapture = chooseFirstCapture(allCaptures, matrix, botColor);

            AlquerquePawn capturingPawn = (AlquerquePawn) board.getElement(firstCapture[0], firstCapture[1]);

            ActionList actions = new ActionList();
            addCaptureToActionList(actions, board, capturingPawn, firstCapture);
            applyMoveOnMatrix(matrix, firstCapture);
            int currentRow = firstCapture[2];
            int currentCol = firstCapture[3];

            while (true) {
                List<int[]> nextCaptures = findCapturesFromPosition(matrix, currentRow, currentCol, botColor);
                if (nextCaptures.isEmpty()) break;

                int[] next = chooseNextCapture(nextCaptures, matrix, botColor);

                addCaptureToActionList(actions, board, capturingPawn, next);
                applyMoveOnMatrix(matrix, next);
                currentRow = next[2];
                currentCol = next[3];
            }

            actions.setDoEndOfTurn(true);
            return actions;
        }

        List<int[]> simpleMoves = findAllSimpleMoves(matrix, botColor);
        if (simpleMoves.isEmpty()) {
            ActionList empty = new ActionList();
            empty.setDoEndOfTurn(true);
            return empty;
        }

        int[] move = chooseSimpleMove(simpleMoves, matrix, botColor);
        return buildSimpleMoveAction(board, move);
    }

    protected void addCaptureToActionList(ActionList actions, AlquerqueBoard board, AlquerquePawn pawn, int[] capture) {
        AlquerquePawn captured = (AlquerquePawn) board.getElement(capture[4], capture[5]);
        ActionList move = ActionFactory.generateMoveWithinContainer(control ,model, pawn, capture[2], capture[3]);
        ActionList remove = ActionFactory.generateRemoveFromStage(model, captured);
        actions.addAll(move);
        actions.addAll(remove);
    }

    protected ActionList buildSimpleMoveAction(AlquerqueBoard board, int[] move) {
        AlquerquePawn pawn = (AlquerquePawn) board.getElement(move[0], move[1]);
        ActionList actions = ActionFactory.generateMoveWithinContainer(control ,model, pawn, move[2], move[3]);
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

    protected int[][] copyMatrix(int[][] matrix) {
        int[][] copy = new int[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                copy[row][col] = matrix[row][col];
            }
        }
        return copy;
    }

    protected List<int[]> getLegalMoves(int[][] matrix, int color) {
        List<int[]> captures = findAllCaptures(matrix, color);
        if (!captures.isEmpty()) return captures;
        return findAllSimpleMoves(matrix, color);
    }
}
