package com.dr7.salesmanmanager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.media.effect.EffectUpdateListener;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.google.zxing.integration.android.IntentIntegrator;

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
import static com.dr7.salesmanmanager.SalesInvoice.checkQtyServer;
import static com.dr7.salesmanmanager.SalesInvoice.editOpen;
import static com.dr7.salesmanmanager.SalesInvoice.listMasterSerialForBuckup;
import static com.dr7.salesmanmanager.SalesInvoice.listSerialTotal;
import static com.dr7.salesmanmanager.SalesInvoice.minusQtyTotal;
import static com.dr7.salesmanmanager.SalesInvoice.unitQtyEdit;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;

public class Serial_Adapter extends RecyclerView.Adapter<Serial_Adapter.ViewHolder>{
    Context context;
   // private AddItemsFragment2 contextAddItem;
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
    public  int sunmiDevice=0,dontShowTax=0,contiusReading=0;


    public Serial_Adapter(List<serialModel> chequeList, Context context) {

        this.context = context;
        this.list = chequeList;
        listMasterSerialForBuckup.clear();
        listMasterSerialForBuckup.addAll(chequeList);
        databaseHandler = new DatabaseHandler(context);
        //this.contextAddItem = contextadd;
        for (int i = 0; i <= chequeList.size(); i++) {
            if(i==0&&chequeList.get(0).getSerialCode().equals(""))
            {
                isClicked.add(1);
            }
            else {
                isClicked.add(0);
            }

        }
        if(databaseHandler.getAllSettings().get(0).getContinusReading()==1)
        {
            sunmiDevice=1;
            contiusReading=1;
            Log.e("sunmiDevice",""+sunmiDevice);
        }
        checkQtyServer=0;

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
        Log.e("onBindViewHolder","voucherType"+voucherType+"\t"+list.get(i).getKindVoucher());
        list.get(i).setKindVoucher(voucherType+"");
        Log.e("onBindViewHolder","voucherType"+voucherType+"\t"+list.get(i).getKindVoucher());

        if(isClicked.get(i)==0 )//empty
        {
            viewHolder.serialNo_linear.setBackgroundColor(context.getResources().getColor(R.color.white));


        }
        else {// exist value ==1

            if(contiusReading==1)
            {
//                viewHolder.editTextSerialCode.requestFocus();
                Log.e("currentUpdate",""+currentUpdate+"readB");
                viewHolder.editTextSerialCode.requestFocus();
                selectedBarcode=i;
                openSmallScanerTextView();
               // contextAddItem.readB();
            }
            else {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.editTextSerialCode.requestFocus();

                    }
                });

            }
            selectedBarcode=i;

//            contextAddItem.readB();
            viewHolder.serialNo_linear.setBackgroundColor(context.getResources().getColor(R.color.layer5));
        }

        Log.e("currentUpdate",""+currentUpdate+"\t"+viewHolder.editTextSerialCode.getTag());
        if(viewHolder.editTextSerialCode.getTag().equals((currentUpdate)+""))
        {
            if(contiusReading==1)
            {
                Log.e("currentUpdate",""+currentUpdate+"readB");
                viewHolder.editTextSerialCode.requestFocus();
//                selectedBarcode=i;
//                contextAddItem.readB();
            }
            else {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.editTextSerialCode.requestFocus();

                    }
                });
            }


        }

        viewHolder.scanBarcode.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedBarcode=i;
            openSmallScanerTextView();
