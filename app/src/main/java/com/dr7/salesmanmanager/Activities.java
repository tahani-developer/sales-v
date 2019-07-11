package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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

public class Activities extends AppCompatActivity implements
        SalesInvoice.SalesInvoiceInterface, AddItemsFragment.AddItemsInterface,
        ReceiptVoucher.ReceiptInterFace, DiscountFragment.DiscountInterface, Update_Items.Update_Items_interface,
        StockRequest.StockInterFace, AddItemsStockFragment.AddItemsInterface, AddItemsFragment2.AddItemsInterface

{

    private ImageView  returnInvImageView, receiptImageView, stockImageView,saleImageView;
  //  private CircleImageView saleImageView;
    private CardView saleCardView, receiptCardView, newOrderCardView, supplimentCardView;

    private int activitySelected;

    private LinearLayout salesInvoiceLayout;

    private SalesInvoice salesInvoice;

    private StockRequest stockRequest;

    private DecimalFormat decimalFormat;

    private boolean isFragmentBlank;
    boolean canClose;


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


    @Override
    public void addDiscount(double discount, int iDiscType) {
        salesInvoice.discTextView.setText(decimalFormat.format(discount));
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
        saleCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.second_color));
        receiptCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        supplimentCardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer2));
        isFragmentBlank = false;
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
                                    displaySaleInvoice();
                                }
                            });
                            builder.setNegativeButton(getResources().getString(R.string.app_no), null);
                            builder.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {

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
                                displayStockRequest();
                            }
                        });

                        builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                        builder2.create().show();
                    } else {
                        displayStockRequest();
                    }

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
