package com.dr7.salesmanmanager.Reports;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportToExcel;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.Modles.InventoryShelf;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.PdfConverter;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.SerialReportAdpter;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class ShelfInventoeryReport extends AppCompatActivity {
    RecyclerView recyclerView;
    public static List<serialModel> allShelflist =new ArrayList<>();
    int[] listImageIcone=new int[]{R.drawable.pdf_icon,R.drawable.excel_small};
    private List<serialModel> searchlist=new ArrayList<>();

    DatabaseHandler databaseHandler;
    public static SerialReportAdpter adapter;
    TextView searchicon,clear_text;
    EditText searchedit;
    public TextView date;
    TableRow tableRow;
    Button button;
    private Button preview;
    Calendar myCalendar;
    HorizontalScrollView HorizontalScrollView01;
    GeneralMethod generalMethod;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_inventoery_report);
        init();
        this.setTitle(""+getResources().getString(R.string.shelf_inventory_report));
        Log.e("SerialReport","activity_shelf_inventoery_report");
        allShelflist =databaseHandler.getAllINVENTORY_SHELF_REPORT();
        Log.e("SerialReport","allShelflist"+allShelflist.size());
        fillAdapterData(allShelflist);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                exportToEx();
            }
        });


        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);

        searchicon.setVisibility(View.INVISIBLE);
        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    private void search() {
        searchlist.clear();
        String searchED=generalMethod.convertToEnglish(searchedit.getText().toString().trim());

        Log.e("search",""+searchED);
        for (int i = 0; i < allShelflist.size(); i++) {
            if ((allShelflist.get(i).getVoucherNo().contains(searchED))
                    || (allShelflist.get(i).getItemNo().contains(searchED))
                    ||(allShelflist.get(i).getSerialCode().contains(searchED))
            )
                searchlist.add(allShelflist.get(i));


        }
        if(searchlist.size()>0){fillAdapterData(searchlist);
            tableRow.setVisibility(View.VISIBLE);}
        else
        { tableRow.setVisibility(View.GONE);}

        Log.e("SerialReport","searchlist"+searchlist.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init() {
        databaseHandler=new DatabaseHandler(ShelfInventoeryReport.this);
        HorizontalScrollView01=findViewById(R.id.HorizontalScrollView01);
        LinearLayout linearMain=findViewById(R.id.linearMain);
        generalMethod=new GeneralMethod(ShelfInventoeryReport.this);
        try{
            if(languagelocalApp.equals("ar"))
            {
                linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                if(languagelocalApp.equals("en"))
                {
                    linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        }
        catch ( Exception e)
        {
            linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        button=findViewById(R.id.export);
        recyclerView=findViewById(R.id.SErecyclerView_report);
        searchedit=findViewById(R.id.search_edt);
        searchicon=findViewById(R.id.Search);
        date=findViewById(R.id.SE_date);
        preview=findViewById(R.id.SE_preview);
        tableRow=findViewById(R.id.serialtable);
        clear_text=findViewById(R.id.clear_text);
        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedit.setText("");
                searchlist.clear();
                fillAdapterData(allShelflist);

            }
        });


        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.e("searchedit",editable+"");
                if( !searchedit.getText().toString().trim().equals(""))search();
                else
                {

                    fillAdapterData(allShelflist);

                }
            }
        });

        inflateBoomMenu();

    }
    private void inflateBoomMenu() {
        BoomMenuButton bmb = (BoomMenuButton)findViewById(R.id.bmb);

        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_2);
//        SimpleCircleButton.Builder b1 = new SimpleCircleButton.Builder();


        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(listImageIcone[i])

                    .listener(new OnBMClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            switch (index)
                            {
                                case 0:
                                    exportToPdf();

                                    break;
                                case 1:
                                    exportToEx();
                                    break;


                            }
                        }
                    }));
//            bmb.addBuilder(builder);


        }
    }
    public void fillAdapterData( List<serialModel> serialModels) {
        Log.e("SerialReport2","SerialReport2");
        recyclerView.setLayoutManager(new LinearLayoutManager(ShelfInventoeryReport.this));
        adapter = new SerialReportAdpter (serialModels,ShelfInventoeryReport.this );
        recyclerView.setAdapter(adapter);

    }
    private void exportToEx() {
        Log.e("exportToPdf",""+allShelflist.size());
        ExportToExcel exportToExcel=new ExportToExcel();
        if(searchlist.size()!=0){
            exportToExcel.createExcelFile(ShelfInventoeryReport.this,"ShelfInventoryReport.xls",13,searchlist);
        }else {
            exportToExcel.createExcelFile(ShelfInventoeryReport.this,"ShelfInventoryReport.xls",13,allShelflist);

        }


    }
    public  void exportToPdf(){

        PdfConverter pdf =new PdfConverter(ShelfInventoeryReport.this);
        if(searchlist.size()!=0)
        {  pdf.exportListToPdf(searchlist,"ShelfInventoryReport","21/12/2020",11);}
        else {


            pdf.exportListToPdf(allShelflist, "ShelfInventoryReport", "21/12/2020", 11);

        }
    }
}
