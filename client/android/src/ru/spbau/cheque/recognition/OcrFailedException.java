package ru.spbau.cheque.recognition;

public class OcrFailedException extends Throwable {
    public OcrFailedException() {
    }

    public OcrFailedException(String message) {
        super(message);
    }

    public OcrFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OcrFailedException(Throwable cause) {
        super(cause);
    }

    public OcrFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        //fixme: got error here.
        //super(message, cause, enableSuppression, writableStackTrace);
    }
}
