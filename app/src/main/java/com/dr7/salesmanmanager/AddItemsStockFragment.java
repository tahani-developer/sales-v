package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Reports.StockRecyclerViewAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.dr7.salesmanmanager.SalesInvoice.totalQty_textView;
import static com.dr7.salesmanmanager.StockRequest.addItemImgButton;
import static com.dr7.salesmanmanager.StockRequest.items;
import static com.dr7.salesmanmanager.StockRequest.itemsNoList;
import static com.dr7.salesmanmanager.StockRequest.itemsRequiredList;
import static com.dr7.salesmanmanager.StockRequest.voucherNumber;
import static com.dr7.salesmanmanager.Stock_Activity.intentData;


public class AddItemsStockFragment extends DialogFragment {

    private static List<Item> List;
    private Item item;
    Button addToListButton, doneButton;
    SearchView search ;
    private ArrayList<String> itemsList;
    private List<Item> jsonItemsList;
    RecyclerView recyclerView;
    private float descPerc;
    boolean added = false;
    public static EditText barcode;
    ImageView barcodebtn;
    CompanyInfo companyInfo;
    DecimalFormat threeDForm;
    TextView textViewItemNum,textViewUnit_qty;
    DatabaseHandler mHandler;



    public AddItemsInterface getListener() {
        return listener;
    }

    public interface AddItemsInterface {
        public void addItemsStockToList(List<Item> itemsList);
    }

    private AddItemsInterface listener;

    public AddItemsStockFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        List = new ArrayList<Item>();
        List.clear();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        final View view = inflater.inflate(R.layout.add_items_stock_dialog, container, false);
        addItemImgButton.setEnabled(true);
         mHandler = new DatabaseHandler(getActivity());
        threeDForm = new DecimalFormat("00.00");
//        String rate_customer=mHandler.getRateOfCustomer();
//        Log.e("rate addItem",""+rate_customer);
//        if (mHandler.getAllSettings().get(0).getPriceByCust() == 0)
//            jsonItemsList = mHandler.getAllJsonItems(rate_customer);
//        else
//            jsonItemsList = mHandler.getAllJsonItems2(rate_customer);
        jsonItemsList = new ArrayList<>();
        companyInfo = new CompanyInfo();
        jsonItemsList=itemsRequiredList;
        textViewItemNum=view.findViewById(R.id.textViewItemNum);
        textViewUnit_qty=view.findViewById(R.id.textViewUnit_qty);
        if(intentData.equals("read"))
        {
            textViewItemNum.setVisibility(View.GONE);
            textViewUnit_qty.setVisibility(View.GONE);
        }
       // jsonItemsList = mHandler.getAllJsonItemsStock(1);






        // ****************************** Category Spinner Spenner*****************************************************

        final Spinner categorySpinner = view.findViewById(R.id.cat);
        List<String> categories = mHandler.getAllExistingCategories();
        categories.add(0 , "no filter");

        ArrayAdapter<String> ad = new ArrayAdapter<>(getActivity() , R.layout.spinner_style, categories);
        categorySpinner.setAdapter(ad);

