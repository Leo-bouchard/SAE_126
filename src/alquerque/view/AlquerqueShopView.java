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
import src.alquerque.control.AlquerqueShopController;

public class AlquerqueShopView {

    private AlquerqueShopController controller;

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


    public AlquerqueShopView() {
        this.controller = new AlquerqueShopController();
    }

    public VBox getPanel() {

        Label title = new Label("Shop");
        title.setStyle("-fx-font-size: 28px; -fx-font-family: 'Impact';");

        Label wingsLabel = new Label("Wings : " + controller.getWings());
        wingsLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Impact';");

        Label resultLabel = new Label("");
        resultLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Impact';");

        ImageView skinView = new ImageView();
        skinView.setFitWidth(120);
        skinView.setFitHeight(120);
        skinView.setPreserveRatio(true);

        Button btnShop = new Button("Buy 1 : 10 Wings");


        for (Button b : new Button[]{btnShop}) {
            b.setPrefWidth(270);
            b.setStyle(BTN_STYLE);

            RotateTransition smallRotationButton = new RotateTransition(Duration.millis(10), b);
            smallRotationButton.setFromAngle(-3);
            smallRotationButton.setToAngle(3);
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


        btnShop.setOnAction(e -> {
            AlquerqueShopController.ShopResult r = controller.buyRandomSkin();

            if (!r.success) {
                resultLabel.setText("NO Wings !");
                skinView.setImage(null);
            } else {
                if (r.isNew) {
                    resultLabel.setText("New skin : " + r.skin + " !");
                } else {
                    resultLabel.setText("Dubble : " + r.skin );
                }

                try {
                    Image img = new Image(new java.io.File(
                            "src/alquerque/Image/" + r.skin + ".png").toURI().toString());
                    skinView.setImage(img);
                } catch (Exception ex) {
                    skinView.setImage(null);
                }
            }

            wingsLabel.setText("Wings : " + r.remainingWings);
        });

        HBox hBoxPawnSkin = new HBox(20);
        hBoxPawnSkin.setAlignment(Pos.CENTER);
        hBoxPawnSkin.getChildren().addAll(btnShop);

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(title, wingsLabel, hBoxPawnSkin, skinView, resultLabel);

        return box;
    }


}