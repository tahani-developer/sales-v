package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v4.content.ContextCompat;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.dr7.salesmanmanager.Modles.inventoryReportItem;
import com.dr7.salesmanmanager.Port.AlertView;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static com.dr7.salesmanmanager.NumberToArabic.getArabicString;
import static com.dr7.salesmanmanager.PrintPayment.pay1;
import static com.dr7.salesmanmanager.PrintPayment.paymentPrinter;
import static com.dr7.salesmanmanager.PrintVoucher.TOTAL;
import static com.dr7.salesmanmanager.PrintVoucher.items;
import static com.dr7.salesmanmanager.PrintVoucher.vouch1;
import static com.dr7.salesmanmanager.ReceiptVoucher.paymentsforPrint;
import static com.dr7.salesmanmanager.Reports.InventoryReport.itemsInventoryPrint;
import static com.dr7.salesmanmanager.Reports.InventoryReport.itemsReportinventory;
import static com.dr7.salesmanmanager.Reports.InventoryReport.typeQty;
import static com.dr7.salesmanmanager.SalesInvoice.finishPrint;
import static com.dr7.salesmanmanager.SalesInvoice.itemForPrint;
import static com.dr7.salesmanmanager.SalesInvoice.itemForPrintLast;
import static com.dr7.salesmanmanager.SalesInvoice.valueCheckHidPrice;
import static com.dr7.salesmanmanager.SalesInvoice.vouchLast;
import static com.dr7.salesmanmanager.SalesInvoice.voucher;
import static com.dr7.salesmanmanager.StockRequest.listItemStock;
import static com.dr7.salesmanmanager.StockRequest.totalQty;
import static com.dr7.salesmanmanager.StockRequest.voucherStock;

import  com.dr7.salesmanmanager.StockRequest.*;

// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)

public class BluetoothConnectMenu extends Activity {
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
    private BluetoothPort bluetoothPort;
    private CheckBox chkDisconnect;
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
    private static final String fileName;
    private String lastConnAddr;
    static String idname;
    DatabaseHandler obj;
    String getData;
    List<Item> long_listItems;
    Voucher voucherforPrint;
    List<Item> itemforPrint;
    List<Payment>payList;
    DecimalFormat decimalFormat;
    Payment payforBank;

    public  static  int valueCheckHidPrice=0;


    static {
        fileName = dir + "//BTPrinter";
    }

    public BluetoothConnectMenu() {

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

        while (iter.hasNext()) {
            BluetoothDevice pairedDevice = (BluetoothDevice) iter.next();
            if (this.bluetoothPort.isValidAddress(pairedDevice.getAddress())) {
                this.remoteDevices.add(pairedDevice);
                this.adapter.add(pairedDevice.getName() + "\n[" + pairedDevice.getAddress() + "] [Paired]");
            }
        }

    }

    double size_subList = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bluetooth_menu);
        this.btAddrBox = (EditText) this.findViewById(R.id.EditTextAddressBT);
        this.connectButton = (Button) this.findViewById(R.id.ButtonConnectBT);
        BluetoothConnectMenu.this.connectButton.setEnabled(true);
        this.searchButton = (Button) this.findViewById(R.id.ButtonSearchBT);
        this.list = (ListView) this.findViewById(R.id.BtAddrListView);
        this.chkDisconnect = (CheckBox) this.findViewById(R.id.check_disconnect);
        this.chkDisconnect.setChecked(true);
        this.context = this;
        obj = new DatabaseHandler(BluetoothConnectMenu.this);
        long_listItems = new ArrayList<Item>();
        decimalFormat = new DecimalFormat("##.000");


//
        getData = getIntent().getStringExtra("printKey");
//        Bundle bundle = getIntent().getExtras();
//         allStudents = (List<Item>) bundle.get("ExtraData");
//
//         Log.e("all",allStudents.get(0).getBarcode());

        Log.e("printKey", "" + getData);
        this.loadSettingFile();
        this.bluetoothSetup();
        this.connectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!BluetoothConnectMenu.this.bluetoothPort.isConnected()) {
                    try {
                        BluetoothConnectMenu.this.btConn(BluetoothConnectMenu.this.mBluetoothAdapter.getRemoteDevice(BluetoothConnectMenu.this.btAddrBox.getText().toString()));
                    } catch (IllegalArgumentException var3) {
                        Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
                        AlertView.showAlert(var3.getMessage(), BluetoothConnectMenu.this.context);
                        return;
                    } catch (IOException var4) {
                        Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                        AlertView.showAlert(var4.getMessage(), BluetoothConnectMenu.this.context);
                        return;
                    }
                } else {
                    BluetoothConnectMenu.this.btDisconn();
                }

            }
        });
        this.searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!BluetoothConnectMenu.this.mBluetoothAdapter.isDiscovering()) {
                    BluetoothConnectMenu.this.clearBtDevData();
                    BluetoothConnectMenu.this.adapter.clear();
                    BluetoothConnectMenu.this.mBluetoothAdapter.startDiscovery();
                } else {
                    BluetoothConnectMenu.this.mBluetoothAdapter.cancelDiscovery();
                }

            }
        });
        this.adapter = new ArrayAdapter(BluetoothConnectMenu.this, R.layout.cci);

        this.list.setAdapter(this.adapter);
        this.addPairedDevices();
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BluetoothDevice btDev = (BluetoothDevice) BluetoothConnectMenu.this.remoteDevices.elementAt(arg2);

                try {
                    if (BluetoothConnectMenu.this.mBluetoothAdapter.isDiscovering()) {
                        BluetoothConnectMenu.this.mBluetoothAdapter.cancelDiscovery();
                    }

                    BluetoothConnectMenu.this.btAddrBox.setText(btDev.getAddress());
                    BluetoothConnectMenu.this.btConn(btDev);
                } catch (IOException var8) {
                    AlertView.showAlert(var8.getMessage(), BluetoothConnectMenu.this.context);
                }
            }
        });
        this.discoveryResult = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothDevice remoteDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (remoteDevice != null) {
                    String key;
                    if (remoteDevice.getBondState() != 12) {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "]";
                    } else {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "] [Paired]";
                    }

                    if (BluetoothConnectMenu.this.bluetoothPort.isValidAddress(remoteDevice.getAddress())) {
                        BluetoothConnectMenu.this.remoteDevices.add(remoteDevice);
                        BluetoothConnectMenu.this.adapter.add(key);
                    }
                }

            }
        };
        this.registerReceiver(this.discoveryResult, new IntentFilter("android.bluetooth.device.action.FOUND"));
        this.searchStart = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothConnectMenu.this.connectButton.setEnabled(false);
                BluetoothConnectMenu.this.btAddrBox.setEnabled(false);
