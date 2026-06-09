package src.alquerque.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class AlquerquePlay implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerquePlay(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println("Play clicked");
    }
}