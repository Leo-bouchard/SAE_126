package alquerque.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * End-of-game screen styled with the Alquerque graphic charter
 * (violet #552688 / cornsilk, Impact font).
 *
 * Reminder of the rule: the colours (white / black) are drawn at random,
 * and White always plays first. This screen therefore shows which colour
 * the winner was playing.
 *
 * @param idWinner   0 = the player with the WHITE pawns,
 *                   1 = the player with the BLACK pawns,
 *                  -1 = draw
 */
public class AlquerqueEndGameView {

    // charter colours
    private static final String VIOLET       = "#552688";
    private static final String VIOLET_DARK  = "#4a1d7a";
    private static final String VIOLET_LIGHT = "#773eb8";
    private static final String CREAM        = "#fdf6e3";

    private static final String BTN_STYLE =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: " + VIOLET + "; -fx-text-fill: cornsilk; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 12 0 12 0;";
    private static final String BTN_HOVER =
            "-fx-font-size: 22px; -fx-font-family: 'Impact'; " +
                    "-fx-background-color: " + VIOLET_DARK + "; -fx-text-fill: white; " +
                    "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 12 0 12 0; " +
                    "-fx-rotate: 2;";

    private final Stage owner;
    private final int idWinner;
    private final String winnerName;
    private final int whitePawns;
    private final int blackPawns;

    private Runnable onReplay;
    private Runnable onMenu;

    public AlquerqueEndGameView(Stage owner, int idWinner, String winnerName,
                                int whitePawns, int blackPawns) {
        this.owner = owner;
        this.idWinner = idWinner;
        this.winnerName = winnerName;
        this.whitePawns = whitePawns;
        this.blackPawns = blackPawns;
    }

    /** action run when the player clicks "Rejouer" */
    public void setOnReplay(Runnable r) { this.onReplay = r; }

    /** action run when the player clicks "Menu" */
    public void setOnMenu(Runnable r) { this.onMenu = r; }

    public void display() {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.TRANSPARENT);
        popup.initModality(Modality.APPLICATION_MODAL);
        if (owner != null) popup.initOwner(owner);

        boolean draw = (idWinner == -1);

        // ----- card -----
        VBox card = new VBox(22);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 50, 40, 50));
        card.setStyle(
                "-fx-background-color: " + CREAM + "; " +
                        "-fx-background-radius: 24; " +
                        "-fx-border-color: " + VIOLET + "; " +
                        "-fx-border-width: 4; -fx-border-radius: 24;");
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(VIOLET_DARK, 0.55));
        shadow.setRadius(28);
        shadow.setOffsetY(8);
        card.setEffect(shadow);

        // ----- banner -----
        Label banner = new Label(draw ? "NULL" : "WIN");
        banner.setStyle("-fx-font-size: 46px; -fx-font-family: 'Impact'; " +
                "-fx-text-fill: " + VIOLET + ";");

        card.getChildren().add(banner);

        if (!draw) {
            // colour token (white pawn / black pawn) reminding the random draw
            boolean winnerIsWhite = (idWinner == 0);
            Circle token = new Circle(26);
            token.setFill(winnerIsWhite ? Color.web("#f5e9c8") : Color.web("#19062b"));
            token.setStroke(Color.web(VIOLET));
            token.setStrokeWidth(3);
            StackPane tokenPane = new StackPane(token);

            Label winner = new Label(winnerName);
            winner.setStyle("-fx-font-size: 30px; -fx-font-family: 'Impact'; " +
                    "-fx-text-fill: " + VIOLET_DARK + ";");

            Label colour = new Label("Win : "
                    + (winnerIsWhite ? "WHITE" : "BLACK"));
            colour.setStyle("-fx-font-size: 17px; -fx-text-fill: #2b2b2b;");

            card.getChildren().addAll(tokenPane, winner, colour);
        } else {
            Label sub = new Label("nobody win");
            sub.setStyle("-fx-font-size: 18px; -fx-text-fill: #2b2b2b;");
            card.getChildren().add(sub);
        }

        // ----- score -----
        Label score = new Label("White " + whitePawns + "   \u2022   " + blackPawns + " Black");
        score.setStyle("-fx-font-size: 18px; -fx-font-family: 'Impact'; " +
                "-fx-text-fill: " + VIOLET_LIGHT + ";");
        card.getChildren().add(score);

        // ----- buttons -----
        Button replay = new Button("Replay");
        Button menu = new Button("Menu");
        for (Button b : new Button[]{replay, menu}) {
            b.setPrefWidth(210);
            b.setStyle(BTN_STYLE);
            b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
            b.setOnMouseExited(e -> b.setStyle(BTN_STYLE));
        }

        replay.setOnAction(e -> {
            popup.close();
            if (onReplay != null) onReplay.run();
        });
        menu.setOnAction(e -> {
            popup.close();
            if (onMenu != null) onMenu.run();
        });

        VBox buttons = new VBox(12, replay, menu);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 0, 0));
        card.getChildren().add(buttons);

        // dim background behind the card
        StackPane backdrop = new StackPane(card);
        backdrop.setStyle("-fx-background-color: rgba(25,6,43,0.45);");
        backdrop.setPadding(new Insets(60));

        javafx.scene.Scene scene = new javafx.scene.Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        popup.setScene(scene);

        // entrance animation
        card.setScaleX(0.7);
        card.setScaleY(0.7);
        card.setOpacity(0);
        ScaleTransition pop = new ScaleTransition(Duration.millis(260), card);
        pop.setToX(1); pop.setToY(1);
        FadeTransition fade = new FadeTransition(Duration.millis(260), card);
        fade.setToValue(1);

        popup.setOnShown(e -> { pop.play(); fade.play(); });
        popup.show();
    }
}