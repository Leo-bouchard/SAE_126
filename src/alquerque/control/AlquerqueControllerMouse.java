package alquerque.control;

import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.control.Controller;
import boardifier.control.ControllerMouse;
import boardifier.model.Model;
import boardifier.view.ContainerLook;
import boardifier.view.View;
import javafx.scene.input.MouseEvent;

public class AlquerqueControllerMouse extends ControllerMouse {

    private AlquerqueController control;
    private AlquerquePawn selected = null; // pawn selected
    public AlquerqueControllerMouse(Model model, View view, Controller control) {
        super(model, view, control);
        this.control = (AlquerqueController) control;
    }
    @Override
    public void handle(MouseEvent event) {
        if (model.getCurrentPlayer().getType() != boardifier.model.Player.HUMAN) return;

        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();

        ContainerLook boardLook = (ContainerLook) control.getElementLook(board);
        int[] dest = boardLook.getCellFromSceneLocation(event.getSceneX(), event.getSceneY());
        if (dest == null) return; // click outside the board

        int row = dest[0], col = dest[1];

        // during a capture chain, the pawn is imposed: clicks are only destinations
        if (control.isMultiCaptureInProgress()) {
            selected = control.getMultiCapturePawn();
            System.out.println("Multi-capture : destination [" + row + "," + col + "]");
            control.tryMove(selected, row, col);
            selected = null;
            return;
        }

        if (selected == null) {
            AlquerquePawn p = (AlquerquePawn) board.getElement(row, col);
            System.out.println("Clic case [" + row + "," + col + "], pion = " + p);
            if (p != null && p.getColor() == model.getIdPlayer()) {
                selected = p;
                board.computeValidCells(p);
                board.computeHighlights(p);
                control.refreshHighlights();
                control.update();
                System.out.println("Pion sélectionné !");
            }
        } else {
            System.out.println("Tentative move vers [" + row + "," + col + "]");
            control.tryMove(selected, row, col);
            selected = null;
            board.resetReachableCells(false);
        }
    }
}