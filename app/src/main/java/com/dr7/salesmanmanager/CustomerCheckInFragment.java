package com.dr7.salesmanmanager;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dr7.salesmanmanager.Modles.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCheckInFragment extends DialogFragment
{
    private static final String TAG = "CustomerCheckInFragment";
    static TextView customerNameTextView, Customer_Name;
    ImageButton findButton;
    Button cancelButton, okButton;
    static TextView Customer_Account;
    public static String customernametest;
    public static int checkOutIn;

    static String cusCode;
    String cusName;
    int status;

    private static DatabaseHandler mDbHandler;


    public interface CustomerCheckInInterface {
        public void showCustomersList();

        public void displayCustomerListShow();
    }

    CustomerCheckInInterface customerCheckInListener;

    public CustomerCheckInFragment() {
        // Required empty public constructor
    }

    public static void settext1() {
        Customer_Name.setText(CustomerListShow.Customer_Name.toString());
        Customer_Account.setText(CustomerListShow.Customer_Account.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_check_in, container, false);
        //selectButton = (ImageButton) view.findViewById(R.id.check_img_button);
        //checkButton = (ImageButton) view.findViewById(R.id.check_img_button);
        okButton = (Button) view.findViewById(R.id.okButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        findButton = (ImageButton) view.findViewById(R.id.find_img_button);
        Customer_Name = (TextView) view.findViewById(R.id.checkInCustomerName);
        Customer_Account = (TextView) view.findViewById(R.id.customerAccountNo);

        customernametest = CustomerListShow.Customer_Name.toString();


        mDbHandler = new DatabaseHandler(getActivity());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.settext2();

                cusCode = Customer_Account.getText().toString();
                cusName = Customer_Name.getText().toString();
                status = 0;

                Date currentTimeAndDate = Calendar.getInstance().getTime();
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                String currentTime = tf.format(currentTimeAndDate);
                String currentDate = df.format(currentTimeAndDate);

                int salesMan = Integer.parseInt(Login.salesMan);

                mDbHandler.addTransaction(new Transaction(salesMan, cusCode, cusName, currentDate, currentTime,
                        "Not Yet", "Not Yet", 0));

                MainActivity.checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_in_black));
                MainActivity.checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cus_check_out));
                dismiss();
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.find_img_button:
                        customerCheckInListener.displayCustomerListShow();
                        break;
                }

            }
        };

        findButton.setOnClickListener(onClickListener);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerListShow.Customer_Name = customernametest;
                MainActivity mainActivity = new MainActivity();
                mainActivity.menuItemState = 0;
                dismiss();
            }
        });


        return view;
    }

    public void setListener(CustomerCheckInInterface listener) {
        this.customerCheckInListener = listener;
    }

    public void editCheckOutTimeAndDate() {
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String currentTime = tf.format(currentTimeAndDate);
        String currentDate = df.format(currentTimeAndDate);

        mDbHandler.updateTransaction(cusCode, currentDate ,currentTime);

    }

}
