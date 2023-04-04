package com.dr7.salesmanmanager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.print.PrintHelper;
//import android.support.v7.widget.RecyclerView;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Interface.DaoRequsts;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.Flag_Settings;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.MainGroup_Id_Count;
import com.dr7.salesmanmanager.Modles.OfferGroupModel;
import com.dr7.salesmanmanager.Modles.OfferListMaster;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.RequestAdmin;
import com.dr7.salesmanmanager.Modles.RequstTest;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.ganesh.intermecarabic.Arabic864;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LOCATION_SERVICE;
//import static android.support.v4.app.DialogFragment.STYLE_NORMAL;
//import static android.support.v4.app.DialogFragment.STYLE_NO_FRAME;
//import static android.support.v4.content.ContextCompat.getSystemService;
//import static android.support.v4.content.ContextCompat.getSystemServiceName;
import static android.widget.LinearLayout.VERTICAL;
import static com.dr7.salesmanmanager.Activities.currentKey;
import static com.dr7.salesmanmanager.Activities.currentKeyTotalDiscount;
import static com.dr7.salesmanmanager.Activities.discType_static;
import static com.dr7.salesmanmanager.Activities.discvalue_static;

import static com.dr7.salesmanmanager.Activities.keyCreditLimit;
import static com.dr7.salesmanmanager.Activities.locationPermissionRequestAc;
import static com.dr7.salesmanmanager.AddItemsFragment2.REQUEST_Camera_Barcode;
import static com.dr7.salesmanmanager.AddItemsFragment2.total_items_quantity;

import static com.dr7.salesmanmanager.DiscountFragment.discountPerc;
import static com.dr7.salesmanmanager.LocationPermissionRequest.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.dr7.salesmanmanager.Login.ExportedVochTaxFlage;
import static com.dr7.salesmanmanager.Login.OfferCakeShop;
import static com.dr7.salesmanmanager.Login.Purchase_Order;
import static com.dr7.salesmanmanager.Login.Separation_of_the_serial;
import static com.dr7.salesmanmanager.Login.contextG;
import static com.dr7.salesmanmanager.Login.getTotalBalanceInActivities;
import static com.dr7.salesmanmanager.Login.gone_noTax_totalDisc;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Login.makeOrders;
import static com.dr7.salesmanmanager.Login.offerQasion;
import static com.dr7.salesmanmanager.Login.offerTalaat;
import static com.dr7.salesmanmanager.Login.talaatLayoutAndPassowrd;
import static com.dr7.salesmanmanager.Login.voucherReturn_spreat;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.price;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialListitems;
import static com.dr7.salesmanmanager.Serial_Adapter.barcodeValue;
import static com.dr7.salesmanmanager.Serial_Adapter.errorData;
import static com.dr7.salesmanmanager.Serial_Adapter.serialValue_Model;

import android.location.LocationManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.datatransport.runtime.dagger.multibindings.ElementsIntoSet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

public class SalesInvoice extends Fragment {
    public static  List<Item> jsonItemsList = new ArrayList<>();
    List<Flag_Settings> flag_settingsList;
    public  int aqapa_tax=0,isDataSmoke=0;
    private DatabaseReference databaseReference,databaseReference2;
    public static List<Item> jsonItemsList2 = new ArrayList<>();
    public static List<Item> jsonItemsList_intermidiate= new ArrayList<>();
    public  TextView addSerial;
    ArrayList<OfferGroupModel> listGroup;
    ArrayList<MainGroup_Id_Count> listMainIdCount;
    List<Integer> listAppliedGroup;
    RecyclerView serial_No_recyclerView;
    public static int noTax=0,priceByCustomer=0,Exported_Tax=0;
    SimpleDateFormat dateFormat, timeformat;
    String dateCurent="",timevocher;
    public static  int updatedSerial=0,addNewSerial=0,taxCalcType=0;
    int typeRequest = 0, haveResult = 0, approveAdmin = 0,countNormalQty=0,countBunosQty=0,contiusReading=0;
    int counterSerial;


    LinearLayout mainRequestLinear_serial,voucherKindLinear;
    LinearLayout resultLinear;
    LinearLayout mainLinear;
    public static  TextView serialValueUpdated;
    RecyclerView recyclerView;
    public  static TextView checkState_LimitCredit,    checkStateResult,voucherValueText;
    private EditText discValueEditText,noteEditText;
    public ImageView requestDiscount;
    ImageView rejectDiscount,acceptDiscount,defaultDiscount;
    LinearLayout requestLinear,mainRequestLinear;
    public  static  String noteRequestLimit="";
    requestAdmin request;
    boolean validCustomerName=false,updatedName=false;
    public  static  String itemNoSelected="";
    public  static  List <serialModel> listSerialTotal,copyListSerial,listMasterSerialForBuckup;
    List<String>listSerialValue=new ArrayList<>();
    public Spinner voucherTypeSpinner;
    public  static  int priceListTypeVoucher=0,listOfferNo=-1;
    LinearLayout linearRegulerOfferList;
    public  static int canChangePrice=0;
    ArrayList<serialModel>listTemporarySerial;
    public  static  String ipValue="";


    //    public static  List<Item> jsonItemsList;
//    public static List<Item> jsonItemsList2;
//    public static List<Item> jsonItemsList_intermidiate;
    public static int size_customerpriceslist = 0;
    private static String smokeGA = "دخان";
    private static String smokeGE = "SMOKE";
    public static List<Item> itemForPrintLast;
    LocationManager locationManager;
    Bitmap testB,bitmap;
    byte[] printIm;
    PrintPic printPic;
    private static int salesMan;
    static int index;
    public static List<Payment> payment_unposted;
    public static List<Voucher> sales_voucher_unposted;
    public List<QtyOffers> list_discount_offers;// offer by pay method
    public List<ItemsQtyOffer> itemsQtyOfferList;
    double max_cridit, available_balance, account_balance, cash_cridit, unposted_sales_vou, unposted_payment, unposted_voucher;
    public ListView itemsListView;
    ArrayList<OfferListMaster> currentListActive;
    public static ArrayList<Item> items;
    ArrayList<Integer> itemIteratot;
    public ItemsListAdapter itemsListAdapter;
    private ImageView custInfoImgButton, SaveData;
    private CircleImageView  refreshData, rePrintimage;
    public  static CircleImageView addItemImgButton2;
    private ImageView connect, pic;
    private RadioGroup paymentTermRadioGroup, voucherTypeRadioGroup;
    public   static  RadioButton cash, credit, retSalesRadioButton, salesRadioButton, orderRadioButton;
    private EditText remarkEditText,valueTotalDiscount;
    private ImageButton newImgBtn;
    private double subTotal, totalTaxValue, netTotal;
    public double totalDiscount = 0, discount_oofers_total_cash = 0, discount_oofers_total_credit = 0, sum_discount = 0, disc_items_value = 0, disc_items_total = 0;
    private TextView taxTextView, subTotalTextView, netTotalTextView;
    public static TextView totalQty_textView,priceListNo;
    public  CheckBox readNewDiscount;
    public TextView discTextView;
    public ImageButton discountButton;
    private DecimalFormat decimalFormat;
    public static TextView voucherNumberTextView, Customer_nameSales;
    static ArrayList<Item> itemForPrint;

    private static DatabaseHandler mDbHandler;
    public static int voucherType = 504;
    public int voucherNumber;
    public  static  int payMethod;
    boolean isFinishPrint = false;
    double total_Qty = 0.0;
    double totalQty_forPrint = 0;
    Voucher voucherShow;
    RadioButton discValueRadioButton,discPercRadioButton;
    RadioGroup radioDiscountSerial;
    static String rowToBeUpdated[] = {"", "", "", "", "", "", "", ""};

    boolean clicked = false;
    String itemsString = "";
    String itemsString2 = "";

    static Voucher voucher;
    static Voucher voucherForPrint;
    static List<Item> itemsList;

    public  static TextView finishPrint;
    bluetoothprinter object;
   public static  EditText  price_serial_edit;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    List<ItemsQtyOffer> offers_ItemsQtyOffer;
    ProgressDialog dialog_progress;

    boolean vocherClick = true;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    public static Voucher SaleInvoicePrinted;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    SweetAlertDialog pd;
    double discountValue=0;
    double discountPerc;
    volatile boolean stopWorker;
    DecimalFormat threeDForm;
    double maxDiscounr_value;
    int savedState = 0;
    public  Location custom_location;

    LocationListener locationListener;
    private static final int REQUEST_LOCATION_PERMISSION = 3;
    private FusedLocationProviderClient fusedLocationClient;
    public  static  CustomerLocation customerLocation;
   public static ArrayList<Bitmap> listItemImage;
   public  static  int unitsItems_select=0;
//    public  Location curent_location;
//    static Voucher voucherSale;
//    static List<Item> itemSale;
   /* public static void test2(){

        Customer_nameSales.setText(CustomerListFragment.Customer_Name.toString());
    }*/

    SalesInvoiceInterface salesInvoiceInterfaceListener;
    Date currentTimeAndDate;
    SimpleDateFormat df, df2,formatTime;
    public  static  String voucherDate, voucherYear,time;
    CompanyInfo companyInfo;
    double limit_offer = 0;
    ImageButton maxDiscount;
    int voucherNo = 0, itemCountTable;
    CheckBox check_HidePrice,totalDiscount_checkbox;;
    public static int valueCheckHidPrice = 0;
    LinearLayout mainlayout;
    double curentLatitude, curentLongitude;
    FusedLocationProviderClient mFusedLocationClient;
    FloatingActionButton save_floatingAction;
    boolean validDiscount=false;
    public  static  int checkQtyServer=0  ,sales=1;
    LinearLayout linearTotalCashDiscount,linearPayMethod;

    int[] listImageIcone=new int[]{R.drawable.ic_delete_forever_black_24dp,
            R.drawable.ic_refresh_white_24dp,
          R.drawable.ic_print_white_24dp,R.drawable.ic_create_white_24dp
            ,R.drawable.ic_account_balance_white_24dp,R.drawable.ic_baseline_share_24};
//    R.drawable.ic_save_black_24dp,
    String[] textListButtons=new String[]{};
   public static RequestAdmin discountRequest;

    List<Settings>settingsList=new ArrayList<>();
    public static EditText unitQtyEdit;
    public static  boolean editOpen=false;
    public  String customerTemporiryNmae="";// in ameel naqdi

    public static String VERSION="";
    public  CheckBox notIncludeTax,visaPay,exported_tax;
    public  int visaPayFlag=0;
    Transaction transaction;

    //B
    GeneralMethod generalMethod;

    public SalesInvoice() {
        // Required empty public constructor
    }

    public List<Item> getItemsList() {
        return this.items;
    }

    public interface SalesInvoiceInterface {
        public void displayFindItemFragment();

        public void displayCustInfoFragment();

        public void displayDiscountFragment();

        public void displayUpdateItems();

        public void displayFindItemFragment2();
    }
    public static String lastrequst="";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sales_invoice, container, false);
        mainlayout = (LinearLayout) view.findViewById(R.id.mainlyout);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(offerTalaat==0)
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        listSerialTotal=new ArrayList<>();
        listMasterSerialForBuckup=new ArrayList<>();
        listSerialTotal.clear();
        copyListSerial=new ArrayList<>();
        voucherType = 504;

        contextG=getActivity().getApplicationContext();

        try{


            try {
                approveAdmin = settingsList.get(0).getApproveAdmin();
            }catch (Exception e){
                approveAdmin=0;
            }
            if(approveAdmin==1)
            {
                DaoRequsts daoRequsts=new DaoRequsts(contextG);
                daoRequsts.getStatusofrequst(contextG);
            }

        }catch (Exception  exception){
            Log.e("Exception",exception.getMessage());
        }




        contextG=getActivity().getApplicationContext();

        try {
            if (languagelocalApp.equals("ar"))
            {
                mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else
                {
                if (languagelocalApp.equals("en")) {
                    mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch (Exception e){
            mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }



        //**********************************************************
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
        } else {
            // portrait
        }

        ////B
        generalMethod = new GeneralMethod(getActivity().getBaseContext());
        try {
            if (!generalMethod.checkDeviceDate()) {

               new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                       .setTitleText(getString(R.string.invalidDate))
                       .setContentText(getString(R.string.invalidDate_msg))
                       .setCustomImage(R.drawable.date_error)
                       .setConfirmButton(getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sweetAlertDialog) {
                               sweetAlertDialog.dismissWithAnimation();
                           }
                       })
                       .show();

           }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getTimeAndDate();
        addSerial=view.findViewById(R.id.addSerial);
        if(Separation_of_the_serial==0)
        {
            addSerial.setVisibility(View.GONE);
        }
        addSerial.setOnClickListener(view1 -> {
            addSerial.setEnabled(false);
            addNewSerial=1;
            updatedSerial=0;
            showDialogSerial(getActivity(),0,"",1);

        });

        save_floatingAction=view.findViewById(R.id.save_floatingAction);
        save_floatingAction.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {

                    if (!generalMethod.checkDeviceDate()) {
                        showMessageInvalidDate();

                    }else {
                        saveValidVoucherNo();// valid voucher no



//                        if(flag_settingsList.get(0).getMax_Voucher()==1)
//                        {
//                        validateCurrentVoucherNo();// go to validate voucherno then return to  saveValidVoucherNo()
//
//                        }else {
//                            saveValidVoucherNo();// valid voucher no
//                        }



                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }






            }
        });
        //**********************************************************

        inflateBoomMenu(view );

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        decimalFormat = new DecimalFormat("00.000");

        mDbHandler = new DatabaseHandler(getActivity());
        Purchase_Order=mDbHandler.getFlagSettings().get(0).getPurchaseOrder();
        Log.e("Purchase_Order","111="+Purchase_Order);
        flag_settingsList = mDbHandler.getFlagSettings();
            totalDiscount_checkbox= (CheckBox)view. findViewById(R.id.totalDiscount_checkbox);
        totalDiscount_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                calculateTotals(0);
            }
        });
        valueTotalDiscount= (EditText) view.findViewById(R.id.valueTotalDiscount);
        valueTotalDiscount.setEnabled(false);
        settingsList= mDbHandler.getAllSettings();
        notIncludeTax=view.findViewById(R.id.notIncludeTax);
        exported_tax=view.findViewById(R.id.exported_tax);
        visaPay=view.findViewById(R.id.visaPay);

        if(offerTalaat==1)
        {
            visaPay.setVisibility(View.VISIBLE);
        }else  visaPay.setVisibility(View.GONE);
        notIncludeTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton , boolean b ) {
                calculateTotals(0);
                if(mDbHandler.getAllSettings().get(0).getTaxClarcKind()==1)
                refreshAdapterItems();
            }
        });
        exported_tax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton , boolean b ) {
                calculateTotals(0);
                if(mDbHandler.getAllSettings().get(0).getTaxClarcKind()==1)
                    refreshAdapterItems();
            }
        });

        visaPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton , boolean b ) {
                Log.e("visaPay","onCheckedChanged="+b);
                if(b){
                    visaPayFlag=1;
                    cash.setChecked(true);
                    cash.setEnabled(false);
                    credit.setEnabled(false);
                    payMethod = 1;
                    calculateTotals(0);
                }else {
                    visaPayFlag=0;
                    cash.setEnabled(true);
                    credit.setEnabled(true);
                    calculateTotals(0);
                }
                Log.e("visaPay","onCheckedChanged="+b+"\t"+visaPayFlag);
//                calculateTotals(0);
//                if(mDbHandler.getAllSettings().get(0).getTaxClarcKind()==1)
//                    refreshAdapterItems();
            }
        });




        itemCountTable = mDbHandler.getCountItemsMaster();
        listItemImage=new ArrayList<>();
       priceByCustomer= mDbHandler.getAllSettings().get(0).getPriceByCust() ;
//        listItemImage=mDbHandler.getItemsImage();
//        Log.e("listItemImage",""+listItemImage.get(0).toString());
//        jsonItemsList = new ArrayList<>();
//        jsonItemsList2 = new ArrayList<>();
//        jsonItemsList_intermidiate = new ArrayList<>();
        list_discount_offers = new ArrayList<>();
        itemsQtyOfferList = new ArrayList<>();
        object = new bluetoothprinter();
        itemForPrint = new ArrayList<>();
        threeDForm = new DecimalFormat("#.#");
        valueCheckHidPrice = CustomerListShow.CustHideValu;
        initialView(view);
        if(voucherReturn_spreat==1)
        retSalesRadioButton.setVisibility(View.GONE);
        if (mDbHandler.getAllSettings().get(0).getNoReturnInvoice() == 1) {
            retSalesRadioButton.setEnabled(false);
        }

       checkQtyServer= mDbHandler.getAllSettings().get(0).getQtyServer();
        addItemImgButton2 = (CircleImageView) view.findViewById(R.id.addItemImgButton2);

        rePrintimage = (CircleImageView) view.findViewById(R.id.pic_Re_print);
        rePrintimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    voucherNo = mDbHandler.getLastVoucherNo(voucherType);
                    if (voucherNo != 0 && voucherNo != -1) {
                        voucherForPrint = mDbHandler.getAllVouchers_VoucherNo(voucherNo,voucherType);
                        Log.e("no", "" + voucherForPrint.getCustName() + "\t voucherType" + voucherType);
                        printLastVoucher(voucherNo, voucherForPrint);
                    } else {
                        Toast.makeText(getActivity(), "there is no voucher for this customer and this type of voucher ", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Log.e("ExceptionReprint", "" + e.getMessage());
                    voucherNo = 0;
                }
            }
        });
        refreshData = (CircleImageView) view.findViewById(R.id.refresh_data);
        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshCustomerBalance obj = new RefreshCustomerBalance(getActivity());
                obj.startParsing();
//                calculateTotals();
            }
        });
        custInfoImgButton = (ImageButton) view.findViewById(R.id.custInfoImgBtn);
        custInfoImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               Intent intent =new Intent(getActivity(), AccountReport.class);
//               startActivity(intent);
            }
        });


        maxDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaxDiscountDialog();
            }
        });

        itemsList = new ArrayList<>();
//        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
//        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;

        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;


        String vn2 = voucherNumber + "";
        voucherNumberTextView.setText(vn2);
        connect.setVisibility(View.GONE);
        companyInfo = new CompanyInfo();
        if(visaPayFlag==0) {
            offers_ItemsQtyOffer = mDbHandler.getItemsQtyOffer();
            limit_offer = mDbHandler.getMinOfferQty(total_items_quantity);
        }
        else {
            offers_ItemsQtyOffer.clear();
            limit_offer=0;
        }


        Log.e("total_items_quantity","=="+total_items_quantity+"\t"+limit_offer);
        refrechItemForReprint();
        //*************************************************************************

        salesRadioButton.setOnClickListener(RADIOCLECKED);
        retSalesRadioButton.setOnClickListener(RADIOCLECKED);
        if (mDbHandler.getAllSettings().get(0).getPriventOrder() == 1) {
            orderRadioButton.setEnabled(false);
            orderRadioButton.setChecked(false);

        } else {
            orderRadioButton.setEnabled(true);
            orderRadioButton.setOnClickListener(RADIOCLECKED);
        }


        if (MainActivity.checknum == 1)
            Customer_nameSales.setText(CustomerListShow.Customer_Name.toString());
        else
            Customer_nameSales.setText("Customer Name");


        if (mDbHandler.getAllSettings().get(0).getPaymethodCheck() == 0) {// first checking of cash
            credit.setChecked(true);
            payMethod = 0;
            Log.e("CustomerListShow", "" + CustomerListShow.paymentTerm);
            if (CustomerListShow.paymentTerm == 1)// second check from customer table
            {
                cash.setChecked(true);
                payMethod = 1;


            } else {
                credit.setChecked(true);
                payMethod = 0;

            }
            Log.e("getPaymethodCheck",""+payMethod);

        } else {// cash paymethod from setting

            cash.setChecked(true);
            payMethod = 1;
//            if (CustomerListShow.paymentTerm == 1)// second check from customer table
//            {
//                cash.setChecked(true);
//                payMethod = 1;
//
//
//            } else {
//                credit.setChecked(true);
//                payMethod = 0;
//
//            }


        }
//        boolean printLocation = checkCustomerLocation();
//        Log.e("printLocation1111", "" + printLocation);
        voucherTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                paymentTermRadioGroup.setVisibility(View.VISIBLE);

                if (itemsListView.getCount() > 0) {
                    if (vocherClick) {

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirm Update")
                                .setCancelable(false)
                                .setMessage("Are you sure you want clear the list !")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        updateListSerialBukupDeleted("",voucherNo+"");
                                        clearItemsList();
                                        clearLayoutData(1);

                                        switch (checkedId) {
                                            case R.id.salesRadioButton:
                                                voucherType = 504;
//                                                voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
                                                String vn1 = voucherNumber + "";
                                                voucherNumberTextView.setText(vn1);
                                                salesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                                                retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                   orderRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                break;
                                            case R.id.retSalesRadioButton:
                                                voucherType = 506;
//                                                voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
                                                String vn2 = voucherNumber + "";
                                                voucherNumberTextView.setText(vn2);
                                                retSalesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                                                salesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                orderRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                break;
                                            case R.id.orderRadioButton:
                                                voucherType = 508;
//                                                voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
                                                String vn3 = voucherNumber + "";
                                                voucherNumberTextView.setText(vn3);
                                                paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                                                orderRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                                                retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                   salesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                break;

                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                refreshRadiogroup(voucherType);
                                                vocherClick = true;
                                                dialog.dismiss();
                                            }
                                        }

                                )
                                .show();
                    }
                } else {
                    if (vocherClick) {//without clear data
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirm Update")
                                .setCancelable(false)
                                .setMessage("Are you sure you want change  voucher type !")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                switch (checkedId) {
                                                    case R.id.salesRadioButton:
                                                        voucherType = 504;
//                                                        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
                                                        String vn1 = voucherNumber + "";
                                                        voucherNumberTextView.setText(vn1);
                                                        salesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                                                        retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                           orderRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));

                                                        break;
                                                    case R.id.retSalesRadioButton:
                                                        voucherType = 506;
//                                                        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
                                                        String vn2 = voucherNumber + "";
                                                        voucherNumberTextView.setText(vn2);
                                                        retSalesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                                                        salesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                        orderRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                        break;
                                                    case R.id.orderRadioButton:
                                                        voucherType = 508;
//                                                        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
                                                        String vn3 = voucherNumber + "";
                                                        voucherNumberTextView.setText(vn3);
                                                        paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                                                        orderRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                                                        retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                           salesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                                                        break;
                                                }
                                            }
                                        }

                                )
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                String s = "";
//                                    int  id=voucherTypeRadioGroup.getCheckedRadioButtonId();

                                                refreshRadiogroup(voucherType);
                                                vocherClick = true;


                                                dialog.dismiss();
                                            }
                                        }

                                )
                                .show();

                    }
                }

            }
        });
        try {
            if (mDbHandler.getAllSettings().get(0).getPreventChangPayMeth() == 1) {
                if (CustomerListShow.paymentTerm == 1)
                {
                    paymentTermRadioGroup.setEnabled(false);
                    credit.setEnabled(false);
                    cash.setEnabled(false);
                }

            } else {
                paymentTermRadioGroup.setEnabled(true);
                credit.setEnabled(true);
                cash.setEnabled(true);
                paymentTermRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.creditRadioButton:
                                payMethod = 0;

                                if (mDbHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1) {
                                    discTextView.setText("0.0");
                                    netTotalTextView.setText("0.0");
                                    netTotal = 0.0;
                                    totalDiscount = 0;
                                    sum_discount = 0;
                                    updateListSerialBukupDeleted("",voucherNo+"");
                                    clearLayoutData(1);
                                }
                                calculateTotals(0);

                                break;
                            case R.id.cashRadioButton:
                                payMethod = 1;

                                if (mDbHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1) {
                                    discTextView.setText("0.0");
                                    netTotalTextView.setText("0.0");
                                    netTotal = 0.0;
                                    totalDiscount = 0;
                                    sum_discount = 0;
                                    updateListSerialBukupDeleted("",voucherNo+"");
                                    clearLayoutData(1);
                                }
                                calculateTotals(0);
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }
        if(mDbHandler.getAllSettings().get(0).getAllowOutOfRange()==1)// validate customer location
        {
            getCurrentLocation();
            //checkCustomerLocation();

        }

        ipValue=mDbHandler.getAllSettings().get(0).getIpAddress();
//        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);

        refreshLocalVoucherNo();
        if (mDbHandler.getAllSettings().get(0).getPreventTotalDisc() == 1) {
            discountButton.setEnabled(false);
        } else {
            discountButton.setEnabled(true);
            discountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1) {
                        Log.e("discountButton", "=" + mDbHandler.getAllSettings().get(0).getNoOffer_for_credit());
                        if (payMethod == 0) {
                            salesInvoiceInterfaceListener.displayDiscountFragment();
                        } else {
                            Toast.makeText(getActivity(), "Sory, you can not add discount in cash invoice  .......", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        salesInvoiceInterfaceListener.displayDiscountFragment();
                    }
                }
            });
        }
        try {
            sales=Integer.parseInt(Login.salesMan);
        }
        catch (Exception e){
            sales=1;
        }
        finishPrint=view.findViewById(R.id.finishPrint);
        finishPrint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(""))
                {
                    if(editable.toString().equals("finish"))
                    {
                        if(checkQtyServer==1&& Purchase_Order==0)
                        {
                            ExportJason exportJason= null;
                            try {
                                exportJason = new ExportJason(getActivity());
//                                exportJason.startExportDatabase();
                                exportJason.startExport(0);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            addItemImgButton2.setEnabled(true);

                        }
                    }
                }

            }
        });
        addItemImgButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addItemImgButton2.setEnabled(false);
                try {


                    if (!mDbHandler.isTableEmpty("SalesMan_Items_Balance").equals("0")) {
                        if (!mDbHandler.isTableEmpty("Items_Master").equals("0")) {
                            if (!mDbHandler.isTableEmpty("Price_List_D").equals("0")) {
                                if (!mDbHandler.isTableEmpty("CUSTOMER_MASTER").equals("0")) {
                                    if (checkQtyServer == 0) {
                                        Log.e("itemCountTable", "" + itemCountTable);
                                        if (itemCountTable >= 500) {
                                            new SalesInvoice.Task().execute();

                                        } else {
                                            try {
                                                salesInvoiceInterfaceListener.displayFindItemFragment2();//for test
                                            } catch (Exception e) {
                                                Log.e("salesInvoiceInterfac", "Errrrr" + e.getMessage());
                                            }

                                        }


                                    } else {
                                        if (mDbHandler.getIsPosted(sales) == 0) {
                                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.exportingData), Toast.LENGTH_SHORT).show();
                                            finishPrint.setText("finish");
                                        } else {
                                            if (itemCountTable >= 500) {
                                                new SalesInvoice.Task().execute();

                                            } else {
                                                try {
                                                    salesInvoiceInterfaceListener.displayFindItemFragment2();//for test
                                                } catch (Exception e) {
                                                }

                                            }
                                        }

                                    }
                                } else {
                                    showDialogEmptyTable("CUSTOMER_MASTER");
                                }

                            } else {
                                showDialogEmptyTable("Price_List_D");
                            }

                        } else {
                            showDialogEmptyTable("Items_Master");
                        }


                    } else {
                        showDialogEmptyTable("SalesMan_Items_Balance");
                        //Toast.makeText(getActivity(), "SalesMan_Items_Balance Empty", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception exception){
                    Log.e("exception",exception.getMessage());
                   // Toast.makeText(SalesInvoice.this, ""+exception.getMessage(), Toast.LENGTH_LONG).show();
                   // Toast.makeText(SalesInvoice.this, ""+exception.getMessage(), Toast.LENGTH_LONG).show();
                }



            }
        });

