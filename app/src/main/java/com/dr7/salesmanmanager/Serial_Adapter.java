package com.dr7.salesmanmanager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dr7.salesmanmanager.Modles.Cheque;
import com.dr7.salesmanmanager.Modles.serialModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.RecyclerViewAdapter.counterSerial;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialListitems;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.unitQty;

public class Serial_Adapter extends RecyclerView.Adapter<Serial_Adapter.ViewHolder>{
        Context context;
        List<serialModel> list;
        Calendar myCalendar;
public static List<Cheque> chequeListall;

public Serial_Adapter(List<serialModel> chequeList,Context context){
    Log.e("Serial_Adapter",""+chequeList.size());
        this.context=context;
        this.list=chequeList;
        // chequeListall = new ArrayList<>();
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i){
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_serial_recycler,viewGroup,false);
        return new Serial_Adapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i){
        viewHolder.editTextSerialCode.setTag(i);
        viewHolder.textView_counterNo.setTag(i);

        viewHolder.editTextSerialCode.setText(list.get(i).getSerialCode());
        viewHolder.textView_counterNo.setText(list.get(i).getCounterSerial()+"");
        viewHolder.deletItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(R.string.Confirm)
                    .setContentText(context.getResources().getString(R.string.AreYouSuretodelete))
                    .setConfirmText(context.getResources().getString(R.string.app_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Log.e("onClick",""+i+serialListitems.size()+"\t"+counterSerial);

//                            serialListitems.remove(i-1);
                            list.remove(i);
                            counterSerial--;
                            unitQty.setText(""+counterSerial);
//                            updatelistOrder(counterSerial);
//                            Log.e("updatelistOrder",""+counterSerial);
//                            ViewHolder viewHolder = null;
//                            for(int i=0;i<counterSerial;i++)
//                            {
//                                viewHolder.textView_counterNo.setText(i+"");
//                            }
                            notifyDataSetChanged();

                            sDialog.dismissWithAnimation();
                        }
                    }).setCancelText(context.getResources().getString(R.string.dialog_cancel)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    sweetAlertDialog.dismissWithAnimation();

                }
            }).show();
        }
    });

        }

@Override
public int getItemCount(){
        return list.size();
        }

public DatePickerDialog.OnDateSetListener openDatePickerDialog(final EditText editText){
final DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener(){
@Override
public void onDateSet(DatePicker view,int year,int month,int dayOfMonth){

        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR,year);
        myCalendar.set(Calendar.MONTH,month);
        myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        updateLabel(editText);
        }

        };
        return date;
        }

private void updateLabel(EditText editText){
        String myFormat="dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf=new SimpleDateFormat(myFormat,Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

        }

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView textView_counterNo;
    EditText editTextSerialCode;
    Spinner spinner_bank;
    ImageView deletItem;

    public ViewHolder(View itemView) {
        super(itemView);
        textView_counterNo = itemView.findViewById(R.id.counter_ser);
        editTextSerialCode = itemView.findViewById(R.id.Serial_No);
        deletItem = itemView.findViewById(R.id.deletItem);
//        spinner_bank = itemView.findViewById(R.id.spinner_bank);
//
        editTextSerialCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = (int) editTextSerialCode.getTag();
                updateListCheque(position, s.toString());

            }
        });
//        editText_amountvalue.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int position = (int) editText_amountvalue.getTag();
//                Float amountValue = 0f;
//                try {
//
//                    amountValue = Float.parseFloat(s.toString());
//                } catch (Exception e) {
//                    amountValue = 0f;
//                    editText_amountvalue.setError("Please Enter Valid Number");
//
//                }
//                if (amountValue != 0) {
//                    updateListAmount(position, amountValue);
//
//                } else {
//                    editText_amountvalue.setError("Error Zero Value");
//
//                }
//
//
//            }
//        });

    }

}



    private void updateListAmount(int position, Float amount) {
//        list.get(position).setChequeValue(amount);
//        Float amountValue = 0f;
//        try {
//
//            amountValue = Float.parseFloat(amount);
//        } catch (Exception e) {
//            amountValue = 0f;
//
//        }
//        if (amountValue != 0) {
//
//
//            Log.e("before2222", "" + list.get(position).getChequeValue());
//        }
//        else {
//
//        }



    }

    private void updateListCheque(int position, String date) {
        list.get(position).setSerialCode(date);
    }
    private void updatelistOrder(int counterUpdate) {

    Log.e("updatelistOrder",""+counterUpdate);
    ViewHolder viewHolder = null;
    for(int i=0;i<list.size();i++)
    {
        viewHolder.textView_counterNo.setText(i+"");
    }


    }
}
