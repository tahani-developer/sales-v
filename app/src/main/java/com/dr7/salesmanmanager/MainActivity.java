package com.dr7.salesmanmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.PrinterSetting;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.VisitRate;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Reports.Reports;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CustomerCheckInFragment.CustomerCheckInInterface, CustomerListShow.CustomerListShow_interface {

    private static final String TAG = "MainActivity";
    public static int menuItemState;
    static public TextView mainTextView;
    LinearLayout checkInLinearLayout, checkOutLinearLayout;
    public static ImageView checkInImageView, checkOutImageView;
    static int checknum;
    private DatabaseHandler mDbHandler;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude, longitude;

    public static final int PICK_IMAGE = 1;
    Bitmap itemBitmapPic = null;
    ImageView logo;
    Calendar myCalendar;
    Bitmap visitPic = null;
    ImageView visitPicture;
    int amountOfmaxDiscount = 0;
    public static List<Transaction> transactions = new ArrayList<>();
    public static List<Voucher> vouchers = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Payment> paymentsPaper = new ArrayList<>();
    public static List<AddedCustomer> addedCustomer = new ArrayList<>();
    int sum_chech_export_lists=0;

    public static void settext2() {
        mainTextView.setText(CustomerListShow.Customer_Name);
    }

    @Override
    public void showCustomersList() {
        CustomerListFragment customerListFragment = new CustomerListFragment();
        customerListFragment.show(getSupportFragmentManager(), "");


    }

    @Override
    public void displayCustomerListShow() {
        try {
            CustomerListShow customerListShow = new CustomerListShow();
            customerListShow.setCancelable(true);
            customerListShow.setListener(this);
            customerListShow.show(getSupportFragmentManager(), "");
        } catch (Exception e) {

        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mainTextView = (TextView) findViewById(R.id.mainTextView);
        settext2();
        checkInLinearLayout = (LinearLayout) findViewById(R.id.checkInLinearLayout);
        checkOutLinearLayout = (LinearLayout) findViewById(R.id.checkOutLinearLayout);
        checkInImageView = (ImageView) findViewById(R.id.checkInImageView);
        checkOutImageView = (ImageView) findViewById(R.id.checkOutImageView);
        if (!CustomerListShow.Customer_Name.equals("No Customer Selected !"))//test after change language
        {
            checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in_black));
            checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out));
        }

        checkInLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_in));
                    if (CustomerListShow.Customer_Name.equals("No Customer Selected !")) {
                        checknum = 1;
                        menuItemState = 1;
                        openSelectCustDialog();
                    } else {
                        Toast.makeText(MainActivity.this, CustomerListShow.Customer_Name + " is checked in", Toast.LENGTH_SHORT).show();
                        checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in_black));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_in_hover));
                }
                return true;
            }
        });

        checkOutLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out));
                    if (!CustomerListShow.Customer_Name.equals("No Customer Selected !")) {
                        openCustCheckOut();
                    } else {
                        Toast.makeText(MainActivity.this, "No Customer Selected !", Toast.LENGTH_SHORT).show();
                        checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_out_black));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cus_check_out_hover));
                }
                return true;
            }
        });

        mDbHandler = new DatabaseHandler(MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                openAddCustomerDialog();
//                mDbHandler.updateSalesManItemBalance("1","1144",100);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.sales_man_name);
        navUsername.setText(Login.salesMan);
        navigationView.setNavigationItemSelectedListener(this);
        menuItemState = 0;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openPasswordDialog(1);
//        } else if (id == R.id.action_cust_check_in) {
//            checknum = 1;
//            menuItemState = 1;
//            openSelectCustDialog();
//
//        } else if (id == R.id.action_cust_check_out) {
//            openCustCheckOut();

        } else if (id == R.id.action_print_voucher) {
            Intent intent = new Intent(MainActivity.this, PrintVoucher.class);
            startActivity(intent);

//        } else if (id == R.id.action_add_cust) {
//            openAddCustomerDialog();

        } else if (id == R.id.action_company_info) {
            openPasswordDialog(2);

        } else if (id == R.id.de_export) {
            openPasswordDialog(3);
        } else if (id == R.id.printerSetting) {
            openPasswordDialog(4);
        }

        return super.

                onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (menuItemState == 1) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(true);
            menu.getItem(0).getIcon().setAlpha(130);
            menu.getItem(1).getIcon().setAlpha(255);
        } else if (menuItemState == 0) {
            menu.getItem(0).setEnabled(true);
            menu.getItem(1).setEnabled(false);
            menu.getItem(0).getIcon().setAlpha(255);
            menu.getItem(1).getIcon().setAlpha(130);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_activities) {
            Intent intent = new Intent(this, Activities.class);
            startActivity(intent);

        } else if (id == R.id.nav_reports) {
            Intent intent = new Intent(this, Reports.class);
            startActivity(intent);

        } else if (id == R.id.nav_exp_data) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to post data ? This will take few minutes !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {


                            Log.e("sumExport",""+sum_chech_export_lists);
                            if(mDbHandler.getAllSettings().get(0).getPassowrd_data()==1) {
                                openPasswordDialog(6);
                            }
                            else{
                                ExportJason obj = null;
                            try {
                                obj = new ExportJason(MainActivity.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            obj.startExportDatabase();

                            }





                            //obj.storeInDatabase();

                        }
                    })
                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.customers_location) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.sales_man_map) {
            Intent intent = new Intent(this, SalesmanMap.class);
            startActivity(intent);

        }

