package com.dr7.salesmanmanager.Modles;

public class OfferListMaster {

    private int PO_LIST_NO ;
    private String PO_LIST_NAME ;
    private int PO_LIST_TYPE ;
    private String  FROM_DATE  ;
    private String  TO_DATE;

    public OfferListMaster() {
    }



    public int getPO_LIST_NO() {
        return PO_LIST_NO;
    }

    public void setPO_LIST_NO(int PO_LIST_NO) {
        this.PO_LIST_NO = PO_LIST_NO;
    }

    public String getPO_LIST_NAME() {
        return PO_LIST_NAME;
    }

    public void setPO_LIST_NAME(String PO_LIST_NAME) {
        this.PO_LIST_NAME = PO_LIST_NAME;
    }

    public int getPO_LIST_TYPE() {
        return PO_LIST_TYPE;
    }

    public void setPO_LIST_TYPE(int PO_LIST_TYPE) {
        this.PO_LIST_TYPE = PO_LIST_TYPE;
    }

    public String getFROM_DATE() {
        return FROM_DATE;
    }

    public void setFROM_DATE(String FROM_DATE) {
        this.FROM_DATE = FROM_DATE;
    }

    public String getTO_DATE() {
        return TO_DATE;
    }

    public void setTO_DATE(String TO_DATE) {
        this.TO_DATE = TO_DATE;
    }
}
