package ru.spbau.cheque.server.recognition;

import java.io.Serializable;
import java.util.List;

public class Cheque implements Serializable {
    public Cheque(String company, List<BlueObject> table) {
        this.company = company;
        this.table = table;
    }

    private String company;
    private List<BlueObject> table;

    @Override
    public String toString() {
        return "Cheque{" +
                "company='" + company + '\'' +
                ", table=" + table +
                '}';
    }
}
