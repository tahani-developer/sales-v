package com.dr7.salesmanmanager;

import static com.dr7.salesmanmanager.Methods.convertToEnglish;
import static com.dr7.salesmanmanager.SalesInvoice.voucherNumberTextView;
import static com.dr7.salesmanmanager.SalesInvoice.voucherType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.serialModel;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllItemRecyclerListViewAdapter extends BaseAdapter {
    private List<Item> items;
    private ArrayList<Integer> isClicked = new ArrayList<>();
    private List<Item> filterList;
    private Context cont;
    int current_itemHasSerial = 0;
    private AddItemsFragment2 context;
    Date currentTimeAndDate;
    public static EditText price;
    SimpleDateFormat df, df2, formatTime;
    String voucherDate, voucherYear, time;
    CompanyInfo companyInfo;
    String ipAddress = "";
    boolean added = false, haveCstomerDisc = false, haveChangeCustDisc = false;
    DatabaseHandler MHandler;
    DecimalFormat threeDForm;
    int settingPriceCus = 0, showItemImageSetting = 0;
    List<String> localItemNumber;
    boolean itemInlocalList = false;
    public static EditText Serial_No, item_serial;
    public static final int REQUEST_Camera_Serial = 22;
    public boolean isFoundSerial = false;
    SweetAlertDialog pdValidation;
    public static int flag = 0, counterSerial = 0, counterBonus = 0;
    RecyclerView serial_No_recyclerView;
    public static ArrayList<serialModel> serialListitems = new ArrayList<>();

    LinearLayoutManager layoutManager;

    List<String> listSerialValue = new ArrayList<>();
    public static serialModel serial;
    public static EditText unitQty, bonus;
    LinearLayout bonusLinearLayout;
    String discountCustomer = "", updateDiscountValue = "";
    public static Button addToList;
    public String exist = "";
    public static String curentSerial = "";
    public static String araySerial[];
    Bitmap itemBitmap;
    public PhotoView photoView, photoDetail;
    public static TextView checkState_recycler, checkStateResult;
    requestAdmin request;


    int typeRequest = 0, haveResult = 0, approveAdmin = 0;

    LinearLayout mainRequestLinear;
    LinearLayout resultLinear;
    LinearLayout mainLinear;
    ImageView acceptDiscount, rejectDiscount;
    public static EditText serialValue;
    public static int numberBarcodsScanner = 0;
    int discountPerVal = 0, showSolidQty = 0;
    int useWeight = 0;
    public int sunmiDevice = 0, dontShowTax = 0, contiusReading = 0;

    int vouch, kindVoucher = 504;
    ImageView addEditBarcode;
    GeneralMethod generalMethod;
    int itemUnit=0,oneUnit=0;
    float priceUnit=0;
    String rate_customer="0";
    public AllItemRecyclerListViewAdapter(List<Item> items, AddItemsFragment2 context) {
        this.items = items;
        this.filterList = items;
        this.context = context;
        cont = context.getActivity();
        try {
            for (int i = 0; i <= items.size(); i++) {//******************
                isClicked.add(0);
            }
        } catch (Exception e) {
            Toast.makeText(cont, "Items Empty", Toast.LENGTH_SHORT).show();
        }


        vouch = Integer.parseInt(voucherNumberTextView.getText().toString());
        kindVoucher = voucherType;
        MHandler = new DatabaseHandler(cont);
        settingPriceCus = MHandler.getAllSettings().get(0).getPriceByCust();
        showItemImageSetting = MHandler.getAllSettings().get(0).getShowItemImage();
        localItemNumber = new ArrayList<>();
        if (MHandler.getAllSettings().get(0).getContinusReading() == 1) {

            contiusReading = 1;
            Log.e("sunmiDevice", "" + sunmiDevice);
        }
        generalMethod = new GeneralMethod(cont);
//        this.listBitmap=listItemImage;
        ipAddress = MHandler.getAllSettings().get(0).getIpAddress();
        itemUnit=MHandler.getAllSettings().get(0).getItemUnit();
        rate_customer = MHandler.getRateOfCustomer();
        getTimeAndDate();
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View itemView = View.inflate(context.getActivity(), R.layout.item_horizontal_listview,null);

        LinearLayout linearLayout;
        CardView cardView;
        TableRow row_qty, table_solidQty;
        TextView itemNumber, itemName, tradeMark, category, unitQty, price, tax, barcode, posprice, textViewTax, textViewsolidQty;
        ImageView imagespecial;
        cardView = itemView.findViewById(R.id.cardView);
        linearLayout = itemView.findViewById(R.id.linear);
        itemNumber = itemView.findViewById(R.id.textViewItemNumber);
        itemName = itemView.findViewById(R.id.textViewItemName);
        tradeMark = itemView.findViewById(R.id.textViewTradeMark);
        category = itemView.findViewById(R.id.textViewCategory);
        unitQty = itemView.findViewById(R.id.textViewUnit_qty);
        price = itemView.findViewById(R.id.textViewPrice);
        tax = itemView.findViewById(R.id.textViewTax);
        barcode = itemView.findViewById(R.id.textViewBarcode);

        posprice = itemView.findViewById(R.id.textViewPosPrice);
        row_qty = itemView.findViewById(R.id.row_qty);
        table_solidQty = itemView.findViewById(R.id.table_solidQty);
        textViewsolidQty = itemView.findViewById(R.id.textViewsolidQty);
        imagespecial = itemView.findViewById(R.id.imagespecial);
        threeDForm = new DecimalFormat("00.000");
        textViewTax = itemView.findViewById(R.id.textTitleTax);
        showSolidQty = MHandler.getAllSettings().get(0).getShow_quantity_sold();

       itemNumber.setText(items.get(i).getItemNo());
      itemName.setText(items.get(i).getItemName());
    itemName.setMovementMethod(new ScrollingMovementMethod());
       tradeMark.setText(items.get(i).getItemName());
       category.setText("" + items.get(i).getCategory());


        return itemView;
    }


    private void getTimeAndDate() {
        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        formatTime = new SimpleDateFormat("hh:mm:ss");

        voucherDate = df.format(currentTimeAndDate);
        voucherDate = convertToEnglish(voucherDate);
        time = formatTime.format(currentTimeAndDate);
        time = convertToEnglish(time);
        Log.e("time", "" + time);
        df2 = new SimpleDateFormat("yyyy");
        voucherYear = df2.format(currentTimeAndDate);
        voucherYear = convertToEnglish(voucherYear);
    }
}
