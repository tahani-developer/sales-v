package com.dr7.salesmanmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
//import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.PrinterSetting;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.SalesManAndStoreLink;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.VisitRate;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.activeKey;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.SalesMan;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class

DatabaseHandler extends SQLiteOpenHelper {

    private static String TAG = "DatabaseHandler";
    // Database Version
    private static final int DATABASE_VERSION = 104;

    // Database Name
    private static final String DATABASE_NAME = "VanSalesDatabase";
    static SQLiteDatabase db;
    // tables from JSON
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
//
    private static final String SERIAL_ITEMS_TABLE  = "SERIAL_ITEMS_TABLE";
    private static final String  KEY_SERIAL="KEY_SERIAL";
    private static final String SERIAL_CODE_NO ="SERIAL_CODE_NO";
    private static final String COUNTER_SERIAL ="COUNTER_SERIAL";
    private static final String  VOUCHER_NO="VOUCHER_NO";
    private static final String ITEMNO_SERIAL="ITEMNO_SERIAL";
    private static final String DATE_VOUCHER="DATE_VOUCHER";
    private static final  String KIND_VOUCHER="KIND_VOUCHER";
    private static final String  STORE_NO_SALESMAN="STORE_NO_SALESMAN";
    private static final String  IS_POSTED_SERIAL="IS_POSTED_SERIAL";
    private static final String  IS_BONUS_SERIAL="IS_BONUS_SERIAL";
    //----------------------------------------------------------------------
    private static final String PASSWORD_TABLE  = "PASSWORD_TABLE";
    private static final String PASS_TYPE = "PASS_TYPE";
    private static final String PASS_NO = "PASS_NO";
    //----------------------------------------------------------------------

    private static final String CUSTOMER_LOCATION  = "CUSTOMER_LOCATION";
    private static final String CUS_NO = "CUS_NO";
    private static final String LONG = "LONG";
    private static final String LATIT = "LATIT";
    //----------------------------------------------------------------------

    private static final String ACCOUNT_REPORT  = "ACCOUNT_REPORT";

    private static final String DATE = "DATE";
    private static final String TRANSFER_NAME = "TRANSFER_NAME";
    private static final String DEBTOR = "DEBTOR";
    private static final String CREDITOR = "CREDITOR";
    private static final String CUST_BALANCE = "CUST_BALANCE";
    private static final String CUST_NUMBER_REPORT = "CUST_NUMBER_REPORT";

//----------------------------------------------------------------------

    private static final String ITEMS_QTY_OFFER  = "ITEMS_QTY_OFFER";

    private static final String ITEMNAME = "ITEMNAME";
    private static final String ITEMNO = "ITEMNO";
    private static final String AMOUNT_QTY = "AMOUNT_QTY";
    private static final String FROMDATE = "FROMDATE";
    private static final String TODATE = "TODATE";
    private static final String DISCOUNT = "DISCOUNT";

    //------------------------------------------------------------------
    private static final String QTY_OFFERS="QTY_OFFERS";
    private static final String QTY ="QTY";
    private static final String DISCOUNT_VALUE ="DISCOUNT_VALUE";
    private static final String PAYMENT_TYPE ="PAYMENT_TYPE";
    //__________________________________________________________________
    private static final String ACTIVE_KEY="ACTIVE_KEY";
    private static final String KEY_VALUE ="KEY_VALUE";
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

    private static final String PRINTER_SETTING_TABLE="PRINTER_SETTING_TABLE";
    private static final String PRINTER_SETTING ="PRINTER_SETTING";
    private static final String PRINTER_SHAPE ="PRINTER_SHAPE";
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
    private static final String MAX_DISCOUNT = "MAX_DISCOUNT";
    private static final String ACCPRC = "ACCPRC";
    private static final String HIDE_VAL = "HIDE_VAL";

    private static final String IS_POST = "IS_POST";
    private static final String CUS_ID_Text="CUS_ID_Text";


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
    private static final String ITEM_F_D = "F_D";
    private static final String KIND_ITEM= "KIND_ITEM";
    private static final String ITEM_HAS_SERIAL= "ITEM_HAS_SERIAL";
    private static final String ITEM_PHOTO= "ITEM_PHOTO";
    /*byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); */
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
    private static final String WORK_ONLINE = "WORK_ONLINE";
    private static final String PAYMETHOD_CHECK = "PAYMETHOD_CHECK";
    private static final String BONUS_NOT_ALLOWED = "BONUS_NOT_ALLOWED";
    private static final String NO_OFFERS_FOR_CREDIT_INVOICE="NO_OFFERS_FOR_CREDIT_INVOICE";
    private static final String AMOUNT_OF_MAX_DISCOUNT="AMOUNT_OF_MAX_DISCOUNT";
    private static final String Customer_Authorized="Customer_Authorized";
    private static final String Password_Data="Password_Data";
    private static final String Arabic_Language="Arabic_Language";
    private static final String HideQty="HideQty";
    private static final String LockCashReport="LockCashReport";
    private static final String salesManName="salesManName";
    private static final String PreventOrder="PreventOrder";
    private static final String RequiredNote="RequiredNote";
    private static final String PreventTotalDiscount="PreventTotalDiscount";
    private static final String AutomaticCheque="AutomaticCheque";
    private static final String Tafqit="Tafqit";
    private static final String  PreventChangPayMeth="PreventChangPayMeth";
    private static final String ShowCustomerList="ShowCustomerList";
    private static final String NoReturnInvoice="NoReturnInvoice";
    private static final String WORK_WITH_SERIAL="WORK_WITH_SERIAL";
    private static final String SHOW_IMAGE_ITEM="SHOW_IMAGE_ITEM";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String COMPANY_INFO = "COMPANY_INFO";

    private static final String COMPANY_NAME = "COMPANY_NAME";
    private static final String COMPANY_TEL = "COMPANY_TEL";
    private static final String TAX_NO = "TAX_NO";
    private static final String LOGO = "LOGO";
    private static final String NOTE = "NOTE";

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
    private static final String ITEM_DESCRIPTION = "ITEM_DESCRIPTION";
    private static final String SERIAL_CODE = "SERIAL_CODE";
    private static final String VOUCH_DATE = "VOUCH_DATE";

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

        String CREATE_SERIAL_ITEMS_TABLE= "CREATE TABLE " + SERIAL_ITEMS_TABLE + "("
                + KEY_SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SERIAL_CODE_NO + " TEXT,"
                + COUNTER_SERIAL + " INTEGER,"
                + VOUCHER_NO + " INTEGER,"
                + ITEMNO_SERIAL + " TEXT,"
                + KIND_VOUCHER + " TEXT,"

                + DATE_VOUCHER + " TEXT,"
                + STORE_NO_SALESMAN + " INTEGER,"
                + IS_POSTED_SERIAL + " INTEGER,"+
                IS_BONUS_SERIAL+" INTEGER"+


                ")";
        db.execSQL(CREATE_SERIAL_ITEMS_TABLE);
        //-------------------------------------------------------------------------
        String CREATE_TABLE_PASSWORD_TABLE= "CREATE TABLE " + PASSWORD_TABLE + "("
                + PASS_TYPE + " INTEGER,"
                + PASS_NO + " TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_PASSWORD_TABLE);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ


        String CREATE_TABLE_CUSTOMER_LOCATION= "CREATE TABLE " + CUSTOMER_LOCATION + "("
                + CUS_NO + " TEXT,"
                + LONG + " TEXT,"
                + LATIT + " TEXT"+

                ")";
        db.execSQL(CREATE_TABLE_CUSTOMER_LOCATION);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_ACCOUNT_REPORT= "CREATE TABLE " + ACCOUNT_REPORT + "("
                + DATE + " TEXT,"
                + TRANSFER_NAME + " TEXT,"
                + DEBTOR + " TEXT,"
                + CREDITOR + " TEXT,"
                + TODATE + " TEXT,"
                + CUST_BALANCE + " TEXT,"
                 +CUST_NUMBER_REPORT + " TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_ACCOUNT_REPORT);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ


        String CREATE_TABLE_ITEMS_QTY_OFFER= "CREATE TABLE " + ITEMS_QTY_OFFER + "("
                + ITEMNAME + " TEXT,"
                + ITEMNO + " INTEGER,"
                + AMOUNT_QTY + " INTEGER,"
                + FROMDATE + " TEXT,"
                + TODATE + " TEXT,"
                + DISCOUNT + " REAL" + ")";
        db.execSQL(CREATE_TABLE_ITEMS_QTY_OFFER);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_QTY_OFFERS = "CREATE TABLE " + QTY_OFFERS + "("
                + QTY + " REAL,"
                + DISCOUNT_VALUE + " REAL,"
                + PAYMENT_TYPE + " INTEGER"+ ")";

        db.execSQL(CREATE_TABLE_QTY_OFFERS);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        String CREATE_TABLE_CUSTOMER_MASTER = "CREATE TABLE " + CUSTOMER_MASTER + "("
                + COMPANY_NUMBER0 + " INTEGER,"
                + CUS_ID + " TEXT,"
                + CUS_NAME0 + " TEXT,"
                + ADDRESS + " TEXT,"
                + IS_SUSPENDED + " INTEGER,"
                + PRICE_LIST_ID + " TEXT,"
                + CASH_CREDIT + " INTEGER,"
                + SALES_MAN_NO + " TEXT,"
                + CREDIT_LIMIT + " INTEGER,"
                + PAY_METHOD0 + " INTEGER,"
                + CUST_LAT + " TEXT not null default '',"
                + CUST_LONG + " TEXT not null default '',"
                + MAX_DISCOUNT + " REAL,"
                +ACCPRC+ " TEXT,"
                +HIDE_VAL+ " INTEGER,"

                +IS_POST+" INTEGER not null default  0,"
                +CUS_ID_Text+ " TEXT"




                + ")";

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
                + ITEM_L1 + " INTEGER,"
                + ITEM_F_D + " REAL,"
                + KIND_ITEM + " TEXT,"
                + ITEM_HAS_SERIAL + " INTEGER,"
                 +ITEM_PHOTO + " TEXT"

                + ")";

        db.execSQL(CREATE_TABLE_Items_Master);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        String CREATE_ACTIVE_KEY = "CREATE TABLE " + ACTIVE_KEY + "("

                + KEY_VALUE + " INTEGER"+ ")";
        db.execSQL(CREATE_ACTIVE_KEY);
        //-----------------------------------------------------

        String CREATE_PRINTER_SETTING_TABLE = "CREATE TABLE " + PRINTER_SETTING_TABLE + "("
                + PRINTER_SETTING +" INTEGER ,"
                + PRINTER_SHAPE + " INTEGER"+ ")";
        db.execSQL(CREATE_PRINTER_SETTING_TABLE);
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
                + READ_DISCOUNT_FROM_OFFERS + " INTEGER,"
                + WORK_ONLINE + " INTEGER,"
                + PAYMETHOD_CHECK + " INTEGER,"
                + BONUS_NOT_ALLOWED + " INTEGER,"
                + NO_OFFERS_FOR_CREDIT_INVOICE + " INTEGER, "
                + AMOUNT_OF_MAX_DISCOUNT + " INTEGER,"
                + Customer_Authorized + " INTEGER,"
                + Password_Data + " INTEGER,"
                + Arabic_Language + " INTEGER,"
                + HideQty + " INTEGER,"
                + LockCashReport + " INTEGER,"
                + salesManName + " TEXT,"
                + PreventOrder + " INTEGER,"
                + RequiredNote + " INTEGER,"
                + PreventTotalDiscount + " INTEGER,"
                + AutomaticCheque + " INTEGER,"
                + Tafqit + " INTEGER,"
                + PreventChangPayMeth + " INTEGER,"
                + ShowCustomerList + " INTEGER DEFAULT 1,"
                + NoReturnInvoice + " INTEGER,"
                + WORK_WITH_SERIAL + " INTEGER,"
                + SHOW_IMAGE_ITEM + " INTEGER"


        + ")";
        db.execSQL(CREATE_TABLE_SETTING);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_COMPANY_INFO = "CREATE TABLE " + COMPANY_INFO + "("
                + COMPANY_NAME + " TEXT,"
                + COMPANY_TEL + " INTEGER,"
                + TAX_NO + " INTEGER,"
                + LOGO + " BLOB,"
                + NOTE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_COMPANY_INFO);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SALES_VOUCHER_MASTER = "CREATE TABLE " + SALES_VOUCHER_MASTER + "("
                + COMPANY_NUMBER + " INTEGER,"
                + VOUCHER_NUMBER + " INTEGER UNIQUE, "
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
                + IS_POSTED1 + " INTEGER,"
                + ITEM_DESCRIPTION+ " TEXT,"
                + SERIAL_CODE+ " TEXT,"

                + VOUCH_DATE+ " TEXT"



        + ")";
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


        try{
            db.execSQL("ALTER TABLE SETTING ADD Customer_Authorized INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD Password_Data INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD Arabic_Language INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }


        try{
            db.execSQL("ALTER TABLE SETTING ADD HideQty  INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD LockCashReport  INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD salesManName  TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD PreventOrder  INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SETTING ADD RequiredNote  INTEGER NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD PreventTotalDiscount  INTEGER NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD AutomaticCheque  INTEGER NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD Tafqit  INTEGER NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try
        {
            db.execSQL("ALTER TABLE SETTING ADD PreventChangPayMeth  INTEGER NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try
        {
            db.execSQL("ALTER TABLE SETTING ADD ShowCustomerList  INTEGER NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try
        {
            db.execSQL("ALTER TABLE SETTING ADD NoReturnInvoice  INTEGER NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try
        {
            db.execSQL("ALTER TABLE SETTING ADD WORK_WITH_SERIAL  INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try
        {
            db.execSQL("ALTER TABLE SETTING ADD SHOW_IMAGE_ITEM  INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

//**************************************End Table setting *************************************************************
        try{
            db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD ITEM_DESCRIPTION  TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD HIDE_VAL  TEXT NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD ACCPRC  TEXT NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE Items_Master ADD KIND_ITEM  TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }



        try{
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD  MAX_DISCOUNT  REAL NOT NULL DEFAULT '0'");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE QTY_OFFERS ADD  PAYMENT_TYPE  INTEGER NOT NULL DEFAULT '0'");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD PRINT_METHOD TEXT NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }



        try{
            db.execSQL("ALTER TABLE VISIT_RATE ADD CUST_CODE TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE VISIT_RATE ADD CUST_NAME TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE VISIT_RATE ADD SALESMAN TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE VISIT_RATE ADD SALESMAN TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SALESMEN_STATIONS ADD CUSTOMR_NO TEXT  NOT NULL DEFAULT '111'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SETTING ADD ALLOW_OUT_OF_RANGE INTEGER  NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SETTING ADD CAN_CHANGE_PRICE INTEGER  NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }


        try{
            db.execSQL("ALTER TABLE Items_Master ADD F_D REAL  NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SETTING ADD BONUS_NOT_ALLOWED INTEGER  NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD NO_OFFERS_FOR_CREDIT_INVOICE INTEGER  NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SETTING ADD AMOUNT_OF_MAX_DISCOUNT INTEGER NOT NULL   DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SETTING ADD WORK_ONLINE INTEGER  NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SETTING ADD PAYMETHOD_CHECK INTEGER  NOT NULL DEFAULT '1'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE TRANSACTIONS ADD IS_POSTED2 INTEGER  NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD PRINTER_SHAPE  INTEGER NOT NULL DEFAULT '0'");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            String CREATE_TABLE_ACCOUNT_REPORT= "CREATE TABLE " + ACCOUNT_REPORT + "("
                    + DATE + " TEXT,"
                    + TRANSFER_NAME + " TEXT,"
                    + DEBTOR + " TEXT,"
                    + CREDITOR + " TEXT,"
                    + TODATE + " TEXT,"
                    + CUST_BALANCE + " TEXT,"
                    +CUST_NUMBER_REPORT + " TEXT"
                    + ")";
            db.execSQL(CREATE_TABLE_ACCOUNT_REPORT);


        }catch (Exception e){    Log.e(TAG, e.getMessage().toString());}

        try{

            String CREATE_TABLE_SALESMEN_STATIONS = "CREATE TABLE " + SALESMEN_STATIONS + "("
                    + SALESMEN_NO + " TEXT,"
                    + DATE_ + " TEXT,"
                    + LATITUDE + " TEXT,"
                    + LONGITUDE + " TEXT,"
                    + SERIAL + " INTEGER,"
                    + CUSTOMR_NO + " TEXT,"
                    + CUSUSTOMR_NAME + " TEXT" + ")";
            db.execSQL(CREATE_TABLE_SALESMEN_STATIONS);

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try {


            String CREATE_ACTIVE_KEY = "CREATE TABLE " + ACTIVE_KEY + "("

                    + KEY_VALUE + " INTEGER"+ ")";
            db.execSQL(CREATE_ACTIVE_KEY);


        } catch (Exception e) {
            Log.e("onUpgrade*****", "duplicated column");
        }


        try {
            String CREATE_PRINTER_SETTING_TABLE = "CREATE TABLE " + PRINTER_SETTING_TABLE + "("

                    + PRINTER_SETTING + " INTEGER"+ ")";
            db.execSQL(CREATE_PRINTER_SETTING_TABLE);


        }
        catch (Exception e) {
            Log.e("onUpgrade*****", "duplicated column PRINTER_SETTING_TABLE");
        }
        try {
        String CREATE_TABLE_QTY_OFFERS = "CREATE TABLE " + QTY_OFFERS + "("
                + QTY + " REAL,"
                + DISCOUNT_VALUE + " REAL,"
                + PAYMENT_TYPE + " INTEGER"+ ")";
        db.execSQL(CREATE_TABLE_QTY_OFFERS);
        }
        catch (Exception e) {
            Log.e("onUpgrade*****", "duplicated column QTY_OFFERS");
        }


        try {
            String CREATE_TABLE_ITEMS_QTY_OFFER = "CREATE TABLE " + ITEMS_QTY_OFFER + "("
                    + ITEMNAME + " TEXT,"
                    + ITEMNO + " INTEGER,"
                    + AMOUNT_QTY + " INTEGER,"
                    + FROMDATE + " TEXT,"
                    + TODATE + " TEXT,"
                    + DISCOUNT + " REAL" + ")";
            db.execSQL(CREATE_TABLE_ITEMS_QTY_OFFER);
        }
          catch (Exception e) {
                Log.e("onUpgrade*****", "duplicated column ItemsQtyOffer");
            }
        try {
            String CREATE_TABLE_CUSTOMER_LOCATION = "CREATE TABLE " + CUSTOMER_LOCATION + "("
                    + CUS_NO + " TEXT,"
                    + LONG + " TEXT,"
                    + LATIT + " TEXT" +

                    ")";
            db.execSQL(CREATE_TABLE_CUSTOMER_LOCATION);
        }
        catch (Exception e) {
            Log.e("onUpgrade*****", "CREATE_TABLE_CUSTOMER_LOCATION");
        }

        try{
            db.execSQL("ALTER TABLE COMPANY_INFO ADD NOTE  TEXT NOT NULL DEFAULT ''");
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{


            db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  SERIAL_CODE  INTEGER  DEFAULT 0 ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }

        try{
            db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  VOUCH_DATE  TEXT NOT NULL DEFAULT '' ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD  IS_POST  INTEGER  DEFAULT 0 ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("alter table CUSTOMER_MASTER ADD COLUMN CUS_ID_Text TEXT NOT NULL DEFAULT '' ");
            db.execSQL("update CUSTOMER_MASTER set CUS_ID_Text = CUS_ID ");
//            db.execSQL("update CUSTOMER_MASTER set CUS_ID = '' ");
            //update CUSTOMER_MASTER set new_CUS_ID = CUS_ID;
            //update CUSTOMER_MASTER set CUS_ID = '';

        } catch (Exception e) {

        }
        try {
            String CREATE_SERIAL_ITEMS_TABLE = "CREATE TABLE " + SERIAL_ITEMS_TABLE + "("
                    + KEY_SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SERIAL_CODE_NO + " TEXT,"
                    + COUNTER_SERIAL + " INTEGER,"
                    + VOUCHER_NO + " INTEGER,"
                    + ITEMNO_SERIAL + " TEXT,"

                    + KIND_VOUCHER + " TEXT,"

                    + DATE_VOUCHER + " TEXT,"
                    + STORE_NO_SALESMAN + " INTEGER,"
                    + IS_POSTED_SERIAL + " INTEGER,"+
                    IS_BONUS_SERIAL+" INTEGER"+

                    ")";
            db.execSQL(CREATE_SERIAL_ITEMS_TABLE);
        }
        catch (Exception e) {

            Log.e("SERIAL_ITEMS_TABLE",""+e.getMessage());
            }
        try{
            db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  STORE_NO_SALESMAN  INTEGER  DEFAULT 1 ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }


        try{
            db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  IS_POSTED_SERIAL  INTEGER  DEFAULT 0 ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{
            db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  IS_BONUS_SERIAL  INTEGER  DEFAULT 0 ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        //********************************************* End SERIAL_ITEMS_TABLE ****************************************************
        try{
            db.execSQL("ALTER TABLE Items_Master ADD  ITEM_HAS_SERIAL  INTEGER  DEFAULT 0 ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        try{

            db.execSQL("ALTER TABLE Items_Master ADD  ITEM_PHOTO  TEXT  DEFAULT '' ");

        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage().toString());
        }
        //



    }
    public void addAccount_report(Account_Report account_report)
    {
        try {
            db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DATE, account_report.getDate());
            values.put(TRANSFER_NAME,account_report.getTransfer_name());
            values.put(DEBTOR,account_report.getDebtor());
            values.put(CREDITOR, account_report.getCreditor());
            values.put(CUST_BALANCE, account_report.getCust_balance());
            values.put(CUST_NUMBER_REPORT, account_report.getCust_no());
            db.insert(ACCOUNT_REPORT, null, values);
            db.close();
        }
        catch (Exception e){
            Log.e("DBAccount_Report",""+e.getMessage());

        }

    }

    public void addCustomerLocation(CustomerLocation customerLocation)
    {
        try {
            db = this.getReadableDatabase();
            ContentValues values = new ContentValues();

            values.put(CUS_NO, customerLocation.getCUS_NO());
            values.put(LONG,customerLocation.getLONG());
            values.put(LATIT,customerLocation.getLATIT());

            db.insert(CUSTOMER_LOCATION, null, values);
            db.close();
        }
        catch (Exception e){
            Log.e("DBAccount_Report",""+e.getMessage());

        }

    }

    public void add_Items_Qty_Offer(ItemsQtyOffer itemsQtyOffer)
    {
        try {
            db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(ITEMNAME, itemsQtyOffer.getItem_name());
            values.put(ITEMNO, itemsQtyOffer.getItem_no());
            values.put(AMOUNT_QTY, itemsQtyOffer.getItemQty());
            values.put(FROMDATE, itemsQtyOffer.getFromDate());
            values.put(TODATE, itemsQtyOffer.getToDate());
            values.put(DISCOUNT, itemsQtyOffer.getDiscount_value());
            db.insert(ITEMS_QTY_OFFER, null, values);
            db.close();
        }
        catch (Exception e){
            Log.e("Dbhandler_addItemQOf",""+e.getMessage());

        }

    }
    public void add_Serial(serialModel serialModelItem)
    {
        try {



            db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(SERIAL_CODE_NO, serialModelItem.getSerialCode());
            values.put(COUNTER_SERIAL, serialModelItem.getCounterSerial());
            values.put(VOUCHER_NO, serialModelItem.getVoucherNo());
            values.put(ITEMNO_SERIAL, serialModelItem.getItemNo());
            values.put(DATE_VOUCHER, serialModelItem.getDateVoucher());
            values.put(KIND_VOUCHER, serialModelItem.getKindVoucher());
            values.put(STORE_NO_SALESMAN, serialModelItem.getStoreNo());
            values.put(IS_POSTED_SERIAL, "0");
            values.put(IS_BONUS_SERIAL, serialModelItem.getIsBonus());
            db.insert(SERIAL_ITEMS_TABLE, null, values);
            Log.e("add_Serial",""+serialModelItem.getSerialCode());
            db.close();
        }
        catch (Exception e){
            Log.e("Dbhandler_addItemQOf",""+e.getMessage());

        }

    }



    public void addQtyOffers(QtyOffers qtyOffers)
    {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(QTY,qtyOffers.getQTY());
        values.put(DISCOUNT_VALUE,qtyOffers.getDiscountValue());
        values.put(PAYMENT_TYPE,qtyOffers.getPaymentType());
        db.insert(QTY_OFFERS, null, values);
        db.close();

    }

    public void addKey(activeKey keyvalue)
    {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE,keyvalue.getKey());
        db.insert(ACTIVE_KEY, null, values);
        db.close();

    }

    public void addPrinterSeting(PrinterSetting printer)
    {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRINTER_SETTING,printer.getPrinterName());
        values.put(PRINTER_SHAPE,printer.getPrinterShape());
        db.insert(PRINTER_SETTING_TABLE, null, values);
        db.close();

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
        values.put(MAX_DISCOUNT, customer.getMax_discount());
        values.put(ACCPRC, customer.getACCPRC());
        values.put(HIDE_VAL, customer.getHide_val());
        values.put(IS_POST, 0);
        values.put(CUS_ID_Text,customer.getCustomerIdText());

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
//        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo1, item.getCompanyNo());
        values.put(ItemNo1, item.getItemNo());
        values.put(Name1, item.getName());
        values.put(CateogryID1, item.getCategoryId());
        values.put(Barcode1, item.getBarcode());
        values.put(IsSuspended1, item.getIsSuspended());
        values.put(ITEM_L1, item.getItemL());
        values.put(ITEM_F_D, item.getPosPrice());
        values.put(KIND_ITEM,item.getKind_item());

//        if(ITEM_HAS_SERIAL)
        values.put(ITEM_HAS_SERIAL,item.getItemHasSerial());
        values.put(ITEM_PHOTO,item.getPhotoItem());



        db.insert(Items_Master, null, values);

//        db.setTransactionSuccessful();
//        db.endTransaction();
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
//        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo5, balance.getCompanyNo());
        values.put(SalesManNo5, balance.getSalesManNo());
        values.put(ItemNo5, balance.getItemNo());
        values.put(Qty5, balance.getQty());

        db.insert(SalesMan_Items_Balance, null, values);
//
//        db.setTransactionSuccessful();
//        db.endTransaction();
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
//        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(ItemNumber, price.getItemNumber());
        values.put(CustomerNumber, price.getCustomerNumber());
        values.put(Price, price.getPrice());

        db.insert(CustomerPrices, null, values);

//        db.setTransactionSuccessful();
//        db.endTransaction();
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
                           int allowMinus, int numOfCopy, int salesManCustomers, int minSalePrice, int printMethod, int allowOutOfRange,int canChangePrice,int readDiscount,
                           int workOnline,int  payMethodCheck,int bonusNotAlowed,int noOfferForCredid,int amountOfMaxDiscount,int customerOthoriz,
                           int passowrdData,int arabicLanguage,int hideQty,int lock_cashreport,String salesman_name,int preventOrder,int requiNote,int preventDiscTotal,
                           int automaticCheque,int tafqit,int preventChangPayMeth,int showCustomer,int noReturnInvoi, int Work_serialNo,int itemPhoto) {
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
        values.put(WORK_ONLINE, workOnline);
        values.put(PAYMETHOD_CHECK, payMethodCheck);
        values.put(BONUS_NOT_ALLOWED,bonusNotAlowed);//16
        values.put(NO_OFFERS_FOR_CREDIT_INVOICE,noOfferForCredid);
        values.put(AMOUNT_OF_MAX_DISCOUNT,amountOfMaxDiscount);
        values.put(Customer_Authorized,customerOthoriz);
        values.put(Password_Data,passowrdData);
        values.put(Arabic_Language,arabicLanguage);
        values.put(HideQty,hideQty);
        values.put(LockCashReport,lock_cashreport);
        values.put(salesManName,salesman_name);
        values.put(PreventOrder,preventOrder);
        values.put(RequiredNote,requiNote);
        values.put(PreventTotalDiscount,preventDiscTotal);
        values.put(AutomaticCheque,automaticCheque);
        values.put(Tafqit,tafqit);
        values.put(PreventChangPayMeth,preventChangPayMeth);
        values.put(ShowCustomerList,showCustomer);
        values.put(NoReturnInvoice,noReturnInvoi);
        values.put(WORK_WITH_SERIAL,Work_serialNo);
        values.put(SHOW_IMAGE_ITEM,itemPhoto);




        db.insert(TABLE_SETTING, null, values);
        db.close();
    }

    public void addCompanyInfo(String companyName, int companyTel, int taxNo, Bitmap logo,String note) {
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
        values.put(NOTE, note);


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
        values.put(ITEM_DESCRIPTION, item.getDescription());

        try {
            if(item.getSerialCode()==null)
            {
                values.put(SERIAL_CODE, "0");
            }
            else {   values.put(SERIAL_CODE, item.getSerialCode());}


            if(item.getVouchDate()!=null || !item.getVouchDate().equals(""))
            values.put(VOUCH_DATE, item.getVouchDate());
            else
                values.put(VOUCH_DATE,"");
        }
        catch ( Exception e)
        {
            values.put(VOUCH_DATE,"");
        }



        // Log.e("addItem",""+item.getDescription());
        //********************************************************

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

    //---------------------------------------------------------------------------------------------------------------
    //****************************get Account Report for  Customer No ***************************************
    public List<Account_Report> getِAccountReport() {
        String CustomerNo =  CustomerListShow.Customer_Account;
        List<Account_Report> account_reports = new ArrayList<Account_Report>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + ACCOUNT_REPORT +" where CUST_NUMBER_REPORT = '" + CustomerNo + "' ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account_Report account_report=new Account_Report();
                account_report.setDate(cursor.getString(0));
                account_report.setTransfer_name(cursor.getString(1));
                account_report.setDebtor(cursor.getString(2));
                account_report.setCreditor(cursor.getString(3));
                account_report.setCust_balance(cursor.getString(4));
                account_report.setCust_no(cursor.getString(5));
                account_reports.add(account_report);
            } while (cursor.moveToNext());
        }

        return account_reports;
    }



//*************************************************************************************************
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
                setting.setCanChangePrice(Integer.parseInt(cursor.getString(11)));
                setting.setAllowOutOfRange(Integer.parseInt(cursor.getString(12)));
                setting.setReadDiscountFromOffers(Integer.parseInt(cursor.getString(13)));
                setting.setWorkOnline(Integer.parseInt(cursor.getString(14)));
                setting.setPaymethodCheck(Integer.parseInt(cursor.getString(15)));
                setting.setBonusNotAlowed(Integer.parseInt(cursor.getString(16)));
                setting.setNoOffer_for_credit(Integer.parseInt(cursor.getString(17)));
                setting.setAmountOfMaxDiscount(Integer.parseInt(cursor.getString(18)));
                setting.setCustomer_authorized(Integer.parseInt(cursor.getString(19)));
                setting.setPassowrd_data(Integer.parseInt(cursor.getString(20)));
                setting.setArabic_language(Integer.parseInt(cursor.getString(21)));
                try {
                    setting.setHide_qty(Integer.parseInt(cursor.getString(22)));

                } catch (Exception e) {
                    setting.setHide_qty(0);
                }
                setting.setLock_cashreport(Integer.parseInt(cursor.getString(23)));
                setting.setSalesMan_name(cursor.getString(24));
                setting.setPriventOrder(Integer.parseInt(cursor.getString(25)));//for test
                setting.setRequiNote(Integer.parseInt(cursor.getString(26)));
                setting.setPreventTotalDisc(Integer.parseInt(cursor.getString(27)));
                setting.setAutomaticCheque(Integer.parseInt(cursor.getString(28)));
                setting.setTafqit(Integer.parseInt(cursor.getString(29)));
                setting.setPreventChangPayMeth(Integer.parseInt(cursor.getString(30)));
                setting.setShowCustomerList(Integer.parseInt(cursor.getString(31)));
                setting.setNoReturnInvoice(Integer.parseInt(cursor.getString(32)));
                setting.setWork_serialNo(Integer.parseInt(cursor.getString(33)));
                setting.setShowItemImage((cursor.getInt(34)));
                settings.add(setting);
            } while (cursor.moveToNext());
        }
        return settings;
    }

    public List<CompanyInfo> getAllCompanyInfo() {
        List<CompanyInfo> infos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM  COMPANY_INFO";
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

                info.setNoteForPrint(cursor.getString(4));

                infos.add(info);
            } while (cursor.moveToNext());
        }
        return infos;
    }
    public List<serialModel> getAllSerialItems() {
        List<serialModel> infos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM  SERIAL_ITEMS_TABLE"+ " WHERE IS_POSTED_SERIAL = " + 0;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                serialModel info = new serialModel();
                info.setSerialCode(cursor.getString(1));
                info.setCounterSerial(Integer.parseInt(cursor.getString(2)));
                info.setVoucherNo((cursor.getString(3)));
                info.setItemNo(cursor.getString(4));
                info.setKindVoucher(cursor.getString(5));
                info.setDateVoucher(cursor.getString(6));
                info.setStoreNo(cursor.getString(7));
                info.setIsPosted(cursor.getString(8));
                info.setIsBonus(cursor.getString(9));

                infos.add(info);

            } while (cursor.moveToNext());
            Log.e("getAllSerialItems",""+infos.size());
        }
        return infos;
    }








    public List<CustomerLocation> getCustomerLocation() {
        List<CustomerLocation> infos = new ArrayList<>();
        String selectQuery = "select  DISTINCT  CUS_ID , CUST_LAT ,CUST_LONG ,IS_POST from CUSTOMER_MASTER";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomerLocation info = new CustomerLocation();
                info.setCUS_NO(cursor.getString(0));
                info.setLATIT((cursor.getString(1)));
                info.setLONG((cursor.getString(2)));
                info.setIsPost((cursor.getInt(3)));

                infos.add(info);
            } while (cursor.moveToNext());
        }
        return infos;
    }
//    public void addCustomerLocation(CustomerLocation customerLocation)
//    {
//        try {
//            db = this.getReadableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(CUS_NO, customerLocation.getCUS_NO());
//            values.put(LONG,customerLocation.getLONG());
//            values.put(LATIT,customerLocation.getLATIT());
//
//            db.insert(CUSTOMER_LOCATION, null, values);
//            db.close();
//        }
//        catch (Exception e){
//            Log.e("DBAccount_Report",""+e.getMessage());
//
//        }
//
//    }

    public int getMaxSerialNumber(int voucherType) {
        String selectQuery = "SELECT  SERIAL_NUMBER FROM " + TABLE_SETTING + " WHERE TRANS_KIND = " + voucherType;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        int maxVoucher = Integer.parseInt(cursor.getString(0));
        return maxVoucher;

    }
    public int getMaxSerialNumberFromVoucherMaster(int voucherType) {
        //SELECT IFNULL((select max(VOUCHER_NUMBER) FROM SALES_VOUCHER_MASTER  where VOUCHER_TYPE = '508'),-1)
        String selectQuery = "SELECT IFNULL((select max(VOUCHER_NUMBER) FROM " + SALES_VOUCHER_MASTER + " WHERE VOUCHER_TYPE = '"+voucherType+"' ),-1)" ;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();


        int maxVoucher = Integer.parseInt(cursor.getString(0));
        if(maxVoucher==-1)
        {
            maxVoucher=getMaxSerialNumber(voucherType);
            Log.e("getMaxSerialNumber","FromSetting"+maxVoucher);
        }
        Log.e("getMaxSerialNumber","FromVoucherMaster"+maxVoucher);
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
                Log.e("getAllSalesMen",""+salesMen.size());
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
                customer.setPayMethod(Integer.parseInt((cursor.getString(9))));
                customer.setCustLat(cursor.getString(10));
                customer.setCustLong(cursor.getString(11));
                customer.setMax_discount(Double.parseDouble(cursor.getString(12)));
                customer.setACCPRC(cursor.getString(13));
                customer.setHide_val(cursor.getInt(14));
//                customer.setCustId(cursor.getString(16));// for test talley
                // 16 column isPosted
                // Adding transaction to list
                customers.add(customer);
            } while (cursor.moveToNext());
        }
        return customers;
    }

    // get customer cridit limit and cash credit balance  by customer No

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Customer> getCustomer_byNo(String number) {
        List<Customer> customer_balance = new ArrayList<Customer>();
        String selectQuery = " SELECT  CASH_CREDIT , CREDIT_LIMIT from "+ CUSTOMER_MASTER +" where CUS_ID ='"+ number +"'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();

                customer.setCashCredit(Integer.parseInt(cursor.getString(0)));
                customer.setCreditLimit(Double.parseDouble(cursor.getString(1)));
//
                customer_balance.add(customer);
                Log.e("CASH_CREDIT",""+customer_balance.get(0).getCashCredit());
                Log.e("CREDIT_LIMIT",""+customer_balance.get(0).getCreditLimit());
            } while (cursor.moveToNext());
        }
        return customer_balance;
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
                customer.setCustId(cursor.getString(1));// test talley

                customer.setCustName(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setIsSuspended(Integer.parseInt(cursor.getString(4)));
                customer.setPriceListId(cursor.getString(5));
                customer.setCashCredit(Integer.parseInt(cursor.getString(6)));
                customer.setSalesManNumber(cursor.getString(7));
                customer.setCreditLimit(Integer.parseInt(cursor.getString(8)));
                customer.setPayMethod(Integer.parseInt(cursor.getString(9)));

                customer.setCustLat(cursor.getString(10));
                customer.setCustLong(cursor.getString(11));
                try {
                    customer.setMax_discount(Double.parseDouble(cursor.getString(12)));
                }catch(NumberFormatException e){
                    Log.e("setMax_discount",""+e.getMessage());
                    customer.setMax_discount(0);
                }
                customer.setACCPRC(cursor.getString(13));
                customer.setHide_val(cursor.getInt(14));
//                customer.setCustId(cursor.getString(16));// test talley
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
                Voucher.setVoucherDiscount(Double.parseDouble(cursor.getString(10)));//5
                Voucher.setVoucherDiscountPercent(Double.parseDouble(cursor.getString(6)));
                Voucher.setRemark(cursor.getString(7));
                Voucher.setPayMethod(Integer.parseInt(cursor.getString(8)));
                Voucher.setIsPosted(Integer.parseInt(cursor.getString(9)));
                Voucher.setTotalVoucherDiscount(Double.parseDouble(cursor.getString(10)));
                Voucher.setSubTotal(Double.parseDouble(cursor.getString(11)));
                try {
                    Voucher.setTax(Double.parseDouble(cursor.getString(12)));
                    Voucher.setNetSales(Double.parseDouble(cursor.getString(13)));
                }catch (NullPointerException e)
                {
                    Voucher.setTax(0);
                    Voucher.setNetSales(-1);
                    Log.e("NullPointerException","tax and netsales");
                }


                Voucher.setCustName(cursor.getString(14));
                Voucher.setCustNumber(cursor.getString(15));
                Voucher.setVoucherYear(Integer.parseInt(cursor.getString(16)));

                // Adding transaction to list
                vouchers.add(Voucher);
            } while (cursor.moveToNext());
        }

        return vouchers;
    }
    //****************************getAllVoucherBy Customer No ***************************************
    public List<Voucher> getAllVouchers_CustomerNo(String CustomerNo) {
        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + SALES_VOUCHER_MASTER +" where CUST_NUMBER = '" + CustomerNo + "' ";
        Log.e("select",""+selectQuery);
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
                Voucher.setVoucherDiscount(Double.parseDouble(cursor.getString(10)));//5
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

    public Voucher getAllVouchers_VoucherNo(int voucherNo) {
//        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query
        Log.e("voucherNoDB",""+voucherNo);

        Voucher Voucher= new Voucher();

        String selectQuery = "SELECT  * FROM " + SALES_VOUCHER_MASTER +" where VOUCHER_NUMBER = '" + voucherNo + "' ";
        Log.e("select",""+selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Voucher.setCompanyNumber(Integer.parseInt(cursor.getString(0)));
                Voucher.setVoucherNumber(Integer.parseInt(cursor.getString(1)));
                Voucher.setVoucherType(Integer.parseInt(cursor.getString(2)));
                Voucher.setVoucherDate(cursor.getString(3));
                Voucher.setSaleManNumber(Integer.parseInt(cursor.getString(4)));
                Voucher.setVoucherDiscount(Double.parseDouble(cursor.getString(10)));//5
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

            } while (cursor.moveToNext());
        }

        return Voucher;
    }





    //***********************************************************************************************
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
                itemsMaster.setKind_item(cursor.getString(7));
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
                "D.VOUCHER_DISCOUNT , D.TAX_VALUE , D.TAX_PERCENT , D.COMPANY_NUMBER , D.ITEM_YEAR , D.IS_POSTED , M.VOUCHER_DATE , D.ITEM_DESCRIPTION ,D.SERIAL_CODE " +
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
               // Log.e("cursorItemNo",""+cursor.getString(2));
                item.setItemName(cursor.getString(3));
                item.setUnit(cursor.getString(4));
                item.setQty(Float.parseFloat(cursor.getString(5)));
                item.setPrice(Float.parseFloat(cursor.getString(6)));
                item.setBonus(Float.parseFloat(cursor.getString(7)));
                item.setDisc(Float.parseFloat(cursor.getString(8)));
                item.setDiscPerc(cursor.getString(9));
                item.setTotalDiscVal(cursor.getFloat(10));
                item.setVoucherDiscount(cursor.getFloat(10));
                try {

                    item.setTaxValue(Double.parseDouble(cursor.getString(11)));
                    item.setTaxPercent(Float.parseFloat(cursor.getString(12)));
                }catch (Exception e)
                {
                    Log.e("DBHandler","impo"+e.getMessage());
                }
                item.setCompanyNumber(Integer.parseInt(cursor.getString(13)));
                item.setYear(cursor.getString(14));
                item.setIsPosted(Integer.parseInt(cursor.getString(15)));
                item.setDate(cursor.getString(16));
                item.setDescreption(cursor.getString(17));
                item.setSerialCode(cursor.getString(18));
                Log.e("setDescreption",""+cursor.getString(17));

                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public String getRateOfCustomer()
    {
       String rate="";
        String customer_id = CustomerListShow.Customer_Account;
        String selectQuery = "select DISTINCT  cusMaster.ACCPRC  from CUSTOMER_MASTER cusMaster  where cusMaster.CUS_ID ='"+customer_id+"' ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                rate=cursor.getString(0);
                Log.e("rate ",""+rate+"\t"+customer_id);
            }
            while (cursor.moveToNext());
        }
        return  rate;


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

    public List<Item> getAllJsonItems(String rate ) {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        String salesMan = Login.salesMan;
//        String cusNo="5";
        String PriceListId = CustomerListShow.PriceListId;
        String selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO \n" +
                "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P\n" +
                "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+rate+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"'";

        Log.e("***" , selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
            do {
                Item item = new Item();
                Bitmap  itemBitmap=null;

                item.setItemNo(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setQty(Float.parseFloat(cursor.getString(3)));
                item.setPrice(Float.parseFloat(cursor.getString(4)));
                item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
                item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
                item.setBarcode(cursor.getString(7));
                item.setItemL(Double.parseDouble(cursor.getString(8)));

                item.setPosPrice(Double.parseDouble(cursor.getString(9)));
                item.setKind_item(cursor.getString(10));
                try {


                if(cursor.getString(12)==null)
                {
                    item.setItemHasSerial("0");
                    Log.e("setItemHasSerial",""+item.getItemHasSerial()+"null");
                }
                else {
                    item.setItemHasSerial(cursor.getString(12));
                }
                }catch (Exception e)
                {
                    item.setItemHasSerial("0");
                    Log.e("setItemHasSerial",""+item.getItemHasSerial()+e.getMessage());

                }


                if(!cursor.getString(13).equals("")) {

                    itemBitmap = StringToBitMap(cursor.getString(13));
                    item.setItemPhoto(itemBitmap);
                }
                else {
                    item.setItemPhoto(null);
                }


//                Log.e("setItemHasSerial",""+item.getItemHasSerial()+e.getMessage());


                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public List<Item> getAllJsonItems2(String rate)
    {

        List<Item> items = new ArrayList<>();
        // Select All Query
        String PriceListId = CustomerListShow.PriceListId;
        String priceItem="";
        String custNum = CustomerListShow.Customer_Account;
        String salesMan = Login.salesMan;
        String selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,C.PRICE ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D,M.KIND_ITEM, cusMaster.ACCPRC ,M.ITEM_HAS_SERIAL , M.ITEM_PHOTO \n" +
                "   from Items_Master M , SalesMan_Items_Balance S , CustomerPrices C ,CUSTOMER_MASTER cusMaster,  Price_List_D P\n" +
                "   where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and M.ItemNo = C.ItemNumber and P.PrNo ='"+rate+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan + "'" +
                "   and C.CustomerNumber = '" + custNum + "'";

        Log.e("***" , selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
            do {
                Item item = new Item();
                Bitmap  itemBitmap=null;

                item.setItemNo(cursor.getString(0));
                String itno=cursor.getString(0);
                item.setItemName(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setQty(Float.parseFloat(cursor.getString(3)));
                if(Float.parseFloat(cursor.getString(4))== 0){
                    priceItem= getPriceforItem(itno,rate);
                    item.setPrice(Float.parseFloat(priceItem));

                }
                else{
                    item.setPrice(Float.parseFloat(cursor.getString(4)));
                }
                item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
                item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
                item.setBarcode(cursor.getString(7));
                item.setItemL(Double.parseDouble(cursor.getString(8)));
                item.setPosPrice(Double.parseDouble(cursor.getString(9)));
                item.setKind_item(cursor.getString(10));
//                item.setp(cursor.getString(10));
                try {


                    if(cursor.getString(12)==null)
                    {
                        item.setItemHasSerial("0");
                        Log.e("setItemHasSerial",""+item.getItemHasSerial()+"null");
                    }
                    else {
                        item.setItemHasSerial(cursor.getString(12));
                    }
                }catch (Exception e)
                {
                    item.setItemHasSerial("0");

                    Log.e("setItemHasSerial",""+item.getItemHasSerial()+e.getMessage());

                }
//                item.setItemPhoto(cursor.getString(13));
                // Adding transaction to list
                if(!cursor.getString(13).equals("")) {

                    itemBitmap = StringToBitMap(cursor.getString(13));
                    item.setItemPhoto(itemBitmap);
                }
                else {
                    item.setItemPhoto(null);
                }

                items.add(item);
            } while (cursor.moveToNext());
        }
        else{
//           items= getAllJsonItems(rate);
//           Log.e("not_pricesincustomerprices",""+items.size());
        }

        return items;
    }
    public List<Item> getAllJsonItemsStock( ) {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        String salesMan = Login.salesMan;
//        String cusNo="5";
        String PriceListId = CustomerListShow.PriceListId;
        String selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC \n" +
                "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P\n" +
                "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+0+"'  and cusMaster.ACCPRC = '"+0+"' and S.SalesManNo = '" + salesMan +"'";

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

                item.setPosPrice(Double.parseDouble(cursor.getString(9)));
                item.setKind_item(cursor.getString(10));

                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }
    public List<String> getItemNumbersNotInPriceListD(){
        String customerId=CustomerListShow.Customer_Account;
        List<String> itemNoList=new ArrayList<>();
        String selectQuery ="SELECT DISTINCT   listItemNo.ItemNo \n"+
      " FROM Items_Master listItemNo \n " +
            "    EXCEPT \n" +
       " select    cast( ItemNumber as text)"+
       " from CustomerPrices  where CustomerNumber = '"+customerId+"' ";


        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.e("getAItemsNotInC", "***************************************" + cursor.getCount());
            do {

                itemNoList.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        Log.e("item size",""+itemNoList.size());

        return itemNoList;



    }
    public boolean checkItemNoTableCustomerPricess(String itemNo){
        String custNum = CustomerListShow.Customer_Account;
        String selectQuery ="  select DISTINCT  C.ItemNumber\n" +
                "        FROM  CustomerPrices C\n" +
                "        where  CustomerNumber= '"+custNum+"' and ItemNumber= '"+itemNo+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
          return  true;

        }

        return false;
    }
    /*      select DISTINCT  C.ItemNumber
        FROM  CustomerPrices C
        where  CustomerNumber= '1110000001' and ItemNumber= '30000188'*/
    public List<Item> getAllJsonItemsNotInCustomerPrices(String rate){
        List<Item> items = new ArrayList<>();
        // Select All Query
        String PriceListId = CustomerListShow.PriceListId;
        String priceItem="";
        String custNum = CustomerListShow.Customer_Account;
        String salesMan = Login.salesMan;
        String selectQuery ="select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.PRICE ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D,M.KIND_ITEM, cusMaster.ACCPRC\n" +
                "    from Items_Master M , SalesMan_Items_Balance S , CustomerPrices C ,CUSTOMER_MASTER cusMaster,  Price_List_D P\n" +
                "    where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo  and P.PrNo = '"+rate+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '"+salesMan+"'\n" +
                "    and\n" +
                "    M.ItemNo  IN \n" +
                "            (  SELECT DISTINCT   pr_list.ItemNo\n" +
                "                    FROM Price_List_D pr_list\n" +
                "                    EXCEPT\n" +
                "                    select    cast( ItemNumber as text)\n" +
                "    from CustomerPrices\n" +
                "\n" +
                ")";

        Log.e("***" , selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.e("getAItemsNotInC", "***************************************" + cursor.getCount());
            do {
                Item item = new Item();

                item.setItemNo(cursor.getString(0));
                String itno = cursor.getString(0);
                item.setItemName(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setQty(Float.parseFloat(cursor.getString(3)));
                item.setPrice(Float.parseFloat(cursor.getString(4)));
                item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
                item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
                item.setBarcode(cursor.getString(7));
                item.setItemL(Double.parseDouble(cursor.getString(8)));
                item.setPosPrice(Double.parseDouble(cursor.getString(9)));
                item.setKind_item(cursor.getString(10));
                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;



    }
    /*      String selectQuery ="select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.PRICE ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D,M.KIND_ITEM, cusMaster.ACCPRC\n" +
                "    from Items_Master M , SalesMan_Items_Balance S , CustomerPrices C ,CUSTOMER_MASTER cusMaster,  Price_List_D P\n" +
                "    where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo  and P.PrNo = '"+rate+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '"+salesMan+"'\n" +
                "    and\n" +
                "    M.ItemNo  IN \n" +
                "            (  SELECT DISTINCT   pr_list.ItemNo\n" +
                "                    FROM Price_List_D pr_list\n" +
                "                    EXCEPT\n" +
                "                    select    cast( ItemNumber as text)\n" +
                "    from CustomerPrices\n" +
                "\n" +
                ")";*/


    private String getPriceforItem(String itemNo,String rate) {

        // Select All Query
        String salesMan = Login.salesMan;
        String price="";
        String selectQuery2 ="select  Price from PRICE_LIST_D where ItemNo = '"+itemNo+"' and PrNo='"+rate+"' ";

        db = this.getWritableDatabase();
        Cursor cursor_price = db.rawQuery(selectQuery2, null);
        if (cursor_price.moveToFirst()){
            price=cursor_price.getString(0);
        }

        cursor_price.close();

        return price;

        /*
        * //        String selectQuery2 = "select DISTINCT  P.Price  \n" +
//                "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P\n" +
//                "                where  P.ItemNo = '"+itemNo+"' and P.PrNo ='"+rate+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"'";
* //        Log.e("DatabaseHandler", "***************************************" + cursor_price.getCount());
        // looping through all rows and adding to list
//        if (cursor_price.moveToFirst()) {
//
//            do {
//                price=cursor_price.getString(0);
//                Log.e("price=",""+price);




//            } while (cursor_price.moveToNext());
//        }*/

    }

    public List<Item> getUnPostedItems() {
        List<Item> items = new ArrayList<Item>();

        String selectQuery = "select D.VOUCHER_NUMBER , D.VOUCHER_TYPE , D.ITEM_NUMBER ,D.ITEM_NAME ," +
                " D.UNIT ,D.UNIT_QTY , D.UNIT_PRICE ,D.BONUS  ,D.ITEM_DISCOUNT_VALUE ,D.ITEM_DISCOUNT_PERC ," +
                "D.VOUCHER_DISCOUNT , D.TAX_VALUE , D.TAX_PERCENT , D.COMPANY_NUMBER , D.ITEM_YEAR , D.IS_POSTED , M.SALES_MAN_NUMBER , M.VOUCHER_DATE " +
                "from SALES_VOUCHER_DETAILS D , SALES_VOUCHER_MASTER M " +
                "where D.VOUCHER_NUMBER  = M.VOUCHER_NUMBER and D.VOUCHER_TYPE = M.VOUCHER_TYPE and D.IS_POSTED = '0'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
//            Log.i("DatabaseHandler", "************************" + selectQuery);
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
                item.setTotalDiscVal(cursor.getFloat(10));
                item.setVoucherDiscount(cursor.getFloat(10));
                item.setTaxValue(Double.parseDouble(cursor.getString(11)));
                item.setTaxPercent(Float.parseFloat(cursor.getString(12)));
                item.setCompanyNumber(Integer.parseInt(cursor.getString(13)));
                item.setYear(cursor.getString(14));
                item.setIsPosted(Integer.parseInt(cursor.getString(15)));
                item.setSalesmanNo(cursor.getString(16));
                item.setDate(cursor.getString(17));


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
    public List<String> getAllKindItems() {
        List<String> kind_items= new ArrayList<>();
        try {
            String selectQuery = "select DISTINCT KIND_ITEM from Items_Master";
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("DB_Exception","cursor"+cursor.getCount());

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    if(cursor.getString(0)== "null")
                    { kind_items.add("**");}
                    else {
                        kind_items.add(cursor.getString(0));

                        Log.e("DB_Exception","kind_itemsElse"+cursor.getString(0));
                    }


                } while (cursor.moveToNext());
            }
        }catch (Exception e)
        {
          kind_items.add("**");
          Log.e("DB_Exception","kind_items"+e.getMessage());
        }

        return kind_items;
    }

    public int getActiveKeyValue() {
        int keyvalue = 0;
        String selectQuery = "select DISTINCT  KEY_VALUE from " + ACTIVE_KEY;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                keyvalue = cursor.getInt(0);

            }
            while (cursor.moveToNext());
        }
//        Log.e("keyvalue", "Db" + keyvalue);
        return keyvalue;
    }

    public List<ItemsQtyOffer> getItemsQtyOffer() {
        List<ItemsQtyOffer> itemsQtyOffers = new ArrayList<>();
        String selectQuery = "select * from " + ITEMS_QTY_OFFER;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                try {

                    ItemsQtyOffer new_Value_Qty_offers = new ItemsQtyOffer();
                    new_Value_Qty_offers.setItem_name((cursor.getString(0)));
                    new_Value_Qty_offers.setItem_no(cursor.getString(1));
                    new_Value_Qty_offers.setItemQty(Double.parseDouble(cursor.getString(2)));
                    new_Value_Qty_offers.setFromDate(cursor.getString(3));
                    new_Value_Qty_offers.setToDate(cursor.getString(4));
                    new_Value_Qty_offers.setDiscount_value(Double.parseDouble(cursor.getString(5)));


                    itemsQtyOffers.add(new_Value_Qty_offers);
                } catch (NumberFormatException e) {
                    Log.e("getitemsQtyOffers", "NumberFormatException" + e.getMessage());
                } catch (Exception e) {
                    Log.e("getitemsQtyOffers", "" + e.getMessage());


                }

            } while (cursor.moveToNext());
        }

        return itemsQtyOffers;
    }

    public List<QtyOffers> getDiscountOffers()
    {
        List<QtyOffers> qtyOffers = new ArrayList<>();
        String selectQuery = "select * from " + QTY_OFFERS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                QtyOffers new_Value_Qty_offers = new QtyOffers();
                new_Value_Qty_offers.setQTY(Double.parseDouble(cursor.getString(0)));
                new_Value_Qty_offers.setDiscountValue(Double.parseDouble(cursor.getString(1)));
                new_Value_Qty_offers.setPaymentType(Integer.parseInt(cursor.getString(2)));
                qtyOffers.add(new_Value_Qty_offers);

            } while (cursor.moveToNext());
        }
//        Log.e("new_Value_Qty_offers", "Db" + qtyOffers);
        return qtyOffers;
    }


    public  int getPrinterSetting()
    { int keyvalue=0;
        String selectQuery = "select DISTINCT  PRINTER_SETTING from "+ PRINTER_SETTING_TABLE;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                keyvalue=cursor.getInt(0);

            } while (cursor.moveToNext());
        }

        return keyvalue;


    }


    public  List<PrinterSetting> getPrinterSetting_()
    {// int keyvalue=0;
        List<PrinterSetting> keyvalue=new ArrayList<>();
        String selectQuery = "select * from "+ PRINTER_SETTING_TABLE;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do {
                PrinterSetting printerSetting=new PrinterSetting();
                printerSetting.setPrinterName(cursor.getInt(0));
                printerSetting.setPrinterShape(cursor.getInt(1));
                keyvalue.add(printerSetting);
            } while (cursor.moveToNext());
        }

        return keyvalue;


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

                payment.setCompanyNumber(cursor.getInt(0));
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
    public Payment getPayments_voucherNo(int vouchNo) {
        Payment payment = new Payment();
       String cusNo=CustomerListShow.Customer_Account;
//        List<Payment> paymentsList = new ArrayList<Payment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PAYMENTS+" where VOUCHER_NUMBER = '" + vouchNo  + "' and CUSTOMER_NUMBER = '"+cusNo+"'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                Payment payment = new Payment();

                payment.setCompanyNumber(cursor.getInt(0));
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


            } while (cursor.moveToNext());
        }

        return payment;
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

    public List<Payment> getRequestedPaymentsPaper(int voucherNo) {

        List<Payment> paymentsList = new ArrayList<Payment>();
        String selectQuery = "SELECT  * FROM " + PAYMENTS_PAPER + " where VOUCHER_NUMBER = '" + voucherNo  + "'";
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
        String selectQuery = "SELECT * FROM " + ADDED_CUSTOMER+ " where IS_POSTED = '" + 0  + "'";;

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
                offer.setToDate(cursor.getString(3));//*************
                offer.setItemNo(cursor.getString(4));
                offer.setItemQty(Double.parseDouble(cursor.getString(5)));
                offer.setBonusQty(Double.parseDouble(cursor.getString(6)));
                offer.setBonusItemNo(cursor.getString(7));

                offers.add(offer);
            } while (cursor.moveToNext());
        }


        return offers;
    }
    //*********************************************************

    public List<SalesManItemsBalance> getSalesManItemsBalance(String salesmanNo) {
        List<SalesManItemsBalance> salesManItemsBalanceList = new ArrayList<>();
        String selectQuery = "SELECT  distinct SalesManNo , ItemNo , Qty  FROM "+ SalesMan_Items_Balance
                +" Where SalesManNo = " + salesmanNo + " And  Qty > 0 ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {


            do {
                SalesManItemsBalance salesManItemsBalance = new SalesManItemsBalance();
                salesManItemsBalance.setSalesManNo(cursor.getString(0));
                salesManItemsBalance.setItemNo(cursor.getString(1));
                salesManItemsBalance.setQty(Double.parseDouble(cursor.getString(2)));
                salesManItemsBalanceList.add(salesManItemsBalance);
            } while (cursor.moveToNext());
//            Log.e("ListItemBalance",""+salesManItemsBalanceList.get(0).getQty());
//            Log.e("ListItemBalance",""+salesManItemsBalanceList.get(1).getQty());

        }
    //Log.e("ListItemBalance",""+salesManItemsBalanceList.get(0).getQty());

        return salesManItemsBalanceList;
    }
    //*********************************************************

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

    public void updateVoucher2(int voucherNo) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(SALES_VOUCHER_MASTER, values, IS_POSTED + "=" + 0 + " and VOUCHER_NUMBER = " + voucherNo, null);
    }

    public void updateVoucherDetails2(int voucherNo) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(SALES_VOUCHER_DETAILS, values, IS_POSTED + "=" + 0 + " and VOUCHER_NUMBER = " + voucherNo, null);
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
    public void updateCustomersMaster() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POST, 1);
        db.update(CUSTOMER_MASTER, values, IS_POST + "=" + 0, null);
    }
    public void updateSerialTableIsposted() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED_SERIAL, 1);
        db.update(SERIAL_ITEMS_TABLE, values, IS_POSTED_SERIAL + "=" + 0, null);
    }

    public void updateTransactions() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED2, 1);
        db.update(TABLE_TRANSACTIONS, values, IS_POSTED + "=" + 0, null);
    }

    public void updateCustomersMaster(String lat , String lon , String custId) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CUST_LAT, lat);
        values.put(CUST_LONG, lon);
        db.update(CUSTOMER_MASTER, values, CUS_ID + "= '" + custId + "'", null);
    }


    public void updateSalesManItemsBalance1(float qty , int salesMan, String itemNo) {
        db = this.getWritableDatabase();
        String s="";

        String selectQuery = "select Qty from SalesMan_Items_Balance " +
                " where ItemNo = '"+itemNo+"'  and SalesManNo = '"+salesMan+"' " ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        float existQty = 0;
        if (cursor.moveToFirst()) {
            do {
                existQty = Float.parseFloat(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        existQty -= qty;

        Log.e("qty1 ***" , ""+existQty);
        db.execSQL("update SalesMan_Items_Balance SET  Qty = '"+existQty+"' where SalesManNo = '"+salesMan+"' and  ItemNo = '"+itemNo+"'");

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
    public void deletSerialItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SERIAL_ITEMS_TABLE);
        db.close();
    }
    public void deletSerialItems_byVoucherNo(int vouch_no ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SERIAL_ITEMS_TABLE+" where VOUCHER_NO =" +vouch_no);
        db.close();
    }
    public void deletSerialItems_byItemNo(String item_no ) {
        //delete from SERIAL_ITEMS_TABLE where ITEMNO_SERIAL=1003
        //+ "= '" + custNo + "'"
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SERIAL_ITEMS_TABLE+" where ITEMNO_SERIAL =" +item_no);
        db.close();
    }



    public void deletAcountReport() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + ACCOUNT_REPORT);
        db.close();
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



    public void deleteAllPrinterSetting() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + PRINTER_SETTING_TABLE);
        db.close();
    }

    public void deleteKeyValue() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + KEY_VALUE);
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
    public void deleteAllOffersQty() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + QTY_OFFERS);
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
    public  void  deletItemsOfferQty(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + ITEMS_QTY_OFFER);
        db.close();


    }
    public List<Payment> getAllPayments_customerNo( String CustomerNo) {

        List<Payment> paymentsList = new ArrayList<Payment>();
        String selectQuery = "SELECT  * FROM " + PAYMENTS +" where CUSTOMER_NUMBER = '" + CustomerNo + "' ";
        Log.e("select",""+selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
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
        public void updateCustomersPayment_Info(double CriditLimit , int CashCredit , String custNo) {
        db = this.getWritableDatabase();
        Log.e("updateCust",""+CriditLimit+CashCredit);
        ContentValues values = new ContentValues();
        values.put(CASH_CREDIT, CashCredit);
        values.put(CREDIT_LIMIT, CriditLimit);

        db.update(CUSTOMER_MASTER, values, CUS_ID    + "= '" + custNo + "'" , null);
    }


    // update SERIAL_ITEMS_TABLE set KIND_VOUCHER=30 where  VOUCHER_NO=4

    public void updatevoucherKindInSerialTable( int kindVoucher,int voucherNo,int storeNo ) {
        db = this.getWritableDatabase();
        Log.e("updateVOUCHERNO",""+kindVoucher);
        ContentValues values = new ContentValues();
        values.put(KIND_VOUCHER, kindVoucher);


        db.update(SERIAL_ITEMS_TABLE, values, VOUCHER_NO    + "= '" + voucherNo + "'" , null);


//        values.put(, serialModelItem.getStoreNo());

    }



    List<Item> vouchersales;
    public void updateSalesManItemBalance(String salesmanNo,String itemNo,double qty) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int unposted_sales=0;
        int unposted__return=0;
        unposted_sales=( getAllItems_bySalesman_No(itemNo,504,salesmanNo));
        unposted__return= ( getAllItems_bySalesman_No(itemNo,506,salesmanNo));

            double newQty=(qty-unposted_sales )+unposted__return;

                        values.put(Qty5, newQty);
            db.update(SalesMan_Items_Balance, values, SalesManNo5 + " = " + salesmanNo + " and " + ItemNo5 + " = " + itemNo, null);

        }


    public int  getAllItems_bySalesman_No( String itemNo,int  vouchType,String salesmanNo){
        String selectQuery ="select IFNULL(sum(D.UNIT_QTY+BONUS),0) As Sold_Qty"+
        " from SALES_VOUCHER_DETAILS D, SALES_VOUCHER_MASTER M"+
               " where M.SALES_MAN_NUMBER = '"+salesmanNo+"'  and  D.ITEM_NUMBER = '"+itemNo+"' and D.IS_POSTED ='"+0+"' and  M.VOUCHER_NUMBER = D.VOUCHER_NUMBER  "
       +" And M.VOUCHER_TYPE ='" +vouchType+"'";
     int total_qty=0;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Adding transaction to list
               total_qty=Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
//        int t=Integer.parseInt(total_qty);
        return total_qty;

    }

//    public List<Item> getAllItems_bySalesman_No() {
//
//        List<Item> items = new ArrayList<Item>();
//        String salesma=Login.salesMan;
//        // Select All Query
//        String selectQuery = "select D.VOUCHER_NUMBER , D.VOUCHER_TYPE , D.ITEM_NUMBER ,D.ITEM_NAME ," +
//                " D.UNIT ,D.UNIT_QTY , D.IS_POSTED " +
//                "from SALES_VOUCHER_DETAILS D , SALES_VOUCHER_MASTER M " +
//                "where D.VOUCHER_NUMBER  = M.VOUCHER_NUMBER and D.VOUCHER_TYPE = M.VOUCHER_TYPE and M.IS_POSTED = 0  and M.SALES_MAN_NUMBER = '" + salesma + "'";
//        Log.e("","");
//        db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            Log.i("DatabaseHandler", "************************" + selectQuery);
//            do {
//                Item item = new Item();
//
//                item.setVoucherNumber(Integer.parseInt(cursor.getString(0)));
//                item.setVoucherType(Integer.parseInt(cursor.getString(1)));
//                item.setItemNo(cursor.getString(2));
//                item.setItemName(cursor.getString(3));
//                item.setUnit(cursor.getString(4));
//                item.setQty(Float.parseFloat(cursor.getString(5)));
//                item.setIsPosted(Integer.parseInt(cursor.getString(6)));
//
//
//                // Adding transaction to list
//                items.add(item);
//            } while (cursor.moveToNext());
//        }
//        return items;
//    }

     public boolean isAllVoucher_posted() {
         String selectQuery = "SELECT * FROM " + SALES_VOUCHER_MASTER + " where  IS_POSTED = 0 ";
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         if (cursor.moveToFirst()) {
             int x = cursor.getInt(0);
             if (x >= 0) {
                 Log.e("selectQuery", "" + selectQuery);
                 return false;


             } else
                 return true;
         }
         return true;
     }

    public double getMinOfferQty(double total) {
        String selectQuery = "SELECT MAX (AMOUNT_QTY) FROM " + ITEMS_QTY_OFFER+
                " WHERE '"+total+"' >= AMOUNT_QTY";
        double limitOffer=0;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                limitOffer=cursor.getDouble(0);
                Log.e("limitDB",""+limitOffer);

            } while (cursor.moveToNext());
        }

        return limitOffer;
    }

    public double getDiscValue_From_ItemsQtyOffer(String itemNo, double total_items_quantity) {
        String selectQuery = "SELECT DISCOUNT FROM " + ITEMS_QTY_OFFER +
                " WHERE ITEMNO =  '"+itemNo+"'  and  AMOUNT_QTY = '"+total_items_quantity+"'";

        double discount_value=0;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                discount_value=cursor.getDouble(0);
                Log.e("discount_value",""+discount_value+"\t"+itemNo);

            } while (cursor.moveToNext());
        }

        return discount_value;





    }
    public double getMaxDiscValue_ForCustomer(String customerNo) {
        String selectQuery = "SELECT MAX_DISCOUNT FROM " + CUSTOMER_MASTER +
                " WHERE CUS_ID  =  '"+customerNo+"' ";

        double max_discount=0;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                max_discount=cursor.getDouble(0);
                Log.e("max_discount_value",""+max_discount+"\t"+customerNo);

            } while (cursor.moveToNext());
        }

        return max_discount;





    }

    public String getSalesmanName() {
        String custId=CustomerListShow.Customer_Account;
        String selectQuery ="select S.salesManName \n" +
                "from Sales_Team S, CUSTOMER_MASTER C \n" +
                "WHERE\n" +
                "S.SalesManNo =  CAST(ifnull(C.SALES_MAN_NO,0) as INTEGER)\n" +
                "AND\n" +
                "CUS_ID_Text = '"+custId+"'";
        String customr_Name="";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                customr_Name=cursor.getString(0);
                Log.e("nam",""+customr_Name+"\t"+custId);

            } while (cursor.moveToNext());
        }


        return  customr_Name;

    }

    public int getLastVoucherNo(int vouchType) {
        int voucNo = 0;
        String cusNo = CustomerListShow.Customer_Account;
        String selectQuery = "select max(VOUCHER_NUMBER) FROM SALES_VOUCHER_MASTER WHERE VOUCHER_TYPE = '" + vouchType + "' AND CUST_NUMBER ='" + cusNo + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            cursor.moveToLast();
            voucNo = cursor.getInt(0);
            Log.e("voucNoBD=", "" + voucNo + "\t");

        } else {
            voucNo = -1;
        }
        return voucNo;

    }

    public int getLastVoucherNo_payment() {
        int voucNo = 0;
        String cusNo = CustomerListShow.Customer_Account;
        String selectQuery = "SELECT max (VOUCHER_NUMBER) FROM PAYMENTS  where  CUSTOMER_NUMBER = '" + cusNo + "' ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            cursor.moveToLast();
            voucNo = cursor.getInt(0);
            Log.e("voucNoPAYMENTS=", "" + voucNo + "\t");

        }
        return voucNo;

    }

    public int getHideValuForCustomer(String customerId) {
        int HideVal = 0;
        String selectQuery = "select HIDE_VAL\n" +
                "        from CUSTOMER_MASTER\n" +
                "        where CUSTOMER_MASTER.CUS_ID_Text = '" + customerId + "'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            cursor.moveToLast();
            HideVal = cursor.getInt(0);
            Log.e("HideValDB=", "" + HideVal + "\t");

        } else {
            HideVal = 0;
        }
        return HideVal;

    }
    public int getCountItemsMaster() {
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM Items_Master";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            count = cursor.getInt(0);
            Log.e("count=", "" + count + "\t");

        }
        return count;

    }


    public int checkVoucherNo(int voucherNumber,int voucherType) {
//        select VOUCHER_NUMBER from SALES_VOUCHER_MASTER WHERE VOUCHER_NUMBER = '147370'
        int count = 0;
        String selectQuery = "select VOUCHER_NUMBER from SALES_VOUCHER_MASTER WHERE VOUCHER_NUMBER = '"+voucherNumber+"' and VOUCHER_TYPE = '"+voucherType+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            count = cursor.getInt(0);
            Log.e("count=", "checkVoucherNo+\t" + count + "\t");

        }
        return count;
    }

    public void updateCustomerMasterLocation(String customer_account, String LATIT, String LONGT) {
        Log.e("updateCustomerMaster",""+customer_account+"\t"+LATIT+"\t"+LONGT);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CUST_LAT, LATIT);
        values.put(CUST_LONG, LONGT);


        // updating row
        db.update(CUSTOMER_MASTER, values, CUS_ID + "= '" + customer_account + "'", null);


    }

    public boolean isCustomerMaster_posted() {
        String selectQuery = "SELECT count() FROM " + CUSTOMER_MASTER + " where  IS_POST = 0 ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int x = cursor.getInt(0);
            Log.e("isCustomerMaster_posted", "" + x);
            if (x > 0) {
                return false;


            } else
                return true;
        }
        return true;
    }
