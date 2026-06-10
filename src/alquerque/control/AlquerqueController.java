package src.alquerque.control;

import src.alquerque.model.AlquerqueStageModel;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueBoard;
import src.boardifier.control.ActionFactory;
import src.boardifier.control.ActionPlayer;
import src.boardifier.control.Controller;
import src.boardifier.control.Decider;
import src.boardifier.control.StageFactory;
import src.boardifier.model.GameException;
import src.boardifier.model.Model;
import src.boardifier.model.action.ActionList;
import src.boardifier.view.RootPane;
import src.boardifier.view.View;
import javafx.stage.Stage;

public class AlquerqueController extends Controller {

    public static int botForPlayer0 = 3;
    public static int botForPlayer1 = 3;

    public AlquerqueController(Model model, View view) {
        super(model, view);
    }

    public static void startGame(Stage stage) {
        Model model = new Model();
        model.addHumanPlayer("Joueur 1");
        model.addHumanPlayer("Joueur 2");

        StageFactory.registerModelAndView(
                "alquerque",
                "src.alquerque.model.AlquerqueStageModel",
                "src.alquerque.view.AlquerqueStageView"
        );

        RootPane root = new RootPane();
        View view = new View(model, stage, root);

        AlquerqueController control = new AlquerqueController(model, view);
        control.setFirstStageName("alquerque");
        control.setControlMouse(new AlquerqueControllerMouse(model, view, control));

        try {
            control.startGame();
            stage.show();
        } catch (GameException e) {
            e.printStackTrace();
        }
    }

    public void playBotTurn() {
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
    }

    public void tryMove(AlquerquePawn pawn, int rowEnd, int colEnd) {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int[] start = board.getElementCell(pawn);
        int rowStart = start[0], colStart = start[1];

        board.computeValidCells(pawn);
        if (!board.canReachCell(rowEnd, colEnd)) {
            return;
        }

        boolean isCapture = Math.abs(rowEnd - rowStart) == 2 || Math.abs(colEnd - colStart) == 2;

        ActionList actions;
        if (isCapture) {
            // déplacement + suppression du pion sauté dans UNE seule liste
            int rowMid = (rowStart + rowEnd) / 2;
            int colMid = (colStart + colEnd) / 2;
            AlquerquePawn captured = (AlquerquePawn) board.getElement(rowMid, colMid);
            actions = ActionFactory.generateMoveWithinContainer(this, model, pawn, rowEnd, colEnd);
            if (captured != null) {
                actions.addAll(ActionFactory.generateRemoveFromStage(model, captured));
            }
        } else {
            actions = ActionFactory.generateMoveWithinContainer(this, model, pawn, rowEnd, colEnd);
        }

        actions.setDoEndOfTurn(true);
        new ActionPlayer(model, this, actions).start();
    }

    @Override
    public void endOfTurn() {
        model.setNextPlayer();
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        stage.getPlayerName().setText(model.getCurrentPlayerName());
    }
}