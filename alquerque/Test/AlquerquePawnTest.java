package alquerque.Test;

import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AlquerquePawnTest {

    @Mock
    private AlquerqueStageModel stageModel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetColorBlack() {
        AlquerquePawn pawn = new AlquerquePawn(0, stageModel);
        assertEquals(0, pawn.getColor());
    }

    @Test
    public void testGetColorWhite() {
        AlquerquePawn pawn = new AlquerquePawn(1, stageModel);
        assertEquals(1, pawn.getColor());
    }

    @Test
    public void testTwoPawnsHaveDifferentColors() {
        AlquerquePawn black = new AlquerquePawn(0, stageModel);
        AlquerquePawn white = new AlquerquePawn(1, stageModel);
        assertNotEquals(black.getColor(), white.getColor());
    }

    @Test
    public void testPawnColorIsImmutable() {
        AlquerquePawn pawn = new AlquerquePawn(0, stageModel);
        int colorBefore = pawn.getColor();
        assertEquals(colorBefore, pawn.getColor());
    }
}
