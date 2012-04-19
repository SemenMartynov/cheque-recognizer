package ru.spbau.cheque.server.recognition;

import java.awt.image.BufferedImage;
import java.util.List;

public interface OcrEngine {
    public List<String> doOcr(BufferedImage image) throws OcrFailedException;
}
