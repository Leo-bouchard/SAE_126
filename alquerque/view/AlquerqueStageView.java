package alquerque.view;

import alquerque.model.AlquerqueStageModel;
import boardifier.model.GameStageModel;
import boardifier.view.ClassicBoardLook;
import boardifier.view.GameStageView;

public class AlquerqueStageView extends GameStageView {

    public AlquerqueStageView(String name, GameStageModel gameStageModel){
        super(name,gameStageModel);
    }

    @Override
    public void createLooks(){
        AlquerqueStageModel model = (AlquerqueStageModel)gameStageModel;
        addLook(new BoardLook(model.getBoard()));
        for(int i=0;i<12;i++){
            addLook(new PawnLook(model.getBlackPawns()[i]));
            addLook(new PawnLook(model.getRedPawns()[i]));
        }
    }
}
