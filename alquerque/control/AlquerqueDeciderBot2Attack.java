package alquerque.control;

import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlquerqueDeciderBot2Attack extends Decider {

    private static final Random rand = new Random();

    public AlquerqueDeciderBot2Attack(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int botColor = model.getIdPlayer();  // color bot get

        // recup bot pawn
        List<AlquerquePawn> myPawns = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Object e = board.getElement(row, col);
                if (e == null) continue;
                AlquerquePawn p = (AlquerquePawn) e;
                if (p.getColor() == botColor) {
                    myPawns.add(p);
                }
            }
        }

        // shuffle to do not play the same pawn every time
        Collections.shuffle(myPawns);

        // find a pawn wich can play
        for (AlquerquePawn pawn : myPawns) {
            List<int[]> validCells = board.computeValidCells(pawn);


            if (!validCells.isEmpty()) {
                // select random destination
                int[] dest = validCells.get(rand.nextInt(validCells.size()));
                int rowEnd = dest[0];
                int colEnd = dest[1];

                // get pawn location
                int[] src = board.getElementCell(pawn);
                int rowStart = src[0];
                int colStart = src[1];

                // construct ActionList
                ActionList actions = ActionFactory.generateMoveWithinContainer(
                        model, pawn, rowEnd, colEnd
                );
                int rowMid = (rowStart + rowEnd) / 2;
                int colMid = (colStart + colEnd) / 2;

                if (!board.isEmptyAt(rowMid, colMid)) {
                    AlquerquePawn captured = (AlquerquePawn) board.getElement(rowMid, colMid);
                    ActionList captureActions = ActionFactory.generateRemoveFromStage(model, captured);
                    ActionList moveActions = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
                    captureActions.addAll(moveActions);
                    captureActions.setDoEndOfTurn(true);
                    new ActionPlayer(model, control, captureActions).start();
                } else {
                    ActionList action = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
                    action.setDoEndOfTurn(true);
                    new ActionPlayer(model, control, action).start();
                }

                actions.setDoEndOfTurn(true);
                return actions;
            }
        }

        // if we don't find
        ActionList empty = new ActionList();
        empty.setDoEndOfTurn(true);
        return empty;
    }
}