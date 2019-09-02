package com.dr7.salesmanmanager.Modles;

import android.util.Log;

/**
 * Created by mohd darras on 15/04/2018.
 */

public class Customer {

    private int companyNumber;
    private String custId;
    private String custName;
    private String address;
    private int isSuspended;
    private String priceListId;
    private int cashCredit;
    private String salesManNumber;
    private double creditLimit;
    private int payMethod;
    private String custLat;
    private String custLong;
    private double max_discount;


    private String customerAccount, customerName;

    public Customer() {

    }

    public Customer(int companyNumber, String custId, String custName, String address, int isSuspended, String priceListId,
                    int cashCredit, String salesManNumber, double creditLimit,
                    int payMethod, String custLat, String custLong, double max_discount, String customerAccount, String customerName) {
        this.companyNumber = companyNumber;
        this.custId = custId;
        this.custName = custName;
        this.address = address;
        this.isSuspended = isSuspended;
        this.priceListId = priceListId;
        this.cashCredit = cashCredit;
        this.salesManNumber = salesManNumber;
        this.creditLimit = creditLimit;
        this.payMethod = payMethod;
        this.custLat = custLat;
        this.custLong = custLong;
        this.max_discount = max_discount;
        this.customerAccount = customerAccount;
        this.customerName = customerName;
    }

    public Customer(String custId, String custName) {
        this.custId = custId;
        this.custName = custName;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public double getMax_discount() {
        return max_discount;
    }

    public void setMax_discount(double max_discount) {
      try {
          this.max_discount = max_discount;
      }
      catch (Exception e)
      {
          Log.e("ExceptionMaxDiscount",""+max_discount);
          max_discount=0;
      }
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
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

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public String getCustLat() {
        return custLat;
    }

    public void setCustLat(String custLat) {
        this.custLat = custLat;
    }

    public String getCustLong() {
        return custLong;
    }

    public void setCustLong(String custLong) {
        this.custLong = custLong;
    }
}
