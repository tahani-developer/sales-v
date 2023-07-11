package com.dr7.salesmanmanager.Modles;

public class PrinterSetting {


    private int printerName;
    private int printerShape;
    private int shortInvoice;
    private int dontPrintHeader;
    private int tayeeLayout;
    private int netsalflag;
    private int dontrprintheadeInOrders;
    private int printItemNumber;

    public int getPrintItemNumber() {
        return printItemNumber;
    }

    public void setPrintItemNumber(int printItemNumber) {
        this.printItemNumber = printItemNumber;
    }

    public int getDontrprintheadeInOrders() {
        return dontrprintheadeInOrders;
    }

    public void setDontrprintheadeInOrders(int dontrprintheadeInOrders) {
        this.dontrprintheadeInOrders = dontrprintheadeInOrders;
    }

    public int getNetsalflag() {
        return netsalflag;
    }

    public void setNetsalflag(int netsalflag) {
        this.netsalflag = netsalflag;
    }

    public int getTayeeLayout() {
        return tayeeLayout;
    }

    public void setTayeeLayout(int tayeeLayout) {
        this.tayeeLayout = tayeeLayout;
    }

    public int getDontPrintHeader() {
        return dontPrintHeader;
    }

    public void setDontPrintHeader(int dontPrintHeader) {
        this.dontPrintHeader = dontPrintHeader;
    }

    public int getShortInvoice() {
        return shortInvoice;
    }

    public void setShortInvoice(int shortInvoice) {
        this.shortInvoice = shortInvoice;
    }

    public int getPrinterShape() {
        return printerShape;
    }

    public void setPrinterShape(int printerShape) {
        this.printerShape = printerShape;
    }

    public PrinterSetting(int printerName, int printerShape) {
        this.printerName = printerName;
        this.printerShape = printerShape;
    }

    public PrinterSetting() {

    }

    public int getPrinterName() {
        return printerName;
    }

    public void setPrinterName(int printerName) {
        this.printerName = printerName;
    }
}
