package com.dr7.salesmanmanager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.QtyOffers;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.ganesh.intermecarabic.Arabic864;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class SalesInvoice extends Fragment {
    private static String smokeGA = "دخان";
    private static String smokeGE = "SMOKE";
    Bitmap testB;
    byte[] printIm;
    PrintPic printPic;
    private static int salesMan;
    static int index;
    public static List<Payment> payment_unposted ;
    public static List<Voucher> sales_voucher_unposted ;
    public List<QtyOffers> list_discount_offers;
    double max_cridit, available_balance, account_balance, cash_cridit, unposted_sales_vou,unposted_payment, unposted_voucher;
    public ListView itemsListView;
    public static List<Item> items;
    public ItemsListAdapter itemsListAdapter;
    private ImageButton addItemImgButton2, custInfoImgButton, SaveData;
    private ImageView connect, pic;
    private RadioGroup paymentTermRadioGroup, voucherTypeRadioGroup;
    private RadioButton cash, credit, retSalesRadioButton, salesRadioButton, orderRadioButton;
    private EditText remarkEditText;
    private ImageButton newImgBtn;
    private double subTotal, totalTaxValue, netTotal;
    public double totalDiscount=0,discount_oofers_total_cash=0, discount_oofers_total_credit=0,sum_discount=0;;
    private TextView taxTextView, subTotalTextView, netTotalTextView;
    public TextView discTextView;
    public ImageButton discountButton;
    private DecimalFormat decimalFormat;
    public static TextView voucherNumberTextView, Customer_nameSales;
     static ArrayList<Item> itemForPrint;
    private static DatabaseHandler mDbHandler;
    public static int voucherType = 504;
    private int voucherNumber;
  public  int payMethod;
    boolean isFinishPrint=false;
    double total_Qty=0.0;
    double totalQty_forPrint=0;

    static String rowToBeUpdated[] = {"", "", "", "", "", "", "", ""};

    boolean clicked = false;
    String itemsString = "";
    String itemsString2 = "";

    static Voucher voucher;
   static List<Item> itemsList;

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
    double discountValue;
    double discountPerc;
    volatile boolean stopWorker;
    DecimalFormat threeDForm ;
//    static Voucher voucherSale;
//    static List<Item> itemSale;
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
//        try {
//            closeBT();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        decimalFormat = new DecimalFormat("##.00");
        mDbHandler = new DatabaseHandler(getActivity());
        list_discount_offers=new ArrayList<>();
        object = new bluetoothprinter();
        itemForPrint=new ArrayList<>();
        threeDForm = new DecimalFormat("0.000");

        addItemImgButton2 = (ImageButton) view.findViewById(R.id.addItemImgButton2);
        custInfoImgButton = (ImageButton) view.findViewById(R.id.custInfoImgBtn);
        connect = (ImageView) view.findViewById(R.id.balanceImgBtn);
        voucherNumberTextView = (TextView) view.findViewById(R.id.voucherNumber);
        Customer_nameSales = (TextView) view.findViewById(R.id.invoiceCustomerName);
        paymentTermRadioGroup = (RadioGroup) view.findViewById(R.id.paymentTermRadioGroup);
        voucherTypeRadioGroup = (RadioGroup) view.findViewById(R.id.transKindRadioGroup);
        cash = (RadioButton) view.findViewById(R.id.cashRadioButton);
        credit = (RadioButton) view.findViewById(R.id.creditRadioButton);
        retSalesRadioButton = (RadioButton) view.findViewById(R.id.retSalesRadioButton);
        salesRadioButton = (RadioButton) view.findViewById(R.id.salesRadioButton);
        orderRadioButton = (RadioButton) view.findViewById(R.id.orderRadioButton);
        remarkEditText = (EditText) view.findViewById(R.id.remarkEditText);
        newImgBtn = (ImageButton) view.findViewById(R.id.newImgBtn);
        SaveData = (ImageButton) view.findViewById(R.id.saveInvoiceData);
        discountButton = (ImageButton) view.findViewById(R.id.discButton);
        pic = (ImageView) view.findViewById(R.id.pic_sale);

        discTextView = (TextView) view.findViewById(R.id.discTextView);
        subTotalTextView = (TextView) view.findViewById(R.id.subTotalTextView);
        taxTextView = (TextView) view.findViewById(R.id.taxTextView);
        netTotalTextView = (TextView) view.findViewById(R.id.netSalesTextView1);

        itemsList = new ArrayList<>();


        custInfoImgButton.setVisibility(View.INVISIBLE);
        connect.setVisibility(View.INVISIBLE);

        if (MainActivity.checknum == 1)
            Customer_nameSales.setText(CustomerListShow.Customer_Name.toString());
        else
            Customer_nameSales.setText("Customer Name");

//        if (CustomerListShow.CashCredit == 0) {
//            credit.setChecked(true);
//            cash.setChecked(false);
//               payMethod = 0;
//
//        } else {
//            cash.setChecked(true);
//            credit.setChecked(false);
//              payMethod = 1;
//
//
//        }
        if (mDbHandler.getAllSettings().get(0).getPaymethodCheck() == 0) {
            credit.setChecked(true);
            cash.setChecked(false);
            payMethod = 0;

        } else {
            cash.setChecked(true);
            credit.setChecked(false);
            payMethod = 1;


        }
        voucherTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                paymentTermRadioGroup.setVisibility(View.VISIBLE);
                if (itemsListView.getCount() > 0) {
                    new android.support.v7.app.AlertDialog.Builder(getActivity())
                            .setTitle("Confirm Update")
                            .setCancelable(false)
                            .setMessage("Are you sure you want clear the list !")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    clearItemsList();

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
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Log.e("voucherType ", "" + voucherType);
                                    switch (voucherType) {
                                        case 504:
                                            voucherTypeRadioGroup.check(R.id.salesRadioButton);
//                                            salesRadioButton.setSelected(true);
//                                            retSalesRadioButton.setSelected(false);
//                                            orderRadioButton.setSelected(false);
                                            break;
                                        case 506:
                                            voucherTypeRadioGroup.check(R.id.retSalesRadioButton);
//                                            salesRadioButton.setSelected(false);
//                                            retSalesRadioButton.setSelected(true);
//                                            orderRadioButton.setSelected(false);
                                            break;
                                        case 508:
                                            voucherTypeRadioGroup.check(R.id.orderRadioButton);
//                                            salesRadioButton.setSelected(false);
//                                            retSalesRadioButton.setSelected(false);
//                                            orderRadioButton.setSelected(true);
                                            paymentTermRadioGroup.setVisibility(View.INVISIBLE);
                                            break;
                                    }
                                }
                            })
                            .show();
                } else {
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
            }
        });

        paymentTermRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.creditRadioButton:
                        payMethod = 0;
                        discTextView.setText("0.0");
                        netTotalTextView.setText("0.0");
                        netTotal = 0.0;
                        totalDiscount=0;
                        sum_discount=0;
                       // if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1)
                            clearLayoutData();

                        break;
                    case R.id.cashRadioButton:
                        payMethod = 1;
                        discTextView.setText("0.0");
                        netTotalTextView.setText("0.0");
                        netTotal = 0.0;
                        totalDiscount=0;
                        sum_discount=0;
                      //  if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1)
                            clearLayoutData();
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
                if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1) {
                    Log.e("discountButton", "=" + mDbHandler.getAllSettings().get(0).getNoOffer_for_credit());
                    if (payMethod == 0) {
                        salesInvoiceInterfaceListener.displayDiscountFragment();
                    } else {
                        Toast.makeText(getActivity(), "Sory, you can not add discount in cash invoice  .......", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    salesInvoiceInterfaceListener.displayDiscountFragment();
                }
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
                try {
                    closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemForPrint.clear();
                clicked = false;
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.app_confirm_dialog_save));
                builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
                builder.setPositiveButton(getResources().getString(R.string.app_ok), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int l) {

                        if (!clicked) {
                            Log.e("sales invoice","kkk");
                            clicked = true;
                            int listSize = itemsListView.getCount();
                            if (listSize == 0)
                                Toast.makeText(getActivity(), "Fill Your List Please", Toast.LENGTH_LONG).show();
                            else {
                                DiscountFragment obj = new DiscountFragment();
                                double discountValue = obj.getDiscountValue();
                                double discountPerc = obj.getDiscountPerc();

                                double totalDisc = Double.parseDouble(discTextView.getText().toString());
                                double subTotal = Double.parseDouble(subTotalTextView.getText().toString());
                                double tax = Double.parseDouble(taxTextView.getText().toString());
                                double netSales = Double.parseDouble(netTotalTextView.getText().toString());
                                if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1 && (discountValue / netSales) > mDbHandler.getAllSettings().get(0).getAmountOfMaxDiscount()) {
                                    Toast.makeText(getActivity(), "You have exceeded the upper limit of the discount", Toast.LENGTH_SHORT).show();

                                } else {

                                    String remark = " " + remarkEditText.getText().toString();

                                    Date currentTimeAndDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    String voucherDate = df.format(currentTimeAndDate);
                                    voucherDate = convertToEnglish(voucherDate);

                                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
                                    String voucherYear = df2.format(currentTimeAndDate);
                                    voucherYear = convertToEnglish(voucherYear);
                                    salesMan = Integer.parseInt(Login.salesMan);


                                    voucher = new Voucher(0, voucherNumber, voucherType, voucherDate,
                                            salesMan, discountValue, discountPerc, remark, payMethod,
                                            0, totalDisc, subTotal, tax, netSales, CustomerListShow.Customer_Name,
                                            CustomerListShow.Customer_Account, Integer.parseInt(voucherYear));
                                    if (payMethod == 0) {
                                        if (mDbHandler.getAllSettings().get(0).getCustomer_authorized() == 1)
                                        {

                                            if (customer_is_authrized()) {

                                                mDbHandler.addVoucher(voucher);
                                                Log.e("paymethod", "" + voucher.getPayMethod());
                                                for (int i = 0; i < items.size(); i++) {

                                                    Item item = new Item(0, voucherYear, voucherNumber, voucherType, items.get(i).getUnit(),
                                                            items.get(i).getItemNo(), items.get(i).getItemName(), items.get(i).getQty(), items.get(i).getPrice(),
                                                            items.get(i).getDisc(), items.get(i).getDiscPerc(), items.get(i).getBonus(), 0,
                                                            items.get(i).getTaxValue(), items.get(i).getTaxPercent(), 0);
                                                    totalQty_forPrint+=items.get(i).getQty();
                                                    Log.e("totalQty_forPrint",""+totalQty_forPrint);

                                                    itemsList.add(item);

                                                    mDbHandler.addItem(item);
                                                    itemForPrint.add(item);

                                                    if (voucherType != 506)
                                                        mDbHandler.updateSalesManItemsBalance1(items.get(i).getQty(), salesMan, items.get(i).getItemNo());
                                                    else
                                                        mDbHandler.updateSalesManItemsBalance2(items.get(i).getQty(), salesMan, items.get(i).getItemNo());

                                                }


                                                if (mDbHandler.getAllSettings().get(0).getWorkOnline() == 1) {
                                                    new JSONTask().execute();
                                                }

                                                if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {
                                                    Log.e("test",""+voucher.getTotalVoucherDiscount() );
//                                                try {
//                                                    findBT();
//                                                    openBT();f


                                                    int printer = mDbHandler.getPrinterSetting();

                                                    switch (printer) {
                                                        case 0:
                                                            Intent i = new Intent(getActivity().getBaseContext(), BluetoothConnectMenu.class);
                                                            i.putExtra("printKey", "1");
                                                            startActivity(i);
//                                                             lk30.setChecked(true);
                                                            break;
                                                        case 1:

                                                            try {
                                                                findBT();
                                                                openBT(1);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
//                                                             lk31.setChecked(true);
                                                            break;
                                                        case 2:

                                                            try {
                                                                findBT();
                                                                openBT(2);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
//                                                             lk32.setChecked(true);
                                                            break;
                                                        case 3:

                                                            try {
                                                                findBT();
                                                                openBT(3);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
//                                                             qs.setChecked(true);
                                                            break;
                                                        case 4:
                                                            printTally(voucher);
                                                            break;

                                                    }


//                                                } catch (IOException ex) {
//                                                }
                                                } else {
                                                    hiddenDialog();
                                                }
                                                mDbHandler.setMaxSerialNumber(voucherType, voucherNumber);

                                            }
                                            else {
                                                Toast.makeText(getActivity(), "Sorry, you are not authorized for this service to verify your financial account", Toast.LENGTH_SHORT).show();
                                            }
                                    }


                                    } else {
                                        Log.e("paymethod is", "cash");
                                        mDbHandler.addVoucher(voucher);
                                        Log.e("paymethod", "" + voucher.getPayMethod());


                                        for (int i = 0; i < items.size(); i++) {

                                            Item item = new Item(0, voucherYear, voucherNumber, voucherType, items.get(i).getUnit(),
                                                    items.get(i).getItemNo(), items.get(i).getItemName(), items.get(i).getQty(), items.get(i).getPrice(),
                                                    items.get(i).getDisc(), items.get(i).getDiscPerc(), items.get(i).getBonus(), items.get(i).getVoucherDiscount(),//0
                                                    items.get(i).getTaxValue(), items.get(i).getTaxPercent(), 0);

                                            itemsList.add(item);
                                            mDbHandler.addItem(item);
                                            itemForPrint.add(item);

                                            if (voucherType != 506)
                                                mDbHandler.updateSalesManItemsBalance1(items.get(i).getQty(), salesMan, items.get(i).getItemNo());
                                            else
                                                mDbHandler.updateSalesManItemsBalance2(items.get(i).getQty(), salesMan, items.get(i).getItemNo());

                                        }


                                        if (mDbHandler.getAllSettings().get(0).getWorkOnline() == 1) {
                                            new JSONTask().execute();
                                        }

                                        if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0) {
//                                            try {
//                                                findBT();
//                                                openBT();
//                                            } catch (IOException ex) {
//                                            }

                                            int printer = mDbHandler.getPrinterSetting();

                                            switch (printer) {
                                                case 0:
                                                    Intent i=new Intent(getActivity().getBaseContext(),BluetoothConnectMenu.class);
                                                    i.putExtra("printKey","1");
                                                    startActivity(i);
//                                                             lk30.setChecked(true);
                                                    break;
                                                case 1:

                                                    try {
                                                        findBT();
                                                        openBT(1);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
//                                                             lk31.setChecked(true);
                                                    break;
                                                case 2:

                                                    try {
                                                        findBT();
                                                        openBT(2);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
//                                                             lk32.setChecked(true);
                                                    break;
                                                case 3:

                                                    try {
                                                        findBT();
                                                        openBT(3);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
//                                                             qs.setChecked(true);
                                                    break;
                                                case 4:
                                                    printTally(voucher);
                                                    break;

                                            }





                                        } else {
                                            hiddenDialog();
                                        }
                                        mDbHandler.setMaxSerialNumber(voucherType, voucherNumber);


                                    }
                                }

                                clearLayoutData();
                            }
                            //not empty list
                        }
                    }//end ok save

                });

                builder.setNegativeButton(getResources().getString(R.string.app_cancel), null);
                builder.create().

                        show();
            }//end save data
        });
        //  Log.e("paymethod",""+voucher.getPayMethod());
        return view;
    }

    public void setListener(SalesInvoiceInterface listener) {
        this.salesInvoiceInterfaceListener = listener;
    }

    public boolean customer_is_authrized() {
        unposted_payment = 0;
      //  unposted_sales_vou = 0;
        double unposted_sales_cash=0,unposted_sales_credit=0;
        max_cridit = CustomerListShow.CreditLimit;
        cash_cridit = CustomerListShow.CashCredit;
        Log.e("max_cridit", "" + max_cridit + "casCre" + cash_cridit);
       // *******************************************************
        payment_unposted = mDbHandler.getAllPayments_customerNo(voucher.getCustNumber());
        for (int i = 0; i < payment_unposted.size(); i++) {
            Log.e("unposted_payment", "" + payment_unposted.size() + "\tcusNO" + voucher.getCustNumber());
            if (payment_unposted.get(i).getIsPosted() == 0) {
                unposted_payment += payment_unposted.get(i).getAmount();

            }
        }
        Log.e("unposted_payment", "" + unposted_payment + "\tcusNO" + voucher.getCustNumber());
        // *******************************************************
        sales_voucher_unposted=mDbHandler.getAllVouchers_CustomerNo(voucher.getCustNumber());
        Log.e("salesvouch_size",""+sales_voucher_unposted.size());
        for (int j=0;j<sales_voucher_unposted.size();j++)
        {
            if(sales_voucher_unposted.get(j).getIsPosted()==0 )
            {
                if( sales_voucher_unposted.get(j).getPayMethod()==0)
                unposted_sales_credit+=sales_voucher_unposted.get(j).getNetSales();
                else
                {
                    unposted_sales_cash+=sales_voucher_unposted.get(j).getNetSales();
                }


            }
        }

        Log.e("unposted_sales== ",""+unposted_sales_vou);

//        if(max_cridit>=cash_cridit) {
        available_balance = max_cridit - cash_cridit-unposted_sales_credit  + unposted_payment;
//        }
//        else
        Log.e("max_cridit", "small");
        Log.e("available", "" + available_balance);
        if (available_balance >= voucher.getNetSales())
            return true;
        else
            return false;

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
                                case 1:
//                                    salesInvoiceInterfaceListener.displayUpdateItems();
//                                    rowToBeUpdated[0] = items.get(position).getItemNo();
//                                    rowToBeUpdated[1] = items.get(position).getItemName();
//                                    rowToBeUpdated[2] = items.get(position).getQty() + "";
//                                    rowToBeUpdated[3] = items.get(position).getPrice() + "";
//                                    rowToBeUpdated[4] = items.get(position).getBonus() + "";
//                                    rowToBeUpdated[5] = items.get(position).getDiscPerc().replaceAll("[%:,]","");
//                                    rowToBeUpdated[6] = items.get(position).getDiscType() + "";
//                                    rowToBeUpdated[7] = items.get(position).getUnit() + "";


                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setContentView(R.layout.update_qty_dialog);

                                    final EditText qty = (EditText) dialog.findViewById(R.id.editText1);
                                    Button okButton = (Button) dialog.findViewById(R.id.button1);
                                    Button cancelButton = (Button) dialog.findViewById(R.id.button2);

                                    okButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            float availableQty = 0;
                                            List<Item> jsonItemsList = AddItemsFragment2.jsonItemsList;
                                            for (int i = 0; i < jsonItemsList.size(); i++) {
                                                if (items.get(position).getItemNo().equals(jsonItemsList.get(i).getItemNo())) {
                                                    availableQty = jsonItemsList.get(i).getQty();
                                                    break;
                                                }
                                            }
                                            Log.e("qty ", "" + availableQty + "  " + qty.getText().toString());
                                            if (mDbHandler.getAllSettings().get(0).getAllowMinus() == 1 ||
                                                    availableQty >= Float.parseFloat(qty.getText().toString()) ||
                                                    voucherType == 506) {
                                                items.get(position).setQty(Float.parseFloat(qty.getText().toString()));
                                                if (items.get(position).getDiscType() == 0)
                                                    items.get(position).setAmount(items.get(position).getQty() * items.get(position).getPrice() - items.get(position).getDisc());
                                                else
                                                    items.get(position).setAmount(items.get(position).getQty() * items.get(position).getPrice() - Float.parseFloat(items.get(position).getDiscPerc().replaceAll("[%:,]", "")));

                                                itemsListView.setAdapter(itemsListAdapter);
                                                calculateTotals();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), "Insufficient Quantity", Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });

                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();

                                    break;
                                case 2:
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
//        paymentTermRadioGroup.check(R.id.creditRadioButton);
        remarkEditText.setText("");
        clearItemsList();
