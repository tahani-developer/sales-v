package com.dr7.salesmanmanager.Modles;

public class SalesTeam {

    private int companyNo;
    private String salesManNo;
    private String salesManName;
    private String isSuspended;
    private String ipAddressDevice;

    public String getIpAddressDevice() {
        return ipAddressDevice;
    }

    public void setIpAddressDevice(String ipAddressDevice) {
        this.ipAddressDevice = ipAddressDevice;
    }

    public SalesTeam(){

    }

    public SalesTeam(int companyNo, String salesManNo, String salesManName, String isSuspended) {
        this.companyNo = companyNo;
        this.salesManNo = salesManNo;
        this.salesManName = salesManName;
        this.isSuspended = isSuspended;
    }

    public int getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(int companyNo) {
        this.companyNo = companyNo;
    }

    public String getSalesManNo() {
        return salesManNo;
    }

    public void setSalesManNo(String salesManNo) {
        this.salesManNo = salesManNo;
    }

    public String getSalesManName() {
        return salesManName;
    }

    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    public String getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(String isSuspended) {
        this.isSuspended = isSuspended;
    }
}
