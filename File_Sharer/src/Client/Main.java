package Client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by miral on 16/03/17.
 */
public class Main extends Application {


    static Socket socket;
    static BufferedReader in;
    static PrintWriter out;

    public  static String HOSTNAME = "localhost";
    public  static int    PORT = 80;

    private static String compName = "Computer";
    private static String folderPath = "~/Desktop/Test";

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = UI.setUI(primaryStage);


    }

    public static void main (String args []) {

//        compName = args[0];
//        folderPath = args[1];

        try {
            socket = new Socket(HOSTNAME,PORT);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());


            // close the connection (3-way tear down handshake)
            out.close();
            in.close();
            socket.close();
        }
        catch (Exception e) {e.printStackTrace();}

        launch(args);
    }

}
