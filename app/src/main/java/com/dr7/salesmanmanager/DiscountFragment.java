package com.dr7.salesmanmanager;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountFragment extends DialogFragment {


    public interface DiscountInterface {
        public void addDiscount(double discount, int iDiscType);
    }

    private Button okButton, cancelButton;
    private RadioGroup discTypeRadioGroup;
    private RadioButton discPercent;
    private EditText discValueEditText;
    private DiscountInterface discountInterface;
    private static double discountValue = 0, discountPerc = 0;
    public int discType;
    public double invoiceTotal;
    private DecimalFormat decimalFormat;
    DatabaseHandler databaseHandler;
    SalesInvoice sal;

    public DiscountFragment() {
        // Required empty public constructor
    }

    public void setListener(DiscountInterface listener) {
        this.discountInterface = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.discountInterface = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragmen
                View view = inflater.inflate(R.layout.fragment_discount, container, false);
                okButton = (Button) view.findViewById(R.id.okButton);
                cancelButton = (Button) view.findViewById(R.id.cancelButton);
                discTypeRadioGroup = (RadioGroup) view.findViewById(R.id.discTypeRadioGroup);
                discPercent = (RadioButton) view.findViewById(R.id.percentRadioButton);
                discValueEditText = (EditText) view.findViewById(R.id.discEditText);
                decimalFormat = new DecimalFormat("##.000");

                DatabaseHandler mdHandler = new DatabaseHandler(getActivity());
                if (mdHandler.getAllSettings().get(0).getTaxClarcKind() == 1) {
                    discPercent.setEnabled(false);
                }

                OnClickListener onClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.okButton:
                                Boolean check = checkDiscount();
                                if (!check) {
                                    Toast.makeText(getActivity(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        if (discTypeRadioGroup.getCheckedRadioButtonId() == R.id.percentRadioButton)
                                            discType = 1;
                                        else
                                            discType = 0;

                                        if (discType == 1) {
                                            discountPerc = Double.parseDouble(discValueEditText.getText().toString().trim());
                                            discountValue = invoiceTotal * discountPerc * 0.01;

                                        } else {
                                            discountValue = Double.parseDouble(discValueEditText.getText().toString().trim());
                                            discountPerc = invoiceTotal * discountValue;

                                        }

                                        //discountValue = Float.parseFloat(decimalFormat.format(discountValue));

                                    } catch (NumberFormatException e) {
                                        Toast.makeText(getActivity(), "NumberFormatException", Toast.LENGTH_LONG).show();
                                        discountValue = 0;
                                    }
                                    if (discountValue >= 0) {
                                        discountInterface.addDiscount(discountValue, discType);
                                    } else {
                                        discountInterface.addDiscount(discountValue, discType);
                                    }

                                    DiscountFragment.this.dismiss();

                                }
                                break;
                            case R.id.cancelButton:
                                DiscountFragment.this.dismiss();
                                break;
                        }
                    }
                };

                okButton.setOnClickListener(onClickListener);
                cancelButton.setOnClickListener(onClickListener);

                return view;
            }




    private Boolean checkDiscount() {
        int radioId = discTypeRadioGroup.getCheckedRadioButtonId();

        if(discValueEditText.getText().toString().equals(""))
            return false;
        else {
            if (radioId == R.id.percentRadioButton) {
                if (Float.parseFloat(discValueEditText.getText().toString()) > 100)
                    return false;
                else
                    return true;
            } else {
                if (Float.parseFloat(discValueEditText.getText().toString()) > invoiceTotal)
                    return false;
                else
                    return true;
            }
        }
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public double getDiscountPerc() {
        return discountPerc;
    }
}
