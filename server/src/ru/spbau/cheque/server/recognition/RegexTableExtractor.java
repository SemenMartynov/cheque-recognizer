package ru.spbau.cheque.server.recognition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTableExtractor implements TableExtractor {
    public List<BlueObject> extract(ChequeFormat format, List<String> text) {
        List<BlueObject> entries = new ArrayList<BlueObject>();
        String nameAccum = "";
        for (String line : text) {
            BlueObject entry = null;
//            Matcher m = Pattern.compile("(.*)\\p{Blank}([\\p{Digit}\\p{Punct}]+)$").matcher(line);
//            if (m.matches()) {
//
//            }


            Matcher matcher = Pattern.compile("^([\\p{Digit}\\p{Punct}]+)\\p{Blank}+(.*)").matcher(line);
            if (matcher.matches()) {
                String f1 = matcher.group(1);
                String f2 = matcher.group(2);
                Matcher matcher2 = Pattern.compile("(.*)\\p{Blank}(.*)?+$").matcher(f2);  //(\p{all}*)$
                if (matcher2.matches()) {
                    String price = matcher2.group(2);
                    try {
                        entry = new BlueObject(nameAccum + matcher2.group(1),
                                               Float.parseFloat(price.replaceAll("^\\p{Punct}|\\p{Punct}$", "")));
                        entries.add(entry);
                        nameAccum = "";
                    } catch (NumberFormatException e) {
                        // suppress it - just don't add this to `entries`
                        // this is most probably caused by line-wrap - save to accumulator
                        nameAccum = matcher2.group(1) + price;
                    }
                    //entry += f1 + ";" + matcher2.group(1) + ";" + matcher2.group(2);
//                } else {
//                    entry += f1 + ";" + f2;
                }
            }

        }
        return entries;
    }
}
