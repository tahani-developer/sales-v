package com.dr7.salesmanmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
//import android.support.v4.content.ContextCompat;
import android.os.Build;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Account_Report;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PrinterSetting;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Reports.AccountReport;
import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.command.ESCPOSConst;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.jpos.printer.LKPrint;
import com.sewoo.jpos.request.RequestQueue;

import static com.dr7.salesmanmanager.NumberToArabic.getArabicString;
import static com.dr7.salesmanmanager.PrintPayment.pay1;
import static com.dr7.salesmanmanager.PrintPayment.paymentPrinter;
import static com.dr7.salesmanmanager.PrintVoucher.items;
import static com.dr7.salesmanmanager.PrintVoucher.vouch1;
import static com.dr7.salesmanmanager.ReceiptVoucher.payment;
import static com.dr7.salesmanmanager.ReceiptVoucher.paymentsforPrint;
import static com.dr7.salesmanmanager.Reports.CashReport.cash;
import static com.dr7.salesmanmanager.Reports.CashReport.cashPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.credit;
import static com.dr7.salesmanmanager.Reports.CashReport.creditCardPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.creditPayment;
import static com.dr7.salesmanmanager.Reports.CashReport.date;
import static com.dr7.salesmanmanager.Reports.CashReport.net;
import static com.dr7.salesmanmanager.Reports.CashReport.returnCash;
import static com.dr7.salesmanmanager.Reports.CashReport.returnCridet;
import static com.dr7.salesmanmanager.Reports.CashReport.total;
import static com.dr7.salesmanmanager.Reports.CashReport.total_cash;
import static com.dr7.salesmanmanager.Reports.InventoryReport.itemsInventoryPrint;
import static com.dr7.salesmanmanager.Reports.InventoryReport.typeQty;
import static com.dr7.salesmanmanager.SalesInvoice.Exported_Tax;
import static com.dr7.salesmanmanager.SalesInvoice.itemForPrint;

import static com.dr7.salesmanmanager.SalesInvoice.noTax;
import static com.dr7.salesmanmanager.SalesInvoice.valueCheckHidPrice;
import static com.dr7.salesmanmanager.SalesInvoice.voucher;
import static com.dr7.salesmanmanager.StockRequest.clearData;
import static com.dr7.salesmanmanager.StockRequest.listItemStock;
import static com.dr7.salesmanmanager.StockRequest.totalQty;
import static com.dr7.salesmanmanager.StockRequest.voucherStockItem;

public class ESCPSample2
{
	private ESCPOSPrinter posPtr;
	private final char ESC = ESCPOS.ESC;
	private final char LF = ESCPOS.LF;
	Voucher voucherforPrint;
	List<Item> itemforPrint;
	DatabaseHandler obj;
	DecimalFormat decimalFormat,formatNoZero;
	List<Payment>payList;
	List<Item>itemList;
	Voucher voucherStockItems;
	Payment payforBank;
	Context context;
	boolean  isArabicBank=false;
	int taxCalc=0;
	public ESCPSample2(Context context)
	{
		posPtr = new ESCPOSPrinter();
		obj = new DatabaseHandler(context);
		decimalFormat = new DecimalFormat("###.###");
//		formatNoZero=new DecimalFormat("#.#");
		this.context=context;
//		posPtr = new ESCPOSPrinter("EUC-KR"); // Korean.
//		posPtr = new ESCPOSPrinter("BIG5"); // Big5
	}
	public static boolean textContainsArabic(String text) {
		for (char charac : text.toCharArray()) {
			if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC) {
				return true;
			}
		}
		return false;
	}
	public void receipt() throws UnsupportedEncodingException
	{
		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "Receipt" + LF + LF);
		posPtr.printNormal(ESC + "|rA" + ESC + "|bC" + "TEL (123)-456-7890" + LF);
		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + "Thank you for coming to our shop!" + LF + LF);

		posPtr.printNormal(ESC + "|cA" +"Chicken                   $10.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Hamburger                 $20.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Pizza                     $30.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Lemons                    $40.00" + LF);
		posPtr.printNormal(ESC + "|cA" +"Drink                     $50.00" + LF + LF);
		posPtr.printNormal(ESC + "|cA" +"Excluded tax             $150.00" + LF);

		posPtr.printNormal( ESC + "|cA" +ESC + "|uC" + "Tax(5%)                    $7.50" + LF);
		posPtr.printNormal( ESC + "|cA" +ESC + "|bC" + ESC + "|2C" + "Total   $157.50" + LF + LF);
		posPtr.printNormal( ESC + "|cA" +ESC + "|bC" + "Payment                  $200.00" + LF);
		posPtr.printNormal( ESC + "|cA" +ESC + "|bC" + "Change                    $42.50" + LF);
	}

	public int sample() throws UnsupportedEncodingException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		receipt();
		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + ESC + "|4C" + "Thank you" + LF);
//    	posPtr.printNormal("測試");
		posPtr.lineFeed(3);
		return 0;
	}

	public int barcode2d() throws UnsupportedEncodingException
	{
		String data = "ABCDEFGHIJKLMN";
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printString("PDF417\r\n");
		posPtr.printPDF417(data, data.length(), 0, 10, ESCPOSConst.LK_ALIGNMENT_CENTER);
		posPtr.printPDF417(data, data.length(), 4, 3, ESCPOSConst.LK_ALIGNMENT_CENTER);
		posPtr.printString("QRCode\r\n");
		posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);

		posPtr.printString("DDDD\r\n");
