package com.dr7.salesmanmanager;

import android.content.Context;

public class GeneralMethod {
    Context  context;
    DatabaseHandler databaseHandler;

    public GeneralMethod(Context context) {
        this.context = context;
        databaseHandler=new DatabaseHandler(context);
    }

    public  String getSalesManLogin(){
        return  databaseHandler.getAllUserNo();
    }
}
