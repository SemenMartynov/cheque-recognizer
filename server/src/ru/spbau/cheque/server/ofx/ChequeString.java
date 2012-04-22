package ru.spbau.cheque.server.ofx;

import java.util.Date;
/**
 * NetBeans
 *
 * @author sam
 */
public class ChequeString {
    public ChequeString(int id, String date, String name, float quantity, float price, String memo) {
        this.id = id;
        this.operationTime = date.replace("-", "").substring(0, 8);
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
    
    public String getPrice() {
        return Float.toString(price).replace(".", ",");
    }
    
    public String getMemo() {
        return memo;
    }
}
