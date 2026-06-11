package src.alquerque.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import src.alquerque.view.AlquerqueCreditView;

public class AlquerqueCreditController implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerqueCreditController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        new AlquerqueCreditView(stage).display();
    }
}