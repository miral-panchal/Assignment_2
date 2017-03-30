package Client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;


/**
 * Created by miral on 14/03/17.
 */
public class UI{
    private static Stage primaryStage;

    private static BorderPane bp;
    private static SplitPane sp;
    private static HBox hb;

    public static TableView<FileList> clientTable;
    public static TableView<FileList> serverTable;

    public static Button download;
    public static Button upload;

    public static Stage setUI(Stage stage) throws Exception {
        primaryStage= stage;
        primaryStage.setTitle("File Sharer");

        sp = new SplitPane();
        clientTable = new TableView();
        serverTable = new TableView();

        TableColumn clientTitle = new TableColumn("Shared Folder - Local Client");
        TableColumn serverTitle = new TableColumn("Shared Folder - Server");

        clientTable.getColumns().add(clientTitle);
        serverTable.getColumns().add(serverTitle);

        clientTitle.setPrefWidth(325);
        clientTitle.setResizable(false);

        serverTitle.setPrefWidth(325);
        serverTitle.setResizable(false);

        clientTitle.setCellValueFactory(new PropertyValueFactory<>("FileName"));
        serverTitle.setCellValueFactory(new PropertyValueFactory<>("FileName"));

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

        download.setOnAction(e -> _downloadFiles(Main.in,Main.out, clientTable, serverTable));
        upload.setOnAction(e -> _uploadFiles(Main.in,Main.out, clientTable, serverTable));

        Scene scene = new Scene(bp, 650, 650);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        return primaryStage;
    }

    public static void _downloadFiles(BufferedReader in, PrintWriter out, TableView<FileList> cTable, TableView<FileList> sTable){
        FileList file_download = sTable.getSelectionModel().getSelectedItem();

        try {
            File new_file = new File(file_download.getFileName());
            if(cTable.getItems().contains(file_download)) {
                boolean replace = confirmBox();
                if(replace == true) {
                    downloadFile(in, out, new_file);
                }
                else {
                    return;
                }
            }
            else {
                new_file.createNewFile();
                downloadFile(in, out, new_file);
            }

            //Main.dir();
        }
        catch (Exception e){e.printStackTrace();}
    }

    private static void downloadFile(BufferedReader in, PrintWriter out, File file) throws IOException{
        FileOutputStream fOut = new FileOutputStream(file);
        out.println("DOWNLOAD");
        out.println(file.getName());
        out.flush();

        while(in.readLine()!= null) {
            fOut.write(in.read());
        }

        out.close();
        in.close();
        fOut.close();
    }

    public static void _uploadFiles(BufferedReader in, PrintWriter out, TableView<FileList> cTable, TableView<FileList> sTable) {
        FileList file_download = cTable.getSelectionModel().getSelectedItem();

        try {
            File new_file = new File(file_download.getFileName());
            if(sTable.getColumns().contains(new_file)) {
                boolean replace = confirmBox();
                if(replace == true)
                    uploadFile(in,out,new_file);
                else
                    return;
            }
            else
                uploadFile(in,out,new_file);
        }
        catch (Exception e){e.printStackTrace();}
    }

    private static void uploadFile(BufferedReader in, PrintWriter out, File file) throws IOException{
        FileInputStream fIn = new FileInputStream(file);
        out.println("UPLOAD");
        out.println(file.getName());
        out.flush();

        int temp;
        while((temp = fIn.read()) != -1) {
            System.out.print((char) temp);
        }

        out.close();
        in.close();
        fIn.close();
    }

    public static boolean confirmBox(){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("File Already Exists");
        confirm.setContentText("Would you like to replace this file?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        confirm.getButtonTypes().setAll(yesButton,noButton);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == yesButton)
            return true;
        else
            return false;

    }

    public static File chooseDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a Shared Folder");
        directoryChooser.setInitialDirectory(new File("."));

        return directoryChooser.showDialog(primaryStage);
    }
}
