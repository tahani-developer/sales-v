package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohd darras on 15/04/2018.
 */

public class TestAdapter extends BaseAdapter {
    private List<Item> items;
    private ArrayList<Integer> isClicked = new ArrayList<>();
    private List<Item> filterList;
    private Context context;
    boolean added = false;

    public TestAdapter(List<Item> items, Context context )
    {
        this.items = items;
        this.filterList = items;
        this.context = context;
        for (int i = 0; i <= items.size(); i++) {
            isClicked.add(0);
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        LinearLayout linearLayout;
        CardView cardView;
        TextView itemNumber, itemName, tradeMark, category, unitQty, price, tax, barcode;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.item_horizontal_listview,null);

        holder.cardView = view.findViewById(R.id.cardView);
        holder.linearLayout = view.findViewById(R.id.linear);
        holder.itemNumber = view.findViewById(R.id.textViewItemNumber);
        holder.itemName = view.findViewById(R.id.textViewItemName);
        holder.tradeMark = view.findViewById(R.id.textViewTradeMark);
        holder.category = view.findViewById(R.id.textViewCategory);
        holder.unitQty = view.findViewById(R.id.textViewUnit_qty);
        holder.price = view.findViewById(R.id.textViewPrice);
        holder.tax = view.findViewById(R.id.textViewTax);
        holder.barcode = view.findViewById(R.id.textViewBarcode);

        holder.itemNumber.setText(""+items.get(i).getItemNo());
        holder.itemName.setText(""+items.get(i).getItemName());
        holder.tradeMark.setText(""+items.get(i).getItemName());
        holder.category.setText("" + items.get(i).getCategory());
        holder.unitQty.setText("" + items.get(i).getQty());
        holder.price.setText("" + items.get(i).getPrice());
        holder.tax.setText("" + items.get(i).getTaxPercent());
        holder.barcode.setText(""+items.get(i).getBarcode());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_item_dialog_small);

                final TextView itemNumber = (TextView) dialog.findViewById(R.id.item_number);
                final TextView itemName = (TextView) dialog.findViewById(R.id.item_name);
                final EditText price = (EditText) dialog.findViewById(R.id.price);
                final Spinner unit = (Spinner) dialog.findViewById(R.id.unit);
                final EditText unitQty = (EditText) dialog.findViewById(R.id.unitQty);
                final EditText unitWeight = (EditText) dialog.findViewById(R.id.unitWeight);
                final CheckBox useWeight = (CheckBox) dialog.findViewById(R.id.use_weight);
                final EditText bonus = (EditText) dialog.findViewById(R.id.bonus);
                final EditText discount = (EditText) dialog.findViewById(R.id.discount);
                final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.discTypeRadioGroup);
                final LinearLayout discountLinearLayout = (LinearLayout) dialog.findViewById(R.id.discount_linear);
                final LinearLayout unitWeightLinearLayout = (LinearLayout) dialog.findViewById(R.id.linearWeight);
                Button addToList = (Button) dialog.findViewById(R.id.addToList);

                itemNumber.setText(items.get(i).getItemNo());
                itemName.setText(items.get(i).getItemName());
                price.setText("" + items.get(i).getPrice());

                final DatabaseHandler mHandler = new DatabaseHandler(context);

                if (mHandler.getAllSettings().get(0).getTaxClarcKind() == 1)
                    discountLinearLayout.setVisibility(View.INVISIBLE);

                if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                    unitWeightLinearLayout.setVisibility(View.INVISIBLE);
                    useWeight.setChecked(false);
                }
                else
                    unitQty.setText("" + items.get(i).getItemL());

                List<String> units = mHandler.getAllexistingUnits(itemNumber.getText().toString());

                ArrayAdapter<String> unitsList = new ArrayAdapter<String>(context, R.layout.spinner_style, units);
                unit.setAdapter(unitsList);

                addToList.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {

                        if (!price.getText().toString().equals("") && !price.getText().toString().equals("0")) {

                            Boolean check = check_Discount(unitWeight, unitQty, price, bonus, discount, radioGroup);
                            if (!check)
                                Toast.makeText(view.getContext(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                            else {

                                String unitValue;
                                if (mHandler.getAllSettings().get(0).getUseWeightCase() == 0) {
                                    unitValue = unit.getSelectedItem().toString();

                                    if (items.get(i).getQty() >= Double.parseDouble(unitQty.getText().toString())
                                            || mHandler.getAllSettings().get(0).getAllowMinus() == 1) {

                                        AddItemsFragment2 obj = new AddItemsFragment2();
                                        added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                holder.tax.getText().toString(), unitValue, unitQty.getText().toString(), price.getText().toString(),
                                                bonus.getText().toString(), discount.getText().toString(), radioGroup, useWeight, view.getContext());

                                        if (added) {
                                            holder.linearLayout.setBackgroundColor(R.color.done_button);
                                            isClicked.set(i, 1);
                                        }
                                    } else
                                        Toast.makeText(view.getContext(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                } else {
                                    if (unitWeight.getText().toString() == "")
                                        Toast.makeText(view.getContext(), "please enter unit weight", Toast.LENGTH_LONG).show();
                                    else {
                                        unitValue = unitWeight.getText().toString();
                                        String qty = (useWeight.isChecked() ? "" + (Double.parseDouble(unitQty.getText().toString()) * Double.parseDouble(unitValue)) : unitQty.getText().toString());

                                        Log.e("here**" , ""+ i);
                                        if(i> -1) {
                                            if (items.get(i).getQty() >= Double.parseDouble(qty)
                                                    || mHandler.getAllSettings().get(0).getAllowMinus() == 1) {

                                                AddItemsFragment2 obj = new AddItemsFragment2();
                                                added = obj.addItem(itemNumber.getText().toString(), itemName.getText().toString(),
                                                        holder.tax.getText().toString(), unitValue, qty, price.getText().toString(),
                                                        bonus.getText().toString(), discount.getText().toString(), radioGroup, useWeight, view.getContext());

                                                if (added) {
                                                    holder.linearLayout.setBackgroundColor(R.color.done_button);
                                                    isClicked.set(i, 1);
                                                }
                                            } else
                                                Toast.makeText(view.getContext(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(view.getContext(), "Please enter the item again", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }

                            dialog.dismiss();
                        } else
                            Toast.makeText(view.getContext(), "Invalid price", Toast.LENGTH_LONG).show();


                    }
                });
                dialog.show();

            }
        });

        return view;
    }

    private Boolean check_Discount(EditText unitEditText, EditText qtyEditText, TextView priceEditText,
                                   EditText bonusEditText, EditText discEditText, RadioGroup discTypeRadioGroup) {
        Boolean check = true;

        if (unitEditText.getText().toString().equals(""))
            unitEditText.setText("0");
        else
            unitEditText.setText(convertToEnglish(unitEditText.getText().toString()));

        if (qtyEditText.getText().toString().equals(""))
            qtyEditText.setText("0");
        else
            qtyEditText.setText(convertToEnglish(qtyEditText.getText().toString()));

        if (priceEditText.getText().toString().equals(""))
            priceEditText.setText("0");
        else
            priceEditText.setText(convertToEnglish(priceEditText.getText().toString()));

        if (discEditText.getText().toString().equals(""))
            discEditText.setText("0");
        else
            discEditText.setText(convertToEnglish(discEditText.getText().toString()));

        if (bonusEditText.getText().toString().equals(""))
            bonusEditText.setText("0");
        else
            bonusEditText.setText(convertToEnglish(bonusEditText.getText().toString()));

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
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }
}
