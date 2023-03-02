package com.dr7.salesmanmanager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;

import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.google.zxing.integration.android.IntentIntegrator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Item;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.dr7.salesmanmanager.Login.POS_ACTIVE;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.Login.sharedPref;
//import static com.dr7.salesmanmanager.SalesInvoice.jsonItemsList;

import static com.dr7.salesmanmanager.RecyclerViewAdapter.CountOfItems;
import static com.dr7.salesmanmanager.SalesInvoice.VERSION;
import static com.dr7.salesmanmanager.SalesInvoice.addItemImgButton2;
import static com.dr7.salesmanmanager.SalesInvoice.canChangePrice;
import static com.dr7.salesmanmanager.SalesInvoice.jsonItemsList;
import static com.dr7.salesmanmanager.SalesInvoice.jsonItemsList2;
import static com.dr7.salesmanmanager.SalesInvoice.listSerialTotal;
import static com.dr7.salesmanmanager.SalesInvoice.minusQtyTotal;
import static com.dr7.salesmanmanager.SalesInvoice.priceListTypeVoucher;
import static com.dr7.salesmanmanager.SalesInvoice.totalQty_textView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherNumberTextView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;


public class AddItemsFragment2 extends DialogFragment {
    public   AllItemRecyclerListViewAdapter allItemAdapterList;
    ArrayList<Item> filteredList_allItem = new ArrayList<>();
    public  CheckBox greaterZero_checkbox;
    public static List<Item> List;
    TableRow HeaderNewLin;
    public  static  int endAddItem=0;
    int  Spancount=2;
    public  static  int size_customerpriceslist=0,qtyGreatZero=0;
    public  List<Item> itemsList_forFilter;
    Context context;
    public LinearLayoutManager linearLayoutManager;
    GeneralMethod generalMethod;
     Spinner categorySpinner,Kind_item_Spinner;
    public  String voucherDate="";
    public static final int REQUEST_Camera_Barcode = 1;
    private Item item;
    Button addToListButton, doneButton;
    SearchView search;
   public static EditText barcode;
   public  TextView clearBarcode;
    ImageView barcodebtn;
    private ArrayList<String> itemsList;
//    public static  List<Item> jsonItemsList;
//    public static List<Item> jsonItemsList2;
//    public static List<Item> jsonItemsList_intermidiate;
    RecyclerView recyclerView;
   CheckBox orientation_checkbox;
        AppCompatCheckBox Smallericon_checkbox;
    ListView listAllItemsView;
    TextView emptyView;
    ListView verticalList;
    public  static   float total_items_quantity=0;

    private float descPerc;
    boolean added = false;
    double  flagBonus=0,amountBonus=0;
    private static String smokeGA = "دخان";
    private static String smokeGE = "SMOKE";
    final String result="";
    int size_firstlist=0;
     public   static String s="";
    SimpleDateFormat df, df2;
    Date currentTimeAndDate;
    String userNo="";
    int itemUnit=0,countListVisible=0;
    private static DatabaseHandler mDbHandler;
    private DecimalFormat decimalFormat    = new DecimalFormat("00.000");;

    public AddItemsInterface getListener() {
        return listener;
    }

    public interface AddItemsInterface {
        public void addItemsToList(List<Item> itemsList);
    }

    private AddItemsInterface listener;

    public AddItemsFragment2() {
        generalMethod=new GeneralMethod(context);
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        new LocaleAppUtils().changeLayot(context);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDbHandler = new DatabaseHandler(getActivity());

        countListVisible = mDbHandler.getCountVisibleItemSize();
//        jsonItemsList = new ArrayList<>();
//        jsonItemsList2= new ArrayList<>();
//        jsonItemsList_intermidiate = new ArrayList<>();
        List = new ArrayList<Item>();
        List.clear();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);


        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        int size_firstlist = 0;

        final View view = inflater.inflate(R.layout.add_items_dialog2, container, false);

        LinearLayout add_item = view.findViewById(R.id.add_item);
        try {
            if (languagelocalApp.equals("ar")) {
                add_item.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    add_item.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        } catch (Exception e) {
            add_item.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        DatabaseHandler mHandler = new DatabaseHandler(getActivity());

        userNo = mDbHandler.getAllUserNo();
        try {
            if (!userNo.equals("")) {
                fillListItemJson();

            } else {
                AddItemsFragment2.this.dismiss();
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
            }
        } catch (Exception e) {
            Log.e("Exception", "getItems" + e.getMessage());

        }


//        String rate_customer=mHandler.getRateOfCustomer();  // customer rate to display price of this customer
//
//        if (mHandler.getAllSettings().get(0).getPriceByCust() == 0)
//            jsonItemsList = mHandler.getAllJsonItems(rate_customer);
//        else {
//
//            jsonItemsList2 = mHandler.getAllJsonItems2(rate_customer);
//            size_firstlist=jsonItemsList2.size();
//
//            jsonItemsList=mHandler.getAllJsonItemsNotInCustomerPrices(rate_customer);
//            int count=jsonItemsList2.size()+jsonItemsList.size();
//
//            for(int i=0;i<count;i++)
//            {
//                if(i<size_firstlist)
//                {
//                    jsonItemsList_intermidiate.add(jsonItemsList2.get(i));
//                }
//                else
//                jsonItemsList_intermidiate.add(jsonItemsList.get(i-size_firstlist));
//            }
//            jsonItemsList=jsonItemsList_intermidiate;
//            Log.e("size after ",""+jsonItemsList.size());
//
//        }

        //    test


        recyclerView = view.findViewById(R.id.recyclerView);

        orientation_checkbox= view.findViewById(R.id.orientation_checkbox);
        Smallericon_checkbox= view.findViewById(R.id.Smallericon_checkbox);
        HeaderNewLin= view.findViewById(R.id.HeaderNewLin);

        orientation_checkbox.setButtonDrawable(R.drawable.custom_checkbox);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        orientation_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("orientation_checkbox="," orientation_checkbox");
                if(b)
                {

                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    int numberOfColumns = 6;
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));


                    SharedPreferences.Editor editor = getContext().getSharedPreferences( Login.SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
                    editor.putBoolean(Login.Items_Orent_PREF, b);
                    editor.apply();
                }
                else
                {    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);}
                recyclerView.setLayoutManager(linearLayoutManager);
                SharedPreferences.Editor editor = getContext().getSharedPreferences( Login.SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(Login.Items_Orent_PREF, b);

                editor.apply();
            }
        });

