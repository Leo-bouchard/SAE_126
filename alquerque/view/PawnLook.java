package alquerque.view;

import alquerque.model.AlquerquePawn;
import boardifier.model.GameElement;
import boardifier.view.ElementLook;

public class PawnLook extends ElementLook {

    public PawnLook(GameElement element){
        super(element,1,1);
    }


public void render() {
    AlquerquePawn pawn = (AlquerquePawn)element;
    if (pawn.getColor()==1){
        shape[0][0] = "●";
    } else if(pawn.getColor()==0) {
        shape[0][0] = "○";
    }
}
}

