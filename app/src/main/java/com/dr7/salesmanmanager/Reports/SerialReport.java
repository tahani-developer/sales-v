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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.dr7.salesmanmanager.DatabaseHandler;
import com.dr7.salesmanmanager.ExportToExcel;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.R;
import com.dr7.salesmanmanager.SerialReportAdpter;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static androidx.core.os.HandlerCompat.postDelayed;
import static com.dr7.salesmanmanager.Login.languagelocalApp;

public class SerialReport extends AppCompatActivity {
    RecyclerView recyclerView;
    public static List<serialModel> allseriallist = new ArrayList<>();
    private List<serialModel> searchlist = new ArrayList<>();
    DatabaseHandler databaseHandler;
    public static SerialReportAdpter adapter;
    TextView searchicon;
    EditText searchedit;
    public TextView date;
    LinearLayout tableRow;
    Button button;
    private Button preview;
    Calendar myCalendar;
    HorizontalScrollView HorizontalScrollView01;
    GeneralMethod generalMethod;
    Spinner ItemKindspinner;
    List<String> spinnerArray = new ArrayList<>();
    List<ItemsMaster> itemsMasters = new ArrayList<>();
    List<ItemsMaster> mastersItemkinds;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_report);
        init();
        itemsMasters.clear();
        itemsMasters = databaseHandler.getItemMaster2();
        //  Log.e("itemsMasters"," "+ itemsMasters.size());
        fillSp();

        Log.e("SerialReport", "SerialReport");
        allseriallist = databaseHandler.getalllserialitems();


        String Date_Vocher = generalMethod.getCurentTimeDate(1);
        filterDate(Date_Vocher);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToEx();
            }
        });

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        date.setText(today);
        myCalendar = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SerialReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchlist.clear();
                getitemkind();
                for (int i = 0; i < allseriallist.size(); i++) {
                    if (date.getText().toString().equals(allseriallist.get(i).getDateVoucher()))

                        if (!ItemKindspinner.getSelectedItem().toString().equals("")) {
                            for (int j = 0; j < mastersItemkinds.size(); j++)
                                if (mastersItemkinds.get(j).getItemNo().equals(allseriallist.get(i).getItemNo())) {
                                    searchlist.add(allseriallist.get(i));
                                    break;
                                }
                        } else {
                            searchlist.add(allseriallist.get(i));
                        }

                }
                if (allseriallist.size() != 0) {
                    Log.e("itemki===", "gg");
                    tableRow.setVisibility(View.VISIBLE);
                    fillAdapterData(searchlist);
                } else {
                    tableRow.setVisibility(View.GONE);
                }
                Log.e("itemki===", "cc");
            }
        });
        searchicon.setVisibility(View.INVISIBLE);
        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
//        HorizontalScrollView01.post(new Runnable() {
//            public void run() {
//                HorizontalScrollView01.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            }
//        });
    }

    private void fillSp() {

        spinnerArray = databaseHandler.getAllKindItems();
        spinnerArray.add(0, "");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        ItemKindspinner.setAdapter(adapter);
    }

    void getitemkind() {

        mastersItemkinds = new ArrayList<ItemsMaster>();

        mastersItemkinds = databaseHandler.getItemkinds(ItemKindspinner.getSelectedItem().toString());
        Log.e("itemsMasters2===", mastersItemkinds.size() + "");
        //   Log.e("itemsMasters2===",mastersItemkinds.get(0).getItemNo());
        //  return mastersItemkinds.get(0).getItemNo();


    }

    private void filterDate(String date_vocher) {
        searchlist.clear();
        // String Date_Vocher="14/02/2021";
        for (int i = 0; i < allseriallist.size(); i++) {
            if (allseriallist.get(i).getDateVoucher().trim().equals(date_vocher.trim()))
                searchlist.add(allseriallist.get(i));
        }


        if (searchlist.size() == 0) tableRow.setVisibility(View.GONE);
        else {
            fillAdapterData(searchlist);

            tableRow.setVisibility(View.VISIBLE);
        }
    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(flag);
            }

        };
        return date;
    }

    private void updateLabel(int flag) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (flag == 0)
            date.setText(sdf.format(myCalendar.getTime()));

    }

    private void search() {
        searchlist.clear();
        getitemkind();
        String searchED = convertToEnglish(searchedit.getText().toString().trim());

        for (int i = 0; i < allseriallist.size(); i++) {
            if ((allseriallist.get(i).getVoucherNo().startsWith(searchED))
                    || (allseriallist.get(i).getItemNo().startsWith(searchED))
                    || (allseriallist.get(i).getSerialCode().startsWith(searchED))

            )
                if (!ItemKindspinner.getSelectedItem().toString().equals("")) {

                    Log.e("allseriallist===", allseriallist.get(i).getItemNo() + "");
                    Log.e("allseriallist.get(i)===", allseriallist.get(i).getItemNo() + "");


                    for (int j = 0; j < mastersItemkinds.size(); j++)
                        if (mastersItemkinds.get(j).getItemNo().equals(allseriallist.get(i).getItemNo())) {
                            searchlist.add(allseriallist.get(i));

                        }
                } else
                    searchlist.add(allseriallist.get(i));


        }
        if (searchlist.size() > 0) {
            fillAdapterData(searchlist);
            tableRow.setVisibility(View.VISIBLE);
        } else {
            tableRow.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init() {
        ItemKindspinner = findViewById(R.id.ItemKindspinner);
        databaseHandler = new DatabaseHandler(SerialReport.this);
        HorizontalScrollView01 = findViewById(R.id.HorizontalScrollView01);
        LinearLayout linearMain = findViewById(R.id.linearMain);
        generalMethod = new GeneralMethod(SerialReport.this);
        try {
            if (languagelocalApp.equals("ar")) {
                linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        } catch (Exception e) {
            linearMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        button = findViewById(R.id.export);
        recyclerView = findViewById(R.id.SErecyclerView_report);
        searchedit = findViewById(R.id.search_edt);
        searchicon = findViewById(R.id.Search);
        date = findViewById(R.id.SE_date);
        preview = findViewById(R.id.SE_preview);
        tableRow = findViewById(R.id.serialtable);


        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.e("searchedit", editable + "");
                if (!searchedit.getText().toString().trim().equals("")) search();
                else {
                    if (!date.getText().toString().equals("")) {
                        filterDate(date.getText().toString());

                        if (adapter != null) adapter.notifyDataSetChanged();
                    }

                }
             /*   if(editable.equals(""))
                {
                    Log.e("searchedit",editable+"");
                    fillAdapterData(todayseariallist);
                adapter.notifyDataSetChanged();
                }
                else
                {
                    search();
                }*/
            }
        });


    }

    public void fillAdapterData(List<serialModel> serialModels) {
        Log.e("SerialReport2", "SerialReport2");
        recyclerView.setLayoutManager(new LinearLayoutManager(SerialReport.this));
        adapter = new SerialReportAdpter(serialModels, SerialReport.this, 0);
        recyclerView.setAdapter(adapter);

    }

    private void exportToEx() {
        ExportToExcel exportToExcel = new ExportToExcel();
        if (searchlist.size() != 0)
            exportToExcel.createExcelFile(SerialReport.this, "Reportallserial.xls", 11, searchlist);
        else {
            exportToExcel.createExcelFile(SerialReport.this, "Reportallserial.xls", 11, allseriallist);

        }

    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

}