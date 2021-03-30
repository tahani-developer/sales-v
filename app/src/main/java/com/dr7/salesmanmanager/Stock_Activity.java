package com.dr7.salesmanmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Reports.Reports;

import java.util.List;

public class Stock_Activity extends AppCompatActivity implements    StockRequest.StockInterFace, AddItemsStockFragment.AddItemsInterface {
    private StockRequest stockRequest;

    @Override
    public void addItemsStockToList(List<Item> itemsList) {
        stockRequest.getItemsStockList().addAll(itemsList);
        // salesInvoice.itemsListAdapter.ite setItemsList(itemsList);
        stockRequest.itemsListAdapter.notifyDataSetChanged();
        stockRequest.calculateTotals();
    }

    public interface StockInterFace {
        public void displayFindItemStockFragment();
    }

    StockRequest.StockInterFace stockInterFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LocaleAppUtils().changeLayot(Stock_Activity.this);
        setContentView(R.layout.activity_stock_);
        displayStockRequest();
    }
    private void displayStockRequest() {

        Log.e("displayStockRequest","yes");
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i= new Intent( Stock_Activity.this,MainActivity.class);
        startActivity(i);
    }
}
