package src.boardifier.model.action;

import src.boardifier.model.ContainerElement;
import src.boardifier.model.Coord2D;
import src.boardifier.model.GameElement;
import src.boardifier.model.Model;
import src.boardifier.model.animation.AnimationTypes;
import src.boardifier.model.animation.LinearMoveAnimation;
import src.boardifier.model.animation.MoveAnimation;
import src.boardifier.model.animation.WaitAnimation;


public class RemoveFromContainerAction extends GameAction {

    // construct an action with an animation
    public RemoveFromContainerAction(Model model, GameElement element) {
        super(model, element, AnimationTypes.WAIT_FRAMES);
        animateBeforeExecute = false;
    }

    public void execute() {
        // if the element is not within a container, do nothing
        if (element.getContainer() == null) return;
        element.waitForContainerOpEnd();
        element.getContainer().removeElement(element);
        onEndCallback.execute();
    }


    public void createAnimation() {
    }
}
