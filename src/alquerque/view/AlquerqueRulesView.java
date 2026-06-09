package src.alquerque.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.alquerque.control.AlquerqueBackToHome;


public class AlquerqueRulesView {

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

    // style commun pour les titres de section
    private static final String SECTION_STYLE =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; -fx-text-fill: #552688;";
    private static final String BODY_STYLE =
            "-fx-font-size: 16px; -fx-font-family: 'Arial'; -fx-text-fill: #2b2b2b;";

    public AlquerqueRulesView(Stage stage) {
        this.stage = stage;
        initWidget();
    }

    // construit (ou reconstruit) la page des regles
    public void initWidget() {

        Label title = new Label("RULES");
        title.setStyle("-fx-font-size: 70px; -fx-font-weight: bold; -fx-font-family: 'Impact'; " +
                "-fx-text-fill: #19062b;");
        title.setPadding(new Insets(20, 0, 10, 0));

        Label setupTitle = new Label("Setup");
        setupTitle.setStyle(SECTION_STYLE);
        Label setupBody = new Label(
                "Players draw lots to decide who plays White and who plays Black.\n" +
                        "White moves first."
        );
        setupBody.setStyle(BODY_STYLE);

        Label playTitle = new Label("How to play");
        playTitle.setStyle(SECTION_STYLE);
        Label playBody = new Label(
                "On their turn, a player moves one of their pieces:\n\n" +
                        "  -  to an empty point linked by a single segment to the piece, or\n\n" +
                        "  -  by jumping over an adjacent enemy piece, if the point right behind\n" +
                        "      it is free. The jump must stay in a straight line - turning during\n" +
                        "      a jump is not allowed. The jumped piece is captured and removed."
        );
        playBody.setStyle(BODY_STYLE);

        Label captureTitle = new Label("Multiple captures");
        captureTitle.setStyle(SECTION_STYLE);
        Label captureBody = new Label(
                "A player may chain several jumps in one turn, but is not forced to\n" +
                        "take every possible capture (unlike in checkers)."
        );
        captureBody.setStyle(BODY_STYLE);

        Label endTitle = new Label("End of the game");
        endTitle.setStyle(SECTION_STYLE);
        Label endBody = new Label(
                "A player loses when they have no pieces left, or when they can no\n" +
                        "longer move any piece. The other player wins."
        );
        endBody.setStyle(BODY_STYLE);

        // bloc de texte centre, sections espacees
        VBox rules = new VBox(6,
                setupTitle, setupBody,
                playTitle, playBody,
                captureTitle, captureBody,
                endTitle, endBody
        );
        rules.setAlignment(Pos.CENTER_LEFT);
        rules.setMaxWidth(720);
        rules.setPadding(new Insets(10, 0, 20, 0));

        Button back = new Button("Back");
        back.setPrefWidth(220);
        back.setStyle(BTN_STYLE);
        back.setOnMouseEntered(e -> back.setStyle(BTN_HOVER));
        back.setOnMouseExited(e -> back.setStyle(BTN_STYLE));
        back.setOnAction(new AlquerqueBackToHome(stage));

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(title, rules, back);
        root.setStyle("-fx-background-color: cornsilk; -fx-font-family: 'Impact';");

        scene = new Scene(root, 900, 700);
    }

    // affiche la page des regles dans la fenetre
    public void display() {
        stage.setTitle("Alquerque - Rules");
        stage.setScene(scene);
        stage.show();
    }
}