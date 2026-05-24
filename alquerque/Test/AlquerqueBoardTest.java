package alquerque.Test;

import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlquerqueBoardTest {

    private Model model;
    private AlquerqueStageModel stageModel;
    private AlquerqueBoard board;

    @BeforeEach
    public void setup() {
        model = Mockito.mock(Model.class);
        stageModel = Mockito.mock(AlquerqueStageModel.class);
        board = new AlquerqueBoard(0, 0, stageModel);
    }

    private AlquerquePawn createAndPlace(int color, int row, int col) {
        AlquerquePawn pawn = new AlquerquePawn(color, stageModel);
        board.addElement(pawn, row, col);
        return pawn;
    }

    @Test
    public void testComputeValidCells_pionCentreSurCaseConnectee_8Directions() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        List<int[]> valid = board.computeValidCells(pawn);
        assertEquals(8, valid.size());
    }

    @Test
    public void testComputeValidCells_pionSurCaseNonConnectee_PasDeDiagonale() {
        AlquerquePawn pawn = createAndPlace(0, 0, 1);
        List<int[]> valid = board.computeValidCells(pawn);
        for (int[] cell : valid) {
            int dr = Math.abs(cell[0] - 0);
            int dc = Math.abs(cell[1] - 1);
            assertFalse(dr == 1 && dc == 1,
                    "Mouvement diagonal interdit sur case non connectée (0,1)");
        }
    }

    @Test
    public void testComputeValidCells_pionEnCoin_MouvementsLimitesParBord() {
        AlquerquePawn pawn = createAndPlace(0, 0, 0);
        List<int[]> valid = board.computeValidCells(pawn);
        assertEquals(3, valid.size());
    }

    @Test
    public void testComputeValidCells_caseBloqueeParAllie_NonIncluse() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(0, 1, 1);
        createAndPlace(0, 1, 2);
        createAndPlace(0, 1, 3);
        createAndPlace(0, 2, 1);
        createAndPlace(0, 2, 3);
        createAndPlace(0, 3, 1);
        createAndPlace(0, 3, 2);
        createAndPlace(0, 3, 3);
        List<int[]> valid = board.computeValidCells(pawn);
        assertEquals(0, valid.size());
    }

    @Test
    public void testComputeValidCells_captureSimple_SautParDessusEnnemi() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        List<int[]> valid = board.computeValidCells(pawn);
        boolean captureFound = valid.stream()
                .anyMatch(c -> c[0] == 2 && c[1] == 4);
        assertTrue(captureFound, "La case de capture (2,4) doit être valide");
    }

    @Test
    public void testComputeValidCells_captureImpossible_CaseAterissageOccupee() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        createAndPlace(0, 2, 4);
        List<int[]> valid = board.computeValidCells(pawn);
        boolean captureFound = valid.stream()
                .anyMatch(c -> c[0] == 2 && c[1] == 4);
        assertFalse(captureFound, "La capture sur case occupée ne doit pas être possible");
    }

    @Test
    public void testComputeValidCells_captureHorsBord_NonIncluse() {
        AlquerquePawn pawn = createAndPlace(0, 0, 3);
        createAndPlace(1, 0, 4);
        List<int[]> valid = board.computeValidCells(pawn);
        boolean outOfBounds = valid.stream()
                .anyMatch(c -> c[1] >= 5 || c[0] >= 5 || c[0] < 0 || c[1] < 0);
        assertFalse(outOfBounds, "Aucune case hors plateau ne doit être retournée");
    }

    @Test
    public void testComputeValidCaptureCells_sansEnnemi_ListeVide() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        assertTrue(captures.isEmpty(), "Sans ennemi, aucune capture ne doit être possible");
    }

    @Test
    public void testComputeValidCaptureCells_avecEnnemiCapturableUniquement() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        assertEquals(1, captures.size());
        assertEquals(2, captures.get(0)[0]);
        assertEquals(4, captures.get(0)[1]);
    }

    @Test
    public void testComputeValidCaptureCells_neRetournePasDeplacementsSimples() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        for (int[] cell : captures) {
            int dr = Math.abs(cell[0] - 2);
            int dc = Math.abs(cell[1] - 2);
            assertTrue(dr == 2 || dc == 2,
                    "computeValidCaptureCells ne doit retourner que des cases de capture (distance 2)");
        }
    }

    @Test
    public void testComputeValidCaptureCells_capturesDiagonales_SeulementSurCasesConnectees() {
        AlquerquePawn pawn = createAndPlace(0, 1, 1);
        createAndPlace(1, 2, 2);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        boolean diagonalCaptureFound = captures.stream()
                .anyMatch(c -> c[0] == 3 && c[1] == 3);
        assertTrue(diagonalCaptureFound, "Capture diagonale doit être possible depuis (1,1) case connectée");
    }
}
