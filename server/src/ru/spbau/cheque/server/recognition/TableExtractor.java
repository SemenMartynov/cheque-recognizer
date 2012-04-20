package ru.spbau.cheque.server.recognition;

import java.util.List;

public interface TableExtractor {
    public List<BlueObject> extract(ChequeFormat format, List<String> text);
}
