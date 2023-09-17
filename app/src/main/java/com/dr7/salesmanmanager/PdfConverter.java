package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.app.DirectAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.solver.widgets.Helper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.dr7.salesmanmanager.Modles.Account__Statment_Model;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.NumberToWordExample3;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PdfDocumentAdapter;
import com.dr7.salesmanmanager.Modles.Plan_SalesMan_model;
import com.dr7.salesmanmanager.Modles.SalesManPlan;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.dr7.salesmanmanager.Modles.visitMedicalModel;
import com.dr7.salesmanmanager.Reports.SerialReport;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgRaw;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.interfaces.PdfRunDirection;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.HeaderFooter;
import jxl.format.Border;
import jxl.write.Boolean;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.dr7.salesmanmanager.Login.languagelocalApp;
import static com.dr7.salesmanmanager.NumberToArabic.getArabicString;
import static com.dr7.salesmanmanager.Reports.CashReport.T_cash;
import static com.dr7.salesmanmanager.Reports.CashReport.T_credit;
import static com.dr7.salesmanmanager.Reports.CashReport.cashPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.credit;
import static com.dr7.salesmanmanager.Reports.CashReport.creditCardPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.creditPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.net;
import static com.dr7.salesmanmanager.Reports.CashReport.returnCridet;
import static com.dr7.salesmanmanager.Reports.CashReport.total;
import static com.dr7.salesmanmanager.Reports.CashReport.total_cash;
import static com.dr7.salesmanmanager.ShowPlan.noteType;
import static com.dr7.salesmanmanager.ShowPlan.state;
import static com.itextpdf.text.Element.ALIGN_CENTER;

public class PdfConverter {
    private Context context;
    Document doc;
    File file;
    PdfWriter docWriter = null;
    //    PDFView pdfView;
    File pdfFileName;
    BaseFont base;
    CompanyInfo companyInfo;
    DatabaseHandler obj;
    static public NumberToWordExample3.AbstractProcessor processor;
    private int directionOfHeader = Element.ALIGN_RIGHT;

    {
        try {
            base = BaseFont.createFont("/assets/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Font arabicFont = new Font(base, 10f);
    Font arabicFontHeader = new Font(base, 11f);
    Font arabicFontHeaderprint = new Font(base, 17f);
    Font arabicFontHeaderVochprint = new Font(base, 25f);

    public PdfConverter(Context context) {
        this.context = context;
        obj = new DatabaseHandler(context);
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    public String getCurentTimeDate(int flag) {
        String dateCurent, timeCurrent, dateTime = "";
        Date currentTimeAndDate;
        SimpleDateFormat dateFormat, timeformat;
        currentTimeAndDate = Calendar.getInstance().getTime();
        if (flag == 1)// return date
        {

            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateCurent = dateFormat.format(currentTimeAndDate);
            dateTime = convertToEnglish(dateCurent);

        } else {
            if (flag == 2)// return time
            {
                timeformat = new SimpleDateFormat("hh:mm:ss");
                dateCurent = timeformat.format(currentTimeAndDate);
                dateTime = convertToEnglish(dateCurent);
            }
        }
        return dateTime;

    }
    public void exportListToPdf_account(List<?> list, String headerDate, String fromDate,String toDate, int report) {

        PdfPTable pdfPTable = new PdfPTable(1);
        PdfPTable pdfPTableHeader = new PdfPTable(1);
        pdfPTable = getContentTable(list, report);

        String dateTime = "";
        dateTime = getCurentTimeDate(1);
        //dateTime=dateTime+getCurentTimeDate(2);
        pdfPTableHeader = getHeaderTable(list, report, fromDate, toDate);

        try {
//

            if (doc != null) doc.add(pdfPTableHeader);
            if (doc != null) doc.add(pdfPTable);
            if (report != 13 && report != 14&& report != 16&& report != 17&& report != 18)
                Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_LONG).show();

        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("DocumentException",e.getMessage() );
        }
        endDocPdf();

        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                if (report != 13 && report != 14 && report != 16&& report != 17&& report != 18) showPdf(pdfFileName);
                Log.e("SDK_INT", "Permission is granted");
            } else {

                Log.e("SDK_INT", "Permission is revoked");
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        } else { // permission is automatically granted on sdk<23 upon
            // installation
            showPdf(pdfFileName);
            Log.e("here,", "Permission is granted");
        }

    }
    public void exportListToPdf(List<?> list, String headerDate, String date, int report) {

        PdfPTable pdfPTable = new PdfPTable(1);
        PdfPTable pdfPTableHeader = new PdfPTable(1);
        pdfPTable = getContentTable(list, report);

        String dateTime = "";
        dateTime = getCurentTimeDate(1);
        //dateTime=dateTime+getCurentTimeDate(2);
        pdfPTableHeader = getHeaderTable(list, report, headerDate, dateTime);

        try {
//

            if (doc != null) doc.add(pdfPTableHeader);
            if (doc != null) doc.add(pdfPTable);
            if (report != 13 && report != 14&& report != 16&& report != 17&& report != 18)
                Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_LONG).show();

        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("DocumentException",e.getMessage() );
        }
        endDocPdf();

        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
               if (report != 13 && report != 14 && report != 16&& report != 17&& report != 18) if(report!=19)showPdf(pdfFileName);
                Log.e("SDK_INT", "Permission is granted");
            } else {

                Log.e("SDK_INT", "Permission is revoked");
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        } else { // permission is automatically granted on sdk<23 upon
            // installation
            if(report!=19) showPdf(pdfFileName);
            Log.e("here,", "Permission is granted");
        }

    }

    private PdfPTable getContentTable(List<?> list, int reportType) {
        PdfPTable tableContent = new PdfPTable(1);
        switch (reportType) {
            case 1:
                tableContent = creatTableCustomerLog((List<Transaction>) list);
                break;
            case 2:
                tableContent = creatTableInventory((List<inventoryReportItem>) list);
                break;
            case 3:
                tableContent = creatTableVoucher((List<Voucher>) list);
                break;
            case 4:
                tableContent = creatItemsReport((List<Item>) list);
                break;
            case 5:
                tableContent = creatPaymentReport((List<Payment>) list);
                break;
            case 6:
                tableContent = creatItems_StockReport((List<Item>) list);
                break;
            case 7:
                tableContent = creatTableVoucher_Stock((List<Voucher>) list);
                break;

            case 8:
                tableContent = creatCashReport((List<Voucher>) list);
                break;
            case 9:
                tableContent = createunCollectedReport((List<Payment>) list);
                break;

            case 10:
                Log.e("createAccountReport", "" + list.size());
                tableContent = createAccountReport((List<Account__Statment_Model>) list);
                break;
            case 11:
                Log.e("createAccountReport", "" + list.size());
                tableContent = createShelReport((List<serialModel>) list);
                break;
            case 12:
                Log.e("createReturnVocher", "" + list.size());
                tableContent = createReturnInvoice((List<Item>) list);
                break;
            case 13:
                Log.e("createReturnVocher", "" + list.size());
                //    tableContent=
                createInvoiceForPrint((List<Item>) list);
                Log.e("path==", String.valueOf(pdfFileName));

                PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                try {
                    PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(context, String.valueOf(pdfFileName));
                    Log.e("path2==", String.valueOf(pdfFileName));
                    printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
                    Log.e("path3==", String.valueOf(pdfFileName));
                } catch (Exception e) {
                    Log.e("Exception==", e.getMessage());
                }


                break;
            case 14:
                Log.e("createReturnVocher", "" + list.size());
                //    tableContent=
                createSaleInvoiceForPrint((List<Item>) list);
                Log.e("path==", String.valueOf(pdfFileName));

                PrintManager printManager1 = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                try {
                    PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(context, String.valueOf(pdfFileName));
                    Log.e("path2==", String.valueOf(pdfFileName));
                    printManager1.print("Document", printAdapter, new PrintAttributes.Builder().build());
                    Log.e("path3==", String.valueOf(pdfFileName));
                } catch (Exception e) {
                    Log.e("Exception==", e.getMessage());
                }


                break;
            case 15:
                Log.e("createserials",""+list.size());
                tableContent=createSerialsReport((List<serialModel>) list);
                break;
            case 16:
                Log.e("createReturnVocher", "" + list.size());
                //    tableContent=
                createInvoiceForPrint((List<Item>) list);
                Log.e("path==", String.valueOf(pdfFileName));
                GeneralMethod generalMethod=new GeneralMethod(context);
          generalMethod.shareWhatsAppA(pdfFileName,2);
                break;
            case 17:
                Log.e("createReturnVocher", "" + list.size());
                //    tableContent=
                createInvoiceForShare ((List<Item>) list);
                Log.e("path==", String.valueOf(pdfFileName));
                GeneralMethod generalMethod1=new GeneralMethod(context);
          generalMethod1.shareWhatsAppA(pdfFileName,2);
                break;

            case 18:
                Log.e("createReturnVocher", "" + list.size());
                //    tableContent=
                obj = new DatabaseHandler(context);
                if ( obj.getAllCompanyInfo().size()!= 0) {
                    if (obj.getAllCompanyInfo().get(0) != null) {
                        companyInfo = obj.getAllCompanyInfo().get(0);
                        if (!companyInfo.getCompanyName().equals(""))
                        {
                            createSaleInvoiceForPrint2((List<Item>) list);
                            Log.e("path==", String.valueOf(pdfFileName));

                            PrintManager printManager2 = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                            try {
//                                PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(context, String.valueOf(pdfFileName));
//                                Log.e("path2==", String.valueOf(pdfFileName));
//                                printManager2.print("Document", printAdapter, new PrintAttributes.Builder().build());
//                                Log.e("path3==", String.valueOf(pdfFileName));

                                GeneralMethod generalMethod2=new GeneralMethod(context);
                                generalMethod2.shareWhatsAppA(pdfFileName,2);


                            } catch (Exception e) {
                                Log.e("Exception==", e.getMessage());
                            }
        } else {
            Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
        }
    }else {
        Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
    }
}
        else {
                Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                }

        break;
            case 19:
                Log.e("createserials",""+list.size());
                tableContent=createMedicalReport((List<visitMedicalModel>) list);
                break;
            case 19:
                Log.e("createReturnVocher", "" + list.size());
                //    tableContent=
                createInvoiceForSharePlan ((List<SalesManPlan>) list);
                Log.e("path==", String.valueOf(pdfFileName));
                GeneralMethod generalMethod2=new GeneralMethod(context);
                generalMethod2.shareWhatsAppA(pdfFileName,2);
                break;

            case 20:
                Log.e("createReturnVocher", "" + list.size());
                createInvoiceForSharePlan ((List<SalesManPlan>) list);

                break;
        }


        return tableContent;
    }

