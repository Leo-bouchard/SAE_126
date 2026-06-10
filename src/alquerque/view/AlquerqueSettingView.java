package src.alquerque.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.alquerque.control.AlquerqueBackToHomeController;
import src.alquerque.control.AlquerqueSettingController;


public class AlquerqueSettingView {

    private Stage stage;
    private Scene scene;
    private AlquerqueSettingController controller;

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

    private static final String SECTION_STYLE =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; -fx-text-fill: #552688;";

    public AlquerqueSettingView(Stage stage) {
        this.stage = stage;
        this.controller = new AlquerqueSettingController();
        initWidget();
    }

    public void initWidget() {

        Label title = new Label("SETTINGS");
        title.setStyle("-fx-font-size: 60px; -fx-font-weight: bold; -fx-font-family: 'Impact'; " +
                "-fx-text-fill: #19062b;");
        title.setPadding(new Insets(20, 0, 30, 0));

        Label musicTitle = new Label("Musique");
        musicTitle.setStyle(SECTION_STYLE);

        Button toggle = new Button("Couper la musique");
        toggle.setPrefWidth(260);
        toggle.setStyle(BTN_STYLE);
        toggle.setOnMouseEntered(e -> toggle.setStyle(BTN_HOVER));
        toggle.setOnMouseExited(e -> toggle.setStyle(BTN_STYLE));
        toggle.setOnAction(e -> {
            boolean active = controller.toggleMusique();
            toggle.setText(active ? "Couper la musique" : "Activer la musique");
        });

        Label volLabel = new Label("Volume");
        volLabel.setStyle("-fx-font-size: 22px; -fx-font-family: 'Impact'; -fx-text-fill: #552688;");
        Slider volume = new Slider(0, 100, controller.getVolume()*100);
        volume.setStyle("-fx-control-inner-background: #552688;");
        volume.setMaxWidth(260);
        volume.setShowTickLabels(true);
        volume.setShowTickMarks(true);
        volume.valueProperty().addListener((obs, oldV, newV) -> {
            controller.setVolume(newV.doubleValue());
            toggle.setText(controller.isMusiqueActive() ? "Couper la musique" : "Activer la musique");
        });

        VBox musicBox = new VBox(12, musicTitle, toggle, volLabel, volume);
        musicBox.setAlignment(Pos.CENTER);

        Button back = new Button("Back");
        back.setPrefWidth(220);
        back.setStyle(BTN_STYLE);
        back.setOnMouseEntered(e -> back.setStyle(BTN_HOVER));
        back.setOnMouseExited(e -> back.setStyle(BTN_STYLE));
        back.setOnAction(new AlquerqueBackToHomeController(stage));

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(title, musicBox, back);
        root.setStyle("-fx-background-color: cornsilk; -fx-font-family: 'Impact';");

        scene = new Scene(root, 1000, 700);
    }

    public void display() {
        stage.setTitle("Alquerque - Settings");
        stage.setScene(scene);
        stage.show();
    }

}