//                else{
//
//                        LocaleAppUtils.setLocale(new Locale("en"));
//                        LocaleAppUtils.setConfigChange(MainActivity.this);
//                        finish();
//                        startActivity(getIntent());
//                }



        else if (id == R.id.nav_imp_data) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to update data ? This will take few minutes !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            try {
                                if (mDbHandler.getAllSettings().get(0).getPassowrd_data() == 1) {
                                    openPasswordDialog(5);
                                } else {
                                    ImportJason obj = new ImportJason(MainActivity.this);
                                    obj.startParsing();


                                }
                            }catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
                            }


//                            transactions = mDbHandler.getAlltransactions();
//                            for (int i = 0; i < transactions.size(); i++)
//                                if (transactions.get(i).getIsPosted() == 0)
//                                {
//                                    sum_chech_export_lists++;
//                                    Log.e("sumExport",""+sum_chech_export_lists);
//                                    break ;//  sum=1
//
//                                }
//                            vouchers = mDbHandler.getAllVouchers();
//                            for (int i = 0; i < vouchers.size(); i++)
//                                if (vouchers.get(i).getIsPosted() == 0)
//                                {
//                                    sum_chech_export_lists++;//  sum=1
//                                    break;
//                                }
//
//                            items = mDbHandler.getAllItems();
//                            for (int i = 0; i < items.size(); i++)
//                                if (items.get(i).getIsPosted() == 0) {
//                                    sum_chech_export_lists++;//  sum=2
//                                    break;
//                                }
//                            payments = mDbHandler.getAllPayments();
//
//                            for (int i = 0; i < payments.size(); i++)
//                                if (payments.get(i).getIsPosted() == 0) {
//                                    sum_chech_export_lists++;//  sum=4
//                                    Log.e("sumExport",""+sum_chech_export_lists);
//                                    break;
//
//                                }
//
//                            paymentsPaper = mDbHandler.getAllPaymentsPaper();
//
//                            for (int i = 0; i < paymentsPaper.size(); i++)
//                                if (paymentsPaper.get(i).getIsPosted() == 0) {
//                                    sum_chech_export_lists++;//  sum=5
//                                    break;
//
//                                }
//
//                            addedCustomer = mDbHandler.getAllAddedCustomer();
//
//                            for (int i = 0; i < addedCustomer.size(); i++)
//                                if (addedCustomer.get(i).getIsPosted() == 0) {
//                                    sum_chech_export_lists++;//  sum=6
//                                    Log.e("sumExport",""+sum_chech_export_lists);
//                                    break;
//
//                                }
//                            if(sum_chech_export_lists>0)
//                            {
//                                Toast.makeText(MainActivity.this, "Please Do Export All Data Before importing the data ", Toast.LENGTH_SHORT).show();
//                                sum_chech_export_lists=0;
//
//
//                            }
//                            else {
////                                Log.e("sumExport",""+sum_chech_export_lists);
//                                openPasswordDialog(5);
//
//
//                            }



                            //obj.storeInDatabase();

                        }
                    })
                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.nav_refreshdata) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Confirm Update")
