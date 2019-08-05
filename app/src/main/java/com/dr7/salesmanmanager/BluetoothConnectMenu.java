package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Port.AlertView;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static com.dr7.salesmanmanager.ReceiptVoucher.paymentsforPrint;

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
    static  String idname;
    DatabaseHandler obj;
    String getData;
    List <Item>allStudents;

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

        while(iter.hasNext()) {
            BluetoothDevice pairedDevice = (BluetoothDevice)iter.next();
            if (this.bluetoothPort.isValidAddress(pairedDevice.getAddress())) {
                this.remoteDevices.add(pairedDevice);
                this.adapter.add(pairedDevice.getName() + "\n[" + pairedDevice.getAddress() + "] [Paired]");
            }
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bluetooth_menu);
        this.btAddrBox = (EditText)this.findViewById(R.id.EditTextAddressBT);
        this.connectButton = (Button)this.findViewById(R.id.ButtonConnectBT);
        BluetoothConnectMenu.this.connectButton.setEnabled(true);
        this.searchButton = (Button)this.findViewById(R.id.ButtonSearchBT);
        this.list = (ListView)this.findViewById(R.id.BtAddrListView);
        this.chkDisconnect = (CheckBox)this.findViewById(R.id.check_disconnect);
        this.chkDisconnect.setChecked(true);
        this.context = this;
        obj=new DatabaseHandler(BluetoothConnectMenu.this);


//
        getData = getIntent().getStringExtra("printKey");
//        Bundle bundle = getIntent().getExtras();
//         allStudents = (List<Item>) bundle.get("ExtraData");
//
//         Log.e("all",allStudents.get(0).getBarcode());

      Log.e("printKey",""+getData);
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
        this.adapter = new ArrayAdapter(BluetoothConnectMenu.this ,R.layout.cci );

        this.list.setAdapter(this.adapter);
        this.addPairedDevices();
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BluetoothDevice btDev = (BluetoothDevice)BluetoothConnectMenu.this.remoteDevices.elementAt(arg2);

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
                BluetoothDevice remoteDevice = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
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
                    BluetoothDevice device = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
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

                Toast toast = Toast.makeText(BluetoothConnectMenu.this.context, "Now Printing ", Toast.LENGTH_SHORT);
                toast.show();


                int count =Integer.parseInt(getData);
                CPCLSample2 sample = new CPCLSample2(BluetoothConnectMenu.this);
                sample.selectContinuousPaper();
                try {
                    CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//                  Log.e("salesVoucher","=" + SalesInvoice.items.get(0).getVoucherNumber());
                    if((count==0)||(count==1)) {
//                        sample.dmStamp(1,companyInfo.getLogo());
                        sample.printMultilingualFont(count,companyInfo.getLogo());

//                        itemForPrint.clear();
                    }else{
                        if(count==2)
                        {
                        sample.printMultilingualFontCash();
                        paymentsforPrint.clear();
                    }
                        else if(count==3){
                            sample.printMultilingualFontCashReport();

                        }
                    }

                    finish();
//                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.clear);
//                    Bitmap testB =convertLayoutToImage(vouch,items);
//                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                    testB.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                    sample.dmStamp(1,testB);
//                    sample.imageTest(1,testB);
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

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
}
