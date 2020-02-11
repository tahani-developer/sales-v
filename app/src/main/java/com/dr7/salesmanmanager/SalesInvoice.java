package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemsQtyOffer;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Reports.AccountReport;
import com.dr7.salesmanmanager.Reports.ItemsReport;
import com.dr7.salesmanmanager.Reports.VouchersReport;
import com.ganesh.intermecarabic.Arabic864;

import org.json.JSONArray;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static android.support.v4.app.DialogFragment.STYLE_NORMAL;
import static android.support.v4.app.DialogFragment.STYLE_NO_FRAME;
import static com.dr7.salesmanmanager.Activities.discvalue_static;
import static com.dr7.salesmanmanager.AddItemsFragment2.REQUEST_Camera;
import static com.dr7.salesmanmanager.AddItemsFragment2.jsonItemsList;
import static com.dr7.salesmanmanager.AddItemsFragment2.s;
import static com.dr7.salesmanmanager.AddItemsFragment2.total_items_quantity;
import static com.dr7.salesmanmanager.MainActivity.languagelocalApp;
import static com.dr7.salesmanmanager.Reports.CashReport.date;

import  com.dr7.salesmanmanager.Activities;
public class SalesInvoice extends Fragment {
    RecyclerView recyclerView;

//    public static  List<Item> jsonItemsList;
//    public static List<Item> jsonItemsList2;
//    public static List<Item> jsonItemsList_intermidiate;
    public  static  int size_customerpriceslist=0;
    private static String smokeGA = "دخان";
    private static String smokeGE = "SMOKE";
    public static List<Item> itemForPrintLast;

    Bitmap testB;
    byte[] printIm;
    PrintPic printPic;
    private static int salesMan;
    static int index;
    public static List<Payment> payment_unposted;
    public static List<Voucher> sales_voucher_unposted;
    public List<QtyOffers> list_discount_offers;
    public List<ItemsQtyOffer> itemsQtyOfferList;
    double max_cridit, available_balance, account_balance, cash_cridit, unposted_sales_vou, unposted_payment, unposted_voucher;
    public ListView itemsListView;
    public static List<Item> items;
    public ItemsListAdapter itemsListAdapter;
    private ImageView  custInfoImgButton, SaveData;
    private CircleImageView addItemImgButton2,refreshData,rePrintimage;
    private ImageView connect, pic;
    private RadioGroup paymentTermRadioGroup, voucherTypeRadioGroup;
    private RadioButton cash, credit, retSalesRadioButton, salesRadioButton, orderRadioButton;
    private EditText remarkEditText;
    private ImageButton newImgBtn;
    private double subTotal, totalTaxValue, netTotal;
    public double totalDiscount=0,discount_oofers_total_cash=0, discount_oofers_total_credit=0,sum_discount=0,disc_items_value=0,disc_items_total=0;
    private TextView taxTextView, subTotalTextView, netTotalTextView;
    public static  TextView totalQty_textView;
    public TextView discTextView;
    public ImageButton discountButton;
    private DecimalFormat decimalFormat;
    public static TextView voucherNumberTextView, Customer_nameSales;
     static ArrayList<Item> itemForPrint;

    private static DatabaseHandler mDbHandler;
    public static int voucherType = 504;
    private int voucherNumber;
  public  int payMethod;
    boolean isFinishPrint=false;
    double total_Qty=0.0;
    double totalQty_forPrint=0;
    Voucher voucherShow;

    static String rowToBeUpdated[] = {"", "", "", "", "", "", "", ""};

    boolean clicked = false;
    String itemsString = "";
    String itemsString2 = "";

    static Voucher voucher;
    static  Voucher voucherForPrint;
   static List<Item> itemsList;

    bluetoothprinter object;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    List<ItemsQtyOffer> offers_ItemsQtyOffer;
    ProgressDialog dialog_progress;

    boolean vocherClick=true;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    double discountValue;
    double discountPerc;
    volatile boolean stopWorker;
    DecimalFormat threeDForm ;
    double maxDiscounr_value;
//    static Voucher voucherSale;
//    static List<Item> itemSale;
   /* public static void test2(){

        Customer_nameSales.setText(CustomerListFragment.Customer_Name.toString());
    }*/

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

    SalesInvoiceInterface salesInvoiceInterfaceListener;
    Date currentTimeAndDate;
    SimpleDateFormat df,df2;
    String voucherDate,voucherYear;
    CompanyInfo companyInfo;
    double limit_offer=0;
    ImageButton maxDiscount;
    int size_firstlist=0;
    int voucherNo=0;
    CheckBox check_HidePrice;
     public  static  int valueCheckHidPrice=0;
     LinearLayout mainlayout;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sales_invoice, container, false);
        mainlayout=(LinearLayout)view.findViewById(R.id.mainlyout);
        Log.e("locallang",""+languagelocalApp);
        if(languagelocalApp.equals("ar"))
        {
            mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else{
            if(languagelocalApp.equals("en"))
            {
                mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }

        }
         currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
         voucherDate = df.format(currentTimeAndDate);
        voucherDate = convertToEnglish(voucherDate);
        df2 = new SimpleDateFormat("yyyy");
       voucherYear = df2.format(currentTimeAndDate);
        voucherYear = convertToEnglish(voucherYear);
        decimalFormat = new DecimalFormat("00.000");
        mDbHandler = new DatabaseHandler(getActivity());
//        jsonItemsList = new ArrayList<>();
//        jsonItemsList2 = new ArrayList<>();
//        jsonItemsList_intermidiate = new ArrayList<>();
        list_discount_offers=new ArrayList<>();
        itemsQtyOfferList=new ArrayList<>();
        object = new bluetoothprinter();
        itemForPrint=new ArrayList<>();
        threeDForm = new DecimalFormat("00.000");
        valueCheckHidPrice=CustomerListShow.CustHideValu;
       Log.e("valueCheckHidPrice",""+valueCheckHidPrice);

        addItemImgButton2 = (CircleImageView) view.findViewById(R.id.addItemImgButton2);
        rePrintimage= (CircleImageView) view.findViewById(R.id.pic_Re_print);
        rePrintimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                     voucherNo = mDbHandler.getLastVoucherNo(voucherType);
                     if(voucherNo!= 0 && voucherNo!=-1)
                     {  voucherForPrint= mDbHandler.getAllVouchers_VoucherNo(voucherNo);
                         Log.e("no", "" +voucherForPrint.getCustName()+"\t voucherType"+voucherType);
                         printLastVoucher(voucherNo, voucherForPrint);}
                     else{
                         Toast.makeText(getActivity(), "there is no voucher for this customer and this type of voucher ", Toast.LENGTH_SHORT).show();

                     }

                }
                catch (Exception e)
                {Log.e("ExceptionReprint", "" +e.getMessage());
                    voucherNo=0;
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
        connect = (ImageView) view.findViewById(R.id.balanceImgBtn);
        voucherNumberTextView = (TextView) view.findViewById(R.id.voucherNumber);
        Customer_nameSales = (TextView) view.findViewById(R.id.invoiceCustomerName);
        paymentTermRadioGroup = (RadioGroup) view.findViewById(R.id.paymentTermRadioGroup);
        voucherTypeRadioGroup = (RadioGroup) view.findViewById(R.id.transKindRadioGroup);
        cash = (RadioButton) view.findViewById(R.id.cashRadioButton);
        credit = (RadioButton) view.findViewById(R.id.creditRadioButton);
        retSalesRadioButton = (RadioButton) view.findViewById(R.id.retSalesRadioButton);

        salesRadioButton = (RadioButton) view.findViewById(R.id.salesRadioButton);
        salesRadioButton.setBackgroundColor(getResources().getColor(R.color.cancel_button));
        salesRadioButton.setChecked(true);

        orderRadioButton = (RadioButton) view.findViewById(R.id.orderRadioButton);
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
        maxDiscount=(ImageButton) view.findViewById(R.id.max_disc);
        maxDiscount.setVisibility(View.GONE);
        maxDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaxDiscountDialog();
            }
        });

        itemsList = new ArrayList<>();
