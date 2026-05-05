package alquerque.model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Pawn extends GameElement {


    // white = 0    black = 1
    int color;


    public Pawn(int color, GameStageModel game) {
        super(game);
        // identification of the ElementType "pawn"
        ElementTypes.register("Pawn", 19);
        type = ElementTypes.getType("Pawn");
        this.color = color;
    }


    public int getColor() {
        return color;
    }
}
