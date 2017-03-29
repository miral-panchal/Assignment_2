package Server;

import java.io.*;
import java.net.Socket;

/**
 * Created by faizan on 27/03/17.
 */
public class FileShareServerThread extends Thread {

    private static final String folderName = "Shared Folder";

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
        Upload("hello.txt");
        boolean endOfSession = false;

        while(!endOfSession) {
            try {
                String message = in.readLine();

                // is the client sending a message
                if(message != null) {
                    // read the message the client sent
                    //message = in.readLine();

                    // if the message is equal to DIR
                    if (message.equals("DIR")) {
                        // Call the DIR function
                        System.out.println(message);
                        DIR();
                        // end the while loop
                        endOfSession = true;
                    } else if (message.equals("UPLOAD")) {
                        System.out.println("Upload");
                        Upload(in.readLine());
                        endOfSession = true;
                    } else if (message.equals("DOWNLOAD")) {
                        System.out.println("Download");
                        endOfSession = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // close the connection(socket)
            socket.close();
        } catch(IOException e) {
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
            System.out.println(filesInDir[i].getName());
        }
    }

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

    public void Download(){

    }

}
