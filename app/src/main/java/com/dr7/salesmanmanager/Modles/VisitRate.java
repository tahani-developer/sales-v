package com.dr7.salesmanmanager.Modles;

import android.graphics.Bitmap;

public class VisitRate {

    private int VisitPerpos;
    private int CustomerRegards;
    private int CheckStore;
    private int PromotionCheckStock;
    private int SpesifyProposedRequest;
    private int Persusion;
    private int VisitRate;
    private Bitmap visitpic;
    private String custCode;
    private String custName;
    private String salesman;

    public VisitRate() {
    }

    public VisitRate(int visitPerpos, int customerRegards, int checkStore, int promotionCheckStock, int spesifyProposedRequest,
                     int persusion, int visitRate, Bitmap visitpic, String custCode, String custName, String salesman) {

        this.VisitPerpos = visitPerpos;
        this.CustomerRegards = customerRegards;
        this.CheckStore = checkStore;
        this.PromotionCheckStock = promotionCheckStock;
        this.SpesifyProposedRequest = spesifyProposedRequest;
        this.Persusion = persusion;
        this.VisitRate = visitRate;
        this.visitpic = visitpic;
        this.custCode = custCode;
        this.custName = custName;
    }


    public int getVisitPerpos() {
        return VisitPerpos;
    }

    public void setVisitPerpos(int visitPerpos) {
        VisitPerpos = visitPerpos;
    }

    public int getCustomerRegards() {
        return CustomerRegards;
    }

    public void setCustomerRegards(int customerRegards) {
        CustomerRegards = customerRegards;
    }

    public int getCheckStore() {
        return CheckStore;
    }

    public void setCheckStore(int checkStore) {
        CheckStore = checkStore;
    }

    public int getPromotionCheckStock() {
        return PromotionCheckStock;
    }

    public void setPromotionCheckStock(int promotionCheckStock) {
        PromotionCheckStock = promotionCheckStock;
    }

    public int getSpesifyProposedRequest() {
        return SpesifyProposedRequest;
    }

    public void setSpesifyProposedRequest(int spesifyProposedRequest) {
        SpesifyProposedRequest = spesifyProposedRequest;
    }

    public int getPersusion() {
        return Persusion;
    }

    public void setPersusion(int persusion) {
        Persusion = persusion;
    }

    public int getVisitRate() {
        return VisitRate;
    }

    public void setVisitRate(int visitRate) {
        VisitRate = visitRate;
    }

    public Bitmap getVisitpic() {
        return visitpic;
    }

    public void setVisitpic(Bitmap visitpic) {
        this.visitpic = visitpic;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
}
