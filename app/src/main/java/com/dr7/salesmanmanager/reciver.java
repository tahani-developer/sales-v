package com.dr7.salesmanmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class reciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, Login.class);
        context.startActivity(intent);
    }
}