//            contextAddItem.readB();

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
                            Log.e("onClick","serialListitems"+i+serialListitems.size()+"\t"+counterSerial);

                            counterSerial=list.size();
                            if(list.get(i).getIsBonus().equals("0"))
                            {
                                counterSerial--;

                                if(editOpen==true)
                                {
                                    unitQtyEdit.setText(counterSerial+"");
                                }
                                else {
                                    unitQty.setText(""+counterSerial);
                                }
                                Log.e("onClick","list"+i+list.size()+"\t"+counterSerial);
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
                            Log.e("onClick","list"+i+list.size()+"\t"+counterSerial);


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
        if(contiusReading==1)
        {
            editTextSerialCode.setEnabled(false);
        }
        else {
            editTextSerialCode.setEnabled(true);
        }
//

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
            try {
                serialValue_Model.addTextChangedListener(new TextWatcher() {// FROM BARCODE
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        boolean isUpdate=true;
                        //   Log.e("afterTextChanged","serialValue_Model="+serialValue_Model.getText().toString()+"\tselectedBarcode"+selectedBarcode);
//                    if(s.toString().length()!=0)
//                    {
//                        if( checkInTotalBarcode(s.toString()))// not in the current voucher
//                        {
//                            if(!list.get(selectedBarcode).getSerialCode().equals(s.toString()))// not in curent ListSerial adapter
//                            {
//                                if(checkQtyServer==0){
//                                    isUpdate= updateListCheque(selectedBarcode, s.toString(),0);
//                                }
//                                else {
//                                    isUpdate= updateListCheque(selectedBarcode, s.toString(),0);
//                                }
//
//
//                            }
//                            if(!isUpdate)
//                            {
//                                if(sunmiDevice!=1)
//                                {
//                                    editTextSerialCode.setError(context.getResources().getString(R.string.duplicate));
//                                }
//                                else {
//                                    try {
//                                        errorSerial.setText(selectedBarcode+"");
//                                        errorSerial.setError(context.getResources().getString(R.string.duplicate));
//                                        Toast.makeText(context, context.getResources().getString(R.string.duplicate), Toast.LENGTH_SHORT).show();
//
//                                    }
//                                    catch (Exception e){
//                                        Log.e("serialValue_Model","Exception"+e.getMessage());
//                                    }
//
//                                    // errorSerial.setError(context.getResources().getString(R.string.duplicate));
//                                }
//
//                                errorData=true;
//
//                            }else {// updated sucssesfuly
//                                if(sunmiDevice!=1)
//                                {
//                                    editTextSerialCode.setError(null);
//
//                                }
//                                else {
//                                    errorSerial.setError(null);
//                                }
//
//                            }
//                        }
//                        else {
//                            if(sunmiDevice!=1) {
//                                editTextSerialCode.setError(context.getResources().getString(R.string.duplicate));
//                            }
//                            else {
//                                errorSerial.setText(selectedBarcode);
//                                errorSerial.setError(context.getResources().getString(R.string.duplicate));
//
//                                // errorSerial.setError(context.getResources().getString(R.string.duplicate));
//                            }
//                            errorData=true;
//                        }
//
//
//
//
//                    }
//                    else {
//                        updateListCheque(selectedBarcode, "",0);
////                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
//                    }
//
                        //***************************Edit Text Serial Method********************************

                        if(s.toString().length()!=0)
                        {
                            if(!list.get(selectedBarcode).getSerialCode().equals(s.toString().trim()))
                            {
                                if(checkQtyServer==0){
                                    isUpdate= updateListCheque(selectedBarcode, s.toString().trim(),0);
                                }
                                else {
                                    isUpdate= updateListCheque(selectedBarcode, s.toString().trim(),0);
                                }

                            }

                            if(!isUpdate)// not exist serial in serial master
                            {

                                if(contiusReading==1)
                                {
                                    editTextSerialCode.setError(context.getResources().getString(R.string.invalidSerial));

                                    editTextSerialCode.setText("");
                                }
                                else {

                                    errorSerial.setText(""+selectedBarcode);
                                    errorSerial.setError(context.getResources().getString(R.string.invalidSerial));
                                    if(  s.toString().length()>14                  )
                                    {
                                        Toast.makeText(context, context.getResources().getString(R.string.invalidSerial), Toast.LENGTH_SHORT).show();

                                    }

                                }

                                errorData=true;

                            }else {
                                if(contiusReading==1)
                                {
                                    editTextSerialCode.setError(null);
                                }
                                else {
                                    errorSerial.setError(null);
                                }

                            }

                        }
                        else {
//                    updateListCheque(position, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
                        }


                    }
                });
            }

            catch (Exception e){}

            if(contiusReading==0){
                try {
                    editTextSerialCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH
                                    || actionId == EditorInfo.IME_NULL) {
                              //  Log.e("setOnEditorActio", "afterTextChangedNOT" +"errorData\t"+event.toString());
                                int position = (int) editTextSerialCode.getTag();
                                boolean isUpdate=true;
                                if(editTextSerialCode.getText().toString().trim().length()!=0)
                                {
                                    if(!list.get(position).getSerialCode().equals(editTextSerialCode.getText().toString().trim()))
                                    {
                                        if(checkQtyServer==0){
                                            isUpdate= updateListCheque(position, editTextSerialCode.getText().toString().trim(),0);
                                        }
                                        else {
                                            isUpdate= updateListCheque(position, editTextSerialCode.getText().toString().trim(),0);
                                        }
//                                    isUpdate= updateListCheque(, ,0);


                                    }

                                    if(!isUpdate)// not exist serial in serial master
                                    {
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                editTextSerialCode.requestFocus();
                                                editTextSerialCode.setError(context.getResources().getString(R.string.invalidSerial));

                                                editTextSerialCode.setText("");

                                            }
                                        });
                                        errorData=true;

                                    }else {
                                        editTextSerialCode.setError(null);
                                    }
                                }


                            }


                            return false;
                        }
                    });

                }catch (Exception e){}



            }
            else {
                try {
                    editTextSerialCode.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {


                                int position = (int) editTextSerialCode.getTag();
                                boolean isUpdate = true;
                                if (s.toString().trim().length() != 0) {
                                    if (!list.get(position).getSerialCode().equals(s.toString().trim())) {
                                        Log.e("positionnMPTY1", "afterTextChanged" + "position\t" + position);

                                        if (checkQtyServer == 0) {
                                            isUpdate = updateListCheque(position, s.toString().trim(), 0);
                                        } else {
                                            isUpdate = updateListCheque(position, s.toString().trim(), 0);
                                        }

                                    }

                                    if (!isUpdate)// not exist serial in serial master
                                    {
                                        Log.e("positionnMPTY2", "afterTextChangedNOT" + "errorData\t" + isUpdate);
                                        if (sunmiDevice != 1) {
                                            editTextSerialCode.setError(context.getResources().getString(R.string.invalidSerial));

                                            editTextSerialCode.setText("");
                                        } else {

                                            errorSerial.setText("" + position);
                                            errorSerial.setError(context.getResources().getString(R.string.invalidSerial));
                                            if (s.toString().length() > 14) {
                                                Toast.makeText(context, context.getResources().getString(R.string.invalidSerial), Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                        errorData = true;

                                    } else {
                                        if (sunmiDevice != 1) {
                                            editTextSerialCode.setError(null);
                                        } else {
                                            errorSerial.setError(null);
                                        }

                                    }

                                } else {
//                    updateListCheque(position, "");
//                    Log.e("positionnMPTY", "afterTextChanged" +"errorData\t"+errorData);
                                }

                            }catch (Exception e){}
                        }

                    });
                }catch(Exception e){}

            }
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
                        updateListCheque(po,"error",0);
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
//        }
//        else {
//            editTextSerialCode.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    if(editable.toString().length()!=0)
//                    {
//                        int position = (int) editTextSerialCode.getTag();
//                        updateListCheque(position, editTextSerialCode.getText().toString(),1);
//
//                    }
//
//                }
//            });
//
//
//        }






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
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
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
//                    if(sunmiDevice!=1)
//                    {
//                        editTextSerialCode.setText(editText.getText().toString());
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//
//                    else {
                        if(validSerial(editText.getText().toString().trim()))
                        {
                            editTextSerialCode.setText(editText.getText().toString().trim());
                            sweetAlertDialog.dismissWithAnimation();

                        }
                        else {editText.setError(context.getResources().getString(R.string.invalidSerial));
                        editText.setText("");}