//                BluetoothConnectMenu.this.searchButton.setText(BluetoothConnectMenu.this.getResources().getString(2131034114));

                BluetoothConnectMenu.this.searchButton.setText("stop ");
            }
        };
        this.registerReceiver(this.searchStart, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_STARTED"));
        this.searchFinish = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothConnectMenu.this.connectButton.setEnabled(true);
                BluetoothConnectMenu.this.btAddrBox.setEnabled(true);
//                BluetoothConnectMenu.this.searchButton.setText(BluetoothConnectMenu.this.getResources().getString(2131034113));
                BluetoothConnectMenu.this.searchButton.setText("search");

            }
        };
        this.registerReceiver(this.searchFinish, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        if (this.chkDisconnect.isChecked()) {
            this.disconnectReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (!"android.bluetooth.device.action.ACL_CONNECTED".equals(action) && "android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                        BluetoothConnectMenu.this.DialogReconnectionOption();
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
        Builder builder = new Builder(this);
        builder.setTitle("connection ...");
        builder.setSingleChoiceItems(items, 0, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).setPositiveButton("connect", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    BluetoothConnectMenu.this.btDisconn();
                    BluetoothConnectMenu.this.btConn(BluetoothConnectMenu.this.mBluetoothAdapter.getRemoteDevice(BluetoothConnectMenu.this.btAddrBox.getText().toString()));
                } catch (IllegalArgumentException var4) {
                    Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                    AlertView.showAlert(var4.getMessage(), BluetoothConnectMenu.this.context);
                } catch (IOException var5) {
                    Log.e("BluetoothConnectMenu", var5.getMessage(), var5);
                    AlertView.showAlert(var5.getMessage(), BluetoothConnectMenu.this.context);
                }
            }
        }).setNegativeButton("cancel", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                BluetoothConnectMenu.this.btDisconn();
            }
        });
        builder.show();
    }

    private void btConn(BluetoothDevice btDev) throws IOException {
        (new BluetoothConnectMenu.connTask()).execute(new BluetoothDevice[]{btDev});
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
        private final ProgressDialog dialog = new ProgressDialog(BluetoothConnectMenu.this);

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
                BluetoothConnectMenu.this.bluetoothPort.connect(params[0]);
                BluetoothConnectMenu.this.lastConnAddr = params[0].getAddress();

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
                BluetoothConnectMenu.this.hThread = new Thread(rh);
                BluetoothConnectMenu.this.hThread.start();
                BluetoothConnectMenu.this.connectButton.setText("Connect");
                BluetoothConnectMenu.this.connectButton.setEnabled(false);
                BluetoothConnectMenu.this.list.setEnabled(false);
                BluetoothConnectMenu.this.btAddrBox.setEnabled(false);
                BluetoothConnectMenu.this.searchButton.setEnabled(false);
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
                double total_Qty=0;

                Toast toast = Toast.makeText(BluetoothConnectMenu.this.context, "Now Printing ", Toast.LENGTH_SHORT);
                toast.show();
                int count = Integer.parseInt(getData);
                CPCLSample2 sample = new CPCLSample2(BluetoothConnectMenu.this);
                sample.selectContinuousPaper();
                try {
                    int printShape=0;
                    List<PrinterSetting> printerSettings=obj.getPrinterSetting_();

                    if(printerSettings.size()!=0){
                        printShape=printerSettings.get(0).getPrinterShape();
                    }

                    CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
                    if (!companyInfo.getCompanyName().equals("") && companyInfo.getcompanyTel() != 0 && companyInfo.getTaxNo() != -1) {
//                  Log.e("salesVoucher","=" + SalesInvoice.items.get(0).getVoucherNumber());


                        if ((count == 0) || (count == 1) || (count==7)) {
//                        sample.dmStamp(1,companyInfo.getLogo());
                            if (count == 0) {
                                voucherforPrint = vouch1;// from print voucher
                                itemforPrint = items;
                            } else  if(count==1){
                                voucherforPrint = voucher;// from sales invoice
                                itemforPrint = itemForPrint;
                            }
                            else if(count==7)
                            { voucherforPrint = vouchLast;// from sales invoice
                                itemforPrint = itemForPrintLast;

                            }
//                                sample.printMultilingualFont(count, companyInfo.getLogo());
                            for (int j = 0; j < itemforPrint.size(); j++) {

                                if (voucherforPrint.getVoucherNumber() == itemforPrint.get(j).getVoucherNumber()) {
                                    Log.e("cherforPrint",""+voucherforPrint.getVoucherNumber()+"\t VoucherNumber"+itemforPrint.get(j).getVoucherNumber());
                                    TOTAL++;
                                    long_listItems.add(itemforPrint.get(j));
                                }
                            }


                            if (printShape == 0) {

                                if (TOTAL < 20) {
//                                Bitmap bit = convertLayoutToImage(voucherforPrint, itemforPrint);
                                    Bitmap bit = convertLayoutToImage(voucherforPrint, itemforPrint);
                                    sample.imageTestArabic(1, bit);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    TOTAL = 0;
                                    if(count==1)
                                    {
                                        finishPrint.setText("finish");
                                    }

                                } else {
                                    Bitmap bit_voucher_Headre = convertLayoutToImage_HEADER(voucherforPrint);
                                    sample.imageTestArabic(1, bit_voucher_Headre);
                                    size_subList = Math.ceil(long_listItems.size() / 30.0);
                                    int n = 0, k = 30;

                                    for (int i = 0; i < size_subList; i++) {//3
                                        if (long_listItems.size() <= 30) {
                                            Bitmap bit_voucher_Body = convertLayoutToImage_Body(voucherforPrint, long_listItems, 0);
                                            sample.imageTestArabic(1, bit_voucher_Body);
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }


                                            if (i == size_subList - 1) {
                                                k = long_listItems.size();

                                            }


                                            Bitmap bit_voucher_Body = convertLayoutToImage_Body(voucherforPrint, long_listItems.subList(n, k), n);
                                            sample.imageTestArabic(1, bit_voucher_Body);
                                            Log.e("n+k", " \t" + n + " " + k);
                                            n = n + 30;
                                            k = n + 30;
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                    try {
                                        Thread.sleep(8000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    Bitmap bit_voucher_Footer = convertLayoutToImage_Footer(voucherforPrint, long_listItems);
                                    sample.imageTestArabic(1, bit_voucher_Footer);
                                    if(count==1)
                                    {
                                        finishPrint.setText("finish");
                                    }

                                }
                            } else {// large name

                                if (TOTAL < 20) {
//                                Bitmap bit = convertLayoutToImage(voucherforPrint, itemforPrint);
                                    Bitmap bit = convertLayoutToImageEjape(voucherforPrint, itemforPrint);
                                    sample.imageTestEnglish(1, bit);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    TOTAL = 0;
                                    if(count==1)
                                    {
                                        finishPrint.setText("finish");
                                    }
                                } else {
                                    Bitmap bit_voucher_Headre = convertLayoutToImage_HEADER_Ejabe(voucherforPrint);
                                    sample.imageTestEnglish(1, bit_voucher_Headre);
                                    size_subList = Math.ceil(long_listItems.size() / 30.0);
                                    int n = 0, k = 30;

                                    for (int i = 0; i < size_subList; i++) {//3
                                        if (long_listItems.size() <= 30) {
                                            Bitmap bit_voucher_Body = convertLayoutToImage_Body_ejabi(voucherforPrint, long_listItems, 0);
                                            sample.imageTestEnglish(1, bit_voucher_Body);
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }


                                            if (i == size_subList - 1) {
                                                k = long_listItems.size();

                                            }


                                            Bitmap bit_voucher_Body = convertLayoutToImage_Body_ejabi(voucherforPrint, long_listItems.subList(n, k), n);
                                            sample.imageTestEnglish(1, bit_voucher_Body);
                                            Log.e("n+k", " \t" + n + " " + k);
                                            n = n + 30;
                                            k = n + 30;
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                    try {
                                        Thread.sleep(8000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    Bitmap bit_voucher_Footer = convertLayoutToImage_Footer_ejabe(voucherforPrint, long_listItems);
                                    sample.imageTestEnglish(1, bit_voucher_Footer);

                                }

                            }

//                        itemForPrint.clear();

                        } else {
                            if (count == 2||count==4 || count==8) {//  (8) for print last payment===> ReciptVoucher
                                if(count==2){
                                    payList=paymentsforPrint;
                                    payforBank=ReceiptVoucher.payment;

                                }
                                else if(count==4){
                                    payList=paymentPrinter;
                                    payforBank=pay1;
                                }
                                else if(count==8){
                                    payList=ReceiptVoucher.paymentPrinter;
                                    payforBank=ReceiptVoucher.pay1;
                                }
                                if(count==2){//2=== from ReceptVoucher
//                                payList=paymentsforPrint;
//                                payforBank=ReceiptVoucher.payment;
                                    if(printShape==0) {
                                        sample.printMultilingualFontCash(2);
                                    }else {// ejabi Layout
                                       // sample.imageTestEnglish_ejabi(1,bitmap);
                                        Bitmap bit = convertLayoutToImage_Recept();
//                                        Log.e("getAllCompanyInfo",""+bitmap);
                                        sample.imageTestEnglish(1, bit);
//                                        sample.printMultilingualFontCash_EJABI(2);

                                    }
                                paymentsforPrint.clear();
                                }
                                else {
//                                    payList=paymentPrinter;
//                                    payforBank=pay1;
                                    if(printShape==0) {
                                        sample.printMultilingualFontCash(count);
                                    }else {
                                        Bitmap bit = convertLayoutToImage_Recept();
//                                        Log.e("getAllCompanyInfo",""+bitmap);
                                        sample.imageTestEnglish(1, bit);
                                       // sample.imageTestEnglish_ejabi(1,bitmap);
//                                        sample.printMultilingualFontCash_EJABI(count);

                                    }
//                                    paymentsforPrint.clear();
                                }
                            }
                            else if (count == 3) {
                                sample.printMultilingualFontCashReport();

                            }
                            else if (count == 5) {
                                sample.printMultilingualFont_AccountReport();

                            }
                            else if(count == 6)
                            {// print stock request
//                                if (TOTAL < 20) {
//                                Bitmap bit = convertLayoutToImage(voucherforPrint, itemforPrint);
                                    Bitmap bit = convertLayoutToImageEjape_Stock(voucherStock, listItemStock);
                                    sample.imageTestEnglish(1, bit);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    listItemStock.clear();
                                    totalQty.setText("0.000");

                            }
                            else if(count == 9)
                            {// print Inventory Report
                                Log.e("printInventory",""+count+"report"+itemsInventoryPrint.size());
                                double totalqty=0;
                                Bitmap headerLayout=convertToImage_HEADER_Prin();

                                sample.imageTestEnglish(1, headerLayout);
                                for(int i=0;i<itemsInventoryPrint.size();i++)
                                {
                                    totalqty+=itemsInventoryPrint.get(i).getQty();

                                    Bitmap inventoryLayout=convertToImage_inventoryRow(itemsInventoryPrint.get(i));
                                    sample.imageTestEnglish(1, inventoryLayout);
                                }
                                Bitmap inventoryLayout_footer=convertToImage_inventoryFooter(totalqty);
                                sample.imageTestEnglish(1, inventoryLayout_footer);

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        finish();
//                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.clear);
//                    Bitmap testB =convertLayoutToImage(vouch,items);
//                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                    testB.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                    sample.dmStamp(1,testB);
//                    sample.imageTest(1,testB);
                    } else {
                        Toast.makeText(BluetoothConnectMenu.this, R.string.error_companey_info, Toast.LENGTH_LONG).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (BluetoothConnectMenu.this.chkDisconnect.isChecked()) {
                    BluetoothConnectMenu.this.registerReceiver(BluetoothConnectMenu.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
                    BluetoothConnectMenu.this.registerReceiver(BluetoothConnectMenu.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
                }
            } else {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                AlertView.showAlert("Disconnect Bluetoothُ", "Try Again ,,,.", BluetoothConnectMenu.this.context);
            }

            super.onPostExecute(result);
        }

    }

    private Bitmap convertToImage_inventoryFooter(double totalqty) {
        Log.e("inventoryFooter",""+totalqty);
        LinearLayout linearView = null;
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
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
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Header.setCancelable(false);
        dialog_Header.setContentView(R.layout.print_inventory_row);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        TextView doneinsewooprint = (TextView) dialog_Header.findViewById(R.id.done);

        TextView textView_itemName, textView_itemQuantity, textView_itemNumber, largeName;

//        textView_itemName = (TextView) dialog_Header.findViewById(R.id.textView_itemName);
        textView_itemQuantity = (TextView) dialog_Header.findViewById(R.id.textView_itemQuantity);
        textView_itemNumber = (TextView) dialog_Header.findViewById(R.id.textView_itemNumber);
        largeName = dialog_Header.findViewById(R.id.textView_itemName_large);
        largeName.setVisibility(View.VISIBLE);
        largeName.setText(inventoryItem.getName());

//        if(inventoryItem.getName().length()>15)
//        {
//            largeName.setVisibility(View.VISIBLE);
//            largeName.setText(inventoryItem.getName());
////            textView_itemName.setVisibility(View.GONE);
//        }
//        else {
//            largeName.setVisibility(View.GONE);
//            textView_itemName.setText(inventoryItem.getName());
//        }

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

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    private Bitmap convertLayoutToImage_HEADER(Voucher voucher) {
        LinearLayout linearView = null;
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Header.setCancelable(false);
        dialog_Header.setContentView(R.layout.header_voucher_print);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        TextView doneinsewooprint = (TextView) dialog_Header.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype,salesName     ;
        ImageView img = (ImageView) dialog_Header.findViewById(R.id.img);
        compname = (TextView) dialog_Header.findViewById(R.id.compname);
        tel = (TextView) dialog_Header.findViewById(R.id.tel);
        taxNo = (TextView) dialog_Header.findViewById(R.id.taxNo);
        vhNo = (TextView) dialog_Header.findViewById(R.id.vhNo);
        date = (TextView) dialog_Header.findViewById(R.id.date);
        custname = (TextView) dialog_Header.findViewById(R.id.custname);
        note = (TextView) dialog_Header.findViewById(R.id.note);
        vhType = (TextView) dialog_Header.findViewById(R.id.vhType);
        paytype = (TextView) dialog_Header.findViewById(R.id.paytype);
        salesName = (TextView) dialog_Header.findViewById(R.id.salesman_name);
        String voucherTyp = "";
        switch (voucher.getVoucherType()) {
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
        if (companyInfo.getLogo()!=(null))
        {
        img.setImageBitmap(companyInfo.getLogo());
        }
        else{img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));}
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        if(companyInfo.getTaxNo()!=0)
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);
        salesName.setText(obj.getAllSettings().get(0).getSalesMan_name());
        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        dialog_Header.show();

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
    private Bitmap convertToImage_HEADER_Prin() {
        LinearLayout linearView = null;
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
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
        LinearLayout printFooter,mainLayout;
        printFooter=dialog_Header.findViewById(R.id.printFooter);
        mainLayout=dialog_Header.findViewById(R.id.ll);
        mainLayout.setVisibility(View.VISIBLE);
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
        else{img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));}
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        if(companyInfo.getTaxNo()!=0)
        taxNo.setText("" + companyInfo.getTaxNo());
        qtyTypeText.setText(typeQty+"");
        salesName.setText(obj.getAllSettings().get(0).getSalesMan_name());

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
    private Bitmap convertLayoutToImage_HEADER_Ejabe(Voucher voucher) {
        LinearLayout linearView = null;
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Header.setCancelable(false);
        dialog_Header.setContentView(R.layout.header_voucher_print_ejabe);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        TextView doneinsewooprint = (TextView) dialog_Header.findViewById(R.id.done);

        TextView compname, store,tel, taxNo, vhNo, date, custname, note, vhType, paytype,salesName     ;
        ImageView img = (ImageView) dialog_Header.findViewById(R.id.img);
        compname = (TextView) dialog_Header.findViewById(R.id.compname);
        tel = (TextView) dialog_Header.findViewById(R.id.tel);
        taxNo = (TextView) dialog_Header.findViewById(R.id.taxNo);
        vhNo = (TextView) dialog_Header.findViewById(R.id.vhNo);
        date = (TextView) dialog_Header.findViewById(R.id.date);
        custname = (TextView) dialog_Header.findViewById(R.id.custname);
        note = (TextView) dialog_Header.findViewById(R.id.note);
        vhType = (TextView) dialog_Header.findViewById(R.id.vhType);
        paytype = (TextView) dialog_Header.findViewById(R.id.paytype);
        store= (TextView) dialog_Header.findViewById(R.id.store);
        salesName = (TextView) dialog_Header.findViewById(R.id.salesman_name);
        String salesmaname=obj.getSalesmanName(voucher.getCustNumber());
        salesName.setText(salesmaname);
        String voucherTyp = "";
        switch (voucher.getVoucherType()) {
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
        if (companyInfo.getLogo()!=(null))
        {
            img.setImageBitmap(companyInfo.getLogo());
        }
        else{img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));}
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        if(companyInfo.getTaxNo()!=0)
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);
        store.setText(Login.salesMan);
//        salesName.setText(obj.getAllSettings().get(0).getSalesMan_name());
        paytype.setText((voucher.getPayMethod() == 0 ? "Credit" : "Cash"));
        dialog_Header.show();

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


    private Bitmap convertLayoutToImage_Recept() {
        LinearLayout linearView = null;
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Header.setCancelable(false);
        dialog_Header.setContentView(R.layout.recept_voucher_ejabi);
        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        TextView doneinsewooprint = (TextView) dialog_Header.findViewById(R.id.done);

        TextView compname, store,tel, taxNo, vhNo, date, textView_amount, note, vhType, paytype,salesName ,amount_ofMoney,customerName    ;
        ImageView img = (ImageView) dialog_Header.findViewById(R.id.img);
        compname = (TextView) dialog_Header.findViewById(R.id.compname);
        tel = (TextView) dialog_Header.findViewById(R.id.tel);
        taxNo = (TextView) dialog_Header.findViewById(R.id.taxNo);
        amount_ofMoney = (TextView) dialog_Header.findViewById(R.id.textView_amount_ofMoney);
        vhNo = (TextView) dialog_Header.findViewById(R.id.vhNo);
        date = (TextView) dialog_Header.findViewById(R.id.date);
        textView_amount = (TextView) dialog_Header.findViewById(R.id.textView_amount);
        note = (TextView) dialog_Header.findViewById(R.id.note);
        paytype = (TextView) dialog_Header.findViewById(R.id.paytype);
        store= (TextView) dialog_Header.findViewById(R.id.store);
        salesName = (TextView) dialog_Header.findViewById(R.id.salesman_name);
        customerName = (TextView) dialog_Header.findViewById(R.id.textView_customerName);
        TableLayout tabLayout = (TableLayout) dialog_Header.findViewById(R.id.table_bank_info);
        //************************** Fill Data  *******************************************

        if (companyInfo.getLogo()!=(null))
        {
            img.setImageBitmap(companyInfo.getLogo());

        }
        else{
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));

            Toast.makeText(context, "Upload Company Logo For Print ", Toast.LENGTH_SHORT).show();
        }
        textView_amount.setText( payforBank.getAmount()+"");
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        if(companyInfo.getTaxNo()!=0)
        taxNo.setText("" + companyInfo.getTaxNo());

        if(obj.getAllSettings().get(0).getTafqit()==1&&valueCheckHidPrice!=1 )
        {
            amount_ofMoney.setText(getArabicString( payforBank.getAmount() +""));

        }
        else {

        }
        vhNo.setText("" + payforBank.getVoucherNumber());
        date.setText(payforBank.getPayDate());
        customerName.setText(payforBank.getCustName());
//        custname.setText(voucher.getCustName());
        note.setText( payforBank.getRemark() );
//        payforBank.getAmount()

        store.setText(Login.salesMan);
        salesName.setText(obj.getAllSettings().get(0).getSalesMan_name());
        paytype.setText((payforBank.getPayMethod() == 1 ? "Cash" : "Credit") );

        if (payforBank.getPayMethod() == 1||payforBank.getPayMethod()==2) {
            tabLayout.setVisibility(View.GONE);
        }
        else {
            tabLayout.setVisibility(View.VISIBLE);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            lp.setMargins(0, 7, 0, 0);
            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            lp2.setMargins(0, 7, 0, 0);


            if(count==2){
                payList=paymentsforPrint;
                payforBank=ReceiptVoucher.payment;

            }
            else if(count==4){
                payList=paymentPrinter;
                payforBank=pay1;
            }
            else if(count==8){
                payList=ReceiptVoucher.paymentPrinter;
                payforBank=ReceiptVoucher.pay1;
            }

//        List<Payment> payments = obj.getRequestedPaymentsPaper(pay.getVoucherNumber());
            List<Payment> payments =payList;
            for (int n = 0; n < payments.size(); n++) {
//                if (payments.get(n).getVoucherNumber() == mDbHandler.getMaxSerialNumber(4)) {
                final TableRow row = new TableRow(BluetoothConnectMenu.this);
                row.setPadding(0, 10, 0, 10);
                Log.e("paymentprint",""+payments.size());
                for (int i = 0; i < 4; i++) {

                    String[] record = {
                            payments.get(n).getBank() + "",
                            payments.get(n).getCheckNumber() + "",
                            payments.get(n).getDueDate() + "",
                            payments.get(n).getAmount() + "",
                    };

                    row.setLayoutParams(lp);
                    TextView textView = new TextView(BluetoothConnectMenu.this);
                    textView.setText(record[i]);
                    textView.setTextColor(ContextCompat.getColor(BluetoothConnectMenu.this, R.color.colorPrimary));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);
                    textView.setLayoutParams(lp2);
                    row.addView(textView);

                }

                tabLayout.addView(row);
            }
        }

        dialog_Header.show();

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

    private Bitmap convertLayoutToImage_Footer(Voucher voucher,List<Item> items) {
        LinearLayout linearView = null;

        final Dialog dialog_footer = new Dialog(BluetoothConnectMenu.this);
        dialog_footer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_footer.setCancelable(false);
        dialog_footer.setContentView(R.layout.footer_voucher_print);

        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        TextView doneinsewooprint = (TextView) dialog_footer.findViewById(R.id.done);

        TextView total, discount, tax, ammont, Total_qty_total;
        total = (TextView) dialog_footer.findViewById(R.id.total);
        discount = (TextView) dialog_footer.findViewById(R.id.discount);
        tax = (TextView) dialog_footer.findViewById(R.id.tax);
        ammont = (TextView) dialog_footer.findViewById(R.id.ammont);
        total.setText("" + voucher.getSubTotal());
        discount.setText(convertToEnglish(String.valueOf(decimalFormat.format( voucher.getTotalVoucherDiscount()))));
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());
        Total_qty_total=(TextView) dialog_footer.findViewById(R.id.total_qty);
        Total_qty_total.setText(count+"");
        linearView = (LinearLayout) dialog_footer.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

        return bit;// creates bitmap and returns the same
    }
    private Bitmap convertLayoutToImage_Footer_ejabe(Voucher voucher,List<Item> items) {
        LinearLayout linearView = null;

        final Dialog dialog_footer = new Dialog(BluetoothConnectMenu.this);
        dialog_footer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_footer.setCancelable(false);
        dialog_footer.setContentView(R.layout.footer_voucher_print_ejabe);

        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
        TextView doneinsewooprint = (TextView) dialog_footer.findViewById(R.id.done);

        TextView total, discount, tax, ammont, Total_qty_total,amountText;
        LinearLayout linearArabicAmount=(LinearLayout) dialog_footer.findViewById(R.id.linearArabicAmount);
        amountText = (TextView) dialog_footer.findViewById(R.id.amountText);
        total = (TextView) dialog_footer.findViewById(R.id.total);
        discount = (TextView) dialog_footer.findViewById(R.id.discount);
        tax = (TextView) dialog_footer.findViewById(R.id.tax);
        ammont = (TextView) dialog_footer.findViewById(R.id.ammont);
        total.setText("" + voucher.getSubTotal());
        discount.setText(convertToEnglish(String.valueOf(decimalFormat.format( voucher.getTotalVoucherDiscount()))));
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());
        Total_qty_total=(TextView) dialog_footer.findViewById(R.id.total_qty);
        Total_qty_total.setText(count+"");
        linearView = (LinearLayout) dialog_footer.findViewById(R.id.ll);
        if(obj.getAllSettings().get(0).getTafqit()==1&&valueCheckHidPrice!=1)
        {
            linearArabicAmount.setVisibility(View.VISIBLE);
            amountText.setText( getArabicString(voucher.getNetSales()+""));
        }
        else {
            linearArabicAmount.setVisibility(View.GONE);

        }




        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

        return bit;// creates bitmap and returns the same
    }


    private Bitmap convertLayoutToImage(Voucher voucher,List<Item> items) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(BluetoothConnectMenu.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.sewo30_printer_layout);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

       TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont,
                textW,total_qty_text,salesName,discountItems;
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);
//
        compname = (TextView) dialogs.findViewById(R.id.compname);
        tel = (TextView) dialogs.findViewById(R.id.tel);
        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
        date = (TextView) dialogs.findViewById(R.id.date);
        custname = (TextView) dialogs.findViewById(R.id.custname);
        salesName = (TextView) dialogs.findViewById(R.id.salesman_name);
        note = (TextView) dialogs.findViewById(R.id.note);
        vhType = (TextView) dialogs.findViewById(R.id.vhType);
        paytype = (TextView) dialogs.findViewById(R.id.paytype);
        total = (TextView) dialogs.findViewById(R.id.total);
        discount = (TextView) dialogs.findViewById(R.id.discount);
        discountItems= (TextView) dialogs.findViewById(R.id.discountItems);
        tax = (TextView) dialogs.findViewById(R.id.tax);
        ammont = (TextView) dialogs.findViewById(R.id.ammont);
        textW = (TextView) dialogs.findViewById(R.id.wa1);
        total_qty_text= (TextView) dialogs.findViewById(R.id.total_qty);
        //total_qty

        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
        String voucherTyp = "";
        switch (voucher.getVoucherType()) {
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
//        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        if (companyInfo.getLogo()!=(null))
        {
            img.setImageBitmap(companyInfo.getLogo());
        }
        else{img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));}

        tel.setText("" + companyInfo.getcompanyTel());
        if(companyInfo.getTaxNo()!=0)
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        salesName.setText(obj.getAllSettings().get(0).getSalesMan_name());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);
        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucher.getSubTotal());
        discount.setText(convertToEnglish(String.valueOf(decimalFormat.format( voucher.getTotalVoucherDiscount()))));
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());

       int count=0;
       float itemsDiscount=0;

        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp4 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);
        for (int j = 0; j < items.size(); j++) {
            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                count+=items.get(j).getQty();
                itemsDiscount+=items.get(j).getDisc();
                final TableRow row = new TableRow(BluetoothConnectMenu.this);


                for (int i = 0; i <= 7; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(500, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(BluetoothConnectMenu.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(14);
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getResources().getColor(R.color.text_view_color));

                    switch (i) {
                        case 0:
                            textView.setText(items.get(j).getItemName());
                            Log.e("textView",""+items.get(j).getItemName().length());
                            if(items.get(j).getItemName().length()<=11)
                            {
                                textView.setLayoutParams(lp3);
                            }
                              else {
                                textView.setLayoutParams(lp4);
                            }

                            break;


                        case 1:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + items.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
//                            amount = convertToEnglish(amount);
                            amount =String.valueOf(decimalFormat.format(Double.parseDouble(amount)));
                            textView.setText(convertToEnglish(amount));
//                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }



                tabLayout.addView(row);
            }
        }


        total_qty_text.setText(count+"");
        Log.e("countItem",""+count);
        discountItems.setText(convertToEnglish(decimalFormat.format(itemsDiscount))+"");

//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

       // dialogs.show();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Drawable bgDrawable = linearView.getBackground();
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        linearView.draw(canvas);

        return bit;// creates bitmap and returns the same
    }

    private Bitmap convertLayoutToImageEjape(Voucher voucher,List<Item> items) {
        LinearLayout linearView = null;
        String CusId=(voucher.getCustNumber());

        valueCheckHidPrice=obj.getHideValuForCustomer(CusId);
        Log.e("valueHidPriceBluDBase",""+valueCheckHidPrice);

        final Dialog dialogs = new Dialog(BluetoothConnectMenu.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.sewo30_printer_layout_ejaby);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname,store, tel, taxNo, vhNo, date, custname, note, vhType, paytype, total, discount, tax, ammont, textW,
                total_qty_text,salesName,textViewPrice,textviewTotal,amountText;
        LinearLayout linearArabicAmount=(LinearLayout) dialogs.findViewById(R.id.linearArabicAmount);
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);
        TableRow totalrow,discountrow,netrow,taxrow;
        totalrow=(TableRow) dialogs.findViewById(R.id.rowTotal);
        discountrow=(TableRow) dialogs.findViewById(R.id.rowDiscount);

        netrow=(TableRow) dialogs.findViewById(R.id.rowNetTotal);
        taxrow=(TableRow) dialogs.findViewById(R.id.rowTax);
        textViewPrice = (TextView) dialogs.findViewById(R.id.textViewPrice);
        textviewTotal = (TextView) dialogs.findViewById(R.id.textViewTotal);
//
        compname = (TextView) dialogs.findViewById(R.id.compname);
        amountText = (TextView) dialogs.findViewById(R.id.amountText);
        tel = (TextView) dialogs.findViewById(R.id.tel);
        taxNo = (TextView) dialogs.findViewById(R.id.taxNo);
        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
        date = (TextView) dialogs.findViewById(R.id.date);
        custname = (TextView) dialogs.findViewById(R.id.custname);
        salesName = (TextView) dialogs.findViewById(R.id.salesman_name);
        note = (TextView) dialogs.findViewById(R.id.note);
        vhType = (TextView) dialogs.findViewById(R.id.vhType);
        paytype = (TextView) dialogs.findViewById(R.id.paytype);
        total = (TextView) dialogs.findViewById(R.id.total);
        discount = (TextView) dialogs.findViewById(R.id.discount);
        tax = (TextView) dialogs.findViewById(R.id.tax);
        ammont = (TextView) dialogs.findViewById(R.id.ammont);
        textW = (TextView) dialogs.findViewById(R.id.wa1);
        store= (TextView) dialogs.findViewById(R.id.store);
        total_qty_text= (TextView) dialogs.findViewById(R.id.total_qty);
        String salesmaname=obj.getSalesmanName(voucher.getCustNumber());
        salesName.setText(salesmaname);
        // to hide price in voucher
        if (valueCheckHidPrice == 1) {
            totalrow.setVisibility(View.GONE);
            discountrow.setVisibility(View.GONE);
            netrow.setVisibility(View.GONE);
            taxrow.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.INVISIBLE);
            textviewTotal.setVisibility(View.INVISIBLE);

        }
        //total_qty

        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
        String voucherTyp = "";
        switch (voucher.getVoucherType()) {
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
//        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        if (companyInfo.getLogo()!=(null))
        {
            img.setImageBitmap(companyInfo.getLogo());
        }
        else{img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));}

        tel.setText("" + companyInfo.getcompanyTel());
        if(companyInfo.getTaxNo()!=0)
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
//        salesName.setText(obj.getAllSettings().get(0).getSalesMan_name());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);
        paytype.setText((voucher.getPayMethod() == 0 ? "Credit" : "Cash"));
        total.setText("" + voucher.getSubTotal());
        discount.setText(convertToEnglish(String.valueOf(decimalFormat.format( voucher.getTotalVoucherDiscount()))));
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());
        store.setText(Login.salesMan);
        int count=0;

        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);
        for (int j = 0; j < items.size(); j++) {
            if ((voucher.getVoucherNumber() == items.get(j).getVoucherNumber())&& (items.get(j).getVoucherType()== voucher.getVoucherType())) {//here chech voucher type
                count+=items.get(j).getQty();
                final TableRow row = new TableRow(BluetoothConnectMenu.this);


                for (int i = 0; i <= 7; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(500, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(BluetoothConnectMenu.this);
                    textView.setGravity(Gravity.LEFT);//test
                    textView.setTextSize(14);
//                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getResources().getColor(R.color.text_view_color));

                    switch (i) {
                        case 0:
                            textView.setText(items.get(j).getItemNo());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            if(valueCheckHidPrice==1)
                            {
                                textView.setText("\t\t\t\t\t\t");
                            }
                            else{
                                textView.setText("" + items.get(j).getPrice());
                                textView.setLayoutParams(lp2);
                            }

                            break;


                        case 4:
                            if(valueCheckHidPrice==1)
                            {
                                textView.setText("\t\t\t\t\t\t");
                            }
                            else{
                                String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
//                            amount = convertToEnglish(amount);
                                amount =String.valueOf(decimalFormat.format(Double.parseDouble(amount)));
                                textView.setText(convertToEnglish(amount));
//                            textView.setText(amount);
                                textView.setLayoutParams(lp2);
                            }

                            break;
                    }
                    row.addView(textView);


                }
                TextView textViews = new TextView(BluetoothConnectMenu.this);
                textViews.setTextSize(14);
                textViews.setPadding(0,0,0,5);
//                textViews.setTypeface(null, Typeface.BOLD);
                textViews.setTextColor(getResources().getColor(R.color.text_view_color));
                textViews.setText(items.get(j).getItemName());
//                rows.addView(textView);

                tabLayout.addView(row);
                tabLayout.addView(textViews);
            }
        }


        total_qty_text.setText(count+"");
        if(obj.getAllSettings().get(0).getTafqit()==1&& valueCheckHidPrice!=1)
        {
            linearArabicAmount.setVisibility(View.VISIBLE);
            amountText.setText( getArabicString(voucher.getNetSales()+""));
        }
        else {
            linearArabicAmount.setVisibility(View.GONE);

        }
       // Log.e("getArabicString","\t"+ getArabicString(voucher.getNetSales()+""));

//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

//        dialogs.show();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Drawable bgDrawable = linearView.getBackground();
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        linearView.draw(canvas);

        return bit;// creates bitmap and returns the same
    }
    private Bitmap convertLayoutToImageEjape_Stock(Voucher voucher,List<Item> items) {
        LinearLayout linearView = null;

        final Dialog dialogs = new Dialog(BluetoothConnectMenu.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.print_stock_request_sewo30);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);

        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);

        TextView compname,store, vhNo, date, custname, note,total_qty_text,salesName;
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);//
        compname = (TextView) dialogs.findViewById(R.id.compname);
        vhNo = (TextView) dialogs.findViewById(R.id.vhNo);
        date = (TextView) dialogs.findViewById(R.id.date);
        salesName = (TextView) dialogs.findViewById(R.id.salesman_name);
        note = (TextView) dialogs.findViewById(R.id.note);
        store= (TextView) dialogs.findViewById(R.id.store);
        total_qty_text= (TextView) dialogs.findViewById(R.id.total_qty);
        // total_qty

        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);

