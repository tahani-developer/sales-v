package com.dr7.salesmanmanager.Modles;

public class CustomerLocation {
    private  String   CUS_NO;
    private  String   LONG ;
    private  String  LATIT ;

    public CustomerLocation() {
    }

    public String getCUS_NO() {
        return CUS_NO;
    }

    public void setCUS_NO(String CUS_NO) {
        this.CUS_NO = CUS_NO;
    }

    public String getLONG() {
        return LONG;
    }

    public void setLONG(String LONG) {
        this.LONG = LONG;
    }

    public String getLATIT() {
        return LATIT;
    }

    public void setLATIT(String LATIT) {
        this.LATIT = LATIT;
    }
}
