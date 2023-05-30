package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.FragmentManager;


import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.dr7.salesmanmanager.Interface.DaoRequsts;
import com.dr7.salesmanmanager.Modles.Flag_Settings;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.RequstTest;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Reports.Reports;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.DiscountFragment.discountPerc;
import static com.dr7.salesmanmanager.LocationPermissionRequest.openDialog;
import static com.dr7.salesmanmanager.Login.Purchase_Order;
import static com.dr7.salesmanmanager.Login.contextG;
import static com.dr7.salesmanmanager.Login.getTotalBalanceInActivities;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Login.offerTalaat;
import static com.dr7.salesmanmanager.Login.talaatLayoutAndPassowrd;
import static com.dr7.salesmanmanager.Login.typaImport;
import static com.dr7.salesmanmanager.Login.voucherReturn_spreat;
import static com.dr7.salesmanmanager.MainActivity.curentDate;
import static com.dr7.salesmanmanager.MainActivity.curentTime;
import static com.dr7.salesmanmanager.MainActivity.masterControlLoc;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.item_serial;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.price;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialListitems;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialValue;
import static com.dr7.salesmanmanager.SalesInvoice.addNewSerial;
import static com.dr7.salesmanmanager.SalesInvoice.listSerialTotal;
import static com.dr7.salesmanmanager.SalesInvoice.price_serial_edit;
import static com.dr7.salesmanmanager.SalesInvoice.serialValueUpdated;
import static com.dr7.salesmanmanager.SalesInvoice.updatedSerial;
import static com.dr7.salesmanmanager.SalesInvoice.voucherNumberTextView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;
import static com.dr7.salesmanmanager.Serial_Adapter.barcodeValue;

import org.json.JSONException;

//import de.hdodenhof.circleimageview.CircleImageView;
//import maes.tech.intentanim.CustomIntent;
//commit test

public class Activities extends AppCompatActivity implements
        SalesInvoice.SalesInvoiceInterface, AddItemsFragment.AddItemsInterface,
        ReceiptVoucher.ReceiptInterFace, DiscountFragment.DiscountInterface, Update_Items.Update_Items_interface,
        StockRequest.StockInterFace, AddItemsStockFragment.AddItemsInterface, AddItemsFragment2.AddItemsInterface

