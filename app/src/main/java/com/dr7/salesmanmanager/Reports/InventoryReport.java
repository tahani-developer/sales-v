package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.dr7.salesmanmanager.BluetoothConnectMenu;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.RecyclerViewAdapter;
import com.dr7.salesmanmanager.bMITP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.dr7.salesmanmanager.Reports.ListInventoryAdapter.totalQty_inventory;

public class InventoryReport extends AppCompatActivity {
    EditText item_number2, item_name;
    TextView print;
    Button preview2;
   public static TextView total_qtyText;
     public static  List<inventoryReportItem> itemsReportinventory;
    public static  List<inventoryReportItem> itemsInventoryPrint;
    SearchView search;
    RecyclerView recyclerView;
    Context context;
//    int totalQty_inventory=0;
     DatabaseHandler obj;
     CompanyInfo companyInfo;
     public  static  String typeQty="";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_report);
        itemsReportinventory = new ArrayList<inventoryReportItem>();
        itemsInventoryPrint=new ArrayList<inventoryReportItem>();
        itemsInventoryPrint.clear();
        itemsReportinventory.clear();
         obj = new DatabaseHandler(InventoryReport.this);
        itemsReportinventory = obj.getInventory_db();
        itemsInventoryPrint=itemsReportinventory;
        Log.e("itemsReportinventory",""+itemsReportinventory.size()+"itemsInventoryPrint="+itemsInventoryPrint.size());
        item_number2 = (EditText) findViewById(R.id.item_number_inventory);
        item_number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!item_number2.getText().toString().equals("")) {
                    ArrayList<inventoryReportItem> filteredList_number = new ArrayList<>();
                    for (int k = 0; k < itemsReportinventory.size(); k++) {
                        if (itemsReportinventory.get(k).getItemNo().contains(item_number2.getText().toString())) {

                            filteredList_number.add(itemsReportinventory.get(k));

                        }
                    }

                    itemsInventoryPrint=filteredList_number;
                    ListInventoryAdapter adapter = new ListInventoryAdapter(filteredList_number, context);
                    recyclerView.setAdapter(adapter);
                    setTextTotalQty( adapter.TotalQtyInventoey());
                } else {
                    itemsInventoryPrint=itemsReportinventory;
                    ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
                    recyclerView.setAdapter(adapter);
                    setTextTotalQty( adapter.TotalQtyInventoey());
                    Log.e("totalqty",""+ adapter.getItemCount());
                }




            }
        });
        total_qtyText = (TextView) findViewById(R.id.total_qty_text);
        print = (TextView) findViewById(R.id.print);
        //---------------   to load Inventory report  accourding to item number---------------------
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printLayout();


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
        setTextTotalQty( adapter.TotalQtyInventoey());

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
                    itemsInventoryPrint=filteredList;
                    ListInventoryAdapter adapter = new ListInventoryAdapter(filteredList, context);
                    recyclerView.setAdapter(adapter);
                    setTextTotalQty( adapter.TotalQtyInventoey());
                } else
                    {
                    itemsInventoryPrint=itemsReportinventory;
                    ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
                    recyclerView.setAdapter(adapter);
                    setTextTotalQty( adapter.TotalQtyInventoey());
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //-------------------------------------spiner Qty -------------------------------------------

        final Spinner qtySpinner = (Spinner) findViewById(R.id.quantityFilter);
       ArrayList<String> qtyList=new ArrayList<>() ;
       qtyList.add("الكل");
       qtyList.add("أكبر من الصفر ");
        qtyList.add("تساوي الصفر ");
        qtyList.add("أصغر من الصفر ");
        typeQty="الكل";

        final ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this, R.layout.spinner_style, qtyList);
        qtySpinner.setAdapter(adapter_2);

