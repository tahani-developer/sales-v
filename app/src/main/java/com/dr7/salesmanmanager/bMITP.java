package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PrinterSetting;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Port.AlertView;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static com.dr7.salesmanmanager.PrintPayment.pay1;
import static com.dr7.salesmanmanager.PrintPayment.paymentPrinter;
import static com.dr7.salesmanmanager.PrintVoucher.items;
import static com.dr7.salesmanmanager.PrintVoucher.vouch1;
import static com.dr7.salesmanmanager.ReceiptVoucher.paymentsforPrint;
import static com.dr7.salesmanmanager.Reports.AccountReport.acount_report_list;
import static com.dr7.salesmanmanager.SalesInvoice.itemForPrint;
import static com.dr7.salesmanmanager.SalesInvoice.voucher;

// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)

public class bMITP extends Activity {
    private static final String TAG = "BluetoothConnectMenu";
    private static final int REQUEST_ENABLE_BT = 2;
    ArrayAdapter<String> adapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Vector<BluetoothDevice> remoteDevices;
    private BroadcastReceiver searchFinish;
    private BroadcastReceiver searchStart;
    private BroadcastReceiver discoveryResult;
    private BroadcastReceiver disconnectReceiver;
    private Thread hThread;
    private Context context;
    private EditText btAddrBox;
    private Button connectButton;
    private Button searchButton;
    private ListView list;
    private com.dr7.salesmanmanager.BluetoothPort bluetoothPort;
    private CheckBox chkDisconnect;
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
    private static final String fileName;
    private String lastConnAddr;
    static  String idname;
    DatabaseHandler obj;
    String getData;
   Voucher printVoucher;
    List<Item>itemPrint;
    List<Item> allStudents;

    static {
        fileName = dir + "//BTPrinter";
    }

    public bMITP() {

    }

