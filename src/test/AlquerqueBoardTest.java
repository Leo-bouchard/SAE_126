package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlquerqueBoardTest {

    private AlquerqueStageModel stageModel;
    private AlquerqueBoard board;

    @BeforeEach
    public void setup() {
        stageModel = Mockito.mock(AlquerqueStageModel.class);
        board = new AlquerqueBoard(0, 0, stageModel);
    }

    private AlquerquePawn createAndPlace(int color, int row, int col) {
        AlquerquePawn pawn = new AlquerquePawn(color, stageModel);
        board.addElement(pawn, row, col);
        return pawn;
    }

    private int countReachable() {
        int count = 0;
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                if (board.canReachCell(r, c)) count++;
        return count;
    }

    @Test
    public void testComputeValidCells_pionCentreCaseConnectee_8Directions() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        board.computeValidCells(pawn);
        assertEquals(8, countReachable());
    }

    @Test
    public void testComputeValidCells_caseNonConnectee_pasDeDiagonale() {
        AlquerquePawn pawn = createAndPlace(0, 0, 1);
        board.computeValidCells(pawn);
        assertFalse(board.canReachCell(1, 0));
        assertFalse(board.canReachCell(1, 2));
        assertTrue(board.canReachCell(0, 0));
        assertTrue(board.canReachCell(0, 2));
        assertTrue(board.canReachCell(1, 1));
        assertEquals(3, countReachable());
    }

    @Test
    public void testComputeValidCells_pionEnCoin_3Mouvements() {
        AlquerquePawn pawn = createAndPlace(0, 0, 0);
        board.computeValidCells(pawn);
        assertEquals(3, countReachable());
    }

    @Test
    public void testComputeValidCells_entoureDAllies_aucunMouvement() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(0, 1, 1);
        createAndPlace(0, 1, 2);
        createAndPlace(0, 1, 3);
        createAndPlace(0, 2, 1);
        createAndPlace(0, 2, 3);
        createAndPlace(0, 3, 1);
        createAndPlace(0, 3, 2);
        createAndPlace(0, 3, 3);
        board.computeValidCells(pawn);
        assertEquals(0, countReachable());
    }

    @Test
    public void testComputeValidCells_captureSimple_sautParDessusEnnemi() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        board.computeValidCells(pawn);
        assertTrue(board.canReachCell(2, 4));
    }

    @Test
    public void testComputeValidCells_captureImpossible_atterrissageOccupe() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        createAndPlace(0, 2, 4);
        board.computeValidCells(pawn);
        assertFalse(board.canReachCell(2, 4));
    }

    @Test
    public void testComputeValidCells_voisinAllie_caseNonAtteignable() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(0, 2, 3);
        board.computeValidCells(pawn);
        assertFalse(board.canReachCell(2, 3));
        assertFalse(board.canReachCell(2, 4));
    }

    @Test
    public void testComputeValidCaptureCells_sansEnnemi_listeVide() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        assertTrue(captures.isEmpty());
    }

    @Test
    public void testComputeValidCaptureCells_unEnnemiCapturable() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        assertEquals(1, captures.size());
        assertEquals(2, captures.get(0)[0]);
        assertEquals(4, captures.get(0)[1]);
    }

    @Test
    public void testComputeValidCaptureCells_captureHorsPlateau_ignoree() {
        AlquerquePawn pawn = createAndPlace(0, 0, 3);
        createAndPlace(1, 0, 4);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        assertTrue(captures.isEmpty());
    }

    @Test
    public void testComputeValidCaptureCells_captureDiagonaleSurCaseConnectee() {
        AlquerquePawn pawn = createAndPlace(0, 1, 1);
        createAndPlace(1, 2, 2);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        boolean found = captures.stream().anyMatch(c -> c[0] == 3 && c[1] == 3);
        assertTrue(found);
    }

    @Test
    public void testComputeValidCaptureCells_pasDeCaptureDiagonaleDepuisCaseNonConnectee() {
        AlquerquePawn pawn = createAndPlace(0, 0, 1);
        createAndPlace(1, 1, 2);
        List<int[]> captures = board.computeValidCaptureCells(pawn);
        boolean found = captures.stream().anyMatch(c -> c[0] == 2 && c[1] == 3);
        assertFalse(found);
    }

    @Test
    public void testComputeCaptureReachableCells_seulesCasesDeCapture() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        board.computeCaptureReachableCells(pawn);
        assertTrue(board.canReachCell(2, 4));
        assertEquals(1, countReachable());
    }

    @Test
    public void testClearHighlights_toutAZero() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        board.computeHighlights(pawn);
        board.clearHighlights();
        int[][] h = board.getHighlights();
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                assertEquals(0, h[r][c]);
    }

    @Test
    public void testComputeHighlights_selectionDeplacementEtCapture() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        board.computeHighlights(pawn);
        int[][] h = board.getHighlights();
        assertEquals(1, h[2][2]);
        assertEquals(2, h[1][2]);
        assertEquals(3, h[2][4]);
        assertEquals(0, h[2][3]);
    }

    @Test
    public void testComputeCaptureHighlights_seulementSelectionEtCaptures() {
        AlquerquePawn pawn = createAndPlace(0, 2, 2);
        createAndPlace(1, 2, 3);
        board.computeCaptureHighlights(pawn);
        int[][] h = board.getHighlights();
        assertEquals(1, h[2][2]);
        assertEquals(3, h[2][4]);
        assertEquals(0, h[1][2]);
    }
}
