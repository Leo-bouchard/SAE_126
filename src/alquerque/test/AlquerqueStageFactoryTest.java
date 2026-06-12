package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import src.alquerque.control.AlquerqueController;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageFactory;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.model.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AlquerqueStageFactoryTest {

    private Model model;
    private AlquerqueStageModel stage;
    private AlquerqueStageFactory factory;
    private boolean savedDemoMode;

    @BeforeEach
    public void setup() {
        savedDemoMode = AlquerqueController.demoMode;
        model = Mockito.mock(Model.class);
        when(model.getCurrentPlayerName()).thenReturn("Joueur 1");
        stage = new AlquerqueStageModel("alquerque", model);
        factory = new AlquerqueStageFactory(stage);
    }

    @AfterEach
    public void tearDown() {
        AlquerqueController.demoMode = savedDemoMode;
    }

    @Test
    public void testSetup_creeLePlateauEtLeNomDuJoueur() {
        AlquerqueController.demoMode = false;
        factory.setup();
        assertNotNull(stage.getBoard());
        assertNotNull(stage.getPlayerName());
        assertEquals("Joueur 1", stage.getPlayerName().getText());
    }

    @Test
    public void testSetup_modeNormal_12PionsParCouleur() {
        AlquerqueController.demoMode = false;
        factory.setup();
        assertEquals(12, stage.getBlackPawnsCount());
        assertEquals(12, stage.getWhitePawnsCount());
        assertEquals(12, stage.getBlackPawns().length);
        assertEquals(12, stage.getRedPawns().length);
    }

    @Test
    public void testSetup_modeNormal_dispositionInitiale() {
        AlquerqueController.demoMode = false;
        factory.setup();
        for (int col = 0; col < 5; col++) {
            assertEquals(0, ((AlquerquePawn) stage.getBoard().getElement(0, col)).getColor());
            assertEquals(0, ((AlquerquePawn) stage.getBoard().getElement(1, col)).getColor());
            assertEquals(1, ((AlquerquePawn) stage.getBoard().getElement(3, col)).getColor());
            assertEquals(1, ((AlquerquePawn) stage.getBoard().getElement(4, col)).getColor());
        }
        assertEquals(0, ((AlquerquePawn) stage.getBoard().getElement(2, 0)).getColor());
        assertEquals(0, ((AlquerquePawn) stage.getBoard().getElement(2, 1)).getColor());
        assertEquals(1, ((AlquerquePawn) stage.getBoard().getElement(2, 3)).getColor());
        assertEquals(1, ((AlquerquePawn) stage.getBoard().getElement(2, 4)).getColor());
    }

    @Test
    public void testSetup_modeNormal_caseCentraleVide() {
        AlquerqueController.demoMode = false;
        factory.setup();
        assertTrue(stage.getBoard().isEmptyAt(2, 2));
    }

    @Test
    public void testSetup_modeDemo_2PionsParCouleur() {
        AlquerqueController.demoMode = true;
        factory.setup();
        assertEquals(2, stage.getBlackPawnsCount());
        assertEquals(2, stage.getWhitePawnsCount());
        assertEquals(2, stage.getBlackPawns().length);
        assertEquals(2, stage.getRedPawns().length);
    }

    @Test
    public void testSetup_modeDemo_positionsAttendues() {
        AlquerqueController.demoMode = true;
        factory.setup();
        assertFalse(stage.getBoard().isEmptyAt(2, 2));
        assertFalse(stage.getBoard().isEmptyAt(2, 3));
        assertFalse(stage.getBoard().isEmptyAt(3, 2));
        assertFalse(stage.getBoard().isEmptyAt(4, 3));
        assertEquals(0, ((AlquerquePawn) stage.getBoard().getElement(2, 2)).getColor());
        assertEquals(1, ((AlquerquePawn) stage.getBoard().getElement(3, 2)).getColor());
    }
}
