package com.dr7.salesmanmanager.Interface;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.Login;
import com.dr7.salesmanmanager.Modles.RequestAdmin;
import com.dr7.salesmanmanager.Modles.RequstTest;
import com.dr7.salesmanmanager.Modles.SalesMenLocation;
import com.dr7.salesmanmanager.MyServices;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.SalesInvoice;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.dr7.salesmanmanager.Activities.currentKey;
import static com.dr7.salesmanmanager.Activities.currentKeyTotalDiscount;
import static com.dr7.salesmanmanager.Activities.keyCreditLimit;
import static com.dr7.salesmanmanager.DiscountFragment.checkState;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.addToList;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.checkState_recycler;
import static com.dr7.salesmanmanager.SalesInvoice.checkState_LimitCredit;
import static com.dr7.salesmanmanager.SalesInvoice.discountRequest;

public class DaoRequsts {
    private DatabaseReference databaseReference,databaseReference2;
    Context context;
    DatabaseHandler mHandler;
String ipAddress="";
  public static String  Firebase_ipAddress;
 public static    String Key;
    public DaoRequsts(Context context) {
        this.mHandler = new DatabaseHandler(context);
        this.context = context;
        FirebaseDatabase dbroot = FirebaseDatabase.getInstance();



        if(mHandler.getAllSettings().size() != 0) {
            ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
            Firebase_ipAddress= ipAddress.replace(".", "_");
          if(Firebase_ipAddress.contains(":"))  Firebase_ipAddress= Firebase_ipAddress.substring(0, Firebase_ipAddress.indexOf(":"));
            Log.e("ipAddress==",Firebase_ipAddress);
        }
       // databaseReference = dbroot.getReference(RequestAdmin.class.getSimpleName());
        databaseReference2 = dbroot.getReference(RequstTest.class.getSimpleName());




        Log.e("addRequst==","addRequst");



    }
    public boolean ChildIsExists(String value) {
        Log.e("ChildIsExists","ChildIsExists");
        final boolean[] flage = {false};
        databaseReference2.child(Firebase_ipAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(value)) {
                    {
                        Log.e("true","true");
                        flage[0] =true;
                    }
                } else {
                    Log.e("false","false");
                    flage[0] =false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flage[0];  }
    public void add(RequestAdmin requsts) {
        Log.e("add==","add");
//        if (!ChildIsExists(requsts.getSalesman_no())) {
            databaseReference.child(ipAddress).setValue(requsts).addOnSuccessListener(new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "New requst Data stored successfully", Toast.LENGTH_SHORT).show();
                    Log.e("onSuccess==","add");
                }
            });
        databaseReference.setValue(requsts).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Exception==",e.getMessage()+"");
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
    public void addRequst(RequstTest requsts){
        Log.e("addRequst==", "addRequst");
       if(!ChildIsExists(requsts.getKey_validation())) {
           databaseReference2.child(Firebase_ipAddress).child(Login.salesMan).child(requsts.getKey_validation()).setValue(requsts).addOnSuccessListener(new OnSuccessListener<Void>() {

               @Override
               public void onSuccess(Void aVoid) {
                   Toast.makeText(context, "New requst Data stored successfully", Toast.LENGTH_SHORT).show();
                   Log.e("onSuccess==", "add");
                   try {
          //   getStatusofrequst(context);
                   }
                   catch (Exception e){

                   }
               }
           });
           databaseReference2.child(Firebase_ipAddress).child(Login.salesMan).child(requsts.getKey_validation()).setValue(requsts).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Log.e("Exception==", e.getMessage() + "");
               }
           });

       }
        else
        Toast.makeText(context, "child is exsits", Toast.LENGTH_SHORT).show();

    }
    public void  getStatusofrequst(Context context){

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getStatusofrequst,onDataChange", "onDataChange");
                // Get Post object and use the values to update the UI

             Log.e("keys==",currentKeyTotalDiscount+" , "+keyCreditLimit+" , "+currentKey);
            Key="";
             if(!currentKeyTotalDiscount.equals(""))Key=currentKeyTotalDiscount;
             else    if(!keyCreditLimit.equals(""))Key=keyCreditLimit;
             else    if(!currentKey.equals(""))Key=currentKey;


                RequstTest requstTest = dataSnapshot.child(Key).getValue(RequstTest.class);


if(requstTest!=null) {
    if (requstTest.getStatus() != null)
    {  if (requstTest.getStatus().equals("1")) {
            GeneralMethod.displayNotification(context, "Requst is Confirm", "");
        Log.e("requstTest.getStatus()==","1");
   deleteRequst(requstTest.getKey_validation());



        } else

    if (requstTest.getStatus().equals("2")) {
        GeneralMethod.displayNotification(context, "Requst is Reject", "");
        Log.e("requstTest.getStatus()==", "2");
      deleteRequst(requstTest.getKey_validation());
    }
        if((requstTest.getRequest_type().equals("0")||requstTest.getRequest_type().equals("2")) && checkState_recycler!=null)   checkState_recycler.setText(requstTest.getStatus().toString());
        if(requstTest.getRequest_type().equals("1")&&checkState!=null)  checkState.setText(requstTest.getStatus().toString());
        if(requstTest.getRequest_type().equals("10")&&checkState!=null)checkState.setText(requstTest.getStatus().toString());
            if(requstTest.getRequest_type().equals("100")&&checkState_LimitCredit!=null)     checkState_LimitCredit.setText(requstTest.getStatus().toString());


    }
}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("databaseError", "loadPost:onCancelled", databaseError.toException());
            }
        };
        databaseReference2.child(Firebase_ipAddress).child(Login.salesMan).addValueEventListener(postListener);

    }
    void deleteRequst(String RequstID){
        Log.e("deleteRequst", "deleteRequst:deleteRequst");
        databaseReference2
                .child(Firebase_ipAddress).child(Login.salesMan).child(RequstID)
                .removeValue();
    }
}
