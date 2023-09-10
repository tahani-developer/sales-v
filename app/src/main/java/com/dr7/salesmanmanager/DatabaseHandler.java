 package com.dr7.salesmanmanager;

import android.app.Activity;
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

import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.Flag_Settings;
import com.dr7.salesmanmanager.Modles.InventoryShelf;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemSwitch;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.ItemsReturn;
import com.dr7.salesmanmanager.Modles.MainGroup_Id_Count;
import com.dr7.salesmanmanager.Modles.NetworkLogModel;
import com.dr7.salesmanmanager.Modles.OfferGroupModel;
import com.dr7.salesmanmanager.Modles.OfferListMaster;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Password;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.PrinterSetting;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.SMItemAvailability;
import com.dr7.salesmanmanager.Modles.SaleManTrips;
import com.dr7.salesmanmanager.Modles.SalesManAndStoreLink;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesManPlan;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.TransactionsInfo;
import com.dr7.salesmanmanager.Modles.VisitRate;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.activeKey;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Modles.visitMedicalModel;
import com.dr7.salesmanmanager.Reports.SalesMan;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dr7.salesmanmanager.ExportJason.ReturnItemsarrayList;
import static com.dr7.salesmanmanager.Login.makeOrders;
import static com.dr7.salesmanmanager.Login.rawahneh_getMaxVouchFromServer;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.Reports.StockRecyclerViewAdapter.itemNoStock;
import static com.dr7.salesmanmanager.SalesInvoice.addNewSerial;
import static com.dr7.salesmanmanager.SalesInvoice.itemNoSelected;
import static com.dr7.salesmanmanager.SalesInvoice.noTax;
import static com.dr7.salesmanmanager.SalesInvoice.time;
import static com.dr7.salesmanmanager.SalesInvoice.voucher;

