package src.alquerque.view;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import src.alquerque.control.AlquerqueSkinController;

public class AlquerqueSkinView {

    private AlquerqueSkinController controller;

    private static final String BTN_STYLE =
            "-fx-font-size: 30px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #552688; -fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";
    private static final String BTN_HOVER =
            "-fx-font-size: 30px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #4a1d7a; -fx-text-fill: white; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";



    public AlquerqueSkinView() {
        this.controller = new AlquerqueSkinController();
    }

    public VBox getPanel() {

        Label title = new Label("Skin Pawn");
        title.setStyle("-fx-font-size: 28px; -fx-font-family: 'Impact';");

        ImageView pawnView = new ImageView();
        pawnView.setFitWidth(120);
        pawnView.setFitHeight(120);
        pawnView.setPreserveRatio(true);
        afficher(pawnView);

        Button leftPawn = new Button("<");
        Button rightPawn = new Button(">");

        for (Button b : new Button[]{leftPawn, rightPawn}) {
            b.setPrefWidth(50);
            b.setStyle(BTN_STYLE);

            RotateTransition smallRotationButton = new RotateTransition(Duration.millis(120), b);
            smallRotationButton.setFromAngle(-5);
            smallRotationButton.setToAngle(5);
            smallRotationButton.setCycleCount(RotateTransition.INDEFINITE);
            smallRotationButton.setAutoReverse(true);

            b.setOnMouseEntered(e -> {
                b.setStyle(BTN_HOVER);
                smallRotationButton.play();
            });
            b.setOnMouseExited(e -> {
                b.setStyle(BTN_STYLE);
                smallRotationButton.stop();
                b.setRotate(0);
            });
        }

        leftPawn.setOnAction(e -> {
            controller.precedent();
            afficher(pawnView);
        });
        rightPawn.setOnAction(e -> {
            controller.suivant();
            afficher(pawnView);
        });

        HBox hBoxPawnSkin = new HBox(20);
        hBoxPawnSkin.setAlignment(Pos.CENTER);
        hBoxPawnSkin.getChildren().addAll(leftPawn, pawnView, rightPawn);

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(title, hBoxPawnSkin);

        return box;
    }

    private void afficher(ImageView view) {
        String chemin = controller.getSkinActuel();
        if (chemin == null) {
            view.setImage(null);
            return;
        }
        java.net.URL url = getClass().getResource(chemin);
        if (url == null) {
            view.setImage(null);
            return;
        }
        view.setImage(new Image(url.toExternalForm()));
    }
}