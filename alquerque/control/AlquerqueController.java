package alquerque.control;

import alquerque.model.AlquerqueStageModel;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueBoard;
import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;

import java.util.List;
import java.util.Scanner;

public class AlquerqueController extends boardifier.control.Controller {

    public AlquerqueController(Model model, View view) {
        super(model, view);
    }

    public void playTurn() {
        int playerType = model.getCurrentPlayer().getType();
        if (playerType == Player.COMPUTER) {
            playBot1Turn();
        } else {
            playHumanTurn();
        }
    }

    private void playBot1Turn() {
        System.out.println("Bot's turn (" + model.getCurrentPlayerName() + ")...");
        AlquerqueDeciderBot3Jesus decider = new AlquerqueDeciderBot3Jesus(model, this);
        ActionList actionsPlayer = decider.decide();

        new ActionPlayer(model, this, actionsPlayer).start();

    }

    private void playHumanTurn() {
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

        // we check if it's the player pawn
        if (pawn == null || pawn.getColor() != model.getIdPlayer()) {
            System.out.println("Ce n'est pas votre pion !");
            return;
        }

        List<int[]> valid = board.computeValidCells(pawn);
        boolean moove = false;
        for (int[] cell : valid) {
            if (!moove && cell[1] == colEnd && cell[0] == rowEnd ) {
                moove = true;
                int rowMid = (rowStart + rowEnd) / 2;
                int colMid = (colStart + colEnd) / 2;

                if (!board.isEmptyAt(rowMid, colMid)) {
                    AlquerquePawn captured = (AlquerquePawn) board.getElement(rowMid, colMid);
                    ActionList moveActions = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
                    ActionList captureActions = ActionFactory.generateRemoveFromStage(model, captured);
                    captureActions.addAll(moveActions);
                    captureActions.setDoEndOfTurn(true);
                    new ActionPlayer(model, this, captureActions).start();
                } else {
                    ActionList action = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
                    action.setDoEndOfTurn(true);
                    new ActionPlayer(model, this, action).start();
                }

            }
        }

        if (!moove) {
            System.out.println("impossible movement");
        }
    }

    @Override
    public void endOfTurn() {
        model.setNextPlayer();
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        stage.getPlayerName().setText(model.getCurrentPlayerName());
    }

    @Override
    public void stageLoop() {
        while (!model.isEndStage()) {
            update();      // show the board
            playTurn();    // launch current player turn
            endOfTurn();   // play next player turn
        }
        update();          // final show
        System.out.println("Game over ! Winner : " + model.getPlayers().get(model.getIdWinner()).getName());
    }
}