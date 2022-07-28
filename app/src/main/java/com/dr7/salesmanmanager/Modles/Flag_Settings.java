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
    private int ActiveSlasmanTrips;
    private int pos_active;
    private int offerCakeShop;
    private int offerTalaat;
    private int offerQasion;
    private int maxvochServer;
    private int purchaseOrder;

    public int getNoTax() {
        return noTax;
    }

    public void setNoTax(int noTax) {
        this.noTax = noTax;
    }

    private int noTax;

    public int getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(int purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public int getActiveSlasmanTrips() {
        return ActiveSlasmanTrips;
    }

    public void setActiveSlasmanTrips(int activeSlasmanTrips) {
        ActiveSlasmanTrips = activeSlasmanTrips;
    }

    public int getMaxvochServer() {
        return maxvochServer;
    }

    public void setMaxvochServer(int maxvochServer) {
        this.maxvochServer = maxvochServer;
    }

    public int getPos_active() {
        return pos_active;
    }

    public void setPos_active(int pos_active) {
        this.pos_active = pos_active;
    }

//    public Flag_Settings(String data_Type, int export_Stock, int max_Voucher, int make_Order, int admin_Password, int total_Balance, int voucher_Return, int activeSlasmanPlan,int posAactive) {
//        Data_Type = data_Type;
//        Export_Stock = export_Stock;
//        Max_Voucher = max_Voucher;
//        Make_Order = make_Order;
//        Admin_Password = admin_Password;
//        Total_Balance = total_Balance;
//        Voucher_Return = voucher_Return;
//        ActiveSlasmanPlan = activeSlasmanPlan;
//        pos_active=posAactive;
//    }

    public Flag_Settings(String data_Type, int export_Stock, int max_Voucher, int make_Order, int admin_Password, int total_Balance,
                         int voucher_Return, int activeSlasmanPlan, int pos_active,
                         int offerCakeShop, int offerTalaat, int offerQasion,int SalsManTrip
            ,int maxVoServer, int purchase_Order,int no_tax) {
        Data_Type = data_Type;
        Export_Stock = export_Stock;
        Max_Voucher = max_Voucher;
        Make_Order = make_Order;
        Admin_Password = admin_Password;
        Total_Balance = total_Balance;
        Voucher_Return = voucher_Return;
        ActiveSlasmanPlan = activeSlasmanPlan;
        this.pos_active = pos_active;
        this.offerCakeShop = offerCakeShop;
        this.offerTalaat = offerTalaat;
        this.offerQasion = offerQasion;
        this.ActiveSlasmanTrips =   SalsManTrip;
        this.maxvochServer=maxVoServer;
        this.purchaseOrder=purchase_Order;
        this.noTax=no_tax;
    }

    public int getOfferCakeShop() {
        return offerCakeShop;
    }

    public void setOfferCakeShop(int offerCakeShop) {
        this.offerCakeShop = offerCakeShop;
    }

    public int getOfferTalaat() {
        return offerTalaat;
    }

    public void setOfferTalaat(int offerTalaat) {
        this.offerTalaat = offerTalaat;
    }

    public int getOfferQasion() {
        return offerQasion;
    }

    public void setOfferQasion(int offerQasion) {
        this.offerQasion = offerQasion;
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
