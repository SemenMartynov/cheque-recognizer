package ru.spbau.cheque.server.reciever;

import ru.spbau.cheque.server.recognition.*;

import java.awt.image.BufferedImage;
import java.util.List;

public class Recognizer {
    public Recognizer() {
        this.engine = new TesseractOcrEngine("tesseract", "-l rus -psm 6");
        this.tableExtractor = new RegexTableExtractor();
        this.companyNameExtractor = new CompanyNameExtractor();
    }

    public Recognizer(OcrEngine engine) {
        if (engine == null) {
            throw new IllegalArgumentException();
        }

        this.engine = engine;
        this.tableExtractor = new RegexTableExtractor();
        this.companyNameExtractor = new CompanyNameExtractor();
    }


    public Cheque doRecognition(BufferedImage image,
                                int tableX, int tableY, int tableW, int tableH) throws OcrFailedException {
        List<String> ocredText = engine.doOcr(image);
        List<String> ocredAreaText = engine.doOcr(image.getSubimage(tableX, tableY, tableW, tableH));

        ChequeFormat format = determineFormat(ocredText);
        String companyName = companyNameExtractor.extract(format, ocredText);
        List<BlueObject> entries = tableExtractor.extract(format, ocredAreaText);

        return new Cheque(companyName, entries);
    }

    public Cheque doRecognition(BufferedImage image) throws OcrFailedException {
        return doRecognition(image, image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
    }

    public ChequeFormat determineFormat(List<String> text) {
        //stub
        return new ChequeFormat(companyNameExtractor.extract(new ChequeFormat(""), text));
    }

    private OcrEngine engine;
    private TableExtractor tableExtractor;
    private CompanyNameExtractor companyNameExtractor;
}
