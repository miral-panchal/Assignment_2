package Client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Optional;


/**
 * Created by miral on 14/03/17.
 */
public class UI{
    private Stage primaryStage;

    private BorderPane bp;
    private SplitPane sp;
    private HBox hb;

    public TableView<FileList> clientTable;
    public TableView<FileList> serverTable;

    public Button download;
    public Button upload;

    private Main main;
    public UI(Main main){
        this.main = main;
    }
    public Stage setUI(Stage stage) throws Exception {
        primaryStage= stage;
        primaryStage.setTitle("File Sharer");

        sp = new SplitPane();
        clientTable = new TableView();
        serverTable = new TableView();

        TableColumn clientTitle = new TableColumn(main.name);
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

        download.setOnAction(e -> _downloadFiles(main.in, main.out, clientTable, serverTable));
        upload.setOnAction(e -> _uploadFiles(main.in, main.out, clientTable, serverTable));

        Scene scene = new Scene(bp, 650, 650);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        return primaryStage;
    }

    public void _downloadFiles(BufferedReader in, PrintWriter out, TableView<FileList> cTable, TableView<FileList> sTable){
        FileList file_download = sTable.getSelectionModel().getSelectedItem();

        try {

            main.socket = new Socket(main.HOSTNAME, main.PORT);
            in = new BufferedReader(new InputStreamReader(main.socket.getInputStream()));
            out = new PrintWriter(main.socket.getOutputStream(),true);

            File new_file = new File(file_download.getFileName());

            for (int i = 0; i < cTable.getItems().size(); i++) {
                if (file_download.getFileName().equals(cTable.getItems().get(i).getFileName())){
                    boolean replace = confirmBox();
                    if(replace == true) {
                        downloadFile(in, out, new_file);
                        return;
                    }
                    else
                        return;
                }
            }
            downloadFile(in, out, new_file);
        }
        catch (Exception e){e.printStackTrace();}
    }

    /**
     * Download file from server
     * @param in  the buffer reader used to read from the server
     * @param out  the printWriter used to write to ther server
     * @param file  the file to download
     */
    private void downloadFile(BufferedReader in, PrintWriter out, File file) throws IOException{
        PrintWriter fOut = new PrintWriter(main.sharedFolder.getAbsolutePath()+File.separator+file);
        out.println("DOWNLOAD");
        out.println(file.getName());
        out.flush();

        String line;
        while((line = in.readLine())!= null) {
            fOut.println(line);
        }

        out.close();
        in.close();
        fOut.close();

        main.clientOL.clear();
        main.traverseDirectory(main.sharedFolder);
    }

    public void _uploadFiles(BufferedReader in, PrintWriter out, TableView<FileList> cTable, TableView<FileList> sTable) {
        FileList file_download = cTable.getSelectionModel().getSelectedItem();

        try {
            main.socket = new Socket(main.HOSTNAME, main.PORT);
            in = new BufferedReader(new InputStreamReader(main.socket.getInputStream()));
            out = new PrintWriter(main.socket.getOutputStream(),true);

            File new_file = new File(file_download.getFileName());
            for (int i = 0; i < sTable.getItems().size(); i++) {
                if (file_download.getFileName().equals(sTable.getItems().get(i).getFileName())){
                    boolean replace = confirmBox();
                    if(replace == true) {
                        downloadFile(in, out, new_file);
                        return;
                    }
                    else
                        return;
                }
            }
            uploadFile(in,out,new_file);
        }
        catch (Exception e){e.printStackTrace();}
    }

    /**
     * Upload file to the server
     * @param in  the buffer reader used to read from the server
     * @param out  the printWriter used to write to ther server
     * @param file  the file to upload
     */
    private void uploadFile(BufferedReader in, PrintWriter out, File file) throws IOException{
        BufferedReader fIn = new BufferedReader(new FileReader(main.sharedFolder.getAbsolutePath()+File.separator+file));
        out.println("UPLOAD");
        out.println(file.getName());
        out.flush();

        String line;
        while((line = fIn.readLine()) != null) {
            out.println(line);
        }

        out.close();
        in.close();
        fIn.close();

        main.serverOL.clear();
        main.dir();
    }

    public boolean confirmBox(){
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

    public File chooseDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a Shared Folder");
        directoryChooser.setInitialDirectory(new File("."));
        return directoryChooser.showDialog(primaryStage);
    }
}
