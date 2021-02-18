package com.dr7.salesmanmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.itextpdf.text.BaseColor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
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
        this.context=context;
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/VanSalesExcelReport");

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
                workbook = voucherStockReport(workbook, (List<Voucher>) list );
                break;
            case 8:
                workbook = cashReport(workbook, (List<Voucher>) list );
                break;
            case 9:
                workbook = SerialListReport(workbook, (List<serialModel>) list );
                break;
            case 10:
                workbook = unCollectedReport(workbook , (List<Payment>) list );
                break;

        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/VanSalesExcelReport/";
        file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + fileName;
        File path = new File(targetPdf);

        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", path);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.ms-excel");//intent.setDataAndType(Uri.fromFile(path), "application/pdf");
        try{
            context.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "Excel program needed!", Toast.LENGTH_SHORT).show();
        }
    }
//********************************************************************
WritableWorkbook unCollectedReport(WritableWorkbook workbook, List<Payment> list) {

    try {
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

        try {
            sheet.addCell(new Label(0, 0, context.getString(R.string.app_bank_name)            )); // column and row
            sheet.addCell(new Label(1, 0, context.getString(R.string.check_number   )) );

            sheet.addCell(new Label(2, 0, context.getString(R.string.chaequeDate   )) );
            sheet.addCell(new Label(3, 0, context.getString(R.string.app_amount   )) );

            sheet.mergeCells(0,1, 3, 1);// col , row, to col , to row

            for (int i = 0; i < list.size(); i++) {
                sheet.addCell(new Label(0, i + 2, list.get(i).getBank()+""));
                sheet.addCell(new Label(1, i + 2,      list.get(i).getCheckNumber()+""));
                sheet.addCell(new Label(2, i + 2,  list.get(i).getDueDate()+""));
                sheet.addCell(new Label(3, i + 2,  list.get(i).getAmount()+""));


                //sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

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

//*********************************************************************

    WritableWorkbook customerLogReport(WritableWorkbook workbook, List<Transaction> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0,context.getString(R.string.SALES_MAN_ID) )); // column and row
                sheet.addCell(new Label(2, 0,  context.getString(R.string.CUS_CODE)));
                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.cust_name)));
                sheet.addCell(new Label(5, 0, context.getString(R.string.CHECK_IN_DATE)));
                sheet.addCell(new Label(6, 0, context.getString(R.string.CHECK_IN_TIME)));
                sheet.addCell(new Label(8, 0, context.getString(R.string.CHECK_OUT_DATE) ));
                sheet.addCell(new Label(9, 0, context.getString(R.string.CHECK_OUT_TIME)));
//                sheet.addCell(new Label(10, 0, "Bundles"));
//                sheet.addCell(new Label(11, 0, "Cubic"));
                sheet.mergeCells(0,0, 1, 0);// col , row, to col , to row
                sheet.mergeCells(3,0, 4, 0);// col , row, to col , to row
                sheet.mergeCells(6,0, 7, 0);// col , row, to col , to row
                sheet.mergeCells(11,0, 12, 0);// col , row, to col , to row

                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row
                sheet.mergeCells(3,1, 4, 1);// col , row, to col , to row
                sheet.mergeCells(6,1, 7, 1);// col , row, to col , to row
                sheet.mergeCells(11,1, 12, 1);// col , row, to col , to row

                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(0, i + 2, list.get(i).getSalesManId()+""));
                    sheet.addCell(new Label(2, i + 2, list.get(i).getCusCode()));
                    sheet.addCell(new Label(3, i + 2, list.get(i).getCusName()));
                    sheet.addCell(new Label(5, i + 2, list.get(i).getCheckInDate()));
                    sheet.addCell(new Label(6, i + 2, list.get(i).getCheckInTime()));
                    sheet.addCell(new Label(8, i + 2, list.get(i).getCheckOutDate()));
                    sheet.addCell(new Label(9, i + 2, "" + list.get(i).getCheckOutTime()));