        final Spinner Kind_item_Spinner = view.findViewById(R.id.spinner_kind_item);
        List<String> Kind_item=new ArrayList<>();
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
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (int j = 0; j < jsonItemsList.size(); j++) {
                        Log.e("llog", jsonItemsList.get(j).getKind_item() + "     *    " + Kind_item_Spinner.getSelectedItem().toString());
                        if (jsonItemsList.get(j).getKind_item().equals(Kind_item_Spinner.getSelectedItem().toString()))
                            filteredList.add(jsonItemsList.get(j));
                    }
                    StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(adapter) ;
                } else {
                    StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(jsonItemsList, getActivity());
                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity() , 1));
        StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(jsonItemsList, getActivity());
        recyclerView.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!categorySpinner.getSelectedItem().toString().equals("no filter")) {
                    ArrayList<Item> filteredList = new ArrayList<>();
//                    jsonItemsList.clear();
//                    jsonItemsList.addAll(itemsRequiredList);
//                    jsonItemsList=itemsRequiredList;
                    for (int k = 0; k < jsonItemsList.size(); k++) {
                        if (jsonItemsList.get(k).getCategory().equals(categorySpinner.getSelectedItem().toString()))
                            filteredList.add(jsonItemsList.get(k));
                    }
                    StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(adapter);
                } else {
                    StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(jsonItemsList, getActivity());
                    recyclerView.setAdapter(adapter);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button cancel = (Button) view.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemsStockFragment.this.dismiss();
//                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
//                builder2.setTitle(getResources().getString(R.string.app_confirm_dialog));
//                builder2.setCancelable(false);
//                builder2.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
//                builder2.setIcon(android.R.drawable.ic_dialog_alert);
//                builder2.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
////                        float count=0;
////
//////                        total_items_quantity -= List.size();
//////                        totalQty_textView.setText("+"+0);
//////                        total_items_quantity=0;
////                        for(int j=0;j<List.size();j++)
////                        {
////                            count+=List.get(j).getQty();
////                        }
//////                        Log.e("count",""+count);
//////                        Log.e("totalQty",""+total_items_quantity+"\t listsize="+""+List.size());
////                        total_items_quantity-=count;
////                        totalQty_textView.setText(total_items_quantity+"");
//                      //  List.clear();
////                        Log.e("totalQty",""+total_items_quantity+"\t listsize="+""+List.size());
//                        AddItemsStockFragment.this.dismiss();
//
//
//                    }
//                });
//
//                builder2.setNegativeButton(getResources().getString(R.string.app_no), null);
//                builder2.create().show();
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

                if (query != null && query.length() > 0) {
                    String[] arrOfStr = query.split(" ");
                    int[] countResult = new int[arrOfStr.length];
                    Log.e("arrOfString", "" + arrOfStr.toString() + " \n   " + arrOfStr[0] + " \n  " + arrOfStr.length);

                    ArrayList<Item> filteredList = new ArrayList<>();

//                    "jkgb".matches()

                    boolean isFound = false;
                    for (int i = 0; i < jsonItemsList.size(); i++) {
                        for (int j = 0; j < arrOfStr.length; j++) {
                            String lowers = arrOfStr[j].toLowerCase();
                            String uppers = arrOfStr[j].toUpperCase();

                            if (jsonItemsList.get(i).getItemName().toLowerCase().contains(lowers) || jsonItemsList.get(i).getItemName().toUpperCase().contains(uppers)) {

                                isFound = true;

                            } else {
                                isFound = false;
                                break;
                            }


                        }
                        if (isFound) {
                            filteredList.add(jsonItemsList.get(i));
                        }


                    }

                    StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(adapter);


                } else {
                    StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(jsonItemsList, getActivity());
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        barcode=(EditText)view.findViewById(R.id.barcode);
        barcodebtn=(ImageView)view.findViewById(R.id.searchBarcode);
        barcodebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=barcode.getText().toString();
                if(!s.equals("")) {
                    searchByBarcodeNo(s + "");
                }
                else{
                    Intent i=new Intent(getActivity(),ScanActivity.class);
                    i.putExtra("key","4");
                    startActivity(i);
                    searchByBarcodeNo(s + "");

                }

            }
        });
        barcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

//                if(event.getRawX() <= (barcode.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))
//                {
//                    // your action here
//                    return true;
//                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (barcode.getRight() - barcode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {   barcode.setText("");
                        StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(jsonItemsList, getActivity());
                        recyclerView.setAdapter(adapter);
                        return true;

                    }

                }
                return false;
            }
        });

        Button doneButton = (Button) view.findViewById(R.id.done2);

        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemsList();
                listener.addItemsStockToList(List);
                AddItemsStockFragment.this.dismiss();
            }
        });
        return view;
    }

    private void addItemsList() {
        for(int i=0;i<itemsRequiredList.size();i++)
        {
            if(itemsRequiredList.get(i).getQty()!=0)
            addItem(itemsRequiredList.get(i).getItemNo(), itemsRequiredList.get(i).getItemName(),
                    "0","1", (itemsRequiredList.get(i).getQty()+""),
                    "1"+itemsRequiredList.get(i).getPrice(),
                    "0", "0", getContext(),itemsRequiredList.get(i).getCurrentQty());
        }
    }

