package alquerque;

import alquerque.control.AlquerqueController;
import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;

public class AlquerqueConsole {

    public static void main(String[] args) {

        // 1. Créer le modèle global
        Model model = new Model();

        // 2. Ajouter les joueurs (humain vs humain pour commencer simple)
        model.addHumanPlayer("player1");
        model.addHumanPlayer("player2");

        // 3. Enregistrer le mapping stage ↔ classes
        StageFactory.registerModelAndView(
                "alquerque",
                "alquerque.model.AlquerqueStageModel",
                "alquerque.view.AlquerqueStageView"
        );

        // 4. Créer la vue
        View view = new View(model);

        // 5. Créer le controller
        AlquerqueController control = new AlquerqueController(model, view);

        // 6. Définir le stage de départ
        control.setFirstStageName("alquerque");

        // 7. Lancer la partie
        try {
            control.startGame();
            control.stageLoop();
        } catch (GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}