//                    .setMessage("Are you sure you want to refresh data ? This will take few minutes !")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {

            RefreshData obj = new RefreshData(MainActivity.this);
            obj.startParsing();
            //obj.storeInDatabase();

//                        }
//                    })
//                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.nav_sign_out) {
//            Intent intent = new Intent(this, CPCL2Menu.class);
//            startActivity(intent);

        } else if (id == R.id.nav_clear_local) {

            mDbHandler.deleteAllPostedData();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSelectCustDialog() {
        CustomerCheckInFragment customerCheckInFragment = new CustomerCheckInFragment();
        customerCheckInFragment.setCancelable(false);
        customerCheckInFragment.setListener(this);
        customerCheckInFragment.show(getFragmentManager(), "");
    }

    public void openMaxDiscount() {
        Log.e("openMaxDiscount", "yes");
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.max_discount_credit);

        final EditText amount = (EditText) dialog.findViewById(R.id.amount_discount_cridit);
        Button okButton = (Button) dialog.findViewById(R.id.okBut_discount);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut_discount);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amount.getText().toString().equals("")) {
                    amountOfmaxDiscount = Integer.parseInt(amount.getText().toString());
                    Log.e("amountOfmaxDiscount", "" + amountOfmaxDiscount);
                    //  mDbHandler.getAllSettings().get(0).setAmountOfMaxDiscount(amountOfmaxDiscount);
                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Incorrect Input !", Toast.LENGTH_SHORT).show();

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


    public void openAddCustomerDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_customer_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();


        final EditText addCus = (EditText) dialog.findViewById(R.id.custEditText);
        final EditText remark = (EditText) dialog.findViewById(R.id.remarkEditText);
        Button done = (Button) dialog.findViewById(R.id.doneButton);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addCus.getText().toString().equals("")) {
                    mDbHandler.addAddedCustomer(new AddedCustomer(addCus.getText().toString(), remark.getText().toString(),
                            latitude, longitude, Login.salesMan, 0, Login.salesManNo));
                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Please add customer name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public void openCustCheckOut() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.app_confirm_dialog));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.app_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checknum = 0;
                CustomerListShow.Customer_Name = "No Customer Selected !";
                settext2();
                menuItemState = 0;

                checkInImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_in));
                checkOutImageView.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.cus_check_out_black));

                CustomerCheckInFragment obj = new CustomerCheckInFragment();
                obj.editCheckOutTimeAndDate();

                openVisitRateDialog();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.app_no), null);
        builder.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void openVisitRateDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.visit_rate);
        dialog.setCanceledOnTouchOutside(true);

        final RadioGroup visitPurpose = dialog.findViewById(R.id.visit_purpose);
        RadioButton payment = dialog.findViewById(R.id.payment);
        RadioButton bankUp = dialog.findViewById(R.id.bank_up);
        RadioButton enterCat = dialog.findViewById(R.id.entering_categories);
        RadioButton vistSpase = dialog.findViewById(R.id.visit_presentation_space);
        RadioButton other = dialog.findViewById(R.id.other);

        final RadioButton r1 = dialog.findViewById(R.id.rate1);
        final RadioButton r2 = dialog.findViewById(R.id.rate2);
        final RadioButton r3 = dialog.findViewById(R.id.rate3);
        final RadioButton r4 = dialog.findViewById(R.id.rate4);
        final RadioButton r5 = dialog.findViewById(R.id.rate5);

        final CheckBox custRegard = dialog.findViewById(R.id.cust_regard);
        final CheckBox checkStore = dialog.findViewById(R.id.check_store);
        final CheckBox PromoCheckStore = dialog.findViewById(R.id.promotion_check_stock);
        final CheckBox selectProposedRequest = dialog.findViewById(R.id.select_proposed_request);
        final CheckBox display = dialog.findViewById(R.id.display_and_persuasion);

        visitPicture = dialog.findViewById(R.id.image);
        Button save = dialog.findViewById(R.id.save);
        Button cancel = dialog.findViewById(R.id.cancel);

        final int[] rate = {0};
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(false);
                r3.setChecked(false);
                r4.setChecked(false);
                r5.setChecked(false);
                rate[0] = 1;
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(false);
                r4.setChecked(false);
                r5.setChecked(false);
                rate[0] = 2;
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(true);
                r4.setChecked(false);
                r5.setChecked(false);
                rate[0] = 3;
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(true);
                r4.setChecked(true);
                r5.setChecked(false);
                rate[0] = 4;
            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r1.setChecked(true);
                r2.setChecked(true);
                r3.setChecked(true);
                r4.setChecked(true);
                r5.setChecked(true);
                rate[0] = 5;
            }
        });

        visitPicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1888);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int visitPurpos = 0;
                switch (visitPurpose.getCheckedRadioButtonId()) {
                    case R.id.payment:
                        visitPurpos = 0;
                        break;
                    case R.id.bank_up:
                        visitPurpos = 1;
                        break;
                    case R.id.entering_categories:
                        visitPurpos = 2;
                        break;
                    case R.id.visit_presentation_space:
                        visitPurpos = 3;
                        break;
                    case R.id.other:
                        visitPurpos = 4;
                        break;
                }

                int custRegar = custRegard.isChecked() ? 1 : 0;
                int checkStor = checkStore.isChecked() ? 1 : 0;
                int PromoCheckStor = PromoCheckStore.isChecked() ? 1 : 0;
                int selectProposedReques = selectProposedRequest.isChecked() ? 1 : 0;
                int displa = display.isChecked() ? 1 : 0;

                mDbHandler.addVisitRate(new VisitRate(visitPurpos, custRegar, checkStor, PromoCheckStor, selectProposedReques,
                        displa, rate[0], visitPic, CustomerListShow.Customer_Account, CustomerListShow.Customer_Name, Login.salesManNo));

                dialog.dismiss();
              //  Toast.makeText(MainActivity.this, "Saved !", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void openPasswordDialog(final int flag) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.password_dialog);

        final EditText password = (EditText) dialog.findViewById(R.id.editText1);
        Button okButton = (Button) dialog.findViewById(R.id.button1);
        Button cancelButton = (Button) dialog.findViewById(R.id.button2);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("301190")) {
                    dialog.dismiss();

                    if (flag == 1) {
                        openSetting alert = new openSetting();
                        alert.showDialog(MainActivity.this, "Error de conexión al servidor");
                    } else if (flag == 2)
                        openCompanyInfoDialog();

                    else if (flag == 3) {
                        openDeExportDialog();
                    } else if (flag == 4) {
                        openPrintSetting();
                    }
                    else if (flag == 5) {
                        ImportJason obj = new ImportJason(MainActivity.this);
                        obj.startParsing();

                    }
                    else if (flag == 6) {
                        ExportJason obj = null;
                        try {
                            obj = new ExportJason(MainActivity.this);

                            obj.startExportDatabase();
                        }
                        catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }

                    }
                } else
                    Toast.makeText(MainActivity.this, "Incorrect Password !", Toast.LENGTH_SHORT).show();
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

    public class openSetting {

        @SuppressLint("SetTextI18n")
        public void showDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.fragment_setting);

            final EditText linkEditText = (EditText) dialog.findViewById(R.id.link);
            final EditText numOfCopy = (EditText) dialog.findViewById(R.id.num_of_copy);
            final EditText invoicEditText = (EditText) dialog.findViewById(R.id.invoice_serial);
            final EditText returnEditText = (EditText) dialog.findViewById(R.id.return_serial);
            final EditText orderEditText = (EditText) dialog.findViewById(R.id.order_serial);
            final EditText paymentEditTextCash = (EditText) dialog.findViewById(R.id.payments_serial_cash);
            final EditText paymentEditTextCheque = (EditText) dialog.findViewById(R.id.payments_serial_cheque);
            final RadioGroup taxCalc = (RadioGroup) dialog.findViewById(R.id.taxTalc);

            final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.price_by_cust);
            final CheckBox checkBox2 = (CheckBox) dialog.findViewById(R.id.use_weight_case);
            final RadioGroup printMethod = (RadioGroup) dialog.findViewById(R.id.printMethod);
            final RadioButton bluetooth = (RadioButton) dialog.findViewById(R.id.bluetoothRadioButton);
            final RadioButton wifi = (RadioButton) dialog.findViewById(R.id.wifiRadioButton);
            final CheckBox allowMinus = (CheckBox) dialog.findViewById(R.id.allow_sale_with_minus);
            final CheckBox salesManCustomersOnly = (CheckBox) dialog.findViewById(R.id.salesman_customers_only);
            final CheckBox minSalePrice = (CheckBox) dialog.findViewById(R.id.min_sale_price);
            final CheckBox allowOutOfRange = (CheckBox) dialog.findViewById(R.id.allow_cust_check_out_range);
            final RadioButton exclude = (RadioButton) dialog.findViewById(R.id.excludeRadioButton);
            final RadioButton include = (RadioButton) dialog.findViewById(R.id.includeRadioButton);
            final CheckBox checkBox_canChangePrice = (CheckBox) dialog.findViewById(R.id.can_change_price);
            final CheckBox readDiscount = (CheckBox) dialog.findViewById(R.id.read_discount);
            final CheckBox workOnline = (CheckBox) dialog.findViewById(R.id.work_online);
            final CheckBox paymetod_check = (CheckBox) dialog.findViewById(R.id.checkBox_paymethod_check);
            final CheckBox bonusNotAlowed = (CheckBox) dialog.findViewById(R.id.checkBox_bonus_notallowed);
            final CheckBox noOfferForCredit = (CheckBox) dialog.findViewById(R.id.checkBox_NoOffer_forCredit);
            final CheckBox customerAuthor = (CheckBox) dialog.findViewById(R.id.CustomerAuthorize_checkbox);
            final CheckBox passowrdData_checkbox = (CheckBox) dialog.findViewById(R.id.PassowrdData_checkbox);
            final CheckBox arabicLanguage_checkbox = (CheckBox) dialog.findViewById(R.id.ArabicLanguage_checkbox);
            Button okButton = (Button) dialog.findViewById(R.id.okBut);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);

            if (mDbHandler.getAllSettings().size() != 0) {
                linkEditText.setText("" + mDbHandler.getAllSettings().get(0).getIpAddress());
                numOfCopy.setText("" + mDbHandler.getAllSettings().get(0).getNumOfCopy());
                invoicEditText.setText("" + (mDbHandler.getMaxSerialNumber(504) + 1));
                returnEditText.setText("" + (mDbHandler.getMaxSerialNumber(506) + 1));
                orderEditText.setText("" + (mDbHandler.getMaxSerialNumber(508) + 1));
                paymentEditTextCash.setText("" + (mDbHandler.getMaxSerialNumber(1) + 1));//test
                paymentEditTextCheque.setText("" + (mDbHandler.getMaxSerialNumber(4) + 1));

                if (mDbHandler.getAllSettings().get(0).getPrintMethod() == 0)
                    bluetooth.setChecked(true);
                else
                    wifi.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0)
                    exclude.setChecked(true);
                else
                    include.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getPriceByCust() == 1)
                    checkBox.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getUseWeightCase() == 1)
                    checkBox2.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getAllowMinus() == 1)
                    allowMinus.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getSalesManCustomers() == 1)
                    salesManCustomersOnly.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getMinSalePric() == 1)
                    minSalePrice.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getAllowOutOfRange() == 1)
                    allowOutOfRange.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getCanChangePrice() == 1)
                    checkBox_canChangePrice.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getReadDiscountFromOffers() == 1)
                    readDiscount.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getBonusNotAlowed() == 1)
                    bonusNotAlowed.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getWorkOnline() == 1)
                    workOnline.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getPaymethodCheck() == 1)
                    paymetod_check.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getCustomer_authorized() == 1)
                    customerAuthor.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getPassowrd_data() == 1)
                    passowrdData_checkbox.setChecked(true);
                if (mDbHandler.getAllSettings().get(0).getArabic_language() == 1) {
                    arabicLanguage_checkbox.setChecked(true);
                }





