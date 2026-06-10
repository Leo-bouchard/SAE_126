package src.alquerque.view;

import src.boardifier.model.ContainerElement;
import src.boardifier.view.GridLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BoardLook extends GridLook {

    public BoardLook(ContainerElement board) {
        super(60, 60, board, 0, 1, Color.TRANSPARENT);

        Image image = new Image(getClass().getResourceAsStream("/src/alquerque/Image/BackgroundBoard.png"));
        ImageView bg = new ImageView(image);
        bg.setFitWidth(60 * 5);
        bg.setFitHeight(60 * 5);
        addNode(bg);
    }
}