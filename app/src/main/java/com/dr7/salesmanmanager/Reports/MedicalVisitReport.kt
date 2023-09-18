package com.dr7.salesmanmanager.Reports

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dr7.salesmanmanager.*
import com.dr7.salesmanmanager.Adapters.VisitMedicalAdapter
import com.dr7.salesmanmanager.Modles.visitMedicalModel
import com.dr7.salesmanmanager.databinding.ActivityMedicalVisitReportBinding
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton
import com.nightonke.boommenu.BoomMenuButton
import com.nightonke.boommenu.ButtonEnum
import com.nightonke.boommenu.Piece.PiecePlaceEnum
import org.apache.http.impl.cookie.DateUtils.formatDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

class MedicalVisitReport : AppCompatActivity() {
    var dataBase= DatabaseHandler(this)
    var voucherNo:String = "0"
    var listAllItems=mutableListOf<visitMedicalModel>()
    var listAllInfo=mutableListOf<visitMedicalModel>()
    var listAllInfo_filtered=mutableListOf<visitMedicalModel>()
    lateinit var curentDate:String
    var generalMethod= GeneralMethod(this)
//    , R.drawable.excel_small
    var listImageIcone = intArrayOf(R.drawable.pdf_icon)
    var myCalendar: Calendar = Calendar.getInstance()
    lateinit var binding:ActivityMedicalVisitReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_medical_visit_report)
         binding= DataBindingUtil.setContentView(
            this, R.layout.activity_medical_visit_report)
        curentDate=generalMethod.getCurentTimeDate(1)
        Log.e("curentDate"," "+curentDate)

        binding. recyclerViewReport.setLayoutManager(LinearLayoutManager(this@MedicalVisitReport))
        fillAdapter(binding)
        inflateBoomMenu(binding)
        binding.fromDate.setText(curentDate)
        binding.toDate.setText(curentDate)
        filterData()
        binding.fromDate.setOnClickListener({view->

            // TODO Auto-generated method stub
            DatePickerDialog(
                this@MedicalVisitReport, openDatePickerDialog(0), myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        })

        binding.toDate.setOnClickListener({view->
        DatePickerDialog(
            this@MedicalVisitReport, openDatePickerDialog(1),
            myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
        })

        binding.preview.setOnClickListener({view->
            filterData()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterData() {
        listAllItems=dataBase.getAllVisitReport()
        listAllInfo=dataBase.getAllVisitReport_noduplicate()
        var from_Date=binding.fromDate.text.toString()
        var to_Date=binding.toDate.text.toString()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate1: LocalDate = LocalDate.parse(from_Date, formatter)
        val localDate2: LocalDate = LocalDate.parse(to_Date, formatter)
        for (i in listAllInfo.indices) {
            listAllInfo= listAllInfo.stream()
                .filter { e ->
                    (!LocalDate.parse(e.dateVoucher, formatter).isBefore(localDate1)
                            && !LocalDate.parse(e.dateVoucher, formatter).isAfter(localDate2))
                }
                .collect(Collectors.toList())

        }
        for (i in listAllItems.indices) {
            listAllItems= listAllItems.stream()
                .filter { e ->
                    (!LocalDate.parse(e.dateVoucher, formatter).isBefore(localDate1)
                            && !LocalDate.parse(e.dateVoucher, formatter).isAfter(localDate2))
                }
                .collect(Collectors.toList())
        }
//        Log.e("localDate1","2listAllItems"+listAllItems.size)
        fillAdapter(binding)
    }

    fun openDatePickerDialog(flag: Int): OnDateSetListener? {
        return OnDateSetListener { view, year, month, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(flag)
        }
    }
    private fun updateLabel(flag: Int) {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        if (flag == 0) binding.fromDate.setText(sdf.format(myCalendar.time)) else binding.toDate.setText(
            sdf.format(
                myCalendar.time
            )
        )
    }
    private fun exportToEx(itemsReportinventory: List<visitMedicalModel?>) {
        val exportToExcel = ExportToExcel()
        exportToExcel.createExcelFile(
            this@MedicalVisitReport,
            "MedicalVisitReport.xls",
            2,
            itemsReportinventory
        )
    }

    fun exportToPdf(itemsReportinventory: List<visitMedicalModel?>) {
        Log.e("exportToPdf", "" + itemsReportinventory.size)
        val pdf = PdfConverter(this@MedicalVisitReport)
        pdf.exportListToPdf(itemsReportinventory, "MedicalVisitReport", "21/12/2020", 21)
    }
    private fun inflateBoomMenu(binding: ActivityMedicalVisitReportBinding) {
        val bmb = findViewById<View>(R.id.bmb) as BoomMenuButton
        bmb.buttonEnum = ButtonEnum.SimpleCircle
        bmb.piecePlaceEnum = PiecePlaceEnum.DOT_1
        bmb.buttonPlaceEnum = ButtonPlaceEnum.SC_1
        //        SimpleCircleButton.Builder b1 = new SimpleCircleButton.Builder();
        for (i in 0 until bmb.buttonPlaceEnum.buttonNumber()) {
            bmb.addBuilder(SimpleCircleButton.Builder()
                .normalImageRes(listImageIcone[i])
                .listener { index -> // When the boom-button corresponding this builder is clicked.
                    when (index) {
                        0 -> exportToPdf(listAllItems)
//                        1 -> exportToEx(listAllItems)
                    }
                })
            //            bmb.addBuilder(builder);
        }
    }

    private fun fillAdapter(binding: ActivityMedicalVisitReportBinding) {
        Log.e("transactionsInfos==", listAllItems.size.toString() + "")

        Log.e("transactionsInfos==2=", listAllInfo.size.toString() + "")
        val customertransAdapter =
            VisitMedicalAdapter(listAllItems, this@MedicalVisitReport,listAllInfo)
        binding. recyclerViewReport.setAdapter(customertransAdapter)

    }
    fun <T> removeDuplicates(list: MutableList<T>): MutableList<T>? {

        // Create a new LinkedHashSet
        val set: MutableSet<T> = LinkedHashSet()

        // Add the elements to set
        set.addAll(list)

        // Clear the list
        list.clear()

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set)

        // return the list
        return list
    }
}