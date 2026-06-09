package src.alquerque.control;

import src.alquerque.model.AlquerqueStageModel;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueBoard;
import src.boardifier.control.ActionFactory;
import src.boardifier.control.ActionPlayer;
import src.boardifier.control.Controller;
import src.boardifier.control.Decider;
import src.boardifier.model.Model;
import src.boardifier.model.Player;
import src.boardifier.model.action.ActionList;
import src.boardifier.view.View;

import java.util.List;
import java.util.Scanner;

public class AlquerqueController extends Controller {

    // bot choices set by the main before the game starts
    // 1 = Fred (random), 2 = Smart, 3 = Jesus (minimax)
    public static int botForPlayer0 = 3;
    public static int botForPlayer1 = 3;

    private final Scanner scanner;

    public AlquerqueController(Model model, View view, Scanner scanner) {
        super(model, view);
        this.scanner = scanner;
    }

    public void playTurn() {
        int playerType = model.getCurrentPlayer().getType();
        if (playerType == Player.COMPUTER) {
            playBotTurn();
        } else {
            playHumanTurn();
        }
    }

    private void playBotTurn() {
        System.out.println("Bot's turn (" + model.getCurrentPlayerName() + ")...");

        int currentPlayerId = model.getIdPlayer();
        int botChoice = (currentPlayerId == 0) ? botForPlayer0 : botForPlayer1;

        Decider decider;
        if (botChoice == 2) {
            decider = new AlquerqueDeciderBot2Jesus(model, this);
        } else if (botChoice == 3) {
            decider = new AlquerqueDeciderBot3MasterMind(model, this);
        } else {
            decider = new AlquerqueDeciderBot1AleatoirenameFred(model, this);
        }

        ActionList actions = decider.decide();
        new ActionPlayer(model, this, actions).start();

        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    public int[] parseInput(String input) {
        int colStart = input.charAt(0) - 'A';
        int rowStart = Integer.parseInt(input.substring(1, 2)) - 1;
        int colEnd = input.charAt(3) - 'A';
        int rowEnd = Integer.parseInt(input.substring(4, 5)) - 1;
        return new int[]{rowStart, colStart, rowEnd, colEnd};
    }

    public AlquerquePawn getPawnAt(AlquerqueBoard board, int row, int col) {
        AlquerquePawn pawn = (AlquerquePawn) board.getElement(row, col);
        if (pawn == null || pawn.getColor() == model.getIdPlayer()) {
            System.out.println("Not your pawn!");
            return null;
        }
        return pawn;
    }

    private void executeCapture(AlquerqueBoard board, AlquerquePawn pawn, int rowEnd, int colEnd) {
        int[] curPos = board.getElementCell(pawn);
        int rowMid = (curPos[0] + rowEnd) / 2;
        int colMid = (curPos[1] + colEnd) / 2;
        AlquerquePawn captured = (AlquerquePawn) board.getElement(rowMid, colMid);
        if (captured == null) return;
        ActionList moveActions = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
        ActionList captureActions = ActionFactory.generateRemoveFromStage(model, captured);
        moveActions.addAll(captureActions);
        moveActions.setDoEndOfTurn(false);
        new ActionPlayer(model, this, moveActions).start();
    }

    private void handleMultiCapture(AlquerqueBoard board, AlquerquePawn pawn) {
        List<int[]> newValid = board.computeValidCaptureCells(pawn);
        while (newValid != null && !newValid.isEmpty()) {
            update();
            System.out.println("You can still eat a pawn!");
            System.out.print("Choose capture destination > ");

            String nextInput = "";
            try {
                nextInput = scanner.nextLine().trim();
            } catch (Exception e) {
            break;
        }
        int nextColEnd = nextInput.charAt(0) - 'A';
            int nextRowEnd = Integer.parseInt(nextInput.substring(1, 2)) - 1;

            boolean validCapture = false;
            for (int[] c : newValid) {
                if (c[0] == nextRowEnd && c[1] == nextColEnd) {
                    validCapture = true;
                    break;
                }
            }
            if (!validCapture) {
                System.out.println("Invalid capture!");
                continue;
            }

            executeCapture(board, pawn, nextRowEnd, nextColEnd);
            newValid = board.computeValidCaptureCells(pawn);
        }
    }

    private void playHumanTurn() {
        System.out.print("Your turn (" + model.getCurrentPlayerName() + ") > ");
        String input = scanner.nextLine().trim();

        if (input.equals("stop")) {
            model.stopStage();
            return;
        }

        int[] coords;
        try {
            coords = parseInput(input);
        } catch (Exception e) {
            System.out.println("Invalid input format! Use format: A1 B2");
            playHumanTurn();
            return;
        }

        int rowStart = coords[0], colStart = coords[1];
        int rowEnd = coords[2], colEnd = coords[3];

        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        AlquerquePawn pawn = getPawnAt(board, rowStart, colStart);
        if (pawn == null) {
            playHumanTurn();
            return;
        }

        board.computeValidCells(pawn);
        boolean moove = false;
        if (board.canReachCell(rowEnd, colEnd)) {
            moove = true;
            if (Math.abs(rowEnd - rowStart) == 2 || Math.abs(colEnd - colStart) == 2) {
                executeCapture(board, pawn, rowEnd, colEnd);
                handleMultiCapture(board, pawn);
            } else {
                ActionList action = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
                action.setDoEndOfTurn(true);
                new ActionPlayer(model, this, action).start();
            }
        }
        if (!moove) {
            System.out.println("Impossible movement!");
            playHumanTurn();
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
        int turnsWithoutCapture = 0;
        int maxTurnsWithoutCapture = 40;
        int previousPawnCount = countPawns();

        while (!model.isEndStage()) {
            update();
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            playTurn();
            endOfTurn();

            int currentPawnCount = countPawns();
            if (currentPawnCount < previousPawnCount) {
                turnsWithoutCapture = 0;
                previousPawnCount = currentPawnCount;
            } else {
                turnsWithoutCapture++;
            }

            if (turnsWithoutCapture >= maxTurnsWithoutCapture) {
                System.out.println("Match nul : 40 tours sans capture.");
                model.setIdWinner(-1);
                model.stopStage();
                break;
            }
        }

        update();
    }

    private int countPawns() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int count = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (!board.isEmptyAt(row, col)) count++;
            }
        }
        return count;
    }
}