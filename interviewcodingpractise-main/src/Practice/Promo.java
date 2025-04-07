package Practice;

class Promo {
    int startTime, endTime, amount;
    String promoId;

    public Promo(int startTime, int endTime, int amount, String promoId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount = amount;
        this.promoId = promoId;
    }
}