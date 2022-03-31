package com.dr7.salesmanmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Reports.SerialReport;
import com.itextpdf.text.BaseColor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import static com.dr7.salesmanmanager.Reports.CashReport.T_cash;
import static com.dr7.salesmanmanager.Reports.CashReport.T_credit;
import static com.dr7.salesmanmanager.Reports.CashReport.cashPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.credit;
import static com.dr7.salesmanmanager.Reports.CashReport.creditPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.net;
import static com.dr7.salesmanmanager.Reports.CashReport.returnCridet;
import static com.dr7.salesmanmanager.Reports.CashReport.total;
import static com.dr7.salesmanmanager.Reports.CashReport.total_cash;
import static com.itextpdf.text.Element.ALIGN_CENTER;


public class ExportToExcel {
    private static ExportToExcel instance;
    Context context;

    public static ExportToExcel getInstance() {
        if (instance == null)
            instance = new ExportToExcel();

        return instance;
    }

    public void createExcelFile(Context context, String fileName, int report, List<?> list) {
//        public void createExcelFile(Context context, String fileName, int report, List<?> list, List<?> listDetail)
//        this.context = context;
//        final String fileName = "planned_packing_list_report.xls";

        //Saving file in external storage
        this.context = context;
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/VanSalesExcelReport");
        Log.e("directory",sdCard.getAbsolutePath()+"");
        if (!directory.isDirectory()) {//create directory if not exist
            directory.mkdirs();
        }

        File file = new File(directory, fileName);//file path

//        WorkbookSettings wbSettings = new WorkbookSettings();
//        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = null;//, wbSettings);
        try {
            workbook = Workbook.createWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            switch (report) {

                case 1:
                    workbook = customerLogReport(workbook, (List<Transaction>) list);
                    break;

                case 2:
                    workbook = inventoryReport(workbook, (List<inventoryReportItem>) list);
                    break;
                case 3:
                    workbook = voucherReport(workbook, (List<Voucher>) list);
                    break;
                case 4:
                    workbook = itemsReport(workbook, (List<Item>) list);
                    break;
                case 5:
                    workbook = paymentReport(workbook, (List<Payment>) list);
                    break;
                case 6:
                    workbook = items_StockReport(workbook, (List<Item>) list);
                    break;
                case 7:
                    workbook = voucherStockReport(workbook, (List<Voucher>) list);
                    break;
                case 8:
                    workbook = cashReport(workbook, (List<Voucher>) list);
                    break;
                case 9:
                    workbook = SerialListReport(workbook, (List<serialModel>) list);
                    break;
                case 10:
                    workbook = unCollectedReport(workbook, (List<Payment>) list);
                    break;
                case 11:
                    workbook = SerialitemListReport(workbook, (List<serialModel>) list);
                    break;

                case 12:
                    workbook = accountStatmentReport(workbook, (List<Account__Statment_Model>) list);
                    break;
                case 13:
                    workbook = shelfInventoryReport(workbook, (List<serialModel>) list);
                    break;
                case 14:
                    workbook = ReturnSerialitemList(workbook, (List<Item>) list);
                    break;

            }
            }catch (Exception e) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

                Log.v("", "Permission is granted");
            } else {

                Log.v("", "Permission is revoked");
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }


        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/VanSalesExcelReport/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            Log.e("file",directory_path);
            String targetPdf = directory_path + fileName;
            File path = new File(targetPdf);

            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.ms-excel");//intent.setDataAndType(Uri.fromFile(path), "application/pdf");
            Log.e("mmm", intent.getType());