//        addItemImgButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDbHandler. getAllJsonItemsStock(1);
//            }
//        });
//
//                salesInvoiceInterfaceListener.displayFindItemFragment2();
//
//
//                Handler handler = new Handler();
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.dismiss();
//                    }
//                }, 5000);


//        custInfoImgButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                salesInvoiceInterfaceListener.displayCustInfoFragment();
//            }
//        });

        itemsListView = (ListView) view.findViewById(R.id.itemsListView);
        items = new ArrayList<>();
//        itemIteratot=new ArrayList<>();
//        for (int i=0;i<items.size();i++)
//        {
//            itemIteratot.set(i,0);
//            Log.e("itemIteratot",""+itemIteratot.size()+"\titem"+itemIteratot.get(0));
//        }


   refreshAdapterItems();

//        totalQty_textView.setText(items.size()+"");


        itemsListView.setOnItemLongClickListener(onItemLongClickListener);

        newImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllData();

            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.findBT();
            }
        });

        SaveData.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {
                    approveAdmin = settingsList.get(0).getApproveAdmin();
                }catch (Exception e){
                    approveAdmin=0;
                }
                if(approveAdmin==1) {

                    boolean locCheck= locationPermissionRequestAc.checkLocationPermission();

                    Log.e("LocationIn","GoToMain"+locCheck);
                    if(locCheck){
                        saveVoucherData();
                    }else {
                        //
                    }


                }else{
//                    if(Separation_of_the_serial==1){
//                        if(validQty()){
//                            saveVoucherData();
//                        }
//                    }else {
//                        saveVoucherData();
//                    }

                }



            }//end save data
        });
        canChangePrice=mDbHandler.getAllSettings().get(0).getCanChangePrice();
        if(CustomerListShow.Customer_Name.equals("عميل نقدي"))
        {
            credit.setEnabled(false);
        }
        Log.e("canChangePrice",""+canChangePrice);


//        notIncludeTax.setVisibility(View.GONE);
        linearTotalCashDiscount.setVisibility(View.GONE);
        if(talaatLayoutAndPassowrd==1)
        {
            notIncludeTax.setVisibility(View.GONE);
            linearTotalCashDiscount.setVisibility(View.GONE);
            offerTalaat=1;
            offerQasion=0;
            OfferCakeShop=1;

        }
//        if(getTotalBalanceInActivities==0){
//            notIncludeTax.setVisibility(View.GONE);
//            linearTotalCashDiscount.setVisibility(View.GONE);
//        }


        if(Purchase_Order==1){
            retSalesRadioButton.setChecked(true);
            voucherType=506;
            voucherKindLinear=view.findViewById(R.id.voucherKindLinear);
            voucherKindLinear.setVisibility(View.GONE);
            refreshLocalVoucherNo();
        }

        aqapa_tax=mDbHandler.getAllSettings().get(0).getAqapaTax();

        if(Purchase_Order==1){
            mDbHandler.getAllSettings().get(0).setBonusNotAlowed(1);
        }
        Log.e("gone_noTax_totalDisc","="+gone_noTax_totalDisc);

        if(ExportedVochTaxFlage==1){
            exported_tax.setVisibility(View.VISIBLE);
        }
        else{
            exported_tax.setVisibility(View.GONE);
        }

        if(gone_noTax_totalDisc==0){
            notIncludeTax.setVisibility(View.GONE);
            linearTotalCashDiscount.setVisibility(View.GONE);
            Log.e("gone_noTax_totalDisc","2="+gone_noTax_totalDisc);
        }
        else {
            notIncludeTax.setVisibility(View.VISIBLE);
//            linearTotalCashDiscount.setVisibility(View.VISIBLE);
        }
        taxCalcType=mDbHandler.getAllSettings().get(0).getTaxClarcKind() ;

        unitsItems_select=mDbHandler.getAllSettings().get(0).getItems_Unit();
        VERSION=this.getResources().getString(R.string.version);
        Log.e("VERSION","= "+VERSION);
        isDataSmoke=mDbHandler.getAllSettings().get(0).getDiscAfterTax();
        Log.e("isDataSmoke"," = "+isDataSmoke);
        return view;
    }

    private void refreshLocalVoucherNo() {
        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
    }

    private void refreshAdapterItems() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            itemsListAdapter = new ItemsListAdapter(getActivity(), items,0);// if screen is landscape =====> 0

        } else {
            // portrait
            itemsListAdapter = new ItemsListAdapter(getActivity(), items,1);// if screen is landscape =====> 0

        }
        itemsListView.setAdapter(itemsListAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveValidVoucherNo() {
        try {
            approveAdmin = settingsList.get(0).getApproveAdmin();
        }catch (Exception e){
            approveAdmin=0;
        }
        if(approveAdmin==1) {

            boolean locCheck= locationPermissionRequestAc.checkLocationPermission();

            Log.e("LocationIn","GoToMain"+locCheck);
            if(locCheck){
                String all_itemValid="";
                if(Separation_of_the_serial==1){
                    all_itemValid=validQty();
                    if(all_itemValid.equals("")){
                        saveVoucherData();
                    }else {
                        showNotQtyValid(all_itemValid);
                    }
                }else {

                    saveVoucherData();
                }
            }else {
                Toast.makeText(getContext(), "check Permision Location ", Toast.LENGTH_SHORT).show();
                //
            }


        }
        else{
            String all_itemValid="";
            if(Separation_of_the_serial==1){
                all_itemValid=validQty();
                if(all_itemValid.equals("")){
                    saveVoucherData();
                }else {
                    showNotQtyValid(all_itemValid);
                }
            }else {

                saveVoucherData();
            }


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void validateCurrentVoucherNo() {
        ImportJason importData=new ImportJason(getContext());
        importData.getMaxVoucherNo(1);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void refreshVoucherNo(){

       int curentVoucher=mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType);

       Log.e("refreshVoucherNo","curentVoucher="+curentVoucher+"\tvoucherNumber="+voucherNumber);
       if(curentVoucher>voucherNumber)
       {
           voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
           String vn = voucherNumber + "";
           voucherNumberTextView.setText(vn);
       }

           voucherNumberTextView.setText(voucherNumber+"");
           saveValidVoucherNo();



    }


    private void showMessageInvalidDate() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(getString(R.string.invalidDate))
                .setContentText(getString(R.string.invalidDate_msg))
                .setCustomImage(R.drawable.date_error)
                .setConfirmButton(getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private String validQty() {
        int count=0,position=-1;
        String itemName="";
        for(int i=0;i<items.size();i++){
            for(int j=0;j<jsonItemsList.size();j++)
            {
                if(items.get(i).getItemNo().trim().equals(jsonItemsList.get(j).getItemNo().trim())){
                    Log.e("validQty","count="+items.get(i).getQty()+"getQty=\t"+jsonItemsList.get(j).getQty());
                    if(items.get(i).getQty()>jsonItemsList.get(j).getQty()){
                        count++;
                        position=i;
                        itemName=items.get(i).getItemName();
                        break;
                    }
                }
            }

        }
        Log.e("validQty","count="+count+"\t"+itemName);
        if(count==0)
        {
//            itemsListAdapter.get
            return itemName;
        }

        else return  itemName;
    }


    private void checkGroupOffer(int flagDel){
        Log.e("checkGroupOffer","flagDel= "+flagDel);
        float qtyOffer=0;
       listGroup=new ArrayList<>();
        listGroup=mDbHandler.getAllGroupOffers(voucherDate);
        if(listGroup.size()!=0)
        {

           // Log.e("checkGroupOffer","listGroup"+listGroup.size()+"\tvoucherDate="+voucherDate);
            for (int i=0;i<items.size();i++)
            {
                for (int j=0;j<listGroup.size();j++)
                {
                    qtyOffer=Float.parseFloat(listGroup.get(j).qtyItem);
                   // Log.e("matchOffer","qtyOffer"+qtyOffer);
                    if((items.get(i).getItemNo().trim().equals(listGroup.get(j).ItemNo.trim()))){
                        items.get(i).setDisc(0);
                        items.get(i).setDiscPerc("0");
//                        if(flagDel==1)
//                        {
                            items.get(i).setAmount(items.get(i).getQty() * items.get(i).getPrice() );
//                        }
                        if(items.get(i).getQty()>=qtyOffer)
                        {
                            Log.e("checkGroupOffer","listGroup"+items.get(i).getItemNo()+"\tlistGroup="+listGroup.get(j).ItemNo);
                            listGroup.get(j).matchOffer=1;



                        }
                    }

                }
            }
            checkMatchesGroup(listGroup);
        }else {
            Log.e("checkMatchesGroup","NoOffer");
        }


    }

    private void checkMatchesGroup(ArrayList<OfferGroupModel> listGroup) {
        listAppliedGroup= new ArrayList<Integer>();
        listMainIdCount=mDbHandler.getMainGroup_Id_Count(voucherDate);
        //check date of listMainIdCount
        int count=0,item_count=1,descType=0;
        double totalItemDiscount=0;
        count=0;
        for(int i=0;i<listGroup.size();i++) {

            if (listGroup.get(i).matchOffer == 1) {


            for (int j = 0; j < listMainIdCount.size(); j++) {
                if (listGroup.get(i).groupIdOffer == listMainIdCount.get(j).idGroup) {
                    if (listGroup.get(i).matchOffer == 1) {
                        count++;
                        if (count == listMainIdCount.get(j).countGroup) {
                            listAppliedGroup.add(listMainIdCount.get(j).idGroup);
                            totalItemDiscount = Double.parseDouble(listGroup.get(i).discount);
                            item_count = listMainIdCount.get(j).countGroup;
                            descType=listGroup.get(i).discountType;
                            Log.e("listAppliedGroup", "idGroup=" + listMainIdCount.get(j).idGroup + "\t totalItemDiscount=" + totalItemDiscount);
                            //count=0;
//                            j++;
                        }

                    }
                }


            }
        }

        }
        if(listAppliedGroup.size()!=0) {

            calckTotalDiscount(listAppliedGroup, totalItemDiscount, item_count,descType);
        }else{
            Log.e("checkMatchesGroup","NoOffer+listAppliedGroup");
             // Log.e("listMainIdCount",""+count+"\tlistAppliedGroup="+listAppliedGroup.size()+"\t"+listAppliedGroup.get(0));

        }
    }

    private void calckTotalDiscount( List<Integer> listAppliedGroup,double totalDiscount,int count,int discType) {
       float descValue=0,descPercent=0,discountOneItem=1;

       try {
           if(count!=0)
            discountOneItem= (float) (totalDiscount/count);
           Log.e("calckTotalDiscount","totalDiscount"+totalDiscount+"\tcount="+count);

           //***************************************************************

           for(int i=0;i<listGroup.size();i++){
               for(int j=0;j<items.size();j++)
               {
                   if(listGroup.get(i).groupIdOffer==listAppliedGroup.get(0))
                   {
                       String itemNo=listGroup.get(i).ItemNo;
                       Log.e("calckTotalDiscount","before="+ items.get(j).getDisc());
                       if(items.get(j).getItemNo().trim().equals(itemNo.trim()))
                       {
                           descPercent=items.get(j).getQty()*items.get(j).getPrice()*(discountOneItem/100);
                           descPercent=Float.parseFloat(convertToEnglish(decimalFormat.format(descPercent)+""));
                           descValue=discountOneItem;
                           // Log.e("calckTotalDiscount","descPercent="+descPercent+"\tdescValue="+descValue);

                           if(discType==1){// percent
                               items.get(j).setDisc(descPercent);

                               items.get(j).setDiscPerc( descValue+"");
                               items.get(j).setDiscType(1);// error for discount promotion // percent discount
                               items.get(j).setAmount(items.get(j).getAmount() - items.get(j).getDisc());

                           } else {


                               items.get(j).setDisc(descValue);

                               items.get(j).setDiscPerc( descPercent+"");
                               items.get(j).setDiscType(0);// value Discount
                               // Log.e("discPercRadio_update","position 1getAmount="+items.get(j).getAmount());
                               items.get(j).setAmount(items.get(j).getAmount() - items.get(j).getDisc());
                               // Log.e("discPercRadio_update","position 1getAmount="+items.get(j).getAmount());
                           }

                           // items.get(j).setDisc(discountOneItem);

                           // itemsListView.setAdapter(itemsListAdapter);
                       }
                   }

               }





           }
       }catch (Exception e){

       }





    }

    public  static  void addQtyTotal(float qty)
    {
        total_items_quantity += qty;
        totalQty_textView.setText(total_items_quantity + "");


    }
    public  static  void minusQtyTotal(float qty)
    {
        total_items_quantity -= qty;
        totalQty_textView.setText(total_items_quantity + "");


    }
    private void showDialogEmptyTable(String tableName) {
        addItemImgButton2.setEnabled(true);
        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(getResources().getString(R.string.emptyTable))
                .setContentText(tableName)
                .show();
    }
//          if(savedInstanceState!=null){
//
//        items=(ArrayList)savedInstanceState.getSerializable("arrayItems");
//        itemsListAdapter = new ItemsListAdapter(getActivity(), items,1);// if screen is landscape =====> 0
//        itemsListView.setAdapter(itemsListAdapter);
//        Log.e("items",""+items.size());
//    }
//        else {
//        items = new ArrayList<>();
//    }

    private void inflateBoomMenu(View view) {
        textListButtons=new String[]{getResources().getString(R.string.delet),getResources().getString(R.string.refresh),getResources().getString(R.string.print),getResources().getString(R.string.request),getResources().getString(R.string.last_visit),getResources().getString(R.string.Shar)};


        BoomMenuButton bmb = (BoomMenuButton)view.findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_6_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_6_3);
Log.e("bmb.size="," "+bmb.getPiecePlaceEnum().pieceNumber());
        for (int j = 0; j < bmb.getPiecePlaceEnum().pieceNumber(); j++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(listImageIcone[j])
                    .textSize(12)
                    .normalText(textListButtons[j])
                    .textPadding(new Rect(5, 5, 5, 0))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index) {
                                case 0:
                                    updateListSerialBukupDeleted("", voucherNo + "");
                                    clearAllData();
                                    break;
                                case 1:
                                    RefreshCustomerBalance obj = new RefreshCustomerBalance(getActivity());
                                    obj.startParsing();
                                    break;

                                case 2:


                                    try {
                                        voucherNo = mDbHandler.getLastVoucherNo(voucherType);
                                        if (voucherNo != 0 && voucherNo != -1) {
                                            voucherForPrint = mDbHandler.getAllVouchers_VoucherNo(voucherNo,voucherType);
                                            Log.e("no", "" + voucherForPrint.getCustName() + "\t voucherType" + voucherType);
                                            printLastVoucher(voucherNo, voucherForPrint);
                                        } else {
                                            Toast.makeText(getActivity(), "there is no voucher for this customer and this type of voucher ", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (Exception e) {
                                        Log.e("ExceptionReprint", "" + e.getMessage());
                                        voucherNo = 0;
                                    }
                                    break;
                                case 3:
                                    if (mDbHandler.getAllSettings().get(0).getPreventTotalDisc() == 0) {

                                        if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1) {
                                            Log.e("discountButton", "=" + mDbHandler.getAllSettings().get(0).getNoOffer_for_credit());
                                            if (payMethod == 0) {
                                                getDataForDiscountTotal();
                                                salesInvoiceInterfaceListener.displayDiscountFragment();
                                            } else {
                                                Toast.makeText(getActivity(), "Sory, you can not add discount in cash invoice  .......", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            getDataForDiscountTotal();

                                            salesInvoiceInterfaceListener.displayDiscountFragment();
                                        }
                                    }
                                    break;
                                case 4:
                                    showLastVisitDialog();
                                case 5:
                                    try {
                                        voucherNo = mDbHandler.getLastVoucherNo(voucherType);
                                        if (voucherNo != 0 && voucherNo != -1) {
                                            voucherForPrint = mDbHandler.getAllVouchers_VoucherNo(voucherNo,voucherType);
                                            Log.e("no", "" + voucherForPrint.getCustName() + "\t voucherType" + voucherType);
                                            sharwhats();
                                        } else {
                                            Toast.makeText(getActivity(), "there is no voucher for this customer and this type of voucher ", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (Exception e) {
                                        Log.e("ExceptionReprint", "" + e.getMessage());
                                        voucherNo = 0;
                                    }

                                 //   shareWhatsApp();
                                    break;

                            }
                        }
                    });
            bmb.addBuilder(builder);
        }
       // inflateMenuInsideText(view);

    }

    private void inflateMenuInsideText(View view) {
        textListButtons=new String[]{getResources().getString(R.string.delet),getResources().getString(R.string.refresh),getResources().getString(R.string.print),getResources().getString(R.string.request),getResources().getString(R.string.last_visit)};


        BoomMenuButton bmb = (BoomMenuButton)view.findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_5_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_5_3);
        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
//            bmb.addBuilder(new SimpleCircleButton.Builder()
//                    .normalImageRes(listImageIcone[i]));

            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(listImageIcone[i])
                    .textSize(12)
                    .normalText(textListButtons[i])
                    .textPadding(new Rect(5, 5, 5, 0))
                    .listener(new OnBMClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index)
                            {
                                case 0:
                                    updateListSerialBukupDeleted("",voucherNo+"");
                                    clearAllData();
                                    break;
                                case 1:
                                    RefreshCustomerBalance obj = new RefreshCustomerBalance(getActivity());
                                    obj.startParsing();
                                    break;

                                case 2:


                                    try {
                                        voucherNo = mDbHandler.getLastVoucherNo(voucherType);
                                        if (voucherNo != 0 && voucherNo != -1) {
                                            voucherForPrint = mDbHandler.getAllVouchers_VoucherNo(voucherNo,voucherType);
                                            Log.e("no", "" + voucherForPrint.getCustName() + "\t voucherType" + voucherType);
                                            printLastVoucher(voucherNo, voucherForPrint);
                                        } else {
                                            Toast.makeText(getActivity(), "there is no voucher for this customer and this type of voucher ", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (Exception e) {
                                        Log.e("ExceptionReprint", "" + e.getMessage());
                                        voucherNo = 0;
                                    }
                                    break;
                                case 3:
                                    if (mDbHandler.getAllSettings().get(0).getPreventTotalDisc() == 0) {

                                        if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1) {
                                            Log.e("discountButton", "=" + mDbHandler.getAllSettings().get(0).getNoOffer_for_credit());
                                            if (payMethod == 0) {
                                                getDataForDiscountTotal();
                                                salesInvoiceInterfaceListener.displayDiscountFragment();
                                            } else {
                                                Toast.makeText(getActivity(), "Sory, you can not add discount in cash invoice  .......", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            getDataForDiscountTotal();

                                            salesInvoiceInterfaceListener.displayDiscountFragment();
                                        }
                                    }
                                    break;
                                case 4:
                             showLastVisitDialog();
                                    break;

                            }
                        }
                    });
            bmb.addBuilder(builder);


        }
    }
    public  void   shareWhatsApp() {


        Log.e("shareWhatsApp==", "shareWhatsApp" );



    }
    private void showLastVisitDialog() {
        String lastVisit=getLastVaisit();
        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(getResources().getString(R.string.last_visit))
                .setContentText(lastVisit)
                .show();

    }
    public  void showNotQtyValid(String itemNa){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getResources().getString(R.string.not_enouf_qty))
                .setContentText(itemNa)
                .show();

    }
    private String getLastVaisit() {
        String visit="";
        transaction=new Transaction();
        if(!CustomerListShow.Customer_Account.equals(""))
        {
            transaction=mDbHandler.getLastVisitInfo(CustomerListShow.Customer_Account,Login.salesMan);
            if(transaction.getCheckInDate()!=null)
            {
                visit=transaction.getCheckInDate()+"\t\t"+transaction.getCheckInTime();
                Log.e("getLastVaisit",""+CustomerListShow.Customer_Account+"\t"+Login.salesMan+"\t"+transaction.getCheckInDate());
            }
            else {
                visit=voucherDate+"\t\t"+time;
            }

        }

        return  visit;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("arrayItems",  items);
    }

    public void getTimeAndDate() {
        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        formatTime=new SimpleDateFormat("hh:mm:ss");

        voucherDate = df.format(currentTimeAndDate);
        voucherDate = convertToEnglish(voucherDate);
        time=formatTime.format(currentTimeAndDate);
        time=convertToEnglish(time);
        Log.e("time",""+time);
        df2 = new SimpleDateFormat("yyyy");
        voucherYear = df2.format(currentTimeAndDate);
        voucherYear = convertToEnglish(voucherYear);
    }

    private void getDataForDiscountTotal() {
        discountRequest=new RequestAdmin();
        if(mDbHandler.getAllSettings().size()!=0)
        {
            discountRequest.setSalesman_name( mDbHandler.getAllSettings().get(0).getSalesMan_name());
        }
        else {
            discountRequest.setSalesman_name("");
        }
        discountRequest.setSalesman_no(Login.salesMan);
        discountRequest.setCustomer_no(CustomerListShow.Customer_Account);
        discountRequest.setCustomer_name(CustomerListShow.Customer_Name);
        discountRequest.setAmount_value("0");
        discountRequest.setTotal_voucher(netTotal+"");
        discountRequest.setVoucher_no(voucherNumber+"");
        discountRequest.setDate(voucherDate);
        discountRequest.setKey_validation("");
        discountRequest.setNote("note");
        discountRequest.setRequest_type("1");
        discountRequest.setStatus("0");
        getTimeAndDate();
        discountRequest.setTime(time);
        discountRequest.setSeen_row("0");
    }

    private void saveVoucherData() {
        SaveData.setEnabled(false);
        save_floatingAction.setEnabled(false);
        savedState = 1;
        final String remarkText = remarkEditText.getText().toString().trim();

        itemForPrint.clear();
        clicked = false;
        int TypeVouch=getVoucherTypeCurrent();

        String voucherType_Word= getVoucherTypeWord(TypeVouch);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_save));
//                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));

        builder.setTitle( voucherType_Word);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int l) {

                if (!clicked) {

                    clicked = true;
                    int listSize = itemsListView.getCount();
                    int validateVoucherNo = mDbHandler.checkVoucherNo(voucherNumber,voucherType);

                    Log.e("ayahvalidateVoucherNo", "===" + validateVoucherNo);

                    if (validateVoucherNo == 0) {// not exist voucher no in the same number
                        if (listSize == 0) {
                            Toast.makeText(getActivity(), "Fill Your List Please", Toast.LENGTH_LONG).show();
                            SaveData.setEnabled(true);
                            save_floatingAction.setEnabled(true);
                        } else {//list is contain data

                            if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange()==1) {// validate customer location
                                if( checkCustomerLocation())
                                {
                                    if (mDbHandler.getAllSettings().get(0).getRequiNote() == 1)
                                    {
                                        if (TextUtils.isEmpty(remarkText)) {
                                            remarkEditText.setError("Required");
                                            remarkEditText.requestFocus();
                                            SaveData.setEnabled(true);
                                            save_floatingAction.setEnabled(true);
                                        } else {
                                            saveData();
                                        }

                                    } else {
                                        saveData();

                                    }

                                }
                                else{
                                    SaveData.setEnabled(true);
                                    save_floatingAction.setEnabled(true);
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(getResources().getString(R.string.warning_message))
                                            .setContentText(getResources().getString(R.string.InvalidLocation))
                                            .show();
                                }
                                Log.e("printLocation1111", "===" + checkCustomerLocation());
                            } else {
                                if (mDbHandler.getAllSettings().get(0).getRequiNote() == 1)
                                {
                                    if (TextUtils.isEmpty(remarkText)) {
                                        remarkEditText.setError("Required");
                                        remarkEditText.requestFocus();
                                        SaveData.setEnabled(true);
                                        save_floatingAction.setEnabled(true);
                                    } else {
                                        saveData();
                                    }

                                } else {
                                    saveData();

                                }
                            }



//                                clearLayoutData();
                        }
                        //not empty list

                    } else {// dublicate voucher no ==>> alert dialog
                        SaveData.setEnabled(true);
                        save_floatingAction.setEnabled(true);
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getResources().getString(R.string.warning_message))
                                .setContentText(getResources().getString(R.string.duplicatedVoucherNo))
                                .show();

                    }


                }
            }//end ok save

        });

        builder.setNegativeButton(getResources().getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveData.setEnabled(true);
                save_floatingAction.setEnabled(true);
            }
        });
        builder.create().

                show();
    }

    private void clearAllData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
        builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
        builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                updateListSerialBukupDeleted("",voucherNo+"");
                clearLayoutData(1);
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
        builder.create().show();
    }

    private String getVoucherTypeWord(int typeVouch) {
        String CurentType="";
        switch (typeVouch)
        {

            case 504:
                CurentType=getResources().getString(R.string.app_sales_inv);

                break;
            case 506:
                CurentType=getResources().getString(R.string.app_ret_inv);
                break;
            case 508:
                CurentType=getResources().getString(R.string.app_cust_order);
                break;
        }
        Log.e("CurentType",""+CurentType);
        return CurentType;
    }

    private boolean checkCustomerLocation() {
        float distance=0;
        if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 1)// validate customer location
        {


            Location curent_location = new Location("");
            curent_location.setLatitude(curentLatitude);
            curent_location.setLongitude(curentLongitude);




            //*********************************************************************

            custom_location=new Location("");
            try {
                custom_location.setLatitude(Double.parseDouble(CustomerListShow.latitude));
                custom_location.setLongitude(Double.parseDouble(CustomerListShow.longtude));
            }
            catch (Exception e)
            {
                custom_location.setLatitude(0);
                custom_location.setLongitude(0);
                Log.e("Exceptioncusto",""+e.getMessage());
                return  true;


            }
            try {
                 distance= curent_location.distanceTo(custom_location);
                 Toast.makeText(getActivity(), "distance="+distance, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                distance=0;
            }



          // *****************************************************************************************
//            Log.e("distanceTo",""+curent_location.distanceTo(custom_location));
//            Log.e("custom_location",""+custom_location.getLongitude()+"\t"+curent_location.getLongitude());
            if(distance<=50)
            { return true;}
            else  return   false;

        } else {
            return true;
        }

    }


    private  void getCurrentLocation() {
        LocationListener locationListener;

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {// Not granted permission
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                curentLatitude  = location.getLatitude();
                curentLongitude = location.getLongitude();


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);//test
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (Exception e)
        {
            Log.e("locationManager",""+e.getMessage());
        }

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            curent_location=new Location(location);
//
//
//
//
//
//                        }
//                        else{
//                            Toast.makeText(getActivity(), "Check GPS", Toast.LENGTH_SHORT).show();
//                        }
//                    Log.e("curent_location", "getCurrentLocation\t" + curent_location.getLatitude() + curent_location.getLongitude());
//                        // Logic to handle location object
//
//                    }
//                });

//            }



    }//end





    private void initialView(View view) {
        connect = (ImageView) view.findViewById(R.id.balanceImgBtn);
        voucherNumberTextView = (TextView) view.findViewById(R.id.voucherNumber);
        voucherNumberTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length()!=0){
                    if(editable.toString().trim().equals("refresh"))
                    {
                        refreshVoucherNo();
                    }
                }

            }
        });
        Customer_nameSales = (TextView) view.findViewById(R.id.invoiceCustomerName);
        Customer_nameSales.setMovementMethod(new ScrollingMovementMethod());
        paymentTermRadioGroup = (RadioGroup) view.findViewById(R.id.paymentTermRadioGroup);
        voucherTypeRadioGroup = (RadioGroup) view.findViewById(R.id.transKindRadioGroup);
        cash = (RadioButton) view.findViewById(R.id.cashRadioButton);
        credit = (RadioButton) view.findViewById(R.id.creditRadioButton);
        retSalesRadioButton = (RadioButton) view.findViewById(R.id.retSalesRadioButton);
        linearTotalCashDiscount= (LinearLayout) view.findViewById(R.id.linearTotalCashDiscount);
        linearPayMethod= (LinearLayout) view.findViewById(R.id.linearPayMethod);
        salesRadioButton = (RadioButton) view.findViewById(R.id.salesRadioButton);
        salesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));

        orderRadioButton = (RadioButton) view.findViewById(R.id.orderRadioButton);
        if(Purchase_Order==0)
        salesRadioButton.setChecked(true);
        else retSalesRadioButton.setChecked(true);


        remarkEditText = (EditText) view.findViewById(R.id.remarkEditText);
        newImgBtn = (ImageButton) view.findViewById(R.id.newImgBtn);
        SaveData = (ImageButton) view.findViewById(R.id.saveInvoiceData);
        discountButton = (ImageButton) view.findViewById(R.id.discButton);
