package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageFactory;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.model.Model;
import src.boardifier.model.TextElement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlquerqueStageModelTest {

    private Model model;
    private AlquerqueStageModel stage;
    private AlquerqueBoard board;

    @BeforeEach
    public void setup() {
        model = Mockito.mock(Model.class);
        stage = new AlquerqueStageModel("alquerque", model);
        board = new AlquerqueBoard(0, 0, stage);
        stage.setBoard(board);
    }

    @Test
    public void testCompteursInitiaux_12contre12() {
        assertEquals(12, stage.getBlackPawnsCount());
        assertEquals(12, stage.getWhitePawnsCount());
        assertEquals(24, stage.getTotalPawnsCount());
    }

    @Test
    public void testInitPawnCounts_modifieLesCompteurs() {
        stage.initPawnCounts(3, 5);
        assertEquals(3, stage.getBlackPawnsCount());
        assertEquals(5, stage.getWhitePawnsCount());
        assertEquals(8, stage.getTotalPawnsCount());
    }

    @Test
    public void testSetBoard_getBoardRetourneLePlateau() {
        assertSame(board, stage.getBoard());
    }

    @Test
    public void testSetPlayerName_getPlayerName() {
        TextElement text = new TextElement("Joueur 1", stage);
        stage.setPlayerName(text);
        assertSame(text, stage.getPlayerName());
    }

    @Test
    public void testSetBlackPawns_ajouteLesElementsAuStage() {
        AlquerquePawn[] pawns = { new AlquerquePawn(0, stage), new AlquerquePawn(0, stage) };
        stage.setBlackPawns(pawns);
        assertSame(pawns, stage.getBlackPawns());
        assertTrue(stage.isElementInStage(pawns[0]));
        assertTrue(stage.isElementInStage(pawns[1]));
    }

    @Test
    public void testSetWhitePawns_ajouteLesElementsAuStage() {
        AlquerquePawn[] pawns = { new AlquerquePawn(1, stage) };
        stage.setWhitePawns(pawns);
        assertSame(pawns, stage.getRedPawns());
        assertTrue(stage.isElementInStage(pawns[0]));
    }

    @Test
    public void testCallback_retraitPionCouleur0_decrementeBlancs() {
        AlquerquePawn pawn = new AlquerquePawn(0, stage);
        board.addElement(pawn, 2, 2);
        int before = stage.getWhitePawnsCount();
        board.removeElement(pawn);
        assertEquals(before - 1, stage.getWhitePawnsCount());
        assertEquals(12, stage.getBlackPawnsCount());
    }

    @Test
    public void testCallback_retraitPionCouleur1_decrementeNoirs() {
        AlquerquePawn pawn = new AlquerquePawn(1, stage);
        board.addElement(pawn, 2, 2);
        int before = stage.getBlackPawnsCount();
        board.removeElement(pawn);
        assertEquals(before - 1, stage.getBlackPawnsCount());
        assertEquals(12, stage.getWhitePawnsCount());
    }

    @Test
    public void testFinDePartie_dernierBlancRetire_vainqueurJoueur1() {
        stage.initPawnCounts(1, 1);
        AlquerquePawn white = new AlquerquePawn(0, stage);
        AlquerquePawn black = new AlquerquePawn(1, stage);
        board.addElement(white, 2, 2);
        board.addElement(black, 0, 0);
        board.removeElement(white);
        verify(model).setIdWinner(1);
        verify(model).stopStage();
    }

    @Test
    public void testFinDePartie_dernierNoirRetire_vainqueurJoueur0() {
        stage.initPawnCounts(1, 1);
        AlquerquePawn white = new AlquerquePawn(0, stage);
        AlquerquePawn black = new AlquerquePawn(1, stage);
        board.addElement(white, 2, 2);
        board.addElement(black, 0, 0);
        board.removeElement(black);
        verify(model).setIdWinner(0);
        verify(model).stopStage();
    }

    @Test
    public void testPasDeFinDePartie_tantQuilResteDesPions() {
        stage.initPawnCounts(2, 2);
        AlquerquePawn white = new AlquerquePawn(0, stage);
        board.addElement(white, 2, 2);
        board.removeElement(white);
        verify(model, never()).setIdWinner(anyInt());
        verify(model, never()).stopStage();
    }

    @Test
    public void testColorHasAnyMove_pionLibre_true() {
        AlquerquePawn pawn = new AlquerquePawn(0, stage);
        board.addElement(pawn, 2, 2);
        assertTrue(stage.colorHasAnyMove(0));
    }

    @Test
    public void testColorHasAnyMove_aucunPionDeCetteCouleur_false() {
        AlquerquePawn pawn = new AlquerquePawn(0, stage);
        board.addElement(pawn, 2, 2);
        assertFalse(stage.colorHasAnyMove(1));
    }

    @Test
    public void testColorHasAnyMove_plateauPleinSansEnnemi_false() {
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                board.addElement(new AlquerquePawn(1, stage), r, c);
        assertFalse(stage.colorHasAnyMove(1));
    }

    @Test
    public void testGetDefaultElementFactory_retourneAlquerqueStageFactory() {
        assertTrue(stage.getDefaultElementFactory() instanceof AlquerqueStageFactory);
    }
}
