package alquerque;

import alquerque.control.AlquerqueController;
import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import org.mockito.internal.matchers.Null;

import java.util.Scanner;

public class BotComparaisonRunner {

    // =============================
    // CONFIGURATION
    // =============================

    private static final int NB_GAMES = 1;

    // 1 = Fred, 2 = Jesus, 3 = Master Mind
    private static final int BOT_PLAYER_0 = 2;
    private static final int BOT_PLAYER_1 = 3;


    public static void main(String[] args) {

        int winsPlayer0 = 0;
        int winsPlayer1 = 0;
        int draws = 0;
        int errors = 0;

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
            else if (result == -1) {
                draws++;
                System.out.println("Draw");
            }
            else {
                errors++;
                System.out.println("Game error, skipped");
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
        if (errors > 0) {
            System.out.println("Errors : " + errors);
        }

        System.out.println();

        int validGames = NB_GAMES - errors;
        if (validGames > 0) {
            double wrBlack = (winsPlayer0 * 100.0) / validGames;
            double wrWhite = (winsPlayer1 * 100.0) / validGames;
            double drawRate = (draws * 100.0) / validGames;

            System.out.println("Black WR : " + wrBlack + "%");
            System.out.println("White WR : " + wrWhite + "%");
            System.out.println("Draw rate : " + drawRate + "%");
        }
    }


    private static int playOneGame() {

        Model model = new Model();

        StageFactory.registerModelAndView(
                "alquerque",
                "alquerque.model.AlquerqueStageModel",
                "alquerque.view.AlquerqueStageView"
        );

        AlquerqueController.botForPlayer0 = BOT_PLAYER_0;
        AlquerqueController.botForPlayer1 = BOT_PLAYER_1;

        model.addComputerPlayer(botName(BOT_PLAYER_0) + " (●)");
        model.addComputerPlayer(botName(BOT_PLAYER_1) + " (○)");

        View view = new View(model);
        Scanner scanner = null;
        AlquerqueController control = new AlquerqueController(model, view,scanner);

        control.setFirstStageName("alquerque");

        try {
            control.startGame();

            while (!model.isEndGame() && !model.isEndStage()) {
                control.stageLoop();
            }

        } catch (GameException e) {
            System.out.println("Error while launching game");
            return -99;
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