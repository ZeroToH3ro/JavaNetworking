package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Test {
    public static void main(String[] args) {
        BigDecimal averageFirst = new BigDecimal(1.5);
        BigDecimal averageSecond = new BigDecimal(2.5);
        BigDecimal averageThird = new BigDecimal(7.5);                   
        BigDecimal midterm = averageFirst.add(averageSecond).add(averageThird).divide(BigDecimal.valueOf(3.0), 2, RoundingMode.HALF_UP);
        System.out.printf("Score: %.2f", midterm);
    }
}
