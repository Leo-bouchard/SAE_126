package test;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.model.ElementTypes;

import static org.junit.jupiter.api.Assertions.*;

public class AlquerquePawnTest {

    @Test
    public void testGetColor_pionBlanc() {
        AlquerqueStageModel stage = Mockito.mock(AlquerqueStageModel.class);
        AlquerquePawn pawn = new AlquerquePawn(1, stage);
        assertEquals(1, pawn.getColor());
    }

    @Test
    public void testGetColor_pionNoir() {
        AlquerqueStageModel stage = Mockito.mock(AlquerqueStageModel.class);
        AlquerquePawn pawn = new AlquerquePawn(0, stage);
        assertEquals(0, pawn.getColor());
    }

    @Test
    public void testType_estEnregistreCommePawn() {
        AlquerqueStageModel stage = Mockito.mock(AlquerqueStageModel.class);
        AlquerquePawn pawn = new AlquerquePawn(0, stage);
        assertEquals(ElementTypes.getType("Pawn"), pawn.getType());
    }
}
