package com.transaction.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStats {
    private double sum;
    private double avg;
    private double max;
    private int count;

    public void add(Transaction transaction) {
        this.count++;
        this.sum+=transaction.getAmount();
        this.avg = sum/count;
        this.max = transaction.getAmount() > this.max ? transaction.getAmount() : this.max;
    }
}
