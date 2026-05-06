package alquerque.view;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;

// the game board will be shown this way
//     A   B   C   D   E
//   ┌───────────────────────────┐
// 2 | ● ─── ● ─── ● ─── ● ─── ● |
//   | |  \  |  /  |  \  |  /  | |
// 2 | ● ─── ● ─── ● ─── ● ─── ● |
//   | |  /  |  \  |  /  |  \  | |
// 3 | ● ─── ● ─── □ ─── ○ ─── ○ |
//   | |  \  |  /  |  \  |  /  | |
// 5 | ○ ─── ○ ─── ○ ─── ○ ─── ○ |
//   | |  /  |  \  |  /  |  \  | |
// 5 | ○ ─── ○ ─── ○ ─── ○ ─── ○ |
//   └───────────────────────────┘


public class BoardLook extends GridLook {

    public BoardLook(ContainerElement containerElement) {
        super(2, 6, containerElement, 1, 1);
    }

    @Override
    public void renderBorders() {
        shape[0][0] = "┌";
        for(int i = 1; i < 28; i++) shape[0][i] = "─";
        shape[0][28] = "┐";
        shape[10][0] = "└";
        for(int i = 1; i < 28; i++) shape[10][i] = "─";
        shape[10][28] = "┘";
        for(int i = 1; i < 10; i++) {
            shape[i][0] =  "│ ";
            shape[i][27] = "│";
        }
        for(int row = 1; row <= 10; row = row+2) {
            for(int col = 0; col < 4; col++) {
                for(int k = 1; k <= 3; k++) {
                    shape[row][2 + col*6 + k] = "─";
                }
            }
        }
        for (int i = 2; i <= 8; i = i+2){
            shape[i][1] =  "│";
            shape[i][7] =  "│";
            shape[i][13] = "│";
            shape[i][19] = "│";
            shape[i][25] = "│";
        }

        shape[2][4] = "\\";
        shape[2][16] = "\\";
        shape[6][4] = "\\";
        shape[6][16] = "\\";
        shape[4][10] = "\\";
        shape[4][22] = "\\";
        shape[8][10] = "\\";
        shape[8][22] = "\\";
        shape[2][10] = "/";
        shape[2][22] = "/";
        shape[6][10] = "/";
        shape[6][22] = "/";
        shape[4][4] = "/";
        shape[4][16] = "/";
        shape[8][4] = "/";
        shape[8][16] = "/";

        shape[5][13] = "□";
    }

}