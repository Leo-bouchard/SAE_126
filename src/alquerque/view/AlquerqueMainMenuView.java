package src.alquerque.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.alquerque.control.AlquerqueBackToHome;

public class AlquerqueMainMenuView {

    private Stage stage;
    private Scene scene;

    private static final String BTN_STYLE =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #552688; -fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";
    private static final String BTN_HOVER =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #4a1d7a; -fx-text-fill: white; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";


    public AlquerqueMainMenuView(Stage stage) {
        this.stage = stage;
        initWidget();
    }

    // construit (ou reconstruit) la page
    public void initWidget() {

        // ----- zone GAUCHE : les boutons -----
        VBox leftBox = new VBox(20);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPadding(new Insets(30));
        leftBox.setStyle("-fx-background-color: cornsilk;");

        Button back = new Button("Back");
        back.setPrefWidth(220);
        back.setStyle(BTN_STYLE);
        back.setOnMouseEntered(e -> back.setStyle(BTN_HOVER));
        back.setOnMouseExited(e -> back.setStyle(BTN_STYLE));
        back.setOnAction(new AlquerqueBackToHome(stage));

        leftBox.getChildren().addAll(back);

        // ----- zone DROITE : les infos -----
        VBox rightBox = new VBox(20);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPadding(new Insets(30));
        rightBox.setStyle("-fx-background-color: #f5e9c8;");
        Label info = new Label("Infos");
        info.setStyle("-fx-font-size: 24px; -fx-font-family: 'Impact';");
        rightBox.getChildren().add(info);

        // ----- le SplitPane qui remplit toute la fenetre -----
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftBox, rightBox);
        splitPane.setDividerPositions(0.3);
        splitPane.getDividers().get(0).positionProperty().addListener(
                (obs, oldVal, newVal) -> splitPane.setDividerPositions(0.3)
        );

        scene = new Scene(splitPane, 1000, 700);
    }

    // affiche la page dans la fenetre
    public void display() {
        stage.setTitle("Alquerque");
        stage.setScene(scene);
        stage.show();
    }
}