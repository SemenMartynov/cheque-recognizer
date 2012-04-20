package ru.spbau.cheque.server.recognition;

import ru.spbau.cheque.server.reciever.Recognizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TesseractOcrMain {
    public static void main(String[] args) {
        String ocrEngineExe = args[0];
        String imageName = args[1];
        TesseractOcrEngine ocr = new TesseractOcrEngine(ocrEngineExe, "-l rus -psm 6");

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            System.err.println("Can't read image");
            e.printStackTrace();
            System.exit(1);
        }
        if (image == null) {
            System.err.println("Unsupported image format");
            System.exit(1);
        }

        List<String> ocrText = null;
        List<BlueObject> csvText = null;
        Cheque cheque = null;
        try {
            ocrText = ocr.doOcr(image);
            TableExtractor extractor = new RegexTableExtractor();
            csvText = extractor.extract(new ChequeFormat(""), ocrText);

            Recognizer recognizer = new Recognizer(new TesseractOcrEngine(ocrEngineExe, "-l rus -psm 6"));
            cheque = recognizer.doRecognition(image);
        } catch (OcrFailedException e) {
            System.err.println("Can't do OCR");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("OCRed text:");
        if (ocrText != null) {
            for (String ocrLine : ocrText) {
                System.out.println(ocrLine);
            }
        }
        System.out.println("Extracted table:");
        if (csvText != null) {
            for (BlueObject line : csvText) {
                System.out.println(line);
            }
        }
        System.out.println("Cheque:");
        if (cheque != null) {
            System.out.println(cheque);
        }
    }
}
