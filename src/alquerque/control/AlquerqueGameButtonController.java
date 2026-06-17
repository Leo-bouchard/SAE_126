package alquerque.control;

import alquerque.view.AlquerqueGameView;
import alquerque.view.AlquerqueMainMenuView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AlquerqueGameButtonController implements EventHandler<ActionEvent> {

    private AlquerqueMainMenuView menu;

    public AlquerqueGameButtonController(AlquerqueMainMenuView menu) {
        this.menu = menu;
    }

    @Override
    public void handle(ActionEvent event) {
        // demande au menu d'afficher le panneau Games a droite
        menu.setRightPanel(new AlquerqueGameView().getPanel());
    }
}