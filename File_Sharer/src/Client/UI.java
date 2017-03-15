package Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Created by miral on 14/03/17.
 */
public class UI extends Application{

    BorderPane bp;
    SplitPane sp;
    HBox hb;

    TableView clientTable;
    TableView serverTable;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("File Sharer");

        sp = new SplitPane();
        clientTable = new TableView();
        serverTable = new TableView();

        sp.setDividerPositions(0.5f);
        sp.getItems().addAll(clientTable,serverTable);

        hb = new HBox();

        Button download = new Button ("Download");
        Button upload = new Button ("Upload");

        hb.getChildren().addAll(download,upload);
        hb.setSpacing(10);

        bp = new BorderPane();

        bp.setTop(hb);
        bp.setCenter(sp);

        Scene scene = new Scene(bp, 650, 650);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main (String args[]) {launch(args);}

}
