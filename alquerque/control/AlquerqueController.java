package alquerque.control;

import boardifier.model.Model;
import boardifier.view.View;

import java.util.Scanner;

public class AlquerqueController extends boardifier.control.Controller {
    public AlquerqueController(Model model, View view) {
        super(model, view);
    }

    public void playTurn() {
        System.out.print("À toi de jouer (" + model.getCurrentPlayerName() + ") > ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println("Tu as tapé : " + input);
    }

    @Override
    public void stageLoop() {
        while (!model.isEndStage()) {
            update();      // affiche le plateau
            playTurn();    // fait jouer le joueur courant
            endOfTurn();   // passe au joueur suivant
        }
        update();          // affichage final après la fin
    }
}