//    select SERIAL_CODE_NO from SERIAL_ITEMS_TABLE where  SERIAL_CODE_NO='11'
    public String isSerialCodeExist(String serialCode) {
//        select VOUCHER_NUMBER from SALES_VOUCHER_MASTER WHERE VOUCHER_NUMBER = '147370'
    String count = "not";
    String selectQuery = "select SERIAL_CODE_NO from SERIAL_ITEMS_TABLE where  SERIAL_CODE_NO='"+serialCode+"' ";
    db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {

        count = cursor.getString(0);
    }
        Log.e("isCustomerMaster_posted", "isCustomerMaster_posted+\t" + count + "\t");
    return count;
}

    public String getItemNameBonus(String bonusItemNo) {
        String name = "";
//        select  Name from  Items_Master where ItemNo= 6934177700484
        String selectQuery = " select  Name from  Items_Master where ItemNo='"+bonusItemNo+"' ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            name = cursor.getString(0);

        }
        Log.e("getItemNameBonus", "getItemNameBonus+\t" + name + "\t");

        return name;
    }

    public ArrayList<Bitmap> getItemsImage() {
        ArrayList<Bitmap> listPhoto = new ArrayList<Bitmap>();

        String selectQuery = "select ITEM_PHOTO   from  Items_Master";

        Log.e("***" , selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
            do {

                Bitmap  itemBitmap=null;

                if(!cursor.getString(0).equals("")) {

                    itemBitmap = StringToBitMap(cursor.getString(0));

                    listPhoto.add(itemBitmap);
                }
                else {
                    listPhoto.add(null);
                }


                Log.e("listPhoto",""+listPhoto.size());

            } while (cursor.moveToNext());
        }

        return listPhoto;

    }
}
