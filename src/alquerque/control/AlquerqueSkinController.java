package alquerque.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlquerqueSkinController {


    private static final String FICHIER_SKINS = "src/alquerque/savedData/allSkinPawn";
    private static final String FICHIER_CHOSE = "src/alquerque/savedData/skinOn";

    private List<String> skins = new ArrayList<>();
    private int index = 0;

    public AlquerqueSkinController() {
        chargerSkins();
        chargerChoix();
    }

    public void precedent() {
        if (skins.isEmpty()) return;
        index--;
        if (index < 0) index = skins.size() - 1;
        sauvegarderChoix();
    }

    public void suivant() {
        if (skins.isEmpty()) return;
        index++;
        if (index >= skins.size()) index = 0;
        sauvegarderChoix();
    }

    public String getSkinActuel() {
        if (skins.isEmpty()) return null;
        return skins.get(index);
    }

    private void chargerSkins() {
        try (BufferedReader r = new BufferedReader(new FileReader(FICHIER_SKINS))) {
            String ligne;
            while ((ligne = r.readLine()) != null) {
                ligne = ligne.trim();
                if (!ligne.isEmpty()) skins.add(ligne);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private void sauvegarderChoix() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FICHIER_CHOSE))) {
            w.write(skins.get(index));
        } catch (IOException e) {
            System.out.println( e.getMessage());
        }
    }

    private void chargerChoix() {
        try (BufferedReader r = new BufferedReader(new FileReader(FICHIER_CHOSE))) {
            String ligne = r.readLine();
            if (ligne != null) {
                ligne = ligne.trim();
                int pos = skins.indexOf(ligne);
                if (pos >= 0) index = pos;
            }
        } catch (IOException e) {
            index = 0;
        }
    }
}