//        discountButton.setVisibility(View.GONE);
        pic = (ImageView) view.findViewById(R.id.pic_sale);
        discTextView = (TextView) view.findViewById(R.id.discTextView);

        totalQty_textView = (TextView) view.findViewById(R.id.items_quntity);
        subTotalTextView = (TextView) view.findViewById(R.id.subTotalTextView);
        taxTextView = (TextView) view.findViewById(R.id.taxTextView);
        netTotalTextView = (TextView) view.findViewById(R.id.netSalesTextView1);
        maxDiscount = (ImageButton) view.findViewById(R.id.max_disc);// the max discount for this customer
        maxDiscount.setVisibility(View.GONE);
        //readNewDiscount=(CheckBox) view.findViewById(R.id.readNewDiscount);
        priceListNo=(TextView) view.findViewById(R.id.priceListNo);
        linearRegulerOfferList=(LinearLayout) view.findViewById(R.id.linearRegulerOfferList);
        if(mDbHandler.getAllSettings().get(0).getReadOfferFromAdmin()==1)
        {
            priceListNo.setVisibility(View.VISIBLE);
            linearRegulerOfferList.setVisibility(View.VISIBLE);
            getPriceListNo();
        }
        else {
            priceListNo.setVisibility(View.GONE);
            linearRegulerOfferList.setVisibility(View.GONE);
        }
        voucherTypeSpinner=(Spinner) view.findViewById(R.id.voucherTypeSpinner);
      currentListActive=new ArrayList<>();
            fillSpiner();
        if(makeOrders==1)
        {
            voucherType = 508;
            salesRadioButton.setVisibility(View.GONE);
            retSalesRadioButton.setVisibility(View.GONE);
            linearTotalCashDiscount.setVisibility(View.GONE);
            notIncludeTax.setVisibility(View.GONE);
//            linearPayMethod.setVisibility(View.GONE);
            orderRadioButton.setChecked(true);
        }


    }

    private void fillSpiner() {
        ArrayList<String> kinds = new ArrayList<>();
        String dateCurent=getCurentTimeDate(1);
        kinds.add(getResources().getString(R.string.regular));

        try {
           currentListActive=mDbHandler.getPriceOfferActive(dateCurent);
            Log.e("currentListActive",""+currentListActive.size()+"\t"+currentListActive.get(0).getPO_LIST_NAME());

            for(int i=0;i<currentListActive.size();i++)
            {
                kinds.add(currentListActive.get(i).getPO_LIST_NAME().toString());

            }
        }catch (Exception e){}


        ArrayAdapter<String> paymentKind = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, kinds);
        voucherTypeSpinner.setAdapter(paymentKind);
        voucherTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i!=0&&currentListActive.size()!=0)
                {
                    listOfferNo=currentListActive.get(i-1).getPO_LIST_NO();
                }

                priceListTypeVoucher=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getPriceListNo() {
        String dateCurent=getCurentTimeDate(1);
//        ArrayList<OfferListMaster> currentListActive=mDbHandler.getPriceOfferActive(dateCurent);
        String rate_customer = mDbHandler.getPriceListNoMaster(dateCurent);
        if(rate_customer.equals("0")||rate_customer.equals(""))
        {
            priceListNo.setVisibility(View.GONE);
        }
        priceListNo.setText(getActivity().getResources().getString(R.string.price_ListNo)+"\t"+ rate_customer);
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
                timeformat = new SimpleDateFormat("HH:mm:ss");
                dateCurent = timeformat.format(currentTimeAndDate);
                dateTime=convertToEnglish(dateCurent);
            }
        }
        return dateTime;

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveData() {
        Log.e("saveData","Customer_Name="+CustomerListShow.Customer_Name);
        Log.e("saveData","temporary_Name="+customerTemporiryNmae);
        validCustomerName=false;
        if(CustomerListShow.Customer_Name.equals("عميل نقدي"))
        {
            showCustomerNameDialog();

        }else {
            saveLocalData();


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveLocalData() {
        if(!CustomerListShow.Customer_Name.equals("عميل نقدي")){

                validDiscount=false;

                double discountValue=0;
                double discountPerc=0;
                DiscountFragment obj_discountFragment = new DiscountFragment(getActivity().getBaseContext(),"");
                discountValue = obj_discountFragment.getDiscountValue();
                discountPerc = obj_discountFragment.getDiscountPerc();

                double totalDisc = Double.parseDouble(discTextView.getText().toString());
                double subTotal = Double.parseDouble(subTotalTextView.getText().toString());
                double tax = 0, netSales = 0;
                String netsale_txt = "";
                netsale_txt = netTotalTextView.getText().toString();
                Log.e("textNt", "" + netsale_txt);

                try {
                    tax = Double.parseDouble(taxTextView.getText().toString());
                    netSales = Double.parseDouble(netTotalTextView.getText().toString());

                } catch (Exception e) {
                    tax = 0;
                    Log.e("tax error E", "" + tax + "   " + taxTextView.getText().toString());

                }

                voucherType=getVoucherTypeCurrent();

                if (netSales != 0 && !Double.isNaN(netSales)) {// test nan


                    if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1 && (discountValue / netSales) > mDbHandler.getAllSettings().get(0).getAmountOfMaxDiscount()) {
                        Toast.makeText(getActivity(), "You have exceeded the upper limit of the discount", Toast.LENGTH_SHORT).show();
                        SaveData.setEnabled(true);
                        save_floatingAction.setEnabled(true);

                    } else {
                        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;

                        String remark = " " + remarkEditText.getText().toString();
                        salesMan = Integer.parseInt(Login.salesMan.trim());

                        ///  1 add time

                        timevocher=getCurentTimeDate(2);






//                        voucher = new Voucher(0, voucherNumber, voucherType, voucherDate,
//                                salesMan, discountValue, discountPerc, remark, payMethod,
//                                0, totalDisc, subTotal, tax, netSales, CustomerListShow.Customer_Name,
//                                CustomerListShow.Customer_Account, Integer.parseInt(voucherYear),timevocher);
                        voucher=new Voucher();
                        voucher.setCompanyNumber(0);
                        voucher.setVoucherNumber(voucherNumber);
                        voucher.setVoucherType(voucherType);
                        voucher.setVoucherDate(voucherDate);
                        voucher.setSaleManNumber(salesMan);
                        voucher.setVoucherDiscount(discountValue);
                        voucher.setVoucherDiscountPercent(discountPerc);
//                        try {
//                            if(remark.trim().length()==0)
//                            {remark=VERSION;}
//                        }catch (Exception e){}

                        voucher.setRemark(remark);
                        voucher.setPayMethod(payMethod);
                        voucher.setIsPosted(0);
                        voucher.setTotalVoucherDiscount(totalDisc);
                        voucher.setSubTotal(subTotal);
                        voucher.setTax(tax);
                        voucher.setNetSales(netSales);
                        voucher.setCustName(CustomerListShow.Customer_Name);
                        voucher.setCustNumber(CustomerListShow.Customer_Account);
                        voucher.setVoucherYear(Integer.parseInt(voucherYear));
                        voucher.setTime(timevocher);
                        voucher.setORIGINALvoucherNo(voucherNumber);
                        discountValue=0;discountPerc=0;
                        obj_discountFragment.setDiscountValue(0);
                        obj_discountFragment.setDiscountPerc(0);
                        if(noTax==0)
                        voucher.setTaxTypa(2);
                        else {
                            if(mDbHandler.getAllSettings().get(0).getTaxClarcKind()==0)
                            {
                                voucher.setTaxTypa(0);
                            }else     voucher.setTaxTypa(1);
                        }

                        if(Exported_Tax==0)
                            voucher.setTaxTypa(3);
                        else {
                            if(noTax==0)
                                voucher.setTaxTypa(2);
                            else {
                                if(mDbHandler.getAllSettings().get(0).getTaxClarcKind()==0)
                                {
                                    voucher.setTaxTypa(0);
                                }else     voucher.setTaxTypa(1);
                            }
                        }

                        if (mDbHandler.getAllSettings().get(0).getCustomer_authorized() == 1) {

                            if (customer_is_authrized()) {


                                if (virefyMaxDescount()) {
                                    if (verifySerialListDosenotDuplicates()) {
                                        AddVoucher();
                                        clearLayoutData(0);
                                    } else {
                                        SaveData.setEnabled(true);
                                        save_floatingAction.setEnabled(true);
                                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText(getContext().getString(R.string.warning_message))
                                                .setContentText(getContext().getString(R.string.thereIsduplicatedSerial))
                                                .show();

                                    }



                                } else {
                                    SaveData.setEnabled(true);
                                    save_floatingAction.setEnabled(true);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(getResources().getString(R.string.app_confirm_dialog_exceedDis));
                                    builder.setTitle(getResources().getString(R.string.app_alert));
                                    builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();

                                        }
                                    });


                                    builder.create().show();


                                }//end else

                            } else {// customer Not authorize
                                SaveData.setEnabled(true);
                                save_floatingAction.setEnabled(true);
                                reCheck_customerAuthorize();// test
                            }
                        } else {// you should not authorize customer account balance


                            if (virefyMaxDescount()) {
                                if (verifySerialListDosenotDuplicates()) {
                                    AddVoucher();
                                    clearLayoutData(0);
                                } else {
                                    SaveData.setEnabled(true);
                                    save_floatingAction.setEnabled(true);
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(getContext().getString(R.string.warning_message))
                                            .setContentText(getContext().getString(R.string.thereIsduplicatedSerial))
                                            .show();

                                }


                            } else {
                                SaveData.setEnabled(true);
                                save_floatingAction.setEnabled(true);
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_exceedDis));
                                builder.setTitle(getResources().getString(R.string.app_alert));
                                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                    }
                                });


                                builder.create().show();


                            }//end else
                        }
                    }
                } else {// if tax ==0 or net sales==0 don't save data
                    SaveData.setEnabled(true);
                    save_floatingAction.setEnabled(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getResources().getString(R.string.zero_value_taxAndNetSales));
                    builder.setTitle(getResources().getString(R.string.warning_message));
                    builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }


                    });
                    builder.create().show();

                }

        }
        else {
            showCustomerNameDialog();
        }
    }

    private void showCustomerNameDialog() {
        final EditText editText = new EditText(getActivity());
        SweetAlertDialog sweetMessage= new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(getResources().getString(R.string.enterCustomerName));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(false);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(!editText.getText().toString().equals(""))
                {
                    customerTemporiryNmae="عميل نقدي";
                    CustomerListShow.Customer_Name=editText.getText().toString();
                    validCustomerName=true;
                    updatedName=true;

                   saveLocalData();
                    sweetAlertDialog.dismissWithAnimation();
                }
                else {
                    validCustomerName=false;
                    customerTemporiryNmae="";
                    editText.setError("Incorrect");
                }
            }
        }).setCancelButton(getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                SaveData.setEnabled(true);
                save_floatingAction.setEnabled(true);
                sweetAlertDialog.dismissWithAnimation();
            }
        })

                .show();
    }

    public int getVoucherTypeCurrent() {
    int checkedId=voucherTypeRadioGroup.getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.salesRadioButton:
                voucherType = 504;
                break;
            case R.id.retSalesRadioButton:
                voucherType = 506;
                break;
            case R.id.orderRadioButton:
                voucherType = 508;
                break;

        }
        return  voucherType;
    }

    private void refrechItemForReprint() {
        itemForPrintLast = new ArrayList<Item>();
        itemForPrintLast = mDbHandler.getAllItems(1);//test

    }

    View.OnClickListener RADIOCLECKED = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            vocherClick = true;
        }
    };


    private void refreshRadiogroup(int voucherType) {
        if (vocherClick) {
            switch (voucherType) {
                case 504:
                    vocherClick = false;
                    voucherTypeRadioGroup.check(R.id.salesRadioButton);
                    salesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                    retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                    orderRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));

//                                            salesRadioButton.setSelected(true);
//                                            retSalesRadioButton.setSelected(false);
//                                            orderRadioButton.setSelected(false);
                    break;
                case 506:
                    vocherClick = false;
                    voucherTypeRadioGroup.check(R.id.retSalesRadioButton);
                    retSalesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                    salesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                    orderRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));

//                                            salesRadioButton.setSelected(false);
//                                            retSalesRadioButton.setSelected(true);
//                                            orderRadioButton.setSelected(false);
                    break;
                case 508:
                    vocherClick = false;
                    voucherTypeRadioGroup.check(R.id.orderRadioButton);
                    orderRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
                    retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                       salesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));

