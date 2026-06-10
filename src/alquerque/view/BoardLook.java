package src.alquerque.view;

import src.boardifier.model.ContainerElement;
import src.boardifier.view.GridLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BoardLook extends GridLook {

    public BoardLook(ContainerElement board) {
        super(122, 122, board, -1, 40, 40, 0, Color.TRANSPARENT);

        Image image = new Image(getClass().getResourceAsStream("/src/alquerque/Image/BackgroundBoard.png"));
        ImageView bg = new ImageView(image);
        bg.setFitWidth(600);
        bg.setFitHeight(600);
        addNode(bg);
    }
}