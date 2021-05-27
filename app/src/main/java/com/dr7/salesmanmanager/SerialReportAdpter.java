package com.dr7.salesmanmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.SerialReport;

import java.util.ArrayList;
import java.util.List;

public class SerialReportAdpter extends   RecyclerView.Adapter<SerialReportAdpter.SerialReportViewHolder >{
    private List<serialModel> list;
    private List<serialModel> serialrowlist=new ArrayList<>();
    Context context;

    public SerialReportAdpter(List<serialModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SerialReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.serialrow, parent, false);
        return new SerialReportAdpter.SerialReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SerialReportViewHolder holder, int position) {
        holder.voucherdate.setText(list.get(position).getDateVoucher());
        holder.vouchertype.setText(String.valueOf(list.get(position).getKindVoucher()));
        holder.serialcode.setText(String.valueOf(list.get(position).getSerialCode()));
        holder.itemnum.setText(String.valueOf(list.get(position).getItemNo()));
        holder.vouchernum.setText(String.valueOf(list.get(position).getVoucherNo()));
        holder.export.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SerialReportViewHolder extends RecyclerView.ViewHolder{
        TextView voucherdate, vouchertype,serialcode,itemnum,vouchernum,export;
        public SerialReportViewHolder(@NonNull View itemView) {

            super(itemView);
            voucherdate=itemView.findViewById(R.id.SE_voucherdate);
            vouchertype=itemView.findViewById(R.id.SE_vocherkind);
            serialcode =itemView.findViewById(R.id.SE__serialcode);
            itemnum=itemView.findViewById(R.id.SE__itemnum);
            vouchernum=itemView.findViewById(R.id.SE_vochernum);
            export=itemView.findViewById(R.id.SE_exportexcel);
            export.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exportToEx();
                }
            });

        }
        private void exportToEx() {
            ExportToExcel exportToExcel=new ExportToExcel();
            final String tag = export.getTag().toString();
            serialModel serialModel = new serialModel();
            serialModel.setDateVoucher(list.get(Integer.parseInt(tag)).getDateVoucher());
            serialModel.setKindVoucher(list.get(Integer.parseInt(tag)).getKindVoucher());
            serialModel.setSerialCode(list.get(Integer.parseInt(tag)).getSerialCode());
            serialModel.setItemNo(list.get(Integer.parseInt(tag)).getItemNo());
            serialModel.setVoucherNo(list.get(Integer.parseInt(tag)).getVoucherNo());
            serialrowlist.add(serialModel);
            exportToExcel.createExcelFile(context,"Reportserial.xls",11,serialrowlist);

        }

    }

}