//        calculateTotals();
        subTotalTextView.setText("0.000");
        taxTextView.setText("0.000");
        netTotalTextView.setText("");
        netTotalTextView.setText("0.000");
        discTextView.setText("0.000");
        subTotal =0.0;
        totalTaxValue =0.0;
        netTotal = 0.0;
        totalDiscount = 0.0;
        sum_discount=0.0;
        items.clear();
        itemsList.clear();
//        calculateTotals();

        voucherNumber = mDbHandler.getMaxSerialNumber(voucherType) + 1;
        String vn = voucherNumber + "";
        voucherNumberTextView.setText(vn);
    }

    public void calculateTotals() {

        discTextView.setText("0.0");
        netTotalTextView.setText("0.0");
//        calculateTotals_cridit();
        double itemTax, itemTotal, itemTotalAfterTax,
                itemTotalPerc, itemDiscVal, posPrice,totalQty=0;
        //**********************************************************************
        list_discount_offers=mDbHandler.getDiscountOffers();
        Log.e("list_discount_offers",""+list_discount_offers);
        String itemGroup;
        subTotal = 0.0;
        totalTaxValue = 0.0;
        netTotal = 0.0;
        totalDiscount=0;
        sum_discount=0;


        //Include tax
        if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0) {
            totalQty=0.0;
            for (int i = 0; i < items.size(); i++) {
                if(items.get(i).getDisc()==0) {  // if not exist discount on item x
                    totalQty += items.get(i).getQty();
                }
                //  Log.e("totalQty",""+totalQty);
                discount_oofers_total_cash=0;
                for(int j=0;j<list_discount_offers.size();j++) {
                    if (payMethod == 1) {
                        if (list_discount_offers.get(j).getPaymentType() == 1) {
                            if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                discount_oofers_total_cash = totalQty * list_discount_offers.get(j).getDiscountValue();
                                Log.e("discount_oofers_total", "" + discount_oofers_total_cash);
                            }
                        }
                    }
                        else {
                        if (list_discount_offers.get(j).getPaymentType() == 0) {
                            if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
                                Log.e("discount_oofecredit", "" + discount_oofers_total_credit);
                            }
                        }
                        }
                        }
                    }


            if (discount_oofers_total_cash > 0)
                sum_discount = discount_oofers_total_cash;
            if(discount_oofers_total_credit>0)
                sum_discount = discount_oofers_total_credit;

            try {
                totalDiscount=sum_discount;
//                totalDiscount = Float.parseFloat(discTextView.getText().toString());
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }

            for (int i = 0; i < items.size(); i++) {
                itemGroup = items.get(i).getCategory();
                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
                }
                else
                    {
                    itemTax = items.get(i).getAmount() * items.get(i).getTaxPercent() * 0.01;
                    itemTotal = items.get(i).getAmount();
                    }
                itemTotalAfterTax = items.get(i).getAmount() + itemTax;
                subTotal = subTotal + itemTotal;
            }
            for (int i = 0; i < items.size(); i++) {
                itemTotal = items.get(i).getAmount();
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal = (itemTotalPerc * totalDiscount);
                items.get(i).setTotalDiscVal(itemDiscVal);
                //************************************************************
//                totalQty +=items.get(i).getQty();
////                Log.e("totalQty",""+totalQty);
                itemGroup = items.get(i).getCategory();
                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTotal = itemTotal - itemDiscVal;
                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
                }

                items.get(i).setTaxValue(itemTax);
                totalTaxValue = totalTaxValue + itemTax;
            }

            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue;