    private PdfPTable createunCollectedReport(List<Payment> list) {
        createPDF("UnCollectedCheques_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(4);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);


        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.app_bank_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.check_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.chaequeDate), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.app_amount), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

//        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getBank()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getDueDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getAmount()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }

    private PdfPTable createAccountReport(List<Account__Statment_Model> list) {
        createPDF("AccountStatment" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(6);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getResources().getString(R.string.balance), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTable, context.getResources().getString(R.string.debit), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.credit), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getString(R.string.date_voucher), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getString(R.string.transName), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getString(R.string.voucherNo), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getBalance()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getDebit()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

            insertCell(pdfPTable, String.valueOf(list.get(i).getCredit()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


            insertCell(pdfPTable, String.valueOf(list.get(i).getDate_voucher()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

            insertCell(pdfPTable, String.valueOf(list.get(i).getTranseNmae()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        }

        return pdfPTable;

    }

    private PdfPTable createShelReport(List<serialModel> list) {
        createPDF("ShelfInventoeryReport" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.voucherNo), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.serialcode), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.customer_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.voucher_date), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getSerialCode()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCustomerNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getDateVoucher()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }

    private PdfPTable createReturnInvoice(List<Item> list) {
        //  insertCell(pdfPTable,context.getString(R.string.serialcode      )                        , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);


        createPDF("ReturnInvoice" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);


        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.voucherNo), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.app_price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getPrice()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

            //     insertCell(pdfPTable, String.valueOf(list.get(i).getDateVoucher()        ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            //      insertCell(pdfPTable, String.valueOf(list.get(i).getSerialCode()       ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }

    private void createInvoiceForPrint(List<Item> list) {
        //  insertCell(pdfPTable,context.getString(R.string.serialcode      )
        //  , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        obj = new DatabaseHandler(context);


        if (obj.getAllCompanyInfo().get(0) != null) {
            companyInfo = obj.getAllCompanyInfo().get(0);
            if (
                    !companyInfo.getCompanyName().equals("") ) {

                createvocherPDF("Invoice" + ".pdf", list);
            } else {
                Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
            }
        }
//        PdfPTable pdfPTable = new PdfPTable(5);
//        pdfPTable.setWidthPercentage(100f);
//        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//
//
//
//        insertCell(pdfPTable,context.getString(R.string.voucherNo), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getString(R.string.item_number      )                        , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getResources().getString(R.string.item_name   )   , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getResources().getString(R.string.qty) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getResources().getString(R.string.app_price) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//
//
//
//      pdfPTable.setHeaderRows(1);
//        for (int i = 0; i < list.size(); i++) {
//           insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber() ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo())       , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//             insertCell(pdfPTable, String.valueOf(list.get(i).getItemName()         ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            insertCell(pdfPTable, String.valueOf(list.get(i).getQty()         ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            insertCell(pdfPTable, String.valueOf(list.get(i).getPrice()       ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//
//            //     insertCell(pdfPTable, String.valueOf(list.get(i).getDateVoucher()        ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            //      insertCell(pdfPTable, String.valueOf(list.get(i).getSerialCode()       ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//        }
//        insertCell(pdfPTable, context.getString(R.string.sub_total) + " : " +  PrintVoucher.vouchPrinted.getSubTotal(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, context.getString(R.string.discount_value) + " : " +  PrintVoucher.vouchPrinted.getVoucherDiscount(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, context.getString(R.string.tax) + " : " +  PrintVoucher.vouchPrinted.getTax(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, context.getString(R.string.net_sales) + " : " +  PrintVoucher.vouchPrinted.getNetSales(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, "  استلمت البضاعة كاملة و بحالة جيدة وخالية من اية عيوب واتعهد بدفع قيمة هذه الفاتورة ." , Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, "المستلم : ", Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, "التوقيع :", Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
        //return pdfPTable;

    }
    private void createInvoiceForShare(List<Item> list) {
        //  insertCell(pdfPTable,context.getString(R.string.serialcode      )
        //  , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        obj = new DatabaseHandler(context);


        if (obj.getAllCompanyInfo().get(0) != null) {
            companyInfo = obj.getAllCompanyInfo().get(0);
            if (
                    !companyInfo.getCompanyName().equals("") ) {

                createvocherPDFForShare("Invoice" + ".pdf", list);
            } else {
                Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
            }
        }
//        PdfPTable pdfPTable = new PdfPTable(5);
//        pdfPTable.setWidthPercentage(100f);
//        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//
//
//
//        insertCell(pdfPTable,context.getString(R.string.voucherNo), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getString(R.string.item_number      )                        , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getResources().getString(R.string.item_name   )   , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getResources().getString(R.string.qty) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//        insertCell(pdfPTable,context.getResources().getString(R.string.app_price) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//
//
//
//      pdfPTable.setHeaderRows(1);
//        for (int i = 0; i < list.size(); i++) {
//           insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber() ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo())       , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//             insertCell(pdfPTable, String.valueOf(list.get(i).getItemName()         ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            insertCell(pdfPTable, String.valueOf(list.get(i).getQty()         ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            insertCell(pdfPTable, String.valueOf(list.get(i).getPrice()       ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//
//            //     insertCell(pdfPTable, String.valueOf(list.get(i).getDateVoucher()        ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//            //      insertCell(pdfPTable, String.valueOf(list.get(i).getSerialCode()       ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//        }
//        insertCell(pdfPTable, context.getString(R.string.sub_total) + " : " +  PrintVoucher.vouchPrinted.getSubTotal(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, context.getString(R.string.discount_value) + " : " +  PrintVoucher.vouchPrinted.getVoucherDiscount(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, context.getString(R.string.tax) + " : " +  PrintVoucher.vouchPrinted.getTax(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, context.getString(R.string.net_sales) + " : " +  PrintVoucher.vouchPrinted.getNetSales(), Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, "  استلمت البضاعة كاملة و بحالة جيدة وخالية من اية عيوب واتعهد بدفع قيمة هذه الفاتورة ." , Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, "المستلم : ", Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//        insertCell(pdfPTable, "التوقيع :", Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
        //return pdfPTable;

    }

    private void createInvoiceForSharePlan(List<SalesManPlan> list) {

        obj = new DatabaseHandler(context);


        if (obj.getAllCompanyInfo().get(0) != null) {
            companyInfo = obj.getAllCompanyInfo().get(0);
            if (
                    !companyInfo.getCompanyName().equals("") ) {

                createvocherPDFForSharePlan("Plan" + ".pdf", list);
            } else {
                Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
            }
        }


    }

    private void createInvoiceForShare2(List<Item> list) {
        //  insertCell(pdfPTable,context.getString(R.string.serialcode      )
        //  , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        obj = new DatabaseHandler(context);


        if (obj.getAllCompanyInfo().get(0) != null) {
            companyInfo = obj.getAllCompanyInfo().get(0);
            if (
                    !companyInfo.getCompanyName().equals("") ) {

                createvocherPDFForShare("Invoice" + ".pdf", list);
            } else {
                Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
            }
        }


    }
    private void createSaleInvoiceForPrint(List<Item> list) {
        //  insertCell(pdfPTable,context.getString(R.string.serialcode      )
        //  , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        obj = new DatabaseHandler(context);


        if ( obj.getAllCompanyInfo().get(0) != null) {
            companyInfo = obj.getAllCompanyInfo().get(0);
            if (
                    !companyInfo.getCompanyName().equals("")
                         ) {

                createSalevocherPDF("Invoice" + ".pdf", list);
            } else {
                Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
            }
        }

    }
    private void createSaleInvoiceForPrint2(List<Item> list) {
        //  insertCell(pdfPTable,context.getString(R.string.serialcode      )
        //  , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        obj = new DatabaseHandler(context);

        if ( obj.getAllCompanyInfo().size()!= 0) {
            if (obj.getAllCompanyInfo().get(0) != null) {
                companyInfo = obj.getAllCompanyInfo().get(0);
                if (
                        !companyInfo.getCompanyName().equals("")
                ) {

                    createSalevocherPDF2("Invoice" + ".pdf", list);
                } else {
                    Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_LONG).show();
        }
    }
    private PdfPTable creatTableCustomerLog(List<Transaction> list) {
        createPDF("CustomerLog_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(7);
        pdfPTable.setWidthPercentage(100f);
        if (!languagelocalApp.equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);


        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getResources().getString(R.string.cust_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_IN_DATE), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_IN_TIME), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_OUT_DATE), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_OUT_TIME), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CUS_CODE), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.SALES_MAN_ID), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

//            pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getCusName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckInDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckInTime()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckOutDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckOutTime()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCusCode()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getSalesManId()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }


        return pdfPTable;

    }

    private PdfPTable creatTableInventory(List<inventoryReportItem> list) {
        createPDF("Inventory_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(3);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getResources().getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.unit_qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

//            insertCell(pdfPTable, String.valueOf(String.format("%.3f", (list.get(i).getSalesManId()))), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }
    private PdfPTable createMedicalReport(List<visitMedicalModel> list)
    {
        createPDF("MedicalReport" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(7);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable,context.getString(R.string.cust_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getString(R.string.Adoption), ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.tool), ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.doubleVisit), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.remark), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getCustname() ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getAdoption()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getTool()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getDoubleVisit()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getRemark()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getNameItem()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

            insertCell(pdfPTable, String.valueOf(list.get(i).getQtyItem()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }
    private PdfPTable createSerialsReport(List<serialModel> list)
    {
        createPDF("SerialsReport" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable,context.getString(R.string.voucher_date), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getString(R.string.voucher_type), ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.serialcode), ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.item_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.voucher_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getDateVoucher() ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getKindVoucher()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getSerialCode()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }

    private PdfPTable creatTableVoucher(List<Voucher> list) {
        createPDF("Voucher_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(8);
        pdfPTable.setWidthPercentage(100f);


        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.cust_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.No), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.voucher_date), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.pay_method), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.app_disc), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.sub_total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.net_sales), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//            pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getCustName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getPayMethod()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherDiscount()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getSubTotal()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getTax()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getNetSales()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

//            insertCell(pdfPTable, String.valueOf(String.format("%.3f", (list.get(i).getSalesManId()))), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }

    private PdfPTable creatTableVoucher_Stock(List<Voucher> list) {
        createPDF("VoucherStock_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(3);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);


        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.voucher_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.voucher_date), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.remark), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getRemark()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        return pdfPTable;

    }

    private PdfPTable creatCashReport(List<Voucher> list) {
        createPDF("CashReport" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(2);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);


        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.sales), ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.cash_sale), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(T_cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.credit_sales), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(T_credit), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.total_sales), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTable, "      ", ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
        //***************************************************************************

        insertCell(pdfPTable, context.getString(R.string.payment), ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(cashPayment), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.app_cheque), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(creditPayment), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.netpayment), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(net), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "      ", ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.app_creditCard), ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.credit_value), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTable, String.valueOf((credit - returnCridet)), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTable, context.getResources().getString(R.string.total_cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, String.valueOf(total_cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        return pdfPTable;

    }

    private PdfPTable creatPaymentReport(List<Payment> list) {
        createPDF("Payment_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(7);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.voucher_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.pay_date), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.cust_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.app_amount), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.remark), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.sale_man_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.pay_method), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


//        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getPayDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCustName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getAmount()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getRemark()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getSaleManNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getPayMethod()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        }

        return pdfPTable;

    }

    private PdfPTable creatItemsReport(List<Item> list) {
        createPDF("Items_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.item_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.total_sold_qty2), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable, context.getResources().getString(R.string.total_bonus_qty2), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.total_sales_noTax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        Float calTotalSales[] = new Float[list.size()];

        for (int n = 0; n < list.size(); n++) {
            calTotalSales[n] = ((list.get(n).getQty() * list.get(n).getPrice()) - list.get(n).getDisc());
            Log.e("calTotalSales", "\t" + calTotalSales[n]);

        }


//            pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getBonus()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(calTotalSales[i]), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }

        return pdfPTable;

    }

    private PdfPTable creatItems_StockReport(List<Item> list) {
        createPDF("Items_Stock_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(3);
        pdfPTable.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        }

        insertCell(pdfPTable, context.getString(R.string.item_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getResources().getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        Float calTotalSales[] = new Float[list.size()];

        for (int n = 0; n < list.size(); n++) {
            calTotalSales[n] = ((list.get(n).getQty() * list.get(n).getPrice()) - list.get(n).getDisc());
            Log.e("calTotalSales", "\t" + calTotalSales[n]);

        }


//        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }


        return pdfPTable;

    }


    private PdfPTable getHeaderTable(List<?> list, int reportType, String headerDate, String
            date) {
        try {
            companyInfo = obj.getAllCompanyInfo().get(0);
        }catch (Exception e){
            Toast.makeText(context, ""+context.getResources().getString(R.string.error_companey_info), Toast.LENGTH_SHORT).show();
        }

        PdfPTable pdfPTableHeader = new PdfPTable(7);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);
        pdfPTableHeader.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//        pdfPTableHeader.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        if (reportType != 13 && reportType != 14&& reportType != 16&& reportType != 17&& reportType != 18&&reportType != 10)
            insertCell(pdfPTableHeader, context.getString(R.string.date) + " : " + date, Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
        if (reportType == 10)
        {  try {
            if (companyInfo.getLogo() != null && !companyInfo.getLogo().equals("")) {
                Bitmap imageBytes = companyInfo.getLogo();
                imageBytes = getResizedBitmap(imageBytes, 80, 80);
                byte[] bytes = convertBitmapToByteArray(imageBytes);


                BitmapFactory.decodeByteArray(bytes, 0, 10);
                try {

                    Image imageView = Image.getInstance(bytes);
                    imageView.setAlignment(ALIGN_CENTER);
                    //   imageView.scaleToFit(10,10);

                    doc.add(imageView);

                } catch (Exception e) {
                }


//                doc.add(headertable);

            }
//            insertCell(pdfPTableHeader, companyInfo.getCompanyName()+"" , ALIGN_CENTER, 7, arabicFontHeader, BaseColor.BLACK);
//            insertCell(pdfPTableHeader, context.getString(R.string.app_account_report)+"" , ALIGN_CENTER, 7, arabicFontHeader, BaseColor.BLACK);
//            insertCell(pdfPTableHeader, "\t\t"+context.getString(R.string.from_date)+"  \t\t"+headerDate+"  \t\t "+context.getString(R.string.to_date)+"  \t"+date+"\t\t" , ALIGN_CENTER, 7, arabicFontHeader, BaseColor.BLACK);
//
//            insertCell(pdfPTableHeader, context.getString(R.string.customer_number) + " : " + CustomerListShow.Customer_Account, Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
//
//            insertCell(pdfPTableHeader, context.getString(R.string.cust_name) + " : " + CustomerListShow.Customer_Name, Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100f);

            if (!Locale.getDefault().getLanguage().equals("ar")) {


                table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

                //   headertable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

            } else {
                table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


            }
            PdfPCell cell14 = new PdfPCell(new Paragraph(companyInfo.getCompanyName() , arabicFontHeaderprint));
            PdfPCell cell144 = new PdfPCell(new Paragraph(context.getString(R.string.app_account_report) , arabicFontHeaderprint));
            PdfPCell cell13 = new PdfPCell(new Paragraph("\t\t"+context.getString(R.string.from_date)+"  \t\t"+headerDate+"  \t\t "+context.getString(R.string.to_date)+"  \t"+date+"\t\t", arabicFontHeaderprint));
            PdfPCell cell16 = new PdfPCell(new Paragraph(context.getString(R.string.customer_number) + " : " + CustomerListShow.Customer_Account, arabicFontHeaderprint));
            PdfPCell cell15 = new PdfPCell(new Paragraph(context.getString(R.string.cust_name) + " : " + CustomerListShow.Customer_Name, arabicFontHeaderprint));
            cell14.setBorder(Rectangle.NO_BORDER);
            cell144.setBorder(Rectangle.NO_BORDER);
            cell13.setBorder(Rectangle.NO_BORDER);
            cell15.setBorder(Rectangle.NO_BORDER);
            cell16.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell14);table.addCell(cell144);
            table.addCell(cell13); table.addCell(cell16); table.addCell(cell15);

            try {
                doc.add(table);
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            Toast.makeText(context, ""+context.getResources().getString(R.string.error_companey_info), Toast.LENGTH_SHORT).show();
        }
        }


        return pdfPTableHeader;
    }

    void createPDF(String fileName) {
        doc = new Document();
        docWriter = null;
        try {


            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ReportVanSales/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;

            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.setPageSize(PageSize.A4);//size of page
            //open document
            doc.open();
            Paragraph paragraph = new Paragraph();
            paragraph.add("");
            doc.add(paragraph);

            Log.e("path44", "" + targetPdf);
            pdfFileName = path;
            Log.e("pdfFileName", "pdfFileName=" + pdfFileName);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    void createvocherPDF(String fileName, List<Item> list) {
        doc = new Document();

        docWriter = null;

        try {


            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ReportVanSales/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;

            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            docWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            doc.setPageSize(PageSize.A4);//size of page
            //open document
            doc.open();


            String voucherTyp = "";
            switch (PrintVoucher.vouchPrinted.getVoucherType()) {
                case 504:
                    voucherTyp = "فاتورة بيع";
                    break;
                case 506:
                    voucherTyp = "فاتورة مرتجعات";
                    break;
                case 508:
                    voucherTyp = "طلب جديد";
                    break;
            }
            //
            String paymentTyp = "";
            switch (PrintVoucher.vouchPrinted.getPayMethod()) {
                case 0:
                    paymentTyp = "ذمم";
                    break;
                case 1:
                    paymentTyp = "نقدا";
                    break;

            }

            PdfPTable headertable = new PdfPTable(1);
            headertable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell13 = new PdfPCell(new Paragraph("                    " + companyInfo.getCompanyName() + "\n", arabicFontHeaderVochprint));
            cell13.setBorder(Rectangle.NO_BORDER);
            if(MainActivity.CompanyinfoINPdf_Hide==0)
                headertable.addCell(cell13);

            if (companyInfo.getLogo() != null && !companyInfo.getLogo().equals("")) {
                Bitmap imageBytes = companyInfo.getLogo();
                imageBytes = getResizedBitmap(imageBytes, 80, 80);
                byte[] bytes = convertBitmapToByteArray(imageBytes);


                BitmapFactory.decodeByteArray(bytes, 0, 10);
                try {

                    Image imageView = Image.getInstance(bytes);
                    imageView.setAlignment(ALIGN_CENTER);
                    //   imageView.scaleToFit(10,10);

                    doc.add(imageView);

                } catch (Exception e) {
                }


                doc.add(headertable);

            }
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));

            String date = getCurentTimeDate(1);

            PdfPTable table = new PdfPTable(1);

            PdfPTable pdfPTable = new PdfPTable(4);
            pdfPTable.setWidthPercentage(100f);

            PdfPTable pdfPTable3 = new PdfPTable(4);
            pdfPTable3.setWidthPercentage(100f);

            if (!Locale.getDefault().getLanguage().equals("ar")) {


                table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
             //   headertable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

            } else {

                table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


            }

            PdfPCell cell1 = new PdfPCell(new Paragraph(context.getString(R.string.date) + " : " +PrintVoucher.vouchPrinted.getVoucherDate() , arabicFontHeaderprint));

             PdfPCell cell2 = new PdfPCell(new Paragraph(context.getString(R.string.company_tel) + " : " + companyInfo.getcompanyTel(), arabicFontHeaderprint));

            PdfPCell cell3 = new PdfPCell(new Paragraph(context.getString(R.string.tax_no) + companyInfo.getTaxNo(), arabicFontHeaderprint));
            PdfPCell cell4 = new PdfPCell(new Paragraph(context.getString(R.string.voucherNo) + " : " + PrintVoucher.vouchPrinted.getVoucherNumber(), arabicFontHeaderprint));
            PdfPCell cell5 = new PdfPCell(new Paragraph(context.getString(R.string.cust_name) + " : " + PrintVoucher.vouchPrinted.getCustName(), arabicFontHeaderprint));
            PdfPCell cell6 = new PdfPCell(new Paragraph(context.getString(R.string.note) + " : " + companyInfo.getNoteForPrint(), arabicFontHeaderprint));
            PdfPCell cell14 = new PdfPCell(new Paragraph(context.getString(R.string.app_paymentType) + " : " + paymentTyp, arabicFontHeaderprint));


            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell4.setBorder(Rectangle.NO_BORDER);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell6.setBorder(Rectangle.NO_BORDER);
            cell14.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell1);
            if(MainActivity.CompanyinfoINPdf_Hide==0)
                table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell14);

            doc.add(table);


            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("\n"));

            /***********/

            insertCell(pdfPTable, context.getResources().getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, context.getResources().getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, context.getResources().getString(R.string.app_price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, context.getResources().getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

            doc.add(pdfPTable);


           /**************/

            // pdfPTable3.setHeaderRows(1);
            Log.e("list.size()==", list.size() + "");
            //  list.add(list.get(list.size()-1));
            for (int i = 0; i < list.size(); i++) {


                if (PrintVoucher.vouchPrinted.getVoucherNumber() == list.get(i).getVoucherNumber()
                        && PrintVoucher.vouchPrinted.getVoucherType() == list.get(i).getVoucherType()) {
                    Log.e("getVoucherNumber5==", list.get(i).getVoucherNumber() + "");
                    Log.e("itenu==", list.get(i).getItemNo() + "");
                    Log.e("itemname==", list.get(i).getItemName() + "");
                    Log.e("qty==", list.get(i).getQty() + "");
                    Log.e("getprice==", list.get(i).getPrice() + "");

                    insertCell(pdfPTable3, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getPrice()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

                    String amount = "" + (list.get(i).getQty() * list.get(i).getPrice() - list.get(i).getDisc());
                    Log.e("amount==", amount + "");
                    insertCell(pdfPTable3, amount, ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

                }
            }

            //   doc.add(new Paragraph(list.get(0).getItemName(),arabicFont));
            doc.add(pdfPTable3);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));


            PdfPTable table2 = new PdfPTable(1);
            table2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell7 = new PdfPCell(new Paragraph(context.getString(R.string.sub_total) + " : " + PrintVoucher.vouchPrinted.getSubTotal(), arabicFontHeaderprint));
            PdfPCell cell8 = new PdfPCell(new Paragraph(context.getString(R.string.discount_value) + " : " + PrintVoucher.vouchPrinted.getVoucherDiscount(), arabicFontHeaderprint));

            PdfPCell cell9 = new PdfPCell(new Paragraph(context.getString(R.string.net_sales) + " : " + PrintVoucher.vouchPrinted.getNetSales(), arabicFontHeaderprint));
            PdfPCell cell10 = new PdfPCell(new Paragraph("استلمت البضاعة كاملة و بحالة جيدة وخالية من اية عيوب واتعهد بدفع قيمة هذه الفاتورة .", arabicFontHeaderprint));
            PdfPCell cell11 = new PdfPCell(new Paragraph("المستلم : ", arabicFontHeaderprint));
            PdfPCell cell12 = new PdfPCell(new Paragraph("التوقيع :", arabicFontHeaderprint));


            cell7.setBorder(Rectangle.NO_BORDER);
            cell8.setBorder(Rectangle.NO_BORDER);
            cell9.setBorder(Rectangle.NO_BORDER);
            cell10.setBorder(Rectangle.NO_BORDER);
            cell11.setBorder(Rectangle.NO_BORDER);
            cell12.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell7);
            table2.addCell(cell8);
            table2.addCell(cell9);
            table2.addCell(cell10);
            table2.addCell(cell11);
            table2.addCell(cell12);
            doc.add(table2);

            Log.e("path44", "" + targetPdf);
            pdfFileName = path;
            Log.e("pdfFileName", "" + pdfFileName);


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    void createvocherPDFForShare(String fileName, List<Item> list) {
        Log.e("createvocherPDFForShare", "" + "createvocherPDFForShare");
        doc = new Document();

        docWriter = null;

        try {

            PdfPCell emptycell = new PdfPCell(new Paragraph("        ", arabicFontHeaderprint));
            emptycell.setBorder(Rectangle.NO_BORDER);
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ReportVanSales/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;

            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            docWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            doc.setPageSize(PageSize.A4);//size of page


            //open document
            doc.open();

            String voucherTyp = "";
            switch (PrintVoucher.vouchPrinted.getVoucherType()) {
                case 504:
                    voucherTyp = "فاتورة بيع";
                    break;
                case 506:
                    voucherTyp = "فاتورة مرتجعات";
                    break;
                case 508:
                    voucherTyp = "طلب جديد";
                    break;
            }
            //
            String paymentTyp = "";
            switch (PrintVoucher.vouchPrinted.getPayMethod()) {
                case 0:
                    paymentTyp = "ذمم";
                    break;
                case 1:
                    paymentTyp = "نقدا";
                    break;

            }

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100f);
            headertable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell13 = new PdfPCell(new Paragraph("" + companyInfo.getCompanyName() + "\n", arabicFontHeaderVochprint));
            cell13.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            PdfPCell cell2 = new PdfPCell(new Paragraph( companyInfo.getcompanyTel(), arabicFontHeaderprint));
            doc.add(new Paragraph("\n"));
            cell13.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);

            if(MainActivity.CompanyinfoINPdf_Hide==0) {
                headertable.addCell(cell13);
                headertable.addCell(cell2);
            }
            doc.add(headertable);


//            doc.add(new Paragraph("\n"));
        //     doc.add(new Paragraph("\n"));

        String date = getCurentTimeDate(1);

        PdfPTable table = new PdfPTable(1);

        PdfPTable pdfPTable = new PdfPTable(6);
        pdfPTable.setWidthPercentage(100f);

        PdfPTable pdfPTable3 = new PdfPTable(6);
        pdfPTable3.setWidthPercentage(100f);

        if (!Locale.getDefault().getLanguage().equals("ar")) {


            table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            //   headertable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        } else {

            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


        }

            table.addCell(emptycell);


            PdfPTable table222 = new PdfPTable(3);
            table222.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            PdfPCell cell_101 = new PdfPCell(new Paragraph("عرض سعر", arabicFontHeaderprint));
            cell_101.setHorizontalAlignment((cell_101.ALIGN_CENTER));
            PdfPCell cell_102 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell_102.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell_103 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell_103.setBorder(Rectangle.NO_BORDER);

            table222.addCell(cell_102);
            table222.addCell(cell_101);
            table222.addCell(cell_103);

            doc.add(table222);


            PdfPTable table02 = new PdfPTable(3);
            table02.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            PdfPCell cell201 = new PdfPCell(new Paragraph(paymentTyp, arabicFontHeaderprint));
            cell201.setBorder(Rectangle.BOTTOM);
            cell201.setHorizontalAlignment((cell201.ALIGN_CENTER));
            PdfPCell cell202 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell202.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell203 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell203.setBorder(Rectangle.NO_BORDER);

            table02.addCell(cell202);
            table02.addCell(cell201);

            table02.addCell(cell203);
            doc.add(table02);

            table.setWidthPercentage(100f);

        PdfPCell cell4 = new PdfPCell(new Paragraph("رقم العرض" + " : " + PrintVoucher.vouchPrinted.getVoucherNumber() + "   Vocher No         " + "التاريخ " + " : " + PrintVoucher.vouchPrinted.getVoucherDate() + "        Date", arabicFontHeaderprint));

        cell4.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Paragraph(context.getString(R.string.cust_name) + " : " + PrintVoucher.vouchPrinted.getCustName(), arabicFontHeaderprint));
        cell5.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell5);
            table.addCell(emptycell);
        doc.add(table);


        /***********/

     insertCell(pdfPTable, "Item Code \nرمز المادة", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
//            PdfPCell cell41 = new PdfPCell(new Paragraph("Item Code \nرمز المادة" +, arabicFontHeaderprint));
//
//            pdfPTable.addCell(cell41);


        insertCell(pdfPTable, "Description \nاسم المادة", ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
        insertCell(pdfPTable, "Qty \nالكمية", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
        insertCell(pdfPTable, "Price \nالسعر", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
        insertCell(pdfPTable, "Total \nالمجموع", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

        doc.add(pdfPTable);


        /**************/

        // pdfPTable3.setHeaderRows(1);
//        Log.e("list.size()==", list.size() + "");
        //  list.add(list.get(list.size()-1));
            int count=0;
        for (int i = 0; i < list.size(); i++) {


            if (PrintVoucher.vouchPrinted.getVoucherNumber() == list.get(i).getVoucherNumber()
                    && PrintVoucher.vouchPrinted.getVoucherType() == list.get(i).getVoucherType()) {
//                Log.e("getVoucherNumber5==", list.get(i).getVoucherNumber() + "");
//                Log.e("itenu==", list.get(i).getItemNo() + "");
//                Log.e("itemname==", list.get(i).getItemName() + "");
//                Log.e("qty==", list.get(i).getQty() + "");
//                Log.e("getprice==", list.get(i).getPrice() + "");

                insertCell(pdfPTable3, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                insertCell(pdfPTable3, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
                insertCell(pdfPTable3, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
                insertCell(pdfPTable3, String.valueOf(list.get(i).getPrice()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                String amount = "" + (list.get(i).getQty() * list.get(i).getPrice() - list.get(i).getDisc());
                Log.e("amount==", amount + "");
                insertCell(pdfPTable3, amount, ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
                count=i;
            }
        }


        //   doc.add(new Paragraph(list.get(0).getItemName(),arabicFont));
        doc.add(pdfPTable3);

            for(int x=0;x<15-count;x++)
                doc.add(new Paragraph("\n"));

        PdfPTable table2 = new PdfPTable(4);
        table2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table2.setWidthPercentage(100f);
        PdfPCell cell7 = new PdfPCell(new Paragraph(context.getString(R.string.sub_total) + " : " , arabicFontHeaderprint));
            PdfPCell cell71 = new PdfPCell(new Paragraph( PrintVoucher.vouchPrinted.getSubTotal()+"", arabicFontHeaderprint));

            PdfPCell cell72 = new PdfPCell(new Paragraph( "الملاحظات"+ " : " , arabicFontHeaderprint));

            PdfPCell cell73 = new PdfPCell(new Paragraph( "_______"  , arabicFontHeaderprint));
            cell7.setBorder(Rectangle.NO_BORDER);
            cell71.setBorder(Rectangle.NO_BORDER);
            cell72.setBorder(Rectangle.NO_BORDER);
            cell73.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell7);
            table2.addCell(cell71);
            table2.addCell(cell72);
            table2.addCell(cell73);
            doc.add(table2);

            PdfPTable table21 = new PdfPTable(4);
            table21.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table21.setWidthPercentage(100f);
        PdfPCell cell8 = new PdfPCell(new Paragraph(context.getString(R.string.discount_value) , arabicFontHeaderprint));
            PdfPCell cell81 = new PdfPCell(new Paragraph( PrintVoucher.vouchPrinted.getVoucherDiscount()+"", arabicFontHeaderprint));
            PdfPCell cell82 = new PdfPCell(new Paragraph("المبلغ :"+" فقط " + getArabicString(PrintVoucher.vouchPrinted.getNetSales() +"")+" لا غير " , arabicFontHeaderprint));
          //  PdfPCell cell83 =  new PdfPCell(new Paragraph( " فقط " + getArabicString( 249.828 +" لا غير ")  , arabicFont));
            cell82 .setColspan(2);
            cell8.setBorder(Rectangle.NO_BORDER);
            cell81.setBorder(Rectangle.NO_BORDER);
            cell82.setBorder(Rectangle.NO_BORDER);
      //      cell83.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell8);
            table21.addCell(cell81);
            table21.addCell(cell82);
           // table21.addCell(cell83);


            doc.add(table21);

            PdfPTable table22 = new PdfPTable(4);
            table22.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table22.setWidthPercentage(100f);
           PdfPCell cell9 = new PdfPCell(new Paragraph(context.getString(R.string.totalafterdiscount) + " : " , arabicFontHeaderprint));
            PdfPCell cell91 = new PdfPCell(new Paragraph(""+(PrintVoucher.vouchPrinted.getSubTotal()-PrintVoucher.vouchPrinted.getVoucherDiscount()), arabicFontHeaderprint));
            PdfPCell cell92 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));

            PdfPCell cell93 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell9.setBorder(Rectangle.NO_BORDER);
            cell91.setBorder(Rectangle.NO_BORDER);
            cell92.setBorder(Rectangle.NO_BORDER);
            cell93.setBorder(Rectangle.NO_BORDER);

            table22.addCell(cell9);
            table22.addCell(cell91);
            table22.addCell(cell92);
            table22.addCell(cell93);
            doc.add(table22);

            PdfPTable table23 = new PdfPTable(4);
            table23.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table23.setWidthPercentage(100f);
            PdfPCell cell10 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            PdfPCell cell11 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            PdfPCell cell12 = new PdfPCell(new Paragraph("" , arabicFontHeaderprint));
            PdfPCell cell131 = new PdfPCell(new Paragraph(" توقيع المستلم", arabicFontHeaderprint));
            cell10.setBorder(Rectangle.NO_BORDER);
            cell11.setBorder(Rectangle.NO_BORDER);
            cell12.setBorder(Rectangle.NO_BORDER);
            cell131.setBorder(Rectangle.NO_BORDER);
            table23.addCell(cell10);
            table23.addCell(cell11);
            table23.addCell(cell12);
            table23.addCell(cell131);
            doc.add(table23);

            PdfPTable table24 = new PdfPTable(4);
            table24.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table24.setWidthPercentage(100f);
            PdfPCell cell15 =new  PdfPCell(new Paragraph("", arabicFontHeaderprint));
            PdfPCell cell16 =new  PdfPCell(new Paragraph("", arabicFontHeaderprint));
            String salesName=obj.getSalesmanName_fromSalesTeam();
            PdfPCell cell17 =new  PdfPCell(new Paragraph( salesName+""  , arabicFontHeaderprint));
            PdfPCell cell18 =new  PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell15.setBorder(Rectangle.NO_BORDER);
            cell16.setBorder(Rectangle.NO_BORDER);
            cell17.setBorder(Rectangle.BOTTOM);
            cell18.setBorder(Rectangle.BOTTOM);

            table24.addCell(cell15);
            table24.addCell(cell16);
            table24.addCell(cell17);
            table24.addCell(cell18);
            doc.add(table24);


        Log.e("path44", "" + targetPdf);
        pdfFileName = path;
        Log.e("pdfFileName", "" + pdfFileName);


        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("DocumentException11==", "" + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("DocumentException==", "" +  e.getMessage());
        }


    }

    void createvocherPDFForSharePlan(String fileName, List<SalesManPlan> list) {
        Log.e("createvocherPDFForShare", "" + "createvocherPDFForShare");
        doc = new Document();

        docWriter = null;

        try {

//            PdfPCell emptycell = new PdfPCell(new Paragraph("        ", arabicFontHeaderprint));
//            emptycell.setBorder(Rectangle.NO_BORDER);
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ReportVanSales/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;

            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            docWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            doc.setPageSize(PageSize.A4);//size of page


            //open document
            doc.open();
String a="",s="";

            if(list.get(0).getTypeOrder()==0)
            {
              a=  context.getResources().getString(R.string.TYPEPOFPLAN1);
            }
            else {
              a=context.getResources().getString(R.string.TYPEPOFPLAN2);
            }



            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100f);
            headertable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell13 = new PdfPCell(new Paragraph("" + companyInfo.getCompanyName() + "\n", arabicFontHeaderVochprint));
            cell13.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            PdfPCell cell2 = new PdfPCell(new Paragraph( companyInfo.getcompanyTel(), arabicFontHeaderprint));

            PdfPCell cell14 = new PdfPCell(new Paragraph("Sales Man No /رقم المندوب :"+list.get(0).getSaleManNumber() + "\n", arabicFontHeader));
            cell14.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            PdfPCell cell15 = new PdfPCell(new Paragraph("plan Type /نوع الخطة :"+a + "\n", arabicFontHeader));
            cell15.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            PdfPCell cell16 = new PdfPCell(new Paragraph("plan Status /الحالة :"+ state+ "\n", arabicFontHeader));
            cell16.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

            PdfPCell cell17 = new PdfPCell(new Paragraph("Note Type /نوع الملاحظة  :"+ noteType+ "\n", arabicFontHeader));
            cell17.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

//            doc.add(new Paragraph("\n"));

            cell13.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell14.setBorder(Rectangle.NO_BORDER);
            cell15.setBorder(Rectangle.NO_BORDER);
            cell16.setBorder(Rectangle.NO_BORDER);
            cell17.setBorder(Rectangle.NO_BORDER);

            if(MainActivity.CompanyinfoINPdf_Hide==0) {
                headertable.addCell(cell13);
                headertable.addCell(cell2);
                headertable.addCell(cell14);
                headertable.addCell(cell15);
                headertable.addCell(cell16);
                headertable.addCell(cell17);
            }
            doc.add(headertable);
            doc.add(new Paragraph("\n"));

//            doc.add(new Paragraph("\n"));
//                 doc.add(new Paragraph("\n"));

            String date = getCurentTimeDate(1);

            PdfPTable table = new PdfPTable(1);

            PdfPTable pdfPTable = new PdfPTable(6);
            pdfPTable.setWidthPercentage(100f);

            PdfPTable pdfPTable3 = new PdfPTable(6);
            pdfPTable3.setWidthPercentage(100f);

            if (!Locale.getDefault().getLanguage().equals("ar")) {


                table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                //   headertable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

            } else {

                table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


            }

//            table.addCell(emptycell);


            PdfPTable table222 = new PdfPTable(5);
            table222.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            PdfPCell cell_101 = new PdfPCell(new Paragraph("الخطة", arabicFontHeaderprint));
            cell_101.setHorizontalAlignment((cell_101.ALIGN_CENTER));
//            PdfPCell cell_102 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
//            cell_102.setBorder(Rectangle.NO_BORDER);

//            PdfPCell cell_103 = new PdfPCell(new Paragraph("VanSale Plan", arabicFontHeaderprint));
//            cell_103.setBorder(Rectangle.NO_BORDER);

//            table222.addCell(cell_102);
            table222.addCell(cell_101);
//            table222.addCell(cell_103);

            doc.add(table222);


            PdfPTable table02 = new PdfPTable(3);
            table02.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

//            PdfPCell cell201 = new PdfPCell(new Paragraph(paymentTyp, arabicFontHeaderprint));
//            cell201.setBorder(Rectangle.BOTTOM);
//            cell201.setHorizontalAlignment((cell201.ALIGN_CENTER));
//            PdfPCell cell202 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
//            cell202.setBorder(Rectangle.NO_BORDER);

//            PdfPCell cell203 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
//            cell203.setBorder(Rectangle.NO_BORDER);
//
//            table02.addCell(cell202);
//            table02.addCell(cell201);
//
//            table02.addCell(cell203);
//            doc.add(table02);

            table.setWidthPercentage(100f);



            /***********/

            insertCell(pdfPTable, "order \n الترتيب ", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
//            PdfPCell cell41 = new PdfPCell(new Paragraph("Item Code \nرمز المادة" +, arabicFontHeaderprint));
//
//            pdfPTable.addCell(cell41);


            insertCell(pdfPTable, "Customer Name \n اسم العميل" ,ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
            if(noteType.contains("Start")) {
                insertCell(pdfPTable, "Start Note \n الملاحظة الاولية ", ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
            }else {
            insertCell(pdfPTable, "End Note \n الملاحظة النهائية ", ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
            }
            insertCell(pdfPTable, "status \n الحالة ", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

            doc.add(pdfPTable);


            /**************/

            for (int i = 0; i < list.size(); i++) {

                    insertCell(pdfPTable3, String.valueOf(list.get(i).getOrder()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                    insertCell(pdfPTable3, String.valueOf(list.get(i).getCustName()), ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
                    if(noteType.contains("Start")){
                        insertCell(pdfPTable3, String.valueOf(list.get(i).getStartNote()), ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);

                    }else {
                        insertCell(pdfPTable3, String.valueOf(list.get(i).getEndNote()), ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);

                    }
//                insertCell(pdfPTable3, String.valueOf(list.get(i).getEndNote()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                    if(list.get(i).getLogoutStatus()==1){
                        insertCell(pdfPTable3, "Visit", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                    }else{
                        insertCell(pdfPTable3, "not Visit", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                    }



            }


            doc.add(pdfPTable3);



            Log.e("path44", "" + targetPdf);
            pdfFileName = path;
            Log.e("pdfFileName", "" + pdfFileName);


        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("DocumentException11==", "" + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("DocumentException==", "" +  e.getMessage());
        }


    }


    @SuppressLint("LongLogTag")
    void createvocherPDFForShare2(String fileName, List<Item> list) {
        Log.e("createvocherPDFForShare2","createvocherPDFForShare2");
        doc = new Document();

        docWriter = null;

        try {

            PdfPCell emptycell = new PdfPCell(new Paragraph("        ", arabicFontHeaderprint));
            emptycell.setBorder(Rectangle.NO_BORDER);
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ReportVanSales/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;

            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            docWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            doc.setPageSize(PageSize.A4);//size of page


            //open document
            doc.open();

            String voucherTyp = "";

            //
            String paymentTyp = "";


            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100f);
            headertable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell13 = new PdfPCell(new Paragraph( "التاريخ " + " : " + PrintVoucher.vouchPrinted.getVoucherDate() + " ", arabicFontHeaderprint));

            cell13.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            PdfPCell cell2 = new PdfPCell(new Paragraph("رقم الفاتورة: " + " : " + PrintVoucher.vouchPrinted.getVoucherNumber(), arabicFontHeaderprint));

            doc.add(new Paragraph("\n"));
            headertable.addCell(cell13);
            headertable.addCell(cell2);
            doc.add(headertable);

//            if (companyInfo.getLogo() != null && !companyInfo.getLogo().equals("")) {
//                Bitmap imageBytes = companyInfo.getLogo();
//                imageBytes = getResizedBitmap(imageBytes, 80, 80);
//                byte[] bytes = convertBitmapToByteArray(imageBytes);
//
//
//                BitmapFactory.decodeByteArray(bytes, 0, 10);
//                try {
//
//                    Image imageView = Image.getInstance(bytes);
//                    imageView.setAlignment(ALIGN_CENTER);
//                    //   imageView.scaleToFit(10,10);
//
//                    doc.add(imageView);
//
//                } catch (Exception e) {
//                }
//
//
//                doc.add(headertable);
//
//            }

            cell13.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);




//            doc.add(new Paragraph("\n"));
            //     doc.add(new Paragraph("\n"));

            String date = getCurentTimeDate(1);

            PdfPTable table = new PdfPTable(1);

            PdfPTable pdfPTable = new PdfPTable(6);
            pdfPTable.setWidthPercentage(100f);

            PdfPTable pdfPTable3 = new PdfPTable(6);
            pdfPTable3.setWidthPercentage(100f);

            if (!Locale.getDefault().getLanguage().equals("ar")) {


                table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                //   headertable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

            } else {

                table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


            }

            table.addCell(emptycell);


            PdfPTable table222 = new PdfPTable(3);
            table222.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            PdfPCell cell_101 = new PdfPCell(new Paragraph("عرض سعر نقداً", arabicFontHeaderprint));
            cell_101.setHorizontalAlignment((cell_101.ALIGN_CENTER));
            PdfPCell cell_102 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell_102.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell_103 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell_103.setBorder(Rectangle.NO_BORDER);

            table222.addCell(cell_102);
            table222.addCell(cell_101);
            table222.addCell(cell_103);

            doc.add(table222);


            PdfPTable table02 = new PdfPTable(3);
            table02.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            PdfPCell cell201 = new PdfPCell(new Paragraph(paymentTyp, arabicFontHeaderprint));
            cell201.setBorder(Rectangle.BOTTOM);
            cell201.setHorizontalAlignment((cell201.ALIGN_CENTER));
            PdfPCell cell202 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell202.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell203 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell203.setBorder(Rectangle.NO_BORDER);

            table02.addCell(cell202);
            table02.addCell(cell201);

            table02.addCell(cell203);
            doc.add(table02);

            table.setWidthPercentage(100f);

            PdfPCell cell4 = new PdfPCell(new Paragraph("اسم العميل" + " : " + PrintVoucher.vouchPrinted.getCustName() + "            " + "" + " : " + PrintVoucher.vouchPrinted.getSaleManNumber() + "        رقم المستودع : ", arabicFontHeaderprint));

            cell4.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Paragraph(context.getString(R.string.cust_name) + " : " + PrintVoucher.vouchPrinted.getCustName(), arabicFontHeaderprint));
            cell5.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell5);
            table.addCell(emptycell);
            doc.add(table);


            /***********/

            insertCell(pdfPTable, "Item Code \nرمز المادة", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
//            PdfPCell cell41 = new PdfPCell(new Paragraph("Item Code \nرمز المادة" +, arabicFontHeaderprint));
//
//            pdfPTable.addCell(cell41);


            insertCell(pdfPTable, "Description \nاسم المادة", ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
            insertCell(pdfPTable, "Qty \nالكمية", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
            insertCell(pdfPTable, "Price \nالسعر", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
            insertCell(pdfPTable, "Total \nالمجموع", ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

            doc.add(pdfPTable);


            /**************/

            // pdfPTable3.setHeaderRows(1);
//        Log.e("list.size()==", list.size() + "");
            //  list.add(list.get(list.size()-1));
            int count=0;
            for (int i = 0; i < list.size(); i++) {


                if (PrintVoucher.vouchPrinted.getVoucherNumber() == list.get(i).getVoucherNumber()
                        && PrintVoucher.vouchPrinted.getVoucherType() == list.get(i).getVoucherType()) {
//                Log.e("getVoucherNumber5==", list.get(i).getVoucherNumber() + "");
//                Log.e("itenu==", list.get(i).getItemNo() + "");
//                Log.e("itemname==", list.get(i).getItemName() + "");
//                Log.e("qty==", list.get(i).getQty() + "");
//                Log.e("getprice==", list.get(i).getPrice() + "");

                    insertCell(pdfPTable3, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                    insertCell(pdfPTable3, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 2, arabicFontHeaderprint, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getPrice()), ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);

                    String amount = "" + (list.get(i).getQty() * list.get(i).getPrice() - list.get(i).getDisc());
                    Log.e("amount==", amount + "");
                    insertCell(pdfPTable3, amount, ALIGN_CENTER, 1, arabicFontHeaderprint, BaseColor.BLACK);
                    count=i;
                }
            }


            //   doc.add(new Paragraph(list.get(0).getItemName(),arabicFont));
            doc.add(pdfPTable3);

            for(int x=0;x<15-count;x++)
                doc.add(new Paragraph("\n"));

            PdfPTable table2 = new PdfPTable(4);
            table2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table2.setWidthPercentage(100f);
            PdfPCell cell7 = new PdfPCell(new Paragraph(context.getString(R.string.sub_total) + " : " , arabicFontHeaderprint));
            PdfPCell cell71 = new PdfPCell(new Paragraph( PrintVoucher.vouchPrinted.getSubTotal()+"", arabicFontHeaderprint));

            PdfPCell cell72 = new PdfPCell(new Paragraph( "الملاحظات"+ " : " , arabicFontHeaderprint));

            PdfPCell cell73 = new PdfPCell(new Paragraph( "_______"  , arabicFontHeaderprint));
            cell7.setBorder(Rectangle.NO_BORDER);
            cell71.setBorder(Rectangle.NO_BORDER);
            cell72.setBorder(Rectangle.NO_BORDER);
            cell73.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell7);
            table2.addCell(cell71);
            table2.addCell(cell72);
            table2.addCell(cell73);
            doc.add(table2);

            PdfPTable table21 = new PdfPTable(4);
            table21.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table21.setWidthPercentage(100f);
            PdfPCell cell8 = new PdfPCell(new Paragraph(context.getString(R.string.discount_value) , arabicFontHeaderprint));
            PdfPCell cell81 = new PdfPCell(new Paragraph( PrintVoucher.vouchPrinted.getVoucherDiscount()+"", arabicFontHeaderprint));
            PdfPCell cell82 = new PdfPCell(new Paragraph("المبلغ :"+" فقط " + getArabicString(PrintVoucher.vouchPrinted.getNetSales() +"")+" لا غير " , arabicFontHeaderprint));
            //  PdfPCell cell83 =  new PdfPCell(new Paragraph( " فقط " + getArabicString( 249.828 +" لا غير ")  , arabicFont));
            cell82 .setColspan(2);
            cell8.setBorder(Rectangle.NO_BORDER);
            cell81.setBorder(Rectangle.NO_BORDER);
            cell82.setBorder(Rectangle.NO_BORDER);
            //      cell83.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell8);
            table21.addCell(cell81);
            table21.addCell(cell82);
            // table21.addCell(cell83);


            doc.add(table21);

            PdfPTable table22 = new PdfPTable(4);
            table22.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table22.setWidthPercentage(100f);
            PdfPCell cell9 = new PdfPCell(new Paragraph(context.getString(R.string.totalafterdiscount) + " : " , arabicFontHeaderprint));
            PdfPCell cell91 = new PdfPCell(new Paragraph(""+(PrintVoucher.vouchPrinted.getSubTotal()-PrintVoucher.vouchPrinted.getVoucherDiscount()), arabicFontHeaderprint));
            PdfPCell cell92 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));

            PdfPCell cell93 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell9.setBorder(Rectangle.NO_BORDER);
            cell91.setBorder(Rectangle.NO_BORDER);
            cell92.setBorder(Rectangle.NO_BORDER);
            cell93.setBorder(Rectangle.NO_BORDER);

            table22.addCell(cell9);
            table22.addCell(cell91);
            table22.addCell(cell92);
            table22.addCell(cell93);
            doc.add(table22);

            PdfPTable table23 = new PdfPTable(4);
            table23.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table23.setWidthPercentage(100f);
            PdfPCell cell10 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            PdfPCell cell11 = new PdfPCell(new Paragraph("", arabicFontHeaderprint));
            PdfPCell cell12 = new PdfPCell(new Paragraph("" , arabicFontHeaderprint));
            PdfPCell cell131 = new PdfPCell(new Paragraph(" توقيع المستلم", arabicFontHeaderprint));
            cell10.setBorder(Rectangle.NO_BORDER);
            cell11.setBorder(Rectangle.NO_BORDER);
            cell12.setBorder(Rectangle.NO_BORDER);
            cell131.setBorder(Rectangle.NO_BORDER);
            table23.addCell(cell10);
            table23.addCell(cell11);
            table23.addCell(cell12);
            table23.addCell(cell131);
            doc.add(table23);

            PdfPTable table24 = new PdfPTable(4);
            table24.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table24.setWidthPercentage(100f);
            PdfPCell cell15 =new  PdfPCell(new Paragraph("", arabicFontHeaderprint));
            PdfPCell cell16 =new  PdfPCell(new Paragraph("", arabicFontHeaderprint));
            String salesName=obj.getSalesmanName_fromSalesTeam();
            PdfPCell cell17 =new  PdfPCell(new Paragraph( salesName+""  , arabicFontHeaderprint));
            PdfPCell cell18 =new  PdfPCell(new Paragraph("", arabicFontHeaderprint));
            cell15.setBorder(Rectangle.NO_BORDER);
            cell16.setBorder(Rectangle.NO_BORDER);
            cell17.setBorder(Rectangle.BOTTOM);
            cell18.setBorder(Rectangle.BOTTOM);

            table24.addCell(cell15);
            table24.addCell(cell16);
            table24.addCell(cell17);
            table24.addCell(cell18);
            doc.add(table24);


            Log.e("path44", "" + targetPdf);
            pdfFileName = path;
            Log.e("pdfFileName", "" + pdfFileName);


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }

    void createSalevocherPDF(String fileName, List<Item> list) {
        doc = new Document();

        docWriter = null;

        try {


            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ReportVanSales/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;

            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            docWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            doc.setPageSize(PageSize.A4);//size of page
            //open document
            doc.open();


            String voucherTyp = "";
            switch (SalesInvoice.SaleInvoicePrinted.getVoucherType()) {
                case 504:
                    voucherTyp = "فاتورة بيع";
                    break;
                case 506:
                    voucherTyp = "فاتورة مرتجعات";
                    break;
                case 508:
                    voucherTyp = "طلب جديد";
                    break;
            }
            //


            PdfPTable headertable = new PdfPTable(1);
            headertable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell13 = new PdfPCell(new Paragraph("                    " + companyInfo.getCompanyName() + "\n", arabicFontHeaderVochprint));
            cell13.setBorder(Rectangle.NO_BORDER);
            headertable.addCell(cell13);

            if (companyInfo.getLogo() != null && !companyInfo.getLogo().equals("")) {
                Bitmap imageBytes = companyInfo.getLogo();
                imageBytes = getResizedBitmap(imageBytes, 80, 80);
                byte[] bytes = convertBitmapToByteArray(imageBytes);


                BitmapFactory.decodeByteArray(bytes, 0, 10);
                try {

                    Image imageView = Image.getInstance(bytes);
                    imageView.setAlignment(ALIGN_CENTER);
                    //   imageView.scaleToFit(10,10);

                    doc.add(imageView);

                } catch (Exception e) {
                }


                doc.add(headertable);

            }
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));

            String date = getCurentTimeDate(1);
            PdfPTable table = new PdfPTable(1);
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell1 = new PdfPCell(new Paragraph(context.getString(R.string.date) + " : " + list.get(0).getDate(), arabicFontHeaderprint));
            PdfPCell cell2 = new PdfPCell(new Paragraph(context.getString(R.string.company_tel) + " : " + companyInfo.getcompanyTel(), arabicFontHeaderprint));

            PdfPCell cell3 = new PdfPCell(new Paragraph(context.getString(R.string.tax_no) +  companyInfo.getTaxNo(), arabicFontHeaderprint));
            PdfPCell cell4 = new PdfPCell(new Paragraph(context.getString(R.string.voucherNo) + " : " + SalesInvoice.SaleInvoicePrinted.getVoucherNumber(), arabicFontHeaderprint));
            PdfPCell cell5 = new PdfPCell(new Paragraph(context.getString(R.string.cust_name) + " : " + SalesInvoice.SaleInvoicePrinted.getCustName(), arabicFontHeaderprint));
            PdfPCell cell6 = new PdfPCell(new Paragraph(context.getString(R.string.note) + " : " + companyInfo.getNoteForPrint(), arabicFontHeaderprint));
            PdfPCell cell14 = new PdfPCell(new Paragraph(context.getString(R.string.app_paymentType) + " : " + SalesInvoice.SaleInvoicePrinted.getPayMethod(), arabicFontHeaderprint));


            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell4.setBorder(Rectangle.NO_BORDER);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell6.setBorder(Rectangle.NO_BORDER);
            cell14.setBorder(Rectangle.NO_BORDER);



            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell14);

            doc.add(table);


            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("\n"));

            PdfPTable pdfPTable = new PdfPTable(4);
            pdfPTable.setWidthPercentage(100f);

            PdfPTable pdfPTable3 = new PdfPTable(4);
            pdfPTable3.setWidthPercentage(100f);

            if (!Locale.getDefault().getLanguage().equals("ar")) {

                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);


            } else {

                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


//                insertCell(pdfPTable, context.getResources().getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//                insertCell(pdfPTable, context.getResources().getString(R.string.app_price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//                insertCell(pdfPTable, context.getResources().getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//                insertCell(pdfPTable, context.getResources().getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//                doc.add(pdfPTable);
//
//
//                PdfPTable pdfPTable3 = new PdfPTable(4);
//                pdfPTable3.setWidthPercentage(100f);
//                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//
//                // pdfPTable3.setHeaderRows(1);
//                Log.e("list.size()==", list.size() + "");
//                //  list.add(list.get(list.size()-1));
//                for (int i = 0; i < list.size(); i++) {
//
//
//                    if (SalesInvoice.SaleInvoicePrinted.getVoucherNumber() == list.get(i).getVoucherNumber()
//                            && SalesInvoice.SaleInvoicePrinted.getVoucherType() == list.get(i).getVoucherType()) {
//                        Log.e("getVoucherNumber5==", list.get(i).getVoucherNumber() + "");
//                        Log.e("itenu==", list.get(i).getItemNo() + "");
//                        Log.e("itemname==", list.get(i).getItemName() + "");
//                        Log.e("qty==", list.get(i).getQty() + "");
//                        Log.e("getprice==", list.get(i).getPrice() + "");
//
//
//                        String amount = "" + (list.get(i).getQty() * list.get(i).getPrice() - list.get(i).getDisc());
//                        Log.e("amount==", amount + "");
//                        insertCell(pdfPTable3, amount, ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//                        insertCell(pdfPTable3, String.valueOf(list.get(i).getPrice()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//                        insertCell(pdfPTable3, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//                        insertCell(pdfPTable3, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
//
//                    }
//                }
//
//                //   doc.add(new Paragraph(list.get(0).getItemName(),arabicFont));
//                doc.add(pdfPTable3);

            }

            insertCell(pdfPTable, context.getResources().getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, context.getResources().getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, context.getResources().getString(R.string.app_price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, context.getResources().getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

            doc.add(pdfPTable);


            /*********/

            // pdfPTable3.setHeaderRows(1);
            Log.e("list.size()==", list.size() + "");
            //  list.add(list.get(list.size()-1));
            for (int i = 0; i < list.size(); i++) {


                if (SalesInvoice.SaleInvoicePrinted.getVoucherNumber() == list.get(i).getVoucherNumber()
                        && SalesInvoice.SaleInvoicePrinted.getVoucherType() == list.get(i).getVoucherType()) {
                    Log.e("getVoucherNumber5==", list.get(i).getVoucherNumber() + "");
                    Log.e("itenu==", list.get(i).getItemNo() + "");
                    Log.e("itemname==", list.get(i).getItemName() + "");
                    Log.e("qty==", list.get(i).getQty() + "");
                    Log.e("getprice==", list.get(i).getPrice() + "");

                    insertCell(pdfPTable3, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getPrice()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

                    String amount = "" + (list.get(i).getQty() * list.get(i).getPrice() - list.get(i).getDisc());
                    Log.e("amount==", amount + "");
                    insertCell(pdfPTable3, amount, ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

                }
            }

            //   doc.add(new Paragraph(list.get(0).getItemName(),arabicFont));
            doc.add(pdfPTable3);

            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));


            PdfPTable table2 = new PdfPTable(1);
            table2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell7 = new PdfPCell(new Paragraph(context.getString(R.string.sub_total) + " : " + SalesInvoice.SaleInvoicePrinted.getSubTotal(), arabicFontHeaderprint));
            PdfPCell cell8 = new PdfPCell(new Paragraph(context.getString(R.string.discount_value) + " : " + SalesInvoice.SaleInvoicePrinted.getVoucherDiscount(), arabicFontHeaderprint));

            PdfPCell cell9 = new PdfPCell(new Paragraph(context.getString(R.string.net_sales) + " : " + SalesInvoice.SaleInvoicePrinted.getNetSales(), arabicFontHeaderprint));
            PdfPCell cell10 = new PdfPCell(new Paragraph("استلمت البضاعة كاملة و بحالة جيدة وخالية من اية عيوب واتعهد بدفع قيمة هذه الفاتورة .", arabicFontHeaderprint));
            PdfPCell cell11 = new PdfPCell(new Paragraph("المستلم : ", arabicFontHeaderprint));
            PdfPCell cell12 = new PdfPCell(new Paragraph("التوقيع :", arabicFontHeaderprint));


            cell7.setBorder(Rectangle.NO_BORDER);
            cell8.setBorder(Rectangle.NO_BORDER);
            cell9.setBorder(Rectangle.NO_BORDER);
            cell10.setBorder(Rectangle.NO_BORDER);
            cell11.setBorder(Rectangle.NO_BORDER);
            cell12.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell7);
            table2.addCell(cell8);
            table2.addCell(cell9);
            table2.addCell(cell10);
            table2.addCell(cell11);
            table2.addCell(cell12);
            doc.add(table2);


            Log.e("path44", "" + targetPdf);
            pdfFileName = path;
            Log.e("pdfFileName", "" + pdfFileName);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    void createSalevocherPDF2(String fileName, List<Item> list) {
        doc = new Document();

        docWriter = null;

        try {


            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Voucher/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;

            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            docWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            doc.setPageSize(PageSize.A4);//size of page
            //open document
            doc.open();


            String voucherTyp = "";
            switch (SalesInvoice.SaleInvoicePrinted.getVoucherType()) {
                case 504:
                    voucherTyp = "فاتورة بيع";
                    break;
                case 506:
                    voucherTyp = "فاتورة مرتجعات";
                    break;
                case 508:
                    voucherTyp = "طلب جديد";
                    break;
            }
            //


            PdfPTable headertable = new PdfPTable(1);
            headertable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell13 = new PdfPCell(new Paragraph("                    " + companyInfo.getCompanyName() + "\n", arabicFontHeaderVochprint));
            cell13.setBorder(Rectangle.NO_BORDER);





            String date = getCurentTimeDate(1);
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(110f);
            PdfPTable table21 = new PdfPTable(1);
            table21.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table21.setWidthPercentage(110f);
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            String dateTime = "";
            String dateTime2 = "";
            dateTime = getCurentTimeDate(1);
            dateTime2= getCurentTimeDate(2);
            PdfPCell cell1 = new PdfPCell(new Paragraph(context.getString(R.string.date) + " : " + dateTime  +"                                                      "  +"  التوقيت: "+dateTime2, arabicFontHeaderprint));
            PdfPCell cell2 = new PdfPCell(new Paragraph(context.getString(R.string.company_tel) + " : " + companyInfo.getcompanyTel(), arabicFontHeaderprint));

            PdfPCell cell3 = new PdfPCell(new Paragraph(context.getString(R.string.tax_no) +  companyInfo.getTaxNo(), arabicFontHeaderprint));
            PdfPCell cell4 = new PdfPCell(new Paragraph(context.getString(R.string.voucherNo) + " : " + SalesInvoice.SaleInvoicePrinted.getVoucherNumber(), arabicFontHeaderprint));
            PdfPCell cell5 = new PdfPCell(new Paragraph(context.getString(R.string.cust_name) + " : " + SalesInvoice.SaleInvoicePrinted.getCustName()

                +"                            "  +"   رقم المستودع: " +SalesInvoice.SaleInvoicePrinted.getSaleManNumber(), arabicFontHeaderprint));
            PdfPCell cell6 = new PdfPCell(new Paragraph(context.getString(R.string.note) + " : " + companyInfo.getNoteForPrint(), arabicFontHeaderprint));
            PdfPCell cell14 = new PdfPCell(new Paragraph(context.getString(R.string.app_paymentType) + " : " + SalesInvoice.SaleInvoicePrinted.getPayMethod(), arabicFontHeaderprint));
            PdfPCell cell_101 = new PdfPCell(new Paragraph( "عرض سعر نقداً", arabicFontHeaderprint));
            cell_101.setHorizontalAlignment(ALIGN_CENTER);
            PdfPCell cell_102 = new PdfPCell(new Paragraph("رقم هاتف العميل:"                  , arabicFontHeaderprint));

            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell4.setBorder(Rectangle.NO_BORDER);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell6.setBorder(Rectangle.NO_BORDER);
            cell14.setBorder(Rectangle.NO_BORDER);
            cell_101.setBorder(Rectangle.NO_BORDER);
            cell_102.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell1);

            table.addCell(cell4);
            doc.add(table);
            if (companyInfo.getLogo() != null && !companyInfo.getLogo().equals("")) {
                Bitmap imageBytes = companyInfo.getLogo();
                imageBytes = getResizedBitmap(imageBytes, 80, 80);
                byte[] bytes = convertBitmapToByteArray(imageBytes);


                BitmapFactory.decodeByteArray(bytes, 0, 10);
                try {

                    Image imageView = Image.getInstance(bytes);
                    imageView.setAlignment(ALIGN_CENTER);


                    doc.add(imageView);

                } catch (Exception e) {
                }




            }


            table21.addCell(cell_101);
            table21.addCell(cell_102);
            table21.addCell(cell5);


            doc.add(table21);


            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("\n"));

            PdfPTable pdfPTable = new PdfPTable(11);
            pdfPTable.setWidthPercentage(100f);

            PdfPTable pdfPTable3 = new PdfPTable(11);
            pdfPTable3.setWidthPercentage(100f);
            PdfPTable pdfPTable4 = new PdfPTable(4);
            pdfPTable4.setWidthPercentage(5f);
            if (!Locale.getDefault().getLanguage().equals("ar")) {

                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                pdfPTable4.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

            } else {

                pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPTable4.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);



            }
            insertCellWithColor(pdfPTable, "#", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.item_name), ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.app_qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.unit_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.packing), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.app_price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.app_tax_perc), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCellWithColor(pdfPTable, context.getResources().getString(R.string.note), ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);

            doc.add(pdfPTable);


            /*********/
           int count=0;

            Log.e("list.size()==", list.size() + "");

            for (int i = 0; i < list.size(); i++) {


                if (SalesInvoice.SaleInvoicePrinted.getVoucherNumber() == list.get(i).getVoucherNumber()
                        && SalesInvoice.SaleInvoicePrinted.getVoucherType() == list.get(i).getVoucherType()) {


                    insertCell(pdfPTable3, i+"", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

                    String amount = "" + (list.get(i).getQty() * list.get(i).getPrice() - list.get(i).getDisc());
                    insertCell(pdfPTable3, amount, ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getItemName()), ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.  valueOf((int)Double.parseDouble(list.get(i).getEnter_qty())), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                   if(list.get(i).getWhich_unit_str().equals(""))
                   {insertCell(pdfPTable3, "حبة", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                       insertCell(pdfPTable3, "1", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

                   }

                   else{
                       insertCell(pdfPTable3, String.valueOf(list.get(i).getWhich_unit_str()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                       insertCell(pdfPTable3, String.  valueOf((int)Double.parseDouble(list.get(i).getWhichu_qty())), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

                   }



                    insertCell(pdfPTable3, String.valueOf(list.get(i).getPrice()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, String.valueOf(list.get(i).getTaxPercent())+"%", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable3, "", ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
                    count=i;


                }
            }


            doc.add(pdfPTable3);








            for(int x=0;x<25-count;x++)
                doc.add(new Paragraph("\n"));




            PdfPTable table2 = new PdfPTable(7);
            table2.setWidthPercentage(110f);
            table2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            PdfPCell cell73 = new PdfPCell(new Paragraph(" ", arabicFontHeaderprint));

            PdfPCell cell8 = new PdfPCell(new Paragraph("  " , arabicFontHeaderprint));
            Paragraph paragraph=    new Paragraph(context.getString(R.string.total)+":", arabicFontHeaderprint);
            paragraph.setAlignment(ALIGN_CENTER);
            PdfPCell cell9 = new PdfPCell(paragraph );
            cell9.setHorizontalAlignment(ALIGN_CENTER);
            cell9.        setBackgroundColor(BaseColor.GRAY);
            processor = new NumberToWordExample3.DefaultProcessor();
            PdfPCell cell10 =  new PdfPCell(new Paragraph( SalesInvoice.SaleInvoicePrinted.getNetSales()+"" , arabicFontHeaderprint));
            cell10.   setBackgroundColor(BaseColor.WHITE);
          //  PdfPCell cell11 =  new PdfPCell(new Paragraph(getArabicString( ""+SalesInvoice.SaleInvoicePrinted.getNetSales()), arabicFontHeaderprint));
            PdfPCell cell11 =  new PdfPCell(new Paragraph("  "+processor.getName( ""+SalesInvoice.SaleInvoicePrinted.getNetSales())+" ONLY", arabicFontHeaderprint));
           Log.e("getArabicString==",getArabicString( ""+SalesInvoice.SaleInvoicePrinted.getNetSales()));

            Log.e("processor.getName==",processor.getName( ""+SalesInvoice.SaleInvoicePrinted.getNetSales()));

            PdfPCell cell12 =  new PdfPCell(new Paragraph(" اسم المستلم " +"   ", arabicFontHeaderprint));
            PdfPCell cell133 =  new PdfPCell(new Paragraph(" توقيع المستلم " +"   ", arabicFontHeaderprint));
            cell11.setColspan(3);
            cell73.setBorder(Rectangle.NO_BORDER);
       cell8.setBorder(Rectangle.NO_BORDER);
            cell9.setBorder(Rectangle.BOX);
           cell10.setBorder(Rectangle.BOX);
          cell11.setBorder(Rectangle.NO_BORDER);
        cell12.setBorder(Rectangle.NO_BORDER);
            cell133.setBorder(Rectangle.NO_BORDER);

            table2.addCell(cell9);

         table2.addCell(cell10);
        table2.addCell(cell11);
         table2.addCell(cell12);
            table2.addCell(cell133);

            doc.add(table2);
            Log.e("path44", "" + targetPdf);
            pdfFileName = path;
            Log.e("pdfFileName", "" + pdfFileName);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void insertCell(PdfPTable table, String text, int align, int colspan, Font
            font, BaseColor border) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setBorderColor(border);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }

        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL); //for make arabic string from right to left ...

//        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        //add the call to the table
        table.addCell(cell);

    }
    public void insertCellWithColor(PdfPTable table, String text, int align, int colspan, Font
            font, BaseColor border) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setBorderColor(border);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }

        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL); //for make arabic string from right to left ...

//        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        //add the call to the table
        table.addCell(cell);

    }
    void endDocPdf() {

        if (doc != null) {
            //close the document
            doc.close();
        }
        if (docWriter != null) {
            //close the writer
            docWriter.close();
        }
    }

    void showPdf(File path) {
        Log.e("showPdf", "" + path);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/pdf");//intent.setDataAndType(Uri.fromFile(path), "application/pdf");
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Not able to open folder", Toast.LENGTH_SHORT).show();
        }


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            return stream.toByteArray();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(Helper.class.getSimpleName(), "ByteArrayOutputStream was not closed");
                }
            }
        }
    }


}
///**************************