import org.jetbrains.annotations.NotNull;

 public class DatabaseHandler extends SQLiteOpenHelper {
    public static String SalmnLat,SalmnLong;
    private static String TAG = "DatabaseHandler";
    // Database Version
    private static final int DATABASE_VERSION = 221;
//
    // Database Name
    private static final String DATABASE_NAME = "VanSalesDatabase";
    static SQLiteDatabase db;
    // tables from JSON
    //----------------------------------------------------------------------
    private static final String NetworkTableLog = "NetworkTableLog";

     private static final String TransNo    = "TransNo";
     private static final String LinkRequest = "LinkRequest";
     private static final String DateTrand   = "DateTrand";
     private static final String TimeTrans   = "TimeTrans";
     private static final String CustomerNo   = "CustomerNo";
     private static final String ResponseLink = "ResponseLink";
     private static final String NoteExport="NoteExport";

    //----------------------------------------------------------------------
    private static final String GroupOffer_Item = "GroupOffer_Item";

    private static final String Id_serial = "Id_serial";
    private static final String ItemNo_Offer = "ItemNo_Offer";
    private static final String ItemName_Offer = "ItemName_Offer";
    private static final String From_Date_Offer = "From_Date_Offer";
    private static final String To_Date_Offer = "To_Date_Offer";
    private static final String Discount_Offer = "Discount_Offer";
    private static final String Discount_Type_Offer = "Discount_Type_Offer";
    private static final String GroupId_Offer = "GroupId_Offer";
    private static final String Qty_item = "Qty_item";


    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    private static final String VoucherSerialize = "VoucherSerialize";

    private static final String VoucherSales_no = "VoucherSales_no";
    private static final String VoucherReturn_no = "VoucherReturn_no";
    private static final String VoucherNewOrder_no = "VoucherNewOrder_no";


    //----------------------------------------------------------------------
    private static final String INVENTORY_SHELF = "INVENTORY_SHELF";

    private static final String TRANS_NO = "TRANS_NO";
    private static final String ITEM_NO = "ITEM_NO";
    private static final String SERIAL_NO = "SERIAL_NO";
    private static final String QTY_ITEM = "QTY_ITEM";
    private static final String TRANS_DATE = "TRANS_DATE";
    private static final String CUSTOMER_NO = "CUSTOMER_NO";
    private static final String SALESMAN_NUMBER = "SALESMAN_NUMBER";
    private static final String VOUCHER_NUMBER_INVENTORY = "VOUCHER_NUMBER_INVENTORY";
    private static final String IsPosted = "IsPosted";

    //----------------------------------------------------------------------
    private static final String price_offer_list_master = "price_offer_list_master";

    private static final String PO_LIST_NO = "PO_LIST_NO";
    private static final String PO_LIST_NAME = "PO_LIST_NAME";
    private static final String PO_LIST_TYPE = "PO_LIST_TYPE";
    private static final String FROM_DATE_master = "FROM_DATE_master";
    private static final String TO_DATE_master = "TO_DATE_master";


    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    private static final String SerialItemMaster = "SerialItemMaster";

    private static final String StoreNo = "StoreNo";
    private static final String ITEM_OCODE_M = "ITEM_OCODE_M";
    private static final String SerialCode = "SerialCode";
    private static final String Qty_serial = "Qty_serial";


    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    private static final String Item_Switch = "Item_Switch";

    private static final String ITEM_NAMEA = "ITEM_NAMEA";
    private static final String ITEM_OCODE = "ITEM_OCODE";
    private static final String ITEM_NCODE = "ITEM_NCODE";

    //----------------------------------------------------------------------
    private static final String  SALESMAN_LOGIN_LOGHistory  = "SALESMAN_LOGIN_LOGHistory";

    private static final String  DATE_LOGIN = "DATE_LOGIN";
    private static final String  TIME_LOGIN = "TIME_LOGIN";
    private static final String  TIME_LOGOUT= "TIME_LOGOUT";
    private static final String  LONGTUDE2  = "LONGTUDE2";
    private static final String  LATITUDE2  = "LATITUDE2";
    private static final String  SALESMAN_NO= "SALESMAN_NO";
    private static final String  IS_POSTED_LOGIN= "IS_POSTED_LOGIN";
    //----------------------------------------------------------------------
    private static final String SERIAL_ITEMS_TABLE  = "SERIAL_ITEMS_TABLE";
    private static final String  KEY_SERIAL="KEY_SERIAL";
    private static final String SERIAL_CODE_NO ="SERIAL_CODE_NO";
    private static final String COUNTER_SERIAL ="COUNTER_SERIAL";
    private static final String  VOUCHER_NO="VOUCHER_NO";
    private static final String ITEMNO_SERIAL="ITEMNO_SERIAL";
    private static final String DATE_VOUCHER="DATE_VOUCHER";
    private static final String KIND_VOUCHER="KIND_VOUCHER";
    private static final String  STORE_NO_SALESMAN="STORE_NO_SALESMAN";
    private static final String  IS_POSTED_SERIAL="IS_POSTED_SERIAL";
    private static final String  IS_BONUS_SERIAL="IS_BONUS_SERIAL";
    private static final String  Price_ITEM="Price_ITEM";
    private static final String  Price_ITEM_Sales="Price_ITEM_Sales";
    private static final String  IS_RETURNED="IS_RETURNED";
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    private static final String SERIAL_ITEMS_TABLE_backup  = "SERIAL_ITEMS_TABLE_backup";
    private static final String  KEY_SERIAL2="KEY_SERIAL2";
    private static final String SERIAL_CODE_NO2 ="SERIAL_CODE_NO2";
    private static final String COUNTER_SERIAL2 ="COUNTER_SERIAL2";
    private static final String  VOUCHER_NO2="VOUCHER_NO2";
    private static final String ITEMNO_SERIAL2="ITEMNO_SERIAL2";
    private static final String DATE_VOUCHER2="DATE_VOUCHER2";
    private static final String KIND_VOUCHER2="KIND_VOUCHER2";
    private static final String  STORE_NO_SALESMAN2="STORE_NO_SALESMAN2";
    private static final String  IS_POSTED_SERIAL2="IS_POSTED_SERIAL2";
    private static final String  IS_BONUS_SERIAL2="IS_BONUS_SERIAL2";
    private static final String  isItemDelete="isItemDelete";
    private static final String  dateDelete="dateDelete";
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
    private static final String SHORT_INVOICE ="SHORT_INVOICE";
    private static final String DONT_PRINT_HEADER ="DONT_PRINT_HEADER";
    private static final String TAYE_LAYOUT="TAYE_LAYOUT";
     private static final String PrintItemNumber="PrintItemNumber";
     private static final String CurrencyValue="CurrencyValue";
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
     private static final String MAXD="MAXD";
     private static final String FAX="FAX";
     private static final String ZipCode="ZipCode";
     private static final String EMail="EMail";
     private static final String C_THECATEG="C_THECATEG";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String Item_Unit_Details = "Item_Unit_Details";

    private static final String ComapnyNo = "ComapnyNo";
    private static final String ItemNo = "ItemNo";
    private static final String UnitID = "UnitID";
    private static final String ConvRate = "ConvRate";
    private static final String PriceUnit = "PriceUnit";
    private static final String ItemBarcode = "ItemBarcode";

    private static final String PRICECLASS_1 = "PRICECLASS_1";
    private static final String PRICECLASS_2 = "PRICECLASS_2";
    private static final String PRICECLASS_3 = "PRICECLASS_3";

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
    private static final String IP_ADDRESS_DEVICE = "IP_ADDRESS_DEVICE";
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
    private static final String UserType = "UserType";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String CustomerPrices = "CustomerPrices";

    private static final String ItemNumber = "ItemNumber";


    private static final String CustomerNumber = "CustomerNumber";
    private static final String Price = "Price";
    private static final String DISCOUNT_CUSTOMER = "DISCOUNT_CUSTOMER";
    private static final String ItemNo_ = "ItemNo_";
    private static final String Other_Discount = "Other_Discount";
    private static final String FromDate = "FromDate";
    private static final String ToDate = "ToDate";
    private static final String ListNo = "ListNo";
    private static final String ListType = "ListType";

    // tables from ORIGINAL
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String CustomerPricesCurrent = "CustomerPricesCurrent";

    private static final String ItemNumber_ = "ItemNumber_";


    private static final String CustomerNumber_ = "CustomerNumber_";
    private static final String Price_ = "Price_";
    private static final String DISCOUNT_CUSTOMER_ = "DISCOUNT_CUSTOMER_";
    private static final String ItemNo_N = "ItemNo_N";
    private static final String Other_Discount_ = "Other_Discount_";
    private static final String FromDate_ = "FromDate_";
    private static final String ToDate_ = "ToDate_";
    private static final String ListNo_ = "ListNo_";
    private static final String ListType_ = "ListType_";

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
    private static final String REAL_LONGTUD = "REAL_LONGTUD";
    private static final String REAL_LATITUDE = "REAL_LATITUDE";
     private static final String  VOUCHERCOUNT= "VOUCHERCOUNT";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String TABLE_SETTING = "SETTING";

    private static final String IP_ADDRESS = "IP_ADDRESS";
    private static final String TAX_CALC_KIND = "TAX_CALC_KIND";
     private static final String plansetting_KIND = "plansetting_KIND";

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
    private static final String CAN_CHANGE_PRICE_RETURNONLY = "CAN_CHANGE_PRICE_RETURNONLY";
    private static final String READ_DISCOUNT_FROM_OFFERS = "READ_DISCOUNT_FROM_OFFERS";
    private static final String WORK_ONLINE = "WORK_ONLINE";
    private static final String PAYMETHOD_CHECK = "PAYMETHOD_CHECK";
    private static final String BONUS_NOT_ALLOWED = "BONUS_NOT_ALLOWED";
    private static final String NO_OFFERS_FOR_CREDIT_INVOICE = "NO_OFFERS_FOR_CREDIT_INVOICE";
    private static final String AMOUNT_OF_MAX_DISCOUNT = "AMOUNT_OF_MAX_DISCOUNT";
    private static final String Customer_Authorized = "Customer_Authorized";
    private static final String Password_Data = "Password_Data";
    private static final String Arabic_Language = "Arabic_Language";
    private static final String HideQty = "HideQty";
    private static final String LockCashReport = "LockCashReport";
    private static final String salesManName = "salesManName";
    private static final String PreventOrder = "PreventOrder";
    private static final String RequiredNote = "RequiredNote";
    private static final String PreventTotalDiscount = "PreventTotalDiscount";
    private static final String AutomaticCheque = "AutomaticCheque";
    private static final String Tafqit = "Tafqit";
    private static final String PreventChangPayMeth = "PreventChangPayMeth";
    private static final String ShowCustomerList = "ShowCustomerList";
    private static final String NoReturnInvoice = "NoReturnInvoice";
    private static final String WORK_WITH_SERIAL = "WORK_WITH_SERIAL";
    private static final String SHOW_IMAGE_ITEM = "SHOW_IMAGE_ITEM";
    private static final String APPROVE_ADMIN = "APPROVE_ADMIN";
    private static final String SAVE_ONLY = "SAVE_ONLY";
    private static final String SHOW_QUANTITY_SOLD = "SHOW_QUANTITY_SOLD";
    private static final String READ_OFFER_FROM_ADMIN = "READ_OFFER_FROM_ADMIN";
    private static final String IP_PORT = "IP_PORT";
    private static final String CheckQtyServer = "CheckQtyServer";
    private static final String DontShowTaxOnPrinter = "DontShowTaxOnPrinter";
    private static final String CONO = "CONO";
    private static final String ContinusReading = "ContinusReading";
    private static final String ActiveTotalDisc = "ActiveTotalDisc";
    private static final String ValueTotalDisc = "ValueTotalDisc";
    private static final String STORE_NO = "STORE_NO";
    private static final String Item_Unit = "Item_Unit";
    private static final String SUM_CURRENT_QTY = "SUM_CURRENT_QTY";
    private static final String DONT_DUPLICATE_ITEMS = "DONT_DUPLICATE_ITEMS";
    private static final String OffersJustForSales= "OffersJustForSales";
    private static final String CheckQtyinOrder= "CheckQtyinOrder";
    private static final String locationtracker ="LocationTracker";
    private static final String AqapaTax ="AqapaTax";
    private static final String SHOW_CUSTOMER_LOCATION ="SHOW_CUSTOMER_LOCATION";
    private static final String Items_Units ="Items_Units";
    private static final String EndTripReport ="EndTripReport";
    private static final String AcountatatmentVis ="AcountatatmentVis";
    private static final String SharWhatsApp_Form ="SharWhatsAppForm";
    private static final String DISC_AFTER_TAX ="DISC_AFTER_TAX";
    private static final String Add_CustumerPer ="Add_CustumerPer";
    private static final String LastCustPrice ="LastCustPrice";
     private static final String ReturnVoch_PERM ="ReturnVoch_PERM";
     private static final String AddedCustomer_PERM ="AddedCustomer_PERM";
     private static final String CompanyinfoINPdf ="CompanyinfoINPdf";



    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String COMPANY_INFO = "COMPANY_INFO";

    private static final String COMPANY_NAME = "COMPANY_NAME";
    private static final String COMPANY_TEL = "COMPANY_TEL";
    private static final String COMPANY_PHONE = "phonnum";
    private static final String TAX_NO = "TAX_NO";
    private static final String LOGO = "LOGO";
    private static final String NOTE = "NOTE";

    private static final String LONGTUDE_COMPANY = "LONGTUDE_COMPANY";
    private static final String LATITUDE_COMPANY = "LATITUDE_COMPANY";
    private static final String NOTEPOSITION = "NOTEPOSITION";
    private static final String netsalFLAG = "netsalFLAG";
    private static final String HeaderprintInOrders = "HeaderprintInOrders";
    private static final String NATIONAL_ID="NATIONAL_ID";
     private static final String CAR_NO="CAR_NO";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SALES_VOUCHER_MASTER = "SALES_VOUCHER_MASTER";

    private static final String COMPANY_NUMBER = "COMPANY_NUMBER";
    private static final String VOUCHER_NUMBER = "VOUCHER_NUMBER";
    private static final String ORIGINALVOUCHER_NUMBER = "ORIGINALVOUCHER_NUMBER";
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
    private static final String VOUCHER_time = "VOUCHER_time";
    private static final String TAX_TYPE = "TAX_TYPE";


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

    private static final String WHICH_UNIT = "WHICH_UNIT";
    private static final String WHICH_UNIT_STR = "WHICH_UNIT_STR";
    private static final String WHICHU_QTY = "WHICHU_QTY";
    private static final String ENTER_QTY = "ENTER_QTY";
    private static final String ENTER_PRICE = "ENTER_PRICE";
    private static final String UNIT_BARCODE = "UNIT_BARCODE";

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
    private static final String CUSTOMER_NUMBER_STR = "CUSTOMER_NUMBER_STR";


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
    private static final String CURRENT_QTY = "CURRENT_QTY";
    private static final String isPostedDetails = "isPostedDetails";
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String ADDED_CUSTOMER = "ADDED_CUSTOMER";

    private static final String CUST_NAME7 = "CUST_NAME";
    private static final String REMARK7 = "REMARK";
    private static final String LATITUDE7 = "LATITUDE";
    private static final String LONGITUDE7 = "LONGITUDE";
    private static final String SALESMAN7 = "SALESMAN";
    private static final String SALESMAN_NO7 = "SALESMAN_NO";
    private static final String IS_POSTED7 = "IS_POSTED";
    private static final String ADRESS_CUSTOMER = "ADRESS_CUSTOMER";
    private static final String TELEPHONE = "TELEPHONE";
    private static final String CONTACT_PERSON = "CONTACT_PERSON";

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
    private static final String DISCOUNT_ITEM_TYPE = "DISCOUNT_ITEM_TYPE";
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SALESMEN_STATIONS = "SALESMEN_STATIONS";

    private static final String SALESMEN_NO = "SALESMEN_NO";
    private static final String DATE_ = "DATE_";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String SERIAL = "SERIAL";
    private static final String CUSTOMR_NO = "CUSTOMR_NO";
    private static final String CUSUSTOMR_NAME = "CUSUSTOMR_NAME";
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private static final String SalesMenLogIn = "SalesMenLogIn";

    private static final String UserNo_LogIn = "UserNo_LogIn";
    private static final String VERSION_APK = "VERSION_APK";

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    ////B
    private static final String Flag_Settings = "Flag_Settings";
    private static final String Data_Type = "Data_Type";
    private static final String Export_Stock = "Export_Stock";
    private static final String Max_Voucher = "Max_Voucher";
    private static final String Max_VoucherSever = "Max_VoucherSever";
    private static final String Make_Order = "Make_Order";
    private static final String Admin_Password = "Admin_Password";
    private static final String Total_Balance = "Total_Balance";
    private static final String Voucher_Return = "Voucher_Return";
    private static final String ActiveSlasmanPlan = "ActiveSlasmanPlan";
    private static final String ActiveSlasmanTrips = "ActiveSlasmanTrips";
    private static final String POS_ACTIVE = "POS_ACTIVE";
    private static final String CakeShop_Offer = "CakeShop_Offer";
    private static final String Talaat_Offer = "Talaat_Offer";
    private static final String Qasion_Offer = "Qasion_Offer";
    private static final String Purchase_Order = "Purchase_Order";
    private static final String NO_TAX = "NO_TAX";
    private static final String exportedVochTax = "exportedVochTax";

    private static final String Nasleh_Offer = "Nasleh_Offer";
     private static final String MaxPaymentVoucherSever = "MaxPaymentVoucherSever";

    //----------------------------------------------------------------------
    private static final String SalesMan_Plan = "SalesMan_Plan";

    private static final String orederd = "ORDERED";
    private static final String typeorederd = "TYPEORDERED";
    private static final String LogoutStatus = "LogoutStatus";
 //------------------------------------------------------------------------------
 private static final String SalesMan_Item_availability = "SalesMan_Item_availability";

    private static final String availability = "AVAILABLITY";
//-----------------------------------------------------
private static final String  SalemanTrips="SalemanTrips";
    private static final String  TripNum="TripNum";
    private static final String  TripstartDate="TripstartDate";
    private static final String  TripstartTime="TripstartTime";
    private static final String  TripEndDate="TripEndDate";
    private static final String  TripEndTime="TripEndTime";
    private static final String  TripStatus="TripStatus";

//-----------------------------------------------------
private static final String  TransactionInfo="TransactionInfo_tabel";
    private static final String  Reson="Reson";
    private static final String  PersonName="PersonName";
    private static final String  PhoneNum="PhoneNum";

    private static final String  Cust_num="Cust_num";
    private static final String  Cust_name="Cust_name";
    //-----------------------------------------------------
    private static final String  PaymentSerials="PaymentSerials";
    private static final String  PaymentSerials_cash="PaymentSerials_cash";
    private static final String  PaymentSerials_cridt="PaymentSerials_cridt";

    //---------------------------------------

     private static final String  AdminPasswords="AdminPasswords";
     private static final String  password_type="password_type";
     private static final String  password_no="password";
     //---------------------------------------

     private static final String  VisitMedicalTable="VisitMedicalTable";
     private static final String  VoucherNo="VoucherNo";
     private static final String  DateVisit="DateVisit";
     private static final String  CustNo="CustNo";
     private static final String  CustName="CustName";
     private static final String  Adoption="Adoption";
     private static final String  Tool="Tool";
     private static final String  DoubleVisit="DoubleVisit";
     private static final String  RemarkVisit="RemarkVisit";
     private static final String  HaveItem="HaveItem";
     //---------------------------------------
     private static final String  ItemsVisit="ItemsVisit";
     private static final String  VoucherNoItem="VoucherNoItem";
     private static final String  CustNoItem="CustNoItem";
     private static final String  ItemName="ItemName";
     private static final String  QtyItem="QtyItem";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

       String CREATE_Item_Switch_TABLE = "CREATE TABLE IF NOT EXISTS " + Item_Switch + "("
               + ITEM_NAMEA + " TEXT,"
               + ITEM_OCODE + " TEXT,"
               + ITEM_NCODE + " TEXT"

               + ")";
       db.execSQL(CREATE_Item_Switch_TABLE);
       //***************************************************************************************
       String CREATE_SALESMAN_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + SALESMAN_LOGIN_LOGHistory + "("
               + DATE_LOGIN + " TEXT,"
               + TIME_LOGIN + " TEXT,"
               + TIME_LOGOUT + " TEXT,"
               + LONGTUDE2 + " REAL,"
               + LATITUDE2 + " REAL,"
               + SALESMAN_NO + " TEXT,"
               + IS_POSTED_LOGIN + " INTEGER,"
               + "  PRIMARY KEY (DATE_LOGIN)" +

               ")";
       db.execSQL(CREATE_SALESMAN_LOGIN_TABLE);
       //-------------------------------------------------------------------------


       String CREATE_SERIAL_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " + SERIAL_ITEMS_TABLE + "("
               + KEY_SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
               + SERIAL_CODE_NO + " TEXT,"
               + COUNTER_SERIAL + " INTEGER,"
               + VOUCHER_NO + " INTEGER,"
               + ITEMNO_SERIAL + " TEXT,"
               + KIND_VOUCHER + " TEXT,"

               + DATE_VOUCHER + " TEXT,"
               + STORE_NO_SALESMAN + " INTEGER,"
               + IS_POSTED_SERIAL + " INTEGER,"
               + IS_BONUS_SERIAL + " INTEGER,"
               + Price_ITEM + " real, "
               + Price_ITEM_Sales + " real,"
               + IS_RETURNED + " INTEGER " +


               ")";
       db.execSQL(CREATE_SERIAL_ITEMS_TABLE);
       //-------------------------------------------------------------------------

       String CREATE_SERIAL_ITEMS_TABLE_backup = "CREATE TABLE IF NOT EXISTS " + SERIAL_ITEMS_TABLE_backup + "("
               + KEY_SERIAL2 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
               + SERIAL_CODE_NO2 + " TEXT,"
               + COUNTER_SERIAL2 + " INTEGER,"
               + VOUCHER_NO2 + " INTEGER,"
               + ITEMNO_SERIAL2 + " TEXT,"
               + KIND_VOUCHER2 + " TEXT,"

               + DATE_VOUCHER2 + " TEXT,"
               + STORE_NO_SALESMAN2 + " INTEGER,"
               + IS_POSTED_SERIAL2 + " INTEGER,"
               + IS_BONUS_SERIAL2 + " INTEGER,"
               + isItemDelete + " INTEGER,"

               + dateDelete + " TEXT" +

                ")";
        db.execSQL(CREATE_SERIAL_ITEMS_TABLE_backup);
        //-------------------------------------------------------------------------

        try {



            String CREATE_SERIAL_TABLE= "CREATE TABLE IF NOT EXISTS " + SerialItemMaster + "("

                    + StoreNo + " TEXT,"
                    + ITEM_OCODE_M + " TEXT,"
                    + SerialCode + " TEXT,"
                    + Qty_serial + " TEXT"

                    +


                    ")";
            db.execSQL(CREATE_SERIAL_TABLE);
        }catch (Exception e){}


        //-------------------------------------------------------------------------
        try {


          String CREATE_OfferListMaster_TABLE = "CREATE TABLE IF NOT EXISTS " + price_offer_list_master + "("

                  + PO_LIST_NO + " INTEGER,"
                  + PO_LIST_NAME + " TEXT,"
                  + PO_LIST_TYPE + " INTEGER,"
                  + FROM_DATE_master + " TEXT,"
                  + TO_DATE_master + " TEXT"
                  +


                  ")";
          db.execSQL(CREATE_OfferListMaster_TABLE);
       } catch (Exception e) {
       }


       //-------------------------------------------------------------------------
       String CREATE_TABLE_PASSWORD_TABLE = "CREATE TABLE IF NOT EXISTS " + PASSWORD_TABLE + "("
               + PASS_TYPE + " INTEGER,"
               + PASS_NO + " TEXT" +
               ")";
       db.execSQL(CREATE_TABLE_PASSWORD_TABLE);
       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ


       String CREATE_TABLE_CUSTOMER_LOCATION = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_LOCATION + "("
               + CUS_NO + " TEXT,"
               + LONG + " TEXT,"
               + LATIT + " TEXT" +

               ")";
       db.execSQL(CREATE_TABLE_CUSTOMER_LOCATION);
       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_ACCOUNT_REPORT = "CREATE TABLE  IF NOT EXISTS " + ACCOUNT_REPORT + "("
               + DATE + " TEXT,"
               + TRANSFER_NAME + " TEXT,"
               + DEBTOR + " TEXT,"
               + CREDITOR + " TEXT,"
               + TODATE + " TEXT,"
               + CUST_BALANCE + " TEXT,"
               + CUST_NUMBER_REPORT + " TEXT" +
               ")";
       db.execSQL(CREATE_TABLE_ACCOUNT_REPORT);
       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ


       String CREATE_TABLE_ITEMS_QTY_OFFER = "CREATE TABLE IF NOT EXISTS " + ITEMS_QTY_OFFER + "("
               + ITEMNAME + " TEXT,"
               + ITEMNO + " INTEGER,"
               + AMOUNT_QTY + " INTEGER,"
               + FROMDATE + " TEXT,"
               + TODATE + " TEXT,"
               + DISCOUNT + " REAL" + ")";
       db.execSQL(CREATE_TABLE_ITEMS_QTY_OFFER);
       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_QTY_OFFERS = "CREATE TABLE IF NOT EXISTS " + QTY_OFFERS + "("
               + QTY + " REAL,"
               + DISCOUNT_VALUE + " REAL,"
               + PAYMENT_TYPE + " INTEGER" + ")";

       db.execSQL(CREATE_TABLE_QTY_OFFERS);
       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
       String CREATE_TABLE_CUSTOMER_MASTER = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_MASTER + "("
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
               + ACCPRC + " TEXT,"
               + HIDE_VAL + " INTEGER,"

               + IS_POST + " INTEGER not null default  0,"
               + CUS_ID_Text + " TEXT,"
               +   MAXD + " TEXT DEFAULT '0',"
               +   FAX + " TEXT DEFAULT '0',"
               +   ZipCode + " TEXT DEFAULT '0',"
               +   EMail + " TEXT DEFAULT '0',"
               +   C_THECATEG + " TEXT DEFAULT '0'"
               + ")";

       Log.e("CREATE_TABLE_CUSTOMER_MASTER",""+CREATE_TABLE_CUSTOMER_MASTER);
       db.execSQL(CREATE_TABLE_CUSTOMER_MASTER);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_Item_Unit_Details = "CREATE TABLE IF NOT EXISTS " + Item_Unit_Details + "("
               + ComapnyNo + " INTEGER,"
               + ItemNo + " TEXT,"
               + UnitID + " TEXT,"
               + ConvRate + " INTEGER,"
               + PriceUnit + " TEXT,"
               + ItemBarcode + " TEXT ,"
               + PRICECLASS_1 + " TEXT ,"
               + PRICECLASS_2 + " TEXT ,"
               + PRICECLASS_3 + " TEXT "

               + ")";
       db.execSQL(CREATE_TABLE_Item_Unit_Details);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
       String CREATE_TABLE_VISIT_RATE = "CREATE TABLE  IF NOT EXISTS " + VISIT_RATE + "("
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

       String CREATE_TABLE_Items_Master = "CREATE TABLE IF NOT EXISTS " + Items_Master + "("
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
               + ITEM_PHOTO + " TEXT"

               + ")";

       db.execSQL(CREATE_TABLE_Items_Master);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
       String CREATE_ACTIVE_KEY = "CREATE TABLE IF NOT EXISTS " + ACTIVE_KEY + "("

               + KEY_VALUE + " INTEGER" + ")";
       db.execSQL(CREATE_ACTIVE_KEY);
       //-----------------------------------------------------

       String CREATE_PRINTER_SETTING_TABLE = "CREATE TABLE IF NOT EXISTS " + PRINTER_SETTING_TABLE + "("
               + PRINTER_SETTING + " INTEGER ,"
               + PRINTER_SHAPE + " INTEGER,"
               + SHORT_INVOICE + " INTEGER,"
               + DONT_PRINT_HEADER + " INTEGER,"
               + TAYE_LAYOUT + " INTEGER,"
               + netsalFLAG + " TEXT , "
               + HeaderprintInOrders + " INTEGER , "
               +PrintItemNumber+ " INTEGER , "
               +CurrencyValue+ " TEXT  "

               + ")";
       db.execSQL(CREATE_PRINTER_SETTING_TABLE);
//ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_Price_List_D = "CREATE TABLE IF NOT EXISTS " + Price_List_D + "("
               + ComapnyNo2 + " INTEGER,"
               + PrNo2 + " INTEGER,"
               + ItemNo2 + " TEXT,"
               + UnitID2 + " TEXT,"
               + Price2 + " INTEGER,"
               + TaxPerc2 + " INTEGER,"
               + MinSalePrice2 + " INTEGER" + ")";
       db.execSQL(CREATE_TABLE_Price_List_D);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_Price_List_M = "CREATE TABLE IF NOT EXISTS " + Price_List_M + "("
               + ComapnyNo3 + " INTEGER,"
               + PrNo3 + " INTEGER,"
               + Description3 + " TEXT,"
               + IsSuspended3 + " INTEGER" + ")";
       db.execSQL(CREATE_TABLE_Price_List_M);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_Sales_Team = "CREATE TABLE IF NOT EXISTS " + Sales_Team + "("
               + ComapnyNo4 + " INTEGER,"
               + SalesManNo4 + " INTEGER,"
               + SalesManName4 + " TEXT,"
               + IsSuspended4 + " INTEGER,"

               + IP_ADDRESS_DEVICE + " TEXT"

               + ")";
       db.execSQL(CREATE_TABLE_Sales_Team);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_SalesMan_Items_Balance = "CREATE TABLE IF NOT EXISTS " + SalesMan_Items_Balance + "("
               + ComapnyNo5 + " INTEGER,"
               + SalesManNo5 + " INTEGER,"
               + ItemNo5 + " TEXT,"
               + Qty5 + " INTEGER" + ")";
       db.execSQL(CREATE_TABLE_SalesMan_Items_Balance);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_SalesmanAndStoreLink = "CREATE TABLE IF NOT EXISTS " + SalesmanAndStoreLink + "("
               + ComapnyNo6 + " INTEGER,"
               + SalesManNo6 + " INTEGER,"
               + StoreNo6 + " INTEGER" + ")";
       db.execSQL(CREATE_TABLE_SalesmanAndStoreLink);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_Salesmen = "CREATE TABLE IF NOT EXISTS " + SalesMen + "("
               + UserName + " TEXT,"
               + Password + " TEXT," +
               UserType + " INTEGER"
               + ")";
       db.execSQL(CREATE_TABLE_Salesmen);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_CustomerPrices = "CREATE TABLE IF NOT EXISTS " + CustomerPrices + "("
               + ItemNumber + " INTEGER,"
               + CustomerNumber + " INTEGER,"
               + Price + " INTEGER,"
               + DISCOUNT_CUSTOMER + " real,"
               + ItemNo_ + " TEXT,"
               + Other_Discount + " TEXT,"
               + FromDate + " TEXT,"
               + ToDate + " TEXT,"
               + ListNo + " TEXT,"
               + ListType + " TEXT"

               + ")";
       db.execSQL(CREATE_TABLE_CustomerPrices);

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_CustomerPrices2 = "CREATE TABLE IF NOT EXISTS " + CustomerPricesCurrent + "("
               + ItemNumber_ + " INTEGER,"
               + CustomerNumber_ + " INTEGER,"
               + Price_ + " INTEGER,"
               + DISCOUNT_CUSTOMER_ + " real,"
               + ItemNo_N + " TEXT,"
               + Other_Discount_ + " TEXT,"
               + FromDate_ + " TEXT,"
               + ToDate_ + " TEXT,"
               + ListNo_ + " TEXT,"
               + ListType_ + " TEXT"

               + ")";
       db.execSQL(CREATE_TABLE_CustomerPrices2);


       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
try {


    String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + "("
            + SALES_MAN_ID + " INTEGER,"
            + CUS_CODE + " INTEGER,"
            + CUS_NAME + " TEXT,"
            + CHECK_IN_DATE + " TEXT,"
            + CHECK_IN_TIME + " TEXT,"
            + CHECK_OUT_DATE + " TEXT,"
            + CHECK_OUT_TIME + " TEXT,"
            + STATUS + " INTEGER,"
            + IS_POSTED2 + " INTEGER,"
            + REAL_LONGTUD + " TEXT,"
            + REAL_LATITUDE + " TEXT," +
            VOUCHERCOUNT + " INTEGER DEFAULT 0" +
            ")";
    db.execSQL(CREATE_TABLE_CONTACTS);
    Log.e("TABLE_TRANSACTIONS====",CREATE_TABLE_CONTACTS);
}catch (Exception exception){
    Log.e("Exception,TABLE_", exception.getMessage());
}

       //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

       String CREATE_TABLE_SETTING = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTING + "("
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
               + ALLOW_OUT_OF_RANGE + " INTEGER,"
               + READ_DISCOUNT_FROM_OFFERS + " INTEGER,"
               + WORK_ONLINE + " INTEGER,"
               + PAYMETHOD_CHECK + " INTEGER,"
               + BONUS_NOT_ALLOWED + " INTEGER,"
               + NO_OFFERS_FOR_CREDIT_INVOICE + " INTEGER, "
               + AMOUNT_OF_MAX_DISCOUNT + " INTEGER,"
               + Customer_Authorized + " INTEGER,"
               + Password_Data + " INTEGER,"
               + Arabic_Language + " INTEGER DEFAULT '1' ,"
               + HideQty + " INTEGER,"
               + LockCashReport + " INTEGER,"
               + salesManName + " TEXT,"
               + PreventOrder + " INTEGER,"
               + RequiredNote + " INTEGER,"
               + PreventTotalDiscount + " INTEGER,"
               + AutomaticCheque + " INTEGER,"
               + Tafqit + " INTEGER,"
               + PreventChangPayMeth + " INTEGER,"
               + ShowCustomerList + " INTEGER DEFAULT 1 ,"
               + NoReturnInvoice + " INTEGER,"
               + WORK_WITH_SERIAL + " INTEGER,"
               + SHOW_IMAGE_ITEM + " INTEGER,"

               + APPROVE_ADMIN + " INTEGER,"
               + SAVE_ONLY + " INTEGER,"
               + SHOW_QUANTITY_SOLD + " INTEGER,"
               + READ_OFFER_FROM_ADMIN + " INTEGER,"
               + IP_PORT + " TEXT,"
               + CheckQtyServer + " INTEGER,"
               + DontShowTaxOnPrinter + " INTEGER,"
               + CONO + " TEXT,"
               + ContinusReading + " INTEGER,"

               + ActiveTotalDisc + " INTEGER,"


               + ValueTotalDisc + " REAL, "
               + STORE_NO + " INTEGER, "
               + Item_Unit + " INTEGER ,"
               + SUM_CURRENT_QTY + " INTEGER, "
               + DONT_DUPLICATE_ITEMS + " INTEGER, "
               + CAN_CHANGE_PRICE_RETURNONLY + " INTEGER, "
               + OffersJustForSales + " INTEGER  DEFAULT 0 , "
               + CheckQtyinOrder + " INTEGER  DEFAULT 0 , "
               + locationtracker + " INTEGER DEFAULT 0 , "
               + AqapaTax + " INTEGER DEFAULT 0 , "
               + SHOW_CUSTOMER_LOCATION + " INTEGER DEFAULT 0 , "
               + Items_Units + " INTEGER DEFAULT 0 , "
               + EndTripReport + " INTEGER DEFAULT 0 ,"
    + AcountatatmentVis+" INTEGER DEFAULT 1 ,"
     +  SharWhatsApp_Form+" INTEGER DEFAULT 0 ,"
      + DISC_AFTER_TAX+" INTEGER DEFAULT 0 ,"
               + Add_CustumerPer+" INTEGER DEFAULT 1 ,"
      + LastCustPrice +" INTEGER DEFAULT 0 ,"
               + ReturnVoch_PERM +" INTEGER DEFAULT 0 ,"
               + plansetting_KIND +" INTEGER DEFAULT 0 ,"
               + CompanyinfoINPdf +" INTEGER DEFAULT 0 ,"
       + AddedCustomer_PERM+" INTEGER DEFAULT 0 "
                + ")";
        db.execSQL(CREATE_TABLE_SETTING);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_COMPANY_INFO = "CREATE TABLE IF NOT EXISTS " + COMPANY_INFO + "("
                + COMPANY_NAME + " TEXT,"
                + COMPANY_TEL + " INTEGER,"
                + TAX_NO + " INTEGER,"
                + LOGO + " BLOB,"
                + NOTE + " TEXT,"
                + LONGTUDE_COMPANY + " REAL,"
                + LATITUDE_COMPANY + " REAL,"
                + NOTEPOSITION + " TEXT,"
                + COMPANY_PHONE + " TEXT,"
                + NATIONAL_ID + " TEXT, "
                + CAR_NO + " TEXT "
                + ")";
        db.execSQL(CREATE_TABLE_COMPANY_INFO);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        try {


            String CREATE_TABLE_SALES_VOUCHER_MASTER = "CREATE TABLE  IF NOT EXISTS " + SALES_VOUCHER_MASTER + "("
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
                    + VOUCHER_YEAR + " INTEGER,"
                    + VOUCHER_time + " TEXT, "
                    + ORIGINALVOUCHER_NUMBER+" INTEGER DEFAULT '0',"
                    +TAX_TYPE+" INTEGER DEFAULT '0'"

                    + ")";
            db.execSQL(CREATE_TABLE_SALES_VOUCHER_MASTER);
        } catch (Exception e) {
            Log.e("databaseHandler", "CREATE_TABLE_SALES_VOUCHER_MASTER");
        }

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SALES_VOUCHER_DETAILS = "CREATE TABLE IF NOT EXISTS " + SALES_VOUCHER_DETAILS + "("
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
                + ITEM_DESCRIPTION + " TEXT,"
                + SERIAL_CODE + " TEXT,"

                + VOUCH_DATE + " TEXT, "
                + WHICH_UNIT + " TEXT, "
                + WHICH_UNIT_STR + " TEXT, "
                + WHICHU_QTY + " TEXT, "
                + ENTER_QTY + " TEXT, "
                + ENTER_PRICE + " TEXT, "
                + UNIT_BARCODE + " TEXT, "
                +IS_RETURNED+" INTEGER DEFAULT 0, "
                + "Avilable_Qty" + " TEXT DEFAULT '0',"
                + ORIGINALVOUCHER_NUMBER+" INTEGER DEFAULT '0'"
                + ")";
        db.execSQL(CREATE_TABLE_SALES_VOUCHER_DETAILS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_PAYMENTS = "CREATE TABLE IF NOT EXISTS " + PAYMENTS + "("
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
                + PAY_YEAR + " INTEGER ,"
                + CUSTOMER_NUMBER_STR + " TEXT "

                + ")";
        db.execSQL(CREATE_TABLE_PAYMENTS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_PAYMENTS_PAPER = "CREATE TABLE IF NOT EXISTS " + PAYMENTS_PAPER + "("
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

        String CREATE_TABLE_REQUEST_MASTER = "CREATE TABLE IF NOT EXISTS " + REQUEST_MASTER + "("
                + COMPANY_NUMBER5 + " INTEGER,"
                + VOUCHER_NUMBER5 + " INTEGER,"
                + VOUCHER_DATE5 + " TEXT,"
                + SALES_MAN_NUMBER5 + " INTEGER,"
                + REMARK5 + " TEXT,"
                + TOTAL_QTY5 + " INTEGER,"
                + IS_POSTED5 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_REQUEST_MASTER);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_REQUEST_DETAILS = "CREATE TABLE IF NOT EXISTS " + REQUEST_DETAILS + "("
                + COMPANY_NUMBER6 + " INTEGER,"
                + VOUCHER_NUMBER6 + " INTEGER,"
                + ITEM_NUMBER6 + " TEXT,"
                + ITEM_NAME6 + " TEXT,"
                + UNIT_QTY6 + " INTEGER,"
                + VOUCHER_DATE5 + " TEXT,"
                + CURRENT_QTY + " real,"
                + isPostedDetails + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_REQUEST_DETAILS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_ADDED_CUSTOMER = "CREATE TABLE IF NOT EXISTS " + ADDED_CUSTOMER + "("
                + CUST_NAME7 + " TEXT,"
                + REMARK7 + " TEXT,"
                + LATITUDE7 + " INTEGER,"
                + LONGITUDE7 + " INTEGER,"
                + SALESMAN7 + " TEXT,"
                + IS_POSTED7 + " INTEGER,"
                + SALESMAN_NO7 + " TEXT,"
                + ADRESS_CUSTOMER + " TEXT,"

                + TELEPHONE + " TEXT,"

                + CONTACT_PERSON + " TEXT"



                + ")";
        db.execSQL(CREATE_TABLE_ADDED_CUSTOMER);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_VS_PROMOTION = "CREATE TABLE IF NOT EXISTS " + VS_PROMOTION + "("
                + PROMOTION_ID + " INTEGER,"
                + PROMOTION_TYPE + " INTEGER,"
                + FROM_DATE + " TEXT,"
                + TO_DATE + " TEXT,"
                + ITEM_NUMBERS + " TEXT,"
                + ITEM_QTY + " INTEGER,"
                + BONUS_QTY + " INTEGER,"
                + BONUS_ITEM_NO + " TEXT,"
                +DISCOUNT_ITEM_TYPE+" INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_VS_PROMOTION);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SALESMEN_STATIONS = "CREATE TABLE IF NOT EXISTS " + SALESMEN_STATIONS + "("
                + SALESMEN_NO + " TEXT,"
                + DATE_ + " TEXT,"
                + LATITUDE + " TEXT,"
                + LONGITUDE + " TEXT,"
                + SERIAL + " INTEGER,"
                + CUSTOMR_NO + " TEXT,"
                + CUSUSTOMR_NAME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SALESMEN_STATIONS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //////B
        String CREATE_TABLE_FlAG_SETTINGS = "CREATE TABLE IF NOT EXISTS " + Flag_Settings + "("
                + Data_Type + " TEXT,"
                + Export_Stock + " INTEGER,"
                + Max_Voucher + " INTEGER,"
                + Make_Order + " INTEGER,"
                + Admin_Password + " INTEGER,"
                + Total_Balance + " INTEGER,"
                + Voucher_Return + " INTEGER,"
                + ActiveSlasmanPlan + " INTEGER DEFAULT '0' ,"
                + POS_ACTIVE + " INTEGER DEFAULT '0' ,"
                + CakeShop_Offer + " INTEGER DEFAULT '0' ,"
                + Talaat_Offer + " INTEGER DEFAULT '0' ,"
                + Qasion_Offer + " INTEGER DEFAULT '0' ,"
                + ActiveSlasmanTrips + " INTEGER DEFAULT '0' ,"
                + Max_VoucherSever + " INTEGER DEFAULT '0' , "
                +Purchase_Order+" INTEGER DEFAULT '0', "
                +NO_TAX+ " INTEGER DEFAULT '0' ,"
                + Nasleh_Offer + " INTEGER DEFAULT '0' ,"
                + exportedVochTax + " INTEGER DEFAULT '0' ,"
                + MaxPaymentVoucherSever + " INTEGER DEFAULT '0' "

                + ")";
        db.execSQL(CREATE_TABLE_FlAG_SETTINGS);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        String CREATE_TABLE_SALESMEN_LOG_IN = "CREATE TABLE IF NOT EXISTS " + SalesMenLogIn + "( "

                + UserNo_LogIn + " TEXT,"
                + LATITUDE + " TEXT,"
                + LONGITUDE + " TEXT ,"
                +VERSION_APK+ " TEXT " +
                ")";
        db.execSQL(CREATE_TABLE_SALESMEN_LOG_IN);
        try {
            String CREATE_TABLE_INVENTORY_SHELF = "CREATE TABLE IF NOT EXISTS " + INVENTORY_SHELF + "("
                    + TRANS_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ITEM_NO + " TEXT,"
                    + SERIAL_NO + " TEXT,"
                    + QTY_ITEM + " INTEGER,"
                    + TRANS_DATE + " TEXT,"
                    + CUSTOMER_NO + " TEXT,"
                    + SALESMAN_NUMBER + " TEXT,"
                    + VOUCHER_NUMBER_INVENTORY + " INTEGER,"
                    + IsPosted + " INTEGER "
                    + ")";
            db.execSQL(CREATE_TABLE_INVENTORY_SHELF);

        } catch (Exception e) {
        }
        try {
            String CREATE_TABLE_VoucherSerialize = "CREATE TABLE IF NOT EXISTS " + VoucherSerialize + "("
                    + VoucherSales_no + " INTEGER,"
                    + VoucherReturn_no + " INTEGER,"
                    + VoucherNewOrder_no + " INTEGER"
                    + ")";
            db.execSQL(CREATE_TABLE_VoucherSerialize);

        } catch (Exception e) {
        }
        try {
            // GroupOffer_Item  = "GroupOffer_Item";
            // Id_serial   = "Id_serial";
            // ItemNo_Offer   = "ItemNo_Offer";
            // ItemName_Offer   = "ItemName_Offer";
            // From_Date_Offer   = "From_Date_Offer";
            // To_Date_Offer   = "To_Date_Offer";
            // Discount_Offer   = "Discount_Offer";
            // Discount_Type_Offer   = "Discount_Type_Offer"
            // GroupId_Offer   = "GroupId_Offer";
            // Qty_item   = "Qty_item";
            String CREATE_TABLE_GroupOffer_Item = "CREATE TABLE IF NOT EXISTS " + GroupOffer_Item + "("
                    + Id_serial + " INTEGER,"
                    + ItemNo_Offer + " TEXT,"
                    + ItemName_Offer + " TEXT,"
                    + From_Date_Offer + " TEXT,"

                    + To_Date_Offer + " TEXT,"

                    + Discount_Offer + " TEXT,"

                    + Discount_Type_Offer + " INTEGER,"
                    + GroupId_Offer + " INTEGER,"

                    + Qty_item + " TEXT "


                    + ")";
            db.execSQL(CREATE_TABLE_GroupOffer_Item);


        } catch (Exception e) {

        }




        try{

            String CREATE_TABLE_SalesMan_Plan = "CREATE TABLE  IF NOT EXISTS " + SalesMan_Plan + "("
                    + DATE + " TEXT,"
                    + SALES_MAN_NUMBER + " INTEGER,"
                    + CUSTOMER_NAME + " TEXT,"
                    + CUSTOMER_NO + " INTEGER,"
                    + LATITUDE + " TEXT,"
                    + LONGITUDE + " TEXT,"
                    + orederd + " INTEGER,"
                    + typeorederd + " INTEGER,"
                    + LogoutStatus + " INTEGER DEFAULT '0'" +
                    ")";
            db.execSQL(CREATE_TABLE_SalesMan_Plan);}
        catch (Exception e){

        }
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ



        String SalesMan_Item_availability_TABLE = "CREATE TABLE IF NOT EXISTS " + SalesMan_Item_availability + "("
                + SALESMAN_NO + " INTEGER,"
                + ITEM_OCODE + " TEXT,"
                + availability + " INTEGER"

                + ")";
        db.execSQL(SalesMan_Item_availability_TABLE);


////////--------------------------------

        String CREATE_SalemanTrips_TABLE = "CREATE TABLE IF NOT EXISTS " + SalemanTrips + "("
                + TripNum + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TripstartDate + " TEXT,"
                + TripstartTime + " TEXT,"
                + TripEndDate + " TEXT,"
                + TripEndTime + " TEXT,"
                + TripStatus + " TEXT"
                + ")";
        db.execSQL(CREATE_SalemanTrips_TABLE);
        ///////-------------------------


       String CREATE_STransactionInfo_TABLE = "CREATE TABLE IF NOT EXISTS " + TransactionInfo + "("
                + " Serial INTEGER PRIMARY KEY AUTOINCREMENT,"
               + Reson + " TEXT,"
               + PersonName + " TEXT,"
               + PhoneNum + " TEXT,"
               + Cust_num + " TEXT,"
               + Cust_name + " TEXT,"
               + DATE_ + " TEXT"
               + ")";
       db.execSQL(CREATE_STransactionInfo_TABLE );
       //------------------------------------------------
       String CREATE_AdminPasswords_TABLE = "CREATE TABLE IF NOT EXISTS " + AdminPasswords+ "("
               + password_type + " TEXT,"
               + password_no + " TEXT"

               + ")";
       db.execSQL(CREATE_AdminPasswords_TABLE);

        String CREATE_PaymentSerials_TABLE = "CREATE TABLE IF NOT EXISTS " + PaymentSerials+ "("
                + PaymentSerials_cash + " TEXT,"
                + PaymentSerials_cridt + " TEXT"

                + ")";
        db.execSQL(CREATE_PaymentSerials_TABLE);

        String CREATE_VisitTABLE = "CREATE TABLE IF NOT EXISTS " + VisitMedicalTable + "("
                + " VoucherNo INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DateVisit + " TEXT,"
                + CustNo + " TEXT,"
                + CustName + " TEXT,"
                + Adoption + " TEXT,"
                + Tool + " TEXT,"
                + DoubleVisit + " TEXT,"
                + RemarkVisit + " TEXT,"
                + HaveItem + " TEXT"
                + ")";
        db.execSQL(CREATE_VisitTABLE );

        String CREATE_ItemsVisitTABLE = "CREATE TABLE IF NOT EXISTS " + ItemsVisit + "("

                + VoucherNoItem + " TEXT,"
                + CustNoItem + " TEXT,"
                + ItemName + " TEXT,"
                + QtyItem + " TEXT"

                + ")";
        db.execSQL(CREATE_ItemsVisitTABLE );
        String CREATE_NetworkTABLE = "CREATE TABLE IF NOT EXISTS " + NetworkTableLog + "("
                + TransNo + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LinkRequest + " TEXT,"
                + DateTrand + " TEXT,"
                + TimeTrans + " TEXT,"
                + CustomerNo + " TEXT,"
                + ResponseLink + " TEXT,"
                +NoteExport + " TEXT "

                + ")";
        db.execSQL(CREATE_NetworkTABLE );
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("onUpgrade","onUpgrade");
       db.execSQL("DROP TABLE IF EXISTS SALESMAN_LOGIN_TABLE ");
       onCreate(db);

       try {
          db.execSQL("ALTER TABLE SETTING ADD Customer_Authorized INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD Password_Data INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD Arabic_Language INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE SETTING ADD HideQty  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD LockCashReport  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD salesManName  TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD PreventOrder  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD RequiredNote  INTEGER NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD PreventTotalDiscount  INTEGER NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD AutomaticCheque  INTEGER NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD Tafqit  INTEGER NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD PreventChangPayMeth  INTEGER NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD ShowCustomerList  INTEGER NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD NoReturnInvoice  INTEGER NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD WORK_WITH_SERIAL  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD SHOW_IMAGE_ITEM  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD APPROVE_ADMIN  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD SAVE_ONLY  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD SHOW_QUANTITY_SOLD  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD READ_OFFER_FROM_ADMIN  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD IP_PORT  TEXT NOT NULL DEFAULT ' '");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD CheckQtyServer INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD DontShowTaxOnPrinter INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD CONO TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD ContinusReading INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD ActiveTotalDisc INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD ValueTotalDisc REAL NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD STORE_NO REAL NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD  Item_Unit INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD  SUM_CURRENT_QTY INTEGER NOT NULL DEFAULT '0'");
          db.execSQL("ALTER TABLE SETTING ADD  DONT_DUPLICATE_ITEMS INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


//**************************************End Table setting *************************************************************
       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD ITEM_DESCRIPTION  TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD HIDE_VAL  TEXT NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD ACCPRC  TEXT NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE Items_Master ADD KIND_ITEM  TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD  MAX_DISCOUNT  REAL NOT NULL DEFAULT '0'");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE QTY_OFFERS ADD  PAYMENT_TYPE  INTEGER NOT NULL DEFAULT '0'");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD PRINT_METHOD TEXT NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE VISIT_RATE ADD CUST_CODE TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE VISIT_RATE ADD CUST_NAME TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE VISIT_RATE ADD SALESMAN TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE VISIT_RATE ADD SALESMAN TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SALESMEN_STATIONS ADD CUSTOMR_NO TEXT  NOT NULL DEFAULT '111'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD ALLOW_OUT_OF_RANGE INTEGER  NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD CAN_CHANGE_PRICE INTEGER  NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE Items_Master ADD F_D REAL  NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD BONUS_NOT_ALLOWED INTEGER  NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD NO_OFFERS_FOR_CREDIT_INVOICE INTEGER  NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD AMOUNT_OF_MAX_DISCOUNT INTEGER NOT NULL   DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD WORK_ONLINE INTEGER  NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD PAYMETHOD_CHECK INTEGER  NOT NULL DEFAULT '1'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE TRANSACTIONS ADD IS_POSTED2 INTEGER  NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          String CREATE_TABLE_ACCOUNT_REPORT = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_REPORT + "("
                  + DATE + " TEXT,"
                  + TRANSFER_NAME + " TEXT,"
                  + DEBTOR + " TEXT,"
                  + CREDITOR + " TEXT,"
                  + TODATE + " TEXT,"
                  + CUST_BALANCE + " TEXT,"
                  + CUST_NUMBER_REPORT + " TEXT"
                  + ")";
          db.execSQL(CREATE_TABLE_ACCOUNT_REPORT);


       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {

          String CREATE_TABLE_SALESMEN_STATIONS = "CREATE TABLE  IF NOT EXISTS " + SALESMEN_STATIONS + "("
                  + SALESMEN_NO + " TEXT,"
                  + DATE_ + " TEXT,"
                  + LATITUDE + " TEXT,"
                  + LONGITUDE + " TEXT,"
                  + SERIAL + " INTEGER,"
                  + CUSTOMR_NO + " TEXT,"
                  + CUSUSTOMR_NAME + " TEXT" + ")";
          db.execSQL(CREATE_TABLE_SALESMEN_STATIONS);

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {


          String CREATE_ACTIVE_KEY = "CREATE TABLE IF NOT EXISTS " + ACTIVE_KEY + "("

                  + KEY_VALUE + " INTEGER" + ")";
          db.execSQL(CREATE_ACTIVE_KEY);


       } catch (Exception e) {
          Log.e("onUpgrade*****", "duplicated column");
       }


       try {
          String CREATE_PRINTER_SETTING_TABLE = "CREATE TABLE IF NOT EXISTS  " + PRINTER_SETTING_TABLE + "("

                  + PRINTER_SETTING + " INTEGER" + ")";
          db.execSQL(CREATE_PRINTER_SETTING_TABLE);


       } catch (Exception e) {
          Log.e("onUpgrade*****", "duplicated column PRINTER_SETTING_TABLE");
       }

       try {
          db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD PRINTER_SHAPE  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD SHORT_INVOICE  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString() + "SHORT_INVOICE");
       }
       try {
          db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD DONT_PRINT_HEADER  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString() + "DONT_PRINT_HEADER");
       }

       try {
          db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD TAYE_LAYOUT  INTEGER NOT NULL DEFAULT '0'");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString() + "DONT_PRINT_HEADER");
       }
       //**************************************************************************************


       try {
          String CREATE_TABLE_QTY_OFFERS = "CREATE TABLE  IF NOT EXISTS " + QTY_OFFERS + "("
                  + QTY + " REAL,"
                  + DISCOUNT_VALUE + " REAL,"
                  + PAYMENT_TYPE + " INTEGER" + ")";
          db.execSQL(CREATE_TABLE_QTY_OFFERS);
       } catch (Exception e) {
          Log.e("onUpgrade*****", "duplicated column QTY_OFFERS");
       }


       try {
          String CREATE_TABLE_ITEMS_QTY_OFFER = "CREATE TABLE IF NOT EXISTS " + ITEMS_QTY_OFFER + "("
                  + ITEMNAME + " TEXT,"
                  + ITEMNO + " INTEGER,"
                  + AMOUNT_QTY + " INTEGER,"
                  + FROMDATE + " TEXT,"
                  + TODATE + " TEXT,"
                  + DISCOUNT + " REAL" + ")";
          db.execSQL(CREATE_TABLE_ITEMS_QTY_OFFER);
       } catch (Exception e) {
          Log.e("onUpgrade*****", "duplicated column ItemsQtyOffer");
       }
       try {
          String CREATE_TABLE_CUSTOMER_LOCATION = "CREATE TABLE " + CUSTOMER_LOCATION + "("
                  + CUS_NO + " TEXT,"
                  + LONG + " TEXT,"
                  + LATIT + " TEXT" +

                  ")";
          db.execSQL(CREATE_TABLE_CUSTOMER_LOCATION);
       } catch (Exception e) {
          Log.e("onUpgrade*****", "CREATE_TABLE_CUSTOMER_LOCATION");
       }

       try {
          db.execSQL("ALTER TABLE COMPANY_INFO ADD NOTE  TEXT NOT NULL DEFAULT ''");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {


          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  SERIAL_CODE  INTEGER  DEFAULT 0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  VOUCH_DATE  TEXT NOT NULL DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  WHICH_UNIT  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  WHICH_UNIT_STR  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  WHICHU_QTY  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  ENTER_QTY  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  ENTER_PRICE  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  UNIT_BARCODE  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       //*****************************************End SALES_VOUCHER_DETAILS upgerade********************************************************


       try {

          db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD  IS_POST  INTEGER  DEFAULT 0 ");

       } catch (Exception e) {
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
          String CREATE_SERIAL_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " + SERIAL_ITEMS_TABLE + "("
                  + KEY_SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                  + SERIAL_CODE_NO + " TEXT,"
                  + COUNTER_SERIAL + " INTEGER,"
                  + VOUCHER_NO + " INTEGER,"
                  + ITEMNO_SERIAL + " TEXT,"

                  + KIND_VOUCHER + " TEXT,"

                  + DATE_VOUCHER + " TEXT,"
                  + STORE_NO_SALESMAN + " INTEGER,"
                  + IS_POSTED_SERIAL + " INTEGER," +
                  IS_BONUS_SERIAL + " INTEGER," +
                  Price_ITEM + " real, "
                  + Price_ITEM_Sales + " real, "
                  + IS_RETURNED + " INTEGER " +

                  ")";
          db.execSQL(CREATE_SERIAL_ITEMS_TABLE);
       } catch (Exception e) {

          Log.e("SERIAL_ITEMS_TABLE", "" + e.getMessage());
       }
       try {
          db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  STORE_NO_SALESMAN  INTEGER  DEFAULT 1 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  IS_POSTED_SERIAL  INTEGER  DEFAULT 0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  IS_BONUS_SERIAL  INTEGER  DEFAULT 0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  Price_ITEM_Sales  REAL  DEFAULT 0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SERIAL_ITEMS_TABLE ADD  IS_RETURNED  INTEGER  DEFAULT 0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       //********************************************* End SERIAL_ITEMS_TABLE ****************************************************
       try {
          db.execSQL("ALTER TABLE Items_Master ADD  ITEM_HAS_SERIAL  INTEGER  DEFAULT 0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {

          db.execSQL("ALTER TABLE Items_Master ADD  ITEM_PHOTO  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       //******************************** Added Customer ****************************************
       try {


          db.execSQL("ALTER TABLE ADDED_CUSTOMER ADD  ADRESS_CUSTOMER  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE ADDED_CUSTOMER ADD  TELEPHONE  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE ADDED_CUSTOMER ADD  CONTACT_PERSON  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       //
//*********************************** TABLE LOGIN *************************************
       //+ KEY_LOGIN + " INTEGER PRIMARY KEY AUTOINCREMENT
       String CREATE_SALESMAN_LOGIN_TABLE = " CREATE TABLE IF NOT EXISTS " + SALESMAN_LOGIN_LOGHistory + "("
               + DATE_LOGIN + " TEXT,"
               + TIME_LOGIN + " TEXT,"
               + TIME_LOGOUT + " TEXT,"
               + LONGTUDE2 + " REAL,"
               + LATITUDE2 + " REAL,"
               + SALESMAN_NO + " TEXT,"
               + IS_POSTED_LOGIN + " INTEGER,"
               + "  PRIMARY KEY ( DATE_LOGIN)" +

               ")";
       db.execSQL(CREATE_SALESMAN_LOGIN_TABLE);
       //****************************************************************************
       try {

          db.execSQL("ALTER TABLE COMPANY_INFO ADD  LONGTUDE_COMPANY  REAL  DEFAULT 0.0 ");
          db.execSQL("ALTER TABLE COMPANY_INFO ADD  LATITUDE_COMPANY  REAL  DEFAULT 0.0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       //*****************************************************************************
       try {

          db.execSQL("ALTER TABLE CustomerPrices ADD  DISCOUNT_CUSTOMER  REAL  DEFAULT 0.0 ");


       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {

          db.execSQL("ALTER TABLE CustomerPrices ADD  ItemNo_  TEXT  DEFAULT '' ");


       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {

          db.execSQL("ALTER TABLE CustomerPrices ADD  Other_Discount  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE CustomerPrices ADD  FromDate  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE CustomerPrices ADD  ToDate  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE CustomerPrices ADD  ListNo  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE CustomerPrices ADD  ListType  TEXT  DEFAULT '' ");


       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {

          db.execSQL("ALTER TABLE TRANSACTIONS ADD  REAL_LONGTUD  TEXT  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {

          db.execSQL("ALTER TABLE TRANSACTIONS ADD  REAL_LATITUDE  TEXT  DEFAULT '0' ");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {

          String CREATE_Item_Switch_TABLE = "CREATE TABLE IF NOT EXISTS " + Item_Switch + "("
                  + ITEM_NAMEA + " TEXT,"
                  + ITEM_OCODE + " TEXT,"
                  + ITEM_NCODE + " TEXT"

                  + ")";
          db.execSQL(CREATE_Item_Switch_TABLE);
       } catch (Exception e) {
       }
       //-------------------------------------------------------------------------

       try {

          String CREATE_SERIAL_TABLE = "CREATE TABLE IF NOT EXISTS " + SerialItemMaster + "("

                  + StoreNo + " TEXT,"
                  + ITEM_OCODE_M + " TEXT,"
                  + SerialCode + " TEXT,"
                  + Qty_serial + " TEXT"

                  +
                  ")";
          db.execSQL(CREATE_SERIAL_TABLE);
       } catch (Exception e) {
       }
       //***************************************************************
       try {


          String CREATE_OfferListMaster_TABLE = "CREATE TABLE IF NOT EXISTS " + price_offer_list_master + "("

                  + PO_LIST_NO + " INTEGER,"
                  + PO_LIST_NAME + " TEXT,"
                  + PO_LIST_TYPE + " INTEGER,"
                  + FROM_DATE_master + " TEXT,"
                  + TO_DATE_master + " TEXT"
                  +


                  ")";
          db.execSQL(CREATE_OfferListMaster_TABLE);
       } catch (Exception e) {
       }
       try {

          db.execSQL("ALTER TABLE REQUEST_DETAILS ADD  CURRENT_QTY  REAL  DEFAULT '0' ");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {

          db.execSQL("ALTER TABLE REQUEST_DETAILS ADD  isPostedDetails  INTEGER  DEFAULT 0 ");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       String CREATE_SERIAL_ITEMS_TABLE_backup = "CREATE TABLE IF NOT EXISTS " + SERIAL_ITEMS_TABLE_backup + "("
               + KEY_SERIAL2 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
               + SERIAL_CODE_NO2 + " TEXT,"
               + COUNTER_SERIAL2 + " INTEGER,"
               + VOUCHER_NO2 + " INTEGER,"
               + ITEMNO_SERIAL2 + " TEXT,"
               + KIND_VOUCHER2 + " TEXT,"

               + DATE_VOUCHER2 + " TEXT,"
               + STORE_NO_SALESMAN2 + " INTEGER,"
               + IS_POSTED_SERIAL2 + " INTEGER,"
               + IS_BONUS_SERIAL2 + " INTEGER,"
               + isItemDelete + " INTEGER,"

               + dateDelete + " TEXT" +

               ")";
       db.execSQL(CREATE_SERIAL_ITEMS_TABLE_backup);


       try {

          String CREATE_TABLE_SALESMEN_LOG_IN = "CREATE TABLE IF NOT EXISTS " + SalesMenLogIn + "( "
                  + UserNo_LogIn + " TEXT" + ")";
          db.execSQL(CREATE_TABLE_SALESMEN_LOG_IN);
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {

          db.execSQL("ALTER TABLE Sales_Team ADD  IP_ADDRESS_DEVICE  TEXT  DEFAULT '' ");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_MASTER ADD  VOUCHER_time  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE  COMPANY_INFO ADD   NOTEPOSITION  TEXT  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          String CREATE_TABLE_INVENTORY_SHELF = "CREATE TABLE IF NOT EXISTS " + INVENTORY_SHELF + "("
                  + TRANS_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                  + ITEM_NO + " TEXT,"
                  + SERIAL_NO + " TEXT,"
                  + QTY_ITEM + " INTEGER,"
                  + TRANS_DATE + " TEXT,"
                  + CUSTOMER_NO + " TEXT,"
                  + SALESMAN_NUMBER + " TEXT,"

                  + VOUCHER_NUMBER_INVENTORY + " INTEGER,"
                  + IsPosted + " INTEGER "

                  + ")";
          db.execSQL(CREATE_TABLE_INVENTORY_SHELF);

       } catch (Exception e) {
       }
       try {
          String CREATE_TABLE_VoucherSerialize = "CREATE TABLE IF NOT EXISTS " + VoucherSerialize + "("
                  + VoucherSales_no + " INTEGER,"
                  + VoucherReturn_no + " INTEGER,"
                  + VoucherNewOrder_no + " INTEGER"
                  + ")";
          db.execSQL(CREATE_TABLE_VoucherSerialize);

       } catch (Exception e) {
       }

       try {
          db.execSQL("ALTER TABLE  INVENTORY_SHELF ADD   VOUCHER_NUMBER_INVENTORY  INTEGER  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE  INVENTORY_SHELF ADD   IsPosted  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE  SERIAL_ITEMS_TABLE ADD   Price_ITEM  real  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE  VS_PROMOTION ADD   DISCOUNT_ITEM_TYPE  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE  Item_Unit_Details ADD   PriceUnit  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE  Item_Unit_Details ADD   ItemBarcode  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {


          db.execSQL("ALTER TABLE  Item_Unit_Details ADD   PRICECLASS_1  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE  Item_Unit_Details ADD   PRICECLASS_2  TEXT  DEFAULT '' ");
          db.execSQL("ALTER TABLE  Item_Unit_Details ADD   PRICECLASS_3  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       //************************************************************************************************
       try {
          String CREATE_TABLE_CustomerPrices2 = "CREATE TABLE IF NOT EXISTS " + CustomerPricesCurrent + "("
                  + ItemNumber_ + " INTEGER,"
                  + CustomerNumber_ + " INTEGER,"
                  + Price_ + " INTEGER,"
                  + DISCOUNT_CUSTOMER_ + " real,"
                  + ItemNo_N + " TEXT,"
                  + Other_Discount_ + " TEXT,"
                  + FromDate_ + " TEXT,"
                  + ToDate_ + " TEXT,"
                  + ListNo_ + " TEXT,"
                  + ListType_ + " TEXT"

                  + ")";
          db.execSQL(CREATE_TABLE_CustomerPrices2);


       } catch (Exception e) {

       }
       try {
          String CREATE_TABLE_GroupOffer_Item = "CREATE TABLE IF NOT EXISTS " + GroupOffer_Item + "("
                  + Id_serial + " INTEGER,"
                  + ItemNo_Offer + " TEXT,"
                  + ItemName_Offer + " TEXT,"
                  + From_Date_Offer + " TEXT,"

                  + To_Date_Offer + " TEXT,"

                  + Discount_Offer + " TEXT,"

                  + Discount_Type_Offer + " INTEGER,"
                  + GroupId_Offer + " INTEGER,"

                  + Qty_item + " TEXT "


                  + ")";
          db.execSQL(CREATE_TABLE_GroupOffer_Item);

       } catch (Exception e) {


       }

       try {
          db.execSQL("ALTER TABLE  PAYMENTS ADD   CUSTOMER_NUMBER_STR  TEXT  DEFAULT '' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       ////B
       try {
          String CREATE_TABLE_FlAG_SETTINGS = "CREATE TABLE IF NOT EXISTS " + Flag_Settings + "("
                  + Data_Type + " TEXT,"
                  + Export_Stock + " INTEGER,"
                  + Max_Voucher + " INTEGER,"
                  + Make_Order + " INTEGER,"
                  + Admin_Password + " INTEGER,"
                  + Total_Balance + " INTEGER,"
                  + Voucher_Return + " INTEGER"

                  + ")";
          db.execSQL(CREATE_TABLE_FlAG_SETTINGS);


       } catch (Exception e) {
          Log.e(TAG, e.getMessage() + "");
       }
       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  IS_RETURNED  INTEGER  DEFAULT 0 ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  Avilable_Qty  Text  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_MASTER ADD  ORIGINALVOUCHER_NUMBER  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SALES_VOUCHER_DETAILS ADD  ORIGINALVOUCHER_NUMBER  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          String CREATE_TABLE_SalesMan_Plan = "CREATE TABLE  IF NOT EXISTS " + SalesMan_Plan + "("
                  + DATE + " TEXT,"
                  + SALES_MAN_NUMBER + " INTEGER,"
                  + CUSTOMER_NAME + " TEXT,"
                  + CUSTOMER_NO + " INTEGER,"
                  + LATITUDE + " TEXT,"
                  + LONGITUDE + " TEXT,"
                  + orederd + " INTEGER,"
                  + typeorederd + " INTEGER,"
                  + LogoutStatus + " INTEGER DEFAULT '0'" +
                  ")";
          db.execSQL(CREATE_TABLE_SalesMan_Plan);
       } catch (Exception e) {

       }


       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + ActiveSlasmanPlan + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + POS_ACTIVE + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + CakeShop_Offer + "' INTEGER DEFAULT '0'");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + Talaat_Offer + "' INTEGER DEFAULT '0'");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + Qasion_Offer + "' INTEGER DEFAULT '0'");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SalesMenLogIn ADD '" + LATITUDE + "'  Text");
          db.execSQL("ALTER TABLE SalesMenLogIn ADD '" + LONGITUDE + "'  Text");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          String SalesMan_Item_availability_TABLE = "CREATE TABLE IF NOT EXISTS " + SalesMan_Item_availability + "("
                  + SALESMAN_NO + " INTEGER,"
                  + ITEM_OCODE + " TEXT,"
                  + availability + " INTEGER"

                  + ")";
          db.execSQL(SalesMan_Item_availability_TABLE);

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {

          String MODIFY1 = "ALTER TABLE COMPANY_INFO ADD COLUMN phonnum TEXT";
          String MODIFY2 = "UPDATE COMPANY_INFO SET phonnum = CAST(COMPANY_TEL as TEXT)";
          db.execSQL(MODIFY1);
          db.execSQL(MODIFY2);

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD '" + netsalFLAG + "' TEXT DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + CAN_CHANGE_PRICE_RETURNONLY + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       //////-------------------------
       String CREATE_SalemanTrips_TABLE = "CREATE TABLE IF NOT EXISTS " + SalemanTrips + "("
               + TripNum + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
               + TripstartDate + " TEXT,"
               + TripstartTime + " TEXT,"
               + TripEndDate + " TEXT,"
               + TripEndTime + " TEXT,"
               + TripStatus + " TEXT"
               + ")";
       db.execSQL(CREATE_SalemanTrips_TABLE);

       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + ActiveSlasmanTrips + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
/////////
       try {
          db.execSQL("ALTER TABLE SalesMen ADD UserType INTEGER");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }


       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + Max_VoucherSever + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          Log.e("onUpgrade*****", "OffersJustForSales");
          db.execSQL("ALTER TABLE SETTING ADD '" + OffersJustForSales + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          Log.e("onUpgrade*****", "CheckQtyinOrder");
          db.execSQL("ALTER TABLE SETTING ADD '" + CheckQtyinOrder + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          Log.e("onUpgrade*****", "HeaderprintInOrders");
          db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD '" + HeaderprintInOrders + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + locationtracker + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + Purchase_Order + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + AqapaTax + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + NO_TAX + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + SHOW_CUSTOMER_LOCATION + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE  COMPANY_INFO ADD   NATIONAL_ID  TEXT  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE  COMPANY_INFO ADD   CAR_NO  TEXT  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       String CREATE_STransactionInfo_TABLE = "CREATE TABLE IF NOT EXISTS " + TransactionInfo + "("
               + " Serial INTEGER PRIMARY KEY AUTOINCREMENT,"
               + Reson + " TEXT,"
               + PersonName + " TEXT,"
               + PhoneNum + " TEXT,"
               + Cust_num + " TEXT,"
               + Cust_name + " TEXT,"
               + DATE_ + " TEXT"
               + ")";
       db.execSQL(CREATE_STransactionInfo_TABLE);


       try {
          db.execSQL("ALTER TABLE  SALES_VOUCHER_MASTER ADD   TAX_TYPE  INTEGER DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + Items_Units + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + EndTripReport + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
///-------------------------------
       String CREATE_AdminPasswords_TABLE = "CREATE TABLE IF NOT EXISTS " + AdminPasswords+ "("
               + password_type + " TEXT,"
               + password_no + " TEXT"

               + ")";
       db.execSQL(CREATE_AdminPasswords_TABLE);
       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + Nasleh_Offer + "' INTEGER DEFAULT '0'");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + AcountatatmentVis + "'  INTEGER DEFAULT '1'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SalesMenLogIn ADD '" + LATITUDE + "'  Text");
          db.execSQL("ALTER TABLE SalesMenLogIn ADD '" + LONGITUDE + "'  Text");
       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SalesMenLogIn ADD '" + VERSION_APK + "'  Text");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + SharWhatsApp_Form + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + DISC_AFTER_TAX + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + Add_CustumerPer + "'  INTEGER DEFAULT '1'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
       try {
          db.execSQL("ALTER TABLE Flag_Settings ADD '" + exportedVochTax + "'  INTEGER  DEFAULT '0' ");

       } catch (Exception e) {
          Log.e(TAG, e.getMessage().toString());
       }

       try {
          db.execSQL("ALTER TABLE SETTING ADD '" + LastCustPrice + "'  INTEGER DEFAULT '0'");

       } catch (Exception e) {

          Log.e(TAG, e.getMessage().toString());
       }
        try {
            db.execSQL("ALTER TABLE SETTING ADD '" + ReturnVoch_PERM + "'  INTEGER DEFAULT '0'");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("ALTER TABLE SETTING ADD '" + plansetting_KIND + "'  INTEGER DEFAULT '0'");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("ALTER TABLE TRANSACTIONS ADD '" + VOUCHERCOUNT + "'  INTEGER DEFAULT '0'");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }

        try {
            db.execSQL("ALTER TABLE SETTING ADD '" + CompanyinfoINPdf + "'  INTEGER DEFAULT '0'");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("ALTER TABLE Flag_Settings ADD '" + MaxPaymentVoucherSever + "'  INTEGER  DEFAULT '0' ");

        } catch (Exception e) {
            Log.e(TAG, e.getMessage().toString());
        }
        try {
        String CREATE_PaymentSerials_TABLE = "CREATE TABLE IF NOT EXISTS " + PaymentSerials+ "("
                + PaymentSerials_cash + " TEXT,"
                + PaymentSerials_cridt + " TEXT"

                + ")";
        db.execSQL(CREATE_PaymentSerials_TABLE);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("ALTER TABLE SETTING ADD '" + AddedCustomer_PERM + "'  INTEGER DEFAULT '0'");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD '" + MAXD + "'  TEXT DEFAULT '0'");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }


        String CREATE_VisitTABLE = "CREATE TABLE IF NOT EXISTS " + VisitMedicalTable + "("
                + " VoucherNo INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DateVisit + " TEXT,"
                + CustNo + " TEXT,"
                + CustName + " TEXT,"
                + Adoption + " TEXT,"
                + Tool + " TEXT,"
                + DoubleVisit + " TEXT,"
                + RemarkVisit + " TEXT,"
                + HaveItem + " TEXT"
                + ")";
        db.execSQL(CREATE_VisitTABLE );

        String CREATE_ItemsVisitTABLE = "CREATE TABLE IF NOT EXISTS " + ItemsVisit + "("

                + VoucherNoItem + " TEXT,"
                + CustNoItem + " TEXT,"
                + ItemName + " TEXT,"
                + QtyItem + " TEXT"

                + ")";
        db.execSQL(CREATE_ItemsVisitTABLE );
        String CREATE_NetworkTABLE = "CREATE TABLE IF NOT EXISTS " + NetworkTableLog + "("
                + TransNo + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LinkRequest + " TEXT,"
                + DateTrand + " TEXT,"
                + TimeTrans + " TEXT,"
                + CustomerNo + " TEXT,"
                + ResponseLink + " TEXT"
                + ")";
        db.execSQL(CREATE_NetworkTABLE );
        try {
            db.execSQL("ALTER TABLE NetworkTableLog ADD '" +  NoteExport + "'  TEXT DEFAULT ''");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD PrintItemNumber  INTEGER NOT NULL DEFAULT '0'");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage().toString() + "DONT_PRINT_HEADER");
        }
        try {

            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD '" + FAX + "' TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD '" + ZipCode + "' TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD '" + EMail + "' TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE CUSTOMER_MASTER ADD '" + C_THECATEG + "' TEXT DEFAULT ''");

            Log.e("CREATE_TABLE_CUSTOMER_MASTER","trueee");
        } catch (Exception e) {

            Log.e(TAG, e.getMessage().toString());
        }
        try {
            db.execSQL("ALTER TABLE PRINTER_SETTING_TABLE ADD CurrencyValue  TEXT NOT NULL DEFAULT 'JD'");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage().toString() + "PRINTER_SETTING_TABLE");
        }


    }

    ////B
    public void insertFlagSettings(com.dr7.salesmanmanager.Modles.Flag_Settings flag_settings) {

        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(Data_Type, flag_settings.getData_Type());
        values.put(Export_Stock, flag_settings.getExport_Stock());
        values.put(Max_Voucher, flag_settings.getMax_Voucher());
        values.put(Make_Order, flag_settings.getMake_Order());
        values.put(Admin_Password, flag_settings.getAdmin_Password());
        values.put(Total_Balance, flag_settings.getTotal_Balance());
        values.put(Voucher_Return, flag_settings.getVoucher_Return());
        values.put(ActiveSlasmanPlan, flag_settings.getActiveSlasmanPlan());
        values.put(POS_ACTIVE, flag_settings.getPos_active());
        values.put(CakeShop_Offer, flag_settings.getOfferCakeShop());
        values.put(Talaat_Offer, flag_settings.getOfferTalaat());
        values.put(Qasion_Offer, flag_settings.getOfferQasion());
        values.put(ActiveSlasmanTrips, flag_settings.getActiveSlasmanTrips());
        values.put(Max_VoucherSever, flag_settings.getMaxvochServer());
        values.put(Purchase_Order,flag_settings.getPurchaseOrder());
       values.put(NO_TAX,flag_settings.getNoTax());
       values.put(exportedVochTax,flag_settings.getExportedVoch_Tax());

       values.put(Nasleh_Offer,flag_settings.getOffernasleh());
        values.put(MaxPaymentVoucherSever,flag_settings.getPaymentvochServer());

        db.insert(Flag_Settings, null, values);
        db.close();

    }
    public void insertAdminPasswords(com.dr7.salesmanmanager.Modles.Password  password) {

       db = this.getReadableDatabase();
       ContentValues values = new ContentValues();

       values.put(password_type, password.getPassword_type());
       values.put(password_no, password.getPassword_no());


       db.insert(AdminPasswords, null, values);
       db.close();

    }
     public void insertPaymentSerials(int type,String SERIAL) {
         Log.e("insertPaymentSerials","insertPaymentSerials");
         db = this.getReadableDatabase();
         ContentValues values = new ContentValues();
if(type==1)
         values.put(PaymentSerials_cash, SERIAL);
      else
         values.put(PaymentSerials_cridt, SERIAL);


         db.insert(PaymentSerials, null, values);
         db.close();

     }
     public void UpdatePaymentSerials(int type,String SERIAL) {
         Log.e("UpdatePaymentSerials","UpdatePaymentSerials");
         db = this.getWritableDatabase();
         ContentValues values = new ContentValues();

         if(type==1)
             values.put(PaymentSerials_cash, SERIAL);
         else
             values.put(PaymentSerials_cridt, SERIAL);

         // updating row
         db.update(PaymentSerials, values,null, null);

     }
     public int getPaymentSerialsCount() {


         String selectQuery = "SELECT * FROM " + PaymentSerials;
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

Log.e("getPaymentSerials",cursor.getCount()+"");
         return cursor.getCount();

     }
     public long getPaymentVochNo(int type) {
         String selectQuery="";
     long    VochNo=0;
         if(type==1)
          selectQuery = "SELECT PaymentSerials_cash FROM " + PaymentSerials ;
else   selectQuery = "SELECT PaymentSerials_cridt FROM " + PaymentSerials ;
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);


         if (cursor.moveToFirst()) {
//            Log.i("DatabaseHandler", "************************" + selectQuery);
             do {

                 VochNo = cursor.getLong(0);


             } while (cursor.moveToNext());
         }


         return VochNo;

     }

    public void DeleteAdminPasswords(){
       SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("delete from " + AdminPasswords);
       db.close();
    }
    public List<Flag_Settings> getFlagSettings() {

        List<Flag_Settings> flagSettings = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Flag_Settings;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Flag_Settings mySettings = new Flag_Settings(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6),
                        cursor.getInt(7),
                        cursor.getInt(8),
                        cursor.getInt(9),
                        cursor.getInt(10),
                        cursor.getInt(11),
                        cursor.getInt(12),
                        cursor.getInt(13),
                        cursor.getInt(14),
                        cursor.getInt(15),
                        cursor.getInt(16),
                        cursor.getInt(17),
                        cursor.getInt(18)
                );

                flagSettings.add(mySettings);

            } while (cursor.moveToNext());
            cursor.close();
            Log.e("getFlagSettings", "" + flagSettings.size());
        }
        return flagSettings;

    }
    public  List<Password> getAdminPasswords() {

       List<Password> passwords = new ArrayList<>();
       String selectQuery = "SELECT * FROM " + AdminPasswords;
       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       if (cursor.moveToFirst()) {
          do {
             Password password = new Password(
                   cursor.getInt(0),
                     cursor.getString(1)


             );

             passwords.add(password);

          } while (cursor.moveToNext());
          cursor.close();
          Log.e("getAdminPasswords", "" + passwords.size());
       }
       return passwords;

    }
    public void updateFlagSettings (String dataType, int export, int max, int order,
                                    int password, int total, int vReturn,int SalPlan,int pos,
                                    int csOffer, int tOffer, int qOffer,int SalTrip,int mavVoServer,int purchOrder
    ,int no_tax,int offernasleh,int exportedVoch_Tax,int MaxpaymentvochFromServ) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Data_Type, dataType);
        values.put(Export_Stock, export);
        values.put(Max_Voucher, max);
        values.put(Make_Order, order);
        values.put(Admin_Password, password);
        values.put(Total_Balance, total);
        values.put(Voucher_Return, vReturn);
        values.put(ActiveSlasmanPlan, SalPlan);
        values.put(POS_ACTIVE, pos);
        values.put(CakeShop_Offer, csOffer);
        values.put(Talaat_Offer, tOffer);
        values.put(Qasion_Offer, qOffer);
        values.put(ActiveSlasmanTrips, SalTrip);
        values.put(Max_VoucherSever, mavVoServer);
        values.put( Purchase_Order,purchOrder);
       values.put( NO_TAX,no_tax);
       values.put( exportedVochTax,exportedVoch_Tax);

       values.put( Nasleh_Offer,offernasleh);
        values.put( MaxPaymentVoucherSever,MaxpaymentvochFromServ);

        db.update(Flag_Settings, values, null, null);

        Log.e("Flag Settings", "UPDATE");
        db.close();

    }


    public void add_GroupOffer(OfferGroupModel offerGroup) {
        try {

            db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(Id_serial, offerGroup.id_serial.trim());
            values.put(ItemNo_Offer, offerGroup.ItemNo);
            values.put(ItemName_Offer, offerGroup.Name);
            values.put(From_Date_Offer, offerGroup.fromDate);
            values.put(To_Date_Offer, offerGroup.toDate);
            values.put(Discount_Offer, offerGroup.discount);
            values.put(Discount_Type_Offer, offerGroup.discountType);
            values.put(GroupId_Offer, offerGroup.groupIdOffer);
            values.put(Qty_item, offerGroup.qtyItem);

            db.insert( GroupOffer_Item, null, values);
            Log.e("GroupOffer_Item","**=="+offerGroup.Name);
            db.close();
        }
        catch (Exception e){
            Log.e("GroupOffer_Item",""+e.getMessage());

        }

    }
    public void add_inventoryShelf(InventoryShelf inventoryShelf)
    {
        try {
            db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(ITEM_NO, inventoryShelf.getITEM_NO().trim());
            values.put(SERIAL_NO, inventoryShelf.getSERIAL_NO());
            values.put(QTY_ITEM, inventoryShelf.getQTY_ITEM());
            values.put(TRANS_DATE, inventoryShelf.getTRANS_DATE());
            values.put(CUSTOMER_NO, inventoryShelf.getCUSTOMER_NO());
            values.put(SALESMAN_NUMBER, inventoryShelf.getSALESMAN_NUMBER());
            values.put(VOUCHER_NUMBER_INVENTORY, inventoryShelf.getVoucherNo());
            values.put(IsPosted, 0);
            db.insert(INVENTORY_SHELF, null, values);
            Log.e("add_Serial",""+inventoryShelf.getSERIAL_NO());
            db.close();
        }
        catch (Exception e){
            Log.e("add_inventoryShelf",""+e.getMessage());

        }

    }


    public void addSerialVoucherNo( long saleVoucher,long retVoucher,long order) {
        long curentMaxVoucher=getMaxFromVoucherMaster(504);
//        Log.e("getMaxSerialNumber", "FromSetting" + curentMaxVoucher+"\t"+saleVoucher);
        if (curentMaxVoucher > saleVoucher&&rawahneh_getMaxVouchFromServer==0) {

           try {
              deleteFromVoucherSerialize();
              db = this.getReadableDatabase();
              ContentValues values = new ContentValues();
              values.put(VoucherSales_no, curentMaxVoucher);

              db.insert(VoucherSerialize, null, values);
//              Log.e("VoucherSerialize", "VoucherSerialize");
              db.close();
           } catch (Exception e) {
              Log.e("VoucherSerialize", "" + e.getMessage());

           }

        } else {


            try {
                deleteFromVoucherSerialize();
                db = this.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(VoucherSales_no, saleVoucher);
                values.put(VoucherReturn_no, retVoucher);
                values.put(VoucherNewOrder_no, order);

                db.insert(VoucherSerialize, null, values);
                Log.e("VoucherSerialize", "VoucherSerialize");
                db.close();
            } catch (Exception e) {
                Log.e("VoucherSerialize", "" + e.getMessage());

            }
        }

    }

    private void deleteFromVoucherSerialize() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + VoucherSerialize);
        db.close();
    }

    public void updateVoucherNo( long saleVoucher,int type,int flag)
    {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        if(flag==0)//update return type from import
        {
            long curentMaxVoucher=getMaxFromVoucherMaster(type);
            Log.e("getMaxSerialNumber", "updateVoucherNo" + curentMaxVoucher+"\t"+saleVoucher);
            if (curentMaxVoucher > saleVoucher&&rawahneh_getMaxVouchFromServer==0) {// dont change
            } else {
                if(type==506)
                {
                    try {
                        values.put(VoucherReturn_no,saleVoucher);
                        db.update(VoucherSerialize, values, null, null);

                        Log.e("VoucherSerialize","VoucherSerialize");
                        db.close();
                    }catch (Exception e){
                        Log.e("VoucherSerialize","Exception=VoucherReturn_no"+e.getMessage());
                    }

                }
                else {
                    if(type==508)
                    {
                        try {
                            values.put(VoucherNewOrder_no,saleVoucher);
                            db.update(VoucherSerialize, values, null, null);

                            Log.e("VoucherSerialize","VoucherNewOrder_no"+saleVoucher);
                            db.close();
                        }catch (Exception e){
                            Log.e("VoucherSerialize","Exception=VoucherReturn_no"+e.getMessage());
                        }

                    }
                }

            }
        }else {
            try {
                Log.e("updateVoucherNo",""+saleVoucher+"\t="+type);


                if(type==504)
                    values.put(VoucherSales_no, saleVoucher);
                if(type==506)
                    values.put(VoucherReturn_no,saleVoucher);
                if(type==508)
                    values.put(VoucherNewOrder_no,saleVoucher);

                db.update(VoucherSerialize, values, null, null);

                Log.e("VoucherSerialize","VoucherSerialize");
                db.close();
            }
            catch (Exception e){
                Log.e("VoucherSerialize",""+e.getMessage());

            }
        }



    }


    public void addItemSwitch(List<ItemSwitch> itemSwitch)
    {
        db = this.getReadableDatabase();
        db.beginTransaction();
        Log.e("addItemSwitch", "" + itemSwitch.size());

        for (int i = 0; i < itemSwitch.size(); i++) {
            try {
                db = this.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put(ITEM_NAMEA, itemSwitch.get(i).getItem_NAMEA());
                values.put(ITEM_OCODE, itemSwitch.get(i).getItem_OCODE());
                values.put(ITEM_NCODE, itemSwitch.get(i).getItem_NCODE());
                db.insertWithOnConflict(Item_Switch, null, values, SQLiteDatabase.CONFLICT_REPLACE);



            } catch (Exception e) {
                Log.e("DBAccount_Report", "" + e.getMessage());

            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

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
            values.put(SERIAL_CODE_NO, serialModelItem.getSerialCode().trim());
            values.put(COUNTER_SERIAL, serialModelItem.getCounterSerial());
            values.put(VOUCHER_NO, serialModelItem.getVoucherNo());
            values.put(ITEMNO_SERIAL, serialModelItem.getItemNo());
            values.put(DATE_VOUCHER, serialModelItem.getDateVoucher());
            values.put(KIND_VOUCHER, serialModelItem.getKindVoucher());
            values.put(STORE_NO_SALESMAN, serialModelItem.getStoreNo());
            values.put(IS_POSTED_SERIAL, "0");
            values.put(IS_BONUS_SERIAL, serialModelItem.getIsBonus());
            values.put(Price_ITEM, serialModelItem.getPriceItem());
            values.put(Price_ITEM_Sales, serialModelItem.getPriceItemSales());
            if(serialModelItem.getKindVoucher().equals("504")){
                values.put(IS_RETURNED,0);
            }else {
                values.put(IS_RETURNED,1);
            }
            db.insert(SERIAL_ITEMS_TABLE, null, values);
            Log.e("add_Serial",""+serialModelItem.getSerialCode());
            db.close();
        }
        catch (Exception e){
            Log.e("Dbhandler_addItemQOf",""+e.getMessage());

        }

    }
    public void add_SerialBackup(serialModel serialModelItem,int flag)
    {
        try {

            Log.e("listMasterSerialFor","add_SerialBackup="+serialModelItem.getSerialCode());

            db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(SERIAL_CODE_NO2, serialModelItem.getSerialCode().trim());
            values.put(COUNTER_SERIAL2, serialModelItem.getCounterSerial());
            values.put(VOUCHER_NO2, serialModelItem.getVoucherNo());
            values.put(ITEMNO_SERIAL2, serialModelItem.getItemNo());
            values.put(DATE_VOUCHER2, serialModelItem.getDateVoucher());
            values.put(KIND_VOUCHER2, serialModelItem.getKindVoucher());
            values.put(STORE_NO_SALESMAN2, serialModelItem.getStoreNo());
            values.put(IS_POSTED_SERIAL2, "0");
            values.put(IS_BONUS_SERIAL2, serialModelItem.getIsBonus());
            if(flag==1)
            {
                values.put(isItemDelete, "1");
            }
            else {
                values.put(isItemDelete, serialModelItem.getIsDeleted());
            }

            values.put(dateDelete, serialModelItem.getDateDelete());



            db.insert(SERIAL_ITEMS_TABLE_backup, null, values);
            Log.e("add_Serial",""+serialModelItem.getSerialCode());
            db.close();
        }
        catch (Exception e){
            Log.e("Dbhandler_addItemQOf",""+e.getMessage());

        }

    }

    public void add_SerialMasteItems(List<serialModel> serialModelItem)
    {

        try {
            db = this.getReadableDatabase();
            db.beginTransaction();
              Log.e("serialModelItem===",serialModelItem.size()+"");
            for (int i = 0; i < serialModelItem.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(StoreNo, serialModelItem.get(i).getStoreNo());

                values.put( ITEM_OCODE_M , serialModelItem.get(i).getItemNo().trim());

                values.put(SerialCode, serialModelItem.get(i).getSerialCode().trim());
                values.put(Qty_serial, serialModelItem.get(i).getQty());


                db.insertWithOnConflict(SerialItemMaster, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (Exception e){
            Log.e("Dbhandler_addItemQOf",""+e.getMessage());

        }

    }
    public void add_OfferListMaster(List<OfferListMaster> offerListMasters)
    {

        try {
            db = this.getReadableDatabase();
            db.beginTransaction();

            for (int i = 0; i < offerListMasters.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(PO_LIST_NO, offerListMasters.get(i).getPO_LIST_NO());

                values.put( PO_LIST_NAME , offerListMasters.get(i).getPO_LIST_NAME());

                values.put(PO_LIST_TYPE, offerListMasters.get(i).getPO_LIST_TYPE());
                values.put(FROM_DATE_master, offerListMasters.get(i).getFROM_DATE());
                values.put(TO_DATE_master, offerListMasters.get(i).getTO_DATE());


                db.insertWithOnConflict(price_offer_list_master, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
            db.setTransactionSuccessful();
            db.endTransaction();
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
        values.put(SHORT_INVOICE,printer.getShortInvoice());
        values.put(DONT_PRINT_HEADER,printer.getDontPrintHeader());
        values.put(TAYE_LAYOUT,printer.getTayeeLayout());
        values.put(netsalFLAG,printer.getNetsalflag());
        values.put(HeaderprintInOrders,printer.getDontrprintheadeInOrders());
        values.put(PrintItemNumber,printer.getPrintItemNumber());
        values.put(CurrencyValue,printer.getCurency());
        db.insert(PRINTER_SETTING_TABLE, null, values);

        db.close();

    }

    public void addCustomer(List<Customer> customer) {
        db = this.getReadableDatabase();
        db.beginTransaction();

        for (int i = 0; i < customer.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(COMPANY_NUMBER0, customer.get(i).getCompanyNumber());
            values.put(CUS_ID, customer.get(i).getCustId());
            values.put(CUS_NAME0, customer.get(i).getCustName());
            Log.e("CUS_NAME0","addCustomer"+customer.get(i).getCustName());
            values.put(ADDRESS, customer.get(i).getAddress());
            values.put(IS_SUSPENDED, customer.get(i).getIsSuspended());
            values.put(PRICE_LIST_ID, customer.get(i).getPriceListId());
            values.put(CASH_CREDIT, customer.get(i).getCashCredit());
            values.put(SALES_MAN_NO, customer.get(i).getSalesManNumber());
            values.put(CREDIT_LIMIT, customer.get(i).getCreditLimit());
            values.put(PAY_METHOD0, customer.get(i).getPayMethod());
            values.put(CUST_LAT, customer.get(i).getCustLat());
            values.put(CUST_LONG, customer.get(i).getCustLong());
            values.put(MAX_DISCOUNT, customer.get(i).getMax_discount());
            values.put(ACCPRC, customer.get(i).getACCPRC());
            values.put(HIDE_VAL, customer.get(i).getHide_val());
            values.put(IS_POST, 1);
            values.put(CUS_ID_Text, customer.get(i).getCustomerIdText());
            values.put(MAXD, customer.get(i).getMaxD());

            values.put(FAX       , customer.get(i).getFax());
            values.put(ZipCode   , customer.get(i).getZipCode());
            values.put(EMail     , customer.get(i).geteMail());
            values.put(C_THECATEG, customer.get(i).getC_THECATEG());

            db.insertWithOnConflict(CUSTOMER_MASTER, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public void addItem_Unit_Details(List<ItemUnitDetails> item) {
        db = this.getReadableDatabase();
        db.beginTransaction();

        for (int i = 0; i < item.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(ComapnyNo, item.get(i).getCompanyNo());
            values.put(ItemNo, item.get(i).getItemNo());
            values.put(UnitID, item.get(i).getUnitId());
            values.put(ConvRate, item.get(i).getConvRate());
            try {
                values.put(PriceUnit, item.get(i).getUnitPrice());
                values.put(ItemBarcode, item.get(i).getItemBarcode());

                values.put(PRICECLASS_1, item.get(i).getPriceClass_1());
                values.put(PRICECLASS_2, item.get(i).getPriceClass_2());
                values.put(PRICECLASS_3, item.get(i).getPriceClass_3());




            }catch (Exception e){

            }

//            values.put(UNIT_PRICE, "");
//            values.put(ItemBarcode, "");
            db.insertWithOnConflict(Item_Unit_Details, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        }
//        db.close();

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void addItemsMaster(List<ItemsMaster> item) {
        db = this.getReadableDatabase();
//        db.beginTransaction();
        db.beginTransaction();

        for (int i = 0; i < item.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(ComapnyNo1, item.get(i).getCompanyNo());
            values.put(ItemNo1, item.get(i).getItemNo());
            values.put(Name1, item.get(i).getName());
            values.put(CateogryID1, item.get(i).getCategoryId());
            values.put(Barcode1, item.get(i).getBarcode());
            values.put(IsSuspended1, item.get(i).getIsSuspended());
            values.put(ITEM_L1, item.get(i).getItemL());
            values.put(ITEM_F_D, item.get(i).getPosPrice());
            values.put(KIND_ITEM, item.get(i).getKind_item());

//        if(ITEM_HAS_SERIAL)
            values.put(ITEM_HAS_SERIAL, item.get(i).getItemHasSerial());
            values.put(ITEM_PHOTO, item.get(i).getPhotoItem());

            db.insertWithOnConflict(Items_Master, null, values, SQLiteDatabase.CONFLICT_REPLACE);

//            db.insert(, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
//        db.close();
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

    public void addPrice_List_D(List<PriceListD> price) {
        db = this.getReadableDatabase();
        db.beginTransaction();

        for (int i = 0; i < price.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(ComapnyNo2, price.get(i).getCompanyNo());
            values.put(PrNo2, price.get(i).getPrNo());
            values.put(ItemNo2, price.get(i).getItemNo());
            values.put(UnitID2, price.get(i).getUnitId());
            values.put(Price2, price.get(i).getPrice());
            values.put(TaxPerc2, price.get(i).getTaxPerc());
            values.put(MinSalePrice2, price.get(i).getMinSalePrice());
            db.insertWithOnConflict(Price_List_D, null, values, SQLiteDatabase.CONFLICT_REPLACE);

//            db.insert(, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
//        db.close();
    }

    public void addPrice_List_M(List<PriceListM> price) {
        db = this.getReadableDatabase();
        db.beginTransaction();

        for (int i = 0; i < price.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(ComapnyNo3, price.get(i).getCompanyNo());
            values.put(PrNo3, price.get(i).getPrNo());
            values.put(Description3, price.get(i).getDescribtion());
            values.put(IsSuspended3, price.get(i).getIsSuspended());
            db.insertWithOnConflict(Price_List_M, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//            db.insert(, null, values);
        }
//        db.close();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void addSales_Team(SalesTeam salesTeam) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ComapnyNo4, salesTeam.getCompanyNo());
        values.put(SalesManNo4, salesTeam.getSalesManNo());
        values.put(SalesManName4, salesTeam.getSalesManName());
        values.put(IsSuspended4, salesTeam.getIsSuspended());
        values.put(IP_ADDRESS_DEVICE, salesTeam.getIpAddressDevice());
        db.insert(Sales_Team, null, values);
        db.close();
    }

    public void addSalesMan_Items_Balance(List<SalesManItemsBalance > balance) {
//        db = this.getReadableDatabase();
//        db.beginTransaction();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        for (int i = 0; i < balance.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(ComapnyNo5, balance.get(i).getCompanyNo());
            values.put(SalesManNo5, balance.get(i).getSalesManNo());
            values.put(ItemNo5, balance.get(i).getItemNo());
            values.put(Qty5, balance.get(i).getQty());
            db.insertWithOnConflict(SalesMan_Items_Balance, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

//        db.insert(SalesMan_Items_Balance, null, values);
//
        db.setTransactionSuccessful();
        db.endTransaction();
//        db.close();
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
        values.put(UserType, salesMan.getUserType());
        db.insert(SalesMen, null, values);
        db.close();
    }
//    public void addItemUniteTable(List<ItemUnit> itemUnits) {
//
//        SQLiteDatabase Idb = this.getReadableDatabase();
//        Idb.beginTransaction();
//
//        for (int i = 0; i < itemUnits.size(); i++) {
//            ContentValues values = new ContentValues();
////            values.put(ITEM_O_CODE5, itemUnits.get(i).getItemOCode());
////            values.put(ITEM_BARCODE5, itemUnits.get(i).getItemBarcode());
////            values.put(SALE_PRICE5, itemUnits.get(i).getSalePrice());
////            values.put(ITEM_U5, convertToEnglish( itemUnits.get(i).getItemU()));
////            values.put(U_QTY5,  convertToEnglish(""+itemUnits.get(i).getUQty()));
////            values.put(U_SERIAL5,  convertToEnglish(""+itemUnits.get(i).getuSerial()));
////            values.put(CALC_QTY5,  convertToEnglish(""+itemUnits.get(i).getCalcQty()));
////            values.put(WHOLE_SALE_PRC5, itemUnits.get(i).getWholeSalePrc());
////            values.put(PURCHASE_PRICE5, itemUnits.get(i).getPurchasePrc());
////            values.put(PCLASS1, itemUnits.get(i).getPclAss1());
////            values.put(PCLASS2, itemUnits.get(i).getPclAss2());
////            values.put(PCLASS3, itemUnits.get(i).getPclAss3());
////            values.put(IN_DATE5, itemUnits.get(i).getInDate());
////            values.put(UNIT_NAME5, itemUnits.get(i).getUnitName());
////            values.put(ORG_SALEPRICE, itemUnits.get(i).getOrgSalePrice());
////            values.put(OLD_SALE_PRICE,  convertToEnglish(itemUnits.get(i).getOldSalePrice()));
////            values.put(UPDATE_DATE,  itemUnits.get(i).getUpdateDate());
//
//            Idb.insertWithOnConflict(ITEM_UNITS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//
//        }
//
//        Idb.setTransactionSuccessful();
//        Idb.endTransaction();
////        Idb.close();
//    }


    public void addCustomerPrice(List<CustomerPrice> price) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        for (int i = 0; i < price.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(ItemNumber, 0);
            values.put(CustomerNumber, price.get(i).getCustomerNumber());
            values.put(Price, price.get(i).getPrice());
            values.put(DISCOUNT_CUSTOMER, price.get(i).getDiscount());
            values.put(ItemNo_, price.get(i).getItemNumber());
            values.put(Other_Discount, price.get(i).getOther_Discount());
            values.put(FromDate, price.get(i).getFromDate());
            values.put(ToDate, price.get(i).getToDate());
            values.put(ListNo, price.get(i).getListNo());
            values.put(ListType, price.get(i).getListType());

            db.insertWithOnConflict(CustomerPrices, null, values, SQLiteDatabase.CONFLICT_REPLACE);

//            db.insert(, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
//        db.close();
    }

    public void addCustomerPrice_current(List<CustomerPrice> price) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Log.e("addCustomerPrice_",""+price.size());

        for (int i = 0; i < price.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(ItemNumber_, 0);
            values.put(CustomerNumber_, price.get(i).getCustomerNumber());
            values.put(Price_, price.get(i).getPrice());
            values.put(DISCOUNT_CUSTOMER_, price.get(i).getDiscount());
            values.put(ItemNo_N, price.get(i).getItemNumber());
            values.put(Other_Discount_, price.get(i).getOther_Discount());
            values.put(FromDate_, price.get(i).getFromDate());
            values.put(ToDate_, price.get(i).getToDate());
            values.put(ListNo_, price.get(i).getListNo());
            values.put(ListType_, price.get(i).getListType());

            db.insertWithOnConflict(CustomerPricesCurrent, null, values, SQLiteDatabase.CONFLICT_REPLACE);

//            db.insert(, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
//        db.close();
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
        values.put(REAL_LONGTUD, transaction.getLongtude());
        values.put(REAL_LATITUDE, transaction.getLatitud());
        values.put(VOUCHERCOUNT, transaction.getVOUCHERCOUNT());

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }
    public void addlogin(Transaction transaction) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_LOGIN, transaction.getCheckInDate());
        values.put(TIME_LOGIN, transaction.getCheckInTime());
        values.put(LONGTUDE2,  transaction.getLongtude());
        values.put(LATITUDE2,  transaction.getLatitud());

        values.put(SALESMAN_NO, transaction.getSalesManId());


        values.put(IS_POSTED_LOGIN, 0);

        db.insert(SALESMAN_LOGIN_LOGHistory, null, values);
        db.close();
    }


    public void addSetting(String ipAddress, int taxCalcKind, int transKind, int serialNumber, int priceByCust, int useWeightCase,
                           int allowMinus, int numOfCopy, int salesManCustomers, int minSalePrice, int printMethod, int allowOutOfRange,int canChangePrice, int canChangePrice_RETURNONLY,int readDiscount,
                           int workOnline,int  payMethodCheck,int bonusNotAlowed,int noOfferForCredid,int amountOfMaxDiscount,int customerOthoriz,
                           int passowrdData,int arabicLanguage,int hideQty,int lock_cashreport,String salesman_name,int preventOrder,int requiNote,int preventDiscTotal,
                           int automaticCheque,int tafqit,int preventChangPayMeth,int showCustomer,int noReturnInvoi,
                           int Work_serialNo,int itemPhoto , int approveAddmin ,int saveOnly,int showSolidQty,int offerFromAdmin,String ipPort,int checkServer,
                           int dontShowTax,String cono,int contireading,int activeTotDisc,double valueDisc,String store,int itemUnit,int sumQtys,int noDuplicate,
                           int salesoffersflage,int checkqtyinorderflage,int locationtrackerflage,int aqapaTax,int show_location,int Items_unitflage,int EndTripReportflage,
                           int AcountatatmentVisflage,int SharWhatsAppform,int discAfterTax,int add_custumerPer,int LastCustPricevalu,int ReturnVoch_approveAdm,int plansetting,int CompanyinfoINpdf ,int AddedCustomer_Per) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        //,String ipPort
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
        values.put(CAN_CHANGE_PRICE_RETURNONLY, canChangePrice_RETURNONLY);

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
        values.put(APPROVE_ADMIN,approveAddmin);
        values.put(SAVE_ONLY,saveOnly);
        Log.e("showSolidQty",""+showSolidQty);
        values.put(SHOW_QUANTITY_SOLD,showSolidQty);
        values.put(READ_OFFER_FROM_ADMIN,offerFromAdmin);
        values.put(IP_PORT,ipPort);
        values.put(  CheckQtyServer,checkServer);
        values.put(  DontShowTaxOnPrinter,dontShowTax);
        values.put(  CONO,cono);
        values.put(  ContinusReading,contireading);

        values.put(  ActiveTotalDisc,activeTotDisc);
        Log.e("valueDisc","addSetting"+valueDisc);
        values.put(  ValueTotalDisc,valueDisc);
        Log.e("valueDisc","store="+store);
        values.put(  STORE_NO,store);
        values.put(Item_Unit,itemUnit);
        values.put( SUM_CURRENT_QTY,sumQtys);
        values.put(      DONT_DUPLICATE_ITEMS,noDuplicate);
        values.put(      OffersJustForSales,salesoffersflage);
        values.put(      CheckQtyinOrder,checkqtyinorderflage);
       values.put(      locationtracker,locationtrackerflage);


       values.put(AqapaTax,aqapaTax);
       values.put(SHOW_CUSTOMER_LOCATION,show_location);
       values .put(Items_Units,Items_unitflage);
       Log.e("EndTripReportflage","EndTripReportflage="+EndTripReportflage);
       values .put(EndTripReport,EndTripReportflage);

       values .put(AcountatatmentVis,AcountatatmentVisflage);
       values .put(SharWhatsApp_Form,SharWhatsAppform);
       values .put(DISC_AFTER_TAX,discAfterTax);
       values .put(Add_CustumerPer,add_custumerPer);
       values .put(LastCustPrice,LastCustPricevalu);
        values .put(ReturnVoch_PERM,ReturnVoch_approveAdm);
        values .put(AddedCustomer_PERM,AddedCustomer_Per);
        values .put(CompanyinfoINPdf,CompanyinfoINpdf);

        values .put(plansetting_KIND,plansetting);

        db.insert(TABLE_SETTING, null, values);
        db.close();
    }
    public void addIPSetting(int transKind,int serialNo,String ipAddress,String ipPort,String compaNO) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        //,String ipPort
        int defaultValue=0;

        values.put(TRANS_KIND, transKind);
        values.put(SERIAL_NUMBER, serialNo);
        values.put(IP_ADDRESS, ipAddress);
        values.put(TAX_CALC_KIND, defaultValue);
        values.put(PRICE_BYCUSTOMER, defaultValue);
        values.put(USE_WEIGHT_CASE, defaultValue);
        values.put(ALLOAW_MINUS, defaultValue);
        values.put(NUMBER_OF_COPIES, 1);
        values.put(SALESMAN_CUSTOMERS, defaultValue);
        values.put(MIN_SALE_PRICE, defaultValue);
        values.put(PRINT_METHOD, defaultValue);
        values.put(CAN_CHANGE_PRICE, defaultValue);
        values.put(CAN_CHANGE_PRICE_RETURNONLY, defaultValue);

        values.put(ALLOW_OUT_OF_RANGE, defaultValue);
        values.put(READ_DISCOUNT_FROM_OFFERS, defaultValue);
        values.put(WORK_ONLINE, defaultValue);
        values.put(PAYMETHOD_CHECK, defaultValue);
        values.put(BONUS_NOT_ALLOWED,defaultValue);//16
        values.put(NO_OFFERS_FOR_CREDIT_INVOICE,defaultValue);
        values.put(AMOUNT_OF_MAX_DISCOUNT,defaultValue);
        values.put(Customer_Authorized,defaultValue);
        values.put(Password_Data,defaultValue);
        values.put(Arabic_Language,1);
        values.put(HideQty,defaultValue);
        values.put(LockCashReport,defaultValue);
        values.put(salesManName,"");
        values.put(PreventOrder,defaultValue);
        values.put(RequiredNote,defaultValue);
        values.put(PreventTotalDiscount,defaultValue);
        values.put(AutomaticCheque,defaultValue);
        values.put(Tafqit,defaultValue);
        values.put(PreventChangPayMeth,defaultValue);
        values.put(ShowCustomerList,1);
        values.put(NoReturnInvoice,defaultValue);
        values.put(WORK_WITH_SERIAL,defaultValue);
        values.put(SHOW_IMAGE_ITEM,defaultValue);
        values.put(APPROVE_ADMIN,defaultValue);
        values.put(SAVE_ONLY,defaultValue);
        values.put(SHOW_QUANTITY_SOLD,defaultValue);
        values.put(READ_OFFER_FROM_ADMIN,defaultValue);
        values.put(IP_PORT,ipPort);
        values.put(CheckQtyServer,defaultValue);
        values.put(DontShowTaxOnPrinter,defaultValue);
        values.put(CONO,compaNO);
        values.put(ContinusReading,defaultValue);
        values.put(  ActiveTotalDisc,defaultValue);
        values.put(  ValueTotalDisc,defaultValue);
        values.put(  STORE_NO,"1");
       values.put(Item_Unit, defaultValue);
       values.put(SUM_CURRENT_QTY, defaultValue);
       values.put(DONT_DUPLICATE_ITEMS, defaultValue);
       values.put(OffersJustForSales, defaultValue);
       values.put(CheckQtyinOrder, defaultValue);
       values.put(locationtracker, defaultValue);
       values.put(AqapaTax, defaultValue);
       values.put( SHOW_CUSTOMER_LOCATION, defaultValue);
       values.put( Items_Units, defaultValue);
       values.put( EndTripReport, defaultValue);
       values.put( AcountatatmentVis, 1);
       values.put( SharWhatsApp_Form, 0);
       values.put( DISC_AFTER_TAX, defaultValue);
       values.put( Add_CustumerPer, 1);
       values.put(      LastCustPrice, 0);
        values.put(      ReturnVoch_PERM, 0);
        values.put(      AddedCustomer_PERM, 0);
        values.put(      plansetting_KIND, 0);
        values.put(      CompanyinfoINPdf, 0);


       db.insert(TABLE_SETTING, null, values);
       db.close();
    }




    public void addCompanyInfo(String companyName, String companyTel, int taxNo, Bitmap logo,String note,
                               double longtude,double latitude,int position,String national_id,String car_no) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
Log.e("addCompanyInfo","addCompanyInfo");
        byte[] byteImage = {};
        if (logo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            logo.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteImage = stream.toByteArray();
        }

        values.put(COMPANY_NAME, companyName);
        values.put(COMPANY_PHONE, companyTel);
        values.put(COMPANY_TEL, companyTel);
        values.put(TAX_NO, taxNo);
        values.put(LOGO, byteImage);
        values.put(NOTE, note);
        values.put(LONGTUDE_COMPANY,longtude);
        values.put(LATITUDE_COMPANY,latitude);
        values.put(NOTEPOSITION,position);
       values.put( NATIONAL_ID ,national_id);
       values.put( CAR_NO ,car_no);

        db.insert(COMPANY_INFO, null, values);
        db.close();
    }

    public void addVoucher(Voucher voucher) {
        // 2 add time
        Log.e("addVoucher","addVoucher");
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
        values.put(VOUCHER_time, voucher.getTime());
        values.put(ORIGINALVOUCHER_NUMBER, voucher.getORIGINALvoucherNo());

        values.put(TAX_TYPE,voucher.getTaxTypa());
        try {

            db.insert(SALES_VOUCHER_MASTER, null, values);
            db.close();
        }
        catch ( Exception e)
        {
            int vouch=voucher.getVoucherNumber();
            values.put(VOUCHER_NUMBER, (vouch++));
            db.insert(SALES_VOUCHER_MASTER, null, values);
            db.close();
            Log.e("DBException","addVoucher");
        }

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
        values.put(ORIGINALVOUCHER_NUMBER, item.getORIGINALvoucherNo());
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

        values.put(WHICH_UNIT    , item.getWhich_unit());
        values.put(WHICH_UNIT_STR, item.getWhich_unit_str());
        values.put(WHICHU_QTY    , item.getWhichu_qty());
        values.put(ENTER_QTY     , item.getEnter_qty());
        values.put(ENTER_PRICE   , item.getEnter_price());
        values.put(UNIT_BARCODE  , item.getUnit_barcode());



         Log.e("addItem",""+item.getBarcode());
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
        Log.e("cusNumber","2="+payment.getCustNumber());
        values.put(CUSTOMER_NUMBER, payment.getCustNumber());
        values.put(AMOUNT, payment.getAmount());
        values.put(REMARK3, payment.getRemark());
        values.put(SALES_MAN_NUMBER3, payment.getSaleManNumber());
        values.put(IS_POSTED3, payment.getIsPosted());
        values.put(PAY_METHOD3, payment.getPayMethod());
        values.put(CUSTOMER_NAME, payment.getCustName());
        values.put(PAY_YEAR, payment.getYear());
        values.put(CUSTOMER_NUMBER_STR, payment.getCustNumber());
        db.insert(PAYMENTS, null, values);
        db.close();
    }

    public void addUserNO(String  USER_NO,String version) {
       Log.e("version","addUserNO="+version);
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserNo_LogIn,USER_NO);
       values.put(VERSION_APK,version);
        db.insert(SalesMenLogIn, null, values);
        db.close();
    }
    public void updateUserNO(String  USER_NO,String version) {
       Log.e("version","updateUserNO="+version);
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserNo_LogIn,USER_NO);
       values.put(VERSION_APK,version);
        db.update(SalesMenLogIn, values,null,null);
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
        values.put(CURRENT_QTY, item.getCurrentQty());
        values.put(isPostedDetails, 0);
        Log.e("CURRENT_QTY",""+CURRENT_QTY);


        db.insert(REQUEST_DETAILS, null, values);
        db.close();
    }

    public void addAddedCustomer(AddedCustomer customer) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        Log.e("#1addAddedCustomer",""+customer.getCustName());

        values.put(CUST_NAME7, customer.getCustName());
        values.put(REMARK7, customer.getRemark());
        values.put(LATITUDE7, customer.getLatitude());
        values.put(LONGITUDE7, customer.getLongtitude());
        values.put(SALESMAN7, customer.getSalesMan());
        values.put(SALESMAN_NO7, customer.getSalesmanNo());
        values.put(IS_POSTED7, customer.getIsPosted());
        values.put(ADRESS_CUSTOMER, customer.getADRESS_CUSTOMER());
        values.put(TELEPHONE, customer.getTELEPHONE());
        values.put(CONTACT_PERSON, customer.getCONTACT_PERSON());





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
        values.put(DISCOUNT_ITEM_TYPE, offers.getDiscountItemType());

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
                setting.setApproveAdmin((cursor.getInt(35)));
                setting.setSaveOnly((cursor.getInt(36)));
                setting.setShow_quantity_sold((cursor.getInt(37)));
                setting.setReadOfferFromAdmin((cursor.getInt(38)));
                setting.setIpPort((cursor.getString(39)));
                setting.setQtyServer((cursor.getInt(40)));
                setting.setDontShowtax((cursor.getInt(41)));
                setting.setCoNo((cursor.getString(42)));
                setting.setContinusReading((cursor.getInt(43)));
                setting.setActiveTotalDiscount((cursor.getInt(44)));
                setting.setValueOfTotalDiscount((cursor.getDouble(45)));
                setting.setStoreNo((cursor.getString(46)));
                setting.setItemUnit((cursor.getInt(47)));
                setting.setSumCurrentQty((cursor.getInt(48)));
                setting.setDontduplicateItem((cursor.getInt(49)));
                setting.setCanChangePrice_returnonly(cursor.getInt(50));
                setting.setOffersJustForSales(cursor.getInt(51));
                setting.setCheckQtyinOrder(cursor.getInt(52));
                setting.setLocationtracker(cursor.getInt(53));
                setting.setAqapaTax(cursor.getInt(54));
                setting.setShowCustomerLocation(cursor.getInt(55));
               setting.setItems_Unit(cursor.getInt(56));
               setting.setEndTripReport(cursor.getInt(57));
               setting.setAcountatatmentVisable(cursor.getInt(58));
               setting.setSharWhatsAppForm(cursor.getInt(59));
               setting.setDiscAfterTax(cursor.getInt(60));
               setting.setAdd_CustumerPer(cursor.getInt(61));
               setting.setLastCustPrice(cursor.getInt(62));
                setting.setReturnVoch_approveAdmin(cursor.getInt(63));
                setting.setPlanKind(cursor.getInt(64));
                setting.setCompanyinfoINPdf(cursor.getInt(65));
                setting.setAddedCustomer_PERM(cursor.getInt(66));

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
        try {


            if (cursor.moveToFirst()) {
                do {
                    CompanyInfo info = new CompanyInfo();
                    info.setCompanyName(cursor.getString(0));
                    info.setcompanyTel(cursor.getString(8));
                    info.setTaxNo(Integer.parseInt(cursor.getString(2)));

                    if (cursor.getBlob(3).length == 0)
                        info.setLogo(null);
                    else
                        info.setLogo(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0, cursor.getBlob(3).length));

                    info.setNoteForPrint(cursor.getString(4));
                    info.setLongtudeCompany(cursor.getDouble(5));
                    info.setLatitudeCompany(cursor.getDouble(6));
                    info.setNotePosition(cursor.getString(7));
                   info.setNational_id(cursor.getString(9));
                   info.setCarNo(cursor.getString(10));
                    infos.add(info);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e("Exception","COMPANY_NAME");
            selectQuery = "SELECT  COMPANY_NAME,COMPANY_PHONE,TAX_NO,NOTE FROM  COMPANY_INFO";
            db = this.getWritableDatabase();
            Cursor cursor2 = db.rawQuery(selectQuery, null);
            if (cursor2.moveToFirst()) {
                do {
                    CompanyInfo info = new CompanyInfo();
                    info.setCompanyName(cursor2.getString(0));
                    info.setcompanyTel(cursor2.getString(1));
                    info.setTaxNo(Integer.parseInt(cursor2.getString(2)));
                    info.setLogo(null);


                    info.setNoteForPrint(cursor2.getString(3));
                    info.setLongtudeCompany(0);
                    info.setLatitudeCompany(0);
                    infos.add(info);
                } while (cursor2.moveToNext());
            }
        }
        return infos;
    }
    public List<serialModel> getAllSerialItems() {
       Log.e("getAllSerialItems==", "getAllSerialItems");
        List<serialModel> infos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM  SERIAL_ITEMS_TABLE WHERE IS_POSTED_SERIAL=0";
       Log.e("selectQuery==", selectQuery+"");
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
       Log.e("getAllSerialItems,cursor==", "cursor"+cursor.getCount());
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
                info.setPriceItem(cursor.getFloat(10));
                infos.add(info);

            } while (cursor.moveToNext());
            Log.e("getAllSerialItems",""+infos.size());
        }
        return infos;
    }
    public ArrayList<serialModel> getAllSerialItemsByVoucherNo(String voucherNo) {// here
        ArrayList<serialModel> infos = new ArrayList<>();
//        SELECT  * FROM  SERIAL_ITEMS_TABLE where VOUCHER_NO='333886' and  KIND_VOUCHER='504'
        String selectQuery = "SELECT  * FROM  SERIAL_ITEMS_TABLE where VOUCHER_NO='"+voucherNo+"' and  KIND_VOUCHER='504' and IS_RETURNED=0 ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                serialModel info = new serialModel();
                info.setSerialCode(cursor.getString(1));
                info.setCounterSerial(Integer.parseInt(cursor.getString(2)));
                info.setVoucherNo((cursor.getString(3)));
                info.setItemNo(cursor.getString(4));
//                info.setKindVoucher(cursor.getString(5));
                info.setKindVoucher("506");
                info.setDateVoucher(cursor.getString(6));
                info.setStoreNo(cursor.getString(7));
                info.setIsPosted(cursor.getString(8));
                info.setIsBonus(cursor.getString(9));
                info.setPriceItem(cursor.getFloat(10));
                info.setIsReturned(cursor.getInt(12));
                Log.e("setIsReturned",""+info.getIsReturned());
                info.setIsPosted("0");
                infos.add(info);

            } while (cursor.moveToNext());
            Log.e("getAllSerialItems",""+infos.size());
        }
        return infos;
    }
    public ArrayList<serialModel> getAllSerialItemsByVoucherNoAndItems(String voucherNo,String itemno) {// here
        ArrayList<serialModel> infos = new ArrayList<>();
//        SELECT  * FROM  SERIAL_ITEMS_TABLE where VOUCHER_NO='333886' and  KIND_VOUCHER='504'
        String selectQuery = "SELECT  * FROM  SERIAL_ITEMS_TABLE where VOUCHER_NO='"+voucherNo+"' and  KIND_VOUCHER='504' and IS_RETURNED=0 and ITEMNO_SERIAL="+"'"+itemno+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                serialModel info = new serialModel();
                info.setSerialCode(cursor.getString(1));
                info.setCounterSerial(Integer.parseInt(cursor.getString(2)));
                info.setVoucherNo((cursor.getString(3)));
                info.setItemNo(cursor.getString(4));
//                info.setKindVoucher(cursor.getString(5));
                info.setKindVoucher("506");
                info.setDateVoucher(cursor.getString(6));
                info.setStoreNo(cursor.getString(7));
                info.setIsPosted(cursor.getString(8));
                info.setIsBonus(cursor.getString(9));
                info.setPriceItem(cursor.getFloat(10));
                info.setIsReturned(cursor.getInt(12));
                Log.e("setIsReturned",""+info.getIsReturned());
                info.setIsPosted("0");
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
     public CustomerLocation getCustomerLocationBYNUMBER(String CUS_NO ) {

//         CREATE TABLE "CUSTOMER_LOCATION" (
//                 "CUS_NO"	TEXT,
//                 "LONG"	TEXT,
//                 "LATIT"	TEXT
//);
         String selectQuery = "select  * FROM CUSTOMER_LOCATION WHERE  CUS_NO= '"+CUS_NO+"' ";
         CustomerLocation info=new CustomerLocation();
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

         if (cursor.moveToFirst()) {
             do {
                  info = new CustomerLocation();
                 info.setCUS_NO(cursor.getString(0));
                 info.setLONG((cursor.getString(1)));
                 info.setLATIT((cursor.getString(2)));



             } while (cursor.moveToNext());
         }
         return info;
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
        int maxVoucher=0;
        try {
            cursor.moveToFirst();

            maxVoucher = Integer.parseInt(cursor.getString(0));
        }
        catch (Exception e){maxVoucher=0;}


        return maxVoucher;

    }
    public int getMaxSerialNumberFromVoucherMaster(int voucherType) {
        int maxVoucher = 0;
        if (typaImport == 1)//iis
        {
            maxVoucher=getMaxSerialNumberFromSerlizeTable(voucherType);
            Log.e("maxVoucher**","=="+maxVoucher);
            if(maxVoucher!=0)
            {
                return maxVoucher;
            }else {
                maxVoucher=getMaxFromVoucherMaster( voucherType);
                return maxVoucher;
            }



        } else {// for sql import

            maxVoucher=getMaxFromVoucherMaster( voucherType);
            return maxVoucher;

        }

    }

    public int getMaxFromVoucherMaster(int voucherType) {

        int maxVoucher=0;
        //SELECT IFNULL((select max(VOUCHER_NUMBER) FROM SALES_VOUCHER_MASTER  where VOUCHER_TYPE = '508'),-1)
        String selectQuery = "SELECT IFNULL((select max(VOUCHER_NUMBER) FROM " + SALES_VOUCHER_MASTER + " WHERE VOUCHER_TYPE = '" + voucherType + "' ),-1)";
        db = this.getWritableDatabase();
        maxVoucher = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            cursor.moveToFirst();


            maxVoucher = Integer.parseInt(cursor.getString(0));
            if (maxVoucher == -1) {
                maxVoucher = getMaxSerialNumber(voucherType);
                Log.e("getMaxSerialNumber", "FromSetting" + maxVoucher);
            }
            Log.e("getMaxSerialNumber", "FromVoucherMaster" + maxVoucher);
        } catch (Exception e) {
            maxVoucher = 0;
        }

        return maxVoucher;


    }

    public int getMaxSerialNumberFromSerlizeTable(int voucherType) {
        //SELECT IFNULL((select max(VOUCHER_NUMBER) FROM SALES_VOUCHER_MASTER  where VOUCHER_TYPE = '508'),-1)
        String selectQuery = "SELECT * FROM " + VoucherSerialize  ;
        db = this.getWritableDatabase();
        int maxVoucher=0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            cursor.moveToFirst();
            if(voucherType==504)
            {
                maxVoucher = Integer.parseInt(cursor.getString(0));
            }
            if(voucherType==506)
            {
                maxVoucher = Integer.parseInt(cursor.getString(1));
            }
            if(voucherType==508)
            {
                maxVoucher = Integer.parseInt(cursor.getString(2));
            }


            Log.e("getFromSerlizeTable","maxVoucher"+maxVoucher);
        }catch (Exception e){maxVoucher=0;}

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
     public void setServer_MaxSerialNumber(int voucherType, long newSerial) {
         db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         if(voucherType==1)
         values.put(PaymentSerials_cash, newSerial+1);
         else
             values.put(PaymentSerials_cridt, newSerial+1);
         // updating row
         db.update(PaymentSerials, values, null, null);


     }


     public  void deleteSerialsPayment(){
         SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL("delete from " + PaymentSerials);
         db.close();
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
                transaction.setLongtude(Double.parseDouble(cursor.getString(9)));
                transaction.setLatitud(Double.parseDouble(cursor.getString(10)));
                transaction.setVOUCHERCOUNT(Integer.parseInt(cursor.getString(11)));
                // Adding transaction to list
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        return transactionList;
    }

    //*******************************************************************
    public List<Transaction> getLoginSalesman() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SALESMAN_LOGIN_LOGHistory;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();

                transaction.setCheckInDate(cursor.getString(0));
                transaction.setCheckInTime(cursor.getString(1));
                // checkout time
                transaction.setLongtude(cursor.getDouble(3));
                transaction.setLatitud(cursor.getDouble(4));
                transaction.setSalesManId(Integer.parseInt(cursor.getString(5)));
                transaction.setIsPosted(Integer.parseInt(cursor.getString(6)));
                // Adding transaction to list
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        return transactionList;
    }
    //*******************************************************************
    /*//    String CREATE_SALESMAN_LOGIN_TABLE= "CREATE TABLE " + SALESMAN_LOGIN_TABLE + "("
//            + KEY_LOGIN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + DATE_LOGIN + " TEXT,"
//            + TIME_LOGIN + " TEXT,"
//            + TIME_LOGOUT + " TEXT,"
//            + LONGTUDE2 + " REAL,"
//            + LATITUDE2 + " REAL,"
//            + SALESMAN_NO + " TEXT,"
//            + IS_POSTED_LOGIN + " INTEGER"+
//
//            ")";
//        db.execSQL(CREATE_SALESMAN_LOGIN_TABLE);*/

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
                salesMan.setUserType(cursor.getInt(2));
                // Adding transaction to list
                salesMen.add(salesMan);
               // Log.e("getAllSalesMen",""+salesMen.size());
            } while (cursor.moveToNext());
        }
        return salesMen;
    }
    public int getAllSalesMentype(String username,String password) {
        int usertype=-1;    // Select All Query
        String selectQuery = "SELECT  UserType FROM " + SalesMen+ " WHERE UserName= '"+username+"' AND Password= '"+password+"' ";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.e("*****", "" + cursor.getCount());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {



                usertype= cursor.getInt(0);
                // Adding transaction to list


            } while (cursor.moveToNext());
        }
        return usertype;
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
                customer.setCompanyNumber(cursor.getString(0));
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
                try {
                    customer.setFax(cursor.getString(18));
                    customer.setZipCode(cursor.getString(19));
                    customer.seteMail(cursor.getString(20));
                    customer.setC_THECATEG(cursor.getString(21));
                }
                catch (Exception e){
                    customer.setFax("");
                    customer.setZipCode("");
                    customer.seteMail("");
                    customer.setC_THECATEG("");
                }

//                customer.setCustId(cursor.getString(16));// for test talley
                // 16 column isPosted
                // Adding transaction to list
                customers.add(customer);
            } while (cursor.moveToNext());
        }
        return customers;
    }

    // get customer cridit limit and cash credit balance  by customer No



     public List<Customer> getAllCustomersFilters(String classifica,String acc,String spicilisty,String categ) {
         List<Customer> customers = new ArrayList<Customer>();
         // Select All Query
         String qundition="";
         if(!classifica.equals("")) {
             if(!acc.equals("")||!spicilisty.equals("")|| !categ.equals(""))
             qundition += "FAX='" + classifica + "' and ";
             else  qundition += "FAX='" + classifica+ "' ";
         }
         if(!acc.equals(""))
         {
             if(!spicilisty.equals("")||!categ.equals(""))
             qundition+="ZipCode='"+acc+"' and ";
             else   qundition+="ZipCode='"+acc+"'";
         }
         if(!spicilisty.equals(""))
         {
             if(!categ.equals("")){
                 qundition+="EMail='"+spicilisty+"' and ";
             }else
             qundition+="EMail='"+spicilisty+"'";
             Log.e("qundition",""+qundition);
         }


         if(!categ.equals(""))
             qundition+="C_THECATEG='"+categ+"'";
         Log.e("qundition",""+qundition);

         String selectQuery ="";
         if(qundition.length()!=0)
         selectQuery = "SELECT  * FROM  CUSTOMER_MASTER where "+qundition+" ";
         else  selectQuery = "SELECT  * FROM  CUSTOMER_MASTER ";
         Log.e("qundition","selectQuery="+selectQuery);
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

         // looping through all rows and adding to list
         if (cursor.moveToFirst()) {
             do {
                 Customer customer = new Customer();
                 customer.setCompanyNumber(cursor.getString(0));
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
                 try {
                     customer.setFax(cursor.getString(18));
                     customer.setZipCode(cursor.getString(19));
                     customer.seteMail(cursor.getString(20));
                     customer.setC_THECATEG(cursor.getString(21));
                 }
                 catch (Exception e){
                     customer.setFax("");
                     customer.setZipCode("");
                     customer.seteMail("");
                     customer.setC_THECATEG("");
                 }

//                customer.setCustId(cursor.getString(16));// for test talley
                 // 16 column isPosted
                 // Adding transaction to list
                 customers.add(customer);
             } while (cursor.moveToNext());
         }
         return customers;
     }
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




    public String getcustomerbyname(String CUS_name) {
       String customer = "";

       String selectQuery = "SELECT  CUS_ID FROM " + CUSTOMER_MASTER + " where CUS_NAME0 = '" + CUS_name + "'";

       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
          do {



             customer=      cursor.getString(0);


          } while (cursor.moveToNext());
       }
       return customer;
    }
    public Customer getcustomerinfo(String CUS_ID) {
      Customer customer = new Customer();
       // Select All Query
       ///   SELECT* from CUSTOMER_MASTER WHERE CUS_ID=1110010131
       String selectQuery = "SELECT  * FROM " + CUSTOMER_MASTER + " where CUS_ID = '" + CUS_ID + "'";

       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
          do {


             customer.setCompanyNumber(cursor.getString(0));
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
             Log.e("  customer.setCustLat",customer.getCustId()+"  "+customer.getCustLong()+","+customer.getCustLat());
             try {
                customer.setMax_discount(Double.parseDouble(cursor.getString(12)));
             }catch(NumberFormatException e){
                Log.e("setMax_discount",""+e.getMessage());
                customer.setMax_discount(0);
             }
             customer.setACCPRC(cursor.getString(13));
             customer.setHide_val(cursor.getInt(14));
              try {
                  customer.setFax(cursor.getString(18));
                  customer.setZipCode(cursor.getString(19));
                  customer.seteMail(cursor.getString(20));
                  customer.setC_THECATEG(cursor.getString(21));
              }
              catch (Exception e){
                  customer.setFax("");
                  customer.setZipCode("");
                  customer.seteMail("");
                  customer.setC_THECATEG("");
              }
//                customer.setCustId(cursor.getString(16));// test talley
             // Adding transaction to list

          } while (cursor.moveToNext());
       }
       return customer;
    }
    public List<Customer> getCustomersBySalesMan(String salesMan) {
        List<Customer> customers = new ArrayList<Customer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CUSTOMER_MASTER + " where CAST(SALES_MAN_NO as integer) = '" + salesMan + "'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();

                customer.setCompanyNumber(cursor.getString(0));
                customer.setCustId(cursor.getString(1));// test talley

                customer.setCustName(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setIsSuspended(Integer.parseInt(cursor.getString(4)));
                customer.setPriceListId(cursor.getString(5));
                customer.setCashCredit(Integer.parseInt(cursor.getString(6)));
                customer.setSalesManNumber(cursor.getString(7));
                customer.setCreditLimit(Double.parseDouble(cursor.getString(8)));
                customer.setPayMethod(Integer.parseInt(cursor.getString(9)));

                customer.setCustLat(cursor.getString(10));
                customer.setCustLong(cursor.getString(11));
               Log.e("  customer.setCustLat",customer.getCustId()+"  "+customer.getCustLong()+","+customer.getCustLat());
                try {
                    customer.setMax_discount(Double.parseDouble(cursor.getString(12)));
                }catch(NumberFormatException e){
                    Log.e("setMax_discount",""+e.getMessage());
                    customer.setMax_discount(0);
                }
                customer.setACCPRC(cursor.getString(13));
                customer.setHide_val(cursor.getInt(14));
                try {
                    customer.setFax(cursor.getString(18));
                    customer.setZipCode(cursor.getString(19));
                    customer.seteMail(cursor.getString(20));
                    customer.setC_THECATEG(cursor.getString(21));
                }
                catch (Exception e){
                    customer.setFax("");
                    customer.setZipCode("");
                    customer.seteMail("");
                    customer.setC_THECATEG("");
                }
//                customer.setCustId(cursor.getString(16));// test talley
                // Adding transaction to list
                customers.add(customer);
            } while (cursor.moveToNext());
           Log.e("getCustomersBySalesMan","salesMan"+customers.size());
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
                Voucher.setTime(cursor.getString(17));
                Voucher.setTaxTypa(cursor.getInt(19));


                // Adding transaction to list
                vouchers.add(Voucher);
            } while (cursor.moveToNext());
        }

        return vouchers;
    }
    public List<Voucher> getAllVouchers_NotPosted() {
        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query
        String selectQuery = "SELECT  * FROM  SALES_VOUCHER_MASTER where IS_POSTED=0";

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
                Voucher.setTime(cursor.getString(17));


                Voucher.setTaxTypa(cursor.getInt(19));
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
                Voucher.setTime(cursor.getString(17));

                // Adding transaction to list
                vouchers.add(Voucher);
            } while (cursor.moveToNext());
        }

        return vouchers;
    }

    public Voucher getAllVouchers_VoucherNo(int voucherNo,int voucherKind ) {
//        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query
        Log.e("voucherNoDB",""+voucherNo);

        Voucher Voucher= new Voucher();

        String selectQuery = "SELECT  * FROM " + SALES_VOUCHER_MASTER +" where VOUCHER_NUMBER = '" + voucherNo + "'and  VOUCHER_TYPE='"+voucherKind+"' ";
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
                Voucher.setTime(cursor.getString(17));
                // Adding transaction to list

            } while (cursor.moveToNext());
        }

        return Voucher;
    }


    public Voucher getAllVouchers_VoucherNoAndCusId(int voucherNo,int voucherKind ,String CusId) {
//        List<Voucher> vouchers = new ArrayList<Voucher>();
        // Select All Query
        Log.e("voucherNoDB",""+voucherNo);

        Voucher Voucher= new Voucher();

        String selectQuery = "SELECT  * FROM " + SALES_VOUCHER_MASTER +" where VOUCHER_NUMBER = '" + voucherNo + "'and  VOUCHER_TYPE='"+voucherKind+"' and CUST_NUMBER= '"+CusId+"' ";
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
                Voucher.setTime(cursor.getString(17));
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
                itemsMaster.setCompanyNo(cursor.getString(0));
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
    public ItemsMaster getItemMasterForItem(String ItemNo2 ) {
        Log.e("getItemMasterForItem===","getItemMasterForItem");
        ItemsMaster itemsMaster = new ItemsMaster();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Items_Master+" WHERE ItemNo= '" + ItemNo2+"'";
        Log.e("selectQuery===",selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                itemsMaster.setCompanyNo(cursor.getString(0));
                itemsMaster.setItemNo(cursor.getString(1));
                itemsMaster.setName(cursor.getString(2));
                itemsMaster.setCategoryId(cursor.getString(3));
                itemsMaster.setBarcode(cursor.getString(4));
                itemsMaster.setIsSuspended(Integer.parseInt(cursor.getString(5)));
                itemsMaster.setItemL(Double.parseDouble(cursor.getString(6)));
                itemsMaster.setPosPrice(cursor.getDouble(7));
                Log.e("getPosPrice===",itemsMaster.getPosPrice()+"");
            }
            while(cursor.moveToNext());
        }
        return  itemsMaster;
    }
    public List<ItemsMaster> getItemkinds(String KINDITEM) {
        List<ItemsMaster> mastersItemkinds = new ArrayList<ItemsMaster>();
        // Select All Query
        //  String selectQuery = "SELECT  * FROM " + SALES_VOUCHER_MASTER +" where VOUCHER_NUMBER = '" + voucherNo + "' ";

        String selectQuery = "SELECT * FROM " + Items_Master+" where KIND_ITEM = '"+KINDITEM+"' ";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemsMaster itemsMaster = new ItemsMaster();
                itemsMaster.setCompanyNo(cursor.getString(0));
                itemsMaster.setItemNo(cursor.getString(1));
                itemsMaster.setName(cursor.getString(2));
                itemsMaster.setCategoryId(cursor.getString(3));
                itemsMaster.setBarcode(cursor.getString(4));
                itemsMaster.setIsSuspended(Integer.parseInt(cursor.getString(5)));
                itemsMaster.setItemL(Double.parseDouble(cursor.getString(6)));
                itemsMaster.setKind_item(cursor.getString(8));
                mastersItemkinds.add(itemsMaster);
            }
            while(cursor.moveToNext());
        }
        return  mastersItemkinds;
    }
    public List<ItemsMaster> getItemMaster2() {
        List<ItemsMaster> masters = new ArrayList<ItemsMaster>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Items_Master;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemsMaster itemsMaster = new ItemsMaster();
                itemsMaster.setCompanyNo(cursor.getString(0));
                itemsMaster.setItemNo(cursor.getString(1));
                itemsMaster.setName(cursor.getString(2));
                itemsMaster.setCategoryId(cursor.getString(3));
                itemsMaster.setBarcode(cursor.getString(4));
                itemsMaster.setIsSuspended(Integer.parseInt(cursor.getString(5)));
                itemsMaster.setItemL(Double.parseDouble(cursor.getString(6)));
                itemsMaster.setKind_item(cursor.getString(8));
                masters.add(itemsMaster);
            }
            while(cursor.moveToNext());
        }
        return  masters;
    }

    public ArrayList<Item> getAllItems_byVoucherNo(String voucherNo) {
        ArrayList<Item> items = new ArrayList<Item>();
        // Select All Query                                                                                                          //AND IS_RETURNED = '0'
        String selectQuery = "SELECT  * FROM  SALES_VOUCHER_DETAILS where VOUCHER_NUMBER='"+voucherNo+"' and  VOUCHER_TYPE='504'";

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

                item.setWhich_unit(cursor.getString(19));
                item.setWhich_unit_str(cursor.getString(20));
                item.setWhichu_qty(cursor.getString(21));
                item.setEnter_qty(cursor.getString(22));
                item.setEnter_price(cursor.getString(23));
                item.setUnit_barcode(cursor.getString(24));
                item.setIS_RETURNED(Integer.parseInt(cursor.getString(25)));
                item.setAvi_Qty(Float.parseFloat(cursor.getString(26)));
//                Log.e("setDescreption",""+cursor.getString(17));
                Log.e("Avi_Qty",cursor.getString(25)+"   return=="+cursor.getString(25));
                // Adding transaction to list
               Log.i("getPrice===", "" + item.getPrice());
                if( item.getIS_RETURNED()==0)
                {
                    if(item.getAvi_Qty()==0)
                        item.setAvi_Qty(  item.getQty());

                    items.add(item);

                }
                else if( item.getIS_RETURNED()==1 &&

                        item.getAvi_Qty()>0)
                {
                    items.add(item);

                }
            } while (cursor.moveToNext());
        }
        return items;
    }

    ///B
    public List<Integer> getVoucherByCustomerNo(String customerNo) {

        List<Integer> voucherNumbers = new ArrayList<>();

        String selectQuery = "SELECT M.VOUCHER_NUMBER "+
                "FROM SALES_VOUCHER_MASTER M, SALES_VOUCHER_DETAILS D " +
                "WHERE M.VOUCHER_NUMBER = D.VOUCHER_NUMBER AND M.VOUCHER_TYPE = D.VOUCHER_TYPE " +
                "AND M.CUST_NUMBER= \"" + customerNo+"\"";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int vNo;

        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "************************" + selectQuery);
            do {

                vNo = cursor.getInt(0);

                voucherNumbers.add(vNo);

            } while (cursor.moveToNext());
        }
        return voucherNumbers;

    }

    ///B
    public List<String> getItemNoByCategory(String categoryId) {
        List<String> itemNoList = new ArrayList<>();

        String selectQuery = "SELECT I.ItemNo "+
                "FROM ITEMS_MASTER I, SALES_VOUCHER_DETAILS D " +
                "WHERE I.ItemNo = D.ITEM_NUMBER " +
                "AND I.CateogryID = \"" + categoryId +"\"";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String itemNo;

        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "************************" + selectQuery);
            do {

                itemNo = cursor.getString(0);

                itemNoList.add(itemNo);

            } while (cursor.moveToNext());
        }
        return itemNoList;

    }


    public List<Item> getAllItems(int type) {
        List<Item> items = new ArrayList<Item>();
        String selectQuery="";
        // Select All Query
        if(type==1){
             selectQuery = "select D.VOUCHER_NUMBER , D.VOUCHER_TYPE , D.ITEM_NUMBER ,D.ITEM_NAME ," +
                    " D.UNIT ,D.UNIT_QTY , D.UNIT_PRICE ,D.BONUS  ,D.ITEM_DISCOUNT_VALUE ,D.ITEM_DISCOUNT_PERC ," +
                    "D.VOUCHER_DISCOUNT , D.TAX_VALUE , D.TAX_PERCENT , D.COMPANY_NUMBER , D.ITEM_YEAR , D.IS_POSTED , M.VOUCHER_DATE ," +
                    " D.ITEM_DESCRIPTION ,D.SERIAL_CODE , D.WHICH_UNIT    , D.WHICH_UNIT_STR , D.WHICHU_QTY    , D.ENTER_QTY ," +
                    " D.ENTER_PRICE , D.UNIT_BARCODE ,D.ORIGINALVOUCHER_NUMBER " +
                    "from SALES_VOUCHER_DETAILS D , SALES_VOUCHER_MASTER M " +
                    "where D.VOUCHER_NUMBER  = M.VOUCHER_NUMBER and D.VOUCHER_TYPE = M.VOUCHER_TYPE";
        }else {
            selectQuery = "select D.VOUCHER_NUMBER , D.VOUCHER_TYPE , D.ITEM_NUMBER ,D.ITEM_NAME ," +
                    " D.UNIT ,D.UNIT_QTY , D.UNIT_PRICE ,D.BONUS  ,D.ITEM_DISCOUNT_VALUE ,D.ITEM_DISCOUNT_PERC ," +
                    "D.VOUCHER_DISCOUNT , D.TAX_VALUE , D.TAX_PERCENT , D.COMPANY_NUMBER , D.ITEM_YEAR , D.IS_POSTED , M.VOUCHER_DATE ," +
                    " D.ITEM_DESCRIPTION ,D.SERIAL_CODE , D.WHICH_UNIT    , D.WHICH_UNIT_STR , D.WHICHU_QTY    , D.ENTER_QTY ," +
                    " D.ENTER_PRICE , D.UNIT_BARCODE ,D.ORIGINALVOUCHER_NUMBER " +
                    "from SALES_VOUCHER_DETAILS D , SALES_VOUCHER_MASTER M " +
                    "where D.VOUCHER_NUMBER  = M.VOUCHER_NUMBER and D.VOUCHER_TYPE = M.VOUCHER_TYPE and M.IS_POSTED = 0 " +
                    " ORDER BY  D.VOUCHER_NUMBER  ,  D.ITEM_NUMBER , D.TAX_PERCENT DESC ";
            //	ORDER BY  D.VOUCHER_NUMBER  ,  D.ITEM_NUMBER , D.TAX_PERCENT DESC
        }


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
              Log.e("setPrice",""+cursor.getString(5));
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

                item.setWhich_unit(cursor.getString(19));
                item.setWhich_unit_str(cursor.getString(20));
                item.setWhichu_qty(cursor.getString(21));
                item.setEnter_qty(cursor.getString(22));
                item.setEnter_price(cursor.getString(23));
                item.setUnit_barcode(cursor.getString(24));
                item.setORIGINALvoucherNo(cursor.getInt(25));
//                Log.e("setDescreption",""+cursor.getString(17));

                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }
    public List<Item> getAllItemsBYVOCHER(String voucherNo,int VOUCHETYPE) {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        //AND VOUCHER_NUMBER= (SELECT MAX(VOUCHER_TYPE VOUCHER_NUMBER FROM SALES_VOUCHER_DETAILS)
        String selectQuery = "select * FROM SALES_VOUCHER_DETAILS where VOUCHER_TYPE= '"+VOUCHETYPE+"' and VOUCHER_NUMBER='"+voucherNo+"'";
        Log.i("DatabaseHandler", "selectQuery" + selectQuery);
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

                item.setWhich_unit(cursor.getString(19));
                item.setWhich_unit_str(cursor.getString(20));
                item.setWhichu_qty(cursor.getString(21));
                item.setEnter_qty(cursor.getString(22));
                item.setEnter_price(cursor.getString(23));
                item.setUnit_barcode(cursor.getString(24));
//                Log.e("setDescreption",""+cursor.getString(17));

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
        String salesMan =getAllUserNo();
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
    public List<inventoryReportItem> getInventory_db2() {

       List<inventoryReportItem> items_inventory = new ArrayList<>();
       String salesMan =getAllUserNo();
       // Select All Query
       String selectQuery = " select  DISTINCT    M.ItemNo ,M.Name ,S.Qty ,M.CateogryID ,I.UnitID ,I.PriceUnit  from Items_Master M  , SalesMan_Items_Balance S ,   Item_Unit_Details I" +
               "  where M.ItemNo  = S.ItemNo  and  S.SalesManNo = '" + salesMan +"'"+" and M.ItemNo =I.ItemNo";

//       select  DISTINCT    M.ItemNo ,M.Name ,S.Qty ,M.CateogryID ,I.UnitID,I.PriceUnit from Items_Master M   ,  SalesMan_Items_Balance S ,  Item_Unit_Details I
//       where M.ItemNo  = S.ItemNo  and  S.SalesManNo = '1' and M.ItemNo =I.ItemNo
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
             itemsinven.setUNITname(cursor.getString(4));
             itemsinven.setUNITprice(cursor.getString(5));

             items_inventory.add(itemsinven);
          }
          while (cursor.moveToNext());
       }
       return  items_inventory;
    }
    public List<Item> getAllJsonItems(String rate,int baseList ,int countVisibleTable) {// price from price list d
        List<Item> items = new ArrayList<Item>();
        String salesMan="1";
        String selectQuery ="";
        // Select All Query
        if(makeOrders==1)
        {
            salesMan=getAllSettings().get(0).getStoreNo();
        }else {
            salesMan = getAllUserNo();
        }
        Log.e("getAllJsonItems","countVisibleTable="+countVisibleTable);

        String priceListBase="";
//        String cusNo="5";
        if(baseList==0)
        {priceListBase="0";

        }
        else {
            priceListBase=rate;
        }
        String PriceListId = CustomerListShow.PriceListId;
        if(countVisibleTable==0){
//
//            selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO  \n" +
//                    "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P \n" +
//                    "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"'";

            selectQuery="select DISTINCT   M.ItemNo ,M.Name ,M.CateogryID ,M.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO from Price_List_D P ,(\n" +
                    " select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty  ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM,  M.ITEM_HAS_SERIAL , M.ITEM_PHOTO  \n" +
                    "                    from Items_Master M , SalesMan_Items_Balance S \n" +
                    "                    where M.ItemNo  = S.ItemNo   and S.SalesManNo = '"+salesMan+"') as M  where  M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"'";


        }else {
//            selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO , visiItem.AVAILABLITY \n" +
//                    "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P , SalesMan_Item_availability visiItem \n" +
//                    "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"' and visiItem.ITEM_OCODE=M.ItemNo";


       selectQuery="select DISTINCT  J.ItemNo ,J.Name ,J.CateogryID ,J.Qty ,J.Price ,J.TaxPerc ,J.MinSalePrice ,J.Barcode ,J.ITEM_L, J.F_D, J.KIND_ITEM , J.ITEM_HAS_SERIAL , J.ITEM_PHOTO ,visiItem.AVAILABLITY from  SalesMan_Item_availability visiItem  ,(select DISTINCT   M.ItemNo ,M.Name ,M.CateogryID ,M.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO from Price_List_D P ,(\n" +
               " select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty  ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM,  M.ITEM_HAS_SERIAL , M.ITEM_PHOTO  \n" +
               "                    from Items_Master M , SalesMan_Items_Balance S \n" +
               "                    where M.ItemNo  = S.ItemNo   and S.SalesManNo = '"+salesMan+"') as M  where  M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"' ) as J where  visiItem.ITEM_OCODE=J.ItemNo ";



        }



        Log.e("***" , selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            // Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
            do {
                Item item = new Item();
                Bitmap itemBitmap = null;

                item.setItemNo(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                try {
                    String itemQty=cursor.getString(3);
                    Log.e("itemQty","1=="+itemQty);
                    if(itemQty.length()>5){
                        itemQty=itemQty.substring(0,5);
                        Log.e("itemQty","2=="+itemQty);
                    }

                    item.setQty(Float.parseFloat(itemQty+""));
                    Log.e("itemQty","3=="+item.getQty());
                }catch ( Exception e){
                    Log.e("Exceptionitem.setQty",""+e.getMessage());
                    item.setQty(Float.parseFloat(cursor.getString(3)));
                }
                item.setQty(Float.parseFloat(cursor.getString(3)));
                item.setPrice(Float.parseFloat(cursor.getString(4)));
                item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
                item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
//               Log.e("   item,setMinSalePric1=", item.getMinSalePrice()+"");
//               try {
//                  if(getAllSettings().get(0).getItems_Unit()==1)
//                     item.setMinSalePrice(  item.getPrice());
//                  Log.e("   item,setMinSalePric2=", item.getMinSalePrice()+"");
//               }catch (Exception e){
//                  Log.e("minprice,Exception",e.getMessage()+"");
//               }

               item.setBarcode(cursor.getString(7));
                item.setItemL(Double.parseDouble(cursor.getString(8)));

                item.setPosPrice(Double.parseDouble(cursor.getString(9)));
                item.setKind_item(cursor.getString(10));
                try {
                    if (cursor.getString(11) == null) {
                        item.setItemHasSerial("0");
                        Log.e("setItemHasSerial", "" + item.getItemHasSerial() + "null");
                    } else {
                        item.setItemHasSerial(cursor.getString(11));
                    }
                } catch (Exception e) {
                    item.setItemHasSerial("0");
                    Log.e("setItemHasSerial", "" + item.getItemHasSerial() + e.getMessage());

                }


                try {


                    if (cursor.getString(12) == null) {
                        item.setItemPhoto(null);

                    } else {
//                    itemBitmap = StringToBitMap(cursor.getString(13));
//                    item.setItemPhoto(itemBitmap);
                        item.setItemPhoto(cursor.getString(12));
                    }
                } catch (Exception e) {
                    item.setItemPhoto(null);
                }
                if (countVisibleTable == 0) {
                    item.setVivible(0);
                } else {
                try {
                    item.setVivible(cursor.getInt(13));
                } catch (Exception e) {
                    item.setVivible(0);
                }
            }

//                Log.e("setItemHasSerial",""+item.getItemHasSerial()+e.getMessage());


                // Adding transaction to list
                items.add(item);
            } while (cursor.moveToNext());
        }
        Log.e("itemsall", "" +items.size() );
        return items;
    }
//     public List<Item> getAllJsonItems1(String rate,int baseList ,int countVisibleTable) {// price from price list d
//         List<Item> items = new ArrayList<Item>();
//         String salesMan="1";
//         String selectQuery ="";
//         // Select All Query
//         if(makeOrders==1)
//         {
//             salesMan=getAllSettings().get(0).getStoreNo();
//         }else {
//             salesMan = getAllUserNo();
//         }
//         Log.e("getAllJsonItems","countVisibleTable="+countVisibleTable);
//
//         String priceListBase="";
////        String cusNo="5";
//         if(baseList==0)
//         {priceListBase="0";
//
//         }
//         else {
//             priceListBase=rate;
//         }
//         String PriceListId = CustomerListShow.PriceListId;
//         if(countVisibleTable==0){
//             selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO  \n" +
//                     "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P \n" +
//                     "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"' LIMIT 500";
//         }else {
//             selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO , visiItem.AVAILABLITY \n" +
//                     "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P , SalesMan_Item_availability visiItem \n" +
//                     "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"' and visiItem.ITEM_OCODE=M.ItemNo LIMIT 500";
//         }
//
//
//
//         Log.e("***" , selectQuery);
//         db = this.getWritableDatabase();
//         Cursor cursor = db.rawQuery(selectQuery, null);
//
//         // looping through all rows and adding to list
//         if (cursor.moveToFirst()) {
//             // Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
//             do {
//                 Item item = new Item();
//                 Bitmap itemBitmap = null;
//
//                 item.setItemNo(cursor.getString(0));
//                 item.setItemName(cursor.getString(1));
//                 item.setCategory(cursor.getString(2));
//                 item.setQty(Float.parseFloat(cursor.getString(3)));
//                 item.setPrice(Float.parseFloat(cursor.getString(4)));
//                 item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
//                 item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
////               Log.e("   item,setMinSalePric1=", item.getMinSalePrice()+"");
////               try {
////                  if(getAllSettings().get(0).getItems_Unit()==1)
////                     item.setMinSalePrice(  item.getPrice());
////                  Log.e("   item,setMinSalePric2=", item.getMinSalePrice()+"");
////               }catch (Exception e){
////                  Log.e("minprice,Exception",e.getMessage()+"");
////               }
//
//                 item.setBarcode(cursor.getString(7));
//                 item.setItemL(Double.parseDouble(cursor.getString(8)));
//
//                 item.setPosPrice(Double.parseDouble(cursor.getString(9)));
//                 item.setKind_item(cursor.getString(10));
//                 try {
//
//
//                     if (cursor.getString(12) == null) {
//                         item.setItemHasSerial("0");
//                         Log.e("setItemHasSerial", "" + item.getItemHasSerial() + "null");
//                     } else {
//                         item.setItemHasSerial(cursor.getString(12));
//                     }
//                 } catch (Exception e) {
//                     item.setItemHasSerial("0");
//                     Log.e("setItemHasSerial", "" + item.getItemHasSerial() + e.getMessage());
//
//                 }
//
//
//                 try {
//
//
//                     if (cursor.getString(13) == null) {
//                         item.setItemPhoto(null);
//
//                     } else {
////                    itemBitmap = StringToBitMap(cursor.getString(13));
////                    item.setItemPhoto(itemBitmap);
//                         item.setItemPhoto(cursor.getString(13));
//                     }
//                 } catch (Exception e) {
//                     item.setItemPhoto(null);
//                 }
//                 if (countVisibleTable == 0) {
//                     item.setVivible(0);
//                 } else {
//                     try {
//                         item.setVivible(cursor.getInt(14));
//                     } catch (Exception e) {
//                         item.setVivible(0);
//                     }
//                 }
//
////                Log.e("setItemHasSerial",""+item.getItemHasSerial()+e.getMessage());
//
//
//                 // Adding transaction to list
//                 items.add(item);
//             } while (cursor.moveToNext());
//         }
//
//         return items;
//     }
//     public List<Item> getAllJsonItems3(String rate,int baseList ,int countVisibleTable) {// price from price list d
//         List<Item> items = new ArrayList<Item>();
//         String salesMan="1";
//         String selectQuery ="";
//         // Select All Query
//         if(makeOrders==1)
//         {
//             salesMan=getAllSettings().get(0).getStoreNo();
//         }else {
//             salesMan = getAllUserNo();
//         }
//         Log.e("getAllJsonItems","countVisibleTable="+countVisibleTable);
//
//         String priceListBase="";
////        String cusNo="5";
//         if(baseList==0)
//         {priceListBase="0";
//
//         }
//         else {
//             priceListBase=rate;
//         }
//         String PriceListId = CustomerListShow.PriceListId;
//         if(countVisibleTable==0){
//             selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO  \n" +
//                     "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P \n" +
//                     "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"' LIMIT  500 OFFSET 500";
//         }else {
//             selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO , visiItem.AVAILABLITY \n" +
//                     "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P , SalesMan_Item_availability visiItem \n" +
//                     "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+priceListBase+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan +"' and visiItem.ITEM_OCODE=M.ItemNo LIMIT  500 OFFSET 500";
//         }
//
//
//
//         Log.e("***" , selectQuery);
//         db = this.getWritableDatabase();
//         Cursor cursor = db.rawQuery(selectQuery, null);
//
//         // looping through all rows and adding to list
//         if (cursor.moveToFirst()) {
//             // Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
//             do {
//                 Item item = new Item();
//                 Bitmap itemBitmap = null;
//
//                 item.setItemNo(cursor.getString(0));
//                 item.setItemName(cursor.getString(1));
//                 item.setCategory(cursor.getString(2));
//                 item.setQty(Float.parseFloat(cursor.getString(3)));
//                 item.setPrice(Float.parseFloat(cursor.getString(4)));
//                 item.setTaxPercent(Float.parseFloat(cursor.getString(5)));
//                 item.setMinSalePrice(Double.parseDouble(cursor.getString(6)));
////               Log.e("   item,setMinSalePric1=", item.getMinSalePrice()+"");
////               try {
////                  if(getAllSettings().get(0).getItems_Unit()==1)
////                     item.setMinSalePrice(  item.getPrice());
////                  Log.e("   item,setMinSalePric2=", item.getMinSalePrice()+"");
////               }catch (Exception e){
////                  Log.e("minprice,Exception",e.getMessage()+"");
////               }
//
//                 item.setBarcode(cursor.getString(7));
//                 item.setItemL(Double.parseDouble(cursor.getString(8)));
//
//                 item.setPosPrice(Double.parseDouble(cursor.getString(9)));
//                 item.setKind_item(cursor.getString(10));
//                 try {
//
//
//                     if (cursor.getString(12) == null) {
//                         item.setItemHasSerial("0");
//                         Log.e("setItemHasSerial", "" + item.getItemHasSerial() + "null");
//                     } else {
//                         item.setItemHasSerial(cursor.getString(12));
//                     }
//                 } catch (Exception e) {
//                     item.setItemHasSerial("0");
//                     Log.e("setItemHasSerial", "" + item.getItemHasSerial() + e.getMessage());
//
//                 }
//
//
//                 try {
//
//
//                     if (cursor.getString(13) == null) {
//                         item.setItemPhoto(null);
//
//                     } else {
////                    itemBitmap = StringToBitMap(cursor.getString(13));
////                    item.setItemPhoto(itemBitmap);
//                         item.setItemPhoto(cursor.getString(13));
//                     }
//                 } catch (Exception e) {
//                     item.setItemPhoto(null);
//                 }
//                 if (countVisibleTable == 0) {
//                     item.setVivible(0);
//                 } else {
//                     try {
//                         item.setVivible(cursor.getInt(14));
//                     } catch (Exception e) {
//                         item.setVivible(0);
//                     }
//                 }
//
////                Log.e("setItemHasSerial",""+item.getItemHasSerial()+e.getMessage());
//
//
//                 // Adding transaction to list
//                 items.add(item);
//             } while (cursor.moveToNext());
//         }
//
//         return items;
//     }
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
    public List<Item> getAllJsonItems2(String rate)// from customer prices
    {

        Log.e("jsonItemsList","getAllJsonItems2");
        List<Item> items = new ArrayList<>();
        // Select All Query
        String PriceListId = CustomerListShow.PriceListId;
        String priceItem="";
        String custNum = CustomerListShow.Customer_Account;
        String salesMan =getAllUserNo();
        String selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,C.PRICE  ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D,M.KIND_ITEM, cusMaster.ACCPRC ,M.ITEM_HAS_SERIAL , M.ITEM_PHOTO , C.DISCOUNT_CUSTOMER \n" +
                "   from Items_Master M , SalesMan_Items_Balance S , CustomerPrices C ,CUSTOMER_MASTER cusMaster,  Price_List_D P\n" +
                "   where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and M.ItemNo = C.ItemNo_ and P.PrNo ='"+rate+"'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan + "'" +
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
                try {
                    if(!cursor.getString(13).equals("")) {

//                        itemBitmap = StringToBitMap(cursor.getString(13));
//                        item.setItemPhoto(itemBitmap);
                        item.setItemPhoto(cursor.getString(13));
                    }
                    else {
                        item.setItemPhoto(null);
                    }
                }

                catch (Exception e)
                {
                    item.setItemPhoto(null);
                }
                try {
                    item.setDiscountCustomer(cursor.getDouble(14));

                }
                catch (Exception e)
                {
                    item.setDiscountCustomer(0.0);
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

    //************************************Read prices from Admin price List master***********************************************
    public List<Item> getAllItemsPriceFromAdmin(String rate,String typeList,int payMethod,String dateCurent)// from customer prices
    {

        List<Item> items = new ArrayList<>();
        // Select All Query
        String PriceListId = CustomerListShow.PriceListId;
        String priceItem="";
        String custNum = CustomerListShow.Customer_Account;
        String salesMan = getAllUserNo();
        String selectQuery="";
        if(typeList.equals("0")) // regular list
        {
            selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,C.PRICE  ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D,M.KIND_ITEM, cusMaster.ACCPRC ,M.ITEM_HAS_SERIAL , M.ITEM_PHOTO , C.DISCOUNT_CUSTOMER , C.Other_Discount\n" +
                    "   from Items_Master M , SalesMan_Items_Balance S , CustomerPrices C ,CUSTOMER_MASTER cusMaster,  Price_List_D P\n" +
                    "   where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and M.ItemNo = C.ItemNo_ and P.PrNo ='0'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan + "'" +
                    "   and C.CustomerNumber = '" + custNum + "'and ListType='0' and '"+dateCurent+"' BETWEEN C.FromDate and C.ToDate";
            //and ListType='0'
        }
        else {
            selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,C.PRICE  ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D,M.KIND_ITEM, cusMaster.ACCPRC ,M.ITEM_HAS_SERIAL , M.ITEM_PHOTO , C.DISCOUNT_CUSTOMER , C.Other_Discount\n" +
                    "   from Items_Master M , SalesMan_Items_Balance S , CustomerPrices C ,CUSTOMER_MASTER cusMaster,  Price_List_D P\n" +
                    "   where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and M.ItemNo = C.ItemNo_ and P.PrNo ='0'  and cusMaster.ACCPRC = '"+rate+"' and S.SalesManNo = '" + salesMan + "'" +
                    "   and C.CustomerNumber = '" + custNum + "'and ListNo='"+typeList+"'";
        }

//        and ListNo='"+rate+"'

        Log.e("***" , selectQuery);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
      //      Log.i("DatabaseHandler", "***************************************" + cursor.getCount());
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
                   //     Log.e("setItemHasSerial",""+item.getItemHasSerial()+"null");
                    }
                    else {
                        item.setItemHasSerial(cursor.getString(12));
                    }
                }catch (Exception e)
                {
                    item.setItemHasSerial("0");

                   // Log.e("setItemHasSerial",""+item.getItemHasSerial()+e.getMessage());

                }
//                item.setItemPhoto(cursor.getString(13));
                // Adding transaction to list
                try {
                    if(!cursor.getString(13).equals("")) {

//                        itemBitmap = StringToBitMap(cursor.getString(13));
//                        item.setItemPhoto(itemBitmap);
                        item.setItemPhoto(cursor.getString(13));
                    }
                    else {
                        item.setItemPhoto(null);
                    }
                }

                catch (Exception e)
                {
                    item.setItemPhoto(null);
                }
                try {
                    if(payMethod==1)
                    {
                        item.setDiscountCustomer(cursor.getDouble(14));
                    }
                    else {
                        item.setDiscountCustomer(cursor.getDouble(15));
                    }


             //       Log.e("setDiscountCustomer",""+cursor.getDouble(14));
                }
                catch (Exception e)
                {
                    item.setDiscountCustomer(0.0);
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
    //***********************************************************************************
//AYAH2023
    public List<Item> AYAHgetAllJsonItems(String rate,int baseList ,int countVisibleTable ) {
       List<Item> items = new ArrayList<Item>();
       // Select All Query
       String salesMan = getAllUserNo();
       String selectQuery="";
//        String cusNo="5";
       String PriceListId = CustomerListShow.PriceListId;
    //   Log.e("getAllJsonItemsStock","=="+flag);

//             selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC \n" +
//                     "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P\n" +
//                     "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+0+"'  and cusMaster.ACCPRC = '"+0+"' and S.SalesManNo = '" + salesMan +"'";
//
//                 "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+0+"'  and cusMaster.ACCPRC = '"+0+"' and S.SalesManNo = '" + salesMan +"'";


       selectQuery ="select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO \n"+
       "from (SELECT * FROM SalesMan_Items_Balance LIMIT 400 offset 0) AS S \n"
       +"JOIN  Items_Master M ,CUSTOMER_MASTER cusMaster, Price_List_D P \n"
       +"oN M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='0'  and cusMaster.ACCPRC = '0' and S.SalesManNo = '1'";


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
//                if(flag==0||flag==2)
//                {
             item.setQty(0);

//                }
//                else {
//                    item.setQty(Float.parseFloat(cursor.getString(3)));
//
//                }
             item.setCurrentQty(Float.parseFloat(cursor.getString(3)));
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

//SECOND STEP
//
//       {//
//
//          selectQuery ="select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC , M.ITEM_HAS_SERIAL , M.ITEM_PHOTO \n"+
//                  "from (SELECT * FROM SalesMan_Items_Balance LIMIT 1000 offset 400) AS S \n"
//                  +"JOIN  Items_Master M ,CUSTOMER_MASTER cusMaster, Price_List_D P \n"
//                  +"oN M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='0'  and cusMaster.ACCPRC = '0' and S.SalesManNo = '1'";
//
//
//          Log.e("***" , selectQuery);
//          db = this.getWritableDatabase();
//          Cursor cursor2 = db.rawQuery(selectQuery, null);
//
//          // looping through all rows and adding to list
//          if (cursor2.moveToFirst()) {
//             Log.i("DatabaseHandler", "***************************************" + cursor2.getCount());
//             do {
//                Item item = new Item();
//
//                item.setItemNo(cursor2.getString(0));
//                item.setItemName(cursor2.getString(1));
//                item.setCategory(cursor2.getString(2));
////                if(flag==0||flag==2)
////                {
//                item.setQty(0);
//
////                }
////                else {
////                    item.setQty(Float.parseFloat(cursor.getString(3)));
////
////                }
//                item.setCurrentQty(Float.parseFloat(cursor2.getString(3)));
//                item.setPrice(Float.parseFloat(cursor2.getString(4)));
//                item.setTaxPercent(Float.parseFloat(cursor2.getString(5)));
//                item.setMinSalePrice(Double.parseDouble(cursor2.getString(6)));
//                item.setBarcode(cursor2.getString(7));
//                item.setItemL(Double.parseDouble(cursor2.getString(8)));
//
//                item.setPosPrice(Double.parseDouble(cursor2.getString(9)));
//                item.setKind_item(cursor2.getString(10));
//
//                // Adding transaction to list
//                items.add(item);
//             } while (cursor2.moveToNext());
//          }
//
//
//       }//

       return items;
    }

    public List<Item> getAllJsonItemsStock(int flag ) {
        List<Item> items = new ArrayList<Item>();
        // Select All Query
        String salesMan = getAllUserNo();
        String selectQuery="";
//        String cusNo="5";
        String PriceListId = CustomerListShow.PriceListId;
        Log.e("getAllJsonItemsStock","=="+flag);
        if(flag==200)// just items has serial
        {

            selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC \n" +
                    "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P\n" +
                    "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+0+"'  and cusMaster.ACCPRC = '"+0+"' and S.SalesManNo = '" + salesMan +"' and M.ITEM_HAS_SERIAL='"+1+"'";
            //+"' and M.ITEM_HAS_SERIAL='"+1+"'"

        }else {
            if(flag==0){
                selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC \n" +
                        "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P\n" +
                        "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+0+"'  and cusMaster.ACCPRC = '"+0+"' and S.SalesManNo = '" + salesMan +"'";

            }else {
                salesMan=flag+"";
                selectQuery = "select DISTINCT  M.ItemNo ,M.Name ,M.CateogryID ,S.Qty ,P.Price ,P.TaxPerc ,P.MinSalePrice ,M.Barcode ,M.ITEM_L, M.F_D, M.KIND_ITEM, cusMaster.ACCPRC \n" +
                        "                from Items_Master M , SalesMan_Items_Balance S ,CUSTOMER_MASTER cusMaster, Price_List_D P\n" +
                        "                where M.ItemNo  = S.ItemNo and M.ItemNo = P.ItemNo and P.PrNo ='"+0+"'  and cusMaster.ACCPRC = '"+0+"' and S.SalesManNo = '" + salesMan +"'";
            }


        }


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
//                if(flag==0||flag==2)
//                {
                    item.setQty(0);

//                }
//                else {
//                    item.setQty(Float.parseFloat(cursor.getString(3)));
//
//                }
                item.setCurrentQty(Float.parseFloat(cursor.getString(3)));
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
                " select    cast( ItemNo_ as text)"+
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
                "        where  CustomerNumber= '"+custNum+"' and ItemNo_= '"+itemNo+"'";
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

    public String gettaxpercforItem(String itemNo) {

        // Select All Query
        String salesMan = getAllUserNo();
        String TaxPerc="";
        String selectQuery2 ="select  TaxPerc from PRICE_LIST_D where ItemNo = '"+itemNo+"'";

        db = this.getWritableDatabase();
        Cursor cursor_TaxPerc = db.rawQuery(selectQuery2, null);
        if (cursor_TaxPerc.moveToFirst()){
            TaxPerc=cursor_TaxPerc.getString(0);
        }

        cursor_TaxPerc.close();

        return TaxPerc;





//            } while (cursor_price.moveToNext());
//        }*/

    }
    private String getPriceforItem(String itemNo,String rate) {

        // Select All Query
        String salesMan = getAllUserNo();
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

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    if(cursor.getString(0)== "null")
                    { kind_items.add("**");}
                    else {
                        if(!cursor.getString(0).equals(""))
                            kind_items.add(cursor.getString(0));
                        else {
                            kind_items.add("**");
                        }

                        // Log.e("DB_Exception","kind_itemsElse"+cursor.getString(0));
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
        String selectQuery = "select * from   QTY_OFFERS ORDER BY QTY ASC " ;
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
                printerSetting.setShortInvoice(cursor.getInt(2));
                printerSetting.setDontPrintHeader(cursor.getInt(3));
                printerSetting.setTayeeLayout(cursor.getInt(4));
                printerSetting.setNetsalflag(cursor.getInt(5));
                printerSetting.setDontrprintheadeInOrders(cursor.getInt(6));
                printerSetting.setPrintItemNumber(cursor.getInt(7));
                printerSetting.setCurency(cursor.getString(8));
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
    public List<String> getItemsUnits(String itemNo) {
       List<String> units = new ArrayList<>();
      // units.add("1");
       String selectQuery = "select DISTINCT  UnitID  from " + Item_Unit_Details + " where ItemNo = '" + itemNo + "'";

       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       if (cursor.moveToFirst()) {
          do {
             units.add(cursor.getString(0));
          } while (cursor.moveToNext());
       }

       return units;
    }
    public String getItemsUnitsQTY(String itemNo,String unitid) {

       // units.add("1");
       String selectQuery = "select ConvRate  from " + Item_Unit_Details + " where ItemNo = '" + itemNo + "' and UnitID='" + unitid + "' ";

       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       if (cursor.moveToFirst()) {
          do {
           return   cursor.getString(0).toString();
          } while (cursor.moveToNext());
       }

       return "";
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
                payment.setCustNumber(cursor.getString(11));

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
                payment.setCustNumber(cursor.getString(11));


            } while (cursor.moveToNext());
        }

        return payment;
    }

    public List<Payment> getAllPaymentsPaper() {

        List<Payment> paymentsList = new ArrayList<Payment>();
        String selectQuery = "SELECT  * FROM " + PAYMENTS_PAPER ;
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


    public String getAllUserNo() {

        // Select All Query
        String userNO="";

        String selectQuery = "SELECT * FROM " + SalesMenLogIn;

        db = this.getWritableDatabase();///66666666
//       2023-03-02 13:05:00.879 24370-24370/com.dr7.salesmanmanager E/SQLiteDatabase: Failed to open database '/data/user/0/com.dr7.salesmanmanager/databases/VanSalesDatabase'.
//               android.database.sqlite.SQLiteException: Failed to change locale for db '/data/user/0/com.dr7.salesmanmanager/databases/VanSalesDatabase' to 'ar_EG'.
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {

                userNO= cursor.getString(0);


            } while (cursor.moveToNext());
        }
        Log.e("userNO=",userNO);
        return userNO;
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
                item.setCurrentQty(cursor.getDouble(6));
                item.setIsPosted(cursor.getInt(7));

                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public List<InventoryShelf> getAllINVENTORY_SHELF() {
        List<InventoryShelf> items = new ArrayList<InventoryShelf>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + INVENTORY_SHELF +" where IsPosted ='0'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InventoryShelf inventoryShelf=new InventoryShelf();
                inventoryShelf.setTransNo((cursor.getInt(0)));

                inventoryShelf.setITEM_NO((cursor.getString(1))+"");
                inventoryShelf.setSERIAL_NO((cursor.getString(2))+"");

                inventoryShelf.setQTY_ITEM(cursor.getInt(3));
                inventoryShelf.setTRANS_DATE(cursor.getString(4));
                inventoryShelf.setCUSTOMER_NO(cursor.getString(5));
                inventoryShelf.setSALESMAN_NUMBER(cursor.getString(6));
                inventoryShelf.setVoucherNo(cursor.getInt(7));


                items.add(inventoryShelf);
            } while (cursor.moveToNext());
        }

        return items;
    }
    public ArrayList<OfferGroupModel> getAllGroupOffers(String currentDate) {
        //Log.e("getAllGroupOffers","currentDate="+currentDate);
        ArrayList<OfferGroupModel> items = new ArrayList<OfferGroupModel>();
        // Select All Query
//        select * from GroupOffer_Item where '18/10/2021' between From_Date_Offer and To_Date_Offer
//        String selectQuery = "SELECT * FROM " + GroupOffer_Item +" where '"+currentDate+"' between From_Date_Offer and To_Date_Offer";
        String selectQuery = "SELECT * FROM " + GroupOffer_Item ;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OfferGroupModel offer=new OfferGroupModel();
                offer.ItemNo=(cursor.getString(1));
                offer.Name=(cursor.getString(2));
                offer.fromDate=(cursor.getString(3));
                offer.toDate=(cursor.getString(4));
                offer.discount=(cursor.getString(5));
                offer.discountType=(cursor.getInt(6));
                offer.groupIdOffer=(cursor.getInt(7));
                offer.qtyItem=(cursor.getString(8));




                items.add(offer);
            } while (cursor.moveToNext());
        }
        Log.e("getAllGroupOffers",""+items.size());

        return items;
    }
    public List<serialModel> getAllINVENTORY_SHELF_REPORT() {
        List<serialModel> items = new ArrayList<serialModel>();
        // Select All Query
        String selectQuery = "select shelf.ITEM_NO,shelf.SERIAL_NO,shelf.TRANS_DATE,shelf.CUSTOMER_NO,shelf.VOUCHER_NUMBER_INVENTORY,customer.CUS_NAME0 from INVENTORY_SHELF shelf,CUSTOMER_MASTER customer where shelf.CUSTOMER_NO=customer.CUS_ID";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                serialModel inventoryShelf=new serialModel();
                // inventoryShelf.setTransNo((cursor.getInt(0)));

                inventoryShelf.setItemNo((cursor.getString(0))+"");
                inventoryShelf.setSerialCode((cursor.getString(1))+"");

                // inventoryShelf.setQTY_ITEM(cursor.getInt(3));
                inventoryShelf.setDateVoucher(cursor.getString(2));
                inventoryShelf.setCustomerNo(cursor.getString(3));
                // inventoryShelf.setSALESMAN_NUMBER(cursor.getString(6));
                inventoryShelf.setVoucherNo(cursor.getString(4));
                inventoryShelf.setCustomerName(cursor.getString(5));

                items.add(inventoryShelf);
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
    public List<Item> getStockRequestItems_temp() {
        List<Item> items = new ArrayList<Item>();
        // Select All Query

        String selectQuery = "SELECT  detail.ITEM_NUMBER , detail.ITEM_NAME  , detail.UNIT_QTY , master.VOUCHER_DATE," +
                " master.VOUCHER_NUMBER ,master.SALES_MAN_NUMBER" +
                " FROM  REQUEST_DETAILS detail,REQUEST_MASTER master " + "where IS_POSTED=0\n" +
                "        and master.VOUCHER_NUMBER=detail.VOUCHER_NUMBER ";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();

                item.setItemNo(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                try {
                   item.setQty(Float.parseFloat(cursor.getString(2)));
                }
                catch (Exception e){
                   Log.e("getStockRequestItems_",""+cursor.getString(2));
                   item.setQty(0);
                }
                item.setDate(cursor.getString(3));
                item.setVoucherNumber(Integer.parseInt(cursor.getString(4)));
                item.setSalesmanNo(cursor.getString(5));

                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }


    public List<AddedCustomer> getAllAddedCustomer() {
        List<AddedCustomer> customers = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + ADDED_CUSTOMER+ " where IS_POSTED = '" + 0  + "'";

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
                customer.setADRESS_CUSTOMER(cursor.getString(7));
                customer.setTELEPHONE(cursor.getString(8));
                customer.setCONTACT_PERSON(cursor.getString(9));



                customers.add(customer);
            }
           while (cursor.moveToNext());
        }

        return customers;
    }

    public List<Offers> getAllOffers() {
        List<Offers> offers = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + VS_PROMOTION    ;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Offers offer = new Offers();
                offer.setPromotionID(Integer.parseInt(cursor.getString(0)));
                Log.e("offer==",cursor.getString(0)+" "+ offer.getPromotionID());
                offer.setPromotionType(Integer.parseInt(cursor.getString(1)));
                offer.setFromDate(cursor.getString(2));
                offer.setToDate(cursor.getString(3));//*************
                offer.setItemNo(cursor.getString(4));
                offer.setItemQty(Double.parseDouble(cursor.getString(5)));
                offer.setBonusQty(Double.parseDouble(cursor.getString(6)));
                offer.setBonusItemNo(cursor.getString(7));
                offer.setDiscountItemType(cursor.getInt(8));
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
        Log.e("ListItemBalance",""+salesManItemsBalanceList.size());

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


    public void updateTransaction(String cusCode, String currentDate, String currentTime,int count) {
        Log.e("updateTransaction","updateTransaction");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CHECK_OUT_DATE, currentDate);
        values.put(CHECK_OUT_TIME, currentTime);
        values.put(VOUCHERCOUNT, count);
        values.put(STATUS, 1);

        // updating row

        db.update(TABLE_TRANSACTIONS, values, CUS_CODE + "=" + cusCode + " AND " + STATUS + "=" + 0, null);
    }

     public Transaction getlastTransaction() {
         Log.e("getlastTransaction","getlastTransaction");

         Transaction transaction=new Transaction();
         String cusNo = CustomerListShow.Customer_Account;
         String selectQuery = "SELECT * FROM TRANSACTIONS";
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

         if (cursor.moveToLast()) {

             transaction .setCheckInTime(cursor.getString(4) );
             Log.e("setCheckInTime=", "" + cursor.getString(4) + "\t");

         }
         return transaction;
     }
    public void updateTransactionLocationReal(String cusCode, String longitude, String latiud ,String cheackoutTime,String chechInDate) {
        Log.e("updateTransactionLocationReal","updateTransactionLocationReal");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(REAL_LONGTUD, longitude);
        values.put(REAL_LATITUDE, latiud);

        // updating row

        db.update(TABLE_TRANSACTIONS, values, CUS_CODE + "= '" + cusCode + "' AND " + STATUS + "= '" + 1 +"' AND "+ IS_POSTED +"= 0 AND " + CHECK_OUT_TIME +"= '"+cheackoutTime +"' and "+CHECK_IN_DATE +"= '"+chechInDate +"'", null);
    }

    public void updateVoucher() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(SALES_VOUCHER_MASTER, values, IS_POSTED + "=" + 0, null);
    }
    public void updateVoucherByDate(String VOUCHER_NUMBER) {
       Log.e("updateVoucherExported","VOUCHER_DATE="+VOUCHER_NUMBER);
       db = this.getWritableDatabase();
       ContentValues values = new ContentValues();

       values.put(IS_POSTED, 1);
       db.update(SALES_VOUCHER_MASTER, values, IS_POSTED + "=" + 0 + " and VOUCHER_NUMBER = " + VOUCHER_NUMBER, null);
    }
    public void updateRequestStockMaster() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED5, 1);
        db.update(REQUEST_MASTER, values, IS_POSTED5 + "=" + 0, null);
    }
    public void updateRequestStockDetail() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(isPostedDetails, 1);
        db.update(REQUEST_DETAILS, values, isPostedDetails + "=" + 0, null);
    }
    public void updateInventoryShelf() {
        Log.e("updateInventoryShelf","updateInventoryShelf");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IsPosted, 1);
        db.update(INVENTORY_SHELF, values, IsPosted + "=" + 0, null);
    }

    public void updateVoucherDetails() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(SALES_VOUCHER_DETAILS, values, IS_POSTED + "=" + 0, null);
    }
    public void updateVoucherDetailsByDate(String VOUCHER_NUMBER) {
       Log.e("updateVoucherDetailsByDate","VOUCHER_NUMBER="+VOUCHER_NUMBER);
       db = this.getWritableDatabase();
       ContentValues values = new ContentValues();

       values.put(IS_POSTED, 1);
       db.update(SALES_VOUCHER_DETAILS, values, IS_POSTED + "=" + 0+ " and VOUCH_DATE = " +VOUCHER_NUMBER, null);
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

    public void updateAddedCustomers(String cusName) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_POSTED, 1);
        db.update(ADDED_CUSTOMER, values, IS_POSTED + " = 0 and CUST_NAME = '"+cusName+"' "  , null);
    }
     public void New_updateAddedCustomers(String cusName) {
         db = this.getWritableDatabase();
         ContentValues values = new ContentValues();

         values.put(IS_POSTED, 1);
         db.update(ADDED_CUSTOMER, values, IS_POSTED + "=" + 0  , null);
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
               String qtyValue= cursor.getString(0);
               if(qtyValue.length()>5)
                   qtyValue=qtyValue.substring(0,5);
                existQty = Float.parseFloat(qtyValue);
            } while (cursor.moveToNext());
        }
        Log.e("updateSalesManItemsBalance1" , "1existQty"+existQty);

        existQty -= qty;

        Log.e("updateSalesManItemsBalance1" , "2"+existQty);
        db.execSQL("update SalesMan_Items_Balance SET  Qty = '"+existQty+"' where SalesManNo = '"+salesMan+"' and  ItemNo = '"+itemNo+"' ");

    }

    public void updateSalesManItemsBalance2(float qty , int salesMan, String itemNo) {
        db = this.getWritableDatabase();


        String selectQuery = "select Qty from SalesMan_Items_Balance " +
                " where ItemNo = '"+itemNo+"' and SalesManNo = " + salesMan;

        Cursor cursor = db.rawQuery(selectQuery, null);
        float existQty = 0;
        if (cursor.moveToFirst()) {
            do {
                existQty = Float.parseFloat(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        existQty += qty;

        Log.e("updateSalesManItemsBalance222" , ""+existQty);

        ContentValues values = new ContentValues();
        values.put(Qty5, existQty);
        db.update(SalesMan_Items_Balance, values, SalesManNo5 + " = " + salesMan + " and  " + ItemNo5 + " =  " + "'"+itemNo+"'" , null);
    }

    public void deletSerialItems_byVoucherNo(int vouch_no ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SERIAL_ITEMS_TABLE+" where VOUCHER_NO =" +vouch_no);
        db.close();
    }

    public void deletAllSalesLogIn() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SalesMenLogIn);
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
    public void deleteAllItemsSwitch() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Item_Switch);
        db.close();
    }
    public void deleteAllItemsSerialMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SerialItemMaster );
        db.close();
    }
    public void deleteOfferMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + price_offer_list_master );
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
    public void deletePriceListDCustomerRate(String rateCustomer) {
        Log.e("rateCustomer",""+rateCustomer);
        SQLiteDatabase db = this.getWritableDatabase();
        // delete from Price_List_D where PrNo <> 1
        db.execSQL("delete from " + Price_List_D +" where PrNo <> '"+rateCustomer+"'");
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
        Log.e("deleteAllCompanyInfo","deleteAllCompanyInfo");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + COMPANY_INFO);
        db.close();
    }

    public void deleteAllCustomerPrice() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CustomerPrices);
        db.close();
    }

    public void deleteAllCustomerPrice_Current() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CustomerPricesCurrent);
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
//        db.execSQL("delete from " + SALES_VOUCHER_MASTER );
//        db.execSQL("delete from " + SALES_VOUCHER_DETAILS );
//        db.execSQL("delete from " + PAYMENTS );
//        db.execSQL("delete from " + PAYMENTS_PAPER);

        db.execSQL("delete from " + SALES_VOUCHER_MASTER + " where IS_POSTED = '1' ");
        db.execSQL("delete from " + SALES_VOUCHER_DETAILS + " where IS_POSTED = '1' ");
        db.execSQL("delete from " + PAYMENTS + " where IS_POSTED = '1' ");
        db.execSQL("delete from " + PAYMENTS_PAPER + " where IS_POSTED = '1' ");
        db.execSQL("delete from " + SERIAL_ITEMS_TABLE + " where IS_POSTED_SERIAL = '1' ");
        db.close();
    }



    public void deleteAllPostedData_serial(String deleteDate) {
       SQLiteDatabase db = this.getWritableDatabase();

       if(deleteDate.length()==1){
          deleteDate="0"+deleteDate;
       }
       db.execSQL("delete from " + SERIAL_ITEMS_TABLE + " where  DATE_VOUCHER LIKE '%/"+deleteDate.trim()+"/%'");
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
                payment.setCustNumber(cursor.getString(11));
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


    public void updateitemDeletedInSerialTable_Backup( String itemNo,String voucherNo ) {
        db = this.getWritableDatabase();
        String dateTime="";
        dateTime=getCurentTimeDate(1);
        dateTime=dateTime+getCurentTimeDate(2);
        Log.e("updateVOUCHERNO",""+itemNo);
        ContentValues values = new ContentValues();
        values.put(isItemDelete, "1");
        values.put(dateDelete, dateTime);
        if(itemNo.equals(""))
        {
            db.update(SERIAL_ITEMS_TABLE_backup, values, VOUCHER_NO2    + " = '" + voucherNo + "'"   , null);

        }
        else {
            db.update(SERIAL_ITEMS_TABLE_backup, values, VOUCHER_NO2    + " = '" + voucherNo + "' and ITEMNO_SERIAL2='"+itemNo+"'"   , null);

        }


//        values.put(, serialModelItem.getStoreNo());

    }
    public String getCurentTimeDate(int flag){
        String dateCurent,timeCurrent,dateTime="";
        Date currentTimeAndDate;
        SimpleDateFormat dateFormat, timeformat;
        currentTimeAndDate = Calendar.getInstance().getTime();
        if(flag==1)// return date
        {

            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateCurent = dateFormat.format(currentTimeAndDate);
            dateTime=convertToEnglish(dateCurent);

        }
        else {
            if(flag==2)// return time
            {
                timeformat = new SimpleDateFormat("hh:mm:ss");
                dateCurent = timeformat.format(currentTimeAndDate);
                dateTime=convertToEnglish(dateCurent);
            }
        }
        return dateTime;

    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
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
    public boolean isAllposted() {
        int x =0;
        //select count(*) from SALES_VOUCHER_MASTER where IS_POSTED=0;
        String selectQuery = "SELECT count(*) FROM " + SALES_VOUCHER_MASTER + " where  IS_POSTED = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int y = cursor.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y" + y);
            }
        }

        //********************************************************
        selectQuery = "SELECT count(*) FROM " + PAYMENTS + " where  IS_POSTED = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor2 = db.rawQuery(selectQuery, null);
        if (cursor2.moveToFirst()) {
            int y = cursor2.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y2" + y);
            }
        }

        selectQuery = "SELECT count(*) FROM " + ADDED_CUSTOMER + " where  IS_POSTED = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor3 = db.rawQuery(selectQuery, null);
        if (cursor3.moveToFirst()) {
            int y = cursor3.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y3Added" + y);
            }
        }

        selectQuery = "SELECT count(*) FROM " + INVENTORY_SHELF + " where  IsPosted = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor4 = db.rawQuery(selectQuery, null);
        if (cursor4.moveToFirst()) {
            int y = cursor4.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y4Added" + y);
            }
        }


        selectQuery = "SELECT count(*) FROM " + REQUEST_MASTER + " where  IS_POSTED = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor5 = db.rawQuery(selectQuery, null);
        if (cursor5.moveToFirst()) {
            int y = cursor5.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y4Added" + y);
            }
        }
       selectQuery = "SELECT count(*) FROM " + SERIAL_ITEMS_TABLE + " where  IS_POSTED_SERIAL = 0 ";

       db = this.getWritableDatabase();
       Cursor cursor6 = db.rawQuery(selectQuery, null);
       if (cursor6.moveToFirst()) {
          int y = cursor6.getInt(0);
          if (y > 0) {
             x++;
             Log.e("selectQuery", "y3" + y);
          }
       }

        Log.e("selectQuery", "x==" + x);
        if(x>0)
        {
            return false;
        }
        return true;
    }
    public boolean isAllReceptposted() {
        int x =0;
        //select count(*) from SALES_VOUCHER_MASTER where IS_POSTED=0;
        String selectQuery = "SELECT count(*) FROM " + SALES_VOUCHER_MASTER + " where  IS_POSTED = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int y = cursor.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y" + y);
            }
        }

        //********************************************************
        selectQuery = "SELECT count(*) FROM " + PAYMENTS + " where  IS_POSTED = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor2 = db.rawQuery(selectQuery, null);
        if (cursor2.moveToFirst()) {
            int y = cursor2.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y2" + y);
            }
        }
        //*******************************************
        selectQuery = "SELECT count(*) FROM " + SERIAL_ITEMS_TABLE + " where  IS_POSTED_SERIAL = 0 ";

        db = this.getWritableDatabase();
        Cursor cursor3 = db.rawQuery(selectQuery, null);
        if (cursor3.moveToFirst()) {
            int y = cursor3.getInt(0);
            if (y > 0) {
                x++;
                Log.e("selectQuery", "y3" + y);
            }
        }




        Log.e("selectQuery", "x==" + x);
        if(x>0)
        {
            return false;
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
       Log.e("getDiscValueselectQuery==",""+selectQuery);
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

    public String getSalesmanName(String cust) {
        Log.e("getSalesmanName",""+cust);
        String custId=cust+"";
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
    public String getSalesmanName_fromSalesTeam() {
        String name="";
        String salesno=Login.salesMan;
        if(salesno.equals("")) {
           salesno = getAllUserNo();
        }
            String selectQuery ="select S.salesManName \n" +
                    "from Sales_Team S WHERE  S.SalesManNo ='"+salesno+"' ";


            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    name=cursor.getString(0);


                } while (cursor.moveToNext());
            }
//        }



        return  name;

    }

    public String getIpAddresDevice_fromSalesTeam() {
        String name="";
        if(!Login.salesMan.equals(""))
        {
            String selectQuery ="select S.IP_ADDRESS_DEVICE \n" +
                    "from Sales_Team S WHERE  S.SalesManNo ='"+Login.salesMan+"' ";


            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    name=cursor.getString(0);


                } while (cursor.moveToNext());
            }
        }



        return  name;

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
        Log.e("checkVoucherNo=", "voucherNumber+\t" + voucherNumber + "\t"+ "\t"+voucherType);
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
    //  select SERIAL_CODE_NO from SERIAL_ITEMS_TABLE where  SERIAL_CODE_NO='11'
    public String isSerialCodeExist(String serial) {
//  select VOUCHER_NUMBER from SALES_VOUCHER_MASTER WHERE VOUCHER_NUMBER = '147370'
        int addSerialCurrent=0;
        String count = "not", isPaid = "";
        String itemNo = "", itemNoExist = "", serialCode = "";
        if (itemNoSelected.equals("")) {
            itemNo = itemNoStock.trim();
        } else {
            itemNo = itemNoSelected.trim();
        }


        serialCode = serial.trim();
        String salesNo = getAllUserNo();

        String selectQuery = "";
        try {
            addSerialCurrent=addNewSerial;
        }catch (Exception e){
            addSerialCurrent=0;
        }
//    String selectQuery = "select SerialCode from SerialItemMaster where  SerialCode='"+serialCode+"' and StoreNo='"+Login.salesMan+"' and  ITEM_OCODE_M='"+itemNo+"'  ";
        if (addSerialCurrent == 0)
        {
            selectQuery = "select SerialCode,ITEM_OCODE_M from SerialItemMaster where  trim(SerialCode)='" + serialCode.trim() + "' and StoreNo='" + salesNo.trim() + "'";

        }
        else {
            selectQuery = " select serialM.SerialCode,serialM.ITEM_OCODE_M,balance.ItemNo from SerialItemMaster  serialM ,SalesMan_Items_Balance balance" +
                    " where  trim(SerialCode)='" + serialCode.trim() + "' and StoreNo='" + salesNo.trim() + "'  and  balance.ItemNo  =serialM.ITEM_OCODE_M and balance.Qty >0";

        }

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            count = cursor.getString(0);
            itemNoExist = cursor.getString(1);
        }
