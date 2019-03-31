package com.dr7.salesmanmanager.Modles;

public class Settings {

    private String IpAddress;
    private int transactionType;
    private  int serial;
    private int taxClarcKind;
    private int priceByCust;
    private int useWeightCase;
    private int allowMinus;
    private int numOfCopy;
    private int salesManCustomers;
    private int minSalePric;
    private int printMethod;

    public Settings(){

    }

    public Settings(String ipAddress, int transactionType, int serial, int taxClarcKind, int priceByCust, int useWeightCase,
                    int allowMinus, int numOfCopy , int salesManCustomers , int minSalePric , int printMethod) {
        IpAddress = ipAddress;
        this.transactionType = transactionType;
        this.serial = serial;
        this.taxClarcKind = taxClarcKind;
        this.priceByCust = priceByCust;
        this.useWeightCase = useWeightCase;
        this.allowMinus = allowMinus;
        this.numOfCopy = numOfCopy;
        this.salesManCustomers = salesManCustomers;
        this.minSalePric = minSalePric;
        this.printMethod = printMethod;
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

    public int getAllowMinus() {
        return allowMinus;
    }

    public void setAllowMinus(int allowMinus) {
        this.allowMinus = allowMinus;
    }

    public int getNumOfCopy() {
        return numOfCopy;
    }

    public void setNumOfCopy(int numOfCopy) {
        this.numOfCopy = numOfCopy;
    }

    public int getSalesManCustomers() {
        return salesManCustomers;
    }

    public void setSalesManCustomers(int salesManCustomers) {
        this.salesManCustomers = salesManCustomers;
    }

    public int getMinSalePric() {
        return minSalePric;
    }

    public void setMinSalePric(int minSalePric) {
        this.minSalePric = minSalePric;
    }

    public int getPrintMethod() {
        return printMethod;
    }

    public void setPrintMethod(int printMethod) {
        this.printMethod = printMethod;
    }
}
