package com.dr7.salesmanmanager.Adapters;

import static com.dr7.salesmanmanager.Reports.SerialReport.allseriallist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportToExcel;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.SerialReportAdpter;

import java.util.ArrayList;
import java.util.List;

public class ReturnItemAdapter extends   RecyclerView.Adapter<ReturnItemAdapter.SerialReportViewHolder >{
    private List<serialModel> list;
    private List<String> vocherlist=new ArrayList<>();;
    private List<serialModel> serialrowlist=new ArrayList<>();
    Context context;
    View linearLayout;
    int shelfReport=0;
    DatabaseHandler databaseHandler;

    public ReturnItemAdapter(List<serialModel> list, Context context,int typeReport) {
        this.list = list;
        this.context = context;
        shelfReport=typeReport;
        databaseHandler=new DatabaseHandler(context);
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


        Log.e("getItemNo","no="+list.get(position).getItemNo());
        holder.serialcode.setText(String.valueOf(list.get(position).getSerialCode()));
//        if(list.get(position).getItemName().equals("null"))
//        {
//            holder.itemName.setText(databaseHandler.getItemName(list.get(position).getItemNo()));
//        }else {
            holder.itemName.setText(String.valueOf(list.get(position).getItemName()));
//        }

        holder.itemnum.setText(String.valueOf(list.get(position).getItemNo()));
        holder.price.setText(String.valueOf(list.get(position).getPriceItem()));

        holder.linearSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("linearSelected","bb"+b);
                if(b)
                {
                    list.get(position).isClicked=1;
                }else {
                    list.get(position).isClicked=0;
                }
                Log.e("linearSelected","position="+list.get(position).isClicked);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SerialReportViewHolder extends RecyclerView.ViewHolder {
        TextView price, vouchertype, serialcode, itemnum, itemName, export,voucherDate,customerName;
        CheckBox linearSelected;

        public SerialReportViewHolder(@NonNull View itemView) {

            super(itemView);
            price = itemView.findViewById(R.id.text_price);
            vouchertype = itemView.findViewById(R.id.SE_vocherkind);
            voucherDate= itemView.findViewById(R.id.text_voucherdate);
            serialcode = itemView.findViewById(R.id.SE__serialcode);
            itemnum = itemView.findViewById(R.id.SE__itemnum);

            linearLayout = itemView.findViewById(R.id.lin);

            linearSelected=itemView.findViewById(R.id.linearSelected);

            itemName=itemView.findViewById(R.id.itemName);

        }


    }
}
