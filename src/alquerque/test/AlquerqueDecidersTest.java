package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import src.alquerque.control.AlquerqueDeciderBot1AleatoirenameFred;
import src.alquerque.control.AlquerqueDeciderBot2Jesus;
import src.alquerque.control.AlquerqueDeciderBot3MasterMind;
import src.alquerque.model.AlquerqueBoard;
import src.alquerque.model.AlquerquePawn;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.control.Controller;
import src.boardifier.model.Model;
import src.boardifier.model.action.ActionList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AlquerqueDecidersTest {

    private Model model;
    private Controller control;
    private AlquerqueStageModel stage;
    private AlquerqueBoard board;

    @BeforeEach
    public void setup() {
        model = Mockito.mock(Model.class);
        control = Mockito.mock(Controller.class);
        stage = Mockito.mock(AlquerqueStageModel.class);
        board = new AlquerqueBoard(0, 0, stage);
        when(stage.getBoard()).thenReturn(board);
        when(model.getGameStage()).thenReturn(stage);
        when(model.getIdPlayer()).thenReturn(0);
    }

    private int totalActions(ActionList list) {
        return list.getActions().stream().mapToInt(java.util.List::size).sum();
    }

    @Test
    public void testFred_aucunCoupPossible_listeVideAvecFinDeTour() {
        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, control);
        ActionList actions = bot.decide();
        assertEquals(0, totalActions(actions));
        assertTrue(actions.mustDoEndOfTurn());
    }

    @Test
    public void testFred_deplacementSimple_uneAction() {
        board.addElement(new AlquerquePawn(0, stage), 2, 2);
        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, control);
        ActionList actions = bot.decide();
        assertEquals(1, totalActions(actions));
        assertTrue(actions.mustDoEndOfTurn());
    }

    @Test
    public void testFred_capturePrioritaire_moveEtRemove() {
        board.addElement(new AlquerquePawn(0, stage), 2, 2);
        board.addElement(new AlquerquePawn(1, stage), 2, 3);
        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, control);
        ActionList actions = bot.decide();
        assertEquals(2, totalActions(actions));
        assertTrue(actions.mustDoEndOfTurn());
    }

    @Test
    public void testFred_captureEnChaine_4Actions() {
        board.addElement(new AlquerquePawn(0, stage), 2, 0);
        board.addElement(new AlquerquePawn(1, stage), 2, 1);
        board.addElement(new AlquerquePawn(1, stage), 2, 3);
        AlquerqueDeciderBot1AleatoirenameFred bot = new AlquerqueDeciderBot1AleatoirenameFred(model, control);
        ActionList actions = bot.decide();
        assertEquals(4, totalActions(actions));
    }

    @Test
    public void testJesus_aucunCoupPossible_listeVide() {
        AlquerqueDeciderBot2Jesus bot = new AlquerqueDeciderBot2Jesus(model, control);
        ActionList actions = bot.decide();
        assertEquals(0, totalActions(actions));
        assertTrue(actions.mustDoEndOfTurn());
    }

    @Test
    public void testJesus_capturePrioritaireSurDeplacement() {
        board.addElement(new AlquerquePawn(0, stage), 2, 2);
        board.addElement(new AlquerquePawn(0, stage), 0, 0);
        board.addElement(new AlquerquePawn(1, stage), 2, 3);
        AlquerqueDeciderBot2Jesus bot = new AlquerqueDeciderBot2Jesus(model, control);
        ActionList actions = bot.decide();
        assertEquals(2, totalActions(actions));
    }

    @Test
    public void testJesus_deplacementSimple_uneAction() {
        board.addElement(new AlquerquePawn(0, stage), 2, 2);
        AlquerqueDeciderBot2Jesus bot = new AlquerqueDeciderBot2Jesus(model, control);
        ActionList actions = bot.decide();
        assertEquals(1, totalActions(actions));
        assertTrue(actions.mustDoEndOfTurn());
    }

    @Test
    public void testMasterMind_aucunCoupPossible_listeVide() {
        AlquerqueDeciderBot3MasterMind bot = new AlquerqueDeciderBot3MasterMind(model, control);
        ActionList actions = bot.decide();
        assertEquals(0, totalActions(actions));
        assertTrue(actions.mustDoEndOfTurn());
    }

    @Test
    public void testMasterMind_choisitLaCaptureDisponible() {
        board.addElement(new AlquerquePawn(0, stage), 2, 2);
        board.addElement(new AlquerquePawn(1, stage), 2, 3);
        AlquerqueDeciderBot3MasterMind bot = new AlquerqueDeciderBot3MasterMind(model, control);
        ActionList actions = bot.decide();
        assertTrue(totalActions(actions) >= 2);
        assertTrue(actions.mustDoEndOfTurn());
    }

    @Test
    public void testMasterMind_deplacementSimpleSansEnnemi() {
        board.addElement(new AlquerquePawn(0, stage), 2, 2);
        AlquerqueDeciderBot3MasterMind bot = new AlquerqueDeciderBot3MasterMind(model, control);
        ActionList actions = bot.decide();
        assertEquals(1, totalActions(actions));
    }
}
