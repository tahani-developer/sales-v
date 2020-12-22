package com.dr7.salesmanmanager;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import androidx.core.content.ContextCompat;

import com.dr7.salesmanmanager.Reports.Reports;

import java.util.Locale;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class LocaleAppUtils {
    private static Locale locale;
    private static DatabaseHandler mHandler;

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale localeIn) {
        locale = localeIn;
        if(locale != null) {
            Locale.setDefault(locale);
        }
    }
    public static void setConfigChange(Context ctx){
        if(locale != null){
            Locale.setDefault(locale);

            Configuration configuration = ctx.getResources().getConfiguration();
            DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
            configuration.locale=locale;

            ctx.getResources().updateConfiguration(configuration, displayMetrics);
        }
    }
    public static void changeLayot(Context context){
        mHandler = new DatabaseHandler(context);
        if (mHandler.getAllSettings().size() != 0) {
            if (mHandler.getAllSettings().get(0).getArabic_language() == 1) {
                languagelocalApp="ar";
                LocaleAppUtils.setLocale(new Locale("ar"));
                LocaleAppUtils.setConfigChange(context);

            } else {
                languagelocalApp="en";
                LocaleAppUtils.setLocale(new Locale("en"));
                LocaleAppUtils.setConfigChange(context);

            }
        }
        else {
            languagelocalApp="ar";
            LocaleAppUtils.setLocale(new Locale("ar"));
            LocaleAppUtils.setConfigChange(context);

        }



    }
}
