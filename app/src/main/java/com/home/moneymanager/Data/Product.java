package com.home.moneymanager.Data;

public class Product {
    private String name, cost, time;

    public Product() {}

    public Product(String name, String cost, String time) {
        this.name = name;
        this.cost = cost;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
