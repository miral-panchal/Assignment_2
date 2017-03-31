package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by faizan on 17/03/17.
 */
public class FileShareServer {

    // create server socket used to accept connections on port SERVER_PORT
    protected ServerSocket serverSocket;

    protected Socket clientSocket;


    // the port to use
    public static int SERVER_PORT = 8080;

    protected int numClients = 0;

    public FileShareServer(){
        try{

            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Waiting for connection on Port "  +  serverSocket.getLocalPort());

            // run client forever
            while (true){

                // wait for client to connect
                clientSocket = serverSocket.accept();
                System.out.println("Client #"+(numClients+1)+" connected.");
                FileShareServerThread thread = new FileShareServerThread(clientSocket);
                thread.start();
                numClients++;
            }
        }
        catch(IOException e){
            System.err.println("IOEXception while creating server connection");
        }
    }

    public static void main(String args[]){
        FileShareServer server = new FileShareServer();
    }
}
