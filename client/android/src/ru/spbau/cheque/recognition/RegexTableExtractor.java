package ru.spbau.cheque.recognition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTableExtractor implements TableExtractor {
    public List<BlueObject> extract(ChequeFormat format, List<String> text) {
        List<BlueObject> entries = typeOneRegex(format, text);
        if (entries.size() == 0) {
            //try another regex set
            entries = typeTwoRegex(format, text);
        }

        return entries;
    }

    private List<BlueObject> typeOneRegex(ChequeFormat format, List<String> text) {
        List<BlueObject> entries = new ArrayList<BlueObject>();
        String nameAccum = "";
        for (String line : text) {
            Matcher matcher = Pattern.compile("^([\\p{Digit}\\p{Punct}]+)\\p{Blank}+(.*)", Pattern.UNICODE_CASE).matcher(line);
            if (matcher.matches()) {
                String f1 = matcher.group(1);
                String f2 = matcher.group(2);
                Matcher matcher2 = Pattern.compile("(.*)\\p{Blank}(.*)?+$", Pattern.UNICODE_CASE).matcher(f2);
                if (matcher2.matches()) {
                    String price = matcher2.group(2);
                    String price2 = price.replaceAll("^\\p{Punct}|\\p{Punct}$", "");
                    if (price2.matches("(\\p{Digit}+\\p{Punct}{1}\\p{Digit}+)")) {
                        entries.add(new BlueObject(nameAccum + matcher2.group(1),
                                Float.parseFloat(price2)));
                    } else {
                        entries.add(new BlueObject(nameAccum + matcher2.group(1), 0));
                    }
                }
            }
        }
        return entries;
    }

    private List<BlueObject> typeTwoRegex(ChequeFormat format, List<String> text) {
        List<BlueObject> entries = new ArrayList<BlueObject>();
        String nameAccum = "";
        for (String line : text) {
            //Matcher matcher = Pattern.compile("^\\p{Digit}+\\.([\\p{Alnum}\\p{Punct}]+)\\p{Blank}+
            // ([\\p{Alnum}\\p{Punct}]+)\\p{Blank}+(([\\p{Alnum}\\p{Punct}]+))").matcher(line);
            //"^\\p{Digit}+\\.(.*)\\p{Blank}+([\\p{Alnum}\\p{Punct}]+)$"
            Matcher matcher = Pattern.compile("^\\p{Digit}+\\.((.*)(\\p{Blank}+([\\p{Alnum}\\p{Punct}]+)))" +
                                              "\\p{Blank}+([\\p{Alnum}\\p{Punct}]+)$",
                                              Pattern.UNICODE_CASE).matcher(line);
            if (matcher.matches()) {
                String f1 = matcher.group(1);
                String f2 = matcher.group(2);   //name,count
                String f3 = matcher.group(5);   //price
                float countf = 0;
                float pricef = 0;
                String name = "";
                f3 = f3.replaceAll("^\\p{Punct}|\\p{Punct}$", "")
                        .replaceAll("^\\p{Alpha}|\\p{Alpha}$", "");
                Matcher matcher2 = Pattern.compile("(.*)\\p{Blank}(.*)?+$", Pattern.UNICODE_CASE).matcher(f2);
                if (matcher2.matches()) {
                    String count = matcher2.group(2).replaceAll("^\\p{Punct}|\\p{Punct}$", "")
                                                    .replaceAll("^\\p{Alpha}|\\p{Alpha}$", "");
                    name = matcher2.group(1);
                    if (count.matches("([\\p{Digit}\\p{Punct}]?\\p{Digit}+)")) {
                        countf = Float.parseFloat(count);
                    }
                    if (f3.matches("(\\p{Digit}+\\p{Punct}{1}\\p{Digit}+)")) {
                        pricef = Float.parseFloat(f3.replaceAll("\\p{Punct}", "."));
                    }
                    entries.add(new BlueObject(name, countf, pricef));
                }

            }
        }
        return entries;
    }
}