//      load  Inventory Report  when choose item from spinner category
        qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<inventoryReportItem> filteredList;
                Log.e("onItemSelected", "====" + i);
                switch (i) {
                    case 0:// all items
                        typeQty="الكل";
                        itemsInventoryPrint = itemsReportinventory;
                        ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
                        recyclerView.setAdapter(adapter);
                        setTextTotalQty(adapter.TotalQtyInventoey());
                        break;
                    case 1://greater than zerro
                        typeQty="أكبر من الصفر";
                        filteredList = new ArrayList<>();
                        for (int m = 0; m < itemsReportinventory.size(); m++) {
                            if (itemsReportinventory.get(m).getQty() > 0) {
                                filteredList.add(itemsReportinventory.get(m));
                            }


                        }
                        itemsInventoryPrint = filteredList;
                        ListInventoryAdapter adapte = new ListInventoryAdapter(filteredList, context);
                        recyclerView.setAdapter(adapte);
                        setTextTotalQty(adapte.TotalQtyInventoey());

                        break;
                    case 2://equal zerro
                        typeQty="تساوي الصفر";

                        filteredList = new ArrayList<>();
                        for (int m = 0; m < itemsReportinventory.size(); m++) {
                            if (itemsReportinventory.get(m).getQty() == 0) {
                                filteredList.add(itemsReportinventory.get(m));
                            }


                        }
                        itemsInventoryPrint = filteredList;
                        ListInventoryAdapter adapter_ = new ListInventoryAdapter(filteredList, context);
                        recyclerView.setAdapter(adapter_);
                        setTextTotalQty(adapter_.TotalQtyInventoey());
                        break;
                    case 3://less  than zerro
                        typeQty="أصغر من الصفر";
                        filteredList = new ArrayList<>();
                        for (int m = 0; m < itemsReportinventory.size(); m++) {
                            if (itemsReportinventory.get(m).getQty() < 0) {
                                filteredList.add(itemsReportinventory.get(m));
                            }


                        }
                        itemsInventoryPrint = filteredList;
                        ListInventoryAdapter adapter_2 = new ListInventoryAdapter(filteredList, context);
                        recyclerView.setAdapter(adapter_2);
                        setTextTotalQty(adapter_2.TotalQtyInventoey());
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //*************************************************************************************

//        preview2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    preview2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.done_button));
//                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    preview2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
//                }
//                return false;
//            }
//        });
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
                    String[] arrOfStr = newText.split(" ");
                    int [] countResult=new int[arrOfStr.length];
                    Log.e("arrOfStr",""+arrOfStr.length);

                    ArrayList<inventoryReportItem> filteredList_name = new ArrayList<>();
                    for (int k = 0; k < itemsReportinventory.size(); k++)
                    {

                        boolean isFound=false;
                            for(int j=0;j<arrOfStr.length;j++){
                                String lowers=arrOfStr[j].toLowerCase();
                                String uppers=arrOfStr[j].toUpperCase();

                                if(itemsReportinventory.get(k).getName().toLowerCase().contains(lowers)||itemsReportinventory.get(k).getName().toUpperCase().contains(uppers)){

                                    isFound=true;

                                }else {
                                    isFound=false;
                                    break;
                                }


                            }
                            if(isFound){
                                filteredList_name.add(itemsReportinventory.get(k));
                            }

                    }
                    itemsInventoryPrint=filteredList_name;
                    ListInventoryAdapter adapter = new ListInventoryAdapter(filteredList_name, context);
                    recyclerView.setAdapter(adapter);
                    setTextTotalQty( adapter.TotalQtyInventoey());
                }
                else
                    {
                        itemsInventoryPrint=itemsReportinventory;
                    ListInventoryAdapter adapter = new ListInventoryAdapter(itemsReportinventory, context);
                    recyclerView.setAdapter(adapter);
                        setTextTotalQty( adapter.TotalQtyInventoey());
                    }

                return false;
            }

        });
    }

    private void printLayout() {
        try{
            if (obj.getAllSettings().get(0).getPrintMethod() == 0) {

                try {
                    int printer = obj.getPrinterSetting();
                    companyInfo = obj.getAllCompanyInfo().get(0);
                    if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
                        if (printer != -1) {
                            switch (printer) {
                                case 0:

                                    Intent i = new Intent(InventoryReport.this, BluetoothConnectMenu.class);
                                    i.putExtra("printKey", "9");
                                    startActivity(i);

//                                                             lk30.setChecked(true);
                                    break;
                                case 1:

//                                    try {
//                                        findBT();
//                                        openBT(1);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                                             lk31.setChecked(true);
                                    break;
                                case 2:

//                                        try {
//                                            findBT();
//                                            openBT(2);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                                             lk32.setChecked(true);

//                                    convertLayoutToImage();

//                                    Intent O1= new Intent(InventoryReport.this, bMITP.class);
//                                    O1.putExtra("printKey", "9");
//                                    startActivity(O1);

                                    break;
                                case 3:

//                                    try {
//                                        findBT();
//                                        openBT(3);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                                             qs.setChecked(true);
                                    break;
                                case 4:
//                                    printTally();
                                    break;
                                case 5:
//                                    convertLayoutToImage();
                                    Intent O= new Intent(InventoryReport.this, bMITP.class);
                                    O.putExtra("printKey", "9");
                                    startActivity(O);

                                    break;


                            }
                        } else {
                            Toast.makeText(InventoryReport.this, "please chose printer setting", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(InventoryReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(InventoryReport.this, "Please set Printer Setting", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Toast.makeText(InventoryReport.this, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(InventoryReport.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();

                }
            } else {
                // hiddenDialog();
            }
        }
        catch(Exception e){
            Toast.makeText(InventoryReport.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
        }

    }




    public void clear() {
        itemsReportinventory.clear();

    }
public static void setTextTotalQty(int qty)
{
  total_qtyText.setText(qty+"");
  totalQty_inventory=0;

}
/*            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
//                adapter.getFilter().filter(query);

                if (query != null && query.length() > 0) {
                    String[] arrOfStr = query.split(" ");
                    int [] countResult=new int[arrOfStr.length];


                    ArrayList<Item> filteredList = new ArrayList<>();

//                    "jkgb".matches()

                    boolean isFound=false;
                    for(int i=0;i<jsonItemsList.size();i++){
                        for(int j=0;j<arrOfStr.length;j++){
                        String lowers=arrOfStr[j].toLowerCase();
                        String uppers=arrOfStr[j].toUpperCase();

                            if(jsonItemsList.get(i).getItemName().toLowerCase().contains(lowers)||jsonItemsList.get(i).getItemName().toUpperCase().contains(uppers)){

                                isFound=true;

                            }else {
                                isFound=false;
                                break;
                            }


                        }
                        if(isFound){
                            filteredList.add(jsonItemsList.get(i));
                        }


                    }

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);



                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList,AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        //***************************************************************************************
        */

}






