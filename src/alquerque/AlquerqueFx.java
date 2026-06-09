package src.alquerque;

import javafx.application.Application;
import javafx.stage.Stage;
import src.alquerque.view.AlquerqueView;

public class AlquerqueFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AlquerqueView view=new AlquerqueView(primaryStage);
        view.display();
    }
}
