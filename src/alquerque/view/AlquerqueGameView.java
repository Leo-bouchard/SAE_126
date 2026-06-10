package src.alquerque.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import src.alquerque.control.AlquerqueGameController;

public class AlquerqueGameView {


    private static final String BTN_STYLE =
            "-fx-font-size: 20px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #773eb8; -fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";
    private static final String BTN_HOVER =
            "-fx-font-size: 20px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: #6a1db3; -fx-text-fill: white; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; " +
                    "-fx-padding: 12 0 12 0;";

    private static final String TEXTFIELD_STYLE =
            "-fx-font-size: 16px; " +
            "-fx-background-color: #ddbbfb; " +
            "-fx-text-fill: #2b2b2b; " +
            "-fx-prompt-text-fill: gray; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #552688; " +
            "-fx-border-radius: 8; " +
            "-fx-border-width: 2; " +
            "-fx-padding: 8;";

    private static final String SECTION_STYLE =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; -fx-text-fill: #552688;";

    private static final String COMBOX_STYLE =
            "-fx-font-size: 16px; " +
                    "-fx-background-color: #ddbbfb; " +
                    "-fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-color: #552688; " +
                    "-fx-border-radius: 8; " +
                    "-fx-border-width: 2; " +
                    "-fx-padding: 4;";

    private VBox configBox;   // zone qui change selon le mode

    public VBox getPanel() {

        AlquerqueGameController controller = new AlquerqueGameController(this);

        Button PvP = new Button("PvsP");
        Button PvB = new Button("PvsB");
        Button BvB = new Button("BvsB");



        HBox modeHbox = new HBox(10);
        modeHbox.setAlignment(Pos.CENTER);
        modeHbox.getChildren().addAll(PvP, PvB, BvB);

        PvP.setOnAction(e -> controller.showPvP());
        PvB.setOnAction(e -> controller.showPvB());
        BvB.setOnAction(e -> controller.showBvB());

        configBox = new VBox(15);
        configBox.setAlignment(Pos.CENTER);

        controller.showPvP();

        Button StartGame = new Button("Start");

        for (Button b : new Button[]{PvP, PvB, BvB, StartGame}) {
            b.setPrefWidth(125);
            b.setPrefHeight(25);
            b.setStyle(BTN_STYLE);
            b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
            b.setOnMouseExited(e -> b.setStyle(BTN_STYLE));
        }


        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(modeHbox, configBox, StartGame);

        return box;
    }

    public void setConfig(VBox content) {
        configBox.getChildren().setAll(content);
    }

    public VBox buildPvP() {
        VBox v = new VBox(8);
        v.setAlignment(Pos.CENTER);

        Label l1 = new Label("Nom joueur 1 :");
        l1.setStyle(SECTION_STYLE);
        TextField nom1 = new TextField(); nom1.setMaxWidth(200);
        nom1.setStyle(TEXTFIELD_STYLE);

        Label l2 = new Label("Nom joueur 2 :");
        l2.setStyle(SECTION_STYLE);
        TextField nom2 = new TextField(); nom2.setMaxWidth(200);
        nom2.setStyle(TEXTFIELD_STYLE);
        v.getChildren().addAll(l1, nom1, l2, nom2);
        return v;
    }

    public VBox buildPvB() {
        VBox v = new VBox(8);
        v.setAlignment(Pos.CENTER);

        Label l1 = new Label("Nom joueur :");
        l1.setStyle(SECTION_STYLE);
        TextField nom = new TextField(); nom.setMaxWidth(200);
        nom.setStyle(TEXTFIELD_STYLE);

        Label l2 = new Label("Choix du bot :");
        l2.setStyle(SECTION_STYLE);
        ComboBox<String> bot = new ComboBox<>();
        bot.getItems().addAll("Fred", "Jesus", "Master Mind");
        bot.setValue("Fred");
        bot.setStyle(COMBOX_STYLE);

        v.getChildren().addAll(l1, nom, l2, bot);
        return v;
    }

    public VBox buildBvB() {
        VBox v = new VBox(8);
        v.setAlignment(Pos.CENTER);

        Label l1 = new Label("Bot 1 :");
        l1.setStyle(SECTION_STYLE);
        ComboBox<String> bot1 = new ComboBox<>();
        bot1.getItems().addAll("Fred", "Jesus", "Master Mind");
        bot1.setValue("Fred");
        bot1.setStyle(COMBOX_STYLE);

        Label l2 = new Label("Bot 2 :");
        l2.setStyle(SECTION_STYLE);

        ComboBox<String> bot2 = new ComboBox<>();
        bot2.getItems().addAll("Fred", "Jesus", "Master Mind");
        bot2.setValue("Jesus");
        bot2.setStyle(COMBOX_STYLE);
        v.getChildren().addAll(l1, bot1, l2, bot2);
        return v;
    }
}