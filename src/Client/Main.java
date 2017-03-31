package Client;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Optional;


/**
 * Created by miral on 16/03/17.
 */
public class Main extends Application {


    // instance variables
    public Socket socket;
    public BufferedReader in;
    public PrintWriter out;

    //the ip address to connect to
    //localhost = local network
    public String HOSTNAME = "localhost";

    // the port on which to connect
    public int PORT = 8080;

    // the shared folder
    File sharedFolder;

    // the dataSource for the client and server
    DataSource clientDS;
    DataSource serverDS;

    // an observable list to hold the client and servers file list
    public ObservableList clientOL;
    public ObservableList serverOL;

    public String name = "Shared Folder - Local Client";

    @Override
    public void start(Stage primaryStage) throws Exception {

        TextInputDialog dialog = new TextInputDialog("Computer");
        dialog.setTitle("Computer Name");
        dialog.setHeaderText("What is your computer Name");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent())
             name = result.get();


        clientDS = new DataSource();
        serverDS = new DataSource();

        // call the dir method
        dir();

        // make an instance of the ui class
        UI ui = new UI(this);
        ui.setUI(primaryStage);

        // chose location of shared folder
        sharedFolder = ui.chooseDirectory();

        traverseDirectory(sharedFolder);

        // add the client and server data sources to the observable lists
        clientOL= clientDS.getFiles();
        serverOL = serverDS.getFiles();

        ui.clientTable.setItems(clientOL);
        ui.serverTable.setItems(serverOL);
    }

    public void traverseDirectory(File file)throws IOException {
        //if the file is a directory
        if (file.isDirectory()) {
            // for directories, recursively call
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                traverseDirectory(filesInDir[i]);
            }
        }
        else
            clientDS.setFiles(file.getName());
    }

    public static void main (String args []) { launch(args); }

    /**
     * connect to the server and send it the DIR command to get a list of files on the servers shared folder
     */
    public void dir() throws IOException{

        // connect to the server
        socket = new Socket(HOSTNAME, PORT);

        // buffered reader to read from the server
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // write to the server
        out = new PrintWriter(socket.getOutputStream(),true);

        // tell the server you want the list of the files on it
        out.println("DIR");


        // as long as the server is sending a list of files
        while(true){

            // save the file name returned by server to temp
            String temp = in.readLine();

            // if there is a file on the server shared folder
            if(temp != null) {

                // add the file datasource
                serverDS.setFiles(temp);
            }

            else {
                out.close();
                in.close();
                socket.close();
                break;
            }
        }
    }

}
