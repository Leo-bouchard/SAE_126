package src.alquerque.view;

import src.alquerque.model.AlquerquePawn;
import src.boardifier.model.GameElement;
import src.boardifier.view.ElementLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PawnLook extends ElementLook {

    private static final Path SKIN_ON_FILE = Paths.get("src/alquerque/savedData/skinOn");
    private static final String DEFAULT_WHITE = "src/alquerque/Image/WhitePawn.png";
    private static final String DEFAULT_BLACK = "src/alquerque/Image/BlackPawn.png";

    private ImageView imageView;

    public PawnLook(GameElement element) {
        super(element);
        AlquerquePawn pawn = (AlquerquePawn) element;

        String path;
        if (pawn.getColor() == 1) {
            path = getWhiteSkinPath();
        } else {
            path = getBlackSkinPath();
        }

        Image image = loadImage(path, pawn.getColor() == 1 ? DEFAULT_WHITE : DEFAULT_BLACK);
        imageView = new ImageView(image);
        imageView.setFitWidth(55);
        imageView.setFitHeight(55);
        imageView.setTranslateX(-40);
        imageView.setTranslateY(-40);
        addNode(imageView);
    }

    private Image loadImage(String path, String fallback) {
        File f = new File(path);
        if (!f.exists()) {
            f = new File(fallback);
        }
        return new Image(f.toURI().toString());
    }

    private String getWhiteSkinPath() {
        try {
            String raw = Files.readString(SKIN_ON_FILE).trim();
            if (raw.startsWith("/")) {
                raw = raw.substring(1);
            }
            if (raw.isEmpty() || !new File(raw).exists()) {
                return DEFAULT_WHITE;
            }
            return raw;
        } catch (Exception e) {
            return DEFAULT_WHITE;
        }
    }

    private String getBlackSkinPath() {
        String whitePath = getWhiteSkinPath();
        String blackPath = whitePath.replace("WhitePawn", "BlackPawn");
        if (new File(blackPath).exists()) {
            return blackPath;
        }
        return DEFAULT_BLACK;
    }

    @Override
    public void render() {
    }
}