package com.dr7.salesmanmanager.Adapters;

import static com.dr7.salesmanmanager.Reports.SerialReport.allseriallist;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportToExcel;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.ReturnByVoucherNo;
import com.dr7.salesmanmanager.SerialReportAdpter;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReturnItemAdapter extends   RecyclerView.Adapter<ReturnItemAdapter.SerialReportViewHolder >{
    private List<serialModel> list;
    private List<String> vocherlist=new ArrayList<>();;
    private List<serialModel> serialrowlist=new ArrayList<>();
    Context context;

    int shelfReport=0;
    DatabaseHandler databaseHandler;
    private int updateFlage=1;
    private int  Flage;
    public ReturnItemAdapter(List<serialModel> list, Context context,int typeReport,int Flag) {
        this.list = list;
        this.context = context;
        shelfReport=typeReport;
        this.Flage=Flag;
        databaseHandler=new DatabaseHandler(context);
        updateFlage=1;
    }

    @NonNull
    @Override
    public SerialReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.serialrow_return, parent, false);
        return new SerialReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnItemAdapter.SerialReportViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.voucherDate.setText(list.get(position).getDateVoucher());

       int c= getserialsClickedcount(list.get(position).getItemNo());

        holder.    text_count.setText("" +c);


        Log.e("getItemNo","no="+list.get(position).getItemNo());
        holder.serialcode.setText(String.valueOf(list.get(position).getSerialCode()));
        holder.itemName.setText(String.valueOf(list.get(position).getItemName()));

        if(list.get(position).getSerialCode().equals("")) {
            if (HaveSerial(list.get(position).getItemNo(), position) == 1) {
                Log.e("caseHaveSerial","==1");
                Log.e("getItemNo",list.get(position).getItemNo()+"");
                Log.e("getserial",list.get(position).getSerialCode()+"");
                Log.e("caseHaveSerial","==1");
                holder.text_count.setEnabled(false);


                if(c==0)
                {
                    Log.e("case4","==4");
                    holder.text_count.setVisibility(View.INVISIBLE);

                }
                else{
                    Log.e("caseHaveSerial","==1");
                    Log.e("getItemNo",list.get(position).getItemNo()+"");
                    Log.e("getserial",list.get(position).getSerialCode()+"");
                    Log.e("caseHaveSerial","==1");
                    Log.e("case5","==5");
                    holder.text_count.setVisibility(View.VISIBLE);

                }
                holder.linearSelected.setVisibility(View.INVISIBLE);

                holder.price.setVisibility(View.INVISIBLE);
                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.done_button2));

            }else
                {

                    holder.    text_count.setText(list.get(position).getQty());
            }

        } else{
            Log.e("caseHaveSerial","==2");
            holder.text_count.setEnabled(false);
            holder.text_count.setVisibility(View.INVISIBLE);
            //   holder.text_count.

        }

        holder.itemnum.setText(String.valueOf(list.get(position).getItemNo()));
        holder.price.setText(String.valueOf(list.get(position).getPriceItem()));




      if  (  list.get(position).getIsClicked()==1)
          holder.linearSelected.setChecked(true);
else  if (  list.get(position).getIsClicked()==0)
        holder.linearSelected.setChecked(false);
        holder.linearSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("linearSelected","bb"+b);


                if(b)
                {
                    list.get(holder.getAdapterPosition()).isClicked=1;
              updateqty(holder, list.get(holder.getAdapterPosition()).getItemNo());

                }else {
                    list.get(holder.getAdapterPosition()).isClicked=0;
                    updateqty2(holder, list.get(holder.getAdapterPosition()).getItemNo());
                }
                    }
        });

        holder.    text_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                {
//                    Log.e("value1",position+" ,"+list.get(position).getQty()+"  ");
//                    Log.e("value2",position+" ,"+Float.parseFloat(list.get(position).getQty())+"  ,");
//                    Log.e("value3",position+" ,"+Float.parseFloat(s.toString().trim()));

                    if(!s.toString().equals("")) {
                       if( HaveSerial(list.get(position).getItemNo(), position) == 0)
                       {

                          if(!s.toString().equals("0")) {

                              if (Float.parseFloat( ReturnByVoucherNo.Recoverallitemsdata.get(position).getQty())
                                 >= Float.parseFloat(s.toString().trim())) {

                             ReturnByVoucherNo.allitemsdata.get(position).setQty(s.toString().trim());
                         }
                         else {
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)

                                     .setContentText(context.getString(R.string.invalidValue))
                                     .setConfirmButton(context.getString(R.string.app_yes), new SweetAlertDialog.OnSweetClickListener() {
                                         @Override
                                         public void onClick(SweetAlertDialog sweetAlertDialog) {
                                              sweetAlertDialog.dismiss();
                                         }
                                     }).show();
                             holder.text_count.setText(ReturnByVoucherNo.Recoverallitemsdata.get(position).getQty());

                         }}else {

                                  new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)

                                          .setContentText(context.getString(R.string.invalidValue))
                                          .setConfirmButton(context.getString(R.string.app_yes), new SweetAlertDialog.OnSweetClickListener() {
                                              @Override
                                              public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                  sweetAlertDialog.dismiss();
                                              }
                                          }).show();
                                  holder.text_count.setText(ReturnByVoucherNo.Recoverallitemsdata.get(position).getQty());


                          }
                     }}}
            }
        });


