package com.example.project;

public class AmortizationEntry {
    private int month;
    private String monthlyPayment;
    private String principal;
    private String interest;
    private String balance;

    public AmortizationEntry(int month, String monthlyPayment, String principal, String interest, String balance) {
        this.month = month;
        this.monthlyPayment = monthlyPayment;
        this.principal = principal;
        this.interest = interest;
        this.balance = balance;
    }

    public int getMonth() {
        return month;
    }

    public String getMonthlyPayment() {
        return monthlyPayment;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getInterest() {
        return interest;
    }

    public String getBalance() {
        return balance;
    }
}
