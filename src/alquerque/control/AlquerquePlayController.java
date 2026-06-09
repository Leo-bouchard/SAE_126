package src.alquerque.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import src.alquerque.view.AlquerqueMainMenuView;

public class AlquerquePlayController implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerquePlayController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        new AlquerqueMainMenuView(stage).display();
    }
}