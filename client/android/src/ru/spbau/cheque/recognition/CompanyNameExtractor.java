package ru.spbau.cheque.recognition;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompanyNameExtractor {
    public String extract(ChequeFormat format, List<String> text) {
        for (String line : text) {
            Matcher m = Pattern.compile(".*(\\p{Alnum}+).*").matcher(line);
            if (m.matches()) {
                return line.replaceAll("^\\p{Punct}|\\p{Punct}$", "");
            }
        }
        return null;    //looks like cheque is invalid
    }
}
