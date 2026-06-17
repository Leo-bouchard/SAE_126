package alquerque.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import alquerque.view.AlquerqueMainMenuView;
import alquerque.view.AlquerqueShopView;

public class AlquerqueGoToShopControl implements EventHandler<ActionEvent> {
    private AlquerqueMainMenuView menu;

    public AlquerqueGoToShopControl(AlquerqueMainMenuView menu) {
        this.menu = menu;
    }

    @Override
    public void handle(ActionEvent event) {
        menu.setRightPanel(new AlquerqueShopView().getPanel());
    }
}
