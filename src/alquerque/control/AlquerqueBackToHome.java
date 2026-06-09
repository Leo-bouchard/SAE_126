package src.alquerque.control;

import src.alquerque.view.AlquerqueView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class AlquerqueBackToHome implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerqueBackToHome(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        new AlquerqueView(stage).display();
    }
}