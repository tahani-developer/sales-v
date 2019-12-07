package com.dr7.salesmanmanager.Modles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dr7.salesmanmanager.R;

public class Account_Report {
     private String date;
    private String transfer_name;
    private String debtor;
    private String creditor;
    private String cust_balance;
    private  String cust_no;


    public Account_Report(String date, String transfer_name, String debtor, String creditor, String cust_balance, String cust_no) {
        this.date = date;
        this.transfer_name = transfer_name;
        this.debtor = debtor;
        this.creditor = creditor;
        this.cust_balance = cust_balance;
        this.cust_no = cust_no;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public Account_Report() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransfer_name() {
        return transfer_name;
    }

    public void setTransfer_name(String transfer_name) {
        this.transfer_name = transfer_name;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public String getCust_balance() {
        return cust_balance;
    }

    public void setCust_balance(String cust_balance) {
        this.cust_balance = cust_balance;
    }
}
