package com.dr7.salesmanmanager.Modles;

public class Account__Statment_Model {
    private String voucherNo;
    private String transeNmae;
    private String date_voucher;
    private  double debit;
    private  double credit;
    private  double balance;
    private String customerNo;

    public Account__Statment_Model() {
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getTranseNmae() {
        return transeNmae;
    }

    public void setTranseNmae(String transeNmae) {
        this.transeNmae = transeNmae;
    }

    public String getDate_voucher() {
        return date_voucher;
    }

    public void setDate_voucher(String date_voucher) {
        this.date_voucher = date_voucher;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
}
