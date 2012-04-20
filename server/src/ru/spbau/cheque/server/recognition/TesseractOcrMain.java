package ru.spbau.cheque.server.recognition;

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
        List<String> csvText = null;
        try {
            ocrText = ocr.doOcr(image);
            TableExtractor extractor = new RegexTableExtractor();
            csvText = extractor.extract(ocrText);
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
            for (String line : csvText) {
                System.out.println(line);
            }
        }
    }
}