//                    }

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

    private boolean validSerial(String serialValue) {

            isFoundSerial=false;

            for(int h=0;h<list.size();h++)
            {

                if(list.get(h).getSerialCode().equals(serialValue.trim()))
                {
                    isFoundSerial=true;
                }
            }
            if(isFoundSerial==true)
            {// FOUND  IN CURRENT LIST
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context.getString(R.string.warning_message))
                        .setContentText(context.getString(R.string.duplicate))
                        .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if(contiusReading==1) {
                                    openSmallScanerTextView();
                                }
//                                contextAddItem.readB();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                Toast.makeText(context, context.getResources().getString(R.string.duplicate), Toast.LENGTH_SHORT).show();

                return  false;
            }



        //Log.e("errorSerial2", "isFoundSerial" +"position\t"+isFoundSerial);
//            if ((databaseHandler.isSerialCodeExist(data).equals("not")) && (isFoundSerial == false)) {
        if (databaseHandler.isSerialCodeExist(serialValue).equals("not")||databaseHandler.getLastTransactionOfSerial(serialValue.trim()).equals("506")) {
            if((databaseHandler.isSerialCodePaied(serialValue+"").equals("not")&&voucherType==504)||
                    (!databaseHandler.isSerialCodePaied(serialValue+"").equals("not")&&voucherType==506))
            {

                return true;

            }
            else {// duplicated
                String voucherNo=databaseHandler.isSerialCodePaied(serialValue+"");
                String voucherDate=voucherNo.substring(voucherNo.indexOf("&")+1);
                voucherNo=voucherNo.substring(0,voucherNo.indexOf("&"));

                Log.e("Activities3", "onActivityResult+false+isSerialCodePaied" + voucherNo+"\t"+voucherDate);
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(context.getString(R.string.duplicate) +"\t"+serialValue+ "\t"+context.getString(R.string.forVoucherNo)+"\t" +voucherNo+"\n"+context.getString(R.string.voucher_date)+"\t"+voucherDate)
                        .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if(contiusReading==1) {
                                    openSmallScanerTextView();
                                }
//                                contextAddItem.readB();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

                Toast.makeText(context, context.getResources().getString(R.string.duplicate), Toast.LENGTH_SHORT).show();

                return  false;
            }

        } else {
            errorData = true;

            // Toast.makeText(context, context.getResources().getString(R.string.invalidSerial), Toast.LENGTH_SHORT).show();
            String ItemNo=databaseHandler.isSerialCodeExist(serialValue+"");

            Log.e("ItemNo","ValidSerial"+ItemNo);
            if(!ItemNo.equals("")){
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context.getString(R.string.warning_message))
                        .setContentText(context.getString(R.string.invalidSerial)+"\t"+serialValue+"\t"+context.getString(R.string.forItemNo)+ItemNo)
                        .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                contextAddItem.readB();
                                if(contiusReading==1) {
                                    openSmallScanerTextView();
                                }
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context.getString(R.string.warning_message))
                        .setContentText(context.getString(R.string.invalidSerial)+"\t"+serialValue)
                        .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if(contiusReading==1) {
                                    openSmallScanerTextView();
                                }
