package src.alquerque.control;

import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.control.ActionFactory;
import src.boardifier.control.ActionPlayer;
import src.boardifier.control.Controller;
import src.boardifier.control.ControllerMouse;
import src.boardifier.model.Coord2D;
import src.boardifier.model.GameElement;
import src.boardifier.model.Model;
import src.boardifier.model.action.ActionList;
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
        // if bot ignore
        if (model.getCurrentPlayer().getType() != src.boardifier.model.Player.HUMAN) return;

        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();

        // 1. get item on clic
        Coord2D click = new Coord2D(event.getX(), event.getY());
        List<GameElement> elements = control.elementsAt(click);
        if (elements.isEmpty()) return;

        // 2. get cells under clic
        int[] cell = board.getElementCell(elements.get(0));

        if (selected == null) {
            // no selected pawn
            for (GameElement el : elements) {
                if (el instanceof AlquerquePawn) {
                    AlquerquePawn p = (AlquerquePawn) el;
                    if (p.getColor() != model.getIdPlayer()) {
                        selected = p;
                        board.computeValidCells(p);
                        control.update();
                        return;
                    }
                }
            }
        } else {
            //  pawn already selected we try to move it
            int[] dest = board.getElementCell(elements.get(0)); // case cliquée
            control.tryMove(selected, dest[0], dest[1]);
            selected = null;
            board.resetReachableCells(false);
            control.update();
        }
    }
}