//        Log.e("isSerialCodeExist", "isSerialCodeExistFrom +SerialItemMaster+\t" + count + "\t"+itemNoExist);
        if(cursor != null)
        {
            cursor.close();
            cursor=null;
            db.close();
        }
        Log.e("1**itemNoExist","="+itemNoExist+"\titemNo="+itemNo+"\tcount="+count);
        if(itemNoExist.toString().trim().equals(itemNo.trim())&&addNewSerial==0 )
        {
            count="not";// valid serial not error
        }
        else {
            try {
                if(addNewSerial==1 && !itemNoExist.equals(""))
                    count="not";// valid serial not error
                else      count=itemNoExist;// exist for another item
            }catch (Exception e){

            }

        }

         Log.e("1**itemNoExist","="+itemNoExist+"\titemNo="+itemNo+"\tcount="+count);
//        if(!count.equals("not"))// exist in DataBase
//        {
//
//                count="not";
//
//        }
//        else {
//            Log.e("itemNoExist",""+itemNoExist);
//            count=itemNoExist;}
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
if(name==null) name = "";

        return name;
    }
    public CompanyInfo getCompanyLocation() {
        CompanyInfo infoLocation=new CompanyInfo();

//        LATITUDE_COMPANY LONGTUDE_COMPANY COMPANY_INFO
        String selectQuery = " select  LONGTUDE_COMPANY , LATITUDE_COMPANY from  COMPANY_INFO ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            infoLocation.setLongtudeCompany( cursor.getDouble(0));
            infoLocation.setLatitudeCompany( cursor.getDouble(1));
            Log.e("setLongtudeCompany", "getItemNameBonus+\t" + infoLocation.getLatitudeCompany() + "\t");

        }


        return infoLocation;
    }
    public Transaction getLastVisitInfo(String customerId,String salesManId) {
        Transaction infoVisit=new Transaction();

        String selectQuery = "select * from TRANSACTIONS WHERE CUS_CODE ='"+customerId+"' and SALES_MAN_ID ='"+salesManId+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToLast()) {
            try {
                if( cursor.moveToPrevious())
                {
                    infoVisit.setCheckInDate( cursor.getString(3));
                    infoVisit.setCheckInTime( cursor.getString(4));
                    Log.e("infoVisit", "infoVisit+\t" + infoVisit.getCheckInDate() + "\t");
                }
            }
            catch ( Exception e)
            {Log.e("infoVisit", "Exception+\t\t");}


        }


        return infoVisit;
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

    public void updatecompanyInfo(double latitudeCheckIn, double longtudeCheckIn) {

        Log.e("updatecompanyInfo",""+latitudeCheckIn+"\t"+LATIT+"\t"+longtudeCheckIn);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LATITUDE_COMPANY, latitudeCheckIn);

        values.put(LONGTUDE_COMPANY, longtudeCheckIn);


        // updating row
        db.update(COMPANY_INFO, values, null, null);

    }

    public String  getItemNoForBarcode(String barcodeValue) {
//        select ITEM_OCODE from Item_Switch where ITEM_NCODE='6008165344933'
        String itemNo="";

        String selectQuery = "select ITEM_OCODE from Item_Switch where ITEM_NCODE ='"+barcodeValue+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {


                itemNo=cursor.getString(0);

                Log.e("itemNo", "getItemNoForBarcode+\t" + itemNo+ "\t");

            }



        }
        catch ( Exception e)
        {itemNo="";
            Log.e("infoVisit", "Exception+\t\t");}
        if(cursor != null)
            cursor.close();

        Log.e("getItemNoForBarcode","itemNo"+itemNo);
        return itemNo;
    }
    //******************************************************************
    public String isSerialCodePaied(String serial) {
        // ***********************************
//        select VOUCHER_NUMBER from SALES_VOUCHER_MASTER WHERE VOUCHER_NUMBER = '147370'
        String valueSer = "not",voucherKind="",voucherNo="",voucherDate="",serialCode="";
        serialCode=serial.trim();
        String selectQuery = "select SERIAL_CODE_NO ,KIND_VOUCHER ,VOUCHER_NO,DATE_VOUCHER from SERIAL_ITEMS_TABLE where  trim(SERIAL_CODE_NO)='"+serialCode+"' ";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                valueSer = cursor.getString(0);
                voucherKind=cursor.getString(1);
                voucherNo=cursor.getString(2);
                voucherDate=cursor.getString(3);

            }
        }
        catch (Exception e){
            valueSer = "not";
        }

        if(!valueSer.equals("not")){// exist in DB
            valueSer=voucherNo+"&"+voucherDate;

        }
        Log.e("isSerialCodePaied", "isSerialCodePaied+\t" + valueSer + "\t");
        if(voucherKind.equals("506"))// returned serial
        {
            valueSer = "not";
        }
        return valueSer;
    }
    //************************************************
    public String getLastTransactionOfSerial(String serial) {
        // ***********************************
//        select VOUCHER_NUMBER from SALES_VOUCHER_MASTER WHERE VOUCHER_NUMBER = '147370'
        String valueSer = "not",voucherKind="",voucherNo="",voucherDate="",serialCode="";
        serialCode=serial.trim();
        String selectQuery = "select SERIAL_CODE_NO ,KIND_VOUCHER ,VOUCHER_NO,DATE_VOUCHER from SERIAL_ITEMS_TABLE where  trim(SERIAL_CODE_NO)='"+serialCode+"' ";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                valueSer = cursor.getString(0);
                voucherKind=cursor.getString(1);
                voucherNo=cursor.getString(2);
                voucherDate=cursor.getString(3);

            }
        }
        catch (Exception e){
            valueSer = "not";
        }


        // Log.e("getLastTransactio", "isSerialCodePaied+\t" + voucherKind + "\t");

        return voucherKind;
    }


    public List<Offers> getAllOffersFromCustomerPrices() {
        List<Offers> offers = new ArrayList<>();
        String customerNo=CustomerListShow.Customer_Account;
        String selectQuery = "select FromDate,ToDate,DISCOUNT_CUSTOMER,Other_Discount,ItemNo_  from CustomerPrices WHERE CustomerNumber='"+customerNo+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Offers offer = new Offers();
                offer.setPromotionID(1);
                offer.setPromotionType(1);
                offer.setItemQty(1);
                offer.setBonusItemNo("");

                offer.setFromDate(cursor.getString(0));
                offer.setToDate(cursor.getString(1));//*************
                offer.setBonusQty(Double.parseDouble(cursor.getString(2)));
                offer.setOtherDiscount(cursor.getString(3));
                Log.e("otherDis",""+offer.getOtherDiscount()+"\t"+offer.getBonusQty());
                if(!cursor.getString(3).equals("0"))
                {
                    double otherDis=Double.parseDouble(offer.getOtherDiscount());
                    Log.e("otherDis1",""+otherDis);
                    offer.setBonusQty(offer.getBonusQty()+otherDis);
                }
                Log.e("otherDis2",""+offer.getBonusQty());
                offer.setItemNo(cursor.getString(4));



                offers.add(offer);
            } while (cursor.moveToNext());
        }


        return offers;
    }

    public String getItemNoForSerial(String barcodeValue) {
        String itemNo="";
        String selectQuery = "select ITEM_OCODE_M from SerialItemMaster where SerialCode ='"+barcodeValue+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                itemNo=cursor.getString(0);

            }
        }
        catch ( Exception e)
        {itemNo="";
           }
        if(cursor != null)
            cursor.close();
        Log.e("getItemNoForSerial","size"+itemNo);

        return itemNo;
    }
    public  String getSolidQtyForItem(String itemNo,String today){
        // SELECT IFNULL( SUM(UNIT_QTY),0) FROM SALES_VOUCHER_DETAILS WHERE ITEM_NUMBER=7022001657 and IS_POSTED='0'
        // String selectQuery = "SELECT IFNULL( SUM(UNIT_QTY),0) FROM SALES_VOUCHER_DETAILS WHERE ITEM_NUMBER='"+itemNo+"' and VOUCHER_TYPE =504 ";

//       Log.e("getSolidQtyForItem","today="+today);
        String soiledQty="";
        //SELECT  IFNULL( SUM(detail.UNIT_QTY),0) FROM SALES_VOUCHER_DETAILS  detail
//        WHERE detail.ITEM_NUMBER='10129' and detail.VOUCHER_TYPE =504 and detail.VOUCHER_NUMBER in( select master.VOUCHER_NUMBER from SALES_VOUCHER_MASTER master  where  master.VOUCHER_DATE='02/09/2021' and master.VOUCHER_TYPE=504 );
        String selectQuery = "SELECT IFNULL( SUM(UNIT_QTY),0) FROM SALES_VOUCHER_DETAILS detail WHERE detail.ITEM_NUMBER='"+itemNo+"' and VOUCHER_TYPE =504 and " +
                "detail.VOUCHER_NUMBER in( select master.VOUCHER_NUMBER from SALES_VOUCHER_MASTER master  where  master.VOUCHER_DATE='"+today+"' and master.VOUCHER_TYPE=504 ) ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                soiledQty=cursor.getString(0);

            }
        }
        catch ( Exception e)
        {soiledQty="0";
        }
        if(cursor != null)
            cursor.close();

        return soiledQty;
    }
    public  String getPriceListNoMaster(String date){
        // select PO_LIST_NO AS PO_LIST_NO from price_offer_list_master where PO_LIST_TYPE ='0' and '26/01/2021' BETWEEN FROM_DATE_master and TO_DATE_master
        String soiledQty="";
        String customerNo=CustomerListShow.Customer_Account;
        //select List_No from customer_prices where  CUSTOMER_NO='1110010002' and '02/02/2021' between FROM_DATE and TO_DATE;
        String selectQuery = "select ListNo  from CustomerPrices where CustomerNumber ='"+customerNo+"' and '"+date+"' BETWEEN FromDate and ToDate";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToLast()) {
                soiledQty=cursor.getString(0);

            }
        }
        catch ( Exception e)
        {Log.e("Exception","getPriceListNoMaster"+e.getMessage());
        }
        if(cursor != null)
            cursor.close();
        Log.e("PO_LIST_NO","getPriceListNoMaster"+soiledQty);

        return soiledQty;
    }
    //******************************************************

    public  ArrayList<OfferListMaster> getPriceOfferActive(String date){
        // select PO_LIST_NAME ,PO_LIST_NO  from price_offer_list_master where PO_LIST_TYPE ='2' and '02/02/2021' between FROM_DATE and TO_DATE;

        //select PO_LIST_NAME,PO_LIST_NO from price_offer_list_master where PO_LIST_NO in( select DISTINCT ListNo from CustomerPrices where ListType=2 and CustomerNumber = 1110010038   and '03/02/2021' BETWEEN FromDate and ToDate)
        String customerNo=CustomerListShow.Customer_Account;
        ArrayList<OfferListMaster> offersList=new ArrayList<>();
        String selectQuery = "select PO_LIST_NAME ,PO_LIST_NO  from price_offer_list_master  where PO_LIST_NO in( select DISTINCT ListNo from CustomerPrices where ListType=2 and CustomerNumber ='"+customerNo+"'   and '"+date+"' BETWEEN FromDate and ToDate)";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    OfferListMaster offers = new OfferListMaster();
                    offers.setPO_LIST_NAME(cursor.getString(0));
                    offers.setPO_LIST_NO(cursor.getInt(1));
                    offersList.add(offers);

                }

                while (cursor.moveToNext());
            }
        }
        catch ( Exception e)
        {Log.e("Exception","getPriceListNoMaster"+e.getMessage());
        }
        if(cursor != null)
            cursor.close();


        return offersList;
    }

    public void updateIpSetting(String ipAddress, String ipPort,String cono) {

        Log.e("updateIpSetting",""+ipAddress+"\t"+ipPort+"\t"+ipPort);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IP_ADDRESS, ipAddress);
        if(!ipPort.equals("")){
            values.put(IP_PORT, ipPort);
            values.put(CONO, cono);

        }


        // updating row
        db.update(TABLE_SETTING, values, null, null);
    }

    public void updateSalesManNameSetting(String name) {

        Log.e("updateSalesManName",""+name);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(salesManName, name);
        // updating row
        db.update(TABLE_SETTING, values, null, null);
    }



    public void updatIpDevice(String ipDevice) {
        Log.e("updateIpSetting",""+ipDevice);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IP_ADDRESS_DEVICE, ipDevice);
        db.update(Sales_Team, values, null, null);
    }

    public void deleteExcept(String salesMan) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from " + ITEMS_QTY_OFFER);
        db.execSQL("DELETE from SerialItemMaster where StoreNo<>"+salesMan);
        db.execSQL("DELETE from SalesMan_Items_Balance where SalesManNo<>"+salesMan);
        db.close();
    }
    public String isTableEmpty(String tableName){
        String isEmpty="";
        String selectQuery = "select count(*) from "+tableName;

        Log.e("isTableEmpty","selectQuery"+selectQuery);
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                isEmpty=cursor.getString(0);

            }
        }
        catch ( Exception e)
        {Log.e("Exception","isSalesManBalanceEmpty"+e.getMessage());
        }
        Log.e("isTableEmpty",""+isEmpty);

        return isEmpty;
    }
    public List<serialModel> getalllserialitems() {
        Log.e("getalllserialitems","getalllserialitems");
        List<serialModel> seriallistList = new ArrayList<>();
        // String Date_Vocher=getCurentTimeDate(1);
        // String Date_Vocher="14/02/2021";
        //       select serial.DATE_VOUCHER,serial.KIND_VOUCHER,serial.SERIAL_CODE_NO,serial.ITEMNO_SERIAL,serial.VOUCHER_NO,master.CUST_NUMBER,master.CUST_NAME  from SERIAL_ITEMS_TABLE serial,SALES_VOUCHER_MASTER master where serial.VOUCHER_NO=master.VOUCHER_NUMBER and serial.KIND_VOUCHER=master.VOUCHER_TYPE";
        String selectQuery = "select serial.DATE_VOUCHER,serial.KIND_VOUCHER,serial.SERIAL_CODE_NO,serial.ITEMNO_SERIAL,serial.VOUCHER_NO,master.CUST_NUMBER,master.CUST_NAME  from SERIAL_ITEMS_TABLE serial,SALES_VOUCHER_MASTER master where serial.VOUCHER_NO = master.VOUCHER_NUMBER and serial.KIND_VOUCHER = master.VOUCHER_TYPE";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor",""+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                serialModel serialModel = new serialModel();

                serialModel.setDateVoucher(cursor.getString(0));
                serialModel.setKindVoucher(cursor.getString(1));//*************
                serialModel.setSerialCode(cursor.getString(2));
                serialModel.setItemNo(cursor.getString(3));
                serialModel.setVoucherNo(cursor.getString(4));
                serialModel.setCustomerNo(cursor.getString(5));
                serialModel.setCustomerName(cursor.getString(6));
                Log.e("serialModel",""+serialModel.getCustomerName());

                seriallistList.add(serialModel);
            } while (cursor.moveToNext());
        }


        return seriallistList;
    }

    public int getmaxSerialInventoryShelf() {

        String selectQuery = "SELECT  MAX(VOUCHER_NUMBER_INVENTORY) FROM " + INVENTORY_SHELF;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) == null) {
                    return 0;
                } else {
                    int maxVoucher = Integer.parseInt(cursor.getString(0));
                    return maxVoucher;
                }
            }
        }catch (Exception e){}

        return 0;
    }

    public String getpreviusePriceSale(String barcode ) {

//select Price_ITEM from  where SERIAL_CODE_NO=355020112920508
        Log.e("getpreviusePriceSale","barcode"+barcode);
        String selectQuery = "SELECT  Price_ITEM_Sales FROM SERIAL_ITEMS_TABLE where SERIAL_CODE_NO= '"+barcode+"'";
        String price="0";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) == null) {
                    return "0";
                } else {
                    price = (cursor.getString(0));
                    Log.e("cursor.getDouble","pr="+price);
                    return price;
                }
            }
        }catch (Exception e){}
        return price;


    }

    public String getCustomerForSerial(String barcodeSerial) {
        Log.e("getpreviusePriceSale","barcodeSerial="+barcodeSerial);
        //select VOUCHER_NO,CUST_NAME from SERIAL_ITEMS_TABLE serial,SALES_VOUCHER_MASTER  master where serial.SERIAL_CODE_NO='GPSZ2020082100556' and master.VOUCHER_NUMBER=serial.VOUCHER_NO
        String selectQuery = "SELECT  CUST_NAME FROM SERIAL_ITEMS_TABLE serial,SALES_VOUCHER_MASTER  master where serial.SERIAL_CODE_NO='"+barcodeSerial+"' and master.VOUCHER_NUMBER=serial.VOUCHER_NO and master.VOUCHER_TYPE=504";
        String CUST_NAME="0";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) == null) {
                    return "nocustomer";
                } else {
                    CUST_NAME = (cursor.getString(0));
                    Log.e("getCustomerForSerial","="+CUST_NAME);
                    return CUST_NAME;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getCustomerForSerial"+e.getMessage());
        }



        return "nocustomer";




    }

    public int getPayTypeForVoucher(String barcodeStr) {
        // Log.e("getpreviusePriceSale","getPayTypeForVoucher="+barcodeStr);
        // select PAY_METHOD from SERIAL_ITEMS_TABLE serial , SALES_VOUCHER_MASTER master where serial.SERIAL_CODE_NO='355020113127244' and master.VOUCHER_NUMBER=serial.VOUCHER_NO
        String selectQuery = "SELECT  PAY_METHOD FROM SERIAL_ITEMS_TABLE serial , SALES_VOUCHER_MASTER master  where serial.SERIAL_CODE_NO='"+barcodeStr.trim()+"' and master.VOUCHER_NUMBER=serial.VOUCHER_NO";
        int payMethod=0;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) == null) {
                    return 1;
                } else {
                    payMethod = (cursor.getInt(0));
                    Log.e("getPayTypeForVoucher","="+payMethod);
                    return payMethod;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getCustomerForSerial"+e.getMessage());
        }



        return payMethod;

    }

    public int getUnitForItem(String itemNumber) {
        String selectQuery = "select ConvRate from Item_Unit_Details where ItemNo='"+itemNumber.trim()+"'";
        int itemUnit=1;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                if (cursor.getString(0) == null) {
                    return 1;
                } else {
                    itemUnit = (cursor.getInt(0));
                   // Log.e("getUnitForItem","="+itemUnit);
                    return itemUnit;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  itemUnit;
    }

    public String getUnitPrice(String itemNo,String rate,float countItems) {
//         Log.e("getUnitPrice","itemNo"+itemNo+"\tcountItems="+countItems);
        String selectQuery="";
//        try {
//            DecimalFormat decimalFormat = new DecimalFormat("00.000");
//        countItems=Math.round(Float.parseFloat(countItems+""));
//        Log.e("getUnitPrice","itemNo"+itemNo+"\tcountItems="+countItems);
//            countItems= Double.parseDouble(convertToEnglish(decimalFormat.format(countItems)));
//        }catch (Exception exception){
//
//        }


        switch (rate){
            case "0":
                selectQuery = "select PriceUnit from Item_Unit_Details where ItemNo='"+itemNo.trim()+"'";
                break;
            case "1":
                selectQuery = "select PRICECLASS_1 from Item_Unit_Details where ItemNo='"+itemNo.trim()+"'";

                break;
            case "2": selectQuery = "select PRICECLASS_2 from Item_Unit_Details where ItemNo='"+itemNo.trim()+"'";

                break;
            case "3":   selectQuery = "select PRICECLASS_3 from Item_Unit_Details where ItemNo='"+itemNo.trim()+"'";

                break;

           case "-1":   selectQuery = "select PriceUnit from Item_Unit_Details where ItemNo='"+itemNo.trim()+"'  and  CAST(ConvRate as float)='"+countItems+"'";

              break;
            case "-2":
                String customerNo=CustomerListShow.Customer_Account;
                String discountType=getDiscountType();
                selectQuery = " select Price from CustomerPrices  where ItemNo_='"+itemNo.trim()+"' and  CustomerNumber='"+customerNo+"' ";
//                if(discountType.equals("1"))
//                {
//                 selectQuery = " select Price - (Price * (DISCOUNT_CUSTOMER/100)) from CustomerPrices  where ItemNo_='"+itemNo.trim()+"' and  CustomerNumber='"+customerNo+"' ";
//                }else {
//                 selectQuery = " select Price - DISCOUNT_CUSTOMER from CustomerPrices  where ItemNo_='"+itemNo.trim()+"' and  CustomerNumber='"+customerNo+"' ";
//                }
                break;
            case "-3":
                String customer_No=CustomerListShow.Customer_Account;
                selectQuery = "select DISCOUNT_CUSTOMER from CustomerPrices  where ItemNo_='"+itemNo.trim()+"' and  CustomerNumber='"+customer_No+"' ";
                break;
        }

         Log.e("selectQuery","itemNo"+selectQuery);
        String itemUnit="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                if (cursor.getString(0) == null) {
                    return "";
                } else {
                    itemUnit = (cursor.getString(0));
                    Log.e("getUnitForItem","price="+itemUnit);
                    return itemUnit;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        Log.e("itemUnit","price=="+itemUnit);
        return  itemUnit;
    }

     private String getDiscountType() {
        return "1";
     }

     public ItemUnitDetails getItemUnitDetails(String itemNumber,float countItem) {
       //and ConvRate=100
       String selectQuery="";
       if(countItem!=-1)
        selectQuery = "select * from Item_Unit_Details where ItemNo='"+itemNumber.trim()+"' and ConvRate='"+countItem+"'";
       else   selectQuery = "select * from Item_Unit_Details where ItemNo='"+itemNumber.trim()+"'";
        Log.e("selectQuery===",selectQuery);
        ItemUnitDetails itemUnit=new ItemUnitDetails();
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    itemUnit.setItemNo(cursor.getString(1));
                    itemUnit.setUnitId(cursor.getString(2));
                    itemUnit.setConvRate(Double.parseDouble(cursor.getString(3)));
                    itemUnit.setUnitPrice(cursor.getString(4));
                    itemUnit.setItemBarcode(cursor.getString(5));
//                    Log.e("getUnitForItem","price="+itemUnit);
//                    Log.e("getUnitForItem","getConvRate="+    itemUnit.getConvRate());
//                    Log.e("getUnitForItem","getUnitId="+    itemUnit.getUnitId());
//                    Log.e("getUnitForItem","price="+    itemUnit.getUnitPrice());
                }while (cursor.moveToNext());

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  itemUnit;
    }

    public void addPriceCurrent(String cusCode) {
        deleteAllCustomerPrice();
        List<CustomerPrice> listCustomer=new ArrayList<>();
//        select * from CustomerPrices where CustomerNumber='1110010376'
        String selectQuery = "select * from CustomerPricesCurrent where CustomerNumber_ ='"+cusCode.trim()+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    CustomerPrice price = new CustomerPrice();
                    price.setItemNumber(cursor.getString(0));
                    price.setCustomerNumber(cursor.getInt(1));
                    price.setPrice(cursor.getDouble(2));
                    price.setDiscount(cursor.getDouble(3));
                    listCustomer.add(price);


                    Log.e("getUnitForItem","price="+listCustomer.size());

                }while (cursor.moveToNext());

            }
            if(listCustomer.size()!=0){
                addCustomerPrice(listCustomer);
            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        // return  listCustomer;

    }

    public void deletOfferGroup() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + GroupOffer_Item );
        db.close();
    }
    public ArrayList<MainGroup_Id_Count> getMainGroup_Id_Count(String currentDate) {

//        +" where '"+currentDate+"' between From_Date_Offer and To_Date_Offer"
        String selectQuery = "select GroupId_Offer, count (GroupId_Offer) as GroupCount from GroupOffer_Item";

        db = this.getWritableDatabase();
        ArrayList<MainGroup_Id_Count> listAllGroup=new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    MainGroup_Id_Count itemUnit=new MainGroup_Id_Count();
                    itemUnit.idGroup=(cursor.getInt(0));
                    itemUnit.countGroup=(cursor.getInt(1));
                    listAllGroup.add(itemUnit);

                    Log.e("getMainGroup_Id_Count","listAllGroup="+listAllGroup.size());

                }while (cursor.moveToNext());

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  listAllGroup;
    }

    public String getPriceItem_forUser(String itemNo, String rate_customer) {
        //select DISTINCT  P.Price
        //  from  Price_List_D P where ItemNo = '0002154' and PrNo=1
        return  "";
    }

    public String getItemName(String itemNo) {
        Log.e("getItemName","getItemName="+itemNo);
        String selectQuery = "select Name from Items_Master where ItemNo='"+itemNo.trim()+"'";
        String itemUnit="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                if (cursor.getString(0) == null) {
                    return "";
                } else {
                    itemUnit = (cursor.getString(0));
                    Log.e("getItemName","getItemName="+itemUnit);
                    return itemUnit;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  itemUnit;
    }

    public String getVoucherNoFromSerialTable(String srialCode) {
        Log.e("getItemName","getItemName="+srialCode);
        String selectQuery = "select VOUCHER_NO , KIND_VOUCHER   from SERIAL_ITEMS_TABLE where SERIAL_CODE_NO='"+srialCode.trim()+"' ";
        String voucherNo="",vouchKind="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                if (cursor.getString(0) == null) {
                    voucherNo= "NotFound";
                } else {
                    voucherNo = (cursor.getString(0));
                    vouchKind=cursor.getString(1);
                    //  Log.e("getVoucherNoFromS","vouchKind="+vouchKind);


                }

            }else {
                voucherNo= "NotFound";
            }
        }
        catch ( Exception e)
        {
            // Log.e("Exception","getUnitForItem"+e.getMessage());
            voucherNo= "NotFound";
        }
        if(vouchKind.equals("506"))
        {
            voucherNo="returned";
        }
        // Log.e("getVoucherNoFromSer","result="+voucherNo);
        return voucherNo;
    }

    public void updateSerialReturnedInBaseInvoice(String voucherNo, String serialCode) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_RETURNED, "1");



        // updating row
        db.update(SERIAL_ITEMS_TABLE, values, VOUCHER_NO + "=" + voucherNo +" and "+ SERIAL_CODE_NO + " = '" + serialCode.trim()+"'", null);
    }


    public int updateItemReturnedInVocherDetails(String voucherNo, String itemCode) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_RETURNED, "1");



        // updating row
        int x=   db.update(SALES_VOUCHER_DETAILS, values, VOUCHER_NUMBER + "=" + voucherNo +" and "+ ITEM_NUMBER + " = '" + itemCode.trim()+"'", null);
        return x; }

    public int  UpdateAvi_QtyInOrigenalVoch(String voucherNo, float newqty,String itemCode,int flagBonus){
        db = this.getWritableDatabase();


        float oldqty= getoldqty(itemCode,Integer.parseInt(voucherNo),flagBonus);
        float endqty =oldqty-newqty;
//
//        Log.e("oldqty==", oldqty+"");
//        Log.e("endqty==", endqty+"");
//        Log.e("newqty==", newqty+"");
        ContentValues values = new ContentValues();
        values.put("Avilable_Qty", endqty);
        // updating row

       int x=0;Log.e("1flagBonus==", flagBonus+"  "+endqty);
        if(flagBonus==0)
        {
           x=   db.update(SALES_VOUCHER_DETAILS, values, VOUCHER_NUMBER + "=" + voucherNo+" and "+ ITEM_NUMBER + " = '" + itemCode.trim()+"'"+" and "+ VOUCHER_TYPE + " = '504' and ITEM_NAME<>'(bonus)'", null);

        }
      else  x=   db.update(SALES_VOUCHER_DETAILS, values, VOUCHER_NUMBER + "=" + voucherNo+" and "+ ITEM_NUMBER + " = '" + itemCode.trim()+"'"+" and "+ VOUCHER_TYPE + " = '504' and ITEM_NAME='(bonus)'", null);
       Log.e("x==", x+"");

        return x;

    }
    public float getoldqty(String itemNo,int voucherNo,int flagBonus) {
        Log.e("getItemName","getItemName="+itemNo);
        String customerNo=CustomerListShow.Customer_Account;
       String selectQuery="";
        if(flagBonus==0)
         selectQuery = " select Avilable_Qty from SALES_VOUCHER_DETAILS  where ITEM_NUMBER='"+itemNo.trim()+"' and VOUCHER_NUMBER  = '" + voucherNo+ "' and VOUCHER_TYPE='504'";
        else  selectQuery = " select Avilable_Qty from SALES_VOUCHER_DETAILS  where ITEM_NUMBER='"+itemNo.trim()+"' and VOUCHER_NUMBER  = '" + voucherNo+ "' and VOUCHER_TYPE='504' and ITEM_NAME='(bonus)'";


        float itemUnit=0;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) == null) {
                    return 0;
                } else {
                    itemUnit = Float.parseFloat(cursor.getString(0));
                    if(itemUnit==0)
                        if(itemUnit==0)
                            itemUnit= getoldqtyMain(itemNo,voucherNo,flagBonus);
                    return itemUnit;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }


        return  itemUnit;
    }
    public float getoldqtyMain(String itemNo,int voucherNo,int flag) {
        Log.e("getoldqtyMain","getItemName="+itemNo);
        String customerNo=CustomerListShow.Customer_Account;
       String selectQuery="";
       if(flag==0){
            selectQuery = " select UNIT_QTY from SALES_VOUCHER_DETAILS  where ITEM_NUMBER='"+itemNo.trim()+"' and VOUCHER_NUMBER  = '" + voucherNo+ "' and VOUCHER_TYPE='504'";

        }else {
          selectQuery = " select UNIT_QTY from SALES_VOUCHER_DETAILS  where ITEM_NUMBER='"+itemNo.trim()+"' and VOUCHER_NUMBER  = '" + voucherNo+ "' and VOUCHER_TYPE='504'  and ITEM_NAME='(bonus)'";

       }
        float itemUnit=0;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) == null) {
                    return 0;
                } else {
                    itemUnit = Float.parseFloat(cursor.getString(0));
                    Log.e("getoldqtyMainitemUnit==", itemUnit+"");

                    return itemUnit;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }




        return  itemUnit;
    }

    public int HASSERAIAL(String itemCode) {
        int x=0;
        /*db = this.getWritableDatabase();
        String selectQuery = "SELECT ITEM_HAS_SERIAL FROM Items_Master WHERE ItemNo="+"'"+itemCode+"'";
        db.execSQL(selectQuery)*/




        String selectQuery = "SELECT ITEM_HAS_SERIAL FROM Items_Master WHERE ItemNo="+"'"+itemCode+"'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                if (cursor.getString(0) == null) {
                    return 0;
                } else {
                    x= Integer.parseInt(cursor.getString(0));
                    Log.e("x","HASSERAIAL="+x);
                    return x;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }

        return  x;

    }


    //    select Price from CustomerPrices where CustomerNumber='1110000002' and ItemNo_='30001826'
    public String getItemPrice(String itemNo) {
        Log.e("getItemName","getItemName="+itemNo);
        String customerNo=CustomerListShow.Customer_Account;
        String selectQuery = " select Price from CustomerPrices  where ItemNo_='"+itemNo.trim()+"' and  CustomerNumber='"+customerNo+"' ";
        String itemUnit="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                if (cursor.getString(0) == null) {
                    return "";
                } else {
                    itemUnit = (cursor.getString(0));
                    Log.e("getItemPrice","getItemPrice="+itemUnit);
                    return itemUnit;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  itemUnit;
    }
    public String getOriginalItemPrice(String itemNo) {
        Log.e("getItemName","getItemName="+itemNo);

        String selectQuery = " select F_D from Items_Master  where ItemNo='"+itemNo.trim()+"' ";
        String itemPrice="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToNext()) {
                if (cursor.getString(0) == null) {
                    return "";
                } else {
                    itemPrice = (cursor.getString(0));
                    Log.e("getItemPrice","getItemPrice="+itemPrice);
                    return itemPrice;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  itemPrice;
    }
    public ArrayList<ItemsReturn >getItemsReturn() {
        ArrayList<ItemsReturn > allItemReturn=new ArrayList<>();


        String selectQuery = " select VOUCHER_NUMBER , ITEM_NUMBER from SALES_VOUCHER_DETAILS where  VOUCHER_TYPE='506' and IS_POSTED='0'";
        String itemUnit="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToNext()) {
                if (cursor.getString(0) == null) {

                } else {
                    ItemsReturn  item =new ItemsReturn();
                    item.setVouycherNo(cursor.getString(0));
                    item.setItemCode( cursor.getString(1));
                    allItemReturn.add(item);
                    Log.e("getItemPrice","getItemPrice="+allItemReturn.size());

                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  allItemReturn;
    }
    public void addSalesmanPlan(SalesManPlan salesManPlans) {

        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(SALES_MAN_NUMBER, salesManPlans.getSaleManNumber());
        values.put(DATE, salesManPlans.getDate());
        values.put(LATITUDE,salesManPlans.getLatitud());
        values.put(LONGITUDE,salesManPlans.getLongtude());
        values.put(CUSTOMER_NAME, salesManPlans.getCustName());
        values.put(CUSTOMER_NO, salesManPlans.getCustNumber());
        values.put(orederd,salesManPlans.getOrder());
        values.put(typeorederd,salesManPlans.getTypeOrder());
        values.put(LogoutStatus,salesManPlans.getLogoutStatus());

        db.insert(SalesMan_Plan, null, values);
        db.close();
    }
    public ArrayList< SalesManPlan >getSalesmanPlan(String date) {
        ArrayList< SalesManPlan > SalesManPlanlist=new ArrayList<>();

        Log.e("date==", date+"");
        String selectQuery = " select * from SalesMan_Plan where DATE= '"+convertToEnglish(date)+"'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            Log.e("getSalesmanPlan1==", "getSalesmanPlan");

            if (cursor.moveToFirst()) {
                do {
                    Log.e("getSalesmanPlan4==", "getSalesmanPlan");

                    if (cursor.getString(0) == null) {
                        Log.e("getSalesmanPlan2==", "getSalesmanPlan");
                    } else {
                        Log.e("getSalesmanPlan3==", "getSalesmanPlan");
                        SalesManPlan plan = new SalesManPlan();
                        plan.setDate(cursor.getString(0));
                        plan.setSaleManNumber(cursor.getInt(1));
                        plan.setCustName(cursor.getString(2));
                        plan.setCustNumber(cursor.getString(3));
                        plan.setLatitud(cursor.getDouble(4));
                        plan.setLongtude(cursor.getDouble(5));
                        plan.setOrder(cursor.getInt(6));
                        plan.setTypeOrder(cursor.getInt(7));
                        plan.setLogoutStatus(cursor.getInt(8));


                        SalesManPlanlist.add(plan);
                        Log.e("SalesManPlanlist", "SalesManPlanlist=" + SalesManPlanlist.size());

                    }

                } while (cursor.moveToNext());
            }
        }

        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }


        //

        //
        return   SalesManPlanlist;
    }

    public void updateLogStatusInPlan(String cusCode,String currentDate) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LogoutStatus,1);


        // updating row
        Log.e("currentDate",currentDate);
        int y=   db.update(SalesMan_Plan, values, CUSTOMER_NO + "=" + cusCode + " AND " + LogoutStatus + "=" + 0+ " AND DATE="  +"'"+currentDate+"'", null);
        Log.e("y===",""+y);

    }
    public void deleteFromSalesMan_Plan(String currentDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SalesMan_Plan+" where DATE='"+currentDate+"'");
        db.close();
    }
    public void deleteFromItemVisiblty() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from   SalesMan_Item_availability ");
        db.close();
    }


    public   ArrayList<AddedCustomer> getAllCustomer() {
        ArrayList<AddedCustomer> customers = new ArrayList<>();
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
                customer.setADRESS_CUSTOMER(cursor.getString(7));
                customer.setTELEPHONE(cursor.getString(8));
                customer.setCONTACT_PERSON(cursor.getString(9));



                customers.add(customer);
            } while (cursor.moveToNext());
        }

        return customers;
    }

    public void updateSettingOnlyCustomer(int i) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SALESMAN_CUSTOMERS, i);
        db.update(TABLE_SETTING, values, null, null);
        Log.e("TABLE_SETTING", "UPDATE");
        db.close();
    }
    public void setSalsemanLocation(String lat,String Longat){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LATITUDE,  lat);
        values.put(LONGITUDE, Longat);
        db.update(SalesMenLogIn, values, null, null);
        Log.e("SalesMenLogIn", "UPDATE");
        db.close();
    }

    public void getSalsmanLoc(){
        String selectQuery = "select * from SalesMenLogIn";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    SalmnLat=(cursor.getString(1));
                    SalmnLong=(cursor.getString(2));




                }while (cursor.moveToNext());

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
    }

    ////B
    public String getLastVoucherDate() {

        String lastDate = "";

        String selectQuery = "SELECT VOUCHER_DATE FROM SALES_VOUCHER_MASTER";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToLast()) {
            try {
             //   if( cursor.moveToPrevious())
                {
                    lastDate =  cursor.getString(0);

                    Log.e("Last_Voucher_Date", lastDate+"");
                }
            } catch ( Exception e)
            {
                Log.e("Last_Voucher_Date", "Exception+\t\t");
            }
        }


        return lastDate;

    }

    public void addSMitemAvilablity(List<SMItemAvailability> item) {

        db = this.getReadableDatabase();
        db.beginTransaction();

        for (int i = 0; i < item.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(SALESMAN_NO, item.get(i).getSalManNo());
            values.put(ITEM_OCODE, item.get(i).getItemOcode());
            values.put(availability, item.get(i).getAvailability());
            db.insertWithOnConflict(SalesMan_Item_availability, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public List<SMItemAvailability> getSMitemAvilablity() {

        ArrayList<SMItemAvailability> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + SalesMan_Item_availability;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SMItemAvailability item= new SMItemAvailability();

                item.setSalManNo(cursor.getInt(0));
                item.setItemOcode(cursor.getString(1));
                item.setAvailability(cursor.getInt(2));



                arrayList.add(item);
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public int isExist(String name) {
         int count=0;

        String selectQuery = "select count(*) CUST_NAME from ADDED_CUSTOMER  where trim(CUST_NAME)=trim('"+name+"');";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            try {
                count =  cursor.getInt(0);

            } catch ( Exception e)
            {
            }
        }


        return count;
    }

    public int getItemsReturndDetails(int VOHNO) {
        Log.e("getItemsReturnd==", "getItemsReturndDetails"+"\t\t");
        int vocherNum;
        ArrayList<Integer>arrayList=new ArrayList<>();

        String selectQuery = "SELECT * FROM  SALES_VOUCHER_DETAILS where VOUCHER_NUMBER='"+VOHNO +"' AND VOUCHER_TYPE='504'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.getCount()+"\t\t");


        if(cursor.getCount()==0)

            return 0;

        else
            return 1;
      /*  if (cursor.moveToFirst()) {
            try {

             do   {
                    Log.e("getItemsReturndDetails==item", "item"+"\t\t");

                    Item item=new Item();
                    item.setItemNo(cursor.getString(0));
                    item.setAvi_Qty(cursor.getFloat(1));
                    item.setIS_RETURNED(cursor.getInt(2));
                    item.setVoucherNumber(cursor.getInt(3));
                    ReturnItemsarrayList.add( item);

            } while (cursor.moveToNext());
            } catch ( Exception e)
            {
                Log.e("getItemsReturndDetails", "Exception+\t\t");
            }
        }*/

  /*      Log.e("getItemsReturndDetails44===",ReturnItemsarrayList.size()+"   "+ImportJason.CopyForServerVocger_listItemsReturn.size()+ "+\t\t");

        if( ReturnItemsarrayList.size()==0){ //this when orignal vocher in server not in local


            {
                Log.e("c",ReturnItemsarrayList.size()+"  if "+ "+\t\t");

                Item item=new Item();
                item.setItemNo();
                item.setAvi_Qty();
                item.setIS_RETURNED();
                item.setVoucherNumber();
                ReturnItemsarrayList.add( item);
            }
*/

  /*  for(int i=0; i<ImportJason.CopyForServerVocger_listItemsReturn.size();i++)
      //    if(VOHNO==ImportJason.CopyForServerVocger_listItemsReturn.get(i).getVoucherNumber())
    {
        Log.e("c",ReturnItemsarrayList.size()+"  if "+ "+\t\t");

        Item item=new Item();
        item.setItemNo(ImportJason.CopyForServerVocger_listItemsReturn.get(i).getItemNo());
        item.setAvi_Qty(ImportJason.CopyForServerVocger_listItemsReturn.get(i).getAvi_Qty());
        item.setIS_RETURNED(1);
        item.setVoucherNumber(ImportJason.CopyForServerVocger_listItemsReturn.get(i).getVoucherNumber());
        ReturnItemsarrayList.add( item);
    }
}*/
}

    public void getvocherNumforItemsReturnd() {
        ArrayList<Item> items = new ArrayList<Item>();
        // Select All Query                                                                                                          //AND IS_RETURNED = '0'
        String selectQuery = "SELECT  * FROM  SALES_VOUCHER_DETAILS where IS_POSTED= '0' and  VOUCHER_TYPE='506'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor2==", cursor.getCount()+"\t\t");


        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "************************" + selectQuery);
            do {
                Item item = new Item();
                Log.e("VoucherNumber==",  item.getVoucherNumber()+"\t\t");
                item.setVoucherNumber(Integer.parseInt(cursor.getString(0)));
                item.setVoucherType(Integer.parseInt(cursor.getString(1)));
                item.setItemNo(cursor.getString(2));
                // Log.e("cursorItemNo",""+cursor.getString(2));
                item.setItemName(cursor.getString(3));
                item.setQty(Float.parseFloat(cursor.getString(5)));
                item.setIS_RETURNED(cursor.getInt(25));
                item.setAvi_Qty(Float.parseFloat(cursor.getString(26)));
                item.setORIGINALvoucherNo(cursor.getInt(27));



                    items.add(item);


            } while (cursor.moveToNext());
        }


        for(int i=0;i<items.size();i++)
   if( getItemsReturndDetails(items.get(i).getORIGINALvoucherNo())==0)   {
       Log.i("DatabaseHandler==", "DatabaseHandler");




       Item item = new Item();

       item.setVoucherNumber(items.get(i).getVoucherNumber());
       item.setVoucherType(items.get(i).getVoucherType());
       item.setItemNo(items.get(i).getItemNo());
       // Log.e("cursorItemNo",""+cursor.getString(2));
       item.setItemName(items.get(i).getItemName());
       item.setQty(items.get(i).getQty());
       item.setIS_RETURNED(items.get(i).getIS_RETURNED());
       item.setQty(items.get(i).getQty());
       item.setORIGINALvoucherNo(items.get(i).getORIGINALvoucherNo());

       ReturnItemsarrayList.add( item);

   }
    }

    public int getCountVisibleItemSize() {
        int count=0;

        String selectQuery = "select count(*)  from SalesMan_Item_availability ";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            try {

                count =  cursor.getInt(0);

                Log.e("count", count+"\tgetCountVisibleItemSize");

            } catch ( Exception e)
            {
            }
        }
        return count;
    }
    public void deleteAllPreviusYear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" delete from SALES_VOUCHER_MASTER where VOUCHER_DATE LIKE '%2022%'");
        db.execSQL("delete from SALES_VOUCHER_DETAILS where ITEM_YEAR LIKE '%2022%'");
        db.execSQL(" delete from TRANSACTIONS where CHECK_IN_DATE LIKE '%2022%'");
        db.execSQL(" delete from SERIAL_ITEMS_TABLE where DATE_VOUCHER LIKE '%2022%'");
        db.close();
    }

    public int getCountPlan(String curentTimeDate) {
        //
        int count=0;

        String selectQuery = "select count(*) from SalesMan_Plan where DATE='"+curentTimeDate+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            try {

                count =  cursor.getInt(0);

                Log.e("getCountPlan", ""+count);

            } catch ( Exception e)
            {
            }
        }
        return count;

    }

  /*  public String getAccPrc(String cust) {
        String ACCPRC="";
        ArrayList<Item> items = new ArrayList<Item>();
        // Select All Query                                                                                                          //AND IS_RETURNED = '0'
        String selectQuery = "SELECT ACCPRC FROM CUSTOMER_MASTER where CUS_ID='"+cust+"'";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor2==", cursor.getCount()+"\t\t");


        try {
            if (cursor.moveToNext()) {
                if (cursor.getString(0) == null) {
                    return "0";
                } else {
                    ACCPRC = (cursor.getString(0));
                    Log.e("ACCPRC", "ACCPRC=" + ACCPRC);
                    return ACCPRC;
                }

            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return ACCPRC;
   }*/
  public String getAccPrc(String cust) {
      //Log.e("cust","cust="+cust);

      String selectQuery = "select ACCPRC from CUSTOMER_MASTER  where CUS_ID='"+cust+"' ";
      Log.e("selectQuery",selectQuery);
      String ACCPRC="";
      db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);
      Log.e("cursor2==", cursor.getCount()+"\t\t");
      try {
          if (cursor.moveToNext()) {
              if (cursor.getString(0) == null) {
                  return "0";
              } else {
                  ACCPRC = (cursor.getString(0));
                  Log.e("ACCPRC","ACCPRC="+ACCPRC);
                  return ACCPRC;
              }

          }else {
              return "0";
          }
      }
      catch ( Exception e)
      {
          Log.e("Exception","getUnitForItem"+e.getMessage());
      }
      return  ACCPRC;
  }
    public String getOriginalItemPriceNEW(String itemNo,String AccPrc) {
        Log.e("getItemName","getItemName="+itemNo);

        String selectQuery = " select Price from Price_List_D  where ItemNo='"+itemNo.trim()+"' And PrNo='"+AccPrc+"'";
        String itemPrice="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToNext()) {
                if (cursor.getString(0) == null) {
                    return "0";
                } else {
                    itemPrice = (cursor.getString(0));
                    Log.e("getItemPrice","getItemPrice="+itemPrice);
                    return itemPrice;
                }

            }
            else
            {
                return "0";
            }
        }
        catch ( Exception e)
        {
            Log.e("Exception","getUnitForItem"+e.getMessage());
        }
        return  itemPrice;
    }
