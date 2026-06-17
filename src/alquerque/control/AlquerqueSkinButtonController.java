package alquerque.control;

import alquerque.view.AlquerqueSkinView;
import alquerque.view.AlquerqueMainMenuView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AlquerqueSkinButtonController implements EventHandler<ActionEvent> {

    private AlquerqueMainMenuView menu;

    public AlquerqueSkinButtonController(AlquerqueMainMenuView menu) {
        this.menu = menu;
    }

    @Override
    public void handle(ActionEvent event) {
        menu.setRightPanel(new AlquerqueSkinView().getPanel());
    }
}