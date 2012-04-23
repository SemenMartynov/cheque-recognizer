package ru.spbau.cheque.server.recognition;

import java.util.ArrayList;
import java.util.List;

public class ColumnDetectionTableExtractor implements TableExtractor {
    @Override
    public List<BlueObject> extract(ChequeFormat format, List<String> text) {
        int maxLineLength = 0;
        for (String line : text) {
            maxLineLength = Math.max(maxLineLength, line.length());
        }

        int blankgram[] = new int[maxLineLength];
        for (int i = 0; i < maxLineLength; ++i) {
            for (String line : text) {
                if ((i >= line.length() && line.length() < maxLineLength)
                        || new String(line.charAt(i) + "").matches("\\p{Blank}")) {
                    blankgram[i] += 1;
                }
            }
        }

        int borderThreshold = (int) (text.size() * 0.7);
        List<Integer> borders = new ArrayList<Integer>();
        for (int i = 0; i < maxLineLength; ++i) {
            if (blankgram[i] >= borderThreshold) {
                borders.add(i);
            }
        }

        int prevBorder = 0;
        List<List<String>> columnList = new ArrayList<List<String>>();
        for (int border : borders) {
            if (prevBorder == border || prevBorder == (border - 1)) {
                prevBorder = border;
            } else {
                List<String> column = new ArrayList<String>();
                for (String line : text) {
                    if (prevBorder >= line.length()) {
                        column.add("");
                    } else if (border >= line.length()) {
                        column.add(line.substring(prevBorder, line.length()));
                    } else {
                        column.add(line.substring(prevBorder, border));
                    }
                }
                columnList.add(column);
                prevBorder = border;
            }
        }


        for (List<String> column : columnList) {
            List<BlueObject> t = new ArrayList<BlueObject>();
            for (String columnLine : column) {
                System.out.println(columnLine);
            }
        }

        List<BlueObject> blueObjects = new ArrayList<BlueObject>();
        return blueObjects;
    }
}