{

    private ImageView  returnInvImageView, receiptImageView, stockImageView,saleImageView,transaction_imageview;
  //  private CircleImageView saleImageView;
    private CardView saleCardView, receiptCardView, accountBalance, returnCardView,uncollectChechue;

    private int activitySelected;
    public  static  String currentKeyTotalDiscount="",keyCreditLimit="",  currentKey="";

    private LinearLayout salesInvoiceLayout,mainlayout,linearMainActivities,mainLinearHolder,
            linearInvoice,linearPayment,linearStock,linearBalance,linearuncollect,dashLayout,fragmentContainer,accountBalanceMainLin;

    private SalesInvoice salesInvoice;
    private  Transaction_Fragment transaction_fragment;

    private StockRequest stockRequest;
    Transaction transaction;
    private DecimalFormat decimalFormat;
    List<Settings>settingsList;
    private boolean isFragmentBlank;
    String today="";
    boolean canClose;
    ProgressDialog dialog_progress;
    DatabaseHandler databaseHandler;
    static String[] araySerial;
    TextView switchLayout;
    public static TextView totalBalance_text,lastVisit_textView;
    public static String totalBalance_value="0";
    public  static  LocationPermissionRequest locationPermissionRequestAc;
    public  GeneralMethod generalMethod;
    LinearLayout linearReturn;
// LocationPermissionRequest locationPermissionRequest;

    @Override 
    public void displayFindItemFragment() {
        try {
            AddItemsFragment addItemsFragment = new AddItemsFragment();
            addItemsFragment.setCancelable(true);
            addItemsFragment.setListener(this);
            addItemsFragment.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            String x = e.getMessage();
        }
    }

    @Override
    public void displayFindItemFragment2() {
        try {
            AddItemsFragment2 addItemsFragment = new AddItemsFragment2();
            addItemsFragment.setCancelable(true);
            addItemsFragment.setListener(this);
            addItemsFragment.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            String x = e.getMessage();
        }
    }

    @Override
    public void displayFindItemStockFragment() {
        try {
            AddItemsStockFragment addItemsStockFragment = new AddItemsStockFragment();
            addItemsStockFragment.setCancelable(true);
            addItemsStockFragment.setListener(this);
            addItemsStockFragment.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            String x = e.getMessage();
        }
    }

    @Override
    public void displayDiscountFragment() {
        currentKeyTotalDiscount="";
        DiscountFragment discountFragment = new DiscountFragment(Activities.this,currentKeyTotalDiscount);
        discountFragment.invoiceTotal = salesInvoice.getItemsTotal();
        discountFragment.setCancelable(true);
        discountFragment.setListener(this);
        discountFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void displayUpdateItems() {
        try {
            Update_Items updateItemsFragment = new Update_Items();
            updateItemsFragment.setCancelable(true);
            updateItemsFragment.setListener(this);
            updateItemsFragment.show(getSupportFragmentManager(), "");
        } catch (Exception e) {

        }
    }

 public  static   double discvalue_static=0,discType_static=0;
    @Override
    public void addDiscount(double discount, int iDiscType) {

//        salesInvoice.sum_discount+=discount;

//        salesInvoice.sum_discount=0;

        discvalue_static=discount;
        discType_static=iDiscType;
        DiscountFragment.setDiscountPerc(discountPerc);
        Log.e("addDiscount","discount"+discount+"\tdiscType_static="+discType_static);
//        salesInvoice.discTextView.setText(decimalFormat.format(discount));
        salesInvoice.calculateTotals(0);
    }

    Animation animZoomIn ;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        new LocaleAppUtils().changeLayot(Activities.this);
        setContentView(R.layout.dashbord_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(offerTalaat==0)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        locationPermissionRequestAc=new LocationPermissionRequest(Activities.this);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        activitySelected = -1;
        databaseHandler=new DatabaseHandler(Activities.this);
        isFragmentBlank = true;


            //locationPermissionRequest = new LocationPermissionRequest(Activities.this);
//            locationPermissionRequest.timerLocation();

        mainlayout = (LinearLayout)findViewById(R.id.mainlyout);
        totalBalance_text=findViewById(R.id.totalBalance_text);

        lastVisit_textView=findViewById(R.id.lastVisit_textView);
        generalMethod=new GeneralMethod(Activities.this);

        fillLastVisit();
        if(getTotalBalanceInActivities==1)
        {
                    fiiltotalBalance();
        }

//        linearMainActivities= (LinearLayout)findViewById(R.id.linearMainActivities);
//        mainLinearHolder= (LinearLayout)findViewById(R.id.mainLinearHolder);
        linearInvoice= (LinearLayout)findViewById(R.id.linearInvoice);
                linearPayment= (LinearLayout)findViewById(R.id.linearPayment);
//                linearStock= (LinearLayout)findViewById(R.id.linearStock);

        linearBalance   = (LinearLayout)findViewById(R.id.linearBalance);

        linearuncollect = (LinearLayout)findViewById(R.id.linearuncollect);
        dashLayout = (LinearLayout)findViewById(R.id.dashLayout);
        fragmentContainer= (LinearLayout)findViewById(R.id.fragmentContainer);
//        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//           // mainLinearHolder.setOrientation(LinearLayout.HORIZONTAL);
////            linearMainActivities.setOrientation(LinearLayout.VERTICAL);
////            linearStock.setOrientation(LinearLayout.VERTICAL);
//            linearPayment.setOrientation(LinearLayout.VERTICAL);
//            linearInvoice.setOrientation(LinearLayout.VERTICAL);
//            linearBalance.setOrientation(LinearLayout.VERTICAL);
//            linearuncollect.setOrientation(LinearLayout.VERTICAL);
//            //Do some stuff
//        }
//        else {
//           // mainLinearHolder.setOrientation(LinearLayout.VERTICAL);
////            linearMainActivities.setOrientation(LinearLayout.HORIZONTAL);
////            linearStock.setOrientation(LinearLayout.HORIZONTAL);
//            linearPayment.setOrientation(LinearLayout.HORIZONTAL);
//            linearInvoice.setOrientation(LinearLayout.HORIZONTAL);
//            linearBalance.setOrientation(LinearLayout.HORIZONTAL);
//            linearuncollect.setOrientation(LinearLayout.HORIZONTAL);
//        }
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
        saleImageView = (ImageView) findViewById(R.id.saleInvImageView);
//        transaction_imageview= (ImageView) findViewById(R.id.transaction_ImageView);
//        transaction_imageview.setOnClickListener(onClickListener);
        saleCardView = (CardView) findViewById(R.id.saleCardView);
        receiptCardView = (CardView) findViewById(R.id.receiptCardView);
        accountBalance= (CardView) findViewById(R.id.accountBalanceCardView);
        accountBalanceMainLin= findViewById(R.id.accountBalanceMainLin);



        List<Flag_Settings> flag_settingsList;
        flag_settingsList = databaseHandler.getFlagSettings();
      settingsList=new ArrayList<>();
        settingsList= databaseHandler.getAllSettings();

        voucherReturn_spreat = flag_settingsList.get(0).getVoucher_Return();
        Purchase_Order = flag_settingsList.get(0).getPurchaseOrder();

        if(MainActivity.Acountatatment==0)  accountBalanceMainLin.setVisibility(View.GONE);
        uncollectChechue= (CardView) findViewById(R.id.unCollectChequesCardView);
        returnCardView= (CardView) findViewById(R.id.returnCardView);
        linearReturn=findViewById(R.id.linearReturn);
        if(voucherReturn_spreat==0||(Purchase_Order==1))
        {
            linearReturn.setVisibility(View.GONE);

        }
        else {
            if(Purchase_Order==0)
            linearReturn.setVisibility(View.VISIBLE);
            else     linearReturn.setVisibility(View.GONE);
        }
        //  newOrderCardView = (CardView) findViewById(R.id.newOrderCardView);
//        supplimentCardView = (CardView) findViewById(R.id.supplimentCardView);
//        switchLayout=findViewById(R.id.switchLayout);
//        switchLayout.setVisibility(View.GONE);
//        switchLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                linearMainActivities.setVisibility(View.VISIBLE);
////                switchLayout.setVisibility(View.GONE);
//            }
//        });
        receiptImageView = (ImageView) findViewById(R.id.paymentImageView);
//        stockImageView = (ImageView) findViewById(R.id.stockRequestImageView);

        saleImageView.setOnClickListener(onClickListener);
        receiptImageView.setOnClickListener(onClickListener);
     //   stockImageView.setOnClickListener(onClickListener);
        accountBalance.setOnClickListener(onClickListener);
        uncollectChechue.setOnClickListener(onClickListener);
        receiptCardView.setOnClickListener(onClickListener);
        saleCardView.setOnClickListener(onClickListener);
        returnCardView.setOnClickListener(onClickListener);

      //  saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        //receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        // newOrderCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        //accountBalance.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        //uncollectChechue.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));

        //supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));

        decimalFormat = new DecimalFormat("##.000");
        if (!(CustomerListShow.Customer_Name == "No Customer Selected !")) {
            //saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorblue_dark));
           // receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
          ///  supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                                    new Task().execute();
            discvalue_static = 0;
//            displaySaleInvoice();
        }

    }

    private void fiiltotalBalance() {

        today=generalMethod.getCurentTimeDate(1);
//        if(!totalBalance_text.getText().toString().equals("")){
            if(isNetworkAvailable())
            {
                ImportJason importJason =new ImportJason(Activities.this);
                importJason.getCustomerInfo(2,"","");
            }

//        }



    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fillLastVisit() {
        String lastVisit=getLastVaisit();
        lastVisit_textView.setText(lastVisit);
    }
    private String getLastVaisit() {
        String visit="";
        transaction=new Transaction();

        if(!CustomerListShow.Customer_Account.equals("")) {
            try {


            transaction = databaseHandler.getLastVisitInfo(CustomerListShow.Customer_Account, generalMethod.getSalesManLogin().trim());
            if (transaction.getCheckInDate() != null) {
                visit = transaction.getCheckInDate() + "\t\t" + transaction.getCheckInTime();
                Log.e("getLastVaisit", "" + CustomerListShow.Customer_Account + "\t" + Login.salesMan + "\t" + transaction.getCheckInDate());
            } else {
                visit = curentDate + "\t\t" + curentTime;
            }
        }catch (Exception e){}

        }

        return  visit;
    }


    private void displaySaleInvoice() {
        dashLayout.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
        //   if (activitySelected == 0)
        //      return;
//        linearMainActivities.setVisibility(View.GONE);
//        switchLayout.setVisibility(View.VISIBLE);
        activitySelected = 0;
        FragmentManager fragmentManager = getSupportFragmentManager();
        salesInvoice = new SalesInvoice();
        salesInvoice.setListener(this);
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        getFragmentManager().popBackStack();
        //transaction.setCustomAnimations(android.R.anim.slide_in_left, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragmentContainer, salesInvoice);
        transaction.addToBackStack(null);
        transaction.commit();
//        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
//        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        isFragmentBlank = false;
        AddItemsFragment2.total_items_quantity=0;
    }
    class Task extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            discvalue_static=0;
            displaySaleInvoice();
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.onPreExecute();
//            dialog_progress = new ProgressDialog(Activities.this);
//            dialog_progress.setCancelable(false);
//            dialog_progress.setMessage(getResources().getString(R.string.loadingItem));
//            dialog_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog_progress.show();
        }
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

//            dialog_progress.dismiss();

            if (result != null) {

            } else {
                Toast.makeText(Activities.this, "Not able to fetch data ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void displayTransactionFragment() {
        activitySelected = 0;
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction_fragment = new Transaction_Fragment();
//        transaction_fragment.setListener(this);
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//        getFragmentManager().popBackStack();
        //transaction.setCustomAnimations(android.R.anim.slide_in_left, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragmentContainer, transaction_fragment);
        transaction.addToBackStack(null);
        transaction.commit();
//        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
//        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        isFragmentBlank = false;
//        AddItemsFragment2.total_items_quantity=0;
    }

    private void displayReceipt() {
//        linearMainActivities.setVisibility(View.GONE);
//        switchLayout.setVisibility(View.VISIBLE);
        dashLayout.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
        activitySelected = 1;
        FragmentManager fragmentManager = getSupportFragmentManager();
        ReceiptVoucher receiptVoucher = new ReceiptVoucher();
        receiptVoucher.setListener(this);
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        getFragmentManager().popBackStack();
        transaction.addToBackStack(null);
        //transaction.setCustomAnimations(android.R.anim.slide_in_left, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragmentContainer, receiptVoucher);
        transaction.addToBackStack(null);
        transaction.commit();
       // saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        //supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        //receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorblue_dark));
        isFragmentBlank = false;
    }

    private void displayStockRequest() {
//        linearMainActivities.setVisibility(View.GONE);
//        switchLayout.setVisibility(View.VISIBLE);
        activitySelected = 2;
        FragmentManager fragmentManager = getSupportFragmentManager();
        stockRequest = new StockRequest();
        stockRequest.setListener(this);
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        getFragmentManager().popBackStack();
        transaction.addToBackStack(null);
        //transaction.setCustomAnimations(android.R.anim.slide_in_left, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragmentContainer, stockRequest);
        transaction.addToBackStack(null);
        transaction.commit();
       // saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        //receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
      //  supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorblue_dark));
        isFragmentBlank = false;
    }


    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e("onClick",""+view.getId());

            if(databaseHandler.getAllSettings().get(0).getItemUnit()==1||
                    databaseHandler.getAllSettings().get(0).getItems_Unit()==1)
            {
                databaseHandler.deleteListD();

            }
            switch (view.getId()) {
                case R.id.saleCardView:
                   // saleImageView.startAnimation(animZoomIn);
                    if (!(CustomerListShow.Customer_Name == "No Customer Selected !")) {
                        if (activitySelected == 0)
                            return;

                        if (!isFragmentBlank) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Activities.this);
                            builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                            builder.setCancelable(false);
                            builder.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   // saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorblue_dark));
                                    //receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                                  //  supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                                    new Task().execute();

                                    discvalue_static=0;
                                    displaySaleInvoice();
                                }
                            });
                            builder.setNegativeButton(getResources().getString(R.string.app_no), null);
                            builder.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                          //  saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorblue_dark));
                            //receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                           // supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                            new Task().execute();
                            dashLayout.setVisibility(View.GONE);
                            discvalue_static=0;
                            displaySaleInvoice();
                        }//displaySaleInvoice();

                    } else
                        Toast.makeText(Activities.this, "Please Select a Customer", Toast.LENGTH_LONG).show();

                    break;

                case R.id.receiptCardView:
                //   receiptImageView.startAnimation(animZoomIn);
                    if (!(CustomerListShow.Customer_Name == "No Customer Selected !")) {
                        if (activitySelected == 1)
                            return;

                        if (!isFragmentBlank) {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
                            builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                            builder2.setCancelable(false);
                            builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                            builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    clearSerial();
                                    dashLayout.setVisibility(View.GONE);
                                    displayReceipt();
                                }
                            });

                            builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                            builder2.create().show();
                        } else {
                            dashLayout.setVisibility(View.GONE);
                            displayReceipt();
                        }
                        // displayReceipt();

                    } else
                        Toast.makeText(Activities.this, "Please Select a Customer", Toast.LENGTH_LONG).show();
                    break;

