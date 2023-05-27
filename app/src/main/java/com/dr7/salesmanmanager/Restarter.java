package com.dr7.salesmanmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.dr7.salesmanmanager.Modles.MyServicesForloc;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Restarter","Restarter");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyServicesForloc.class));
        } else {
            context.startService(new Intent(context, MyServicesForloc.class));
        }
    }


}
