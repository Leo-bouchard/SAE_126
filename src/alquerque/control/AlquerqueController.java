package src.alquerque.control;

import src.alquerque.model.AlquerqueStageModel;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueBoard;
import src.alquerque.view.AlquerqueSidePanel;
import src.alquerque.view.AlquerqueEndGameView;
import src.alquerque.view.AlquerqueMainMenuView;
import src.alquerque.view.BoardLook;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AlquerqueController extends Controller {

    public static boolean demoMode = false;

    // 0 = human, 1 = Fred, 2 = Jesus, 3 = MasterMind
    public static int botForPlayer0 = 0;
    public static int botForPlayer1 = 0;

    // player names (filled by the StartController)
    public static String namePlayer0 = "Joueur 1";
    public static String namePlayer1 = "Joueur 2";

    // delay between two bot moves (in milliseconds)
    private static final int BOT_DELAY_MS = 800;

    // delay to let the move animation finish before checking chain captures
    private static final int CAPTURE_CHECK_DELAY_MS = 600;

    private AlquerqueSidePanel sidePanel;

    // multi-capture: pawn currently chaining captures, null if none
    private AlquerquePawn multiCapturePawn = null;

    public boolean isMultiCaptureInProgress() { return multiCapturePawn != null; }
    public AlquerquePawn getMultiCapturePawn() { return multiCapturePawn; }

    public void setSidePanel(AlquerqueSidePanel panel) {
        this.sidePanel = panel;
    }

    // file where the wings balance is stored
    private static final String WINGS_FILE = "src/alquerque/savedData/wings";

    public AlquerqueController(Model model, View view) {
        super(model, view);
    }

    public static void startGame(Stage stage) {
        Model model = new Model();

        // randomly choose which player gets the white pawns (plays first)
        if (new Random().nextBoolean()) {
            String tmpName = namePlayer0; namePlayer0 = namePlayer1; namePlayer1 = tmpName;
            int tmpBot = botForPlayer0; botForPlayer0 = botForPlayer1; botForPlayer1 = tmpBot;
        }

        // player 0: human if bot==0, otherwise computer
        if (botForPlayer0 == 0) model.addHumanPlayer(namePlayer0);
        else model.addComputerPlayer(namePlayer0);

        // player 1
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
            control.setSidePanel(side);
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

    // refreshes the highlight overlay from the board state
    public void refreshHighlights() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        if (stage == null) return;
        BoardLook look = (BoardLook) getElementLook(stage.getBoard());
        if (look != null) look.refreshHighlights();
    }

    // starts the bot turn if the current player is one, with a delay
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

        // during a capture chain, only capture cells are valid destinations
        boolean chaining = (multiCapturePawn != null && pawn == multiCapturePawn);
        if (chaining) {
            board.computeCaptureReachableCells(pawn);
        } else {
            board.computeValidCells(pawn);
        }
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

            multiCapturePawn = pawn;
            board.clearHighlights();
            refreshHighlights();
            actions.setDoEndOfTurn(false);
            new ActionPlayer(model, this, actions).start();

            // wait for the animation to finish, then check if the chain can continue
            PauseTransition wait = new PauseTransition(Duration.millis(CAPTURE_CHECK_DELAY_MS));
            wait.setOnFinished(e -> {
                if (multiCapturePawn == null) return;
                List<int[]> captures = board.computeValidCaptureCells(multiCapturePawn);
                if (captures.isEmpty()) {
                    // no more captures available -> end of turn
                    multiCapturePawn = null;
                    board.resetReachableCells(false);
                    board.clearHighlights();
                    refreshHighlights();
                    endOfTurn();
                } else {
                    // chain continues: highlight only the capture cells, enable Pass
                    board.computeCaptureReachableCells(multiCapturePawn);
                    board.computeCaptureHighlights(multiCapturePawn);
                    if (sidePanel != null) sidePanel.setPassEnabled(true);
                }
                refreshHighlights();
                update();
            });
            wait.play();
        } else {
            multiCapturePawn = null;
            board.clearHighlights();
            refreshHighlights();
            actions = ActionFactory.generateMoveWithinContainer(this, model, pawn, rowEnd, colEnd);
            actions.setDoEndOfTurn(true);
            new ActionPlayer(model, this, actions).start();
        }
    }

    @Override
    public void endOfTurn() {
        multiCapturePawn = null;
        if (sidePanel != null) sidePanel.setPassEnabled(false);
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        if (stage != null) {
            stage.getBoard().clearHighlights();
            refreshHighlights();
        }
        model.setNextPlayer();
        stage = (AlquerqueStageModel) model.getGameStage();
        if (stage == null) return;
        stage.getPlayerName().setText(model.getCurrentPlayerName());
        if (sidePanel != null) sidePanel.refresh();

        // check the end-of-game conditions before letting the next player move
        if (checkEndConditions(stage)) return;

        lancerBotSiNecessaire();
    }

    // returns true if the game just ended
    private boolean checkEndConditions(AlquerqueStageModel stage) {
        int white = stage.getWhitePawnsCount();
        int black = stage.getBlackPawnsCount();

        // one side has no pawn left
        if (white == 0 || black == 0) {
            triggerEnd(white == 0 ? 1 : 0);
            return true;
        }

        // one pawn on each side -> draw
        if (white == 1 && black == 1) {
            triggerEnd(-1);
            return true;
        }

        // neither side can move -> draw
        boolean whiteCanMove = stage.colorHasAnyMove(0);
        boolean blackCanMove = stage.colorHasAnyMove(1);
        if (!whiteCanMove && !blackCanMove) {
            triggerEnd(-1);
            return true;
        }

        return false;
    }

    // sets the winner and shows the end-of-game popup
    private void triggerEnd(int idWinner) {
        model.setIdWinner(idWinner);
        awardWings(idWinner);
        endGame();
    }

    // we capture the winner and trigger the popup before the model gets reset
    @Override
    public void stopStage() {
        int idWinner = model.getIdWinner();
        awardWings(idWinner);
        endGame();
    }

    // shows our own styled end-of-game screen instead of the default Alert
    @Override
    public void endGame() {
        // disable all events so the board can no longer be played
        model.setCaptureEvents(false);

        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        int idWinner = model.getIdWinner();

        // pawn counts for the final score (white = colour 0, black = colour 1)
        int white = (stage != null) ? stage.getWhitePawnsCount() : 0;
        int black = (stage != null) ? stage.getBlackPawnsCount() : 0;

        // name of the winner (empty on a draw)
        String winnerName = "";
        if (idWinner != -1 && idWinner < model.getPlayers().size()) {
            winnerName = model.getPlayers().get(idWinner).getName();
        }

        Stage window = view.getStage();

        AlquerqueEndGameView endView =
                new AlquerqueEndGameView(window, idWinner, winnerName, white, black);

        // Rejouer: full restart -> re-draws the colours and rebuilds the scene
        endView.setOnReplay(() -> startGame(window));
        // Menu: go back to the main menu
        endView.setOnMenu(() -> new AlquerqueMainMenuView(window).display());

        endView.display();
    }

    // gives wings to a human player who beat a bot
    private void awardWings(int idWinner) {
        if (idWinner == -1) return;   // draw: nothing to award

        Player winner = model.getPlayers().get(idWinner);
        if (winner.getType() != Player.HUMAN) return;   // bot win: no reward

        // opponent must be a bot, reward depends on its level
        int opponentBot = (idWinner == 0) ? botForPlayer1 : botForPlayer0;
        if (opponentBot == 0) return;   // opponent was human: no wings

        int reward = opponentBot * 10;  // Fred=10, Jesus=20, MasterMind=30
        writeWings(readWings() + reward);
    }

    // reads the current wings balance from the file (0 if missing)
    public static int readWings() {
        try (BufferedReader r = new BufferedReader(new FileReader(WINGS_FILE))) {
            String line = r.readLine();
            return (line == null) ? 0 : Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    // overwrites the wings file with the new balance
    private static void writeWings(int balance) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(WINGS_FILE, false))) {
            w.write(String.valueOf(balance));
            w.newLine();
        } catch (IOException e) {
            System.out.println("Cannot write wings balance: " + e.getMessage());
        }
    }
}