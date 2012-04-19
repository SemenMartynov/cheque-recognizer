package ru.spbau.cheque.server;

import ru.spbau.cheque.server.recognition.OcrFailedException;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.Socket;
import java.util.List;

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
        File file = null;
        try{
            file = new File(Double.toString(System.nanoTime()));
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

        if (file != null) {
            Recognizer recognizer = new Recognizer();
            try {
                List<String> ocrText = recognizer.doRecognition(ImageIO.read(file));
                for (String ocrLine : ocrText) {
                    System.out.println(ocrLine);
                }
                System.out.println("File (probably) recognized.");
            } catch (OcrFailedException e) {
                System.out.println("Shit happens! (especially when trying to recognize an image)");
            } catch (IOException e) {
                System.out.println("Shit happens! (especially when trying to read a file)");
            }
        }
    }
}
