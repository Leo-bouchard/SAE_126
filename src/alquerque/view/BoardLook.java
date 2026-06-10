package src.alquerque.view;

import src.boardifier.model.ContainerElement;
import src.boardifier.view.GridLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BoardLook extends GridLook {

    public BoardLook(ContainerElement board) {
        super(180, 180, board, 0, 1, Color.TRANSPARENT);

        Image image = new Image(getClass().getResourceAsStream("/src/alquerque/Image/BackgroundBoard.png"));
        ImageView bg = new ImageView(image);
        bg.setFitWidth(180 * 5);
        bg.setFitHeight(180 * 5);
        addNode(bg);
    }
}