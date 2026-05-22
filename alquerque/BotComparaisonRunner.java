package alquerque;

import alquerque.control.AlquerqueController;
import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;

public class BotComparaisonRunner {

    // =============================
    // CONFIGURATION
    // =============================

    // Number of games
    private static final int NB_GAMES = 100000;

    // Bot for black player (●)
    // 1 = Fred
    // 2 = Jesus
    // 3 = Master Mind
    private static final int BOT_PLAYER_0 = 3;

    // Bot for white player (○)
    private static final int BOT_PLAYER_1 = 2;


    public static void main(String[] args) {

        int winsPlayer0 = 0;
        int winsPlayer1 = 0;
        int draws = 0;

        System.out.println("====================================");
        System.out.println("     BOT TOURNAMENT STARTED");
        System.out.println("====================================");
        System.out.println();

        for (int i = 1; i <= NB_GAMES; i++) {

            System.out.println("Game " + i + " / " + NB_GAMES);

            int result = playOneGame();

            if (result == 0) {
                winsPlayer0++;
                System.out.println("Winner : BLACK (●)");
            }
            else if (result == 1) {
                winsPlayer1++;
                System.out.println("Winner : WHITE (○)");
            }
            else {
                draws++;
                System.out.println("Draw");
            }

            System.out.println();
        }


        // =============================
        // FINAL RESULTS
        // =============================

        System.out.println("====================================");
        System.out.println("          FINAL RESULTS");
        System.out.println("====================================");

        System.out.println("Black bot (" + botName(BOT_PLAYER_0) + ") wins : " + winsPlayer0);
        System.out.println("White bot (" + botName(BOT_PLAYER_1) + ") wins : " + winsPlayer1);
        System.out.println("Draws : " + draws);

        System.out.println();

        double wrBlack = (winsPlayer0 * 100.0) / NB_GAMES;
        double wrWhite = (winsPlayer1 * 100.0) / NB_GAMES;

        System.out.println("Black WR : " + wrBlack + "%");
        System.out.println("White WR : " + wrWhite + "%");
    }


    private static int playOneGame() {

        // Create model
        Model model = new Model();

        // Register stage
        StageFactory.registerModelAndView(
                "alquerque",
                "alquerque.model.AlquerqueStageModel",
                "alquerque.view.AlquerqueStageView"
        );

        // Configure bots
        AlquerqueController.botForPlayer0 = BOT_PLAYER_0;
        AlquerqueController.botForPlayer1 = BOT_PLAYER_1;

        // Add players
        model.addComputerPlayer(botName(BOT_PLAYER_0) + " (●)");
        model.addComputerPlayer(botName(BOT_PLAYER_1) + " (○)");

        // Create view + controller
        View view = new View(model);
        AlquerqueController control = new AlquerqueController(model, view);

        control.setFirstStageName("alquerque");

        try {
            control.startGame();

            // Main game loop
            while (!model.isEndGame() && !model.isEndStage()) {
                control.stageLoop();
            }

        } catch (GameException e) {
            System.out.println("Error while launching game");
            return -1;
        }

        return model.getIdWinner();
    }


    private static String botName(int botChoice) {
        if (botChoice == 1) return "Fred";
        if (botChoice == 2) return "Jesus";
        if (botChoice == 3) return "Master Mind";
        return "Bot";
    }
}