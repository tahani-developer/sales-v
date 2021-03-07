package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.FragmentManager;


import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Reports.Reports;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.LocationPermissionRequest.openDialog;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.MainActivity.curentDate;
import static com.dr7.salesmanmanager.MainActivity.curentTime;
import static com.dr7.salesmanmanager.MainActivity.masterControlLoc;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.item_serial;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialValue;
import static com.dr7.salesmanmanager.SalesInvoice.voucherNumberTextView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;
import static com.dr7.salesmanmanager.Serial_Adapter.barcodeValue;

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
    private CardView saleCardView, receiptCardView, accountBalance, supplimentCardView,uncollectChechue;

    private int activitySelected;
    public  static  String currentKeyTotalDiscount="",keyCreditLimit="",  currentKey="";

    private LinearLayout salesInvoiceLayout,mainlayout,linearMainActivities,mainLinearHolder,
            linearInvoice,linearPayment,linearStock,linearBalance,linearuncollect,dashLayout,fragmentContainer;

    private SalesInvoice salesInvoice;
    private  Transaction_Fragment transaction_fragment;

    private StockRequest stockRequest;
    Transaction transaction;
    private DecimalFormat decimalFormat;

    private boolean isFragmentBlank;
    boolean canClose;
    ProgressDialog dialog_progress;
    DatabaseHandler databaseHandler;
    static String[] araySerial;
    TextView switchLayout;
    public static TextView totalBalance_text,lastVisit_textView;
    public  static  LocationPermissionRequest locationPermissionRequestAc;
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

 public  static   double discvalue_static=0;
    @Override
    public void addDiscount(double discount, int iDiscType) {

//        salesInvoice.sum_discount+=discount;

//        salesInvoice.sum_discount=0;
        Log.e("addDiscount","discount"+discount);
        discvalue_static=discount;
//        salesInvoice.discTextView.setText(decimalFormat.format(discount));
        salesInvoice.calculateTotals();
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
//        fillLastVisit();
//        fiiltotalBalance();
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
        uncollectChechue= (CardView) findViewById(R.id.unCollectChequesCardView);
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

//        if(!totalBalance_text.getText().toString().equals("")){
            if(isNetworkAvailable())
            {
                ImportJason importJason =new ImportJason(Activities.this);
                importJason.getCustomerInfo(2);
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
        if(!CustomerListShow.Customer_Account.equals(""))
        {
            transaction=databaseHandler.getLastVisitInfo(CustomerListShow.Customer_Account,Login.salesMan);
            if(transaction.getCheckInDate()!=null)
            {
                visit=transaction.getCheckInDate()+"\t\t"+transaction.getCheckInTime();
                Log.e("getLastVaisit",""+CustomerListShow.Customer_Account+"\t"+Login.salesMan+"\t"+transaction.getCheckInDate());
            }
            else {
                visit=curentDate+"\t\t"+curentTime;
            }

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
//
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

                                finish();
                                Intent inte=new Intent(Activities.this,AccountStatment.class);
                                startActivity(inte);
                            }
                        });

                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                        builder2.create().show();
                    } else {
                        finish();
                        Intent inte=new Intent(Activities.this,AccountStatment.class);
                        startActivity(inte);

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
            }
        }
    };

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
                MainActivity. masterControlLoc.setText("2");
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
        salesInvoice.calculateTotals();

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
        salesInvoice.calculateTotals();
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
//        if (requestCode == 0x0000c0de) {
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.e("MainActivity", "cancelled scan");
                Toast.makeText(Activities.this, "cancelled", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("Activities1", "onActivityResult" + Result.getContents());


                String serialBarcode = Result.getContents();

                if((databaseHandler.isSerialCodeExist(serialBarcode+"").equals("not")))
                {
                    if((databaseHandler.isSerialCodePaied(serialBarcode+"").equals("not")&&voucherType==504)||
                            (!databaseHandler.isSerialCodePaied(serialBarcode+"").equals("not")&&voucherType==506))
                    {
                        Log.e("Activities2", "onActivityResult+true+serialBarcode" + serialBarcode);
                        serialValue.setText(serialBarcode);
                    }
                    else
                    {
                        Log.e("Activities3", "onActivityResult+false+isSerialCodePaied" + serialBarcode);
                        new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(Activities.this.getString(R.string.warning_message))
                            .setContentText(Activities.this.getString(R.string.duplicate)+"\t"+serialBarcode)
                            .show();}


                }
                else {
                    Log.e("Activities4", "onActivityResult+false+isSerialCodeExist" + serialBarcode);

                    new SweetAlertDialog(Activities.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(Activities.this.getString(R.string.warning_message))
                            .setContentText(Activities.this.getString(R.string.invalidSerial)+"\t"+serialBarcode)
                            .show();
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
}
