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

public class AlquerqueDeciderBot2Jesus extends Decider {

    public AlquerqueDeciderBot2Jesus(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int botColor = model.getIdPlayer();

        int[][] matrix = boardToMatrix(board);

        // ALQUERQUE RULE: capture is mandatory if available
        List<int[]> allCaptures = findAllCaptures(matrix, botColor);

        if (!allCaptures.isEmpty()) {
            // pick the capture with the best local score
            int[] bestFirst = allCaptures.get(0);
            int bestScore = Integer.MIN_VALUE;
            for (int[] c : allCaptures) {
                int score = scoreCapture(c);
                if (score > bestScore) {
                    bestScore = score;
                    bestFirst = c;
                }
            }

            // get the pawn ONCE from the real board, then reuse for the whole chain
            AlquerquePawn capturingPawn = (AlquerquePawn) board.getElement(bestFirst[0], bestFirst[1]);

            // start the chain
            ActionList actions = new ActionList();
            addCaptureToActionList(actions, board, capturingPawn, bestFirst);
            applyMoveOnMatrix(matrix, bestFirst);
            int currentRow = bestFirst[2];
            int currentCol = bestFirst[3];

            // continue chain by picking the best capture at each step
            while (true) {
                List<int[]> nextCaptures = findCapturesFromPosition(matrix, currentRow, currentCol, botColor);
                if (nextCaptures.isEmpty()) break;

                int[] bestNext = nextCaptures.get(0);
                int bestNextScore = Integer.MIN_VALUE;
                for (int[] c : nextCaptures) {
                    int score = scoreCapture(c);
                    if (score > bestNextScore) {
                        bestNextScore = score;
                        bestNext = c;
                    }
                }

                // reuse the SAME pawn (it's the one that's moving)
                addCaptureToActionList(actions, board, capturingPawn, bestNext);
                applyMoveOnMatrix(matrix, bestNext);
                currentRow = bestNext[2];
                currentCol = bestNext[3];
            }

            actions.setDoEndOfTurn(true);
            return actions;
        }

        // no capture: pick the best simple move
        List<int[]> simpleMoves = findAllSimpleMoves(matrix, botColor);
        if (simpleMoves.isEmpty()) {
            ActionList empty = new ActionList();
            empty.setDoEndOfTurn(true);
            return empty;
        }

        int[] bestMove = simpleMoves.get(0);
        int bestScore = Integer.MIN_VALUE;
        for (int[] move : simpleMoves) {
            int score = scoreSimpleMove(move);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return buildSimpleMoveAction(board, bestMove);
    }


    /**
     * Scores a capture move.
     * Base: +10 because it's a capture.
     * Bonus: +2 if we land on a safer cell (edge / corner).
     */
    private int scoreCapture(int[] capture) {
        int score = 10;
        int safetyBefore = edgeBonus(capture[0], capture[1]);
        int safetyAfter = edgeBonus(capture[2], capture[3]);
        if (safetyAfter > safetyBefore) score += 2;
        return score;
    }

    /**
     * Scores a simple move.
     * Bonus: +2 for moving to a safer cell.
     */
    private int scoreSimpleMove(int[] move) {
        int score = 0;
        int safetyBefore = edgeBonus(move[0], move[1]);
        int safetyAfter = edgeBonus(move[2], move[3]);
        if (safetyAfter > safetyBefore) score += 2;
        return score;
    }

    /**
     * Safety bonus depending on the position.
     * Corner = +3, edge = +2, inside = 0.
     */
    private int edgeBonus(int row, int col) {
        boolean topBot = (row == 0 || row == 4);
        boolean leftRight = (col == 0 || col == 4);
        if (topBot && leftRight) return 3;
        if (topBot || leftRight) return 2;
        return 0;
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

    private List<int[]> findAllCaptures(int[][] matrix, int color) {
        List<int[]> captures = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[row][col] != color) continue;
                captures.addAll(findCapturesFromPosition(matrix, row, col, color));
            }
        }
        return captures;
    }

    private List<int[]> findCapturesFromPosition(int[][] matrix, int row, int col, int color) {
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

    private List<int[]> findAllSimpleMoves(int[][] matrix, int color) {
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

    private int[][] boardToMatrix(AlquerqueBoard board) {
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

    private void applyMoveOnMatrix(int[][] matrix, int[] move) {
        int color = matrix[move[0]][move[1]];
        matrix[move[0]][move[1]] = -1;
        matrix[move[2]][move[3]] = color;
        if (move.length >= 6) matrix[move[4]][move[5]] = -1;
    }
}