package com.dr7.salesmanmanager;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.ganesh.intermecarabic.Arabic864;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class SalesInvoice extends Fragment {


    public ListView itemsListView;
    public static List<Item> items;
    public ItemsListAdapter itemsListAdapter;
    private ImageButton addItemImgButton2, custInfoImgButton, SaveData;
    private ImageView connect;
    private RadioGroup paymentTermRadioGroup, voucherTypeRadioGroup;
    private RadioButton cash, credit;
    private EditText remarkEditText;
    private ImageButton newImgBtn;
    private double subTotal, totalTaxValue, netTotal;
    public double totalDiscount;
    private TextView taxTextView, subTotalTextView, netTotalTextView;
    public TextView discTextView;
    public ImageButton discountButton;
    private DecimalFormat decimalFormat;
    public static TextView voucherNumberTextView, Customer_nameSales;

    private static DatabaseHandler mDbHandler;
    private int voucherType = 504;
    private int voucherNumber;
    private int payMethod;
    static int index;
    static String rowToBeUpdated[] = {"", "", "", "", "", "", "", ""};

    boolean clicked = false;

    public static Voucher voucher;
    public static List<Item> itemsList;

    bluetoothprinter object;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
   /* public static void test2(){

        Customer_nameSales.setText(CustomerListFragment.Customer_Name.toString());
    }*/

    public SalesInvoice() {
        // Required empty public constructor
    }

    public List<Item> getItemsList() {
        return this.items;
    }

    public interface SalesInvoiceInterface {
        public void displayFindItemFragment();

        public void displayCustInfoFragment();

        public void displayDiscountFragment();

        public void displayUpdateItems();

        public void displayFindItemFragment2();
    }


    SalesInvoiceInterface salesInvoiceInterfaceListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sales_invoice, container, false);
        decimalFormat = new DecimalFormat("##.000");
        mDbHandler = new DatabaseHandler(getActivity());
        object = new bluetoothprinter();

        addItemImgButton2 = (ImageButton) view.findViewById(R.id.addItemImgButton2);
        custInfoImgButton = (ImageButton) view.findViewById(R.id.custInfoImgBtn);
        connect = (ImageView) view.findViewById(R.id.balanceImgBtn);
        voucherNumberTextView = (TextView) view.findViewById(R.id.voucherNumber);
        Customer_nameSales = (TextView) view.findViewById(R.id.invoiceCustomerName);
        paymentTermRadioGroup = (RadioGroup) view.findViewById(R.id.paymentTermRadioGroup);
        voucherTypeRadioGroup = (RadioGroup) view.findViewById(R.id.transKindRadioGroup);
        cash = (RadioButton) view.findViewById(R.id.cashRadioButton);
        credit = (RadioButton) view.findViewById(R.id.creditRadioButton);
        remarkEditText = (EditText) view.findViewById(R.id.remarkEditText);
        newImgBtn = (ImageButton) view.findViewById(R.id.newImgBtn);
        SaveData = (ImageButton) view.findViewById(R.id.saveInvoiceData);
        discountButton = (ImageButton) view.findViewById(R.id.discButton);

        discTextView = (TextView) view.findViewById(R.id.discTextView);
        subTotalTextView = (TextView) view.findViewById(R.id.subTotalTextView);
        taxTextView = (TextView) view.findViewById(R.id.taxTextView);
        netTotalTextView = (TextView) view.findViewById(R.id.netSalesTextView1);

        itemsList = new ArrayList<>();

        if (MainActivity.checknum == 1)
            Customer_nameSales.setText(CustomerListShow.Customer_Name.toString());
        else
            Customer_nameSales.setText("Customer Name");

        if (CustomerListShow.CashCredit == 0) {
            cash.setChecked(true);
            credit.setChecked(false);
        } else {
            credit.setChecked(true);
            cash.setChecked(false);
        }

        voucherTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                paymentTermRadioGroup.setVisibility(View.VISIBLE);
                switch (checkedId) {
                    case R.id.salesRadioButton:
                        voucherType = 504;
                        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                        String vn1 = voucherNumber + "";
                        voucherNumberTextView.setText(vn1);
                        break;
                    case R.id.retSalesRadioButton:
                        voucherType = 506;
                        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                        String vn2 = voucherNumber + "";
                        voucherNumberTextView.setText(vn2);
                        break;
                    case R.id.orderRadioButton:
                        voucherType = 508;
                        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
                        String vn3 = voucherNumber + "";
                        voucherNumberTextView.setText(vn3);
                        paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        paymentTermRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.creditRadioButton:
                        payMethod = 0;
                        break;
                    case R.id.cashRadioButton:
                        payMethod = 1;
                        break;
                }
            }
        });

        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);

        discountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salesInvoiceInterfaceListener.displayDiscountFragment();
            }
        });

        addItemImgButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salesInvoiceInterfaceListener.displayFindItemFragment2();
            }
        });
        custInfoImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salesInvoiceInterfaceListener.displayCustInfoFragment();
            }
        });

        itemsListView = (ListView) view.findViewById(R.id.itemsListView);
        items = new ArrayList<>();

        itemsListAdapter = new ItemsListAdapter(getActivity(), items);
        itemsListView.setAdapter(itemsListAdapter);


        itemsListView.setOnItemLongClickListener(onItemLongClickListener);

        newImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_clear));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearLayoutData();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().show();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.findBT();
            }
        });

        SaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked = false;
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_save));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));


                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int l) {

                        if (!clicked) {
                            clicked = true;
                            int listSize = itemsListView.getCount();
                            if (listSize == 0)
                                Toast.makeText(getActivity(), "Fill Your List Please", Toast.LENGTH_LONG).show();
                            else {

                                String remark = " " + remarkEditText.getText().toString();

                                Date currentTimeAndDate = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String voucherDate = df.format(currentTimeAndDate);

                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
                                String voucherYear = df2.format(currentTimeAndDate);

                                int salesMan = Integer.parseInt(Login.salesMan);

                                DiscountFragment obj = new DiscountFragment();
                                double discountValue = obj.getDiscountValue();
                                double discountPerc = obj.getDiscountPerc();

                                double totalDisc = Double.parseDouble(discTextView.getText().toString());
                                double subTotal = Double.parseDouble(subTotalTextView.getText().toString());
                                double tax = Double.parseDouble(taxTextView.getText().toString());
                                double netSales = Double.parseDouble(netTotalTextView.getText().toString());

                                voucher = new Voucher(0, voucherNumber, voucherType, voucherDate,
                                        salesMan, discountValue, discountPerc, remark, payMethod,
                                        0, totalDisc, subTotal, tax, netSales, CustomerListShow.Customer_Name,
                                        CustomerListShow.Customer_Account, Integer.parseInt(voucherYear));

                                mDbHandler.addVoucher(voucher);


                                for (int i = 0; i < items.size(); i++) {

                                    Item item = new Item(0, voucherYear, voucherNumber, voucherType, items.get(i).getUnit(), items.get(i).getItemNo(), items.get(i).getItemName(),
                                            items.get(i).getQty(), items.get(i).getPrice(), items.get(i).getDisc(), items.get(i).getDiscPerc(),
                                            items.get(i).getBonus(), 0, items.get(i).getTaxValue(), items.get(i).getTaxPercent(), 0);

                                    itemsList.add(item);
                                    mDbHandler.addItem(item);
                                }

//                                Intent intent = new Intent(getActivity(), BluetoothConnectMenu.class);
//                                startActivity(intent);

                                try {
                                    findBT();
                                    openBT();
                                } catch (IOException ex) {
                                }

//                            Intent intent = new Intent(getActivity(), bluetoothprinter.class);
//                            startActivity(intent);

                                mDbHandler.setMaxSerialNumber(voucherType, voucherNumber);

                            }
                            clearLayoutData();
                        }
                    }
                });

                builder.setNegativeButton(getResources(). getString(R.string.app_cancel), null);
                builder.create().

                        show();
            }
        });

        return view;
    }

    public void setListener(SalesInvoiceInterface listener) {
        this.salesInvoiceInterfaceListener = listener;
    }

    public OnItemLongClickListener onItemLongClickListener =
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.app_select_option));
                    builder.setCancelable(true);
                    builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                    builder.setItems(R.array.list_items_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    items.remove(position);
                                    itemsListView.setAdapter(itemsListAdapter);
                                    calculateTotals();
                                    break;
//                                case 1:
//                                    salesInvoiceInterfaceListener.displayUpdateItems();
//
//                                    rowToBeUpdated[0] = items.get(position).getItemNo();
//                                    rowToBeUpdated[1] = items.get(position).getItemName();
//                                    rowToBeUpdated[2] = items.get(position).getQty() + "";
//                                    rowToBeUpdated[3] = items.get(position).getPrice() + "";
//                                    rowToBeUpdated[4] = items.get(position).getBonus() + "";
//                                    rowToBeUpdated[5] = items.get(position).getDiscPerc().replaceAll("[%:,]","");
//                                    rowToBeUpdated[6] = items.get(position).getDiscType() + "";
//                                    rowToBeUpdated[7] = items.get(position).getUnit() + "";
//                                    index = position;
//                                    break;
                                case 1:
                                    clearItemsList();
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                    return true;
                }
            };

    public String[] getIndexToBeUpdated() {
        return rowToBeUpdated;
    }

    public int getIndex() {
        return index;
    }

    private void clearItemsList() {
        items.clear();
        itemsListAdapter.setItemsList(items);
        itemsListAdapter.notifyDataSetChanged();
    }

    private void clearLayoutData() {
        paymentTermRadioGroup.check(R.id.creditRadioButton);
        remarkEditText.setText("");
        clearItemsList();
        calculateTotals();
        subTotalTextView.setText("0.000");
        taxTextView.setText("0.000");
        netTotalTextView.setText("0.000");
        discTextView.setText("0.000");
        subTotal = 0;
        totalTaxValue = 0;
        netTotal = 0;
        totalDiscount = 0;
        calculateTotals();

        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
    }

    public void calculateTotals() {
        double itemTax, itemTotal, itemTotalAfterTax, itemTotalPerc, itemDiscVal;

        subTotal = 0.0;
        totalTaxValue = 0.0;
        netTotal = 0.0;

        totalDiscount = 0;

        if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0) {
            try {
                totalDiscount = Float.parseFloat(discTextView.getText().toString());
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }

            for (int i = 0; i < items.size(); i++) {
                itemTotal = items.get(i).getAmount();
                itemTax = items.get(i).getAmount() * items.get(i).getTaxPercent() * 0.01;
                itemTotalAfterTax = items.get(i).getAmount() + itemTax;
                subTotal = subTotal + itemTotal;
            }

            for (int i = 0; i < items.size(); i++) {
                itemTotal = items.get(i).getAmount();
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal = (itemTotalPerc * totalDiscount);
                items.get(i).setTotalDiscVal(itemDiscVal);
                itemTotal = itemTotal - itemDiscVal;
                itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
                items.get(i).setTaxValue(itemTax);
                totalTaxValue = totalTaxValue + itemTax;
            }

            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue;

        } else {
            try {
                totalDiscount = Float.parseFloat(discTextView.getText().toString());
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }

            for (int i = 0; i < items.size(); i++) {

                itemTax = items.get(i).getAmount() -
                        (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));

                itemTotal = items.get(i).getAmount() - itemTax;
                itemTotalAfterTax = items.get(i).getAmount();
                subTotal = subTotal + itemTotal;
            }

            for (int i = 0; i < items.size(); i++) {

                itemTax = items.get(i).getAmount() -
                        (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));

                itemTotal = items.get(i).getAmount() - itemTax;
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal = (itemTotalPerc * totalDiscount);
                items.get(i).setTotalDiscVal(itemDiscVal);
                itemTotal = itemTotal - itemDiscVal;

                itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;


                items.get(i).setTaxValue(itemTax);
                totalTaxValue = totalTaxValue + itemTax;
            }

            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue;

        }


        subTotalTextView.setText(String.valueOf(decimalFormat.format(subTotal)));
        taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));
        netTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));

    }

    public double getItemsTotal() {
        double total = 0;

        for (Item i : items) {
            total = total + i.getAmount();
        }

        return total;
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // MP300 is the name of the bluetooth printer device07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ C4:73:1E:67:29:6C
                    /*07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ E8:99:C4:FF:B1:AC
                    07-28 13:20:10.946  10461-10461/com.example.printer E/sex﹕ 0C:A6:94:35:11:27*/

                    /*Log.e("sex",device.getName());*/
//                    if (device.getName().equals("mobile printer")) { // PR3-30921446556
                    /*Log.e("sex1",device.getAddress());*/
                    mmDevice = device;
//                        break;
//                    }
                }
            }
