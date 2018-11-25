package com.dr7.salesmanmanager.Modles;

/**
 * Created by mohd darras on 15/04/2018.
 */

public class Customer {

    private int companyNumber ;
    private int custId ;
    private String custName ;
    private String address ;
    private int isSuspended ;
    private String priceListId ;
    private int cashCredit ;
    private String salesManNumber ;
    private double creditLimit ;


    private  String customerAccount, customerName;

    public Customer(){

    }

    public Customer(int companyNumber, int custId, String custName, String address, int isSuspended,
                    String priceListId, int cashCredit, String salesManNumber, double creditLimit,
                    String customerAccount, String customerName) {

        this.companyNumber = companyNumber;
        this.custId = custId;
        this.custName = custName;
        this.address = address;
        this.isSuspended = isSuspended;
        this.priceListId = priceListId;
        this.cashCredit = cashCredit;
        this.salesManNumber = salesManNumber;
        this.creditLimit = creditLimit;
        this.customerAccount = customerAccount;
        this.customerName = customerName;
    }

    public  Customer(int custId, String custName) {
        this.custId = custId;
        this.custName = custName;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(int isSuspended) {
        this.isSuspended = isSuspended;
    }

    public String getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(String priceListId) {
        this.priceListId = priceListId;
    }

    public int getCashCredit() {
        return cashCredit;
    }

    public void setCashCredit(int cashCredit) {
        this.cashCredit = cashCredit;
    }

    public String getSalesManNumber() {
        return salesManNumber;
    }

    public void setSalesManNumber(String salesManNumber) {
        this.salesManNumber = salesManNumber;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public  String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public   String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
