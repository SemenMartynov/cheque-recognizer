package ru.spbau.cheque.server.ofx;

import java.util.Date;
/**
 * NetBeans
 *
 * @author sam
 */
public class ChequeString {
    public ChequeString(int id, Date date, String name, float quantity, float price, String memo) {
        this.id = id;
        this.operationTime = new java.text.SimpleDateFormat("yyyyMMdd").format(date);
        this.name = name;
        this.price = price * quantity;
        this.memo = memo;
    }
    private int id;
    private String operationTime;
    private String name;
    private float price;
    private String memo;
    
    public int getId() {
        return id;
    }
    
    public String getOperationTime() {
        return operationTime;
    }
    
    public String getName() {
        return name;
    }
    
    public float getPrice() {
        return price;
    }
    
    public String getMemo() {
        return memo;
    }
}
