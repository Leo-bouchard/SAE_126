package Test;

import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.alquerque.control.AlquerqueDeciderBot1AleatoirenameFred;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlquerqueDeciderBot1Test {

    @Mock
    private Model model;

    private AlquerqueStageModel stageModel;
    private AlquerqueBoard board;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stageModel = new AlquerqueStageModel("test", model);
        board = new AlquerqueBoard(0, 0, stageModel);
        stageModel.setBoard(board);
    }


    private AlquerquePawn place(int color, int row, int col) {
        AlquerquePawn pawn = new AlquerquePawn(color, stageModel);
        board.addElement(pawn, row, col);
        return pawn;
    }

    @Test
    public void testBoardToMatrix_videRetourneMatriceMoinsUn() {
        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        int[][] matrix = bot.boardToMatrix(board);
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                assertEquals(-1, matrix[row][col]);
            }
        }
    }

    @Test
    public void testBoardToMatrix_refleteLesPions() {
        place(0, 0, 0);
        place(1, 4, 4);
        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        int[][] matrix = bot.boardToMatrix(board);
        assertEquals(0, matrix[0][0]);
        assertEquals(1, matrix[4][4]);
        assertEquals(-1, matrix[2][2]);
    }

    @Test
    public void testApplyMoveOnMatrix_deplacementSimple() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[2][2] = 0;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        bot.applyMoveOnMatrix(matrix, new int[]{2, 2, 2, 3});

        assertEquals(-1, matrix[2][2]);
        assertEquals(0, matrix[2][3]);
    }

    @Test
    public void testApplyMoveOnMatrix_captureSupprimeCase() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[2][2] = 0;
        matrix[2][3] = 1;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        bot.applyMoveOnMatrix(matrix, new int[]{2, 2, 2, 4, 2, 3});

        assertEquals(-1, matrix[2][2]);
        assertEquals(-1, matrix[2][3]);
        assertEquals(0, matrix[2][4]);
    }

    @Test
    public void testFindCapturesFromPosition_sansEnnemi_listeVide() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[2][2] = 0;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        List<int[]> captures = bot.findCapturesFromPosition(matrix, 2, 2, 0);

        assertTrue(captures.isEmpty());
    }

    @Test
    public void testFindCapturesFromPosition_avecEnnemi_retourneCapture() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[2][2] = 0;
        matrix[2][3] = 1;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        List<int[]> captures = bot.findCapturesFromPosition(matrix, 2, 2, 0);

        assertEquals(1, captures.size());
        assertEquals(2, captures.get(0)[2]);
        assertEquals(4, captures.get(0)[3]);
    }

    @Test
    public void testFindCapturesFromPosition_atterrissageOccupe_pasDeCap() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[2][2] = 0;
        matrix[2][3] = 1;
        matrix[2][4] = 0;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        List<int[]> captures = bot.findCapturesFromPosition(matrix, 2, 2, 0);

        assertTrue(captures.isEmpty());
    }

    @Test
    public void testFindAllSimpleMoves_pionSeul_retourneMouvements() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[2][2] = 0;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        List<int[]> moves = bot.findAllSimpleMoves(matrix, 0);

        assertFalse(moves.isEmpty());
    }

    @Test
    public void testFindAllSimpleMoves_plateauVide_listeVide() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        List<int[]> moves = bot.findAllSimpleMoves(matrix, 0);

        assertTrue(moves.isEmpty());
    }

    @Test
    public void testFindAllSimpleMoves_pionBloqueParAllie_listeVide() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[0][0] = 0;
        matrix[0][1] = 0;
        matrix[1][0] = 0;
        matrix[1][1] = 0;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        List<int[]> moves = bot.findAllSimpleMoves(matrix, 0);

        boolean moveFromOrigin = moves.stream().anyMatch(m -> m[0] == 0 && m[1] == 0);
        assertFalse(moveFromOrigin);
    }

    @Test
    public void testFindCapturesFromPosition_diagonaleInterditeCase1_1() {
        int[][] matrix = new int[5][5];
        for (int[] row : matrix) java.util.Arrays.fill(row, -1);
        matrix[0][1] = 0;
        matrix[1][2] = 1;

        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, null);
        List<int[]> captures = bot.findCapturesFromPosition(matrix, 0, 1, 0);

        boolean diagonalCapture = captures.stream()
                .anyMatch(c -> c[2] == 2 && c[3] == 3);
        assertFalse(diagonalCapture);
    }
}
