package src.alquerque.view;

import src.alquerque.model.AlquerquePawn;
import src.boardifier.model.GameElement;
import src.boardifier.view.ElementLook;

public class PawnLook extends ElementLook {

    public PawnLook(GameElement element) {
        super(element, 1, 1);
    }

    // temp comment
    @Override
    protected void render() {
        if (!element.isVisible()) {
            shape[0][0] = " ";
            return;
        }
        AlquerquePawn pawn = (AlquerquePawn) element;
        if (pawn.getColor() == 1) {
            shape[0][0] = "●";
        } else {
            shape[0][0] = "○";
        }
    }
}