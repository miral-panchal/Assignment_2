package Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


/**
 * Created by miral on 14/03/17.
 */
public class UI{

    private static BorderPane bp;
    private static SplitPane sp;
    private static HBox hb;

    public static TableView clientTable;
    public static TableView serverTable;

    public static Button download;
    public static Button upload;

    public static Stage setUI(Stage primaryStage) throws Exception {

        primaryStage.setTitle("File Sharer");

        sp = new SplitPane();
        clientTable = new TableView();
        serverTable = new TableView();

        sp.setDividerPositions(0.5f);
        sp.getItems().addAll(clientTable,serverTable);

        hb = new HBox();

        download = new Button ("Download");
        upload = new Button ("Upload");

        hb.getChildren().addAll(download,upload);
        hb.setSpacing(10);

        bp = new BorderPane();

        bp.setTop(hb);
        bp.setCenter(sp);

        Scene scene = new Scene(bp, 650, 650);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        return primaryStage;
    }
}
