package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.dr7.salesmanmanager.Adapters.TapsAdapter;
import com.dr7.salesmanmanager.Modles.CompanyInfo;
import com.dr7.salesmanmanager.Modles.Flag_Settings;
import com.dr7.salesmanmanager.Modles.Password;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.activeKey;
import com.dr7.salesmanmanager.Reports.SalesMan;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.dr7.salesmanmanager.LocationPermissionRequest.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.dr7.salesmanmanager.LocationPermissionRequest.openDialog;
import static com.dr7.salesmanmanager.MainActivity.latitude_main;
import static com.dr7.salesmanmanager.MainActivity.longitude_main;


@SuppressWarnings("unchecked")
public class Login extends AppCompatActivity {
   public static String SETTINGS_PREFERENCES="SETTINGS_PREFERENCES";
    public static String Items_Orent_PREF="Items_Orent_PREF";
    public static String  Smallericon_PREF="Smallericon_PREF";
    public static String  UpdateFlag="UpdateFlag";
    public static String  FirstRun_3="FirstRun_4";
    public static SharedPreferences sharedPref;
    public static boolean Items_Orent =true;

    public static EditText storeNo_edit;
    String user="", password="";
    List<Flag_Settings> flag_settingsList;
    private String username, link, ipAddress;
    private EditText usernameEditText, passwordEditText;
    private CircleImageView logo;
    private CardView loginCardView;
    public static String salesMan = "", salesManNo = "";
    private boolean isMasterLogin;
    public static int key_value_Db;
    activeKey model_key;
    int key_int;
    public static TextView   passwordrespon;
    Context context;
    TextView loginText,companyInfo_btn;
    EditText ipEditText;
    public static String userNo = "";
    SweetAlertDialog dialogTem, sweetAlertDialog;
    ImportJason importData;

    DatabaseHandler mDHandler;
    String shortUserName = "", fullUserName = "";
    int indexfirst = 0, indexEdit = 0;
    boolean exist = false;
    int index = 0;
    List<SalesMan> salesMenList;
    public static String languagelocalApp = "";
    FusedLocationProviderClient fusedLocationClient;
    LocationRequest mLocationRequest;
    public LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 3;
    public static Date currentTimeAndDate;
    public static SimpleDateFormat df, df2;
    public static String curentDate, curentTime;
    public static Location location_main;
    LinearLayout mainlayout;
    String provider;
    int salesManInt=1;
    public static Timer timer = null;
    LocationPermissionRequest locationPermissionRequest;
    public static String currentIp="",previousIp="";
    String serialNo2="";
    int approveAdmin=0;
    int LocationTracker=0;
    public  static  TextView checkIpDevice,goMainText;
    public static Context contextG;
    FloatingActionButton setting_floatingBtn;
    public static  int typaImport=1;//0---- mySql   1-----IIs
    public static  int Purchase_Order=0;
    public  static int rawahneh=0;// 1= EXPORT STOCK TABLES
    public  static int getMaxVoucherServer=0;
    public  static int rawahneh_getMaxVouchFromServer =0;
    public  static int passwordSettingAdmin=0;//0 ---> static password   1 ----->password from admin
    public  static int makeOrders=0;// 1= just orders app

    public  static int OfferCakeShop=0;// if 0 calck offer many times
    public  static int Plan_Kind =0;// 1 if saleman plan based on day of week ,, 0 if saleman plan based on date
    public  static    int  offerTalaat=0;
    public  static    int   MaxpaymentvochFromServ=0;
    public  static    int  offerQasion=0;
    public   static  int  offernasleh=1;
    public  static    int getTotalBalanceInActivities=1;
    public  static    int dateFromToActive=1;

    public  static    int  talaatLayoutAndPassowrd=0;
    public  static    int  voucherReturn_spreat=0;
    public  static    int  updateOnlySelectedCustomer=0;// just for OneOOne

    public  static    int   SalsManPlanFlage=0;
    public  static    int   SalsManTripFlage=0;
    public  static    int   POS_ACTIVE=0;
    public  static    int   Plan_ACTIVE=1;
    public  static    int   Separation_of_the_serial=0;// for oppo
    public  static    String  headerDll = "/Falcons/VAN.dll";
//  public  static    String headerDll = "";

    public  static  int gone_noTax_totalDisc=0;
    public  static  int ExportedVochTaxFlage=0;
    public  static  int password_rawat=0;
    public  static  int password_talaat=0;
    public  static  String Mainpassword_setting="303090";
    public  static  String Secondpassword_setting ="2021000";
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleAppUtils.setConfigChange(Login.this);

        new LocaleAppUtils().changeLayot(Login.this);

        setContentView(R.layout.login_free_size);
        context=Login.this;
        initialView();


         sharedPref = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE);
         Items_Orent = sharedPref.getBoolean(Items_Orent_PREF,true);


     //if(   password_talaat==1) Secondpassword_setting ="2022111";
//        try{
//            exportForClient();
//        }catch (Exception e){
//
//        }