//        Smallericon_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.e("Smallericon_checkbox="," Smallericon_checkbox");
//                if(b)
//                {
//                    Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.ic_baseline_grid_on_24));
//                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Spancount));
//
//Log.e("Smallericon_checkbox=",b+" ");
//
//                    SharedPreferences.Editor editor = getContext().getSharedPreferences( Login.SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
//                    editor.putBoolean(Login.Smallericon_PREF, b);
//                    editor.apply();
//                }
//                else
//                {
//                    Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.ic_baseline_grid_off_24));
//                SharedPreferences.Editor editor = getContext().getSharedPreferences( Login.SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
//                editor.putBoolean(Login.Smallericon_PREF, b);
//
//                editor.apply();
//                    if(orientation_checkbox.isChecked()==true) {
//                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//                        recyclerView.setLayoutManager(linearLayoutManager);
//                    }   else {
//                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//                        recyclerView.setLayoutManager(linearLayoutManager);
//                    }
//
//            }}
//        });
        Smallericon_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("Smallericon_checkbox="," Smallericon_checkbox");
                if(b)
                {
                    Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.blueic_baseline_table_view_24));
                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.blueic_baseline_table_view_24));
                    HeaderNewLin.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = getContext().getSharedPreferences( Login.SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
                    editor.putBoolean(Login.Smallericon_PREF, b);
                    editor.apply();
                }
                else
                {
                    Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.grayic_baseline_table_view_24));
                    SharedPreferences.Editor editor = getContext().getSharedPreferences( Login.SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
                    editor.putBoolean(Login.Smallericon_PREF, b);
                    Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.grayic_baseline_table_view_24));
                    editor.apply();
                    HeaderNewLin.setVisibility(View.GONE);
                    if(orientation_checkbox.isChecked()==true) {
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }   else {
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }

                }}
        });
        emptyView = (TextView) view.findViewById(R.id.empty_view);
//        listAllItemsView=view.findViewById(R.id.itemsListView);
        listAllItemsView = (ListView) view.findViewById(R.id.itemsListView);

// ...


//       if( jsonItemsList.size()!=0){

      if(sharedPref==null)  sharedPref = getActivity().getSharedPreferences("SETTINGS_PREFERENCES", MODE_PRIVATE);
        if  (sharedPref.getBoolean(Login.Smallericon_PREF, false))
        {     Log.e("Smallericon_checkbox11==","case1");
            Smallericon_checkbox.setChecked(true);
            HeaderNewLin.setVisibility(View.VISIBLE);
            Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.blueic_baseline_table_view_24));

        }
        else
        {  Smallericon_checkbox.setChecked(false);
            HeaderNewLin.setVisibility(View.GONE);
            Log.e("Smallericon_checkbox11==","case2");
            Smallericon_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.grayic_baseline_table_view_24));
        }

     if  (sharedPref.getBoolean(Login.Items_Orent_PREF, true))
         orientation_checkbox.setChecked(true);
     else
         orientation_checkbox.setChecked(false);

        if(orientation_checkbox.isChecked()==true) {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }   else {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        Log.e("Smallericon_checkbox11==",Smallericon_checkbox.isChecked()+"");

        if(Smallericon_checkbox.isChecked()==true) {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        else
        {

            if(orientation_checkbox.isChecked()==true) {
                linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
            }   else {
                linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
            }


        }




//        fillListViewAllItem();
        fillItemRecycler();

//       }


        categorySpinner = view.findViewById(R.id.cat);
        List<String> categories = mHandler.getAllExistingCategories();
        categories.add(0, getResources().getString(R.string.all_item));

        final ArrayAdapter<String> ad = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, categories);
        categorySpinner.setAdapter(ad);

//        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
//        recyclerView.setAdapter(adapter);

        // ****************************** Kind Item Spenner*****************************************************

        Kind_item_Spinner = view.findViewById(R.id.spinner_kind_item);
        List<String> Kind_item = new ArrayList<>();
        try {
            Kind_item = mHandler.getAllKindItems();

        } catch (Exception e) {
            Kind_item.add(0, getResources().getString(R.string.all_item));


        }
        Kind_item.add(0, getResources().getString(R.string.all_item));

        final ArrayAdapter<String> adapter_kind = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, Kind_item);
        Kind_item_Spinner.setAdapter(adapter_kind);

        //kind item
        Kind_item_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Kind_item_Spinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                    if (!categorySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                        filterTow();
                    } else {
                        filterKindItem();
                    }


                } else {
                    Log.e("categorySpinner", "else" + categorySpinner.getSelectedItemPosition());
                    if (!categorySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                        filterCategoury();// filter about categoury

                    } else {
                        fillAllItems();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fillAllItems();
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!categorySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                    if (!Kind_item_Spinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                        filterTow();
                    } else {
                        filterCategoury();
                    }


                } else {
                    if (!Kind_item_Spinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                        filterKindItem();
                    } else
                        fillAllItems();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fillAllItems();

            }
        });

        search = view.findViewById(R.id.mSearch);


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
//                adapter.getFilter().filter(query);
                boolean isFound = false;
