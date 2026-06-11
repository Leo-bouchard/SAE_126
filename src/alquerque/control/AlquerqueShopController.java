package src.alquerque.control;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class AlquerqueShopController {
    private static final Path WINGS_FILE = Paths.get("src/alquerque/savedData/wings");
    private static final Path OWNED_FILE = Paths.get("src/alquerque/savedData/allSkinPawn");
    private static final int SHOP_PRICE = 10;

    private static final String SKIN_PATH_PREFIX = "/src/alquerque/Image/";
    private static final String SKIN_PATH_SUFFIX = ".png";

    private final Random random = new Random();

    public int getWings() {
        try {
            if (!Files.exists(WINGS_FILE)) {
                Files.createDirectories(WINGS_FILE.getParent());
                Files.writeString(WINGS_FILE, "100");
                return 100;
            }
            return Integer.parseInt(Files.readString(WINGS_FILE).trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    private void setWings(int wings) {
        try {
            Files.writeString(WINGS_FILE, String.valueOf(wings));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getOwnedSkins() {
        try {
            if (!Files.exists(OWNED_FILE)) return new HashSet<>();
            return new HashSet<>(Files.readAllLines(OWNED_FILE));
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    private void addOwnedSkin(String skin) {
        try {
            Files.createDirectories(OWNED_FILE.getParent());
            Files.writeString(OWNED_FILE, SKIN_PATH_PREFIX + skin + SKIN_PATH_SUFFIX + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String rollRandomSkin() {
        int roll = random.nextInt(7900) + 1;

        if (roll <= 4000) {
            return "WhitePawn10bit";
        } else if (roll <= 6000) {
            return "WhitePawnStrar";
        } else if (roll <= 7000) {
            return "WhitePawnMickey";
        } else if (roll <= 7500) {
            return "WhitePawnPat";
        }
        else if (roll <= 7750) {
            return "WhitePawnStarDrops";
        }
        else if (roll <= 7850) {
            return "WhitePawnUrssaf";
        }
        else if (roll <= 7900) {
            return "WhitePawnValou";
        }
        else {
            return "BlackPawn";
        }
    }

    public ShopResult buyRandomSkin() {
        int wings = getWings();
        if (wings < SHOP_PRICE) {
            return new ShopResult(false, null, false, wings);
        }

        wings -= SHOP_PRICE;
        setWings(wings);

        String skin = rollRandomSkin();
        String skinPath = SKIN_PATH_PREFIX + skin + SKIN_PATH_SUFFIX;
        Set<String> owned = getOwnedSkins();
        boolean isNew = !owned.contains(skinPath);
        if (isNew) {
            addOwnedSkin(skin);
        }
        return new ShopResult(true, skin, isNew, wings);
    }

    public static class ShopResult {
        public final boolean success;
        public final String skin;
        public final boolean isNew;
        public final int remainingWings;

        public ShopResult(boolean success, String skin, boolean isNew, int remainingWings) {
            this.success = success;
            this.skin = skin;
            this.isNew = isNew;
            this.remainingWings = remainingWings;
        }
    }

}