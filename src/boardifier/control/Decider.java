package src.boardifier.control;

import src.boardifier.model.action.ActionList;
import src.boardifier.model.Model;

public abstract class Decider {
    protected Model model;
    protected Controller control;

    public Decider(Model model, Controller control) {
        this.model = model;
        this.control = control;
    }

    public abstract ActionList decide();
}
