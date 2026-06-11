package src.alquerque.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.alquerque.control.AlquerqueBackToHomeController;

public class AlquerqueCreditView {

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

    private static final String SECTION_STYLE =
            "-fx-font-size: 20px; -fx-font-family: 'Impact'; -fx-text-fill: #552688;";
    private static final String NAME_STYLE =
            "-fx-font-size: 16px; -fx-font-family: 'Arial'; -fx-text-fill: #2b2b2b;";

    public AlquerqueCreditView(Stage stage) {
        this.stage = stage;
        initWidget();
    }

    public void initWidget() {

        Label title = new Label("CREDITS");
        title.setStyle("-fx-font-size: 70px; -fx-font-weight: bold; -fx-font-family: 'Impact'; " +
                "-fx-text-fill: #19062b;");
        title.setPadding(new Insets(20, 0, 30, 0));

        Label devTitle = new Label("Developers");
        devTitle.setStyle(SECTION_STYLE);
        Label dev1 = new Label("Emile CAP");         dev1.setStyle(NAME_STYLE);
        Label dev2 = new Label("Leo BOUCHARD");       dev2.setStyle(NAME_STYLE);
        Label dev3 = new Label("Mathis CHIVE");       dev3.setStyle(NAME_STYLE);

        Label uiTitle = new Label("Interface Design");
        uiTitle.setStyle(SECTION_STYLE);
        Label ui1 = new Label("Emile CAP");           ui1.setStyle(NAME_STYLE);
        Label ui2 = new Label("Leo BOUCHARD");        ui2.setStyle(NAME_STYLE);
        Label ui3 = new Label("Mathis CHIVE");        ui3.setStyle(NAME_STYLE);

        Label pawnTitle = new Label("Pawn Design");
        pawnTitle.setStyle(SECTION_STYLE);
        Label pawn1 = new Label("Valentin BEURET (with his permission)");  pawn1.setStyle(NAME_STYLE);
        Label pawn2 = new Label("Valentin BEURET with hair");              pawn2.setStyle(NAME_STYLE);

        VBox contenu = new VBox(8);
        contenu.setAlignment(Pos.CENTER);
        contenu.getChildren().addAll(
                devTitle, dev1, dev2, dev3,
                new Label(" "),
                uiTitle, ui1, ui2, ui3,
                new Label(" "),
                pawnTitle, pawn1, pawn2
        );

        Button back = new Button("Back");
        back.setPrefWidth(220);
        back.setStyle(BTN_STYLE);
        back.setOnMouseEntered(e -> back.setStyle(BTN_HOVER));
        back.setOnMouseExited(e -> back.setStyle(BTN_STYLE));
        back.setOnAction(new AlquerqueBackToHomeController(stage));

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(title, contenu, back);
        root.setStyle("-fx-background-color: cornsilk; -fx-font-family: 'Impact';");

        scene = new Scene(root, 1000, 700);
    }

    public void display() {
        stage.setTitle("Alquerque - Credits");
        stage.setScene(scene);
        stage.show();
    }
}