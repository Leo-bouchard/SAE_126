package src.alquerque.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.alquerque.control.AlquerqueClose;
import src.alquerque.control.AlquerquePlay;
import src.alquerque.control.AlquerqueRules;


public class AlquerqueView {

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
                    "-fx-padding: 12 0 12 0;" +
                    "-fx-rotate: 2";

    public AlquerqueView(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        stage.setMinWidth(900);
        stage.setMinHeight(700);
        initWidget();
    }

    // construit (ou reconstruit) la page d'accueil
    public void initWidget() {

        Label title = new Label("ALQUERQUE");
        title.setStyle("-fx-font-size: 90px; -fx-font-weight: bold; -fx-font-family: 'Impact'; " +
                "-fx-text-fill: #19062b;");
        title.setPadding(new Insets(30, 0, 70, 0));

        Button play = new Button("Play");
        Button rules = new Button("Rules");
        Button exit = new Button("Exit");

        // un handler par bouton (fichiers de control separes)
        play.setOnAction(new AlquerquePlay(stage));
        rules.setOnAction(new AlquerqueRules(stage));
        exit.setOnAction(new AlquerqueClose(stage));

        for (Button b : new Button[]{play, rules, exit}) {
            b.setPrefWidth(220);
            b.setStyle(BTN_STYLE);
            b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
            b.setOnMouseExited(e -> b.setStyle(BTN_STYLE));
        }

        VBox vBoxButtonHome = new VBox(35);
        vBoxButtonHome.setAlignment(Pos.CENTER);
        vBoxButtonHome.getChildren().addAll(play, rules, exit);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(title, vBoxButtonHome);
        root.setStyle("-fx-background-color: cornsilk; -fx-font-family: 'Impact';");

        scene = new Scene(root, 900, 700);
    }

    // affiche la page d'accueil dans la fenetre
    public void display() {
        stage.setTitle("Alquerque - Menu");
        stage.setScene(scene);
        stage.show();
    }
}