//                if (mDbHandler.getAllSettings().get(0).getNoOffer_for_credit() == 1)
//                    noOfferForCredit.setChecked(true);


            }
            arabicLanguage_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if( arabicLanguage_checkbox.isChecked())
                   {
                        LocaleAppUtils.setLocale(new Locale("ar"));
                    }
                   else {    LocaleAppUtils.setLocale(new Locale("en"));}

                }
            });
            noOfferForCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (noOfferForCredit.isChecked()) {
                        Log.e("noOfferForCredit", "yes");
                        openMaxDiscount();
                    }
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocaleAppUtils.setConfigChange(MainActivity.this);
                    finish();
                    startActivity(getIntent());
                    settext2();

                    if (!(linkEditText.getText().toString().equals(""))) {
                        if ((!numOfCopy.getText().toString().equals("")) && !invoicEditText.getText().toString().equals("") && !returnEditText.getText().toString().equals("") &&
                                !orderEditText.getText().toString().equals("") && !paymentEditTextCash.getText().toString().equals("") && !paymentEditTextCheque.getText().toString().equals("")) {

                            if (Integer.parseInt(numOfCopy.getText().toString()) < 5) {
                                String link = linkEditText.getText().toString().trim();
                                int numOfCopys = Integer.parseInt(numOfCopy.getText().toString());
                                int invoice = Integer.parseInt(invoicEditText.getText().toString()) - 1;
                                int return1 = Integer.parseInt(returnEditText.getText().toString()) - 1;
                                int order = Integer.parseInt(orderEditText.getText().toString()) - 1;
                                int paymentCash = Integer.parseInt(paymentEditTextCash.getText().toString()) - 1;
                                int paymentCheque = Integer.parseInt(paymentEditTextCheque.getText().toString()) - 1;

                                int taxKind = taxCalc.getCheckedRadioButtonId() == R.id.excludeRadioButton ? 0 : 1;
                                int pprintMethod = printMethod.getCheckedRadioButtonId() == R.id.bluetoothRadioButton ? 0 : 1;
                                int priceByCust = checkBox.isChecked() ? 1 : 0;
                                int useWeightCase = checkBox2.isChecked() ? 1 : 0;
                                int alowMinus = allowMinus.isChecked() ? 1 : 0;
                                int salesManCustomers = salesManCustomersOnly.isChecked() ? 1 : 0;
                                int minSalePric = minSalePrice.isChecked() ? 1 : 0;
                                int alowOutOfRange = allowOutOfRange.isChecked() ? 1 : 0;
                                int canChangPrice = checkBox_canChangePrice.isChecked() ? 1 : 0;
                                int readDiscountFromoffer = readDiscount.isChecked() ? 1 : 0;
                                int workOnlin = workOnline.isChecked() ? 1 : 0;
                                int paymethodCheck = paymetod_check.isChecked() ? 1 : 0;
                                int bonusNotalow = bonusNotAlowed.isChecked() ? 1 : 0;
                                int noOffer_Credit = noOfferForCredit.isChecked() ? 1 : 0;
                                int Customerauthorized = customerAuthor.isChecked() ? 1 : 0;
                                int passordData = passowrdData_checkbox.isChecked() ? 1 : 0;
                                int arabicLanguage = arabicLanguage_checkbox.isChecked() ? 1 : 0;

                                mDbHandler.deleteAllSettings();

                                mDbHandler.addSetting(link, taxKind, 504, invoice, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage);
                                mDbHandler.addSetting(link, taxKind, 506, return1, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage);
                                mDbHandler.addSetting(link, taxKind, 508, order, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage);
                                mDbHandler.addSetting(link, taxKind, 1, paymentCash, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage);
                                mDbHandler.addSetting(link, taxKind, 4, paymentCheque, priceByCust, useWeightCase, alowMinus, numOfCopys, salesManCustomers, minSalePric, pprintMethod, alowOutOfRange, canChangPrice, readDiscountFromoffer, workOnlin, paymethodCheck, bonusNotalow, noOffer_Credit, amountOfmaxDiscount,Customerauthorized,passordData,arabicLanguage);

                                dialog.dismiss();
                            } else
                                Toast.makeText(MainActivity.this, "Number of copies must be maximum 4 !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter All Enformation Filed", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Please enter IP address", Toast.LENGTH_SHORT).show();
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

        }

    }

    @SuppressLint("SetTextI18n")
    public void openCompanyInfoDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.company_info_dialog);

        final EditText name = (EditText) dialog.findViewById(R.id.com_name);
        final EditText tel = (EditText) dialog.findViewById(R.id.com_tel);
        final EditText tax = (EditText) dialog.findViewById(R.id.tax_no);
        logo = (ImageView) dialog.findViewById(R.id.logo);

        Button okButton = (Button) dialog.findViewById(R.id.okBut);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);

        if (mDbHandler.getAllCompanyInfo().size() != 0) {
            name.setText("" + mDbHandler.getAllCompanyInfo().get(0).getCompanyName());
            tel.setText("" + mDbHandler.getAllCompanyInfo().get(0).getcompanyTel());
            tax.setText("" + mDbHandler.getAllCompanyInfo().get(0).getTaxNo());
            logo.setImageDrawable(new BitmapDrawable(getResources(), mDbHandler.getAllCompanyInfo().get(0).getLogo()));
        }

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                intent.putExtra("crop", "false");
//                intent.putExtra("scale", true);
//                intent.putExtra("outputX", 256);
//                intent.putExtra("outputY", 256);
//                intent.putExtra("aspectX", 0);
//                intent.putExtra("aspectY", 0);
//                intent.putExtra("return-data", true);
//                startActivityForResult(intent, 1);

