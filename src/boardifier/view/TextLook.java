package src.boardifier.view;

import src.boardifier.model.GameElement;
import src.boardifier.model.TextElement;

public class TextLook extends ElementLook {

    public TextLook(GameElement element) {
        super(element);
    }

    public int getWidth() {
        TextElement te = (TextElement) getElement();
        return te.getText().length();
    }

    public int getHeight() {
        return 1;
    }

    public void render() {
        setSize(getWidth(), getHeight());
        String txt = ((TextElement)getElement()).getText();
        for(int i=0;i<txt.length();i++) {
            shape[0][i] = String.valueOf(txt.charAt(i));
        }
    }
}