//        getIpAddressForDevice();
//        if(sharedPref==null)  sharedPref = getActivity().getSharedPreferences("SETTINGS_PREFERENCES", MODE_PRIVATE);
//        if  (sharedPref.getBoolean(Login.Smallericon_PREF, false)){}
        boolean isFirstRun = sharedPref.getBoolean(Login.FirstRun_3, true);

        SharedPreferences.Editor editor = sharedPref.edit();
        Log.e("isFirstRun","1"+isFirstRun);
        if (isFirstRun) {
            // Code to run once
            Log.e("isFirstRun","2"+isFirstRun);
            mDHandler.deleteSerialsPayment();

            editor.putBoolean(Login.FirstRun_3, false);
            editor.apply();
        }
         isFirstRun = sharedPref.getBoolean(Login.FirstRun_3, true);
        Log.e("isFirstRun","33"+isFirstRun);
        validLocation();


 //       getPasswords();
        try {
                if (mDHandler.getAllSettings().size() != 0) {
                    if (mDHandler.getAllSettings().get(0).getPassowrd_data()== 1) {
                        importData.getPassowrdSetting("2");
                    }

                if (mDHandler.getAllSettings().get(0).getArabic_language() == 1) {
                    languagelocalApp = "ar";
                    LocaleAppUtils.setLocale(new Locale("ar"));
                    LocaleAppUtils.setConfigChange(Login.this);

                } else {
                    languagelocalApp = "en";
                    LocaleAppUtils.setLocale(new Locale("en"));
                    LocaleAppUtils.setConfigChange(Login.this);

                }
                try {
                    if(updateOnlySelectedCustomer==1)
                    mDHandler.updateSettingOnlyCustomer(updateOnlySelectedCustomer);
                }catch (Exception e){

                }


            } else {
                languagelocalApp = "ar";
                LocaleAppUtils.setLocale(new Locale("ar"));
                LocaleAppUtils.setConfigChange(Login.this);

            }


            Log.e("languagelocalApp2", "" + languagelocalApp);

        } catch (Exception e) {
            languagelocalApp = "ar";
            LocaleAppUtils.setLocale(new Locale("ar"));
            LocaleAppUtils.setConfigChange(Login.this);

        }

        try {
            if (languagelocalApp.equals("ar")) {
                mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        } catch (Exception e) {
            mainlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        setLogo();


        ///B
        flag_settingsList = mDHandler.getFlagSettings();
        if (flag_settingsList.size() != 0) {

            typaImport = flag_settingsList.get(0).getData_Type().equals("mysql") ? 0 : 1;
            rawahneh = flag_settingsList.get(0).getExport_Stock();
            getMaxVoucherServer = flag_settingsList.get(0).getMax_Voucher();
            rawahneh_getMaxVouchFromServer = flag_settingsList.get(0).getMaxvochServer();
            makeOrders = flag_settingsList.get(0).getMake_Order();
            passwordSettingAdmin = flag_settingsList.get(0).getAdmin_Password();
            getTotalBalanceInActivities = flag_settingsList.get(0).getTotal_Balance();
            voucherReturn_spreat = flag_settingsList.get(0).getVoucher_Return();
            SalsManPlanFlage=flag_settingsList.get(0).getActiveSlasmanPlan();
            SalsManTripFlage=flag_settingsList.get(0).getActiveSlasmanTrips();
            POS_ACTIVE=flag_settingsList.get(0).getPos_active();
            OfferCakeShop = flag_settingsList.get(0).getOfferCakeShop();
            offerQasion = flag_settingsList.get(0).getOfferQasion();
            offerTalaat = flag_settingsList.get(0).getOfferTalaat();
            gone_noTax_totalDisc = flag_settingsList.get(0).getNoTax();
            ExportedVochTaxFlage = flag_settingsList.get(0).getExportedVoch_Tax();
            Purchase_Order = flag_settingsList.get(0).getPurchaseOrder();
            MaxpaymentvochFromServ= flag_settingsList.get(0).getPaymentvochServer();
            Log.e(" Purchase_Order==",""+ Purchase_Order);
            Log.e(" SalsManPlanFlage",""+ SalsManPlanFlage+"MaxpaymentvochFromServ"+MaxpaymentvochFromServ);
        } else {


            typaImport = 1;
//            rawahneh = 1;
//            getMaxVoucherServer = 1;
//            makeOrders = 0;
//            passwordSettingAdmin = 0;
//            getTotalBalanceInActivities = 0;
//            voucherReturn_spreat = 0;
//
//            mDHandler.insertFlagSettings(new Flag_Settings("iis", 1, 1,
//                    0, 0, 0, 0));

        }

        loginCardView.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                CompanyInfo companyLocation = mDHandler.getCompanyLocation();
                 user = usernameEditText.getText().toString().trim();
                 password = passwordEditText.getText().toString().trim();
                salesMenList = mDHandler.getAllSalesMen();

                if (salesMenList.size() == 0)//Empty DB
                {
//                    Toast.makeText(Login.this, R.string.failUsers, Toast.LENGTH_LONG).show();

                    if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {
                        if (passwordEditText.getText().toString().equals("2240m")) {
                            exist = true;
                            index = 1;
                            isMasterLogin = true;
                        } else {
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getResources().getString(R.string.warning_message))
                                    .setContentText(getResources().getString(R.string.failUsers))
                                    .show();
                        }
                        checkExistToLogin();
                    } else {
                        new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getResources().getString(R.string.warning_message))
                                .setContentText(getResources().getString(R.string.failUsers))
                                .show();
                        if (TextUtils.isEmpty(user))
                            usernameEditText.setError("Required");
                        if (TextUtils.isEmpty(password)) {
                            passwordEditText.setError("Required");
                        }

                    }

                } else {//item in list
//                    int order_make=mDHandler.getFlagSettings().get(0).getMake_Order();
//                    userNo=mDHandler.getAllSettings().get(0).getStoreNo().trim();
                    if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {

//                   if(userNo.equals(usernameEditText.getText().toString().trim())&&order_make==0)     {
                        if (passwordEditText.getText().toString().equals("2240m")) {
                            exist = true;
                            index = 1;
                            isMasterLogin = true;
                        } else {
                            exist = false;
                            isMasterLogin = false;
                            for (int i = 0; i < salesMenList.size(); i++) {
                                fullUserName = salesMenList.get(i).getUserName();//  00002
                                if ((fullUserName.charAt(0) + "").equals("0")) {
                                    if (checkAllCharacterName(i, fullUserName)) {
                                        break;
                                    }

                                } else {
                                    checkFullName(i, fullUserName);

                                }

                            }
                        }
                        checkExistToLogin();
                    }
//                    else
//                   {
//                       Log.e("userNo",userNo+"   "+usernameEditText.getText().toString().trim());
//                       usernameEditText.setError("");
//                   }
//                    }
                else {
                        if (TextUtils.isEmpty(user))
                            usernameEditText.setError("Required");
                        if (TextUtils.isEmpty(password)) {
                            passwordEditText.setError("Required");
                        }

                    }

                }


            }
        });
      // locationPermissionRequest.timerLocation();
        setting_floatingBtn.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {


//                copyFile();
                showSettingIpDialog();
            }
        });
        try {
                flag_settingsList = mDHandler.getFlagSettings();
        if (flag_settingsList.size() == 0) {
            getPasswords();
            showMoreSettingDialog();
        }
        }catch (Exception e){

        }
        mDHandler.deleteAllPreviusYear();
        try {
       //     Purchase_Order=0;
            mDHandler.getFlagSettings().get(0).setPurchaseOrder(0);
        }catch (Exception e){
            Log.e("mDHandler","setPurchaseOrder"+e.getMessage());
        }

