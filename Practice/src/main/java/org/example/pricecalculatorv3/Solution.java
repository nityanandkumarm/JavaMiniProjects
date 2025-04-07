package org.example.pricecalculatorv3;

import java.util.*;

public class Solution {
    static final int MOD = 1_000_000_007;

    public static List<Integer> arraysCount(List<Integer> n, List<Integer> m, List<Integer> totalCost) {
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < n.size(); i++) {
            results.add(countArrays(n.get(i), m.get(i), totalCost.get(i)));
        }
        return results;
    }

    private static int countArrays(int n, int m, int totalCost) {
        int[][] dp = new int[m + 1][totalCost + 1];

        for (int j = 1; j <= m; j++) dp[j][1] = 1;

        for (int i = 2; i <= n; i++) {
            int[][] newDp = new int[m + 1][totalCost + 1];
            for (int j = 1; j <= m; j++) {
                for (int k = 1; k <= totalCost; k++) {
                    newDp[j][k] = (int) ((dp[j][k] * (long) j) % MOD);
                    if (j > 1)
                        newDp[j][k] = (newDp[j][k] + (int) ((dp[j - 1][k - 1] * (long) (m - j + 1)) % MOD)) % MOD;
                }
            }
            dp = newDp;
        }

        int result = 0;
        for (int j = 1; j <= m; j++) result = (result + dp[j][totalCost]) % MOD;
        return result;
    }

    public static void main(String[] args) {
        List<Integer> n = Arrays.asList(2, 3, 3);
        List<Integer> m = Arrays.asList(3, 3, 3);
        List<Integer> totalCost = Arrays.asList(1, 2, 2);

        List<Integer> expected = Arrays.asList(3, 1, 6);
        List<Integer> output = arraysCount(n, m, totalCost);

        System.out.println("Output: " + output);
        System.out.println("Test " + (output.equals(expected) ? "Passed" : "Failed"));
    }
}
