package com.dr7.salesmanmanager;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Item;

import java.util.List;

public class Update_Items extends DialogFragment {


    private Button updateList;
    private List<Item> itemsList;
    private Item item;
    private Spinner unit ;
    private EditText itemNo, desc,qty, price, bonus, disc, tax, netQty;
    private RadioGroup discType;
    private RadioButton discValueRadioButton , discPercRadioButton ;
    private float discPerc, discValue;
    static String rowToBeUpdated[];


    public Update_Items.Update_Items_interface getListener() {
        return listener;
    }

    public interface Update_Items_interface {
        public void updateItemInList(Item item);

        void Update_Items();
    }

    private Update_Items.Update_Items_interface listener;

    public Update_Items() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(getResources().getString(R.string.app_update_items));
        final View view = inflater.inflate(R.layout.activity_update__items, container, false);

        itemNo = (EditText) view.findViewById(R.id.itemCode);
        desc = (EditText) view.findViewById(R.id.itemDesc);
        qty = (EditText) view.findViewById(R.id.qty);
        unit = (Spinner) view.findViewById(R.id.unit);
        netQty = (EditText) view.findViewById(R.id.netQty);
        price = (EditText) view.findViewById(R.id.price);
        bonus = (EditText) view.findViewById(R.id.bonus);
        disc = (EditText) view.findViewById(R.id.lineDisc);
        discType = (RadioGroup) view.findViewById(R.id.discType);
        discValueRadioButton = (RadioButton) view.findViewById(R.id.discValueRadioButton);
        discPercRadioButton = (RadioButton) view.findViewById(R.id.discPercRadioButton);
        tax = (EditText)view.findViewById(R.id.taxPercent);

        updateList = (Button) view.findViewById(R.id.updateItems);

        SalesInvoice obj = new SalesInvoice();
        rowToBeUpdated = obj.getIndexToBeUpdated();

        double net_qty = Double.parseDouble(rowToBeUpdated[2]) * Double.parseDouble(rowToBeUpdated[7]) ;

        DatabaseHandler mHandler = new DatabaseHandler(getActivity());
        List<String> units = mHandler.getAllexistingUnits();
        units.add(0,rowToBeUpdated[7]);
        
        ArrayAdapter<String> unitsList = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, units);
        unit.setAdapter(unitsList);

        itemNo.setText(rowToBeUpdated[0]);
        desc.setText(rowToBeUpdated[1]);
        qty.setText(rowToBeUpdated[2]);
        price.setText(rowToBeUpdated[3]);
        bonus.setText(rowToBeUpdated[4]);
        disc.setText(rowToBeUpdated[5]);
        netQty.setText(""+net_qty);

        if (rowToBeUpdated[6].equals("0")) {
            discValueRadioButton.setChecked(true);
        } else {
            discPercRadioButton.setChecked(true);
        }
        updateList.setOnClickListener(onClickListener);

        return view;
    }


    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.updateItems:

                    Boolean check = check_Discount();
                    if (!check)
                        Toast.makeText(getActivity(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();

                    else {
                        item = new Item();
                        item.setItemNo(itemNo.getText().toString());
                        if (disc.getText().toString() == "")
                            item.setItemName("0");
                        else
                            item.setItemName(desc.getText().toString());
                        item.setTax(Float.parseFloat(tax.getText().toString().trim()));

                        try {
                            item.setQty(Float.parseFloat(qty.getText().toString().trim()));
                            item.setUnit(unit.getSelectedItem().toString().trim());
                            item.setPrice(Float.parseFloat(price.getText().toString().trim()));
                            if (bonus.getText().toString() == "")
                                item.setBonus(Float.parseFloat("0.0"));
                            else
                                item.setBonus(Float.parseFloat(bonus.getText().toString()));
                            item.setTax(Float.parseFloat(tax.getText().toString().trim()));
                        } catch (NumberFormatException e) {
                            item.setQty(0);
                            item.setUnit("");
                            item.setPrice(0);
                            item.setBonus(0);
                            item.setDisc(0);
                            item.setDiscPerc("0%");
                            item.setAmount(0);
                            Log.e("Add new item error", e.getMessage().toString());
                        }


                        if (discType.getCheckedRadioButtonId() == R.id.discPercRadioButton) {
                            item.setDiscType(1);
                        } else {
                            item.setDiscType(0);
                        }

                        try {
                            if (item.getDiscType() == 0) {
                                item.setDisc(Float.parseFloat(disc.getText().toString().trim()));
                                item.setDiscPerc((item.getQty() * item.getPrice() *
                                        (Float.parseFloat(disc.getText().toString().trim()) / 100)) + "%");

                            } else {
                                item.setDiscPerc(Float.parseFloat(disc.getText().toString().trim()) + "%");
                                item.setDisc(item.getQty() * item.getPrice() *
                                        (Float.parseFloat(disc.getText().toString().trim())) / 100);
                            }
                            discPerc = ((item.getQty() * item.getPrice() *
                                    (Float.parseFloat(disc.getText().toString().trim()) / 100)));
                        } catch (NumberFormatException e) {
                            item.setDisc(0);
                            item.setDiscPerc("0%");
                        }

                        try {
                            if (item.getDiscType() == 0) {
                                item.setAmount(item.getQty() * item.getPrice() - item.getDisc());
//                                Toast.makeText(getActivity(),"value", Toast.LENGTH_LONG).show();
                            } else {
                                item.setAmount(item.getQty() * item.getPrice() - discPerc);
//                                Toast.makeText(getActivity(),"percent", Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException e) {
                            item.setAmount(0);
                        }


                        if ((!item.getItemName().equals("")) && item.getAmount() > 0) {
                            listener.updateItemInList(item);
                            Update_Items.this.dismiss();
                            Toast.makeText(getActivity(),
                                    "Updated Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.app_fail_to_add_item), Toast.LENGTH_LONG).show();
                        }
                    }


                    break;
            }
        }
    };


    public void setListener(Update_Items.Update_Items_interface listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    private Boolean check_Discount() {
        Boolean check = true;

        if (qty.getText().toString().equals(""))
            qty.setText("0");

        if (price.getText().toString().equals(""))
            price.setText("0");

        if (disc.getText().toString().equals(""))
            disc.setText("0");

        if (bonus.getText().toString().equals(""))
            bonus.setText("0");

        Float totalValue = (Float.parseFloat(qty.getText().toString())) * (Float.parseFloat(price.getText().toString()));
        Float discount = Float.parseFloat(disc.getText().toString());
        int radioId = discType.getCheckedRadioButtonId();

        if (radioId == R.id.discType) {
            if (discount > totalValue)
                return false;
        } else {
            if (discount > 100)
                return false;
        }

        return check;
    }
}
