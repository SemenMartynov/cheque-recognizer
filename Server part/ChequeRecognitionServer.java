import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/19/12
 * Time: 3:43 PM
 */

public class ChequeRecognitionServer {
    private ServerSocket mySocket;

    public void start() throws NoConnectionExeption{
        try{
            mySocket = new ServerSocket(3843);
        } catch (IOException e){
            System.err.println("Can't listen port 3843");
            throw new NoConnectionExeption();
        }
        while (true){
            try{
                Socket clientSocket = mySocket.accept();
                Worker wk = new Worker(clientSocket);
                wk.start();
            } catch (IOException e){
                System.err.println("Can't accept on ServerSocket.");
                throw new NoConnectionExeption();
            }
        }
    }
}
