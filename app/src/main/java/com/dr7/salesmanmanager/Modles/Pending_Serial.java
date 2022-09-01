package com.dr7.salesmanmanager.Modles;

public class Pending_Serial {
    private  String SERIALCODE;
    private  String TRNSKIND;
    private  String TRNSDATE;
    private  String VHFNO;
    private  String STORENO;

    public Pending_Serial() {
    }

    public String getSERIALCODE() {
        return SERIALCODE;
    }

    public void setSERIALCODE(String SERIALCODE) {
        this.SERIALCODE = SERIALCODE;
    }

    public String getTRNSKIND() {
        return TRNSKIND;
    }

    public void setTRNSKIND(String TRNSKIND) {
        this.TRNSKIND = TRNSKIND;
    }

    public String getTRNSDATE() {
        return TRNSDATE;
    }

    public void setTRNSDATE(String TRNSDATE) {
        this.TRNSDATE = TRNSDATE;
    }

    public String getVHFNO() {
        return VHFNO;
    }

    public void setVHFNO(String VHFNO) {
        this.VHFNO = VHFNO;
    }

    public String getSTORENO() {
        return STORENO;
    }

    public void setSTORENO(String STORENO) {
        this.STORENO = STORENO;
    }
}