//                                contextAddItem.readB();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }



            return false;


        }
    }
    public void openSmallScanerTextView() {

            new IntentIntegrator((Activity) context).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();



    }

    private boolean updateListCheque(int position, String barcode,int dontValidate) {
        String data =barcode.toString().trim();
        Log.e("updateListCheque", "afterTextChanged" +"position\t"+position+data+"\tdontValidate="+dontValidate);
try {


        if(dontValidate==1){
            list.get(position).setSerialCode(data.trim());
            listMasterSerialForBuckup.get(position).setSerialCode(data.trim());
            currentUpdate = position;

            isClicked.set(position, 0);
            isClicked.set(position + 1, 1);
            notifyDataSetChanged();
            return  true;

        }
        else {


        if(data.toString().trim().length()!=0)
        {
            if(data.toString().trim().equals("error"))
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
                        break;
                    }
                }
                if(isFoundSerial==true)
                {// FOUND  IN CURRENT LIST
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.duplicate)+"\t"+context.getResources().getString(R.string.inThisVoucher))
                            .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    if(contiusReading==1) {
                                        openSmallScanerTextView();
                                    }
//                                    contextAddItem.readB();
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    Toast.makeText(context, context.getResources().getString(R.string.duplicate)+"\t"+context.getResources().getString(R.string.inThisVoucher), Toast.LENGTH_SHORT).show();

                    return  false;
                }
            }


            //Log.e("errorSerial2", "isFoundSerial" +"position\t"+isFoundSerial);
//            if ((databaseHandler.isSerialCodeExist(data).equals("not")) && (isFoundSerial == false)) {
            if (databaseHandler.isSerialCodeExist(data).equals("not")||voucherType==506||databaseHandler.getLastTransactionOfSerial(data.trim()).equals("506")) {
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
                    if(voucherType==506)
                    {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(context.getString(R.string.warning_message))
                                .setContentText(context.getString(R.string.serialIsNotPaied))
                                .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if(contiusReading==1) {
                                            openSmallScanerTextView();
                                        }
//                                        contextAddItem.readB();
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                    else {
                        String voucherNo=databaseHandler.isSerialCodePaied(data+"");
                        String voucherDate=voucherNo.substring(voucherNo.indexOf("&")+1);
                        voucherNo=voucherNo.substring(0,voucherNo.indexOf("&"));

                        Log.e("Activities3", "onActivityResult+false+isSerialCodePaied" + voucherNo+"\t"+voucherDate);
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setContentText(context.getString(R.string.duplicate) +"\t"+data+ "\t"+context.getString(R.string.forVoucherNo)+"\t" +voucherNo+"\n"+context.getString(R.string.voucher_date)+"\t"+voucherDate)
                                .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if(contiusReading==1) {
                                            openSmallScanerTextView();
                                        }
//                                        contextAddItem.readB();
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }


                    return  false;
                }

            } else {
                errorData = true;
               // Toast.makeText(context, context.getResources().getString(R.string.invalidSerial), Toast.LENGTH_SHORT).show();
                String ItemNo=databaseHandler.isSerialCodeExist(data+"");
                if(!ItemNo.equals("")){
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.invalidSerial)+"\t"+data+"\t"+context.getString(R.string.forItemNo)+ItemNo)
                            .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    if(contiusReading==1) {
                                        openSmallScanerTextView();
                                    }
//                                    contextAddItem.readB();
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
                else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(context.getString(R.string.warning_message))
                            .setContentText(context.getString(R.string.invalidSerial)+"\t"+data)
                            .setConfirmButton(context.getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    if(contiusReading==1) {
                                        openSmallScanerTextView();
                                    }
//                                    contextAddItem.readB();
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
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

}catch (Exception e){
    return  false;
}


    }// end
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
