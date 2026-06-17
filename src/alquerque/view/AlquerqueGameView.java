package alquerque.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import alquerque.control.AlquerqueGameController;
import alquerque.control.AlquerqueStartController;

public class AlquerqueGameView {

    private Button PvP, PvB, BvB;

    private String mode = "PvP";

    private TextField nom1, nom2;
    private ComboBox<String> bot1, bot2;

    private static final String BTN_STYLE =
            "-fx-font-size: 20px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #773eb8; -fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";
    private static final String BTN_STYLE_ACTIVE =
            "-fx-font-size: 20px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #4d2281; -fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";
    private static final String BTN_HOVER =
            "-fx-font-size: 20px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #6a1db3; -fx-text-fill: white; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";
    private static final String TEXTFIELD_STYLE =
            "-fx-font-size: 16px; -fx-background-color: #ddbbfb; -fx-text-fill: #2b2b2b; " +
                    "-fx-prompt-text-fill: gray; -fx-background-radius: 8; -fx-border-color: #552688; " +
                    "-fx-border-radius: 8; -fx-border-width: 2; -fx-padding: 8;";
    private static final String SECTION_STYLE =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; -fx-text-fill: #552688;";
    private static final String COMBOX_STYLE =
            "-fx-font-size: 16px; -fx-background-color: #ddbbfb; -fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 8; -fx-border-color: #552688; -fx-border-radius: 8; " +
                    "-fx-border-width: 2; -fx-padding: 4;";

    private VBox configBox;

    public VBox getPanel() {

        AlquerqueGameController controller = new AlquerqueGameController(this);

        PvP = new Button("2 Players");
        PvB = new Button("1 Player");
        BvB = new Button("2 Bots");

        HBox modeHbox = new HBox(10);
        modeHbox.setAlignment(Pos.CENTER);
        modeHbox.getChildren().addAll(PvP, PvB, BvB);

        PvP.setOnAction(e -> controller.showPvP());
        PvB.setOnAction(e -> controller.showPvB());
        BvB.setOnAction(e -> controller.showBvB());

        configBox = new VBox(15);
        configBox.setAlignment(Pos.CENTER);

        controller.showPvP();

        Button startGame = new Button("Start");
        startGame.setPrefWidth(125);
        startGame.setPrefHeight(25);
        startGame.setStyle(BTN_STYLE);
        startGame.setOnMouseEntered(e -> startGame.setStyle(BTN_HOVER));
        startGame.setOnMouseExited(e -> startGame.setStyle(BTN_STYLE));
        startGame.setOnAction(new AlquerqueStartController(this));

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(modeHbox, configBox, startGame);

        return box;
    }

    public void setConfig(VBox content) {
        configBox.getChildren().setAll(content);
    }

    public String getMode() { return mode; }

    public String getNom1() { return (nom1 != null) ? nom1.getText() : ""; }
    public String getNom2() { return (nom2 != null) ? nom2.getText() : ""; }
    public String getBot1() { return (bot1 != null) ? bot1.getValue() : null; }
    public String getBot2() { return (bot2 != null) ? bot2.getValue() : null; }

    public VBox buildPvP() {
        mode = "PvP";
        VBox v = new VBox(8);
        v.setAlignment(Pos.CENTER);

        Label l1 = new Label("Nom joueur 1 :"); l1.setStyle(SECTION_STYLE);
        nom1 = new TextField(); nom1.setMaxWidth(200); nom1.setStyle(TEXTFIELD_STYLE);

        Label l2 = new Label("Nom joueur 2 :"); l2.setStyle(SECTION_STYLE);
        nom2 = new TextField(); nom2.setMaxWidth(200); nom2.setStyle(TEXTFIELD_STYLE);

        highlight(PvP);
        v.getChildren().addAll(l1, nom1, l2, nom2);
        return v;
    }

    public VBox buildPvB() {
        mode = "PvB";
        VBox v = new VBox(8);
        v.setAlignment(Pos.CENTER);

        Label l1 = new Label("Nom joueur :"); l1.setStyle(SECTION_STYLE);
        nom1 = new TextField(); nom1.setMaxWidth(200); nom1.setStyle(TEXTFIELD_STYLE);

        Label l2 = new Label("Choix du bot :"); l2.setStyle(SECTION_STYLE);
        bot1 = new ComboBox<>();
        bot1.getItems().addAll("Fred", "Jesus", "Master Mind");
        bot1.setValue("Fred");
        bot1.setStyle(COMBOX_STYLE);

        highlight(PvB);
        v.getChildren().addAll(l1, nom1, l2, bot1);
        return v;
    }

    public VBox buildBvB() {
        mode = "BvB";
        VBox v = new VBox(8);
        v.setAlignment(Pos.CENTER);

        Label l1 = new Label("Bot 1 :"); l1.setStyle(SECTION_STYLE);
        bot1 = new ComboBox<>();
        bot1.getItems().addAll("Fred", "Jesus", "Master Mind");
        bot1.setValue("Fred");
        bot1.setStyle(COMBOX_STYLE);

        Label l2 = new Label("Bot 2 :"); l2.setStyle(SECTION_STYLE);
        bot2 = new ComboBox<>();
        bot2.getItems().addAll("Fred", "Jesus", "Master Mind");
        bot2.setValue("Jesus");
        bot2.setStyle(COMBOX_STYLE);

        highlight(BvB);
        v.getChildren().addAll(l1, bot1, l2, bot2);
        return v;
    }

    private void highlight(Button actif) {
        for (Button b : new Button[]{PvP, PvB, BvB}) {
            b.setPrefWidth(125);
            b.setPrefHeight(25);
            if (b == actif) {
                b.setStyle(BTN_STYLE_ACTIVE);
                b.setOnMouseExited(e -> b.setStyle(BTN_STYLE_ACTIVE));
            } else {
                b.setStyle(BTN_STYLE);
                b.setOnMouseExited(e -> b.setStyle(BTN_STYLE));
            }
            b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
        }
    }
}