//                                            salesRadioButton.setSelected(false);
//                                            retSalesRadioButton.setSelected(false);
//                                            orderRadioButton.setSelected(true);
                    paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    static Voucher vouchLast;
    public  static void refreshPayMethod(int payMent)
    {
        Log.e("refreshPayMethod",""+payMent);
        if(payMethod==1)
        {
            cash.setChecked(true);
        }
        else {
            credit.setChecked(true);
        }
    }
    private void printLastVoucher(int voucher_no, Voucher vouchPrint) {
        getTimeAndDate();
        if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {
            try {
                int printer = mDbHandler.getPrinterSetting();
                companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                if (!companyInfo.getCompanyName().equals("") && !companyInfo.getcompanyTel().equals("0")  && companyInfo.getTaxNo() != -1) {
                    switch (printer) {
                        case 0:
                            vouchLast = vouchPrint;
                            Intent i = new Intent(getActivity().getBaseContext(), BluetoothConnectMenu.class);
                            i.putExtra("printKey", "7");
                            startActivity(i);
//                                                             lk30.setChecked(true);
                            break;
                        case 1:

//                            try {
//                                findBT();
//                                openBT(1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
////                                                             lk31.setChecked(true);
//                            break;
                        case 2://                               try {
//                                   findBT();
//                                   openBT(2);
//                               } catch (IOException e) {
//                                   e.printStackTrace();
//                               }
////                                                             lk32.setChecked(true);
//                            voucher = vouchPrint;
////                            vouch1 = vouch;
////                            voucherPrint=vouch;
//
//
//                            convertLayoutToImagew(getActivity());
//                            Intent O1 = new Intent(getActivity().getBaseContext(), bMITP.class);
//                            O1.putExtra("printKey", "1");
//                            startActivity(O1);
//
//
//                            break;
                        case 3:

//                            try {
//                                findBT();
//                                openBT(3);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
////                                                             qs.setChecked(true);
//                            break;
                        case 4:
//                            printTally(vouchPrint);
//                            break;


                        case 5:

//                                                             MTP.setChecked(true);
                            vouchLast = vouchPrint;
//                            convertLayoutToImage(vouchPrint);

                            Intent O = new Intent(getActivity(), bMITP.class);
//                            getPackageManager().getLaunchIntentForPackage("com.android.chrome");

                            O.putExtra("printKey", "7");
                            O.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            O.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(O);


                            break;
                        case 6:// inner prenter
                        case 7:
//                                                             MTP.setChecked(true);
                            vouchLast = vouchPrint;
//                            convertLayoutToImage(vouchPrint);

                            Intent ineer = new Intent(getActivity().getBaseContext(), bMITP.class);
                            ineer.putExtra("printKey", "7");
                            ineer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ineer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(ineer);


                            break;

                    }
                } else {
//                   Toast.makeText(SalesInvoice.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();

            }


//                                                } catch (IOException ex) {
//                                                }
        } else {
            hiddenDialog();
        }


//        if (!obj.getAllCompanyInfo().get(0).getCompanyName().equals("") && obj.getAllCompanyInfo().get(0).getcompanyTel() != 0 && obj.getAllCompanyInfo().get(0).getTaxNo() != -1) {
//            if (obj.getAllSettings().get(0).getPrintMethod() == 0) {
////                                                     try {
//                Log.e("voucher", "  " + vouch.getVoucherNumber());
//                try {
//
//                    int printer = obj.getPrinterSetting();
//
//
//                    switch (printer) {
//                        case 0:
//                            vouch1 = vouch;
//                            Intent i = new Intent(PrintVoucher.this, BluetoothConnectMenu.class);
//                            i.putExtra("printKey", "0");
//                            startActivity(i);
//
////                                                             lk30.setChecked(true);
//                            break;
//                        case 1:
//
//                            try {
//                                findBT(Integer.parseInt(textView.getText().toString()),vouch);
//                                openBT(vouch, 1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
////                                                             lk31.setChecked(true);
//                            break;
//                        case 2:
//
////                                                                try {
////                                                                    findBT(Integer.parseInt(textView.getText().toString()));
////                                                                    openBT(vouch, 2);
////                                                                } catch (IOException e) {
////                                                                    e.printStackTrace();
////                                                                }
////                                                             lk32.setChecked(true);
//
//                            vouch1 = vouch;
//                            voucherPrint=vouch;
//                            convertLayoutToImageW();
//                            Intent o = new Intent(PrintVoucher.this, bMITP.class);
//                            o.putExtra("printKey", "0");
//                            startActivity(o);
//
//
//                            break;
//                        case 3:
//
//                            try {
//                                findBT(Integer.parseInt(textView.getText().toString()),vouch);
//                                openBT(vouch, 3);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
////                                                             qs.setChecked(true);
//                            break;
//
//                        case 4:
//                            printTally(vouch);
//                            break;
//
//                        case 5:
//
//                            vouch1 = vouch;
//                            voucherPrint=vouch;
//                            convertLayoutToImageW();
//                            Intent o1 = new Intent(PrintVoucher.this, bMITP.class);
//                            o1.putExtra("printKey", "0");
//                            startActivity(o1);
//
//
//                            break;
//
//                    }
//                }
//                catch(Exception e){
//                    Toast.makeText(PrintVoucher.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
//
//                }
//
//
////
////                                                         master(vouch);
////                                                     testB =convertLayoutToImage(v);
//
//
////                                                     } catch (IOException ex) {
////                                                     }
//            } else {
//                hiddenDialog(vouch);
//            }
//        } else {
//            Toast.makeText(PrintVoucher.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
//        }
    }

//    private void fillListItemJson() {
//        String s = "";
//        List<String> itemNoList = mDbHandler.getItemNumbersNotInPriceListD();// difference itemNo between tow table (CustomerPricess and priceListD)
//        jsonItemsList = new ArrayList<>();
//        jsonItemsList2 = new ArrayList<>();
//        jsonItemsList_intermidiate = new ArrayList<>();
//        String rate_customer = mDbHandler.getRateOfCustomer();  // customer rate to display price of this customer
//
//        if (mDbHandler.getAllSettings().get(0).getPriceByCust() == 0)
//            jsonItemsList = mDbHandler.getAllJsonItems(rate_customer);
//        else {
//            jsonItemsList2 = mDbHandler.getAllJsonItems2(rate_customer);//from customers pricess
//
//            size_firstlist = jsonItemsList2.size();
//            if (size_firstlist != 0) {
//                size_customerpriceslist = size_firstlist;
//
//                for (int k = 0; k < size_firstlist; k++) {
//                    jsonItemsList_intermidiate.add(jsonItemsList2.get(k));
//                }
//                //****************************************************************************************
//
//                jsonItemsList = mDbHandler.getAllJsonItems(rate_customer); // from price list d
//
//                for (int i = 0; i < jsonItemsList.size(); i++) {
//                    for (int j = 0; j < itemNoList.size(); j++)
//                        if (jsonItemsList.get(i).getItemNo().equals(itemNoList.get(j).toString())) {
//                            jsonItemsList_intermidiate.add(size_firstlist, jsonItemsList.get(i));
//                            size_firstlist++;
//
//
//                        } else {
//
//                        }
//
//                }
//                jsonItemsList = jsonItemsList_intermidiate;
//
//            } else {//  (Customer Pricesfor this customer==0)    ====== >>>>>     get data from priceListD
//                Log.e("jsonItemsList2size", "zero");
//                jsonItemsList = mDbHandler.getAllJsonItems(rate_customer);
//            }
//
////            Collections.sort(jsonItemsList<itemNoList>);
//
//        }
//    }

    class Task extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
//            for (int i = 0; i < 100; i++) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                publishProgress(i);
//            }


            salesInvoiceInterfaceListener.displayFindItemFragment2();
            return "items";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
          //  dialog_progress.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog_progress = new ProgressDialog(getActivity());
                dialog_progress.setCancelable(false);
                dialog_progress.setMessage(getResources().getString(R.string.loadingItem));
                dialog_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                dialog_progress.show();//test
            }
            catch (Exception e){

            }

        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            try {
                dialog_progress.dismiss();
            }
            catch (Exception e){}


            if (result != null) {

            } else {
                Toast.makeText(getActivity(), "Not able to fetch data ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMaxDiscountDialog() {
        double discount_voucher = 0;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.max_discount_info_dialog);

        TextView name = (TextView) dialog.findViewById(R.id.textView_customerName);
        TextView total = (TextView) dialog.findViewById(R.id.textView_total);
        TextView maxDisc = (TextView) dialog.findViewById(R.id.textview_maxDiscount);
        try {
            name.setText(CustomerListShow.Customer_Name);
            total.setText(convertToEnglish(decimalFormat.format(subTotal) + ""));
            maxDiscounr_value = mDbHandler.getMaxDiscValue_ForCustomer(CustomerListShow.Customer_Account);

            discount_voucher = ((maxDiscounr_value * subTotal) / 100);
            maxDisc.setText(convertToEnglish(decimalFormat.format(discount_voucher) + ""));
        } catch (Exception e)
        {

        }


//        maxDisc.setText("50 "+"JD");

        Button okButton = (Button) dialog.findViewById(R.id.okBut);
//        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut_discount);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();
    }


    public void reCheck_customerAuthorize() {
        SaveData.setEnabled(true);
        save_floatingAction.setEnabled(true);
        if(mDbHandler.getAllSettings().get(0).getApproveAdmin()==1)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.not_authoriz));
            builder.setTitle(getResources().getString(R.string.warning_message));
            builder.setPositiveButton(getResources().getString(R.string.makeRequest), new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    requestOverLimitCredit();
//
//                 dialogInterface.dismiss();
                }


            });
            builder.create().show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.not_authoriz));
            builder.setTitle(getResources().getString(R.string.warning_message));
            builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }

            });
            builder.create().show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestOverLimitCredit() {
        final Dialog dialog_request = new Dialog(getActivity());
        dialog_request.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_request.setCancelable(true);
        dialog_request.setContentView(R.layout.request_limit_cridet);
        request=new requestAdmin(getContext());
        final EditText noteEditText = (EditText) dialog_request.findViewById(R.id.noteEditText);
        final Button okButton = (Button) dialog_request.findViewById(R.id.okButton);
        final ImageView cancelButton = (ImageView) dialog_request.findViewById(R.id.cancelButton);
        checkState_LimitCredit=dialog_request.findViewById(R.id.checkState);
        requestDiscount = dialog_request.findViewById(R.id.requestDiscount);

        voucherValueText=dialog_request.findViewById(R.id.voucherValueText);
        voucherValueText.setText(netTotalTextView.getText().toString());
        mainRequestLinear=dialog_request.findViewById(R.id.mainRequestLinear);
        checkStateResult=dialog_request.findViewById(R.id.checkStateResult);
        defaultDiscount=dialog_request.findViewById(R.id.defaultDiscount);
        acceptDiscount=dialog_request.findViewById(R.id.acceptDiscount);
        rejectDiscount=dialog_request.findViewById(R.id.rejectDiscount);
        requestLinear=dialog_request.findViewById(R.id.requestLinear);
        okButton.setVisibility(View.GONE);
        checkState_LimitCredit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.toString().equals("1"))
                {
                    requestLinear.setVisibility(View.GONE);
                    checkStateResult.setText(getContext().getResources().getString(R.string.acceptedRequest));
                    acceptDiscount.setVisibility(View.VISIBLE);
                    defaultDiscount.setVisibility(View.GONE);
                    okButton.setVisibility(View.VISIBLE);
                    okButton.setEnabled(true);

                }
                else if(s.toString().equals("2"))
                {
                    requestLinear.setVisibility(View.GONE);
                    checkStateResult.setText(getContext().getResources().getString(R.string.rejectedRequest));
                    acceptDiscount.setVisibility(View.GONE);
                    defaultDiscount.setVisibility(View.GONE);
                    rejectDiscount.setVisibility(View.VISIBLE);


                }

            }
        });

        //************************************************************
        requestDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!voucherValueText.getText().toString().equals("")) {

                    try {
                        okButton.setEnabled(false);
                        noteEditText.setEnabled(false);
                        requestDiscount.setEnabled(false);
                        noteRequestLimit = noteEditText.getText().toString();
                        Log.e("getDataForDiscountTotal", "" + max_cridit + "\t" + voucherValueText.getText().toString() + "\t" + noteRequestLimit);
                        getDataForDiscountTotal(max_cridit + "", voucherValueText.getText().toString(), noteRequestLimit);

                        request.startParsing();
//
                    } catch (Exception e) {
                        Log.e("request", "" + e.getMessage());

                    }


//            DiscountFragment.this.dismiss();


                } else {
                    voucherValueText.setError("required");
                }


            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_request.dismiss();
                currentKey = "";
                currentKeyTotalDiscount="";
                keyCreditLimit="";
                Log.e("okButtononClick==",keyCreditLimit+" ");
                if(listSerialTotal.size()!=0)
                {
                    if (verifySerialListDosenotDuplicates()) {
                        AddVoucher();
                        clearLayoutData(0);
                    } else {
                        SaveData.setEnabled(true);
                        save_floatingAction.setEnabled(true);
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getContext().getString(R.string.warning_message))
                                .setContentText(getContext().getString(R.string.thereIsduplicatedSerial))
                                .show();

                    }
                }
                else {
                    AddVoucher();
                    clearLayoutData(0);
                }



            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKey = "";
                currentKeyTotalDiscount="";
                keyCreditLimit="";
                Log.e("cancelButton==",keyCreditLimit+" ");
                dialog_request.dismiss();
            }
        });
        dialog_request.show();
    }
    private void getDataForDiscountTotal(String cridetLimit, String totalVoucher,String note) {
        discountRequest = new RequestAdmin();
        if (mDbHandler.getAllSettings().size() != 0) {
            discountRequest.setSalesman_name(mDbHandler.getAllSettings().get(0).getSalesMan_name());
        } else {
            discountRequest.setSalesman_name("");
        }
        discountRequest.setSalesman_no(Login.salesMan);
        discountRequest.setCustomer_no(CustomerListShow.Customer_Account);
        discountRequest.setCustomer_name(CustomerListShow.Customer_Name);
        discountRequest.setAmount_value(cridetLimit);
        discountRequest.setTotal_voucher(totalVoucher + "");// if request for item not for all voucher
        discountRequest.setVoucher_no(voucherNumberTextView.getText().toString() + "");

        discountRequest.setKey_validation("");
        discountRequest.setNote(note);
        discountRequest.setRequest_type("100");
        discountRequest.setStatus("0");
        getTimeAndDate();
        discountRequest.setTime(time);
        discountRequest.setDate(voucherDate);
        discountRequest.setSeen_row("0");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void AddVoucher() {

//        if (verifySerialListDosenotDuplicates()) {
        int store_No = salesMan;
        voucherNumber = mDbHandler.getMaxSerialNumberFromVoucherMaster(voucherType) + 1;
        mDbHandler.addVoucher(voucher);
        for (int i = 0; i < items.size(); i++){
            for (int j = 0; j < listSerialTotal.size(); j++) {
                if(items.get(i).getItemNo().equals(listSerialTotal.get(j).getItemNo()))
                {
                    listSerialTotal.get(j).setCounterSerial(i+1);
                    listSerialTotal.get(j).setPriceItem(items.get(i).getPrice());
                }

            }
        }
        for (int j = 0; j < listSerialTotal.size(); j++) {
            mDbHandler.add_Serial(listSerialTotal.get(j));
        }

        savedState = 2;// addesd sucssesfulley
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).setORIGINALvoucherNo(voucherNumber);


            Item item = new Item(0, voucherYear, voucherNumber, voucherType, items.get(i).getUnit(),
                    items.get(i).getItemNo(), items.get(i).getItemName(),items.get(i).getQty(), items.get(i).getPrice(),
                    items.get(i).getDisc(), items.get(i).getDiscPerc(), items.get(i).getBonus(), items.get(i).getVoucherDiscount(),// was 0 in credit
                    items.get(i).getTaxValue(), items.get(i).getTaxPercent(), 0, items.get(i).getDescription(), items.get(i).getSerialCode()

            ,items.get(i).getWhich_unit(),items.get(i).getWhich_unit_str(),items.get(i).getWhichu_qty(),items.get(i).getEnter_qty()
            ,items.get(i).getEnter_price(),items.get(i).getUnit_barcode(),items.get(i).getORIGINALvoucherNo());

            totalQty_forPrint += items.get(i).getQty();
            itemsList.add(item);
            mDbHandler.addItem(item);
            itemForPrint.add(item);
           // mDbHandler.updatevoucherKindInSerialTable(voucherType, voucherNumber, store_No);
            // exportSerialToExcel(listSerialTotal, voucherNo);


            if (voucherType == 504&&(Purchase_Order==0))
                mDbHandler.updateSalesManItemsBalance1(items.get(i).getQty(), salesMan, items.get(i).getItemNo());
            else if (voucherType == 506||(Purchase_Order==1)) {
                mDbHandler.updateSalesManItemsBalance2(items.get(i).getQty(), salesMan, items.get(i).getItemNo());

            }

        }
        if(customerTemporiryNmae.equals("عميل نقدي"))
        {
            CustomerListShow.Customer_Name="عميل نقدي" ;
            customerTemporiryNmae="";
        }

        mDbHandler.setMaxSerialNumber(voucherType, voucherNumber);
        mDbHandler.updateVoucherNo(voucherNumber,voucherType,2);
        getTimeAndDate();



        if (mDbHandler.getAllSettings().get(0).getSaveOnly() == 0) {
            if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {

                try {
                    int printer = mDbHandler.getPrinterSetting();
                    companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                    if (!companyInfo.getCompanyName().equals("") && !companyInfo.getcompanyTel() .equals("0") && companyInfo.getTaxNo() != -1) {
                        switch (printer) {
                            case 0:
                                Intent i = new Intent(getActivity().getBaseContext(), BluetoothConnectMenu.class);
                                i.putExtra("printKey", "1");
                                startActivity(i);
//                                                             lk30.setChecked(true);
                                break;
                            case 1:

//                            try {
//                                findBT();
//                                openBT(1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                                voucherShow = voucher;
//
////                                   convertLayoutToImage(voucher);
//                                Intent intent1 = new Intent(getActivity().getBaseContext(), bMITP.class);
//                                intent1.putExtra("printKey", "1");
//                                startActivity(intent1);

//                                                             lk31.setChecked(true);
//                                break;
                            case 2:

//                               try {
//                                   findBT();
//                                   openBT(2);
//                               } catch (IOException e) {
//                                   e.printStackTrace();
//                               }
//                                                             lk32.setChecked(true);
//                                voucherShow = voucher;
//
////                                   convertLayoutToImage(voucher);
//                                Intent O1 = new Intent(getActivity().getBaseContext(), bMITP.class);
//                                O1.putExtra("printKey", "1");
//                                startActivity(O1);
//
//
//                                break;
                            case 3:

//                            try {
//                                findBT();
//                                openBT(3);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
                                 voucherShow = voucher;
//                                   convertLayoutToImage(voucher);
                                Intent inte3 = new Intent(getActivity().getBaseContext(), bMITP.class);
                                inte3.putExtra("printKey", "1");
                                inte3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                inte3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(inte3);//qs.setChecked(true);
                                break;
                            case 4:
                                printTally(voucher);
                                break;


                            case 5:

//                             MTP.setChecked(true);


                            case 6:

//                             InnerPrenter.setChecked(true);
                                voucherShow = voucher;
//                            convertLayoutToImage(voucher);
                                Intent O = new Intent(getActivity().getBaseContext(), bMITP.class);
                                O.putExtra("printKey", "1");
                                O.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                O.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(O);


                                break;

                        }
                    } else {
//                   Toast.makeText(SalesInvoice.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();


                        finishPrint.setText("finish");

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                    finishPrint.setText("finish");
                }


//                                                } catch (IOException ex) {
//                                                }
            } else {
                hiddenDialog();
            }
        } else {

            if (mDbHandler.getAllSettings().get(0).getQtyServer() == 1&&Purchase_Order==0) {
                try {
                    ExportJason exportJason = new ExportJason(getActivity());
//                    exportJason.startExportDatabase();
                    exportJason.startExport(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getContext().getString(R.string.succsesful))
                    .show();
        }


//    }
//        else {
//            SaveData.setEnabled(true);
//            save_floatingAction.setEnabled(true);
//            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText(getContext().getString(R.string.warning_message))
//                    .setContentText(getContext().getString(R.string.thereIsduplicatedSerial))
//                    .show();
//        }

    }

    private boolean verifySerialListDosenotDuplicates() {
        listSerialValue=new ArrayList<>();
        for (int i=0;i<listSerialTotal.size();i++)
        {
            listSerialValue.add(listSerialTotal.get(i).getSerialCode());
        }
        if(hasDuplicate(listSerialValue))
        {
           return  false;
        }
        else return true;


    }

//    private void verifySerialListDosenotDuplicates() {
//        findDuplicates(listSerialTotal);
//    }
//    public Set<Integer> findDuplicates(List<String> listContainingDuplicates)
//    {
//        final Set<Integer> setToReturn = new HashSet<>();
//        final Set<Integer> set1 = new HashSet<>();
//
//        for (Integer yourInt : listContainingDuplicates)
//        {
//            if (!set1.add(yourInt))
//            {
//                setToReturn.add(yourInt);
//            }
//        }
//        return setToReturn;
//    }

    private void exportSerialToExcel(List<serialModel> list,int voucheNum) {
        ExportToExcel exportToExcel=new ExportToExcel();
        exportToExcel.createExcelFile(getActivity(),"SerialVoucher"+voucheNum+".xls",3,list);
    }

    private boolean virefyMaxDescount() {
        double discount_voucher = 0;
        double discount_total_voucher = 0;
        maxDiscounr_value = mDbHandler.getMaxDiscValue_ForCustomer(CustomerListShow.Customer_Account);
        discount_voucher = ((maxDiscounr_value * subTotal) / 100);
        if (discount_voucher == 0)// الحد الاعلى لخصم الزبون =0 يسمح ببيعه
        {
            return true;
        }
        String disc = discTextView.getText().toString();
        if (disc != "") {
            discount_total_voucher = Double.parseDouble(disc);

        } else {
            discount_total_voucher = 0;
        }

        if (discount_total_voucher <= discount_voucher) {
            return true;
        }


        return false;

    }


    public void setListener(SalesInvoiceInterface listener) {
        this.salesInvoiceInterfaceListener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean customer_is_authrized() {
        if (cash.isChecked())
            return true;
        unposted_payment = 0;
        double unposted_sales_cash = 0, unposted_sales_credit = 0;

        List<Customer> customer_balance = mDbHandler.getCustomer_byNo(CustomerListShow.Customer_Account);
        for (int i = 0; i < customer_balance.size(); i++) {
            cash_cridit = Double.parseDouble(customer_balance.get(i).getCashCredit() + "");
            max_cridit = Double.parseDouble(customer_balance.get(i).getCreditLimit() + "");
        }


//        max_cridit = CustomerListShow.CreditLimit;
//        cash_cridit = CustomerListShow.CashCredit;
        // *******************************************************
        payment_unposted = mDbHandler.getAllPayments_customerNo(voucher.getCustNumber());
        for (int i = 0; i < payment_unposted.size(); i++) {
            if (payment_unposted.get(i).getIsPosted() == 0) {
                unposted_payment += payment_unposted.get(i).getAmount();

            }
        }
        // *******************************************************
        sales_voucher_unposted = mDbHandler.getAllVouchers_CustomerNo(voucher.getCustNumber());
        for (int j = 0; j < sales_voucher_unposted.size(); j++) {
            if (sales_voucher_unposted.get(j).getIsPosted() == 0) {
                if (sales_voucher_unposted.get(j).getPayMethod() == 0)// for credit
                    unposted_sales_credit += sales_voucher_unposted.get(j).getNetSales();
                else {
                    unposted_sales_cash += sales_voucher_unposted.get(j).getNetSales();
                }


            }
        }
        available_balance = max_cridit - cash_cridit - unposted_sales_credit + unposted_payment;
        if (available_balance >= voucher.getNetSales())
            return true;
        else
            return false;

    }

    float updaQty = 0, currentDisc = 0;
    float disount_totalnew = 0;
    Offers appliedOffer = null;

    public OnItemLongClickListener onItemLongClickListener =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.app_select_option));
                    builder.setCancelable(true);
                    builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                    if(items.get(position).getItemHasSerial().equals("1")){
                        String itemNoForDelete = items.get(position).getItemNo();
                        float priceCurrentItem=items.get(position).getPrice();
                        builder.setItems(R.array.list_items_dialog_SERIAL, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0://update  qty
                                        updatedSerial=1;
                                        addNewSerial=0;
                                        showDialogSerial(getContext(),position,itemNoForDelete,priceCurrentItem);
                                        break;
                                    case 1:// delete item
                                        //copyListSerial.clear();

//                                        total_items_quantity -= items.get(position).getQty();
//                                        totalQty_textView.setText("+" + total_items_quantity);
                                        minusQtyTotal(items.get(position).getQty());

                                        if(mDbHandler.getAllSettings().get(0).getWork_serialNo()==1||items.get(position).getItemHasSerial().equals("1"))
                                        {

                                            try {

                                               updateListSerialBukupDeleted( itemNoForDelete,voucherNumberTextView.getText().toString());


//                                                mDbHandler.deletSerialItems_byItemNo(items.get(position).getItemNo());
                                               // Log.e("listSerialTotal","copyListSerial.size()"+copyListSerial.size()+"\tlistSerialTotal="+listSerialTotal.size());
                                                if(listSerialTotal.size()!=0)
                                                {
                                                    int k=0;
                                                    while(k<listSerialTotal.size()){
                                                        if((listSerialTotal.get(k).getItemNo().equals(itemNoForDelete))&&(listSerialTotal.get(k).getPriceItem()==priceCurrentItem))
                                                        {

                                                            listSerialTotal.remove(k);

                                                        }else {k++;}
                                                    }

//                                                    for(int k=0;k<listSerialTotal.size();k++)
//                                                    {
//                                                            if((listSerialTotal.get(k).getItemNo().equals(itemNoForDelete))&&(listSerialTotal.get(k).getPriceItem()==priceCurrentItem))
//                                                            {
//
//                                                                listSerialTotal.remove(k);
//                                                                if(k!=0)
//                                                                k--;
//                                                            }
////
//
//                                                    }
                                                }


                                            }
                                            catch (Exception e)
                                            {}


                                        }

                                        items.remove(position);

                                        itemsListView.setAdapter(itemsListAdapter);
                                        calculateTotals(1);

//                                    clearLayoutData();

                                        break;

                                    case 2:// clear all


                                        updateListSerialBukupDeleted("",voucherNo+"");
                                        clearItemsList();
                                        clearLayoutData(1);
                                        total_items_quantity = 0;
                                        total_items_quantity = 0;
                                        totalQty_textView.setText("+" + total_items_quantity);
                                        break;
                                }
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                    else{
                        builder.setItems(R.array.list_items_dialog, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        String st = "";
                                        total_items_quantity -= items.get(position).getQty();
                                        totalQty_textView.setText("+" + total_items_quantity);

                                        removeItem(position);

                                       // items.remove(position);


                                        itemsListView.setAdapter(itemsListAdapter);
                                        calculateTotals(1);

//                                    clearLayoutData();

                                        break;
                                    case 1:
                                        if(!items.get(position).getItemName().equals("(bonus)"))
                                        showDialogUpdateAmountRegular(position);
//

                                        break;
                                    case 2:
                                        updateListSerialBukupDeleted("",voucherNo+"");
                                        clearItemsList();
                                        clearLayoutData(1);
                                        total_items_quantity = 0;
                                        totalQty_textView.setText("+" + total_items_quantity);
                                        break;
                                }
                            }
                        });
                        builder.create().show();
                        return true;
                    }

                }
            };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showDialogUpdateAmountRegular(int position) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.update_qty_dialog);

        final EditText qty = (EditText) dialog.findViewById(R.id.editText1);
        final EditText price_update = (EditText) dialog.findViewById(R.id.price_update);
        final EditText discount_update = (EditText) dialog.findViewById(R.id.discount_update);

        LinearLayout mainLineardialog=dialog.findViewById(R.id.mainLineardialog);
        Button okButton = (Button) dialog.findViewById(R.id.button1);
        Button cancelButton = (Button) dialog.findViewById(R.id.button2);
        int itemUnit=1;
        final RadioGroup discTypeRadioGroup_up= dialog.findViewById(R.id.discTypeRadioGroup_up);
        if(mDbHandler.getAllSettings().get(0).getItems_Unit()==1)
        {
            try {
                if(!items.get(position).getWhichu_qty().equals(""))
                    itemUnit=(int)(Double.parseDouble(items.get(position).getWhichu_qty()));
            }catch (Exception e){
                Log.e("itemList***","getItems_Unit= "+e.getMessage());
            }

        }else itemUnit=mDbHandler.getUnitForItem(items.get(position).getItemNo());

        if(mDbHandler.getAllSettings().get(0).getItemUnit()==1&&items.get(position).getOneUnitItem().equals("0"))
        {
            if(itemUnit!=0)
            qty.setText(items.get(position).getQty()/itemUnit+"");
            else {
                qty.setText(items.get(position).getQty()+"");
            }

        }else {
            qty.setText(items.get(position).getQty()+"");
        }
        if(mDbHandler.getAllSettings().get(0).getItemUnit()==1&&items.get(position).getOneUnitItem().equals("0"))

        {   if(itemUnit!=0)
            price_update.setText((items.get(position).getPrice()*itemUnit)+"");

        }
        else {
            price_update.setText(items.get(position).getPrice()+"");
        }
        if (mDbHandler.getAllSettings().get(0).getCanChangePrice_returnonly() == 1) {
            if (voucherType == 506) {
                price_update.setEnabled(true);
            }
            else {
                price_update.setEnabled(false);
                price_update.setAlpha(0.8f);
            }
        }else {
            if(mDbHandler.getAllSettings().get(0).getCanChangePrice()==0)
            {
                price_update.setEnabled(false);
                price_update.setAlpha(0.8f);
            }
        }

        if(mDbHandler.getAllSettings().get(0).getApproveAdmin()==1){
            discount_update.setEnabled(false);
            price_update.setAlpha(0.8f);
            discTypeRadioGroup_up.setEnabled(false);
//            discValueRadioButton_update.set
        }

        discount_update.setText(items.get(position).getDisc()+"");
        try {
            if (languagelocalApp.equals("ar")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mainLineardialog.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }
            } else {
                if (languagelocalApp.equals("en")) {
                    mainLineardialog.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
//
        } catch (Exception e) {
            mainLineardialog.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        if(items.get(position).getDiscType()==1)// percent
        {

            discount_update.setText(items.get(position).getDiscPerc()+"");
            discTypeRadioGroup_up.check(R.id.discPercRadioButton_update);


        }
        else {
            discount_update.setText(items.get(position).getDisc()+"");
            // discTypeRadioGroup_up= dialog.findViewById(R.id.discTypeRadioGroup_up);
            discTypeRadioGroup_up.check(R.id.discValueRadioButton_update);


        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float availableQty = 0, priceValue=0;
                if (!qty.getText().toString().equals("")) {
                    if (!qty.getText().toString().equals(".")) {
                        if (Float.parseFloat((qty.getText().toString().trim()))!=0) {
                            disount_totalnew=0;

                            List<Item> jsonItemsList_insal = jsonItemsList;
                            for (int i = 0; i < jsonItemsList.size(); i++) {
                                if (items.get(position).getItemNo().equals(jsonItemsList.get(i).getItemNo())) {
                                    availableQty = jsonItemsList.get(i).getQty();

                                    break;
                                }
                            }
                            if (mDbHandler.getAllSettings().get(0).getAllowMinus() == 1 ||
                                    availableQty >= Float.parseFloat(qty.getText().toString()) ||
                                    voucherType == 506) {
                                total_items_quantity -= items.get(position).getQty();

                                try {
                                    priceValue=Float.parseFloat((price_update.getText().toString().trim()));
                                    String itemNumber=items.get(position).getItemNo();
                                    //***************************************************
                                    if(mDbHandler.getAllSettings().get(0).getItemUnit()==1
                                            &&items.get(position).getOneUnitItem().equals("0"))
                                    {// case unit




                                        int itemUnit=1;
                                        if(mDbHandler.getAllSettings().get(0).getItems_Unit()==1)
                                        {
                                            try {
                                                if(!items.get(position).getWhichu_qty().equals(""))
                                                    itemUnit=(int)(Double.parseDouble(items.get(position).getWhichu_qty()));
                                            }catch (Exception e){
                                                Log.e("itemList***","getItems_Unit= "+e.getMessage());
                                            }

                                        }else itemUnit=mDbHandler.getUnitForItem(items.get(position).getItemNo());


                                        items.get(position).setQty(Float.parseFloat(qty.getText().toString().trim())*itemUnit);

                                        if(itemUnit!=1)
                                        {
                                            items.get(position).setEnter_price(priceValue+"");
                                            float priceUnitItem=priceValue/itemUnit;
                                            items.get(position).setPrice(priceUnitItem);

                                            items.get(position).setEnter_qty((qty.getText().toString().trim()));

                                        }
                                        else {
                                            items.get(position).setPrice(priceValue);
                                            items.get(position).setEnter_price(priceValue+"");
                                            items.get(position).setEnter_qty((qty.getText().toString().trim()));
                                        }
                                    }else {



                                        items.get(position).setPrice(priceValue);
                                        items.get(position).setQty(Float.parseFloat(qty.getText().toString().trim()));
                                        items.get(position).setEnter_qty((qty.getText().toString().trim()));
                                        items.get(position).setEnter_price((priceValue)+"");
                                    }



                                    //***************************************************

                                    updaQty = Float.parseFloat(qty.getText().toString().trim());



                                } catch (Exception e) {

                                }
                                //********************** update Discount ****************************
                                float discountNew=0;
                                try {
                                    discountNew=Float.parseFloat(discount_update.getText().toString().trim());

                                    if(discountNew!=100)
                                    {
                                        //radioDiscountSerial= dialog.findViewById(R.id.discTypeRadioGroup);

                                        if (discTypeRadioGroup_up.getCheckedRadioButtonId()==R.id.discPercRadioButton_update) {
                                            items.get(position).setDiscType(1);// error for discount promotion // percent discount

                                        } else {
                                            items.get(position).setDiscType(0);// value Discount

                                        }

                                        if (items.get(position).getDiscType() == 0) {// value

                                            items.get(position).setDisc((discountNew));
                                            items.get(position).setDiscPerc( items.get(position).getQty()*items.get(position).getPrice()*(discountNew/100)+"");
                                        } else {// percent
                                            // percentOld=Float.parseFloat(items.get(position).getDiscPerc());
                                            items.get(position).setDiscPerc(discountNew+"");
                                            items.get(position).setDisc(items.get(position).getQty()*items.get(position).getPrice()*(discountNew/100));



                                        }

                                    }

                                }catch (Exception e){

                                }

                                List<Offers> offer = checkOffers(items.get(position).getItemNo());
                                if (offer.size() > 0&&voucherType==504) {
                                    appliedOffer = getAppliedOffer(items.get(position).getItemNo(), updaQty + "", 1);
                                    Log.e("appliedOffer",""+appliedOffer.getItemNo()+"offer="+offer.size());
                                    if (!appliedOffer.getItemNo().equals("-1"))
                                    {


                                        double bonus_calc = 0;
                                        int discOfferType=0;
                                       // Log.e("getPromotionType()", "2====" + appliedOffer.getBonusQty()+"\tgetPromotionType+"+offer.get(0).getPromotionType());

                                        if (offer.get(0).getPromotionType() == 0) {// bunos
                                            if (OfferCakeShop == 0) {
                                                bonus_calc = ((int) (updaQty / appliedOffer.getItemQty())) * appliedOffer.getBonusQty();

                                            } else {
                                                bonus_calc = appliedOffer.getBonusQty();
                                            }
                                          //  Log.e("bonus_calc=", "added1" + bonus_calc+"\tposition="+position);
                                            if((position+1 )!= items.size())
                                            {
                                                if(items.get(position+1).getItemName().equals("(bonus)"))
                                                {
                                                    items.get(position+1).setQty(Float.parseFloat(bonus_calc+""));
                                                }
                                                else {
                                                    addItemBonus(position,offer.get(0).getBonusItemNo(),bonus_calc);



                                                }
                                            }else {
                                                addItemBonus(position,offer.get(0).getBonusItemNo(),bonus_calc);
                                            }



                                        }
                                        else {
                                            Log.e("appliedOffer","getPromotionType="+offer.get(0).getPromotionType());
                                            items.get(position).setDisc(0);
                                            items.get(position).setDiscPerc("0");
                                            // promotion type==1 ++++++>>discount offer
                                            discOfferType=appliedOffer.getDiscountItemType();
                                            items.get(position).setDiscType(discOfferType);

                                            if(offerQasion==1){
                                                disount_totalnew=Float.parseFloat(appliedOffer.getBonusQty()+"")*updaQty;

                                            }
                                            else {
                                                if(offerTalaat==1)
                                                {
                                                    disount_totalnew = Float.parseFloat((((int) (updaQty / appliedOffer.getItemQty())) * appliedOffer.getBonusQty())+"");
                                                } else {
                                                    disount_totalnew=Float.parseFloat(appliedOffer.getBonusQty()+"")*updaQty;
                                                }
                                            }



                                        }
                                    }
                                    else {
                                        // remove items not in offer (bonus item)
//                                        items.get(position+1)
                                           if((position+1 )!= items.size()) {
                                            if (items.get(position + 1).getItemName().equals("(bonus)")) {
                                                items.remove(position + 1);
                                            }
                                        }
                                        disount_totalnew=0;
                                        items.get(position).setDisc(0);
                                        items.get(position).setDiscPerc("0");

                                    }
                                }
                                Log.e("appliedOffer","getDisc="+ items.get(position).getDisc()+"\t"+disount_totalnew);


                                total_items_quantity += items.get(position).getQty();
                                totalQty_textView.setText("+" + total_items_quantity);
                                if(disount_totalnew!=0)
                                {
                                    if (items.get(position).getDiscType() == 0)// value
                                    {
                                        items.get(position).setDisc(disount_totalnew);
                                        items.get(position).setDiscPerc((items.get(position).getQty()*items.get(position).getPrice()*(disount_totalnew/100))+"");

                                    }
                                    else{
                                        items.get(position).setDiscPerc(disount_totalnew+"");
                                        items.get(position).setDisc(items.get(position).getQty()*items.get(position).getPrice()*(disount_totalnew/100));

                                    }
                                }
                                items.get(position).setAmount(items.get(position).getQty() * items.get(position).getPrice() - items.get(position).getDisc());


                                  Log.e("updateAmount1=","getAmount="+items.get(position).getAmount());
                              //  Log.e("updateAmount1=","getDisc="+items.get(position).getDisc());
                              //  Log.e("updateAmount1=","getDiscPerc="+items.get(position).getDiscPerc());
                                calculateTotals(0);
                                itemsListView.setAdapter(itemsListAdapter);

                                dialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), ""+getActivity().getResources().getString(R.string.insufficientQuantity), Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        } else {
                            qty.setError("Invalid Zero");
                        }
                    }else {qty.setError("Invalid . ");}
                }

                else {
                    qty.setError("Required");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addItemBonus(int position, String bonusItemNo, double bonus_calc) {
        Item item_bonus=new Item();
        item_bonus.setQty(Float.parseFloat(bonus_calc+""));
        item_bonus.setItemNo(bonusItemNo);
        item_bonus.setItemName("(bonus)");
        item_bonus.setPrice(0);
        item_bonus.setCategory("");
        item_bonus.setBonus(0);
        item_bonus.setDisc(0);
        item_bonus.setAmount(0);
        item_bonus.setKind_item("");
        item_bonus.setItemHasSerial("0");
        item_bonus.setBarcode("");
        item_bonus.setCurrentQty(0);
        item_bonus.setDate("");
        item_bonus.setCust("");
        item_bonus.setDescreption("");


        item_bonus.setOneUnitItem("0");

        items.add(position+1,item_bonus);

    }

    private void removeItem(int position) {
        if(items.size()>1)
        {
            if(position+1!=items.size())// not last element
            {
                if(items.get(position+1).getItemName().equals("(bonus)"))
                {
                    items.remove(position);

                }

            }

        }
        items.remove(position);

    }

    boolean isFoundSerial=false; serialModel serial;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showDialogSerial(Context context, int position, String itemNo, float priceUpdated) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_item_serial_dialog);

        editOpen = true;
        addSerial.setEnabled(true);

        TextView item_number, item_name, discount,pricee_label;
        EditText bonus;
        LinearLayout bonusLinearLayout, _linear_switch, discount_linear, linear_bonus, mainRequestLinear, mainLinearAddItem, linearPrice, linearUnit;
        listTemporarySerial = new ArrayList<>();
        if(addNewSerial==0)
        listTemporarySerial = getserialForItem(itemNo, priceUpdated);
        Log.e("listTemporarySerial","="+listTemporarySerial.size());
        discPercRadioButton = dialog.findViewById(R.id.discPercRadioButton);
        discValueRadioButton = dialog.findViewById(R.id.discValueRadioButton);
        radioDiscountSerial = dialog.findViewById(R.id.discTypeRadioGroup);
        radioDiscountSerial.setVisibility(View.VISIBLE);
        linearPrice = dialog.findViewById(R.id.linearPrice);

        linearUnit = dialog.findViewById(R.id.linearUnit);


        //radioDiscountSerial.setEnabled(false);
        itemNoSelected = itemNo;


        //**********************************************
        int current_itemHasSerial = 1;

        dialog.setContentView(R.layout.add_item_serial_dialog);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(context);
        ImageView addEditBarcode;
        serial_No_recyclerView = dialog.findViewById(R.id.serial_No_recyclerView);
        serial_No_recyclerView.setLayoutManager(layoutManager);
        Log.e("listTemporarySerial","2="+listTemporarySerial.size());
        if (listTemporarySerial.size() != 0) {
            serial_No_recyclerView.setAdapter(new Serial_Adapter(listTemporarySerial, context));
        }

        mainRequestLinear_serial = dialog.findViewById(R.id.mainRequestLinear);
        mainLinearAddItem = dialog.findViewById(R.id.mainLinearAddItem);
        if (mDbHandler.getAllSettings().get(0).getContinusReading() == 1) {

            contiusReading = 1;
            Log.e("contiusReading", "update===" + contiusReading);
        }

        try {
            if (languagelocalApp.equals("ar")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mainLinearAddItem.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }
            } else {
                if (languagelocalApp.equals("en")) {
                    mainLinearAddItem.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
//
        } catch (Exception e) {
            mainLinearAddItem.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        TextView textQty;
        mainRequestLinear = dialog.findViewById(R.id.mainRequestLinear);
        textQty = dialog.findViewById(R.id.textQty);
        unitQtyEdit = dialog.findViewById(R.id.unitQty);
        discount_linear = dialog.findViewById(R.id.discount_linear);
        linear_bonus = dialog.findViewById(R.id.linear_bonus);
        addEditBarcode = dialog.findViewById(R.id.addEditBarcode);

        discount = dialog.findViewById(R.id.discount);
        item_number = dialog.findViewById(R.id.item_number);
        item_name = dialog.findViewById(R.id.item_name);
        price_serial_edit = dialog.findViewById(R.id.price);
        price_serial_edit.setEnabled(false);
        price_serial_edit.setAlpha(0.8f);
        _linear_switch = dialog.findViewById(R.id._linear_switch);
        checkStateResult = dialog.findViewById(R.id.checkStateResult);
        rejectDiscount = dialog.findViewById(R.id.rejectDiscount);
        mainRequestLinear_serial.setVisibility(View.VISIBLE);
        serialValueUpdated = dialog.findViewById(R.id.serialValue);
        mainLinear = dialog.findViewById(R.id.mainLinearAddItem);
        bonusLinearLayout = dialog.findViewById(R.id.linear_bonus);
        Button addToList = dialog.findViewById(R.id.addToList);
        Button cancel = dialog.findViewById(R.id.cancelAdd);
        if(jsonItemsList.size()==0){
            getAllItems();
        }



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOpen = false;
                updatedSerial = 0;
                addNewSerial=0;
                updateQtyBasket();
                dialog.dismiss();

            }
        });
        bonus = dialog.findViewById(R.id.bonus);
        Log.e("addNewSerial","="+addNewSerial);
        if (addNewSerial == 1) {
            discount_linear.setVisibility(View.GONE);
            linear_bonus.setVisibility(View.GONE);
            _linear_switch.setVisibility(View.GONE);
            linearPrice.setVisibility(View.GONE);
            linearUnit.setVisibility(View.GONE);
            item_name.setVisibility(View.GONE);
            item_number.setVisibility(View.GONE);
            price_serial_edit.setVisibility(View.GONE);
            linearUnit.setVisibility(View.GONE);
            pricee_label= dialog.findViewById(R.id.pricee);
            pricee_label.setVisibility(View.GONE);

        }
        if (addNewSerial == 0) {


        if (items.get(position).getDiscType() == 1)// percent
        {
            Log.e("discountP************", "1 \t" + items.get(position).getDisc() + "\t getDiscType" + items.get(position).getDiscType() + "\t getDiscPerc" + items.get(position).getDiscPerc());

            discount.setText(items.get(position).getDiscPerc() + "");
            radioDiscountSerial = dialog.findViewById(R.id.discTypeRadioGroup);
            radioDiscountSerial.check(R.id.discPercRadioButton);


        } else {
            Log.e("discountV************", "2 \t" + items.get(position).getDisc() + "\t getDiscType" + items.get(position).getDiscType() + "\t getDiscPerc" + items.get(position).getDiscPerc());
            discount.setText(items.get(position).getDisc() + "");
            radioDiscountSerial = dialog.findViewById(R.id.discTypeRadioGroup);
            radioDiscountSerial.check(R.id.discValueRadioButton);


        }
            item_number.setText(items.get(position).getItemNo()+"");

            item_name.setText(items.get(position).getItemName());
            price_serial_edit.setText((items.get(position).getPrice()+""));
    }
        mainRequestLinear.setVisibility(View.GONE);
       // discount_linear.setVisibility(View.GONE);

        linear_bonus.setVisibility(View.GONE);


       countNormalQty= getNumberOfNormalQty(listTemporarySerial);
        unitQtyEdit.setText(countNormalQty+"");
        Log.e("unitQtyEdit","1="+countNormalQty);
        Log.e("unitQtyEdit","2="+counterSerial);
        textQty.setText(context.getResources().getString(R.string.qty));
        unitQtyEdit.setEnabled(false);
        unitQtyEdit.setAlpha(0.8f);
        counterSerial=countNormalQty;
        _linear_switch.setVisibility(View.GONE);

        if(contiusReading==0) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    serialValueUpdated.requestFocus();

                }
            });
            serialValueUpdated.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_NULL) {
                        if (!serialValueUpdated.getText().toString().equals("")) {

                                Log.e("listTemporarySerial5",""+listTemporarySerial.size());
                                Log.e("afterTextChanged","serialValueUpdated"+serialValueUpdated.getText().toString());
                                barcodeValue=serialValueUpdated.getText().toString().trim();
                                if(validbarcodeValue(barcodeValue,listTemporarySerial,(counterSerial-1)))
                                {

                                    addToList.setVisibility(View.VISIBLE);
                                    addToList.setEnabled(true);
                                    unitQtyEdit.setEnabled(false);
                                    // flag = 1;
                                    Log.e("itemNo",""+itemNo+"\t"+position);
                                    fillSerial(context ,itemNo,position);

                                }
                                else {
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {

                                            serialValueUpdated.setText("");
                                            serialValueUpdated.setError("invalid");
                                            serialValueUpdated.requestFocus();

                                        }
                                    });

                                }
                                // serialValue_Model.setText(s.toString().trim());


                        }

                    }
                    return false;
                }

                                                         }

            );
        }else {
            serialValueUpdated.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @SuppressLint("WrongConstant")
                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals(""))
                    {
//                        Log.e("listTemporarySerial5",""+listTemporarySerial.size());
//                        Log.e("afterTextChanged","serialValueUpdated"+serialValueUpdated.getText().toString());
                        barcodeValue=s.toString().trim();
                        if(validbarcodeValue(barcodeValue,listTemporarySerial,(counterSerial-1)))
                        {
                            addToList.setVisibility(View.VISIBLE);
                            addToList.setEnabled(true);
                            unitQtyEdit.setEnabled(false);
                            // flag = 1;
                            fillSerial(context ,itemNo,position);
                        }
                        else {
                            serialValueUpdated.setError("invalid");
                        }
                        // serialValue_Model.setText(s.toString().trim());

                    }


                }
            });
        }




        bonusLinearLayout.setVisibility(View.GONE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        bonus.setEnabled(false);

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyNotEmpty(listTemporarySerial)) {
                    countNormalQty = getNumberOfNormalQty(listTemporarySerial);
                    if (addNewSerial == 1 && updatedSerial == 0) {
                        addNewSerial = 0;
                        addSerialToItems();
                        addForListMasterSerial(listTemporarySerial);
                        updateQtyBasket();// check item amount
                        refreshDataItems();
                        calculateTotals(0);
                        editOpen = false;
                        dialog.dismiss();

                    } else {

                    if (updateListSerialTotal(listTemporarySerial, position, priceUpdated, discount.getText().toString())) {
                        editOpen = false;
                        updatedSerial = 0;
                        dialog.dismiss();
                    }
                }

                }else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getActivity().getString(R.string.warning_message))
                            .setContentText(getActivity().getString(R.string.reqired_filled))
                            .show();
                }


            }
        });

        final LinearLayout unitWeightLinearLayout = dialog.findViewById(R.id.linearWeight);
        unitWeightLinearLayout.setVisibility(View.GONE);
       EditText item_serial = dialog.findViewById(R.id.item_serial);
        final ImageView serialScanBunos = dialog.findViewById(R.id.serialScanBunos);
        final ImageView serialScan = dialog.findViewById(R.id.serialScan);


        serialScan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                openSmallScanerTextView();
