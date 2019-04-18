package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class InventoryReport  extends AppCompatActivity {
    EditText item_number2;
     Button preview;
    TableLayout tableInventoryReport;
    List<inventoryReportItem> itemsReportinventory;
    List<Voucher> item;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_report);

        itemsReportinventory = new ArrayList<inventoryReportItem>();
        DatabaseHandler obj = new DatabaseHandler(InventoryReport.this);

        itemsReportinventory = obj.getInventory_db();
        item_number2 = (EditText) findViewById(R.id.item_number_inventory);
        item_number2.requestFocus();
        tableInventoryReport = (TableLayout) findViewById(R.id.TableInventoryReport);
        preview = (Button) findViewById(R.id.preview_button_inventory);

        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    preview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
                }
                return true;
            }
        });
        load();

    }
    public  void load()
    {
        clear();
        for (int n = 0; n < itemsReportinventory.size(); n++) {
            try {

                TableRow row = new TableRow(InventoryReport.this);
                row.setPadding(5, 10, 5, 10);

                if (n % 2 == 0)
                    row.setBackgroundColor(ContextCompat.getColor(InventoryReport.this, R.color.layer4));
                else
                    row.setBackgroundColor(ContextCompat.getColor(InventoryReport.this, R.color.layer5));
                for (int i = 0; i < 3; i++) {
                    String[] record = {itemsReportinventory.get(n).getItemNo() + "",
                            itemsReportinventory.get(n).getName() + "",
                            itemsReportinventory.get(n).getQty() + "",};
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView textView = new TextView(InventoryReport.this);
                    textView.setText(record[i].toString());
                    textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    textView.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                    textView.setLayoutParams(lp2);

                    row.addView(textView);
                }
                tableInventoryReport.addView(row);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // Button preview to filter the result accourding to item number
    public void creatInventoryReport_button(View v) {
        clear();
        if (!item_number2.getText().toString().equals("")) {

            for (int n = 0; n < itemsReportinventory.size(); n++) {
                try {
                    if (filters(n)) {
                        TableRow row = new TableRow(InventoryReport.this);
                        row.setPadding(5, 10, 5, 10);

                        if (n % 2 == 0)
                            row.setBackgroundColor(ContextCompat.getColor(InventoryReport.this, R.color.layer4));
                        else
                            row.setBackgroundColor(ContextCompat.getColor(InventoryReport.this, R.color.layer5));
                        for (int i = 0; i < 3; i++) {
                            String[] record = {itemsReportinventory.get(n).getItemNo() + "",
                                    itemsReportinventory.get(n).getName() + "",
                                    itemsReportinventory.get(n).getQty() + "",};
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            TextView textView = new TextView(InventoryReport.this);
                            textView.setText(record[i].toString());
                            textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                            textView.setGravity(Gravity.CENTER);

                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            textView.setLayoutParams(lp2);

                            row.addView(textView);
                        }
                        tableInventoryReport.addView(row);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(InventoryReport.this, "Please fill the item number fields", Toast.LENGTH_LONG).show();
        }
    }

    public void clear() {
        int childCount = tableInventoryReport.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            tableInventoryReport.removeViews(1, childCount - 1);

        }
    }
    public boolean filters (int n) throws ParseException {

        String item_num = item_number2.getText().toString().trim();
        String item_number_inventory=itemsReportinventory.get(n).getItemNo();
        if (!item_number2.getText().toString().equals("")) {

            if (item_num.equals(item_number_inventory)) {
                return true;
            }
        }
        Toast.makeText(InventoryReport.this, "invalid number ", Toast.LENGTH_SHORT).show();
        return  false;
    }
}