//              netTotal = netTotal + subTotal -sum_discount + totalTaxValue;


        }

        else {
            totalQty=0.0;
            for (int i = 0; i < items.size(); i++) {

                totalQty +=items.get(i).getQty();
              //  Log.e("totalQty",""+totalQty);

                discount_oofers_total_cash=0;
                for(int j=0;j<list_discount_offers.size();j++) {
                    if (payMethod == 1) {
                        if (list_discount_offers.get(j).getPaymentType() == 1) {
                            if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                discount_oofers_total_cash = totalQty * list_discount_offers.get(j).getDiscountValue();
                                Log.e("discount_oofers_total", "" + discount_oofers_total_cash);
                            }
                        }
                    }
                    else {
                        if (list_discount_offers.get(j).getPaymentType() == 0) {
                            if (totalQty >= list_discount_offers.get(j).getQTY()) {
                                discount_oofers_total_credit = totalQty * list_discount_offers.get(j).getDiscountValue();
                                Log.e("discount_oofecredit", "" + discount_oofers_total_credit);
                            }
                        }
                    }
                }
            }


            if (discount_oofers_total_cash > 0)
                sum_discount = discount_oofers_total_cash;
            if(discount_oofers_total_credit>0)
                sum_discount = discount_oofers_total_credit;

            try {
                totalDiscount=sum_discount;
//                totalDiscount = Float.parseFloat(discTextView.getText().toString());
            } catch (NumberFormatException e) {
                totalDiscount = 0.0;
            }

            for (int i = 0; i < items.size(); i++) {


                itemGroup = items.get(i).getCategory();


                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
                } else {
                    itemTax = items.get(i).getAmount() -
                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
                }


                itemTotal = items.get(i).getAmount() - itemTax;
                itemTotalAfterTax = items.get(i).getAmount();
                subTotal = subTotal + itemTotal;
            }

            for (int i = 0; i < items.size(); i++) {


                itemGroup = items.get(i).getCategory();

                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTax = items.get(i).getAmount() -
                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
                }

                itemTotal = items.get(i).getAmount() - itemTax;
                itemTotalPerc = itemTotal / subTotal;
                itemDiscVal = (itemTotalPerc * totalDiscount);
                items.get(i).setVoucherDiscount( (float)itemDiscVal);
                items.get(i).setTotalDiscVal(itemDiscVal);
          //      discount_oofers_total=0.0;
