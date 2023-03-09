package com.dr7.salesmanmanager.Reports;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportJason;
import com.dr7.salesmanmanager.ImportJason;
import com.dr7.salesmanmanager.LocaleAppUtils;
import com.dr7.salesmanmanager.Login;
import com.dr7.salesmanmanager.MainActivity;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.TransactionsInfo;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.ShowPlan;

import org.json.JSONException;

import java.util.List;
import java.util.Locale;

import static com.dr7.salesmanmanager.Login.Purchase_Order;
import static com.dr7.salesmanmanager.Login.languagelocalApp;


public class Reports extends AppCompatActivity {


    LinearLayout showplanLin,customer_log_report,transactions_report,stock_request_report,Inventory_report,cash_reoprt,
            custperformance  , return_report,serial_report,shelf_inventory_report,custwithouttrans_report,Target_Report;

    DatabaseHandler MHandler;
    LinearLayout inventory_layout,mainLayout;
    List<Settings> settings;
//    private Toolbar toolbar;
    /*   List<Settings> settings =  mHandler.getAllSettings();
        System.setProperty("http.keepAlive", "false");
        if(settings.size() != 0) {
        */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MHandler = new DatabaseHandler(Reports.this);
        settings =  MHandler.getAllSettings();
        new LocaleAppUtils().changeLayot(Reports.this);

        setContentView(R.layout.activity_reports);
        mainLayout=findViewById(R.id.mainLayout);

        Log.e("languagelocalApp2",""+languagelocalApp);
        try{
            if(languagelocalApp.equals("ar"))
            {
                mainLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    mainLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            mainLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        customer_log_report = (LinearLayout) findViewById(R.id.customer_log_report);
        transactions_report = (LinearLayout) findViewById(R.id.transactions_report);
        custperformance= (LinearLayout) findViewById(R.id.custperformance);
        return_report = (LinearLayout) findViewById(R.id.return_report);
        Inventory_report = (LinearLayout) findViewById(R.id.inventory_report);
        serial_report = (LinearLayout) findViewById(R.id.serial_report);
        stock_request_report = (LinearLayout) findViewById(R.id.stock_request_report);
        cash_reoprt = (LinearLayout) findViewById(R.id.cash_report);
        shelf_inventory_report=findViewById(R.id.shelf_inventory_report);
        shelf_inventory_report.setOnClickListener(onClickListener);
        custwithouttrans_report=findViewById(R.id.custwithouttrans);
        Target_Report=findViewById(R.id.TargetReport);
        Target_Report.setOnClickListener(onClickListener);
        custwithouttrans_report.setOnClickListener(onClickListener);
//          if( Purchase_Order==0)   custwithouttrans_report.setVisibility(View.GONE);
//          if(MHandler.getAllSettings().get(0).getEndTripReport()==1)
//              custwithouttrans_report.setVisibility(View.VISIBLE);
        customer_log_report.setOnClickListener(onClickListener);
        custperformance.setOnClickListener(onClickListener);
        serial_report.setOnClickListener(onClickListener);
        cash_reoprt.setOnClickListener(onClickListener);
        transactions_report.setOnClickListener(onClickListener);
        return_report.setOnClickListener(onClickListener);
        stock_request_report.setOnClickListener(onClickListener);
        Inventory_report.setOnClickListener(onClickListener);
        showplanLin=findViewById(R.id.showplan);
        showplanLin.setOnClickListener(onClickListener);
     if(Login.SalsManPlanFlage==0)   showplanLin.setVisibility(View.GONE);
     else  showplanLin.setVisibility(View.VISIBLE);
        inventory_layout=(LinearLayout) findViewById(R.id.inventory_layout);
        if(settings.size() != 0) {
            if (MHandler.getAllSettings().get(0).getHide_qty() == 1) {
                inventory_layout.setVisibility(View.GONE);
            }
        }


    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.customer_log_report:
                    Intent intent1 = new Intent(Reports.this, CustomerLogReport.class);
                    startActivity(intent1);
                    break ;

                case R.id.transactions_report:
                    Intent intent2 = new Intent(Reports.this, TransactionsReport.class);
                    startActivity(intent2);
                    break ;

                case R.id.return_report:
                    Intent intent3 = new Intent(Reports.this, PaymentDetailsReport.class);
                    startActivity(intent3);
                    break ;

                case R.id.stock_request_report:
                    Intent intent4 = new Intent(Reports.this, StockRequestReport.class);
                    startActivity(intent4);
                    break ;
                case R.id.inventory_report:
                    Intent intent5 = new Intent(Reports.this, InventoryReport.class);
                    startActivity(intent5);
                    break ;

                case R.id.serial_report:
                {Intent intent = new Intent(Reports.this, SerialReport.class);
                    startActivity(intent);
                    break ;}

                case R.id.cash_report:
                    if(settings.size()!=0){
                        if(MHandler.getAllSettings().get(0).getLock_cashreport()==1) {
                            openPassowrdDialog();

                        }
                        else{
                            Intent intent6 = new Intent(Reports.this, CashReport.class);
                            startActivity(intent6);
                        }
                    }

                    else{
                        Intent intent6 = new Intent(Reports.this, CashReport.class);
                        startActivity(intent6);
                    }
                    break ;
                case R.id.shelf_inventory_report:
                    Intent intent8 = new Intent(Reports.this, ShelfInventoeryReport.class);
                    startActivity(intent8);
                    break;

                case R.id. showplan:
                    Intent intent9 = new Intent(Reports.this, ShowPlan.class);
                    startActivity(intent9);
                    break;
                case R.id. custwithouttrans:
                    Intent intent10 = new Intent(Reports.this, CustomerWithoutTrasn_Report.class);
                    startActivity(intent10);
                    break;
                case R.id.TargetReport:
                    Intent intent11 = new Intent(Reports.this, TargetReport.class);
                    startActivity(intent11);
                    break;
                case R.id.custperformance:
                    Intent intent12 = new Intent(Reports.this, CustomersPerformanceReport.class);
                    startActivity(intent12);
                    break;

            }

        }

    };

    private void openPassowrdDialog() {

        final Dialog dialog = new Dialog(Reports.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.password_dialog);

        final EditText password = (EditText) dialog.findViewById(R.id.editText1);
        Button okButton = (Button) dialog.findViewById(R.id.button1);
        Button cancelButton = (Button) dialog.findViewById(R.id.button2);
        final CheckBox cb_show = (CheckBox) dialog.findViewById(R.id.checkBox_showpass);
//        EditText et1 = (EditText) this.findViewById(R.id.editText1);
        cb_show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_show.isChecked()) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(129);
                }
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(Login.Mainpassword_setting)) {
                    dialog.dismiss();
                    Intent intent6 = new Intent(Reports.this, CashReport.class);
                    startActivity(intent6);


                    } else
                        Toast.makeText(Reports.this, "Incorrect Password !", Toast.LENGTH_SHORT).show();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    public void onCheckBox(View v2)
    {



        CheckBox cb = (CheckBox)this.findViewById(R.id.checkBox_showpass);
        EditText et1=(EditText)this.findViewById(R.id.editText1);
        if(cb.isChecked())
        {
            et1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        else
        {
            et1.setInputType(129);
        }

    }
    }

