package org.example.pricecalculatorv2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class PriceCalculator {

    // Define a formatter for the yyyy-MM-dd format.
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static List<Interval> computeFinalIntervals(Interval priceRecord, List<Promo> promos) {
        // Parse the priceRecord dates.
        LocalDate prStart = LocalDate.parse(priceRecord.startTime, formatter);
        LocalDate prEnd = LocalDate.parse(priceRecord.endTime, formatter);
        // Convert closed interval [prStart, prEnd] into half-open: [prStart, prEnd.plusDays(1))
        LocalDate prEndHalf = prEnd.plusDays(1);

        // Step 1: Collect unique boundary dates (in half-open form)
        TreeSet<LocalDate> boundaries = new TreeSet<>();
        boundaries.add(prStart);
        boundaries.add(prEndHalf);

        for (Promo promo : promos) {
            LocalDate promoStart = LocalDate.parse(promo.startTime, formatter);
            LocalDate promoEnd = LocalDate.parse(promo.endTime, formatter);
            // Convert promo closed interval [promoStart, promoEnd] to half-open [promoStart, promoEnd.plusDays(1))
            LocalDate promoEndHalf = promoEnd.plusDays(1);
            // Clip boundaries to be within priceRecord interval
            LocalDate clippedStart = promoStart.isBefore(prStart) ? prStart : promoStart;
            LocalDate clippedEnd = promoEndHalf.isAfter(prEndHalf) ? prEndHalf : promoEndHalf;
            boundaries.add(clippedStart);
            boundaries.add(clippedEnd);
        }

        List<LocalDate> boundaryList = new ArrayList<>(boundaries);
        List<Interval> result = new ArrayList<>();

        // Step 2: Create sub-intervals in half-open form [s, e)
        for (int i = 0; i < boundaryList.size() - 1; i++) {
            LocalDate s = boundaryList.get(i);
            LocalDate e = boundaryList.get(i + 1);
            // Only consider sub-intervals fully within the priceRecord half-open interval.
            if (s.isBefore(prStart) || e.isAfter(prEndHalf)) {
                continue;
            }
            // Step 3: Determine applicable promos for sub-interval [s, e)
            List<Promo> applicablePromos = new ArrayList<>();
            for (Promo promo : promos) {
                LocalDate promoStart = LocalDate.parse(promo.startTime, formatter);
                LocalDate promoEnd = LocalDate.parse(promo.endTime, formatter);
                LocalDate promoEndHalf = promoEnd.plusDays(1);
                // Clip the promo's half-open interval to priceRecord
                if (promoStart.isBefore(prStart)) {
                    promoStart = prStart;
                }
                if (promoEndHalf.isAfter(prEndHalf)) {
                    promoEndHalf = prEndHalf;
                }
                // A promo is applicable if it fully covers [s, e)
                if (!promoStart.isAfter(s) && !promoEndHalf.isBefore(e)) {
                    applicablePromos.add(promo);
                }
            }
            int totalPromo = applicablePromos.stream().mapToInt(p -> p.amount).sum();
            List<String> promoIds = new ArrayList<>();
            for (Promo promo : applicablePromos) {
                promoIds.add(promo.promoId);
            }
            Collections.sort(promoIds);
            int finalPrice = priceRecord.finalPrice - totalPromo;
            // Step 5: Convert half-open [s, e) back to a closed interval [s, e.minusDays(1)]
            String closedStart = s.format(formatter);
            // If s equals e then degenerate interval; else, subtract one day from e.
            String closedEnd = s.equals(e) ? s.format(formatter) : e.minusDays(1).format(formatter);
            result.add(new Interval(closedStart, closedEnd, finalPrice, promoIds));
        }

        // Step 4: Merge consecutive intervals if they have the same final price and identical promo sets.
        List<Interval> merged = new ArrayList<>();
        if (!result.isEmpty()) {
            Interval current = result.get(0);
            for (int i = 1; i < result.size(); i++) {
                Interval next = result.get(i);
                if (current.endTime.equals(next.startTime) &&
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

        return merged;
    }

    public static void main(String[] args) {

        Interval priceRecord = new Interval("2024-10-03", "2024-12-13", 1000, new ArrayList<>());
        List<Promo> promos = Arrays.asList(
                new Promo("2024-10-10", "2025-01-20", 100, "p1"),
                new Promo("2024-10-10", "2024-12-01", 50, "p2")
//                new Promo("2024-02-07", "2024-02-12", 3, "p3"),
//                new Promo("2024-02-10", "2024-02-15", 7, "p4") // Promo starts at 2024-02-10 and extends beyond
        );

        List<Interval> intervals = computeFinalIntervals(priceRecord, promos);
        for (Interval interval : intervals) {
            System.out.println(interval);
        }
    }
}