//                Intent intent = new Intent();
//                //******call android default gallery
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                //******code for crop image
//                intent.putExtra("crop", "true");
//                intent.putExtra("aspectX", 0);
//                intent.putExtra("aspectY", 0);
//                try {
//                    intent.putExtra("return-data", true);
//                    startActivityForResult(
//                            Intent.createChooser(intent,"Complete action using"),
//                            2);
//                } catch (ActivityNotFoundException e) {}

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("") && !tel.getText().toString().equals("") && !tax.getText().toString().equals("")) {
                    String comName = name.getText().toString().trim();
                    int comTel = Integer.parseInt(tel.getText().toString());
                    int taxNo = Integer.parseInt(tax.getText().toString());

                    mDbHandler.deleteAllCompanyInfo();
                    mDbHandler.addCompanyInfo(comName, comTel, taxNo, itemBitmapPic);

                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Please ensure your inputs", Toast.LENGTH_SHORT).show();
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

    public void openPrintSetting() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.printer_setting);

        final RadioButton lk30, lk32, lk31, qs,dotMatrix;
        lk30 = (RadioButton) dialog.findViewById(R.id.LK30);
        lk31 = (RadioButton) dialog.findViewById(R.id.LK31);

        lk32 = (RadioButton) dialog.findViewById(R.id.LK32);
        qs = (RadioButton) dialog.findViewById(R.id.QS);

        dotMatrix=(RadioButton) dialog.findViewById(R.id.dotMatrix);

        Button save = (Button) dialog.findViewById(R.id.save);

        int printer = mDbHandler.getPrinterSetting();

        switch (printer) {
            case 0:
                lk30.setChecked(true);
                break;
            case 1:
                lk31.setChecked(true);
                break;
            case 2:
                lk32.setChecked(true);
                break;
            case 3:
                qs.setChecked(true);
                break;
            case 4:
                dotMatrix.setChecked(true);
                break;

        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHandler.deleteAllPrinterSetting();
                PrinterSetting printerSetting = new PrinterSetting();

                if (lk30.isChecked()) {
                    printerSetting.setPrinterName(0);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "lk30");
                } else if (lk31.isChecked()) {
                    printerSetting.setPrinterName(1);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "lk31");
                } else if (lk32.isChecked()) {
                    printerSetting.setPrinterName(2);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "lk32");
                } else if (qs.isChecked()) {
                    printerSetting.setPrinterName(3);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "qs");
                }else if (dotMatrix.isChecked()) {
                    printerSetting.setPrinterName(4);
                    mDbHandler.addPrinterSeting(printerSetting);
                    Log.e("click ", "dotMatrix");
                }
