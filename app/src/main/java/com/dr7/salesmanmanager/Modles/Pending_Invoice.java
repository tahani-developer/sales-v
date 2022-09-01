package com.dr7.salesmanmanager.Modles;

public class Pending_Invoice {
     private  String VHFI;
     private  String TRANSKIND;
     private  String VANCODE;
     private  String TRDATE;
     private  String NETTOT;

     public Pending_Invoice() {
     }

     public String getVHFI() {
          return VHFI;
     }

     public void setVHFI(String VHFI) {
          this.VHFI = VHFI;
     }

     public String getTRANSKIND() {
          return TRANSKIND;
     }

     public void setTRANSKIND(String TRANSKIND) {
          this.TRANSKIND = TRANSKIND;
     }

     public String getVANCODE() {
          return VANCODE;
     }

     public void setVANCODE(String VANCODE) {
          this.VANCODE = VANCODE;
     }

     public String getTRDATE() {
          return TRDATE;
     }

     public void setTRDATE(String TRDATE) {
          this.TRDATE = TRDATE;
     }

     public String getNETTOT() {
          return NETTOT;
     }

     public void setNETTOT(String NETTOT) {
          this.NETTOT = NETTOT;
     }
}