//                try {
//
//                    Log.e("jsonItemsList=", jsonItemsList.size()+"" );
//                    for(int i=0;i<jsonItemsList.size();i++) {
//                        Log.e("getItemName=", "" +jsonItemsList.get(i).getItemName());
//
//                        if (fisrtchar(jsonItemsList.get(i).getItemName(), query.toString())) {
//                            Log.e("fisrtchar=", "" + "true");
//                            isFound = true;
//                        } else {
//                            Log.e("fisrtchar=", "" + "false");
//                            isFound = false;
//                        }
//                    }
//
//                }catch (Exception e){
//                    Log.e("ifmatch=",""+"Exception");
//                }



                filteredList_allItem.clear();
                if (query != null && query.length() > 0) {
                    String[] arrOfStr = query.split(" ");
                    int[] countResult = new int[arrOfStr.length];


                    ArrayList<Item> filteredList = new ArrayList<>();
//                    filteredList_allItem


                    if(filteredList_allItem.size()==0){
                        filteredList_allItem.addAll(jsonItemsList);
                    }


                    for (int i = 0; i < filteredList_allItem.size(); i++) {

                        if (filteredList_allItem.get(i).getItemName().toLowerCase().contains(query.toString())
                                || filteredList_allItem.get(i).getItemName().toUpperCase().contains(query.toString())
                        ||
                                FisrtCharsFromString(jsonItemsList.get(i).getItemName().toLowerCase(Locale.ROOT), query.toString().toLowerCase(Locale.ROOT))
                        ) {
                            isFound = true;
                        }
                             else {
                                isFound = false;


                        }
//                        for (int j = 0; j < arrOfStr.length; j++) {
//                            String lowers = arrOfStr[j].toLowerCase();
//                            String uppers = arrOfStr[j].toUpperCase();
//
//                            if (filteredList_allItem.get(i).getItemName().toLowerCase().contains(lowers)
//                                    || filteredList_allItem.get(i).getItemName().toUpperCase().contains(uppers)) {
//
//                                isFound = true;
//
//                            } else {
//                                isFound = false;
//                               break;
//                            }
//
//
//                        }
                        if (isFound) {

                            if(qtyGreatZero==1)
                            {
                                if(jsonItemsList.get(i).getQty()>0)
                                {
                                    filteredList.add(filteredList_allItem.get(i));

                                }
                            }else {
                                filteredList.add(filteredList_allItem.get(i));
                            }





                        }


                    }

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);


                } else {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    filteredList=   getFilteredZero();
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        //***************************************************************************************
        barcode = (EditText) view.findViewById(R.id.barcode);
        clearBarcode = (TextView) view.findViewById(R.id.clearBarcode);
        clearBarcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBarcodeLayout();


            }
        });
        barcodebtn = (ImageView) view.findViewById(R.id.searchBarcode);
        barcodebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                 s=barcode.getText().toString();
//                if(!s.equals("")) {
//                    searchByBarcodeNo(s + "");
//                }
//                else{
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_Camera_Barcode);
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {//just for first time
                        Log.e("requestresult", "PERMISSION_GRANTED");
                        Intent i = new Intent(getActivity(), ScanActivity.class);
                        i.putExtra("key", "1");
                        startActivity(i);
                        try {
                            searchByBarcodeNo(s + "");
                        } catch (Exception e) {
                            Toast.makeText(context, "try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Intent i = new Intent(getActivity(), ScanActivity.class);
                    i.putExtra("key", "1");
                    startActivity(i);
                    try {
                        searchByBarcodeNo(s + "");
                    } catch (Exception e) {
                        Toast.makeText(context, "try Again", Toast.LENGTH_SHORT).show();
                    }
                }


//                }

            }
        });
        if (POS_ACTIVE == 0) {
            barcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    try {
                        if (!s.toString().equals("")) {
                            Log.e("afterTextChanged", "" + s.toString());

                            try {
                                searchByBarcodeNo(s.toString().trim() + "");
                            } catch (Exception e) {
                                Toast.makeText(context, "try Again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (Exception e) {
                    }
                }
            });
        } else {




        //********************************************** for POS *********************************************************
        barcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_NULL) {
//                    Log.e("setOnEditorActio", "afterTextChangedNOT" +"errorData\t"+actionId);
                    try {
                        if (!barcode.getText().toString().trim().equals("")) {

                            try {
                                searchByBarcodeNo(barcode.getText().toString().trim() + "");
                            } catch (Exception e) {
                                Toast.makeText(context, "try Again", Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            cleanBarcodeLayout();

                        }
                    } catch (Exception e) {
                    }
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            barcode.requestFocus();
//
//                        }
//                    });

                }


                return false;
            }
        });

    }








//        barcode.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
////                if(event.getRawX() <= (barcode.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))
////                {
////                    // your action here
////                    return true;
////                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (barcode.getRight() - barcode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
//                    {   barcode.setText("");
//                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, AddItemsFragment2.this);
//                        recyclerView.setAdapter(adapter);
//                        return true;
//
//                    }
//
//                }
//                return false;
//            }
//        });
        barcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                try {
//                   Log.e("Runnable","--"+barcode.getText().toString());
//                    Log.e("onFocusChange",""+hasFocus+endAddItem);
//                    if (hasFocus&&endAddItem==1) {
                    if (POS_ACTIVE == 1) {
                        if (endAddItem==1) {
                            endAddItem = 0;
                            cleanBarcodeLayout();
                        }
//                        searchByBarcodeNo("");

//                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    } else {
                         // endAddItem=0;
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(barcode.getWindowToken(), 0);
                    }
                }catch (Exception e){}

