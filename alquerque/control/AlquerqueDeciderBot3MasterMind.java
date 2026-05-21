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

public class AlquerqueDeciderBot3MasterMind extends Decider {
    public AlquerqueDeciderBot3MasterMind(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int myColor = model.getIdPlayer();

        //get all possible moves for my pawns
        List<int[]> allMoves = new ArrayList<>();

        // each move = {rowStart, colStart, rowEnd, colEnd}
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Object e = board.getElement(row, col);
                if (e == null) continue; // if e is void skip to the next itteration
                AlquerquePawn p = (AlquerquePawn) e;
                if (p.getColor() != myColor) continue;

                // pawn belongs to me, get valid destinations
                List<int[]> validCells = board.computeValidCells(p);
                for (int[] dest : validCells) {
                    allMoves.add(new int[]{row, col, dest[0], dest[1]});
                }
            }
        }

        // no moves available, return empty action
        if (allMoves.isEmpty()) {
            ActionList empty = new ActionList();
            empty.setDoEndOfTurn(true);
            return empty;
        }

        // score each move and keep the best one
        int bestScore = -1;
        int[] bestMove = allMoves.get(0);

        for (int[] move : allMoves) {
            int score = scoreMove(move);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        // build the action list for the chosen move
        return buildActionList(board, bestMove);
    }

    /**
     * +10 for a capture.
     * +2 for moving to a safer position (edge or corner).
     */
    private int scoreMove(int[] move) {
        int rowStart = move[0];
        int colStart = move[1];
        int rowEnd = move[2];
        int colEnd = move[3];

        int score = 0;

        // capture bonus (move of 2 cells)
        if (Math.abs(rowEnd - rowStart) == 2 || Math.abs(colEnd - colStart) == 2) {
            score += 10;
        }

        // safety bonus if we move to a safer cell
        int safetyBefore = edgeBonus(rowStart, colStart);
        int safetyAfter = edgeBonus(rowEnd, colEnd);
        if (safetyAfter > safetyBefore) {
            score += 2;
        }

        return score;
    }

    /**
     * Returns a safety
     * Corner = +3, edge = +2, inside = 0.
     */
    private int edgeBonus(int row, int col) {
        boolean onTopOrBottom = (row == 0 || row == 4);
        boolean onLeftOrRight = (col == 0 || col == 4);

        if (onTopOrBottom && onLeftOrRight) return 3;   // corner
        if (onTopOrBottom || onLeftOrRight) return 2;   // edge
        return 0;                                       // inside
    }

    private ActionList buildActionList(AlquerqueBoard board, int[] move) {
        int rowStart = move[0];
        int colStart = move[1];
        int rowEnd = move[2];
        int colEnd = move[3];

        AlquerquePawn pawn = (AlquerquePawn) board.getElement(rowStart, colStart);

        ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);

        // if capture remove the jumped pawn from the board
        if (Math.abs(rowEnd - rowStart) == 2 || Math.abs(colEnd - colStart) == 2) {
            int rowMid = (rowStart + rowEnd) / 2;
            int colMid = (colStart + colEnd) / 2;
            AlquerquePawn captured = (AlquerquePawn) board.getElement(rowMid, colMid);
            ActionList removeAction = ActionFactory.generateRemoveFromStage(model, captured);
            actions.addAll(removeAction);
        }

        actions.setDoEndOfTurn(true);
        return actions;
    }
}