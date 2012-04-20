package ru.spbau.cheque.server.recognition;

import java.util.ArrayList;
import java.util.List;

public class TableExtractor {
    public List<String> extractBadly(List<String> text) {
        List<String> csv = new ArrayList<String>();
        for (String line : text) {
            String field[] = line.split("([\\p{Digit}]) (.)* ([\\p{Alpha}\\p{Punct}])");

            String csvLine = new String();
            for (int i = 0; i < field.length; ++i) {
                csvLine += field[i] + ",";
            }
            csv.add(csvLine);
        }
        return csv;
    }
}
