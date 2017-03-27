package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by faizan on 17/03/17.
 */
public class FileShareServer {
    public static void main(String args[]){
        try{
            // create server socket used to accept connections
            // 0 = bind to any available port
            ServerSocket sock = new ServerSocket(8080);

            System.out.println("Port: "  +  sock.getLocalPort());

            // run client forever
            while (true){
                // wait for client to connect
                Socket client = sock.accept();
                System.out.println("Now Connected to " + client.getInetAddress().getHostName());
                PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                out.println("Connected to server");

                out.close();
                client.close();
                //sock.close();
            }
        }


        catch(IOException e){
            System.err.println(e);
        }
    }
}
