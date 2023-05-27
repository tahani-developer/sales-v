package com.dr7.salesmanmanager.Interface;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dr7.salesmanmanager.Activities;
import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.Login;
import com.dr7.salesmanmanager.Modles.RequestAdmin;
import com.dr7.salesmanmanager.Modles.RequstTest;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.ReturnByVoucherNo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.dr7.salesmanmanager.Activities.currentKey;
import static com.dr7.salesmanmanager.Activities.currentKeyTotalDiscount;
import static com.dr7.salesmanmanager.Activities.keyCreditLimit;
import static com.dr7.salesmanmanager.DiscountFragment.checkState;
import static com.dr7.salesmanmanager.RecyclerViewAdapter.checkState_recycler;
import static com.dr7.salesmanmanager.SalesInvoice.checkState_LimitCredit;

public class DaoRequsts {
    private DatabaseReference databaseReference,databaseReference2;
    Context context;
    DatabaseHandler mHandler;
String ipAddress="";
  public static String  Firebase_ipAddress;
 public static    String Key,LogedReq_Key="",ReturnVochReq_Key;

    public static ArrayList<RequstTest> AllowedCut_List=new ArrayList<>();
    public static ArrayList<RequstTest> AllRequsts=new ArrayList<>();
    public DaoRequsts(Context context) {
        this.mHandler = new DatabaseHandler(context);
        this.context = context;
        FirebaseDatabase dbroot = FirebaseDatabase.getInstance();



        if(mHandler.getAllSettings().size() != 0) {
            ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
            Firebase_ipAddress= ipAddress.replace(".", "_");
          if(Firebase_ipAddress.contains(":"))  Firebase_ipAddress= Firebase_ipAddress.substring(0, Firebase_ipAddress.indexOf(":"));
          //  Log.e("ipAddress==",Firebase_ipAddress);
        }
       // databaseReference = dbroot.getReference(RequestAdmin.class.getSimpleName());
        databaseReference2 = dbroot.getReference(RequstTest.class.getSimpleName());




      //  Log.e("addRequst==","addRequst");



    }
    public boolean ChildIsExists(String value) {
    //    Log.e("ChildIsExists","ChildIsExists");
        final boolean[] flage = {false};
        databaseReference2.child(Firebase_ipAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(value)) {
                    {
                      //  Log.e("true",snapshot.getRef()+" "+snapshot.getValue());
                        flage[0] =true;
                    }
                } else {
              //      Log.e("false","false");
                    flage[0] =false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flage[0];
    }
    public void add(RequestAdmin requsts) {
      //  Log.e("add==","add");
//        if (!ChildIsExists(requsts.getSalesman_no())) {
            databaseReference.child(ipAddress).setValue(requsts).addOnSuccessListener(new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "New requst Data stored successfully", Toast.LENGTH_SHORT).show();
                 //   Log.e("onSuccess==","add");
                }
            });
        databaseReference.setValue(requsts).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             //   Log.e("Exception==",e.getMessage()+"");
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
                   Toast.makeText(context, context.getResources().getString(R.string.sendsuccess), Toast.LENGTH_SHORT).show();
               //    Log.e("onSuccess==", "add");
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
                 //  Log.e("Exception==", e.getMessage() + "");
               }
           });

       }
        else
        Toast.makeText(context, "child is exsits", Toast.LENGTH_SHORT).show();

    }
    public void  getStatusofrequst(Context context){
try {
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        //    Log.e("getStatusofrequst,onDataChange", "onDataChange");
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
                    GeneralMethod.displayNotification(context, ""+context.getResources().getString(R.string.acceptedRequest), "");
               //     Log.e("requstTest.getSta","1");
                    deleteRequst(requstTest.getKey_validation());



                } else

                if (requstTest.getStatus().equals("2")) {
                    GeneralMethod.displayNotification(context, ""+context.getResources().getString(R.string.rejectedRequest), "");
             //       Log.e("requstTest.getSt", "2");
                    deleteRequst(requstTest.getKey_validation());
                }
                    if((requstTest.getRequest_type().equals("0")||requstTest.getRequest_type().equals("2")) && checkState_recycler!=null)   checkState_recycler.setText(requstTest.getStatus().toString());
                    if(requstTest.getRequest_type().equals("1")&&checkState!=null)  checkState.setText(requstTest.getStatus().toString());
                    if(requstTest.getRequest_type().equals("10")&&checkState!=null)checkState.setText(requstTest.getStatus().toString());
                    if(requstTest.getRequest_type().equals("100")&&checkState_LimitCredit!=null)     checkState_LimitCredit.setText(requstTest.getStatus().toString());
                    if(requstTest.getRequest_type().equals("506")&&ReturnByVoucherNo.ReturnPerm_respon!=null)     ReturnByVoucherNo.ReturnPerm_respon.setText(requstTest.getStatus().toString());


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

}catch (Exception e){
    Log.e("getStatusofrequst,Exception==",""+e.getMessage());
}

    }
    public void  getStatusofReturnvochrequst(String Key,Context context){
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //    Log.e("getStatusofrequst,onDataChange", "onDataChange");
                    // Get Post object and use the values to update the UI

                    //   Log.e("keys==",currentKeyTotalDiscount+" , "+keyCreditLimit+" , "+currentKey);



                    RequstTest requstTest = dataSnapshot.child(Key).getValue(RequstTest.class);


                    if(requstTest!=null) {
                        if (requstTest.getStatus() != null)
                        {  if (requstTest.getStatus().equals("1")) {
                            GeneralMethod.displayNotification(context, ""+context.getResources().getString(R.string.acceptedRequest), "");
                            //     Log.e("requstTest.getSta","1");
                            deleteRequst(requstTest.getKey_validation());



                        } else

                        if (requstTest.getStatus().equals("2")) {
                            GeneralMethod.displayNotification(context, ""+context.getResources().getString(R.string.rejectedRequest), "");
                            //       Log.e("requstTest.getSt", "2");
                            deleteRequst(requstTest.getKey_validation());
                        }
                              if(requstTest.getRequest_type().equals("506")&&ReturnByVoucherNo.ReturnPerm_respon!=null)     ReturnByVoucherNo.ReturnPerm_respon.setText(requstTest.getStatus().toString());


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

        }catch (Exception e){
            Log.e("getStatusofrequst,Exception==",""+e.getMessage());
        }

    }
    public void  getStatusofLogedrequsts(Context context){
       // Log.e("getStatusofLogedrequsts==","getStatusofLogedrequsts");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
           //         Log.e("key==",key+"");
                    getlistofdata(key);
//                    DatabaseReference usersRef = rootRef.child("users").child(key);
//                    ValueEventListener eventListener = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for(DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
//                                String username = dSnapshot.child("username").getValue(String.class);
//                                Log.d("TAG", username);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {}
//                    };
//                    usersRef.addListenerForSingleValueEvent(eventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference2.child(Firebase_ipAddress).child(Login.salesMan).addListenerForSingleValueEvent(valueEventListener);




        //////



    }
    void oldmethod(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
         //       Log.e("getStatusofLogedrequsts,onDataChange", "getStatusofLogedrequsts");
                // Get Post object and use the values to update the UI

                RequstTest requstTest = dataSnapshot.getValue(RequstTest.class);

           //     Log.e("requstTest,getRef", dataSnapshot.child(Login.salesMan).getRef()+ "");
                if(requstTest!=null) {
        //            Log.e("requstTest,requstTest",requstTest.getStatus()+ "");
                    if (requstTest.getStatus() != null)
                    {
                        if(requstTest.getRequest_type().equals("5")) {
                    //       Log.e("requstTest,getRequest_type", "5");
                            if (requstTest.getStatus().equals("1")) {
                 //               Log.e("getStatus,getStatus", "1");



                            } else if (requstTest.getStatus().equals("2")) {
                              //  Log.e("getStatus,getStatus", "2");

                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("databaseError", "loadPost:onCancelled", databaseError.toException());
            }
        };
        databaseReference2.child(Firebase_ipAddress).addValueEventListener(postListener);
    }
    public void getlistofAllrequst() {
        Log.e("getlistofdata==", "getlistofdata");
        AllowedCut_List.clear();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
             //   Log.e("onChildAdded", "getKey:" + dataSnapshot.getKey());
                // A new comment has been added, add it to the displayed list
                try {


                    RequstTest requstTest = dataSnapshot.getValue(RequstTest.class);
                    if(requstTest!=null) {
                    //    Log.e("requstTest,requstTest",requstTest.getStatus()+ "");
                        AllRequsts.add(requstTest);
                    }
                }catch (Exception e){
                    Log.e("Exception", "Exception:" + e.getMessage());
                }

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {



                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference2.child(Firebase_ipAddress).child(Login.salesMan).addChildEventListener(childEventListener);

    }
    private void getlistofdata(String key) {
      //  Log.e("getlistofdata==", "getlistofdata");
        AllowedCut_List.clear();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            //    Log.e("onChildAdded", "getKey:" + dataSnapshot.getKey());
                // A new comment has been added, add it to the displayed list
                try {


                    RequstTest requstTest = dataSnapshot.getValue(RequstTest.class);
                    if(requstTest!=null) {
                     //   Log.e("requstTest,requstTest",requstTest.getStatus()+ "");
                        if (requstTest.getStatus() != null)
                        {
                            if(requstTest.getRequest_type().equals("5")) {
                              //  Log.e("requstTest,getRequest_type", "5");
//                                if (requstTest.getStatus().equals("1"))
                                {
                             //       Log.e("getStatus,getStatus", "1");
                                    AllowedCut_List.add(requstTest);
                               //     Log.e("getStatus,AllowedCut_List", AllowedCut_List.size()+"");

                                }

                            }
                        }
                    }
                }catch (Exception e){
                    Log.e("Exception", "Exception:" + e.getMessage());
                }

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
              //  Log.e("onChildAdded", "onChildChanged:" + dataSnapshot.getKey());
                try {    RequstTest requstTest = dataSnapshot.getValue(RequstTest.class);
                for (int i=0;i<DaoRequsts.AllowedCut_List.size();i++){
                    if (DaoRequsts.AllowedCut_List.get(i).getKey_validation().equals(requstTest.getKey_validation()))
                        DaoRequsts.AllowedCut_List.get(i).setStatus(requstTest.getStatus());
                }

            }catch (Exception e){
                Log.e("Exception", "Exception:" + e.getMessage());
            }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("onChildAdded", "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                try {    RequstTest requstTest = dataSnapshot.getValue(RequstTest.class);
                    for (int i=0;i<DaoRequsts.AllowedCut_List.size();i++){
                        if (DaoRequsts.AllowedCut_List.get(i).getKey_validation().equals(requstTest.getKey_validation())) {
                            DaoRequsts.AllowedCut_List.remove(i);
                            i--;
                        }
                    }

                }catch (Exception e){
                    Log.e("Exception", "Exception:" + e.getMessage());
                }


                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.e("onChildAdded", "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onChildAdded", "postComments:onCancelled", databaseError.toException());


            }
        };
        databaseReference2.child(Firebase_ipAddress).child(Login.salesMan).addChildEventListener(childEventListener);

    }


    public void  getStatusofLogedrequst(Context context){

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getStatusofLogedrequst,onDataChange", "onDataChange");
                // Get Post object and use the values to update the UI


                RequstTest requstTest = dataSnapshot.child(LogedReq_Key).getValue(RequstTest.class);


                if(requstTest!=null) {
               //     Log.e("getStatusofLogedrequst,requstTest", "requstTest");
                    if (requstTest.getStatus() != null)
                    {  if (requstTest.getStatus().equals("1")) {
                        GeneralMethod.displayNotification(context, ""+context.getResources().getString(R.string.acceptedRequest), "");
                 //       Log.e("requstTest.getSta","1");
                    //    deleteRequst(requstTest.getKey_validation());
//                      CustomerCheckInFragment.AllowedCut_List.add(requstTest.getCustomer_no());


                    } else

                    if (requstTest.getStatus().equals("2")) {
                        GeneralMethod.displayNotification(context, ""+context.getResources().getString(R.string.rejectedRequest), "");
                        Log.e("requstTest.getSt", "2");
//                        deleteRequst(requstTest.getKey_validation());
                    }


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

   public void  deleteRequst(String RequstID){
        //Log.e("deleteRequst", "deleteRequst:deleteRequst");
        databaseReference2
                .child(Firebase_ipAddress).child(Login.salesMan).child(RequstID)
                .removeValue();
    }
}