//        voucherType = 504;
        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        String vn2 = voucherNumber + "";
        voucherNumberTextView.setText(vn2);
        connect.setVisibility(View.INVISIBLE);
        companyInfo=new CompanyInfo();
        offers_ItemsQtyOffer = mDbHandler.getItemsQtyOffer();
        limit_offer=mDbHandler.getMinOfferQty(total_items_quantity);
        refrechItemForReprint();

        //*****************************fill list items json*******************************************
//        fillListItemJson();

        //*************************************************************************

        salesRadioButton.setOnClickListener(RADIOCLECKED);
        retSalesRadioButton.setOnClickListener(RADIOCLECKED);
        orderRadioButton.setOnClickListener(RADIOCLECKED);


        if (MainActivity.checknum == 1)
            Customer_nameSales.setText(CustomerListShow.Customer_Name.toString());
        else
            Customer_nameSales.setText("Customer Name");

//        if (CustomerListShow.CashCredit == 0) {
//            credit.setChecked(true);
//            cash.setChecked(false);
//               payMethod = 0;
//
//        } else {
//            cash.setChecked(true);
//            credit.setChecked(false);
//              payMethod = 1;
//
//
//        }
        if (mDbHandler.getAllSettings().get(0).getPaymethodCheck() == 0) {
            credit.setChecked(true);
            cash.setChecked(false);
            payMethod = 0;

        } else {
            cash.setChecked(true);
            credit.setChecked(false);
            payMethod = 1;


        }
        voucherTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                paymentTermRadioGroup.setVisibility(View.VISIBLE);

                    if (itemsListView.getCount() > 0) {
                        if(vocherClick) {

                        new android.support.v7.app.AlertDialog.Builder(getActivity())
                                .setTitle("Confirm Update")
                                .setCancelable(false)
                                .setMessage("Are you sure you want clear the list !")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        clearItemsList();
                                        clearLayoutData();

                                        switch (checkedId) {
                                            case R.id.salesRadioButton:
                                                voucherType = 504;
                                                voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                String vn1 = voucherNumber + "";
                                                voucherNumberTextView.setText(vn1);
                                                salesRadioButton.setBackgroundColor(getResources().getColor(R.color.cancel_button));
                                                retSalesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                orderRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                break;
                                            case R.id.retSalesRadioButton:
                                                voucherType = 506;
                                                voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                String vn2 = voucherNumber + "";
                                                voucherNumberTextView.setText(vn2);
                                                retSalesRadioButton.setBackgroundColor(getResources().getColor(R.color.cancel_button));
                                                salesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                orderRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                break;
                                            case R.id.orderRadioButton:
                                                voucherType = 508;
                                                voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                String vn3 = voucherNumber + "";
                                                voucherNumberTextView.setText(vn3);
                                                paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                                                orderRadioButton.setBackgroundColor(getResources().getColor(R.color.cancel_button));
                                                retSalesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                salesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                break;

                                        }
                                    }
                                })
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
                    } else {
                        if(vocherClick) {
                            new android.support.v7.app.AlertDialog.Builder(getActivity())
                                    .setTitle("Confirm Update")
                                    .setCancelable(false)
                                    .setMessage("Are you sure you want change  voucher type !")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            switch (checkedId) {
                                                case R.id.salesRadioButton:
                                                    voucherType = 504;
                                                    voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                    String vn1 = voucherNumber + "";
                                                    voucherNumberTextView.setText(vn1);
                                                    salesRadioButton.setBackgroundColor(getResources().getColor(R.color.cancel_button));
                                                    retSalesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                    orderRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));

                                                    break;
                                                case R.id.retSalesRadioButton:
                                                    voucherType = 506;
                                                    voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                    String vn2 = voucherNumber + "";
                                                    voucherNumberTextView.setText(vn2);
                                                    retSalesRadioButton.setBackgroundColor(getResources().getColor(R.color.cancel_button));
                                                    salesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                    orderRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                    break;
                                                case R.id.orderRadioButton:
                                                    voucherType = 508;
                                                    voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                                                    String vn3 = voucherNumber + "";
                                                    voucherNumberTextView.setText(vn3);
                                                    paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                                                    orderRadioButton.setBackgroundColor(getResources().getColor(R.color.cancel_button));
                                                    retSalesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
                                                    salesRadioButton.setBackgroundColor(getResources().getColor(R.color.layer1));
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

        paymentTermRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.creditRadioButton:
                        payMethod = 0;

                        if (mDbHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1)
                        {
                            discTextView.setText("0.0");
                            netTotalTextView.setText("0.0");
                            netTotal = 0.0;
                            totalDiscount=0;
                            sum_discount=0;
                            clearLayoutData();
                        }
                        calculateTotals();

                        break;
                    case R.id.cashRadioButton:
                        payMethod = 1;

                        if (mDbHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1)
                        {
                            discTextView.setText("0.0");
                            netTotalTextView.setText("0.0");
                            netTotal = 0.0;
                            totalDiscount=0;
                            sum_discount=0;
                            clearLayoutData();
                        }
                        calculateTotals();
                        break;
                }
            }
        });

        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
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

        addItemImgButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                salesInvoiceInterfaceListener.displayFindItemFragment2();//for test
                new SalesInvoice.Task().execute();
            }
        });





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

        itemsListAdapter = new ItemsListAdapter(getActivity(), items);
        itemsListView.setAdapter(itemsListAdapter);
