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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlquerqueDecider_Bot1_Aleatoire_name_Fred extends Decider {

    private static final Random rand = new Random();

    public AlquerqueDecider_Bot1_Aleatoire_name_Fred(Model model, Controller control) {
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

        // shuffle for don't play the same pawn every time
        Collections.shuffle(myPawns);

        // find a pawn how can play
        for (AlquerquePawn pawn : myPawns) {
            List<int[]> validCells = board.computeValidCells(pawn);


            if (!validCells.isEmpty()) {
                // select destination random
                int[] dest = validCells.get(rand.nextInt(validCells.size()));
                int rowDest = dest[0];
                int colDest = dest[1];

                // construc l'ActionList
                ActionList actions = ActionFactory.generateMoveWithinContainer(
                        model, pawn, rowDest, colDest
                );
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