package ru.spbau.cheque.server;

import ru.spbau.cheque.server.recognition.OcrEngine;
import ru.spbau.cheque.server.recognition.OcrFailedException;
import ru.spbau.cheque.server.recognition.TesseractOcrEngine;

import java.awt.image.BufferedImage;
import java.util.List;

public class Recognizer extends Thread {
    public Recognizer() {
        this.engine = new TesseractOcrEngine("tesseract", "-l rus -psm 6");
    }

    List<String> doRecognition(BufferedImage image) throws OcrFailedException {
        return engine.doOcr(image);
    }

    private OcrEngine engine;
}
