package com.dr7.salesmanmanager.Modles;

public class Offers {
    private int promotionID;
    private int promotionType;
    private String fromDate;
    private String toDate;
    private String itemNo;
    private double itemQty;
    private double bonusQty;
    private String bonusItemNo;

    public Offers(int promotionID, int promotionType, String fromDate, String toDate, String itemNo, double itemQty,
                  double bonusQty, String bonusItemNo) {
        this.promotionID = promotionID;
        this.promotionType = promotionType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.itemNo = itemNo;
        this.itemQty = itemQty;
        this.bonusQty = bonusQty;
        this.bonusItemNo = bonusItemNo;
    }

    public Offers() {

    }

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public double getItemQty() {
        return itemQty;
    }

    public void setItemQty(double itemQty) {
        this.itemQty = itemQty;
    }

    public double getBonusQty() {
        return bonusQty;
    }

    public void setBonusQty(double bonusQty) {
        this.bonusQty = bonusQty;
    }

    public String getBonusItemNo() {
        return bonusItemNo;
    }

    public void setBonusItemNo(String bonusItemNo) {
        this.bonusItemNo = bonusItemNo;
    }
}
