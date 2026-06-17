package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import alquerque.control.AlquerqueDeciderBase;
import alquerque.control.AlquerqueDeciderBot1AleatoirenameFred;
import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.control.Controller;
import boardifier.model.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlquerqueDeciderBaseTest {

    private AlquerqueDeciderBase decider;
    private AlquerqueStageModel stageModel;

    @BeforeEach
    public void setup() {
        Model model = Mockito.mock(Model.class);
        Controller control = Mockito.mock(Controller.class);
        stageModel = Mockito.mock(AlquerqueStageModel.class);
        decider = new AlquerqueDeciderBot1AleatoirenameFred(model, control);
    }

    private int[][] emptyMatrix() {
        int[][] m = new int[5][5];
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                m[r][c] = -1;
        return m;
    }

    @Test
    public void testBoardToMatrix_correspondAuPlateau() {
        AlquerqueBoard board = new AlquerqueBoard(0, 0, stageModel);
        board.addElement(new AlquerquePawn(0, stageModel), 2, 2);
        board.addElement(new AlquerquePawn(1, stageModel), 0, 0);
        int[][] m = decider.boardToMatrix(board);
        assertEquals(0, m[2][2]);
        assertEquals(1, m[0][0]);
        assertEquals(-1, m[4][4]);
    }

    @Test
    public void testFindCapturesFromPosition_captureSimple() {
        int[][] m = emptyMatrix();
        m[2][2] = 0;
        m[2][3] = 1;
        List<int[]> captures = decider.findCapturesFromPosition(m, 2, 2, 0);
        assertEquals(1, captures.size());
        int[] cap = captures.get(0);
        assertArrayEquals(new int[]{2, 2, 2, 4, 2, 3}, cap);
    }

    @Test
    public void testFindCapturesFromPosition_atterrissageOccupe_aucune() {
        int[][] m = emptyMatrix();
        m[2][2] = 0;
        m[2][3] = 1;
        m[2][4] = 0;
        List<int[]> captures = decider.findCapturesFromPosition(m, 2, 2, 0);
        assertTrue(captures.isEmpty());
    }

    @Test
    public void testFindCapturesFromPosition_diagonaleInterditeDepuisCaseNonConnectee() {
        int[][] m = emptyMatrix();
        m[0][1] = 0;
        m[1][2] = 1;
        List<int[]> captures = decider.findCapturesFromPosition(m, 0, 1, 0);
        assertTrue(captures.isEmpty());
    }

    @Test
    public void testFindCapturesFromPosition_diagonaleAutoriseeDepuisCaseConnectee() {
        int[][] m = emptyMatrix();
        m[1][1] = 0;
        m[2][2] = 1;
        List<int[]> captures = decider.findCapturesFromPosition(m, 1, 1, 0);
        assertEquals(1, captures.size());
        assertEquals(3, captures.get(0)[2]);
        assertEquals(3, captures.get(0)[3]);
    }

    @Test
    public void testFindCapturesFromPosition_pasDeCaptureSurAllie() {
        int[][] m = emptyMatrix();
        m[2][2] = 0;
        m[2][3] = 0;
        List<int[]> captures = decider.findCapturesFromPosition(m, 2, 2, 0);
        assertTrue(captures.isEmpty());
    }

    @Test
    public void testFindAllCaptures_plusieursPions() {
        int[][] m = emptyMatrix();
        m[2][2] = 0;
        m[2][3] = 1;
        m[0][0] = 0;
        m[0][1] = 1;
        List<int[]> captures = decider.findAllCaptures(m, 0);
        assertEquals(2, captures.size());
    }

    @Test
    public void testFindAllSimpleMoves_pionCentral_8Mouvements() {
        int[][] m = emptyMatrix();
        m[2][2] = 0;
        List<int[]> moves = decider.findAllSimpleMoves(m, 0);
        assertEquals(8, moves.size());
    }

    @Test
    public void testFindAllSimpleMoves_caseNonConnectee_4Mouvements() {
        int[][] m = emptyMatrix();
        m[2][1] = 0;
        List<int[]> moves = decider.findAllSimpleMoves(m, 0);
        assertEquals(4, moves.size());
    }

    @Test
    public void testFindAllSimpleMoves_aucunPion_listeVide() {
        int[][] m = emptyMatrix();
        List<int[]> moves = decider.findAllSimpleMoves(m, 0);
        assertTrue(moves.isEmpty());
    }

    @Test
    public void testApplyMoveOnMatrix_deplacementSimple() {
        int[][] m = emptyMatrix();
        m[2][2] = 0;
        decider.applyMoveOnMatrix(m, new int[]{2, 2, 2, 3});
        assertEquals(-1, m[2][2]);
        assertEquals(0, m[2][3]);
    }

    @Test
    public void testApplyMoveOnMatrix_capture_supprimeLePionMange() {
        int[][] m = emptyMatrix();
        m[2][2] = 0;
        m[2][3] = 1;
        decider.applyMoveOnMatrix(m, new int[]{2, 2, 2, 4, 2, 3});
        assertEquals(-1, m[2][2]);
        assertEquals(-1, m[2][3]);
        assertEquals(0, m[2][4]);
    }
}
