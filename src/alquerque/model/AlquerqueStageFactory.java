package src.alquerque.model;

import src.alquerque.control.AlquerqueController;
import src.boardifier.model.GameStageModel;
import src.boardifier.model.StageElementsFactory;
import src.boardifier.model.TextElement;

public class AlquerqueStageFactory extends StageElementsFactory {
    private AlquerqueStageModel alquerqueStageModel;

    public AlquerqueStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        alquerqueStageModel = (AlquerqueStageModel) gameStageModel;
    }

    @Override
    public void setup() {
        // setup player name
        TextElement text = new TextElement(alquerqueStageModel.getCurrentPlayerName(), alquerqueStageModel);
        text.setLocation(0, 0);
        alquerqueStageModel.setPlayerName(text);

        // setup board
        AlquerqueBoard board = new AlquerqueBoard(2, 2, alquerqueStageModel);
        alquerqueStageModel.setBoard(board);

        alquerqueStageModel.initPawnCounts(
                AlquerqueController.demoMode ? 2 : 12,
                AlquerqueController.demoMode ? 2 : 12
        );

        if (AlquerqueController.demoMode) {
            // DEMO: 2 black pawns + 2 white pawns
            AlquerquePawn[] whitePawns = new AlquerquePawn[2];
            for (int i = 0; i < whitePawns.length; i++)
                whitePawns[i] = new AlquerquePawn(1, alquerqueStageModel);
            alquerqueStageModel.setWhitePawns(whitePawns);

            AlquerquePawn[] blackPawns = new AlquerquePawn[2];
            for (int i = 0; i < blackPawns.length; i++)
                blackPawns[i] = new AlquerquePawn(0, alquerqueStageModel);
            alquerqueStageModel.setBlackPawns(blackPawns);

            board.addElement(blackPawns[0], 2, 2);
            board.addElement(blackPawns[1], 2, 3);
            board.addElement(whitePawns[0], 3, 2);
            board.addElement(whitePawns[1], 4, 3);

        } else {
            // NORMAL: 12 vs 12
            AlquerquePawn[] whitePawns = new AlquerquePawn[12];
            for (int i = 0; i < 12; i++)
                whitePawns[i] = new AlquerquePawn(1, alquerqueStageModel);
            alquerqueStageModel.setWhitePawns(whitePawns);

            AlquerquePawn[] blackPawns = new AlquerquePawn[12];
            for (int i = 0; i < 12; i++)
                blackPawns[i] = new AlquerquePawn(0, alquerqueStageModel);
            alquerqueStageModel.setBlackPawns(blackPawns);

            int index = 0;
            for (int col = 0; col < 5; col++) board.addElement(blackPawns[index++], 0, col);
            for (int col = 0; col < 5; col++) board.addElement(blackPawns[index++], 1, col);
            for (int col = 0; col < 2; col++) board.addElement(blackPawns[index++], 2, col);

            index = 0;
            for (int col = 3; col < 5; col++) board.addElement(whitePawns[index++], 2, col);
            for (int col = 0; col < 5; col++) board.addElement(whitePawns[index++], 3, col);
            for (int col = 0; col < 5; col++) board.addElement(whitePawns[index++], 4, col);
        }
    }
}