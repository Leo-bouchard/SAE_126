package alquerque.view;

import alquerque.model.*;
import alquerque.model.AlquerqueStageModel;
import alquerque.model.AlquerquePawn;
import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;

public class AlquerqueStageView extends GameStageView {

    public AlquerqueStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }
    @Override
    public void createLooks() {
        AlquerqueStageModel model = (AlquerqueStageModel) gameStageModel;
        addLook(new BoardLook(model.getBoard()));

        if (model.getPlayerName() != null) {
            addLook(new TextLook(18, "#552688", model.getPlayerName()));
        }

        AlquerquePawn[] blackPawns = model.getBlackPawns();
        AlquerquePawn[] whitePawns = model.getRedPawns();
        for (int i = 0; i < blackPawns.length; i++) {
            addLook(new PawnLook(blackPawns[i]));
        }
        for (int i = 0; i < whitePawns.length; i++) {
            addLook(new PawnLook(whitePawns[i]));
        }
    }
}