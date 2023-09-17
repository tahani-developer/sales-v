package com.dr7.salesmanmanager.Modles;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Adapters.AccountStatmentAdapter;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.R;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SalesManPlanAdapter extends  RecyclerView.Adapter<SalesManPlanAdapter.SalesManPlanAdapterViewHolder>   {

     ArrayList<SalesManPlan>salesManPlans;
     Context context;
     DatabaseHandler databaseHandler;

    public SalesManPlanAdapter(ArrayList<SalesManPlan> salesManPlans, Context context) {
        this.salesManPlans = salesManPlans;
        this.context = context;
        databaseHandler=new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public SalesManPlanAdapter.SalesManPlanAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salman_plan_row, parent, false);
        // Log.e("", "onCreateViewHolder");
        return new SalesManPlanAdapter.SalesManPlanAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesManPlanAdapter.SalesManPlanAdapterViewHolder holder, int position) {
     holder.order.setText(position+"");
        holder.cusname.setText(salesManPlans.get(position).getCustName()+"");


        if(salesManPlans.get(position).getLogoutStatus()==1){
            holder.linRow.setBackgroundColor(context.getResources().getColor(R.color.green_color));

        }else {
           holder. linRow.setBackgroundColor(context.getResources().getColor(R.color.white_alphe));

        }

        holder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog noteForPlan=new Dialog(context);
                noteForPlan.requestWindowFeature(Window.FEATURE_NO_TITLE);
                noteForPlan.setCancelable(true);
                noteForPlan.setContentView(R.layout.note_plan_dialog);
                noteForPlan.setCanceledOnTouchOutside(true);

                TextView CustName=noteForPlan.findViewById(R.id.CustName);
                RadioButton end=noteForPlan.findViewById(R.id.endN);
                RadioButton start=noteForPlan.findViewById(R.id.starN);
                EditText note=noteForPlan.findViewById(R.id.note);
                Button save=noteForPlan.findViewById(R.id.saveN);

                CustName.setText(salesManPlans.get(position).getCustName());


                NoteSalesPlane noteSalesPlane=databaseHandler.getNoteBySerial(""+salesManPlans.get(position).getSerial());
                try {
                    note.setText("" + noteSalesPlane.getNoteStart());

                }catch (Exception e){

                }
                start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b){
                            if(noteSalesPlane!=null){

                                note.setText(""+noteSalesPlane.getNoteStart());
                            }

                        }else {

                            if(noteSalesPlane!=null){

                                note.setText(""+noteSalesPlane.getNoteEnd());
                            }

                        }

                    }
                });




                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!note.getText().toString().equals("")){
                            String s="-11";
                            try{
                              s=   noteSalesPlane.getSerial();
                            //    Log.e("SerialTex",""+noteSalesPlane.getPlaneSerial());

                            }catch (Exception e){
                                s="-11";
                                Log.e("SerialTex","-11");

                            }

                            if(TextUtils.isEmpty(s)) {
                                String noteS = "", noteE = "";
                                if (start.isChecked()) {
                                    noteS = note.getText().toString();
                                    noteE = "";
                                } else {
                                    noteE = note.getText().toString();
                                    noteS = "";
                                }

                                NoteSalesPlane noteSalesPlane = new NoteSalesPlane();
                                noteSalesPlane.setPlaneSerial("" + salesManPlans.get(position).getSerial());
                                Log.e("serial",""+salesManPlans.get(position).getSerial());
                                noteSalesPlane.setEditEnd("0");
                                noteSalesPlane.setNoteStart(noteS);
                                noteSalesPlane.setNoteEnd(noteE);
                                noteSalesPlane.setDate(salesManPlans.get(position).getDate());
                                noteSalesPlane.setEditStart("0");
                                noteSalesPlane.setCustomerId(salesManPlans.get(position).getCustNumber());

                                databaseHandler.insertNote(noteSalesPlane);
                                noteForPlan.dismiss();
                                Toast.makeText(context, "Save Successful", Toast.LENGTH_LONG).show();
                            }else {

                                if(start.isChecked()) {

                                    databaseHandler.updateNote(note.getText().toString(), "1",""+salesManPlans.get(position).getSerial());
                                }else{
                                    databaseHandler.updateNote(note.getText().toString(), "0",""+salesManPlans.get(position).getSerial());

                                }
                                noteForPlan.dismiss();
                                Toast.makeText(context, "update Successful", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            note.setError("Required!");
                        }

                    }
                });

                noteForPlan.show();
            }
        });

 holder.cuslocation.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Log.e("onClick","onClick");
         if(salesManPlans.get(position).getLatitud()!=0) {

             if(salesManPlans.get(position).getLatitud()!=0) {
                 String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f",
                        salesManPlans.get(position).getLatitud(),
                   salesManPlans.get(position).getLongtude());
                 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                 context.startActivity(intent);
             }
             else {
                 new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                         .setTitleText(context.getResources().getString(R.string.Noloction))
                         .setContentText("")
                         .show();
             }
         }else{
             new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                     .setTitleText(context.getResources().getString(R.string.Noloction))
                     .setContentText("")
                     .show();
         }
     }
 });
    }

    @Override
    public int getItemCount() {
        return salesManPlans.size();
    }


  public   class SalesManPlanAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView order,cusname,cuslocation,note;
        LinearLayout linRow;
        public SalesManPlanAdapterViewHolder(View itemView) {
            super(itemView);

            order = itemView.findViewById(R.id.order);
            cusname = itemView.findViewById(R.id.cusname);
            cuslocation = itemView.findViewById(R.id.cuslocation);
            linRow=itemView.findViewById(R.id.linRow);
            note=itemView.findViewById(R.id.note);


            //Log.e("", "ViewHolder const");

        }
    }



}
