package ru.spbau.cheque.server.reciever;

import ru.spbau.cheque.server.recognition.*;

import java.awt.image.BufferedImage;
import java.util.List;

public class Recognizer implements Runnable {
    public Recognizer() {
        this.engine = new TesseractOcrEngine("tesseract", "-l rus -psm 6");
        this.tableExtractor = new RegexTableExtractor();
    }

    @Override
    public void run() {

    }

    public List<String> doRecognition(BufferedImage image) throws OcrFailedException {
        return tableExtractor.extract(engine.doOcr(image));
    }

    private OcrEngine engine;
    private TableExtractor tableExtractor;
}