//    delete from SALES_VOUCHER_MASTER where VOUCHER_DATE LIKE '%2021%'
//delete from SALES_VOUCHER_DETAILS where ITEM_YEAR LIKE '%2021%'


  public  void  insertSaleManTrip(SaleManTrips saleManTrips){
      Log.e("insertSaleManTrip","insertSaleManTrip");
      db = this.getReadableDatabase();
      ContentValues values = new ContentValues();

      values.put(TripEndDate, saleManTrips.getTripEndDate());
      values.put(TripEndTime, saleManTrips.getTripEndTime());
      values.put(TripstartDate, saleManTrips.getTripstartDate());
      values.put(TripstartTime, saleManTrips.getTripstartTime());
      values.put(TripStatus, saleManTrips.getTripStatus());
      db.insert(SalemanTrips, null, values);
      db.close();


  }
    public void UpdateSaleManTrip(SaleManTrips saleManTrips) {
        Log.e("UpdateSaleManTrip","UpdateSaleManTrip");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TripEndDate, saleManTrips.getTripEndDate());
        values.put(TripEndTime, saleManTrips.getTripEndTime());
        values.put(TripStatus, saleManTrips.getTripStatus());

        // updating row
        db.update(SalemanTrips, values, TripNum + "=" + "(SELECT MAX(TripNum) FROM SalemanTrips)", null);

    }
    public void deletefromSalemanTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + SalemanTrips);
        db.close();
    }
    public String getLastSaleManTrip(String date ,String time) {
        Log.e("getLastSaleManTrip","getLastSaleManTrip");
        String selectQuery = " select TripStatus from "+SalemanTrips;
        String itemPrice="";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToLast()) {
                 return cursor.getString(0);


            }
        }
        catch ( Exception e)
        {
            return "2";
        }
        return "2" ;}
    public String getSaleManTrip(String date,String time) {
//     date="13/09/2022";test
//     time="18:51";
       Log.e("getLastSaleManTrip","getLastSaleManTrip "+date+" ,"+time);
       String selectQuery = " select TripstartTime from "+SalemanTrips+" Where TripstartDate= '"+date+"' and TripEndTime= '"+time+"' ";
       String itemPrice="";
       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       Log.e("selectQuery",selectQuery+"");
       Log.e("cursor",cursor.getCount()+"");
       try {
          if (cursor.moveToFirst()) {


              {

                return cursor.getString(0);

             }
//            Log.e("ListItemBalance",""+salesManItemsBalanceList.get(0).getQty());
//            Log.e("ListItemBalance",""+salesManItemsBalanceList.get(1).getQty());

          }
       }
       catch ( Exception e)
       {
          return "";
       }
       return "" ;}

    public List<SalesManItemsBalance> getSalesManItemsQTY(String salesmanNo) {
        List<SalesManItemsBalance> salesManItemsBalanceList = new ArrayList<>();
        String selectQuery = "SELECT  distinct SalesManNo , ItemNo , Qty  FROM "+ SalesMan_Items_Balance
                +" Where SalesManNo = " + salesmanNo + "";
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
      Log.e("getSalesManItemsQTY",""+salesManItemsBalanceList.get(0).getQty());

        return salesManItemsBalanceList;
    }
    public ArrayList<String> getvocherDoesNotReturned() {
        ArrayList<String> Vochers = new ArrayList<>();
        // Select All Query
        String custNo=CustomerListShow.Customer_Account;
        String selectQuery = "SELECT DISTINCT SALES_VOUCHER_MASTER.VOUCHER_NUMBER from SALES_VOUCHER_MASTER INNER JOIN SALES_VOUCHER_DETAILS d ON SALES_VOUCHER_MASTER.VOUCHER_NUMBER==d.VOUCHER_NUMBER WHERE d.IS_RETURNED==0 and  CUST_NUMBER= '"+custNo+"' and SALES_VOUCHER_MASTER.VOUCHER_TYPE=504  and d.VOUCHER_TYPE=504";

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor2==", cursor.getCount()+"\t\t");


        if (cursor.moveToFirst()) {
            Log.i("DatabaseHandler", "************************" + selectQuery);
            do {


                Vochers.add(cursor.getString(0));


            } while (cursor.moveToNext());
        }


       return Vochers;
    }
    public void insertTrans_info(TransactionsInfo transactionsInfo) {

       db = this.getReadableDatabase();
       ContentValues values = new ContentValues();

       values.put(PersonName, transactionsInfo.getPersonname());
       values.put( Reson, transactionsInfo.getReson());
       values.put(PhoneNum, transactionsInfo.getPhoneNum());
       values.put(Cust_num, transactionsInfo.getCust_num());
       values.put(Cust_name, transactionsInfo.getCust_name());
       values.put(DATE_, transactionsInfo.getDate());
       db.insert(TransactionInfo, null, values);
       db.close();
    }
    public ArrayList< TransactionsInfo> getTrans_info() {
       ArrayList<TransactionsInfo>transactionsInfos = new ArrayList<>();
       // Select All Query

       String selectQuery = "SELECT * from TransactionInfo_tabel";

       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       Log.e("cursor2==", cursor.getCount()+"\t\t");


       if (cursor.moveToFirst()) {
          Log.i("DatabaseHandler", "************************" + selectQuery);
          do {



             TransactionsInfo transactionsInfo=new TransactionsInfo();
             transactionsInfo.setReson(cursor.getString(1));
             transactionsInfo.setPersonname(cursor.getString(2));
             transactionsInfo.setPhoneNum(cursor.getString(3));
             transactionsInfo.setCust_num(cursor.getString(4));
             transactionsInfo.setCust_name(cursor.getString(5));
             transactionsInfo.setDate(cursor.getString(6));
             transactionsInfos.add(transactionsInfo);


          } while (cursor.moveToNext());
       }


       return transactionsInfos;
    }
    public Voucher getLastVoucher() {
       String cutomer = "",time="";
       Voucher voucher=new Voucher();
       String selectQuery = "select CUST_NUMBER,VOUCHER_time FROM SALES_VOUCHER_MASTER WHERE VOUCHER_NUMBER= (select max(VOUCHER_NUMBER) FROM SALES_VOUCHER_MASTER)";
       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       if (cursor.moveToFirst()) {

          cursor.moveToLast();
          cutomer = cursor.getString(0);
          time= cursor.getString(1);
          voucher.setCustNumber(cursor.getString(0));
          voucher.setTime(cursor.getString(1));
          Log.e("cutomer=", "" + cutomer + "\t");

       }
       return voucher;

    }
