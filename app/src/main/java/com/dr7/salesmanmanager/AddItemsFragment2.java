package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Reports.StockRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.dr7.salesmanmanager.SalesInvoice.jsonItemsList;
import static com.dr7.salesmanmanager.SalesInvoice.totalQty_textView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemsFragment2 extends DialogFragment {


    private static List<Item> List;
    private Item item;
    Button addToListButton, doneButton;
    SearchView search;
    private ArrayList<String> itemsList;
//    public static  List<Item> jsonItemsList;
//    public static List<Item> jsonItemsList2;
//    public static List<Item> jsonItemsList_intermidiate;
    RecyclerView recyclerView;
    ListView verticalList;
    public  static   int total_items_quantity=0;
    private float descPerc;
    boolean added = false;
    double  flagBonus=0,amountBonus=0;
    private static String smokeGA = "دخان";
    private static String smokeGE = "SMOKE";


    public AddItemsInterface getListener() {
        return listener;
    }

    public interface AddItemsInterface {
        public void addItemsToList(List<Item> itemsList);
    }

    private AddItemsInterface listener;

    public AddItemsFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        jsonItemsList = new ArrayList<>();
//        jsonItemsList2= new ArrayList<>();
//        jsonItemsList_intermidiate = new ArrayList<>();
        List = new ArrayList<Item>();
        List.clear();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);
        String s="";
        int size_firstlist=0;

        final View view = inflater.inflate(R.layout.add_items_dialog2, container, false);
        DatabaseHandler mHandler = new DatabaseHandler(getActivity());
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
        recyclerView.setAdapter(adapter);

        final Spinner categorySpinner = view.findViewById(R.id.cat);
        List<String> categories = mHandler.getAllExistingCategories();
        categories.add(0, getResources().getString(R.string.all_item));

        final ArrayAdapter<String> ad = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, categories);
        categorySpinner.setAdapter(ad);

