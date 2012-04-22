package ru.spbau.cheque.recognition;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChequeFormatLibrary {
    public ChequeFormatLibrary() {
        this.fingerprintToFormat = fingerprintToFormat;
    }

    public void registerChequeFormat(ChequeFingerprint fingerprint, ChequeFormat format) {
        lineNumberToFingerprint.get(fingerprint.getLineNumber()).add(fingerprint);
        fingerprintToFormat.put(fingerprint, format);
    }

    public ChequeFormat chequeFormat(ChequeFingerprint fingerprint) {
        return fingerprintToFormat.get(fingerprint);
    }

    public ChequeFormat chequeFormat(int lineNumber, String line) {
        List<ChequeFingerprint> fingerprints = lineNumberToFingerprint.get(lineNumber);
        for (ChequeFingerprint fingerprint : fingerprints) {
            Matcher m = Pattern.compile(fingerprint.getRegex()).matcher(line);
            if (m.matches()) {
                return fingerprintToFormat.get(fingerprint);
            }
        }
        return null;
    }

    private Map<Integer, List<ChequeFingerprint>> lineNumberToFingerprint;
    private Map<ChequeFingerprint, ChequeFormat> fingerprintToFormat;
}
