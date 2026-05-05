package alquerque.model;

import boardifier.model.*;

public class AlquerqueStageModel extends GameStageModel {


    // define stage game elements
    private TextElement playerName;
    private Board board;
    private Pawn[] blackPawns;
    private Pawn[] redPawns;

    private int blackPawnsCount = 12;
    private int whitePawnsCount = 12;

    public AlquerqueStageModel(String name, Model model) {
        super(name, model);
        setupCallbacks();
    }


    // setteur

    public void setBlackPawns(Pawn[] pawns) {
        this.blackPawns = pawns;
        for (int i = 0; i < pawns.length; i++) {
            addElement(pawns[i]);
        }
    }


    public void setWhitePawns(Pawn[] pawns) {
        this.redPawns = pawns;
        for (int i = 0; i < pawns.length; i++) {
            addElement(pawns[i]);
        }
    }


    public void setBoard(Board board) {
        this.board = board;
        addContainer(board);
    }

    public void setPlayerName(TextElement t) {
        this.playerName = t;
        addElement(t);
    }

    // getteur

    public Board getBoard() { return board; }
    public Pawn[] getBlackPawns() { return blackPawns; }
    public Pawn[] getRedPawns() { return redPawns; }
    public TextElement getPlayerName() { return playerName; }




    private void setupCallbacks() {
        onRemoveFromContainer( (element, container, row, col) -> {
            if (container != board) return;     // verify if it's the board
            Pawn p = (Pawn) element;            // Retrieve the removed pawn

            if (p.getColor() == 0) {
                whitePawnsCount--;
            } else {
                blackPawnsCount--;
            }

            // end ?
            if (whitePawnsCount == 0 || blackPawnsCount == 0) {
                computePartyResult();
            }
        });
    }

    private void computePartyResult() {
        int nbWhitePawn = 0;
        int idWinner;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                GameElement element = board.getElement(i, j);   // Retrieve the pawn of what color if there is one.
                if (element != null) {
                    Pawn p = (Pawn) element;
                    if (p.getColor() == 0) {
                        nbWhitePawn++;
                    }
                }
            }
        }



        if (nbWhitePawn == 0) {
            idWinner = 1;
        } else {
            idWinner = 0;
        }

        model.setIdWinner(idWinner);
        model.stopStage();

    }


    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new AlquerqueStageFactory(this);
    }
}
