package org.example.pricecalculatorv2;

class Promo {
    String startTime, endTime;
    int amount;
    String promoId;

    public Promo(String startTime, String endTime, int amount, String promoId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount = amount;
        this.promoId = promoId;
    }
}