//        try {
//            if (Build.VERSION.SDK_INT >= 30){
//                if (!Environment.isExternalStorageManager()){
//                    Intent getpermission = new Intent();
//                    getpermission.setAction(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    startActivity(getpermission);
//                }
//            }
//        }catch (Exception e){
//
//        }

    }

    private void getPasswords() {
        List<Password> passwords=mDHandler.getAdminPasswords();
        if (passwords.size() != 0)
        {
            for(int i=0;i<passwords.size();i++) {
                if (passwords.get(i).getPassword_type() == 1)
                    Mainpassword_setting = passwords.get(i).getPassword_no();
                else if (passwords.get(i).getPassword_type() == 2)
                    Secondpassword_setting = passwords.get(i).getPassword_no();

            }

            if(Mainpassword_setting.equals(""))  Mainpassword_setting="303090";
            if(Secondpassword_setting.equals(""))  Secondpassword_setting="2021000";
        }else{
            Mainpassword_setting="303090";
            Secondpassword_setting="2021000";
        }
//        Log.e("getPasswords=",Mainpassword_setting+"  "+Secondpassword_setting);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void copyFile()
    {
        try
        {
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File data = Environment.getDataDirectory();
            boolean isPresent = true;
            if (!sd.canWrite())
            {
                isPresent= sd.mkdir();

            }



            String backupDBPath = "VanSalesDatabase_backup";

            File currentDB= getApplicationContext().getDatabasePath("VanSalesDatabase");
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()&&isPresent) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(Login.this, "Backup Succesfulley", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(Login.this, "Backup Failed", Toast.LENGTH_SHORT).show();
            }
            isPresent=false;


        }
        catch (Exception e) {
            Log.e("Settings Backup", e.getMessage());
        }
    }

    private void showSettingIpDialog() {
        final Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ip_setting_dialog);
        dialog.show();

        //****************************
        ipEditText = (EditText) dialog.findViewById(R.id.ipEditText);
        final EditText portSetting = (EditText) dialog.findViewById(R.id.portSetting);
       storeNo_edit = (EditText) dialog.findViewById(R.id.storeNo_edit);
         TextView editIp= (TextView) dialog.findViewById(R.id.editIp);
        TextView editUserno= (TextView) dialog.findViewById(R.id.editUserNum);
        final EditText cono = (EditText) dialog.findViewById(R.id.cono);

        final Button cancel_button = (Button) dialog.findViewById(R.id.cancelBtn);
        final Button importDataBtn = (Button) dialog.findViewById(R.id.importData);

        //B
        final TextView more = dialog.findViewById(R.id.more);
        TextView editPort= (TextView) dialog.findViewById(R.id.editPort);
        TextView editCono= (TextView) dialog.findViewById(R.id.editCono);



        //********************************fill data******************************************
        if (mDHandler.getAllSettings().size() != 0) {
            ipEditText.setText(mDHandler.getAllSettings().get(0).getIpAddress());
            portSetting.setText(mDHandler.getAllSettings().get(0).getIpPort());
            storeNo_edit.setText(mDHandler.getAllUserNo());
            cono.setText(mDHandler.getAllSettings().get(0).getCoNo());

            ipEditText.setClickable(false);
            ipEditText.setEnabled(false);
            storeNo_edit.setEnabled(false);
            cono.setEnabled(false);
            portSetting.setEnabled(false);
//            ipEditText.setAlpha(0.5f);
        } else {
            ipEditText.setEnabled(true);
            storeNo_edit.setEnabled(true);
            portSetting.setEnabled(true);
            cono.setEnabled(true);
        }
        editIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ///
                showPasswordDialog(1);


            }
        });
        editPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIdetUuseNumPasswordDialog(portSetting);


            }
        });
        editCono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIdetUuseNumPasswordDialog(cono);


            }
        });

        editUserno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIdetUuseNumPasswordDialog(storeNo_edit);


            }
        });
        cancel_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //////B
        more.setOnClickListener(v -> {

           showPasswordDialog(2);






        });


        final Button ok_button = (Button) dialog.findViewById(R.id.saveSetting);
        ok_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateNotEmpty(ipEditText) && validateNotEmpty(storeNo_edit)) {
                    addIpSetting(ipEditText.getText().toString(), portSetting.getText().toString(), cono.getText().toString());
                    importData.getPassowrdSetting("2");//88
                    dialog.dismiss();
                    Log.e("validateNotEmpty", "validateNotEmpty");
                } else {
                    Log.e("validateNotEmpty", "NOTTTTT");
                }
            }
        });
        importDataBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateNotEmpty(ipEditText)&&validateNotEmpty(storeNo_edit))
                {
                    if(mDHandler.getAllSettings().size()==0)
                    {
                        addIpSetting(ipEditText.getText().toString(),portSetting.getText().toString(),cono.getText().toString());

                    }
                    else {
                        mDHandler.updateIpSetting(ipEditText.getText().toString(),portSetting.getText().toString(),cono.getText().toString());
                    }
                  //  mDHandler.deletAllSalesLogIn();

                    mDHandler.addUserNO(storeNo_edit.getText().toString(),context.getResources().getString(R.string.version));
                    refreshSalesName();

                   Log.e("mDHandler1=",mDHandler.getAllUserNo());
                   if( mDHandler.getAllUserNo()!=null) {
                        if ( mDHandler.getAllUserNo().equals(""))
                        {        mDHandler.addUserNO(storeNo_edit.getText().toString(),context.getResources().getString(R.string.version));
                            refreshSalesName();}

                        else{
                            Log.e("mDHandler6=",mDHandler.getAllUserNo());
                            mDHandler.updateUserNO(storeNo_edit.getText().toString(),context.getResources().getString(R.string.version));
                        }
                    }  else{
                       Log.e("mDHandler7=",mDHandler.getAllUserNo());
                        mDHandler.updateUserNO(Login.salesMan,context.getResources().getString(R.string.version));
                    }
                   if( Login.salesMan.trim().length()==0)
                    Login.salesMan=storeNo_edit.getText().toString().trim();

                   boolean isPosted=mDHandler.isAllVoucher_posted();
                   if(isPosted)
                   {


                       ImportJason importJason=new ImportJason(Login.this);
                       importJason.startParsing(storeNo_edit.getText().toString());


                   }else {
                       Toast.makeText(Login.this,R.string.failImpo_export_data , Toast.LENGTH_SHORT).show();

                   }


                    dialog.dismiss();
                }

            }
        });
    }

    private void refreshSalesName() {
        try {
            String name=mDHandler.getSalesmanName_fromSalesTeam();
            mDHandler.updateSalesManNameSetting(name.trim());
        }

        catch(Exception e){

        }
    }

    private void showMoreSettingDialog() {

        final Dialog moreDialog = new Dialog(Login.this);
        moreDialog.setCancelable(false);
        moreDialog.setContentView(R.layout.more_settings_dialog);
        moreDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(moreDialog.getWindow().getAttributes());
        lp.width = (int)(getResources().getDisplayMetrics().widthPixels/1.15);
        moreDialog.getWindow().setAttributes(lp);
        moreDialog.show();


        Button okBtn = moreDialog.findViewById(R.id.okBtn);
        LinearLayout plan_linear= moreDialog.findViewById(R.id.plan_linear);

        Button cancelBtn = moreDialog.findViewById(R.id.cancelBtn);

        RadioGroup radioGrpData = moreDialog.findViewById(R.id.radioGrpData);
//            RadioButton radioBtnSQL = moreDialog.findViewById(R.id.radioBtnSQL);
//            RadioButton radioBtnIIS = moreDialog.findViewById(R.id.radioBtnIIS);

        Switch swExport, swMax, swOrder, swPassword, swTotal, swReturn,plan,Trips,activePos,
                cakeshopSW, qasionSW, talaatSW,maxvochServer,purchaseOrder_switch,noTax_Sw,naslehSW,ExportedVochTax_Sw,maxpaymentvoch_SW;

        ImageButton cakeshopInfo, qasionInfo, talaatInfo,naslehInfo;
        TextView cakeExpandedText, qasionExpandedText, talaatExpandedText,naslehExpandedText;
        noTax_Sw=moreDialog.findViewById(R.id.noTax);

        purchaseOrder_switch=moreDialog.findViewById(R.id.purchaseOrder_switch);
//        purchaseOrder_switch.setVisibility(View.GONE);
        swExport = moreDialog.findViewById(R.id.swExport);
        swMax = moreDialog.findViewById(R.id.swMax);
        maxvochServer= moreDialog.findViewById(R.id.maxvochServer);
        swOrder = moreDialog.findViewById(R.id.swOrder);
        swPassword = moreDialog.findViewById(R.id.swPassword);
        swTotal = moreDialog.findViewById(R.id.swTotal);
        swReturn = moreDialog.findViewById(R.id.swReturn);
        plan = moreDialog.findViewById(R.id.SalsManPlan);
        Trips= moreDialog.findViewById(R.id.SalsManTrips);
        maxpaymentvoch_SW= moreDialog.findViewById(R.id.maxpaymentvoch_SW);
        if(Plan_ACTIVE==0)
        {
            plan_linear.setVisibility(View.GONE);
        }else plan_linear.setVisibility(View.VISIBLE);
        activePos= moreDialog.findViewById(R.id.activePos);
        cakeshopSW = moreDialog.findViewById(R.id.cakeshopSW);
        qasionSW = moreDialog.findViewById(R.id.qasionSW);
        naslehSW  = moreDialog.findViewById(R.id.naslehSW);
        ExportedVochTax_Sw = moreDialog.findViewById(R.id.exportedVoch);
        talaatSW = moreDialog.findViewById(R.id.talaatSW);
        cakeshopInfo = moreDialog.findViewById(R.id.cakeshopInfo);
        qasionInfo = moreDialog.findViewById(R.id.qasionInfo);
        talaatInfo = moreDialog.findViewById(R.id.talaatInfo);
        naslehInfo= moreDialog.findViewById(R.id.naslehInfo);
        cakeExpandedText = moreDialog.findViewById(R.id.cakeExpandedText);
        qasionExpandedText = moreDialog.findViewById(R.id.qasionExpandedText);
        talaatExpandedText = moreDialog.findViewById(R.id.talaatExpandedText);
        naslehExpandedText= moreDialog.findViewById(R.id.naslehExpandedText);
        flag_settingsList = mDHandler.getFlagSettings();

        if (flag_settingsList.size() != 0) {

            if (flag_settingsList.get(0).getData_Type().equals("mysql")) {
//                    radioBtnSQL.setChecked(true);
//                    radioBtnIIS.setChecked(false);
                radioGrpData.check(R.id.radioBtnSQL);
            } else {
//                    radioBtnSQL.setChecked(false);
//                    radioBtnIIS.setChecked(true);
                radioGrpData.check(R.id.radioBtnIIS);
            }
            noTax_Sw.setChecked((flag_settingsList.get(0).getNoTax() == 1));
            ExportedVochTax_Sw.setChecked((flag_settingsList.get(0).getExportedVoch_Tax() == 1));
            purchaseOrder_switch.setChecked((flag_settingsList.get(0).getPurchaseOrder() == 1));
            swExport.setChecked((flag_settingsList.get(0).getExport_Stock() == 1));
            swMax.setChecked((flag_settingsList.get(0).getMax_Voucher() == 1));
            maxvochServer .setChecked((flag_settingsList.get(0).getMaxvochServer() == 1));
            swOrder.setChecked((flag_settingsList.get(0).getMake_Order() == 1));
            swPassword.setChecked((flag_settingsList.get(0).getAdmin_Password() == 1));
            swTotal.setChecked((flag_settingsList.get(0).getTotal_Balance() == 1));
            swReturn.setChecked((flag_settingsList.get(0).getVoucher_Return() == 1));
            plan.setChecked((flag_settingsList.get(0).getActiveSlasmanPlan() == 1));
            Trips.setChecked((flag_settingsList.get(0).getActiveSlasmanTrips()== 1));
            activePos.setChecked((flag_settingsList.get(0).getPos_active() == 1));
            cakeshopSW.setChecked((flag_settingsList.get(0).getOfferCakeShop() == 1));
            qasionSW.setChecked((flag_settingsList.get(0).getOfferQasion() == 1));
            naslehSW.setChecked((flag_settingsList.get(0).getOffernasleh() == 1));
            talaatSW.setChecked((flag_settingsList.get(0).getOfferTalaat() == 1));
            maxpaymentvoch_SW.setChecked((flag_settingsList.get(0).getPaymentvochServer() == 1));
        }

        OnClickListener infoClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.naslehInfo) {

                    if (naslehExpandedText.getVisibility() == View.GONE)
                        naslehExpandedText.setVisibility(View.VISIBLE);
                    else
                        naslehExpandedText.setVisibility(View.GONE);

                }
           else   if (v.getId() == R.id.cakeshopInfo) {

                    if (cakeExpandedText.getVisibility() == View.GONE)
                        cakeExpandedText.setVisibility(View.VISIBLE);
                    else
                        cakeExpandedText.setVisibility(View.GONE);

                } else if (v.getId() == R.id.talaatInfo) {

                    if (talaatExpandedText.getVisibility() == View.GONE)
                        talaatExpandedText.setVisibility(View.VISIBLE);
                    else
                        talaatExpandedText.setVisibility(View.GONE);

                } else if (v.getId() == R.id.qasionInfo) {

                    if (qasionExpandedText.getVisibility() == View.GONE)
                        qasionExpandedText.setVisibility(View.VISIBLE);
                    else
                        qasionExpandedText.setVisibility(View.GONE);

                }

            }
        };

        cakeshopInfo.setOnClickListener(infoClickListener);
        talaatInfo.setOnClickListener(infoClickListener);
        qasionInfo.setOnClickListener(infoClickListener);
        naslehInfo.setOnClickListener(infoClickListener);
        okBtn.setOnClickListener(v1 -> {
            Log.e("okBtn", "okBtn");
            //update flag_settings
            //update variables
            String dataType1;
            if (radioGrpData.getCheckedRadioButtonId() == R.id.radioBtnSQL) {
                typaImport = 0;
                dataType1 = "mysql";
            } else {
                typaImport = 1;
                dataType1 = "iis";
            }

            gone_noTax_totalDisc=noTax_Sw.isChecked()?1:0;
            ExportedVochTaxFlage= ExportedVochTax_Sw.isChecked()?1:0;
            Purchase_Order=purchaseOrder_switch.isChecked()?1:0;
            rawahneh = swExport.isChecked() ? 1 : 0;
            getMaxVoucherServer = swMax.isChecked() ? 1 : 0;
            rawahneh_getMaxVouchFromServer = maxvochServer.isChecked() ? 1 : 0;
            makeOrders = swOrder.isChecked() ? 1 : 0;
            passwordSettingAdmin = swPassword.isChecked() ? 1 : 0;
            getTotalBalanceInActivities = swTotal.isChecked() ? 1 : 0;
            voucherReturn_spreat = swReturn.isChecked() ? 1 : 0;
            SalsManPlanFlage = plan.isChecked() ? 1 : 0;
            SalsManTripFlage= Trips.isChecked() ? 1 : 0;
            POS_ACTIVE = activePos.isChecked() ? 1 : 0;
            OfferCakeShop = cakeshopSW.isChecked() ? 1 : 0;
            offerQasion = qasionSW.isChecked() ? 1 : 0;
            offernasleh= naslehSW.isChecked() ? 1 : 0;
            offerTalaat = talaatSW.isChecked() ? 1 : 0;
            MaxpaymentvochFromServ=maxpaymentvoch_SW.isChecked() ? 1 : 0;

            if(flag_settingsList.size()==0)
            {
                mDHandler.insertFlagSettings(new Flag_Settings(dataType1, rawahneh, getMaxVoucherServer,
                        makeOrders, passwordSettingAdmin, getTotalBalanceInActivities, voucherReturn_spreat,SalsManPlanFlage,POS_ACTIVE,
                        OfferCakeShop, offerTalaat, offerQasion,SalsManTripFlage, rawahneh_getMaxVouchFromServer,Purchase_Order,gone_noTax_totalDisc,offernasleh,ExportedVochTaxFlage,MaxpaymentvochFromServ));
            }else {
                mDHandler.updateFlagSettings(dataType1, rawahneh, getMaxVoucherServer,
                        makeOrders, passwordSettingAdmin, getTotalBalanceInActivities, voucherReturn_spreat,SalsManPlanFlage,POS_ACTIVE,
                        OfferCakeShop, offerTalaat, offerQasion,SalsManTripFlage, rawahneh_getMaxVouchFromServer,Purchase_Order,gone_noTax_totalDisc,offernasleh,ExportedVochTaxFlage,MaxpaymentvochFromServ);
            }

            getPasswords();


            moreDialog.dismiss();

        });

        cancelBtn.setOnClickListener(v12 -> moreDialog.dismiss());
    }

    private void showPasswordDialog(int flag ) {
        final EditText editText = new EditText(Login.this);
        editText.setTextColor(getResources().getColor(R.color.text_view_color));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        SweetAlertDialog sweetMessage= new SweetAlertDialog(Login.this, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(getResources().getString(R.string.enter_password));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(editText.getText().toString().equals(Secondpassword_setting))
                {
                    if(flag==1){
                        ipEditText.setAlpha(1f);
                        ipEditText.setEnabled(true);
                        ipEditText.requestFocus();
                    }else {
                        if(flag==2){
                            showMoreSettingDialog();
                        }
                    }

                    sweetAlertDialog.dismissWithAnimation();

                }
                else {
                    editText.setError("Incorrect");

                }
            }
        })

                .show();
    }
    private void showIdetUuseNumPasswordDialog( TextView textInput) {
        final EditText editText = new EditText(Login.this);
        editText.setTextColor(getResources().getColor(R.color.text_view_color));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        SweetAlertDialog sweetMessage= new SweetAlertDialog(Login.this, SweetAlertDialog.NORMAL_TYPE);

        sweetMessage.setTitleText(getResources().getString(R.string.enter_password));
        sweetMessage .setConfirmText("Ok");
        sweetMessage.setCanceledOnTouchOutside(true);
        sweetMessage.setCustomView(editText);
        sweetMessage.setConfirmButton(getResources().getString(R.string.app_ok), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(editText.getText().toString().equals(Secondpassword_setting))
                {
                    textInput.setEnabled(true);
                    sweetAlertDialog.dismissWithAnimation();

                }
                else {
                    editText.setError("Incorrect");

                }
            }
        })

                .show();
    }
    private void addIpSetting(String ipAddress, String ipPort,String cono) {
        if(mDHandler.getAllSettings().size()==0)
        {
            mDHandler.addIPSetting(504,0,ipAddress,ipPort,cono);
            mDHandler.addIPSetting(506,0,ipAddress,ipPort,cono);
            mDHandler.addIPSetting(508,0,ipAddress,ipPort,cono);
            mDHandler.addIPSetting(1,0,ipAddress,ipPort,cono);
            mDHandler.addIPSetting(4,0,ipAddress,ipPort,cono);
            mDHandler.addIPSetting(2,0,ipAddress,ipPort,cono);
        }
        else {
            mDHandler.updateIpSetting(ipAddress,ipPort,cono);
        }

    }

    private boolean validateNotEmpty(EditText editText) {
        if(!editText.getText().toString().equals(""))
        {
            editText.setError(null);
            return true;
        }
        else {
            editText.setError(getResources().getString(R.string.reqired_filled));
            editText.requestFocus();
            return false;
        }

    }

    private void setLogo() {
        try {
            if (mDHandler.getAllCompanyInfo().get(0).getLogo() == null) {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.logo_vansales));
            } else {
                logo.setImageBitmap(mDHandler.getAllCompanyInfo().get(0).getLogo());
            }
        } catch (Exception e) {

        }
    }

    private void initialView() {
        checkIpDevice=findViewById(R.id.checkIpDevice);
        locationPermissionRequest = new LocationPermissionRequest(Login.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        passwordrespon= findViewById(R.id.passwordrespon);
        passwordrespon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                         if(editable.toString().trim().length()!=0){

                             if(passwordrespon.getText().toString().trim().equals("PASSWORDTYPE")){
                                 Log.e("passwordrespon=","passwordrespon");
                             //    getPasswords();
                             }
                         }
            }
        });
        provider = locationManager.getBestProvider(new Criteria(), false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDHandler = new DatabaseHandler(Login.this);
        model_key = new activeKey();
        goMainText=findViewById(R.id.goMainText);
        loginText = (TextView) findViewById(R.id.logInTextView);
        companyInfo_btn= (TextView) findViewById(R.id.companyInfo_btn);
        companyInfo_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                uploadDb();
                upload();
                openCompanyDialog();
            }
        });
        currentTimeAndDate = Calendar.getInstance().getTime();
        Log.e("currentTimeAndDate", "" + currentTimeAndDate);
        df = new SimpleDateFormat("dd/MM/yyyy");
        curentDate = df.format(currentTimeAndDate);
        curentDate = convertToEnglish(curentDate);
        Log.e("curentDate", "" + curentDate);

        df2 = new SimpleDateFormat("hh:mm:ss");
        curentTime = df2.format(currentTimeAndDate);
        curentTime = convertToEnglish(curentTime);
        Log.e("curentTime", "" + curentTime);
        mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
        setting_floatingBtn=findViewById(R.id.setting_floatingBtn);
