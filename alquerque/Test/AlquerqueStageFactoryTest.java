package alquerque.Test;

import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageFactory;
import alquerque.model.AlquerqueStageModel;
import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlquerqueStageFactoryTest {

    @Mock
    private Model model;

    private AlquerqueStageModel stageModel;
    private AlquerqueStageFactory factory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(model.getCurrentPlayerName()).thenReturn("Player1");
        stageModel = new AlquerqueStageModel("test", model);
        factory = new AlquerqueStageFactory(stageModel);
        factory.setup();
    }

    @Test
    public void testSetup_plateau5x5Cree() {
        AlquerqueBoard board = stageModel.getBoard();
        assertNotNull(board);
    }

    @Test
    public void testSetup_12PionsNoirs() {
        AlquerquePawn[] blacks = stageModel.getBlackPawns();
        assertNotNull(blacks);
        assertEquals(12, blacks.length);
    }

    @Test
    public void testSetup_12PionsBlancs() {
        // utilise getRedPawns() qui est le getter des blancs dans le projet actuel
        AlquerquePawn[] whites = stageModel.getRedPawns();
        assertNotNull(whites);
        assertEquals(12, whites.length);
    }

    @Test
    public void testSetup_pionNoir_couleur0() {
        for (AlquerquePawn p : stageModel.getBlackPawns()) {
            assertEquals(0, p.getColor());
        }
    }

    @Test
    public void testSetup_pionBlanc_couleur1() {
        for (AlquerquePawn p : stageModel.getRedPawns()) {
            assertEquals(1, p.getColor());
        }
    }

    @Test
    public void testSetup_ligne0_remplieDePionsNoirs() {
        AlquerqueBoard board = stageModel.getBoard();
        for (int col = 0; col < 5; col++) {
            AlquerquePawn p = (AlquerquePawn) board.getElement(0, col);
            assertNotNull(p);
            assertEquals(0, p.getColor());
        }
    }

    @Test
    public void testSetup_ligne1_remplieDePionsNoirs() {
        AlquerqueBoard board = stageModel.getBoard();
        for (int col = 0; col < 5; col++) {
            AlquerquePawn p = (AlquerquePawn) board.getElement(1, col);
            assertNotNull(p);
            assertEquals(0, p.getColor());
        }
    }

    @Test
    public void testSetup_ligne3_remplieDePionsBlancs() {
        AlquerqueBoard board = stageModel.getBoard();
        for (int col = 0; col < 5; col++) {
            AlquerquePawn p = (AlquerquePawn) board.getElement(3, col);
            assertNotNull(p);
            assertEquals(1, p.getColor());
        }
    }

    @Test
    public void testSetup_ligne4_remplieDePionsBlancs() {
        AlquerqueBoard board = stageModel.getBoard();
        for (int col = 0; col < 5; col++) {
            AlquerquePawn p = (AlquerquePawn) board.getElement(4, col);
            assertNotNull(p);
            assertEquals(1, p.getColor());
        }
    }

    @Test
    public void testSetup_ligne2_cols0et1_noirs_cols3et4_blancs() {
        AlquerqueBoard board = stageModel.getBoard();
        assertEquals(0, ((AlquerquePawn) board.getElement(2, 0)).getColor());
        assertEquals(0, ((AlquerquePawn) board.getElement(2, 1)).getColor());
        assertNull(board.getElement(2, 2));
        assertEquals(1, ((AlquerquePawn) board.getElement(2, 3)).getColor());
        assertEquals(1, ((AlquerquePawn) board.getElement(2, 4)).getColor());
    }

    @Test
    public void testSetup_caseCentrale_vide() {
        AlquerqueBoard board = stageModel.getBoard();
        assertNull(board.getElement(2, 2));
    }

    @Test
    public void testSetup_24PionsPlacesSurLePlateau() {
        AlquerqueBoard board = stageModel.getBoard();
        int count = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (board.getElement(row, col) != null) count++;
            }
        }
        assertEquals(24, count);
    }
}
