package com.dr7.salesmanmanager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Reports.Reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CustomerCheckInFragment.CustomerCheckInInterface, CustomerListShow.CustomerListShow_interface {

    private static final String TAG = "MainActivity";
    public static int menuItemState;
    static public TextView mainTextView;
    static int checknum;
    private DatabaseHandler mDbHandler;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude, longitude;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainTextView = (TextView) findViewById(R.id.mainTextView);

        mDbHandler = new DatabaseHandler(MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            openSetting alert = new openSetting();
            alert.showDialog(this, "Error de conexión al servidor");

        } else if (id == R.id.action_cust_check_in) {
            checknum = 1;
            menuItemState = 1;
            openSelectCustDialog();

        } else if (id == R.id.action_add_cust) {
            openAddCustomerDialog();

        } else if (id == R.id.action_cust_check_out) {

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

                    CustomerCheckInFragment obj = new CustomerCheckInFragment();
                    obj.editCheckOutTimeAndDate();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.app_no), null);
            builder.setMessage(getResources().getString(R.string.app_confirm_dialog_msg));
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
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

                            ExportJason obj = new ExportJason(MainActivity.this);
                            obj.startExportDatabase();
                            //obj.storeInDatabase();

                        }
                    })
                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.customers_location) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_imp_data) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to update data ? This will take few minutes !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            ImportJason obj = new ImportJason(MainActivity.this);
                            obj.startParsing();
                            //obj.storeInDatabase();

                        }
                    })
                    .setNegativeButton("Cancel", null).show();

        } else if (id == R.id.nav_sign_out) {
            Intent intent = new Intent(this, CPCL2Menu.class);
            startActivity(intent);

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
        customerCheckInFragment.show(getSupportFragmentManager(), "");
    }

    public void openAddCustomerDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_customer_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setLayout(730, 330);

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
                            latitude, longitude));
                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Please add customer name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public class openSetting {

        public void showDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.fragment_setting);

            final EditText linkEditText = (EditText) dialog.findViewById(R.id.link);
            final EditText invoicEditText = (EditText) dialog.findViewById(R.id.invoice_serial);
            final EditText returnEditText = (EditText) dialog.findViewById(R.id.return_serial);
            final EditText orderEditText = (EditText) dialog.findViewById(R.id.order_serial);
            final EditText paymentEditTextCash = (EditText) dialog.findViewById(R.id.payments_serial_cash);
            final EditText paymentEditTextCheque = (EditText) dialog.findViewById(R.id.payments_serial_cheque);
            final RadioGroup taxCalc = (RadioGroup) dialog.findViewById(R.id.taxTalc);
            final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.price_by_cust);
            final RadioButton exclude = (RadioButton) dialog.findViewById(R.id.excludeRadioButton);
            final RadioButton include = (RadioButton) dialog.findViewById(R.id.includeRadioButton);

            Button okButton = (Button) dialog.findViewById(R.id.okBut);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelBut);

            if (mDbHandler.getAllSettings().size() != 0) {
                linkEditText.setText("" + mDbHandler.getAllSettings().get(0).getIpAddress());
                invoicEditText.setText("" + (mDbHandler.getMaxSerialNumber(504) + 1));
                returnEditText.setText("" + (mDbHandler.getMaxSerialNumber(506) + 1));
                orderEditText.setText("" + (mDbHandler.getMaxSerialNumber(508) + 1));
                paymentEditTextCash.setText("" + (mDbHandler.getMaxSerialNumber(0) + 1));
                paymentEditTextCheque.setText("" + (mDbHandler.getMaxSerialNumber(4) + 1));
                if (mDbHandler.getAllSettings().get(0).getTaxClarcKind() == 0)
                    exclude.setChecked(true);
                else
                    include.setChecked(true);

                if (mDbHandler.getAllSettings().get(0).getPriceByCust() == 1)
                    checkBox.setChecked(true);
            }

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!linkEditText.getText().toString().equals("")) {
                        String link = linkEditText.getText().toString();
                        int invoice = Integer.parseInt(invoicEditText.getText().toString()) - 1;
                        int return1 = Integer.parseInt(returnEditText.getText().toString()) - 1;
                        int order = Integer.parseInt(orderEditText.getText().toString()) - 1;
                        int paymentCash = Integer.parseInt(paymentEditTextCash.getText().toString()) - 1;
                        int paymentCheque = Integer.parseInt(paymentEditTextCheque.getText().toString()) - 1;

                        int taxKind = taxCalc.getCheckedRadioButtonId() == R.id.excludeRadioButton ? 0 : 1;
                        int priceByCust = checkBox.isChecked() ? 1 : 0;

                        mDbHandler.deleteAllSettings();
                        mDbHandler.addSetting(link, taxKind, 504, invoice, priceByCust);
                        mDbHandler.addSetting(link, taxKind, 506, return1, priceByCust);
                        mDbHandler.addSetting(link, taxKind, 508, order, priceByCust);
                        mDbHandler.addSetting(link, taxKind, 0, paymentCash, priceByCust);
                        mDbHandler.addSetting(link, taxKind, 4, paymentCheque, priceByCust);

                        dialog.dismiss();
                    } else
                        Toast.makeText(MainActivity.this, "Please enter IP address", Toast.LENGTH_SHORT).show();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(800, 450);
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

}
