package ru.spbau.cheque.server.reciever;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.cheque.server.recognition.*;

import javax.imageio.ImageIO;

public class Worker implements Runnable{
    private Socket mySocket;
    private static final Integer bufferSize = 1024; // bufferSize temporary hardcoded

    public Worker(Socket inpSocket){
        mySocket = inpSocket;
    }
    
    BufferedImage recieveImage() throws ImageSavingFailureException{
        try{
            InputStream is = mySocket.getInputStream();
            BufferedImage image = ImageIO.read(is);
            System.out.println("File successfully recieved."); //Supposed to be written in log.
            return image;
        } catch (IOException e){
            System.err.println("Can't recieve image. Got IOException."); //Supposed to be written in log.
            throw new ImageSavingFailureException();
        }
    }

    public void sendResponse(String inpResponse){
        DataOutputStream os = null;
        try{
            os = new DataOutputStream(mySocket.getOutputStream());
            os.writeBytes(inpResponse);
            os.close();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
    }

    List<Integer> recieveAreaCoordinates() throws ImageSavingFailureException{
        List<Integer> coordinates = new ArrayList<Integer>(4);
        byte[] buffer = new byte[4];
        try{
            InputStream is = mySocket.getInputStream();
            for (int i = 0; i < 4; ++i){
                int bytesRead = is.read(buffer, 0, 4);
                if (bytesRead <= 0) throw new ImageSavingFailureException();
                coordinates.add(ByteBuffer.wrap(buffer, 0, 4).getInt());
            }
            return coordinates;
        } catch (IOException e){
            System.err.println("Can't recieve coordinates. Got IOException."); //Supposed to be written in log.
            throw new ImageSavingFailureException();
        }
    }

    @Override
    public void run(){
        System.out.println("New worker started.");  //Supposed to be written in log.
        try{
            List<Integer> areaCoordinates = recieveAreaCoordinates();
            BufferedImage image = recieveImage();
            Recognizer recognizer = new Recognizer();
            Cheque recognizedCheque = recognizer.doRecognition(image, areaCoordinates.get(0), areaCoordinates.get(1), areaCoordinates.get(2), areaCoordinates.get(3));
            sendResponse("OK");
            //todo: serialize and send cheque
        } catch (ImageSavingFailureException e){
            System.err.println("Worker cant't complete task because image can not be saved or received."); //Supposed to be written in log.
            sendResponse("ERROR");
        } catch (OcrFailedException e){
            System.err.println("Worker cant't complete task because image can not be saved or received."); //Supposed to be written in log.
            sendResponse("ERROR");
        } finally {
            try{
                mySocket.close();
            } catch (IOException e){
                System.err.println("Strange IOException on closing socket."); //Supposed to be written in log.
                //sendResponse("ERROR");  Error already sent in catch.
            }
        }
    }
}
