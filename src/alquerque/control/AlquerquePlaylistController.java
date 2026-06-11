package src.alquerque.control;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlquerquePlaylistController {

    private static AlquerquePlaylistController instance;

    public static AlquerquePlaylistController getInstance() {
        if (instance == null) {
            instance = new AlquerquePlaylistController();
        }
        return instance;
    }

    private MediaPlayer player;
    private List<String> morceaux = new ArrayList<>();
    private int index = 0;

    private AlquerquePlaylistController() {
        morceaux.add("Jazz Bar No Copyright.mp3");
        morceaux.add("Jazz Music 519632.mp3");
        morceaux.add("Jazz Music Elegant.mp3");
        morceaux.add("Jazz Music No Copyright (1).mp3");
        morceaux.add("Jazz Music No Copyright.mp3");
        morceaux.add("Jazz Piano No Copyright.mp3");
        morceaux.add("Jazz Restaurant Music.mp3");
        morceaux.add("Jazz Restaurant Music 2.mp3");


        Collections.shuffle(morceaux);
    }

    public void play() {
        if (morceaux.isEmpty()) return;
        jouerMorceau(morceaux.get(index));
    }

    private void jouerMorceau(String nom) {
        URL url = getClass().getResource("/src/alquerque/playlist/" + nom);
        if (url == null) {
            System.out.println("Fichier introuvable : " + nom);
            return;
        }
        try {
            Media media = new Media(url.toExternalForm());
            player = new MediaPlayer(media);
            player.setVolume(0.5);
            player.setOnEndOfMedia(this::morceauSuivant);
            player.play();
        } catch (Exception e) {
            System.out.println("Musique indisponible : " + e.getMessage());
        }
    }

    private void morceauSuivant() {
        index++;
        if (index >= morceaux.size()) {
            index = 0;
            Collections.shuffle(morceaux);
        }
        jouerMorceau(morceaux.get(index));
    }

    public void stop() {
        if (player != null) player.stop();
    }

    public void setVolume(double v) {
        if (player != null) player.setVolume(v/100);
    }

    public double getVolume() {
        if (player != null) return player.getVolume();
        return (0.5);
    }

}