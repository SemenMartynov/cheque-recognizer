import java.io.*;
import java.net.Socket;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/19/12
 * Time: 4:18 PM
 */
public class Worker implements Runnable{
    private Socket mySocket;
    private static final Integer bufferSize = 1024; // bufferSize temporary hardcoded

    public Worker(Socket inpSocket){
        mySocket = inpSocket;
    }


    @Override
    public void run(){
        System.out.println("New worker started.");
        byte[] buffer = new byte[bufferSize];
        try{
            File imageFile = File.createTempFile("img", ".tmp");
            InputStream is = mySocket.getInputStream();
            FileOutputStream fos = new FileOutputStream(imageFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            final int offset = 0;
            int bytesRead = 0;
            while (is.available() > 0){
                bytesRead = is.read(buffer, offset, buffer.length);
                bos.write(buffer, 0, bytesRead);
            }
            bos.close();
            mySocket.close();
        } catch (IOException e){
            System.out.println("Can't save image. Got IOException.");
        }
        System.out.println("File successfully written.");
    }
}
