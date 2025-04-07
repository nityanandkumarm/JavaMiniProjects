package Practice;

import java.util.ArrayList;
import java.util.List;

class Interval {
    int startTime, endTime, finalPrice;
    List<String> promoIds;

    public Interval(int startTime, int endTime, int finalPrice, List<String> promoIds) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.finalPrice = finalPrice;
        this.promoIds = new ArrayList<>(promoIds);
    }

    @Override
    public String toString() {
        return "[" + startTime + ", " + endTime + ", " + finalPrice + ", " + promoIds + "]";
    }
}