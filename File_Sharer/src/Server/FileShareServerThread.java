package Server;

import java.io.*;
import java.net.Socket;

/**
 * Created by faizan on 27/03/17.
 */
public class FileShareServerThread extends Thread {

    // the name of the shared folder on the server
    private final String folderName = "Shared Folder";

    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;

    public FileShareServerThread(Socket socket) {
        super();
        this.socket = socket;

        try {
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
    }

    public void run() {

        try {
            /**
             * Get the input from the client. What they want to do
             * DIR, UPLOAD, or DOWNLOAD
             */
            String message = in.readLine();

            // if the message is equal to DIR
            if (message.equals("DIR")) {
                // Call the DIR function
                DIR();
            } else if (message.equals("UPLOAD")) {
                //get the name of the file to upload from the client
                message = in.readLine();
                Upload(message);
            } else if (message.equals("DOWNLOAD")) {
                //get the name of the file to download from the client
                message = in.readLine();
                Download(message);
            }else{
                /**
                 *if the client entered a command other then the above 3
                 * tell the client its not a valid command
                 */
                out.println("Not a valid command");
            }

            // close the connection(socket) with the client
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * send the list of the files in the shared folder on the server to the client
     */
    public void DIR(){
        File mainDirectory = new File(folderName);
        File[] filesInDir = mainDirectory.listFiles();
        for (int i = 0; i < filesInDir.length; i++) {
            out.println(filesInDir[i].getName());
        }
    }


    /**
     * gets a text file from the client line by line and saves it into another
     * text file in the Shared Folder on the Server
     * @param fileName the name of the file to upload
     */
    public void Upload(String fileName){
        // make a new file object and tell it the location+name of the file to create
        File file = new File(folderName+File.separator+fileName);
        try {
            PrintWriter output = new PrintWriter(file);
            String msg = in.readLine();
            while(msg != null){
                msg = in.readLine();
                output.println(msg);
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a file on the servers shared folder line by line and sends it to client 1 line at a time
     * @param fileName the name of the file the to download
     */
    public void Download(String fileName){
        try {
            FileReader fileReader  = new FileReader(folderName+File.separator+fileName);
            BufferedReader input = new BufferedReader(fileReader);
            String line;

            System.out.println(fileName);
            while ((line = input.readLine()) != null) {
                out.println(line);
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
