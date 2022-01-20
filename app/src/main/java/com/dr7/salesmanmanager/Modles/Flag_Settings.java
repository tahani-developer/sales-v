package com.dr7.salesmanmanager.Modles;

public class Flag_Settings {

    private String Data_Type;
    private int Export_Stock;
    private int Max_Voucher;
    private int Make_Order;
    private int Admin_Password;
    private int Total_Balance;
    private int Voucher_Return;
    private int ActiveSlasmanPlan;
    public Flag_Settings(String data_Type, int export_Stock, int max_Voucher, int make_Order, int admin_Password, int total_Balance, int voucher_Return, int activeSlasmanPlan) {
        Data_Type = data_Type;
        Export_Stock = export_Stock;
        Max_Voucher = max_Voucher;
        Make_Order = make_Order;
        Admin_Password = admin_Password;
        Total_Balance = total_Balance;
        Voucher_Return = voucher_Return;
        ActiveSlasmanPlan = activeSlasmanPlan;
    }
    public Flag_Settings(String data_Type, int export_Stock, int max_Voucher, int make_Order, int admin_Password, int total_Balance, int voucher_Return) {
        Data_Type = data_Type;
        Export_Stock = export_Stock;
        Max_Voucher = max_Voucher;
        Make_Order = make_Order;
        Admin_Password = admin_Password;
        Total_Balance = total_Balance;
        Voucher_Return = voucher_Return;

    }

    public int getActiveSlasmanPlan() {
        return ActiveSlasmanPlan;
    }

    public void setActiveSlasmanPlan(int activeSlasmanPlan) {
        ActiveSlasmanPlan = activeSlasmanPlan;
    }

    public String getData_Type() {
        return Data_Type;
    }

    public void setData_Type(String data_Type) {
        Data_Type = data_Type;
    }

    public int getExport_Stock() {
        return Export_Stock;
    }

    public void setExport_Stock(int export_Stock) {
        Export_Stock = export_Stock;
    }

    public int getMax_Voucher() {
        return Max_Voucher;
    }

    public void setMax_Voucher(int max_Voucher) {
        Max_Voucher = max_Voucher;
    }

    public int getMake_Order() {
        return Make_Order;
    }

    public void setMake_Order(int make_Order) {
        Make_Order = make_Order;
    }

    public int getAdmin_Password() {
        return Admin_Password;
    }

    public void setAdmin_Password(int admin_Password) {
        Admin_Password = admin_Password;
    }

    public int getTotal_Balance() {
        return Total_Balance;
    }

    public void setTotal_Balance(int total_Balance) {
        Total_Balance = total_Balance;
    }

    public int getVoucher_Return() {
        return Voucher_Return;
    }

    public void setVoucher_Return(int voucher_Return) {
        Voucher_Return = voucher_Return;
    }

}