//    public  void searchByBarcodeNo(String barcodeValue) {
//        if(!barcodeValue.equals(""))
//        {
//
//
//            ArrayList<Item> filteredList = new ArrayList<>();
//            for (int k = 0; k < jsonItemsList.size(); k++) {
//                if (jsonItemsList.get(k).getItemNo().equals(barcodeValue)){
//                    filteredList.add(jsonItemsList.get(k));
//                }
//            }
//            StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(filteredList, getActivity());
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
//            StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(jsonItemsList, getActivity());
//            recyclerView.setAdapter(adapter);
//
//
//        }
//    }
    public  void searchByBarcodeNo(String barcode) {
        String itemNo="",barcodeValue="";
        barcodeValue=barcode.trim();
        try {
            itemNo=mHandler.getItemNoForBarcode(barcodeValue);
            if(itemNo.equals(""))
            {
                itemNo=mHandler.getItemNoForSerial(barcodeValue);
            }
            Log.e("searchByBarcodeNo",""+itemNo);
        }catch (Exception e)
        {
            itemNo="";
        }
        try {


            if (!barcodeValue.equals("")) {
                ArrayList<Item> filteredList = new ArrayList<>();
                for (int k = 0; k < jsonItemsList.size(); k++) {
                    if (jsonItemsList.get(k).getBarcode().equals(barcodeValue.trim())) {
                        filteredList.add(jsonItemsList.get(k));
                        break;

                    } else {

                        if (!itemNo.equals("")) {
                            if (itemNo.equals(jsonItemsList.get(k).getItemNo())) {

                                filteredList.add(jsonItemsList.get(k));
                                break;
                            }
                        }


                    }
                }
                // Log.e("searchByBarcodeNo","size"+filteredList.size());
                StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(filteredList, getActivity());
                recyclerView.setAdapter(adapter);
                Log.e("filteredList=","" + filteredList.size());
                if(filteredList.size()==0)
                {
                    Toast.makeText(getActivity(), barcodeValue+"\tNot Found", Toast.LENGTH_LONG).show();
                }



            } else {
                StockRecyclerViewAdapter adapter = new StockRecyclerViewAdapter(jsonItemsList, getActivity());
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

    @SuppressLint("ResourceAsColor")
    public boolean addItem(String itemNumber, String itemName, String tax, String unit , String qty,
                           String price, String bonus, String discount, Context context,double currentQty) {
//        RadioGroup discTypeRadioGroup,
        boolean found=false;
        item = new Item();
        item.setItemNo(itemNumber);
        item.setItemName(itemName);
        item.setTax(Float.parseFloat(tax.trim()));
        item.setVoucherNumber(voucherNumber);

        float qtyValue=0;
        try {
            qtyValue= Float.parseFloat(qty.trim());
        }
        catch (Exception e){qtyValue=0;}

        try {
            item.setUnit(unit);
            item.setQty(qtyValue);
            item.setPrice(Float.parseFloat(price.trim()));
            if (bonus == "")
                item.setBonus(Float.parseFloat("0.0"));
            else
                item.setBonus(Float.parseFloat(bonus));
            item.setTax(Float.parseFloat(tax.trim()));
            item.setCurrentQty(currentQty);

        } catch (NumberFormatException e) {
            item.setUnit("");
            item.setQty(0);
            item.setPrice(0);
            item.setBonus(0);
            item.setDisc(0);
            item.setDiscPerc("0%");
            item.setAmount(0);
            Log.e("Add new item error", e.getMessage().toString());
        }


        try {
            if (item.getDiscType() == 0) {
                item.setDisc(Float.parseFloat(discount.trim()));
                item.setDiscPerc((item.getQty() * item.getPrice() *
                        (Float.parseFloat(discount.trim()) / 100)) + "%");

            } else {
                item.setDiscPerc(Float.parseFloat(discount.trim()) + "%");
                item.setDisc(item.getQty() * item.getPrice() *
                        (Float.parseFloat(discount.trim())) / 100);
            }
            descPerc = ((item.getQty() * item.getPrice() *
                    (Float.parseFloat(discount.trim()) / 100)));
        } catch (NumberFormatException e) {
            item.setDisc(0);
            item.setDiscPerc("0%");
        }

        try {
            if (item.getDiscType() == 0) {
                item.setAmount(item.getQty() * item.getPrice() - item.getDisc());
            } else {
                item.setAmount(item.getQty() * item.getPrice() - descPerc);
            }
        } catch (NumberFormatException e) {
            item.setAmount(0);
        }
        Toast toast=null;
        if ((!item.getItemName().equals("")) && item.getAmount() > 0) {
            found=false;
            for(int i=0;i<items.size();i++)
            {
                if(items.get(i).getItemNo().equals(itemNumber))
                {
                    found=true;
                    items.get(i).setQty(qtyValue);

                     //toast = Toast.makeText(context, "UpdatedSuccessfully", Toast.LENGTH_SHORT);

                }
            }
            if(found==false)
            {
                List.add(item);
                itemsNoList.add(item.getItemNo());
                /// toast = Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT);
            }





//            toast.setGravity(Gravity.CENTER, 0, 180);
//            ViewGroup group = (ViewGroup) toast.getView();
//            TextView messageTextView = (TextView) group.getChildAt(0);
//            messageTextView.setTextSize(20);
//            toast.show();

            return true;

        } else {
//            toast = Toast.makeText(context, "Items has not been added, insure your entries", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 180);
//            ViewGroup group = (ViewGroup) toast.getView();
//            TextView messageTextView = (TextView) group.getChildAt(0);
//            messageTextView.setTextSize(20);
//            toast.show();
            return false;
        }
    }


}