//        receipt();
		posPtr.printBarCode("1234567890", LKPrint.LK_BCS_Code39, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
//        posPtr.printNormal(ESC + "|cA" + ESC + "|4C" + ESC + "|bC" + "Thank you" + LF);

		posPtr.lineFeed(4);
//	    posPtr.cutPaper();
		return 0;
	}
	public int barcodesample() throws UnsupportedEncodingException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printBarCode("1234567890", LKPrint.LK_BCS_Code39, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("0123498765", LKPrint.LK_BCS_Code93, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("0987654321", LKPrint.LK_BCS_ITF, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("{ACODE 128", LKPrint.LK_BCS_Code128, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("{BCode 128", LKPrint.LK_BCS_Code128, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("{C12345", LKPrint.LK_BCS_Code128, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printBarCode("A1029384756A", LKPrint.LK_BCS_Codabar, 40, 2, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printNormal(ESC + "|cA" + ESC + "|4C" + ESC + "|bC" + "Thank you" + LF);
		posPtr.lineFeed(3);
		return 0;
	}

	public int imageTest() throws IOException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printBitmap("//sdcard//temp//test//logo_s.jpg", LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.printBitmap("//sdcard//temp//test//sample_2.jpg", LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.printBitmap("//sdcard//temp//test//sample_3.jpg", LKPrint.LK_ALIGNMENT_LEFT);
		posPtr.printBitmap("//sdcard//temp//test//sample_4.jpg", LKPrint.LK_ALIGNMENT_RIGHT);

		Bitmap _bitmap = BitmapFactory.decodeFile("//sdcard//temp//test//logo_s.jpg");
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_CENTER, 0, 1);

		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_LEFT, 1);
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_LEFT, 2);
		posPtr.printBitmap(_bitmap, LKPrint.LK_ALIGNMENT_LEFT, 3);

		posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + ESC + "|4C" + "Thank you" + LF);

		posPtr.lineFeed(3);
		return 0;
	}

	public int imageTest1() throws IOException
	{
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.printBitmap("//sdcard//temp//test//1512301638.png", LKPrint.LK_ALIGNMENT_CENTER);
		posPtr.lineFeed(3);
		return 0;
	}

	public int invoice() throws UnsupportedEncodingException
	{
/*
    	posPtr.setCharSet("UTF-8");

		// Setting PageMode
		posPtr.setPageMode(true);
    	// 180 DPI or 203 DPI
		// 180 DPI - 7 dot per 1mm
    	// 203 DPI - 8 dot per 1mm
    	posPtr.setDPI(203);
    	// Print direction.
		posPtr.setPrintDirection(ESCPOSConst.DIRECTION_LEFT_RIGHT);
    	// 399 dot x 630 dot.
		posPtr.setPrintingArea(399, 630);

    	// Data
    	// Medium Text (20, 20)
    	posPtr.setAbsoluteVertical(20);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("丟並乾亂佔佪亙", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_2WIDTH | LKPrint.LK_TXT_2HEIGHT);

    	// Large Text
	    posPtr.setAbsoluteVertical(90);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("伋伕佇佈", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

	    // Must be Off Unicode when print Alphabet or print barcode.
	    posPtr.setCharSet("Big5");

	    posPtr.setAbsoluteVertical(190);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("ABCDE", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

	    posPtr.setCharSet("UTF-8");

    	// Small Text
	    posPtr.setAbsoluteVertical(300);
    	posPtr.setAbsoluteHorizontal(20);
	    posPtr.printText("壓壘壙壚壞壟壢壩壯壺", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH | LKPrint.LK_TXT_1HEIGHT);

	    // Must be Off Unicode when print Alphabet or print barcode.
    	posPtr.setCharSet("Big5");

    	// 1D Barcode
    	posPtr.setAbsoluteVertical(380);
    	posPtr.setAbsoluteHorizontal(0);
    	posPtr.printBarCode("0123456789012345678901", ESCPOSConst.LK_BCS_Code39, 40, 1, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);
//    	    	posPtr.printBarCode("0123498765", ESCPOSConst.LK_BCS_Code93, 40, 2, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);

    	// QRCODE
    	String data = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
    	posPtr.setAbsoluteVertical(450);
    	posPtr.setAbsoluteHorizontal(40);
    	posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);
    	posPtr.setAbsoluteVertical(450);
    	posPtr.setAbsoluteHorizontal(240);
    	posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);

    	// Data
	    posPtr.printPageModeData();
    	posPtr.setPageMode(false);
    	posPtr.lineFeed(4);
    	return 0;
*/
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		posPtr.setCharSet("Big5");

		// Setting PageMode
		posPtr.setPageMode(true);
		// 180 DPI or 203 DPI
		// 180 DPI - 7 dot per 1mm
		// 203 DPI - 8 dot per 1mm
		posPtr.setDPI(203);
		// Print direction.
		posPtr.setPrintDirection(ESCPOSConst.DIRECTION_LEFT_RIGHT);
		// 399 dot x 630 dot.
		posPtr.setPrintingArea(399, 730); // al

		// Data
		// Medium Text (20, 20)
		posPtr.setAbsoluteVertical(20);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("丟並乾亂佔佪亙", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_2WIDTH | LKPrint.LK_TXT_2HEIGHT);

		// Large Text
		posPtr.setAbsoluteVertical(90);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("伋伕佇佈", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

		// Must be Off Unicode when print Alphabet or print barcode.
		posPtr.setCharSet("Big5");

		posPtr.setAbsoluteVertical(190);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("ABCDE", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_3WIDTH | LKPrint.LK_TXT_3HEIGHT);

		posPtr.setCharSet("Big5");

		// Small Text
		posPtr.setAbsoluteVertical(300);
		posPtr.setAbsoluteHorizontal(20);
		posPtr.printText("壓壘壙壚壞壟壢壩壯壺桌號菇", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH | LKPrint.LK_TXT_1HEIGHT);

		// Must be Off Unicode when print Alphabet or print barcode.
		//posPtr.setCharSet("Big5");

		// 1D Barcode
		posPtr.setAbsoluteVertical(380);
		posPtr.setAbsoluteHorizontal(0);
		posPtr.printBarCode("0123456789012345678901", ESCPOSConst.LK_BCS_Code39, 40, 1, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);
//    	    	posPtr.printBarCode("0123498765", ESCPOSConst.LK_BCS_Code93, 40, 2, ESCPOSConst.LK_ALIGNMENT_CENTER, ESCPOSConst.LK_HRI_TEXT_NONE);

		// QRCODE
		String data = "中華民國萬萬稅1234567890123456789012345678901234567890123456789012345678";
		posPtr.setAbsoluteVertical(450);
		posPtr.setAbsoluteHorizontal(40);
		posPtr.printQRCode(data, data.length(), 5, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);
		posPtr.setAbsoluteVertical(450);
		posPtr.setAbsoluteHorizontal(240);
		posPtr.printQRCode(data, data.length(), 3, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);

		// Data
		posPtr.printPageModeData();
		posPtr.setPageMode(false);
		posPtr.lineFeed(4);
		return 0;

	}

	public int printDataMatrix() throws UnsupportedEncodingException
	{
		// DataMatrix
		int sts;

		sts = posPtr.printerCheck();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		sts = posPtr.status();
		if(sts != ESCPOSConst.LK_STS_NORMAL)
		{
			return sts;
		}

		posPtr.setAsync(false);

		String data = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		posPtr.printDataMatrix(data, data.length(), 6, ESCPOSConst.LK_ALIGNMENT_CENTER);

		posPtr.lineFeed(4);
		return 0;
	}

	public int printAndroidFont() throws UnsupportedEncodingException
	{
		int nLineWidth = 384;
		String data = "Receipt";
//    	String data = "영수증";
		Typeface typeface = null;

		try
		{
			int sts;

			sts = posPtr.printerCheck();
			if(sts != ESCPOSConst.LK_STS_NORMAL)
			{
				return sts;
			}

			sts = posPtr.status();
			if(sts != ESCPOSConst.LK_STS_NORMAL)
			{
				return sts;
			}

			posPtr.setAsync(false);

			posPtr.printAndroidFont(data, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(2);
			posPtr.printAndroidFont("Left Alignment", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont("Center Alignment", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont("Right Alignment", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, "SANS_SERIF : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, "SERIF : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(typeface.MONOSPACE, "MONOSPACE : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, "SANS : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, "SANS BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, false, "SANS BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, false, true, "SANS ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, true, "SANS BOLD ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SANS_SERIF, true, true, true, "SANS B/I/U : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.SERIF, "SERIF : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, "SERIF BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, false, "SERIF BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, false, true, "SERIF ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, true, "SERIF BOLD ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.SERIF, true, true, true, "SERIF B/I/U : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(2);
			posPtr.printAndroidFont(Typeface.MONOSPACE, "MONOSPACE : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, "MONO BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, false, "MONO BOLD : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, false, true, "MONO ITALIC : 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, true, "MONO BOLD ITALIC: 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(Typeface.MONOSPACE, true, true, true, "MONO B/I/U: 1234iwIW", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(4);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public int printMultilingualFont() throws UnsupportedEncodingException
	{
		int nLineWidth = 550;
		String Koreandata = "영수증";
		String Turkishdata = "Turkish(İ,Ş,Ğ)";
		String Russiandata = "Получение";
		String Arabicdata = "الإيصال";
		String Greekdata = "Παραλαβή";
		String Japanesedata = "領収書";
		String GB2312data = "收据";
		String BIG5data = "收據";

//		try
//		{
			int sts;

//        	sts = posPtr.printerCheck();
//    		if(sts != ESCPOSConst.LK_STS_NORMAL)
//    		{
//    			return sts;
//    		}
//
//        	sts = posPtr.status();
//    		if(sts != ESCPOSConst.LK_STS_NORMAL)
//    		{
//    			return sts;
//    		}

			posPtr.setAsync(false);

//    		posPtr.printAndroidFont("Korean Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Korean 100-dot size font in android device.
//        	posPtr.printAndroidFont(Koreandata, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);

//			posPtr.printAndroidFont("Turkish Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Turkish 50-dot size font in android device.
//        	posPtr.printAndroidFont(Turkishdata, nLineWidth, 50, ESCPOSConst.LK_ALIGNMENT_CENTER);

//			posPtr.printAndroidFont("Russian Font", 384, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Russian 60-dot size font in android device.
//        	posPtr.printAndroidFont(Russiandata, nLineWidth, 60, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("Arabic Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			// Arabic 100-dot size font in android device.
//			posPtr.printAndroidFont(Arabicdata, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);


//			Log.e("ifEnglish",""+ifEnglish('a'));
//		Log.e("ifEnglish",""+ifEnglish('1'));
//		Log.e("ifEnglish",""+ifEnglish('V'));
//		Log.e("ifEnglish",""+ifEnglish('m'));
//		Log.e("ifEnglish",""+ifEnglish(';'));

//			posPtr.printAndroidFont("Greek Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// Greek 60-dot size font in android device.
//        	posPtr.printAndroidFont(Greekdata, nLineWidth, 60, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("Japanese Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//        	// Japanese 100-dot size font in android device.
//        	posPtr.printAndroidFont(Japanesedata, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("GB2312 Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// GB2312 100-dot size font in android device.
//        	posPtr.printAndroidFont(GB2312data, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);
//
//			posPtr.printAndroidFont("BIG5 Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//    		// BIG5 100-dot size font in android device.
//        	posPtr.printAndroidFont(BIG5data, nLineWidth, 100, ESCPOSConst.LK_ALIGNMENT_CENTER);

			posPtr.lineFeed(4);
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return 0;
	}


	public void printMultilingualFontEsc(int count) throws UnsupportedEncodingException {
		if (count == 0) {
			voucherforPrint = vouch1;
			itemforPrint = items;
		} else {
			voucherforPrint = voucher;
			itemforPrint = itemForPrint;
		}
		int nLineWidth = 550;
		double total_Qty=0;

		try {

			String voucherTyp = "";
			switch (voucherforPrint.getVoucherType()) {
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

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//			public int printAndroidFont(Typeface typeface, boolean isBold, String textString, int widthDots, int textSize, int alignment)
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);

			posPtr.printAndroidFont(null,true,companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"هاتف : " + companyInfo.getcompanyTel()+"    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"رقم الفاتورة : " + voucherforPrint.getVoucherNumber()+"    " + "          التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"اسم العميل   : " + voucherforPrint.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"ملاحظة        : " + voucherforPrint.getRemark() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"نوع الفاتورة : " +voucherTyp+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"طريقة الدفع  : " +  (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا")+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				total_Qty=0;
				posPtr.printAndroidFont(null,true," السلعة              " + "العدد" + "\t\t\t" + "الوزن" + "\t\t\t" +  "سعر الوحدة" + "\t\t\t" +"المجموع" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();

						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
						if (itemforPrint.get(i).getItemName().length() <= 12) {
							String space = itemforPrint.get(i).getItemName();
							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
								space =  space+" " ;
							}
							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						} else {
							String space = itemforPrint.get(i).getItemName().substring(0, 10);
//                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
//                            space = " " + space;
//                        }
							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						}


					}
				}
			} else {
				total_Qty=0;
				posPtr.printAndroidFont(  null,true," السلعة              " + "العدد" + "\t\t\t" + "سعر الوحدة" + "\t\t\t" + "المجموع" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());


						if (itemforPrint.get(i).getItemName().length() <= 12) {
							String space = itemforPrint.get(i).getItemName();
							int m=(12 - itemforPrint.get(i).getItemName().length());
							Log.e("true22",""+m);

							for (int g = 0; g <m ; g++) {
								space =  space+"   ";
							}
							char nam=itemforPrint.get(i).getItemName().charAt(0);
							if(!ifEnglish(nam)){
								Log.e("true22",""+nam);
								posPtr.printAndroidFont(null,true,  space + "     " + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
							}else {
								Log.e("false22",""+nam);
								posPtr.printAndroidFont(null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t" +itemforPrint.get(i).getPrice()  + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" +space + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);

							}

						} else {
							String space = itemforPrint.get(i).getItemName().substring(0, 12);
//                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
//                            space = " " + space;
//                        }
							String fullString = itemforPrint.get(i).getItemName().substring(12, itemforPrint.get(i).getItemName().length() - 1);

							char nam=itemforPrint.get(i).getItemName().charAt(0);
							if(!ifEnglish(nam)) {
								posPtr.printAndroidFont( null,true,  space + "     "  + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
								Log.e("true233",""+nam);
							}else {
								posPtr.printAndroidFont( null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t"  +itemforPrint.get(i).getPrice()  + "\t\t\t\t" +itemforPrint.get(i).getQty()  + "\t\t\t\t" +space  + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);
								Log.e("false233",""+nam);

							}
						}

					}
				}

			}

			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "اجمالي الكمية  : " + total_Qty + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "المجموع  : " + voucherforPrint.getSubTotal() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الخصم    : " + voucherforPrint.getTotalVoucherDiscount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الضريبة  : " + voucherforPrint.getTax() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الصافي   : " + voucherforPrint.getNetSales() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true,  "المستلم : ________________ التوقيع : __________" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			Log.e("IOException","printevoucherforPrint="+e.getMessage());
			e.printStackTrace();
		}


	}

	public void printMultilingualFontEsc2(int count) throws UnsupportedEncodingException {
		if (count == 0) {
			voucherforPrint = vouch1;
			itemforPrint = items;
		} else {
			voucherforPrint = voucher;
			itemforPrint = itemForPrint;
		}
		int nLineWidth = 550;
		double total_Qty=0;

		try {

			String voucherTyp = "";
			switch (voucherforPrint.getVoucherType()) {
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

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//			public int printAndroidFont(Typeface typeface, boolean isBold, String textString, int widthDots, int textSize, int alignment)
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);

//			posPtr.printAndroidFont(null,true,companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"هاتف : " + companyInfo.getcompanyTel()+"    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"رقم الفاتورة : " + voucherforPrint.getVoucherNumber()+"    " + "          التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(null,true,"اسم العميل   : " + voucherforPrint.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"ملاحظة        : " + voucherforPrint.getRemark() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"نوع الفاتورة : " +voucherTyp+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"طريقة الدفع  : " +  (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا")+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				total_Qty=0;
				posPtr.printAndroidFont(null,true," السلعة              " + "العدد" + "\t\t\t" + "الوزن" + "\t\t\t" +  "سعر الوحدة" + "\t\t\t" +"المجموع" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();

						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
						if (itemforPrint.get(i).getItemName().length() <= 12) {
							String space = itemforPrint.get(i).getItemName();
							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
								space =  space+" " ;
							}
							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						} else {
							String space = itemforPrint.get(i).getItemName().substring(0, 10);
//                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
//                            space = " " + space;
//                        }
							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						}


					}
				}
			} else {
				total_Qty=0;
				posPtr.printAndroidFont(  null,true," السلعة              " + "العدد" + "\t\t\t" + "سعر الوحدة" + "\t\t\t" + "المجموع" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());

						posPtr.printAndroidFont(null,true,  itemforPrint.get(i).getItemName()+"\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						posPtr.printAndroidFont(null,true,   "   "+convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t\t\t\t\t" +itemforPrint.get(i).getQty() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                        posPtr.printAndroidFont(  null,true,"---------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


//						if (itemforPrint.get(i).getItemName().length() <= 12) {
//							String space = itemforPrint.get(i).getItemName();
//							int m=(12 - itemforPrint.get(i).getItemName().length());
//							Log.e("true22",""+m);
//
//							for (int g = 0; g <m ; g++) {
//								space =  space+"   ";
//							}
//							char nam=itemforPrint.get(i).getItemName().charAt(0);
//							if(!ifEnglish(nam)){
//								Log.e("true22",""+nam);
//								posPtr.printAndroidFont(null,true,  itemforPrint.get(i).getItemName()+"\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//								posPtr.printAndroidFont(null,true,   "\t\t\t\t\t\t\t"+itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//							}else {
//								Log.e("false22",""+nam);
//								posPtr.printAndroidFont(null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t" +itemforPrint.get(i).getPrice()  + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" +space + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);
//
//							}
//
//						} else {
//							String space = itemforPrint.get(i).getItemName().substring(0, 12);
////                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
////                            space = " " + space;
////                        }
//							String fullString = itemforPrint.get(i).getItemName().substring(12, itemforPrint.get(i).getItemName().length() - 1);
//
//							char nam=itemforPrint.get(i).getItemName().charAt(0);
//							if(!ifEnglish(nam)) {
//								posPtr.printAndroidFont( null,true,  space + "     "  + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//								Log.e("true233",""+nam);
//							}else {
//								posPtr.printAndroidFont( null,true,  convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\t\t\t\t"  +itemforPrint.get(i).getPrice()  + "\t\t\t\t" +itemforPrint.get(i).getQty()  + "\t\t\t\t" +space  + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_RIGHT);
//								Log.e("false233",""+nam);
//
//							}
//						}

					}
				}

			}

//			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(  null,true, "اجمالي الكمية  : " + total_Qty + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "المجموع  : " + voucherforPrint.getSubTotal() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "الخصم    : " + voucherforPrint.getTotalVoucherDiscount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "الضريبة  : " + voucherforPrint.getTax() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "الصافي   : " + voucherforPrint.getNetSales() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true, "اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(  null,true,  "المستلم : ________________ التوقيع : __________" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	int  printerType=5,dontshowTax=0; String notePosition="0";
	public void printMultilingualFontEsc3(int count, Voucher voucherforPrint, List<Item>itemforPrint) throws UnsupportedEncodingException {

		int largeHeader=24,textSmallcontent=20,x_Larg=26;
		int dontShowHeader=0,dontShowHeaderinOrders=0;
		int tayaaLayout=0,net_requiredValue=0;
		try {
			List<PrinterSetting> printerSettings = obj.getPrinterSetting_();
            notePosition=obj.getAllCompanyInfo().get(0).getNotePosition();
            Log.e("notePosition","=="+notePosition);

			if (printerSettings.size() != 0) {
				printerType = printerSettings.get(0).getPrinterName();
				dontShowHeader=printerSettings.get(0).getDontPrintHeader();
				dontShowHeaderinOrders=printerSettings.get(0).getDontrprintheadeInOrders();
				tayaaLayout=printerSettings.get(0).getTayeeLayout();
				net_requiredValue=printerSettings.get(0).getNetsalflag();
				Log.e("printerType",""+printerType+"\ttayaaLayout="+tayaaLayout);
			}
		}catch (Exception e)
		{
			Log.e("printerType","1="+e.getMessage());
			printerType=5;
		}
		try {
			dontshowTax=obj.getAllSettings().get(0).getDontShowtax();

		}catch (Exception e){
			Log.e("printerType","2="+e.getMessage());
			dontshowTax=0;
		}



//		if (count == 0) {
//			voucherforPrint = vouch1;
//			itemforPrint = items;
//		} else {
//			voucherforPrint = voucher;
//			itemforPrint = itemForPrint;
//		}
		int nLineWidth = 550;// 550
		int alignment=0;
		String line="";

		String headerVoucher="";
		if(printerType==6||printerType==7){
			alignment=ESCPOSConst.LK_ALIGNMENT_LEFT;
			nLineWidth=370;
			largeHeader=20;
			textSmallcontent=16;
			x_Larg=22;
			headerVoucher= "العدد | " + "\t" + " السعر | " +" الخصم  | " + "\t" + " المجموع" + "\n" ;
			line="-------------------------------------------------------";
		}
		else {
			nLineWidth=550;
//			nLineWidth=370;
//			headerVoucher=" السلعة            " + "العدد " + "\t\t" + "السعر "+"\t\t"+"الخصم " + "\t\t" + "المجموع" + "\n" ;
			headerVoucher= " العدد  " + "\t\t" + "  السعر "+"\t\t"+"    الخصم  " + "\t" +" المجاني" + "\t" + "  المجموع " + "\n" ;
			alignment=ESCPOSConst.LK_ALIGNMENT_LEFT;
			line="-----------------------------------------------";
//			line="---------------------------------------------------------------";
		}



		double total_Qty=0,itemDiscount=0;

		try {

			String voucherTyp = "فاتورة بيع";
			switch (voucherforPrint.getVoucherType()) {
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

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			taxCalc = obj.getAllSettings().get(0).getTaxClarcKind();
			if (companyInfo.getLogo() != null) {
				try {
					if (((dontShowHeader == 1 && voucherforPrint.getPayMethod() == 1) || (dontShowHeaderinOrders == 1 && voucherforPrint.getVoucherType() == 508)) || (dontShowHeader == 1 && noTax == 0)) {
					} else {
						posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER, 200);
					}
				} catch (Exception e) {
					Log.e("Exception", "getLogo=" + e.getMessage());
				}

				Log.e("12222print", "pyyy");
			}
			if (((dontShowHeader == 1 && voucherforPrint.getPayMethod() == 1) || (dontShowHeaderinOrders == 1 && voucherforPrint.getVoucherType() == 508)) || (dontShowHeader == 1 && noTax == 0)) {
			} else {
				posPtr.printAndroidFont(null, true, companyInfo.getCompanyName() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

			}
			if (printerType == 6||printerType==7) {
				if (((dontShowHeader == 1 && voucherforPrint.getPayMethod() == 1) || (dontShowHeaderinOrders == 1 && voucherforPrint.getVoucherType() == 508)) || (dontShowHeader == 1 && noTax == 0)) {
				} else {
					posPtr.printAndroidFont(null, true, "هاتف : " + companyInfo.getcompanyTel() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

					if (companyInfo.getTaxNo() != 0)
						posPtr.printAndroidFont(null, true, " الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(null, true, "رقم الفاتورة : " + voucherforPrint.getVoucherNumber() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

				}

				//posPtr.printAndroidFont(null,true, " التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(null, true, voucherforPrint.getVoucherDate() + "\t\t" + getCurentTimeDate(2) + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);
//				posPtr.printAndroidFont(null,true, " التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(null, true, voucherTyp + "\t\t" + (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

				posPtr.printAndroidFont(null, true, line + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

			} else {
				if (((dontShowHeader == 1 && voucherforPrint.getPayMethod() == 1) || (dontShowHeaderinOrders == 1 && voucherforPrint.getVoucherType() == 508)) || (dontShowHeader == 1 && noTax == 0)) {
				} else {
					if (tayaaLayout == 1) {
						posPtr.printAndroidFont(null, true, voucherTyp + "\t\t" + (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا") + "رقم الفاتورة : " + voucherforPrint.getVoucherNumber() + "\t\t" + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

						posPtr.printAndroidFont(null, true, "هاتف : " + companyInfo.getcompanyTel() + "    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);
						posPtr.printAndroidFont(null, true, " التاريخ: " + voucherforPrint.getVoucherDate() + "      الوقت :" + voucherforPrint.getTime() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);


					} else {
						if (companyInfo.getTaxNo() != 0)
							posPtr.printAndroidFont(null, true, "هاتف : " + companyInfo.getcompanyTel() + "    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);
						else {
							posPtr.printAndroidFont(null, true, "هاتف : " + companyInfo.getcompanyTel() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

						}

						posPtr.printAndroidFont(null, true, " التاريخ: " + voucherforPrint.getVoucherDate() + "      الوقت :" + voucherforPrint.getTime() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);
					}


				}
				if (tayaaLayout == 0)
					posPtr.printAndroidFont(null, true, "رقم الفاتورة : " + voucherforPrint.getVoucherNumber() + "\t\t" + voucherTyp + "\t\t" + (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				//posPtr.printAndroidFont(null, true,  voucherTyp +"\t\t"+(voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null, true, line + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			}
			String salesmanName = obj.getAllSettings().get(0).getSalesMan_name();
			if (salesmanName.equals("")) {
				salesmanName = obj.getSalesmanName_fromSalesTeam();
			}
			Log.e("salesmanName", "" + salesmanName);

			posPtr.printAndroidFont(null, true, "اسم  المندوب    : " + salesmanName + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null, true, "اسم العميل   : " + voucherforPrint.getCustName() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_LEFT);
//			posPtr.printAndroidFont(null, true,  voucherTyp +"\t\t"+(voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			//posPtr.printAndroidFont(null, true, "نوع الفاتورة : " + voucherTyp + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			//posPtr.printAndroidFont(null, true, "طريقة الدفع  : " + (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			String noteCompany = companyInfo.getNoteForPrint();
			if (((dontShowHeader == 1 && voucherforPrint.getPayMethod() == 1) || (dontShowHeaderinOrders == 1 && voucherforPrint.getVoucherType() == 508)) || (dontShowHeader == 1 && noTax == 0)) {
			} else {
				if (dontshowTax == 0 && (notePosition.equals("0")) && noteCompany.trim().length() != 0) {
					posPtr.printAndroidFont(null, true, companyInfo.getNoteForPrint() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}

			}
			String voucherNote = voucherforPrint.getRemark();

			//Log.e("voucherNote","="+voucherNote+"\t"+voucherNote.length());
			try {


				if (voucherNote.trim().length() != 0)
					posPtr.printAndroidFont(null, true, "ملاحظة        : " + voucherforPrint.getRemark() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_LEFT);
			} catch (Exception e) {
				Log.e("IOException", "printegetRemark=" + e.getMessage());
			}
			posPtr.printAndroidFont(null, true, line + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				total_Qty = 0;
				posPtr.printAndroidFont(null, true, "العدد" + "\t\t\t" + " الوزن" + "\t\t\t" + "السعر" + "\t\t" + "الضريبة" + "\t\t" + "المجموع" + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				//	posPtr.printAndroidFont(null, true, " السلعة   " + "العدد" + "\t\t\t" + "الوزن" + "\t\t\t" + "سعر الوحدة" + "\t\t\t" + "المجموع" + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null, true, line + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);
				//*********************************************************************************************************************
				for (int i = 0; i < itemforPrint.size(); i++) {
					if ((voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) && (itemforPrint.get(i).getVoucherType() == voucherforPrint.getVoucherType())) {
						total_Qty += itemforPrint.get(i).getQty();

						itemDiscount += itemforPrint.get(i).getDisc();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
//						if (itemforPrint.get(i).getItemName().length() <= 12) {
//							String space = itemforPrint.get(i).getItemName();
//							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
//								space =  space+" " ;
//							}
//							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						} else {
//							String space = itemforPrint.get(i).getItemName().substring(0, 10);
////                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
////                            space = " " + space;
////                        }
//							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
//							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						}
						String space = itemforPrint.get(i).getItemNo();
						for (int g = 0; g < (10 - itemforPrint.get(i).getItemNo().length()); g++) {
							space = space + " ";
						}
						space = "";

						posPtr.printAndroidFont(null, true, itemforPrint.get(i).getItemName() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_LEFT);

						posPtr.printAndroidFont(null, true, convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(itemforPrint.get(i).getTaxPercent()))) + "\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(itemforPrint.get(i).getPrice()))) + "\t\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(itemforPrint.get(i).getQty()))) + "\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(itemforPrint.get(i).getUnit()))) + "\t\t" + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_RIGHT);


					}
				}
				//*************************************************************************************

//				for (int i = 0; i < itemforPrint.size(); i++) {
//					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
//						total_Qty+=itemforPrint.get(i).getQty();
//
//						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
//						if (itemforPrint.get(i).getItemName().length() <= 12) {
//							String space = itemforPrint.get(i).getItemName();
//							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
//								space =  space+" " ;
//							}
//							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						} else {
//							String space = itemforPrint.get(i).getItemName().substring(0, 10);
////                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
////                            space = " " + space;
////                        }
//							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
//							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						}
//
//
//					}
//				}
			} else {
				total_Qty = 0;
				itemDiscount = 0;

				posPtr.printAndroidFont(null, true, headerVoucher, nLineWidth, largeHeader, alignment);
				posPtr.printAndroidFont(null, true, line + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					if ((voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) && (itemforPrint.get(i).getVoucherType() == voucherforPrint.getVoucherType())) {
						total_Qty += itemforPrint.get(i).getQty();

						itemDiscount += itemforPrint.get(i).getDisc();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
						posPtr.printBitmap(itemPrint(itemforPrint.get(i).getPrice() + "",
								Double.valueOf(convertToEnglish(amount)), itemforPrint.get(i).getQty() + "",
								itemforPrint.get(i).getItemName(), itemforPrint.get(i).getDisc(),
								itemforPrint.get(i).getItemNo(), itemforPrint.get(i).getTaxValue(), itemforPrint.get(i).getBonus()),
								ESCPOSConst.LK_ALIGNMENT_CENTER, nLineWidth);

					}
				}

			}

			double totalItemsDiscount = 0;
			try {
				totalItemsDiscount = itemDiscount;
			} catch (Exception e) {
				Log.e("getException", "totalItemsDiscount===");
			}
			//Log.e("getTotalVoucherDiscount",""+totalItemsDiscount+"\t"+(voucherforPrint.getSubTotal() + totalItemsDiscount));


			posPtr.printAndroidFont(null, true, line + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);
			if (printerType == 6) {

//
				if (dontshowTax == 0) {

					posPtr.printAndroidFont(null, true, "الكمية       " + " المجموع      " + " الخصم  " + "\n", nLineWidth, x_Larg, alignment);
					posPtr.printAndroidFont(null, true, +voucherforPrint.getTotalVoucherDiscount() + "\t\t\t\t" + convertToEnglish(decimalFormat.format(voucherforPrint.getSubTotal())) + "\t\t\t\t" + convertToEnglish(total_Qty + "") + "\n", nLineWidth, 26, ESCPOSConst.LK_ALIGNMENT_RIGHT);
					if(net_requiredValue==1)
					{
						posPtr.printAndroidFont(null, true, "الضريبة       "+"القيمة المطلوبة   : "+  "\n" , nLineWidth, x_Larg, alignment);

					}else {
						posPtr.printAndroidFont(null, true, "ا " +
								"لضريبة       " + "الصافي " + "\n", nLineWidth, x_Larg, ESCPOSConst.LK_ALIGNMENT_LEFT);
					}


					posPtr.printAndroidFont(null, true, convertToEnglish(decimalFormat.format(Double.valueOf(voucherforPrint.getNetSales()))) + "\t\t\t\t" + convertToEnglish(decimalFormat.format(Double.valueOf(voucherforPrint.getTax()))) + "" + "\n", nLineWidth, x_Larg, ESCPOSConst.LK_ALIGNMENT_RIGHT);
				}
				else {

					posPtr.printAndroidFont(null, true, "الكمية " + " خصم.س  " + " خصم.ك  " + "\n", nLineWidth, x_Larg, alignment);
					posPtr.printAndroidFont(null, true, convertToEnglish(decimalFormat.format(Double.valueOf(voucherforPrint.getTotalVoucherDiscount()))) + "\t\t" + convertToEnglish(decimalFormat.format(itemDiscount)) + "\t\t" + convertToEnglish(decimalFormat.format((total_Qty))) + "\n", nLineWidth, x_Larg, ESCPOSConst.LK_ALIGNMENT_RIGHT);

					if(net_requiredValue==1)
					{
						posPtr.printAndroidFont(null, true, "القيمة المطلوبة   : " + convertToEnglish(decimalFormat.format(voucherforPrint.getNetSales()) )+ "\n", nLineWidth, x_Larg, alignment);

					}else
					posPtr.printAndroidFont(null, true, "الصافي   " + convertToEnglish(decimalFormat.format(Double.valueOf(voucherforPrint.getNetSales()))), nLineWidth, x_Larg, ESCPOSConst.LK_ALIGNMENT_LEFT);
//					posPtr.printAndroidFont(null, true,    convertToEnglish(decimalFormat.format(Double.valueOf(voucherforPrint.getNetSales())))  +"\n", nLineWidth, 26, ESCPOSConst.LK_ALIGNMENT_RIGHT);
//
				}
			} else {

				if (printerType == 7) {
					posPtr.printBitmap(convertLayoutToImage_Footer(voucherforPrint, itemforPrint, total_Qty), ESCPOSConst.LK_ALIGNMENT_CENTER, nLineWidth);

				} else {

				if (tayaaLayout == 0)
					posPtr.printAndroidFont(null, true, "اجمالي الكمية  : " + convertToEnglish(decimalFormat.format(total_Qty)) + "\n", nLineWidth, x_Larg, alignment);
				if (dontshowTax == 0) {//MORMAL PRINT
					posPtr.printAndroidFont(null, true, "المجموع  : " + convertToEnglish(decimalFormat.format(voucherforPrint.getSubTotal() + totalItemsDiscount)) + "\n", nLineWidth, x_Larg, alignment);

				}
				if (dontshowTax == 0) {
					//	posPtr.printAndroidFont(null, true, "الخصم    : " + voucherforPrint.getTotalVoucherDiscount() + "\n", nLineWidth, 26, alignment);

					posPtr.printAndroidFont(null, true, "الضريبة  : " + voucherforPrint.getTax() + "\n", nLineWidth, x_Larg, alignment);

				}
				//posPtr.printAndroidFont(null, true, "اجمالي الخصم   : " + convertToEnglish(decimalFormat.format((voucherforPrint.getTotalVoucherDiscount()+itemDiscount) ))+ "\n", nLineWidth, 26, alignment);
				if (tayaaLayout == 0)
					posPtr.printAndroidFont(null, true, " خصم سطري    : " + convertToEnglish(decimalFormat.format(itemDiscount)) + "\n", nLineWidth, x_Larg, alignment);
				posPtr.printAndroidFont(null, true, " خصم كلي    : " + voucherforPrint.getTotalVoucherDiscount() + "\n", nLineWidth, x_Larg, alignment);


				if (net_requiredValue == 1) {
					posPtr.printAndroidFont(null, true, "القيمة المطلوبة   : " + convertToEnglish(decimalFormat.format(voucherforPrint.getNetSales())) + "\n", nLineWidth, x_Larg, alignment);

				} else {
					posPtr.printAndroidFont(null, true, "الصافي   : " + convertToEnglish(decimalFormat.format(voucherforPrint.getNetSales())) + "\n", nLineWidth, x_Larg, alignment);
				}
			}

			}
			if (printerType != 7) {

				posPtr.printAndroidFont(null, true, "استلمت البضاعة كاملة و بحالة جيدة " + "\n", nLineWidth, x_Larg, alignment);
				posPtr.printAndroidFont(null, true, " و خالية من اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n", nLineWidth, x_Larg, alignment);
				if (obj.getAllSettings().get(0).getTafqit() == 1 && valueCheckHidPrice != 1) {
					posPtr.printAndroidFont(null, true, "استلمت : " + getArabicString(voucherforPrint.getNetSales() + "")/* + "\n" */, nLineWidth, x_Larg, alignment);

				}
				posPtr.printAndroidFont(null, true, "المستلم : ________________ " + "\n", nLineWidth, x_Larg, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, "التوقيع : ________________ " + "\n", nLineWidth, x_Larg, ESCPOSConst.LK_ALIGNMENT_LEFT);
//Log.e("tayaaLayout","=="+tayaaLayout);

			if (notePosition.equals("1")) {
				posPtr.printAndroidFont(null, true, companyInfo.getNoteForPrint() + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_RIGHT);

			}
		}

			if(tayaaLayout==1)
			posPtr.printBitmap(getCompanyTayeeLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER, 300);


            posPtr.printAndroidFont(null, true, line + "\n", nLineWidth, largeHeader, ESCPOSConst.LK_ALIGNMENT_CENTER);

            posPtr.lineFeed(4);

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e("IOException","printeThread.sleep="+e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			Log.e("printerType","3="+e.getMessage());
			e.printStackTrace();
		}


	}
	private Bitmap convertLayoutToImage_Footer(Voucher voucher,List<Item> items,double tot_qty) {
		LinearLayout linearView = null;

		final Dialog dialog_footer = new Dialog(context);
		dialog_footer.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_footer.setCancelable(false);
//		dialog_footer.setContentView(R.layout.footer_voucher_print);
		dialog_footer.setContentView(R.layout.print_footer_voucher);
		int textSize=16;
		CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);


		TextView total, discount, tax, ammont, Total_qty_total;
		total = (TextView) dialog_footer.findViewById(R.id.total);
		discount = (TextView) dialog_footer.findViewById(R.id.discount);
		tax = (TextView) dialog_footer.findViewById(R.id.tax);
		ammont = (TextView) dialog_footer.findViewById(R.id.ammont);
		total.setText("" + voucher.getSubTotal());
		total.setTextSize(textSize);
		discount.setText(convertToEnglish(String.valueOf(decimalFormat.format( voucher.getTotalVoucherDiscount()))));
		discount.setTextSize(textSize);
		tax.setText("" + voucher.getTax());
		tax.setTextSize(textSize);
		ammont.setText("" + voucher.getNetSales());
		ammont.setTextSize(textSize);
		Total_qty_total=(TextView) dialog_footer.findViewById(R.id.total_qty);
		Total_qty_total.setText(tot_qty+"");
		linearView = (LinearLayout) dialog_footer.findViewById(R.id.tab);

		linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

		Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

		Bitmap bitmap=null;
		try {
			bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Drawable bgDrawable = linearView.getBackground();
			if (bgDrawable != null) {
				bgDrawable.draw(canvas);
			} else {
				canvas.drawColor(Color.WHITE);
			}
			linearView.draw(canvas);
		}catch (Exception e){
			Log.e("Exception","printebitmap="+e.getMessage());
		}

		return bitmap;


	}
	private Bitmap getCompanyTayeeLogo() {
		Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logos);
		Log.e("getCompanyTayeeLogo","largeIcon"+largeIcon);
	//	Bitmap image =((BitmapDrawable)imageView1.getDrawable()).getBitmap();
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		largeIcon.compress(Bitmap.CompressFormat.JPEG,50/100,byteArrayOutputStream);
		return largeIcon;
	}

	public void printMultilingualFontEscEjapyArabic(int count, Voucher voucherforPrint, List<Item>itemforPrint) throws UnsupportedEncodingException {

//		if (count == 0) {
//			voucherforPrint = vouch1;
//			itemforPrint = items;
//		} else {
//			voucherforPrint = voucher;
//			itemforPrint = itemForPrint;
//		}


		int nLineWidth = 550;
		double total_Qty=0;

		try {

			String voucherTyp = "فاتورة بيع";
			switch (voucherforPrint.getVoucherType()) {
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

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			if(companyInfo.getLogo()!=null) {
				posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER,200);
				Log.e("12222print","pyyy");
			}
			posPtr.printAndroidFont(null,true,companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			if(companyInfo.getTaxNo()!=0)
				posPtr.printAndroidFont(null,true,"هاتف : " + companyInfo.getcompanyTel()+"    " + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			else{
				posPtr.printAndroidFont(null, true, "هاتف : " + companyInfo.getcompanyTel()  + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			}
			posPtr.printAndroidFont(null,true,"رقم الفاتورة : " + voucherforPrint.getVoucherNumber()+"    " + "          التاريخ: " + voucherforPrint.getVoucherDate() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"اسم  المندوب    : " +obj.getAllSettings().get(0).getSalesMan_name() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"اسم العميل   : " + voucherforPrint.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"ملاحظة        : " + voucherforPrint.getRemark() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"نوع الفاتورة : " +voucherTyp+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"طريقة الدفع  : " +  (voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا")+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				total_Qty=0;
				posPtr.printAndroidFont(null,true," رقم المادة                          " + "العدد" + "\t\t\t" + "الوزن" + "\t\t\t" +  "سعر الوحدة" + "\t\t\t" +"المجموع" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null,true,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();

						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
//						if (itemforPrint.get(i).getItemName().length() <= 12) {
//							String space = itemforPrint.get(i).getItemName();
//							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
//								space =  space+" " ;
//							}
//							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						} else {
//							String space = itemforPrint.get(i).getItemName().substring(0, 10);
////                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
////                            space = " " + space;
////                        }
//							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
//							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						}

						posPtr.printAndroidFont( null,true,itemforPrint.get(i).getItemNo() + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + itemforPrint.get(i).getItemName() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);




					}
				}
			} else {
				total_Qty=0;
				posPtr.printAndroidFont(  null,true," رقم الماده                          " + "العدد" + "\t\t\t" + "سعر الوحدة" + "\t\t\t" + "المجموع" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					if (voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber()) {
						total_Qty+=itemforPrint.get(i).getQty();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
//						posPtr.printBitmap(itemPrint(itemforPrint.get(i).getPrice()+"",convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))),itemforPrint.get(i).getQty()+"",itemforPrint.get(i).getItemName()),ESCPOSConst.LK_ALIGNMENT_CENTER,550);
						posPtr.printAndroidFont( null,true,itemforPrint.get(i).getItemNo() + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + itemforPrint.get(i).getItemName() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

					}
				}

			}

			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "اجمالي الكمية  : " + total_Qty + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "المجموع  : " + voucherforPrint.getSubTotal() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الخصم    : " + voucherforPrint.getTotalVoucherDiscount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الضريبة  : " + voucherforPrint.getTax() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "الصافي   : " + voucherforPrint.getNetSales() + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true,  "المستلم : ________________ التوقيع : __________" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			Log.e("IOException","posPtr=e="+e.getMessage());
			e.printStackTrace();
		}


	}

	public  void printMultilingualFontEscInventory(){
		Log.e("FontEscInventory","convertToImage_HEADER_Prin");
		try {
			double totalqty=0;
			posPtr.printBitmap(convertToImage_HEADER_Prin(),ESCPOSConst.LK_ALIGNMENT_CENTER,550);


			for(int i=0;i<itemsInventoryPrint.size();i++)
			{
				totalqty+=itemsInventoryPrint.get(i).getQty();

				posPtr.printBitmap(convertToImage_inventoryRow(itemsInventoryPrint.get(i)),ESCPOSConst.LK_ALIGNMENT_CENTER,550);
			}
			posPtr.printBitmap(convertToImage_inventoryFooter(totalqty),ESCPOSConst.LK_ALIGNMENT_CENTER,550);

		} catch (IOException e) {
			Log.e("IOException","posPtrprintBitmap=e="+e.getMessage());
			e.printStackTrace();
		}
//		posPtr.printBitmap(itemPrint(itemforPrint.get(i).getPrice()+"",convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))),itemforPrint.get(i).getQty()+"",itemforPrint.get(i).getItemName()),ESCPOSConst.LK_ALIGNMENT_CENTER,550);


	}



	private Bitmap convertToImage_inventoryFooter(double totalqty) {
		Log.e("inventoryFooter",""+totalqty);
		LinearLayout linearView = null;
		final Dialog dialog_Header = new Dialog(context);
		dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_Header.setCancelable(false);
		dialog_Header.setContentView(R.layout.header_layout_print);

		TextView total_qty   ;
		LinearLayout printFooter,mainLayout;
		printFooter=dialog_Header.findViewById(R.id.printFooter);
		mainLayout=dialog_Header.findViewById(R.id.ll);
		mainLayout.setVisibility(View.GONE);
		printFooter.setVisibility(View.VISIBLE);

		total_qty = (TextView) dialog_Header.findViewById(R.id.totalQty_Text);
		total_qty.setText(totalqty+"");

		linearView = (LinearLayout) dialog_Header.findViewById(R.id.printFooter);

		linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		linearView.layout(1, 1, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
		linearView.setDrawingCacheEnabled(true);
		linearView.buildDrawingCache();
		Bitmap bit =linearView.getDrawingCache();

		return bit;// creates bitmap and returns the same





	}
	private Bitmap convertToImage_inventoryRow(inventoryReportItem inventoryItem) {
		Log.e("inventoryItem", "width=" +inventoryItem.getName()+ " ");
		LinearLayout linearView = null;
		final Dialog dialog_Header = new Dialog(context);
		dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_Header.setCancelable(false);
		dialog_Header.setContentView(R.layout.print_inventory_row);
		CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
		TextView doneinsewooprint = (TextView) dialog_Header.findViewById(R.id.done);

		TextView textView_itemName, textView_itemQuantity, textView_itemNumber, largeName;

        textView_itemName = (TextView) dialog_Header.findViewById(R.id.textView_itemName);
		textView_itemQuantity = (TextView) dialog_Header.findViewById(R.id.textView_itemQuantity);
		textView_itemNumber = (TextView) dialog_Header.findViewById(R.id.textView_itemNumber);
		largeName = dialog_Header.findViewById(R.id.textView_itemName_large);
//		largeName.setVisibility(View.VISIBLE);
//		largeName.setText(inventoryItem.getName());

        if(inventoryItem.getName().length()>20)
        {
            largeName.setVisibility(View.VISIBLE);
            largeName.setText(inventoryItem.getName());
            textView_itemName.setVisibility(View.GONE);
        }
        else {
            largeName.setVisibility(View.GONE);
            textView_itemName.setText(inventoryItem.getName());
        }

		textView_itemQuantity.setText(inventoryItem.getQty()+"");

		textView_itemNumber.setText(inventoryItem.getItemNo());


//        dialog_Header.show();

		linearView = (LinearLayout) dialog_Header.findViewById(R.id.ll);

		linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		linearView.layout(1, 1, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

		Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

		linearView.setDrawingCacheEnabled(true);
		linearView.buildDrawingCache();
		Bitmap bit =linearView.getDrawingCache();

		return bit;// creates bitmap and returns the same


	}
	public void printMultilingualFontEscEjapy(int count, Voucher voucherforPrint, List<Item>itemforPrint) throws UnsupportedEncodingException {
	try{
		String CusId=(voucherforPrint.getCustNumber());
//		Log.e("HidPriceBluDBaseESC","CusId="+CusId);
		valueCheckHidPrice=obj.getHideValuForCustomer(CusId);
//		Log.e("HidPriceBluDBaseESC",""+valueCheckHidPrice);
//		valueCheckHidPrice=1;


	}catch(Exception e){
		valueCheckHidPrice=0;
		Log.e("HidPriceExceptionESC","*****"+e.getMessage());
	}
		valueCheckHidPrice=1;

		int nLineWidth = 550;
		double total_Qty=0;

		try {

			String voucherTyp = "Sales Invoice";
			switch (voucherforPrint.getVoucherType()) {
				case 504:
					voucherTyp = "Sales Invoice";
					break;
				case 506:
					voucherTyp = "Return Invoice";
					break;
				case 508:
					voucherTyp = "New Order";
					break;
			}

//			String salesmaname=obj.getSalesmanName(voucherforPrint.getCustNumber());
			String salesmaname= obj.getAllSettings().get(0).getSalesMan_name();
			if(salesmaname.equals(""))
			{
				salesmaname=obj.getSalesmanName_fromSalesTeam();
			}

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			if(companyInfo.getLogo()!=null) {
				posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER,250);
				Log.e("12222print","1pyyy");
			}

			posPtr.printAndroidFont(null,"\n"+companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,"Tel : " + companyInfo.getcompanyTel()+"    " + "Tax No: " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,true,"Voucher No :" + voucherforPrint.getVoucherNumber()+"    " + "          Date: " + voucherforPrint.getVoucherDate() /* + "\n" */  , nLineWidth, 25, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(null,"Store No. : " +  Login.salesMan/* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,"SalesMan Name :" +salesmaname /* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,"Customer Name :" + voucherforPrint.getCustName() /* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,"Remark : " + voucherforPrint.getRemark()/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,true,"Voucher Type : " +voucherTyp/* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,"Pay Method : " +  (voucherforPrint.getPayMethod() == 0 ? "Credit" : "Cash")/* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(null,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
				Log.e("12222print","2pyyy");
				total_Qty=0;
				posPtr.printAndroidFont(null," Item No.                          " + "QTY" + "\t\t\t" + "الوزن" + "\t\t\t" +  "Price" + "\t\t\t" +"Total" /* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null,"--------------------------------------------------------------------------------" /* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < itemforPrint.size(); i++) {
					if ((voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber())&& (itemforPrint.get(i).getVoucherType()== voucherforPrint.getVoucherType())) {
						total_Qty+=itemforPrint.get(i).getQty();

						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
//						if (itemforPrint.get(i).getItemName().length() <= 12) {
//							String space = itemforPrint.get(i).getItemName();
//							for (int g = 0; g < 12 - itemforPrint.get(i).getItemName().length(); g++) {
//								space =  space+" " ;
//							}
//							posPtr.printAndroidFont(null,true,space + "  " + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))))+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						} else {
//							String space = itemforPrint.get(i).getItemName().substring(0, 10);
////                        for (int g = 0; g < 16 - itemforPrint.get(i).getItemName().length(); g++) {
////                            space = " " + space;
////                        }
//							String fullString = itemforPrint.get(i).getItemName().substring(10, itemforPrint.get(i).getItemName().length() - 1);
//							posPtr.printAndroidFont( null,true,space + "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + fullString + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
//						}
						String space = itemforPrint.get(i).getItemNo();
						for (int g = 0; g < (20 - itemforPrint.get(i).getItemNo().length()); g++) {
							space =  space+" " ;
						}

						posPtr.printAndroidFont( null,space+ "\t\t\t" + itemforPrint.get(i).getUnit() + "\t\t\t\t" + itemforPrint.get(i).getQty() + "\t\t\t\t" + itemforPrint.get(i).getPrice() + "\t\t\t\t" +convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))) + "\n" + itemforPrint.get(i).getItemName() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);




					}
				}
			} else {
				if(valueCheckHidPrice==1){

					posPtr.printAndroidFont(  null," Item No.                   " + " Qty" + "\t\t\t" /* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}
				else{				posPtr.printAndroidFont(  null," Item No.                   " + " Qty" + "\t\t\t" + " Price" + "\t\t\t\t" + " Total" /* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				}
				total_Qty=0;
				posPtr.printAndroidFont(  null,"--------------------------------------------------------------------------------" /* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				for (int i = 0; i < itemforPrint.size(); i++) {
					Log.e("12222print","3pyyy"+itemforPrint.size());
					if ((voucherforPrint.getVoucherNumber() == itemforPrint.get(i).getVoucherNumber())&& (itemforPrint.get(i).getVoucherType()== voucherforPrint.getVoucherType())) {
						total_Qty+=itemforPrint.get(i).getQty();
						String amount = "" + (itemforPrint.get(i).getQty() * itemforPrint.get(i).getPrice() - itemforPrint.get(i).getDisc());
						String amounts="";
						try {
						 amounts=convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount))));
					}catch (Exception e){
							Log.e("Exception","amounts"+e.getMessage());
						}

//						posPtr.printBitmap(itemPrint(itemforPrint.get(i).getPrice()+"",convertToEnglish(decimalFormat.format(Double.valueOf(convertToEnglish(amount)))),itemforPrint.get(i).getQty()+"",itemforPrint.get(i).getItemName()),ESCPOSConst.LK_ALIGNMENT_CENTER,550);
						String space = itemforPrint.get(i).getItemNo();
							for (int g = 0; g < (15 - itemforPrint.get(i).getItemNo().length()); g++) {
								space =  space+"  " ;
							}
//
						String Qty = ""+itemforPrint.get(i).getQty();
						for (int a = 0; a < (6 - (""+itemforPrint.get(i).getQty()).length()); a++) {
							Qty =  Qty+"  " ;
						}
//
						String price = ""+itemforPrint.get(i).getPrice();
						for (int a = 0; a < (6 - (""+itemforPrint.get(i).getPrice()).length()); a++) {
							price =  price+"  " ;
						}

					String sMounts=amounts;
						for (int a = 0; a < (12 - (amounts).length()); a++) {
							sMounts =  sMounts+"  " ;
						}
						if(valueCheckHidPrice==1){
//							posPtr.printAndroidFont( null,space  + "\t\t" + Qty + "\t\t" +"\n" + itemforPrint.get(i).getItemName() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
							posPtr.printAndroidFont( null,space  + "\t\t" + Qty + "\t\t" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
							posPtr.printAndroidFont( null, "\t\t"+  itemforPrint.get(i).getItemName().toString() , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						}
						else{
//							posPtr.printAndroidFont( null,space  + "\t\t" + Qty + "\t\t" + price + "\t\t" +sMounts + "\n" + itemforPrint.get(i).getItemName() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
							posPtr.printAndroidFont( null,space  + "\t\t" + Qty + "\t\t" + price + "\t\t" +sMounts , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
							posPtr.printAndroidFont( null, itemforPrint.get(i).getItemName().toString(), nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

						}

					}
				}

			}
			//Log.e("getArabicString",""+getArabicString(voucherforPrint.getSubTotal()+""));


			Log.e("12222print","4pyyy");
			posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null, "Total Qty  : " + total_Qty/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			if(valueCheckHidPrice==0){
				posPtr.printAndroidFont(  null, "Total         : " + voucherforPrint.getSubTotal()/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Discount  : " + voucherforPrint.getTotalVoucherDiscount() /* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Tax            : " + voucherforPrint.getTax() /* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Net Total  :" + voucherforPrint.getNetSales()  + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			}

			posPtr.printAndroidFont(  null, "I received the goods complete and in good condition and free from any defects and I pledge to pay the value of this invoice." /* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			if(obj.getAllSettings().get(0).getTafqit()==1 && (valueCheckHidPrice!=1))
            {
                posPtr.printAndroidFont(  null, "I received : " +   getArabicString(voucherforPrint.getNetSales()+"")/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

            }
			Log.e("12222print","5pyyy");
			posPtr.printAndroidFont(  null, "" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,  "Recipient : ____________  Signature : __________" + "\n"  , nLineWidth, 22, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (Exception e) {
			Log.e("ExceptionposPtr",""+e.getMessage());
			e.printStackTrace();
		}


	}

	Bitmap itemPrint (String prices,double totals,String qtys,String items,float discount,String itemNo,double taxValue,float bonus){


	final Dialog dialogs = new Dialog(context);
	dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialogs.setCancelable(true);
	dialogs.setContentView(R.layout.tabres);
	int textSize=16;
	if(printerType==6)
	{
//		textSize=20;
	}

	TextView price,total,qty,item,item_largeName,item_discount,item_bonus;

	price=(TextView)dialogs.findViewById(R.id.price);
	total=(TextView)dialogs.findViewById(R.id.total);
	qty=(TextView)dialogs.findViewById(R.id.qty);
	item=(TextView)dialogs.findViewById(R.id.ittem);
	item_discount=(TextView)dialogs.findViewById(R.id.item_discount);
	item_largeName=(TextView)dialogs.findViewById(R.id.ittem_largeName);
		item_bonus=(TextView)dialogs.findViewById(R.id.item_bonus);
	LinearLayout linearView=(LinearLayout)dialogs.findViewById(R.id.tab);
	if (printerType == 6||printerType==7) {

		item_bonus.setVisibility(View.GONE);
	}
		item_bonus.setText(""+bonus);
//	prices=convertToEnglish(decimalFormat.format(prices));
//		totals=convertToEnglish(decimalFormat.format(totals));
//		*********************** here
		String discountStr=convertToEnglish(decimalFormat.format(discount));
//		prices=convertToEnglish(decimalFormat.format(prices));
	price.setText(prices);
	price.setTextSize(textSize);
	try {
		if(noTax==0){
			if(taxCalc==1)
			total.setText(convertToEnglish(decimalFormat.format((totals-taxValue)))+"");
			else total.setText(convertToEnglish(decimalFormat.format((totals))));
		}else {
			total.setText(convertToEnglish(decimalFormat.format(totals)+""));
		}
	}catch (Exception e){
		total.setText(convertToEnglish(decimalFormat.format(totals)+""));
		Log.e("IOException","printtotal=e="+e.getMessage());
	}


	total.setTextSize(textSize);
	qty.setText(qtys);
	qty.setTextSize(textSize);
	Log.e("item_discount",""+discount);

	Log.e("itemPrint",""+items.length()+"\t items="+items);
//	if(items.length()>19||printerType==6)
//	{

		item_largeName.setVisibility(View.VISIBLE);
		if(items.contains("(bonus)")) {
			String itemName_str=obj.getItemName(itemNo);
			Log.e("itemName_str",""+itemName_str);
			item_largeName.setText(itemName_str+"\t"+"مجاني");
		}else
		item_largeName.setText(items);
		item_largeName.setTextSize(textSize);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			item_largeName.setTextDirection(View.TEXT_DIRECTION_LTR);
		}
		item.setText("item name");
		item.setVisibility(View.GONE);
		item_discount.setText(discountStr);
		item_discount.setTextSize(textSize);
		Log.e("itemPrint",""+items.length()+items);
//	}
//	else {
//		if(items.contains("(bonus)"))
//		{
//			item.setText("مجاني");
//			item_largeName.setVisibility(View.GONE);
//			item.setVisibility(View.VISIBLE);
//			item.setTextSize(textSize);
//			item_discount.setText(discountStr);
//			item_discount.setTextSize(textSize);
//		}else {
//			item_largeName.setVisibility(View.GONE);
//			item.setVisibility(View.VISIBLE);
//			item.setText(items);
//			item.setTextSize(textSize);
//			item_discount.setText(discountStr);
//			item_discount.setTextSize(textSize);
//		}
//
//	}

//		item.setText(items);
	linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
			View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
	linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

	Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

		Bitmap bitmap=null;
		try {
			bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Drawable bgDrawable = linearView.getBackground();
			if (bgDrawable != null) {
				bgDrawable.draw(canvas);
			} else {
				canvas.drawColor(Color.WHITE);
			}
			linearView.draw(canvas);
		}catch (Exception e){
			Log.e("Exception","printebitmap="+e.getMessage());
		}

	return bitmap;

}
	public String getCurentTimeDate(int flag){
		String dateCurent,timeCurrent,dateTime="";
		Date currentTimeAndDate;
		SimpleDateFormat dateFormat, timeformat;
		currentTimeAndDate = Calendar.getInstance().getTime();
		if(flag==1)// return date
		{

			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateCurent = dateFormat.format(currentTimeAndDate);
			dateTime=convertToEnglish(dateCurent);

		}
		else {
			if(flag==2)// return time
			{
				timeformat = new SimpleDateFormat("hh:mm");
				dateCurent = timeformat.format(currentTimeAndDate);
				dateTime=convertToEnglish(dateCurent);
			}
		}
		return dateTime;

	}
	 Bitmap convertToImage_HEADER_Prin() {
		LinearLayout linearView = null;
		final Dialog dialog_Header = new Dialog(context);
		dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_Header.setCancelable(false);
		dialog_Header.setContentView(R.layout.header_layout_print);
		CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
		Date currentTimeAndDate;
		SimpleDateFormat df, df2;
		String voucherDate, voucherYear;
		df = new SimpleDateFormat("dd/MM/yyyy");
		currentTimeAndDate = Calendar.getInstance().getTime();
		voucherDate = df.format(currentTimeAndDate);
		voucherDate = convertToEnglish(voucherDate);
		TextView doneinsewooprint = (TextView) dialog_Header.findViewById(R.id.done);

		TextView compname, tel, taxNo, salesName  ,date,note,qtyTypeText   ;
		ImageView img = (ImageView) dialog_Header.findViewById(R.id.img);
		compname = (TextView) dialog_Header.findViewById(R.id.compname);
		tel = (TextView) dialog_Header.findViewById(R.id.tel);
		taxNo = (TextView) dialog_Header.findViewById(R.id.taxNo);
		qtyTypeText=(TextView) dialog_Header.findViewById(R.id.qtyTypeText);
		LinearLayout printFooter;
		printFooter=dialog_Header.findViewById(R.id.printFooter);
		 linearView=dialog_Header.findViewById(R.id.ll);
		 linearView.setVisibility(View.VISIBLE);
		printFooter.setVisibility(View.INVISIBLE);
		date = (TextView) dialog_Header.findViewById(R.id.date);
		note = (TextView) dialog_Header.findViewById(R.id.note);

		date.setText(voucherDate+"");
		note.setText(companyInfo.getNoteForPrint());
		salesName = (TextView) dialog_Header.findViewById(R.id.salesman_name);

		if (companyInfo.getLogo()!=(null))
		{
			img.setImageBitmap(companyInfo.getLogo());
		}
		else{img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_foreground));}
		compname.setText(companyInfo.getCompanyName());
		tel.setText("" + companyInfo.getcompanyTel());
		 if(companyInfo.getTaxNo()!=0)
		taxNo.setText("" + companyInfo.getTaxNo());
		qtyTypeText.setText(typeQty+"");
		 String salesmanName= obj.getAllSettings().get(0).getSalesMan_name();
		 if(salesmanName.equals(""))
		 {
			 salesmanName=obj.getSalesmanName_fromSalesTeam();
		 }
		salesName.setText(salesmanName);

		//***************************************
		 //		item.setText(items);
		 linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				 View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		 linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

		 Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

		 Bitmap bitmap=null;
		 try {
			 bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Drawable bgDrawable = linearView.getBackground();
			if (bgDrawable != null) {
				bgDrawable.draw(canvas);
			} else {
				canvas.drawColor(Color.WHITE);
			}
			linearView.draw(canvas);
		}catch (Exception e){
			Log.e("Exception","printedraw="+e.getMessage());
		}

		 return bitmap;
		 //**************************************


	}


	public void printMultilingualFontPayCash(int count) throws UnsupportedEncodingException {
		try {
			List<PrinterSetting> printerSettings = obj.getPrinterSetting_();

			if (printerSettings.size() != 0) {
				printerType = printerSettings.get(0).getPrinterName();
				Log.e("printerType",""+printerType);
			}
		}catch (Exception e)
		{
			printerType=5;
			Log.e("IOException","printerType=e="+e.getMessage());
		}



//		if (count == 0) {
//			voucherforPrint = vouch1;
//			itemforPrint = items;
//		} else {
//			voucherforPrint = voucher;
//			itemforPrint = itemForPrint;
//		}
		int nLineWidth = 550;// 550
		int alignment=0;
		String line="";
		String headerVoucher="";
		if (printerType == 6||printerType==7) {
			alignment=ESCPOSConst.LK_ALIGNMENT_LEFT;
			nLineWidth=370;
			headerVoucher=" القيمة  | " + "  التاريخ | " + " رقم الشيك | " + "   البنك  ";

			line="-------------------------------------------------------";
		}
		else {
			nLineWidth=550;
			headerVoucher="      القيمة    " + "   التاريخ      " + "     رقم الشيك          " + "   البنك    ";

			alignment=ESCPOSConst.LK_ALIGNMENT_LEFT;
			line="--------------------------------------------------------------------------------";

		}

		if(count==0)
		{
			payList=paymentsforPrint;
			payforBank=ReceiptVoucher.payment;

		}else{
			payList=paymentPrinter;
			payforBank=PrintPayment.pay1;
			Log.e("Pay 0000 ==>",""+pay1.getPayMethod());
			Log.e("payforBank 0000 ==>",""+payforBank.getPayMethod());
		}


		try {

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			if(companyInfo.getLogo()!=null) {
				posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER, 200);
			}
			if (payforBank.getPayMethod() == 1||payforBank.getPayMethod() == 2) {
				posPtr.printAndroidFont(  null,true, companyInfo.getCompanyName() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				if (printerType == 6||printerType==7) {
					posPtr.printAndroidFont(  null,true, "هاتف : " + companyInfo.getcompanyTel() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					if(companyInfo.getTaxNo()!=0)
					posPtr.printAndroidFont(  null,true,  "الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					posPtr.printAndroidFont(  null,true, line + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					posPtr.printAndroidFont(  null,true, " سند قبض "+ "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(  null,true, "رقم السند: " + payforBank.getVoucherNumber()+"\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					posPtr.printAndroidFont(  null,true, "التاريخ: " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}
				else {
					posPtr.printAndroidFont(  null,true, "هاتف : " + companyInfo.getcompanyTel() +"    "+ "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(  null,true, line + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(  null,true, " سند قبض "+ "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(  null,true, "رقم السند: " + payforBank.getVoucherNumber()+"        " + "التاريخ: " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				}

				posPtr.printAndroidFont(  null,true, "وصلني من السيد/السادة: " +payforBank.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "ملاحظة: " + payforBank.getRemark() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "المبلغ المقبوض: " + payforBank.getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "طريقة الدفع: " + (payforBank.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			} else {
				posPtr.printAndroidFont(  null,true, companyInfo.getCompanyName() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				if (printerType == 6||printerType==7) {
					posPtr.printAndroidFont(  null,true, "هاتف : " + companyInfo.getcompanyTel() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					if(companyInfo.getTaxNo()!=0)
					posPtr.printAndroidFont(  null,true,  "الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					posPtr.printAndroidFont(  null,true, line + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					posPtr.printAndroidFont(  null,true, " سند قبض "+ "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(  null,true, "رقم السند: " + payforBank.getVoucherNumber()+"\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					posPtr.printAndroidFont(  null,true, "التاريخ: " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}
				else {
					if(companyInfo.getTaxNo()!=0)
						posPtr.printAndroidFont(  null,true, "هاتف : " + companyInfo.getcompanyTel() +"    "+ "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					else{
						posPtr.printAndroidFont(null, true, "هاتف : " + companyInfo.getcompanyTel()  + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

					}
					posPtr.printAndroidFont(  null,true, line + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(  null,true, " سند قبض "+ "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
					posPtr.printAndroidFont(  null,true, "رقم السند: " + payforBank.getVoucherNumber()+"        " + "التاريخ: " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				}
				posPtr.printAndroidFont(  null,true, "وصلني من السيد/السادة: " +payforBank.getCustName() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "ملاحظة: " + payforBank.getRemark() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "المبلغ المقبوض: " + payforBank.getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, "طريقة الدفع: " + (payforBank.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null,true, line + "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				if( textContainsArabic(payList.get(0).getBank())){
					Log.e("textContainsArabic",""+ textContainsArabic(payList.get(0).getBank()));
					headerVoucher="       البنك   " + "   رقم الشيك       " + "      التاريخ       " + "      القيمة   ";

					posPtr.printAndroidFont(  null,true, headerVoucher +  "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);// test Zaid

				}else {
//					Log.e("textContainsArabic","Else"+ textContainsArabic(payList.get(0).getBank()));

					posPtr.printAndroidFont(  null,true, headerVoucher+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}
				posPtr.printAndroidFont(  null,true, line + "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


				for (int i = 0; i < payList.size(); i++) {
					if (printerType == 6||printerType==7) {
//						if (payList.get(i).getBank().length() <= 12) {
							String space = payList.get(i).getBank();
//							for (int g = 0; g < 12 - payList.get(i).getBank().length(); g++) {
//								space += "\t";
//							}//"\t\t\t\t" +
							posPtr.printAndroidFont(  null,true, payList.get(i).getBank()+  "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

							posPtr.printAndroidFont(  null,true,  "\t"+payList.get(i).getCheckNumber()+"\t\t"+ payList.get(i).getDueDate()+"\t\t" + payList.get(i).getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

//                    dataArabic += "\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space + "\n";
//						} else {
//							String space = payList.get(i).getBank().substring(0, 10);
////                    for (int g = 0; g <  payList.get(i).getBank().length()-12; g++) {
////                        space+= "\t" ;
////                    }
//							String fullString = payList.get(i).getBank().substring(10, payList.get(i).getBank().length() - 1);
//							posPtr.printAndroidFont(  null,true, "\t"+space +"\t\t\t"+ payList.get(i).getCheckNumber() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t" + payList.get(i).getAmount() + "\n" + fullString + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
////                    dataArabic +=   "\n\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space +fullString + "\n";
//						}
					}
					else {
						if (payList.get(i).getBank().length() <= 12) {
							String space = payList.get(i).getBank();
							for (int g = 0; g < 12 - payList.get(i).getBank().length(); g++) {
								space += "\t";
							}//"\t\t\t\t" +
							posPtr.printAndroidFont(  null,true, "\t"+space+"\t\t"+  payList.get(i).getCheckNumber()+"\t\t\t\t"+ payList.get(i).getDueDate()+"\t\t\t" + payList.get(i).getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

//                    dataArabic += "\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space + "\n";
						} else {
							String space = payList.get(i).getBank().substring(0, 10);
//                    for (int g = 0; g <  payList.get(i).getBank().length()-12; g++) {
//                        space+= "\t" ;
//                    }
							//String fullString = payList.get(i).getBank().substring(10, payList.get(i).getBank().length() - 1);
							posPtr.printAndroidFont(  null,true, "\t"+payList.get(i).getBank()  +"\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                            posPtr.printAndroidFont(  null,true,  payList.get(i).getAmount()+ "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t" +payList.get(i).getCheckNumber()   + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

//                    dataArabic +=   "\n\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space +fullString + "\n";
						}
					}


				}


			}
			posPtr.lineFeed(2);
            if(obj.getAllSettings().get(0).getTafqit()==1&&valueCheckHidPrice!=1 )
            {
                posPtr.printAndroidFont(  null, true,"استلمت : " +   getArabicString( payforBank.getAmount() +"")/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

            }
			posPtr.printAndroidFont(  null,true, line + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
			posPtr.printAndroidFont(  null,true, "   المستلم ---------------           "  +"\n"+" التوقيع --------------               " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IOException","valueCheckHidPrice=e="+e.getMessage());
		}


	}
	public void printMultilingualFontPayCash_EJABI(int count) throws UnsupportedEncodingException {

		if(count==0)
		{
			payList=paymentsforPrint;
			payforBank=ReceiptVoucher.payment;

		}else{
			payList=paymentPrinter;
			payforBank=PrintPayment.pay1;
			Log.e("Pay 0000 ==>",""+pay1.getPayMethod());
			Log.e("payforBank 0000 ==>",""+payforBank.getPayMethod());
		}
//		String salesmaname=obj.getSalesmanName(voucher.getCustNumber());
		String salesmaname= obj.getAllSettings().get(0).getSalesMan_name();
		if(salesmaname.equals(""))
		{
			salesmaname=obj.getSalesmanName_fromSalesTeam();
		}

		int nLineWidth = 550;
		try {

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			if(companyInfo.getLogo()!=null) {
				posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER, 250);
			}
			if (payforBank.getPayMethod() == 1||payforBank.getPayMethod()==2) {
				posPtr.printAndroidFont(  null, companyInfo.getCompanyName() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null, "Tel No :" + companyInfo.getcompanyTel() +"    "+ "Tax No :" + companyInfo.getTaxNo() /*+ "\n"*/, nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null, (payforBank.getPayMethod() == 1? "Cash Receipt":"Credit Receipt")+ "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				posPtr.printAndroidFont(  null, "Receipt No:" + payforBank.getVoucherNumber()+"        " + "Date : " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null, "Store No :" + Login.salesMan /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Sales Man:" + salesmaname/*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				Log.e("printAndroidFont",""+salesmaname);
				Log.e("printAndroidFont",""+obj.getAllSettings().get(0).getSalesMan_name());
				posPtr.printAndroidFont(  null, "I received from Mr. / Messrs:" +payforBank.getCustName() /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Remark :" + payforBank.getRemark() /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Amount received: " + payforBank.getAmount() /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Payment Method: " + (payforBank.getPayMethod() == 1 ? "Cash" : "Credit") /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			} else {// 0 paymethod
				posPtr.printAndroidFont(  null, companyInfo.getCompanyName() +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null, "Tel No :" + companyInfo.getcompanyTel() +"    "+ "    Tax No :" + companyInfo.getTaxNo() /*+ "\n"*/, nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				posPtr.printAndroidFont(  null, "cheque Receipt"+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				posPtr.printAndroidFont(  null, "Receipt No: " + payforBank.getVoucherNumber()+"        " + "Date : " + payforBank.getPayDate() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null, "Store No :" + Login.salesMan /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Sales Man:" + salesmaname /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "I received from Mr. / Messrs: " +payforBank.getCustName()/*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Remark :" + payforBank.getRemark() /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Amount received: " + payforBank.getAmount() /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "Payment Method: " + (payforBank.getPayMethod() == 1 ? "Cash" : "Cheque") /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" /*+ "\n"*/ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//				posPtr.printAndroidFont(  null,true, "        Value     " + "      Date      " + "   Cheque No          " + "  Bank Name   " + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "        Cheque No        " + "      Date      " + "         Value       "  + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" + "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


//				for (int i = 0; i < payList.size(); i++) {
//
//					if (payList.get(i).getBank().length() <= 12) {
//						String space = payList.get(i).getBank();
//						for (int g = 0; g < 12 - payList.get(i).getBank().length(); g++) {
//							space += "\t";
//						}//"\t\t\t\t" +
//						posPtr.printAndroidFont(  null,true, "\t\t"+space+ payList.get(i).getCheckNumber()+"\t\t\t\t"+ payList.get(i).getDueDate()+"\t\t\t" + payList.get(i).getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//
////                    dataArabic += "\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space + "\n";
//					} else {
//						String space = payList.get(i).getBank().substring(0, 10);
////                    for (int g = 0; g <  payList.get(i).getBank().length()-12; g++) {
////                        space+= "\t" ;
////                    }
//						String fullString = payList.get(i).getBank().substring(10, payList.get(i).getBank().length() - 1);
//						posPtr.printAndroidFont(  null,true, "\t\t"+space +"\t\t\t"+ payList.get(i).getCheckNumber() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t" + payList.get(i).getAmount() + "\n" + fullString + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
////                    dataArabic +=   "\n\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space +fullString + "\n";
//					}
//				}


				for (int i = 0; i < payList.size(); i++) {

					if ((""+payList.get(i).getCheckNumber()).length() <= 20) {
						String space = ""+payList.get(i).getCheckNumber();
						for (int g = 0; g < 20 - (""+payList.get(i).getCheckNumber()).length(); g++) {
							space += " ";
						}//"\t\t\t\t" +
						posPtr.printAndroidFont(  null, "\t\t"+space+"\t\t\t"+ payList.get(i).getDueDate()+"\t\t\t" + payList.get(i).getAmount() + "\n" +payList.get(i).getBank()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

//                    dataArabic += "\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space + "\n";
					} else {
						String space = (""+payList.get(i).getCheckNumber()).substring(0, 20);
//                    for (int g = 0; g <  payList.get(i).getBank().length()-12; g++) {
//                        space+= "\t" ;
//                    }
						String fullString = (""+payList.get(i).getCheckNumber()).substring(20, payList.get(i).getBank().length() - 1);
						posPtr.printAndroidFont(  null, "\t\t"+space +"\t\t\t"+ payList.get(i).getDueDate() + "\t\t\t" + payList.get(i).getAmount() + "\n" + fullString + "\n"+ payList.get(i).getBank(), nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//                    dataArabic +=   "\n\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space +fullString + "\n";
					}
				}


			}
			posPtr.lineFeed(2);
            if(obj.getAllSettings().get(0).getTafqit()==1 && valueCheckHidPrice!=1)
            {
                posPtr.printAndroidFont(  null, "I receved \t\t" +   getArabicString( payforBank.getAmount() +"")/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

            }
			posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.printAndroidFont(  null, "          Recipient ---------------            Signature --------------         " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			posPtr.lineFeed(4);
		} catch (IOException e) {
			Log.e("IOException","Recipient=e="+e.getMessage());
			e.printStackTrace();
		}


	}

	public void printMultilingualFontStock_EJABI(int count) throws UnsupportedEncodingException {
		try {
			List<PrinterSetting> printerSettings = obj.getPrinterSetting_();

			if (printerSettings.size() != 0) {
				printerType = printerSettings.get(0).getPrinterName();
				Log.e("printerType",""+printerType);
			}
		}catch (Exception e)
		{
			printerType=5;
			Log.e("IOException","printerType=e="+e.getMessage());
		}

		if(count==0) {
			itemList=listItemStock;
			voucherStockItems=voucherStockItem;
		}
//		}else{
//			payList=paymentPrinter;
//			payforBank=PrintPayment.pay1;
//			Log.e("Pay 0000 ==>",""+pay1.getPayMethod());
//			Log.e("payforBank 0000 ==>",""+payforBank.getPayMethod());
//		}

		int nLineWidth = 550;
		double total_Qty=0;
		int font=(printerType==6) ? 20:24 ;
		try {
			CompanyInfo companyInfo = null;
			posPtr.setAsync(false);
			if(obj.getAllCompanyInfo().size()!=0){
				 companyInfo = obj.getAllCompanyInfo().get(0);
				if(companyInfo.getLogo()!=null) {
					posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER, 250);
				}
			}

//			String salesmaname=obj.getSalesmanName(voucher.getCustNumber());
//			if(salesmaname.equals("")){
//				salesmaname=obj.getSalesmanName_fromSalesTeam();
//			}
			String salesmaname= obj.getAllSettings().get(0).getSalesMan_name();
			if(salesmaname.equals(""))
			{
				salesmaname=obj.getSalesmanName_fromSalesTeam();
			}


				posPtr.setAsync(false);
//				if(companyInfo.getLogo()!=null) {
//					posPtr.printBitmap(companyInfo.getLogo(), ESCPOSConst.LK_ALIGNMENT_CENTER,250);
//					Log.e("12222print","pyyy");
//				}
				posPtr.printAndroidFont(null,"\n"+companyInfo.getCompanyName()+"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			if (printerType == 6||printerType==7) {
					posPtr.printAndroidFont(null,true,"Voucher No :" + voucherStockItems.getVoucherNumber() , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
					posPtr.printAndroidFont(null,true,"Date: " + voucherStockItems.getVoucherDate() /* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}
				else {
					posPtr.printAndroidFont(null,true,"Voucher No :" + voucherStockItems.getVoucherNumber()+"    " + "          Date: " + voucherStockItems.getVoucherDate() /* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				}
				posPtr.printAndroidFont(null,"--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                posPtr.printAndroidFont(null,"Stock Request" + "\n"   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                posPtr.printAndroidFont(null,"Store No. : " +  Login.salesMan/* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null,"SalesMan Name :" +salesmaname/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null,"Remark : " + voucherStockItems.getRemark()/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//				posPtr.printAndroidFont(null,"Pay Method : " +  (voucherStockItems.getPayMethod() == 0 ? "Credit" : "Cash")/* + "\n" */  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null,"--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

				if(printerType==6)

				{
					posPtr.printAndroidFont(null,"        Item No                               Qty      ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}else {
					posPtr.printAndroidFont(null,"        Item No                                       Qty         ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				}
			posPtr.printAndroidFont(null,"--------------------------------------------------------------------------------"  +"\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			Log.e("itemList",""+itemList.size());

				for (int i = 0; i < itemList.size(); i++) {

					total_Qty+=itemList.get(i).getQty();
					if ((""+itemList.get(i).getItemNo()).length() <= 40) {
						String space = ""+itemList.get(i).getItemNo();
						for (int g = 0; g < 20 - (""+itemList.get(i).getItemNo()).length(); g++) {
							space += " ";
						}//"\t\t\t\t" +
						posPtr.printAndroidFont(  null, "\t\t"+space+"\t\t\t\t\t\t\t\t"+ itemList.get(i).getQty()+ "\n" +itemList.get(i).getItemName()+"\n", nLineWidth, font, ESCPOSConst.LK_ALIGNMENT_LEFT);

//                    dataArabic += "\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space + "\n";
					} else {
						String space = (""+itemList.get(i).getItemNo()).substring(0, 20);
//                    for (int g = 0; g <  payList.get(i).getBank().length()-12; g++) {
//                        space+= "\t" ;
//                    }
						String fullString = (""+itemList.get(i).getItemNo()).substring(20, itemList.get(i).getItemNo().length() - 1);
						posPtr.printAndroidFont(  null, "\t\t"+space +"\t\t\t\t\t\t\t\t"+ itemList.get(i).getQty() +  "\n" + fullString + "\n"+ itemList.get(i).getItemName(), nLineWidth, font, ESCPOSConst.LK_ALIGNMENT_LEFT);
//                    dataArabic +=   "\n\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space +fullString + "\n";
					}
				}

			posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			posPtr.printAndroidFont(  null, "Total Qty : " + total_Qty/* + "\n" */ , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);


//			posPtr.lineFeed(2);
			posPtr.printAndroidFont(  null, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
//			posPtr.printAndroidFont(  null, "          Recipient ---------------            Signature --------------         " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

			posPtr.lineFeed(4);
			clearData.setText("1");
			listItemStock.clear();
			totalQty.setText("0.00");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IOException","printertotalQty=e="+e.getMessage());
		}


	}

	public void printMultilingualFontCashReport() throws UnsupportedEncodingException {
		try {
			List<PrinterSetting> printerSettings = obj.getPrinterSetting_();

			if (printerSettings.size() != 0) {
				printerType = printerSettings.get(0).getPrinterName();
				Log.e("printerType",""+printerType);
			}
		}catch (Exception e)
		{
			printerType=5;
		}

		int nLineWidth = 550;// 550
		int alignment=0;
		String line="";
		String headerVoucher="";
		if (printerType == 6||printerType==7) {
			alignment=ESCPOSConst.LK_ALIGNMENT_LEFT;
			nLineWidth=370;
			headerVoucher=" القيمة  | " + "  التاريخ | " + " رقم الشيك | " + "   البنك  ";

			line="-------------------------------------------------------";
		}
		else {
			nLineWidth=550;
			headerVoucher="      القيمة    " + "   التاريخ      " + "     رقم الشيك          " + "   البنك    ";

			alignment=ESCPOSConst.LK_ALIGNMENT_LEFT;
			line="--------------------------------------------------------------------------------";

		}
		try {

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			try {
                posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);
            }
			catch (Exception e)
            {
				Log.e("IOException","printerprintBitmap=e="+e.getMessage());
            }


			String companney_name="";
			decimalFormat = new DecimalFormat("##.00");
			String dataArabic_Report="";
			try {
				//total_cash=net+cash-returnCash;

				if (companyInfo.getCompanyName().equals("")) {
					companney_name = "Companey";
					//Please fill  the companey name
//					Toast.makeText(context, R.string.fill_name, Toast.LENGTH_SHORT).show();
				} else {
					companney_name = companyInfo.getCompanyName();
				}
				String salesmanName= obj.getAllSettings().get(0).getSalesMan_name();
				if(salesmanName.equals(""))
				{
					salesmanName=obj.getSalesmanName_fromSalesTeam();
				}

				posPtr.printAndroidFont(null, true, companney_name + "\n", nLineWidth, 25, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(null, true, "رقم المستودع " + Login.salesMan + "\n", nLineWidth, 25, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, "اسم المندوب " + salesmanName + "\n", nLineWidth, 25, ESCPOSConst.LK_ALIGNMENT_LEFT);

				if(companyInfo.getTaxNo()!=0)
				posPtr.printAndroidFont(null, true, "  الرقم الضريبي :  " + companyInfo.getTaxNo() + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null, true, "التاريخ  : " + date.getText() + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, line + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(null, true, "المبيعات نقدا :     " + convertToEnglish(decimalFormat.format((cash - returnCash))) + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, "المبيعات ذمم :     " + convertToEnglish(decimalFormat.format((credit - returnCridet))) + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, "المرتجع نقدا : " +convertToEnglish(decimalFormat.format(( returnCash))) + " \n ",  nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, "المرتجع ذمم  : " + convertToEnglish(decimalFormat.format((returnCridet))) + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(null, true, "إجمالي المبيعات :     " + convertToEnglish(decimalFormat.format(total)) + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, line + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(null, true, "الدفع نقدا :     " + convertToEnglish(decimalFormat.format(cashPayment)) + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, "الدفع شيك :     " + convertToEnglish(decimalFormat.format(creditPayment)) + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
				posPtr.printAndroidFont(null, true, "الاجمالي :     " + convertToEnglish(decimalFormat.format(net)) + " \n ", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

				posPtr.printAndroidFont(  null,true,"اجمالي البطاقة الائتمانية :     " + convertToEnglish(decimalFormat.format(creditCardPayment)) + " \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//				"اجمالي البطاقة الائتمانية :     " + convertToEnglish(decimalFormat.format(creditCardPayment)) + " \n " +
				posPtr.printAndroidFont(  null,true,line + " \n "   , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
				posPtr.printAndroidFont(  null,true,"اجمالي المقبوضات :     " + convertToEnglish(decimalFormat.format(total_cash)) + " \n\n \n "  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

			}catch (Exception e){
				Log.e("IOException","printerprintBitmaptotal_cash=e="+e.getMessage());
//				Toast.makeText(context, R.string.error_companey_info, Toast.LENGTH_SHORT).show();
			}

			posPtr.printAndroidFont(  null,true, line+ "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			Log.e("IOException","printerprintBitmaptotal_cash2=e="+e.getMessage());
			e.printStackTrace();
		}


	}


	public String convertToEnglish(String value) {
		String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
		return newValue;
	}


	public boolean ifEnglish(char value) {

for(int i=0;i<28;i++){
	if(((97+i)==((int)value))||((65+i)==((int)value)) ){
		return true;
	}
}
		return false;
	}


	public Bitmap convertLayoutToImageW(Context context) {
		LinearLayout linearView = null;

		final Dialog dialogs = new Dialog(context);
		dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogs.setCancelable(false);
		dialogs.setContentView(R.layout.printdialog);
//            fill_theVocher( voucherPrint);

		TextView	doneinsewooprint;

		CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
		doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

		TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;
		ImageView img = (ImageView) dialogs.findViewById(R.id.img);

		compname = (TextView) dialogs.findViewById(R.id.compname);
		tel = (TextView) dialogs.findViewById(R.id.tel);
		taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
		vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
		date = (TextView) dialogs.findViewById(R.id.date);
		custname = (TextView) dialogs.findViewById(R.id.custname);
		note = (TextView) dialogs.findViewById(R.id.note);
		vhType = (TextView) dialogs.findViewById(R.id.vhType);
		paytype = (TextView) dialogs.findViewById(R.id.paytype);
		total = (TextView) dialogs.findViewById(R.id.total);
		discount = (TextView) dialogs.findViewById(R.id.discount);
		tax = (TextView) dialogs.findViewById(R.id.tax);
		ammont = (TextView) dialogs.findViewById(R.id.ammont);
		textW = (TextView) dialogs.findViewById(R.id.wa1);
		TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
//



		String voucherTyp = "";
		switch (voucherforPrint.getVoucherType()) {
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
		img.setImageBitmap(companyInfo.getLogo());
		compname.setText(companyInfo.getCompanyName());
		tel.setText("" + companyInfo.getcompanyTel());
		if(companyInfo.getTaxNo()!=0)
		taxNo.setText("" + companyInfo.getTaxNo());
		vhNo.setText("" + voucherforPrint.getVoucherNumber());
		date.setText(voucherforPrint.getVoucherDate());
		custname.setText(voucherforPrint.getCustName());
		note.setText(voucherforPrint.getRemark());
		vhType.setText(voucherTyp);

		paytype.setText((voucherforPrint.getPayMethod() == 0 ? "ذمم" : "نقدا"));
		total.setText("" + voucherforPrint.getSubTotal());
		discount.setText("" + voucherforPrint.getVoucherDiscount());///
		tax.setText("" + voucherforPrint.getTax());
		ammont.setText("" + voucherforPrint.getNetSales());


		if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
			textW.setVisibility(View.GONE);
		} else {
			textW.setVisibility(View.VISIBLE);
		}


		TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
		TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
		lp2.setMargins(0, 7, 0, 0);
		lp3.setMargins(0, 7, 0, 0);

		for (int j = 0; j < itemforPrint.size(); j++) {

			if (voucherforPrint.getVoucherNumber() == itemforPrint.get(j).getVoucherNumber()) {
				final TableRow row = new TableRow(context);


				for (int i = 0; i <= 7; i++) {
					TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 10, 0, 0);
					row.setLayoutParams(lp);

					TextView textView = new TextView(context);
					textView.setGravity(Gravity.CENTER);
					textView.setTextSize(18);

					switch (i) {
						case 0:
							textView.setText(itemforPrint.get(j).getItemName());
							textView.setLayoutParams(lp3);
							break;


						case 1:
							if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
								textView.setText("" + itemforPrint.get(j).getUnit());
								textView.setLayoutParams(lp2);
							} else {
								textView.setText("" + itemforPrint.get(j).getQty());
								textView.setLayoutParams(lp2);
							}
							break;

						case 2:
							if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
								textView.setText("" + itemforPrint.get(j).getQty());
								textView.setLayoutParams(lp2);
								textView.setVisibility(View.VISIBLE);
							} else {
								textView.setVisibility(View.GONE);
							}
							break;

						case 3:
							textView.setText("" + itemforPrint.get(j).getPrice());
							textView.setLayoutParams(lp2);
							break;


						case 4:
							String amount = "" + (itemforPrint.get(j).getQty() * itemforPrint.get(j).getPrice() - itemforPrint.get(j).getDisc());
							amount = convertToEnglish(amount);
							textView.setText(amount);
							textView.setLayoutParams(lp2);
							break;
					}
					row.addView(textView);
				}


				tabLayout.addView(row);
			}
		}


        dialogs.show();


//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
		linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

		linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

		Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

		Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Drawable bgDrawable = linearView.getBackground();
		if (bgDrawable != null) {
			bgDrawable.draw(canvas);
		} else {
			canvas.drawColor(Color.WHITE);
		}
		linearView.draw(canvas);

		return bitmap;// creates bitmap and returns the same
	}

	public void printMultilingualFont_AccountReport(List<Account_Report> listAccountReport) {
		int nLineWidth = 550;
		try {

			posPtr.setAsync(false);
			CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
			posPtr.printBitmap(companyInfo.getLogo(),ESCPOSConst.LK_ALIGNMENT_CENTER,150);

			String companney_name="";
			decimalFormat = new DecimalFormat("##.00");
			String dataArabic_Report="";
			try {
				if (companyInfo.getCompanyName().equals("")) {
					companney_name = "Companey";
					//Please fill  the companey name
//					Toast.makeText(context, R.string.fill_name, Toast.LENGTH_SHORT).show();
				} else {
					companney_name = companyInfo.getCompanyName();
				}


                    posPtr.printAndroidFont(  null,true, companney_name +"\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                    posPtr.printAndroidFont(  null,true, "هاتف : " + companyInfo.getcompanyTel() +"    "+ "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                    posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                    posPtr.printAndroidFont(  null,true, "كشف حساب "+ "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                    posPtr.printAndroidFont(  null,true, "من تاريخ : " +   AccountReport.from_date   +   "  الى تاريخ: " + AccountReport.to_date + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                    posPtr.printAndroidFont(  null,true, "الرصيد السابق : " + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                    posPtr.printAndroidFont(  null,true, "ملاحظة: " + payforBank.getRemark() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                    posPtr.printAndroidFont(  null,true, "المبلغ المقبوض: " + payforBank.getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                    posPtr.printAndroidFont(  null,true, "طريقة الدفع: " + (payforBank.getPayMethod() == 1 ? "نقدا" : "شيك") + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                    posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                    posPtr.printAndroidFont(  null,true, "        القيمة     " + "      التاريخ      " + "   رقم الشيك         " + "  البنك    " + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
                    posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);


                    for (int i = 0; i < payList.size(); i++) {

                        if (payList.get(i).getBank().length() <= 12) {
                            String space = payList.get(i).getBank();
                            for (int g = 0; g < 12 - payList.get(i).getBank().length(); g++) {
                                space += "\t";
                            }//"\t\t\t\t" +
                            posPtr.printAndroidFont(  null,true, "\t\t"+space+ payList.get(i).getCheckNumber()+"\t\t\t\t"+ payList.get(i).getDueDate()+"\t\t\t" + payList.get(i).getAmount() + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);

//                    dataArabic += "\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space + "\n";
                        } else {
                            String space = payList.get(i).getBank().substring(0, 10);
//                    for (int g = 0; g <  payList.get(i).getBank().length()-12; g++) {
//                        space+= "\t" ;
//                    }
                            String fullString = payList.get(i).getBank().substring(10, payList.get(i).getBank().length() - 1);
                            posPtr.printAndroidFont(  null,true, "\t\t"+space +"\t\t\t"+ payList.get(i).getCheckNumber() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t" + payList.get(i).getAmount() + "\n" + fullString + "\n" , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);
//                    dataArabic +=   "\n\t\t\t\t" + payList.get(i).getAmount() + "\t\t\t\t" + payList.get(i).getDueDate() + "\t\t\t\t" + payList.get(i).getCheckNumber() + "\t\t" + space +fullString + "\n";
                        }
                    }



                posPtr.lineFeed(2);
                posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
                posPtr.printAndroidFont(  null,true, "   المستلم ---------------                 التوقيع --------------               " + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);

                posPtr.lineFeed(4);
            } catch (IOException e) {
				Log.e("IOException","printerppayListe="+e.getMessage());
                e.printStackTrace();
            }


			posPtr.printAndroidFont(  null,true, "--------------------------------------------------------------------------------" + "\n"  , nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_CENTER);
			posPtr.lineFeed(4);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IOException","printerppayListe="+e.getMessage());
		}
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		if (bm != null){
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
			return resizedBitmap;
		}
		return null;
	}
}
