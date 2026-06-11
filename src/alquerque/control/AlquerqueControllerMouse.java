package src.alquerque.control;

import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.control.ActionFactory;
import src.boardifier.control.ActionPlayer;
import src.boardifier.control.Controller;
import src.boardifier.control.ControllerMouse;
import src.boardifier.model.GameElement;
import src.boardifier.model.Model;
import src.boardifier.model.action.ActionList;
import src.boardifier.view.ContainerLook;
import src.boardifier.view.View;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class AlquerqueControllerMouse extends ControllerMouse {

    private AlquerqueController control;
    private AlquerquePawn selected = null; //pawn selected
    public AlquerqueControllerMouse(Model model, View view, Controller control) {
        super(model, view, control);
        this.control = (AlquerqueController) control;
    }
    @Override
    public void handle(MouseEvent event) {
        if (model.getCurrentPlayer().getType() != src.boardifier.model.Player.HUMAN) return;

        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();

        ContainerLook boardLook = (ContainerLook) control.getElementLook(board);
        int[] dest = boardLook.getCellFromSceneLocation(event.getSceneX(), event.getSceneY());
        if (dest == null) return; // clic hors plateau

        int row = dest[0], col = dest[1];

        if (selected == null) {
            AlquerquePawn p = (AlquerquePawn) board.getElement(row, col);
            System.out.println("Clic case [" + row + "," + col + "], pion = " + p);
            if (p != null && p.getColor() == model.getIdPlayer()) {
                selected = p;
                board.computeValidCells(p);
                control.update();
                System.out.println("Pion sélectionné !");
            }
        } else {
            System.out.println("Tentative move vers [" + row + "," + col + "]");
            control.tryMove(selected, row, col);
            selected = null;
            board.resetReachableCells(false);
            control.update();
        }
    }
}