dialog.dismiss();
            }

        });


        dialog.show();


    }


    public void openDeExportDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.de_export_dialog);

        final EditText from_date = (EditText) dialog.findViewById(R.id.from_date);
        final EditText to_date = (EditText) dialog.findViewById(R.id.to_date);
        final RadioGroup exportTerm = (RadioGroup) dialog.findViewById(R.id.export_term);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        from_date.setText(today);
        to_date.setText(today);

        myCalendar = Calendar.getInstance();
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, openDatePickerDialog(from_date), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, openDatePickerDialog(to_date), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.okBut);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!from_date.getText().toString().equals("") && !to_date.getText().toString().equals("")) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Confirm Update")
                            .setMessage("Are you sure you want to post data ? This will take few minutes !")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    int flag;

                                    if (exportTerm.getCheckedRadioButtonId() == R.id.invoiceRadioButton)
                                        flag = 0;
                                    else if (exportTerm.getCheckedRadioButtonId() == R.id.paymentRadioButton)
                                        flag = 1;
                                    else
                                        flag = 2;

                                    DeExportJason obj = new DeExportJason(MainActivity.this, from_date.getText().toString(),
                                            to_date.getText().toString(), flag);

                                    obj.startExportDatabase();
                                    //obj.storeInDatabase();

                                }
                            })
                            .setNegativeButton("Cancel", null).show();

                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Please select date !", Toast.LENGTH_SHORT).show();
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

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final EditText editText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };
        return date;
    }


    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                itemBitmapPic = bitmap;
                logo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                visitPic = extras.getParcelable("data");
                visitPicture.setImageDrawable(new BitmapDrawable(getResources(), visitPic));
            }
        }
    }

    private void openCheckInDialog() {
        RelativeLayout myLayout = new RelativeLayout(this);
        TextView enterCustTextView = new TextView(this);
        enterCustTextView.setText(getResources().getString(R.string.app_enter_cust_code));
        EditText custCodeEditText = new EditText(this);
        Button confirmButton = new Button(this);
        confirmButton.setText(getResources().getString(R.string.app_Confirm));
        Button searchButton = new Button(this);
        searchButton.setText(getResources().getText(R.string.app_search));
        myLayout.setPadding(50, 40, 50, 10);
        enterCustTextView.setId('1');
        custCodeEditText.setId('2');
        confirmButton.setId('3');
        searchButton.setId('4');

        RelativeLayout.LayoutParams textViewDetails =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

        textViewDetails.addRule(RelativeLayout.ALIGN_LEFT);

        RelativeLayout.LayoutParams editDetails =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

        editDetails.addRule(RelativeLayout.ALIGN_LEFT);

        editDetails.addRule(RelativeLayout.BELOW, enterCustTextView.getId());

        RelativeLayout.LayoutParams buttonDetails1 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        buttonDetails1.addRule(RelativeLayout.ALIGN_RIGHT);
        buttonDetails1.addRule(RelativeLayout.ALIGN_BOTTOM);
        buttonDetails1.addRule(RelativeLayout.BELOW, custCodeEditText.getId());
        buttonDetails1.addRule(RelativeLayout.RIGHT_OF, searchButton.getId());

        RelativeLayout.LayoutParams buttonDetails2 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        buttonDetails2.addRule(RelativeLayout.ALIGN_RIGHT);
        buttonDetails2.addRule(RelativeLayout.ALIGN_BOTTOM);
        buttonDetails2.addRule(RelativeLayout.BELOW, custCodeEditText.getId());
        //buttonDetails2.addRule(RelativeLayout.LEFT_OF, confirmButton.getId());

        myLayout.addView(enterCustTextView, textViewDetails);
        myLayout.addView(custCodeEditText, editDetails);
        myLayout.addView(confirmButton, buttonDetails1);
        myLayout.addView(searchButton, buttonDetails2);

        AlertDialog.Builder enterCustDialog = new AlertDialog.Builder(this);
        enterCustDialog.setTitle(getResources().getString(R.string.app_select_cust));
        enterCustDialog.setCancelable(false);
        enterCustDialog.setNegativeButton("Cancel", null);
        enterCustDialog.setView(myLayout);
        enterCustDialog.create().show();
    }//openCheckInDialog

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

}
