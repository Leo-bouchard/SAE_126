package alquerque.control;

import alquerque.view.AlquerqueGameView;

public class AlquerqueGameController {

    private AlquerqueGameView view;

    public AlquerqueGameController(AlquerqueGameView view) {
        this.view = view;
    }

    // une methode par mode : decide quel panneau afficher
    public void showPvP() {
        view.setConfig(view.buildPvP());
    }

    public void showPvB() {
        view.setConfig(view.buildPvB());
    }

    public void showBvB() {
        view.setConfig(view.buildBvB());
    }
}