//                    sheet.addCell(new Label(10, i + 2, "" + list.get(i).getStatus()));
//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));
                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row
                    sheet.mergeCells(3,i + 2, 4, i + 2);// col , row, to col , to row
                    sheet.mergeCells(6,i + 2, 7, i + 2);// col , row, to col , to row
                    sheet.mergeCells(11,i + 2, 12, i + 2);// col , row, to col , to row
                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
    WritableWorkbook inventoryReport(WritableWorkbook workbook, List<inventoryReportItem> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0,context.getString(R.string.item_name) )); // column and row
                sheet.addCell(new Label(2, 0,  context.getString(R.string.item_number)));
                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.unit_qty)));


                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row


                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(0, i + 2, list.get(i).getName()+""));
                    sheet.addCell(new Label(2, i + 2, list.get(i).getItemNo()));
                    sheet.addCell(new Label(3, i + 2, list.get(i).getQty()+""));

//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));
                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
    WritableWorkbook voucherReport(WritableWorkbook workbook, List<Voucher> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0, context.getString(R.string.cust_name)                    )); // column and row
                sheet.addCell(new Label(2, 0, context.getString(R.string.customer_number))             );
                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.voucher_date) ));
                sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.pay_method)   ));
                sheet.addCell(new Label(5, 0, context.getResources().getString(R.string.app_disc)     ));
                sheet.addCell(new Label(6, 0, context.getResources().getString(R.string.sub_total)    ));
                sheet.addCell(new Label(7, 0, context.getResources().getString(R.string.tax)            ));
                sheet.addCell(new Label(8, 0, context.getResources().getString(R.string.net_sales)      ));


                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row


                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(0, i + 2, list.get(i).getCustName()+""));
                    sheet.addCell(new Label(2, i + 2, list.get(i).getCustNumber()));
                    sheet.addCell(new Label(3, i + 2, list.get(i).getVoucherDate()+""));
                    sheet.addCell(new Label(4, i + 2, list.get(i).getPayMethod()+""));
                    sheet.addCell(new Label(5, i + 2, list.get(i).getVoucherDiscount()+""));
                    sheet.addCell(new Label(6, i + 2, list.get(i).getSubTotal()+""));
                    sheet.addCell(new Label(7, i + 2, list.get(i).getTax()+""));
                    sheet.addCell(new Label(8, i + 2, list.get(i).getNetSales()+""));

