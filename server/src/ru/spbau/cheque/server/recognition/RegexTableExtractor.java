package ru.spbau.cheque.server.recognition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTableExtractor implements TableExtractor {
    public List<String> extract(List<String> text) {
        List<String> csv = new ArrayList<String>();
        for (String line : text) {
            String csvLine = new String();
            Matcher matcher = Pattern.compile("^([\\p{Digit}\\p{Punct}]+)\\p{Blank}+(.*)").matcher(line);
            if (matcher.matches()) {
                String f1 = matcher.group(1);
                String f2 = matcher.group(2);
                Matcher matcher2 = Pattern.compile("(.*)\\p{Blank}(.*)?+$").matcher(f2);  //(\p{all}*)$
                if (matcher2.matches()) {
                    csvLine += f1 + ";" + matcher2.group(1) + ";" + matcher2.group(2);
                } else {
                    csvLine += f1 + ";" + f2;
                }
            }
            csv.add(csvLine);
        }
        return csv;
    }
}
