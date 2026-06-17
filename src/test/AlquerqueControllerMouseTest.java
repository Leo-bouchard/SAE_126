package test;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import alquerque.control.AlquerqueController;
import alquerque.control.AlquerqueControllerMouse;
import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.ContainerLook;
import boardifier.view.RootPane;
import boardifier.view.View;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AlquerqueControllerMouseTest {

    private Model model;
    private View view;
    private AlquerqueController control;
    private AlquerqueStageModel stage;
    private AlquerqueBoard board;
    private ContainerLook boardLook;
    private AlquerqueControllerMouse mouse;

    @BeforeEach
    public void setup() {
        model = Mockito.mock(Model.class);
        view = Mockito.mock(View.class);
        control = Mockito.mock(AlquerqueController.class);
        stage = Mockito.mock(AlquerqueStageModel.class);
        board = Mockito.mock(AlquerqueBoard.class);
        boardLook = Mockito.mock(ContainerLook.class);

        when(view.getRootPane()).thenReturn(new RootPane());
        when(model.getGameStage()).thenReturn(stage);
        when(stage.getBoard()).thenReturn(board);
        when(control.getElementLook(board)).thenReturn(boardLook);
        when(model.getCurrentPlayer()).thenReturn(Player.createHumanPlayer("J1"));
        when(model.getIdPlayer()).thenReturn(0);

        mouse = new AlquerqueControllerMouse(model, view, control);
    }

    private MouseEvent click() {
        return new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, false, false, false, null);
    }

    @Test
    public void testHandle_joueurOrdinateur_clicIgnore() {
        when(model.getCurrentPlayer()).thenReturn(Player.createComputerPlayer("Bot"));
        mouse.handle(click());
        verify(control, never()).getElementLook(any());
        verify(control, never()).tryMove(any(), anyInt(), anyInt());
    }

    @Test
    public void testHandle_clicHorsPlateau_aucuneAction() {
        when(boardLook.getCellFromSceneLocation(anyDouble(), anyDouble())).thenReturn(null);
        mouse.handle(click());
        verify(board, never()).getElement(anyInt(), anyInt());
        verify(control, never()).tryMove(any(), anyInt(), anyInt());
    }

    @Test
    public void testHandle_clicPionDeSaCouleur_selectionEtHighlights() {
        AlquerquePawn pawn = mock(AlquerquePawn.class);
        when(pawn.getColor()).thenReturn(0);
        when(boardLook.getCellFromSceneLocation(anyDouble(), anyDouble())).thenReturn(new int[]{2, 2});
        when(board.getElement(2, 2)).thenReturn(pawn);

        mouse.handle(click());

        verify(board).computeValidCells(pawn);
        verify(board).computeHighlights(pawn);
        verify(control).refreshHighlights();
        verify(control).update();
        verify(control, never()).tryMove(any(), anyInt(), anyInt());
    }

    @Test
    public void testHandle_clicPionAdverse_pasDeSelection() {
        AlquerquePawn pawn = mock(AlquerquePawn.class);
        when(pawn.getColor()).thenReturn(1);
        when(boardLook.getCellFromSceneLocation(anyDouble(), anyDouble())).thenReturn(new int[]{2, 2});
        when(board.getElement(2, 2)).thenReturn(pawn);

        mouse.handle(click());

        verify(board, never()).computeValidCells(any());
        verify(control, never()).tryMove(any(), anyInt(), anyInt());
    }

    @Test
    public void testHandle_clicCaseVide_pasDeSelection() {
        when(boardLook.getCellFromSceneLocation(anyDouble(), anyDouble())).thenReturn(new int[]{3, 3});
        when(board.getElement(3, 3)).thenReturn(null);

        mouse.handle(click());

        verify(board, never()).computeValidCells(any());
        verify(control, never()).tryMove(any(), anyInt(), anyInt());
    }

    @Test
    public void testHandle_deuxiemeClic_declencheTryMove() {
        AlquerquePawn pawn = mock(AlquerquePawn.class);
        when(pawn.getColor()).thenReturn(0);
        when(boardLook.getCellFromSceneLocation(anyDouble(), anyDouble()))
                .thenReturn(new int[]{2, 2})
                .thenReturn(new int[]{2, 3});
        when(board.getElement(2, 2)).thenReturn(pawn);

        mouse.handle(click());
        mouse.handle(click());

        verify(control).tryMove(pawn, 2, 3);
        verify(board).resetReachableCells(false);
    }

    @Test
    public void testHandle_multiCaptureEnCours_destinationEnvoyeeDirectement() {
        AlquerquePawn pawn = mock(AlquerquePawn.class);
        when(control.isMultiCaptureInProgress()).thenReturn(true);
        when(control.getMultiCapturePawn()).thenReturn(pawn);
        when(boardLook.getCellFromSceneLocation(anyDouble(), anyDouble())).thenReturn(new int[]{2, 4});

        mouse.handle(click());

        verify(control).tryMove(pawn, 2, 4);
        verify(board, never()).computeValidCells(any());
    }

    @Test
    public void testHandle_multiCapture_neGardePasLaSelectionApres() {
        AlquerquePawn pawn = mock(AlquerquePawn.class);
        when(control.isMultiCaptureInProgress()).thenReturn(true);
        when(control.getMultiCapturePawn()).thenReturn(pawn);
        when(boardLook.getCellFromSceneLocation(anyDouble(), anyDouble())).thenReturn(new int[]{2, 4});
        mouse.handle(click());

        when(control.isMultiCaptureInProgress()).thenReturn(false);
        when(board.getElement(2, 4)).thenReturn(null);
        mouse.handle(click());

        verify(control, times(1)).tryMove(any(), anyInt(), anyInt());
    }
}
