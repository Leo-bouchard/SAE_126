package src.alquerque.view;

import src.alquerque.model.AlquerquePawn;
import src.boardifier.model.GameElement;
import src.boardifier.view.ElementLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PawnLook extends ElementLook {

    private ImageView imageView;

    public PawnLook(GameElement element) {
        super(element);
        AlquerquePawn pawn = (AlquerquePawn) element;

        String file;
        if (pawn.getColor() == 1) {
            file = "/src/alquerque/Image/BlackPawn.png";
        } else {
            file = "/src/alquerque/Image/WhitePAwn.png";
        }

        Image image = new Image(getClass().getResourceAsStream(file));
        imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        addNode(imageView);
    }

    @Override
    public void render() {
    }
}