//                case R.id.stockRequestImageView:
//                  //  stockImageView.startAnimation(animZoomIn);
//                    if (!isFragmentBlank) {
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
//                        builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
//                        builder2.setCancelable(false);
//                        builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
//                        builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
////                                saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
////                                receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
////                                supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
////                                clearSerial();
//                                displayStockRequest();
////                                new TaskStock().execute();
//                            }
//                        });
//
//                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
//                        builder2.create().show();
//                    } else {
////                        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
////                        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
////                        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
//                        displayStockRequest();
//
//                    }
//                    break;
//                case R.id.transaction_ImageView:
//                    Toast.makeText(Activities.this, "transaction clicked", Toast.LENGTH_SHORT).show();
//                    if (!isFragmentBlank) {
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
//                        builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
//                        builder2.setCancelable(false);
//                        builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
//                        builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
////                                displayTransactionFragment();
//                            }
//                        });
//
//                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
//                        builder2.create().show();
//                    } else {
////                        displayTransactionFragment();
//                    }
//
////                    displayTransactionFragment();
//                    break;
                case  R.id.accountBalanceCardView:
                    if (!isFragmentBlank) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
                        builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                        builder2.setCancelable(false);
                        builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                        builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                if(allDataPosted()||typaImport==0 || Purchase_Order==1)
                                {
                                    finish();
                                    Intent inte=new Intent(Activities.this,AccountStatment.class);
                                    startActivity(inte);
                                }else {
                                    showDialogExportData();
                                }


