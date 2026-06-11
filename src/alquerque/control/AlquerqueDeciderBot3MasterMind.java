package src.alquerque.control;

import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.control.Controller;
import src.boardifier.model.Model;
import src.boardifier.model.action.ActionList;

import java.util.Collections;
import java.util.List;

public class AlquerqueDeciderBot3MasterMind extends AlquerqueDeciderBase {

    private static final int DEPTH = 5;

    public AlquerqueDeciderBot3MasterMind(Model model, Controller control) {
        super(model, control);
    }

    // ces 3 methodes ne sont pas utilisees : ce bot redefinit decide() avec minimax
    @Override
    protected int[] chooseFirstCapture(List<int[]> captures, int[][] matrix, int botColor) {
        return captures.get(0);
    }

    @Override
    protected int[] chooseNextCapture(List<int[]> captures, int[][] matrix, int botColor) {
        return captures.get(0);
    }

    @Override
    protected int[] chooseSimpleMove(List<int[]> moves, int[][] matrix, int botColor) {
        return moves.get(0);
    }

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int myColor = model.getIdPlayer();
        int oppColor = 1 - myColor;

        int[][] matrix = boardToMatrix(board);

        List<int[]> myMoves = getLegalMoves(matrix, myColor);
        if (myMoves.isEmpty()) {
            ActionList empty = new ActionList();
            empty.setDoEndOfTurn(true);
            return empty;
        }

        Collections.shuffle(myMoves);

        int[] bestMove = myMoves.get(0);
        int bestScore = Integer.MIN_VALUE;
        for (int[] move : myMoves) {
            int[][] sim = copyMatrix(matrix);
            applyMoveOnMatrix(sim, move);
            int score = minimax(sim, DEPTH - 1, false, myColor, oppColor);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return buildActionForMove(board, matrix, bestMove, myColor);
    }

    private int minimax(int[][] matrix, int depth, boolean isMyTurn, int myColor, int currentColor) {
        if (depth == 0) return evaluatePosition(matrix, myColor);

        List<int[]> moves = getLegalMoves(matrix, currentColor);
        if (moves.isEmpty()) return evaluatePosition(matrix, myColor);

        int nextColor = 1 - currentColor;

        if (isMyTurn) {
            int maxScore = Integer.MIN_VALUE;
            for (int[] move : moves) {
                int[][] sim = copyMatrix(matrix);
                applyMoveOnMatrix(sim, move);
                int score = minimax(sim, depth - 1, false, myColor, nextColor);
                if (score > maxScore) maxScore = score;
            }
            return maxScore;
        } else {
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

    private int edgeBonus(int row, int col) {
        boolean onEdgeRow = (row == 0 || row == 4);
        boolean onEdgeCol = (col == 0 || col == 4);
        if (onEdgeRow && onEdgeCol) return 3;
        if (onEdgeRow || onEdgeCol) return 2;
        return 0;
    }

    private ActionList buildActionForMove(AlquerqueBoard board, int[][] matrix, int[] move, int botColor) {
        ActionList actions = new ActionList();

        if (move.length < 6) {
            AlquerquePawn pawn = (AlquerquePawn) board.getElement(move[0], move[1]);
            actions.addAll(buildSimpleMoveAction(board, move));
            return actions;
        }

        AlquerquePawn capturingPawn = (AlquerquePawn) board.getElement(move[0], move[1]);
        int[][] sim = copyMatrix(matrix);

        addCaptureToActionList(actions, board, capturingPawn, move);
        applyMoveOnMatrix(sim, move);
        int currentRow = move[2];
        int currentCol = move[3];

        while (true) {
            List<int[]> nextCaptures = findCapturesFromPosition(sim, currentRow, currentCol, botColor);
            if (nextCaptures.isEmpty()) break;

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
}
