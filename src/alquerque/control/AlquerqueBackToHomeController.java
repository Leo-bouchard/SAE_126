package alquerque.control;

import alquerque.view.AlquerqueView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class AlquerqueBackToHomeController implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerqueBackToHomeController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        new AlquerqueView(stage).display();
    }
}