//                                finish();
//                                Intent inte=new Intent(Activities.this,AccountStatment.class);
//                                startActivity(inte);
                            }
                        });

                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                        builder2.create().show();
                    } else {
                        if(offerTalaat==1){
                            finish();
                            Intent inte=new Intent(Activities.this,AccountStatment.class);
                            startActivity(inte);
                        }else {
                            if(allDataPosted()||typaImport==0|| Purchase_Order==1)
                            {
                                finish();
                                Intent inte=new Intent(Activities.this,AccountStatment.class);
                                startActivity(inte);
                            }else {
                                showDialogExportData();
                            }
                        }



                    }
                    break;
                case  R.id.unCollectChequesCardView:
                    if (!isFragmentBlank) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
                        builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                        builder2.setCancelable(false);
                        builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                        builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                finish();
                                Intent inte=new Intent(Activities.this,UnCollectedData.class);
                                inte.putExtra("type","2");
                                startActivity(inte);
                            }
                        });

                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                        builder2.create().show();
                    } else {
                        finish();
                        Intent inte=new Intent(Activities.this,UnCollectedData.class);
                        inte.putExtra("type","2");
                        startActivity(inte);

                    }
                    break;
                case  R.id.returnCardView :
                    if (!(CustomerListShow.Customer_Name == "No Customer Selected !")) {
                        if (!isFragmentBlank) {




                            AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
                            builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                            builder2.setCancelable(false);
                            builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                            builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    finish();
                                    Intent inte = new Intent(Activities.this, ReturnByVoucherNo.class);
                                    inte.putExtra("type", "2");
                                    startActivity(inte);
//
//                                    try {
//                                        approveAdmin = settingsList.get(0).getReturnVoch_approveAdmin();
//                                        Log.e("approveAdmin==",approveAdmin+"");
//                                    }catch (Exception e){
//                                        Log.e("Exceptionhere22==",e.getMessage());
//                                        approveAdmin=0;
//                                    }
//                                    if(approveAdmin==1)
//                                    {
//                                        Log.e("approveAdmin","approveAdmin===");
//                                        showRequstdailog(CustomerListShow.Customer_Account,CustomerListShow.Customer_Name);
//
//                                    }else
//                                    {
//                                        finish();
//                                        Intent inte = new Intent(Activities.this, ReturnByVoucherNo.class);
//                                        inte.putExtra("type", "2");
//                                        startActivity(inte);
//                                    }



                                    ////

                                }
                            });

                            builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                            builder2.create().show();
                        } else {
                            finish();
                            Intent inte = new Intent(Activities.this, ReturnByVoucherNo.class);
                            inte.putExtra("type", "2");
                            startActivity(inte);

//                            try {
//                                approveAdmin = settingsList.get(0).getReturnVoch_approveAdmin();
//                                Log.e("approveAdmin==", approveAdmin + "");
//                            } catch (Exception e) {
//                                Log.e("Exceptionhere22==", e.getMessage());
//                                approveAdmin = 0;
//                            }
//                            if (approveAdmin == 1) {
//                                Log.e("approveAdmin", "approveAdmin===");
//                                showRequstdailog(CustomerListShow.Customer_Account, CustomerListShow.Customer_Name);
//
//                            } else{
//
//
//                            finish();
//                            Intent inte = new Intent(Activities.this, ReturnByVoucherNo.class);
//                            inte.putExtra("type", "2");
//                            startActivity(inte);
//                        }


                        }
                    }else {
                        Toast.makeText(Activities.this, "Please Select a Customer", Toast.LENGTH_LONG).show();

                    }
                    break;
            }
        }
    };

    private void showDialogExportData() {
        SweetAlertDialog sweetMessage= new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);


        sweetMessage.setTitleText(this.getResources().getString(R.string.please_export_data));
        sweetMessage.setContentText("");
        sweetMessage.setCanceledOnTouchOutside(true);

        sweetMessage.setConfirmButton(this.getResources().getString(R.string.app_exp_data), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                ExportJason objJson = null;
                try {
                    objJson = new ExportJason(Activities.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
//                        objJson.startExportDatabase();
                    objJson.startExport(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sweetMessage.dismissWithAnimation();
            }
        })

                .show();
    }

    private boolean allDataPosted() {
        boolean isPosted = databaseHandler.isAllReceptposted();
        Log.e("isAllReceptposted","1"+isPosted);
        if (!isPosted) {
//            return  true;
            return false;

        }
        else {
            return  true;
        }
    }

    private void clearSerial() {
        try {
            String curentVoucherNo=voucherNumberTextView.getText().toString();
            int curent=Integer.parseInt(curentVoucherNo);
            int lastNo= databaseHandler.getLastVoucherNo(SalesInvoice.voucherType);

            if(!curentVoucherNo.equals(lastNo+"") )
            {
                databaseHandler.updateitemDeletedInSerialTable_Backup("",curentVoucherNo);
//                databaseHandler.updateitemDeletedInSerialTable_Backup(curent);


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("onBackPressed",""+e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
        builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
        builder2.setCancelable(false);
        builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
        builder2.setIcon(android.R.drawable.ic_dialog_alert);
        builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isFragmentBlank = true;
                activitySelected=-1;


              //  locationPermissionRequest.closeLocation();
                try {
                    MainActivity. masterControlLoc.setText("2");
                }
              catch (Exception e){}
                dashLayout.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
                clearSerial();
               // saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                //receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
               // supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));

                back();
//                linearMainActivities.setVisibility(View.VISIBLE);
              //  switchLayout.setVisibility(View.GONE);
//                salesInvoice.total_items_quantity=0


            }
        });

        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
        builder2.create().show();

    }

    @Override
    public void displayCustInfoFragment() {
        try {
            CustomerInfoFragment customerInfoFragment = new CustomerInfoFragment();
            customerInfoFragment.setCancelable(true);
            customerInfoFragment.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            String x = e.getMessage();
        }
    }

    @Override
    public void addItemsToList(List<Item> itemsList) {
        salesInvoice.getItemsList().addAll(itemsList);
        // salesInvoice.itemsListAdapter.ite setItemsList(itemsList);
        salesInvoice.itemsListAdapter.notifyDataSetChanged();
        salesInvoice.calculateTotals(0);

    }

    @Override
    public void updateItemInList(Item item) {
        int index = salesInvoice.getIndex();

        salesInvoice.getItemsList().get(index).setItemNo(item.getItemNo());
        salesInvoice.getItemsList().get(index).setItemName(item.getItemName());
        salesInvoice.getItemsList().get(index).setQty(item.getQty());
        salesInvoice.getItemsList().get(index).setUnit(item.getUnit());
        salesInvoice.getItemsList().get(index).setPrice(item.getPrice());
        salesInvoice.getItemsList().get(index).setBonus(item.getBonus());
        salesInvoice.getItemsList().get(index).setDisc(item.getDisc());
        salesInvoice.getItemsList().get(index).setDiscPerc(item.getDiscPerc());
        salesInvoice.getItemsList().get(index).setTax(item.getTaxPercent());
        salesInvoice.getItemsList().get(index).setDiscType(item.getDiscType());
        salesInvoice.getItemsList().get(index).setAmount(item.getAmount());
        salesInvoice.getItemsList().get(index).setCategory(item.getCategory());
        salesInvoice.getItemsList().get(index).setPosPrice(item.getPosPrice());

        // salesInvoice.itemsListAdapter.ite setItemsList(itemsList);
        salesInvoice.itemsListAdapter.notifyDataSetChanged();
        salesInvoice.calculateTotals(0);
    }

    @Override
    public void addItemsStockToList(List<Item> itemsList) {
        stockRequest.getItemsStockList().addAll(itemsList);
        // salesInvoice.itemsListAdapter.ite setItemsList(itemsList);
        stockRequest.itemsListAdapter.notifyDataSetChanged();
        stockRequest.calculateTotals();
    }

    @Override
    public void Update_Items() {

    }


    public void back() {

        super.onBackPressed();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("MainActivity", ""+requestCode);
        String serialBarcode="";
//        if (requestCode == 0x0000c0de) {
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.e("MainActivity", "cancelled scan");
                Toast.makeText(Activities.this, "cancelled", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("Activities1", "onActivityResult" + Result.getContents());

                try {
                    serialBarcode = Result.getContents().trim();
                }
                catch (Exception e){
                    serialBarcode="";
                    Toast.makeText(Activities.this, "Error1"+e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                 try {
                     String ItemNo=databaseHandler.isSerialCodeExist(serialBarcode.trim());
                     if(voucherType==504)
                     {
                         if((ItemNo.equals("not")||databaseHandler.getLastTransactionOfSerial(serialBarcode.trim()).equals("506")))
                         {
                             if((databaseHandler.isSerialCodePaied(serialBarcode.trim()).equals("not")&&voucherType==504)||
                                     (!databaseHandler.isSerialCodePaied(serialBarcode.trim()).equals("not")&&voucherType==506))
                             {
                                 if(checkInTotalList(serialBarcode.trim()))
                                 {
                                     if(updatedSerial==1||addNewSerial==1)
                                     {
                                         serialValueUpdated.setText(serialBarcode.toString().trim());
                                     }else {
                                         serialValue.setText(serialBarcode.toString().trim());
                                     }

                                 }
                                 else {
                                     new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                             .setTitleText(Activities.this.getString(R.string.warning_message))
                                             .setContentText(Activities.this.getString(R.string.duplicate)+"\t"+Activities.this.getResources().getString(R.string.inThisVoucher))

                                             .show();

                                 }
                             }
                             else {
                                     try {
                                         String voucherNo=databaseHandler.isSerialCodePaied(serialBarcode.trim());
                                         String voucherDate=voucherNo.substring(voucherNo.indexOf("&")+1);
                                         voucherNo=voucherNo.substring(0,voucherNo.indexOf("&"));

                                         new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                                 .setContentText(Activities.this.getString(R.string.duplicate) +"\t"+serialBarcode+ "\t"+Activities.this.getString(R.string.forVoucherNo)+"\t" +voucherNo+"\n"+Activities.this.getString(R.string.voucher_date)+"\t"+voucherDate)
                                                 .setConfirmButton(Activities.this.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                                     @Override
                                                     public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                         openSmallScanerTextView();
                                                         sweetAlertDialog.dismissWithAnimation();
                                                     }
                                                 })
                                                 .show();
                                     }catch (Exception e){
                                         Toast.makeText(Activities.this, ""+Activities.this.getString(R.string.duplicate) +"\t"+serialBarcode, Toast.LENGTH_SHORT).show();
                                     }


                             }


                         }
                         else {


                             if(!ItemNo.equals("")){
                                 new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                         .setTitleText(Activities.this.getString(R.string.warning_message))
                                         .setContentText(Activities.this.getString(R.string.invalidSerial)+"\t"+serialBarcode+"\t"+Activities.this.getString(R.string.forItemNo)+ItemNo)
                                         .setConfirmButton(Activities.this.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                             @Override
                                             public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                 openSmallScanerTextView();
                                                 sweetAlertDialog.dismissWithAnimation();
                                             }
                                         })
                                         .show();
                             }else {
                                 new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                         .setTitleText(Activities.this.getString(R.string.warning_message))
                                         .setContentText(Activities.this.getString(R.string.invalidSerial)+"\t"+serialBarcode)
                                         .setConfirmButton(Activities.this.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                             @Override
                                             public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                 openSmallScanerTextView();
                                                 sweetAlertDialog.dismissWithAnimation();

                                             }
                                         })
                                         .show();

                             }

                         }
                     }
                     else {// vouchertype=506
                         String customerVoucher=databaseHandler.getCustomerForSerial(serialBarcode.trim());
                         if( (!databaseHandler.isSerialCodePaied(serialBarcode.trim()).equals("not")&&voucherType==506))
                         {
                             if(checkCustomer(customerVoucher)){


                             if(checkPrice(serialBarcode.trim()))
                             {

                                 if(checkInTotalList(serialBarcode.trim()))
                                 {

                                     updatePaymentTypeForVoucher(serialBarcode.trim());
                                     if(updatedSerial==1||addNewSerial==1)
                                     {
                                         serialValueUpdated.setText(serialBarcode.toString().trim());
                                     }else {
                                         serialValue.setText(serialBarcode.toString().trim());
                                     }
                                 }
                                 else {
                                     new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                             .setTitleText(Activities.this.getString(R.string.warning_message))
                                             .setContentText(Activities.this.getString(R.string.duplicate)+"\t"+Activities.this.getResources().getString(R.string.inThisVoucher))

                                             .show();

                                 }




                                 Log.e("checkPrice","true");
                             }else {
                                 Log.e("checkPrice","false");
                                 new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                         .setTitleText(Activities.this.getString(R.string.warning_message))
                                         .setContentText(Activities.this.getString(R.string.deffirentPrice)+"\t"+price.getText().toString())

                                         .show();
                             }
                             }
                             else {
                                 Log.e("checkPrice", "false");
                                 new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                         .setTitleText(Activities.this.getString(R.string.warning_message))
                                         .setContentText(Activities.this.getString(R.string.defferentCustomer) + "\t" + customerVoucher)

                                         .show();
                             }

                         }
                         else {
                             new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                                     .setTitleText(Activities.this.getString(R.string.warning_message))
                                     .setContentText(Activities.this.getString(R.string.serialIsNotPaied) + "\t" + serialBarcode)
                                     .setConfirmButton(Activities.this.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                         @Override
                                         public void onClick(SweetAlertDialog sweetAlertDialog) {
                                             openSmallScanerTextView();
                                             sweetAlertDialog.dismissWithAnimation();
                                         }
                                     })
                                     .show();
                         }

                     }



                 }catch (Exception e){
                     Log.e("","");
                     Toast.makeText(Activities.this, "Error2"+e.getMessage(), Toast.LENGTH_SHORT).show();

                 }





            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
