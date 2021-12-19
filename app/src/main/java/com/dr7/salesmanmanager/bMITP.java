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
import com.dr7.salesmanmanager.Reports.VouchersReport;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.ImportJason.voucherReturn;
import static com.dr7.salesmanmanager.PrintPayment.pay1;
import static com.dr7.salesmanmanager.PrintPayment.paymentPrinter;
import static com.dr7.salesmanmanager.PrintVoucher.items;
import static com.dr7.salesmanmanager.PrintVoucher.vouch1;
import static com.dr7.salesmanmanager.ReceiptVoucher.paymentsforPrint;
import static com.dr7.salesmanmanager.Reports.AccountReport.acount_report_list;
import static com.dr7.salesmanmanager.ReturnByVoucherNo.listItemsMain;
import static com.dr7.salesmanmanager.ReturnByVoucherNo.returblistItemsMain;
import static com.dr7.salesmanmanager.SalesInvoice.finishPrint;
import static com.dr7.salesmanmanager.SalesInvoice.itemForPrint;
import static com.dr7.salesmanmanager.SalesInvoice.itemForPrintLast;
import static com.dr7.salesmanmanager.SalesInvoice.vouchLast;
import static com.dr7.salesmanmanager.SalesInvoice.voucher;
import static com.dr7.salesmanmanager.StockRequest.clearData;

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
    LinearLayout mainLinearPrinting;
    TextView text_hideDialog;
    double itemDiscount=0;

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
            else {
                Log.e("lastConnAddr",""+lastConnAddr);
                fWriter.close();
            }

            fWriter.close();
        } catch (FileNotFoundException var3) {
            Log.e("BluetoothConnectMenu1", var3.getMessage(), var3);
            if(getData.equals("6"))
            { clearData.setText("1");
                finish();

//                Intent i=new Intent(context,Stock_Activity.class);
//                startActivity(i);
            }


        } catch (IOException var4) {
            Log.e("BluetoothConnectMenu2", var4.getMessage(), var4);
        }
        catch (Exception e)

        {
            Log.e("BluetoothConnectMenu3", e.getMessage());
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
        Log.e("remoteDevices",""+remoteDevices.size());
//        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bluetooth_menu);
        this.mainLinearPrinting= (LinearLayout) this.findViewById(R.id.mainLinearPrinting);
        text_hideDialog = (TextView) this.findViewById(R.id.text_hideDialog);
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
        BluetoothDevice btDev = null;

        if(obj.getAllSettings().size()!=0)
        {
            if(obj.getAllSettings().get(0).getApproveAdmin()==0){
                mainLinearPrinting.setVisibility(View.VISIBLE);
                text_hideDialog.setVisibility(View.GONE);
                this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                BluetoothDevice btDev = (BluetoothDevice) bMITP.this.remoteDevices.elementAt(0);
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
            }
            else {
                mainLinearPrinting.setVisibility(View.GONE);
                text_hideDialog.setVisibility(View.VISIBLE);
                if(remoteDevices.size()!=0)
                {

                    try {
                        btDev = (BluetoothDevice) bMITP.this.remoteDevices.elementAt(0);
                    }
                    catch (Exception e)
                    {       }

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
                else {
                    new SweetAlertDialog(bMITP.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getResources().getString(R.string.warning_message))
                            .setContentText(getResources().getString(R.string.checkBlutoothPrinterPaired))
                            .setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                }
                            })
                            .show();

                }
            }
        }


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
                    if(obj.getAllSettings().size()!=0) {
                        if (obj.getAllSettings().get(0).getApproveAdmin() == 0) {
                            if (bMITP.this.bluetoothPort.isValidAddress(remoteDevice.getAddress())) {
                                bMITP.this.remoteDevices.add(remoteDevice);
                                bMITP.this.adapter.add(key);
                            }
                        }
                        else {
                            bMITP.this.remoteDevices.add(remoteDevice);
                            bMITP.this.adapter.add(key);
                        }

                        }

