package alquerque;

import alquerque.control.AlquerqueController;
import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;

import java.util.Scanner;

public class AlquerqueConsole {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // 1. Create the global model
        Model model = new Model();

        // 2. Show menu and choose game mode
        int mode = chooseMode(scanner);

        // 3. Setup players according to mode
        setupPlayers(model, scanner, mode);

        // 4. Register stage classes
        StageFactory.registerModelAndView(
                "alquerque",
                "alquerque.model.AlquerqueStageModel",
                "alquerque.view.AlquerqueStageView"
        );

        // 5. Create the view
        View view = new View(model);

        // 6. Create the controller
        AlquerqueController control = new AlquerqueController(model, view);

        // 7. Set first stage
        control.setFirstStageName("alquerque");

        // 8. Launch the game
        try {
            control.startGame();
            control.stageLoop();
        } catch (GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }


    /**
     * Game mode menu.
     * 0 = Player vs Player, 1 = Player vs Bot, 2 = Bot vs Bot
     */
    private static int chooseMode(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("═══════════════════════════════");
            System.out.println("       ALQUERQUE - MENU        ");
            System.out.println("═══════════════════════════════");
            System.out.println(" 1. Player vs Player");
            System.out.println(" 2. Player vs Bot");
            System.out.println(" 3. Bot vs Bot");
            System.out.println("═══════════════════════════════");
            System.out.print("Your choice (1-3) : ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) return 0;
            if (input.equals("2")) return 1;
            if (input.equals("3")) return 2;
            if (input.equals("4")) return 3;


            System.out.println("Invalid choice. Type 1, 2 or 3.");
        }
    }


    /**
     * Bot choice menu.
     * 1 = Random (Fred), 2 = Smart, 3 = Jesus (minimax)
     */
    private static int chooseBot(Scanner scanner, String botLabel) {
        while (true) {
            System.out.println();
            System.out.println("───────────────────────────────");
            System.out.println("    Choose " + botLabel);
            System.out.println("───────────────────────────────");
            System.out.println(" 1. Fred  (random, easy)");
            System.out.println(" 2. Jesus (thinks 1 move)");
            System.out.println(" 3. Master Mind (thinks 4 moves)");
            System.out.println("───────────────────────────────");
            System.out.print("Your choice (1-3) : ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) return 1;
            if (input.equals("2")) return 2;
            if (input.equals("3")) return 3;
            if (input.equals("4")) return 4;


            System.out.println("Invalid choice. Type 1, 2 or 3.");
        }
    }


    private static void setupPlayers(Model model, Scanner scanner, int mode) {
        if (mode == 0) {
            // Player vs Player
            System.out.print("Player 1 name (●) : ");
            String p1Name = scanner.nextLine().trim();
            if (p1Name.isEmpty()) p1Name = "Player 1";
            p1Name = p1Name + " (●)";

            System.out.print("Player 2 name (○) : ");
            String p2Name = scanner.nextLine().trim();
            if (p2Name.isEmpty()) p2Name = "Player 2";
            p2Name = p2Name + " (○)";

            model.addHumanPlayer(p1Name);
            model.addHumanPlayer(p2Name);
        } else if (mode == 1) {
            // Player vs Bot
            System.out.print("Your name (●) : ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Player";
            name = name + " (●)";

            // ask which bot to play against
            int botChoice = chooseBot(scanner, "your opponent bot (○)");
            AlquerqueController.botForPlayer1 = botChoice;

            model.addComputerPlayer(botName(botChoice) + " (○)");
            model.addHumanPlayer(name);
        } else if (mode == 2) {
            // Bot vs Bot
            int bot1Choice = chooseBot(scanner, "Bot 1 (●)");
            int bot2Choice = chooseBot(scanner, "Bot 2 (○)");

            AlquerqueController.botForPlayer0 = bot1Choice;
            AlquerqueController.botForPlayer1 = bot2Choice;

            model.addComputerPlayer(botName(bot1Choice) + " (●)");
            model.addComputerPlayer(botName(bot2Choice) + " (○)");
        } else if (mode == 4) {
        } else if (mode == 4) {
            System.out.println("═══════════════════════════════");
            System.out.println("         DEMO MODE             ");
            System.out.println("═══════════════════════════════");
            System.out.println(" 1. Partie normale");
            System.out.println(" 2. Captures");
            System.out.println(" 3. Multi-captures");
            System.out.println(" 4. Coups invalides (syntaxe)");
            System.out.println(" 5. Coups invalides (règles)");
            System.out.println("═══════════════════════════════");
            System.out.print("Votre choix : ");
            String demoChoice = scanner.nextLine().trim();

            model.addHumanPlayer("Player1 (●)");
            model.addHumanPlayer("Player2 (○)");
        }
    }

    /**
     * Returns the display name of a bot from its number.
     */
    private static String botName(int botChoice) {
        if (botChoice == 1) return "Fred";
        if (botChoice == 2) return "Jesus";
        if (botChoice == 3) return "Master Mind";
        else return "Bot";
    }
}