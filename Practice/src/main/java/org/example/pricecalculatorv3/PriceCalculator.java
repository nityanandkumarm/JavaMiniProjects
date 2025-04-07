package org.example.pricecalculatorv3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PriceCalculator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static List<Interval> computeFinalIntervals(Interval priceRecord, List<Promo> promos) {
        LocalDate prStart = LocalDate.parse(priceRecord.startTime, formatter);
        LocalDate prEndHalf = LocalDate.parse(priceRecord.endTime, formatter).plusDays(1);

        TreeSet<LocalDate> boundaries = new TreeSet<>(Arrays.asList(prStart, prEndHalf));
        
        promos.forEach(promo -> {
            LocalDate promoStart = LocalDate.parse(promo.startTime, formatter);
            LocalDate promoEndHalf = LocalDate.parse(promo.endTime, formatter).plusDays(1);
            boundaries.add(Collections.max(Arrays.asList(prStart, promoStart)));
            boundaries.add(Collections.min(Arrays.asList(prEndHalf, promoEndHalf)));
        });

        List<LocalDate> boundaryList = new ArrayList<>(boundaries);

        List<Interval> result = IntStream.range(0, boundaryList.size() - 1)
                .mapToObj(i -> {
                    LocalDate s = boundaryList.get(i);
                    LocalDate e = boundaryList.get(i + 1);
                    if (s.isBefore(prStart) || e.isAfter(prEndHalf)) return null;
                    List<Promo> applicablePromos = promos.stream()
                            .filter(promo -> {
                                LocalDate ps = LocalDate.parse(promo.startTime, formatter);
                                LocalDate peHalf = LocalDate.parse(promo.endTime, formatter).plusDays(1);
                                return !ps.isAfter(s) && !peHalf.isBefore(e);
                            })
                            .collect(Collectors.toList());
                    
                    int totalPromo = applicablePromos.stream().mapToInt(p -> p.amount).sum();
                    List<String> promoIds = applicablePromos.stream().map(p -> p.promoId).sorted().collect(Collectors.toList());
                    int finalPrice = priceRecord.finalPrice - totalPromo;
                    
                    return new Interval(s.format(formatter), e.minusDays(1).format(formatter), finalPrice, promoIds);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return mergeIntervals(result);
    }

    private static List<Interval> mergeIntervals(List<Interval> intervals) {
        return IntStream.range(0, intervals.size())
                .mapToObj(i -> {
                    if (i == 0) return intervals.get(i);
                    Interval prev = intervals.get(i - 1);
                    Interval curr = intervals.get(i);
                    if (prev.endTime.equals(curr.startTime) && prev.finalPrice == curr.finalPrice && prev.promoIds.equals(curr.promoIds)) {
                        return new Interval(prev.startTime, curr.endTime, prev.finalPrice, prev.promoIds);
                    }
                    return curr;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Interval priceRecord = new Interval("2024-10-03", "2024-12-13", 1000, new ArrayList<>());
        List<Promo> promos = Arrays.asList(
                new Promo("2024-10-10", "2025-01-20", 100, "p1"),
                new Promo("2024-10-10", "2024-12-01", 50, "p2")
        );

        computeFinalIntervals(priceRecord, promos).forEach(System.out::println);
    }
}
