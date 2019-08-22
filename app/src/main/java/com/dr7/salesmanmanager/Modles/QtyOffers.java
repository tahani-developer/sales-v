package com.dr7.salesmanmanager.Modles;

public class QtyOffers {
    private double QTY;
    private double DiscountValue;

    public QtyOffers() {
    }

    public QtyOffers(double QTY, double discountValue) {
        this.QTY = QTY;
        DiscountValue = discountValue;
    }

    public double getQTY() {
        return QTY;
    }

    public void setQTY(double QTY) {
        this.QTY = QTY;
    }

    public double getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(double discountValue) {
        DiscountValue = discountValue;
    }
}
