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
import java.util.Collections;
import java.util.List;

public class AlquerqueDeciderBot3MasterMind extends Decider {

    private static final int DEPTH = 1;

    public AlquerqueDeciderBot3MasterMind(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int myColor = model.getIdPlayer();
        int oppColor = 1 - myColor;

        int[][] matrix = boardToMatrix(board);

        // ALQUERQUE RULE: capture is mandatory if available
        List<int[]> myMoves = getLegalMoves(matrix, myColor);

        if (myMoves.isEmpty()) {
            ActionList empty = new ActionList();
            empty.setDoEndOfTurn(true);
            return empty;
        }

        // shuffle to avoid deterministic loops between equal scores
        Collections.shuffle(myMoves);

        // for each move, simulate and run minimax
        int[] bestMove = myMoves.get(0);
        int bestScore = Integer.MIN_VALUE;

        for (int[] move : myMoves) {
            int[][] sim = copyMatrix(matrix);
            applyMoveOnMatrix(sim, move);

            // after my move, it's the opponent's turn
            int score = minimax(sim, DEPTH - 1, false, myColor, oppColor);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return buildActionForMove(board, matrix, bestMove, myColor);
    }


    /**
     * Minimax algorithm.
     * Recursively explores future moves up to "depth" levels.
     */
    private int minimax(int[][] matrix, int depth, boolean isMyTurn, int myColor, int currentColor) {
        if (depth == 0) {
            return evaluatePosition(matrix, myColor);
        }

        List<int[]> moves = getLegalMoves(matrix, currentColor);
        if (moves.isEmpty()) {
            return evaluatePosition(matrix, myColor);
        }

        int nextColor = 1 - currentColor;

        if (isMyTurn) {
            // my turn: maximize
            int maxScore = Integer.MIN_VALUE;
            for (int[] move : moves) {
                int[][] sim = copyMatrix(matrix);
                applyMoveOnMatrix(sim, move);
                int score = minimax(sim, depth - 1, false, myColor, nextColor);
                if (score > maxScore) maxScore = score;
            }
            return maxScore;
        } else {
            // opponent's turn: he plays the worst for us (minimize)
            int minScore = Integer.MAX_VALUE;
            for (int[] move : moves) {
                int[][] sim = copyMatrix(matrix);
                applyMoveOnMatrix(sim, move);
                int score = minimax(sim, depth - 1, true, myColor, nextColor);
                if (score < minScore) minScore = score;
            }
            return minScore;
        }
    }


    /**
     * Evaluates a position. Higher = better for the bot.
     */
    private int evaluatePosition(int[][] matrix, int myColor) {
        int score = 0;
        int oppColor = 1 - myColor;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[row][col] == myColor) {
                    score += 100;
                    score += edgeBonus(row, col);
                } else if (matrix[row][col] == oppColor) {
                    score -= 100;
                    score -= edgeBonus(row, col);
                }
            }
        }
        return score;
    }

    /**
     * Position bonus. Edges and corners are safer in Alquerque.
     */
    private int edgeBonus(int row, int col) {
        boolean onEdgeRow = (row == 0 || row == 4);
        boolean onEdgeCol = (col == 0 || col == 4);
        if (onEdgeRow && onEdgeCol) return 3;   // corner
        if (onEdgeRow || onEdgeCol) return 2;   // edge
        return 0;
    }


    /**
     * Returns all legal moves (Alquerque mandatory capture rule applied).
     * If captures are available, only captures are returned.
     */
    private List<int[]> getLegalMoves(int[][] matrix, int color) {
        List<int[]> captures = findAllCaptures(matrix, color);
        if (!captures.isEmpty()) return captures;
        return findAllSimpleMoves(matrix, color);
    }


    /**
     * Builds the real ActionList for the chosen move.
     * If it's a capture, also adds the full chain (greedy: longest path).
     */
    private ActionList buildActionForMove(AlquerqueBoard board, int[][] matrix, int[] move, int botColor) {
        ActionList actions = new ActionList();

        // simple move (4 elements only, no capture data)
        if (move.length < 6) {
            AlquerquePawn pawn = (AlquerquePawn) board.getElement(move[0], move[1]);
            ActionList moveAction = ActionFactory.generateMoveWithinContainer(model, pawn, move[2], move[3]);
            actions.addAll(moveAction);
            actions.setDoEndOfTurn(true);
            return actions;
        }

        // capture: build the chain
        AlquerquePawn capturingPawn = (AlquerquePawn) board.getElement(move[0], move[1]);
        int[][] sim = copyMatrix(matrix);

        addCaptureToActionList(actions, board, capturingPawn, move);
        applyMoveOnMatrix(sim, move);
        int currentRow = move[2];
        int currentCol = move[3];

        // continue the chain greedily
        while (true) {
            List<int[]> nextCaptures = findCapturesFromPosition(sim, currentRow, currentCol, botColor);
            if (nextCaptures.isEmpty()) break;

            // pick the continuation that leads to the longest chain
            int[] bestNext = nextCaptures.get(0);
            int bestChain = -1;
            for (int[] c : nextCaptures) {
                int[][] simSim = copyMatrix(sim);
                applyMoveOnMatrix(simSim, c);
                int len = 1 + maxChainFrom(simSim, c[2], c[3], botColor);
                if (len > bestChain) {
                    bestChain = len;
                    bestNext = c;
                }
            }

            addCaptureToActionList(actions, board, capturingPawn, bestNext);
            applyMoveOnMatrix(sim, bestNext);
            currentRow = bestNext[2];
            currentCol = bestNext[3];
        }

        actions.setDoEndOfTurn(true);
        return actions;
    }


    /**
     * Recursively computes the maximum chain length from a position.
     */
    private int maxChainFrom(int[][] matrix, int row, int col, int color) {
        List<int[]> nextCaptures = findCapturesFromPosition(matrix, row, col, color);
        if (nextCaptures.isEmpty()) return 0;

        int best = 0;
        for (int[] c : nextCaptures) {
            int[][] sim = copyMatrix(matrix);
            applyMoveOnMatrix(sim, c);
            int sub = 1 + maxChainFrom(sim, c[2], c[3], color);
            if (sub > best) best = sub;
        }
        return best;
    }


    // === HELPERS ===

    private void addCaptureToActionList(ActionList actions, AlquerqueBoard board, AlquerquePawn pawn, int[] capture) {
        AlquerquePawn captured = (AlquerquePawn) board.getElement(capture[4], capture[5]);
        ActionList move = ActionFactory.generateMoveWithinContainer(model, pawn, capture[2], capture[3]);
        ActionList remove = ActionFactory.generateRemoveFromStage(model, captured);
        actions.addAll(move);
        actions.addAll(remove);
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

    private int[][] copyMatrix(int[][] matrix) {
        int[][] copy = new int[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                copy[row][col] = matrix[row][col];
            }
        }
        return copy;
    }

    private void applyMoveOnMatrix(int[][] matrix, int[] move) {
        int color = matrix[move[0]][move[1]];
        matrix[move[0]][move[1]] = -1;
        matrix[move[2]][move[3]] = color;
        if (move.length >= 6) matrix[move[4]][move[5]] = -1;
    }
}