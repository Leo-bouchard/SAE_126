package alquerque.view;

import alquerque.model.AlquerqueBoard;
import boardifier.model.ContainerElement;
import boardifier.view.GridLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BoardLook extends GridLook {

    // grid geometry (must match the GridLook constructor below)
    private static final int CELL = 122;
    private static final int OFFSET = 40;

    private final AlquerqueBoard board;
    private final ImageView[][] overlays = new ImageView[5][5];

    private final Image selectedImg;
    private final Image reachableImg;
    private final Image eatImg;

    public BoardLook(ContainerElement board) {
        super(122, 122, board, 0, 40, 40, 0, Color.TRANSPARENT);
        this.board = (AlquerqueBoard) board;

        Image image = new Image(getClass().getResourceAsStream("/alquerque/Image/BackgroundBoard.png"));
        ImageView bg = new ImageView(image);
        bg.setFitWidth(600);
        bg.setFitHeight(600);
        addNode(bg);

        selectedImg = loadImage("SelectedCells.png");
        reachableImg = loadImage("ReachableCells.png");
        eatImg = loadImage("EatPawnCells.png");

        // one overlay ImageView per cell, hidden by default, drawn above the board
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                ImageView iv = new ImageView();
                iv.setFitWidth(CELL);
                iv.setFitHeight(CELL);
                iv.setTranslateX(OFFSET + c * CELL-45);
                iv.setTranslateY(OFFSET + r * CELL-47);
                iv.setMouseTransparent(true);
                iv.setVisible(false);
                overlays[r][c] = iv;
                addNode(iv);
            }
        }
    }

    private Image loadImage(String name) {
        return new Image(getClass().getResourceAsStream("/alquerque/Image/" + name));
    }

    // refreshes the overlay layer from the board highlight state
    public void refreshHighlights() {
        int[][] h = board.getHighlights();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                switch (h[r][c]) {
                    case 1 -> { overlays[r][c].setImage(selectedImg); overlays[r][c].setVisible(true); }
                    case 2 -> { overlays[r][c].setImage(reachableImg); overlays[r][c].setVisible(true); }
                    case 3 -> { overlays[r][c].setImage(eatImg); overlays[r][c].setVisible(true); }
                    default -> overlays[r][c].setVisible(false);
                }
            }
        }
    }
}