    private void bluetoothSetup() {
        this.clearBtDevData();
        this.bluetoothPort = BluetoothPort.getInstance();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            if (!this.mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.startActivityForResult(enableBtIntent, 2);
            }

        }
    }

    private void loadSettingFile() {
//        int rin = false;
        char[] buf = new char[128];

        try {
            FileReader fReader = new FileReader(fileName);
            int rin = fReader.read(buf);
            if (rin > 0) {
                this.lastConnAddr = new String(buf, 0, rin);
                this.btAddrBox.setText(this.lastConnAddr);
            }

            fReader.close();
        } catch (FileNotFoundException var4) {
            Log.i("BluetoothConnectMenu", "Connection history not exists.");
        } catch (IOException var5) {
            Log.e("BluetoothConnectMenu", var5.getMessage(), var5);
        }

    }

    private void saveSettingFile() {
        try {
            File tempDir = new File(dir);
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }

            FileWriter fWriter = new FileWriter(fileName);
            if (this.lastConnAddr != null) {
                fWriter.write(this.lastConnAddr);
            }

            fWriter.close();
        } catch (FileNotFoundException var3) {
            Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
        } catch (IOException var4) {
            Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
        }

    }

    private void clearBtDevData() {
        this.remoteDevices = new Vector();
    }

    private void addPairedDevices() {
        Iterator iter = this.mBluetoothAdapter.getBondedDevices().iterator();

        while(iter.hasNext()) {
            BluetoothDevice pairedDevice = (BluetoothDevice)iter.next();
//            if (this.bluetoothPort.isValidAddress(pairedDevice.getAddress())) {
                this.remoteDevices.add(pairedDevice);
                this.adapter.add(pairedDevice.getName() + "\n[" + pairedDevice.getAddress() + "] [Paired]");
            }
//        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bluetooth_menu);
        this.btAddrBox = (EditText)this.findViewById(R.id.EditTextAddressBT);
        this.connectButton = (Button)this.findViewById(R.id.ButtonConnectBT);
        bMITP.this.connectButton.setEnabled(true);
        this.searchButton = (Button)this.findViewById(R.id.ButtonSearchBT);
        this.list = (ListView)this.findViewById(R.id.BtAddrListView);
        this.chkDisconnect = (CheckBox)this.findViewById(R.id.check_disconnect);
        this.chkDisconnect.setChecked(true);
        this.context = this;
        obj=new DatabaseHandler(bMITP.this);


//
        getData = getIntent().getStringExtra("printKey");
//        Bundle bundle = getIntent().getExtras();
//         allStudents = (List<Item>) bundle.get("ExtraData");
//
//         Log.e("all",allStudents.get(0).getBarcode());

        Log.e("printKey",""+getData);
        this.loadSettingFile();
        this.bluetoothSetup();
        this.connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!bMITP.this.bluetoothPort.isConnected()) {
                    try {
                        bMITP.this.btConn(bMITP.this.mBluetoothAdapter.getRemoteDevice(bMITP.this.btAddrBox.getText().toString()));
                    } catch (IllegalArgumentException var3) {
                        Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
                        AlertView.showAlert(var3.getMessage(), bMITP.this.context);
                        return;
                    } catch (IOException var4) {
                        Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                        AlertView.showAlert(var4.getMessage(), bMITP.this.context);
                        return;
                    }
                } else {
                    bMITP.this.btDisconn();
                }

            }
        });
        this.searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!bMITP.this.mBluetoothAdapter.isDiscovering()) {
                    bMITP.this.clearBtDevData();
                    bMITP.this.adapter.clear();
                    bMITP.this.mBluetoothAdapter.startDiscovery();
                } else {
                    bMITP.this.mBluetoothAdapter.cancelDiscovery();
                }

            }
        });
        this.adapter = new ArrayAdapter(bMITP.this , R.layout.cci );

        this.list.setAdapter(this.adapter);
        this.addPairedDevices();
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BluetoothDevice btDev = (BluetoothDevice) bMITP.this.remoteDevices.elementAt(arg2);

                try {
                    if (bMITP.this.mBluetoothAdapter.isDiscovering()) {
                        bMITP.this.mBluetoothAdapter.cancelDiscovery();
                    }

                    bMITP.this.btAddrBox.setText(btDev.getAddress());
                    bMITP.this.btConn(btDev);
                } catch (IOException var8) {
                    AlertView.showAlert(var8.getMessage(), bMITP.this.context);
                }
            }
        });
        this.discoveryResult = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothDevice remoteDevice = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (remoteDevice != null) {
                    String key;
                    if (remoteDevice.getBondState() != 12) {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "]";
                    } else {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "] [Paired]";
                    }

//                    if (bMITP.this.bluetoothPort.isValidAddress(remoteDevice.getAddress())) {
                        bMITP.this.remoteDevices.add(remoteDevice);
                        bMITP.this.adapter.add(key);
                    }
//                }

            }
        };
        this.registerReceiver(this.discoveryResult, new IntentFilter("android.bluetooth.device.action.FOUND"));
        this.searchStart = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                bMITP.this.connectButton.setEnabled(false);
                bMITP.this.btAddrBox.setEnabled(false);
