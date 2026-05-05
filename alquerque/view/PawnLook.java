package alquerque.view;

import alquerque.model.Pawn;
import boardifier.model.GameElement;
import boardifier.view.ElementLook;

import javax.swing.text.Element;

public class PawnLook extends ElementLook {

    public PawnLook(GameElement element){
        super(element,1,1);
    }

    protected void render() {
        Pawn pawn = (Pawn)element;
        if (pawn.getColor()==0){
            shape[0][0] = "●";
        } else if(pawn.getColor()==0) {
            shape[0][0] = "○";
        }
    }
}


