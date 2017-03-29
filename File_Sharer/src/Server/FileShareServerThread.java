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
                if(in.ready()) {
                    message = in.readLine();
                    if (message.equals("DIR")) {
                        DIR();
                        endOfSession = true;
                    } else if (message.equals("UPLOAD")) {
                        Upload(in.readLine());
                        System.out.println("Upload");
                        endOfSession = true;
                    } else if (message.equals("DOWNLOAD")) {
                        System.out.println("Download");
                        endOfSession = true;
                    } else {
                        System.out.println("Not a valid command");
                        out.println("Not a valid command");
                    }
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
        File mainDirectory = new File(folderName);
        File[] filesInDir = mainDirectory.listFiles();
        for (int i = 0; i < filesInDir.length; i++) {
            out.println(filesInDir[i].getName());
        }
    }

    public void Upload(String fileName){
    }

    public void Download(){

    }

}
