package org.example.pricecalculator;

import java.util.ArrayList;
import java.util.List;

// **Data Classes**
public class Interval {
    int startTime, endTime, finalPrice;
    List<String> promoIds;

    public Interval(int startTime, int endTime, int finalPrice, List<String> promoIds) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.finalPrice = finalPrice;
        // **Store a copy of the promoIds list**
        this.promoIds = new ArrayList<>(promoIds);
    }

    @Override
    public String toString() {
        // **Output intervals in half‑open notation [start, end)**
        return "[" + startTime + ", " + endTime + "] -> Final Price: " + finalPrice + ", Promos: " + promoIds;
    }
}