//
            }
        });

        //***************************************************************************************


        Button done = (Button) view.findViewById(R.id.done);

        Button cancel = (Button) view.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder2.setCancelable(false);
                builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
                builder2.setIcon(android.R.drawable.ic_dialog_alert);
                builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float count=0;
                        addItemImgButton2.setEnabled(true);

                        for(int h=0;h<List.size();h++)
                        {

                            updateListSerialBukupDeleted( List.get(h).getItemNo(),voucherNumberTextView.getText().toString());
                        }
                        int listSize=listSerialTotal.size();

                        if(listSerialTotal.size()!=0)
                        {
                            for(int j=0;j<List.size();j++)

                            {
                                int k=0;
                                while (k<listSerialTotal.size())
                                {
                                    count+=List.get(j).getQty();
                                    if(listSerialTotal.get(k).getItemNo().equals(List.get(j).getItemNo())&&(listSerialTotal.get(k).getPriceItem()==List.get(j).getPrice()))// CRASH IT
                                    {
                                        listSerialTotal.remove(k);
                                    }
                                    else {
                                        k++;
                                    }
                                }
                            }
                        }
                        else {
                            for(int j=0;j<List.size();j++)
                            {
                                count+=List.get(j).getQty();
                            }

                        }

                        minusQtyTotal(count);
                        List.clear();
                        SalesInvoice.updateQtyBasket();
                        AddItemsFragment2.this.dismiss();


                    }
                });

                builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
                builder2.create().show();
            }
        });

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    listener.addItemsToList(List);
                    addItemImgButton2.setEnabled(true);
                    SalesInvoice.updateQtyBasket();

                    AddItemsFragment2.this.dismiss();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Re Select Items Please", Toast.LENGTH_SHORT).show();
                    Log.e("AddItemException",""+e.getMessage());
                }

          }
        });

        greaterZero_checkbox=view.findViewById(R.id.greaterZero_checkbox);
