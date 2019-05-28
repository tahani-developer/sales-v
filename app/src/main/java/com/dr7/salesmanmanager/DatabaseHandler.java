package com.dr7.salesmanmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.SalesManAndStoreLink;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.VisitRate;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Reports.SalesMan;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class

DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 31;

    // Database Name
    private static final String DATABASE_NAME = "VanSalesDatabase";
    static SQLiteDatabase db;


    // tables from JSON
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String VISIT_RATE="VISIT_RATE";

    private static final String VISIT_PERPOS ="VISIT_PERPOS";
    private static final String CUSTOMER_REGARDS ="CUSTOMER_REGARDS";
    private static final String CHECK_STORE ="CHECK_STORE";
    private static final String PROMOTION_CHECK_STOCK ="PROMOTION_CHECK_STOCK";
    private static final String SPESIFY_PROPOSED_REQUEST ="SPESIFY_PROPOSED_REQUEST";
    private static final String PERSUSION ="PERSUSION";
    private static final String VISIT_RATE1 = "VISIT_RATE";
    private static final String VISIT_PIC ="VISIT_PIC";
    private static final String CUST_CODE_ ="CUST_CODE";
    private static final String CUST_NAME_ ="CUST_NAME";
    private static final String SALESMAN_="SALESMAN";

    //__________________________________________________________________________
    private static final String CUSTOMER_MASTER = "CUSTOMER_MASTER";

    private static final String COMPANY_NUMBER0 = "COMPANY_NUMBER0";
    private static final String CUS_ID = "CUS_ID";
    private static final String CUS_NAME0 = "CUS_NAME0";
    private static final String ADDRESS = "ADDRESS";
    private static final String IS_SUSPENDED = "IS_SUSPENDED";
    private static final String PRICE_LIST_ID = "PRICE_LIST_ID";
    private static final String CASH_CREDIT = "CASH_CREDIT";
    private static final String SALES_MAN_NO = "SALES_MAN_NO";
    private static final String CREDIT_LIMIT = "CREDIT_LIMIT";
    private static final String PAY_METHOD0 = "PAY_METHOD";
    private static final String CUST_LAT = "CUST_LAT";
    private static final String CUST_LONG = "CUST_LONG";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String Item_Unit_Details = "Item_Unit_Details";

    private static final String ComapnyNo = "ComapnyNo";
    private static final String ItemNo = "ItemNo";
    private static final String UnitID = "UnitID";
    private static final String ConvRate = "ConvRate";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String Items_Master = "Items_Master";

    private static final String ComapnyNo1 = "ComapnyNo";
    private static final String ItemNo1 = "ItemNo";
    private static final String Name1 = "Name";
    private static final String CateogryID1 = "CateogryID";
    private static final String Barcode1 = "Barcode";
    private static final String IsSuspended1 = "IsSuspended";
    private static final String ITEM_L1 = "ITEM_L";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String Price_List_D = "Price_List_D";

    private static final String ComapnyNo2 = "ComapnyNo";
    private static final String PrNo2 = "PrNo";
    private static final String ItemNo2 = "ItemNo";
    private static final String UnitID2 = "UnitID";
    private static final String Price2 = "Price";
    private static final String TaxPerc2 = "TaxPerc";
    private static final String MinSalePrice2 = "MinSalePrice";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String Price_List_M = "Price_List_M";

    private static final String ComapnyNo3 = "ComapnyNo";
    private static final String PrNo3 = "PrNo";
    private static final String Description3 = "Description";
    private static final String IsSuspended3 = "IsSuspended";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String Sales_Team = "Sales_Team";

    private static final String ComapnyNo4 = "ComapnyNo";
    private static final String SalesManNo4 = "SalesManNo";
    private static final String SalesManName4 = "SalesManName";
    private static final String IsSuspended4 = "IsSuspended";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SalesMan_Items_Balance = "SalesMan_Items_Balance";

    private static final String ComapnyNo5 = "ComapnyNo";
    private static final String SalesManNo5 = "SalesManNo";
    private static final String ItemNo5 = "ItemNo";
    private static final String Qty5 = "Qty";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SalesmanAndStoreLink = "SalesmanAndStoreLink";

    private static final String ComapnyNo6 = "ComapnyNo";
    private static final String SalesManNo6 = "SalesManNo";
    private static final String StoreNo6 = "StoreNo";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SalesMen = "SalesMen";

    private static final String UserName = "UserName";
    private static final String Password = "Password";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String CustomerPrices = "CustomerPrices";

    private static final String ItemNumber = "ItemNumber";
    private static final String CustomerNumber = "CustomerNumber";
    private static final String Price = "Price";

    // tables from ORIGINAL
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String TABLE_TRANSACTIONS = "TRANSACTIONS";

    private static final String SALES_MAN_ID = "SALES_MAN_ID";
    private static final String CUS_CODE = "CUS_CODE";
    private static final String CUS_NAME = "CUS_NAME";
    private static final String CHECK_IN_DATE = "CHECK_IN_DATE";
    private static final String CHECK_IN_TIME = "CHECK_IN_TIME";
    private static final String CHECK_OUT_DATE = "CHECK_OUT_DATE";
    private static final String CHECK_OUT_TIME = "CHECK_OUT_TIME";
    private static final String STATUS = "STATUS";
    private static final String IS_POSTED2 = "IS_POSTED";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String TABLE_SETTING = "SETTING";

    private static final String IP_ADDRESS = "IP_ADDRESS";
    private static final String TAX_CALC_KIND = "TAX_CALC_KIND";
    private static final String TRANS_KIND = "TRANS_KIND";
    private static final String SERIAL_NUMBER = "SERIAL_NUMBER";
    private static final String PRICE_BYCUSTOMER = "PRICE_BYCUSTOMER";
    private static final String USE_WEIGHT_CASE = "USE_WEIGHT_CASE";
    private static final String ALLOAW_MINUS = "ALLOAW_MINUS";
    private static final String NUMBER_OF_COPIES = "NUMBER_OF_COPIES";
    private static final String SALESMAN_CUSTOMERS = "SALESMAN_CUSTOMERS";
    private static final String MIN_SALE_PRICE = "MIN_SALE_PRICE";
    private static final String PRINT_METHOD = "PRINT_METHOD";
    private static final String ALLOW_OUT_OF_RANGE = "ALLOW_OUT_OF_RANGE";
    private static final String CAN_CHANGE_PRICE = "CAN_CHANGE_PRICE";
    private static final String READ_DISCOUNT_FROM_OFFERS = "READ_DISCOUNT_FROM_OFFERS";


    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String COMPANY_INFO = "COMPANY_INFO";

    private static final String COMPANY_NAME = "COMPANY_NAME";
    private static final String COMPANY_TEL = "COMPANY_TEL";
    private static final String TAX_NO = "TAX_NO";
    private static final String LOGO = "LOGO";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SALES_VOUCHER_MASTER = "SALES_VOUCHER_MASTER";

    private static final String COMPANY_NUMBER = "COMPANY_NUMBER";
    private static final String VOUCHER_NUMBER = "VOUCHER_NUMBER";
    private static final String VOUCHER_TYPE = "VOUCHER_TYPE";
    private static final String VOUCHER_DATE = "VOUCHER_DATE";
    private static final String SALES_MAN_NUMBER = "SALES_MAN_NUMBER";
    private static final String VOUCHER_DISCOUNT = "VOUCHER_DISCOUNT";
    private static final String VOUCHER_DISCOUNT_PERCENT = "VOUCHER_DISCOUNT_PERCENT";
    private static final String REMARK = "REMARK";
    private static final String PAY_METHOD = "PAY_METHOD";
    private static final String IS_POSTED = "IS_POSTED";
    private static final String TOTAL_DISC = "TOTAL_DISC";
    private static final String SUB_TOTAL = "SUB_TOTAL";
    private static final String TAX = "TAX";
    private static final String NET_SALES = "NET_SALES";
    private static final String CUST_NAME = "CUST_NAME";
    private static final String CUST_NUMBER = "CUST_NUMBER";
    private static final String VOUCHER_YEAR = "VOUCHER_YEAR";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SALES_VOUCHER_DETAILS = "SALES_VOUCHER_DETAILS";

    private static final String ITEM_NUMBER = "ITEM_NUMBER";
    private static final String ITEM_NAME = "ITEM_NAME";
    private static final String UNIT = "UNIT";
    private static final String UNIT_QTY = "UNIT_QTY";
    private static final String UNIT_PRICE = "UNIT_PRICE";
    private static final String BONUS = "BONUS";
    private static final String ITEM_DISCOUNT_VALUE = "ITEM_DISCOUNT_VALUE";
    private static final String ITEM_DISCOUNT_PERC = "ITEM_DISCOUNT_PERC";
    private static final String VOUCHER_DISCOUNT2 = "VOUCHER_DISCOUNT";
    private static final String TAX_VALUE = "TAX_VALUE";
    private static final String TAX_PERCENT = "TAX_PERCENT";
    private static final String COMPANY_NUMBER2 = "COMPANY_NUMBER";
    private static final String ITEM_YEAR = "ITEM_YEAR";
    private static final String IS_POSTED1 = "IS_POSTED";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String PAYMENTS = "PAYMENTS";

    private static final String COMPANY_NUMBER3 = "COMPANY_NUMBER";
    private static final String VOUCHER_NUMBER3 = "VOUCHER_NUMBER";
    private static final String PAY_DATE = "PAY_DATE";
    private static final String CUSTOMER_NUMBER = "CUSTOMER_NUMBER";
    private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String AMOUNT = "AMOUNT";
    private static final String REMARK3 = "REMARK";
    private static final String SALES_MAN_NUMBER3 = "SALES_MAN_NUMBER";
    private static final String IS_POSTED3 = "IS_POSTED";
    private static final String PAY_METHOD3 = "PAY_METHOD3";
    private static final String PAY_YEAR = "PAY_YEAR";


    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String PAYMENTS_PAPER = "PAYMENTS_PAPER";

    private static final String COMPANY_NUMBER4 = "COMPANY_NUMBER";
    private static final String VOUCHER_NUMBER4 = "VOUCHER_NUMBER";
    private static final String CHECK_NUMBER = "CHECK_NUMBER";
    private static final String BANK = "BANK";
    private static final String DUE_DATE = "DUE_DATE";
    private static final String AMOUNT4 = "AMOUNT";
    private static final String IS_POSTED4 = "IS_POSTED";
    private static final String PAY_YEAR4 = "PAY_YEAR";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String REQUEST_MASTER = "REQUEST_MASTER";

    private static final String COMPANY_NUMBER5 = "COMPANY_NUMBER";
    private static final String VOUCHER_NUMBER5 = "VOUCHER_NUMBER";
    private static final String VOUCHER_DATE5 = "VOUCHER_DATE";
    private static final String SALES_MAN_NUMBER5 = "SALES_MAN_NUMBER";
    private static final String REMARK5 = "REMARK";
    private static final String TOTAL_QTY5 = "TOTAL_QTY";
    private static final String IS_POSTED5 = "IS_POSTED";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String REQUEST_DETAILS = "REQUEST_DETAILS";

    private static final String COMPANY_NUMBER6 = "COMPANY_NUMBER";
    private static final String VOUCHER_NUMBER6 = "VOUCHER_NUMBER";
    private static final String ITEM_NUMBER6 = "ITEM_NUMBER";
    private static final String ITEM_NAME6 = "ITEM_NAME";
    private static final String UNIT_QTY6 = "UNIT_QTY";
    private static final String VOUCHER_DATE6 = "VOUCHER_DATE";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String ADDED_CUSTOMER = "ADDED_CUSTOMER";

    private static final String CUST_NAME7 = "CUST_NAME";
    private static final String REMARK7 = "REMARK";
    private static final String LATITUDE7 = "LATITUDE";
    private static final String LONGITUDE7 = "LONGITUDE";
    private static final String SALESMAN7 = "SALESMAN";
    private static final String SALESMAN_NO7 = "SALESMAN_NO";
    private static final String IS_POSTED7 = "IS_POSTED";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String VS_PROMOTION = "VS_PROMOTION";

    private static final String PROMOTION_ID = "PROMOTION_ID";
    private static final String PROMOTION_TYPE = "PROMOTION_TYPE";
    private static final String FROM_DATE = "FROM_DATE";
    private static final String TO_DATE = "TO_DATE";
    private static final String ITEM_NUMBERS = "ITEM_NUMBER";
    private static final String ITEM_QTY = "ITEM_QTY";
    private static final String BONUS_QTY = "BONUS_QTY";
    private static final String BONUS_ITEM_NO = "BONUS_ITEM_NO";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SALESMEN_STATIONS = "SALESMEN_STATIONS";

    private static final String SALESMEN_NO = "SALESMEN_NO";
    private static final String DATE_ = "DATE_";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String SERIAL = "SERIAL";
    private static final String CUSTOMR_NO = "CUSTOMR_NO";
    private static final String CUSUSTOMR_NAME = "CUSUSTOMR_NAME";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_CUSTOMER_MASTER = "CREATE TABLE " + CUSTOMER_MASTER + "("
                + COMPANY_NUMBER0 + " INTEGER,"
                + CUS_ID + " INTEGER,"
                + CUS_NAME0 + " TEXT,"
                + ADDRESS + " TEXT,"
                + IS_SUSPENDED + " INTEGER,"
                + PRICE_LIST_ID + " TEXT,"
                + CASH_CREDIT + " INTEGER,"
                + SALES_MAN_NO + " TEXT,"
                + CREDIT_LIMIT + " INTEGER,"
                + PAY_METHOD0 + " INTEGER,"
                + CUST_LAT + " TEXT,"
                + CUST_LONG + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_CUSTOMER_MASTER);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_Item_Unit_Details = "CREATE TABLE " + Item_Unit_Details + "("
                + ComapnyNo + " INTEGER,"
                + ItemNo + " TEXT,"
                + UnitID + " TEXT,"
                + ConvRate + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_Item_Unit_Details);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        String CREATE_TABLE_VISIT_RATE= "CREATE TABLE " + VISIT_RATE + "("
                + VISIT_PERPOS + " INTEGER,"
                + CUSTOMER_REGARDS + " INTEGER,"
                + CHECK_STORE + " INTEGER,"
                + PROMOTION_CHECK_STOCK + " INTEGER,"
                + SPESIFY_PROPOSED_REQUEST + " INTEGER,"
                + PERSUSION + " INTEGER,"
                + VISIT_RATE1 + " INTEGER,"
                + VISIT_PIC + " BLOB,"
                + CUST_CODE_ + " TEXT,"
                + CUST_NAME_ + " TEXT,"
                + SALESMAN_ + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_VISIT_RATE);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_Items_Master = "CREATE TABLE " + Items_Master + "("
                + ComapnyNo1 + " INTEGER,"
                + ItemNo1 + " TEXT,"
                + Name1 + " TEXT,"
                + CateogryID1 + " TEXT,"
                + Barcode1 + " TEXT,"
                + IsSuspended1 + " INTEGER,"
                + ITEM_L1 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_Items_Master);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_Price_List_D = "CREATE TABLE " + Price_List_D + "("
                + ComapnyNo2 + " INTEGER,"
                + PrNo2 + " INTEGER,"
                + ItemNo2 + " TEXT,"
                + UnitID2 + " TEXT,"
                + Price2 + " INTEGER,"
                + TaxPerc2 + " INTEGER,"
                + MinSalePrice2 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_Price_List_D);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_Price_List_M = "CREATE TABLE " + Price_List_M + "("
                + ComapnyNo3 + " INTEGER,"
                + PrNo3 + " INTEGER,"
                + Description3 + " TEXT,"
                + IsSuspended3 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_Price_List_M);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_Sales_Team = "CREATE TABLE " + Sales_Team + "("
                + ComapnyNo4 + " INTEGER,"
                + SalesManNo4 + " INTEGER,"
                + SalesManName4 + " TEXT,"
                + IsSuspended4 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_Sales_Team);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SalesMan_Items_Balance = "CREATE TABLE " + SalesMan_Items_Balance + "("
                + ComapnyNo5 + " INTEGER,"
                + SalesManNo5 + " INTEGER,"
                + ItemNo5 + " TEXT,"
                + Qty5 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_SalesMan_Items_Balance);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SalesmanAndStoreLink = "CREATE TABLE " + SalesmanAndStoreLink + "("
                + ComapnyNo6 + " INTEGER,"
                + SalesManNo6 + " INTEGER,"
                + StoreNo6 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_SalesmanAndStoreLink);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_Salesmen = "CREATE TABLE " + SalesMen + "("
                + UserName + " TEXT,"
                + Password + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_Salesmen);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_CustomerPrices = "CREATE TABLE " + CustomerPrices + "("
                + ItemNumber + " INTEGER,"
                + CustomerNumber + " INTEGER,"
                + Price + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_CustomerPrices);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + SALES_MAN_ID + " INTEGER,"
                + CUS_CODE + " INTEGER,"
                + CUS_NAME + " TEXT,"
                + CHECK_IN_DATE + " TEXT,"
                + CHECK_IN_TIME + " TEXT,"
                + CHECK_OUT_DATE + " TEXT,"
                + CHECK_OUT_TIME + " TEXT,"
                + STATUS + " INTEGER,"
                + IS_POSTED2 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_CONTACTS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SETTING = "CREATE TABLE " + TABLE_SETTING + "("
                + TRANS_KIND + " INTEGER,"
                + SERIAL_NUMBER + " INTEGER,"
                + IP_ADDRESS + " TEXT,"
                + TAX_CALC_KIND + " INTEGER,"
                + PRICE_BYCUSTOMER + " INTEGER,"
                + USE_WEIGHT_CASE + " INTEGER,"
                + ALLOAW_MINUS + " INTEGER,"
                + NUMBER_OF_COPIES + " INTEGER,"
                + SALESMAN_CUSTOMERS + " INTEGER,"
                + MIN_SALE_PRICE + " INTEGER,"
                + PRINT_METHOD + " INTEGER,"
                + CAN_CHANGE_PRICE + " INTEGER,"
                + ALLOW_OUT_OF_RANGE+  " INTEGER,"
                + READ_DISCOUNT_FROM_OFFERS+ " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_SETTING);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_COMPANY_INFO = "CREATE TABLE " + COMPANY_INFO + "("
                + COMPANY_NAME + " TEXT,"
                + COMPANY_TEL + " INTEGER,"
                + TAX_NO + " INTEGER,"
                + LOGO + " BLOB" + ")";
        db.execSQL(CREATE_TABLE_COMPANY_INFO);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SALES_VOUCHER_MASTER = "CREATE TABLE " + SALES_VOUCHER_MASTER + "("
                + COMPANY_NUMBER + " INTEGER,"
                + VOUCHER_NUMBER + " INTEGER,"
                + VOUCHER_TYPE + " INTEGER,"
                + VOUCHER_DATE + " TEXT,"
                + SALES_MAN_NUMBER + " INTEGER,"
                + VOUCHER_DISCOUNT + " INTEGER,"
                + VOUCHER_DISCOUNT_PERCENT + " TEXT,"
                + REMARK + " TEXT,"
                + PAY_METHOD + " INTEGER,"
                + IS_POSTED + " INTEGER,"
                + TOTAL_DISC + " INTEGER,"
                + SUB_TOTAL + " INTEGER,"
                + TAX + " INTEGER,"
                + NET_SALES + " INTEGER,"
                + CUST_NAME + " TEXT,"
                + CUST_NUMBER + " TEXT,"
                + VOUCHER_YEAR + " INTEGER " + ")";
        db.execSQL(CREATE_TABLE_SALES_VOUCHER_MASTER);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SALES_VOUCHER_DETAILS = "CREATE TABLE " + SALES_VOUCHER_DETAILS + "("
                + VOUCHER_NUMBER + " INTEGER,"
                + VOUCHER_TYPE + " INTEGER,"
                + ITEM_NUMBER + " TEXT,"
                + ITEM_NAME + " TEXT,"
                + UNIT + " TEXT,"
                + UNIT_QTY + " INTEGER,"
                + UNIT_PRICE + " INTEGER,"
                + BONUS + " INTEGER,"
                + ITEM_DISCOUNT_VALUE + " INTEGER,"
                + ITEM_DISCOUNT_PERC + " TEXT,"
                + VOUCHER_DISCOUNT2 + " INTEGER,"
                + TAX_VALUE + " INTEGER,"
                + TAX_PERCENT + " INTEGER,"
                + COMPANY_NUMBER2 + " INTEGER,"
                + ITEM_YEAR + " TEXT,"
                + IS_POSTED1 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_SALES_VOUCHER_DETAILS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_PAYMENTS = "CREATE TABLE " + PAYMENTS + "("
                + COMPANY_NUMBER3 + " INTEGER,"
                + VOUCHER_NUMBER3 + " INTEGER,"
                + PAY_DATE + " INTEGER,"
                + CUSTOMER_NUMBER + " INTEGER,"
                + AMOUNT + " INTEGER,"
                + REMARK3 + " TEXT,"
                + SALES_MAN_NUMBER3 + " INTEGER,"
                + IS_POSTED3 + " INTEGER,"
                + PAY_METHOD3 + " INTEGER,"
                + CUSTOMER_NAME + " TEXT,"
                + PAY_YEAR + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_PAYMENTS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_PAYMENTS_PAPER = "CREATE TABLE " + PAYMENTS_PAPER + "("
                + COMPANY_NUMBER4 + " INTEGER,"
                + VOUCHER_NUMBER4 + " INTEGER,"
                + CHECK_NUMBER + " INTEGER,"
                + BANK + " TEXT,"
                + DUE_DATE + " TEXT,"
                + AMOUNT4 + " INTEGER,"
                + IS_POSTED4 + " INTEGER,"
                + PAY_YEAR4 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_PAYMENTS_PAPER);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_REQUEST_MASTER = "CREATE TABLE " + REQUEST_MASTER + "("
                + COMPANY_NUMBER5 + " INTEGER,"
                + VOUCHER_NUMBER5 + " INTEGER,"
                + VOUCHER_DATE5 + " TEXT,"
                + SALES_MAN_NUMBER5 + " INTEGER,"
                + REMARK5 + " TEXT,"
                + TOTAL_QTY5 + " INTEGER,"
                + IS_POSTED5 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_REQUEST_MASTER);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_REQUEST_DETAILS = "CREATE TABLE " + REQUEST_DETAILS + "("
                + COMPANY_NUMBER6 + " INTEGER,"
                + VOUCHER_NUMBER6 + " INTEGER,"
                + ITEM_NUMBER6 + " TEXT,"
                + ITEM_NAME6 + " TEXT,"
                + UNIT_QTY6 + " INTEGER,"
                + VOUCHER_DATE5 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_REQUEST_DETAILS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_ADDED_CUSTOMER = "CREATE TABLE " + ADDED_CUSTOMER + "("
                + CUST_NAME7 + " TEXT,"
                + REMARK7 + " TEXT,"
                + LATITUDE7 + " INTEGER,"
                + LONGITUDE7 + " INTEGER,"
                + SALESMAN7 + " TEXT,"
                + IS_POSTED7 + " INTEGER,"
                + SALESMAN_NO7 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_ADDED_CUSTOMER);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_VS_PROMOTION = "CREATE TABLE " + VS_PROMOTION + "("
                + PROMOTION_ID + " INTEGER,"
                + PROMOTION_TYPE + " INTEGER,"
                + FROM_DATE + " TEXT,"
                + TO_DATE + " TEXT,"
                + ITEM_NUMBERS + " TEXT,"
                + ITEM_QTY + " INTEGER,"
                + BONUS_QTY + " INTEGER,"
                + BONUS_ITEM_NO + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_VS_PROMOTION);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SALESMEN_STATIONS = "CREATE TABLE " + SALESMEN_STATIONS + "("
                + SALESMEN_NO + " TEXT,"
                + DATE_ + " TEXT,"
                + LATITUDE + " TEXT,"
                + LONGITUDE + " TEXT,"
                + SERIAL + " INTEGER,"
                + CUSTOMR_NO + " TEXT,"
                + CUSUSTOMR_NAME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SALESMEN_STATIONS);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("DROP TABLE SALESMEN_STATIONS");
        }catch (Exception e) {
            Log.e("onUpgrade*****", "duplicated column");
        }

        try {

//            db.execSQL("ALTER TABLE SETTING ADD PRINT_METHOD TEXT NOT NULL DEFAULT '1'");TABLE_SETTING
//            db.execSQL("ALTER TABLE VISIT_RATE ADD CUST_CODE TEXT NOT NULL DEFAULT ''");
//            db.execSQL("ALTER TABLE VISIT_RATE ADD CUST_NAME TEXT NOT NULL DEFAULT ''");
//            db.execSQL("ALTER TABLE VISIT_RATE ADD SALESMAN TEXT NOT NULL DEFAULT ''");
//            db.execSQL("ALTER TABLE SALESMEN_STATIONS ADD CUSTOMR_NO TEXT  NOT NULL DEFAULT '111'");
//            db.execSQL("ALTER TABLE SALESMEN_STATIONS ADD CUSUSTOMR_NAME TEXT  NOT NULL DEFAULT 'sss'");

            String CREATE_TABLE_SALESMEN_STATIONS = "CREATE TABLE " + SALESMEN_STATIONS + "("
                    + SALESMEN_NO + " TEXT,"
                    + DATE_ + " TEXT,"
                    + LATITUDE + " TEXT,"
                    + LONGITUDE + " TEXT,"
                    + SERIAL + " INTEGER,"
                    + CUSTOMR_NO + " TEXT,"
                    + CUSUSTOMR_NAME + " TEXT" + ")";
            db.execSQL(CREATE_TABLE_SALESMEN_STATIONS);

        } catch (Exception e) {
            Log.e("onUpgrade*****", "duplicated column");
        }

    }

    public void addCustomer(Customer customer) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(COMPANY_NUMBER0, customer.getCompanyNumber());
        values.put(CUS_ID, customer.getCustId());
        values.put(CUS_NAME0, customer.getCustName());
        values.put(ADDRESS, customer.getAddress());
        values.put(IS_SUSPENDED, customer.getIsSuspended());
        values.put(PRICE_LIST_ID, customer.getPriceListId());
        values.put(CASH_CREDIT, customer.getCashCredit());
        values.put(SALES_MAN_NO, customer.getSalesManNumber());
        values.put(CREDIT_LIMIT, customer.getCreditLimit());
        values.put(PAY_METHOD0, customer.getPayMethod());
        values.put(CUST_LAT, customer.getCustLat());
        values.put(CUST_LONG, customer.getCustLong());

        db.insert(CUSTOMER_MASTER, null, values);
        db.close();
    }

    public void addItem_Unit_Details(ItemUnitDetails item) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo, item.getCompanyNo());
        values.put(ItemNo, item.getItemNo());
        values.put(UnitID, item.getUnitId());
        values.put(ConvRate, item.getConvRate());

        db.insert(Item_Unit_Details, null, values);
        db.close();
    }

    public void addItemsMaster(ItemsMaster item) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo1, item.getCompanyNo());
        values.put(ItemNo1, item.getItemNo());
        values.put(Name1, item.getName());
        values.put(CateogryID1, item.getCategoryId());
        values.put(Barcode1, item.getBarcode());
        values.put(IsSuspended1, item.getIsSuspended());
        values.put(ITEM_L1, item.getItemL());

        db.insert(Items_Master, null, values);
        db.close();
    }
    //----------------------------------------------------
    public void addVisitRate(VisitRate visitRate) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(VISIT_PERPOS,visitRate.getVisitPerpos());
        values.put(CUSTOMER_REGARDS, visitRate.getCustomerRegards());
        values.put(CHECK_STORE, visitRate.getCheckStore());
        values.put(PROMOTION_CHECK_STOCK, visitRate.getPromotionCheckStock());
        values.put(SPESIFY_PROPOSED_REQUEST, visitRate.getSpesifyProposedRequest());
        values.put(PERSUSION, visitRate.getPersusion());
        values.put(VISIT_RATE1, visitRate.getVisitRate());

        byte[] byteImage = {};
        if (visitRate.getVisitpic() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            visitRate.getVisitpic() .compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteImage = stream.toByteArray();
            values.put(VISIT_PIC, byteImage);
        }

        values.put(CUST_CODE_, visitRate.getCustCode());
        values.put(CUST_NAME_, visitRate.getCustName());
        values.put(SALESMAN_, visitRate.getSalesman());

        db.insert(VISIT_RATE, null, values);
        db.close();
    }
    //----------------------------------------------------

    public void addPrice_List_D(PriceListD price) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo2, price.getCompanyNo());
        values.put(PrNo2, price.getPrNo());
        values.put(ItemNo2, price.getItemNo());
        values.put(UnitID2, price.getUnitId());
        values.put(Price2, price.getPrice());
        values.put(TaxPerc2, price.getTaxPerc());
        values.put(MinSalePrice2, price.getMinSalePrice());

        db.insert(Price_List_D, null, values);
        db.close();
    }

    public void addPrice_List_M(PriceListM price) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo3, price.getCompanyNo());
        values.put(PrNo3, price.getPrNo());
        values.put(Description3, price.getDescribtion());
        values.put(IsSuspended3, price.getIsSuspended());

        db.insert(Price_List_M, null, values);
        db.close();
    }

    public void addSales_Team(SalesTeam salesTeam) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo4, salesTeam.getCompanyNo());
        values.put(SalesManNo4, salesTeam.getSalesManNo());
        values.put(SalesManName4, salesTeam.getSalesManName());
        values.put(IsSuspended4, salesTeam.getIsSuspended());

        db.insert(Sales_Team, null, values);
        db.close();
    }

    public void addSalesMan_Items_Balance(SalesManItemsBalance balance) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo5, balance.getCompanyNo());
        values.put(SalesManNo5, balance.getSalesManNo());
        values.put(ItemNo5, balance.getItemNo());
        values.put(Qty5, balance.getQty());

        db.insert(SalesMan_Items_Balance, null, values);
        db.close();
    }

    public void addSalesmanAndStoreLink(SalesManAndStoreLink store) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo6, store.getCompanyNo());
        values.put(SalesManNo6, store.getSalesManNo());
        values.put(StoreNo6, store.getStoreNo());

        db.insert(SalesmanAndStoreLink, null, values);
        db.close();
    }

    public void addSalesmen(SalesMan salesMan) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(UserName, salesMan.getUserName());
        values.put(Password, salesMan.getPassword());

        db.insert(SalesMen, null, values);
        db.close();
    }

    public void addCustomerPrice(CustomerPrice price) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ItemNumber, price.getItemNumber());
        values.put(CustomerNumber, price.getCustomerNumber());
        values.put(Price, price.getPrice());

        db.insert(CustomerPrices, null, values);
        db.close();
    }

    public void addTransaction(Transaction transaction) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SALES_MAN_ID, transaction.getSalesManId());
        values.put(CUS_CODE, transaction.getCusCode());
        values.put(CUS_NAME, transaction.getCusName());
        values.put(CHECK_IN_DATE, transaction.getCheckInDate());
        values.put(CHECK_IN_TIME, transaction.getCheckInTime());
        values.put(CHECK_OUT_DATE, transaction.getCheckOutDate());
        values.put(CHECK_OUT_TIME, transaction.getCheckOutTime());
        values.put(STATUS, transaction.getStatus());
        values.put(IS_POSTED2, transaction.getIsPosted());

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    public void addSetting(String ipAddress, int taxCalcKind, int transKind, int serialNumber, int priceByCust, int useWeightCase,
                           int allowMinus, int numOfCopy, int salesManCustomers, int minSalePrice, int printMethod, int allowOutOfRange,int canChangePrice,int readDiscount) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANS_KIND, transKind);
        values.put(SERIAL_NUMBER, serialNumber);
        values.put(IP_ADDRESS, ipAddress);
        values.put(TAX_CALC_KIND, taxCalcKind);
        values.put(PRICE_BYCUSTOMER, priceByCust);
        values.put(USE_WEIGHT_CASE, useWeightCase);
        values.put(ALLOAW_MINUS, allowMinus);
        values.put(NUMBER_OF_COPIES, numOfCopy);
        values.put(SALESMAN_CUSTOMERS, salesManCustomers);
        values.put(MIN_SALE_PRICE, minSalePrice);
        values.put(PRINT_METHOD, printMethod);
        values.put(CAN_CHANGE_PRICE, canChangePrice);
        values.put(ALLOW_OUT_OF_RANGE, allowOutOfRange);
        values.put(READ_DISCOUNT_FROM_OFFERS, readDiscount);

        db.insert(TABLE_SETTING, null, values);
        db.close();
    }

    public void addCompanyInfo(String companyName, int companyTel, int taxNo, Bitmap logo) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        byte[] byteImage = {};
        if (logo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            logo.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteImage = stream.toByteArray();
        }

        values.put(COMPANY_NAME, companyName);
        values.put(COMPANY_TEL, companyTel);
        values.put(TAX_NO, taxNo);
        values.put(LOGO, byteImage);

        db.insert(COMPANY_INFO, null, values);
        db.close();
    }

    public void addVoucher(Voucher voucher) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(COMPANY_NUMBER, voucher.getCompanyNumber());
        values.put(VOUCHER_NUMBER, voucher.getVoucherNumber());
        values.put(VOUCHER_TYPE, voucher.getVoucherType());
        values.put(VOUCHER_DATE, voucher.getVoucherDate());
        values.put(SALES_MAN_NUMBER, voucher.getSaleManNumber());
        values.put(VOUCHER_DISCOUNT, voucher.getVoucherDiscount());
        values.put(VOUCHER_DISCOUNT_PERCENT, voucher.getVoucherDiscountPercent());
        values.put(REMARK, voucher.getRemark());
        values.put(PAY_METHOD, voucher.getPayMethod());
        values.put(IS_POSTED, voucher.getIsPosted());
        values.put(TOTAL_DISC, voucher.getTotalVoucherDiscount());
        values.put(SUB_TOTAL, voucher.getSubTotal());
        values.put(TAX, voucher.getTax());
        values.put(NET_SALES, voucher.getNetSales());
        values.put(CUST_NAME, voucher.getCustName());
        values.put(CUST_NUMBER, voucher.getCustNumber());
        values.put(VOUCHER_YEAR, voucher.getVoucherYear());

        db.insert(SALES_VOUCHER_MASTER, null, values);
        db.close();
    }

    public void addItem(Item item) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(VOUCHER_NUMBER, item.getVoucherNumber());
        values.put(VOUCHER_TYPE, item.getVoucherType());
        values.put(ITEM_NUMBER, item.getItemNo());
        values.put(ITEM_NAME, item.getItemName());
        values.put(UNIT, item.getUnit());
        values.put(UNIT_QTY, item.getQty());
        values.put(UNIT_PRICE, item.getPrice());
        values.put(BONUS, item.getBonus());
        values.put(ITEM_DISCOUNT_VALUE, item.getDisc());
        values.put(ITEM_DISCOUNT_PERC, item.getDiscPerc());
        values.put(VOUCHER_DISCOUNT2, item.getVoucherDiscount());
        values.put(TAX_VALUE, item.getTaxValue());
        values.put(TAX_PERCENT, item.getTaxPercent());
        values.put(COMPANY_NUMBER2, item.getCompanyNumber());
        values.put(ITEM_YEAR, item.getYear());
        values.put(IS_POSTED1, item.getIsPosted());

        db.insert(SALES_VOUCHER_DETAILS, null, values);
        db.close();
    }

    public void addPayment(Payment payment) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(COMPANY_NUMBER3, payment.getCompanyNumber());
        values.put(VOUCHER_NUMBER3, payment.getVoucherNumber());
        values.put(PAY_DATE, payment.getPayDate());
        values.put(CUSTOMER_NUMBER, payment.getCustNumber());
        values.put(AMOUNT, payment.getAmount());
        values.put(REMARK3, payment.getRemark());
        values.put(SALES_MAN_NUMBER3, payment.getSaleManNumber());
        values.put(IS_POSTED3, payment.getIsPosted());
        values.put(PAY_METHOD3, payment.getPayMethod());
        values.put(CUSTOMER_NAME, payment.getCustName());
        values.put(PAY_YEAR, payment.getYear());

        db.insert(PAYMENTS, null, values);
        db.close();
    }

    public void addPaymentPaper(Payment payment) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(COMPANY_NUMBER4, payment.getCompanyNumber());
        values.put(VOUCHER_NUMBER4, payment.getVoucherNumber());
        values.put(CHECK_NUMBER, payment.getCheckNumber());
        values.put(BANK, payment.getBank());
        values.put(DUE_DATE, payment.getDueDate());
        values.put(AMOUNT4, payment.getAmount());
        values.put(IS_POSTED4, payment.getIsPosted());
        values.put(PAY_YEAR4, payment.getYear());

        db.insert(PAYMENTS_PAPER, null, values);
        db.close();
    }

    public void addRequestVoucher(Voucher voucher) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(COMPANY_NUMBER5, voucher.getCompanyNumber());
        values.put(VOUCHER_NUMBER5, voucher.getVoucherNumber());
        values.put(VOUCHER_DATE5, voucher.getVoucherDate());
        values.put(SALES_MAN_NUMBER5, voucher.getSaleManNumber());
        values.put(REMARK5, voucher.getRemark());
        values.put(TOTAL_QTY5, voucher.getTotalQty());
        values.put(IS_POSTED5, voucher.getIsPosted());

        db.insert(REQUEST_MASTER, null, values);
        db.close();
    }

    public void addRequestItems(Item item) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(COMPANY_NUMBER6, item.getCompanyNumber());
        values.put(VOUCHER_NUMBER6, item.getVoucherNumber());
        values.put(ITEM_NUMBER6, item.getItemNo());
        values.put(ITEM_NAME6, item.getItemName());
        values.put(UNIT_QTY6, item.getQty());
        values.put(VOUCHER_DATE6, item.getDate());

        db.insert(REQUEST_DETAILS, null, values);
        db.close();
    }

    public void addAddedCustomer(AddedCustomer customer) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(CUST_NAME7, customer.getCustName());
        values.put(REMARK7, customer.getRemark());
        values.put(LATITUDE7, customer.getLatitude());
        values.put(LONGITUDE7, customer.getLongtitude());
        values.put(SALESMAN7, customer.getSalesMan());
        values.put(SALESMAN_NO7, customer.getSalesmanNo());
        values.put(IS_POSTED7, customer.getIsPosted());

        db.insert(ADDED_CUSTOMER, null, values);
        db.close();
    }

    public void addOffer(Offers offers) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(PROMOTION_ID, offers.getPromotionID());
        values.put(PROMOTION_TYPE, offers.getPromotionType());
        values.put(FROM_DATE, offers.getFromDate());
        values.put(TO_DATE, offers.getToDate());
        values.put(ITEM_NUMBERS, offers.getItemNo());
        values.put(ITEM_QTY, offers.getItemQty());
        values.put(BONUS_QTY, offers.getBonusQty());
        values.put(BONUS_ITEM_NO, offers.getBonusItemNo());

        db.insert(VS_PROMOTION, null, values);
        db.close();
    }

    public void addSalesmanStation(SalesmanStations station) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SALESMEN_NO, station.getSalesmanNo());
        values.put(DATE_, station.getDate());
        values.put(LATITUDE, station.getLatitude());
        values.put(LONGITUDE, station.getLongitude());
        values.put(SERIAL, station.getSerial());
        values.put(CUSTOMR_NO, station.getCustNo());
        values.put(CUSUSTOMR_NAME, station.getCustName());

        db.insert(SALESMEN_STATIONS, null, values);
        db.close();
    }
    //--------------------------------------------------------
    public List<VisitRate> getVisitRate() {
        List<VisitRate> visitrate = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + VISIT_RATE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VisitRate rate = new VisitRate();
                rate.setVisitPerpos(Integer.parseInt(cursor.getString(0)));
                rate.setCustomerRegards(Integer.parseInt(cursor.getString(1)));
                rate.setCheckStore(Integer.parseInt(cursor.getString(2)));
                rate.setPromotionCheckStock(Integer.parseInt(cursor.getString(3)));
                rate.setCheckStore(Integer.parseInt(cursor.getString(4)));
                rate.setSpesifyProposedRequest(Integer.parseInt(cursor.getString(5)));
                rate.setPersusion(Integer.parseInt(cursor.getString(6)));
                rate.setVisitRate(Integer.parseInt(cursor.getString(7)));
                if (cursor.getBlob(8).length == 0)
                    rate.setVisitpic(null);
                else
                    rate.setVisitpic(BitmapFactory.decodeByteArray(cursor.getBlob(8), 0, cursor.getBlob(8).length));

                rate.setCustCode(cursor.getString(9));
                rate.setCustName(cursor.getString(10));
                rate.setSalesman(cursor.getString(11));

                visitrate.add(rate);
            } while (cursor.moveToNext());
        }
        return visitrate;
    }

    //--------------------------------------------------------


    public List<Settings> getAllSettings() {
        List<Settings> settings = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTING;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Settings setting = new Settings();
                setting.setTransactionType(Integer.parseInt(cursor.getString(0)));
                setting.setSerial(Integer.parseInt(cursor.getString(1)));
                setting.setIpAddress(cursor.getString(2));
                setting.setTaxClarcKind(Integer.parseInt(cursor.getString(3)));
                setting.setPriceByCust(Integer.parseInt(cursor.getString(4)));
                setting.setUseWeightCase(Integer.parseInt(cursor.getString(5)));
                setting.setAllowMinus(Integer.parseInt(cursor.getString(6)));
                setting.setNumOfCopy(Integer.parseInt(cursor.getString(7)));
                setting.setSalesManCustomers(Integer.parseInt(cursor.getString(8)));
                setting.setMinSalePric(Integer.parseInt(cursor.getString(9)));
                setting.setPrintMethod(Integer.parseInt(cursor.getString(10)));
                setting.setAllowOutOfRange(Integer.parseInt(cursor.getString(11)));
                setting.setCanChangePrice(Integer.parseInt(cursor.getString(12)));
                setting.setReadDiscountFromOffers(Integer.parseInt(cursor.getString(13)));
                settings.add(setting);
            } while (cursor.moveToNext());
        }
        return settings;
    }

    public List<CompanyInfo> getAllCompanyInfo() {
        List<CompanyInfo> infos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + COMPANY_INFO;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CompanyInfo info = new CompanyInfo();
                info.setCompanyName(cursor.getString(0));
                info.setcompanyTel(Integer.parseInt(cursor.getString(1)));
                info.setTaxNo(Integer.parseInt(cursor.getString(2)));

                if (cursor.getBlob(3).length == 0)
                    info.setLogo(null);
                else
                    info.setLogo(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0, cursor.getBlob(3).length));

                infos.add(info);
            } while (cursor.moveToNext());
        }
        return infos;
    }

    public int getMaxSerialNumber(int voucherType) {
        String selectQuery = "SELECT  SERIAL_NUMBER FROM " + TABLE_SETTING + " WHERE TRANS_KIND = " + voucherType;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        int maxVoucher = Integer.parseInt(cursor.getString(0));
        return maxVoucher;

    }

    // hello
    public void setMaxSerialNumber(int voucherType, int newSerial) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERIAL_NUMBER, newSerial);


        // updating row
        db.update(TABLE_SETTING, values, TRANS_KIND + "=" + voucherType, null);

    }

    public int getMaxVoucherNumber() {
        String selectQuery = "SELECT  MAX(VOUCHER_NUMBER) FROM " + PAYMENTS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getString(0) == null) {
            return 0;
        } else {
            int maxVoucher = Integer.parseInt(cursor.getString(0));
            return maxVoucher;
        }
    }

    public int getMaxVoucherStockNumber() {
        String selectQuery = "SELECT  MAX(VOUCHER_NUMBER) FROM " + REQUEST_MASTER;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getString(0) == null) {
            return 0;
        } else {
            int maxVoucher = Integer.parseInt(cursor.getString(0));
            return maxVoucher;
        }
    }

    public List<Transaction> getAlltransactions() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTIONS;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();

                transaction.setSalesManId(Integer.parseInt(cursor.getString(0)));
                transaction.setCusCode(cursor.getString(1));
                transaction.setCusName(cursor.getString(2));
                transaction.setCheckInDate(cursor.getString(3));
                transaction.setCheckInTime(cursor.getString(4));
                transaction.setCheckOutDate(cursor.getString(5));
                transaction.setCheckOutTime(cursor.getString(6));
                transaction.setStatus(Integer.parseInt(cursor.getString(7)));
                transaction.setIsPosted(Integer.parseInt(cursor.getString(8)));

                // Adding transaction to list
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        return transactionList;
    }

    public List<SalesMan> getAllSalesMen() {
        List<SalesMan> salesMen = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SalesMen;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.e("*****", "" + cursor.getCount());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SalesMan salesMan = new SalesMan();

                salesMan.setUserName(cursor.getString(0));
                salesMan.setPassword(cursor.getString(1));

                // Adding transaction to list
                salesMen.add(salesMan);
            } while (cursor.moveToNext());
        }
        return salesMen;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<Customer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CUSTOMER_MASTER;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();

                customer.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                customer.setCustId(cursor.getString(1));
                customer.setCustName(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setIsSuspended(Integer.parseInt(cursor.getString(4)));
                customer.setPriceListId(cursor.getString(5));
                customer.setCashCredit(Integer.parseInt(cursor.getString(6)));
                customer.setSalesManNumber(cursor.getString(7));
                customer.setCreditLimit(Double.parseDouble(cursor.getString(8)));
                customer.setCustLat(cursor.getString(10));
                customer.setCustLong(cursor.getString(11));

                // Adding transaction to list
                customers.add(customer);
            } while (cursor.moveToNext());
        }
        return customers;
    }

    public List<Customer> getCustomersBySalesMan(String salesMan) {
        List<Customer> customers = new ArrayList<Customer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CUSTOMER_MASTER + " where SALES_MAN_NO = '" + salesMan + "'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();

                customer.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                customer.setCustId(cursor.getString(1));
                customer.setCustName(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setIsSuspended(Integer.parseInt(cursor.getString(4)));
                customer.setPriceListId(cursor.getString(5));
                customer.setCashCredit(Integer.parseInt(cursor.getString(6)));
                customer.setSalesManNumber(cursor.getString(7));
                customer.setCreditLimit(Integer.parseInt(cursor.getString(8)));
                customer.setCustLat(cursor.getString(9));
                customer.setCustLong(cursor.getString(10));

                // Adding transaction to list
                customers.add(customer);
            } while (cursor.moveToNext());
        }
        return customers;
    }

    public List<Voucher> getAllVouchers() {
        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SALES_VOUCHER_MASTER;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Voucher Voucher = new Voucher();

                Voucher.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                Voucher.setVoucherNumber(Integer.parseInt(cursor.getString(1)));
                Voucher.setVoucherType(Integer.parseInt(cursor.getString(2)));
                Voucher.setVoucherDate(cursor.getString(3));
                Voucher.setSaleManNumber(Integer.parseInt(cursor.getString(4)));
                Voucher.setVoucherDiscount(Double.parseDouble(cursor.getString(5)));
                Voucher.setVoucherDiscountPercent(Double.parseDouble(cursor.getString(6)));
                Voucher.setRemark(cursor.getString(7));
                Voucher.setPayMethod(Integer.parseInt(cursor.getString(8)));
                Voucher.setIsPosted(Integer.parseInt(cursor.getString(9)));
                Voucher.setTotalVoucherDiscount(Double.parseDouble(cursor.getString(10)));
                Voucher.setSubTotal(Double.parseDouble(cursor.getString(11)));
                Voucher.setTax(Double.parseDouble(cursor.getString(12)));
                Voucher.setNetSales(Double.parseDouble(cursor.getString(13)));
                Voucher.setCustName(cursor.getString(14));
                Voucher.setCustNumber(cursor.getString(15));
                Voucher.setVoucherYear(Integer.parseInt(cursor.getString(16)));

                // Adding transaction to list
                vouchers.add(Voucher);
            } while (cursor.moveToNext());
        }

        return vouchers;
    }