//        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
//        recyclerView.setAdapter(adapter);

        // ****************************** Kind Item Spenner*****************************************************

        final Spinner Kind_item_Spinner = view.findViewById(R.id.spinner_kind_item);
        List<String> Kind_item=new ArrayList<>();
        try {
            Kind_item = mHandler.getAllKindItems();

        }
        catch (Exception e)
        {
            Kind_item.add(0 ,getResources().getString(R.string.all_item));


        }
        Kind_item.add(0 ,getResources().getString(R.string.all_item));

        final  ArrayAdapter<String> adapter_kind = new ArrayAdapter<>(getActivity() , R.layout.spinner_style, Kind_item);
        Kind_item_Spinner.setAdapter(adapter_kind);

        //kind item
        Kind_item_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Kind_item_Spinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (int j = 0; j < jsonItemsList.size(); j++) {
                        Log.e("llog",jsonItemsList.get(j).getKind_item() + "     *    "  +Kind_item_Spinner.getSelectedItem().toString() );
                        if (jsonItemsList.get(j).getKind_item().equals(Kind_item_Spinner.getSelectedItem().toString()))
                            filteredList.add(jsonItemsList.get(j));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(adapter);
                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
                recyclerView.setAdapter(adapter);
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!categorySpinner.getSelectedItem().toString().equals(getResources().getString(R.string.all_item))) {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (int k = 0; k < jsonItemsList.size(); k++) {
                        if (jsonItemsList.get(k).getCategory().equals(categorySpinner.getSelectedItem().toString()))
                            filteredList.add(jsonItemsList.get(k));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(adapter);
                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
                recyclerView.setAdapter(adapter);

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

                if (query != null && query.length() > 0) {
                    ArrayList<Item> filteredList = new ArrayList<>();
                    for (int k = 0; k < jsonItemsList.size(); k++) {
                        if (jsonItemsList.get(k).getItemName().toUpperCase().contains(query))
                            filteredList.add(jsonItemsList.get(k));
                    }
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(adapter);
                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });


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

//                        total_items_quantity -= List.size();
//                        totalQty_textView.setText("+"+0);
//                        total_items_quantity=0;
                        for(int j=0;j<List.size();j++)
                        {
                            count+=List.get(j).getQty();
                        }
//                        Log.e("count",""+count);
//                        Log.e("totalQty",""+total_items_quantity+"\t listsize="+""+List.size());
                        total_items_quantity-=count;
                        totalQty_textView.setText(total_items_quantity+"");
                        List.clear();
//                        Log.e("totalQty",""+total_items_quantity+"\t listsize="+""+List.size());
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
                listener.addItemsToList(List);
                AddItemsFragment2.this.dismiss();
          }
        });
        return view;
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


    @SuppressLint("ResourceAsColor")
    public boolean addItem(String itemNumber, String itemName, String tax, String unit, String qty,
                           String price, String bonus, String discount, RadioGroup discTypeRadioGroup,
                           String category, String posPrice,CheckBox useWeight, Context context) {

        SalesInvoice obj = new SalesInvoice();
        String itemGroup;
        boolean existItem = false;
        for(int i = 0 ; i< obj.getItemsList().size() ; i++){
            Log.e("***" , obj.getItemsList().get(i).getItemNo() + " " + itemNumber);
            if(obj.getItemsList().get(i).getItemNo().equals(itemNumber)){
                existItem = true;
                break;
            }
        }
        if(existItem) {
            Toast toast = Toast.makeText(context, "This item has been added before !", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 180);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(15);
            toast.show();

            return false;
        }
//        else {

            item = new Item();
            item.setItemNo(itemNumber);
            item.setItemName(itemName);
            item.setTax(Float.parseFloat(tax.trim()));
            item.setCategory(category);

            try {
                item.setUnit(unit);
                //****************************

                item.setQty(Float.parseFloat(qty));

                item.setPrice(Float.parseFloat(price.trim()));
                if (bonus == "")
                    item.setBonus(Float.parseFloat("0.0"));
                else
                    item.setBonus(Float.parseFloat(bonus));
                item.setTax(Float.parseFloat(tax.trim()));
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
                item.setDiscType(1);
            } else {
                item.setDiscType(0);
            }

            try {
                if (item.getDiscType() == 0) {
                    item.setDisc(Float.parseFloat(discount.trim()));
                    item.setDiscPerc((item.getQty() * item.getPrice() *
                            (Float.parseFloat(discount.trim()) / 100)) + "");

                } else {
                    item.setDiscPerc(Float.parseFloat(discount.trim()) + "");
                    item.setDisc(item.getQty() * item.getPrice() *
                            (Float.parseFloat(discount.trim())) / 100);
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

                /*if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE) )
                    item.setAmount(item.getQty() * (float)item.getPosPrice()  - item.getDisc());
                else*/


                    item.setAmount(item.getQty() * item.getPrice() - item.getDisc());


                    Log.e("log =", item.getQty() + " * " + item.getPrice() + " -" + item.getDisc());
//                    item.setAmount(Float.parseFloat(item.getUnit()) * item.getQty() * item.getPrice() - item.getDisc());
                } else {
//                item.setAmount(Float.parseFloat(item.getUnit()) * item.getQty() * item.getPrice() - descPerc);
                    item.setAmount(item.getQty() * item.getPrice() - descPerc);
                    Log.e("log ==", item.getQty() + " * " + item.getPrice() + " -" + descPerc);
                }
            } catch (NumberFormatException e) {
                item.setAmount(0);
            }
//        }


        if ((!item.getItemName().equals("")) && item.getAmount() > 0 || item.getDiscType()==0 ) {
            if (item.getItemName().equals("(bonus)")) {
                flagBonus = List.get(List.size() - 1).getQty();
                total_items_quantity -= flagBonus;
                Log.e("flagBonus", "" + flagBonus);
//               ?     amountBonus = items.get(i).getQty();
//                    totalQty = totalQty - flagBonus;
            }
            else {
                total_items_quantity += item.getQty();
                totalQty_textView.setText("+ " + total_items_quantity);
                Log.e("setQty", "" + Float.parseFloat(qty));
                Log.e("total_items_quantity", "" + total_items_quantity);
            }


                List.add(item);
                Toast toast = Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 180);
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(15);
                toast.show();


            return true;

        } else {
            Toast toast = Toast.makeText(context, "Items has not been added, insure your entries", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 180);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(15);
            toast.show();
            return false;
        }
    }


}

