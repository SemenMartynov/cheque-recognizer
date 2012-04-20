package ru.spbau.cheque.server.reciever;

public class ImageSavingFailureException extends Exception{
    @Override
    public String getMessage(){
        return "Can't recieve or save image.";
    }
}