            try {
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Excel program needed!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}

    }

    //********************************************************************
    WritableWorkbook unCollectedReport(WritableWorkbook workbook, List<Payment> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet
            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                sheet.mergeCells(2, 0, 3, 0);
                sheet.mergeCells(4, 0, 5, 0);
                sheet.mergeCells(6, 0, 7, 0);
                sheet.mergeCells(0, 1, 7, 1);

                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.app_bank_name), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.check_number), format));
                    sheet.addCell(new Label(4, 0, context.getString(R.string.chaequeDate), format));
                    sheet.addCell(new Label(6, 0, context.getString(R.string.app_amount), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(0, i + 2, list.get(i).getBank() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getCheckNumber() + "", format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getDueDate() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getAmount() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);

                    }

                } else {

                    sheet.addCell(new Label(6, 0, context.getString(R.string.app_bank_name), format)); // column and row
                    sheet.addCell(new Label(4, 0, context.getString(R.string.check_number), format));
                    sheet.addCell(new Label(2, 0, context.getString(R.string.chaequeDate), format));
                    sheet.addCell(new Label(0, 0, context.getString(R.string.app_amount), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(6, i + 2, list.get(i).getBank() + "", format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getCheckNumber() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getDueDate() + "", format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getAmount() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

//*********************************************************************

    WritableWorkbook customerLogReport(WritableWorkbook workbook, List<Transaction> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet
            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            try {
                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.SALES_MAN_ID), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.CUS_CODE), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.cust_name), format));
                    sheet.addCell(new Label(6, 0, context.getString(R.string.CHECK_IN_DATE), format));
                    sheet.addCell(new Label(8, 0, context.getString(R.string.CHECK_IN_TIME), format));
                    sheet.addCell(new Label(10, 0, context.getString(R.string.CHECK_OUT_DATE), format));
                    sheet.addCell(new Label(12, 0, context.getString(R.string.CHECK_OUT_TIME), format));
//                sheet.addCell(new Label(10, 0, "Bundles"));
//                sheet.addCell(new Label(11, 0, "Cubic"));
                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);// col , row, to col , to row
                    sheet.mergeCells(4, 0, 5, 0);// col , row, to col , to row
                    sheet.mergeCells(6, 0, 7, 0);// col , row, to col , to row
                    sheet.mergeCells(8, 0, 9, 0);// col , row, to col , to row
                    sheet.mergeCells(10, 0, 11, 0);// col , row, to col , to row
                    sheet.mergeCells(12, 0, 13, 0);// col , row, to col , to row

                    sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row
                    sheet.mergeCells(2, 1, 3, 1);// col , row, to col , to row
                    sheet.mergeCells(4, 1, 5, 1);// col , row, to col , to row
                    sheet.mergeCells(6, 1, 7, 1);// col , row, to col , to row
                    sheet.mergeCells(8, 1, 9, 1);// col , row, to col , to row
                    sheet.mergeCells(10, 1, 11, 1);// col , row, to col , to row
                    sheet.mergeCells(12, 1, 13, 1);// col , row, to col , to row

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getSalesManId() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getCusCode(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getCusName(), format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getCheckInDate(), format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getCheckInTime(), format));
                        sheet.addCell(new Label(10, i + 2, list.get(i).getCheckOutDate(), format));
                        sheet.addCell(new Label(12, i + 2, "" + list.get(i).getCheckOutTime(), format));
//                    sheet.addCell(new Label(10, i + 2, "" + list.get(i).getStatus()));
//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));
                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);// col , row, to col , to row
                        sheet.mergeCells(4, i + 2, 5, i + 2);// col , row, to col , to row
                        sheet.mergeCells(6, i + 2, 7, i + 2);// col , row, to col , to row
                        sheet.mergeCells(8, i + 2, 9, i + 2);// col , row, to col , to row
                        sheet.mergeCells(10, i + 2, 11, i + 2);// col , row, to col , to row
                        sheet.mergeCells(12, i + 2, 13, i + 2);// col , row, to col , to row
                    }

                } else {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.CHECK_OUT_TIME), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.CHECK_OUT_DATE), format));
                    sheet.addCell(new Label(4, 0, context.getString(R.string.CHECK_IN_TIME), format));
                    sheet.addCell(new Label(6, 0, context.getString(R.string.CHECK_IN_DATE), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.cust_name), format));
                    sheet.addCell(new Label(10, 0, context.getString(R.string.CUS_CODE), format));
                    sheet.addCell(new Label(12, 0, context.getString(R.string.SALES_MAN_ID), format));


                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);// col , row, to col , to row
                    sheet.mergeCells(4, 0, 5, 0);// col , row, to col , to row
                    sheet.mergeCells(6, 0, 7, 0);// col , row, to col , to row
                    sheet.mergeCells(8, 0, 9, 0);// col , row, to col , to row
                    sheet.mergeCells(10, 0, 11, 0);// col , row, to col , to row
                    sheet.mergeCells(12, 0, 13, 0);// col , row, to col , to row

                    sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row
                    sheet.mergeCells(2, 1, 3, 1);// col , row, to col , to row
                    sheet.mergeCells(4, 1, 5, 1);// col , row, to col , to row
                    sheet.mergeCells(6, 1, 7, 1);// col , row, to col , to row
                    sheet.mergeCells(8, 1, 9, 1);// col , row, to col , to row
                    sheet.mergeCells(10, 1, 11, 1);// col , row, to col , to row
                    sheet.mergeCells(12, 1, 13, 1);// col , row, to col , to row

                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(0, i + 2, "" + list.get(i).getCheckOutTime(), format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getCheckOutDate(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getCheckInTime(), format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getCheckInDate(), format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getCusName(), format));
                        sheet.addCell(new Label(10, i + 2, list.get(i).getCusCode(), format));
                        sheet.addCell(new Label(12, i + 2, list.get(i).getSalesManId() + "", format));


                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);// col , row, to col , to row
                        sheet.mergeCells(4, i + 2, 5, i + 2);// col , row, to col , to row
                        sheet.mergeCells(6, i + 2, 7, i + 2);// col , row, to col , to row
                        sheet.mergeCells(8, i + 2, 9, i + 2);// col , row, to col , to row
                        sheet.mergeCells(10, i + 2, 11, i + 2);// col , row, to col , to row
                        sheet.mergeCells(12, i + 2, 13, i + 2);// col , row, to col , to row

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook inventoryReport(WritableWorkbook workbook, List<inventoryReportItem> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            try {
                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.item_name), format)); // column and row
                    sheet.addCell(new Label(3, 0, context.getString(R.string.item_number), format));
                    sheet.addCell(new Label(5, 0, context.getResources().getString(R.string.unit_qty), format));

                    sheet.mergeCells(0, 0, 2, 0);// col , row, to col , to row
                    sheet.mergeCells(3, 0, 4, 0);// col , row, to col , to row
                    sheet.mergeCells(5, 0, 6, 0);// col , row, to col , to row


                    sheet.mergeCells(0, 1, 2, 1);// col , row, to col , to row
                    sheet.mergeCells(3, 1, 4, 1);// col , row, to col , to row
                    sheet.mergeCells(5, 1, 6, 1);// col , row, to col , to row

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getName() + "", format));
                        sheet.addCell(new Label(3, i + 2, list.get(i).getItemNo(), format));
                        sheet.addCell(new Label(5, i + 2, list.get(i).getQty() + "", format));

