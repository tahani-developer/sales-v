package com.dr7.salesmanmanager.Reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportToExcel;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.SerialReportAdpter;
import com.dr7.salesmanmanager.Serial_Adapter;

import java.util.ArrayList;
import java.util.List;

public class SerialReport extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<serialModel> list=new ArrayList<>();
    DatabaseHandler databaseHandler;
    public static SerialReportAdpter adapter;
Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_report);
        init();
        Log.e("SerialReport","SerialReport");
        list=databaseHandler.getalllserialitems();
        fillAdapterData(list);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToEx();
            }
        });

    }

    private void init() {
        databaseHandler=new DatabaseHandler(SerialReport.this);
        button=findViewById(R.id.export);
        recyclerView=findViewById(R.id.SErecyclerView_report);
    }
    public void fillAdapterData( List<serialModel> serialModels) {
        Log.e("SerialReport2","SerialReport2");
        recyclerView.setLayoutManager(new LinearLayoutManager(SerialReport.this));
        adapter = new SerialReportAdpter (serialModels,SerialReport.this );
        recyclerView.setAdapter(adapter);

    }
    private void exportToEx() {
        ExportToExcel exportToExcel=new ExportToExcel();
        exportToExcel.createExcelFile(SerialReport.this,"Reportallserial.xls",11,list);

    }
}