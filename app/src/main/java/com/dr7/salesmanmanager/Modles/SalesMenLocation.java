package com.dr7.salesmanmanager.Modles;

public class SalesMenLocation {
   private String latitude;
   private String longitude;
   private String salesmanNo;
   private String salesmanName;

   public String getLatitude() {
      return latitude;
   }

   public void setLatitude(String latitude) {
      this.latitude = latitude;
   }

   public String getLongitude() {
      return longitude;
   }

   public void setLongitude(String longitude) {
      this.longitude = longitude;
   }

   public String getSalesmanNo() {
      return salesmanNo;
   }

   public void setSalesmanNo(String salesmanNo) {
      this.salesmanNo = salesmanNo;
   }

   public String getSalesmanName() {
      return salesmanName;
   }

   public void setSalesmanName(String salesmanName) {
      this.salesmanName = salesmanName;
   }
}
