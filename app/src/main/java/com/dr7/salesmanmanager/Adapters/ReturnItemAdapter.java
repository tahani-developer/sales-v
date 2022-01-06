package com.dr7.salesmanmanager.Adapters;

import static com.dr7.salesmanmanager.Reports.SerialReport.allseriallist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
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

    public ReturnItemAdapter(List<serialModel> list, Context context,int typeReport) {
        this.list = list;
        this.context = context;
        shelfReport=typeReport;
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

        holder.    text_count.setText(list.get(position).getQty());
        Log.e("getItemNo","no="+list.get(position).getItemNo());
        holder.serialcode.setText(String.valueOf(list.get(position).getSerialCode()));
//        if(list.get(position).getItemName().equals("null"))
//        {
//            holder.itemName.setText(databaseHandler.getItemName(list.get(position).getItemNo()));
//        }else {
            holder.itemName.setText(String.valueOf(list.get(position).getItemName()));
//        }
        if(list.get(position).getSerialCode().equals("")) {
            if (HaveSerial(list.get(position).getItemNo(), position) == 1) {
                holder.linearSelected.setVisibility(View.INVISIBLE);

                holder.price.setVisibility(View.INVISIBLE);
                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.done_button2));

            }

        } else{
            holder.text_count.setEnabled(false);
            //   holder.text_count.

        }

        holder.itemnum.setText(String.valueOf(list.get(position).getItemNo()));
        holder.price.setText(String.valueOf(list.get(position).getPriceItem()));



      /*  holder.linearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(holder.getAdapterPosition()).getSerialCode().equals("")) {
                    {
                        for (int i = 0; i < list.size(); i++)
                            if (list.get(holder.getAdapterPosition()).getItemNo().equals(list.get(i).getItemNo()))
                                list.get(holder.getAdapterPosition()).isClicked = 1;
                    }
                }
            }
        });*/
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
                       {  if (Float.parseFloat( ReturnByVoucherNo.Recoverallitemsdata.get(position).getQty())
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

                         }
                     }}}
            }
        });
    }

    private void updateqty(ReturnItemAdapter.SerialReportViewHolder holder,String itemNo) {
        for(int i=0;i<list.size();i++)
            if(list.get(i).getItemNo().equals(itemNo)&&list.get(i).getSerialCode().equals(""))
            {
                if(HaveSerial(list.get(i).getItemNo(),i) == 1) {
                    list.get(i).setQty(String.valueOf(Float.parseFloat(list.get(i).getQty()) + 1.0));


                    notifyItemChanged(i);
                }
                //yourMethodName(holder,i);
            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SerialReportViewHolder extends RecyclerView.ViewHolder {
        TextView price, vouchertype, serialcode, itemnum, itemName, export,voucherDate,customerName;
        EditText text_count;
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
}
