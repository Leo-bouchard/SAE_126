package src.alquerque.view;

import src.boardifier.model.ContainerElement;
import src.boardifier.view.GridLook;

// the game board will be shown this way
//     A     B     C     D     E
//   ┌───────────────────────────┐
// 1 | ● ─── ● ─── ● ─── ● ─── ● |
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
        super(2, 6, containerElement, 1, 1,2,1);
    }

    @Override
    public void renderBorders() {
        shape[1][2] = "┌";
        for(int i = 3; i < 30; i++) shape[1][i] = "─";
        shape[1][30] = "┐";
        shape[11][2] = "└";
        for(int i = 3; i < 30; i++) shape[11][i] = "─";
        shape[11][30] = "┘";
        for(int i = 2; i < 11; i++) {
            shape[i][2] =  "│ ";
            shape[i][29] = "│";
        }
        for(int row = 3; row <= 12; row = row+2) {
            for(int col = 0; col < 4; col++) {
                for(int k = 1; k <= 3; k++) {
                    shape[row-1][4 + col*6 + k] = "─";
                }
            }
        }
        for (int i = 2; i <= 8; i = i+2){
            shape[i+1][3] =  "│";
            shape[i+1][9] =  "│";
            shape[i+1][15] = "│";
            shape[i+1][21] = "│";
            shape[i+1][27] = "│";
        }

        shape[3][6] = "\\";
        shape[3][18] = "\\";
        shape[7][6] = "\\";
        shape[7][18] = "\\";
        shape[5][12] = "\\";
        shape[5][24] = "\\";
        shape[9][12] = "\\";
        shape[9][24] = "\\";
        shape[3][12] = "/";
        shape[3][24] = "/";
        shape[7][12] = "/";
        shape[7][24] = "/";
        shape[5][6] = "/";
        shape[5][18] = "/";
        shape[9][6] = "/";
        shape[9][18] = "/";

        // lettres A B C D E
        String[] letters = {"A", "B", "C", "D", "E"};
        for (int col = 0; col < 5; col++) {
            shape[0][4 + col * 6] = letters[col];
        }
        // chiffres 1 2 3 4 5
        for (int row = 0; row < 5; row++) {
            shape[2 + row * 2][0] = String.valueOf(row + 1);
        }
    }

}