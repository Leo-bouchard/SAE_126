package alquerque.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import alquerque.view.AlquerqueSettingView;

public class AlquerqueButtonGoSettingController implements EventHandler<ActionEvent> {

    private Stage stage;

    public AlquerqueButtonGoSettingController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        new AlquerqueSettingView(stage).display();
    }
}