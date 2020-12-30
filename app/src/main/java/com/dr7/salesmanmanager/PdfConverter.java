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
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
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
import static com.itextpdf.text.Element.ALIGN_CENTER;

public class PdfConverter {
    private Context context;
    Document doc;
    File file;
    PdfWriter docWriter = null;
    //    PDFView pdfView;
    File pdfFileName;
    BaseFont base;
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


    public PdfConverter(Context context) {
        this.context = context;
    }

    public void exportListToPdf(List<?> list, String headerDate, String date,int report ) {

        PdfPTable pdfPTable= new PdfPTable(1);
        PdfPTable pdfPTableHeader= new PdfPTable(1);
        pdfPTable = getContentTable(  list,report);

        pdfPTableHeader = getHeaderTable(list,report,headerDate,date);

        try {
//

            doc.add(  pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_LONG).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                showPdf(pdfFileName);
                Log.v("", "Permission is granted");
            } else {

                Log.v("", "Permission is revoked");
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        } else { // permission is automatically granted on sdk<23 upon
            // installation
            showPdf(pdfFileName);
            Log.v("", "Permission is granted");
        }

    }

    private PdfPTable getContentTable(List<?> list,int reportType) {
        PdfPTable tableContent= new PdfPTable(1);
        switch (reportType)
        {
            case 1:
                 tableContent=creatTableCustomerLog((List<Transaction>) list);
                break;
            case 2:
                tableContent=creatTableInventory((List<inventoryReportItem>) list);
                break;
            case 3:
                tableContent=creatTableVoucher((List<Voucher>) list);
                break;
            case 4:
                tableContent=creatItemsReport((List<Item>) list);
                break;
            case 5:
                tableContent=creatPaymentReport((List<Payment>) list);
                break;
            case 6:
                tableContent=creatItems_StockReport((List<Item>) list);
                break;
            case 7:
                tableContent=creatTableVoucher_Stock((List<Voucher>) list);
                break;

            case 8:
                tableContent=creatCashReport((List<Voucher>) list);
                break;
        }
        return  tableContent;
    }

    private PdfPTable creatTableCustomerLog(List<Transaction> list) {
        createPDF("CustomerLog_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(7);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        insertCell(pdfPTable, context.getResources().getString(R.string.cust_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_IN_DATE), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_IN_TIME), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_OUT_DATE), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CHECK_OUT_TIME), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.CUS_CODE), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.SALES_MAN_ID), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getCusName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckInTime()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckInTime()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckOutDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCheckOutTime()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCusCode()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getSalesManId()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }
        return  pdfPTable;

    }
    private PdfPTable creatTableInventory(List<inventoryReportItem> list) {
        createPDF("Inventory_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(3);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        insertCell(pdfPTable, context.getResources().getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.unit_qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getItemNo()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getQty()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

//            insertCell(pdfPTable, String.valueOf(String.format("%.3f", (list.get(i).getSalesManId()))), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }
        return pdfPTable;

    }
    private PdfPTable    creatTableVoucher(List<Voucher> list)
    {
        createPDF("Voucher_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(8);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        insertCell(pdfPTable,context.getString(R.string.cust_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getString(R.string.customer_number), ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.voucher_date), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.pay_method), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.app_disc), ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.sub_total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.net_sales), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getCustName()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCustNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
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
    private PdfPTable    creatTableVoucher_Stock(List<Voucher> list)
    {
        createPDF("VoucherStock_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(3);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        insertCell(pdfPTable,context.getString(R.string.voucher_number), ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.voucher_date), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.remark), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {

            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherDate()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getRemark()), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }
        return pdfPTable;

    }
    private PdfPTable    creatCashReport(List<Voucher> list)
    {
        createPDF("CashReport" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(2);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        insertCell(pdfPTable,context.getString(R.string.sales), ALIGN_CENTER   , 2, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.cash_sale), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(T_cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.credit_sales), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(T_credit), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.total_sales), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTable, "      ", ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
        //***************************************************************************

         insertCell(pdfPTable,context.getString(R.string.payment), ALIGN_CENTER   , 2, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(cashPayment), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.app_cheque), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(creditPayment), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.netpayment), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(net), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "      ", ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.app_creditCard), ALIGN_CENTER, 2, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.credit_value), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTable, String.valueOf((credit -returnCridet)), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTable,context.getResources().getString(R.string.total_cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(total_cash), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);



        return pdfPTable;

    }

