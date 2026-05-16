package alquerque.view;

import alquerque.model.AlquerquePawn;
import boardifier.model.GameElement;
import boardifier.view.ElementLook;

public class PawnLook extends ElementLook {

    public PawnLook(GameElement element) {
        super(element, 1, 1);
    }

    // temp comment
    @Override
    protected void render() {
        AlquerquePawn pawn = (AlquerquePawn) element;
        if (pawn.getColor() == 1) {
            shape[0][0] = "●";
        } else {
            shape[0][0] = "○";
        }
    }
}