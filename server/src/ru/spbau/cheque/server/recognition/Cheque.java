package ru.spbau.cheque.server.recognition;

import java.util.List;

public class Cheque {
    public Cheque(String company, List<BlueObject> table) {
        this.company = company;
        this.table = table;
    }

    private String company;
    private List<BlueObject> table;
}