//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));
                        sheet.mergeCells(0, i + 2, 2, i + 2);// col , row, to col , to row
                        sheet.mergeCells(3, i + 2, 4, i + 2);// col , row, to col , to row
                        sheet.mergeCells(5, i + 2, 6, i + 2);// col , row, to col , to row
                    }

                } else {

                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.unit_qty), format));
                    sheet.addCell(new Label(2, 0, context.getString(R.string.item_number), format));
                    sheet.addCell(new Label(4, 0, context.getString(R.string.item_name), format)); // column and row

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);// col , row, to col , to row
                    sheet.mergeCells(4, 0, 6, 0);// col , row, to col , to row


                    sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row
                    sheet.mergeCells(2, 1, 3, 1);// col , row, to col , to row
                    sheet.mergeCells(4, 1, 6, 1);// col , row, to col , to row

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getQty() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getItemNo(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getName() + "", format));


//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));
                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);// col , row, to col , to row
                        sheet.mergeCells(4, i + 2, 6, i + 2);// col , row, to col , to row
                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook voucherReport(WritableWorkbook workbook, List<Voucher> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            try {
                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.cust_name), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.customer_number), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.voucher_date), format));
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.pay_method), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.app_disc), format));
                    sheet.addCell(new Label(10, 0, context.getResources().getString(R.string.sub_total), format));
                    sheet.addCell(new Label(12, 0, context.getResources().getString(R.string.tax), format));
                    sheet.addCell(new Label(14, 0, context.getResources().getString(R.string.net_sales), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);// col , row, to col , to row
                    sheet.mergeCells(4, 0, 5, 0);// col , row, to col , to row
                    sheet.mergeCells(6, 0, 7, 0);// col , row, to col , to row
                    sheet.mergeCells(8, 0, 9, 0);// col , row, to col , to row
                    sheet.mergeCells(10, 0, 11, 0);// col , row, to col , to row
                    sheet.mergeCells(12, 0, 13, 0);// col , row, to col , to row
                    sheet.mergeCells(14, 0, 15, 0);

                    sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row
                    sheet.mergeCells(2, 1, 3, 1);// col , row, to col , to row
                    sheet.mergeCells(4, 1, 5, 1);// col , row, to col , to row
                    sheet.mergeCells(6, 1, 7, 1);// col , row, to col , to row
                    sheet.mergeCells(8, 1, 9, 1);// col , row, to col , to row
                    sheet.mergeCells(10, 1, 11, 1);// col , row, to col , to row
                    sheet.mergeCells(12, 1, 13, 1);// col , row, to col , to row
                    sheet.mergeCells(14, 1, 15, 1);// col , row, to col , to row


                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getCustName() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getCustNumber(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getVoucherDate() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getPayMethod() + "", format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getVoucherDiscount() + "", format));
                        sheet.addCell(new Label(10, i + 2, list.get(i).getSubTotal() + "", format));
                        sheet.addCell(new Label(12, i + 2, list.get(i).getTax() + "", format));
                        sheet.addCell(new Label(14, i + 2, list.get(i).getNetSales() + "", format));

