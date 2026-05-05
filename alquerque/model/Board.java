package alquerque.model;

import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;

import java.util.ArrayList;
import java.util.List;


public class Board extends ContainerElement {


    public Board(int x, int y, GameStageModel gameStageModel){
        super("board",x,y,5,5,gameStageModel);
    }

    public List computeValidCells(Pawn pawn){
        List<int[]> valid = new ArrayList<>();
        int[] pos = getElementCell(pawn);
        int x = pos[0];
        int y = pos[1];
        for(int i=-1;i<=1;i++){
            for(int j = -1 ; j <= 1; j++){
                if (x + i  >= 0 && x + i  < 5 && y + j  >= 0 && y + j  < 5 && !(i == 0 && j == 0) && ((x + y) % 2 == 0 || i == 0 || j == 0)){
                    valid.add(new int[]{x + i  , y + j});
                }
            }
        }
        return valid;
    }
}
