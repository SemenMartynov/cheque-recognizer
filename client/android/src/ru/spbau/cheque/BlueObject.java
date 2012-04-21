package ru.spbau.cheque;

public class BlueObject {
    public BlueObject(String name, float price) {
        this.name = name;
        this.count = 1;
        this.price = price;
    }
    public BlueObject(String name, float count, float price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BlueObject{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    private String name;
    private float count;
    private float price;
}
