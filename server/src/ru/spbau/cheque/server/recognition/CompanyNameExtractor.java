package ru.spbau.cheque.server.recognition;

import java.util.List;

public class CompanyNameExtractor {
    public String extract(ChequeFormat format, List<String> text) {
        return text.get(0);
    }
}