void updateDataForClient(){

   SQLiteDatabase db = this.getWritableDatabase();
   db.execSQL("UPDATE SALES_VOUCHER_MASTER  SET IS_POSTED=1 WHERE VOUCHER_NUMBER>=300626 and VOUCHER_NUMBER<=300635");
   db.execSQL("UPDATE SALES_VOUCHER_DETAILS  SET IS_POSTED=1 WHERE VOUCHER_NUMBER>=300626 and VOUCHER_NUMBER<=300635");
   String date="10/09/2022";
 //  db.execSQL("UPDATE TRANSACTIONS  SET IS_POSTED2=1 WHERE CHECK_OUT_DATE= \'"+date+ "\' ");


   ContentValues values = new ContentValues();

   values.put("IS_POSTED",1);

   int y=   db.update("TRANSACTIONS", values," CHECK_OUT_DATE="  +"'"+date+"'", null);
   Log.e("y===",""+y);

   db.close();
}
    void SetVocherPosted(){

       SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("UPDATE SALES_VOUCHER_MASTER  SET IS_POSTED=1");
       db.execSQL("UPDATE SALES_VOUCHER_DETAILS  SET IS_POSTED=1");

       db.close();
    }
    public String getConvRate(String itemNo,String UnitID) {
        Log.e("getConvRate==","getConvRate"+UnitID);
      String ConvRate="1";
       // units.add("1");
       String selectQuery = "select DISTINCT  ConvRate  from " + Item_Unit_Details + " where ItemNo = '" + itemNo + "' and UnitID= '"+UnitID+"' ";
        Log.e("selectQuery==",selectQuery+"");
       db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       if (cursor.moveToFirst()) {
          do {
              try {
                  ConvRate=cursor.getDouble(0)+"";
              }catch (Exception e){
                  Log.e("ConvRateE==",e.getMessage()+"");
                  ConvRate=cursor.getString(0);
              }

             Log.e("ConvRate***==",ConvRate+"");
          } while (cursor.moveToNext());
       }

       return ConvRate;
    }