//        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        if (companyInfo.getLogo()!=(null))
        {
            img.setImageBitmap(companyInfo.getLogo());
        }
        else{img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));}
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        String salesmaname=obj.getSalesmanName(voucher.getCustNumber());
        salesName.setText(salesmaname);
        note.setText(voucher.getRemark());

        store.setText(Login.salesMan);
        int count=0;
        String s="";



        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);
        for (int j = 0; j < items.size(); j++) {
            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                count+=items.get(j).getQty();
                final TableRow row = new TableRow(BluetoothConnectMenu.this);


                for (int i = 0; i <3; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(500, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(BluetoothConnectMenu.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(14);
//                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getResources().getColor(R.color.text_view_color));

                    switch (i) {
                        case 0:
                            textView.setText(items.get(j).getItemNo());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            textView.setText("" + items.get(j).getQty());
                            textView.setLayoutParams(lp2);
//                            textView.setText("" + items.get(j).getItemName().substring(0,6));
//                            textView.setLayoutParams(lp2);
                            break;

                        case 2:

                            break;

                    }
                    row.addView(textView);


                }
                TextView textViews = new TextView(BluetoothConnectMenu.this);
                textViews.setTextSize(14);
                textViews.setPadding(0,0,0,5);
//                textViews.setTypeface(null, Typeface.BOLD);
                textViews.setTextColor(getResources().getColor(R.color.text_view_color));
                textViews.setText(items.get(j).getItemName());
//                rows.addView(textView);

                tabLayout.addView(row);
                tabLayout.addView(textViews);
            }
        }


        total_qty_text.setText(count+"");
        Log.e("countItem",""+count);
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();


        return bit;// creates bitmap and returns the same
    }

    int count=0;
    private Bitmap convertLayoutToImage_Body(Voucher voucher,List<Item> items,int visible) {
        LinearLayout linearView = null;
        final Dialog dialogs = new Dialog(BluetoothConnectMenu.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.body_voucher_print);
//            fill_theVocher( voucher);
        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);
        TextView  total, discount, tax, ammont, textW;
        textW = (TextView) dialogs.findViewById(R.id.wa1);
