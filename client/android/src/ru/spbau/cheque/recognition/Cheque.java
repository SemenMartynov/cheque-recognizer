package ru.spbau.cheque.recognition;

import java.io.Serializable;
import java.util.List;

public class Cheque implements Serializable {
    public Cheque(String company, List<BlueObject> table) {
        this.company = company;
        this.table = table;
    }

    private String company;
    private List<BlueObject> table;
    private int userId = 1; // for authorization

    @Override
    public String toString() {
        return "Cheque{" +
                "company='" + company + '\'' +
                ", table=" + table +
                '}';
    }
    
    public String getCompany() {
        return company;
    }
    
    public List<BlueObject> getTable() {
        return table;
    }
    
    public int getUsrId() {
        return userId;
    }
}
