package alquerque.control;

import alquerque.model.AlquerqueStageModel;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueBoard;
import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;

import java.util.List;
import java.util.Scanner;

public class AlquerqueController extends boardifier.control.Controller {

    // bot choices set by the main before the game starts
    // 1 = Fred (random), 2 = Smart, 3 = Jesus (minimax)
    public static int botForPlayer0 = 1;   // default: Fred
    public static int botForPlayer1 = 1;   // default: Fred

    public AlquerqueController(Model model, View view) {
        super(model, view);
    }

    public void playTurn() {
        int playerType = model.getCurrentPlayer().getType();
        if (playerType == Player.COMPUTER) {
            playBotTurn();
        } else {
            playHumanTurn();
        }
    }

    /**
     * Plays the turn of the current bot.
     * The bot to use depends on which player is currently active.
     */
    private void playBotTurn() {
        System.out.println("Bot's turn (" + model.getCurrentPlayerName() + ")...");

        // pick the correct bot for the current player
        int currentPlayerId = model.getIdPlayer();
        int botChoice;
        if (currentPlayerId == 0) {
            botChoice = botForPlayer0;
        } else {
            botChoice = botForPlayer1;
        }

        // instantiate the right bot class
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

        // small pause so we can see the bot's move
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 800) { }
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

        List<int[]> valid = board.computeValidCells(pawn);
        boolean moove = false;
        for (int[] cell : valid) {
            if (cell[1] == colEnd && cell[0] == rowEnd) {
                ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, rowEnd, colEnd);
                actions.setDoEndOfTurn(true);
                new ActionPlayer(model, this, actions).start();
                moove = true;
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
            update();
            playTurn();
            endOfTurn();
        }
        update();
    }
}