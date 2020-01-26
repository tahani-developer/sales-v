package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Item;

import java.text.DecimalFormat;
import java.util.List;

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
    private CardView saleCardView, receiptCardView, newOrderCardView, supplimentCardView;

    private int activitySelected;

    private LinearLayout salesInvoiceLayout;

    private SalesInvoice salesInvoice;
    private  Transaction_Fragment transaction_fragment;

    private StockRequest stockRequest;

    private DecimalFormat decimalFormat;

    private boolean isFragmentBlank;
    boolean canClose;
    ProgressDialog dialog_progress;


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
        DiscountFragment discountFragment = new DiscountFragment();
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
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        activitySelected = -1;
        isFragmentBlank = true;
        saleImageView = (ImageView) findViewById(R.id.saleInvImageView);
        transaction_imageview= (ImageView) findViewById(R.id.transaction_ImageView);
        transaction_imageview.setOnClickListener(onClickListener);
        saleCardView = (CardView) findViewById(R.id.saleCardView);
        receiptCardView = (CardView) findViewById(R.id.receiptCardView);
        //  newOrderCardView = (CardView) findViewById(R.id.newOrderCardView);
        supplimentCardView = (CardView) findViewById(R.id.supplimentCardView);

        receiptImageView = (ImageView) findViewById(R.id.paymentImageView);
        stockImageView = (ImageView) findViewById(R.id.stockRequestImageView);

        saleImageView.setOnClickListener(onClickListener);
        receiptImageView.setOnClickListener(onClickListener);
        stockImageView.setOnClickListener(onClickListener);

        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        // newOrderCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        decimalFormat = new DecimalFormat("##.000");

    }

    private void displaySaleInvoice() {

        //   if (activitySelected == 0)
        //      return;

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
//        isFragmentBlank = false;
//        AddItemsFragment2.total_items_quantity=0;
    }

    private void displayReceipt() {

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
        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
        isFragmentBlank = false;
    }

    private void displayStockRequest() {

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
        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
        isFragmentBlank = false;
    }


    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.saleInvImageView:
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
                                    saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
                                    receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                                    supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
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
                            saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
                            receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                            supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                            new Task().execute();
                            discvalue_static=0;
                            displaySaleInvoice();
                        }//displaySaleInvoice();

                    } else
                        Toast.makeText(Activities.this, "Please Select a Customer", Toast.LENGTH_LONG).show();
                    break;

                case R.id.paymentImageView:
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
                                    displayReceipt();
                                }
                            });

                            builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                            builder2.create().show();
                        } else {
                            displayReceipt();
                        }
                        // displayReceipt();

                    } else
                        Toast.makeText(Activities.this, "Please Select a Customer", Toast.LENGTH_LONG).show();
                    break;

                case R.id.stockRequestImageView:
                  //  stockImageView.startAnimation(animZoomIn);
                    if (!isFragmentBlank) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
                        builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                        builder2.setCancelable(false);
                        builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                        builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                                receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                                supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
                                displayStockRequest();
//                                new TaskStock().execute();
                            }
                        });

                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                        builder2.create().show();
                    } else {
//                        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
//                        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
                        displayStockRequest();

                    }
                    break;
                case R.id.transaction_ImageView:
                    Toast.makeText(Activities.this, "transaction clicked", Toast.LENGTH_SHORT).show();
                    if (!isFragmentBlank) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Activities.this);
                        builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                        builder2.setCancelable(false);
                        builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                        builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                displayTransactionFragment();
                            }
                        });

                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                        builder2.create().show();
                    } else {
//                        displayTransactionFragment();
                    }

//                    displayTransactionFragment();
                    break;
            }
        }
    };
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
                back();
                saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
                isFragmentBlank = true;
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
}