//                    sheet.addCell(new Label(11, i + 2, "" +  String.format("%.3f", (list.get(i).getCubic()))));
                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
    WritableWorkbook voucherStockReport(WritableWorkbook workbook, List<Voucher> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0, context.getString(R.string.voucher_number)                    )); // column and row

                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.voucher_date) ));
                sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.remark)   ));



                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row


                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber()+""));
                    sheet.addCell(new Label(3, i + 2, list.get(i).getVoucherDate()+""));
                    sheet.addCell(new Label(4, i + 2, list.get(i).getRemark()+""));


                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
    WritableWorkbook cashReport(WritableWorkbook workbook, List<Voucher> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.mergeCells(0,0, 1, 0);
                sheet.addCell(new Label(2, 0, context.getString(R.string.sales) )); // column and row

                sheet.addCell(new Label(1, 2, context.getResources().getString(R.string.cash_sale) ));
                sheet.addCell(new Label(3, 2, T_cash+"")   );

                sheet.addCell(new Label(1, 4, context.getResources().getString(R.string.credit_sales) ));
                sheet.addCell(new Label(3, 4, T_credit+"")   );

                sheet.addCell(new Label(1, 6, context.getResources().getString(R.string.total_sales) ));
                sheet.addCell(new Label(3, 6, total+"")   );

                sheet.mergeCells(0,1, 5, 1);// col , row, to col , to row
                //***************************************************************************
                sheet.mergeCells(0,7, 5, 7);
                sheet.mergeCells(0,8, 1, 8);
                sheet.addCell(new Label(2, 8, context.getString(R.string.payment) )); // column and row

                sheet.addCell(new Label(1, 10, context.getResources().getString(R.string.cash) ));
                sheet.addCell(new Label(3, 10, cashPayment+"")   );

                sheet.addCell(new Label(1, 12, context.getResources().getString(R.string.app_cheque) ));
                sheet.addCell(new Label(3, 12, creditPayment+"")   );

                sheet.addCell(new Label(1, 14, context.getResources().getString(R.string.netpayment) ));
                sheet.addCell(new Label(3, 14, net+"")   );
                //********************************************************************************
                sheet.mergeCells(0,15, 5, 15);

                sheet.addCell(new Label(2, 16, context.getString(R.string.app_creditCard) )); // column and row

                sheet.addCell(new Label(1, 18, context.getResources().getString(R.string.credit_value) ));
                sheet.addCell(new Label(3, 18, (credit -returnCridet)+"")   );

                sheet.addCell(new Label(1, 20, context.getResources().getString(R.string.total_cash) ));
                sheet.addCell(new Label(3, 20, total_cash+"")   );





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
    WritableWorkbook itemsReport(WritableWorkbook workbook, List<Item> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0, context.getString(R.string.item_number)            )); // column and row
                sheet.addCell(new Label(2, 0, context.getString(R.string.item_name)                        )  );
                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.total_sold_qty2)  ) );
                sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.total_bonus_qty2)   ));
                sheet.addCell(new Label(5, 0, context.getResources().getString(R.string.total_sales_noTax)  ));

                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row


                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(0, i + 2, list.get(i).getItemNo()+""));
                    sheet.addCell(new Label(2, i + 2, list.get(i).getItemName()));
                    sheet.addCell(new Label(3, i + 2, list.get(i).getQty()+""));
                    sheet.addCell(new Label(4, i + 2, list.get(i).getBonus()+""));
                    sheet.addCell(new Label(5, i + 2, ((list.get(i).getQty() * list.get(i).getPrice()) - list.get(i).getDisc())+""));
                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
    WritableWorkbook items_StockReport(WritableWorkbook workbook, List<Item> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0, context.getString(R.string.item_number)            )); // column and row
                sheet.addCell(new Label(2, 0, context.getString(R.string.item_name)                        )  );
                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.qty)  ) );


                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row


                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(0, i + 2, list.get(i).getItemNo()+""));
                    sheet.addCell(new Label(2, i + 2, list.get(i).getItemName()));
                    sheet.addCell(new Label(3, i + 2, list.get(i).getQty()+""));
                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
    WritableWorkbook paymentReport(WritableWorkbook workbook, List<Payment> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0, context.getString(R.string.voucher_number)            )); // column and row
                sheet.addCell(new Label(2, 0, context.getString(R.string.pay_date   )                          )  );
                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.cust_name  )  ) );
                sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.app_amount )   ));
                sheet.addCell(new Label(5, 0, context.getResources().getString(R.string.remark     )  ));
                sheet.addCell(new Label(5, 0, context.getResources().getString(R.string.sale_man_number     )  ));
                sheet.addCell(new Label(5, 0, context.getResources().getString(R.string.pay_method     )  ));

                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row

                for (int i = 0; i < list.size(); i++) {
                    sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber()+""));
                    sheet.addCell(new Label(2, i + 2,      list.get(i).getPayDate()));
                    sheet.addCell(new Label(3, i + 2,  list.get(i).getCustName()+""));
                    sheet.addCell(new Label(4, i + 2,  list.get(i).getAmount()+""));
                    sheet.addCell(new Label(2, i + 2,      list.get(i).getRemark()));
                    sheet.addCell(new Label(3, i + 2,  list.get(i).getSaleManNumber()+""));
                    sheet.addCell(new Label(4, i + 2,  list.get(i).getPayMethod()+""));
                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
    WritableWorkbook SerialListReport(WritableWorkbook workbook, List<serialModel> list) {

        try {
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);//Excel sheet name. 0 represents first sheet

            try {
                sheet.addCell(new Label(0, 0, context.getString(R.string.voucher_number)                    )); // column and row

                sheet.addCell(new Label(3, 0, context.getResources().getString(R.string.voucher_date) ));
                sheet.addCell(new Label(4, 0, context.getResources().getString(R.string.remark)   ));



                sheet.mergeCells(0,1, 1, 1);// col , row, to col , to row


                for (int i = 0; i < list.size(); i++) {
//                    sheet.addCell(new Label(0, i + 2, list.get(i).getVoucherNumber()+""));
//                    sheet.addCell(new Label(3, i + 2, list.get(i).getVoucherDate()+""));
//                    sheet.addCell(new Label(4, i + 2, list.get(i).getRemark()+""));


                    sheet.mergeCells(0,i + 2, 1, i + 2);// col , row, to col , to row

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
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
}
