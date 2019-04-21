package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class InventoryReport extends AppCompatActivity {
    EditText item_number2, item_name;
    Button preview2;
    List<inventoryReportItem> itemsReportinventory;
    SearchView search;
    RecyclerView recyclerView;
    Context context;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_report);
        itemsReportinventory = new ArrayList<inventoryReportItem>();
        itemsReportinventory.clear();
        DatabaseHandler obj = new DatabaseHandler(InventoryReport.this);
        itemsReportinventory = obj.getInventory_db();
        item_number2 = (EditText) findViewById(R.id.item_number_inventory);
        preview2 = (Button) findViewById(R.id.preview_button_inventory);
        //---------------   to load Inventory report  accourding to item number---------------------
        preview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item_number2.getText().toString().equals("")) {
                    ArrayList<inventoryReportItem> filteredList_number = new ArrayList<>();
                    for (int k = 0; k < itemsReportinventory.size(); k++) {
                        if (itemsReportinventory.get(k).getItemNo().equals(item_number2.getText().toString())) {

                            filteredList_number.add(itemsReportinventory.get(k));

                        }
                    }

                    ListInventoryAdapter adapter = new ListInventoryAdapter(filteredList_number, context);
                    recyclerView.setAdapter(adapter);
                } else {
                    ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
                    recyclerView.setAdapter(adapter);
                }

            }
        });

        //-------------------------------Spinner-------------------------------------------------
        final Spinner categorySpinner = (Spinner) findViewById(R.id.cat);
        List<String> categories = obj.getAllExistingCategories();
        categories.add(0, "no filter");

        final ArrayAdapter<String> ad = new ArrayAdapter<>(this, R.layout.spinner_style, categories);
        categorySpinner.setAdapter(ad);

//      load Inventory Report
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_report);
        recyclerView.setLayoutManager(linearLayoutManager);
        final ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
        recyclerView.setAdapter(adapter);

//      load  Inventory Report  when choose item from spinner category
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                if (!categorySpinner.getSelectedItem().toString().equals("no filter"))
                {
                    ArrayList<inventoryReportItem> filteredList = new ArrayList<>();
                    for (int k = 0; k < itemsReportinventory.size(); k++)
                    {
                        if (itemsReportinventory.get(k).getCategoryId().equals(categorySpinner.getSelectedItem().toString()))
                            filteredList.add(itemsReportinventory.get(k));


                    }
                    ListInventoryAdapter adapter = new ListInventoryAdapter(filteredList, context);
                    recyclerView.setAdapter(adapter);
                } else
                    {
                    ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
                    recyclerView.setAdapter(adapter);
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //--------------------------------------------------------------------------------

        preview2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    preview2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    preview2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
                }
                return false;
            }
        });
       //--------------------------------SearchView  to load Inventory report  accourding to item name ------------------------------------------------
        search = (SearchView) findViewById(R.id.mSearch2);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // @Override
            public boolean onQueryTextChange(String newText)
            {
                Log.e("loa", "text cha");
                if (newText != null && newText.length() > 0)
                {
                    ArrayList<inventoryReportItem> filteredList_name = new ArrayList<>();
                    for (int k = 0; k < itemsReportinventory.size(); k++)
                    {
                        if (itemsReportinventory.get(k).getName().contains(newText.toString()))
                        {
                            filteredList_name.add(itemsReportinventory.get(k));

                        }
                    }
                    ListInventoryAdapter adapter = new ListInventoryAdapter(filteredList_name, context);
                    recyclerView.setAdapter(adapter);
                }
                else
                    {
                    ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
                    recyclerView.setAdapter(adapter);
                    }

                return false;
            }

        });
    }

    public void clear() {
        itemsReportinventory.clear();

    }

}