//        if(databaseHandler.getAllSettings().get(0).getCanChangePrice()==1
//        ||databaseHandler.getAllSettings().get(0).getCanChangePrice_returnonly()==1)
//            holder.    price.setEnabled(true);
//        else
            holder.    price.setEnabled(false);

            holder.    price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                {
//                    Log.e("value1",position+" ,"+list.get(position).getQty()+"  ");
//                    Log.e("value2",position+" ,"+Float.parseFloat(list.get(position).getQty())+"  ,");
//                    Log.e("value3",position+" ,"+Float.parseFloat(s.toString().trim()));

                    if(!s.toString().equals("")) {

                            if(!s.toString().equals("0")) {



                                    ReturnByVoucherNo.allitemsdata.get(holder.getAdapterPosition()).setPriceItem(Float.parseFloat(s.toString().trim()));
                                }
                             }else {

                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)

                                        .setContentText(context.getString(R.string.invalidValue))
                                        .setConfirmButton(context.getString(R.string.app_yes), new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                            }
                                        }).show();

                        holder.price.setText(ReturnByVoucherNo.Recoverallitemsdata.get(holder.getAdapterPosition()).getPriceItem()+"");

                            }
                        }}

        });
    }

    private void updateqty(ReturnItemAdapter.SerialReportViewHolder holder,String itemNo) {
        for(int i=0;i<list.size();i++)

            if(list.get(i).getItemNo().equals(itemNo)&&list.get(i).getSerialCode().equals(""))
            {
                if(HaveSerial(list.get(i).getItemNo(),i) == 1) {
                    list.get(i).setQty(String.valueOf(Float.parseFloat(list.get(i).getQty()) + 1.0));


                if (!ReturnByVoucherNo.recyclerView.isComputingLayout())
                    {
                        //updateqty(holder,list.get(0).getItemNo());
                        ReturnByVoucherNo.  checked.setText("checked");

                    }

                }

            }

    }
    private void updateqty2(ReturnItemAdapter.SerialReportViewHolder holder,String itemNo) {
        for(int i=0;i<list.size();i++)

            if(list.get(i).getItemNo().equals(itemNo)&&list.get(i).getSerialCode().equals(""))
            {
                if(HaveSerial(list.get(i).getItemNo(),i) == 1) {
                    list.get(i).setQty(String.valueOf(Float.parseFloat(list.get(i).getQty()) - 1.0));

                    if (!ReturnByVoucherNo.recyclerView.isComputingLayout()){
                        ReturnByVoucherNo.  checked.setText("checked");

                    }
                }
                //yourMethodName(holder,i);
            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SerialReportViewHolder extends RecyclerView.ViewHolder {
        TextView  vouchertype, serialcode, itemnum, itemName, export,voucherDate,customerName;
        EditText text_count,price;
        CheckBox linearSelected;
        LinearLayout linearLayout;

        public SerialReportViewHolder(@NonNull View itemView) {

            super(itemView);
            price = itemView.findViewById(R.id.text_price);
            vouchertype = itemView.findViewById(R.id.SE_vocherkind);
            voucherDate= itemView.findViewById(R.id.text_voucherdate);
            serialcode = itemView.findViewById(R.id.SE__serialcode);
            itemnum = itemView.findViewById(R.id.SE__itemnum);
            text_count= itemView.findViewById(R.id. text_count);
            linearLayout = itemView.findViewById(R.id.lin);

            linearSelected=itemView.findViewById(R.id.linearSelected);

            itemName=itemView.findViewById(R.id.itemName);

        }


    }
    int HaveSerial (String serial,int pos){
        int x=0;
        for (int i = 0; i < list.size(); i++)
            if (i!=pos &&serial.equals(list.get(i).getItemNo()))
                x=1;
            return x;
    }
    private void yourMethodName(final ReturnItemAdapter.SerialReportViewHolder holder,final int position)
    {
        updateFlage=2;
        holder.text_count.setText(list.get(position).getQty());

    }
 int   getserialsClickedcount(String s){
        int count=0;
     for(int i=0;i<list.size();i++)
     {
         if(list.get(i).getItemNo().equals(s) &&!list.get(i).getSerialCode().equals("")
         &&list.get(i).getIsClicked()==1)
             count++;

     }
     return count;
    }
}
