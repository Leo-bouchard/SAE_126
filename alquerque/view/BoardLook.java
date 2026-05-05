package alquerque.view;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;

// the game board will be shown this way
//     A   B   C   D   E
//   ┌───────────────────┐
// 1 | ●---●---●---●---● |
//   | | \ | / | \ | / | |
// 2 | ●---●---●---●---● |
//   | | / | \ | / | \ | |
// 3 | ●---●---□---○---○ |
//   | | \ | / | \ | / | |
// 4 | ○---○---○---○---○ |
//   | | / | \ | / | \ | |
// 5 | ○---○---○---○---○ |
//   └───────────────────┘


public class BoardLook extends GridLook {

    public BoardLook(ContainerElement containerElement) {
        super(2, 4, containerElement, 1, 1);
    }

    @Override
    protected void renderBorders() {
        shape[0][0] = "┌";
        for(int i = 1; i < 21; i++) shape[0][i] = "─";
        shape[0][21] = "┐";
        shape[10][0] = "└";
        for(int i = 1; i < 21; i++) shape[10][i] = "─";
        shape[10][21] = "┘";
        for(int i = 1; i < 10; i++) {
            shape[i][0] = "│";
            shape[i][21] = "│";
        }
        for(int row = 1; row <= 4; row = row+2) {
            for(int col = 0; col < 4; col++) {
                for(int k = 1; k <= 3; k++) {
                    shape[1][2 + col*4 + k] = "-";
                }
            }
        }
        for (int row = 1; row <= 4; row = row+2){

        }

    }
}