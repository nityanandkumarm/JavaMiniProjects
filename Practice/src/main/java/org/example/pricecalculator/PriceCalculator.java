package org.example.pricecalculator;

import java.util.*;

class PriceCalculator {

    public static List<Interval> computeFinalIntervals(Interval priceRecord, List<Promo> promos) {
        // Convert priceRecord to a half-open interval [start, end+1)
        int prStart = priceRecord.startTime;
        int prEnd = priceRecord.endTime + 1; // Convert to half-open

        // Step 1: Collect unique boundary points
        TreeSet<Integer> boundaries = new TreeSet<>();
        boundaries.add(prStart);
        boundaries.add(prEnd);

        for (Promo promo : promos) {
            int promoStart = promo.startTime;
            int promoEnd = promo.endTime + 1; // Convert promo to half-open
            int clippedStart = Math.max(promoStart, prStart);
            int clippedEnd = Math.min(promoEnd, prEnd);
            boundaries.add(clippedStart);
            boundaries.add(clippedEnd);
        }

        List<Integer> boundaryList = new ArrayList<>(boundaries);
        List<Interval> result = new ArrayList<>();

        // Step 2: Create sub-intervals
        for (int i = 0; i < boundaryList.size() - 1; i++) {
            int s = boundaryList.get(i);
            int e = boundaryList.get(i + 1);

            // Step 3: Determine applicable promos
            List<Promo> applicablePromos = new ArrayList<>();
            for (Promo promo : promos) {
                int clippedStart = Math.max(promo.startTime, prStart);
                int clippedEnd = Math.min(promo.endTime + 1, prEnd);
                if (clippedStart <= s && clippedEnd >= e) {
                    applicablePromos.add(promo);
                }
            }

            int totalPromo = applicablePromos.stream().mapToInt(p -> p.amount).sum();
            List<String> promoIds = new ArrayList<>();
            for (Promo promo : applicablePromos) {
                promoIds.add(promo.promoId);
            }
            Collections.sort(promoIds); // Ensure consistent order

            int finalPrice = priceRecord.finalPrice - totalPromo;
            result.add(new Interval(s, e, finalPrice, promoIds));
        }

        // Step 4: Merge consecutive intervals with the same price and promo set
        List<Interval> merged = new ArrayList<>();
        if (!result.isEmpty()) {
            Interval current = result.get(0);
            for (int i = 1; i < result.size(); i++) {
                Interval next = result.get(i);
                if (current.endTime == next.startTime &&
                        current.finalPrice == next.finalPrice &&
                        current.promoIds.equals(next.promoIds)) {
                    current = new Interval(current.startTime, next.endTime, current.finalPrice, current.promoIds);
                } else {
                    merged.add(current);
                    current = next;
                }
            }
            merged.add(current);
        }

        // Step 5: Convert back to closed intervals before returning
        List<Interval> finalIntervals = new ArrayList<>();
        for (Interval interval : merged) {
            int closedEnd = (interval.endTime > interval.startTime) ? interval.endTime - 1 : interval.startTime;
            finalIntervals.add(new Interval(interval.startTime, closedEnd, interval.finalPrice, interval.promoIds));
        }

        return finalIntervals;
    }

    public static void main(String[] args) {
        Interval priceRecord = new Interval(1, 10, 100, new ArrayList<>());
        List<Promo> promos = Arrays.asList(
                new Promo(0, 5, 10, "p1"),
                new Promo(3, 8, 5, "p2"),
                new Promo(7, 12, 3, "p3"),
                new Promo(10, 15, 7, "p4") // Promo starts at 10 and extends beyond the price grid
        );

        List<Interval> intervals = computeFinalIntervals(priceRecord, promos);
        for (Interval interval : intervals) {
            System.out.println(interval);
        }
    }
}


