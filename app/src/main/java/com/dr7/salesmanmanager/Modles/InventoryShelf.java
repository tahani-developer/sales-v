package com.dr7.salesmanmanager.Modles;

public class InventoryShelf {
  private String ITEM_NO;
    private String SERIAL_NO;
    private int  QTY_ITEM;
    private String TRANS_DATE;
    private String CUSTOMER_NO;
    private String  SALESMAN_NUMBER;
    private  int voucherNo;

    public int getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(int voucherNo) {
        this.voucherNo = voucherNo;
    }

    public InventoryShelf() {
    }

    public String getITEM_NO() {
        return ITEM_NO;
    }

    public void setITEM_NO(String ITEM_NO) {
        this.ITEM_NO = ITEM_NO;
    }

    public String getSERIAL_NO() {
        return SERIAL_NO;
    }

    public void setSERIAL_NO(String SERIAL_NO) {
        this.SERIAL_NO = SERIAL_NO;
    }

    public int getQTY_ITEM() {
        return QTY_ITEM;
    }

    public void setQTY_ITEM(int QTY_ITEM) {
        this.QTY_ITEM = QTY_ITEM;
    }

    public String getTRANS_DATE() {
        return TRANS_DATE;
    }

    public void setTRANS_DATE(String TRANS_DATE) {
        this.TRANS_DATE = TRANS_DATE;
    }

    public String getCUSTOMER_NO() {
        return CUSTOMER_NO;
    }

    public void setCUSTOMER_NO(String CUSTOMER_NO) {
        this.CUSTOMER_NO = CUSTOMER_NO;
    }

    public String getSALESMAN_NUMBER() {
        return SALESMAN_NUMBER;
    }

    public void setSALESMAN_NUMBER(String SALESMAN_NUMBER) {
        this.SALESMAN_NUMBER = SALESMAN_NUMBER;
    }
}
