package alquerque.model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class alquequeStageModel extends GameStageModel {


    // define stage game elements
    private TextElement playerName;
    private Board board;
    private Pawn[] blackPawns;
    private Pawn[] redPawns;

    private int blackPawnsCount = 12;
    private int redPawnsCount = 12;

    public alquequeStageModel(String name, Model model) {
        super(name, model);
        setupCallbacks();
    }

    public void setBlackPawns(Pawn[] pawns) {
        this.blackPawns = pawns;
        for (int i = 0; i < pawns.length; i++) {
            addElement(pawns[i]);
        }
    }


    public void setRedPawns(Pawn[] pawns) {
        this.redPawns = pawns;
        for (int i = 0; i < pawns.length; i++) {
            addElement(pawns[i]);
        }
    }





    private void setupCallbacks() {
        onRemoveFromContainer( (element, container, row, col) -> {

            // 1) On veut réagir uniquement si c'est le board
            if (container != board) return;

            // 2) Récupérer le pion retiré (on cast parce que element est un GameElement)
            Pawn p = (Pawn) element;

            // 3) Décrémenter le bon compteur selon la couleur
            if (p.getColor() == 0) {
                whitePawnsCount--;
            } else {
                blackPawnsCount--;
            }

            // 4) Vérifier la fin de partie
            if (whitePawnsCount == 0 || blackPawnsCount == 0) {
                computePartyResult();
            }
        });
    }



    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return null;
    }
}
