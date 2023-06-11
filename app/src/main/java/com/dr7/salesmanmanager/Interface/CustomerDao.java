package com.dr7.salesmanmanager.Interface;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dr7.salesmanmanager.DatabaseHandler;

import com.dr7.salesmanmanager.Modles.NewAddedCustomer;
import com.dr7.salesmanmanager.Modles.RequestAdmin;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerDao {
    private DatabaseReference databaseReference;
    Context context;
    DatabaseHandler mHandler;
    String ipAddress="";
    public static String  Firebase_ipAddress;
    public static    String Key,LogedReq_Key="",ReturnVochReq_Key;
    public CustomerDao(Context context) {
        this.mHandler = new DatabaseHandler(context);
        this.context = context;
        FirebaseDatabase dbroot = FirebaseDatabase.getInstance();



        if(mHandler.getAllSettings().size() != 0) {
            ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
            Firebase_ipAddress= ipAddress.replace(".", "_");
            if(Firebase_ipAddress.contains(":"))
                Firebase_ipAddress= Firebase_ipAddress.substring(0, Firebase_ipAddress.indexOf(":"));

        }
        databaseReference = dbroot.getReference(NewAddedCustomer.class.getSimpleName());





    }
    public void add(NewAddedCustomer addedCustomer) {

        databaseReference.child(Firebase_ipAddress).child(addedCustomer.getTELEPHONE()).setValue(addedCustomer).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Customer data has been sent to the Admin successfully", Toast.LENGTH_SHORT).show();

            }
        });
        databaseReference.child(Firebase_ipAddress).child(addedCustomer.getTELEPHONE()).setValue(addedCustomer).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
//        }
//        else {
//            Log.e("onnotSuccess==","notadd");
//            Toast.makeText(context, "Child Is Exists", Toast.LENGTH_SHORT).show();
//        }
        //databaseReference.setValue(requsts);
//    databaseReference.child("22_22_22_22_295").child(requsts.getId()).setValue(requsts).addOnCompleteListener(this, new OnCompleteListener<Void>() {
//
//        @Override
//        public void onComplete(@NonNull Task<Void> task) {
//            if (task.isSuccessful()) {
//
//                Toast.makeText(context, "New requst Data stored successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "New requst Data does not stored successfully", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    });
    }
}