//        if(typaImport==0)
//        {
//            setting_floatingBtn.setVisibility(View.GONE);
//        }
        logo = (CircleImageView) findViewById(R.id.imageView3);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginCardView = (CardView) findViewById(R.id.loginCardView);
        userNo= mDHandler.getAllUserNo();
        importData=new ImportJason(Login.this);
        goMainText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().length()!=0)
                {
                    if(editable.toString().equals("main"))
                    {

                        mainIntent();
                    }
                }
            }
        });
//        openApp();
        if(talaatLayoutAndPassowrd==1)
        {
            passwordSettingAdmin=1;
            offerTalaat=1;
            dateFromToActive=0;
            getTotalBalanceInActivities=0;
            offerQasion=0;
            OfferCakeShop=1;

        }
        SendMail send =new SendMail();


//            new Thread(new Runnable() {
//
//                public void run() {
//                    try {
//                        send.sendMail("test1","helo","1234developersteam@gmail","ta.email.2022.fa@gmail.com");
//                    } catch (Exception e) {
//                        Log.e("sendMail",""+e.getMessage());
//                        e.printStackTrace();
//                    }
//                    }
//                }).start();
            }
    private File exportFile(File src, File dst) throws IOException {

        //if folder does not exist

//        deleteFiles(dst.getPath() + File.separator + "InventoryDBase");
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File expFile = new File(dst.getPath() + File.separator + "VanSalesDatabase");

        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (Exception e) {


                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)//Tools.this.getResources().getString(R.string.save_SUCCESS)
                        .setTitleText("Exception ")
                        .setContentText("Exception Error in read File" + e.toString() + " DataBaseFile ")
                        .show();



        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

//        dialogSwite.dismissWithAnimation();
        return expFile;
    }
   void upload(){
        try
        {
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File data = Environment.getDataDirectory();
            boolean isPresent = true;
            if (!sd.canWrite())
            {
                isPresent= sd.mkdir();

            }



            String backupDBPath = "VanSalesDatabase";

            File currentDB= getApplicationContext().getDatabasePath("VanSalesDatabase");
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()&&isPresent) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();

                src.transferTo(0, src.size(), dst);
//                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(Login.this, "Backup Succesfulley", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(Login.this, "Backup Failed", Toast.LENGTH_SHORT).show();
            }
            isPresent=false;


            Log.e("backupDB.getA", backupDB.getAbsolutePath());
        }
        catch (Exception e) {
            Log.e("Settings Backup", e.getMessage());
        }
    }
    private void uploadDb() {


        String backupDBPath = "VanSalesDatabase";
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File backupDB = new File(sd, backupDBPath);

        File f=new File("Documents/VanSalesDatabase");


        File Dba = new File("/data/data/com.dr7.salesmanmanager/databases");

//        String dstPathw = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dsts = new File(sd + "VanSalesDatabase");
        Log.e("myAppDataBasedstPath", " " + sd);
        Log.e("myAppDataBaseDb", " " + Dba);
        try {
            exportFile(dsts, Dba);
        } catch (IOException e) {
            e.printStackTrace();
        }





        FileInputStream fis=null;
        FileOutputStream fos=null;

        try
        {
            fis=new FileInputStream(f);
            fos=new FileOutputStream(f);
            while(true)
            {
                int i=fis.read();
                if(i!=-1)
                {fos.write(i);}
                else
                {break;}
            }
            fos.flush();
            Toast.makeText(this, "DB dump OK", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "DB dump ERROR", Toast.LENGTH_LONG).show();
        }
        finally
        {
            try
            {
                fos.close();
                fis.close();
            }
            catch(Exception ioe)
            {}
        }
    }


    private void openCompanyDialog() {

        final Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.company_info_dialog);


        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);
        dialog.getWindow().setLayout(width, height);
        lp2.copyFrom(dialog.getWindow().getAttributes());
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGrp);
        RadioButton radioTop = dialog.findViewById(R.id.radioTop);
        RadioButton radioBottom = dialog.findViewById(R.id.radioBottom);

        LinearLayout mainLinear = dialog.findViewById(R.id.linearCompany);
        try {
            if (languagelocalApp.equals("ar")) {
                mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                if (languagelocalApp.equals("en")) {
                    mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

            }
        } catch (Exception e) {
            mainLinear.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        final EditText name = (EditText) dialog.findViewById(R.id.com_name);
        final EditText tel = (EditText) dialog.findViewById(R.id.com_tel);
        final EditText tax = (EditText) dialog.findViewById(R.id.tax_no);
        final EditText noteInvoice = (EditText) dialog.findViewById(R.id.notes);
        final EditText salesman_name = (EditText) dialog.findViewById(R.id.salesman_name);
        TableRow note_position = dialog.findViewById(R.id.note_position);
        final EditText salesman_car = (EditText) dialog.findViewById(R.id.salesman_car);
        final EditText salesman_id = (EditText) dialog.findViewById(R.id.salesman_id);

            final  TextView savecompanyLocation=(TextView) dialog.findViewById(R.id.savecompanyLocation);

           ImageView logo = (ImageView) dialog.findViewById(R.id.logo);

            Button okButton = (Button) dialog.findViewById(R.id.okBut);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);
            okButton.setText(getResources().getString(R.string.ok));
            cancelButton.setVisibility(View.GONE);
            DatabaseHandler mDbHandler=new DatabaseHandler(Login.this);
            try {
                if (mDbHandler.getAllCompanyInfo().size() != 0) {
                    name.setText("" + mDbHandler.getAllCompanyInfo().get(0).getCompanyName());
                    name.setEnabled(false);
                    tel.setText("" + mDbHandler.getAllCompanyInfo().get(0).getcompanyTel());
                    tel.setEnabled(false);
                    tax.setText("" + mDbHandler.getAllCompanyInfo().get(0).getTaxNo());
                    tax.setEnabled(false);
//            logo.setImageDrawable(new BitmapDrawable(getResources(), mDbHandler.getAllCompanyInfo().get(0).getLogo()));
                    logo.setBackground(new BitmapDrawable(getResources(), mDbHandler.getAllCompanyInfo().get(0).getLogo()));
//                    itemBitmapPic= mDbHandler.getAllCompanyInfo().get(0).getLogo();
                    noteInvoice.setVisibility(View.GONE);
                    note_position.setVisibility(View.GONE);
                    salesman_id.setText(mDbHandler.getAllCompanyInfo().get(0).getNational_id()+"");
                    salesman_id.setEnabled(false);
                    salesman_car.setText(mDbHandler.getAllCompanyInfo().get(0).getCarNo()+"");
                    salesman_car.setEnabled(false);
                    noteInvoice.setText(""+mDbHandler.getAllCompanyInfo().get(0).getNoteForPrint());
                    noteInvoice.setEnabled(false);
                    if(mDbHandler.getAllCompanyInfo().get(0).getNotePosition().equals("1"))
                    {
                        radioBottom.setChecked(true);
                    }
                    else {
                        radioTop.setChecked(true);
                    }

                }
                else {
                    tax.setText(0+"");
                }
                String salesName=mDbHandler.getSalesmanName_fromSalesTeam();
                salesman_name.setText(salesName+"");
                salesman_name.setEnabled(false);
                Log.e("salesName",""+salesName);
            }catch ( Exception e){

            }


            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  dialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }


    private void openApp() {
//        KeyguardManager mKeyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock mLock = mKeyGuardManager.newKeyguardLock("Login");
//        mLock.disableKeyguard();
//
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
//                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
//                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
//        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
//               Login.this.getPackageName());
//        startActivity(intent);
    }