//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);
                        sheet.mergeCells(10, i + 2, 11, i + 2);
                        sheet.mergeCells(12, i + 2, 13, i + 2);
                        sheet.mergeCells(14, i + 2, 15, i + 2);
                    }

                } else {

                    sheet.addCell(new Label(14, 0, context.getString(R.string.cust_name), format)); // column and row
                    sheet.addCell(new Label(12, 0, context.getString(R.string.customer_number), format));
                    sheet.addCell(new Label(10, 0, context.getResources().getString(R.string.voucher_date), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.pay_method), format));
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.app_disc), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.sub_total), format));
                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.tax), format));
                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.net_sales), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);// col , row, to col , to row
                    sheet.mergeCells(4, 0, 5, 0);// col , row, to col , to row
                    sheet.mergeCells(6, 0, 7, 0);// col , row, to col , to row
                    sheet.mergeCells(8, 0, 9, 0);// col , row, to col , to row
                    sheet.mergeCells(10, 0, 11, 0);// col , row, to col , to row
                    sheet.mergeCells(12, 0, 13, 0);// col , row, to col , to row
                    sheet.mergeCells(14, 0, 15, 0);

                    sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row
                    sheet.mergeCells(2, 1, 3, 1);// col , row, to col , to row
                    sheet.mergeCells(4, 1, 5, 1);// col , row, to col , to row
                    sheet.mergeCells(6, 1, 7, 1);// col , row, to col , to row
                    sheet.mergeCells(8, 1, 9, 1);// col , row, to col , to row
                    sheet.mergeCells(10, 1, 11, 1);// col , row, to col , to row
                    sheet.mergeCells(12, 1, 13, 1);// col , row, to col , to row
                    sheet.mergeCells(14, 1, 15, 1);// col , row, to col , to row


                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(14, i + 2, list.get(i).getCustName() + "", format));
                        sheet.addCell(new Label(12, i + 2, list.get(i).getCustNumber(), format));
                        sheet.addCell(new Label(10, i + 2, list.get(i).getVoucherDate() + "", format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getPayMethod() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getVoucherDiscount() + "", format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getSubTotal() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getTax() + "", format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getNetSales() + "", format));

