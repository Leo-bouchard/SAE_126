package src.alquerque.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AlquerqueSkinView {

    public VBox getPanel() {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("Skin");
        title.setStyle("-fx-font-size: 28px; -fx-font-family: 'Impact';");
        box.getChildren().add(title);

        return box;
    }
}