//

            }
        });
        serialScanBunos.setVisibility(View.GONE);
        TextView generateSerial = (TextView) dialog.findViewById(R.id.generateSerial);
        generateSerial.setVisibility(View.GONE);
        item_serial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("WrongConstant")
            @Override
            public void afterTextChanged(Editable s) {
//
//                                    flag = 1;
                isFoundSerial = false;

                for (int h = 0; h < serialListitems.size(); h++) {

                    if (serialListitems.get(h).getSerialCode().equals(s.toString())) {
                        isFoundSerial = true;
                    }
                }
                Log.e("isFoundSerial", "" + isFoundSerial + s.toString());
                if (isFoundSerial == false) {
                    List<serialModel> listitems_adapter = new ArrayList<>();
                    int id = ((Serial_Adapter) serial_No_recyclerView.getAdapter()).selectedBarcode;
                    listitems_adapter = ((Serial_Adapter) serial_No_recyclerView.getAdapter()).list;

                    Log.e("curentSerial", "" + id + s.toString());



                    serial_No_recyclerView.setLayoutManager(layoutManager);

                        listitems_adapter.get(id).setSerialCode(s.toString());
                        Log.e("listitems_adapter", "" + s.toString());



                    serial_No_recyclerView.setAdapter(new Serial_Adapter(listitems_adapter, context));

                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.itemadedbefor))
                            .show();
                }

            }
        });


        addEditBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openeditDialog();
            }
        });
        dialog.show();



    }

     private void getAllItems() {// test
        String s = "";
        String dateCurent=getCurentTimeDate(1);
        String rate_customer = mDbHandler.getRateOfCustomer();
        if(rate_customer.equals(""))
        {
            rate_customer="0";

        }
       int   countListVisible = mDbHandler.getCountVisibleItemSize();
        Log.e("fillListItemJson","rate_customer="+rate_customer);// customer rate to display price of this customer
        Log.e("jsonItemsList","countListVisible="+countListVisible);
            jsonItemsList=new ArrayList<>();

            jsonItemsList = mDbHandler.getAllJsonItems(rate_customer,1,countListVisible);

            Log.e("jsonItemsList", "="+jsonItemsList.size());
         Log.e("jsonItemsList", "="+jsonItemsList.get(jsonItemsList.size()-1).getQty());


    }

    private void  refreshDataItems() {
        Log.e("refreshDataItems","items="+items.size());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            itemsListAdapter = new ItemsListAdapter(getActivity(), items,0);// if screen is landscape =====> 0

        } else {
            // portrait
            itemsListAdapter = new ItemsListAdapter(getActivity(), items,1);// if screen is landscape =====> 0

        }
        itemsListView.setAdapter(itemsListAdapter);


    }

    private void addSerialToItems() {
        for(int i=0;i<listTemporarySerial.size();i++) {

            if (itemsContain(listTemporarySerial.get(i).getItemNo(),i)) {
            } else {

                String itemNumber="",itemName,price;
                Item item=getItemByNumber(listTemporarySerial.get(i).getItemNo());

                if(item!=null){
                    item.setQty(1);
                    if (priceByCustomer==1) {// for ejabi ***** test
                        String priceCus = mDbHandler.getItemPrice(item.getItemNo());
                        Log.e("priceCus",""+priceCus+"\t"+item.getItemNo());
                        if (!priceCus.equals("")) {

                            item.setPrice(Float.parseFloat(priceCus));
                        }

                    }
                    item.setAmount(1*item.getPrice());
                    item.setUnit("1");
                    item.setDiscPerc("0");
                    item.setDescreption("");
                    item.setWhich_unit("0");
                    item.setWhich_unit_str("");
                    item.setEnter_qty(item.getQty()+"");
                    item.setWhichu_qty(item.getQty()+"");
                    item.setEnter_price(item.getPrice()+"");
                    item.setUnit_barcode("");
//                    Log.e("item","1***"+item.getItemName()+"\t"+  listTemporarySerial.get(i).getPriceItem());
                    listTemporarySerial.get(i).setPriceItem(item.getPrice());
                    listTemporarySerial.get(i).setQty("1");
                    listTemporarySerial.get(i).setIsPosted("0");
                    listTemporarySerial.get(i).setIsBonus("0");
                    listTemporarySerial.get(i).setPriceItemSales(item.getPrice()+"");
                    items.add(item);
//                    Log.e("item","1***"+item.getItemName()+"\t"+ listTemporarySerial.get(i).getPriceItem());
                }else {//test here
                     }


        }
        }
    }

    private Item getItemByNumber(String itemNo) {
        Item selectItem=null;
        for(int i=0;i<jsonItemsList.size();i++)
        {
            if(  jsonItemsList.get(i).getItemNo().trim().equals(itemNo.trim()))
            {   selectItem=new Item();
                selectItem.setItemNo(jsonItemsList.get(i).getItemNo().trim());
                selectItem.setItemName(jsonItemsList.get(i).getItemName().trim());
                selectItem.setWhich_unit_str("");
                selectItem.setQty(jsonItemsList.get(i).getQty());
                selectItem.setItemHasSerial(jsonItemsList.get(i).getItemHasSerial().trim());
                selectItem.setAmount(jsonItemsList.get(i).getAmount());
                selectItem.setTax(jsonItemsList.get(i).getTaxPercent());
                selectItem.setTaxPercent(jsonItemsList.get(i).getTaxPercent());
                selectItem.setTaxValue(jsonItemsList.get(i).getTaxValue());
                selectItem.setBarcode(jsonItemsList.get(i).getBarcode());
                selectItem.setWhich_unit("");
                selectItem.setAvi_Qty(jsonItemsList.get(i).getAvi_Qty());
                selectItem.setBonus(jsonItemsList.get(i).getBonus());
                selectItem.setDisc(jsonItemsList.get(i).getDisc());
                selectItem.setDiscPerc(jsonItemsList.get(i).getDiscPerc());
                selectItem.setCategory(jsonItemsList.get(i).getCategory().trim());
                selectItem.setDiscType(jsonItemsList.get(i).getDiscType());
                selectItem.setPosPrice(jsonItemsList.get(i).getPosPrice());
                selectItem.setPrice(jsonItemsList.get(i).getPrice());
                selectItem.setMinSalePrice(jsonItemsList.get(i).getMinSalePrice());
                Log.e("selectItem==","selectItem");
//                Log.e("getItemByNumber","itemNo="+itemNo+"\t"+jsonItemsList.get(i).getItemName());
//                Log.e("getItemByNumber","getQty="+"\t"+jsonItemsList.get(i).getQty());
                return selectItem;
            }
        }
        return  null;

    }

    private boolean itemsContain(String itemNo ,int position) {
        boolean found=false;
//        Log.e("itemsContain","="+itemNo);
        float priceItem=1;

        for(int i=0;i<items.size();i++) {
            if (items.get(i).getItemNo().trim().equals(itemNo.trim())) {
//                Log.e("itemsContain","1found="+items.get(i).getQty()+"\tamount"+items.get(i).getAmount());

//                for (int j = 0; j < jsonItemsList.size(); j++) {
//                    Log.e("itemsContain","***2found="+items.get(i).getQty()+"\tjsonItemsList="+jsonItemsList.get(j).getQty());

//                    if (items.get(i).getQty() < jsonItemsList.get(j).getQty()) {
                    items.get(i).setQty(items.get(i).getQty() + 1);
                    items.get(i).setAmount((items.get(i).getQty() * items.get(i).getPrice()) - items.get(i).getDisc());
                    items.get(i).setEnter_qty(items.get(i).getQty() + "");
                    items.get(i).setWhichu_qty(items.get(i).getQty() + "");
                    items.get(i).setEnter_price(items.get(i).getPrice() + "");
                    items.get(i).setWhich_unit_str("");
//                Log.e("itemsContain","2found="+items.get(i).getQty()+"\tamount"+items.get(i).getAmount());

                    priceItem = items.get(i).getPrice();
//                priceItem=items.get(i).getPrice();
                    found = true;
                    break;
//                }
//                    else{
//                        Log.e("itemsContain","noooot="+items.get(i).getQty()+"\tjsonItemsList="+jsonItemsList.get(j).getQty());
//
//                    }
//            }
        }
        }
        if(found){
            listTemporarySerial.get(position).setQty("1");
            listTemporarySerial.get(position).setIsPosted("0");
            listTemporarySerial.get(position).setPriceItem(priceItem);
            listTemporarySerial.get(position).setPriceItemSales(priceItem+"");
        }
        Log.e("itemsContain","found="+found);
        return  found;
    }


    @SuppressLint("WrongConstant")
    private void fillSerial(Context context , String itemNo, int position) {
        serial = new serialModel();
        counterSerial++;
        if(addNewSerial==1)
        {
            itemNo=getItemNo(barcodeValue);
//            serial.setPriceItem(1);
        }else {
            serial.setPriceItem(items.get(position).getPrice());
        }


        serial.setCounterSerial(counterSerial);
        serial.setSerialCode(barcodeValue);
        serial.setIsBonus("0");
        serial.setIsDeleted("0");
        Log.e("itemNo",""+itemNo);
        serial.setVoucherNo(voucherNumber+"");
        serial.setKindVoucher(voucherType+"");
        serial.setStoreNo(Login.salesMan);
        serial.setDateVoucher(voucherDate);
        serial.setItemNo(itemNo);
        serial.setIsPosted("0");
        serial.setQty("1");

        listTemporarySerial.add(serial);
        Log.e("unitQtyEdit",""+counterSerial);
        unitQtyEdit.setText(listTemporarySerial.size() + "");
        serialValueUpdated.setText("");
        if(contiusReading==0)
        {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    serialValueUpdated.setText("");
                    serialValueUpdated.requestFocus();

                }
            });

        }



        //addQtyTotal(1);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(VERTICAL);





        serial_No_recyclerView.setLayoutManager(layoutManager);

        serial_No_recyclerView.setAdapter(new Serial_Adapter(listTemporarySerial, context));
        Log.e("listTemporarySerial6",""+listTemporarySerial.size());
    }

    private String getItemNo(String barcodeValue) {
        Log.e("getItemNo",""+barcodeValue);
        return  mDbHandler.getItemNoForSerial(barcodeValue);
    }

    public void openSmallScanerTextView() {

        new IntentIntegrator(getActivity()).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();



    }
    private void openeditDialog() {
        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        SweetAlertDialog sweetMessage= new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(getActivity().getResources().getString(R.string.enter_serial));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(getActivity().getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(!editText.getText().toString().equals(""))
                {
                    serialValueUpdated.setText(editText.getText().toString().trim());
                    sweetAlertDialog.dismissWithAnimation();



                }
                else {
                    editText.setError(getActivity().getResources().getString(R.string.reqired_filled));
                }
            }
        })

                .show();
    }
    private boolean validbarcodeValue(String barcode,ArrayList<serialModel> serialListitems,int numberBarcodsScanner) {
        String data =barcode.toString().trim();
       // Log.e("updateListCheque", "afterTextChanged" +"position\t"+numberBarcodsScanner+data+"\tdontValidate="+barcode);
        try {

            if(data.toString().trim().length()!=0)
            {
                if(data.toString().trim().equals("error"))
                {

                }
                else {
                    isFoundSerial=false;

                    for(int h=0;h<serialListitems.size();h++)
                    {

                        if(serialListitems.get(h).getSerialCode().equals(data))
                        {
                            isFoundSerial=true;
                            break;
                        }
                    }
                    if(isFoundSerial==true)
                    {// FOUND  IN CURRENT LIST
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getActivity().getString(R.string.warning_message))
                                .setContentText(getActivity().getString(R.string.duplicate)+"\t"+getActivity().getResources().getString(R.string.inThisVoucher))
                                .setConfirmButton(getActivity().getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.duplicate)+"\t"+getActivity().getResources().getString(R.string.inThisVoucher), Toast.LENGTH_SHORT).show();

                        return  false;
                    }
                }


                Log.e("errorSerial2", "isFoundSerial" +"addNewSerial=\t"+addNewSerial);
//            if ((databaseHandler.isSerialCodeExist(data).equals("not")) && (isFoundSerial == false)) {
                if (mDbHandler.isSerialCodeExist(data).equals("not")||voucherType==506||mDbHandler.getLastTransactionOfSerial(data.trim()).equals("506")) {
                    if((mDbHandler.isSerialCodePaied(data+"").equals("not")&&voucherType==504)||
                            (!mDbHandler.isSerialCodePaied(data+"").equals("not")&&voucherType==506))
                    {
                        errorData = false;

//                         serialListitems.get(numberBarcodsScanner).setSerialCode(data);
//                          listMasterSerialForBuckup.get(numberBarcodsScanner).setSerialCode(data);
//                            currentUpdate = position;
//
//                            isClicked.set(position, 0);
//                            isClicked.set(position + 1, 1);

                        // notifyDataSetChanged();
//                        if(validQtyItem(data))
//                        {
//                            return true;
//                        }else {
//
//                        }
                        return true;


                    }
                    else {// duplicated
                        if(voucherType==506)
                        {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getActivity().getString(R.string.warning_message))
                                    .setContentText(getActivity().getString(R.string.serialIsNotPaied))
                                    .setConfirmButton(getActivity().getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            String voucherNo=mDbHandler.isSerialCodePaied(data+"");
                            String voucherDate=voucherNo.substring(voucherNo.indexOf("&")+1);
                            voucherNo=voucherNo.substring(0,voucherNo.indexOf("&"));

                            Log.e("Activities3", "onActivityResult+false+isSerialCodePaied" + voucherNo+"\t"+voucherDate);
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setContentText(getActivity().getString(R.string.duplicate) +"\t"+data+ "\t"+getActivity().getString(R.string.forVoucherNo)+"\t" +voucherNo+"\n"+getActivity().getString(R.string.voucher_date)+"\t"+voucherDate)
                                    .setConfirmButton(getActivity().getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }


                        return  false;
                    }

                } else {
                    errorData = true;
                    // Toast.makeText(context, context.getResources().getString(R.string.invalidSerial), Toast.LENGTH_SHORT).show();
                    String ItemNo=mDbHandler.isSerialCodeExist(data+"");
                    if(!ItemNo.equals("")){
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getActivity().getString(R.string.warning_message))
                                .setContentText(getActivity().getString(R.string.invalidSerial)+"\t"+data+"\t"+getActivity().getString(R.string.forItemNo)+ItemNo)
                                .setConfirmButton(getActivity().getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                    else {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getActivity().getString(R.string.warning_message))
                                .setContentText(getActivity().getString(R.string.invalidSerial)+"\t"+data)
                                .setConfirmButton(getActivity().getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }



                    return false;


                }
            }

            else {
                return  false;
//                    updateListCheque(position, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
            }


        }catch (Exception e){
            return  false;
        }

    }

