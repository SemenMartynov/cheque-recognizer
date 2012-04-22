package ru.spbau.cheque.recognition;

public class ChequeFingerprint {
    public ChequeFingerprint(int lineNumber, String regex) {
        if (regex == null) {
            throw new IllegalArgumentException();
        }
        this.lineNumber = lineNumber;
        this.regex = regex;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getRegex() {
        return regex;
    }

    private int lineNumber;
    private String regex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChequeFingerprint that = (ChequeFingerprint) o;

        if (lineNumber != that.lineNumber) return false;
        if (!regex.equals(that.regex)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lineNumber;
        result = 31 * result + regex.hashCode();
        return result;
    }
}