// to retrive items number and name to inventory report
    public List<ItemsMaster> getItemMaster() {
        List<ItemsMaster> masters = new ArrayList<ItemsMaster>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Items_Master;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemsMaster itemsMaster = new ItemsMaster();
                itemsMaster.setCompanyNo(Integer.parseInt(cursor.getString(0)));
                itemsMaster.setItemNo(cursor.getString(1));
                itemsMaster.setName(cursor.getString(2));
                itemsMaster.setCategoryId(cursor.getString(3));
                itemsMaster.setBarcode(cursor.getString(4));
                itemsMaster.setIsSuspended(Integer.parseInt(cursor.getString(5)));
                itemsMaster.setItemL(Double.parseDouble(cursor.getString(6)));
                masters.add(itemsMaster);
            }
            while(cursor.moveToNext());
        }
        return  masters;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "select D.VOUCHER_NUMBER , D.VOUCHER_TYPE , D.ITEM_NUMBER ,D.ITEM_NAME ," +
                " D.UNIT ,D.UNIT_QTY , D.UNIT_PRICE ,D.BONUS  ,D.ITEM_DISCOUNT_VALUE ,D.ITEM_DISCOUNT_PERC ," +
                "D.VOUCHER_DISCOUNT , D.TAX_VALUE , D.TAX_PERCENT , D.COMPANY_NUMBER , D.ITEM_YEAR , D.IS_POSTED , M.VOUCHER_DATE " +
                "from SALES_VOUCHER_DETAILS D , SALES_VOUCHER_MASTER M " +
                "where D.VOUCHER_NUMBER  = M.VOUCHER_NUMBER and D.VOUCHER_TYPE = M.VOUCHER_TYPE";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "************************" + selectQuery);
            do {
                Item item = new Item();

                item.setVoucherNumber(Integer.parseInt(cursor.getString(0)));
                item.setVoucherType(Integer.parseInt(cursor.getString(1)));
                item.setItemNo(cursor.getString(2));
                item.setItemName(cursor.getString(3));
                item.setUnit(cursor.getString(4));
                item.setQty(Float.parseFloat(cursor.getString(5)));
                item.setPrice(Float.parseFloat(cursor.getString(6)));
                item.setBonus(Float.parseFloat(cursor.getString(7)));
                item.setDisc(Float.parseFloat(cursor.getString(8)));
                item.setDiscPerc(cursor.getString(9));
                item.setTotalDiscVal(Integer.parseInt(cursor.getString(10)));
                item.setTaxValue(Double.parseDouble(cursor.getString(11)));
                item.setTaxPercent(Float.parseFloat(cursor.getString(12)));
                item.setCompanyNumber(Integer.parseInt(cursor.getString(13)));
                item.setYear(cursor.getString(14));
                item.setIsPosted(Integer.parseInt(cursor.getString(15)));
                item.setDate(cursor.getString(16));

                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public List<inventoryReportItem> getInventory_db() {

        List<inventoryReportItem> items_inventory = new ArrayList<>();
        String salesMan = Login.salesMan;
        // Select All Query
        String selectQuery = " select  DISTINCT    M.ItemNo ,M.Name ,S.Qty ,M.CateogryID from Items_Master M   ,  SalesMan_Items_Balance S  " +
                "  where M.ItemNo  = S.ItemNo  and  S.SalesManNo = '" + salesMan +"'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                inventoryReportItem  itemsinven=new inventoryReportItem();
                itemsinven.setItemNo(cursor.getString(0));
                itemsinven.setName(cursor.getString(1));
                itemsinven.setQty(Double.parseDouble(cursor.getString(2)));
                itemsinven.setCategoryId(cursor.getString(3));
                items_inventory.add(itemsinven);


            }
            while (cursor.moveToNext());
        }
    return  items_inventory;
    }

    public List<Item> getAllJsonItems() {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        String salesMan = Login.salesMan;
        String PriceListId = CustomerListShow.PriceListId;
        String selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L\n" +
                "                from Items_Master M , SalesMan_Items_Balance S , Price_List_D P\n" +
                "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo = '1' and S.SalesManNo = '" + salesMan +"'";

        Log.e("***" , selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
            do {
                Item item = new Item();

                item.setItemNo(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setQty(Float.parseFloat(cursor.getString(3)));
                item.setPrice(Float.parseFloat(cursor.getString(4)));
                item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
                item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
                item.setBarcode(cursor.getString(7));
                item.setItemL(Double.parseDouble(cursor.getString(8)));

                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public List<Item> getAllJsonItems2()
    {

        List<Item> items = new ArrayList<>();
        // Select All Query
        String PriceListId = CustomerListShow.PriceListId;
        String custNum = CustomerListShow.Customer_Account;
        String salesMan = Login.salesMan;
        String selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,C.PRICE ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L\n" +
                "   from Items_Master M , SalesMan_Items_Balance S , CustomerPrices C , Price_List_D P\n" +
                "   where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and M.ItemNo = C.ItemNumber and P.PrNo = '1' and S.SalesManNo = '" + salesMan + "'" +
                "   and C.CustomerNumber = '" + custNum + "'";

        Log.e("***" , selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
            do {
                Item item = new Item();

                item.setItemNo(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setQty(Float.parseFloat(cursor.getString(3)));
                item.setPrice(Float.parseFloat(cursor.getString(4)));
                item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
                item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
                item.setBarcode(cursor.getString(7));
                item.setItemL(Double.parseDouble(cursor.getString(8)));

                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public List<String> getAllExistingCategories() {
        List<String> categories = new ArrayList<>();
        String selectQuery = "select DISTINCT CateogryID from Items_Master";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public List<String> getAllexistingUnits(String itemNo) {
        List<String> units = new ArrayList<>();
        units.add("1");
        String selectQuery = "select DISTINCT  ConvRate  from " + Item_Unit_Details + " where ItemNo = '" + itemNo + "'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                units.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return units;
    }

    public List<Payment> getAllPayments() {

        List<Payment> paymentsList = new ArrayList<Payment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PAYMENTS;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();

                payment.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                payment.setVoucherNumber(Integer.parseInt(cursor.getString(1)));
                payment.setPayDate(cursor.getString(2));
                payment.setCustNumber(cursor.getString(3));
                payment.setAmount(Double.parseDouble(cursor.getString(4)));
                payment.setRemark(cursor.getString(5));
                payment.setSaleManNumber(Integer.parseInt(cursor.getString(6)));
                payment.setIsPosted(Integer.parseInt(cursor.getString(7)));
                payment.setPayMethod(Integer.parseInt(cursor.getString(8)));
                payment.setCustName(cursor.getString(9));
                payment.setYear(Integer.parseInt(cursor.getString(10)));

                paymentsList.add(payment);
            } while (cursor.moveToNext());
        }

        return paymentsList;
    }

    public List<Payment> getAllPaymentsPaper() {

        List<Payment> paymentsList = new ArrayList<Payment>();
        String selectQuery = "SELECT  * FROM " + PAYMENTS_PAPER;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();

                payment.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                payment.setVoucherNumber(Integer.parseInt(cursor.getString(1)));
                payment.setCheckNumber(Integer.parseInt(cursor.getString(2)));
                payment.setBank(cursor.getString(3));
                payment.setDueDate(cursor.getString(4));
                payment.setAmount(Double.parseDouble(cursor.getString(5)));
                payment.setIsPosted(Integer.parseInt(cursor.getString(6)));
                payment.setYear(Integer.parseInt(cursor.getString(7)));

                paymentsList.add(payment);
            } while (cursor.moveToNext());
        }

        return paymentsList;
    }

    public List<Voucher> getStockRequestVouchers() {
        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query
        String selectQuery = "SELECT  VOUCHER_NUMBER , VOUCHER_DATE , REMARK FROM " + REQUEST_MASTER;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Voucher Voucher = new Voucher();

                Voucher.setVoucherNumber(Integer.parseInt(cursor.getString(0)));
                Voucher.setVoucherDate(cursor.getString(1));
                Voucher.setRemark(cursor.getString(2));

                vouchers.add(Voucher);
            } while (cursor.moveToNext());
        }

        return vouchers;
    }

    public List<Voucher> getAllStockRequestVouchers() {
        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + REQUEST_MASTER;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Voucher Voucher = new Voucher();

                Voucher.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                Voucher.setVoucherNumber(Integer.parseInt(cursor.getString(1)));
                Voucher.setVoucherDate(cursor.getString(2));
                Voucher.setSaleManNumber(Integer.parseInt(cursor.getString(3)));
                Voucher.setRemark(cursor.getString(4));
                Voucher.setTotalQty(Integer.parseInt(cursor.getString(5)));
                Voucher.setIsPosted(Integer.parseInt(cursor.getString(6)));

                vouchers.add(Voucher);
            } while (cursor.moveToNext());
        }

        return vouchers;
    }

    public List<Item> getAllStockRequestItems() {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + REQUEST_DETAILS;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();

                item.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                item.setVoucherNumber(Integer.parseInt(cursor.getString(1)));
                item.setItemNo(cursor.getString(2));
                item.setItemName(cursor.getString(3));
                item.setQty(Integer.parseInt(cursor.getString(4)));
                item.setDate(cursor.getString(5));

                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public List<Item> getStockRequestItems() {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  ITEM_NUMBER , ITEM_NAME , UNIT_QTY , VOUCHER_DATE , VOUCHER_NUMBER FROM " + REQUEST_DETAILS;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();

                item.setItemNo(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                item.setQty(Integer.parseInt(cursor.getString(2)));
                item.setDate(cursor.getString(3));
                item.setVoucherNumber(Integer.parseInt(cursor.getString(4)));

                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public List<AddedCustomer> getAllAddedCustomer() {
        List<AddedCustomer> customers = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + ADDED_CUSTOMER;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddedCustomer customer = new AddedCustomer();

                customer.setCustName(cursor.getString(0));
                customer.setRemark(cursor.getString(1));
                customer.setLatitude(Double.parseDouble(cursor.getString(2)));
                customer.setLongtitude(Double.parseDouble(cursor.getString(3)));
                customer.setSalesMan(cursor.getString(4));
                customer.setIsPosted(Integer.parseInt(cursor.getString(5)));
                customer.setSalesmanNo(cursor.getString(6));

                customers.add(customer);
            } while (cursor.moveToNext());
        }

        return customers;
    }

    public List<Offers> getAllOffers() {
        List<Offers> offers = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + VS_PROMOTION;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Offers offer = new Offers();
                offer.setPromotionID(Integer.parseInt(cursor.getString(0)));
                offer.setPromotionType(Integer.parseInt(cursor.getString(1)));
                offer.setFromDate(cursor.getString(2));
                offer.setToDate(cursor.getString(3));
                offer.setItemNo(cursor.getString(4));
                offer.setItemQty(Double.parseDouble(cursor.getString(5)));
                offer.setBonusQty(Double.parseDouble(cursor.getString(6)));
                offer.setBonusItemNo(cursor.getString(7));

                offers.add(offer);
            } while (cursor.moveToNext());
        }

        return offers;
    }

    public List<SalesmanStations> getAllSalesmanSatation(String salesmanNo , String date) {
        List<SalesmanStations> stations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SALESMEN_STATIONS + " where SALESMEN_NO = '" + salesmanNo + "' and DATE_ = '" + date + " 00:00:00'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SalesmanStations station = new SalesmanStations();

                station.setSalesmanNo(cursor.getString(0));
                station.setDate(cursor.getString(1).replaceAll("-", "/"));
                station.setLatitude(cursor.getString(2));
                station.setLongitude(cursor.getString(3));
                station.setSerial(Integer.parseInt(cursor.getString(4)));
                station.setCustNo(cursor.getString(5));
                station.setCustName(cursor.getString(6));

                stations.add(station);
            } while (cursor.moveToNext());
        }

        return stations;
    }

    public int getIsPosted(int salesMan) {
        String selectQuery = "select IS_POSTED  from " + SALES_VOUCHER_MASTER +
                " where SALES_MAN_NUMBER = '" + salesMan + "'" ;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int isPosted = 1;
        if (cursor.moveToFirst()) {
            do {
//                Log.e("isPosted***" , cursor.getString(0));
                if (Integer.parseInt(cursor.getString(0)) == 0) {
                    isPosted = 0;
                    break;
                }
            } while (cursor.moveToNext());
        }
        return isPosted;
    }


    public void updateTransaction(String cusCode, String currentDate, String currentTime) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CHECK_OUT_DATE, currentDate);
        values.put(CHECK_OUT_TIME, currentTime);
        values.put(STATUS, 1);

        // updating row
        db.update(TABLE_TRANSACTIONS, values, CUS_CODE + "=" + cusCode + " AND " + STATUS + "=" + 0, null);
    }

    public void updateVoucher() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(SALES_VOUCHER_MASTER, values, IS_POSTED + "=" + 0, null);
    }

    public void updateVoucherDetails() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(SALES_VOUCHER_DETAILS, values, IS_POSTED + "=" + 0, null);
    }

    public void updatePayment() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(PAYMENTS, values, IS_POSTED + "=" + 0, null);
    }

    public void updatePaymentPaper() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(PAYMENTS_PAPER, values, IS_POSTED + "=" + 0, null);
    }

    public void updateAddedCustomers() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(ADDED_CUSTOMER, values, IS_POSTED + "=" + 0, null);
    }

    public void updateTransactions() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED2, 1);
        db.update(TABLE_TRANSACTIONS, values, IS_POSTED + "=" + 0, null);
    }

    public void updateCustomersMaster(String lat , String lon , int custId) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CUST_LAT, lat);
        values.put(CUST_LONG, lon);

        db.update(CUSTOMER_MASTER, values, CUS_ID + "=" + custId, null);
    }

    public void updateSalesManItemsBalance1(float qty , int salesMan, String itemNo) {
        db = this.getWritableDatabase();

        String selectQuery = "select Qty from SalesMan_Items_Balance " +
                " where ItemNo = " + itemNo + " and SalesManNo = " + salesMan;

        Cursor cursor = db.rawQuery(selectQuery, null);
        float existQty = 0;
        if (cursor.moveToFirst()) {
            do {
                existQty = Float.parseFloat(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        existQty -= qty;

        Log.e("qty1 ***" , ""+existQty);

        ContentValues values = new ContentValues();
        values.put(Qty5, existQty);
        db.update(SalesMan_Items_Balance, values, SalesManNo5 + " = " + salesMan + " and " + ItemNo5 + " = " + itemNo, null);
    }

    public void updateSalesManItemsBalance2(float qty , int salesMan, String itemNo) {
        db = this.getWritableDatabase();

        String selectQuery = "select Qty from SalesMan_Items_Balance " +
                " where ItemNo = " + itemNo + " and SalesManNo = " + salesMan;

        Cursor cursor = db.rawQuery(selectQuery, null);
        float existQty = 0;
        if (cursor.moveToFirst()) {
            do {
                existQty = Float.parseFloat(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        existQty += qty;

        Log.e("qty2 ***" , ""+existQty);

        ContentValues values = new ContentValues();
        values.put(Qty5, existQty);
        db.update(SalesMan_Items_Balance, values, SalesManNo5 + " = " + salesMan + " and " + ItemNo5 + " = " + itemNo, null);
    }

    public void deleteVoucher(int voucherNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_TRANSACTIONS, SALES_MAN_ID + " = ?",
//                new String[] { String.valueOf(Id) });
        db.execSQL("delete from " + SALES_VOUCHER_MASTER + " where VOUCHER_NUMBER = " + voucherNumber);
        db.execSQL("delete from " + SALES_VOUCHER_DETAILS + " where VOUCHER_NUMBER = " + voucherNumber);
        db.close();
    }

    public void deleteVoucher2(int voucherNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + PAYMENTS + " where VOUCHER_NUMBER = " + voucherNumber);
        db.close();
    }

    public void deleteAllCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CUSTOMER_MASTER);
        db.close();
    }

    public void deleteAllItemUnitDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Item_Unit_Details);
        db.close();
    }

    public void deleteAllItemsMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Items_Master);
        db.close();
    }

    public void deleteAllPriceListD() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Price_List_D);
        db.close();
    }

    public void deleteAllPriceListM() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Price_List_M);
        db.close();
    }

    public void deleteAllSalesTeam() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Sales_Team);
        db.close();
    }

    public void deleteAllSalesManItemsBalance() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SalesMan_Items_Balance);
        db.close();
    }

    public void deleteAllSalesmanAndStoreLink() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SalesmanAndStoreLink);
        db.close();
    }

    public void deleteAllSalesmen() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SalesMen);
        db.close();
    }

    public void deleteAllSettings() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SETTING);
        db.close();
    }

    public void deleteAllCompanyInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + COMPANY_INFO);
        db.close();
    }

    public void deleteAllCustomerPrice() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CustomerPrices);
        db.close();
    }

    public void deleteAllOffers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + VS_PROMOTION);
        db.close();
    }

    public void deleteAllSalesmenStations() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SALESMEN_STATIONS);
        db.close();
    }

    public void deleteAllPostedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SALES_VOUCHER_MASTER + " where IS_POSTED = '1' ");
        db.execSQL("delete from " + SALES_VOUCHER_DETAILS + " where IS_POSTED = '1' ");
        db.execSQL("delete from " + PAYMENTS + " where IS_POSTED = '1' ");
        db.execSQL("delete from " + PAYMENTS_PAPER + " where IS_POSTED = '1' ");
        db.close();
    }

}
