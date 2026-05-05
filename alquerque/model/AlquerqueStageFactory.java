package alquerque.model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class AlquerqueStageFactory extends StageElementsFactory {
    private AlquerqueStageModel alquerqueStageModel;

    public AlquerqueStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        alquerqueStageModel = (AlquerqueStageModel) gameStageModel;
    }

    @Override
    public void setup() {
        // setup pseudo
        TextElement text = new TextElement(alquerqueStageModel.getCurrentPlayerName(), alquerqueStageModel);
        text.setLocation(0, 0);
        alquerqueStageModel.setPlayerName(text);

        // setup board
        Board board = new Board(2, 2, alquerqueStageModel);
        alquerqueStageModel.setBoard(board);

        // setup white pawn
        Pawn[] whitePawns = new Pawn[12];
        for (int i = 0; i < 12; i++) {
            whitePawns[i] = new Pawn(0, alquerqueStageModel);   // 0 = blanc
        }
        alquerqueStageModel.setWhitePawns(whitePawns);

        // setup black pawn
        Pawn[] blackPawns = new Pawn[12];
        for (int i = 0; i < 12; i++) {
            blackPawns[i] = new Pawn(1, alquerqueStageModel);   // 1 = noir
        }
        alquerqueStageModel.setBlackPawns(blackPawns);

        // put black pawn
        int index = 0;

        for (int col = 0; col < 5; col++) {
            board.addElement(blackPawns[index], 0, col);
            index++;
        }

        for (int col = 0; col < 5; col++) {
            board.addElement(blackPawns[index], 1, col);
            index++;
        }

        for (int col = 0; col < 2; col++) {
            board.addElement(blackPawns[index], 2, col);
            index++;
        }

        // put white pawn

        index = 0;

        for (int col = 3; col < 5; col++) {
            board.addElement(whitePawns[index], 2, col);
            index++;
        }

        for (int col = 0; col < 5; col++) {
            board.addElement(whitePawns[index], 3, col);
            index++;
        }

        for (int col = 0; col < 5; col++) {
            board.addElement(whitePawns[index], 4, col);
            index++;
        }

    }
}
