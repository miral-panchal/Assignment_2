package Client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;


/**
 * Created by miral on 16/03/17.
 */
public class Main extends Application {


    public static Socket socket;
    public static BufferedReader in;
    public static PrintWriter out;

    public static String HOSTNAME = "99.236.141.52";
    public static int    PORT = 8080;

    File sharedFolder;

    private DataSource clientDS;
    private DataSource serverDS;

    @Override
    public void start(Stage primaryStage) throws Exception {
        clientDS = new DataSource();
        serverDS = new DataSource();

        dir();
        UI.setUI(primaryStage);
        sharedFolder = UI.chooseDirectory();

        traverseDirectory(sharedFolder);

        UI.clientTable.setItems(clientDS.getFiles());
        UI.serverTable.setItems(serverDS.getFiles());
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

    public static void main (String args []) {
        try {
            socket = new Socket(HOSTNAME,PORT);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            launch(args);

            socket.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public void dir() throws IOException{
        socket = new Socket(HOSTNAME, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);

<<<<<<< HEAD
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
=======
        out.println("DIR");

        while(true){

            String temp = in.readLine();
            if(temp != null) {
                serverDS.setFiles(temp);
                System.out.println(temp);
            }

            else {
                out.close();
                in.close();
                socket.close();
                break;
            }
        }

>>>>>>> 6d1bde579104be0fb9aa01eac80398bb3f57510c
    }

}
