package com.dr7.salesmanmanager.Modles;

public class UnCollect_Modell {
    private  String  AccCode;
    private  String  RECVD;
    private  String  PAIDAMT;

    public UnCollect_Modell() {
    }

    public String getAccCode() {
        return AccCode;
    }

    public void setAccCode(String accCode) {
        AccCode = accCode;
    }

    public String getRECVD() {
        return RECVD;
    }

    public void setRECVD(String RECVD) {
        this.RECVD = RECVD;
    }

    public String getPAIDAMT() {
        return PAIDAMT;
    }

    public void setPAIDAMT(String PAIDAMT) {
        this.PAIDAMT = PAIDAMT;
    }
}
