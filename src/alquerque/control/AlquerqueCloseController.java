package src.alquerque.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class AlquerqueCloseController implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerqueCloseController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        stage.close();
    }
}