//    public List<ItemUnitDetails> getItemUnits(String itemNo) {
//
//       List<ItemUnitDetails> list=new ArrayList<>();
//       ItemUnitDetails itemUnitDetails=new ItemUnitDetails();
//       String selectQuery = "select DISTINCT UnitID,PriceUnit  from " + Item_Unit_Details + " where ItemNo = '" + itemNo + "'";
//       Log.e("DB=",(db==null)+"");
//       SQLiteDatabase db = this.getWritableDatabase();
//
//       Cursor cursor = db.rawQuery(selectQuery, null);
//
//       if (cursor.moveToFirst()) {
//          do {
//             itemUnitDetails.setUnitId(cursor.getString(0));
//             itemUnitDetails.setUnitPrice(cursor.getString(1));
//             list.add(itemUnitDetails);
//          } while (cursor.moveToNext());
//       }
//
//       return list;
//    }
    public List< ItemUnitDetails> getItemUnits( String itemNo) {
       List<ItemUnitDetails> list=new ArrayList<>();
       // Select All Query

       String selectQuery = "select DISTINCT UnitID,PriceUnit  from " + Item_Unit_Details + " where ItemNo = '" + itemNo + "'";
       Log.e("selectQuery==", selectQuery+"\t\t");
     //  db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       Log.e("cursor2==", cursor.getCount()+"\t\t");

       if (cursor.moveToFirst()) {
          Log.i("DatabaseHandler", "************************" + selectQuery);
          do {

             ItemUnitDetails     itemUnitDetails=new ItemUnitDetails();
             itemUnitDetails.setUnitId(cursor.getString(0));
             itemUnitDetails.setUnitPrice(cursor.getString(1));
             list.add(itemUnitDetails);


          } while (cursor.moveToNext());
       }


       return list;
    }

    public double getSalesManQty(String itemNo) {
     //SELECT Qty FROM "SalesMan_Items_Balance" WHERE "ItemNo"='67'
       List<ItemUnitDetails> list=new ArrayList<>();
       // Select All Query

       String selectQuery = "SELECT Qty FROM SalesMan_Items_Balance where ItemNo = '" + itemNo + "'";
       Log.e("selectQuerygetSalesManQty==", selectQuery+"\t\t");
       //  db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       Log.e("cursor2==", cursor.getCount()+"\t\t");
       double available=0;
       if (cursor.moveToFirst()) {
          Log.i("DatabaseHandler", "************************" + selectQuery);
          do {

               available=(cursor.getDouble(0));



          } while (cursor.moveToNext());
       }

       return    available;
    }
    public String getLastPriceforCustomer(String itemNo) {

       // Select All Query
       String salesMan = getAllUserNo();
       String price="";
       String customerNo=CustomerListShow.Customer_Account;
       String selectQuery2 ="SELECT detail.UNIT_PRICE ,master.VOUCHER_NUMBER from SALES_VOUCHER_DETAILS detail,SALES_VOUCHER_MASTER master" +
               " where detail.VOUCHER_NUMBER=master.VOUCHER_NUMBER and  detail.ITEM_NUMBER='"+itemNo+"' "+
       "and  master.CUST_NUMBER = '"+customerNo+"' ORDER BY detail.VOUCHER_NUMBER DESC LIMIT 1";
//       String selectQuery2 ="select  Price from PRICE_LIST_D where ItemNo = '"+itemNo+"' and PrNo='"+rate+"' ";

       db = this.getWritableDatabase();
       Log.e("selectQuery2",""+selectQuery2);
       Cursor cursor_price = db.rawQuery(selectQuery2, null);
       if (cursor_price.moveToFirst()){
          price=cursor_price.getString(0);
       }
       cursor_price.close();
       Log.e("selectQuery2","price="+price);
       return price;
    }

     public void deleteListD() {
//         SQLiteDatabase db = this.getWritableDatabase();
//         db.execSQL(" DELETE FROM Price_List_D where  PrNo<>0");
//
//         db.close();
     }
     public   List<String> gettodayVoucher( String CUSTNUMBER,String date) {
         String cutomer = "",time="";
        List<String>list=new ArrayList<>();
         String selectQuery =" select VOUCHER_time FROM SALES_VOUCHER_MASTER WHERE CUST_NUMBER = '"+ CUSTNUMBER+"' and VOUCHER_DATE= '"+date+"' ";
         Log.e("selectQuery=", "" + selectQuery + "\t");
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         Log.e("cursor==", "" + cursor.getCount() + "\t");

         if (cursor.moveToFirst()) {
             Log.i("DatabaseHandler", "************************" + selectQuery);
             do {
                 time= cursor.getString(0);

                 list.add(time);

             } while (cursor.moveToNext());
         }

         return list;

     }

     public void updateLastVoucher() {
//         select max(VOUCHER_NUMBER)+1 from SALES_VOUCHER_MASTER where VOUCHER_NUMBER<>20000053
     
         List<ItemUnitDetails> list=new ArrayList<>();
         // Select All Query

         String selectQuery = "select max(VOUCHER_NUMBER)+1 from SALES_VOUCHER_MASTER where VOUCHER_NUMBER<>20000053";
         Log.e("selectQuerygetSalesManQty==", selectQuery+"\t\t");
         //  db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         Log.e("cursor2==", cursor.getCount()+"\t\t");
         String available="";
         if (cursor.moveToFirst()) {
             Log.i("DatabaseHandler", "************************" + selectQuery);
             do {

                 available=(cursor.getString(0));



             } while (cursor.moveToNext());
         }
         Log.i("DatabaseHandler", "************************" + available);
      updateTableMax(available);
     }

     private void updateTableMax(String voucherNo) {
//         update SALES_VOUCHER_MASTER set  VOUCHER_NUMBER =900666 where VOUCHER_NUMBER=20000053
//
//
         db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(VOUCHER_NUMBER,voucherNo);
         // updating row
         Log.e("currentDate",voucherNo);
         int y=   db.update(SALES_VOUCHER_MASTER, values, VOUCHER_NUMBER + "=20000053" , null);
         Log.e("y===",""+y);
         updatedetails( voucherNo);
     }

     private void updatedetails(String voucherNo) {
      // update SALES_VOUCHER_DETAILS  set  VOUCHER_NUMBER=900666 where VOUCHER_NUMBER=20000053
         db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(VOUCHER_NUMBER,voucherNo);
         // updating row
         Log.e("currentDate",voucherNo);
         int y=   db.update(SALES_VOUCHER_DETAILS, values, VOUCHER_NUMBER + "=20000053" , null);
         Log.e("y===",""+y);
         long vouch=Long.parseLong(voucherNo);
        if(getMaxSerialNumberFromVoucherMaster(508)==20000052)
         updateVoucherNo(vouch,508,0);
     }

     public List<String> getItemPrices(String itemNo) {
         List<String> prices = new ArrayList<>();
         // units.add("1");
         String selectQuery = "select PriceUnit ,PRICECLASS_1,PRICECLASS_2,PRICECLASS_3 from  Item_Unit_Details  where ItemNo='" + itemNo + "'";

         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

         if (cursor.moveToFirst()) {
             do {
                 prices.add(cursor.getString(0));
                 Log.e("getItemPrices","prices="+cursor.getString(0));
                 prices.add(cursor.getString(1));
                 prices.add(cursor.getString(2));
                 prices.add(cursor.getString(3));
             } while (cursor.moveToNext());
         }

         Log.e("getItemPrices","prices="+prices.size());
         return prices;
     }

     @NotNull
     public List<String> getAllItemsName() {
         List<String> items = new ArrayList<>();
        String  selectQuery="select Name from  Items_Master ";
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

         if (cursor.moveToFirst()) {
             do {
                 items.add(cursor.getString(0));
                 Log.e("getItemPrices","items="+cursor.getString(0));

             } while (cursor.moveToNext());
         }

         Log.e("getAllItemsName","prices="+items.size());
         return items;
     }



     public void addVisitMedical(@NotNull visitMedicalModel visitItems) {
         Log.e("addVisitMedical", "===="+visitItems.getCustname());
         db = this.getReadableDatabase();
         ContentValues values = new ContentValues();
         values.put(DateVisit, visitItems.getDateVoucher());
         values.put(CustNo, visitItems.getCustNo());
         values.put(CustName, visitItems.getCustname());
         values.put(Adoption, visitItems.getAdoption());
         values.put(Tool, visitItems.getTool());
         values.put(DoubleVisit, visitItems.getDoubleVisit());
         values.put(RemarkVisit, visitItems.getRemark());
         values.put(HaveItem, visitItems.getHaveItem());
         db.insert(VisitMedicalTable, null, values);
         db.close();
     }

     @NotNull
     public String getMaxVoucherVisit() {

         String maxV = "0";
         String  selectQuery="select max(VoucherNo) from VisitMedicalTable ";
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

         if (cursor.moveToFirst()) {
             do {
                 maxV=(cursor.getString(0));
                 Log.e("getItemPrices","items="+cursor.getString(0));

             } while (cursor.moveToNext());
         }

         Log.e("getAllItemsName","maxV="+maxV);
         return maxV;

     }
     public void addNetworkLog(@NotNull NetworkLogModel listNetworkItem) {
         db = this.getReadableDatabase();
         ContentValues values = new ContentValues();
//         for(int i=0;i<listNetworkItem.size();i++){
             values.put(LinkRequest, listNetworkItem.LinkRequest);
             values.put(DateTrand, listNetworkItem.DateTrand);
             values.put(TimeTrans, listNetworkItem.TimeTrans);
             values.put(CustomerNo, listNetworkItem.CustomerNo);
             values.put( ResponseLink,listNetworkItem.ResponseLink);
             values.put(NoteExport,listNetworkItem.NoteExport);
             db.insert(NetworkTableLog, null, values);
//         }
         db.close();
     }
     public List< NetworkLogModel> getNetworkData( String itemNo) {
         List<NetworkLogModel> list=new ArrayList<>();
         String selectQuery = "select * from  NetworkTableLog ";
         Log.e("getNetworkData==", selectQuery+"\t\t");
         //  db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         Log.e("getNetworkData==", cursor.getCount()+"\t\t");
         if (cursor.moveToFirst()) {
             do {
                 NetworkLogModel     itemUnitDetails=new NetworkLogModel();
                 itemUnitDetails.TransNumber=(cursor.getString(0));
                 itemUnitDetails.LinkRequest=cursor.getString(1);
                 itemUnitDetails.DateTrand=cursor.getString(2);
                 itemUnitDetails.TimeTrans=cursor.getString(3);
                 itemUnitDetails.CustomerNo=cursor.getString(4);
                 itemUnitDetails.ResponseLink=cursor.getString(5);
                 itemUnitDetails.NoteExport=cursor.getString(6);
                 list.add(itemUnitDetails);


             } while (cursor.moveToNext());
         }
         Log.e("getNetworkData=c==", list.size()+"\t\t");

         return list;
     }



     public void addItemVisit(@NotNull List<Item> listSelectedItem) {
         Log.e("addVisitMedical", "items ===="+listSelectedItem.size());
         db = this.getReadableDatabase();
         ContentValues values = new ContentValues();
         for(int i=0;i<listSelectedItem.size();i++){
             values.put(VoucherNoItem, listSelectedItem.get(i).getORIGINALvoucherNo());
             values.put(CustNoItem, listSelectedItem.get(i).getCust());
             values.put(ItemName, listSelectedItem.get(i).getItemName());
             values.put(QtyItem, listSelectedItem.get(i).getQty());
             db.insert(ItemsVisit, null, values);
         }
         db.close();
     }
     public  List<visitMedicalModel>  getAllVisitReport(){
      List<visitMedicalModel> list=new ArrayList<>();
         String  selectQuery=" select VisitMedicalTable.* ,ItemsVisit.ItemName,ItemsVisit.QtyItem from  VisitMedicalTable LEFT JOIN ItemsVisit" +
                 "  ON   ItemsVisit.VoucherNoItem=VisitMedicalTable.VoucherNo";

         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         Log.e("cursor==", "" + cursor.getCount() + "\t");

         if (cursor.moveToFirst()) {
             Log.i("DatabaseHandler", "************************" + selectQuery);
             do {
                 visitMedicalModel visitMedicalModel=new visitMedicalModel(
                         cursor.getString(0),
                         cursor.getString(1),
                         cursor.getString(2),
                         cursor.getString(3),
                         cursor.getString(4),
                         cursor.getString(5),
                         cursor.getString(6),
                         cursor.getString(7),
                         cursor.getString(8),
                         cursor.getString(9),
                         cursor.getString(10)

                 );


                 list.add(visitMedicalModel);
                 Log.e("list","getVoucherNo="+list.get(0).getVoucherNo());
             } while (cursor.moveToNext());
         }
         Log.e("list",""+list.size());
         return  list;
     }
     public  List<visitMedicalModel>  getAllVisitReport_noduplicate(){
         List<visitMedicalModel> list=new ArrayList<>();
         String  selectQuery=" select VisitMedicalTable.* from  VisitMedicalTable ";

         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);
         Log.e("cursor==", "" + cursor.getCount() + "\t");

         if (cursor.moveToFirst()) {
             Log.i("DatabaseHandler", "************************" + selectQuery);
             do {
                 visitMedicalModel visitMedicalModel=new visitMedicalModel(
                         cursor.getString(0),
                         cursor.getString(1),
                         cursor.getString(2),
                         cursor.getString(3),
                         cursor.getString(4),
                         cursor.getString(5),
                         cursor.getString(6),
                         cursor.getString(7),
                         cursor.getString(8),
                         "",
                         ""

                 );


                 list.add(visitMedicalModel);
                 Log.e("list","getVoucherNo="+list.get(0).getVoucherNo());
             } while (cursor.moveToNext());
         }
         Log.e("list",""+list.size());
         return  list;
     }


     @NotNull
     public List<String> getAllFilterMedical(int flag) {
         List<String> items = new ArrayList<>();

         String  selectQuery="";
         switch (flag)
         {
             case 0:
                 selectQuery=" SELECT DISTINCT FAX FROM CUSTOMER_MASTER ORDER BY FAX ASC";
                 break;
             case 1:
                 selectQuery=" SELECT DISTINCT ZipCode FROM CUSTOMER_MASTER ORDER BY ZipCode ASC";
                 break;
             case 2:
                 selectQuery=" SELECT DISTINCT EMail FROM CUSTOMER_MASTER ORDER BY EMail ASC";
                 break;
             case 3:
                 selectQuery=" SELECT DISTINCT C_THECATEG FROM CUSTOMER_MASTER ORDER BY C_THECATEG ASC";
                 break;


         }
         db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(selectQuery, null);

         if (cursor.moveToFirst()) {
             do {
                 items.add(cursor.getString(0));
                 Log.e("getItemPrices","items="+cursor.getString(0));

             } while (cursor.moveToNext());
         }

         return items;
     }


 }

