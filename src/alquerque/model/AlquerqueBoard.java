package src.alquerque.model;

import src.boardifier.model.ContainerElement;
import src.boardifier.model.GameStageModel;

import java.util.ArrayList;
import java.util.List;


public class AlquerqueBoard extends ContainerElement {


    public AlquerqueBoard(int x, int y, GameStageModel gameStageModel){
        super("board",x,y,5,5,gameStageModel);
    }

    public void computeValidCells(AlquerquePawn pawn) {
        resetReachableCells(false);
        int[] pos = getElementCell(pawn);
        int x = pos[0];
        int y = pos[1];

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (i != 0 && j != 0 && (x + y) % 2 != 0) continue;

                int nx = x + i, ny = y + j;
                if (nx < 0 || nx >= 5 || ny < 0 || ny >= 5) continue;

                if (isEmptyAt(nx, ny)) {
                    reachableCells[nx][ny] = true;
                } else {
                    AlquerquePawn neighbor = (AlquerquePawn) getElement(nx, ny);
                    if (neighbor.getColor() != pawn.getColor()) {
                        int lx = x + i * 2, ly = y + j * 2;
                        if (lx >= 0 && lx < 5 && ly >= 0 && ly < 5 && isEmptyAt(lx, ly)) {
                            reachableCells[lx][ly] = true;
                        }
                    }
                }
            }
        }
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