//    private String getIpAddressForDevice() {
//        String ipNo="";
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
//        }
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return "";
//            }
//            else {
//                ipNo = Build.getSerial();
//            }
//
//        }
//        else {
//            ipNo = Build.SERIAL;
//        }
//        Log.e("getMacAddress","MAC Address : " + ipNo);
//
//
//        Log.e("getMacAddress","serialNo2"+ipNo);
//        return ipNo;
//    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    else {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

//                            serialNo2 = Build.getSerial();


                        }
                        else {
                            serialNo2 = Build.SERIAL;
                        }

                       Log.e("serialNo2","Permissions  "+serialNo2);
                    }
                } else {
                    //not granted
                }
                break;

            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if(mDHandler.getAllSettings().get(0).getApproveAdmin()==1)
                {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Location", "granted");
                        Log.e("LocationIn","GoToMain 1");

                        // locationPermissionRequest.displayLocationSettingsRequest(Login.this);
                    //    startService(new Intent(Login.this, MyServices.class));
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
                        // permission was granted, yay! Do the
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(Login.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            Log.e("Location", "granted updates");
                            Log.e("LocationIn","GoToMain 2");
                            //Request location updates:
//                        locationPermissionRequest.
//                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                        }

                    } else {
                        Log.e("LocationIn","GoToMain 3");
                        Log.e("Location", "Deny");
                        // permission, denied, boo! Disable the
                        // functionality that depends on this permission.

                    }
                }

                break;
            }




            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