    private PdfPTable    creatPaymentReport(List<Payment> list)
    {
        createPDF("Payment_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(7);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        insertCell(pdfPTable,context.getString(R.string.voucher_number)                    , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getString(R.string.pay_date      )                        , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.cust_name   )   , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.app_amount  )  , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.remark      ) , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.sale_man_number) , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.pay_method) , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);







        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i).getVoucherNumber() ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getPayDate())       , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getCustName()       ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getAmount()         ) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i).getRemark()          )       , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i) .getSaleManNumber()   )       , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i)  .getPayMethod() )               , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        }
        return pdfPTable;

    }
    private PdfPTable    creatItemsReport(List<Item> list)
    {
        createPDF("Items_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        insertCell(pdfPTable,context.getString(R.string.item_number)                    , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getString(R.string.item_name)                        , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.total_sold_qty2)   , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        insertCell(pdfPTable,context.getResources().getString(R.string.total_bonus_qty2)  , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.total_sales_noTax) , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);

        Float calTotalSales[] = new Float[list.size()];

        for(int n=0;n<list.size();n++)
        {
            calTotalSales[n]= ((list.get(n).getQty() * list.get(n).getPrice()) - list.get(n).getDisc());
            Log.e("calTotalSales","\t"+ calTotalSales[n]);

        }


        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i). getItemNo() )  , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i). getItemName()  )  , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i). getQty()  ), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i). getBonus() ), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf( calTotalSales[i] ), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }
        return pdfPTable;

    }
    private PdfPTable    creatItems_StockReport(List<Item> list)
    {
        createPDF("Items_Stock_Report" + ".pdf");
        PdfPTable pdfPTable = new PdfPTable(3);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        insertCell(pdfPTable,context.getString(R.string.item_number)                    , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getString(R.string.item_name)                        , ALIGN_CENTER   , 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,context.getResources().getString(R.string.qty)   , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        Float calTotalSales[] = new Float[list.size()];

        for(int n=0;n<list.size();n++)
        {
            calTotalSales[n]= ((list.get(n).getQty() * list.get(n).getPrice()) - list.get(n).getDisc());
            Log.e("calTotalSales","\t"+ calTotalSales[n]);

        }


        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < list.size(); i++) {
            insertCell(pdfPTable, String.valueOf(list.get(i). getItemNo() )  , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i). getItemName()  )  , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(list.get(i). getQty()  ), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }
        return pdfPTable;

    }



    private PdfPTable getHeaderTable(List<?> list,int reportType ,String headerDate, String date) {
        PdfPTable pdfPTableHeader = new PdfPTable(7);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);
        pdfPTableHeader.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        insertCell(pdfPTableHeader, headerDate, ALIGN_CENTER, 7, arabicFontHeader, BaseColor.BLACK);
        insertCell(pdfPTableHeader, context.getString(R.string.date) + " : " + date, Element.ALIGN_LEFT, 7, arabicFontHeader, BaseColor.BLACK);
        return  pdfPTableHeader;
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
            Log.e("pdfFileName", "" + pdfFileName);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void insertCell(PdfPTable table, String text, int align, int colspan, Font font, BaseColor border) {

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
        Log.e("showPdf",""+path);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/pdf");//intent.setDataAndType(Uri.fromFile(path), "application/pdf");
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Not able to open flder", Toast.LENGTH_SHORT).show();
        }


    }
}
