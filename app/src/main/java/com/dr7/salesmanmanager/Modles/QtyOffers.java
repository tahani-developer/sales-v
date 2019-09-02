package com.dr7.salesmanmanager.Modles;

public class QtyOffers {
    private double QTY;
    private double DiscountValue;
    private  int PaymentType;

    public QtyOffers() {
    }

    public QtyOffers(double QTY, double discountValue, int paymentType) {
        this.QTY = QTY;
        DiscountValue = discountValue;
        PaymentType = paymentType;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
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
