package Test;

import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.alquerque.control.AlquerqueController;
import src.boardifier.view.View;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import src.boardifier.model.TextElement;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlquerqueControllerTest {

    @Mock
    private Model model;

    @Mock
    private View view;

    @Mock
    private AlquerqueStageModel stageModel;

    @Mock
    private AlquerqueBoard board;

    private AlquerqueController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Scanner scanner=null;
        controller = new AlquerqueController(model, view,scanner);
    }

    // NOTE : parseInput et getPawnAt doivent être "public" dans AlquerqueController pour ces tests

    @Test
    public void testParseInput_formatValide_retourneCoordonnees() {
        int[] coords = controller.parseInput("A1 B2");
        assertEquals(0, coords[0]); // rowStart
        assertEquals(0, coords[1]); // colStart
        assertEquals(1, coords[2]); // rowEnd
        assertEquals(1, coords[3]); // colEnd
    }

    @Test
    public void testParseInput_colonneEtLigneMaximales() {
        int[] coords = controller.parseInput("E5 A1");
        assertEquals(4, coords[0]);
        assertEquals(4, coords[1]);
        assertEquals(0, coords[2]);
        assertEquals(0, coords[3]);
    }

    @Test
    public void testParseInput_memeCase_coordonneesIdentiques() {
        int[] coords = controller.parseInput("C3 C3");
        assertEquals(2, coords[0]);
        assertEquals(2, coords[1]);
        assertEquals(2, coords[2]);
        assertEquals(2, coords[3]);
    }

    @Test
    public void testGetPawnAt_pionAbsent_retourneNull() {
        when(model.getIdPlayer()).thenReturn(0);
        when(board.getElement(0, 0)).thenReturn(null);

        AlquerquePawn result = controller.getPawnAt(board, 0, 0);

        assertNull(result);
    }

    @Test
    public void testGetPawnAt_pionMauvaiseCouleur_retourneNull() {
        // player 0 joue les pions couleur 1 (noirs), couleur 0 = adverse
        when(model.getIdPlayer()).thenReturn(0);
        AlquerquePawn pawn = mock(AlquerquePawn.class);
        when(pawn.getColor()).thenReturn(0); // couleur adverse pour player 0
        when(board.getElement(0, 0)).thenReturn(pawn);

        AlquerquePawn result = controller.getPawnAt(board, 0, 0);

        assertNull(result);
    }

    @Test
    public void testGetPawnAt_pionBonneCouleur_retournePion() {
        // player 1 joue les pions couleur 0 (blancs)
        when(model.getIdPlayer()).thenReturn(1);
        AlquerquePawn pawn = mock(AlquerquePawn.class);
        when(pawn.getColor()).thenReturn(0); // couleur correcte pour player 1
        when(board.getElement(2, 3)).thenReturn(pawn);

        AlquerquePawn result = controller.getPawnAt(board, 2, 3);

        assertEquals(pawn, result);
    }

    @Test
    public void testEndOfTurn_appelleSetNextPlayer() {
        AlquerqueStageModel stage = mock(AlquerqueStageModel.class);
        TextElement text = mock(TextElement.class);
        when(model.getGameStage()).thenReturn(stage);
        when(stage.getPlayerName()).thenReturn(text);
        when(model.getCurrentPlayerName()).thenReturn("Player2");

        controller.endOfTurn();

        verify(model).setNextPlayer();
        verify(text).setText("Player2");
    }
}
