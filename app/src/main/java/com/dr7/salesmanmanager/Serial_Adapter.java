package com.dr7.salesmanmanager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.media.effect.EffectUpdateListener;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.Cheque;
import com.dr7.salesmanmanager.Modles.serialModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.RecyclerViewAdapter.bonus;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.counterBonus;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.counterSerial;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.serialListitems;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.unitQty;

public class Serial_Adapter extends RecyclerView.Adapter<Serial_Adapter.ViewHolder>{
        Context context;
        private AddItemsFragment2 contextAddItem;
        List<serialModel> list;
        DatabaseHandler databaseHandler;
        Calendar myCalendar;
public static List<Cheque> chequeListall;
public  static boolean errorData=false,isFoundSerial=false;
public int selectedBarcode=0;
public  static String barcodeValue="";

public Serial_Adapter(List<serialModel> chequeList,Context context,AddItemsFragment2 contextadd){
    Log.e("Serial_Adapter",""+chequeList.size());
        this.context=context;
        this.list=chequeList;
        databaseHandler=new DatabaseHandler(context);
        this.contextAddItem=contextadd;
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
        errorData=false;
         Log.e("position", "onBindViewHolder" + i+"errorData\t"+errorData);
        viewHolder.editTextSerialCode.setTag(i);
        viewHolder.textView_counterNo.setTag(i);
        list.get(i).setCounterSerial(i+1);
        if(i==0)
        {
            viewHolder.editTextSerialCode.requestFocus();
        }

        viewHolder.scanBarcode.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedBarcode=i;
            Log.e("selectedBarcode",""+selectedBarcode);
            contextAddItem.readB();

        }
    });
        viewHolder.editTextSerialCode.setText(list.get(i).getSerialCode());

    if(list.get(i).getIsBonus().equals("1"))
    {
        viewHolder.textView_counterNo.setTextColor(context.getResources().getColor(R.color.cancel_button));
        viewHolder.textView_counterNo.setText(context.getResources().getString(R.string.app_bonus));

    }
    else {
        viewHolder.textView_counterNo.setText((i+1)+"");
        viewHolder.textView_counterNo.setTextColor(context.getResources().getColor(R.color.text_view_color));

    }

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

                            if(list.get(i).getIsBonus().equals("0"))
                            {
                                counterSerial--;
                                unitQty.setText(""+counterSerial);
                            }
                            else {
                                if(list.get(i).getIsBonus().equals("1"))
                                {
                                    counterBonus--;
                                    bonus.setText(""+counterBonus);
                                }
                            }
                            list.remove(i);
                            notifyDataSetChanged();

                            sDialog.dismissWithAnimation();
                        }
                    }).setCancelText(context.getResources().getString(R.string.cancel)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
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

    public   EditText editTextSerialCode;
    Spinner spinner_bank;
    ImageView deletItem,scanBarcode;

    public ViewHolder(View itemView) {
        super(itemView);
        textView_counterNo = itemView.findViewById(R.id.counter_ser);
        editTextSerialCode = itemView.findViewById(R.id.Serial_No);
        scanBarcode = itemView.findViewById(R.id.scanBarcode);
        deletItem = itemView.findViewById(R.id.deletItem);
//        editTextSerialCode.requestFocus();
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
                boolean isUpdate=true;
                Log.e("afterTextChanged", "afterTextChanged" +list.get(position).getSerialCode()+"\t"+s.toString());
                if(s.toString().length()!=0)
                {
                    Log.e("afterTextChanged", "afterTextChanged" +s+"\t"+s.toString());
                        if(!list.get(position).getSerialCode().equals(s.toString()))
                        {
                            isUpdate= updateListCheque(position, s.toString());

                        }

                        if(!isUpdate)
                        {
                            editTextSerialCode.setError(context.getResources().getString(R.string.duplicate));
                            errorData=true;

                        }else {
                            editTextSerialCode.setError(null);
                        }

//                    Log.e("positionnOTeMPTY", "afterTextChanged" +"errorData\t"+errorData);
//
//                    isFoundSerial=false;
//
//                        for(int h=0;h<list.size();h++)
//                        {
//
//                            if(list.get(h).getSerialCode().equals(s.toString()))
//                            {
//                                isFoundSerial=true;
//                            }
//                        }
//
//
//                    Log.e("position", "afterTextChanged" + position+"errorData\t"+errorData);
//                    if((databaseHandler.isSerialCodeExist(s.toString()).equals("not"))&&(isFoundSerial==false))
//                    {
//                        errorData=false;
//                        editTextSerialCode.setError(null);
//                        updateListCheque(position, s.toString());
//                        Log.e("positionYES", "afterTextChanged" + position+" s.toString()\t"+ s.toString());
//                    }
//                    else {
//                        Log.e("positionNo", "afterTextChanged" + position+" s.toString()\t"+ s.toString());
//                        updateListCheque(position, "");
//                        errorData=true;
//                        editTextSerialCode.setError(context.getResources().getString(R.string.duplicate));
//                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText(context.getString(R.string.warning_message))
//                                .setContentText(context.getString(R.string.itemadedbefor))
//                                .show();
//
//
//
//                    }
                }
                else {
//                    updateListCheque(position, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
                }



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
    void setEditTextSerialCode(String s)
    {

        editTextSerialCode.setText(s.toString());
    }

}



    private boolean updateListCheque(int position, String data) {
    Log.e("updateListCheque",""+position+data);
        if(data.toString().length()!=0)
        {

                    Log.e("positionnOTeMPTY", "afterTextChanged" +"errorData\t"+errorData);

                    isFoundSerial=false;

                        for(int h=0;h<list.size();h++)
                        {

                            if(list.get(h).getSerialCode().equals(data))
                            {
                                isFoundSerial=true;
                            }
                        }


//            Log.e("position", "afterTextChanged" + position + "errorData\t" + errorData);
            if ((databaseHandler.isSerialCodeExist(data).equals("not")) && (isFoundSerial == false)) {
                errorData = false;
                list.get(position).setSerialCode(data);



//                Log.e("positionYES", "afterTextChanged" + position + " s.toString()\t" + data);
                return true;
            } else {
//                Log.e("positionNo", "afterTextChanged" + position + " s.toString()\t" + data);
                errorData = true;
                list.get(position).setSerialCode(data);
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context.getString(R.string.warning_message))
                        .setContentText(context.getString(R.string.itemadedbefor))
                        .show();

                return false;


            }
        }
        else {
            return  true;
//                    updateListCheque(position, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
        }

    }
    private void updatelistOrder(int counterUpdate) {

    Log.e("updatelistOrder",""+counterUpdate);
    ViewHolder viewHolder = null;
    for(int i=0;i<list.size();i++)
    {
        viewHolder.textView_counterNo.setText(i+"");
    }


    }

    public int getSelectedBarcode() {
        return selectedBarcode;
    }
}
