package alquerque;

import alquerque.control.AlquerqueController;
import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;

import java.util.Scanner;

public class AlquerqueConsole {



    public static void main(String[] args) {        // cette fonction est refaite avec IA sur les truc d'avant j'ai juste modif quelque truc pour faire le menu

        Scanner scanner = new Scanner(System.in);

        // 1. Créer le modèle global
        Model model = new Model();

        // 2. Afficher le menu et choisir le mode
        int mode = chooseMode(scanner);

        // 3. Configurer les joueurs selon le mode
        setupPlayers(model, scanner, mode);

        // 4. Enregistrer le mapping stage ↔ classes
        StageFactory.registerModelAndView(
                "alquerque",
                "alquerque.model.AlquerqueStageModel",
                "alquerque.view.AlquerqueStageView"
        );

        // 5. Créer la vue
        View view = new View(model);

        // 6. Créer le controller
        AlquerqueController control = new AlquerqueController(model, view);

        // 7. Définir le stage de départ
        control.setFirstStageName("alquerque");

        // 8. Lancer la partouze
        try {
            control.startGame();
            control.stageLoop();
        } catch (GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }



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



            System.out.println("Invalid choice. Type 1, 2 or 3.");
        }
    }



    private static void setupPlayers(Model model, Scanner scanner, int mode) {
        if (mode == 0) {
            System.out.print("Player 1 name (○) : ");
            String p1Name = scanner.nextLine().trim();
            if (p1Name.isEmpty()) p1Name = "Player 1";

            System.out.print("Player name 2 (●) : ");
            String p2Name = scanner.nextLine().trim();
            if (p2Name.isEmpty()) p2Name = "Player 2";

            model.addHumanPlayer(p1Name);
            model.addHumanPlayer(p2Name);
        }
        else if (mode == 1) {
            System.out.print("Your name (●) : ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Player";

            model.addHumanPlayer(name);
            model.addComputerPlayer("Bot");
        }
        else if (mode == 2) {
            model.addComputerPlayer("Bot 1");
            model.addComputerPlayer("Bot 2");
        }
    }
}