//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);// col , row, to col , to row
                        sheet.mergeCells(4, i + 2, 5, i + 2);// col , row, to col , to row
                        sheet.mergeCells(6, i + 2, 7, i + 2);// col , row, to col , to row
                        sheet.mergeCells(8, i + 2, 9, i + 2);// col , row, to col , to row
                        sheet.mergeCells(10, i + 2, 11, i + 2);// col , row, to col , to row
                        sheet.mergeCells(12, i + 2, 13, i + 2);// col , row, to col , to row
                        sheet.mergeCells(14, i + 2, 15, i + 2);
                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook voucherStockReport(WritableWorkbook workbook, List<Voucher> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.voucher_number), format)); // column and row

                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.voucher_date), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.remark), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);
                    sheet.mergeCells(4, 0, 5, 0);

                    sheet.mergeCells(0, 1, 1, 1);
                    sheet.mergeCells(2, 1, 3, 1);
                    sheet.mergeCells(4, 1, 5, 1);

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getVoucherDate() + "", format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getRemark() + "", format));


                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);

                    }

                } else {

                    sheet.addCell(new Label(4, 0, context.getString(R.string.voucher_number), format)); // column and row

                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.voucher_date), format));
                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.remark), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);
                    sheet.mergeCells(4, 0, 5, 0);

                    sheet.mergeCells(0, 1, 1, 1);
                    sheet.mergeCells(2, 1, 3, 1);
                    sheet.mergeCells(4, 1, 5, 1);

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(4, i + 2, list.get(i).getVoucherNumber() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getVoucherDate() + "", format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getRemark() + "", format));


                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook cashReport(WritableWorkbook workbook, List<Voucher> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                sheet.mergeCells(0, 0, 3, 0);
                sheet.addCell(new Label(0, 0, context.getString(R.string.sales), format)); // column and row

                sheet.mergeCells(0, 1, 3, 1);// col , row, to col , to row

                sheet.mergeCells(0, 2, 1, 2);
                sheet.mergeCells(2, 2, 3, 2);

                sheet.mergeCells(0, 3, 1, 3);
                sheet.mergeCells(2, 3, 3, 3);

                sheet.mergeCells(0, 4, 1, 4);
                sheet.mergeCells(2, 4, 3, 4);

                sheet.mergeCells(0, 5, 1, 5);
                sheet.mergeCells(2, 5, 3, 5);

                sheet.mergeCells(0, 6, 1, 6);
                sheet.mergeCells(2, 6, 3, 6);

                sheet.mergeCells(0, 7, 3, 7);
                sheet.mergeCells(0, 8, 3, 8);
                sheet.addCell(new Label(0, 8, context.getString(R.string.payment), format)); // column and row

                sheet.mergeCells(0, 9, 3, 9);

                sheet.mergeCells(0, 10, 1, 10);
                sheet.mergeCells(2, 10, 3, 10);

                sheet.mergeCells(0, 11, 1, 11);
                sheet.mergeCells(2, 11, 3, 11);

                sheet.mergeCells(0, 12, 1, 12);
                sheet.mergeCells(2, 12, 3, 12);

                sheet.mergeCells(0, 13, 1, 13);
                sheet.mergeCells(2, 13, 3, 13);

                sheet.mergeCells(0, 14, 1, 14);
                sheet.mergeCells(2, 14, 3, 14);

                sheet.mergeCells(0, 15, 3, 15);
                sheet.mergeCells(0, 16, 3, 16);

                sheet.addCell(new Label(0, 16, context.getString(R.string.app_creditCard), format)); // column and row

                sheet.mergeCells(0, 17, 3, 17);

                sheet.mergeCells(0, 18, 1, 18);
                sheet.mergeCells(2, 18, 3, 18);

                sheet.mergeCells(0, 19, 1, 19);
                sheet.mergeCells(2, 19, 3, 19);

                sheet.mergeCells(0, 20, 1, 20);
                sheet.mergeCells(2, 20, 3, 20);

                if (!Locale.getDefault().getLanguage().equals("ar")) {


                    sheet.addCell(new Label(0, 2, context.getResources().getString(R.string.cash_sale), format));
                    sheet.addCell(new Label(2, 2, T_cash + "", format));

                    sheet.addCell(new Label(0, 4, context.getResources().getString(R.string.credit_sales), format));
                    sheet.addCell(new Label(2, 4, T_credit + "", format));

                    sheet.addCell(new Label(0, 6, context.getResources().getString(R.string.total_sales), format));
                    sheet.addCell(new Label(2, 6, total + "", format));

                    //***************************************************************************

                    sheet.addCell(new Label(0, 10, context.getResources().getString(R.string.cash), format));
                    sheet.addCell(new Label(2, 10, cashPayment + "", format));

                    sheet.addCell(new Label(0, 12, context.getResources().getString(R.string.app_cheque), format));
                    sheet.addCell(new Label(2, 12, creditPayment + "", format));

                    sheet.addCell(new Label(0, 14, context.getResources().getString(R.string.netpayment), format));
                    sheet.addCell(new Label(2, 14, net + "", format));

                    //********************************************************************************

                    sheet.addCell(new Label(0, 18, context.getResources().getString(R.string.credit_value), format));
                    sheet.addCell(new Label(2, 18, (credit - returnCridet) + "", format));

                    sheet.addCell(new Label(0, 20, context.getResources().getString(R.string.total_cash), format));
                    sheet.addCell(new Label(2, 20, total_cash + "", format));


                } else {

                    sheet.addCell(new Label(2, 2, context.getResources().getString(R.string.cash_sale), format));
                    sheet.addCell(new Label(0, 2, T_cash + "", format));


                    sheet.addCell(new Label(2, 4, context.getResources().getString(R.string.credit_sales), format));
                    sheet.addCell(new Label(0, 4, T_credit + "", format));

                    sheet.addCell(new Label(2, 6, context.getResources().getString(R.string.total_sales), format));
                    sheet.addCell(new Label(0, 6, total + "", format));

                    //***************************************************************************

                    sheet.addCell(new Label(2, 10, context.getResources().getString(R.string.cash), format));
                    sheet.addCell(new Label(0, 10, cashPayment + "", format));

                    sheet.addCell(new Label(2, 12, context.getResources().getString(R.string.app_cheque), format));
                    sheet.addCell(new Label(0, 12, creditPayment + "", format));

                    sheet.addCell(new Label(2, 14, context.getResources().getString(R.string.netpayment), format));
                    sheet.addCell(new Label(0, 14, net + "", format));

                    //********************************************************************************

                    sheet.addCell(new Label(2, 18, context.getResources().getString(R.string.credit_value), format));
                    sheet.addCell(new Label(0, 18, (credit - returnCridet) + "", format));

                    sheet.addCell(new Label(2, 20, context.getResources().getString(R.string.total_cash), format));
                    sheet.addCell(new Label(0, 20, total_cash + "", format));

                }


//
//                sheet.mergeCells(0,1, 5, 1);// col , row, to col , to row


//                for (int i = 0; i < list.size(); i++) {
//                    sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber()+""));
//                    sheet.addCell(new Label(3, i + 2, list.get(i).getVoucherDate()+""));
//                    sheet.addCell(new Label(4, i + 2, list.get(i).getRemark()+""));
//
//
//                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row
//
//                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook itemsReport(WritableWorkbook workbook, List<Item> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            try {

                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.item_number), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.item_name), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.total_sold_qty2), format));
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.total_bonus_qty2), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.total_sales_noTax), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);// col , row, to col , to row
                    sheet.mergeCells(4, 0, 5, 0);// col , row, to col , to row
                    sheet.mergeCells(6, 0, 7, 0);// col , row, to col , to row
                    sheet.mergeCells(8, 0, 9, 0);// col , row, to col , to row

                    sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row
                    sheet.mergeCells(2, 1, 3, 1);// col , row, to col , to row
                    sheet.mergeCells(4, 1, 5, 1);// col , row, to col , to row
                    sheet.mergeCells(6, 1, 7, 1);// col , row, to col , to row
                    sheet.mergeCells(8, 1, 9, 1);// col , row, to col , to row


                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getItemName(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getQty() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getBonus() + "", format));
                        sheet.addCell(new Label(8, i + 2, ((list.get(i).getQty() * list.get(i).getPrice()) - list.get(i).getDisc()) + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);// col , row, to col , to row
                        sheet.mergeCells(4, i + 2, 5, i + 2);// col , row, to col , to row
                        sheet.mergeCells(6, i + 2, 7, i + 2);// col , row, to col , to row
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                } else {

                    sheet.addCell(new Label(8, 0, context.getString(R.string.item_number), format)); // column and row
                    sheet.addCell(new Label(6, 0, context.getString(R.string.item_name), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.total_sold_qty2), format));
                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.total_bonus_qty2), format));
                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.total_sales_noTax), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);// col , row, to col , to row
                    sheet.mergeCells(4, 0, 5, 0);// col , row, to col , to row
                    sheet.mergeCells(6, 0, 7, 0);// col , row, to col , to row
                    sheet.mergeCells(8, 0, 9, 0);// col , row, to col , to row

                    sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row
                    sheet.mergeCells(2, 1, 3, 1);// col , row, to col , to row
                    sheet.mergeCells(4, 1, 5, 1);// col , row, to col , to row
                    sheet.mergeCells(6, 1, 7, 1);// col , row, to col , to row
                    sheet.mergeCells(8, 1, 9, 1);// col , row, to col , to row


                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(8, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getItemName(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getQty() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getBonus() + "", format));
                        sheet.addCell(new Label(0, i + 2, ((list.get(i).getQty() * list.get(i).getPrice()) - list.get(i).getDisc()) + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);// col , row, to col , to row
                        sheet.mergeCells(4, i + 2, 5, i + 2);// col , row, to col , to row
                        sheet.mergeCells(6, i + 2, 7, i + 2);// col , row, to col , to row
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook items_StockReport(WritableWorkbook workbook, List<Item> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.item_number), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.item_name), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.qty), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);
                    sheet.mergeCells(4, 0, 5, 0);

                    sheet.mergeCells(0, 1, 1, 1);
                    sheet.mergeCells(2, 1, 3, 1);
                    sheet.mergeCells(4, 1, 5, 1);


                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getItemName(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getQty() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                    }

                } else {

                    sheet.addCell(new Label(4, 0, context.getString(R.string.item_number), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.item_name), format));
                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.qty), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);
                    sheet.mergeCells(4, 0, 5, 0);

                    sheet.mergeCells(0, 1, 1, 1);
                    sheet.mergeCells(2, 1, 3, 1);
                    sheet.mergeCells(4, 1, 5, 1);


                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(4, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getItemName(), format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getQty() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook paymentReport(WritableWorkbook workbook, List<Payment> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            try {

                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.voucher_number), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.pay_date), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.cust_name), format));
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.app_amount), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.remark), format));
                    sheet.addCell(new Label(10, 0, context.getResources().getString(R.string.sale_man_number), format));
                    sheet.addCell(new Label(12, 0, context.getResources().getString(R.string.pay_method), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);
                    sheet.mergeCells(4, 0, 5, 0);
                    sheet.mergeCells(6, 0, 7, 0);
                    sheet.mergeCells(8, 0, 9, 0);
                    sheet.mergeCells(10, 0, 11, 0);
                    sheet.mergeCells(12, 0, 13, 0);

                    sheet.mergeCells(0, 1, 1, 1);
                    sheet.mergeCells(2, 1, 3, 1);
                    sheet.mergeCells(4, 1, 5, 1);
                    sheet.mergeCells(6, 1, 7, 1);
                    sheet.mergeCells(8, 1, 9, 1);
                    sheet.mergeCells(10, 1, 11, 1);
                    sheet.mergeCells(12, 1, 13, 1);

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getPayDate(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getCustName() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getAmount() + "", format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getRemark(), format));
                        sheet.addCell(new Label(10, i + 2, list.get(i).getSaleManNumber() + "", format));
                        sheet.addCell(new Label(12, i + 2, list.get(i).getPayMethod() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);
                        sheet.mergeCells(10, i + 2, 11, i + 2);
                        sheet.mergeCells(12, i + 2, 13, i + 2);

                    }

                } else {

                    sheet.addCell(new Label(12, 0, context.getString(R.string.voucher_number), format)); // column and row
                    sheet.addCell(new Label(10, 0, context.getString(R.string.pay_date), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.cust_name), format));
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.app_amount), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.remark), format));
                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.sale_man_number), format));
                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.pay_method), format));

                    sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                    sheet.mergeCells(2, 0, 3, 0);
                    sheet.mergeCells(4, 0, 5, 0);
                    sheet.mergeCells(6, 0, 7, 0);
                    sheet.mergeCells(8, 0, 9, 0);
                    sheet.mergeCells(10, 0, 11, 0);
                    sheet.mergeCells(12, 0, 13, 0);

                    sheet.mergeCells(0, 1, 1, 1);
                    sheet.mergeCells(2, 1, 3, 1);
                    sheet.mergeCells(4, 1, 5, 1);
                    sheet.mergeCells(6, 1, 7, 1);
                    sheet.mergeCells(8, 1, 9, 1);
                    sheet.mergeCells(10, 1, 11, 1);
                    sheet.mergeCells(12, 1, 13, 1);

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(12, i + 2, list.get(i).getVoucherNumber() + "", format));
                        sheet.addCell(new Label(10, i + 2, list.get(i).getPayDate(), format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getCustName() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getAmount() + "", format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getRemark(), format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getSaleManNumber() + "", format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getPayMethod() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);
                        sheet.mergeCells(10, i + 2, 11, i + 2);
                        sheet.mergeCells(12, i + 2, 13, i + 2);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;

    }

    WritableWorkbook SerialListReport(WritableWorkbook workbook, List<serialModel> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0, context.getString(R.string.voucher_number))); // column and row

                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.voucher_date)));
                sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.remark)));


                sheet.mergeCells(0, 1, 1, 1);// col , row, to col , to row


                for (int i = 0; i < list.size(); i++) {
//                    sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber()+""));
//                    sheet.addCell(new Label(3, i + 2, list.get(i).getVoucherDate()+""));
//                    sheet.addCell(new Label(4, i + 2, list.get(i).getRemark()+""));


                    sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;


    }

    WritableWorkbook SerialitemListReport(WritableWorkbook workbook, List<serialModel> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                sheet.mergeCells(0, 0, 1, 0);// col , row, to col , to row
                sheet.mergeCells(2, 0, 3, 0);
                sheet.mergeCells(4, 0, 5, 0);
                sheet.mergeCells(6, 0, 7, 0);
                sheet.mergeCells(8, 0, 9, 0);

                sheet.mergeCells(0, 1, 1, 1);
                sheet.mergeCells(2, 1, 3, 1);
                sheet.mergeCells(4, 1, 5, 1);
                sheet.mergeCells(6, 1, 7, 1);
                sheet.mergeCells(8, 1, 9, 1);

                if (!Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.voucher_date), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.voucher_type), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.serialcode), format));
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.item_number), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.voucher_number), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(0, i + 2, list.get(i).getDateVoucher() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getKindVoucher(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getSerialCode() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getVoucherNo(), format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                } else {

                    sheet.addCell(new Label(8, 0, context.getString(R.string.voucher_date), format)); // column and row
                    sheet.addCell(new Label(6, 0, context.getString(R.string.voucher_type), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.serialcode), format));
                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.item_number), format));
                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.voucher_number), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(8, i + 2, list.get(i).getDateVoucher() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getKindVoucher(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getSerialCode() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNo(), format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    WritableWorkbook ReturnSerialitemList(WritableWorkbook workbook, List<Item> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                sheet.mergeCells(0, 0, 1, 0);
                sheet.mergeCells(2, 0, 3, 0);
                sheet.mergeCells(4, 0, 5, 0);
                sheet.mergeCells(6, 0, 7, 0);
                sheet.mergeCells(8, 0, 9, 0);

                sheet.mergeCells(0, 0, 1, 1);
                sheet.mergeCells(2, 0, 3, 1);
                sheet.mergeCells(4, 0, 5, 1);
                sheet.mergeCells(6, 0, 7, 1);
                sheet.mergeCells(8, 0, 9, 1);


                if (Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.app_price), format)); // column and row
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.qty), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.item_name), format));
                    sheet.addCell(new Label(2, 0, context.getString(R.string.item_number), format));
                    sheet.addCell(new Label(0, 0, context.getString(R.string.voucherNo), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(8, i + 2, String.valueOf(list.get(i).getPrice()), format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getQty() + "", format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getItemName() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                } else {


                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.app_price), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.qty), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.item_name), format));
                    sheet.addCell(new Label(6, 0, context.getString(R.string.item_number), format));
                    sheet.addCell(new Label(8, 0, context.getString(R.string.voucherNo), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(0, i + 2, String.valueOf(list.get(i).getPrice()), format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getQty() + "", format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getItemName() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getItemNo() + "", format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getVoucherNumber() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    WritableWorkbook accountStatmentReport(WritableWorkbook workbook, List<Account__Statment_Model> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                sheet.mergeCells(0, 0, 11, 0);// col , row, to col , to row

                sheet.mergeCells(0, 1, 1, 1);
                sheet.mergeCells(2, 1, 3, 1);
                sheet.mergeCells(4, 1, 5, 1);
                sheet.mergeCells(6, 1, 7, 1);
                sheet.mergeCells(8, 1, 9, 1);
                sheet.mergeCells(10, 1, 11, 1);

                sheet.mergeCells(0, 2, 1, 2);
                sheet.mergeCells(2, 2, 3, 2);
                sheet.mergeCells(4, 2, 5, 2);
                sheet.mergeCells(6, 2, 7, 2);
                sheet.mergeCells(8, 2, 9, 2);
                sheet.mergeCells(10, 2, 11, 2);

                sheet.addCell(new Label(0, 0, context.getString(R.string.AccountStatment) + " : \t " + CustomerListShow.Customer_Name, format)); // column and row

                if (Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(10, 2, context.getString(R.string.balance), format)); // column and row
                    sheet.addCell(new Label(8, 2, context.getString(R.string.debit), format));
                    sheet.addCell(new Label(6, 2, context.getResources().getString(R.string.credit), format));
                    sheet.addCell(new Label(4, 2, context.getResources().getString(R.string.date_voucher), format));
                    sheet.addCell(new Label(2, 2, context.getResources().getString(R.string.transName), format));
                    sheet.addCell(new Label(0, 2, context.getResources().getString(R.string.voucherNo), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(10, i + 3, list.get(i).getBalance() + "", format));
                        sheet.addCell(new Label(8, i + 3, list.get(i).getCredit() + "", format));
                        sheet.addCell(new Label(6, i + 3, list.get(i).getDebit() + "", format));
                        sheet.addCell(new Label(4, i + 3, list.get(i).getDate_voucher() + "", format));
                        sheet.addCell(new Label(2, i + 3, list.get(i).getTranseNmae() + "", format));
                        sheet.addCell(new Label(0, i + 3, list.get(i).getVoucherNo() + "", format));

                        sheet.mergeCells(0, i + 3, 1, i + 3);// col , row, to col , to row
                        sheet.mergeCells(2, i + 3, 3, i + 3);
                        sheet.mergeCells(4, i + 3, 5, i + 3);
                        sheet.mergeCells(6, i + 3, 7, i + 3);
                        sheet.mergeCells(8, i + 3, 9, i + 3);
                        sheet.mergeCells(10, i + 3, 11, i + 3);

                    }

                } else {

                    sheet.addCell(new Label(0, 2, context.getString(R.string.balance), format)); // column and row
                    sheet.addCell(new Label(2, 2, context.getString(R.string.debit), format));
                    sheet.addCell(new Label(4, 2, context.getResources().getString(R.string.credit), format));
                    sheet.addCell(new Label(6, 2, context.getResources().getString(R.string.date_voucher), format));
                    sheet.addCell(new Label(8, 2, context.getResources().getString(R.string.transName), format));
                    sheet.addCell(new Label(10, 2, context.getResources().getString(R.string.voucherNo), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(0, i + 3, list.get(i).getBalance() + "", format));
                        sheet.addCell(new Label(2, i + 3, list.get(i).getCredit() + "", format));
                        sheet.addCell(new Label(4, i + 3, list.get(i).getDebit() + "", format));
                        sheet.addCell(new Label(6, i + 3, list.get(i).getDate_voucher() + "", format));
                        sheet.addCell(new Label(8, i + 3, list.get(i).getTranseNmae() + "", format));
                        sheet.addCell(new Label(10, i + 3, list.get(i).getVoucherNo() + "", format));

                        sheet.mergeCells(0, i + 3, 1, i + 3);// col , row, to col , to row
                        sheet.mergeCells(2, i + 3, 3, i + 3);
                        sheet.mergeCells(4, i + 3, 5, i + 3);
                        sheet.mergeCells(6, i + 3, 7, i + 3);
                        sheet.mergeCells(8, i + 3, 9, i + 3);
                        sheet.mergeCells(10, i + 3, 11, i + 3);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    WritableWorkbook shelfInventoryReport(WritableWorkbook workbook, List<serialModel> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);

            try {

                sheet.mergeCells(0, 0, 1, 0);
                sheet.mergeCells(2, 0, 3, 0);
                sheet.mergeCells(4, 0, 5, 0);
                sheet.mergeCells(6, 0, 7, 0);
                sheet.mergeCells(8, 0, 9, 0);

                sheet.mergeCells(0, 0, 1, 1);
                sheet.mergeCells(2, 0, 3, 1);
                sheet.mergeCells(4, 0, 5, 1);
                sheet.mergeCells(6, 0, 7, 1);
                sheet.mergeCells(8, 0, 9, 1);


                if (Locale.getDefault().getLanguage().equals("ar")) {

                    sheet.addCell(new Label(8, 0, context.getString(R.string.voucherNo), format)); // column and row
                    sheet.addCell(new Label(6, 0, context.getString(R.string.item_number), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.serialcode), format));
                    sheet.addCell(new Label(2, 0, context.getResources().getString(R.string.customer_number), format));
                    sheet.addCell(new Label(0, 0, context.getResources().getString(R.string.voucher_date), format));



                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(8, i + 2, list.get(i).getVoucherNo() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getItemNo(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getSerialCode() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getCustomerNo() + "", format));
                        sheet.addCell(new Label(0, i + 2, list.get(i).getDateVoucher() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                } else {

                    sheet.addCell(new Label(0, 0, context.getString(R.string.voucherNo), format)); // column and row
                    sheet.addCell(new Label(2, 0, context.getString(R.string.item_number), format));
                    sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.serialcode), format));
                    sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.customer_number), format));
                    sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.voucher_date), format));


                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNo() + "", format));
                        sheet.addCell(new Label(2, i + 2, list.get(i).getItemNo(), format));
                        sheet.addCell(new Label(4, i + 2, list.get(i).getSerialCode() + "", format));
                        sheet.addCell(new Label(6, i + 2, list.get(i).getCustomerNo() + "", format));
                        sheet.addCell(new Label(8, i + 2, list.get(i).getDateVoucher() + "", format));

                        sheet.mergeCells(0, i + 2, 1, i + 2);// col , row, to col , to row
                        sheet.mergeCells(2, i + 2, 3, i + 2);
                        sheet.mergeCells(4, i + 2, 5, i + 2);
                        sheet.mergeCells(6, i + 2, 7, i + 2);
                        sheet.mergeCells(8, i + 2, 9, i + 2);

                    }

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            Toast.makeText(context, "Exported To Excel ", Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return workbook;
    }
}