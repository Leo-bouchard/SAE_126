package alquerque.control;

import alquerque.model.AlquerqueStageModel;
import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueBoard;

import java.util.List;
import java.util.Scanner;

public class AlquerqueController extends boardifier.control.Controller {
    public AlquerqueController(Model model, View view) {
        super(model, view);

    }

    public void playTurn() {


        System.out.print("Your turn (" + model.getCurrentPlayerName() + ") > ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println("You wrote : " + input);

        int colStart = input.charAt(0) - 'A';
        int rowStart = Integer.parseInt(input.substring(1, 2)) - 1;
        int colEnd = input.charAt(3) - 'A';
        int rowEnd = Integer.parseInt(input.substring(4, 5)) - 1;

        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        AlquerquePawn pawn = (AlquerquePawn) board.getElement(rowStart, colStart);

        List<int[]> valid = board.computeValidCells(pawn);
        boolean moove = false;
        for (int[] cell : valid) {
            if (cell[1] == colEnd && cell[0] == rowEnd) {
                ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
                actions.setDoEndOfTurn(true);
                new ActionPlayer(model, this, actions).start();
                moove = true;
                actions.setDoEndOfTurn(true);

            }

        }
        if (!moove) {
            System.out.println("impossible movement");
            moove = false;
        }


    }

    @Override
    public void stageLoop() {
        while (!model.isEndStage()) {
            update();      // show the board
            playTurn();    // launch current player turn
            endOfTurn();   // play next player turn
        }
        update();          // final show
    }
}
