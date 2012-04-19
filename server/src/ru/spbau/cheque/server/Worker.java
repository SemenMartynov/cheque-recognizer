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
        File imageFile = null;
        try{
            imageFile = File.createTempFile("img", ".tmp");
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

        if (imageFile != null) {
            Recognizer recognizer = new Recognizer();
            try {
                List<String> ocrText = recognizer.doRecognition(ImageIO.read(imageFile));
                for (String ocrLine : ocrText) {
                    System.out.println(ocrLine);
                }
                System.out.println("File (probably) recognized.");
            } catch (OcrFailedException e) {
                System.out.println("OCR failed for some reason");
            } catch (IOException e) {
                System.out.println("Can't read image file");
            }
        }
    }
}
