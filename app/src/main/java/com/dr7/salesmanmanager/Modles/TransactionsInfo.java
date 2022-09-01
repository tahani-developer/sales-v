package com.dr7.salesmanmanager.Modles;

public class TransactionsInfo {
    String reson;
    String personname;
    String PhoneNum;
    String Cust_num;
    String Cust_name;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCust_num() {
        return Cust_num;
    }

    public void setCust_num(String cust_num) {
        Cust_num = cust_num;
    }

    public String getCust_name() {
        return Cust_name;
    }

    public void setCust_name(String cust_name) {
        Cust_name = cust_name;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }
}
