package com.dr7.salesmanmanager.Reports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.dr7.salesmanmanager.LocaleAppUtils;
import com.dr7.salesmanmanager.R;

public class TransactionsReport extends AppCompatActivity {

    CardView vouchersReportCardView ;
    CardView itemsReportsCardView ;
    LinearLayout itemsReportsLinearLayout ;
    LinearLayout voucherReportLinearLayout ;
    Animation animationenter,animat_exit;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LocaleAppUtils().changeLayot(TransactionsReport.this);
        setContentView(R.layout.transactions_report);
        animationenter = AnimationUtils.loadAnimation(getBaseContext(), R.anim.enter);
        animat_exit = AnimationUtils.loadAnimation(getBaseContext(), R.anim.exit);
        vouchersReportCardView = (CardView) findViewById(R.id.voucherReportCardView);
        itemsReportsCardView = (CardView) findViewById(R.id.itemsReportsCardView2);
        voucherReportLinearLayout = (LinearLayout)  findViewById(R.id.voucherReportLinearLayout);
        itemsReportsLinearLayout = (LinearLayout)  findViewById(R.id.itemsReportsLinearLayout);
        itemsReportsCardView.startAnimation(animationenter);
        vouchersReportCardView.startAnimation(animat_exit);
        vouchersReportCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionsReport.this , VouchersReport.class);
                startActivity(intent);
            }
        });

//        vouchersReportCardView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    voucherReportLinearLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer3));
//                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    voucherReportLinearLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
//                }
//                return false;
//            }
//        });


        itemsReportsCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionsReport.this , ItemsReport.class);
                startActivity(intent);
            }
        });

//        itemsReportsCardView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    itemsReportsLinearLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer3));
//                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    itemsReportsLinearLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.layer5));
//                }
//                return false;
//            }
//        });
    }
}
