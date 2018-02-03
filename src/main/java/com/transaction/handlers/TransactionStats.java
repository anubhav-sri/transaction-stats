package com.transaction.handlers;

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
}
