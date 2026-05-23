package alquerque.model;

import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;

import java.util.ArrayList;
import java.util.List;


public class AlquerqueBoard extends ContainerElement {


    public AlquerqueBoard(int x, int y, GameStageModel gameStageModel){
        super("board",x,y,5,5,gameStageModel);
    }

    public List<int[]> computeValidCells(AlquerquePawn pawn) {
        List<int[]> valid = new ArrayList<>();
        int[] pos = getElementCell(pawn);
        int x = pos[0];
        int y = pos[1];  // we look at the 8 box around the pawn
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < 5 && y + j >= 0 && y + j < 5 && !(i == 0 && j == 0) && ((x + y) % 2 == 0 || i == 0 || j == 0) && isEmptyAt(x + i, y + j)) {

                    //we add every empty cell around the pawn to a list
                    valid.add(new int[]{x + i, y + j});

                } else if (x + i >= 0 && x + i < 5 && y + j >= 0 && y + j < 5 && !isEmptyAt(x + i, y + j) && ((AlquerquePawn) getElement(x + i, y + j)).getColor() != pawn.getColor()) {
                        if (x + i * 2 >= 0 && x + i * 2 < 5 && y + j * 2 >= 0 && y + j * 2 < 5 && isEmptyAt(x + i * 2, y + j * 2)) {

                            //we add every empty cell behind an enemy to the list
                            valid.add(new int[]{x + i * 2, y + j * 2});
                        }
                    }
                }
            }
            return valid;
        }
        public List<int[]> computeValidCaptureCells(AlquerquePawn pawn) {
        List<int[]> validcapture = new ArrayList<>();
        int[] pos = getElementCell(pawn);
        int x = pos[0];
        int y = pos[1];
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < 5 && y + j >= 0 && y + j < 5 && ((x + y) % 2 == 0 || i == 0 || j == 0) && !isEmptyAt(x + i, y + j) && ((AlquerquePawn) getElement(x + i, y + j)).getColor() != pawn.getColor()) {
                    if (x + i * 2 >= 0 && x + i * 2 < 5 && y + j * 2 >= 0 && y + j * 2 < 5 && isEmptyAt(x + i * 2, y + j * 2)) {

                        //we add every empty cell behind an enemy to the list
                        validcapture.add(new int[]{x + i * 2, y + j * 2});
                    }
                }
            }
        }
        return validcapture;
    }
    }
