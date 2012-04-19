package ru.spbau.cheque.server.recognition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TesseractOcrEngine implements OcrEngine {
    public TesseractOcrEngine(String tesseractExe, String tesseractParameters) {
        this.tesseractExe = tesseractExe;
        this.tesseractParameters = tesseractParameters;
    }

    @Override
    public List<String> doOcr(BufferedImage image) throws OcrFailedException {
        if (image == null) {
            throw new IllegalArgumentException("Can't do OCR on null image");
        }

        try {
            File tmpImageFile = File.createTempFile("receipt_", ".jpg");
            ImageIO.write(image, "jpg", tmpImageFile);
            return doOcr(tmpImageFile.getCanonicalPath());
        } catch (IOException e) {
            throw new OcrFailedException(e);
        }
    }

    private List<String> doOcr(String imageName) throws OcrFailedException {
        if (imageName == null) {
            throw new IllegalArgumentException("Can't do OCR on an image with null name");
        }

        String outputNameBase = imageName + "_out";
        String outputName = outputNameBase + ".txt";    // file extension will be appended
                                                        // to the base name by tesseract executable

        String tesseractLauchLine = tesseractExe
                                    + " " + tesseractParameters
                                    + " " + imageName
                                    + " " + outputNameBase;

        try {
            Process p = Runtime.getRuntime().exec(tesseractLauchLine);
            System.out.println("Tesseract exit value: " + p.waitFor());
            //} catch (IOException | InterruptedException e) {
        } catch (IOException e) {
            throw new OcrFailedException(e);
        } catch (InterruptedException e) {
            throw new OcrFailedException(e);
        }

        //List<String> tesseractOutput = new ArrayList<>();
        List<String> tesseractOutput = new ArrayList<String>();
        File outputFile = new File(outputName);
        BufferedReader outputFileReader = null;
        //try (BufferedReader outputFileReader = new BufferedReader(new FileReader(outputFile))) {
        try {
            String str;
            outputFileReader = new BufferedReader(new FileReader(outputFile));
            while ((str = outputFileReader.readLine()) != null) {
                tesseractOutput.add(str);
            }
        } catch (IOException e) {
            throw new OcrFailedException("Can't read output file", e);
        } finally {
            try {
                if (outputFileReader != null) {
                    outputFileReader.close();
                }
                outputFile.delete();
            } catch (IOException e) {
                throw new OcrFailedException("Can't close output file reader", e);
            }
        }

        return tesseractOutput;
    }

    private String tesseractExe;
    private String tesseractParameters;
}
