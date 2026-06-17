package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import alquerque.control.AlquerqueController;
import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerqueStageModel;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.TextElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlquerqueControllerTest {

    private Model model;
    private AlquerqueStageModel stage;
    private AlquerqueBoard board;
    private TextElement playerName;
    private AlquerqueController controller;

    @BeforeEach
    public void setup() throws Exception {
        model = Mockito.mock(Model.class);
        stage = Mockito.mock(AlquerqueStageModel.class);
        board = Mockito.mock(AlquerqueBoard.class);
        playerName = Mockito.mock(TextElement.class);

        when(model.getGameStage()).thenReturn(stage);
        when(stage.getBoard()).thenReturn(board);
        when(stage.getPlayerName()).thenReturn(playerName);
        when(stage.getWhitePawnsCount()).thenReturn(12);
        when(stage.getBlackPawnsCount()).thenReturn(12);
        when(stage.colorHasAnyMove(anyInt())).thenReturn(true);
        when(model.getCurrentPlayer()).thenReturn(Player.createHumanPlayer("J1"));
        when(model.getCurrentPlayerName()).thenReturn("Player2");
        List<Player> players = new ArrayList<>();
        players.add(Player.createHumanPlayer("J1"));
        players.add(Player.createHumanPlayer("J2"));
        when(model.getPlayers()).thenReturn(players);

        controller = Mockito.mock(AlquerqueController.class);
        injectField(controller, "model", model, "boardifier.control.Controller");
        injectField(controller, "mapElementLook", new HashMap<>(), "boardifier.control.Controller");

        doCallRealMethod().when(controller).endOfTurn();
        doCallRealMethod().when(controller).isMultiCaptureInProgress();
        doCallRealMethod().when(controller).getMultiCapturePawn();
        doNothing().when(controller).refreshHighlights();
        doNothing().when(controller).endGame();
    }

    private void injectField(Object target, String fieldName, Object value, String declaringClass) throws Exception {
        Field f = Class.forName(declaringClass).getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    public void testEndOfTurn_passeAuJoueurSuivantEtMetAJourLeNom() {
        controller.endOfTurn();
        verify(model).setNextPlayer();
        verify(playerName).setText("Player2");
    }

    @Test
    public void testEndOfTurn_nettoieLesHighlights() {
        controller.endOfTurn();
        verify(board, atLeastOnce()).clearHighlights();
    }

    @Test
    public void testEndOfTurn_partieEnCours_pasDeFinDeJeu() {
        controller.endOfTurn();
        verify(model, never()).setIdWinner(anyInt());
        verify(controller, never()).endGame();
    }

    @Test
    public void testEndOfTurn_plusDeBlancs_vainqueurJoueur1() {
        when(stage.getWhitePawnsCount()).thenReturn(0);
        controller.endOfTurn();
        verify(model).setIdWinner(1);
        verify(controller).endGame();
    }

    @Test
    public void testEndOfTurn_plusDeNoirs_vainqueurJoueur0() {
        when(stage.getBlackPawnsCount()).thenReturn(0);
        controller.endOfTurn();
        verify(model).setIdWinner(0);
        verify(controller).endGame();
    }

    @Test
    public void testEndOfTurn_unPionChacun_matchNul() {
        when(stage.getWhitePawnsCount()).thenReturn(1);
        when(stage.getBlackPawnsCount()).thenReturn(1);
        controller.endOfTurn();
        verify(model).setIdWinner(-1);
        verify(controller).endGame();
    }

    @Test
    public void testEndOfTurn_aucunCampNePeutJouer_matchNul() {
        when(stage.colorHasAnyMove(anyInt())).thenReturn(false);
        controller.endOfTurn();
        verify(model).setIdWinner(-1);
        verify(controller).endGame();
    }

    @Test
    public void testEndOfTurn_unSeulCampBloque_pasDeFin() {
        when(stage.colorHasAnyMove(0)).thenReturn(true);
        when(stage.colorHasAnyMove(1)).thenReturn(false);
        controller.endOfTurn();
        verify(model, never()).setIdWinner(anyInt());
    }

    @Test
    public void testIsMultiCaptureInProgress_fauxParDefaut() {
        assertFalse(controller.isMultiCaptureInProgress());
        assertNull(controller.getMultiCapturePawn());
    }

    @Test
    public void testEndOfTurn_remetLaMultiCaptureAZero() {
        controller.endOfTurn();
        assertFalse(controller.isMultiCaptureInProgress());
    }

    @Test
    public void testReadWings_fichierAbsent_retourneZero() throws Exception {
        File file = new File("src/alquerque/savedData/wings");
        File backup = new File("src/alquerque/savedData/wings.bak");
        boolean existed = file.exists();
        if (existed) file.renameTo(backup);
        try {
            assertEquals(0, AlquerqueController.readWings());
        } finally {
            if (existed) backup.renameTo(file);
        }
    }

    @Test
    public void testReadWings_fichierPresent_retourneLeSolde() throws Exception {
        File dir = new File("src/alquerque/savedData");
        File file = new File(dir, "wings");
        File backup = new File(dir, "wings.bak");
        dir.mkdirs();
        boolean existed = file.exists();
        if (existed) file.renameTo(backup);
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
            w.write("42");
        }
        try {
            assertEquals(42, AlquerqueController.readWings());
        } finally {
            file.delete();
            if (existed) backup.renameTo(file);
        }
    }

    @Test
    public void testEndOfTurn_victoireHumainContreHumain_pasDeWingsModifiees() throws Exception {
        when(stage.getWhitePawnsCount()).thenReturn(0);
        List<Player> players = new ArrayList<>();
        players.add(Player.createHumanPlayer("J1"));
        players.add(Player.createHumanPlayer("J2"));
        when(model.getPlayers()).thenReturn(players);
        int saved0 = AlquerqueController.botForPlayer0;
        int saved1 = AlquerqueController.botForPlayer1;
        AlquerqueController.botForPlayer0 = 0;
        AlquerqueController.botForPlayer1 = 0;
        int before = AlquerqueController.readWings();
        try {
            controller.endOfTurn();
            assertEquals(before, AlquerqueController.readWings());
        } finally {
            AlquerqueController.botForPlayer0 = saved0;
            AlquerqueController.botForPlayer1 = saved1;
        }
    }
}