//        int count=0;
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
        TableRow row_header=(TableRow)dialogs.findViewById(R.id.row_header);
        if(visible==0)
        {
            row_header.setVisibility(View.VISIBLE);
        }
        else {
            row_header.setVisibility(View.INVISIBLE);
        }

        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);
        Log.e("itemSize",""+items.size());

        for (int j = 0; j < items.size(); j++) {

            if (voucher.getVoucherNumber() == items.get(j).getVoucherNumber()) {
                count+=items.get(j).getQty();
                final TableRow row = new TableRow(BluetoothConnectMenu.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(500, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(4);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(BluetoothConnectMenu.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(14);
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getResources().getColor(R.color.text_view_color));

                    switch (i) {
                        case 0:
                            textView.setText(items.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + items.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
//                            amount = convertToEnglish(amount);
                            amount =String.valueOf(decimalFormat.format(Double.parseDouble(amount)));
                            textView.setText(convertToEnglish(amount));
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }


                tabLayout.addView(row);
            }
        }
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

        return bit;// creates bitmap and returns the same
    }
    private Bitmap convertLayoutToImage_Body_ejabi(Voucher voucher,List<Item> items,int visible) {
        LinearLayout linearView = null;
        final Dialog dialogs = new Dialog(BluetoothConnectMenu.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.body_voucher_print_ejabe);
//            fill_theVocher( voucher);
        TextView doneinsewooprint = (TextView) dialogs.findViewById(R.id.done);
        TextView  total, discount, tax, ammont, textW;
        textW = (TextView) dialogs.findViewById(R.id.wa1);
//        int count=0;
        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
        TableRow row_header=(TableRow)dialogs.findViewById(R.id.row_header);
        if(visible==0)
        {
            row_header.setVisibility(View.VISIBLE);
        }
        else {
            row_header.setVisibility(View.INVISIBLE);
        }

        if (obj.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        } else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);
        Log.e("itemSize",""+items.size());

        for (int j = 0; j < items.size(); j++) {

           if ((voucher.getVoucherNumber() == items.get(j).getVoucherNumber())&& (items.get(j).getVoucherType()== voucher.getVoucherType())){
                count+=items.get(j).getQty();
                final TableRow row = new TableRow(BluetoothConnectMenu.this);


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(500, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(4);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(BluetoothConnectMenu.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(14);
//                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getResources().getColor(R.color.text_view_color));

                    switch (i) {
                        case 0:
                            textView.setText(items.get(j).getItemNo());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            } else {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (obj.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + items.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (items.get(j).getQty() * items.get(j).getPrice() - items.get(j).getDisc());
//                            amount = convertToEnglish(amount);
                            amount =String.valueOf(decimalFormat.format(Double.parseDouble(amount)));
                            textView.setText(convertToEnglish(amount));
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }

                TextView textViews = new TextView(BluetoothConnectMenu.this);
                textViews.setTextSize(14);
                textViews.setPadding(0,0,0,5);
//                textViews.setTypeface(null, Typeface.BOLD);
                textViews.setTextColor(getResources().getColor(R.color.text_view_color));
                textViews.setText(items.get(j).getItemName());

                tabLayout.addView(row);
                tabLayout.addView(textViews);
            }
        }
        linearView = (LinearLayout) dialogs.findViewById(R.id.ll);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

        return bit;// creates bitmap and returns the same
    }

}
