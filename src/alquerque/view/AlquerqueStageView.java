package src.alquerque.view;

import src.alquerque.model.AlquerqueStageModel;
import src.alquerque.model.AlquerquePawn;
import src.boardifier.model.GameStageModel;
import src.boardifier.view.GameStageView;
import src.boardifier.view.TextLook;

public class AlquerqueStageView extends GameStageView {

    public AlquerqueStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() {
        AlquerqueStageModel model = (AlquerqueStageModel) gameStageModel;
        addLook(new TextLook(model.getPlayerName()));
        addLook(new BoardLook(model.getBoard()));
        AlquerquePawn[] blackPawns = model.getBlackPawns();
        AlquerquePawn[] redPawns = model.getRedPawns();
        for (int i = 0; i < 12; i++) {
            addLook(new PawnLook(blackPawns[i]));
            addLook(new PawnLook(redPawns[i]));
        }
    }
}