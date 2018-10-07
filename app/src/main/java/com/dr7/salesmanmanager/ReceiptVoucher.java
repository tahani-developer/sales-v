package com.dr7.salesmanmanager;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Payment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptVoucher extends Fragment {

    private static DatabaseHandler mDbHandler;
    private int voucherNumber;

    int position = 0;
    public List<Payment> payments;

    private LinearLayout chequeLayout;
    private ScrollView scrollView;
    private Spinner paymentKindSpinner;
    private ImageButton custInfoImgButton, clearImgButton, saveData;
    private Button addCheckButton;

    private double total = 0.0;

    private EditText amountEditText, remarkEditText;
    private TableLayout tableCheckData;
    Calendar myCalendar;

    private TextView chequeTotal;

    public static TextView customername;


   /* public static void test3(){
        customername.setText(CustomerListFragment.Customer_Name.toString());

    }*/

    public interface ReceiptInterFace {
        public void displayCustInfoFragment();
    }

    ReceiptInterFace receiptInterFace;

    public ReceiptVoucher() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_receipt_voucher, container, false);


        mDbHandler = new DatabaseHandler(getActivity());
        payments = new ArrayList<Payment>();

        chequeLayout = (LinearLayout) view.findViewById(R.id.cheques_totals);
        paymentKindSpinner = (Spinner) view.findViewById(R.id.paymentTypeSpinner);
        custInfoImgButton = (ImageButton) view.findViewById(R.id.custInfoImgBtn);
        saveData = (ImageButton) view.findViewById(R.id.SaveData);
        clearImgButton = (ImageButton) view.findViewById(R.id.ClearForm);
        scrollView = (ScrollView) view.findViewById(R.id.chequesScroll);
        chequeTotal = (TextView) view.findViewById(R.id.chequesTotalsEditText);
        amountEditText = (EditText) view.findViewById(R.id.amountEditText);
        remarkEditText = (EditText) view.findViewById(R.id.remarkEditText);
        customername = (TextView) view.findViewById(R.id.customer_nameVoucher);
        addCheckButton = (Button) view.findViewById(R.id.addCheck);

        tableCheckData = (TableLayout) view.findViewById(R.id.TableCheckData);

        addCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TableRow row = new TableRow(getActivity());
                row.setPadding(5, 5, 5, 5);


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.check_info_dialog);
                Window window = dialog.getWindow();
                window.setLayout(700, 250);

                final EditText chNum = (EditText) dialog.findViewById(R.id.editText1);
                final EditText bank = (EditText) dialog.findViewById(R.id.editText2);
                final EditText chDate = (EditText) dialog.findViewById(R.id.editText3);
                final EditText chValue = (EditText) dialog.findViewById(R.id.editText4);

                Button okButton = (Button) dialog.findViewById(R.id.button1);
                Button cancelButton = (Button) dialog.findViewById(R.id.button2);

                myCalendar = Calendar.getInstance();
                chDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(getActivity(), openDatePickerDialog(chDate), myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkDialogFields(chNum.getText().toString(), bank.getText().toString(),
                                chDate.getText().toString(), chValue.getText().toString())) {

                            if (checkTotal(chValue.getText().toString())) {
                                total = total + Double.parseDouble(chValue.getText().toString());
                                chequeTotal.setText(total + "");

                                Payment check = new Payment();
                                check.setCheckNumber(Integer.parseInt(chNum.getText().toString()));
                                check.setBank(bank.getText().toString());
                                check.setDueDate(chDate.getText().toString());
                                check.setAmount(Integer.parseInt(chValue.getText().toString()));
                                payments.add(check);

                                for (int i = 0; i < 4; i++) {

                                    String[] record = {chNum.getText().toString(), bank.getText().toString(),
                                            chDate.getText().toString(), chValue.getText().toString()};

                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                    row.setLayoutParams(lp);

                                    TextView textView = new TextView(getActivity());

                                    textView.setHint(record[i]);
                                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_view_color));
                                    textView.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.layer4));
                                    textView.setGravity(Gravity.CENTER);

                                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                                    textView.setLayoutParams(lp2);

                                    row.addView(textView);


                                }
                                row.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                tableCheckData.removeView(row);
                                                payments.remove(position);
                                                total = total - Double.parseDouble(chValue.getText().toString());
                                                chequeTotal.setText(total + "");
                                            }
                                        });

                                        builder.setNegativeButton(getResources().getString(R.string.app_no), null);
                                        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear_item));
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();

                                        return true;
                                    }
                                });
                                tableCheckData.addView(row);

                                position++;
                                dialog.dismiss();
                            } else
                                Toast.makeText(getActivity(), "Cheque Total is greater than Amount value", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), "Please Enter all values", Toast.LENGTH_SHORT).show();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            } // end of button preview

        });

        if (MainActivity.checknum == 1)
            customername.setText(CustomerListShow.Customer_Name.toString());
        else
            customername.setText("Customer Name");


        ArrayList<String> kinds = new ArrayList<>();
        kinds.add("Cash");
        kinds.add("Cheque");

        ArrayAdapter<String> paymentKind = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, kinds);
        paymentKindSpinner.setAdapter(paymentKind);


        clearImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearForm();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });


        voucherNumber = mDbHandler.getMaxSerialNumber(0) + 1;

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_save));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));

                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int l) {

                        String s = amountEditText.getText().toString();
                        String spinner = paymentKindSpinner.getSelectedItem().toString();
                        if (spinner == "Cash") {
                            if (s.isEmpty() || s == "0")
                                Toast.makeText(getActivity(), "Please Enter amount value", Toast.LENGTH_LONG).show();

                            else {
                                Toast.makeText(getActivity(), "Amount Saved***", Toast.LENGTH_LONG).show();

                                Date currentTimeAndDate = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String payDate = df.format(currentTimeAndDate);

                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
                                String paymentYear = df2.format(currentTimeAndDate);

                                int cusNumber = Integer.parseInt(CustomerListShow.Customer_Account);
                                String cusName = CustomerListShow.Customer_Name;
                                Double amount = Double.parseDouble(amountEditText.getText().toString());
                                String remark = remarkEditText.getText().toString();

                                int salesMan = Integer.parseInt(Login.salesMan);

                                mDbHandler.addPayment(new Payment(0, voucherNumber, salesMan, payDate,
                                        remark, amount, 0, cusNumber, cusName, 0 , Integer.parseInt(paymentYear)));

                                mDbHandler.setMaxSerialNumber(0, voucherNumber);
                            }
                        } else if (spinner == "Cheque") {
                            if (!checkValue())
                                Toast.makeText(getActivity(), "Amount Value not matches Cheque Total", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getActivity(), "Amount Saved", Toast.LENGTH_LONG).show();

                                Date currentTimeAndDate = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String payDate = df.format(currentTimeAndDate);

                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
                                String paymentYear = df2.format(currentTimeAndDate);

                                int cusNumber = Integer.parseInt(CustomerListShow.Customer_Account);
                                String cusName = CustomerListShow.Customer_Name;
                                Double amount = Double.parseDouble(amountEditText.getText().toString());
                                String remark = remarkEditText.getText().toString();

                                int salesMan = Integer.parseInt(Login.salesMan);

                                mDbHandler.addPayment(new Payment(0, voucherNumber, salesMan,
                                        payDate, remark, amount, 0, cusNumber, cusName, 1 , Integer.parseInt(paymentYear)));

                                for (int i = 0; i < payments.size(); i++) {
                                    mDbHandler.addPaymentPaper(new Payment(0, voucherNumber, payments.get(i).getCheckNumber(),
                                            payments.get(i).getBank(),  payments.get(i).getDueDate(), payments.get(i).getAmount(),
                                            0, Integer.parseInt(paymentYear)));

                                    mDbHandler.setMaxSerialNumber(4, voucherNumber);
                                }
                            }
                        }
                        clearForm();

                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.custInfoImgBtn:
                        receiptInterFace.displayCustInfoFragment();
                        break;
                }
            }
        };

        custInfoImgButton.setOnClickListener(onClickListener);

        paymentKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    chequeLayout.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.INVISIBLE);
                    addCheckButton.setVisibility(View.INVISIBLE);
                    tableCheckData.setVisibility(View.INVISIBLE);
                    voucherNumber = mDbHandler.getMaxSerialNumber(0) + 1;
                } else {
                    chequeLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    addCheckButton.setVisibility(View.VISIBLE);
                    tableCheckData.setVisibility(View.VISIBLE);
                    voucherNumber = mDbHandler.getMaxSerialNumber(4) + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    public void clearForm() {
        tableCheckData.removeAllViews();
        amountEditText.setText("");
        remarkEditText.setText("");
        paymentKindSpinner.setSelection(0);
        chequeTotal.setText("0.00");
    }


    private Boolean checkTotal(String s) {
        if (!amountEditText.getText().toString().equals("")) {
            if (total + Double.parseDouble(s) > Double.parseDouble(amountEditText.getText().toString())) {
                return false;
            }
            return true;
        }
        return false;
    }

    private Boolean checkValue() {
        if (total != Double.parseDouble(amountEditText.getText().toString())) {
            return false;
        }
        return true;
    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final EditText editText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };
        return date;
    }


    private void updateLabel(EditText editText) {
        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

    }

    public void setListener(ReceiptInterFace listener) {
        this.receiptInterFace = listener;
    }

    public boolean checkDialogFields(String field1, String field2, String field3, String field4) {
        if (!field1.equals("") && !field2.equals("") && !field3.equals("") && !field4.equals(""))
            return true;
        return false;
    }

}
