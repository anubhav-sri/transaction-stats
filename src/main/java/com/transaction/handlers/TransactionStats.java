package com.transaction.handlers;

public class TransactionStats {
    private double sum;
    private double avg;
    private double max;
    private int count;

    public TransactionStats(double updateAvg, double updatedMax, double updatedSum, int updatedCount) {
        avg = updateAvg;
        max = updatedMax;
        sum = updatedSum;
        count = updatedCount;
    }

    public TransactionStats() {
        sum = 0;
        avg = 0;
        max = 0;
        count = 0;
    }


    public double getMax() {
        return max;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public int getCount() {
        return count;
    }
}
