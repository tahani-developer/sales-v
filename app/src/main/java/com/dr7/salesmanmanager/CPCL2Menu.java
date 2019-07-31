//package com.dr7.salesmanmanager;
////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//
//import android.app.Activity;
//import android.app.AlertDialog.Builder;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Spinner;
//
//import com.dr7.salesmanmanager.Port.AlertView;
//
//import java.io.IOException;
//
//public class CPCL2Menu extends Activity {
//	static final String[] menuArray = new String[]{"Barcode", "Sewoo Tech.", "2D Barcode", "Denmark Stamp", "Font Test", "Font Type Test", "Setting Test1", "Setting Test2", "MULTILINE", "Print Android Font", "Print Multilingual", "Printer Status", "Print GS1 1", "Print GS1 2"};
//	private String strCount;
//	private Spinner paperSpinner;
//
//	public CPCL2Menu() {
//	}
//
//	protected void onDestroy() {
//		super.onDestroy();
//		Log.d("CPCL2Menu", "OnDestroy");
//	}
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		this.setContentView(R.layout.label_sample_menu);
////        ArrayAdapter<CharSequence> paperAdapter = ArrayAdapter.createFromResource(this, 2130968579, 17367048);
////        paperAdapter.setDropDownViewResource(17367049);
////        this.paperSpinner = (Spinner)this.findViewById(2131165194);
////        this.paperSpinner.setAdapter(paperAdapter);
////        this.paperSpinner.setSelection(2);
//		ListView sampleList = (ListView)findViewById(R.id.spi);
//		sampleList.setAdapter(new ArrayAdapter(this, R.layout.cci, menuArray));
//		sampleList.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				CPCL2Menu.this.dialog(arg2);
//			}
//		});
//
//		Log.e("123","jjj");
//	}
//
//	private void dialog(final int index) {
//		LinearLayout linear = (LinearLayout)View.inflate(this, R.layout.input_popup, (ViewGroup)null);
//		final EditText number = (EditText)linear.findViewById(R.id.EditTextPopup);
//		if (this.strCount == null) {
//			this.strCount = "1";
//		}
//
//		number.setText(this.strCount);
//		(new Builder(this)).setTitle("Test Count.").setIcon(R.drawable.ic_launcher).setView(linear).setPositiveButton("OK", new OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				try {
//					CPCL2Menu.this.strCount = number.getText().toString();
//					int count = Integer.parseInt(CPCL2Menu.this.strCount);
//					Log.d("NUM", String.valueOf(count));
//					CPCLSample2 sample = new CPCLSample2(CPCLSample2.this);
////                    int paperType = CPCL2Menu.this.paperSpinner.getSelectedItemPosition();
//					switch(2) {
//						case 0:
//							sample.selectGapPaper();
//							break;
//						case 1:
//							sample.selectBlackMarkPaper();
//							break;
//						case 2:
//							sample.selectContinuousPaper();
//					}
//
//					switch(index) {
//						case 0:
//							sample.barcodeTest(count);
//							break;
//						case 1:
//							sample.profile2(count);
//							break;
//						case 2:
//							sample.barcode2DTest(count);
//							break;
//						case 3:
////							sample.dmStamp(count);
//							break;
//						case 4:
//							sample.fontTest(count);
//							break;
//						case 5:
//							sample.fontTypeTest(count);
//							break;
//						case 6:
//							sample.settingTest1(count);
//							break;
//						case 7:
//							sample.settingTest2(count);
//							break;
//						case 8:
//							sample.multiLineTest(count);
//							break;
//						case 9:
//							sample.printAndroidFont(count);
//							break;
//						case 10:
//							sample.printMultilingualFont(count);
//							break;
//						case 11:
//							String strresult = sample.statusCheck();
//							AlertView.showAlert("Status Error", strresult + " : ", CPCL2Menu.this);
//							break;
//						case 12:
//							sample.RSS1(count);
//							break;
//						case 13:
//							sample.RSS2(count);
//					}
//				} catch (NumberFormatException var7) {
//					Log.e("NumberFormatException", "Invalid Input Nubmer.", var7);
//				} catch (IOException var8) {
//					Log.e("IO Exception", "IO Error", var8);
//				}
//
//			}
//		}).setNegativeButton("Cancel", new OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				Log.d("Cancel", "Cancel Button Clicked.");
//			}
//		}).show();
//	}
//}
