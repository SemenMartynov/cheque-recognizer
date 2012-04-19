package ru.spbau.cheque.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/19/12
 * Time: 3:43 PM
 */

public class ChequeRecognitionServer {
    private final ServerSocket myServerSocket;
    private final ExecutorService pool;

    public ChequeRecognitionServer(int port) throws NoConnectionException{
        try{
            myServerSocket = new ServerSocket(3843);
        } catch (IOException e){
            System.err.println("Can't listen port 3843");
            throw new NoConnectionException();
        }
        pool = Executors.newCachedThreadPool();
    }

    public void start() throws NoConnectionException {
        try{
            for(;;){
                pool.execute(new Worker(myServerSocket.accept()));
            }
        } catch (IOException e){
                System.err.println("Can't accept on ServerSocket.");
                throw new NoConnectionException();
        }
    }
}
