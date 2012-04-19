import java.io.File;
import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/19/12
 * Time: 4:18 PM
 */
public class Worker extends Thread{
    private Socket mySocket;
    private static final Integer fileSize = 1024*1024*10; // filesize temporary hardcoded

    public Worker(Socket inpSocket){
        mySocket = inpSocket;
    }

    @Override
    public void run(){
        System.out.println("New worker started.");
        byte[] mybytearray = new byte[fileSize];
        try{
            File file = new File(Double.toString(System.nanoTime()));
            if (file.exists()){
                file.delete();
            }
            if (!file.createNewFile()){
                throw new Exception();
            }
            InputStream is = mySocket.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int offset = 0;
            int bytesRead = 0;
            int currBytesRead = 0;
            do{
                currBytesRead = is.read(mybytearray, offset, mybytearray.length - offset);
                if (currBytesRead > 0){
                    bytesRead += currBytesRead;
                }
                offset += bytesRead;
            } while (currBytesRead > 0);
            bos.write(mybytearray, 0, bytesRead);
            bos.flush();
            bos.close();
            mySocket.close();
        } catch (Exception e){
            System.out.println("Shit happens!");
        }
        System.out.println("File written.");
    }
}
