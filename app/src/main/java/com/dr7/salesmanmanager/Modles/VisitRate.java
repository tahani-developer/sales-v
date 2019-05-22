package com.dr7.salesmanmanager.Modles;

import android.graphics.Bitmap;

import java.sql.Blob;

public class VisitRate {


    int VisitPerpos;
    int CustomerRegards;
    int CheckStore;
   int PromotionCheckStock;
    int SpesifyProposedRequest;
    int Persusion;
    int VisitRate;
    Bitmap visitpic;

    public VisitRate() {
    }

    public VisitRate( int visitPerpos, int customerRegards, int checkStore, int promotionCheckStock, int spesifyProposedRequest, int persusion, int visitRate, Bitmap visitpic) {

        VisitPerpos = visitPerpos;
        CustomerRegards = customerRegards;
        CheckStore = checkStore;
        PromotionCheckStock = promotionCheckStock;
        SpesifyProposedRequest = spesifyProposedRequest;
        Persusion = persusion;
        VisitRate = visitRate;
        this.visitpic = visitpic;
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
}