//        totalQty_textView.setText(items.size()+"");


        itemsListView.setOnItemLongClickListener(onItemLongClickListener);

        newImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearLayoutData();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.findBT();
            }
        });

        SaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    closeBT();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                itemForPrint.clear();
                clicked = false;
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_save));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int l) {

                        if (!clicked) {

                            clicked = true;
                            int listSize = itemsListView.getCount();
                            if (listSize == 0)
                                Toast.makeText(getActivity(), "Fill Your List Please", Toast.LENGTH_LONG).show();
                            else {
                                DiscountFragment obj = new DiscountFragment();
                                double discountValue = obj.getDiscountValue();
                                double discountPerc = obj.getDiscountPerc();

                                double totalDisc = Double.parseDouble(discTextView.getText().toString());
                                double subTotal = Double.parseDouble(subTotalTextView.getText().toString());
                                double tax=0, netSales=0;
                                String netsale_txt="";
                                netsale_txt=netTotalTextView.getText().toString();
                                Log.e("textNt",""+netsale_txt);

                                try{
                                     tax = Double.parseDouble(taxTextView.getText().toString());
                                     netSales = Double.parseDouble(netTotalTextView.getText().toString());
                                     Log.e("netSales_isnan",""+Double.isNaN(netSales));

                                }catch (Exception e){
                                    tax=0;
                                    Log.e("tax error E",""+tax+"   "+taxTextView.getText().toString());

                                }
                                if(netSales!=0 && !Double.isNaN(netSales) ){// test nan

                                    Log.e("not zero ","tax="+tax+"\t"+ netSales);
                                    //******************************




                                if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1 && (discountValue / netSales) > mDbHandler.getAllSettings().get(0).getAmountOfMaxDiscount()) {
                                    Toast.makeText(getActivity(), "You have exceeded the upper limit of the discount", Toast.LENGTH_SHORT).show();

                                } else {

                                    String remark = " " + remarkEditText.getText().toString();
                                    salesMan = Integer.parseInt(Login.salesMan);

                                    voucher = new Voucher(0, voucherNumber, voucherType, voucherDate,
                                            salesMan, discountValue, discountPerc, remark, payMethod,
                                            0, totalDisc, subTotal, tax, netSales, CustomerListShow.Customer_Name,
                                            CustomerListShow.Customer_Account, Integer.parseInt(voucherYear));
                                        if (mDbHandler.getAllSettings().get(0).getCustomer_authorized() == 1) {

                                            if (customer_is_authrized()) {


                                                if(virefyMaxDescount()){
                                                    if(!remarkEditText.getText().toString().equals("")) {
                                                        AddVoucher();
                                                        clearLayoutData();
                                                    }else{
                                                        Toast.makeText(getActivity(), "Please Add Remark Filed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                      else{
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

                                            } else {
                                                reCheck_customerAuthorize();// test
                                            }
                                        } else {// you should not authorize customer account balance


                                            if(virefyMaxDescount()){
//                                                if (!remarkEditText.getText().toString().equals("")){
                                                AddVoucher();
                                                clearLayoutData();
//                                            }else{
//                                                Toast.makeText(getActivity(), "Please Add Remark Filed", Toast.LENGTH_SHORT).show();
//                                            }
                                            }

                                            else{
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
                                }
                                else{// if tax ==0 or net sales==0 don't save data
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

//                                clearLayoutData();
                            }
                            //not empty list
                        }
                    }//end ok save

                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().

                        show();
            }//end save data
        });
        return view;
    }

    private void refrechItemForReprint() {
        itemForPrintLast = new ArrayList<Item>();
        itemForPrintLast = mDbHandler.getAllItems();//test
        Log.e("itemForPrintLast",""+itemForPrintLast.size());

    }

    View.OnClickListener RADIOCLECKED   =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            vocherClick=true;
        }
    } ;


    private void refreshRadiogroup(int voucherType) {
        if(vocherClick) {
            switch (voucherType) {
                case 504:
                    vocherClick = false;
                    voucherTypeRadioGroup.check(R.id.salesRadioButton);

//                                            salesRadioButton.setSelected(true);
//                                            retSalesRadioButton.setSelected(false);
//                                            orderRadioButton.setSelected(false);
                    break;
                case 506:
                    vocherClick = false;
                    voucherTypeRadioGroup.check(R.id.retSalesRadioButton);

//                                            salesRadioButton.setSelected(false);
//                                            retSalesRadioButton.setSelected(true);
//                                            orderRadioButton.setSelected(false);
                    break;
                case 508:
                    vocherClick = false;
                    voucherTypeRadioGroup.check(R.id.orderRadioButton);

//                                            salesRadioButton.setSelected(false);
//                                            retSalesRadioButton.setSelected(false);
//                                            orderRadioButton.setSelected(true);
                    paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    static Voucher vouchLast;
    private void printLastVoucher(int voucher_no, Voucher vouchPrint) {
        if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {

            try {
                int printer = mDbHandler.getPrinterSetting();
                companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
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
                            convertLayoutToImage(vouchPrint);

                            Intent O = new Intent(getActivity().getBaseContext(), bMITP.class);
                            O.putExtra("printKey", "7");
                            startActivity(O);






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
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }


            salesInvoiceInterfaceListener.displayFindItemFragment2();
            return "items";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

//            pb.setProgress(values[0]);
//            dialog_progress.setProgress(values);
        }

        @Override
        protected void onPreExecute() {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            super.onPreExecute();
            dialog_progress = new ProgressDialog(getActivity());
            dialog_progress.setCancelable(false);
            dialog_progress.setMessage(getResources().getString(R.string.loadingItem));
            dialog_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dialog_progress.show();
        }
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            dialog_progress.dismiss();

            if (result != null) {

            } else {
                Toast.makeText(getActivity(), "Not able to fetch data ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMaxDiscountDialog() {
        double discount_voucher=0;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.max_discount_info_dialog);

        TextView name= (TextView) dialog.findViewById(R.id.textView_customerName);
        TextView total= (TextView) dialog.findViewById(R.id.textView_total);
        TextView maxDisc= (TextView) dialog.findViewById(R.id.textview_maxDiscount);
        name.setText(CustomerListShow.Customer_Name);
        total.setText(convertToEnglish(decimalFormat.format(subTotal)+""));
        maxDiscounr_value=mDbHandler.getMaxDiscValue_ForCustomer(CustomerListShow.Customer_Account);
        discount_voucher=((maxDiscounr_value*subTotal)/100);
        maxDisc.setText(convertToEnglish(decimalFormat.format(discount_voucher)+""));
        Log.e("max",""+maxDiscounr_value+"\t"+ discount_voucher);

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


    public  void reCheck_customerAuthorize()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.not_authoriz));
        builder.setTitle(getResources().getString(R.string.warning_message));
        builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                if (customer_is_authrized()) {
//
//                    AddVoucher();
//                    clearLayoutData();
//                }
                dialogInterface.dismiss();
            }


        });
        builder.create().show();

    }

   public void  AddVoucher(){
          mDbHandler.addVoucher(voucher);
          for (int i = 0; i < items.size(); i++) {

              Item item = new Item(0, voucherYear, voucherNumber, voucherType, items.get(i).getUnit(),
                      items.get(i).getItemNo(), items.get(i).getItemName(), items.get(i).getQty(), items.get(i).getPrice(),
                      items.get(i).getDisc(), items.get(i).getDiscPerc(), items.get(i).getBonus(), items.get(i).getVoucherDiscount(),// was 0 in credit
                      items.get(i).getTaxValue(), items.get(i).getTaxPercent(), 0);
              totalQty_forPrint += items.get(i).getQty();
              itemsList.add(item);

              mDbHandler.addItem(item);
              itemForPrint.add(item);

              if (voucherType == 504)
                  mDbHandler.updateSalesManItemsBalance1(items.get(i).getQty(), salesMan, items.get(i).getItemNo());
              else    if (voucherType ==506){
                  mDbHandler.updateSalesManItemsBalance2(items.get(i).getQty(), salesMan, items.get(i).getItemNo());

              }

          }


//           if (mDbHandler.getAllSettings().get(0).getWorkOnline() == 1) {
//               new JSONTask().execute();
//           }

          if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {
              Log.e("test", "" + voucher.getTotalVoucherDiscount());

              try {
                  int printer = mDbHandler.getPrinterSetting();
                  companyInfo = mDbHandler.getAllCompanyInfo().get(0);
                  if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
                      switch (printer) {
                          case 0:
                              Intent i = new Intent(getActivity().getBaseContext(), BluetoothConnectMenu.class);
                              i.putExtra("printKey", "1");
                              startActivity(i);
//                                                             lk30.setChecked(true);
                              break;
                          case 1:

                              try {
                                  findBT();
                                  openBT(1);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
//                                                             lk31.setChecked(true);
                              break;
                          case 2:

//                               try {
//                                   findBT();
//                                   openBT(2);
//                               } catch (IOException e) {
//                                   e.printStackTrace();
//                               }
//                                                             lk32.setChecked(true);
                              voucherShow = voucher;

                              convertLayoutToImagew(getActivity());
                              Intent O1 = new Intent(getActivity().getBaseContext(), bMITP.class);
                              O1.putExtra("printKey", "1");
                              startActivity(O1);


                              break;
                          case 3:

                              try {
                                  findBT();
                                  openBT(3);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
//                                                             qs.setChecked(true);
                              break;
                          case 4:
                              printTally(voucher);
                              break;


                          case 5:

//                                                             MTP.setChecked(true);
                              voucherShow = voucher;
                              convertLayoutToImage(voucher);
                              Intent O = new Intent(getActivity().getBaseContext(), bMITP.class);
                              O.putExtra("printKey", "1");
                              startActivity(O);


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
          mDbHandler.setMaxSerialNumber(voucherType, voucherNumber);

       }

    private boolean virefyMaxDescount() {
        double discount_voucher=0;
        double discount_total_voucher=0;
        maxDiscounr_value=mDbHandler.getMaxDiscValue_ForCustomer(CustomerListShow.Customer_Account);
        discount_voucher=((maxDiscounr_value*subTotal)/100);
        if(discount_voucher==0)// الحد الاعلى لخصم الزبون =0 يسمح ببيعه
        {
            return true;
        }
        String disc=discTextView.getText().toString();
        if(disc!="")
        {
            discount_total_voucher=Double.parseDouble(disc);
            Log.e("discount_voucher",""+discount_total_voucher);

        }
        else {
            discount_total_voucher=0;
        }

        if(discount_total_voucher<=discount_voucher)
        {
            return  true;
        }


        return  false;

    }


    public void setListener(SalesInvoiceInterface listener) {
        this.salesInvoiceInterfaceListener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean customer_is_authrized() {
        if(cash.isChecked())
            return  true;
        unposted_payment = 0;
        double unposted_sales_cash=0,unposted_sales_credit=0;

        List<Customer> customer_balance = mDbHandler.getCustomer_byNo(CustomerListShow.Customer_Account);
        for (int i = 0; i < customer_balance.size(); i++) {
            cash_cridit = Double.parseDouble(customer_balance.get(i).getCashCredit() + "");
            max_cridit = Double.parseDouble(customer_balance.get(i).getCreditLimit() + "");
            Log.e("cas= ", "" + cash_cridit + "\t creditlimit = " + max_cridit);
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
        sales_voucher_unposted=mDbHandler.getAllVouchers_CustomerNo(voucher.getCustNumber());
        for (int j=0;j<sales_voucher_unposted.size();j++)
        {
            if(sales_voucher_unposted.get(j).getIsPosted()==0 )
            {
                if( sales_voucher_unposted.get(j).getPayMethod()==0)// for credit
                unposted_sales_credit+=sales_voucher_unposted.get(j).getNetSales();
                else
                {
                    unposted_sales_cash+=sales_voucher_unposted.get(j).getNetSales();
                }


            }
        }
        available_balance = max_cridit - cash_cridit-unposted_sales_credit  + unposted_payment;
        if (available_balance >= voucher.getNetSales())
            return true;
        else
            return false;

    }
    double updaQty=0,currentDisc=0;
    float disount_totalnew=0;
    Offers appliedOffer = null;

    public OnItemLongClickListener onItemLongClickListener =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.app_select_option));
                    builder.setCancelable(true);
                    builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                    builder.setItems(R.array.list_items_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    String st="";
                                    total_items_quantity-=items.get(position).getQty();
                                    totalQty_textView.setText("+"+total_items_quantity);
                                    items.remove(position);

                                    itemsListView.setAdapter(itemsListAdapter);
                                    calculateTotals();

//                                    clearLayoutData();

                                    break;
                                case 1:
//                                    salesInvoiceInterfaceListener.displayUpdateItems();
//                                    rowToBeUpdated[0] = items.get(position).getItemNo();
//                                    rowToBeUpdated[1] = items.get(position).getItemName();
//                                    rowToBeUpdated[2] = items.get(position).getQty() + "";
//                                    rowToBeUpdated[3] = items.get(position).getPrice() + "";
//                                    rowToBeUpdated[4] = items.get(position).getBonus() + "";
//                                    rowToBeUpdated[5] = items.get(position).getDiscPerc().replaceAll("[%:,]","");
//                                    rowToBeUpdated[6] = items.get(position).getDiscType() + "";
//                                    rowToBeUpdated[7] = items.get(position).getUnit() + "";




                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setContentView(R.layout.update_qty_dialog);

                                    final EditText qty = (EditText) dialog.findViewById(R.id.editText1);
                                    Button okButton = (Button) dialog.findViewById(R.id.button1);
                                    Button cancelButton = (Button) dialog.findViewById(R.id.button2);

                                    okButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            float availableQty = 0;
                                            List<Item> jsonItemsList_insal =jsonItemsList;
                                            Log.e("jsonItemsList",""+jsonItemsList.size());
                                            for (int i = 0; i < jsonItemsList.size(); i++) {
                                                if (items.get(position).getItemNo().equals(jsonItemsList.get(i).getItemNo())) {
                                                    availableQty = jsonItemsList.get(i).getQty();
                                                    Log.e("availableQty",""+availableQty);

                                                    break;
                                                }
                                            }
                                            if (mDbHandler.getAllSettings().get(0).getAllowMinus() == 1 ||
                                                    availableQty >= Float.parseFloat(qty.getText().toString()) ||
                                                    voucherType == 506) {
                                                total_items_quantity-=items.get(position).getQty();
                                                Log.e("total_itemsbefore",""+total_items_quantity);
                                                items.get(position).setQty(Float.parseFloat(qty.getText().toString()));
                                                updaQty=Double.parseDouble(qty.getText().toString());
//                                                currentDisc=items.get(position).getDisc();
//                                                if(items.get(position).getDisc()!=0) {
                                                List<Offers> offer = checkOffers(items.get(position).getItemNo());
                                                if(offer.size()>0)
                                                {
                                                    appliedOffer = getAppliedOffer(items.get(position).getItemNo(), updaQty + "", 1);
                                                    if (appliedOffer != null) {

                                                        disount_totalnew = Float.parseFloat((((int) (updaQty / appliedOffer.getItemQty())) * appliedOffer.getBonusQty()) + "");
                                                        items.get(position).setDisc(disount_totalnew);


                                                    }
                                                }

                                                total_items_quantity+=items.get(position).getQty();
                                                Log.e("total_itemsafter",""+total_items_quantity);
                                                totalQty_textView.setText("+"+total_items_quantity);
                                                if (items.get(position).getDiscType() == 0)
                                                    items.get(position).setAmount(items.get(position).getQty() * items.get(position).getPrice() - items.get(position).getDisc());
                                                else
                                                    items.get(position).setAmount(items.get(position).getQty() * items.get(position).getPrice() - Float.parseFloat(items.get(position).getDiscPerc().replaceAll("[%:,]", "")));
                                                calculateTotals();
                                                itemsListView.setAdapter(itemsListAdapter);

                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                              Log.e("qty",  qty.getText().toString());
                                                dialog.dismiss();
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

                                    break;
                                case 2:
                                    clearItemsList();
                                    clearLayoutData();
                                    total_items_quantity=0;
                                    totalQty_textView.setText("+"+total_items_quantity);
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                    return true;
                }
            };

    public String[] getIndexToBeUpdated() {
        return rowToBeUpdated;
    }

    public int getIndex() {
        return index;
    }
    private Offers getAppliedOffer(String itemNo, String qty, int flag) {

        double qtyy = Double.parseDouble(qty);
        List<Offers> offer = checkOffers(itemNo);

        List<Double> itemQtys = new ArrayList<>();
        for (int i = 0; i < offer.size(); i++) {
            itemQtys.add(offer.get(i).getItemQty());
        }
        Collections.sort(itemQtys);

        double iq = itemQtys.get(0);
        for (int i = 0; i < itemQtys.size(); i++) {
            if (qtyy >= itemQtys.get(i))
                iq = itemQtys.get(i);
        }

        for (int i = 0; i < offer.size(); i++) {
            if (iq == offer.get(i).getItemQty())
                return offer.get(i);
        }

        return null;
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
                Log.e("log2 " , date + "  " + offers.get(i).getFromDate() + " " + offers.get(i).getToDate());
                if (itemNo.equals(offers.get(i).getItemNo()) &&
                        (formatDate(date).after(formatDate(offers.get(i).getFromDate())) || formatDate(date).equals(formatDate(offers.get(i).getFromDate()))) &&
                        (formatDate(date).before(formatDate(offers.get(i).getToDate())) || formatDate(date).equals(offers.get(i).getToDate()))) {

                    offer = offers.get(i);
                    Offers.add(offer);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Offers;
    }


    private void clearItemsList() {
        items.clear();
        itemsListAdapter.setItemsList(items);
        itemsListAdapter.notifyDataSetChanged();
    }

    private void clearLayoutData() {
//        paymentTermRadioGroup.check(R.id.creditRadioButton);
        remarkEditText.setText("");
        clearItemsList();
//        calculateTotals();
        subTotalTextView.setText("0.000");
        taxTextView.setText("0.000");
        netTotalTextView.setText("");
        netTotalTextView.setText("0.000");
        discTextView.setText("0.000");
        subTotal =0.0;
        totalTaxValue =0.0;
        netTotal = 0.0;
        totalDiscount = 0.0;
        sum_discount=0.0;
        items.clear();
        itemsList.clear();
//        calculateTotals();
        voucherType = 504;
        salesRadioButton.setChecked(true);
        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
        total_items_quantity=0;
        totalQty_textView.setText("+0");
        discvalue_static=0;
        refrechItemForReprint();

    }

    public void calculateTotals()
    {
        Log.e("TOTAL",""+total_items_quantity);
//        discTextView.setText("0.0");
        netTotalTextView.setText("0.0");
//        calculateTotals_cridit();
        double itemTax, itemTotal, itemTotalAfterTax,
                itemTotalPerc, itemDiscVal, posPrice, totalQty = 0;
        //**********************************************************************
        list_discount_offers = mDbHandler.getDiscountOffers();
        itemsQtyOfferList = mDbHandler.getItemsQtyOffer();
        String itemGroup;
        subTotal = 0.0;
        totalTaxValue = 0.0;
        netTotal = 0.0;
        totalDiscount = 0;
//        double disc_dentail=Double.parseDouble(discTextView.getText().toString());
        totalDiscount+=discvalue_static;
        //test discount item with discount total voucher
//        sum_discount +=DiscountFragment.getDiscountPerc();
//        sum_discount +=DiscountFragment.getDiscountValue();
//        Log.e("sum_discount","="+DiscountFragment.getDiscountPerc()+"\t"+ DiscountFragment.getDiscountValue());
        float flagBonus = 0;
        float amountBonus = 0;
        totalQty = 0.0;
        double limit_offer=0;
        //Excluclude tax
        if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0) {
            totalQty=0.0;
            try {

                limit_offer = mDbHandler.getMinOfferQty(total_items_quantity);
            }catch (Exception e){
                limit_offer=0;
            }
            for (int i = 0; i < items.size(); i++) {
                discount_oofers_total_cash = 0;
                discount_oofers_total_credit = 0;
                disc_items_total = 0;
                disc_items_value = 0;

                if (total_items_quantity >= limit_offer && limit_offer != 0 && payMethod == 1) {// all item without bonus item
                    for (int b = 0; b < items.size(); b++) {


                                if (checkOffers_no(items.get(b).getItemNo())) {
//                                    if (items.get(b).getItemNo().equals(itemsQtyOfferList.get(k).getItem_no())&&limit_offer==itemsQtyOfferList.get(k).getItemQty()) {
                                        disc_items_value+=items.get(b).getQty() * mDbHandler.getDiscValue_From_ItemsQtyOffer(items.get(b).getItemNo(),limit_offer);
                                        if (items.get(b).getDisc() != 0) {// delete the discount(table bromotion vs ) from this item
                                            disount_totalnew = 0;
                                            items.get(b).setDisc(disount_totalnew);
                                            itemsListView.setAdapter(itemsListAdapter);

                                        }


//
                                                          }
                    }


                }

                else {// all item without discount item
                    totalQty = 0.0;
                    for (int x = 0; x < items.size(); x++) {
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
                        if (payMethod == 1) {
                            if (list_discount_offers.get(j).getPaymentType() == 1) {
                                if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                    discount_oofers_total_cash = totalQty * list_discount_offers.get(j).getDiscountValue();
//                                discount_oofers_total_cash =( totalQty /list_discount_offers.get(j).getQTY()) * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        } else {
                            if (list_discount_offers.get(j).getPaymentType() == 0) {
                                if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                    discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        }
                    }
                }
                }
//            }
            //**********************************************************************************************************************************************

                disc_items_total += disc_items_value;
                totalDiscount += disc_items_total;
                Log.e("disc_items_total ", " " + disc_items_total);


                if (discount_oofers_total_cash > 0)
                    sum_discount += discount_oofers_total_cash;
                if (discount_oofers_total_credit > 0)
                    sum_discount += discount_oofers_total_credit;


            try {
                totalDiscount+=sum_discount;
                Log.e("totalDiscount",""+totalDiscount);
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }

            for (int i = 0; i < items.size(); i++) {
                itemGroup = items.get(i).getCategory();
                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
                }
                else
                    {
                    itemTax = items.get(i).getAmount() * items.get(i).getTaxPercent() * 0.01;
                    itemTotal = items.get(i).getAmount();
                    }
                itemTotalAfterTax = items.get(i).getAmount() + itemTax;
                subTotal = subTotal + itemTotal;
            }
            for (int i = 0; i < items.size(); i++) {
                itemTotal = items.get(i).getAmount();
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal = (itemTotalPerc * totalDiscount);
                items.get(i).setTotalDiscVal(itemDiscVal);
                //************************************************************
//                totalQty +=items.get(i).getQty();
////                Log.e("totalQty",""+totalQty);
                itemGroup = items.get(i).getCategory();
                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTotal = itemTotal - itemDiscVal;
                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
                }

                items.get(i).setTaxValue(itemTax);
                totalTaxValue = totalTaxValue + itemTax;
            }

            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue;
//              netTotal = netTotal + subTotal -sum_discount + totalTaxValue;


        }

        else {
            totalQty=0.0;
            try {

                limit_offer = mDbHandler.getMinOfferQty(total_items_quantity);
            }catch (Exception e){
                limit_offer=0;
            }
            Log.e("limit_offer",""+limit_offer);
            for (int i = 0; i < items.size(); i++) {
                discount_oofers_total_cash = 0;
                discount_oofers_total_credit = 0;
                disc_items_total = 0;
                disc_items_value = 0;

                if (total_items_quantity >= limit_offer && limit_offer != 0 && payMethod == 1) {// all item without bonus item
                    for (int b = 0; b < items.size(); b++) {

                        if (checkOffers_no(items.get(b).getItemNo())) {
//                                    if (items.get(b).getItemNo().equals(itemsQtyOfferList.get(k).getItem_no())&&limit_offer==itemsQtyOfferList.get(k).getItemQty()) {
                            disc_items_value+=items.get(b).getQty() * mDbHandler.getDiscValue_From_ItemsQtyOffer(items.get(b).getItemNo(),limit_offer);
                            Log.e("disc_items_value ", " " + disc_items_value);

                            if (items.get(b).getDisc() != 0) {// delete the discount(table bromotion vs ) from this item
                                disount_totalnew = 0;
                                items.get(b).setDisc(disount_totalnew);
                                itemsListView.setAdapter(itemsListAdapter);

                            }



                        }




                    }


                }

                else {// all item without discount item
                    totalQty = 0.0;
                    for (int x = 0; x < items.size(); x++) {
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
                        if (payMethod == 1) {
                            if (list_discount_offers.get(j).getPaymentType() == 1) {
                                if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                    discount_oofers_total_cash = totalQty * list_discount_offers.get(j).getDiscountValue();
//                                discount_oofers_total_cash =( totalQty /list_discount_offers.get(j).getQTY()) * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        } else {
                            if (list_discount_offers.get(j).getPaymentType() == 0) {
                                if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                    discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
                                }
                            }
                        }
                    }
                }
            }

            //**********************************************************************************************************************************************


            // ******************************Items Qty Offer By special qty***********************************
//                try {
//                    limit_offer = itemsQtyOfferList.get(0).getItemQty();
//                } catch (Exception e) {
//                    Log.e("limit_offer", "empty");
//                }
//                if (totalQty >= limit_offer && limit_offer != 0) {//10
//                    for (int b = 0; b < items.size(); b++) {
//                        for (int k = 0; k < itemsQtyOfferList.size(); k++) {
//
//                            {
//                                if (checkOffers_no(items.get(b).getItemNo())) {
//                                    if (items.get(b).getItemNo().equals(itemsQtyOfferList.get(k).getItem_no())) {
////
//
//                                        disc_items_value += items.get(b).getQty() * itemsQtyOfferList.get(k).getDiscount_value();
//                                        if (items.get(b).getDisc() != 0) {
//                                            disount_totalnew = 0;
//                                            items.get(b).setDisc(disount_totalnew);
//                                            itemsListView.setAdapter(itemsListAdapter);
//
//                                        }
//
//
//                                    }
//
//
//                                    Log.e("disc_items_value ", " " + disc_items_value);
//                                }
//
//                            }
//                            Log.e("checkOffers", "false");
//                        }
//                    }
//                }
//
//                else {// total qty less than  special qty offer (10)
//                    for (int j = 0; j < list_discount_offers.size(); j++) {
////                            totalDiscount=0;
//                        if (payMethod == 1) {
//                            if (list_discount_offers.get(j).getPaymentType() == 1) {
//                                if (totalQty >= list_discount_offers.get(j).getQTY()) {
//                                    discount_oofers_total_cash = totalQty * list_discount_offers.get(j).getDiscountValue();
////                                discount_oofers_total_cash =( totalQty /list_discount_offers.get(j).getQTY()) * list_discount_offers.get(j).getDiscountValue();
//                                }
//                            }
//                        } else {
//                            if (list_discount_offers.get(j).getPaymentType() == 0) {
//                                if (totalQty >= list_discount_offers.get(j).getQTY()) {
//                                    discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
//                                }
//                            }
//                        }
//                    }
//
//                }
//

            disc_items_total += disc_items_value;
            totalDiscount += disc_items_total;
            Log.e("disc_items_total ", " " + disc_items_total);


            if (discount_oofers_total_cash > 0)
                sum_discount += discount_oofers_total_cash;
            if (discount_oofers_total_credit > 0)
                sum_discount += discount_oofers_total_credit;


            try {
                totalDiscount+=sum_discount;
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }

            for (int i = 0; i < items.size(); i++) {


                itemGroup = items.get(i).getCategory();


                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
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

            for (int i = 0; i < items.size(); i++) {


                itemGroup = items.get(i).getCategory();

                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTax = items.get(i).getAmount() -
                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
                }

                itemTotal = items.get(i).getAmount() - itemTax;
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal = (itemTotalPerc * totalDiscount);
                items.get(i).setVoucherDiscount( (float)itemDiscVal);
                items.get(i).setTotalDiscVal(itemDiscVal);
                itemTotal = itemTotal - itemDiscVal;

                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
                }


                items.get(i).setTaxValue(itemTax);
                totalTaxValue = totalTaxValue + itemTax;
            }

            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue; // tahani -discount_oofers_total

        }



        subTotalTextView.setText(String.valueOf(decimalFormat.format(subTotal)));
        taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));

        discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(discTextView.getText().toString()))));
        discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(totalDiscount+""))));
        netTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));

        subTotalTextView.setText(convertToEnglish(subTotalTextView.getText().toString()));
        taxTextView.setText(convertToEnglish(taxTextView.getText().toString()));
        netTotalTextView.setText(convertToEnglish(netTotalTextView.getText().toString()));

        discTextView.setText(String.valueOf(convertToEnglish(decimalFormat.format(totalDiscount))+""));
        totalDiscount=0.0;
        sum_discount = 0;


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
            for (int i = 0; i < offers_ItemsQtyOffer.size(); i++) {
                Log.e("log2 " , date + "  " + offers_ItemsQtyOffer.get(i).getFromDate() + " " + offers_ItemsQtyOffer.get(i).getToDate());
                if (itemNo.equals(offers_ItemsQtyOffer.get(i).getItem_no()) &&
                        (formatDate(date).after(formatDate(offers_ItemsQtyOffer.get(i).getFromDate())) || formatDate(date).equals(formatDate(offers_ItemsQtyOffer.get(i).getFromDate()))) &&
                        (formatDate(date).before(formatDate(offers_ItemsQtyOffer.get(i).getToDate())) || formatDate(date).equals(offers_ItemsQtyOffer.get(i).getToDate()))) {

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
        double itemTax, itemTotal=0, itemTotalAfterTax,
                itemTotalPerc, itemDiscVal, posPrice,totalQty=0,total_Amount=0;

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
            testB =convertLayoutToImage(voucher);
            printPic = PrintPic.getInstance();
            printPic.init(testB);
            printIm= printPic.printDraw();
            mmOutputStream.write(printIm);
            isFinishPrint=true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private Bitmap convertLayoutToImage(Voucher voucher) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(getActivity());
        dialogs.setContentView(R.layout.printdialog);
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
                            textView.setText(""+convertToEnglish(threeDForm.format(Double.parseDouble(amount))));
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
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.print);

        final Button okButton = dialog.findViewById(R.id.print1);
        final LinearLayout linearLayout = dialog.findViewById(R.id.linear1);
        TableLayout tabLayout = (TableLayout) dialog.findViewById(R.id.table_);

        TextView companyName = dialog.findViewById(R.id.company);
        TextView phone = dialog.findViewById(R.id.phone);
        TextView taxNo = dialog.findViewById(R.id.tax_no);
        TextView date = dialog.findViewById(R.id.date);
        TextView vouch_no = dialog.findViewById(R.id.voucher_no);
        TextView vouchType = dialog.findViewById(R.id.voucher_type);
        TextView payMethod = dialog.findViewById(R.id.payMethod);
        TextView cust = dialog.findViewById(R.id.cust_);
        TextView remark = dialog.findViewById(R.id.remark_);
        TextView totalNoTax = dialog.findViewById(R.id.total_noTax);
        TextView discount = dialog.findViewById(R.id.discount);
        TextView tax = dialog.findViewById(R.id.tax);
        TextView netSale = dialog.findViewById(R.id.net_sales_);

        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);

        companyName.setText(companyInfo.getCompanyName());
        phone.setText(phone.getText().toString() + companyInfo.getcompanyTel());
        taxNo.setText(taxNo.getText().toString() + companyInfo.getTaxNo());
        date.setText(date.getText().toString() + voucher.getVoucherDate());
        vouch_no.setText(vouch_no.getText().toString() + voucher.getVoucherNumber());
        remark.setText(remark.getText().toString() + voucher.getRemark());
        cust.setText(cust.getText().toString() + voucher.getCustName());
        totalNoTax.setText(totalNoTax.getText().toString() + voucher.getSubTotal());
        discount.setText(discount.getText().toString() +totalDiscount);
        tax.setText(tax.getText().toString() + voucher.getTax());
        netSale.setText(netSale.getText().toString() + voucher.getNetSales());

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
        vouchType.setText(vouchType.getText().toString() + voucherTyp);
        payMethod.setText(payMethod.getText().toString() + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));

        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2.0f);
        lp2.setMargins(2, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {

            final TableRow headerRow = new TableRow(getActivity());

            TextView headerView7 = new TextView(getActivity());
            headerView7.setGravity(Gravity.CENTER);
            headerView7.setText("المجموع");
            headerView7.setLayoutParams(lp2);
            headerView7.setTextSize(12);
            headerRow.addView(headerView7);

            TextView headerView6 = new TextView(getActivity());
            headerView6.setGravity(Gravity.CENTER);
            headerView6.setText("الخصم");
            headerView6.setLayoutParams(lp2);
            headerView6.setTextSize(12);
            headerRow.addView(headerView6);

            TextView headerView5 = new TextView(getActivity());
            headerView5.setGravity(Gravity.CENTER);
            headerView5.setText("المجاني");
            headerView5.setLayoutParams(lp2);
            headerView5.setTextSize(12);
            headerRow.addView(headerView5);

            TextView headerView4 = new TextView(getActivity());
            headerView4.setGravity(Gravity.CENTER);
            headerView4.setText("سعر الوحدة");
            headerView4.setLayoutParams(lp2);
            headerView4.setTextSize(12);
            headerRow.addView(headerView4);

            TextView headerView3 = new TextView(getActivity());
            headerView3.setGravity(Gravity.CENTER);
            headerView3.setText("الوزن");
            headerView3.setLayoutParams(lp2);
            headerView3.setTextSize(12);
            headerRow.addView(headerView3);

            TextView headerView2 = new TextView(getActivity());
            headerView2.setGravity(Gravity.CENTER);
            headerView2.setText("العدد");
            headerView2.setLayoutParams(lp2);
            headerView2.setTextSize(12);
            headerRow.addView(headerView2);

            TextView headerView1 = new TextView(getActivity());
            headerView1.setGravity(Gravity.CENTER);
            headerView1.setText("السلعة");
            headerView1.setLayoutParams(lp3);
            headerView1.setTextSize(12);
            headerRow.addView(headerView1);

            tabLayout.addView(headerRow);
        } else {
            final TableRow headerRow = new TableRow(getActivity());
            TextView headerView1 = new TextView(getActivity());

            TextView headerView6 = new TextView(getActivity());
            headerView6.setGravity(Gravity.CENTER);
            headerView6.setText("المجموع");
            headerView6.setLayoutParams(lp2);
            headerView6.setTextSize(12);
            headerRow.addView(headerView6);

            TextView headerView5 = new TextView(getActivity());
            headerView5.setGravity(Gravity.CENTER);
            headerView5.setText("الخصم");
            headerView5.setLayoutParams(lp2);
            headerView5.setTextSize(12);
            headerRow.addView(headerView5);

            TextView headerView4 = new TextView(getActivity());
            headerView4.setGravity(Gravity.CENTER);
            headerView4.setText("المجاني");
            headerView4.setLayoutParams(lp2);
            headerView4.setTextSize(12);
            headerRow.addView(headerView4);

            TextView headerView3 = new TextView(getActivity());
            headerView3.setGravity(Gravity.CENTER);
            headerView3.setText("سعر الوحدة");
            headerView3.setLayoutParams(lp2);
            headerView3.setTextSize(12);
            headerRow.addView(headerView3);

            TextView headerView2 = new TextView(getActivity());
            headerView2.setGravity(Gravity.CENTER);
            headerView2.setText("العدد");
            headerView2.setLayoutParams(lp2);
            headerView2.setTextSize(12);
            headerRow.addView(headerView2);

            headerView1.setGravity(Gravity.CENTER);
            headerView1.setText("السلعة");
            headerView1.setLayoutParams(lp3);
            headerView1.setTextSize(12);
            headerRow.addView(headerView1);

            tabLayout.addView(headerRow);
        }

        for (int j = 0; j < itemsList.size(); j++) {
            final TableRow row = new TableRow(getActivity());

            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {

                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(10);

                    switch (i) {
                        case 6:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;

                        case 5:
                            textView.setText(itemsList.get(j).getUnit());
                            textView.setLayoutParams(lp2);
                            break;

                        case 4:
                            textView.setText("" + itemsList.get(j).getQty());
                            textView.setLayoutParams(lp2);
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;

                        case 2:
                            textView.setText("" + itemsList.get(j).getBonus());
                            textView.setLayoutParams(lp2);
                            break;

                        case 1:
                            textView.setText("" + itemsList.get(j).getDisc());
                            textView.setLayoutParams(lp2);
                            break;

                        case 0:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }

            } else {
                for (int i = 0; i <= 6; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(10);

                    switch (i) {
                        case 5:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;

                        case 4:
                            textView.setText(itemsList.get(j).getUnit());
                            textView.setLayoutParams(lp2);
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;

                        case 2:
                            textView.setText("" + itemsList.get(j).getBonus());
                            textView.setLayoutParams(lp2);
                            break;

                        case 1:
                            textView.setText("" + itemsList.get(j).getDisc());
                            textView.setLayoutParams(lp2);
                            break;

                        case 0:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }
            }
            tabLayout.addView(row);

        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintHelper photoPrinter = new PrintHelper(getActivity());
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                linearLayout.setDrawingCacheEnabled(true);
                Bitmap bitmap = linearLayout.getDrawingCache();
                photoPrinter.printBitmap("invoice.jpg", bitmap);

            }
        });

        dialog.show();

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


            switch (casePrinter){

                case 1:
                    sendData();
                    break;
                case 2:
                    Settings settings = mDbHandler.getAllSettings().get(0);
                    for(int i=0;i<settings.getNumOfCopy();i++)
                    {send_dataSewoo(voucher);}

                    break;
                case 3:
                    sendData2();
                    break;



            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void printTally(Voucher voucher) {

        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        List<Item> items1=new ArrayList<>();
        for (int j = 0; j < itemsList.size(); j++) {

            if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                items1.add(itemsList.get(j));

            }
        }

        Log.e("Items1__",""+items1.size()+"    "+(items1.size()<=17));

        if(items1.size()<=17){
            bitmap = convertLayoutToImageTally(voucher,1,0,items1.size(),items1);
            try {
                Settings settings = mDbHandler.getAllSettings().get(0);
                File file = savebitmap(bitmap, settings.getNumOfCopy(),"org");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {

            Settings settings = mDbHandler.getAllSettings().get(0);
            for(int i=0;i<settings.getNumOfCopy();i++) {
                bitmap = convertLayoutToImageTally(voucher, 0,0,17,items1);
                bitmap2 = convertLayoutToImageTally(voucher, 1,17,items1.size(),items1);


                try {

                    File file = savebitmap(bitmap, 1,"fir"+""+i);
                    File file2 = savebitmap(bitmap2, 1,"sec"+""+i);

                    Log.e("save image ", "" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }




    }


    private Bitmap convertLayoutToImageTally(Voucher voucher,int okShow,int start,int end,List<Item>items) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(getActivity());
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(true);
        dialogs.setContentView(R.layout.printdialog_tally);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
        TextView  doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW,noteLast;
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
        noteLast =(TextView) dialogs.findViewById(R.id.notelast);
        TableRow sing=(TableRow) dialogs.findViewById(R.id.sing);
//

        if(okShow==0){
            sumLayout.setVisibility(View.GONE);
            noteLast.setVisibility(View.GONE);
            sing.setVisibility(View.GONE);
        }else{
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

        for (int j = start; j <end; j++) {

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


    public static File savebitmap(Bitmap bmp, int numCope,String next) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = null;
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/VanSaleS/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (int i = 0; i < numCope; i++) {
            String targetPdf = directory_path + "testimageSales" + i +""+next +  ".png";
            f = new File(targetPdf);


//        f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        return f;
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
            CompanyInfo companyInfo=new CompanyInfo();

            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            try {
              companyInfo = mDbHandler.getAllCompanyInfo().get(0);
            }catch (Exception e)
            {
                companyInfo.setCompanyName("Companey Name");
                companyInfo.setTaxNo(0);
                companyInfo.setCompanyTel(00000);
//                companyInfo.setLogo();
                            }

            //&& !companyInfo.getLogo().equals(null)
            if (!companyInfo.getCompanyName().equals("")&& companyInfo.getcompanyTel()!=0&&companyInfo.getTaxNo()!=-1) {
                pic.setImageBitmap(companyInfo.getLogo());
                pic.setDrawingCacheEnabled(true);
                Bitmap bitmap = pic.getDrawingCache();
try {
    PrintPic printPic = PrintPic.getInstance();
    printPic.init(bitmap);
    byte[] bitmapdata = printPic.printDraw();
}catch (Exception e){
    Log.e("pic sales invoice ","**");
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
                    printCustom("الخصم    : " +voucher.getTotalVoucherDiscount()+ "\n", 1, 2);

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
            if (!companyInfo.getCompanyName().equals("")&& companyInfo.getcompanyTel()!=0&& !companyInfo.getLogo().equals(null)&&companyInfo.getTaxNo()!=-1) {

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


            }
            else{
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
            case REQUEST_Camera: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("requestresult" ,"REQUEST_Camera");
                    Intent i=new Intent(getActivity(),ScanActivity.class);
                    startActivity(i);
//                    searchByBarcodeNo(s + "");
                } else {
                    Toast.makeText(getActivity(), "check permission Camera ", Toast.LENGTH_SHORT).show();

                }
                return;
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
