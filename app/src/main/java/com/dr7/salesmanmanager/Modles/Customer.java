package com.dr7.salesmanmanager.Modles;

import android.util.Log;

/**
 * Created by mohd darras on 15/04/2018.
 */

public class Customer {

    private String companyNumber;
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
    private String ACCPRC;// customer rate for price
    private int Hide_val;
    private int isPost;
    private  String customerIdText;
    private  String MaxD;

    private  String fax;
    private  String zipCode;
    private  String eMail;
    private  String c_THECATEG;

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getC_THECATEG() {
        return c_THECATEG;
    }

    public void setC_THECATEG(String c_THECATEG) {
        this.c_THECATEG = c_THECATEG;
    }

    public String getMaxD() {
        return MaxD;
    }

    public void setMaxD(String MaxD) {
        this.MaxD = MaxD;
    }

    public String getCustomerIdText() {
        return customerIdText;
    }

    public void setCustomerIdText(String customerIdText) {
        this.customerIdText = customerIdText;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getIsPost() {
        return isPost;
    }

    public void setIsPost(int isPost) {
        this.isPost = isPost;
    }

    public Customer(String custId, String custName, int payMethod) {
        this.custId = custId;
        this.custName = custName;
        this.payMethod=payMethod;
    }

    public int getHide_val() {
        return Hide_val;
    }

    public void setHide_val(int hide_val) {
        Hide_val = hide_val;
    }

    public Customer(String companyNumber, String custId, String custName, String address, int isSuspended,
                    String priceListId, int cashCredit, String salesManNumber, double creditLimit, int payMethod, String custLat, String custLong,
                    double max_discount, String ACCPRC, int hide_val, int isPost, String customerIdText) {
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
        this.ACCPRC = ACCPRC;
        Hide_val = hide_val;
        this.isPost = isPost;
        this.customerIdText = customerIdText;
    }

    private String customerAccount, customerName;

    public Customer() {

    }

    public String getACCPRC() {
        return ACCPRC;
    }

    public void setACCPRC(String ACCPRC) {
        this.ACCPRC = ACCPRC;
    }

    public Customer(String companyNumber, String custId, String custName, String address, int isSuspended, String priceListId,
                    int cashCredit, String salesManNumber, double creditLimit,
                    int payMethod, String custLat, String custLong, double max_discount, String ACCPRC, String customerAccount, String customerName) {
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
        this.ACCPRC = ACCPRC;
        this.customerAccount = customerAccount;
        this.customerName = customerName;
    }

    public Customer(String companyNumber, String custId, String custName, String address, int isSuspended, String priceListId,
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

//    public Customer(String custId, String custName) {
//        this.custId = custId;
//        this.custName = custName;
//        this.payMethod=pay;
//    }

    public String getCompanyNumber() {
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

    public void setCompanyNumber(String companyNumber) {
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