//                BluetoothConnectMenu.this.searchButton.setText(BluetoothConnectMenu.this.getResources().getString(2131034114));

                bMITP.this.searchButton.setText("stop ");
            }
        };
        this.registerReceiver(this.searchStart, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_STARTED"));
        this.searchFinish = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                bMITP.this.connectButton.setEnabled(true);
                bMITP.this.btAddrBox.setEnabled(true);
//                BluetoothConnectMenu.this.searchButton.setText(BluetoothConnectMenu.this.getResources().getString(2131034113));
                bMITP.this.searchButton.setText("search");

            }
        };
        this.registerReceiver(this.searchFinish, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        if (this.chkDisconnect.isChecked()) {
            this.disconnectReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    BluetoothDevice device = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (!"android.bluetooth.device.action.ACL_CONNECTED".equals(action) && "android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                        bMITP.this.DialogReconnectionOption();
                    }

                }
            };
        }

    }

    protected void onDestroy() {
        try {
            if (this.bluetoothPort.isConnected() && this.chkDisconnect.isChecked()) {
                this.unregisterReceiver(this.disconnectReceiver);
            }

            this.saveSettingFile();
            this.bluetoothPort.disconnect();
        } catch (IOException var2) {
            Log.e("BluetoothConnectMenu", var2.getMessage(), var2);
        } catch (InterruptedException var3) {
            Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
        }

        if (this.hThread != null && this.hThread.isAlive()) {
            this.hThread.interrupt();
            this.hThread = null;
        }

        this.unregisterReceiver(this.searchFinish);
        this.unregisterReceiver(this.searchStart);
        this.unregisterReceiver(this.discoveryResult);
        super.onDestroy();
    }

    private void DialogReconnectionOption() {
        String[] items = new String[]{"Bluetooth printer"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("connection ...");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).setPositiveButton("connect", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    bMITP.this.btDisconn();
                    bMITP.this.btConn(bMITP.this.mBluetoothAdapter.getRemoteDevice(bMITP.this.btAddrBox.getText().toString()));
                } catch (IllegalArgumentException var4) {
                    Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                    AlertView.showAlert(var4.getMessage(), bMITP.this.context);
                } catch (IOException var5) {
                    Log.e("BluetoothConnectMenu", var5.getMessage(), var5);
                    AlertView.showAlert(var5.getMessage(), bMITP.this.context);
                }
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                bMITP.this.btDisconn();
            }
        });
        builder.show();
    }

    private void btConn(BluetoothDevice btDev) throws IOException {
        (new bMITP.connTask()).execute(new BluetoothDevice[]{btDev});
    }

    private void btDisconn() {
        try {
            this.bluetoothPort.disconnect();
            if (this.chkDisconnect.isChecked()) {
                this.unregisterReceiver(this.disconnectReceiver);
            }
        } catch (Exception var2) {
            Log.e("BluetoothConnectMenu", var2.getMessage(), var2);
        }

        if (this.hThread != null && this.hThread.isAlive()) {
            this.hThread.interrupt();
        }

        this.connectButton.setText("Connect");
        this.list.setEnabled(true);
        this.btAddrBox.setEnabled(true);
        this.searchButton.setEnabled(true);
        Toast toast = Toast.makeText(this.context, "disconnect", Toast.LENGTH_SHORT);
        toast.show();
    }

    class connTask extends AsyncTask<BluetoothDevice, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(bMITP.this);

        connTask() {
        }

        protected void onPreExecute() {
            this.dialog.setTitle(" Try Connect ");
            this.dialog.setMessage("Please Wait ....");
            this.dialog.show();
            super.onPreExecute();
        }

        protected Integer doInBackground(BluetoothDevice... params) {
            Integer retVal = null;

            try {
                bMITP.this.bluetoothPort.connect(params[0]);
                bMITP.this.lastConnAddr = params[0].getAddress();
                retVal = 0;
            } catch (IOException var4) {
                Log.e("BluetoothConnectMenu", var4.getMessage());
                retVal = -1;
            }

            return retVal;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(Integer result) {
            if (result == 0) {
                RequestHandler rh = new RequestHandler();
                bMITP.this.hThread = new Thread(rh);
                bMITP.this.hThread.start();
                bMITP.this.connectButton.setText("Connect");
                bMITP.this.connectButton.setEnabled(false);
                bMITP.this.list.setEnabled(false);
                bMITP.this.btAddrBox.setEnabled(false);
                bMITP.this.searchButton.setEnabled(false);
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                Toast toast = Toast.makeText(bMITP.this.context, "Now Printing ", Toast.LENGTH_SHORT);
                toast.show();


                int count =Integer.parseInt(getData);
                ESCPSample2 sample = new ESCPSample2(bMITP.this);

                int settings =0;
                try {
                     settings = obj.getAllSettings().get(0).getNumOfCopy();
                }catch (Exception e){
                    settings=0;
                }
                try {
                    int printShape=0;
                    List<PrinterSetting> printerSettings=obj.getPrinterSetting_();

                    if(printerSettings.size()!=0){
                        printShape=printerSettings.get(0).getPrinterShape();
                    }
                  switch (count){

                      case 0:
                          printVoucher = vouch1;
                          itemPrint = items;
//                          convertLayoutToImageW(bMITP.this,sample,settings);
                          if(printShape==0) {
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                                  sample.printMultilingualFontEsc3(0, printVoucher, itemPrint);
                              }
                          }else {
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                                  sample.printMultilingualFontEscEjapy(0, printVoucher, itemPrint);
                              }

                          }
                          break;
                      case 1:
                          printVoucher = voucher;
                          itemPrint = itemForPrint;
//                          convertLayoutToImageW(bMITP.this,sample,settings);
//
                          if(printShape==0){
                          for(int i=0;i<settings;i++) {
//                              sample.printMultilingualFontEsc(1);
                              sample.printMultilingualFontEsc3(1,printVoucher,itemPrint);

                          }}
                          else {
                          for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                              sample.printMultilingualFontEscEjapy(1, printVoucher, itemPrint);
                          }

                      }
                          break;

                      case 2:

                          for(int i=0;i<settings;i++) {
                              sample.printMultilingualFontPayCash(0);

                          }
                          Log.e("Re","print");

                          paymentsforPrint.clear();
                          break;

                      case 3:
                          for(int i=0;i<settings;i++) {
                              sample.printMultilingualFontCashReport();
                          }
                          break;

                      case 4:
                          Log.e("pay","print");
                          for(int i=0;i<settings;i++) {
                              sample.printMultilingualFontPayCash(1);
                          }
                          paymentPrinter.clear();
                          break;
                      case 5:
                          for(int i=0;i<settings;i++) {
                              sample.printMultilingualFont_AccountReport(acount_report_list);
                          }

                          break;

                  }


//                    sample.printMultilingualFont();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                    finish();


                if (bMITP.this.chkDisconnect.isChecked()) {
                    bMITP.this.registerReceiver(bMITP.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
                    bMITP.this.registerReceiver(bMITP.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
                }
            } else {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                AlertView.showAlert("Disconnect Bluetoothُ", "Try Again ,,,.", bMITP.this.context);
            }

            super.onPostExecute(result);
        }
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

//    public void convertLayoutToImageW(Context context,ESCPSample2 sample,int settingsSi) {
//        LinearLayout linearView = null;
//
//        final Dialog dialogs = new Dialog(context);
//        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogs.setCancelable(false);
//        dialogs.setContentView(R.layout.printdialog);
////            fill_theVocher( voucherPrint);
//
//        TextView	doneinsewooprint;
//
//        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//        doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);
//
//        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW;
//        ImageView img = (ImageView) dialogs.findViewById(R.id.img);
//
//        compname = (TextView) dialogs.findViewById(R.id.compname);
//        tel = (TextView) dialogs.findViewById(R.id.tel);
//        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
//        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
//        date = (TextView) dialogs.findViewById(R.id.date);
//        custname = (TextView) dialogs.findViewById(R.id.custname);
//        note = (TextView) dialogs.findViewById(R.id.note);
//        vhType = (TextView) dialogs.findViewById(R.id.vhType);
//        paytype = (TextView) dialogs.findViewById(R.id.paytype);
//        total = (TextView) dialogs.findViewById(R.id.total);
//        discount = (TextView) dialogs.findViewById(R.id.discount);
//        tax = (TextView) dialogs.findViewById(R.id.tax);
//        ammont = (TextView) dialogs.findViewById(R.id.ammont);
//        textW = (TextView) dialogs.findViewById(R.id.wa1);
//        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
////
//
//
//
//        String voucherTyp = "";
//        switch (printVoucher.getVoucherType()) {
//            case 504:
//                voucherTyp = "فاتورة بيع";
//                break;
//            case 506:
//                voucherTyp = "فاتورة مرتجعات";
//                break;
//            case 508:
//                voucherTyp = "طلب جديد";
//                break;
//        }
//        img.setImageBitmap(companyInfo.getLogo());
//        compname.setText(companyInfo.getCompanyName());
//        tel.setText("" + companyInfo.getcompanyTel());
//        taxNo.setText("" + companyInfo.getTaxNo());
//        vhNo.setText("" + printVoucher.getVoucherNumber());
//        date.setText(printVoucher.getVoucherDate());
//        custname.setText(printVoucher.getCustName());
//        note.setText(printVoucher.getRemark());
//        vhType.setText(voucherTyp);
//
//        paytype.setText((printVoucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
//        total.setText("" + printVoucher.getSubTotal());
//        discount.setText("" + printVoucher.getVoucherDiscount());///
//        tax.setText("" + printVoucher.getTax());
//        ammont.setText("" + printVoucher.getNetSales());
//
//
//        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
//            textW.setVisibility(View.GONE);
//        } else {
//            textW.setVisibility(View.VISIBLE);
//        }
//
//
//        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//        lp2.setMargins(0, 7, 0, 0);
//        lp3.setMargins(0, 7, 0, 0);
//
//        for (int j = 0; j < itemPrint.size(); j++) {
//
//            if (printVoucher.getVoucherNumber() == itemPrint.get(j).getVoucherNumber()) {
//                final TableRow row = new TableRow(context);
//
//
//                for (int i = 0; i <= 7; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                    lp.setMargins(0, 10, 0, 0);
//                    row.setLayoutParams(lp);
//
//                    TextView textView = new TextView(context);
//                    textView.setGravity(Gravity.CENTER);
//                    textView.setTextSize(18);
//
//                    switch (i) {
//                        case 0:
//                            textView.setText(itemPrint.get(j).getItemName());
//                            textView.setLayoutParams(lp3);
//                            break;
//
//
//                        case 1:
//                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
//                                textView.setText("" + itemPrint.get(j).getUnit());
//                                textView.setLayoutParams(lp2);
//                            } else {
//                                textView.setText("" + itemPrint.get(j).getQty());
//                                textView.setLayoutParams(lp2);
//                            }
//                            break;
//
//                        case 2:
//                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
//                                textView.setText("" + itemPrint.get(j).getQty());
//                                textView.setLayoutParams(lp2);
//                                textView.setVisibility(View.VISIBLE);
//                            } else {
//                                textView.setVisibility(View.GONE);
//                            }
//                            break;
//
//                        case 3:
//                            textView.setText("" + itemPrint.get(j).getPrice());
//                            textView.setLayoutParams(lp2);
//                            break;
//
//
//                        case 4:
//                            String amount = "" + (itemPrint.get(j).getQty() * itemPrint.get(j).getPrice() - itemPrint.get(j).getDisc());
//                            amount = convertToEnglish(amount);
//                            textView.setText(amount);
//                            textView.setLayoutParams(lp2);
//                            break;
//                    }
//                    row.addView(textView);
//                }
//
//
//                tabLayout.addView(row);
//            }
//        }
//
//
//        dialogs.show();
//
//
//        for(int i=0;i<settingsSi;i++) {
////                              sample.printMultilingualFontEsc(1);
//            try {
//                sample.printMultilingualFontEsc3(1,printVoucher,itemPrint);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//
//
////        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
////        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);
////
////        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
////                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
////        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
////
////        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());
////
//////        linearView.setDrawingCacheEnabled(true);
//////        linearView.buildDrawingCache();
//////        Bitmap bit =linearView.getDrawingCache();
////
//////        linearView.setDrawingCacheEnabled(true);
//////        linearView.buildDrawingCache();
//////        Bitmap bit =linearView.getDrawingCache();
////
////        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
////        Canvas canvas = new Canvas(bitmap);
////        Drawable bgDrawable = linearView.getBackground();
////        if (bgDrawable != null) {
////            bgDrawable.draw(canvas);
////        } else {
////            canvas.drawColor(Color.WHITE);
////        }
////        linearView.draw(canvas);
//
////        return bitmap;// creates bitmap and returns the same
//    }

}
