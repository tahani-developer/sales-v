package com.dr7.salesmanmanager.Modles;

import static com.dr7.salesmanmanager.ShowCustomerNote.dateAll;

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

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SalesManCustomerNoteAdapter extends  RecyclerView.Adapter<SalesManCustomerNoteAdapter.SalesManPlanAdapterViewHolder>   {

  List<Customer> salesManPlans;
     Context context;
     DatabaseHandler databaseHandler;

    GeneralMethod generalMethod = null;

    public SalesManCustomerNoteAdapter(List<Customer> salesManPlans, Context context) {
        this.salesManPlans = salesManPlans;
        this.context = context;
        databaseHandler=new DatabaseHandler(context);
        try{
            generalMethod=new GeneralMethod(context);
        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public SalesManCustomerNoteAdapter.SalesManPlanAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salman_customer_note_row, parent, false);
        // Log.e("", "onCreateViewHolder");
        return new SalesManCustomerNoteAdapter.SalesManPlanAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesManCustomerNoteAdapter.SalesManPlanAdapterViewHolder holder, int position) {
     holder.order.setText(position+"");
        holder.cusname.setText(salesManPlans.get(position).getCustName()+"");


        if(salesManPlans.get(position).getStatus().equals("1")){
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


//                String serialGet=""+salesManPlans.get(position).getCustId()+generalMethod.getCurentTimeDate(1).replace("/","");
                String serialGet=""+salesManPlans.get(position).getCustId()+dateAll.replace("/","");
                NoteSalesPlane noteSalesPlane=databaseHandler.getNoteBySerial(""+serialGet);
              Log.e("notee",""+serialGet);
                try {
                    note.setText("" + noteSalesPlane.getNoteStart());
                    salesManPlans.get(position).setNoteS(noteSalesPlane.getNoteStart());
                    salesManPlans.get(position).setNoteE(noteSalesPlane.getNoteEnd());


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

                           if(dateAll.equals(generalMethod.getCurentTimeDate(1))) {
                               if (!note.getText().toString().equals("")) {
                                   String s = "-11";
                                   try {
                                       s = noteSalesPlane.getSerial();
                                       //    Log.e("SerialTex",""+noteSalesPlane.getPlaneSerial());

                                   } catch (Exception e) {
                                       s = "-11";
                                       Log.e("SerialTex", "-11");

                                   }

                                   if (TextUtils.isEmpty(s)) {
                                       String noteS = "", noteE = "";
                                       if (start.isChecked()) {
                                           noteS = note.getText().toString();
                                           noteE = "";
                                       } else {
                                           noteE = note.getText().toString();
                                           noteS = "";
                                       }

                                       NoteSalesPlane noteSalesPlane = new NoteSalesPlane();
                                       String serial = salesManPlans.get(position).getCustId() + generalMethod.getCurentTimeDate(1).replace("/", "");
                                       noteSalesPlane.setPlaneSerial("" + serial);
                                       Log.e("serial", "" + serial);
                                       noteSalesPlane.setEditEnd("0");
                                       noteSalesPlane.setNoteStart(noteS);
                                       noteSalesPlane.setNoteEnd(noteE);

                                       noteSalesPlane.setDate(generalMethod.getCurentTimeDate(1));
                                       noteSalesPlane.setEditStart("0");
                                       noteSalesPlane.setCustomerId(salesManPlans.get(position).getCustId());

                                       databaseHandler.insertNote(noteSalesPlane);
                                       noteForPlan.dismiss();
                                       Toast.makeText(context, "Save Successful", Toast.LENGTH_LONG).show();
                                   } else {

                                       if (start.isChecked()) {

                                           databaseHandler.updateNote(note.getText().toString(), "1", "" + salesManPlans.get(position).getCustId() + generalMethod.getCurentTimeDate(1).replace("/", ""));
                                       } else {
                                           databaseHandler.updateNote(note.getText().toString(), "0", "" + salesManPlans.get(position).getCustId() + generalMethod.getCurentTimeDate(1).replace("/", ""));

                                       }
                                       noteForPlan.dismiss();
                                       Toast.makeText(context, "update Successful", Toast.LENGTH_LONG).show();
                                   }

                                   try {
//                                if(start.isChecked()) {
//                                    salesManPlans.get(position).setStartNote(note.getText().toString());
//                                }else{
//                                    salesManPlans.get(position).setEndNote(note.getText().toString());
//
//                                }

                                   } catch (Exception e) {

                                   }
                               } else {
                                   note.setError("Required!");
                               }
                           }else {
                               Toast.makeText(context, "Save And Update In Same Date Only", Toast.LENGTH_LONG).show();
                           }
                    }
                });

                noteForPlan.show();
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
