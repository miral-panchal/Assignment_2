package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by faizan on 27/03/17.
 */
public class FileShareServerThread extends Thread {

    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;

    public FileShareServerThread(Socket socket) {
        super();
        this.socket = socket;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
    }

    public void run() {
        // initialize interaction
        out.println("Connected to the server");

        boolean endOfSession = false;

        while(!endOfSession) {
            try {
                String message = null;
                if(in.ready())
                    message = in.readLine();
                if(message.equals("DIR")) {
                    System.out.println("DIR");
                    endOfSession = true;
                }
                 else if(message.equals("UPLOAD")){
                    System.out.println("Upload");
                    endOfSession = true;
                }else if(message.equals("DOWNLOAD")) {
                    System.out.println("Download");
                    endOfSession = true;
                }
                else {
                    System.out.println("Not a valid command");
                    out.println("Not a valid command");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void DIR(){
        File
    }

}