//            myLabel.setText("Bluetooth Device Found");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

//            myLabel.setText("Bluetooth Opened");
            sendData();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
//                                                myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * This will send data to be printed by the bluetooth printer
     */
    void sendData() throws IOException {
        try {

            // the text typed by the user
            String msg = "";

            msg += " " + "\n" +
                    "_______________________" + "\n" +
                    "ملاحظة: " + voucher.getRemark() + "\n" +
                    "الضريبة: " + voucher.getTax() + "  النت: " + voucher.getNetSales() + "\n" +
                    "المجموع الجزئي: " + voucher.getSubTotal() + "  الخصم: " + voucher.getVoucherDiscount() + "\n" + "\n";

            for (int i = 0; i < itemsList.size(); i++) {
                double amount = itemsList.get(i).getQty() * itemsList.get(i).getPrice() - itemsList.get(i).getDisc();
                String text = "السلعة:" + itemsList.get(i).getItemName() + " الكمية:" + itemsList.get(i).getQty() + " الزيادة:" +
                        itemsList.get(i).getBonus() + " المجموع:" + amount;
                msg += text + "\n";
            }

            msg += "طريقة الدفع : " + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n" +
                    "اسم العميل: " + voucher.getCustName() + "\n" +
                    "\n" + "رقم الفاتورة : " + voucher.getVoucherNumber() + "   التاريخ: " + voucher.getVoucherDate() + "\n";


            Arabic864 arabic = new Arabic864();
            byte[] arabicArr = arabic.Convert(msg, false);

            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            for (int i = 0; i <= numOfCopy; i++)
                mmOutputStream.write(arabicArr);

            closeBT();
            // tell the user data were sent
//                myLabel.setText("Data Sent");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
//            myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}// class salesInvoice