//    private boolean validQtyItem(String data) {
//        String itemNo=mDbHandler.getItemNoForSerial(data);
////        for (int i=0;i<listitem)
//    }

    private int getNumberOfNormalQty(ArrayList<serialModel> listTemporarySerial) {
        int qty=0;
        for(int i=0;i<listTemporarySerial.size();i++)
        {
            if(listTemporarySerial.get(i).getIsBonus().equals("0"))
            {
                qty++;
            }
        }
        return  qty;
    }

    private boolean verifyNotEmpty(ArrayList<serialModel> listTemporarySerial) {
        for(int i=0;i<listTemporarySerial.size();i++)
        {
            if (listTemporarySerial.get(i).getSerialCode().equals("")) {// for empty serial

                return  false;

            }
        }
        return  true;

    }

    private boolean updateListSerialTotal(ArrayList<serialModel> listTemporarySerial,int position,float price,String discount) {
        if(listTemporarySerial.size()!=0)
        {
            countBunosQty=listTemporarySerial.size()-countNormalQty;
            deleteFromListMasterSerial(listTemporarySerial.get(0).getItemNo(),price);
            addForListMasterSerial(listTemporarySerial);
            if(items.size()!=0)
            updateQtyInItemsList(countNormalQty,countBunosQty,position);
            for(int k=0;k<listMasterSerialForBuckup.size();k++)
            {
                mDbHandler.add_SerialBackup(listMasterSerialForBuckup.get(k),0);
            }
            updateQtyBasket();// check item amount
            updateAmount(position,discount);
            calculateTotals(0);
         return  true;
        }
        else {// empty List

            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getActivity().getString(R.string.warning_message))
                    .setContentText(getActivity().getString(R.string.emptyList))
                    .show();
            return  false;

        }

    }

    private void updateAmount(int position,String discount) {

        float discountNew=0;
        try {
            if (discountNew != 100) {


            discountNew = Float.parseFloat(discount);

            Log.e("discPercRadioButton", "position 11  " + (radioDiscountSerial.getCheckedRadioButtonId() == R.id.discPercRadioButton));
            Log.e("discValcRadioButton", "position 122  " + (radioDiscountSerial.getCheckedRadioButtonId() == R.id.discValueRadioButton));
            //radioDiscountSerial= dialog.findViewById(R.id.discTypeRadioGroup);

            if (radioDiscountSerial.getCheckedRadioButtonId() == R.id.discPercRadioButton) {
                items.get(position).setDiscType(1);// error for discount promotion // percent discount
                Log.e("discPercRadioButton", "position 1");
            } else {
                items.get(position).setDiscType(0);// value Discount
                Log.e("discPercRadioButton", "position 0");
            }

            if (items.get(position).getDiscType() == 0) {// value

                items.get(position).setDisc((discountNew));
                items.get(position).setDiscPerc(items.get(position).getQty() * items.get(position).getPrice() * (discountNew / 100) + "");
            } else {// percent
                // percentOld=Float.parseFloat(items.get(position).getDiscPerc());
                items.get(position).setDiscPerc(discountNew + "");
                items.get(position).setDisc(items.get(position).getQty() * items.get(position).getPrice() * (discountNew / 100));


            }
        }
        }catch (Exception e){

        }





        if (items.get(position).getDiscType() == 0)
        {
            items.get(position).setAmount(items.get(position).getQty() * items.get(position).getPrice() - items.get(position).getDisc());

        }

        else{
            items.get(position).setAmount((items.get(position).getQty() * items.get(position).getPrice() - items.get(position).getDisc()));

        }
        itemsListAdapter.notifyDataSetChanged();
    }

    public static void updateQtyBasket() {
        float qty=0;
        total_items_quantity=0;
        for(int i=0;i<items.size();i++){
            qty+=items.get(i).getQty();
        }

        addQtyTotal(qty);
    }

    private void updateQtyInItemsList(int size,int bunos, int position) {
        items.get(position).setQty(size);
        //itemsListAdapter.setItemsList(items);
        items.get(position).setBonus(bunos);
        itemsListAdapter.notifyDataSetChanged();
    }

    private void addForListMasterSerial(ArrayList<serialModel> listTemporarySerial) {
        for (int i=0;i<listTemporarySerial.size();i++)
        {
            listSerialTotal.add(listTemporarySerial.get(i));
        }

    }

    private void deleteFromListMasterSerial(String itemNo,float price) {

        int i=0;
        while (i<listSerialTotal.size())
        {
            if((listSerialTotal.get(i).getItemNo().equals(itemNo))&&(listSerialTotal.get(i).getPriceItem()==price))
            {
                listSerialTotal.remove(i);
            }
            else {
                i++;
            }
        }





      //***************************************************
//        for(int i=0;i<listSerialTotal.size();i++)
//        {
//            if((listSerialTotal.get(i).getItemNo().equals(itemNo))&&(listSerialTotal.get(i).getPriceItem()==price))
//            {
//                listSerialTotal.remove(i);
//                if(i!=0)
//                i--;
//            }
//        }


    }

    private ArrayList<serialModel> getserialForItem(String itemNo,float  price) {
        ArrayList<serialModel> list=new ArrayList<>();
        for (int i = 0; i < listSerialTotal.size(); i++) {
            Log.e("price",""+itemNo+"\tprice="+price+"\tgetPriceItem="+listSerialTotal.get(i).getPriceItem());

            if(listSerialTotal.get(i).getItemNo().equals(itemNo)&&listSerialTotal.get(i).getPriceItem()==price)
            {
                Log.e("getserialForItem",""+itemNo);
                list.add(listSerialTotal.get(i));
            }
        }
        return list;
    }

    private int checkSerialDB(Context context) {
        listSerialValue =new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < serialListitems.size(); i++) {
            listSerialValue.add(serialListitems.get(i).getSerialCode());
            if (serialListitems.get(i).getSerialCode().equals("")) {
                counter = -1;

                return counter;
            }

            if (!mDbHandler.isSerialCodeExist(serialListitems.get(i).getSerialCode()).equals("not")) {
//                if(MHandler.isSerialCodePaied(serialListitems.get(i).getSerialCode()).equals("not"))
//                {
                counter++;
                String ItemNo=mDbHandler.isSerialCodeExist(serialListitems.get(i).getSerialCode()+"");

                if(!ItemNo.equals("")){
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.invalidSerial)+"\t"+serialListitems.get(i).getSerialCode()+"\t"+context.getString(R.string.forItemNo)+ItemNo)
                            .show();
                }else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.invalidSerial)+"\t"+serialListitems.get(i).getSerialCode())
                            .show();

                }
//                }


            }

        }

        if(hasDuplicate(listSerialValue)){counter=100;}

        return counter;

    }
    private void updateListSerialBukupDeleted(String itemNoSelected, String vouch) {

            mDbHandler.updateitemDeletedInSerialTable_Backup(itemNoSelected,vouch);



    }

    public String[] getIndexToBeUpdated() {
        return rowToBeUpdated;
    }

    public int getIndex() {
        return index;
    }

    private Offers getAppliedOffer(String itemNo, String qty, int flag) {

        double qtyy = Double.parseDouble(qty);
        List<Offers> offer = checkOffers(itemNo);

        double currentQty=mDbHandler.getSalesManQty(itemNo);
        List<Double> itemQtys = new ArrayList<>();
        for (int i = 0; i < offer.size(); i++) {
            itemQtys.add(offer.get(i).getItemQty());
        }
        Collections.sort(itemQtys);

        double iq = itemQtys.get(0);
        iq=0;
        for (int i = 0; i < itemQtys.size(); i++) {
            if (qtyy >= itemQtys.get(i)) {
                iq = itemQtys.get(i);
            }
        }

        double bonus_calc=0;
        for (int i = 0; i < offer.size(); i++) {
            if (iq == offer.get(i).getItemQty())
            {
//                Log.e("getSalesManQty",""+mDbHandler.getSalesManQty(offer.get(i).getBonusItemNo()));
//                Log.e("getSalesManQty",""+offer.get(i).getBonusQty());
//                Log.e("getSalesManQty","qtyy="+qtyy);
                if (OfferCakeShop == 0) {
                    bonus_calc = ((int) (qtyy / offer.get(i).getItemQty())) * offer.get(i).getBonusQty();

                } else {
                    bonus_calc = offer.get(i).getBonusQty();
                }
                if(mDbHandler.getSalesManQty(offer.get(i).getBonusItemNo())>=(bonus_calc))
                return offer.get(i);
        }
        }
        Offers offerEmpty = new Offers();
        offerEmpty.setItemNo("-1");

        return offerEmpty;
    }

    private List<Offers> checkOffers(String itemNo) {

        Offers offer = null;
        List<Offers> Offers = new ArrayList<>();
        try {
            Date currentTimeAndDate = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = df.format(currentTimeAndDate);
            date = convertToEnglish(date);


            List<Offers> offers = mDbHandler.getAllOffers();


            for (int i = 0; i < offers.size(); i++) {
             //   Log.e("log2 ", date + "  " + offers.get(i).getFromDate() + " " + offers.get(i).getToDate());
                if (itemNo.equals(offers.get(i).getItemNo()) &&
                        (formatDate(date).after(formatDate(offers.get(i).getFromDate())) || formatDate(date).equals(formatDate(offers.get(i).getFromDate()))) &&
                        (formatDate(date).before(formatDate(offers.get(i).getToDate())) || formatDate(date).equals(formatDate(offers.get(i).getToDate())))) {

                    offer = offers.get(i);
                    Offers.add(offer);
                }
            }
            Log.e("checkOffers",""+Offers.size());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Offers;
    }


    private void clearItemsList() {
        items.clear();
        itemsListAdapter.setItemsList(items);
        itemsListAdapter.notifyDataSetChanged();
        listSerialTotal.clear();
        jsonItemsList.clear();
    }

    private void clearLayoutData(int flag) {
        int vouch=0;
        vouch=Integer.parseInt(voucherNumberTextView.getText().toString());
            remarkEditText.setText("");
            clearItemsList();
//        calculateTotals();
            subTotalTextView.setText("0.000");
            taxTextView.setText("0.000");
            netTotalTextView.setText("");
            netTotalTextView.setText("0.000");
            discTextView.setText("0.000");
            subTotal = 0.0;
            totalTaxValue = 0.0;
            netTotal = 0.0;
            totalDiscount = 0.0;
            sum_discount = 0.0;
            items.clear();
            itemsList.clear();
            try {
                discountValue=0;
                discountPerc=0;
            }catch (Exception e){}

//        calculateTotals();
            //***********************************************
        if(makeOrders!=1) {
            if (Purchase_Order == 0) {
            voucherType = 504;
            salesRadioButton.setChecked(true);
            salesRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
            retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
            orderRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
        }else {
                voucherType=506;
                retSalesRadioButton.setChecked(true);
            }
        }
        else {
            if (Purchase_Order == 0) {
                voucherType = 508;
                orderRadioButton.setChecked(true);
                salesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                retSalesRadioButton.setTextColor(getResources().getColor(R.color.text_view_color));
                orderRadioButton.setTextColor(getResources().getColor(R.color.cancel_button));
            }else {
                voucherType=506;
                retSalesRadioButton.setChecked(true);

            }
        }

            //***********************************************
//        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
            refreshLocalVoucherNo();
            total_items_quantity = 0;
            totalQty_textView.setText("+0");
            discvalue_static = 0;
            refrechItemForReprint();
            SaveData.setEnabled(true);
        save_floatingAction.setEnabled(true);
        listSerialTotal.clear();

    }
    public static <T> List getDuplicate(Collection<T> list) {

        final List<T> duplicatedObjects = new ArrayList<T>();
        Set<T> set = new HashSet<T>() {
            @Override
            public boolean add(T e) {
                if (contains(e)) {
                    duplicatedObjects.add(e);
                }
                return super.add(e);
            }
        };
        for (T t : list) {
            set.add(t);
        }
        return duplicatedObjects;
    }


//    public static <T> boolean hasDuplicate(Collection<T> list) {
//        if (getDuplicate(list).isEmpty())
//            return false;
//        return true;
//    }
    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each: all) if (!set.add(each)) return true;
        return false;
    }

    public void calculateTotals(int flagDelete) {


        if(notIncludeTax.isChecked())
        {
            noTax=0;

        }else {noTax=1;}
        if(exported_tax.isChecked())
        {
            Exported_Tax=0;
            noTax=0;

        }else {

            if(notIncludeTax.isChecked())
            {
                noTax=0;

            }else {noTax=1;}
            Exported_Tax=1;

        }


        Log.e("TOTAL", "visaPayFlag==" +visaPayFlag);
//        discTextView.setText("0.0");
        netTotalTextView.setText("0.0");
//        calculateTotals_cridit();
        double itemTax, itemTotal, itemTotalAfterTax,
                itemTotalPerc,  posPrice, totalQty = 0,allItemQtyWithDisc=0,itemsQty=0;
        Float itemDiscVal;
        //**********************************************************************
        if(visaPayFlag==0){
        list_discount_offers = mDbHandler.getDiscountOffers();// total discount
        itemsQtyOfferList = mDbHandler.getItemsQtyOffer();
        }else {
            list_discount_offers.clear();
            itemsQtyOfferList.clear();
        }
        String itemGroup;
        subTotal = 0.0;
        totalTaxValue = 0.0;
        netTotal = 0.0;
        totalDiscount = 0;
//        double disc_dentail=Double.parseDouble(discTextView.getText().toString());
//        totalDiscount += discvalue_static;
        //test discount item with discount total voucher
//        sum_discount +=DiscountFragment.getDiscountPerc();
//        sum_discount +=DiscountFragment.getDiscountValue();
//        Log.e("sum_discount","="+DiscountFragment.getDiscountPerc()+"\t"+ DiscountFragment.getDiscountValue());
        float flagBonus = 0;
        float amountBonus = 0;
        totalQty = 0.0;
        double limit_offer = 0;
        //Excluclude tax//خاضع
        if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0) {
            Log.e("getTaxClarcKind","==0");
            totalQty = 0.0;
            try {

                if(visaPayFlag==0)
                limit_offer = mDbHandler.getMinOfferQty(total_items_quantity);
                else limit_offer=0;
                Log.e("total_items_quantity","=="+total_items_quantity+"\t"+limit_offer);
            } catch (Exception e) {
                limit_offer = 0;
            }

            allItemQtyWithDisc=0;
            for (int i = 0; i < items.size(); i++) {
                allItemQtyWithDisc+=items.get(i).getQty();

                disc_items_total = 0;
                disc_items_value = 0;

                if (total_items_quantity >= limit_offer && limit_offer != 0 && payMethod == 1&& visaPayFlag!=1) {// all item without bonus item
                    discount_oofers_total_credit=0;
                    discount_oofers_total_cash=0;
                    for (int b = 0; b < items.size(); b++) {
                        if (checkOffers_no(items.get(b).getItemNo())) {
//                                    if (items.get(b).getItemNo().equals(itemsQtyOfferList.get(k).getItem_no())&&limit_offer==itemsQtyOfferList.get(k).getItemQty()) {

                            if (items.get(b).getDisc() != 0) {// delete the discount(table bromotion vs ) from this item
//                                disount_totalnew = 0;
//                                items.get(b).setDisc(disount_totalnew);
//                                items.get(b).setAmount(items.get(b).getQty() * items.get(b).getPrice());
//                                itemsListView.setAdapter(itemsListAdapter);

                            }
                            else {
                                disc_items_value += items.get(b).getQty() * mDbHandler.getDiscValue_From_ItemsQtyOffer(items.get(b).getItemNo(), limit_offer);

                                Log.e("disc_items_value","222="+disc_items_value);
                            }


//
                        }else {

                        }

                    }


                }
                else {// all item without discount item
                    totalQty = 0.0;
                    allItemQtyWithDisc=0;
                    for (int x = 0; x < items.size(); x++) {
                        allItemQtyWithDisc+=items.get(x).getQty();
                        if (items.get(x).getDisc() == 0) {// if not exist discount on item x and type off offer is bonus ===> disc type =0
                            if (items.get(x).getItemName().equals("(bonus)")) {
                                flagBonus = items.get(x - 1).getQty();
                                amountBonus = items.get(x).getQty();
                                totalQty = totalQty - flagBonus;

                            } else {//item without discount
                                totalQty = totalQty + items.get(x).getQty();
                            }

                        }


                    }

                    for (int j = 0; j < list_discount_offers.size(); j++) {
//                            totalDiscount=0;
                        if (payMethod == 1&&visaPayFlag==0) {
                            if (list_discount_offers.get(j).getPaymentType() == 1) {
                                if (total_items_quantity>=list_discount_offers.get(j).getQTY()  ) {
                                    discount_oofers_total_cash=0;
                                    discount_oofers_total_cash = totalQty * list_discount_offers.get(j).getDiscountValue();

//                                discount_oofers_total_cash =( totalQty /list_discount_offers.get(j).getQTY()) * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        } else {
                            if (list_discount_offers.get(j).getPaymentType() == 0) {
                                if ( total_items_quantity>=  list_discount_offers.get(j).getQTY() ) {
                                    discount_oofers_total_credit=0;
                                    discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        }
                    }
                }

            }
            if(items.size()==0){
                disc_items_total = 0;
                disc_items_value = 0;
                discount_oofers_total_credit=0;
                discount_oofers_total_cash=0;
            }
//            }
            //**********************************************************************************************************************************************

            disc_items_total += disc_items_value;
            totalDiscount += disc_items_total;


            if (discount_oofers_total_cash > 0)
                sum_discount += discount_oofers_total_cash;
            if (discount_oofers_total_credit > 0)
                sum_discount += discount_oofers_total_credit;


            try {
                totalDiscount += sum_discount;
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }
            checkGroupOffer(flagDelete);
            // for rawat almazaq

            for (int i = 0; i < items.size(); i++) {
                itemGroup = items.get(i).getCategory();
                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    isDataSmoke=1;
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
                } else {

                    itemTax = items.get(i).getAmount() * items.get(i).getTaxPercent() * 0.01;
                    itemTotal = items.get(i).getAmount();
                }
                itemTotalAfterTax = items.get(i).getAmount() + itemTax;
                subTotal = subTotal + itemTotal;
            }


            if(discType_static==0)// value
            {
                totalDiscount+=discvalue_static;
            }else {// percent
//                Log.e("totalDiscount",""+totalDiscount+"--"+DiscountFragment.getDiscountPerc()+"\t"+subTotal);
                discvalue_static=subTotal*(DiscountFragment.getDiscountPerc()/100);
                totalDiscount+=discvalue_static;
            }
            DecimalFormat df = new DecimalFormat("#.00");
//            y1 = Float.valueOf(df.format(y1));
            // re calculate total disc
            for (int i = 0; i < items.size(); i++) {
                itemTotal = items.get(i).getAmount();
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal =(float) (itemTotalPerc * totalDiscount);
//                Log.e("itemDiscVal","-------====="+itemDiscVal+"\t percent=="+itemTotalPerc);
//                Log.e("itemDiscVal","-------====="+itemDiscVal+"\t percent=="+decimalFormat.format(itemDiscVal));

//
//                itemDiscVal = Float.valueOf(convertToEnglish(df.format(itemDiscVal));
//                Log.e("itemDiscVal2222","-------====="+itemDiscVal);
                items.get(i).setVoucherDiscount(itemDiscVal);
                items.get(i).setTotalDiscVal(itemDiscVal);
                //************************************************************
//                totalQty +=items.get(i).getQty();
////                Log.e("totalQty",""+totalQty);
                if(isDataSmoke==0)
                itemTotal = itemTotal - itemDiscVal;//for rawat_mazaq
                itemGroup = items.get(i).getCategory();
                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    isDataSmoke=1;
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
//                    itemTotal = itemTotal - itemDiscVal;
                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
                }

                if(aqapa_tax==0)
                items.get(i).setTaxValue(itemTax);
                else
                {
                    items.get(i).setTaxValue(0);
                    totalTaxValue=0;
                    itemTax=0;
                }
                totalTaxValue = totalTaxValue + itemTax;
            }

//            totalTaxValue=(subTotal-totalDiscount)*0.16;
                // tahani -discount_oofers_total

            if(visaPayFlag==0) {
                totalDiscount += getTotalDiscSetting(netTotal);

                netTotal = netTotal - getTotalDiscSetting(netTotal);
            }



            Log.e("TOTAL", "noTax2totalTaxValue==" +totalTaxValue);
            if(visaPayFlag==1)totalDiscount=0;
            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue;
//              netTotal = netTotal + subTotal -sum_discount + totalTaxValue;


        } else
            {
                Log.e("getTaxClarcKind","==1");
            totalQty = 0.0;
            try {
                if(visaPayFlag==0)
                    limit_offer = mDbHandler.getMinOfferQty(total_items_quantity);
                else limit_offer=0;

            } catch (Exception e) {
                limit_offer = 0;
            }

            for (int i = 0; i < items.size(); i++) {

                disc_items_total = 0;
                disc_items_value = 0;
                totalQty = 0.0;allItemQtyWithDisc=0;
                for (int x = 0; x < items.size(); x++) {
                    allItemQtyWithDisc+=items.get(i).getQty();
                    if (items.get(x).getDisc() == 0) {// if not exist discount on item x and type off offer is bonus ===> disc type =0
                        if (items.get(x).getItemName().equals("(bonus)")) {
                            flagBonus = items.get(x - 1).getQty();
                            amountBonus = items.get(x).getQty();
                            totalQty = totalQty - flagBonus;

                        } else {//item without discount
                            totalQty = totalQty + items.get(x).getQty();
                        }

                    }


                }

//                Log.e("allItemQtyWithDisc=",total_items_quantity+""+"\ttotalQty="+totalQty);
                if (total_items_quantity >= limit_offer && limit_offer != 0 && payMethod == 1&& visaPayFlag!=1) {// all item without bonus item
                    discount_oofers_total_credit=0;
                    discount_oofers_total_cash=0;
                    for (int b = 0; b < items.size(); b++) {

                        if (checkOffers_no(items.get(b).getItemNo())) {
//                                    if (items.get(b).getItemNo().equals(itemsQtyOfferList.get(k).getItem_no())&&limit_offer==itemsQtyOfferList.get(k).getItemQty()) {


                            if (items.get(b).getDisc() != 0) {// delete the discount(table bromotion vs ) from this item
//                                disount_totalnew = 0;
//                                items.get(b).setDisc(disount_totalnew);
//                                items.get(b).setAmount(items.get(b).getQty() * items.get(b).getPrice());
//                                itemsListView.setAdapter(itemsListAdapter);

                            }
                            else {// zero descount for this item
//                                disc_items_value += items.get(b).getQty() * mDbHandler.getDiscValue_From_ItemsQtyOffer(items.get(b).getItemNo(), limit_offer);

                                disc_items_value += items.get(b).getQty() * mDbHandler.getDiscValue_From_ItemsQtyOffer(items.get(b).getItemNo(), limit_offer);

                            }
//                            Log.e("disc_items_value","else="+disc_items_value);



                        }
                        else{
                            if(items.get(b).getDisc()==0)
                            {// test all items without discount and without offer table 50

                                if(payMethod==1)
                                {discount_oofers_total_cash = getCashCreditOffer(total_items_quantity,list_discount_offers,payMethod,items.get(b).getQty());
                                }
                                else {
                                    discount_oofers_total_credit= getCashCreditOffer(total_items_quantity,list_discount_offers,payMethod,items.get(b).getQty());
                                }



                                Log.e("disc_items_value","else22="+disc_items_value);

                            }

                        }


                    }


                } else {// all item without discount item

                    for (int j = 0; j < list_discount_offers.size(); j++) {// here to test   offer by cash  *****************
//                            totalDiscount=0;
                        if (payMethod == 1&&visaPayFlag==0) {
                            if (list_discount_offers.get(j).getPaymentType() == 1) {
                                if ( total_items_quantity>=  list_discount_offers.get(j).getQTY()  ) {
                                    discount_oofers_total_cash = 0;

                                    discount_oofers_total_cash = totalQty * list_discount_offers.get(j).getDiscountValue();
//                                discount_oofers_total_cash =( totalQty /list_discount_offers.get(j).getQTY()) * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        } else {
                            if (list_discount_offers.get(j).getPaymentType() == 0) {
                                if (total_items_quantity >=list_discount_offers.get(j).getQTY() ) {
                                    discount_oofers_total_credit = 0;
                                    discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        }
                    }
                }
            }

                if(items.size()==0){
                    disc_items_total = 0;
                    disc_items_value = 0;
                    discount_oofers_total_credit=0;
                    discount_oofers_total_cash=0;
                }
//
//                Log.e("allItemQtyWithDisc222=",allItemQtyWithDisc+""+"\ttotalQty="+totalQty);
            disc_items_total += disc_items_value;
            totalDiscount += disc_items_total;


            if (discount_oofers_total_cash > 0)
                sum_discount += discount_oofers_total_cash;
            if (discount_oofers_total_credit > 0)
                sum_discount += discount_oofers_total_credit;


            try {
                totalDiscount += sum_discount;
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }
                checkGroupOffer(flagDelete);

            for (int i = 0; i < items.size(); i++) {


                itemGroup = items.get(i).getCategory();


                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    isDataSmoke=1;
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
                } else {
                    itemTax = items.get(i).getAmount() -
                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
                }


                itemTotal = items.get(i).getAmount() - itemTax;
                itemTotalAfterTax = items.get(i).getAmount();
                subTotal = subTotal + itemTotal;
            }
                // for rawat almazaq
                if(discType_static==0)// value
                {
                    totalDiscount+=discvalue_static;
                }else {// percent

                    discvalue_static=subTotal*(DiscountFragment.getDiscountPerc()/100);
                    totalDiscount+=discvalue_static;
                }
            for (int i = 0; i < items.size(); i++) {


                itemGroup = items.get(i).getCategory();

                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    isDataSmoke=1;
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTax = items.get(i).getAmount() -
                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
                }

                itemTotal = items.get(i).getAmount() - itemTax;
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal = (float)(itemTotalPerc * totalDiscount);
                items.get(i).setVoucherDiscount((float) itemDiscVal);
                items.get(i).setTotalDiscVal(itemDiscVal);
//                itemTotal = itemTotal - itemDiscVal;

                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    isDataSmoke=1;
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
                }

                itemTotal = itemTotal - itemDiscVal;// here for rawat mazaq
                if(aqapa_tax==0)
                    items.get(i).setTaxValue(itemTax);
                else
                {
                    items.get(i).setTaxValue(0);
                    totalTaxValue=0;
                    itemTax=0;
                }
                totalTaxValue = totalTaxValue + itemTax;
            }

//            totalDiscount+=getTotalDiscSetting(netTotal);
//            Log.e("TOTAL", "noTax3totalTaxValue==" +totalTaxValue);
                if(visaPayFlag==1)totalDiscount=0;
            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue; // tahani -discount_oofers_total

            if(visaPayFlag==0) {
                totalDiscount += getTotalDiscSetting(netTotal);

                netTotal = netTotal - getTotalDiscSetting(netTotal);
            }

        }




        if(noTax==0)
        {
            Log.e("ayanoTax=====","="+"noTax");
            if(mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 1){// شامل

                Log.e("getTaxClarcKind","totalTaxValue="+totalTaxValue+"\tnetTotal="+netTotal);
                netTotal=netTotal-totalTaxValue;
                subTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));

                totalTaxValue=0;
                taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));
            }else{// خاضع
                netTotal=netTotal-totalTaxValue;
                totalTaxValue=0;
                taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));
                subTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));
            }

        }else
        {
            taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));
            subTotalTextView.setText(String.valueOf(decimalFormat.format(subTotal)));
        }



        if(visaPayFlag==1)totalDiscount = 0;
        discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(discTextView.getText().toString()))));
        discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(totalDiscount + ""))));
        netTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));

        subTotalTextView.setText(convertToEnglish(subTotalTextView.getText().toString()));
        taxTextView.setText(convertToEnglish(taxTextView.getText().toString()));
        netTotalTextView.setText(convertToEnglish(netTotalTextView.getText().toString()));

        discTextView.setText(String.valueOf(convertToEnglish(decimalFormat.format(totalDiscount)) + ""));
        totalDiscount = 0.0;
        sum_discount = 0;


    }

    private double getTotalDiscSetting(double totalVoucher) {
        double discTot=0;
        if(payMethod==1)
        {
            if(totalDiscount_checkbox.isChecked())
            {
                if(!valueTotalDiscount.getText().toString().equals(""))
                {
                    discTot=Double.parseDouble(valueTotalDiscount.getText().toString().trim());
                    if(discTot!=0)
                    {
                        discTot=totalVoucher*(discTot/100);
                    }
                }  return discTot;
            }

//            if(mDbHandler.getAllSettings().get(0).getActiveTotalDiscount()==1)
//            {
//                Log.e("getTotalDiscSetting",""+mDbHandler.getAllSettings().get(0).getValueOfTotalDiscount()+"\t"+totalVoucher);
//                if(mDbHandler.getAllSettings().get(0).getValueOfTotalDiscount()!=0)
//                {
//                    discTot=totalVoucher*(mDbHandler.getAllSettings().get(0).getValueOfTotalDiscount()/100);
//                }
//                Log.e("getTotalDiscSetting","discTot= "+discTot);
//
//                return discTot;
//            }
        }

        return 0;
    }

    private double getCashCreditOffer(double totalQty, List<QtyOffers> list_discount_offers, int payMethod,double curenQty) {
        Log.e("disc_items_value","else22=totalQty"+totalQty);
       // discount_oofers_total_cash=0,discount_oofers_total_credit=0;
        for (int j = 0; j < list_discount_offers.size(); j++) {
//                            totalDiscount=0;
            if (payMethod == 1) {
                if (list_discount_offers.get(j).getPaymentType() == 1) {
                    if (totalQty >= list_discount_offers.get(j).getQTY()) {
                        discount_oofers_total_cash = 0;
                        discount_oofers_total_cash = curenQty * list_discount_offers.get(j).getDiscountValue();
//                                discount_oofers_total_cash =( totalQty /list_discount_offers.get(j).getQTY()) * list_discount_offers.get(j).getDiscountValue();
                    }
                }
            } else {
                if (list_discount_offers.get(j).getPaymentType() == 0) {
                    if (totalQty >= list_discount_offers.get(j).getQTY()) {
                        discount_oofers_total_credit=0;
                        discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
                    }
                }
            }
        }
        Log.e("disc_items_value","else22=totalQty"+discount_oofers_total_cash+"\t"+discount_oofers_total_credit);
        if(payMethod==1)
        {
            return discount_oofers_total_cash;
        }else return discount_oofers_total_credit;

    }

    class QtySorter implements Comparator<ItemsQtyOffer> {
        @Override
        public int compare(ItemsQtyOffer one, ItemsQtyOffer another) {
            int returnVal = 0;

            if (one.getItemQty() < another.getItemQty()) {
                returnVal = -1;
            } else if (one.getItemQty() > another.getItemQty()) {
                returnVal = 1;
            } else if (one.getItemQty() == another.getItemQty()) {
                returnVal = 0;
            }
            return returnVal;
        }

    }

    private Boolean checkOffers_no(String itemNo) {

        Offers offer = null;
        List<ItemsQtyOffer> Offers = new ArrayList<>();
        try {
//            Date currentTimeAndDate = Calendar.getInstance().getTime();
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = df.format(currentTimeAndDate);
            date = convertToEnglish(date);
            Log.e("log2Trueee", date + "  " + offers_ItemsQtyOffer.get(0).getFromDate() + " " + offers_ItemsQtyOffer.get(0).getToDate());


            for (int i = 0; i < offers_ItemsQtyOffer.size(); i++) {
                if (itemNo.equals(offers_ItemsQtyOffer.get(i).getItem_no()) &&
                        (formatDate(date).after(formatDate(offers_ItemsQtyOffer.get(i).getFromDate())) || formatDate(date).equals(formatDate(offers_ItemsQtyOffer.get(i).getFromDate()))) &&
                        (formatDate(date).before(formatDate(offers_ItemsQtyOffer.get(i).getToDate())) || formatDate(date).equals(offers_ItemsQtyOffer.get(i).getToDate()))) {
                    Log.e("log2Trueee", date + "  " + offers_ItemsQtyOffer.get(i).getFromDate() + " " + offers_ItemsQtyOffer.get(i).getToDate());

                    return true;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Date formatDate(String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d;
    }

    public void calculateTotals_cridit() {
        double itemTax, itemTotal = 0, itemTotalAfterTax,
                itemTotalPerc, itemDiscVal, posPrice, totalQty = 0, total_Amount = 0;

        //**********************************************************************
//        String itemGroup;
//        subTotal = 0.0;
//        totalTaxValue = 0.0;
//        netTotal = 0.0;
//        double discount_credit=0;
//        //  Exclude  ------> TaxClarcKind() == 0
//        if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0)
//        {
//            for (int i = 0; i < items.size(); i++)
//            {
//                totalQty += items.get(i).getQty();
//                discount_oofers_total = 0;
//                if (payMethod== 1) {
//                    list_discount_offers = mDbHandler.getDiscountOffers();
//                    for (int j = 0; j < list_discount_offers.size(); j++) {
//                        if (totalQty >= list_discount_offers.get(j).getQTY()) {
//                            discount_oofers_total = totalQty * list_discount_offers.get(j).getDiscountValue();
//                        }
//                    }
//                }
//
//
//            }
//
//
//
//            for (int i = 0; i < items.size(); i++) {
//                itemGroup = items.get(i).getCategory();
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
//                }
//                else
//                {
//                    itemTax = items.get(i).getAmount() * items.get(i).getTaxPercent() * 0.01;
//                    itemTotal = items.get(i).getAmount();
//                }
//                itemTotalAfterTax = items.get(i).getAmount() + itemTax;
//                subTotal = subTotal + itemTotal;
//            }
//
//            for (int i = 0; i < items.size(); i++) {
//                itemTotal = items.get(i).getAmount();
//                total_Amount+=itemTotal;
//                itemTotalPerc = itemTotal / subTotal;
//                itemDiscVal = (itemTotalPerc * totalDiscount);
//                items.get(i).setTotalDiscVal(itemDiscVal);
//                //************************************************************
//                itemGroup = items.get(i).getCategory();
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                } else {
//                    itemTotal = itemTotal - itemDiscVal;
//                    total_Amount+=itemTotal;
//                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
//                }
//
//                items.get(i).setTaxValue(itemTax);
//                totalTaxValue = totalTaxValue + itemTax;
//            }
//            if (discount_oofers_total > 0) {
//                sum_discount = discount_oofers_total;
//            }
//
//            try {
//                totalDiscount=sum_discount;
////                totalDiscount = Float.parseFloat(discTextView.getText().toString());
//            } catch (NumberFormatException e) {
//                totalDiscount = 0.0;
//            }
//            if (payMethod == 0) {
//                double maxDiscount = CustomerListShow.Max_Discount_value;
//                discount_credit = (total_Amount * maxDiscount)/100;
//                Log.e("discount_credit",""+discount_credit);
//            }
//           if(payMethod==0) {
//               netTotal = netTotal + subTotal - totalDiscount - discount_credit + totalTaxValue;
//           }
//           else{
//               netTotal = netTotal + subTotal - totalDiscount  + totalTaxValue;
////               netTotal = netTotal + subTotal - totalDiscount - discount_oofers_total + totalTaxValue;
//           }
//
//        }
//      //********************************************  Include  ------> TaxClarcKind() == 1
//        else {
//
//            for (int i = 0; i < items.size(); i++) {
//
//                totalQty += items.get(i).getQty();
//                discount_oofers_total = 0;
//                if (payMethod == 1) {
//                    list_discount_offers = mDbHandler.getDiscountOffers();
//                    for (int j = 0; j < list_discount_offers.size(); j++) {
//                        if (totalQty >= list_discount_offers.get(j).getQTY()) {
//                            discount_oofers_total = totalQty * list_discount_offers.get(j).getDiscountValue(); }
//                    }
//                }
//
//                if (payMethod== 0) {
//
//                    double maxDiscount = CustomerListShow.Max_Discount_value;
//
//                    discount_credit = totalQty * maxDiscount;
//                }
//
//            }
//
//
//
//            if (discount_oofers_total > 0)
//                sum_discount = discount_oofers_total;
//
//            try {totalDiscount=sum_discount;
////                totalDiscount = Float.parseFloat(discTextView.getText().toString());
//            } catch (NumberFormatException e) {
//                totalDiscount = 0.0;
//            }
//
//            for (int i = 0; i < items.size(); i++) {
//
//
//                itemGroup = items.get(i).getCategory();
//
//
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
//                } else {
//                    itemTax = items.get(i).getAmount() -
//                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
//                }
//
//
//                itemTotal = items.get(i).getAmount() - itemTax;
//                itemTotalAfterTax = items.get(i).getAmount();
//                subTotal = subTotal + itemTotal;
//            }
//
//            for (int i = 0; i < items.size(); i++) {
//
//
//                itemGroup = items.get(i).getCategory();
//
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                } else {
//                    itemTax = items.get(i).getAmount() -
//                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
//                }
//
//                itemTotal = items.get(i).getAmount() - itemTax;
//                itemTotalPerc = itemTotal / subTotal;
//                itemDiscVal = (itemTotalPerc * totalDiscount);
//                items.get(i).setVoucherDiscount( (float)itemDiscVal);
//                items.get(i).setTotalDiscVal(itemDiscVal);
//
//                itemTotal = itemTotal - itemDiscVal;
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                } else {
//                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
//                }
//                items.get(i).setTaxValue(itemTax);
//                totalTaxValue = totalTaxValue + itemTax;
//            }
//            if(payMethod==0) {
//                netTotal = netTotal + subTotal - totalDiscount - discount_credit + totalTaxValue;
//            }
//            else{
//                netTotal = netTotal + subTotal - totalDiscount  + totalTaxValue;
////                netTotal = netTotal + subTotal - totalDiscount - discount_oofers_total + totalTaxValue;
//            }
//        }
//        subTotalTextView.setText(String.valueOf(decimalFormat.format(subTotal)));
//        taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));
//        if(payMethod==0) {//credit
//            discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(discount_credit + ""))));
//            discTextView.setText(convertToEnglish(discount_credit+""));
//        }
//        else{
//            //cash
//            discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(discount_oofers_total + ""))));
//            discTextView.setText(convertToEnglish(discount_oofers_total+""));
//        }
//        netTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));
//        subTotalTextView.setText(convertToEnglish(subTotalTextView.getText().toString()));
//        taxTextView.setText(convertToEnglish(taxTextView.getText().toString()));
//        netTotalTextView.setText(convertToEnglish(netTotalTextView.getText().toString()));
//        discount_oofers_total=0.0;
//        discount_credit=0;
//        total_Amount=0;


    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    void send_dataSewoo(Voucher voucher) throws IOException {
        try {
            testB = convertLayoutToImage(voucher);
            printPic = PrintPic.getInstance();
            printPic.init(testB);
            printIm = printPic.printDraw();
            mmOutputStream.write(printIm);
            isFinishPrint = true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private Bitmap convertLayoutToImage(Voucher voucher) {
//        for(int i=0;i<90;i++)
//            itemsList.add( itemsList.get( itemsList.size()-1));
        LinearLayout linearView = null;
Log.e("voucher===",voucher.getVoucherNumber()+"");
        final Dialog dialogs = new Dialog(getActivity());
        dialogs.setContentView(R.layout.printdialog);
        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
        Log.e("voucher2===",voucher.getVoucherNumber()+"");
        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);
        compname = (TextView) dialogs.findViewById(R.id.compname);
        tel = (TextView) dialogs.findViewById(R.id.tel);
        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
        date = (TextView) dialogs.findViewById(R.id.date);
        custname = (TextView) dialogs.findViewById(R.id.custname);
        note = (TextView) dialogs.findViewById(R.id.note);
        vhType = (TextView) dialogs.findViewById(R.id.vhType);
        paytype = (TextView) dialogs.findViewById(R.id.paytype);
        total = (TextView) dialogs.findViewById(R.id.total);
        discount = (TextView) dialogs.findViewById(R.id.discount);
        tax = (TextView) dialogs.findViewById(R.id.tax);
        ammont = (TextView) dialogs.findViewById(R.id.ammont);
        textW = (TextView) dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
//

        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFinishPrint) {
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialogs.dismiss();
                }
            }
        });

        Log.e("voucher3===",voucher.getVoucherNumber()+"");
        String voucherTyp = "";
        switch (voucher.getVoucherType()) {
            case 504:
                voucherTyp = "فاتورة بيع";
                break;
            case 506:
                voucherTyp = "فاتورة مرتجعات";
                break;
            case 508:
                voucherTyp = "طلب جديد";
                break;
        }

        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);

        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucher.getSubTotal());
        discount.setText("" + voucher.getTotalVoucherDiscount());
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());


        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        for (int j = 0; j < itemsList.size(); j++) {

            if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(getActivity());


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);

                    switch (i) {
                        case 0:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText("" + convertToEnglish(threeDForm.format(Double.parseDouble(amount))));
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }


                tabLayout.addView(row);
            }
        }
        dialogs.show();

