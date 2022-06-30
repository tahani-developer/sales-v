package com.dr7.salesmanmanager;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.dr7.salesmanmanager.Modles.Item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemsFragment extends DialogFragment {

    Button addToListButton, doneButton;
    private List<Item> itemsList;
    private DecimalFormat decimalFormat;
    private Item item;
    private EditText itemNoEditText, descEditText, qtyEditText, priceEditText,
            bonusEditText, discEditText, taxEditText;
    private RadioGroup discTypeRadioGroup;
    private float descPerc;

    public AddItemsInterface getListener() {
        return listener;
    }

    public interface AddItemsInterface {
        public void addItemsToList(List<Item> itemsList);
    }

    private AddItemsInterface listener;

    public AddItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(getResources().getString(R.string.app_add_items));
        final View view = inflater.inflate(R.layout.add_items_dialog, container, false);
        itemsList = new ArrayList<Item>();
        decimalFormat = new DecimalFormat("00.000");
        addToListButton = (Button) view.findViewById(R.id.addToListButton);
        doneButton = (Button) view.findViewById(R.id.doneButton);
        itemNoEditText = (EditText) view.findViewById(R.id.itemCodeEditText);
        descEditText = (EditText) view.findViewById(R.id.itemDescEditText);
        qtyEditText = (EditText) view.findViewById(R.id.qtyEditText);
        priceEditText = (EditText) view.findViewById(R.id.priceEditText);
        bonusEditText = (EditText) view.findViewById(R.id.bonusEditText);
        discEditText = (EditText) view.findViewById(R.id.lineDiscText);
        taxEditText = (EditText) view.findViewById(R.id.taxPercentText);
        discTypeRadioGroup = (RadioGroup) view.findViewById(R.id.discTypeRadioGroup);
        addToListButton.setOnClickListener(onClickListener);
        doneButton.setOnClickListener(onClickListener);

        return view;
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.addToListButton:

                    Boolean check = check_Discount();
                    if (!check)
                        Toast.makeText(getActivity(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();

                    else {
                        item = new Item();
                        item.setItemNo(itemNoEditText.getText().toString());
                        if (discEditText.getText().toString() == "")
                            item.setItemName("0");
                        else
                            item.setItemName(descEditText.getText().toString());
                        item.setTax(Float.parseFloat(taxEditText.getText().toString().trim()));

                        try {

                            item.setQty(Float.parseFloat(decimalFormat.format(Float.parseFloat(qtyEditText.getText().toString().trim()))));
                            item.setPrice(Float.parseFloat(priceEditText.getText().toString().trim()));
                            if (bonusEditText.getText().toString() == "")
                                item.setBonus(Float.parseFloat("0.0"));
                            else
                                item.setBonus(Float.parseFloat(bonusEditText.getText().toString()));
                            item.setTax(Float.parseFloat(taxEditText.getText().toString().trim()));
                        } catch (NumberFormatException e) {
                            item.setQty(0);
                            item.setPrice(0);
                            item.setBonus(0);
                            item.setDisc(0);
                            item.setDiscPerc("0%");
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
                                item.setDisc(Float.parseFloat(discEditText.getText().toString().trim()));
                                item.setDiscPerc((item.getQty() * item.getPrice() *
                                        (Float.parseFloat(discEditText.getText().toString().trim()) / 100)) + "%");

                            } else {
                                item.setDiscPerc(Float.parseFloat(discEditText.getText().toString().trim()) + "%");
                                item.setDisc(item.getQty() * item.getPrice() *
                                        (Float.parseFloat(discEditText.getText().toString().trim())) / 100);
                            }
                            descPerc = ((item.getQty() * item.getPrice() *
                                    (Float.parseFloat(discEditText.getText().toString().trim()) / 100)));
                        } catch (NumberFormatException e) {
                            item.setDisc(0);
                            item.setDiscPerc("0%");
                        }

                        Log.e("setDisc1",""+item.getDisc());
                        try {
                            if (item.getDiscType() == 0) {
                                item.setAmount(item.getQty() * item.getPrice() - item.getDisc());
//                                Toast.makeText(getActivity(),"value", Toast.LENGTH_LONG).show();
                            } else {
                                item.setAmount(item.getQty() * item.getPrice() - descPerc);
//                                Toast.makeText(getActivity(),"percent", Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException e) {
                            item.setAmount(0);
                        }


                        if ((!item.getItemName().equals("")) && item.getAmount() > 0) {
                            itemsList.add(item);
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.app_item_add), Toast.LENGTH_LONG).show();
                            clearLayout();
                        } else {
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.app_fail_to_add_item), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case R.id.doneButton:
                    listener.addItemsToList(itemsList);
                    AddItemsFragment.this.dismiss();
                    break;
            }
        }
    };

    public void setListener(AddItemsInterface listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    private void clearLayout() {
        itemNoEditText.setText("");
        //descEditText.setText("");
        qtyEditText.setText("");
        priceEditText.setText("");
        bonusEditText.setText("");
        discEditText.setText("");
        itemNoEditText.requestFocus();
    }

    private Boolean check_Discount() {
        Boolean check = true;
        if (qtyEditText.getText().toString().equals(""))
            qtyEditText.setText("0");

        if (priceEditText.getText().toString().equals(""))
            priceEditText.setText("0");

        if (discEditText.getText().toString().equals(""))
            discEditText.setText("0");

        if (bonusEditText.getText().toString().equals(""))
            bonusEditText.setText("0");

        Float totalValue = (Float.parseFloat(qtyEditText.getText().toString())) * (Float.parseFloat(priceEditText.getText().toString()));
        Float discount = Float.parseFloat(discEditText.getText().toString());
        int radioId = discTypeRadioGroup.getCheckedRadioButtonId();

        if (radioId == R.id.discValueRadioButton) {
            if (discount > totalValue)
                return false;
        } else {
            if (discount > 100)
                return false;
        }

        return check;
    }

}
