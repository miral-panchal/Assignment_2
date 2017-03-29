package Client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 * Created by miral on 16/03/17.
 */
public class Main extends Application {


    static Socket socket;
    static BufferedReader in;
    static PrintWriter out;

    public  static String HOSTNAME = "localhost";
    public  static int    PORT = 8080;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = UI.setUI(primaryStage);


    }

    public static void main (String args []) {

        try {
            socket = new Socket(HOSTNAME,PORT);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);


            System.out.println(in.readLine());
        }
        catch (Exception e) {e.printStackTrace();}

        boolean exit = false;
        while(!exit){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Command");
            out.println(scanner.next()); // Get what the user types.
            try {
                if(in.ready()) {
                    if(!in.readLine().equals("Not a valid command"))
                        exit = true;
                }else{
                    exit = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("rerun");
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        launch(args);
    }

}