//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
      // linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
/*
        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit = linearView.getDrawingCache();
        return bit;*/

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        linearView.setDrawingCacheEnabled(true);
        bitmap= linearView.getDrawingCache();
        photoPrinter.printBitmap("pay.jpg", bitmap);
        return bit;// creates bitmap and returns the same



        // creates bitmap and returns the same
    }

    public Bitmap convertLayoutToImagew(Context context) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(context);
        dialogs.setContentView(R.layout.printdialog);
        dialogs.setCanceledOnTouchOutside(true);
        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;

        ImageView img = (ImageView) dialogs.findViewById(R.id.img);
        compname = (TextView) dialogs.findViewById(R.id.compname);
        tel = (TextView) dialogs.findViewById(R.id.tel);
        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
        date = (TextView) dialogs.findViewById(R.id.date);
        custname = (TextView) dialogs.findViewById(R.id.custname);
        note = (TextView) dialogs.findViewById(R.id.note);
        vhType = (TextView) dialogs.findViewById(R.id.vhType);
        paytype = (TextView) dialogs.findViewById(R.id.paytype);
        total = (TextView) dialogs.findViewById(R.id.total);
        discount = (TextView) dialogs.findViewById(R.id.discount);
        tax = (TextView) dialogs.findViewById(R.id.tax);
        ammont = (TextView) dialogs.findViewById(R.id.ammont);
        textW = (TextView) dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
//

        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFinishPrint) {
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialogs.dismiss();
                }
            }
        });


        String voucherTyp = "";
        switch (voucherShow.getVoucherType()) {
            case 504:
                voucherTyp = "فاتورة بيع";
                break;
            case 506:
                voucherTyp = "فاتورة مرتجعات";
                break;
            case 508:
                voucherTyp = "طلب جديد";
                break;
        }

        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucherShow.getVoucherNumber());
        date.setText(voucherShow.getVoucherDate());
        custname.setText(voucherShow.getCustName());
        note.setText(voucherShow.getRemark());
        vhType.setText(voucherTyp);

        paytype.setText((voucherShow.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucherShow.getSubTotal());
        discount.setText("" + voucherShow.getTotalVoucherDiscount());
        tax.setText("" + voucherShow.getTax());
        ammont.setText("" + voucherShow.getNetSales());


        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        for (int j = 0; j < itemsList.size(); j++) {

            if (voucherShow.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(getActivity());


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);

                    switch (i) {
                        case 0:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(decimalFormat.format(amount));
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }


                tabLayout.addView(row);
            }
        }
        dialogs.show();

//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit = linearView.getDrawingCache();
        return bit;
        // creates bitmap and returns the same
    }

//    private Bitmap convertLayoutToImageTally(Voucher voucher,int okShow,int start,int end,List<Item>items) {
//        LinearLayout linearView=null;
//
//        final Dialog dialogs=new Dialog(getActivity());
//        dialogs.setContentView(R.layout.printdialog_tally);
////            fill_theVocher( voucher);
//
//        List <CompanyInfo>comp=mDbHandler.getAllCompanyInfo();
//        CompanyInfo companyInfo = null;
//        if(comp.size()!=0){
//         companyInfo =comp.get(0);}
//        else {
//            Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_SHORT).show();
//        }
//
//        TextView compname,tel,taxNo,vhNo,date,custname,note,vhType,paytype,total,discount,tax,ammont,textW;
//
//        ImageView img =(ImageView)dialogs.findViewById(R.id.img);
//        compname=(TextView)dialogs.findViewById(R.id.compname);
//        tel=(TextView)dialogs.findViewById(R.id.tel);
//        taxNo=(TextView)dialogs.findViewById(R.id.taxNo);
//        vhNo=(TextView)dialogs.findViewById(R.id.vhNo);
//        date=(TextView)dialogs.findViewById(R.id.date);
//        custname=(TextView)dialogs.findViewById(R.id.custname);
//        note=(TextView)dialogs.findViewById(R.id.note);
//        vhType=(TextView)dialogs.findViewById(R.id.vhType);
//        paytype=(TextView)dialogs.findViewById(R.id.paytype);
//        total=(TextView)dialogs.findViewById(R.id.total);
//        discount=(TextView)dialogs.findViewById(R.id.discount);
//        tax=(TextView)dialogs.findViewById(R.id.tax);
//        ammont=(TextView)dialogs.findViewById(R.id.ammont);
//        textW=(TextView)dialogs.findViewById(R.id.wa1);
//        TableLayout tabLayout=(TableLayout)dialogs.findViewById(R.id.tab);
//        TableLayout sumLayout = (TableLayout) dialogs.findViewById(R.id.table);
//        TextView noteLast =(TextView) dialogs.findViewById(R.id.notelast);
//        TableRow sing=(TableRow) dialogs.findViewById(R.id.sing);
////
//
//        TextView doneinsewooprint =(TextView) dialogs.findViewById(R.id.done);
//
//        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(isFinishPrint) {
//                    try {
//                        closeBT();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    dialogs.dismiss();
//                }
//            }
//        });
//
//
//        String voucherTyp = "";
//        switch (voucher.getVoucherType()) {
//            case 504:
//                voucherTyp = "فاتورة بيع";
//                break;
//            case 506:
//                voucherTyp = "فاتورة مرتجعات";
//                break;
//            case 508:
//                voucherTyp = "طلب جديد";
//                break;
//        }
//
//        img.setImageBitmap(companyInfo.getLogo());
//        compname.setText(companyInfo.getCompanyName());
//        tel.setText("" + companyInfo.getcompanyTel());
//        taxNo.setText("" + companyInfo.getTaxNo());
//        vhNo.setText("" + voucher.getVoucherNumber());
//        date.setText(voucher.getVoucherDate());
//        custname.setText(voucher.getCustName());
//        note.setText(voucher.getRemark());
//        vhType.setText(voucherTyp);
//
//        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
//        total.setText("" + voucher.getSubTotal());
//        discount.setText("" + totalDiscount);
//        tax.setText("" + voucher.getTax());
//        ammont.setText("" + voucher.getNetSales());
//
//        if(okShow==0){
//            sumLayout.setVisibility(View.GONE);
//            noteLast.setVisibility(View.GONE);
//            sing.setVisibility(View.GONE);
//        }else{
//            sumLayout.setVisibility(View.VISIBLE);
//            noteLast.setVisibility(View.VISIBLE);
//            sing.setVisibility(View.VISIBLE);
//        }
//        img.setVisibility(View.INVISIBLE);
//        compname.setVisibility(View.INVISIBLE);
//
//
//
//        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() != 1) {
//            textW.setVisibility(View.GONE);
//        }else {
//            textW.setVisibility(View.VISIBLE);
//        }
//
//
//        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//        lp2.setMargins(0, 7, 0, 0);
//        lp3.setMargins(0, 7, 0, 0);
//
//        for (int j = 0; j < itemsList.size(); j++) {
//
//            if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
//                final TableRow row = new TableRow(getActivity());
//
//
//                for (int i = 0; i <= 7; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                    lp.setMargins(0, 10, 0, 0);
//                    row.setLayoutParams(lp);
//
//                    TextView textView = new TextView(getActivity());
//                    textView.setGravity(Gravity.CENTER);
//                    textView.setTextSize(32);
//
//                    switch (i) {
//                        case 0:
//                            textView.setText(itemsList.get(j).getItemName());
//                            textView.setLayoutParams(lp3);
//                            break;
//
//
//                        case 1:
//                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
//                                textView.setText("" + itemsList.get(j).getUnit());
//                                textView.setLayoutParams(lp2);
//                            }else{
//                                textView.setText("" + itemsList.get(j).getQty());
//                                textView.setLayoutParams(lp2);
//                            }
//                            break;
//
//                        case 2:
//                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
//                                textView.setText("" + itemsList.get(j).getQty());
//                                textView.setLayoutParams(lp2);
//                                textView.setVisibility(View.VISIBLE);
//                            }else {
//                                textView.setVisibility(View.GONE);
//                            }
//                            break;
//
//                        case 3:
//                            textView.setText("" + itemsList.get(j).getPrice());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//
//                        case 4:
//                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
//                            amount = convertToEnglish(amount);
//                            textView.setText(amount);
//                            textView.setLayoutParams(lp2);
//                            break;
//                    }
//                    row.addView(textView);
//                }
//
//
//                tabLayout.addView(row);
//            }
//        }
//        dialogs.show();
//
////        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
//        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);
//
//        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
//
//
////        linearView.setDrawingCacheEnabled(true);
////        linearView.buildDrawingCache();
////        Bitmap bit = linearView.getDrawingCache();
//
//
//        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Drawable bgDrawable = linearView.getBackground();
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        linearView.draw(canvas);
//        return bitmap;// creates bitmap and returns the same
//    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    public double getItemsTotal() {
        double total = 0;

        for (Item i : items) {
            total = total + i.getAmount();
        }

        return total;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("SetTextI18n")
    public void hiddenDialog() {

try {
    SaleInvoicePrinted = voucher;
    List<Item> itemVOCHER = new ArrayList<>();
    itemVOCHER = mDbHandler.getAllItemsBYVOCHER(String.valueOf(voucher.getVoucherNumber()), voucher.getVoucherType());

   // Log.e("itemVOCHER==", "" + itemVOCHER.size());
    PdfConverter pdf = new PdfConverter(SalesInvoice.this.getActivity());
    pdf.exportListToPdf(itemVOCHER, "Vocher", "", 14);
}catch (Exception e){
    Log.e("Exception22==", "" + e.getMessage());
}
    //    convertLayoutToImage(voucher);
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.print);
//
//        final Button okButton = dialog.findViewById(R.id.print1);
//        final LinearLayout linearLayout = dialog.findViewById(R.id.linear1);
//        TableLayout tabLayout = (TableLayout) dialog.findViewById(R.id.table_);
//
//        TextView companyName = dialog.findViewById(R.id.company);
//        TextView phone = dialog.findViewById(R.id.phone);
//        TextView taxNo = dialog.findViewById(R.id.tax_no);
//        TextView date = dialog.findViewById(R.id.date);
//        TextView vouch_no = dialog.findViewById(R.id.voucher_no);
//        TextView vouchType = dialog.findViewById(R.id.voucher_type);
//        TextView payMethod = dialog.findViewById(R.id.payMethod);
//        TextView cust = dialog.findViewById(R.id.cust_);
//        TextView remark = dialog.findViewById(R.id.remark_);
//        TextView totalNoTax = dialog.findViewById(R.id.total_noTax);
//        TextView discount = dialog.findViewById(R.id.discount);
//        TextView tax = dialog.findViewById(R.id.tax);
//        TextView netSale = dialog.findViewById(R.id.net_sales_);
//
//        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
//
//        companyName.setText(companyInfo.getCompanyName());
//        phone.setText(phone.getText().toString() + companyInfo.getcompanyTel());
//        taxNo.setText(taxNo.getText().toString() + companyInfo.getTaxNo());
//        date.setText(date.getText().toString() + voucher.getVoucherDate());
//        vouch_no.setText(vouch_no.getText().toString() + voucher.getVoucherNumber());
//        remark.setText(remark.getText().toString() + voucher.getRemark());
//        cust.setText(cust.getText().toString() + voucher.getCustName());
//        totalNoTax.setText(totalNoTax.getText().toString() + voucher.getSubTotal());
//        discount.setText(discount.getText().toString() + totalDiscount);
//        tax.setText(tax.getText().toString() + voucher.getTax());
//        netSale.setText(netSale.getText().toString() + voucher.getNetSales());
//
//        String voucherTyp = "";
//        switch (voucher.getVoucherType()) {
//            case 504:
//                voucherTyp = "فاتورة بيع";
//                break;
//            case 506:
//                voucherTyp = "فاتورة مرتجعات";
//                break;
//            case 508:
//                voucherTyp = "طلب جديد";
//                break;
//        }
//        vouchType.setText(vouchType.getText().toString() + voucherTyp);
//        payMethod.setText(payMethod.getText().toString() + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
//
//        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2.0f);
//        lp2.setMargins(2, 7, 0, 0);
//        lp3.setMargins(0, 7, 0, 0);
//
//        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
//
//            final TableRow headerRow = new TableRow(getActivity());
//
//            TextView headerView7 = new TextView(getActivity());
//            headerView7.setGravity(Gravity.CENTER);
//            headerView7.setText("المجموع");
//            headerView7.setLayoutParams(lp2);
//            headerView7.setTextSize(12);
//            headerRow.addView(headerView7);
//
//            TextView headerView6 = new TextView(getActivity());
//            headerView6.setGravity(Gravity.CENTER);
//            headerView6.setText("الخصم");
//            headerView6.setLayoutParams(lp2);
//            headerView6.setTextSize(12);
//            headerRow.addView(headerView6);
//
//            TextView headerView5 = new TextView(getActivity());
//            headerView5.setGravity(Gravity.CENTER);
//            headerView5.setText("المجاني");
//            headerView5.setLayoutParams(lp2);
//            headerView5.setTextSize(12);
//            headerRow.addView(headerView5);
//
//            TextView headerView4 = new TextView(getActivity());
//            headerView4.setGravity(Gravity.CENTER);
//            headerView4.setText("سعر الوحدة");
//            headerView4.setLayoutParams(lp2);
//            headerView4.setTextSize(12);
//            headerRow.addView(headerView4);
//
//            TextView headerView3 = new TextView(getActivity());
//            headerView3.setGravity(Gravity.CENTER);
//            headerView3.setText("الوزن");
//            headerView3.setLayoutParams(lp2);
//            headerView3.setTextSize(12);
//            headerRow.addView(headerView3);
//
//            TextView headerView2 = new TextView(getActivity());
//            headerView2.setGravity(Gravity.CENTER);
//            headerView2.setText("العدد");
//            headerView2.setLayoutParams(lp2);
//            headerView2.setTextSize(12);
//            headerRow.addView(headerView2);
//
//            TextView headerView1 = new TextView(getActivity());
//            headerView1.setGravity(Gravity.CENTER);
//            headerView1.setText("السلعة");
//            headerView1.setLayoutParams(lp3);
//            headerView1.setTextSize(12);
//            headerRow.addView(headerView1);
//
//            tabLayout.addView(headerRow);
//        } else {
//            final TableRow headerRow = new TableRow(getActivity());
//            TextView headerView1 = new TextView(getActivity());
//
//            TextView headerView6 = new TextView(getActivity());
//            headerView6.setGravity(Gravity.CENTER);
//            headerView6.setText("المجموع");
//            headerView6.setLayoutParams(lp2);
//            headerView6.setTextSize(12);
//            headerRow.addView(headerView6);
//
//            TextView headerView5 = new TextView(getActivity());
//            headerView5.setGravity(Gravity.CENTER);
//            headerView5.setText("الخصم");
//            headerView5.setLayoutParams(lp2);
//            headerView5.setTextSize(12);
//            headerRow.addView(headerView5);
//
//            TextView headerView4 = new TextView(getActivity());
//            headerView4.setGravity(Gravity.CENTER);
//            headerView4.setText("المجاني");
//            headerView4.setLayoutParams(lp2);
//            headerView4.setTextSize(12);
//            headerRow.addView(headerView4);
//
//            TextView headerView3 = new TextView(getActivity());
//            headerView3.setGravity(Gravity.CENTER);
//            headerView3.setText("سعر الوحدة");
//            headerView3.setLayoutParams(lp2);
//            headerView3.setTextSize(12);
//            headerRow.addView(headerView3);
//
//            TextView headerView2 = new TextView(getActivity());
//            headerView2.setGravity(Gravity.CENTER);
//            headerView2.setText("العدد");
//            headerView2.setLayoutParams(lp2);
//            headerView2.setTextSize(12);
//            headerRow.addView(headerView2);
//
//            headerView1.setGravity(Gravity.CENTER);
//            headerView1.setText("السلعة");
//            headerView1.setLayoutParams(lp3);
//            headerView1.setTextSize(12);
//            headerRow.addView(headerView1);
//
//            tabLayout.addView(headerRow);
//        }
//
//        for (int j = 0; j < itemsList.size(); j++) {
//            final TableRow row = new TableRow(getActivity());
//
//            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
//
//                for (int i = 0; i <= 7; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                    lp.setMargins(0, 10, 0, 0);
//                    row.setLayoutParams(lp);
//
//                    TextView textView = new TextView(getActivity());
//                    textView.setGravity(Gravity.CENTER);
//                    textView.setTextSize(10);
//
//                    switch (i) {
//                        case 6:
//                            textView.setText(itemsList.get(j).getItemName());
//                            textView.setLayoutParams(lp3);
//                            break;
//
//                        case 5:
//                            textView.setText(itemsList.get(j).getUnit());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 4:
//                            textView.setText("" + itemsList.get(j).getQty());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 3:
//                            textView.setText("" + itemsList.get(j).getPrice());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 2:
//                            textView.setText("" + itemsList.get(j).getBonus());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 1:
//                            textView.setText("" + itemsList.get(j).getDisc());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 0:
//                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
//                            amount = convertToEnglish(amount);
//                            textView.setText(amount);
//                            textView.setLayoutParams(lp2);
//                            break;
//                    }
//                    row.addView(textView);
//                }
//
//            } else {
//                for (int i = 0; i <= 6; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                    lp.setMargins(0, 10, 0, 0);
//                    row.setLayoutParams(lp);
//
//                    TextView textView = new TextView(getActivity());
//                    textView.setGravity(Gravity.CENTER);
//                    textView.setTextSize(10);
//
//                    switch (i) {
//                        case 5:
//                            textView.setText(itemsList.get(j).getItemName());
//                            textView.setLayoutParams(lp3);
//                            break;
//
//                        case 4:
//                            textView.setText(itemsList.get(j).getQty()+"");
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 3:
//                            textView.setText("" + itemsList.get(j).getPrice());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 2:
//                            textView.setText("" + itemsList.get(j).getBonus());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 1:
//                            textView.setText("" + itemsList.get(j).getDisc());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//                        case 0:
//                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
//                            amount = convertToEnglish(amount);
//                            textView.setText(amount);
//                            textView.setLayoutParams(lp2);
//                            break;
//                    }
//                    row.addView(textView);
//                }
//            }
//            tabLayout.addView(row);
//
//        }
//
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PrintHelper photoPrinter = new PrintHelper(getActivity());
//                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//
//                linearLayout.setDrawingCacheEnabled(true);
//                Bitmap bitmap = linearLayout.getDrawingCache();
//                photoPrinter.printBitmap("invoice.jpg", bitmap);
//
//            }
//        });
//
//        dialog.show();

    }
    public void sharwhats() {

        try {
       SaleInvoicePrinted = voucherForPrint;
            List<Item> itemVOCHER = new ArrayList<>();
            itemVOCHER = mDbHandler.getAllItemsBYVOCHER(String.valueOf(SaleInvoicePrinted.getVoucherNumber()), SaleInvoicePrinted.getVoucherType());

            // Log.e("itemVOCHER==", "" + itemVOCHER.size());
            PdfConverter pdf = new PdfConverter(SalesInvoice.this.getActivity());
            pdf.exportListToPdf(itemVOCHER, "Vocher", "", 18);
      } catch (Exception e) {
            Log.e("Exception22==", "" + e.getMessage());
       }
    }
    void findBT() {

//        try {
//            /*  very important **********************************************************/
//            closeBT();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        DecimalFormat threeDForm = new DecimalFormat("0.000");
        itemsString = "";
        itemsString2 = "";
        for (int j = 0; j < itemsList.size(); j++) { // don't know why is it here :/
            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
            amount = convertToEnglish(amount);

            String row = itemsList.get(j).getItemName() + "                                             ";
            row = row.substring(0, 21) + itemsList.get(j).getUnit() + row.substring(21, row.length());
            row = row.substring(0, 31) + itemsList.get(j).getQty() + row.substring(31, row.length());
            row = row.substring(0, 41) + itemsList.get(j).getPrice() + row.substring(41, row.length());
            row = row.substring(0, 52) + convertToEnglish(threeDForm.format(Double.parseDouble(convertToEnglish(amount))));
            row = row.trim();
            itemsString = itemsString + "\n" + row;

            String row2 = itemsList.get(j).getItemName() + "                                             ";
            row2 = row2.substring(0, 21) + itemsList.get(j).getQty() + row2.substring(21, row2.length());
            row2 = row2.substring(0, 31) + itemsList.get(j).getPrice() + row2.substring(31, row2.length());
            row2 = row2.substring(0, 42) + convertToEnglish(threeDForm.format(Double.parseDouble(convertToEnglish(amount))));
            row2 = row2.trim();
            itemsString2 = itemsString2 + "\n" + row2;

        }

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // MP300 is the name of the bluetooth printer device07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ C4:73:1E:67:29:6C
                    /*07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ E8:99:C4:FF:B1:AC
                    07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ 0C:A6:94:35:11:27*/

                    /*Log.e("sex",device.getName());*/
//                    if (device.getName().equals("mobile printer")) { // PR3-30921446556
                    /*Log.e("sex1",device.getAddress());*/
                    mmDevice = device;
//                        break;
//                    }
                }
            }
