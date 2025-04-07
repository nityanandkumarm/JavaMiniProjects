package Practice;

import java.util.*;

public class PriceCalculator {
    
    public static List<Interval> computeFinalIntervals(Interval priceRecord, List<Promo> promos) {
        // **Initialize boundaries with the price record's start and end times**
        Set<Integer> boundaries = new HashSet<>();
        boundaries.add(priceRecord.startTime);
        boundaries.add(priceRecord.endTime);

        // **Collect boundaries from each promo, clipping to the price record boundaries**
        for (Promo promo : promos) {
            int promoStart = Math.max(promo.startTime, priceRecord.startTime);
            int promoEnd = Math.min(promo.endTime, priceRecord.endTime);
            boundaries.add(promoStart);
            boundaries.add(promoEnd);
        }

        // **Sort all boundaries in ascending order**
        List<Integer> sortedBoundaries = new ArrayList<>(boundaries);
        Collections.sort(sortedBoundaries);

        List<Interval> result = new ArrayList<>();

        // **Iterate over each adjacent pair to form sub‑intervals**
        for (int i = 0; i < sortedBoundaries.size() - 1; i++) {
            int s = sortedBoundaries.get(i);
            int e = sortedBoundaries.get(i + 1);
            if (s == e) continue; // **Skip zero-length intervals**

            // **Determine applicable promos for the sub‑interval**
            List<Promo> applicablePromos = new ArrayList<>();
            for (Promo promo : promos) {
                if (promo.startTime <= s && promo.endTime >= e) {
                    applicablePromos.add(promo);
                }
            }

            // **Compute total promo discount and collect promo IDs**
            int totalPromo = applicablePromos.stream().mapToInt(p -> p.amount).sum();
            List<String> promoIds = new ArrayList<>();
            for (Promo promo : applicablePromos) {
                promoIds.add(promo.promoId);
            }
            Collections.sort(promoIds); // **Sort for consistent ordering**

            int finalPrice = priceRecord.finalPrice - totalPromo;
            result.add(new Interval(s, e, finalPrice, promoIds));
        }

        // **Merge consecutive intervals if they have the same final price and same set of applicable promoIds**
        List<Interval> merged = new ArrayList<>();
        if (!result.isEmpty()) {
            Interval current = result.get(0);
            for (int i = 1; i < result.size(); i++) {
                Interval next = result.get(i);
                if (current.endTime == next.startTime && current.finalPrice == next.finalPrice && current.promoIds.equals(next.promoIds)) {
                    // **Merge by extending the current interval's end time**
                    current = new Interval(current.startTime, next.endTime, current.finalPrice, current.promoIds);
                } else {
                    merged.add(current);
                    current = next;
                }
            }
            merged.add(current);
        }
        return merged;
    }

    public static void main(String[] args) {
        Interval priceRecord = new Interval(1, 10, 100, new ArrayList<>());
        List<Promo> promos = Arrays.asList(
                new Promo(0, 5, 10, "p1"),
                new Promo(3, 8, 5, "p2"),
                new Promo(7, 12, 3, "p3")
        );

        List<Interval> intervals = computeFinalIntervals(priceRecord, promos);
        for (Interval interval : intervals) {
            System.out.println(interval);
        }
    }
}

// **Data Classes**



