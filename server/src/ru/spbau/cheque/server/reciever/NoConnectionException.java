package ru.spbau.cheque.server.reciever;

public class NoConnectionException extends Exception{
    @Override
    public String getMessage(){
        return "Can't establish connection.";
    }
}
