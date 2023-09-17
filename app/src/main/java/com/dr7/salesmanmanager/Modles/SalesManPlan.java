package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import com.dr7.salesmanmanager.MainActivity;

import java.util.Comparator;

public class SalesManPlan  implements Comparable {

      private String custName ;
    private String custNumber ;
    private  String Date;
    private int saleManNumber;
    private  double latitud;
    private  double longtude;

    private int order;
    private int typeOrder;

    private int logoutStatus;

    private int serial;
    private String startNote;
    private String endNote;

    public String getStartNote() {
        return startNote;
    }

    public void setStartNote(String startNote) {
        this.startNote = startNote;
    }

    public String getEndNote() {
        return endNote;
    }

    public void setEndNote(String endNote) {
        this.endNote = endNote;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getSaleManNumber() {
        return saleManNumber;
    }

    public void setSaleManNumber(int saleManNumber) {
        this.saleManNumber = saleManNumber;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongtude() {
        return longtude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getTypeOrder() {
        return typeOrder;
    }
   double  distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setTypeOrder(int typeOrder) {
        this.typeOrder = typeOrder;
    }

    public int getLogoutStatus() {
        return logoutStatus;
    }

    public void setLogoutStatus(int logoutStatus) {
        this.logoutStatus = logoutStatus;
    }

   @Override
    public int compareTo(Object o) {
        Log.e("SalesManPlan===",o.toString());
        int compareorder=((SalesManPlan)o).getOrder();
        return this.order-compareorder;

    }




 /* @Override
    public int compareTo(Object o) {
        if (MainActivity.OrderTypeFlage == 0) {
            Log.e("SalesManPlan===", o.toString());
            int compareorder = ((SalesManPlan) o).getOrder();
            return this.order - compareorder;
        } else {
            Log.e("SalesManPlan===", o.toString());
           double compareorder =((SalesManPlan) o).getDistance();
           int c1=Integer.parseInt(String.valueOf(this.distance));
            int c2=Integer.parseInt(String.valueOf(compareorder));

          return  c1-c2;
        }

    }*/




}
