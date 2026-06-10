package src.alquerque.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import src.alquerque.control.AlquerqueBackToHomeController;
import src.alquerque.control.AlquerqueGameButtonController;
import src.alquerque.control.AlquerqueSkinButtonController;

public class AlquerqueMainMenuView {

    private Stage stage;
    private Scene scene;

    // zone de droite : champ pour pouvoir changer son contenu
    private VBox rightBox;

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

        Button game = new Button("Game");
        Button skin = new Button("Skin");
        Button back = new Button("Back");

        for (Button b : new Button[]{game, skin, back}) {
            b.setPrefWidth(220);
            b.setStyle(BTN_STYLE);
            b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
            b.setOnMouseExited(e -> b.setStyle(BTN_STYLE));
        }

        // un controller par bouton
        game.setOnAction(new AlquerqueGameButtonController(this));
        skin.setOnAction(new AlquerqueSkinButtonController(this));
        back.setOnAction(new AlquerqueBackToHomeController(stage));

        leftBox.getChildren().addAll(game, skin, back);

        // ----- zone DROITE : contenu echangeable -----
        rightBox = new VBox(20);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPadding(new Insets(30));
        rightBox.setStyle("-fx-background-color: #f5e9c8;");
        rightBox.getChildren().add(new AlquerqueGameView().getPanel());  // Games par défaut

        // ----- le SplitPane qui remplit toute la fenetre -----
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftBox, rightBox);
        splitPane.setDividerPositions(0.3);
        splitPane.getDividers().get(0).positionProperty().addListener(
                (obs, oldVal, newVal) -> splitPane.setDividerPositions(0.3)
        );

        scene = new Scene(splitPane, 1000, 700);
    }

    public void setRightPanel(Node panel) {
        rightBox.getChildren().setAll(panel);
    }

    // affiche la page dans la fenetre
    public void display() {
        stage.setTitle("Alquerque");
        stage.setScene(scene);
        stage.show();
    }
}