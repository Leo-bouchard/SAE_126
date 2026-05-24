package alquerque.Test;

import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class AlquerqueStageModelTest {

    private Model model;
    private AlquerqueStageModel stageModel;

    @BeforeEach
    public void setup() {
        model = Mockito.mock(Model.class);
        Mockito.when(model.getCurrentPlayerName()).thenReturn("Player1");
        stageModel = new AlquerqueStageModel("test", model);
    }

    @Test
    public void testConstructeur_compteursPionsInitialises() {
        assertEquals(12, stageModel.getBlackPawnsCount());
        assertEquals(12, stageModel.getWhitePawnsCount());
    }

    @Test
    public void testSetBoard_boardNonNull() {
        AlquerqueBoard board = new AlquerqueBoard(0, 0, stageModel);
        stageModel.setBoard(board);
        assertNotNull(stageModel.getBoard());
        assertEquals(board, stageModel.getBoard());
    }

    @Test
    public void testSetBlackPawns_tableauStocke() {
        AlquerqueBoard board = new AlquerqueBoard(0, 0, stageModel);
        stageModel.setBoard(board);
        AlquerquePawn[] pawns = new AlquerquePawn[12];
        for (int i = 0; i < 12; i++) {
            pawns[i] = new AlquerquePawn(0, stageModel);
        }
        stageModel.setBlackPawns(pawns);
        assertNotNull(stageModel.getBlackPawns());
        assertEquals(12, stageModel.getBlackPawns().length);
    }

    @Test
    public void testSetWhitePawns_tableauStocke() {
        AlquerqueBoard board = new AlquerqueBoard(0, 0, stageModel);
        stageModel.setBoard(board);
        AlquerquePawn[] pawns = new AlquerquePawn[12];
        for (int i = 0; i < 12; i++) {
            pawns[i] = new AlquerquePawn(1, stageModel);
        }
        stageModel.setWhitePawns(pawns);
        assertNotNull(stageModel.getRedPawns());
        assertEquals(12, stageModel.getRedPawns().length);
    }
}
