package src.alquerque.control;

import src.alquerque.model.AlquerqueStageModel;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueBoard;
import src.alquerque.view.AlquerqueSidePanel;
import src.boardifier.control.ActionFactory;
import src.boardifier.control.ActionPlayer;
import src.boardifier.control.Controller;
import src.boardifier.control.Decider;
import src.boardifier.control.StageFactory;
import src.boardifier.model.GameException;
import src.boardifier.model.Model;
import src.boardifier.model.Player;
import src.boardifier.model.action.ActionList;
import src.boardifier.view.RootPane;
import src.boardifier.view.View;
import javafx.animation.PauseTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AlquerqueController extends Controller {

    // 0 = humain, 1 = Fred, 2 = Jesus, 3 = MasterMind
    public static int botForPlayer0 = 0;
    public static int botForPlayer1 = 0;

    // noms des joueurs (remplis par le StartController)
    public static String namePlayer0 = "Joueur 1";
    public static String namePlayer1 = "Joueur 2";

    // delai entre deux coups de bot (en millisecondes)
    private static final int BOT_DELAY_MS = 800;

    // fichier ou on note les victoires
    private static final String FICHIER_WINS = "src/alquerque/savedData/wings";

    public AlquerqueController(Model model, View view) {
        super(model, view);
    }

    public static void startGame(Stage stage) {
        Model model = new Model();

        // joueur 0 : humain si bot==0, sinon ordinateur
        if (botForPlayer0 == 0) model.addHumanPlayer(namePlayer0);
        else model.addComputerPlayer(namePlayer0);

        // joueur 1
        if (botForPlayer1 == 0) model.addHumanPlayer(namePlayer1);
        else model.addComputerPlayer(namePlayer1);

        StageFactory.registerModelAndView(
                "alquerque",
                "src.alquerque.model.AlquerqueStageModel",
                "src.alquerque.view.AlquerqueStageView"
        );

        RootPane root = new RootPane();
        View view = new View(model, stage, root);
        root.setStyle("-fx-background-color: f5e9c8;");

        AlquerqueController control = new AlquerqueController(model, view);
        control.setFirstStageName("alquerque");
        control.setControlMouse(new AlquerqueControllerMouse(model, view, control));

        try {
            control.startGame();

            AlquerqueSidePanel side = new AlquerqueSidePanel(stage, control, model);
            javafx.scene.control.SplitPane split = new javafx.scene.control.SplitPane();
            split.getItems().addAll(side.getRoot(), view.getRootPane());
            split.setDividerPositions(0.28);

            javafx.scene.Scene scene = new javafx.scene.Scene(split, 1000, 700);
            stage.setScene(scene);
            stage.show();
            control.lancerBotSiNecessaire();

        } catch (GameException e) {
            e.printStackTrace();
        }
    }

    // lance le tour du bot si le joueur courant en est un, avec un delai
    private void lancerBotSiNecessaire() {
        if (model.getCurrentPlayer().getType() == Player.COMPUTER) {
            PauseTransition pause = new PauseTransition(Duration.millis(BOT_DELAY_MS));
            pause.setOnFinished(e -> playBotTurn());
            pause.play();
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
        actions.setDoEndOfTurn(true);
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
        lancerBotSiNecessaire();
    }

    @Override
    public void endGame() {
        enregistrerVictoire();
        super.endGame();
    }

    private void enregistrerVictoire() {
        int idWinner = model.getIdWinner();
        if (idWinner == -1) return;   // match nul : rien a noter

        String gagnant = model.getPlayers().get(idWinner).getName();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FICHIER_WINS, true))) {  // true = ajout
            w.write(gagnant + " win");
            w.newLine();
        } catch (IOException e) {
            System.out.println("Impossible d'ecrire la victoire : " + e.getMessage());
        }
    }
}