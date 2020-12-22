package com.dr7.salesmanmanager;



import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.dr7.salesmanmanager.Reports.Reports;

import java.text.DecimalFormat;
import java.util.Timer;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.SalesInvoice.discountRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountFragment extends DialogFragment {


    public interface DiscountInterface {
        public void addDiscount(double discount, int iDiscType);
    }

    private Button okButton;
    ImageView  cancelButton;
    private RadioGroup discTypeRadioGroup;
    private RadioButton discPercent;
    private EditText discValueEditText,noteEditText;
    public ImageView requestDiscount;
    public  static TextView checkState;
    private DiscountInterface discountInterface;
    private static double discountValue = 0 , discountPerc = 0;
    public int discType;
    public double invoiceTotal;
    private DecimalFormat decimalFormat;
    DatabaseHandler mdHandler;
    SalesInvoice sal;
    Context main_context;
    public  static  String noteRequest="";
    requestAdmin request;
    TextView checkStateResult;
    ImageView rejectDiscount,acceptDiscount,defaultDiscount;
    LinearLayout requestLinear;
    public   Timer timer;
    TimerTask task;
    public  static  boolean stateZero=false;
    LinearLayout mainRequestLinear;


    public DiscountFragment(Context context) {
        this.main_context=context;

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
         request=new requestAdmin(main_context);
         mdHandler = new DatabaseHandler(getActivity());
        // Inflate the layout for this fragmen
//        new LocaleAppUtils().changeLayot(Disc);
                View view = inflater.inflate(R.layout.fragment_discount, container, false);
        okButton = (Button) view.findViewById(R.id.okButton);
        cancelButton = (ImageView) view.findViewById(R.id.cancelButton);
        checkState=view.findViewById(R.id.checkState);
        mainRequestLinear=view.findViewById(R.id.mainRequestLinear);
        checkStateResult=view.findViewById(R.id.checkStateResult);
        defaultDiscount=view.findViewById(R.id.defaultDiscount);
        acceptDiscount=view.findViewById(R.id.acceptDiscount);
        rejectDiscount=view.findViewById(R.id.rejectDiscount);
        requestLinear=view.findViewById(R.id.requestLinear);
        checkState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                Log.e("afterTextChanged",""+s.toString());
                if(s.toString().equals("0"))
                {
//                    checkState.setText("3");

//                    checkStatuseRequest();
                }
                else
                    if(s.toString().equals("1"))
                {
                    requestLinear.setVisibility(View.GONE);
                    checkStateResult.setText(main_context.getResources().getString(R.string.acceptedRequest));
                    acceptDiscount.setVisibility(View.VISIBLE);
                    defaultDiscount.setVisibility(View.GONE);
                    okButton.setVisibility(View.VISIBLE);
                    okButton.setEnabled(true);

                }
                else if(s.toString().equals("2"))
                {
                    requestLinear.setVisibility(View.GONE);
                    checkStateResult.setText(main_context.getResources().getString(R.string.rejectedRequest));
                    acceptDiscount.setVisibility(View.GONE);
                    defaultDiscount.setVisibility(View.GONE);
                    rejectDiscount.setVisibility(View.VISIBLE);
                    discValueEditText.setText("");
                    noteEditText.setText("");

                }

            }
        });
        discTypeRadioGroup = (RadioGroup) view.findViewById(R.id.discTypeRadioGroup);
        discPercent = (RadioButton) view.findViewById(R.id.percentRadioButton);
        discValueEditText = (EditText) view.findViewById(R.id.discEditText);
        noteEditText=(EditText) view.findViewById(R.id.noteEditText);
        requestDiscount = view.findViewById(R.id.requestDiscount);
        if(mdHandler.getAllSettings().size()!=0)
        {
            if(mdHandler.getAllSettings().get(0).getApproveAdmin()==0)
            {
                mainRequestLinear.setVisibility(View.GONE);
                okButton.setVisibility(View.VISIBLE);
            }

        }
        requestDiscount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!discValueEditText.getText().toString().equals(""))
                {
                    Boolean check = checkDiscount();
                    if (!check) {
                        discValueEditText.setError("Not Valid");
                        Toast.makeText(getActivity(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            okButton.setEnabled(false);
                            discValueEditText.setEnabled(false);
                            noteEditText.setEnabled(false);
                            noteRequest=noteEditText.getText().toString();
                            discountRequest.setAmount_value(discValueEditText.getText().toString());

                            request.startParsing();
//
                        } catch (Exception e) {
                            Log.e("request",""+e.getMessage());

                        }



//            DiscountFragment.this.dismiss();

                    }


                }
                else {
                    discValueEditText.setError("required");
                }



            }
        });
        decimalFormat = new DecimalFormat("##.000");


        if (mdHandler.getAllSettings().get(0).getTaxClarcKind() == 1) {
            discPercent.setEnabled(false);
        }

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.okButton:
                        Log.e("okButton","okButton");
                               addDiscount();
                                break;
                            case R.id.cancelButton:
//;
                                DiscountFragment.this.dismiss();
                                break;
                        }
                    }
                };

                okButton.setOnClickListener(onClickListener);
                cancelButton.setOnClickListener(onClickListener);

                return view;
            }
    void stopTimer() {
        Log.e("stopTimer","stopTimer");
        task=new TimerTask(main_context);
        task.stopTimer();
    }


    private void addDiscount() {
//        Boolean check = checkDiscount();
//        if (!check) {
//            Toast.makeText(getActivity(), "Invalid Discount Value please Enter a valid Discount", Toast.LENGTH_LONG).show();
//        } else {
            try {
                Log.e("okButton","addDiscount");
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

//        }
    }

    private void showMessage(int flag) {
        if(flag==1)
        {
            new SweetAlertDialog(main_context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(main_context.getResources().getString(R.string.succsesful))
                    .setContentText(main_context.getResources().getString(R.string.acceptedRequest))
                    .show();
        }
        else if(flag==2)
        {
            new SweetAlertDialog(main_context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(main_context.getResources().getString(R.string.sorry))
                    .setContentText(main_context.getResources().getString(R.string.rejectedRequest))
                    .show();
        }

    }

    public void checkStatuseRequest() {
        if(stateZero)
        {
            Log.e("checkStatuseRequest","first"+checkState.getText().toString());
            task=new TimerTask(main_context);
            task.startTimer();
        }


//        timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//
//                    request.checkRequestState();
//
//
//                }
//
//            }, 0, 5000);




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

    public  static double getDiscountValue() {
        return discountValue;
    }

    public static double getDiscountPerc() {
        return discountPerc;
    }
}
