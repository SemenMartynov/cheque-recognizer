package ru.spbau.cheque.server;

/**
 * Classname:
 * User: dimatwl
 * Date: 4/19/12
 * Time: 4:49 PM
 */
public class ServerMain {
    public static void main(String[] argv){
        ChequeRecognitionServer srv = new ChequeRecognitionServer();
        try{
            srv.start();
        } catch (NoConnectionException e){
            System.out.println("Shit happens.");
        }
    }
}
