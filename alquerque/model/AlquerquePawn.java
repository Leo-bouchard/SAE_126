package alquerque.model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class AlquerquePawn extends GameElement {


    // white = 1    black = 0
    int color;

    static {
        ElementTypes.register("Pawn", 50);

    }
    public AlquerquePawn(int color, GameStageModel game) {
        super(game);
        // identification of the ElementType "pawn"
        type = ElementTypes.getType("Pawn");
        this.color = color;
    }


    public int getColor() {
        return color;
    }
}