//        greaterZero_checkbox.setChecked(false);
        greaterZero_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("onCheckedChanged",""+b);

                if(b){qtyGreatZero=1;}else {
                    qtyGreatZero=0;
                }
                doAllFilters();
            }
        });


        return view;
    }

    private void doAllFilters() {
        if (!Kind_item_Spinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
            if (!categorySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                filterTow();
            } else {
                filterKindItem();
            }


        } else {
            Log.e("categorySpinner", "else" + categorySpinner.getSelectedItemPosition());
            if (!categorySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                filterCategoury();// filter about categoury

            } else {
                fillAllItems();
            }

        }
    }

    private void cleanBarcodeLayout() {
        barcode.setText("");
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        ArrayList<Item> filteredList = new ArrayList<>();
        filteredList=   getFilteredZero();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);
    }

    private void fillItemRecycler() {
        listAllItemsView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        RecyclerViewAdapter adapter;
        try {
           // Log.e("forAdapter",""+jsonItemsList.size());



            adapter = new RecyclerViewAdapter(jsonItemsList, AddItemsFragment2.this);

                    recyclerView.setAdapter(adapter);
//            recyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    recyclerView.setAdapter(adapter);
//                }
//            },100);

        }catch (Exception e){

            Log.e("recyclerView","exce="+e.getMessage());
        }
    }

    private void fillListViewAllItem() {
        Log.e("fillListViewAllItem","jsonItemsList="+jsonItemsList.size());
        allItemAdapterList = new AllItemRecyclerListViewAdapter( jsonItemsList,AddItemsFragment2.this);// if screen is landscape =====> 0


        listAllItemsView.setAdapter(allItemAdapterList);
    }

    private void filterTow() {
//        ArrayList<Item> filteredList = new ArrayList<>();
        filteredList_allItem= new ArrayList<>();
        for (int j = 0; j < jsonItemsList.size(); j++) {
            if (jsonItemsList.get(j).getKind_item().equals(Kind_item_Spinner.getSelectedItem().toString())&&
                    jsonItemsList.get(j).getCategory().equals(categorySpinner.getSelectedItem().toString()))
            {
                if(qtyGreatZero==1)
                {
                    if(jsonItemsList.get(j).getQty()>0)
                    {
                        filteredList_allItem.add(jsonItemsList.get(j));

                    }
                }else {
                    filteredList_allItem.add(jsonItemsList.get(j));
                }
            }

        }
        Log.e("filterTow",filteredList_allItem.size()+ "     *    "  +Kind_item_Spinner.getSelectedItem().toString() );
        Log.e("filterTow",filteredList_allItem.size()+ "     *    "  +categorySpinner.getSelectedItem().toString() );

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList_allItem, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);
    }
    private void filterTowByIndex(int j) {
        ArrayList<Item> filteredList = new ArrayList<>();
//        for (int j = 0; j < jsonItemsList.size(); j++) {
            if (jsonItemsList.get(j).getKind_item().equals(Kind_item_Spinner.getSelectedItem().toString())&&
                    jsonItemsList.get(j).getCategory().equals(categorySpinner.getSelectedItem().toString()))
            {
                if(qtyGreatZero==1)
                {
                    if(jsonItemsList.get(j).getQty()>0)
                    {
                        filteredList.add(jsonItemsList.get(j));

                    }
                }else {
                    filteredList.add(jsonItemsList.get(j));
                }
            }

//        }
        Log.e("filterTow",filteredList.size()+ "     *    "  +Kind_item_Spinner.getSelectedItem().toString() );
        Log.e("filterTow",filteredList.size()+ "     *    "  +categorySpinner.getSelectedItem().toString() );

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);
    }


    public  ArrayList<Item> getFilteredZero(){
        Log.e("getFilteredZero","getFilteredZero");
        ArrayList<Item> filteredList = new ArrayList<>();
        Log.e("filteredList","filteredList=="+filteredList.size());
        if(filteredList_allItem.size()==0){
            filteredList_allItem.addAll(jsonItemsList);
        }
        for (int j = 0; j < filteredList_allItem.size(); j++) {

                if(qtyGreatZero==1)
                {
                    if(filteredList_allItem.get(j).getQty()>0)
                    {
                        filteredList.add(filteredList_allItem.get(j));

                    }
                }else {
                    filteredList.add(filteredList_allItem.get(j));
                }


        }
        return  filteredList;

    }

    private void filterKindItem() {
//        ArrayList<Item> filteredList = new ArrayList<>();
        filteredList_allItem= new ArrayList<>();
        for (int j = 0; j < jsonItemsList.size(); j++) {
            if (jsonItemsList.get(j).getKind_item().equals(Kind_item_Spinner.getSelectedItem().toString()))
            {
                if(qtyGreatZero==1)
                {
                    if(jsonItemsList.get(j).getQty()>0)
                    {
                        filteredList_allItem.add(jsonItemsList.get(j));

                    }
                }else {
                    filteredList_allItem.add(jsonItemsList.get(j));
                }
            }

        }
        Log.e("filterKindItem",filteredList_allItem.size() + "     *    "  +Kind_item_Spinner.getSelectedItem().toString() );

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList_allItem, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);
    }

    private void fillAllItems() {
//        ArrayList<Item> filteredList = new ArrayList<>();
        filteredList_allItem= new ArrayList<>();
        for (int k = 0; k < jsonItemsList.size(); k++) {
            if (qtyGreatZero == 1) {
                if (jsonItemsList.get(k).getQty() > 0) {
                    filteredList_allItem.add(jsonItemsList.get(k));

                }
            } else {
                filteredList_allItem.add(jsonItemsList.get(k));
            }
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList_allItem,AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);
    }

    private void filterCategoury() {
//        ArrayList<Item> filteredList = new ArrayList<>();
        filteredList_allItem= new ArrayList<>();
        for (int k = 0; k < jsonItemsList.size(); k++) {
            if (jsonItemsList.get(k).getCategory().equals(categorySpinner.getSelectedItem().toString()))
            {
                if(qtyGreatZero==1)
                {
                    if(jsonItemsList.get(k).getQty()>0)
                    {
                        filteredList_allItem.add(jsonItemsList.get(k));

                    }
                }else {
                    filteredList_allItem.add(jsonItemsList.get(k));
                }
            }

        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList_allItem, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);
    }

    /*//                new com.google.zxing.integration.android.IntentIntegrator(getActivity()).initiateScan();
//
//                barcode.setText("");
//                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
//
//                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
//                intentIntegrator.setBeepEnabled(true);
//                intentIntegrator.setCameraId(0);
//                intentIntegrator.setOrientationLocked(true);
//                intentIntegrator.setPrompt("SCAN");
////              intentIntegrator.setBarcodeImageEnabled(false);
//                intentIntegrator.initiateScan();
////              IntentIntegrator.forSupportFragment(AddItemsFragment2.this);*/
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
//        if (Result != null) {
//            String barcodeValue;
//
//            Log.e("bar","found"+data);
//            if (Result.getContents() == null) {
//                Log.d("MainActivity", "cancelled scan");
//                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
//                barcodeValue = "cancelled";
//            } else {
//                Log.d("MainActivity", "Scanned");
//                Toast.makeText(getActivity(), "Scanned -> " + Result.getContents(), Toast.LENGTH_SHORT).show();
//
//                barcodeValue = Result.getContents();
////                String[] arrayString = barcodeValue.split(" ");
//
////Log.e("barcode_value ",""+barcodeValue+"\n"+"th ="+arrayString[0]+"\n"+"w ="+arrayString[1]+"\n"+"l ="
////        +arrayString[2]+"\n"+"grad ="+arrayString[3]);
////                searchByBundleNo(barcodeValue);
//
//                searchByBarcodeNo(barcodeValue);
//                barcode.setText(barcodeValue+"");
//                Log.e("bar","found"+barcodeValue);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
    private void updateListSerialBukupDeleted(String itemNoSelected, String vouch) {
//        for(int j=0;j<listMasterSerialForBuckup.size();j++)
//        {
        mDbHandler.updateitemDeletedInSerialTable_Backup(itemNoSelected,vouch);
//        }
    }
   public  void searchByBarcodeNo(String barcode) {
       String itemNo="",barcodeValue="";
       barcodeValue=barcode.trim();
       try {
            itemNo=mDbHandler.getItemNoForBarcode(barcodeValue);
            if(itemNo.equals(""))
            {
                itemNo=mDbHandler.getItemNoForSerial(barcodeValue);
            }

       }catch (Exception e)
       {
           itemNo="";
       }
try {


    if (!barcodeValue.equals("")) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (int k = 0; k < jsonItemsList.size(); k++) {
            if (jsonItemsList.get(k).getBarcode().trim().equals(barcodeValue.trim())) {
                if(qtyGreatZero==1){
                    if(jsonItemsList.get(k).getQty()>0)
                    {
                        filteredList.add(jsonItemsList.get(k));
                        break;
                    }else {
                        filteredList.clear();
                    }
                }else {
                    filteredList.add(jsonItemsList.get(k));
                    break;
                }



            } else {

                if (!itemNo.equals("")) {
                    if (itemNo.trim().equals(jsonItemsList.get(k).getItemNo().trim())) {

                        if(qtyGreatZero==1){
                            if(jsonItemsList.get(k).getQty()>0)
                            {
                                filteredList.add(jsonItemsList.get(k));
                                break;
                            }
                            else  filteredList.clear();
                        }else {
                            filteredList.add(jsonItemsList.get(k));
                            break;
                        }
                    }
                }


            }
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);


        if (filteredList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
//                  Toast.makeText(getActivity(), barcodeValue+"\tNot Found", Toast.LENGTH_LONG).show();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

        }


    } else {
        Log.e("jsonItemsList","2="+jsonItemsList.size());
        ArrayList<Item> filteredList = new ArrayList<>();
        filteredList=   getFilteredZero();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, AddItemsFragment2.this);
        recyclerView.setAdapter(adapter);


    }

}catch (Exception e){}

    }

    public void setListener(AddItemsInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
//    @Override
//    public void onBackPressed() {
//
//    }

    private void fillListItemJson() {// test
        String s = "";
//        jsonItemsList              = new ArrayList<>();
//        jsonItemsList2             = new ArrayList<>();
//        jsonItemsList_intermidiate = new ArrayList<>();
        String dateCurent=getCurentTimeDate(1);
        String rate_customer = mDbHandler.getRateOfCustomer();
        if(rate_customer.equals(""))
        {
            rate_customer="0";

        }
       // Log.e("fillListItemJson",""+rate_customer);// customer rate to display price of this customer
        Log.e("jsonItemsList","countListVisible"+countListVisible);
        if (mDbHandler.getAllSettings().get(0).getPriceByCust() == 0) {
            Log.e("jsonItemsList","000000");
            jsonItemsList = mDbHandler.getAllJsonItems(rate_customer,1,countListVisible);

           // Log.e("jsonItemsList", "zero"+jsonItemsList.get(0).getItemName()+"\t"+jsonItemsList.get(0).getItemHasSerial());
        }

        else {// price by customer
            try {
               // List<String> itemNoList = mDbHandler.getItemNumbersNotInPriceListD();// difference itemNo between tow table (CustomerPricess and priceListD)
               // Log.e("itemsize","3**************"+itemNoList.size());
            }catch (Exception e){
                Log.e("itemNoList","**********"+e.getMessage());
            }

            //from customers pricess

//            if(mDbHandler.getAllSettings().get(0).getReadOfferFromAdmin()==1)
//            {
//                if(priceListTypeVoucher!=0)
//                {
//                    jsonItemsList2 = mDbHandler.getAllItemsPriceFromAdmin(rate_customer,""+listOfferNo,payMethod,dateCurent);
//                  //  Log.e("priceListTypeVoucher",""+listOfferNo+"\t"+jsonItemsList2.size());
//                }
//                else {// regular list
//
//
//                    jsonItemsList2 = mDbHandler.getAllItemsPriceFromAdmin(rate_customer,""+priceListTypeVoucher,payMethod,dateCurent);
//                    jsonItemsList = mDbHandler.getAllJsonItems(rate_customer,0); // from price list d
//                }
//
//
//            }
//            else {// here 1
               // jsonItemsList2 = mDbHandler.getAllJsonItems2(rate_customer);
                jsonItemsList = mDbHandler.getAllJsonItems(rate_customer,1,countListVisible); // from price list d
//            }
            size_firstlist = jsonItemsList2.size();
           // Log.e("size_firstlist",""+size_firstlist);
            if (size_firstlist != 0&&priceListTypeVoucher!=1) {
//                Log.e("jsonItemsList_inte","11111"+jsonItemsList_intermidiate.size()+"\t"+jsonItemsList2.size());
//                size_customerpriceslist = size_firstlist;
//                 Log.e("size_firstlist",""+size_firstlist);

//                for (int k = 0; k < size_firstlist; k++) {
//                    jsonItemsList_intermidiate.add(jsonItemsList2.get(k));
//                }
//                jsonItemsList_intermidiate.addAll(jsonItemsList2);
                //****************************************************************************************




//                for (int i = 0; i < jsonItemsList.size(); i++) {
//                    for (int j = 0; j < itemNoList.size(); j++)
//                    {
//                        if (jsonItemsList.get(i).getItemNo().equals(itemNoList.get(j).toString())) {
//                            jsonItemsList_intermidiate.add(size_firstlist, jsonItemsList.get(i));
//                            size_firstlist++;
//                            break;
//
//
//
//                        }
//                    }
//
//
//                }
//
//                jsonItemsList = jsonItemsList_intermidiate;
               // Log.e("jsonItemsList",""+jsonItemsList.size());


            } else {//  (Customer Pricesfor this customer==0)    ====== >>>>>     get data from priceListD
//                Log.e("jsonItemsList","2222");
//                if(priceListTypeVoucher!=0)
//                {
//                    jsonItemsList=jsonItemsList2;
//                }else {
//                    Log.e("jsonItemsList","233");
//                   // if(jsonItemsList.size()==0)
//                    jsonItemsList = mDbHandler.getAllJsonItems(rate_customer,1);
//                }

            }


//            Collections.sort(jsonItemsList<itemNoList>);

        }
    }


    @SuppressLint("ResourceAsColor")
    public boolean addItem(String itemNumber, String itemName, String tax, String unit, String qty,
                           String price, String bonus, String discount, RadioGroup discTypeRadioGroup,
                           String category, String posPrice, int useWeight, Context context,
                           String descriptRemark, ArrayList<serialModel > itemSerialList,int hasSerial,int oneUnit)
    {
        boolean itemInlocalList=false;
        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        qty=convertToEnglish(decimalFormat.format(Float.parseFloat(qty)));

        voucherDate = df.format(currentTimeAndDate);
        SalesInvoice obj = new SalesInvoice();
        String itemGroup;
        ItemUnitDetails unitDetail;
        float priceItem=0,unitInt=0,qtyFloat=1;
       // Log.e("addItem","addItem=unit="+unit+"\tqty"+qty+"\tuseWeight"+useWeight);
        boolean existItem = false;
        float previousePrice=0,curentPrice=0;
        try {
             curentPrice=Float.parseFloat(price.trim());
        }catch (Exception e){


            Log.e("curentPrice","price="+price+"\t"+e.getMessage());
            curentPrice=0;}

//        price=convertToEnglish(decimalFormat.format(curentPrice));


//        if(obj.getItemsList().size()!=0)// check In total List
//        {
            for(int i = 0 ; i< obj.getItemsList().size() ; i++){
                if(obj.getItemsList().get(i).getItemNo().equals(itemNumber)){
                    previousePrice=obj.getItemsList().get(i).getPrice();
                    existItem = true;
                    break;
                }
            }
//        }
//        else {// check in current List
            for(int i=0;i<List.size();i++)
            {
                if(List.get(i).getItemNo().equals(itemNumber)){
                    previousePrice=List.get(i).getPrice();
                    existItem = true;
                    break;
                }
            }

//        }

       // Log.e("canChangePrice","discount=="+discount+"\tpreviousePrice="+previousePrice+"\tcurentPrice"+curentPrice);

        if(!price.equals("0"))
        {
            if((existItem)&&(canChangePrice==0)||(canChangePrice==1&&previousePrice==curentPrice)) {
               Toast toast = Toast.makeText(context, ""+context.getResources().getString(R.string.itemadedbefor), Toast.LENGTH_LONG);
                toast.show();

                return false;
            }
        }



            item = new Item();
            unitDetail=new ItemUnitDetails();
            if(mDbHandler.getAllSettings().get(0).getItems_Unit()==0)// not enabled units
            {
                CountOfItems=-1;
            }
            unitDetail=getItemUnitInfo(itemNumber,CountOfItems);
            item.setItemNo(itemNumber);
            item.setItemName(itemName);

            item.setTax(Float.parseFloat(generalMethod.convertToEnglish(generalMethod.getDecimalFormat(Double.parseDouble(tax.trim())))));
            item.setCategory(category);
            try {
                if(descriptRemark.trim().length()==0)
                {
                    descriptRemark=VERSION;
                }
            }catch ( Exception e){}

            item.setDescreption(descriptRemark);

            Log.e("setDescreption",""+item.getDescription());
            try {
                item.setUnit(unit);

                try {
                    priceItem=Float.parseFloat(price.trim());

                }catch (Exception e){
                    priceItem=0;
                }
                try{
                     unitInt=Float.parseFloat(unit);
                }
                catch (Exception e)
                {
                    unitInt=1;
                }
                try {
                   qtyFloat= Float.parseFloat(qty);
                }catch (Exception e)
                {

                }
                if(oneUnit==0)
                    item.setOneUnitItem("0");
                else item.setOneUnitItem("1");


                //***************************************************
                if(mDbHandler.getAllSettings().get(0).getItemUnit()==1)
                {int itemUnit=1;
                    if(mDbHandler.getAllSettings().get(0).getItems_Unit()==1)
                    { itemUnit=CountOfItems;

                        Log.e("getItemUnitInfo2","itemUnit="+itemUnit);
                    }
                    else {
                        itemUnit = mDbHandler.getUnitForItem(itemNumber);// get convrate
                    }
                    if(oneUnit==0)
                    item.setQty(Float.parseFloat(qty)*itemUnit);
                    else {// just item one unit
                        item.setQty(Float.parseFloat(qty));
                    }
                  //  Log.e("priceItem","="+item.getQty()+"\titemUnit="+itemUnit);
                    if(itemUnit!=1)
                    {
                        float priceUnitItem=priceItem/itemUnit;
                        item.setPrice(priceUnitItem);

                    }
                    else {
                        item.setPrice(priceItem);
                    }

                    if(unitDetail.getItemNo()!=null)
                    {

                        if(unitDetail.getConvRate()!=1)
                        {
                            // there are units
                            item.setWhich_unit("1");
                        }
                        else
                        {
                            item.setWhich_unit("0");
                        }
                        if(oneUnit==1)//one unit not
                        {
                            float priceUnitItem=priceItem/itemUnit;
                            Log.e("oneUnit",""+oneUnit+"\tpriceItem="+priceItem+"\tqty="+qty);
                            item.setWhich_unit("0");
                            item.setWhich_unit_str("");
                            item.setWhichu_qty(0+"");
                            item.setEnter_qty(qty);
                            item.setEnter_price(priceUnitItem+"");
                            item.setUnit_barcode(unitDetail.getItemBarcode());

                        }else {
                            item.setWhich_unit_str(unitDetail.getUnitId());
                            item.setWhichu_qty(unitDetail.getConvRate()+"");
                            item.setEnter_qty(qty);
                            item.setEnter_price((priceItem)+"");
                            item.setUnit_barcode(unitDetail.getItemBarcode());
                        }



                    }
                    else {

                        item.setWhich_unit("0");

                        item.setWhich_unit_str("");
                        item.setWhichu_qty("");
                        item.setEnter_qty(qty);
                        item.setEnter_price((priceItem*qtyFloat)+"");
                        item.setUnit_barcode("");
                    }



                }else {

                    if(useWeight==1)
                        item.setQty(Float.parseFloat(qty)*unitInt);
                    else {
                        item.setQty(Float.parseFloat(qty));
                    }
                    item.setPrice(priceItem);



                    item.setWhich_unit("0");

                    item.setWhich_unit_str("");
                    item.setWhichu_qty("");
                    item.setEnter_qty(qty);
                    item.setEnter_price((priceItem*qtyFloat)+"");
                    item.setUnit_barcode("");
                }

                //***************************************************

                if(useWeight==1){


                }else {

                }
                item.setItemHasSerial(hasSerial+"");


                if (bonus == "")
                    item.setBonus(Float.parseFloat("0.0"));
                else
                    item.setBonus(Float.parseFloat(bonus));
                item.setTax(Float.parseFloat(generalMethod.convertToEnglish(generalMethod.getDecimalFormat(Double.parseDouble(tax.trim())))));
                item.setPosPrice(Float.parseFloat(posPrice.trim()));

            } catch (NumberFormatException e) {
                item.setUnit("");
                item.setQty(0);
                item.setPrice(0);
                item.setBonus(0);
                item.setDisc(0);
                item.setDiscPerc("0");
                item.setAmount(0);
                Log.e("Add new item error", e.getMessage().toString());
            }


            if (discTypeRadioGroup.getCheckedRadioButtonId() == R.id.discPercRadioButton) {
                item.setDiscType(1);// error for discount promotion // percent discount

            } else {
                item.setDiscType(0);// value Discount

            }

            try {
                if (item.getDiscType() == 0) {
                    item.setDisc(Float.parseFloat(discount.trim()));// for Qasion offer * Qty
                    item.setDiscPerc((item.getQty() * item.getPrice() *
                            (Float.parseFloat(discount.trim()) / 100)) + "");

                } else {
                    item.setDiscPerc(Float.parseFloat(discount.trim()) + "");
                    item.setDisc((item.getQty() * item.getPrice() *
                            (Float.parseFloat(discount.trim())) / 100));
                }

                descPerc = ((item.getQty() * item.getPrice() *
                        (Float.parseFloat(discount.trim()) / 100)));

            } catch (NumberFormatException e) {
                item.setDisc(0);
                item.setDiscPerc("0");
            }
            try {
                if (item.getDiscType() == 0) {

                    itemGroup = item.getCategory();


                    item.setAmount(item.getQty() * item.getPrice() - item.getDisc());

//                    item.setAmount(Float.parseFloat(item.getUnit()) * item.getQty() * item.getPrice() - item.getDisc());
                } else {
//                item.setAmount(Float.parseFloat(item.getUnit()) * item.getQty() * item.getPrice() - descPerc);
                    item.setAmount(item.getQty() * item.getPrice() - descPerc);

                    Log.e("log222=", "amount==="+item.getAmount() );
                   // Log.e("log ==", item.getQty() + " * " + item.getPrice() + " -" + descPerc);
                }
            } catch (NumberFormatException e) {
                item.setAmount(0);
            }
//        }


        if ((!item.getItemName().equals("")) && item.getAmount() > 0 || item.getDiscType()==0 ||(item.getItemName().equals("(bonus)"))) {
            if (item.getItemName().equals("(bonus)")) {
                flagBonus = List.get(List.size() - 1).getQty();
                total_items_quantity -= flagBonus;
                Log.e("flagBonus", "" + flagBonus);

            }
            else {
                total_items_quantity += item.getQty();
                totalQty_textView.setText("+ " + total_items_quantity);
                //Log.e("setQty", "" + Float.parseFloat(qty));

            }







                List.add(item);
                Toast toast = Toast.makeText(context, ""+context.getResources().getString(R.string.succsesful), Toast.LENGTH_LONG);
                toast.show();
                String storeNo=mDbHandler.getAllUserNo();
                voucherDate=convertToEnglish(voucherDate);
            if(item.getItemHasSerial().equals("1"))
            {
//                Log.e("itemSerialList",""+itemSerialList.size()+itemSerialList.get(0).getSerialCode());
                for(int i=0;i<itemSerialList.size();i++)
                {
                    itemSerialList.get(i).setItemNo(itemNumber);
                    itemSerialList.get(i).setDateVoucher(voucherDate);
                    itemSerialList.get(i).setKindVoucher(voucherType+"");
                    itemSerialList.get(i).setVoucherNo(voucherNumberTextView.getText().toString());
                    itemSerialList.get(i).setStoreNo(storeNo);
                    itemSerialList.get(i).setIsDeleted("0");
                    itemSerialList.get(i).setDateDelete(voucherDate);
                    itemSerialList.get(i).setPriceItem(curentPrice);
                    itemSerialList.get(i).setPriceItem(priceItem);


                    if(voucherType!=506) {
                        try {


                        float netPriceItem = ((item.getQty() * item.getPrice()) - item.getDisc()) / item.getQty();

                        String prc = generalMethod.convertToEnglish(generalMethod.getDecimalFormat(netPriceItem));
                       // float prcFloat = Float.parseFloat(prc);
                        itemSerialList.get(i).setPriceItemSales(prc);
                    }catch (Exception e){

                        }
                    }

                    listSerialTotal.add( itemSerialList.get(i));

                }


            }

//            if(addNewSerial==1)
//            {
//                listener.addItemsToList(List);
//            }

            return true;

        } else {
            Toast toast = Toast.makeText(context, "Items has not been added, insure your entries", Toast.LENGTH_LONG);

            toast.show();
            return false;
        }
    }

    private ItemUnitDetails getItemUnitInfo(String itemNumber,int countItem) {
        Log.e("getItemUnitInfo","itemNumber="+itemNumber+"\tcountItem= "+countItem);
        ItemUnitDetails item= new ItemUnitDetails();
        item=mDbHandler.getItemUnitDetails(itemNumber,countItem);
        return  item;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","================");
    }

    @Override
    public void onStop() {
        super.onStop();
//        int vouch=Integer.parseInt(voucherNumberTextView.getText().toString());
//        mDbHandler.deletSerialItems_byVoucherNo(vouch);
        Log.e("onStop","================"+voucherNumberTextView.getText().toString());
    }

    public  void readB(){
        Log.e("barcode_099", "in");
        Log.e("currentUpdate",""+"readB");
       // openIntegratorLandScapeActivity();

       // openSannActivity();

       // openSmallActivity();
            //*********************************************************
        openSmallScanerTextView();
        //**************************************************************
//        public void scanToolbar(View view) {
//            new IntentIntegrator(getActivity()).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
//        }

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
               timeformat = new SimpleDateFormat("hh:mm:ss");
               dateCurent = timeformat.format(currentTimeAndDate);
               dateTime=convertToEnglish(dateCurent);
           }
       }
       return dateTime;

   }

    private void openIntegratorLandScapeActivity() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("SCAN");

        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    private void openSannActivity() {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CAMERA}, REQUEST_Camera_Barcode);
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            {//just for first time
                Log.e("requestresult" ,"PERMISSION_GRANTED");
                Intent i=new Intent(getActivity(),ScanActivity.class);
                i.putExtra("key","3");
                startActivity(i);

            }
        } else {
            Intent i=new Intent(getActivity(),ScanActivity.class);
            i.putExtra("key","3");
            startActivity(i);

        }
    }

    private void openSmallActivity() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setOrientationLocked(false);
            integrator.setCaptureActivity(SmallCaptureActivity.class);
            integrator.initiateScan();
    }

    public void openSmallScanerTextView() {
        new IntentIntegrator(getActivity()).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();

    }


    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

boolean FisrtCharsFromString(String ItemName, String SerachText){

    String initialsOfSubName = "";
    boolean flage = true;
    for (int i = 0; i < ItemName.length(); i++) {

        if (ItemName.charAt(i) == ' ')
            flage = true;
        else if (ItemName.charAt(i) != ' ' && flage == true) {
            initialsOfSubName += (ItemName.charAt(i));

            flage = false;
        }
    }

  if(initialsOfSubName.trim().contains(SerachText.replaceAll("\\s", "")))  return true;
  else return false;

}
    }




