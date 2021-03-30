package com.dr7.salesmanmanager.Modles;

/**
 * Created by mohd darras on 14/01/2018.
 */

public class Cheque {

    private String chequeNo, bankName, chequeDate;
    private int chequeSerial;
    private float chequeValue;

    public Cheque() {
    }

    public Cheque(String chequeNo, String bankName, String chequeDate, int chequeSerial, float chequeValue) {
        this.chequeNo = chequeNo;
        this.bankName = bankName;
        this.chequeDate = chequeDate;
        this.chequeSerial = chequeSerial;
        this.chequeValue = chequeValue;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public void setChequeSerial(int chequeSerial) {
        this.chequeSerial = chequeSerial;
    }

    public void setChequeValue(float chequeValue) {
        this.chequeValue = chequeValue;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public String getBankName() {
        return bankName;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public int getChequeSerial() {
        return chequeSerial;
    }

    public float getChequeValue() {
        return chequeValue;
    }
}
