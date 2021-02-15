package com.dr7.salesmanmanager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.media.effect.EffectUpdateListener;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import static com.dr7.salesmanmanager.SalesInvoice.listMasterSerialForBuckup;
import static com.dr7.salesmanmanager.SalesInvoice.listSerialTotal;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;

public class Serial_Adapter extends RecyclerView.Adapter<Serial_Adapter.ViewHolder>{
    Context context;
    private AddItemsFragment2 contextAddItem;
    List<serialModel> list;
    DatabaseHandler databaseHandler;
    Calendar myCalendar;



    public static List<Cheque> chequeListall;
    public static boolean errorData = false, isFoundSerial = false;
    public int selectedBarcode = 0;
    public static String barcodeValue = "";
    public  static  TextView serialValue_Model;
    public  int currentUpdate=-1;
    private ArrayList<Integer> isClicked = new ArrayList<>();
    public  int sunmiDevice=0;


    public Serial_Adapter(List<serialModel> chequeList, Context context, AddItemsFragment2 contextadd) {

        this.context = context;
        this.list = chequeList;
        listMasterSerialForBuckup.clear();
        listMasterSerialForBuckup.addAll(chequeList);
        databaseHandler = new DatabaseHandler(context);
        this.contextAddItem = contextadd;
        for (int i = 0; i <= chequeList.size(); i++) {
            if(i==0&&chequeList.get(0).getSerialCode().equals(""))
            {
                isClicked.add(1);
            }
            else {
                isClicked.add(0);
            }

        }
        if(databaseHandler.getPrinterSetting()==6)
        {
            sunmiDevice=1;
            Log.e("sunmiDevice",""+sunmiDevice);
        }
        // chequeListall = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_serial_recycler, viewGroup, false);
        return new Serial_Adapter.ViewHolder(view);
    }

@Override
public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i){
        errorData=false;

        viewHolder.editTextSerialCode.setTag(i);

        viewHolder.textView_counterNo.setTag(i);
        list.get(i).setCounterSerial(i+1);

        if(isClicked.get(i)==0 )//empty
        {
            viewHolder.serialNo_linear.setBackgroundColor(context.getResources().getColor(R.color.white));


        }
        else {// exist value ==1
            if(sunmiDevice!=1)
            {
                viewHolder.editTextSerialCode.requestFocus();
            }


            viewHolder.serialNo_linear.setBackgroundColor(context.getResources().getColor(R.color.layer5));
        }

        Log.e("currentUpdate",""+currentUpdate+"\t"+viewHolder.editTextSerialCode.getTag());
        if(viewHolder.editTextSerialCode.getTag().equals((currentUpdate)+""))
        {
            if(sunmiDevice!=1)
            {
                viewHolder.editTextSerialCode.requestFocus();
               // viewHolder.editTextSerialCode.setText("Curent");
            }


        }

        viewHolder.scanBarcode.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedBarcode=i;
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
                            listMasterSerialForBuckup.get(i).setIsDeleted("1");
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
    TextView textView_counterNo,errorSerial;

    public   EditText editTextSerialCode;
    Spinner spinner_bank;
    ImageView deletItem,scanBarcode,editBarcode;
    LinearLayout serialNo_linear;

    public ViewHolder(View itemView) {
        super(itemView);

        textView_counterNo = itemView.findViewById(R.id.counter_ser);
        errorSerial=itemView.findViewById(R.id.errorSerial);
        editTextSerialCode = itemView.findViewById(R.id.Serial_No);
        serialNo_linear=itemView.findViewById(R.id.serialNo_linear);
//        editTextSerialCode.setEnabled(false);

        scanBarcode = itemView.findViewById(R.id.scanBarcode);
        deletItem = itemView.findViewById(R.id.deletItem);
        editBarcode= itemView.findViewById(R.id.editBarcode);
        editBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openeditDialog();
            }
        });
        serialValue_Model = itemView.findViewById(R.id.serialValue_Model);
        serialValue_Model.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isUpdate=true;
                Log.e("afterTextChanged","serialValue_Model="+serialValue_Model.getText().toString()+"\tselectedBarcode"+selectedBarcode);
                if(s.toString().length()!=0)
                {
                   if( checkInTotalBarcode(s.toString()))// not in the current voucher
                   {
                       if(!list.get(selectedBarcode).getSerialCode().equals(s.toString()))// not in curent ListSerial adapter
                       {
                           isUpdate= updateListCheque(selectedBarcode, s.toString());

                       }
                       if(!isUpdate)
                       {
                           if(sunmiDevice!=1)
                           {
                               editTextSerialCode.setError(context.getResources().getString(R.string.duplicate));
                           }
                           else {
                               try {
                                   errorSerial.setText(selectedBarcode+"");
                                   errorSerial.setError(context.getResources().getString(R.string.duplicate));
                               }
                               catch (Exception e){
                                   Log.e("serialValue_Model","Exception"+e.getMessage());
                               }

                              // errorSerial.setError(context.getResources().getString(R.string.duplicate));
                           }

                           errorData=true;

                       }else {// updated sucssesfuly
                           if(sunmiDevice!=1)
                           {
                               editTextSerialCode.setError(null);

                           }
                           else {
                               errorSerial.setError(null);
                           }

                       }
                   }
                   else {
                       if(sunmiDevice!=1) {
                           editTextSerialCode.setError(context.getResources().getString(R.string.duplicate));
                       }
                       else {
                           errorSerial.setText(selectedBarcode);
                           errorSerial.setError(context.getResources().getString(R.string.duplicate));

                          // errorSerial.setError(context.getResources().getString(R.string.duplicate));
                       }
                       errorData=true;
                   }




                }
                else {
                    updateListCheque(selectedBarcode, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
                }


            }
        });
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
                if(s.toString().length()!=0)
                {
                        if(!list.get(position).getSerialCode().equals(s.toString()))
                        {
                            Log.e("positionnMPTY1", "afterTextChanged" +"position\t"+position);
                            isUpdate= updateListCheque(position, s.toString());


                        }

                        if(!isUpdate)
                        {
                            Log.e("positionnMPTY2", "afterTextChangedNOT" +"errorData\t"+isUpdate);
                           if(sunmiDevice!=1)
                           {
                               editTextSerialCode.setError(context.getResources().getString(R.string.invalidSerial));

                               editTextSerialCode.setText("");
                           }
                           else {
                               errorSerial.setText(""+position);
                               errorSerial.setError(context.getResources().getString(R.string.invalidSerial));
                           }

                            errorData=true;

                        }else {
                            if(sunmiDevice!=1)
                            {
                                editTextSerialCode.setError(null);
                            }
                            else {
                                errorSerial.setError(null);
                            }

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

        errorSerial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(""))
                {
                    int po= Integer.parseInt(s.toString());
                    Log.e("errorSerial1", "afterTextChanged" +"position\t"+po);
//                    errorSerial.setError("Invalid");
                    updateListCheque(po,"error");
//
//                    if(s.toString().equals("delet"))
//                    {
//
//                        Log.e("afterTextChanged","delet");
//                        editTextSerialCode.setText("");
//                    }
//                    else if(s.toString().equals("invalidSerial"))
//                    {
//                        editTextSerialCode.setText("");
//                    }
                }
            }
        });

    }

    private boolean checkInTotalBarcode(String serial) {
        for(int i=0;i<listSerialTotal.size();i++)
        {
            if(listSerialTotal.get(i).getSerialCode().equals(serial))
            {
                return false;
            }
        }
        return true;
    }

    private void openeditDialog() {
        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        SweetAlertDialog sweetMessage= new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(context.getResources().getString(R.string.enter_serial));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(!editText.getText().toString().equals(""))
                {
                    editTextSerialCode.setText(editText.getText().toString());
//                    linkEditText.setAlpha(1f);
//                    linkEditText.setEnabled(true);
//                    linkEditText.requestFocus();
                    sweetAlertDialog.dismissWithAnimation();
                }
                else {
                    editText.setError(context.getResources().getString(R.string.reqired_filled));
                }
            }
        })

                .show();
    }

    void setEditTextSerialCode(String s)
    {

        editTextSerialCode.setText(s.toString());
    }

}



    private boolean updateListCheque(int position, String data) {
       // Log.e("updateListCheque", "afterTextChanged" +"position\t"+position+data);

        if(data.toString().length()!=0)
        {
            if(data.equals("error"))
            {
               // Log.e("errorSerial2", "afterTextChanged" +"position\t"+data);
                list.get(position).setSerialCode("");
                listMasterSerialForBuckup.get(position).setIsDeleted("1");
               // isFoundSerial=true;

                return false;
            }
            else {
                isFoundSerial=false;

                for(int h=0;h<list.size();h++)
                {

                    if(list.get(h).getSerialCode().equals(data))
                    {
                        isFoundSerial=true;
                    }
                }
                if(isFoundSerial==true)
                {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.duplicate))
                            .show();
                    return  false;
                }
            }


            //Log.e("errorSerial2", "isFoundSerial" +"position\t"+isFoundSerial);
//            if ((databaseHandler.isSerialCodeExist(data).equals("not")) && (isFoundSerial == false)) {
            if (databaseHandler.isSerialCodeExist(data).equals("not")) {
                if((databaseHandler.isSerialCodePaied(data+"").equals("not")&&voucherType==504)||
                        (!databaseHandler.isSerialCodePaied(data+"").equals("not")&&voucherType==506))
                {
                errorData = false;

                list.get(position).setSerialCode(data);
                listMasterSerialForBuckup.get(position).setSerialCode(data);
                currentUpdate = position;

                isClicked.set(position, 0);
                isClicked.set(position + 1, 1);

                notifyDataSetChanged();

                return true;

            }
                else {// duplicated
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.duplicate))
                            .show();
                    return  false;
                }

            } else {
                errorData = true;
                if(isFoundSerial)
                {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.duplicate))
                            .show();
                }


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