//                    if (bMITP.this.bluetoothPort.isValidAddress(remoteDevice.getAddress())) {

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
            Log.e("BluetoothConnectMenu2", var2.getMessage(), var2);
        } catch (InterruptedException var3) {
            Log.e("BluetoothConnectMenu3", var3.getMessage(), var3);
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
        if(remoteDevices.size()!=0)
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
           String s="";
            this.dialog.setTitle(" Try Connect ");
            this.dialog.setMessage("Please Wait ....");
            this.dialog.show();
            super.onPreExecute();
        }

        protected Integer doInBackground(BluetoothDevice... params) {
            Integer retVal = null;
           String s="";
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

//                Toast toast = Toast.makeText(bMITP.this.context, "Now Printing ", Toast.LENGTH_SHORT);
//                toast.show();


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
                          if(printShape==0) {//Normal print (Arabic)
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                                  sample.printMultilingualFontEsc3(0, printVoucher, itemPrint);

                              }
                          }else {// just for ejabi customer (English Voucher ) and for  Large Name
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
                              finishPrint.setText("finish");

                          }}
                          else {
                          for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                              sample.printMultilingualFontEscEjapy(1, printVoucher, itemPrint);
                          }

                      }
                          break;

                      case 2:

                          if(printShape==0){
                          for(int i=0;i<settings;i++) {
                              sample.printMultilingualFontPayCash(0);

                          }}else {
                              for(int i=0;i<settings;i++) {
                                  sample.printMultilingualFontPayCash_EJABI(0);

                              }
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
                          if(printShape==0){
                          for(int i=0;i<settings;i++) {
                              sample.printMultilingualFontPayCash(1);
                          }}else {
                              for(int i=0;i<settings;i++) {
                                  sample.printMultilingualFontPayCash_EJABI(1);
                              }
                          }
                          paymentPrinter.clear();
                          break;
                      case 5:
                          for(int i=0;i<settings;i++) {
                              sample.printMultilingualFont_AccountReport(acount_report_list);
                          }

                          break;
                      case 6:
//                          if(printShape==0)
//                          {
//
//
//                          }
//                          else{

                              for(int i=0;i<settings;i++) {
                                  sample.printMultilingualFontStock_EJABI(0);
                              }



//                          }


                          break;
                      case 7:// print last voucher
                          printVoucher = vouchLast;
                          itemPrint = itemForPrintLast;
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
                          //*****************************************************************************
                      case 9:// print inventory report

//                          if(printShape==0) {
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
//                                  itemsInventoryPrint
                                  sample.printMultilingualFontEscInventory();
                              }
//                          }
//                          else {
//                              for (int i = 0; i < settings; i++) {
////                              sample.printMultilingualFontEsc(0);
//                                  sample.printMultilingualFontEscEjapy(0, printVoucher, itemPrint);
//                              }
//
//                          }

                          break;
                      case 10:
                  //        printVoucher = voucherReturn;
                      //    itemPrint = listItemsMain;

                          DatabaseHandler databaseHandler=new DatabaseHandler(context);
                          int x=databaseHandler.getLastVoucherNo(506);
                          Log.e("x==",x+"");
                         // voucherReturn=dataBase.getAllVouchers_VoucherNo(Integer.parseInt(voucherNo),504);

                          printVoucher =databaseHandler.getAllVouchers_VoucherNo(x,506);
                          Log.e("printVoucher==",printVoucher.getVoucherNumber()+"");



                     //  int y=databaseHandler.getMaxFromVoucherMaster(506);

                          returblistItemsMain=databaseHandler.getAllItems();
                          itemPrint = returblistItemsMain;




                       //   itemPrint=databaseHandler.
//                          convertLayoutToImageW(bMITP.this,sample,settings);
                          if(printShape==0) {//Normal print (Arabic)
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                                  sample.printMultilingualFontEsc3(0, printVoucher, itemPrint);

                              }
                          }else {// just for ejabi customer (English Voucher ) and for  Large Name
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                                  sample.printMultilingualFontEscEjapy(0, printVoucher, itemPrint);
                              }

                          }
                          break;
                      case 12:
                          //        printVoucher = voucherReturn;
                          //    itemPrint = listItemsMain;


                          printVoucher = VouchersReport.VocherToPrint;
                          itemPrint = VouchersReport.itemsToPrint;

                          if(printShape==0) {//Normal print (Arabic)
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                                  sample.printMultilingualFontEsc3(0, printVoucher, itemPrint);

                              }
                          }else {// just for ejabi customer (English Voucher ) and for  Large Name
                              for (int i = 0; i < settings; i++) {
//                              sample.printMultilingualFontEsc(0);
                                  sample.printMultilingualFontEscEjapy(0, printVoucher, itemPrint);
                              }

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


}
