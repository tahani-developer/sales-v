package com.dr7.salesmanmanager.Interface;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.Login;
import com.dr7.salesmanmanager.Modles.RequstTest;
import com.dr7.salesmanmanager.Modles.SalesMenLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationDao {
   private DatabaseReference databaseReference;
   Context context;
   DatabaseHandler mHandler;
   String ipAddress="";
   public static String  Firebase_ipAddress;
   public LocationDao(Context context) {
      this.mHandler = new DatabaseHandler(context);
      this.context = context;
      FirebaseApp.initializeApp(context);
      FirebaseDatabase dbroot = FirebaseDatabase.getInstance();



      if(mHandler.getAllSettings().size() != 0) {
         ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
         Firebase_ipAddress= ipAddress.replace(".", "_");
         if(Firebase_ipAddress.contains(":"))  Firebase_ipAddress= Firebase_ipAddress.substring(0, Firebase_ipAddress.indexOf(":"));
         Log.e("ipAddress==",Firebase_ipAddress);
      }

      databaseReference = dbroot.getReference(SalesMenLocation.class.getSimpleName());








   }
   public void addLocation(SalesMenLocation salesMenLocation){

if(databaseReference==null) {
   FirebaseDatabase dbroot = FirebaseDatabase.getInstance();
   databaseReference = dbroot.getReference(SalesMenLocation.class.getSimpleName());
}

//      if(!ChildIsExists(salesMenLocation.getSalesmanNo())) {
         databaseReference.child(Firebase_ipAddress).child(salesMenLocation.getSalesmanNo()).setValue(salesMenLocation).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
//             Log.e("addLocation==", "databaseReference"+databaseReference.getRoot());
             Log.e("onSuccess==", "New salesMenLocation Data stored successfully");

            }
         });
         databaseReference.child(Firebase_ipAddress).child(salesMenLocation.getSalesmanNo()).setValue(salesMenLocation).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Log.e("Exception==", e.getMessage() + "");
            }
         });

//      }
//      else
//         Toast.makeText(context, "child is exsits", Toast.LENGTH_SHORT).show();

   }
   public boolean ChildIsExists(String value) {
      Log.e("ChildIsExists","ChildIsExists");
      final boolean[] flage = {false};
      databaseReference.child(Firebase_ipAddress).addListenerForSingleValueEvent(new ValueEventListener() {
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

}
