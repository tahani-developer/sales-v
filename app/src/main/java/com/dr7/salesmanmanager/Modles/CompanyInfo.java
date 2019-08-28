package com.dr7.salesmanmanager.Modles;

import android.graphics.Bitmap;

public class CompanyInfo {
    
    private String companyName;
    private int companyTel;
    private int taxNo;
    private Bitmap logo;

    public CompanyInfo() {
    }

    public CompanyInfo(String companyName, int companyTel, int taxNo, Bitmap logo) {
        this.companyName = companyName;
        this.companyTel = companyTel;
        this.taxNo = taxNo;
        this.logo = logo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getcompanyTel() {
        return companyTel;
    }

    public void setcompanyTel(int companyTel) {
        this.companyTel = companyTel;
    }

    public int getTaxNo() {
        return taxNo;
    }

    public void setCompanyTel(int companyTel) {
        this.companyTel = companyTel;
    }

    public void setTaxNo(int taxNo) {
        this.taxNo = taxNo;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }
}
