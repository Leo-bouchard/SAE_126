package alquerque.model;

import boardifier.model.ContainerElement;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;


public class Board extends ContainerElement {


    public Board(int x, int y, GameStageModel gameStageModel){
        super("board",x,y,5,5,gameStageModel);
    }

    public computeValidCells(Pawn pawn){
        int[][] valid;
        int[] pos = getElementCell(pawn);
        int x = pos[0];
        int y = pos[1];
        for(int i=0;i<9;i++)
    }
}
