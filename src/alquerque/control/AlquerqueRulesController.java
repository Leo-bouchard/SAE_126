package src.alquerque.control;

import src.alquerque.view.AlquerqueRulesView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class AlquerqueRulesController implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerqueRulesController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        new AlquerqueRulesView(stage).display();
    }
}