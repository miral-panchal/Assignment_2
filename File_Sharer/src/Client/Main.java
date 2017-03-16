package Client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by miral on 16/03/17.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = UI.setUI(primaryStage);

    }

    public static void main (String args []) {launch(args);}

}
