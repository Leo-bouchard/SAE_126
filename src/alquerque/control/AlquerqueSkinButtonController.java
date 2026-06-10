package src.alquerque.control;

import src.alquerque.view.AlquerqueSkinView;
import src.alquerque.view.AlquerqueMainMenuView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AlquerqueSkinButtonController implements EventHandler<ActionEvent> {

    private AlquerqueMainMenuView menu;

    public AlquerqueSkinButtonController(AlquerqueMainMenuView menu) {
        this.menu = menu;
    }

    @Override
    public void handle(ActionEvent event) {
        // demande au menu d'afficher le panneau Skin a droite
        menu.setRightPanel(new AlquerqueSkinView().getPanel());
    }
}