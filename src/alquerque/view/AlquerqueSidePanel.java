package src.alquerque.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.alquerque.control.AlquerqueController;
import src.alquerque.model.AlquerqueStageModel;
import src.boardifier.model.Model;

public class AlquerqueSidePanel {

    private VBox root;
    private Label turnLabel;
    private Label blackTaken;
    private Label whiteTaken;

    private final Model model;
    private final AlquerqueStageModel stage;

    public AlquerqueSidePanel(Stage window, AlquerqueController control, Model model) {
        this.model = model;
        this.stage = (AlquerqueStageModel) model.getGameStage();

        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setPrefWidth(280);
        root.setStyle("-fx-background-color: #fdf6e3;");

        Label title = new Label("ALQUERQUE");
        title.setStyle("-fx-font-size: 35px; -fx-font-family: 'Impact'; -fx-text-fill: #552688;");

        turnLabel = new Label();
        turnLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Impact'; -fx-text-fill: #773eb8;");

        blackTaken = new Label();
        whiteTaken = new Label();
        for (Label l : new Label[]{blackTaken, whiteTaken}) {
            l.setStyle("-fx-font-size: 22px; -fx-text-fill: #2b2b2b;");
        }

        Button pass = new Button("Skip capture");
        Button back = new Button("Main menu");

        VBox buttonCol = new VBox(10);
        buttonCol.setAlignment(Pos.CENTER);
        buttonCol.getChildren().addAll(pass, back);

        for (Button b : new Button[]{pass, back}) {
            b.setPrefWidth(220);
            b.setStyle("-fx-font-size: 22px; -fx-font-family: 'Impact'; "
                    + "-fx-background-color: #773eb8; -fx-text-fill: cornsilk; "
                    + "-fx-background-radius: 10; -fx-cursor: hand;");
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        pass.setOnAction(e -> {
            control.endOfTurn();
            control.update();
            refresh();
        });

        back.setOnAction(e -> {
            new AlquerqueMainMenuView(window).display();
        });

        root.getChildren().addAll(title, turnLabel, blackTaken, whiteTaken, spacer, buttonCol);
        refresh();
    }

    public void refresh() {
        turnLabel.setText(model.getCurrentPlayerName() + " turn ");
        whiteTaken.setText("Black took " + (12 - stage.getWhitePawnsCount()));
        blackTaken.setText("White took " + (12 - stage.getBlackPawnsCount()));
    }

    public VBox getRoot() {
        return root;
    }
}