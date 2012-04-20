package ru.spbau.cheque.server.recognition;

import java.util.List;

public interface TableExtractor {
    public List<String> extract(List<String> text);
}
