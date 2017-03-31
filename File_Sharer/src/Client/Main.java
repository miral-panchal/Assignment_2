package Client;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;


/**
 * Created by miral on 16/03/17.
 */
public class Main extends Application {


    public Socket socket;
    public BufferedReader in;
    public PrintWriter out;

    public String HOSTNAME = "localhost";
    public int PORT = 8080;

    File sharedFolder;

    DataSource clientDS;
    DataSource serverDS;

    public ObservableList clientOL;
    public ObservableList serverOL;


    @Override
    public void start(Stage primaryStage) throws Exception {
        clientDS = new DataSource();
        serverDS = new DataSource();

        dir();

        UI ui = new UI(this);
        ui.setUI(primaryStage);
        sharedFolder = ui.chooseDirectory();

        traverseDirectory(sharedFolder);

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

    public void dir() throws IOException{
        socket = new Socket(HOSTNAME, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);

        out.println("DIR");

        while(true){

            String temp = in.readLine();
            if(temp != null) {
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
