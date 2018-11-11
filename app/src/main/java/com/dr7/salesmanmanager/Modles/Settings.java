package com.dr7.salesmanmanager.Modles;

public class Settings {

    private String IpAddress;
    private int transactionType;
    private  int serial;
    private int taxClarcKind;
    private int priceByCust;
    private int useWeightCase;

    public Settings(){

    }

    public Settings(String ipAddress, int transactionType, int serial, int taxClarcKind, int priceByCust, int useWeightCase) {
        IpAddress = ipAddress;
        this.transactionType = transactionType;
        this.serial = serial;
        this.taxClarcKind = taxClarcKind;
        this.priceByCust = priceByCust;
        this.useWeightCase = useWeightCase;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getTaxClarcKind() {
        return taxClarcKind;
    }

    public void setTaxClarcKind(int taxClarcKind) {
        this.taxClarcKind = taxClarcKind;
    }

    public int getPriceByCust() {
        return priceByCust;
    }

    public void setPriceByCust(int priceByCust) {
        this.priceByCust = priceByCust;
    }

    public int getUseWeightCase() {
        return useWeightCase;
    }

    public void setUseWeightCase(int useWeightCase) {
        this.useWeightCase = useWeightCase;
    }
}
