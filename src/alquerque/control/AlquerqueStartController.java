package src.alquerque.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Node;
import src.alquerque.view.AlquerqueGameView;

public class AlquerqueStartController implements EventHandler<ActionEvent> {

    private AlquerqueGameView gameView;

    public AlquerqueStartController(AlquerqueGameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void handle(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        String mode = gameView.getMode();

        switch (mode) {
            case "PvP":   // 2 joueurs humains
                AlquerqueController.botForPlayer0 = 0;
                AlquerqueController.botForPlayer1 = 0;
                AlquerqueController.namePlayer0 = nomOuDefaut(gameView.getNom1(), "Joueur 1");
                AlquerqueController.namePlayer1 = nomOuDefaut(gameView.getNom2(), "Joueur 2");
                break;

            case "PvB":   // 1 joueur + 1 bot
                AlquerqueController.botForPlayer0 = 0;
                AlquerqueController.botForPlayer1 = botId(gameView.getBot1());
                AlquerqueController.namePlayer0 = nomOuDefaut(gameView.getNom1(), "Joueur");
                AlquerqueController.namePlayer1 = gameView.getBot1();
                break;

            case "BvB":   // 2 bots
                AlquerqueController.botForPlayer0 = botId(gameView.getBot1());
                AlquerqueController.botForPlayer1 = botId(gameView.getBot2());
                AlquerqueController.namePlayer0 = gameView.getBot1();
                AlquerqueController.namePlayer1 = gameView.getBot2();
                break;
        }

        AlquerqueController.startGame(stage);
    }

    // renvoie le nom saisi, ou un nom par defaut si vide
    private String nomOuDefaut(String saisi, String defaut) {
        if (saisi == null || saisi.trim().isEmpty()) return defaut;
        return saisi.trim();
    }

    // 1=Fred, 2=Jesus, 3=Master Mind
    private int botId(String nom) {
        if (nom == null) return 1;
        switch (nom) {
            case "Jesus":       return 2;
            case "Master Mind": return 3;
            default:            return 1;
        }
    }
}