//        }

        switch (requestCode) {
            case 10001:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();

                        openDialog=true;
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

                        openDialog=false;
                        break;
                    default:
                        break;
                }
                break;
        }

    }

    private void updatePaymentTypeForVoucher(String barcodeStr) {
        int payType=databaseHandler.getPayTypeForVoucher(barcodeStr);
       // SalesInvoice salesInvoice =new SalesInvoice();
        Log.e("updatePaymentTypeFor",""+salesInvoice.payMethod+"\t pay2"+payType);
        SalesInvoice.payMethod=payType;
        SalesInvoice.refreshPayMethod(payType);
      //  salesInvoice.refreshPayMethod(payType);
        Log.e("updatePaymentTypeFor",""+SalesInvoice.payMethod);
    }

    private boolean checkCustomer(String customerVoucher) {
        Log.e("checkCustomer","= "+customerVoucher+"\tCustomer_Name= "+CustomerListShow.Customer_Name);
        if(customerVoucher.length()!=0)
        {
            if(CustomerListShow.Customer_Name.trim().equals(customerVoucher.trim()))
                return  true;

        }
        return  false;
    }

    private boolean checkPrice(String barcode) {
        double previusPrice_doub=0;
        String previusPrice= databaseHandler.getpreviusePriceSale(barcode);
        try{
            previusPrice_doub= Double.parseDouble(previusPrice);
        }catch (Exception e){

        }

//        Log.e("previusPrice1","updatedSerial="+updatedSerial);
        if(updatedSerial==0)// from adapter recycler
        {

            if(serialListitems.size()==0)
            {
                if(previusPrice_doub!=0)
                price.setText(previusPrice+"");

                return true;
            }else {
//                Log.e("previusPrice2",""+price.getText().toString());
                double curentPrc=0;
                try{
                    curentPrc=Double.parseDouble(price.getText().toString().trim());

                }catch (Exception e){
                    curentPrc=0;
                }
                if(previusPrice_doub==curentPrc)
                    return  true;
            }




        }else {// from edit serial

//            if(serialListitems.size()==0)
//            {
//                price.setText(previusPrice+"");
//
//                return true;
//            }else {
                Log.e("previusPrice2",""+price_serial_edit.getText().toString());
                double curentPrc=0;
                try{
                    curentPrc=Double.parseDouble(price_serial_edit.getText().toString().trim());

                }catch (Exception e){
                    curentPrc=0;
                }
                if(previusPrice_doub==curentPrc)
                    return  true;
//            }
        }


       return  false;
    }

    private boolean checkInTotalList(String s) {
        boolean existInTotal=false;
        if(listSerialTotal.size()!=0){
           // Log.e("checkInTotalList","indexOf"+listSerialTotal.indexOf(s.toString().trim()));
                for(int j=0;j<listSerialTotal.size();j++)
                {
                    if(listSerialTotal.get(j).getSerialCode().equals(s.toString().trim()))
                    {
                        return  false;
                    }

                }

//
//                if(listSerialTotal.indexOf(s.toString().trim())!=-1)
//                {
//
//                }
        }
        return  true;
    }

    public void openSmallScanerTextView() {
        new IntentIntegrator(Activities.this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();

    }


}
