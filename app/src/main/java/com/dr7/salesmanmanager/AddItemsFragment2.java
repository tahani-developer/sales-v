package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemsFragment2 extends DialogFragment {


    private static List<Item> List;
    private Item item;
    Button addToListButton, doneButton;
    private ArrayList<String> itemsList;
    private List<Item> jsonItemsList;
    RecyclerView recyclerView;
    private float descPerc;
    boolean added = false;


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

        jsonItemsList = new ArrayList<>();
        List = new ArrayList<Item>();
        List.clear();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        final View view = inflater.inflate(R.layout.add_items_dialog2, container, false);

        DatabaseHandler mHandler = new DatabaseHandler(getActivity());
        if (mHandler.getAllSettings().get(0).getPriceByCust() == 0)
            jsonItemsList = mHandler.getAllJsonItems();
        else
            jsonItemsList = mHandler.getAllJsonItems2();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonItemsList, getActivity());
        recyclerView.setAdapter(adapter);


        Button done = (Button) view.findViewById(R.id.done);

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

    @SuppressLint("ResourceAsColor")
    public boolean addItem(String itemNumber, String itemName, String tax, String unit, String qty,
                           String price, String bonus, String discount, RadioGroup discTypeRadioGroup, Context context) {

        item = new Item();
        item.setItemNo(itemNumber);
        item.setItemName(itemName);
        item.setTax(Float.parseFloat(tax.trim()));

        try {
            item.setUnit(unit);
            item.setQty(Float.parseFloat(qty.trim()));
            item.setPrice(Float.parseFloat(price.trim()));
            if (bonus == "")
                item.setBonus(Float.parseFloat("0.0"));
            else
                item.setBonus(Float.parseFloat(bonus));
            item.setTax(Float.parseFloat(tax.trim()));

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
                item.setAmount(item.getQty() * item.getPrice() - item.getDisc());
            } else {
                item.setAmount(item.getQty() * item.getPrice() - descPerc);
            }
        } catch (NumberFormatException e) {
            item.setAmount(0);
        }


        if ((!item.getItemName().equals("")) && item.getAmount() > 0) {
            List.add(item);
            Toast toast = Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 180);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(25);
            toast.show();

            return true;

        } else {
            Toast toast = Toast.makeText(context, "Items has not been added, insure your entries", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 180);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(25);
            toast.show();
            return false;
        }
    }

}