//            myLabel.setText("Bluetooth Device Found");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tries to open a connection to the bluetooth printer device
    void openBT(int casePrinter) throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();


            switch (casePrinter) {

                case 1:
                    sendData();
                    break;
                case 2:
                    Settings settings = mDbHandler.getAllSettings().get(0);
                    for (int i = 0; i < settings.getNumOfCopy(); i++) {
                        send_dataSewoo(voucher);
                    }

                    break;
                case 3:
                    sendData2();
                    break;


            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    void printTally(Voucher voucher) {
        pd = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pd.setTitleText(getActivity().getResources().getString(R.string.Printing));
        pd.setCancelable(false);
        pd.show();
        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        List<Item> items1 = new ArrayList<>();
        for (int j = 0; j < itemsList.size(); j++) {

            if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                items1.add(itemsList.get(j));

            }
        }

        Log.e("Items1__", "" + items1.size() + "    " + (items1.size() <= 17));

        if (items1.size() <= 17) {
            bitmap = convertLayoutToImageTally(voucher, 1, 0, items1.size(), items1);
            Log.e("bitmap",""+bitmap);
            try {
                Settings settings = mDbHandler.getAllSettings().get(0);
                File file = savebitmap(bitmap, settings.getNumOfCopy(), "org");
                pd.dismissWithAnimation();
                Log.e("file",""+file);
            } catch (IOException e) {
                pd.dismissWithAnimation();
                e.printStackTrace();
            }

        } else {

            Settings settings = mDbHandler.getAllSettings().get(0);
            for (int i = 0; i < settings.getNumOfCopy(); i++) {
                bitmap = convertLayoutToImageTally(voucher, 0, 0, 17, items1);
                bitmap2 = convertLayoutToImageTally(voucher, 1, 17, items1.size(), items1);


                try {

                    File file = savebitmap(bitmap, 1, "fir" + "" + i);
                    pd.dismissWithAnimation();
                    File file2 = savebitmap(bitmap2, 1, "sec" + "" + i);

                    Log.e("save image ", "" + file.getAbsolutePath());
                } catch (IOException e) {
                    pd.dismissWithAnimation();
                    e.printStackTrace();
                }
            }

        }


    }


    private Bitmap convertLayoutToImageTally(Voucher voucher, int okShow, int start, int end, List<Item> items) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(getActivity());
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(true);
        dialogs.setContentView(R.layout.printdialog_tally);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW, noteLast;
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);

        compname = (TextView) dialogs.findViewById(R.id.compname);
        tel = (TextView) dialogs.findViewById(R.id.tel);
        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
        date = (TextView) dialogs.findViewById(R.id.date);
        custname = (TextView) dialogs.findViewById(R.id.custname);
        note = (TextView) dialogs.findViewById(R.id.note);
        vhType = (TextView) dialogs.findViewById(R.id.vhType);
        paytype = (TextView) dialogs.findViewById(R.id.paytype);
        total = (TextView) dialogs.findViewById(R.id.total);
        discount = (TextView) dialogs.findViewById(R.id.discount);
        tax = (TextView) dialogs.findViewById(R.id.tax);
        ammont = (TextView) dialogs.findViewById(R.id.ammont);
        textW = (TextView) dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
        TableLayout sumLayout = (TableLayout) dialogs.findViewById(R.id.table);
        noteLast = (TextView) dialogs.findViewById(R.id.notelast);
        TableRow sing = (TableRow) dialogs.findViewById(R.id.sing);
//

        if (okShow == 0) {
            sumLayout.setVisibility(View.GONE);
            noteLast.setVisibility(View.GONE);
            sing.setVisibility(View.GONE);
        } else {
            sumLayout.setVisibility(View.VISIBLE);
            noteLast.setVisibility(View.VISIBLE);
            sing.setVisibility(View.VISIBLE);
        }
        img.setVisibility(View.INVISIBLE);
        compname.setVisibility(View.INVISIBLE);


        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(isFinishPrint) {
//                    try {
//                        closeBT();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                dialogs.dismiss();
//                }
            }
        });


        String voucherTyp = "";
        switch (voucher.getVoucherType()) {
            case 504:
                voucherTyp = "فاتورة بيع";
                break;
            case 506:
                voucherTyp = "فاتورة مرتجعات";
                break;
            case 508:
                voucherTyp = "طلب جديد";
                break;
        }
        try {
            img.setImageBitmap(companyInfo.getLogo());
        }
        catch (Exception e)
        {}

        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);

        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucher.getSubTotal());
        discount.setText("" + voucher.getTotalVoucherDiscount());
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());


        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        for (int j = start; j < end; j++) {

            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(getActivity());


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(32);

                    switch (i) {
                        case 0:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }


                tabLayout.addView(row);
            }
        }


//        dialogs.show();


//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = linearView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        linearView.draw(canvas);

        return bitmap;// creates bitmap and returns the same
    }


    public  File savebitmap(Bitmap bmp, int numCope, String next) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = null;
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/VanSaleS/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (int i = 0; i < numCope; i++) {
            String targetPdf = directory_path + "testimageSales" + i + "" + next + ".png";
            f = new File(targetPdf);


//        f.createNewFile();
            try {
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            }
            catch (Exception e)
            {
                pd.dismissWithAnimation();
                verifyStoragePermissions(getActivity());


            }
        }
        return f;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() throws IOException {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
//                                                myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            closeBT();
            e.printStackTrace();
        } catch (Exception e) {
            closeBT();
            e.printStackTrace();
        }
    }

    /*
     * This will send data to be printed by the bluetooth printer
     */
    void sendData() throws IOException {
        try {
            CompanyInfo companyInfo = new CompanyInfo();

            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            try {
                companyInfo = mDbHandler.getAllCompanyInfo().get(0);
            } catch (Exception e) {
                companyInfo.setCompanyName("Companey Name");
                companyInfo.setTaxNo(0);
                companyInfo.setCompanyTel("00000");
//                companyInfo.setLogo();
            }

            //&& !companyInfo.getLogo().equals(null)
            if (!companyInfo.getCompanyName().equals("") && !companyInfo.getcompanyTel().equals("0") && companyInfo.getTaxNo() != -1) {
                pic.setImageBitmap(companyInfo.getLogo());
                pic.setDrawingCacheEnabled(true);
                Bitmap bitmap = pic.getDrawingCache();
                try {
                    PrintPic printPic = PrintPic.getInstance();
                    printPic.init(bitmap);
                    byte[] bitmapdata = printPic.printDraw();
                } catch (Exception e) {
                    Log.e("pic sales invoice ", "**");
                }

                for (int i = 1; i <= numOfCopy; i++) {
                    Thread.sleep(1000);
                    String voucherTyp = "";
                    switch (voucher.getVoucherType()) {
                        case 504:
                            voucherTyp = "فاتورة بيع";
                            break;
                        case 506:
                            voucherTyp = "فاتورة مرتجعات";
                            break;
                        case 508:
                            voucherTyp = "طلب جديد";
                            break;
                    }

                    if (companyInfo.getLogo() != null) {

//                        mmOutputStream.write(bitmapdata);
                        printCustom(" \n ", 1, 0);
                    }

                    printCustom(companyInfo.getCompanyName() + "\n", 1, 1);
                    printCustom("هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", 1, 0);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    printCustom("رقم الفاتورة : " + voucher.getVoucherNumber() + "          التاريخ: " + voucher.getVoucherDate() + "\n", 1, 2);
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("اسم العميل   : " + voucher.getCustName() + "\n", 1, 2);
                    printCustom("ملاحظة        : " + voucher.getRemark() + "\n", 1, 2);
                    printCustom("نوع الفاتورة : " + voucherTyp + "\n", 1, 2);
                    printCustom("طريقة الدفع  : " + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", 1, 2);
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                        printCustom(" السلعة              " + "العدد   " + "الوزن    " + "سعر الوحدة   " + "المجموع  " + "\n", 0, 2);
                        printCustom("----------------------------------------------" + "\n", 1, 2);

                        printCustom(itemsString + "\n", 0, 2);
                    } else {
                        printCustom(" السلعة              " + "العدد   " + "سعر الوحدة   " + "المجموع  " + "\n", 0, 2);
                        printCustom("----------------------------------------------" + "\n", 1, 2);

                        printCustom(itemsString2 + "\n", 0, 2);
                    }

                    printCustom("----------------------------------------------" + "\n", 1, 2);

                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("اجمالي الكمية:  " + convertToEnglish(threeDForm.format(voucher.getTotalQty())) + " : " + " \n ", 1, 0);
                    printCustom("المجموع  : " + voucher.getSubTotal() + "\n", 1, 2);
                    printCustom("الخصم    : " + voucher.getTotalVoucherDiscount() + "\n", 1, 2);

                    printCustom("الضريبة  : " + voucher.getTax() + "\n", 1, 2);
                    printCustom("الصافي   : " + voucher.getNetSales() + "\n", 1, 2);
                    if (voucher.getVoucherType() != 506) {
                        printCustom("استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n", 1, 2);
                        printCustom("اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n", 1, 2);
                        mmOutputStream.write(PrinterCommands.FEED_LINE);
                        printCustom("المستلم : ________________ التوقيع : __________" + "\n", 1, 2);
                    }
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);

                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    //  Arabic864 arabic = new Arabic864();
                    //   byte[] arabicArr = arabic.Convert(  new StringBuilder(msg).reverse().toString(),false);
                    //    byte[] arabicArr = arabic.Convert("الاسم",false);
//                    Log.e("mymsg", "" + msg);
//                        Log.e("Not ---LtoR",""+msg);
//                        Log.e("LtoR",""+msg);
//                        byte[] arabicArr = arabic.Convert(new StringBuilder(msg).reverse().toString(), false);
//                        Log.e("byte", "" + arabicArr.toString());
//                        mmOutputStream.write(arabicArr);
                }
                closeBT();
                // tell the user data were sent
//                myLabel.setText("Data Sent");

            } else
                Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_LONG).show();

        } catch (NullPointerException e) {
            closeBT();
            e.printStackTrace();
        } catch (Exception e) {
            closeBT();
            e.printStackTrace();
        }
    }

    void sendData2() {
        try {

            double totalQty = 0;
            double totalPrice = 0;
            double totalDisc = 0;
            double totalNet = 0;
            double totalTax = 0;
            double totalTotal = 0;

            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
            if (!companyInfo.getCompanyName().equals("") && !companyInfo.getcompanyTel().equals("0") && !companyInfo.getLogo().equals(null) && companyInfo.getTaxNo() != -1) {

                pic.setImageBitmap(companyInfo.getLogo());
                pic.setDrawingCacheEnabled(true);
                Bitmap bitmap = pic.getDrawingCache();

                PrintPic printPic = PrintPic.getInstance();
                printPic.init(bitmap);
                byte[] bitmapdata = printPic.printDraw();


                for (int i = 1; i <= numOfCopy; i++) {

                    //  printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);

                    if (companyInfo.getLogo() != null) {

                        mmOutputStream.write(bitmapdata);
                        //     printCustom(" \n ", 1, 1);
                    }

                    printCustom(companyInfo.getCompanyName() + "   \n   ", 1, 0);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("\n الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("التاريخ        " + voucher.getVoucherDate() + " : " + " \n ", 1, 0);
                    printCustom("رقم الفاتورة   " + voucher.getVoucherNumber() + " : " + "\n", 1, 0);
                    printCustom("رقم العميل     " + voucher.getCustNumber() + " : " + "\n", 1, 0);
                    printCustom("اسم العميل " + " : " + voucher.getCustName() + "\n", 1, 0);
                    printCustom("مندوب المبيعات " + voucher.getSaleManNumber() + " : " + "\n", 1, 0);
                    printCustom("------------------------------------------" + "\n", 1, 0);

                    int serial = 1;
                    for (int j = 0; j < itemsList.size(); j++) {
                        if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            String amountATax = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc() + itemsList.get(j).getTaxValue());
                            amount = convertToEnglish(amount);
                            amountATax = convertToEnglish(amountATax);


                            printCustom("(" + serial + "" + "\n", 1, 0);
                            printCustom("رقم الصنف " + itemsList.get(j).getItemNo() + " : " + " \n ", 1, 0);
                            printCustom("الصنف " + " : " + itemsList.get(j).getItemName() + " \n ", 1, 0);
                            printCustom("الكمية    " + itemsList.get(j).getQty() + " : " + " \n ", 1, 0);
                            printCustom("المجاني    " + itemsList.get(j).getBonus() + " : " + " \n ", 1, 0);
                            printCustom("السعر     " + " JD " + itemsList.get(j).getPrice() + " : " + " \n ", 1, 0);
                            printCustom("الخصم     " + " JD " + itemsList.get(j).getDisc() + " : " + " \n ", 1, 0);
                            printCustom("الصافي    " + " JD " + convertToEnglish(threeDForm.format(Double.parseDouble(amount))) + " : " + "\n", 1, 0);
                            printCustom("الضريبة   " + " JD " + convertToEnglish(threeDForm.format(itemsList.get(j).getTaxValue())) + " : " + " \n ", 1, 0);
                            printCustom("الاجمالي   " + " JD " + convertToEnglish(threeDForm.format(Double.parseDouble(amountATax))) + " : " + " \n ", 1, 0);

                            printCustom("* * * * * * * * * * * * * " + " \n ", 1, 0);

                            serial++;
                            totalQty += itemsList.get(j).getQty() + itemsList.get(j).getBonus();
                            totalPrice += itemsList.get(j).getPrice();
                            totalDisc += itemsList.get(j).getDisc();
                            totalNet += (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            totalTax += itemsList.get(j).getTaxValue();
                            totalTotal += itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc() + itemsList.get(j).getTaxValue();
                        }
                    }


                    printCustom("اجمالي الكمية  " + convertToEnglish(threeDForm.format(totalQty)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي السعر   " + " JD " + convertToEnglish(threeDForm.format(totalPrice)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الخصم   " + " JD " + convertToEnglish(threeDForm.format(totalDisc)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الصافي  " + " JD " + convertToEnglish(threeDForm.format(totalNet)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الضريبة " + " JD " + convertToEnglish(threeDForm.format(totalTax)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الإجمالي " + " JD " + convertToEnglish(threeDForm.format(totalTotal)) + " : " + " \n ", 1, 0);

                    if (voucher.getVoucherType() != 506) {
                        printCustom("استلمت البضاعة خالية من اي عيب او توالف" + " \n ", 1, 0);
                        printCustom("توقيع العميل" + "  " + "_______________" + " \n ", 1, 0);
                    }
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("\n", 1, 0);
                    printCustom("\n", 1, 0);

                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                }
                closeBT();


            } else {
                Toast.makeText(getActivity(), R.string.error_companey_info, Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
//
            Arabic864 arabic = new Arabic864();
            byte[] arabicArr = arabic.Convert(msg, false);
            mmOutputStream.write(arabicArr);
//            mmOutputStream.write(msg.getBytes());

//            outputStream.write(PrinterCommands.LF);
//            outputStream.write(cc);
//            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            workerThread.stop();
//            myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                String ipAddress = mDbHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
                String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";

                URL url = new URL(URL_TO_HIT);

                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('2'), "UTF-8");

                JSONArray jsonArrayVouchers = new JSONArray();
                voucher.setIsPosted(1);
                jsonArrayVouchers.put(voucher.getJSONObject());

                JSONArray jsonArrayItems = new JSONArray();
                for (int i = 0; i < itemsList.size(); i++) {
                    itemsList.get(i).setIsPosted(1);
                    jsonArrayItems.put(itemsList.get(i).getJSONObject());
                }

                String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
                table1 += "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
                        + "&" + "Payments=[]"
                        + "&" + "Payments_Checks=[]"
                        + "&" + "Added_Customers=[]"
                        + "&" + "TABLE_TRANSACTIONS=[]";

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                try {
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(table1);

                    wr.flush();
                } catch (Exception e) {
                    Log.e("here****", e.getMessage());
                }


                // get response
                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JsonResponse = sb.toString();
                // Log.e("tag", "" + JsonResponse);

                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                if (s.contains("SUCCESS")) {
                    mDbHandler.updateVoucher2(voucher.getVoucherNumber());
                    mDbHandler.updateVoucherDetails2(voucher.getVoucherNumber());

                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Success");
                } else {
                    Toast.makeText(getActivity(), "Failed to export data", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

        @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_Camera_Barcode: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i=new Intent(getActivity(),ScanActivity.class);
                    startActivity(i);
//                    searchByBarcodeNo(s + "");
                } else {
                    Toast.makeText(getActivity(), "check permission Camera ", Toast.LENGTH_SHORT).show();

                }
                return;
            }

                case REQUEST_LOCATION_PERMISSION: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                       getCurrentLocation();

                    } else {
                    Toast.makeText(getActivity(), "check permission location ", Toast.LENGTH_SHORT).show();

                    }
                    return;
                }
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Location", "granted");
                    Log.e("LocationIn","GoToMain 1");

                    saveVoucherData();
                    // locationPermissionRequest.displayLocationSettingsRequest(Login.this);
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Location", "granted updates");
                        Log.e("LocationIn","GoToMain 2");
                        //Request location updates:
//                        locationPermissionRequest.
//                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                    }

                } else {
                    Log.e("LocationIn","GoToMain 3");
                    Log.e("Location", "Deny");
                    // permission, denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(getActivity(), "Please Allow location permission  to save voucher ", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }





//    public  void searchByBarcodeNo(String barcodeValue) {
//        if(!barcodeValue.equals(""))
//        {
//            ArrayList<Item> filteredList = new ArrayList<>();
//            for (int k = 0; k < jsonItemsList.size(); k++) {
//                if (jsonItemsList.get(k).getItemNo().equals(barcodeValue)){
//                    filteredList.add(jsonItemsList.get(k));
//                }
//            }
//            RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, getActivity());
//            recyclerView.setAdapter(adapter);
//            Log.e("filteredList=","" + filteredList.size());
//            if(filteredList.size()==0)
//            {
//                Toast.makeText(getActivity(), barcodeValue+"\tNot Found", Toast.LENGTH_LONG).show();
//            }
//
//
//
//        } else {
//            RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
//            recyclerView.setAdapter(adapter);
//
//
//        }
//    }

}// class salesInvoice
