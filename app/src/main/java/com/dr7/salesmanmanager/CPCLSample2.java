//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.dr7.salesmanmanager;


import android.graphics.Typeface;
import com.sewoo.jpos.printer.CPCLPrinter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CPCLSample2 {
	private CPCLPrinter cpclPrinter = new CPCLPrinter();
	private int paperType = 2;

	public CPCLSample2() {
	}

	public void selectGapPaper() {
		this.paperType = 0;
	}

	public void selectBlackMarkPaper() {
		this.paperType = 1;
	}

	public void selectContinuousPaper() {
		this.paperType = 2;
	}

	public void barcodeTest(int count) throws UnsupportedEncodingException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.setCPCLBarcode(0, 0, 0);
		this.cpclPrinter.printCPCLBarcode(0, "CODABAR", 2, 0, 30, 19, 45, "A37859B", 0);
		this.cpclPrinter.printCPCLText(0, 7, 0, 19, 18, "CODABAR", 0);
		this.cpclPrinter.setCPCLBarcode(0, 0, 0);
		this.cpclPrinter.printCPCLBarcode(0, "39", 2, 1, 30, 19, 130, "0123456", 0);
		this.cpclPrinter.printCPCLText(0, 7, 0, 21, 103, "CODE 39", 0);
		this.cpclPrinter.setCPCLBarcode(0, 0, 0);
		this.cpclPrinter.printCPCLBarcode(0, "93", 2, 1, 30, 19, 215, "0123456", 0);
		this.cpclPrinter.printCPCLText(0, 7, 0, 21, 180, "CODE 93", 0);
		this.cpclPrinter.setCPCLBarcode(0, 0, 0);
		this.cpclPrinter.printCPCLBarcode(0, "128", 2, 1, 30, 19, 300, "A37859B", 0);
		this.cpclPrinter.printCPCLText(0, 7, 0, 21, 270, "CODE 128", 0);
		this.cpclPrinter.printForm();
	}

	public void profile2(int count) throws UnsupportedEncodingException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.printCPCLText(0, 5, 1, 1, 1, "SEWOO TECH CO.,LTD.", 0);
		this.cpclPrinter.printCPCLText(0, 0, 2, 1, 70, "Global leader in the mini-printer industry.", 0);
		this.cpclPrinter.printCPCLText(0, 0, 2, 1, 110, "Total Printing Solution", 0);
		this.cpclPrinter.printCPCLText(0, 0, 2, 1, 150, "Diverse innovative and reliable products", 0);
		this.cpclPrinter.printCPCLText(0, 7, 0, 1, 200, "TEL : 82-31-459-8200", 0);
		this.cpclPrinter.printCPCL2DBarCode(0, "QRCODE", 0, 250, 4, 0, 1, 0, "http://www.miniprinter.com");
		this.cpclPrinter.printCPCLText(0, 7, 0, 130, 250, "www.miniprinter.com", 0);
		this.cpclPrinter.printCPCLText(0, 1, 0, 130, 300, "<-- Check This.", 0);
		this.cpclPrinter.printForm();
	}

	public void barcode2DTest(int count) throws IOException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.printBitmap("//sdcard//temp//test//logo_s.jpg", 1, 1);
		this.cpclPrinter.setCPCLBarcode(0, 0, 0);
		this.cpclPrinter.printCPCLBarcode(0, "128", 2, 1, 30, 19, 125, "12345690AB", 0);
		this.cpclPrinter.printCPCL2DBarCode(0, "PDF-417", 80, 180, 2, 7, 2, 1, "SEWOO TECH\r\nLK-P11");
		this.cpclPrinter.printCPCL2DBarCode(0, "QRCODE", 30, 260, 4, 0, 1, 0, "LK-P11");
		this.cpclPrinter.printCPCLText(0, 0, 2, 130, 280, "SEWOO TECH", 0);
		this.cpclPrinter.printCPCLText(0, 0, 2, 130, 300, "LK-P11", 0);
		this.cpclPrinter.printForm();
	}

	public void imageTest(int count) throws IOException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.printBitmap("//sdcard//temp//test//sample_2.jpg", 1, 200);
		this.cpclPrinter.printBitmap("//sdcard//temp//test//sample_3.jpg", 100, 200);
		this.cpclPrinter.printBitmap("//sdcard//temp//test//sample_4.jpg", 120, 245);
		this.cpclPrinter.printForm();
	}

	public void dmStamp(int count) throws IOException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.printBitmap("//sdcard//temp//test//danmark_windmill.jpg", 10, 10);
		this.cpclPrinter.printBitmap("//sdcard//temp//test//denmark_flag.jpg", 222, 55);
		this.cpclPrinter.setCPCLBarcode(0, 0, 0);
		this.cpclPrinter.printCPCLBarcode(0, "128", 2, 1, 30, 19, 290, "0123456", 1);
		this.cpclPrinter.printCPCLText(0, 0, 1, 21, 345, "Quantity 001", 1);
		this.cpclPrinter.printForm();
	}

	public void fontTest(int count) throws UnsupportedEncodingException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.printCPCLText(0, 0, 0, 1, 1, "FONT-0-0", 2);
		this.cpclPrinter.printCPCLText(0, 0, 1, 1, 50, "FONT-0-1", 0);
		this.cpclPrinter.printCPCLText(0, 4, 0, 1, 100, "FONT-4-0", 0);
		this.cpclPrinter.printCPCLText(0, 4, 1, 1, 150, "FONT-4-1", 0);
		this.cpclPrinter.printCPCLText(0, 4, 2, 1, 260, "4-2", 0);
		this.cpclPrinter.printForm();
	}

	public void fontTypeTest(int count) throws UnsupportedEncodingException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.printCPCLText(0, 0, 0, 1, 1, "ABCD1234", 0);
		this.cpclPrinter.printCPCLText(0, 1, 0, 1, 20, "ABCD1234", 0);
		this.cpclPrinter.printCPCLText(0, 2, 0, 1, 70, "ABCD1234", 0);
		this.cpclPrinter.printCPCLText(0, 4, 0, 1, 100, "ABCD1234", 0);
		this.cpclPrinter.printCPCLText(0, 5, 0, 1, 150, "ABCD1234", 0);
		this.cpclPrinter.printCPCLText(0, 6, 0, 1, 200, "ABCD1234", 0);
		this.cpclPrinter.printCPCLText(0, 7, 0, 1, 250, "ABCD1234", 0);
		this.cpclPrinter.printForm();
	}

	public void settingTest1(int count) throws UnsupportedEncodingException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.setMagnify(2, 2);
		this.cpclPrinter.setJustification(0);
		this.cpclPrinter.printCPCLText(0, 0, 0, 1, 1, "FONT-0-0", 2);
		this.cpclPrinter.setMagnify(1, 1);
		this.cpclPrinter.printCPCLText(0, 0, 1, 1, 50, "FONT-0-1", 0);
		this.cpclPrinter.setJustification(1);
		this.cpclPrinter.printCPCLText(0, 4, 0, 1, 100, "FONT-4-0", 0);
		this.cpclPrinter.setMagnify(2, 1);
		this.cpclPrinter.printCPCLText(0, 4, 1, 1, 150, "FONT-4-1", 0);
		this.cpclPrinter.setMagnify(1, 1);
		this.cpclPrinter.setJustification(2);
		this.cpclPrinter.printCPCLText(0, 4, 2, 1, 260, "4-2", 0);
		this.cpclPrinter.resetMagnify();
		this.cpclPrinter.printForm();
	}

	public void settingTest2(int count) throws UnsupportedEncodingException {
		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.setConcat(0, 75, 75);
		this.cpclPrinter.concatText(4, 2, 5, "$");
		this.cpclPrinter.concatText(4, 3, 0, "12");
		this.cpclPrinter.concatText(4, 2, 5, "34");
		this.cpclPrinter.resetConcat();
		this.cpclPrinter.printForm();
	}

	public void multiLineTest(int count) throws UnsupportedEncodingException {
		String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZ;0123456789!@#%^&*\r\n";
		StringBuffer sb = new StringBuffer();

		for(int i = 0; i < 16; ++i) {
			sb.append(data);
		}

		this.cpclPrinter.setForm(0, 200, 200, 406, count);
		this.cpclPrinter.setMedia(this.paperType);
		this.cpclPrinter.setMultiLine(15);
		this.cpclPrinter.multiLineText(0, 0, 0, 10, 20);
		this.cpclPrinter.multiLineData(sb.toString());
		this.cpclPrinter.resetMultiLine();
		this.cpclPrinter.printForm();
	}

	public String statusCheck() {
		String result = "";
		if (this.cpclPrinter.printerCheck() >= 0) {
			int sts = this.cpclPrinter.status();
			if (sts == 0) {
				return "Normal";
			}

			if ((sts & 1) > 0) {
				result = result + "Busy\r\n";
			}

			if ((sts & 2) > 0) {
				result = result + "Paper empty\r\n";
			}

			if ((sts & 4) > 0) {
				result = result + "Cover open\r\n";
			}

			if ((sts & 8) > 0) {
				result = result + "Battery low\r\n";
			}
		} else {
			result = "Check the printer\r\nNo response";
		}

		return result;
	}

	public void printAndroidFont(int count) throws UnsupportedEncodingException {
		int nLineWidth = 384;
		String data = "Receipt";
		Object var4 = null;

		try {
			this.cpclPrinter.setForm(0, 200, 200, 406, count);
			this.cpclPrinter.setMedia(this.paperType);
			this.cpclPrinter.printAndroidFont(data, nLineWidth, 100, 0, 1);
			this.cpclPrinter.printAndroidFont("Left Alignment", nLineWidth, 24, 120, 0);
			this.cpclPrinter.printAndroidFont("Center Alignment", nLineWidth, 24, 150, 1);
			this.cpclPrinter.printAndroidFont("Right Alignment", nLineWidth, 24, 180, 2);
			this.cpclPrinter.printAndroidFont(Typeface.SANS_SERIF, "SANS_SERIF : 1234iwIW", nLineWidth, 24, 210, 0);
			this.cpclPrinter.printAndroidFont(Typeface.SERIF, "SERIF : 1234iwIW", nLineWidth, 24, 240, 0);
			this.cpclPrinter.printAndroidFont(Typeface.MONOSPACE, "MONOSPACE : 1234iwIW", nLineWidth, 24, 270, 0);
			this.cpclPrinter.printForm();
		} catch (IOException var6) {
			var6.printStackTrace();
		}

	}

	public void printMultilingualFont(int count) throws UnsupportedEncodingException {
		int nLineWidth = 384;
		String Koreandata = "영수증";
		String Turkishdata = "Turkish(İ,Ş,Ğ)";
		String Russiandata = "Получение";
		String Arabicdata = "الإيصال";
		String Greekdata = "Παραλαβή";
		String Japanesedata = "領収書";
		String GB2312data = "收据";
		String BIG5data = "收據";

		try {
			this.cpclPrinter.setForm(0, 200, 200, 1000, count);
			this.cpclPrinter.setMedia(this.paperType);
			this.cpclPrinter.printAndroidFont("Korean Font", nLineWidth, 24, 0, 0);
			this.cpclPrinter.printAndroidFont(Koreandata, nLineWidth, 100, 30, 1);
			this.cpclPrinter.printAndroidFont("Turkish Font", nLineWidth, 24, 140, 0);
			this.cpclPrinter.printAndroidFont(Turkishdata, nLineWidth, 50, 170, 1);
			this.cpclPrinter.printAndroidFont("Russian Font", nLineWidth, 24, 230, 0);
			this.cpclPrinter.printAndroidFont(Russiandata, nLineWidth, 60, 260, 1);
			this.cpclPrinter.printAndroidFont("Arabic Font", nLineWidth, 24, 330, 0);
			this.cpclPrinter.printAndroidFont(Arabicdata, nLineWidth, 100, 360, 1);
			this.cpclPrinter.printAndroidFont("Greek Font", nLineWidth, 24, 470, 0);
			this.cpclPrinter.printAndroidFont(Greekdata, nLineWidth, 60, 500, 1);
			this.cpclPrinter.printAndroidFont("Japanese Font", nLineWidth, 24, 570, 0);
			this.cpclPrinter.printAndroidFont(Japanesedata, nLineWidth, 100, 600, 1);
			this.cpclPrinter.printAndroidFont("GB2312 Font", nLineWidth, 24, 710, 0);
			this.cpclPrinter.printAndroidFont(GB2312data, nLineWidth, 100, 740, 1);
			this.cpclPrinter.printAndroidFont("BIG5 Font", nLineWidth, 24, 850, 0);
			this.cpclPrinter.printAndroidFont(BIG5data, nLineWidth, 100, 880, 1);
			this.cpclPrinter.printForm();
		} catch (IOException var12) {
			var12.printStackTrace();
		}

	}

	public void RSS1(int count) throws UnsupportedEncodingException {
		String rsscommand = "B RSS-EXPSTACK 2 40 10 10 [01]98898765432106[3202]012345\r\n";

		try {
			this.cpclPrinter.setForm(0, 200, 200, 1000, count);
			this.cpclPrinter.setMedia(this.paperType);
			this.cpclPrinter.userString(rsscommand);
			this.cpclPrinter.printForm();
		} catch (IOException var4) {
			var4.printStackTrace();
		}

	}

	public void RSS2(int count) throws UnsupportedEncodingException {
		String rsscommand = "B RSS-EXPSTACK 2 40 10 10 [01]98898765432106[3920]15000[3101]000015[17]160210[3202]012345[15]991231\r\n";

		try {
			this.cpclPrinter.setForm(0, 200, 200, 1000, count);
			this.cpclPrinter.setMedia(this.paperType);
			this.cpclPrinter.userString(rsscommand);
			this.cpclPrinter.printForm();
		} catch (IOException var4) {
			var4.printStackTrace();
		}

	}
}
