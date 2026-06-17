package alquerque;

import javafx.application.Application;
import javafx.stage.Stage;
import alquerque.control.AlquerquePlaylistController;
import alquerque.view.AlquerqueView;

// change VM option with this : --module-path

/// Users/spines/Downloads/javafx-sdk-17.0.18/lib
//--add-modules
//javafx.controls,javafx.fxml,javafx.media

// because We need JavaFx.media

public class AlquerqueFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AlquerquePlaylistController.getInstance().play();
        AlquerqueView view=new AlquerqueView(primaryStage);
        view.display();
    }
}