////                new AlertDialog.Builder(this)
////                        .setCancelable(false)
////                        .setTitle(R.string.title_location_permission)
////                        .setMessage(R.string.text_location_permission)
////                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////                                //Prompt the user once explanation has been shown
////                                    ActivityCompat.requestPermissions(Login.this,
////                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
////                                        MY_PERMISSIONS_REQUEST_LOCATION);
////                            }
////                        })
////                        .create()
////                        .show();
//
//
//         dialogLoc();
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//                Log.e("Location","explanation need");
//
//            }
//            return false;
//        } else {
//            Log.e("Location","true need");
//            return true;
//        }
//    }
//
//    private void dialogLoc() {
//
//
//        sweetAlertDialog.setTitleText(R.string.title_location_permission);
//        sweetAlertDialog.setContentText(String.valueOf(R.string.text_location_permission));
//        sweetAlertDialog.setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.dismissWithAnimation();
//                finish();
//            }
//        });
//        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(Login.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//                dialogTem=sweetAlertDialog;
//            }
//        });
//        sweetAlertDialog.setCancelable(false);
//        sweetAlertDialog.show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.e("Location","granted");
//                    sweetAlertDialog.dismissWithAnimation();
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        Log.e("Location","granted updates");
//
//                        //Request location updates:
////                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
//                    }
//
//                } else {
//
//                    Log.e("Location","Deny");
//                    // permission, denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//                return;
//            }
//
//        }
//    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkExistToLogin() {
        if (exist) {
            if (isMasterLogin) {
                key_value_Db = mDHandler.getActiveKeyValue();
                if (key_value_Db == 0) {//dosent exist value key in DB

                    showDialog_key();
                } else {

                    salesMan = convertToEnglish(usernameEditText.getText().toString());
                    salesManNo = passwordEditText.getText().toString();
                    try {
                        Transaction transaction=new Transaction();
                        transaction.setCheckInDate(curentDate);
                        transaction.setCheckInTime(curentTime);
                        transaction.setLongtude(location_main.getLongitude());
                        transaction.setLatitud(location_main.getLatitude());
                        transaction.setSalesManId(Integer.parseInt(salesMan));
                        transaction.setSalesManId(Integer.parseInt(salesMan));
                        mDHandler.addlogin(transaction);
                    }
                    catch (Exception e){

                    }
                    if(mDHandler.getAllSettings().size()!=0)
                    {
                        if(mDHandler.getAllSettings().get(0).getApproveAdmin()==1)
                        {
//                            goToMain();
                            verifyIpDevice();
                        }
                        else {

                            verifyIpDevice();
//                            goToMain();
                        }
                    }
                    else {
//                        goToMain();
                        verifyIpDevice();
                    }

//                    locationPermissionRequest.closeLocation();

//                    if(validLocation()){}
                   
//                                CustomIntent.customType(getBaseContext(),"left-to-right");
                }
            } else {

                if (salesMenList.get(index).getPassword().equals(passwordEditText.getText().toString())) {
                    key_value_Db = mDHandler.getActiveKeyValue();
                    if (key_value_Db == 0) {//dosent exist value key in DB

                        showDialog_key();
                    } else {

                        salesMan =convertToEnglish( usernameEditText.getText().toString());
                        salesManNo =passwordEditText.getText().toString();
//                       locationPermissionRequest.closeLocation();
                        verifyIpDevice();
//                       goToMain();
//                                CustomIntent.customType(getBaseContext(),"left-to-right");


                    }

                } else
                    Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }

        } else
            Toast.makeText(Login.this, "UserName does not exist", Toast.LENGTH_SHORT).show();
        exist = false;
    }

    private void getIpDevice() {
//        currentIp=getIpAddressForDevice();
        previousIp=getPreviousIpForSalesMen();
        //V22219AQ02457

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void  verifyIpDevice(){
        String  userStore=mDHandler.getAllUserNo();

        if(mDHandler.getFlagSettings().get(0).getMake_Order()==0){
            if(userStore.equals(""))
            {try {


                userStore=mDHandler.getAllSettings().get(0).getStoreNo();
                Log.e("verifyIpDevice","="+userStore+"\tuser="+user);
            }catch (Exception e){
                userStore="1";
            }
            }

            if(user.trim().equals(userStore.trim()))
                goToMain();
            else{
                if(userStore.equals(""))
                { goToMain();}
                else usernameEditText.setError("*Invalid");
            }
        }else {
            goToMain();
        }



//        getIpDevice();
//        Log.e("checkIpDevice",""+currentIp+"\t"+previousIp);
//        if(previousIp.equals(currentIp)||previousIp.equals("")){
//            goToMain();
//
//        }
//        else {
//            if(!previousIp.equals(currentIp)&& !previousIp.equals(""))
//            {
//                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText(getResources().getString(R.string.warning_message))
//                        .setContentText(getResources().getString(R.string.userNotOwnwerDevice))
//                        .show();
//            }
//
//        }
    }

    private void addCurentIp(String currentIp) {
        ImportJason importJason=new ImportJason(Login.this);
        importJason.addCurentIp(currentIp);
    }

    private String getPreviousIpForSalesMen() {

        String ipDevice=mDHandler.getIpAddresDevice_fromSalesTeam();
        Log.e("getPreviousIpFo","ipDevice"+ipDevice);
        if(ipDevice.equals(""))
        {
//          ipDevice= getIpAddressForDevice();
           mDHandler.updatIpDevice(ipDevice);
           addCurentIp(ipDevice);
            return  "";
        }
        else {
            return ipDevice;
        }

//        ImportJason importJason=new ImportJason(Login.this);
//        importJason.getPreviousIpForSalesMen();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void goToMain() {
        Log.e("goToMain","goToMain");
       List<Settings>settingsList= mDHandler.getAllSettings();
         approveAdmin=0;
        LocationTracker=0;
       try {
            approveAdmin = settingsList.get(0).getApproveAdmin();
           LocationTracker= settingsList.get(0).getLocationtracker();
       }catch (Exception e){
            approveAdmin=0;
           LocationTracker=0;
       }
        Log.e("uttttttt","ll "+Utils.getIPAddress(true)); // IPv6

   //     mDHandler.deletAllSalesLogIn();
        Log.e("mDHandler2=",mDHandler.getAllUserNo());


     //   mDHandler.addUserNO(Login.salesMan);
        if( mDHandler.getAllUserNo()!=null) {
            if ( mDHandler.getAllUserNo().equals(""))
            {  mDHandler.addUserNO(Login.salesMan,context.getResources().getString(R.string.version));

            }

            else{
                Log.e("mDHandler3=",mDHandler.getAllUserNo());
                mDHandler.updateUserNO(Login.salesMan,context.getResources().getString(R.string.version));
            }
        }  else{
            Log.e("mDHandler4=",mDHandler.getAllUserNo());
            mDHandler.updateUserNO(Login.salesMan,context.getResources().getString(R.string.version));
        }
        refreshSalesName();


//        try {
//            if(!Login.salesMan.equals("1"))
//            {
//                if(Integer.parseInt(Login.salesMan)!=1)
//                {
//                    mDHandler.deleteExcept(Login.salesMan);
//                }
//
//            }
//
//        }catch (Exception e){
//            Log.e("deleteExcept",""+Login.salesMan);
//        }
        try {
            salesManInt=Integer.parseInt(salesMan);
        }catch (Exception e){
            salesManInt=1;
        }
        try{
            getMaxVoucherServer=mDHandler.getFlagSettings().get(0).getMax_Voucher();

        }catch(Exception e){

        }
        Log.e("getMaxVoucherServer","="+getMaxVoucherServer);
        if(typaImport==1&&getMaxVoucherServer==1&&Purchase_Order==0)//iis
        {
           boolean isPosted=mDHandler.isAllVoucher_posted();
            mainIntent();
//        if(isPosted)
//        {
//            getMaxVoucherFromServer(salesManInt);
//        }else {
//            Toast.makeText(Login.this,R.string.failImportMaxExportData , Toast.LENGTH_SHORT).show();
//            mainIntent();
//
//        }
           // mainIntent();
        }
        else {//mysql
            mainIntent();
        }






//        if(typaImport==1&&rawahneh_getMaxVouchFromServer==1)//iis
//        {
//            boolean isPosted = mDHandler.isAllVoucher_posted();
//            if (isPosted) {
//                getMaxVoucherFromServer(salesManInt);
//            }
//        }

}

    private void updateForKhashram() {
        mDHandler.getFlagSettings().get(0).setMax_Voucher(0);
        mDHandler.getFlagSettings().get(0).setMaxvochServer(0);
        getMaxVoucherServer=0;
        rawahneh_getMaxVouchFromServer=0;
        mDHandler.getFlagSettings().get(0).setMax_Voucher(0);
        mDHandler.getFlagSettings().get(0).setMaxvochServer(0);
        updateInDataBase();
    }

    private void updateInDataBase() {
        mDHandler.updateLastVoucher();

    }


    private void getMaxVoucherFromServer(int salesManInt) {
        importData=new ImportJason(Login.this);
        importData.getMaxVoucherNo(0);
    }
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void mainIntent(){
        Log.e("mainIntent===","mainIntent");
        if(LocationTracker==1) {
            boolean locCheck= locationPermissionRequest.checkLocationPermission();
            boolean isNetworkAvailable=isNetworkAvailable();
            if(!isNetworkAvailable){
                Toast.makeText(Login.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }

            Log.e("LocationIn===","GoToMain"+locCheck);
            if(locCheck){
                Log.e("LocationIn====","GoToMain IN "+locCheck);
//                startService(new Intent(Login.this, MyServices.class));
//                finish();
                Intent main = new Intent(Login.this, MainActivity.class);
                startActivity(main);
            }else {
                Log.e("LocationIn","GoToMain else "+locCheck);
            }

        }else {
            Log.e("LocationIn","GoToMain no LocationTracker =" +LocationTracker);
//            finish();
            Intent main = new Intent(Login.this, MainActivity.class);
           startActivity(main);
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private boolean validLocation() {
//        getCompanyLocation();
//        getCurentLocation();
//        compareLocation();
        return true;
    }
//    LocationCallback mLocationCallback = new LocationCallback(){
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Log.e("onLocationResult",""+locationResult);
//            Log.e("onLocationResultEn",""+convertToEnglish(locationResult+""));
//            if(getLocationComp)
//            {
//                for (Location location : locationResult.getLocations()) {
//                    Log.e("MainActivity", "getLocationComp: " + location.getLatitude() + " " + location.getLongitude());
//                    if (mDbHandler.getAllCompanyInfo().size() != 0) {
//                        if (mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany() == 0) {
//                            latitude_main = location.getLatitude();
//                            longitude_main = location.getLongitude();
//                            Log.e("updatecompanyInfo", "" + mDbHandler.getAllCompanyInfo().get(0).getLatitudeCompany());
//                            mDbHandler.updatecompanyInfo(latitude_main, longitude_main);
//                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                    .setTitleText(getResources().getString(R.string.succsesful))
//                                    .setContentText(getResources().getString(R.string.LocationSaved))
//                                    .show();
//
//
//                        }
//                    }
//                    else{
//
//                    }
//
//
//
//
//                    Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);
//
//
//
//                }
//                getLocationComp=false;
//            }
//            else {
//                if(CustomerListShow.Customer_Account.equals("")&& isClickLocation == 2)
//                {
//                    if(first!=1)
//                    {
//                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText(getResources().getString(R.string.warning_message))
//                                .setContentText(getResources().getString(R.string.pleaseSelectUser))
//                                .show();
//                    }
//
//
//                } else {
//
//
//                    if(isNetworkAvailable()){
//                        String latitude="",  longitude="" ;
//                        try {
//                            latitude = CustomerListShow.latitude;
//                            longitude = CustomerListShow.longtude;
//                            Log.e("latitude",""+latitude+longitude);
//                        }
//                        catch (Exception e)
//                        {
//                            latitude="";
//                            longitude="";
//
//                        }
//                        Log.e("latitude",""+latitude+longitude);
//
//
//                        if(!latitude.equals("")&&!longitude.equals("")&&isClickLocation==2)
//                        {
//
//                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                    .setTitleText(getResources().getString(R.string.warning_message))
//                                    .setContentText(getResources().getString(R.string.customerHaveLocation))
//                                    .show();
//                        }
//                        else {
//                            if (isClickLocation == 2) {
//                                for (Location location : locationResult.getLocations()) {
//                                    Log.e("MainActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
//                                    latitude_main = location.getLatitude();
//                                    longitude_main = location.getLongitude();
//                                    customerLocation_main = new CustomerLocation();
//                                    customerLocation_main.setCUS_NO(CustomerListShow.Customer_Account);
//                                    customerLocation_main.setLONG(longitude_main + "");
//                                    customerLocation_main.setLATIT(latitude_main + "");
//
//                                    mDbHandler.addCustomerLocation(customerLocation_main);
//                                    mDbHandler.updateCustomerMasterLocation(CustomerListShow.Customer_Account, latitude_main + "", longitude_main + "");
//                                    CustomerListShow.latitude = latitude_main + "";
//                                    CustomerListShow.longtude = longitude_main + "";
//                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                            .setTitleText(getResources().getString(R.string.succsesful))
//                                            .setContentText(getResources().getString(R.string.LocationSaved))
//                                            .show();
//
//
//                                    Log.e("saveCurrentLocation", "" + latitude_main + "\t" + longitude_main);
//
//
//
//                                }
//
//                            }
//
//                        }
//
//                    }
////            else {
////                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
////                        .setTitleText(getResources().getString(R.string.warning_message))
////                        .setContentText(getResources().getString(R.string.enternetConnection))
////                        .show();
////            }
//
//
//                }// END ELSE
//                isClickLocation=1;
//            }
//
//
//        };
//
//    };

    private void getCurentLocation() {
                            //****************************************************************
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


                  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {// Not granted permission

                        ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

                    }
//                    Thread.sleep(1000);


                    /////////////////////////////////////////**********************************
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(Login.this);
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(Login.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        location_main=new Location(location);

                                        location_main.setLatitude(latitude_main);
                                        location_main.setLongitude(longitude_main);
                                        Log.e("saveCurrentLocation", "" + location_main.getLatitude() + "\t" + location_main.getLongitude());

                                        new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(getResources().getString(R.string.succsesful))
                                                .setContentText(getResources().getString(R.string.LocationSaved))
                                                .show();
                                        Toast.makeText(Login.this, "latitude="+latitude_main+"long="+longitude_main, Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        new SweetAlertDialog(Login.this,SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText(getResources().getString(R.string.warning_message))
                                                .setContentText(getResources().getString(R.string.enternetConnection))
                                                .show();
                                    }
                                    // Logic to handle location object

                                }
                            });


}

    private void checkFullName(int i, String fullUserName) {
        if (usernameEditText.getText().toString().equals(fullUserName)) {
            exist = true;
            index = i;

        }
    }

    private boolean checkAllCharacterName(int i, String fullUserName) {
        for (int j = 0; j < fullUserName.length(); j++) {
            indexfirst = 0;
            if ((fullUserName.charAt(j) + "").equals("0")) {
                continue;
            } else {
                indexfirst = j;
                break;
            }

        }
        shortUserName = fullUserName.substring(indexfirst, fullUserName.length());

        //********************************************************************
        String editUser = usernameEditText.getText().toString();
        for (int j = 0; j < editUser.length(); j++) {
            indexEdit = 0;
            if ((editUser.charAt(j) + "").equals("0")) {
                continue;
            } else {
                indexEdit = j;
                break;

            }
        }
        String shortUserEdit = editUser.substring(indexEdit, editUser.length());
        Log.e("checkAllCharacterName", "" + shortUserEdit + "\t" + shortUserName);
        //********************************************************************

        if (shortUserEdit.equals(shortUserName)) {
            exist = true;
            index = i;
            return true;
        }
        return false;
    }

    public void showDialog_key() {
        final Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.active_key);
        dialog.show();
        // if not eist value key in DB
        //*****************************
//        mDHandler.deleteKeyValue();
        model_key.setKey(1111);
        mDHandler.addKey(model_key);
        //****************************
        final EditText key_value = (EditText) dialog.findViewById(R.id.editText_active_key);
        final Button cancel_button = (Button) dialog.findViewById(R.id.button_cancel_key);
        cancel_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final Button ok_button = (Button) dialog.findViewById(R.id.button_activeKey);
        ok_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                key_int = Integer.parseInt(key_value.getText().toString());
                key_value_Db = mDHandler.getActiveKeyValue();
                Log.e("key_value_Db", "" + key_value_Db);
                if (key_value_Db == key_int) {
                    salesMan =convertToEnglish(  usernameEditText.getText().toString());
                    salesManNo = passwordEditText.getText().toString();
//
                    Toast.makeText(Login.this, "welcome" + salesMan, Toast.LENGTH_SHORT).show();

//                    locationPermissionRequest.closeLocation();
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    //  CustomIntent.customType(getBaseContext(),"left-to-right");
                    dialog.dismiss();
                } else {
                    Toast.makeText(Login.this, "Please enter valid Active key", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 10001:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        openDialog=false;
                        Toast.makeText(Login.this, states.isLocationPresent() + "", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        openDialog=false;
                        Toast.makeText(Login.this, "Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private class RequestLogin extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @SuppressLint("WrongThread")
        @Override
        protected Object doInBackground(Object[] objects) {

            try {

                username = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                String data = URLEncoder.encode("USERNAME=" + username, "UTF-8");
                data += "&" + URLEncoder.encode("PASSWORD=" + password, "UTF-8");

                URL url = new URL(link);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter =
                        new OutputStreamWriter(urlConnection.getOutputStream());

                outputStreamWriter.write(data);

                String line = null;


            } catch (Exception e) {

            }

            return null;
        }
    }// Class RequestLogin
    void    exportForClient(){

        DatabaseHandler mDHandler=new DatabaseHandler(Login.this);
        mDHandler.updateDataForClient();
    }
}
