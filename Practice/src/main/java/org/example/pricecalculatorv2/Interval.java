package org.example.pricecalculatorv2;

import java.util.ArrayList;
import java.util.List;

// Data Classes
class Interval {
    String startTime, endTime;
    int finalPrice;
    List<String> promoIds;

    public Interval(String startTime, String endTime, int finalPrice, List<String> promoIds) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.finalPrice = finalPrice;
        this.promoIds = new ArrayList<>(promoIds);
    }

    @Override
    public String toString() {
        return "[" + startTime + ", " + endTime + "] -> Final Price: " + finalPrice + ", Promos: " + promoIds;
    }
}