package com.example.project;
import java.io.Serializable;
public class User implements Serializable {
    private String name;
    private String rfc;
    private double income;
    public User(String name, String rfc, double income) {
        this.name = name;
        this.rfc = rfc;
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRFC() {
        return rfc;
    }

    public void setRFC(String rfc) {
        this.rfc = rfc;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}