//                totalQty +=items.get(i).getQty();
//                Log.e("totalQty",""+totalQty);






                itemTotal = itemTotal - itemDiscVal;

                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
                } else {
                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
                }


                items.get(i).setTaxValue(itemTax);
                totalTaxValue = totalTaxValue + itemTax;
            }

            netTotal = netTotal + subTotal - totalDiscount + totalTaxValue; // tahani -discount_oofers_total
           // discount_oofers_total=0;

        }

//        double discount_All_invoice=discount_oofers_total+Double.parseDouble(discTextView.getText().toString());
        subTotalTextView.setText(String.valueOf(decimalFormat.format(subTotal)));
        taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));

        discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(discTextView.getText().toString()))));
        discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(totalDiscount+""))));
        netTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));

        subTotalTextView.setText(convertToEnglish(subTotalTextView.getText().toString()));
        taxTextView.setText(convertToEnglish(taxTextView.getText().toString()));
        netTotalTextView.setText(convertToEnglish(netTotalTextView.getText().toString()));

        discTextView.setText(convertToEnglish(totalDiscount+""));
        totalDiscount=0.0;


    }
    public void calculateTotals_cridit() {
        double itemTax, itemTotal=0, itemTotalAfterTax,
                itemTotalPerc, itemDiscVal, posPrice,totalQty=0,total_Amount=0;

        //**********************************************************************
//        String itemGroup;
//        subTotal = 0.0;
//        totalTaxValue = 0.0;
//        netTotal = 0.0;
//        double discount_credit=0;
//        //  Exclude  ------> TaxClarcKind() == 0
//        if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0)
//        {
//            for (int i = 0; i < items.size(); i++)
//            {
//                totalQty += items.get(i).getQty();
//                discount_oofers_total = 0;
//                if (payMethod== 1) {
//                    list_discount_offers = mDbHandler.getDiscountOffers();
//                    for (int j = 0; j < list_discount_offers.size(); j++) {
//                        if (totalQty >= list_discount_offers.get(j).getQTY()) {
//                            discount_oofers_total = totalQty * list_discount_offers.get(j).getDiscountValue();
//                        }
//                    }
//                }
//
//
//            }
//
//
//
//            for (int i = 0; i < items.size(); i++) {
//                itemGroup = items.get(i).getCategory();
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
//                }
//                else
//                {
//                    itemTax = items.get(i).getAmount() * items.get(i).getTaxPercent() * 0.01;
//                    itemTotal = items.get(i).getAmount();
//                }
//                itemTotalAfterTax = items.get(i).getAmount() + itemTax;
//                subTotal = subTotal + itemTotal;
//            }
//
//            for (int i = 0; i < items.size(); i++) {
//                itemTotal = items.get(i).getAmount();
//                total_Amount+=itemTotal;
//                itemTotalPerc = itemTotal / subTotal;
//                itemDiscVal = (itemTotalPerc * totalDiscount);
//                items.get(i).setTotalDiscVal(itemDiscVal);
//                //************************************************************
//                itemGroup = items.get(i).getCategory();
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                } else {
//                    itemTotal = itemTotal - itemDiscVal;
//                    total_Amount+=itemTotal;
//                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
//                }
//
//                items.get(i).setTaxValue(itemTax);
//                totalTaxValue = totalTaxValue + itemTax;
//            }
//            if (discount_oofers_total > 0) {
//                sum_discount = discount_oofers_total;
//            }
//
//            try {
//                totalDiscount=sum_discount;
////                totalDiscount = Float.parseFloat(discTextView.getText().toString());
//            } catch (NumberFormatException e) {
//                totalDiscount = 0.0;
//            }
//            if (payMethod == 0) {
//                double maxDiscount = CustomerListShow.Max_Discount_value;
//                discount_credit = (total_Amount * maxDiscount)/100;
//                Log.e("discount_credit",""+discount_credit);
//            }
//           if(payMethod==0) {
//               netTotal = netTotal + subTotal - totalDiscount - discount_credit + totalTaxValue;
//           }
//           else{
//               netTotal = netTotal + subTotal - totalDiscount  + totalTaxValue;
////               netTotal = netTotal + subTotal - totalDiscount - discount_oofers_total + totalTaxValue;
//           }
//
//        }
//      //********************************************  Include  ------> TaxClarcKind() == 1
//        else {
//
//            for (int i = 0; i < items.size(); i++) {
//
//                totalQty += items.get(i).getQty();
//                discount_oofers_total = 0;
//                if (payMethod == 1) {
//                    list_discount_offers = mDbHandler.getDiscountOffers();
//                    for (int j = 0; j < list_discount_offers.size(); j++) {
//                        if (totalQty >= list_discount_offers.get(j).getQTY()) {
//                            discount_oofers_total = totalQty * list_discount_offers.get(j).getDiscountValue(); }
//                    }
//                }
//
//                if (payMethod== 0) {
//
//                    double maxDiscount = CustomerListShow.Max_Discount_value;
//
//                    discount_credit = totalQty * maxDiscount;
//                }
//
//            }
//
//
//
//            if (discount_oofers_total > 0)
//                sum_discount = discount_oofers_total;
//
//            try {totalDiscount=sum_discount;
////                totalDiscount = Float.parseFloat(discTextView.getText().toString());
//            } catch (NumberFormatException e) {
//                totalDiscount = 0.0;
//            }
//
//            for (int i = 0; i < items.size(); i++) {
//
//
//                itemGroup = items.get(i).getCategory();
//
//
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                    itemTotal = items.get(i).getQty() * items.get(i).getPosPrice() - itemTax;
//                } else {
//                    itemTax = items.get(i).getAmount() -
//                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
//                }
//
//
//                itemTotal = items.get(i).getAmount() - itemTax;
//                itemTotalAfterTax = items.get(i).getAmount();
//                subTotal = subTotal + itemTotal;
//            }
//
//            for (int i = 0; i < items.size(); i++) {
//
//
//                itemGroup = items.get(i).getCategory();
//
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                } else {
//                    itemTax = items.get(i).getAmount() -
//                            (items.get(i).getAmount() / (1 + items.get(i).getTaxPercent() * 0.01));
//                }
//
//                itemTotal = items.get(i).getAmount() - itemTax;
//                itemTotalPerc = itemTotal / subTotal;
//                itemDiscVal = (itemTotalPerc * totalDiscount);
//                items.get(i).setVoucherDiscount( (float)itemDiscVal);
//                items.get(i).setTotalDiscVal(itemDiscVal);
//
//                itemTotal = itemTotal - itemDiscVal;
//                if (itemGroup.equals(smokeGA) || itemGroup.equals(smokeGE)) {
//                    itemTax = items.get(i).getQty() * items.get(i).getPosPrice();
//                    itemTax = (itemTax * items.get(i).getTaxPercent() * 0.01) / (1 + items.get(i).getTaxPercent() * 0.01);
//                } else {
//                    itemTax = itemTotal * items.get(i).getTaxPercent() * 0.01;
//                }
//                items.get(i).setTaxValue(itemTax);
//                totalTaxValue = totalTaxValue + itemTax;
//            }
//            if(payMethod==0) {
//                netTotal = netTotal + subTotal - totalDiscount - discount_credit + totalTaxValue;
//            }
//            else{
//                netTotal = netTotal + subTotal - totalDiscount  + totalTaxValue;
////                netTotal = netTotal + subTotal - totalDiscount - discount_oofers_total + totalTaxValue;
//            }
//        }
//        subTotalTextView.setText(String.valueOf(decimalFormat.format(subTotal)));
//        taxTextView.setText(String.valueOf(decimalFormat.format(totalTaxValue)));
//        if(payMethod==0) {//credit
//            discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(discount_credit + ""))));
//            discTextView.setText(convertToEnglish(discount_credit+""));
//        }
//        else{
//            //cash
//            discTextView.setText(String.valueOf(decimalFormat.format(Double.parseDouble(discount_oofers_total + ""))));
//            discTextView.setText(convertToEnglish(discount_oofers_total+""));
//        }
//        netTotalTextView.setText(String.valueOf(decimalFormat.format(netTotal)));
//        subTotalTextView.setText(convertToEnglish(subTotalTextView.getText().toString()));
//        taxTextView.setText(convertToEnglish(taxTextView.getText().toString()));
//        netTotalTextView.setText(convertToEnglish(netTotalTextView.getText().toString()));
//        discount_oofers_total=0.0;
//        discount_credit=0;
//        total_Amount=0;


    }



    void send_dataSewoo(Voucher voucher) throws IOException {
        try {
            Log.e("send","'yes");
            testB =convertLayoutToImage(voucher);

            printPic = PrintPic.getInstance();
            printPic.init(testB);
            printIm= printPic.printDraw();
            mmOutputStream.write(printIm);
            isFinishPrint=true;
//            dialogs.show();
//            ImageView iv = (ImageView) view.findViewById(R.id.ivw);
//////                iv.setLayoutParams(layoutParams);
//            iv.setBackgroundColor(Color.TRANSPARENT);
//            iv.setImageBitmap(testB);

//            int w=10/0;


//                iv.setMaxHeight(100);
//                iv.setMaxWidth(100);



        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap convertLayoutToImage(Voucher voucher) {
        LinearLayout linearView=null;

        final Dialog dialogs=new Dialog(getActivity());
        dialogs.setContentView(R.layout.printdialog);
//            fill_theVocher( voucher);


        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);

        TextView compname,tel,taxNo,vhNo,date,custname,note,vhType,paytype,total,discount,tax,ammont,textW;

        ImageView img =(ImageView)dialogs.findViewById(R.id.img);
        compname=(TextView)dialogs.findViewById(R.id.compname);
        tel=(TextView)dialogs.findViewById(R.id.tel);
        taxNo=(TextView)dialogs.findViewById(R.id.taxNo);
        vhNo=(TextView)dialogs.findViewById(R.id.vhNo);
        date=(TextView)dialogs.findViewById(R.id.date);
        custname=(TextView)dialogs.findViewById(R.id.custname);
        note=(TextView)dialogs.findViewById(R.id.note);
        vhType=(TextView)dialogs.findViewById(R.id.vhType);
        paytype=(TextView)dialogs.findViewById(R.id.paytype);
        total=(TextView)dialogs.findViewById(R.id.total);
        discount=(TextView)dialogs.findViewById(R.id.discount);
        tax=(TextView)dialogs.findViewById(R.id.tax);
        ammont=(TextView)dialogs.findViewById(R.id.ammont);
        textW=(TextView)dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout=(TableLayout)dialogs.findViewById(R.id.tab);
//

       TextView doneinsewooprint =(TextView) dialogs.findViewById(R.id.done);

        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFinishPrint) {
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialogs.dismiss();
                }
            }
        });


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

        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);

        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucher.getSubTotal());
        discount.setText("" + voucher.getTotalVoucherDiscount());
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());



        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        }else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        for (int j = 0; j < itemsList.size(); j++) {

            if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(getActivity());


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(18);

                    switch (i) {
                        case 0:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                            textView.setText("" + itemsList.get(j).getUnit());
                            textView.setLayoutParams(lp2);
                    }else{
                        textView.setText("" + items.get(j).getQty());
                        textView.setLayoutParams(lp2);
                    }
                            break;

                        case 2:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            }else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
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

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit = linearView.getDrawingCache();
        return bit;// creates bitmap and returns the same
    }

    private Bitmap convertLayoutToImageTally(Voucher voucher) {
        LinearLayout linearView=null;

        final Dialog dialogs=new Dialog(getActivity());
        dialogs.setContentView(R.layout.printdialog_tally);
//            fill_theVocher( voucher);

        List <CompanyInfo>comp=mDbHandler.getAllCompanyInfo();
        CompanyInfo companyInfo = null;
        if(comp.size()!=0){
         companyInfo =comp.get(0);}
        else {
            Toast.makeText(getActivity(), "Please Add Company Information", Toast.LENGTH_SHORT).show();
        }

        TextView compname,tel,taxNo,vhNo,date,custname,note,vhType,paytype,total,discount,tax,ammont,textW;

        ImageView img =(ImageView)dialogs.findViewById(R.id.img);
        compname=(TextView)dialogs.findViewById(R.id.compname);
        tel=(TextView)dialogs.findViewById(R.id.tel);
        taxNo=(TextView)dialogs.findViewById(R.id.taxNo);
        vhNo=(TextView)dialogs.findViewById(R.id.vhNo);
        date=(TextView)dialogs.findViewById(R.id.date);
        custname=(TextView)dialogs.findViewById(R.id.custname);
        note=(TextView)dialogs.findViewById(R.id.note);
        vhType=(TextView)dialogs.findViewById(R.id.vhType);
        paytype=(TextView)dialogs.findViewById(R.id.paytype);
        total=(TextView)dialogs.findViewById(R.id.total);
        discount=(TextView)dialogs.findViewById(R.id.discount);
        tax=(TextView)dialogs.findViewById(R.id.tax);
        ammont=(TextView)dialogs.findViewById(R.id.ammont);
        textW=(TextView)dialogs.findViewById(R.id.wa1);
        TableLayout tabLayout=(TableLayout)dialogs.findViewById(R.id.tab);
//

        TextView doneinsewooprint =(TextView) dialogs.findViewById(R.id.done);

        doneinsewooprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFinishPrint) {
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialogs.dismiss();
                }
            }
        });


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

        img.setImageBitmap(companyInfo.getLogo());
        compname.setText(companyInfo.getCompanyName());
        tel.setText("" + companyInfo.getcompanyTel());
        taxNo.setText("" + companyInfo.getTaxNo());
        vhNo.setText("" + voucher.getVoucherNumber());
        date.setText(voucher.getVoucherDate());
        custname.setText(voucher.getCustName());
        note.setText(voucher.getRemark());
        vhType.setText(voucherTyp);

        paytype.setText((voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));
        total.setText("" + voucher.getSubTotal());
        discount.setText("" + totalDiscount);
        tax.setText("" + voucher.getTax());
        ammont.setText("" + voucher.getNetSales());



        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() != 1) {
            textW.setVisibility(View.GONE);
        }else {
            textW.setVisibility(View.VISIBLE);
        }


        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        for (int j = 0; j < itemsList.size(); j++) {

            if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                final TableRow row = new TableRow(getActivity());


                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(32);

                    switch (i) {
                        case 0:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getUnit());
                                textView.setLayoutParams(lp2);
                            }else{
                                textView.setText("" + items.get(j).getQty());
                                textView.setLayoutParams(lp2);
                            }
                            break;

                        case 2:
                            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                                textView.setText("" + itemsList.get(j).getQty());
                                textView.setLayoutParams(lp2);
                                textView.setVisibility(View.VISIBLE);
                            }else {
                                textView.setVisibility(View.GONE);
                            }
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;


                        case 4:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
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
//        Bitmap bit = linearView.getDrawingCache();


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

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    public double getItemsTotal() {
        double total = 0;

        for (Item i : items) {
            total = total + i.getAmount();
        }

        return total;
    }

    @SuppressLint("SetTextI18n")
    public void hiddenDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.print);

        final Button okButton = dialog.findViewById(R.id.print1);
        final LinearLayout linearLayout = dialog.findViewById(R.id.linear1);
        TableLayout tabLayout = (TableLayout) dialog.findViewById(R.id.table_);

        TextView companyName = dialog.findViewById(R.id.company);
        TextView phone = dialog.findViewById(R.id.phone);
        TextView taxNo = dialog.findViewById(R.id.tax_no);
        TextView date = dialog.findViewById(R.id.date);
        TextView vouch_no = dialog.findViewById(R.id.voucher_no);
        TextView vouchType = dialog.findViewById(R.id.voucher_type);
        TextView payMethod = dialog.findViewById(R.id.payMethod);
        TextView cust = dialog.findViewById(R.id.cust_);
        TextView remark = dialog.findViewById(R.id.remark_);
        TextView totalNoTax = dialog.findViewById(R.id.total_noTax);
        TextView discount = dialog.findViewById(R.id.discount);
        TextView tax = dialog.findViewById(R.id.tax);
        TextView netSale = dialog.findViewById(R.id.net_sales_);

        CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);

        companyName.setText(companyInfo.getCompanyName());
        phone.setText(phone.getText().toString() + companyInfo.getcompanyTel());
        taxNo.setText(taxNo.getText().toString() + companyInfo.getTaxNo());
        date.setText(date.getText().toString() + voucher.getVoucherDate());
        vouch_no.setText(vouch_no.getText().toString() + voucher.getVoucherNumber());
        remark.setText(remark.getText().toString() + voucher.getRemark());
        cust.setText(cust.getText().toString() + voucher.getCustName());
        totalNoTax.setText(totalNoTax.getText().toString() + voucher.getSubTotal());
        discount.setText(discount.getText().toString() +totalDiscount);
        tax.setText(tax.getText().toString() + voucher.getTax());
        netSale.setText(netSale.getText().toString() + voucher.getNetSales());

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
        vouchType.setText(vouchType.getText().toString() + voucherTyp);
        payMethod.setText(payMethod.getText().toString() + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا"));

        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2.0f);
        lp2.setMargins(2, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);

        if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {

            final TableRow headerRow = new TableRow(getActivity());

            TextView headerView7 = new TextView(getActivity());
            headerView7.setGravity(Gravity.CENTER);
            headerView7.setText("المجموع");
            headerView7.setLayoutParams(lp2);
            headerView7.setTextSize(12);
            headerRow.addView(headerView7);

            TextView headerView6 = new TextView(getActivity());
            headerView6.setGravity(Gravity.CENTER);
            headerView6.setText("الخصم");
            headerView6.setLayoutParams(lp2);
            headerView6.setTextSize(12);
            headerRow.addView(headerView6);

            TextView headerView5 = new TextView(getActivity());
            headerView5.setGravity(Gravity.CENTER);
            headerView5.setText("المجاني");
            headerView5.setLayoutParams(lp2);
            headerView5.setTextSize(12);
            headerRow.addView(headerView5);

            TextView headerView4 = new TextView(getActivity());
            headerView4.setGravity(Gravity.CENTER);
            headerView4.setText("سعر الوحدة");
            headerView4.setLayoutParams(lp2);
            headerView4.setTextSize(12);
            headerRow.addView(headerView4);

            TextView headerView3 = new TextView(getActivity());
            headerView3.setGravity(Gravity.CENTER);
            headerView3.setText("الوزن");
            headerView3.setLayoutParams(lp2);
            headerView3.setTextSize(12);
            headerRow.addView(headerView3);

            TextView headerView2 = new TextView(getActivity());
            headerView2.setGravity(Gravity.CENTER);
            headerView2.setText("العدد");
            headerView2.setLayoutParams(lp2);
            headerView2.setTextSize(12);
            headerRow.addView(headerView2);

            TextView headerView1 = new TextView(getActivity());
            headerView1.setGravity(Gravity.CENTER);
            headerView1.setText("السلعة");
            headerView1.setLayoutParams(lp3);
            headerView1.setTextSize(12);
            headerRow.addView(headerView1);

            tabLayout.addView(headerRow);
        } else {
            final TableRow headerRow = new TableRow(getActivity());
            TextView headerView1 = new TextView(getActivity());

            TextView headerView6 = new TextView(getActivity());
            headerView6.setGravity(Gravity.CENTER);
            headerView6.setText("المجموع");
            headerView6.setLayoutParams(lp2);
            headerView6.setTextSize(12);
            headerRow.addView(headerView6);

            TextView headerView5 = new TextView(getActivity());
            headerView5.setGravity(Gravity.CENTER);
            headerView5.setText("الخصم");
            headerView5.setLayoutParams(lp2);
            headerView5.setTextSize(12);
            headerRow.addView(headerView5);

            TextView headerView4 = new TextView(getActivity());
            headerView4.setGravity(Gravity.CENTER);
            headerView4.setText("المجاني");
            headerView4.setLayoutParams(lp2);
            headerView4.setTextSize(12);
            headerRow.addView(headerView4);

            TextView headerView3 = new TextView(getActivity());
            headerView3.setGravity(Gravity.CENTER);
            headerView3.setText("سعر الوحدة");
            headerView3.setLayoutParams(lp2);
            headerView3.setTextSize(12);
            headerRow.addView(headerView3);

            TextView headerView2 = new TextView(getActivity());
            headerView2.setGravity(Gravity.CENTER);
            headerView2.setText("العدد");
            headerView2.setLayoutParams(lp2);
            headerView2.setTextSize(12);
            headerRow.addView(headerView2);

            headerView1.setGravity(Gravity.CENTER);
            headerView1.setText("السلعة");
            headerView1.setLayoutParams(lp3);
            headerView1.setTextSize(12);
            headerRow.addView(headerView1);

            tabLayout.addView(headerRow);
        }

        for (int j = 0; j < itemsList.size(); j++) {
            final TableRow row = new TableRow(getActivity());

            if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {

                for (int i = 0; i <= 7; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(10);

                    switch (i) {
                        case 6:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;

                        case 5:
                            textView.setText(itemsList.get(j).getUnit());
                            textView.setLayoutParams(lp2);
                            break;

                        case 4:
                            textView.setText("" + itemsList.get(j).getQty());
                            textView.setLayoutParams(lp2);
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;

                        case 2:
                            textView.setText("" + itemsList.get(j).getBonus());
                            textView.setLayoutParams(lp2);
                            break;

                        case 1:
                            textView.setText("" + itemsList.get(j).getDisc());
                            textView.setLayoutParams(lp2);
                            break;

                        case 0:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }

            } else {
                for (int i = 0; i <= 6; i++) {
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(10);

                    switch (i) {
                        case 5:
                            textView.setText(itemsList.get(j).getItemName());
                            textView.setLayoutParams(lp3);
                            break;

                        case 4:
                            textView.setText(itemsList.get(j).getUnit());
                            textView.setLayoutParams(lp2);
                            break;

                        case 3:
                            textView.setText("" + itemsList.get(j).getPrice());
                            textView.setLayoutParams(lp2);
                            break;

                        case 2:
                            textView.setText("" + itemsList.get(j).getBonus());
                            textView.setLayoutParams(lp2);
                            break;

                        case 1:
                            textView.setText("" + itemsList.get(j).getDisc());
                            textView.setLayoutParams(lp2);
                            break;

                        case 0:
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            amount = convertToEnglish(amount);
                            textView.setText(amount);
                            textView.setLayoutParams(lp2);
                            break;
                    }
                    row.addView(textView);
                }
            }
            tabLayout.addView(row);

        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintHelper photoPrinter = new PrintHelper(getActivity());
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                linearLayout.setDrawingCacheEnabled(true);
                Bitmap bitmap = linearLayout.getDrawingCache();
                photoPrinter.printBitmap("invoice.jpg", bitmap);

            }
        });

        dialog.show();

    }

    void findBT() {

        try {
            /*  very important **********************************************************/
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }

        itemsString = "";
        itemsString2 = "";
        for (int j = 0; j < itemsList.size(); j++) { // don't know why is it here :/
            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
            amount = convertToEnglish(amount);

            String row = itemsList.get(j).getItemName() + "                                             ";
            row = row.substring(0, 21) + itemsList.get(j).getUnit() + row.substring(21, row.length());
            row = row.substring(0, 31) + itemsList.get(j).getQty() + row.substring(31, row.length());
            row = row.substring(0, 41) + itemsList.get(j).getPrice() + row.substring(41, row.length());
            row = row.substring(0, 52) + new DecimalFormat("#.##").format(Double.valueOf(amount));
            row = row.trim();
            itemsString = itemsString + "\n" + row;

            String row2 = itemsList.get(j).getItemName() + "                                             ";
            row2 = row2.substring(0, 21) + itemsList.get(j).getUnit() + row2.substring(21, row2.length());
            row2 = row2.substring(0, 31) + itemsList.get(j).getPrice() + row2.substring(31, row2.length());
            row2 = row2.substring(0, 42) + new DecimalFormat("#.##").format(Double.valueOf(amount));
            row2 = row2.trim();
            itemsString2 = itemsString2 + "\n" + row2;

        }

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
    void openBT(int casePrinter) throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();


            switch (casePrinter){

                case 1:
                    sendData();
                    break;
                case 2:
                    Settings settings = mDbHandler.getAllSettings().get(0);
                    for(int i=0;i<settings.getNumOfCopy();i++)
                    {send_dataSewoo(voucher);}

                    break;
                case 3:
                    sendData2();
                    break;



            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void printTally(Voucher voucher) {

        Bitmap bitmap = convertLayoutToImageTally(voucher);

        try {
            Settings settings = mDbHandler.getAllSettings().get(0);
            File file = savebitmap(bitmap, settings.getNumOfCopy());
            Log.e("save image ", "" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static File savebitmap(Bitmap bmp, int numCope) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = null;
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/VanSale/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (int i = 0; i < numCope; i++) {
            String targetPdf = directory_path + "testimageSales" + i + ".png";
            f = new File(targetPdf);


//        f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        return f;
    }




    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() throws IOException {
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
            closeBT();
            e.printStackTrace();
        } catch (Exception e) {
            closeBT();
            e.printStackTrace();
        }
    }

    /*
     * This will send data to be printed by the bluetooth printer
     */
    void sendData() throws IOException {
        try {
            Log.e("getTotalQty",""+voucher.getTotalQty());
            CompanyInfo companyInfo=new CompanyInfo();



            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            try {
              companyInfo = mDbHandler.getAllCompanyInfo().get(0);
            }catch (Exception e)
            {
                companyInfo.setCompanyName("Companey Name");
                companyInfo.setTaxNo(0);
                companyInfo.setCompanyTel(00000);
//                companyInfo.setLogo();
                            }
//&& !companyInfo.getLogo().equals(null)
            if (!companyInfo.getCompanyName().equals("")&& companyInfo.getcompanyTel()!=0&&companyInfo.getTaxNo()!=0) {
                pic.setImageBitmap(companyInfo.getLogo());
                pic.setDrawingCacheEnabled(true);
                Bitmap bitmap = pic.getDrawingCache();

                PrintPic printPic = PrintPic.getInstance();
                printPic.init(bitmap);
                byte[] bitmapdata = printPic.printDraw();


                for (int i = 1; i <= numOfCopy; i++) {
                    Thread.sleep(1000);
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

                    if (companyInfo.getLogo() != null) {

                        mmOutputStream.write(bitmapdata);
                        printCustom(" \n ", 1, 0);
                    }

                    printCustom(companyInfo.getCompanyName() + "\n", 1, 1);
                    printCustom("هاتف : " + companyInfo.getcompanyTel() + "    الرقم الضريبي : " + companyInfo.getTaxNo() + "\n", 1, 0);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    printCustom("رقم الفاتورة : " + voucher.getVoucherNumber() + "          التاريخ: " + voucher.getVoucherDate() + "\n", 1, 2);
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("اسم العميل   : " + voucher.getCustName() + "\n", 1, 2);
                    printCustom("ملاحظة        : " + voucher.getRemark() + "\n", 1, 2);
                    printCustom("نوع الفاتورة : " + voucherTyp + "\n", 1, 2);
                    printCustom("طريقة الدفع  : " + (voucher.getPayMethod() == 0 ? "ذمم" : "نقدا") + "\n", 1, 2);
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1) {
                        printCustom(" السلعة              " + "العدد   " + "الوزن    " + "سعر الوحدة   " + "المجموع  " + "\n", 0, 2);
                        printCustom("----------------------------------------------" + "\n", 1, 2);

                        printCustom(itemsString + "\n", 0, 2);
                    } else {
                        printCustom(" السلعة              " + "العدد   " + "سعر الوحدة   " + "المجموع  " + "\n", 0, 2);
                        printCustom("----------------------------------------------" + "\n", 1, 2);

                        printCustom(itemsString2 + "\n", 0, 2);
                    }

                    printCustom("----------------------------------------------" + "\n", 1, 2);

                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("اجمالي الكمية:  " + convertToEnglish(threeDForm.format(voucher.getTotalQty())) + " : " + " \n ", 1, 0);
                    printCustom("المجموع  : " + voucher.getSubTotal() + "\n", 1, 2);
                    printCustom("الخصم    : " +voucher.getTotalVoucherDiscount()+ "\n", 1, 2);

                    printCustom("الضريبة  : " + voucher.getTax() + "\n", 1, 2);
                    printCustom("الصافي   : " + voucher.getNetSales() + "\n", 1, 2);
                    if (voucher.getVoucherType() != 506) {
                        printCustom("استلمت البضاعة كاملة و بحالة جيدة و خالية من " + "\n", 1, 2);
                        printCustom("اية  عيوب و اتعهد بدفع قيمة هذه الفاتورة." + "\n", 1, 2);
                        mmOutputStream.write(PrinterCommands.FEED_LINE);
                        printCustom("المستلم : ________________ التوقيع : __________" + "\n", 1, 2);
                    }
                    mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("----------------------------------------------" + "\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);
                    printCustom("\n", 1, 2);

                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                  //  Arabic864 arabic = new Arabic864();
                    //   byte[] arabicArr = arabic.Convert(  new StringBuilder(msg).reverse().toString(),false);
                    //    byte[] arabicArr = arabic.Convert("الاسم",false);
//                    Log.e("mymsg", "" + msg);
//                        Log.e("Not ---LtoR",""+msg);
//                        Log.e("LtoR",""+msg);
//                        byte[] arabicArr = arabic.Convert(new StringBuilder(msg).reverse().toString(), false);
//                        Log.e("byte", "" + arabicArr.toString());
//                        mmOutputStream.write(arabicArr);
                }
                closeBT();
                // tell the user data were sent
//                myLabel.setText("Data Sent");

            } else
                Toast.makeText(getActivity(), " please enter company information", Toast.LENGTH_LONG).show();

        } catch (NullPointerException e) {
            closeBT();
            e.printStackTrace();
        } catch (Exception e) {
            closeBT();
            e.printStackTrace();
        }
    }

    void sendData2() {
        try {

            double totalQty = 0;
            double totalPrice = 0;
            double totalDisc = 0;
            double totalNet = 0;
            double totalTax = 0;
            double totalTotal = 0;

            int numOfCopy = mDbHandler.getAllSettings().get(0).getNumOfCopy();
            Log.e("nocopy",""+numOfCopy);
            CompanyInfo companyInfo = mDbHandler.getAllCompanyInfo().get(0);
            pic.setImageBitmap(companyInfo.getLogo());
            pic.setDrawingCacheEnabled(true);
            Bitmap bitmap = pic.getDrawingCache();

            PrintPic printPic = PrintPic.getInstance();
            printPic.init(bitmap);
            byte[] bitmapdata = printPic.printDraw();

            if (companyInfo != null) {

                for (int i = 1; i <= numOfCopy; i++) {

                    //  printCustom(companyInfo.getCompanyName() + " \n ", 1, 0);

                    if (companyInfo.getLogo() != null) {

                        mmOutputStream.write(bitmapdata);
                        //     printCustom(" \n ", 1, 1);
                    }

                    printCustom(companyInfo.getCompanyName() + "   \n   ", 1, 0);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
                    printCustom("\n الرقم الضريبي  " + companyInfo.getTaxNo() + " : " + " \n ", 1, 0);
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("التاريخ        " + voucher.getVoucherDate() + " : " + " \n ", 1, 0);
                    printCustom("رقم الفاتورة   " + voucher.getVoucherNumber() + " : " + "\n", 1, 0);
                    printCustom("رقم العميل     " + voucher.getCustNumber() + " : " + "\n", 1, 0);
                    printCustom("اسم العميل " + " : " + voucher.getCustName() + "\n", 1, 0);
                    printCustom("مندوب المبيعات " + voucher.getSaleManNumber() + " : " + "\n", 1, 0);
                    printCustom("------------------------------------------" + "\n", 1, 0);

                    int serial = 1;
                    for (int j = 0; j < itemsList.size(); j++) {
                        if (voucher.getVoucherNumber() == itemsList.get(j).getVoucherNumber()) {
                            String amount = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            String amountATax = "" + (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc() + itemsList.get(j).getTaxValue());
                            amount = convertToEnglish(amount);
                            amountATax = convertToEnglish(amountATax);


                            printCustom("(" + serial + "" + "\n", 1, 0);
                            printCustom("رقم الصنف " + itemsList.get(j).getItemNo() + " : " + " \n ", 1, 0);
                            printCustom("الصنف " + " : " + itemsList.get(j).getItemName() + " \n ", 1, 0);
                            printCustom("الكمية    " + itemsList.get(j).getQty() + " : " + " \n ", 1, 0);
                            printCustom("المجاني    " + itemsList.get(j).getBonus() + " : " + " \n ", 1, 0);
                            printCustom("السعر     " + " JD " + itemsList.get(j).getPrice() + " : " + " \n ", 1, 0);
                            printCustom("الخصم     " + " JD " + itemsList.get(j).getDisc() + " : " + " \n ", 1, 0);
                            printCustom("الصافي    " + " JD " + convertToEnglish(threeDForm.format(Double.parseDouble(amount))) + " : " + "\n", 1, 0);
                            printCustom("الضريبة   " + " JD " + convertToEnglish(threeDForm.format(itemsList.get(j).getTaxValue())) + " : " + " \n ", 1, 0);
                            printCustom("الاجمالي   " + " JD " + convertToEnglish(threeDForm.format(Double.parseDouble(amountATax))) + " : " + " \n ", 1, 0);

                            printCustom("* * * * * * * * * * * * * " + " \n ", 1, 0);

                            serial++;
                            totalQty += itemsList.get(j).getQty() + itemsList.get(j).getBonus();
                            totalPrice += itemsList.get(j).getPrice();
                            totalDisc += itemsList.get(j).getDisc();
                            totalNet += (itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc());
                            totalTax += itemsList.get(j).getTaxValue();
                            totalTotal += itemsList.get(j).getQty() * itemsList.get(j).getPrice() - itemsList.get(j).getDisc() + itemsList.get(j).getTaxValue();
                        }
                    }


                    printCustom("اجمالي الكمية  " + convertToEnglish(threeDForm.format(totalQty)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي السعر   " + " JD " + convertToEnglish(threeDForm.format(totalPrice)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الخصم   " + " JD " + convertToEnglish(threeDForm.format(totalDisc)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الصافي  " + " JD " + convertToEnglish(threeDForm.format(totalNet)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الضريبة " + " JD " + convertToEnglish(threeDForm.format(totalTax)) + " : " + " \n ", 1, 0);
                    printCustom("اجمالي الإجمالي " + " JD " + convertToEnglish(threeDForm.format(totalTotal)) + " : " + " \n ", 1, 0);

                    if (voucher.getVoucherType() != 506) {
                        printCustom("استلمت البضاعة خالية من اي عيب او توالف" + " \n ", 1, 0);
                        printCustom("توقيع العميل" + "  " + "_______________" + " \n ", 1, 0);
                    }
                    printCustom("------------------------------------------" + " \n ", 1, 0);
                    printCustom("\n", 1, 0);
                    printCustom("\n", 1, 0);

                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                }
                closeBT();
            } else
                Toast.makeText(getActivity(), " please enter company information", Toast.LENGTH_LONG).show();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
//
            Arabic864 arabic = new Arabic864();
            byte[] arabicArr = arabic.Convert(msg, false);
            mmOutputStream.write(arabicArr);
//            mmOutputStream.write(msg.getBytes());

//            outputStream.write(PrinterCommands.LF);
//            outputStream.write(cc);
//            printNewLine();
        } catch (IOException e) {
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
            workerThread.stop();
//            myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                String ipAddress = mDbHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
                String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";

                URL url = new URL(URL_TO_HIT);

                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('2'), "UTF-8");

                JSONArray jsonArrayVouchers = new JSONArray();
                voucher.setIsPosted(1);
                jsonArrayVouchers.put(voucher.getJSONObject());

                JSONArray jsonArrayItems = new JSONArray();
                for (int i = 0; i < itemsList.size(); i++) {
                    itemsList.get(i).setIsPosted(1);
                    jsonArrayItems.put(itemsList.get(i).getJSONObject());
                }

                String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
                table1 += "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
                        + "&" + "Payments=[]"
                        + "&" + "Payments_Checks=[]"
                        + "&" + "Added_Customers=[]"
                        + "&" + "TABLE_TRANSACTIONS=[]";

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                try {
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(table1);

                    wr.flush();
                } catch (Exception e) {
                    Log.e("here****", e.getMessage());
                }


                // get response
                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JsonResponse = sb.toString();
               // Log.e("tag", "" + JsonResponse);

                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                if (s.contains("SUCCESS")) {
                    mDbHandler.updateVoucher2(voucher.getVoucherNumber());
                    mDbHandler.updateVoucherDetails2(voucher.getVoucherNumber());

                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Success");
                } else {
                    Toast.makeText(getActivity(), "Failed to export data", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

}// class salesInvoice
