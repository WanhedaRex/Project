package com.example.project;

public class Simulation {
    private double amount;
    private int term;
    private boolean eligible;
    private int score;
    private String date;
    public Simulation(double amount, int term, boolean eligible, int score, String date) {
        this.amount = amount;
        this.term = term;
        this.eligible = eligible;
        this.score = score;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public int getTerm() {
        return term;
    }

    public boolean isEligible() {
        return eligible;
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }
}
