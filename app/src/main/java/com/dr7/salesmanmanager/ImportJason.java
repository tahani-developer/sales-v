package com.dr7.salesmanmanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dr7.salesmanmanager.Modles.Customer;
import com.dr7.salesmanmanager.Modles.CustomerPrice;
import com.dr7.salesmanmanager.Modles.ItemUnitDetails;
import com.dr7.salesmanmanager.Modles.ItemsMaster;
import com.dr7.salesmanmanager.Modles.Offers;
import com.dr7.salesmanmanager.Modles.PriceListD;
import com.dr7.salesmanmanager.Modles.PriceListM;
import com.dr7.salesmanmanager.Modles.SalesManAndStoreLink;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.SalesTeam;
import com.dr7.salesmanmanager.Modles.SalesmanStations;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Reports.SalesMan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ImportJason extends AppCompatActivity{

    private String URL_TO_HIT ;
    private Context context;
    private ProgressDialog progressDialog;
    DatabaseHandler mHandler;

    public static List<Customer> customerList = new ArrayList<>();
    public static List<ItemUnitDetails> itemUnitDetailsList = new ArrayList<>();
    public static List<ItemsMaster> itemsMasterList = new ArrayList<>();
    public static List<PriceListD> priceListDpList = new ArrayList<>();
    public static List<PriceListM> priceListMpList = new ArrayList<>();
    public static List<SalesTeam> salesTeamList = new ArrayList<>();
    public static List<SalesManItemsBalance> salesManItemsBalanceList = new ArrayList<>();
    public static List<SalesManAndStoreLink> salesManAndStoreLinksList = new ArrayList<>();
    public static List<SalesMan> salesMenList = new ArrayList<>();
    public static List<CustomerPrice> customerPricesList = new ArrayList<>();
    public static List<Offers> offersList = new ArrayList<>();
    public static List<SalesmanStations> salesmanStationsList = new ArrayList<>();

    public ImportJason(Context context){
        this.context = context ;
        this.mHandler = new DatabaseHandler(context);
    }

    public void startParsing(){
        List<Settings> settings =  mHandler.getAllSettings();
        if(settings.size() != 0) {
            String ipAddress = settings.get(0).getIpAddress();
            URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
            new JSONTask().execute(URL_TO_HIT);
        }
    }

    void storeInDatabase() {
        new SQLTask().execute(URL_TO_HIT);
    }

    private class JSONTask extends AsyncTask<String, String, List<Customer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected List<Customer> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

   //             URL url = new URL(URL_TO_HIT);
//                connection = url.openConnection();
//                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder buffer = new StringBuilder();
//                String line = null;
//                // Read Server Response
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                    break;
//                }

                String link= URL_TO_HIT;


                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf('1'), "UTF-8");

                URL url = new URL(link);

                URLConnection conn = url.openConnection();

              /*  HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                try {
                    request.setURI(new URI(link));
                }catch (Exception e)
                {

                }

                HttpResponse response = client.execute(request);*/


                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));


                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String finalJson = sb.toString();
                Log.e("finalJson*********" , finalJson);

                JSONObject parentObject = new JSONObject(finalJson);

                JSONArray parentArrayCustomers = parentObject.getJSONArray("CUSTOMERS");
                customerList.clear();
                for (int i = 0; i < parentArrayCustomers.length(); i++) {
                    JSONObject finalObject = parentArrayCustomers.getJSONObject(i);

                    Customer Customer = new Customer();
                    Customer.setCompanyNumber(finalObject.getInt("ComapnyNo"));
                    Customer.setCustId(finalObject.getString("CustID"));
                    Customer.setCustName(finalObject.getString("CustName"));
                    Customer.setAddress(finalObject.getString("Address"));
//                    if (finalObject.getString("IsSuspended") == null)
                    Customer.setIsSuspended(0);
//                    else
//                        Customer.setIsSuspended(finalObject.getInt("IsSuspended"));
                    Customer.setPriceListId(finalObject.getString("PriceListID"));
                    Customer.setCashCredit(finalObject.getInt("CashCredit"));
                    Customer.setSalesManNumber(finalObject.getString("SalesManNo"));
                    Customer.setCreditLimit(finalObject.getDouble("CreditLimit"));
                    Customer.setPayMethod(finalObject.getInt("PAYMETHOD"));
                    Customer.setCustLat(finalObject.getString("LATITUDE"));
                    Customer.setCustLong(finalObject.getString("LONGITUDE"));

                    customerList.add(Customer);
                }

                JSONArray parentArrayItem_Unit_Details = parentObject.getJSONArray("Item_Unit_Details");
                itemUnitDetailsList.clear();
                for (int i = 0; i < parentArrayItem_Unit_Details.length(); i++) {
                    JSONObject finalObject = parentArrayItem_Unit_Details.getJSONObject(i);

                    ItemUnitDetails item = new ItemUnitDetails();
                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                    item.setItemNo(finalObject.getString("ItemNo"));
                    item.setUnitId(finalObject.getString("UnitID"));
                    item.setConvRate(finalObject.getDouble("ConvRate"));

                    itemUnitDetailsList.add(item);
                }

                JSONArray parentArrayItems_Master = parentObject.getJSONArray("Items_Master");
                itemsMasterList.clear();
                for (int i = 0; i < parentArrayItems_Master.length(); i++) {
                    JSONObject finalObject = parentArrayItems_Master.getJSONObject(i);

                    ItemsMaster item = new ItemsMaster();
                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                    item.setItemNo(finalObject.getString("ItemNo"));
                    item.setName(finalObject.getString("Name"));
                    item.setCategoryId(finalObject.getString("CateogryID"));
                    item.setBarcode(finalObject.getString("Barcode"));
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));
                    item.setPosPrice(finalObject.getDouble("F_D"));
                    item.setIsSuspended(0);
                    item.setItemL(finalObject.getDouble("ItemL"));

                    itemsMasterList.add(item);
                }

                JSONArray parentArrayPrice_List_D = parentObject.getJSONArray("Price_List_D");

                priceListDpList.clear();
                for (int i = 0; i < parentArrayPrice_List_D.length(); i++) {
                    JSONObject finalObject = parentArrayPrice_List_D.getJSONObject(i);

                    PriceListD item = new PriceListD();
                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                    item.setPrNo(finalObject.getInt("PrNo"));
                    item.setItemNo(finalObject.getString("ItemNo"));
                    item.setUnitId(finalObject.getString("UnitID"));
                    item.setPrice(finalObject.getDouble("Price"));
                    item.setTaxPerc(finalObject.getDouble("TaxPerc"));
                    item.setMinSalePrice(finalObject.getDouble("MINPRICE"));

                    priceListDpList.add(item);
                }
                Log.e("priceList " , ""+ priceListDpList.get(0).getPrice());


                JSONArray parentArrayPrice_List_M = parentObject.getJSONArray("Price_List_M");
                priceListMpList.clear();
                for (int i = 0; i < parentArrayPrice_List_M.length(); i++) {
                    JSONObject finalObject = parentArrayPrice_List_M.getJSONObject(i);

                    PriceListM item = new PriceListM();
                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                    item.setPrNo(finalObject.getInt("PrNo"));
                    item.setDescribtion(finalObject.getString("Description"));
                    item.setIsSuspended(0);
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));

                    priceListMpList.add(item);
                }

                JSONArray parentArraySales_Team = parentObject.getJSONArray("Sales_Team");
                salesTeamList.clear();
                for (int i = 0; i < parentArraySales_Team.length(); i++) {
                    JSONObject finalObject = parentArraySales_Team.getJSONObject(i);

                    SalesTeam item = new SalesTeam();
                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                    item.setSalesManNo(finalObject.getString("SalesManNo"));
                    item.setSalesManName(finalObject.getString("SalesManName"));
                    item.setIsSuspended(0);
//                    item.setIsSuspended(finalObject.getInt("IsSuspended"));

                    salesTeamList.add(item);
                }

                JSONArray parentArraySalesMan_Items_Balance = parentObject.getJSONArray("SalesMan_Items_Balance");
                salesManItemsBalanceList.clear();
                for (int i = 0; i < parentArraySalesMan_Items_Balance.length(); i++) {
                    JSONObject finalObject = parentArraySalesMan_Items_Balance.getJSONObject(i);

                    SalesManItemsBalance item = new SalesManItemsBalance();
                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
                    item.setSalesManNo(finalObject.getString("SalesManNo"));
                    item.setItemNo(finalObject.getString("ItemNo"));
                    item.setQty(finalObject.getDouble("Qty"));

                    salesManItemsBalanceList.add(item);
                }

//                JSONArray parentArraySalesmanAndStoreLink = parentObject.getJSONArray("SalesmanAndStoreLink");
//                salesManAndStoreLinksList.clear();
//                for (int i = 0; i < parentArraySalesmanAndStoreLink.length(); i++) {
//                    JSONObject finalObject = parentArraySalesmanAndStoreLink.getJSONObject(i);
//
//                    SalesManAndStoreLink item = new SalesManAndStoreLink();
//                    item.setCompanyNo(finalObject.getInt("ComapnyNo"));
//                    item.setSalesManNo(finalObject.getInt("SalesmanNo"));
//                    item.setStoreNo(finalObject.getInt("StoreNo"));
//
//                    salesManAndStoreLinksList.add(item);
//                }

                JSONArray parentArraySalesMan = parentObject.getJSONArray("SALESMEN");
                salesMenList.clear();
                for (int i = 0; i < parentArraySalesMan.length(); i++) {
                    JSONObject finalObject = parentArraySalesMan.getJSONObject(i);

                    SalesMan salesMan = new SalesMan();
                    salesMan.setPassword(finalObject.getString("USER_PASSWORD"));
                    salesMan.setUserName(finalObject.getString("SALESNO"));

                    Log.e("*******" , finalObject.getString("SALESNO"));
                    salesMenList.add(salesMan);
                }

                JSONArray parentArrayCustomerPrice = parentObject.getJSONArray("customer_prices");
                customerPricesList.clear();
                for (int i = 0; i < parentArrayCustomerPrice.length(); i++) {
                    JSONObject finalObject = parentArrayCustomerPrice.getJSONObject(i);

                    CustomerPrice price = new CustomerPrice();
                    price.setItemNumber(finalObject.getInt("ITEMNO"));
                    price.setCustomerNumber(finalObject.getInt("CUSTOMER_NO"));
                    price.setPrice(finalObject.getDouble("PRICE"));

                    customerPricesList.add(price);
                }

                JSONArray parentArrayOffers = parentObject.getJSONArray("VN_PROMOTION");
                offersList.clear();
                for (int i = 0; i < parentArrayOffers.length(); i++) {
                    JSONObject finalObject = parentArrayOffers.getJSONObject(i);

                    Offers offer = new Offers();
                    offer.setPromotionID(finalObject.getInt("PROMOID"));
                    offer.setPromotionType(finalObject.getInt("PROMOTYPE"));
                    offer.setFromDate(finalObject.getString("BDTAE"));
                    offer.setToDate(finalObject.getString("PEDTAE"));
                    offer.setItemNo(finalObject.getString("ITEMCODE"));
                    offer.setItemQty(finalObject.getDouble("PQTY"));
                    offer.setBonusQty(finalObject.getDouble("BQTY"));
                    offer.setBonusItemNo(finalObject.getString("BITEMCODE"));

                    offersList.add(offer);
                }

                JSONArray parentArraySalesmanStations = parentObject.getJSONArray("SALESMEN_STATIONS");
                salesmanStationsList.clear();
                for (int i = 0; i < parentArraySalesmanStations.length(); i++) {
                    JSONObject finalObject = parentArraySalesmanStations.getJSONObject(i);

                    SalesmanStations station = new SalesmanStations();
                    station.setSalesmanNo(finalObject.getString("SALESMAN_NO"));
                    station.setDate(finalObject.getString("DATE_"));
                    station.setLatitude(finalObject.getString("LATITUDE"));
                    station.setLongitude(finalObject.getString("LONGITUDE"));
                    station.setSerial(finalObject.getInt("SERIAL"));
                    station.setCustNo(finalObject.getString("ACCCODE"));
                    station.setCustName(finalObject.getString("ACCNAME"));

                    salesmanStationsList.add(station);
                }
                Log.e("salesmanStationsList ", "********" + salesmanStationsList.size());

            } catch (MalformedURLException e) {
                Log.e("Customer", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Customer", e.getMessage().toString());
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer", "********ex3  "+e.toString());
                e.printStackTrace();
            } finally {
                Log.e("Customer", "********finally");
                if (connection != null) {
                    Log.e("Customer", "********ex4");
                    // connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return customerList;
        }


        @Override
        protected void onPostExecute(final List<Customer> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null) {
                Log.e("Customerr", "*****************" + customerList.size());
                storeInDatabase();
            } else {
                Toast.makeText(context, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SQLTask extends AsyncTask<String, Integer, String> {
        ProgressBar pb;
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            Window window = dialog.getWindow();
            window.setLayout(700, 350);

            pb = (ProgressBar) dialog.findViewById(R.id.progress);

            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }

            mHandler.deleteAllCustomers();
            mHandler.deleteAllItemUnitDetails();
            mHandler.deleteAllItemsMaster();
            mHandler.deleteAllPriceListD();
            mHandler.deleteAllPriceListM();
            mHandler.deleteAllSalesTeam();
            mHandler.deleteAllSalesmanAndStoreLink();
            mHandler.deleteAllSalesmen();
            mHandler.deleteAllCustomerPrice();
            mHandler.deleteAllOffers();
            mHandler.deleteAllSalesmenStations();

            for (int i = 0; i < customerList.size(); i++) {
                mHandler.addCustomer(customerList.get(i));
            }

            for (int i = 0; i < itemUnitDetailsList.size(); i++) {
                mHandler.addItem_Unit_Details(itemUnitDetailsList.get(i));
            }

            for (int i = 0; i < itemsMasterList.size(); i++) {
                mHandler.addItemsMaster(itemsMasterList.get(i));
            }

            for (int i = 0; i < priceListDpList.size(); i++) {
                mHandler.addPrice_List_D(priceListDpList.get(i));
            }

            for (int i = 0; i < priceListMpList.size(); i++) {
                mHandler.addPrice_List_M(priceListMpList.get(i));
            }
            for (int i = 0; i < salesTeamList.size(); i++) {
                mHandler.addSales_Team(salesTeamList.get(i));
            }

            if (mHandler.getIsPosted(Integer.parseInt(Login.salesMan)) == 1) {
                Log.e("In***" , " in");
                mHandler.deleteAllSalesManItemsBalance();
                for (int i = 0; i < salesManItemsBalanceList.size(); i++) {
                    mHandler.addSalesMan_Items_Balance(salesManItemsBalanceList.get(i));
                }
            }

            for (int i = 0; i < salesManAndStoreLinksList.size(); i++) {
                mHandler.addSalesmanAndStoreLink(salesManAndStoreLinksList.get(i));
            }

            for (int i = 0; i < salesMenList.size(); i++) {
                mHandler.addSalesmen(salesMenList.get(i));
            }

            for (int i = 0; i < customerPricesList.size(); i++) {
                mHandler.addCustomerPrice(customerPricesList.get(i));
            }

            for (int i = 0; i < offersList.size(); i++) {
                mHandler.addOffer(offersList.get(i));
            }

            for (int i = 0; i < salesmanStationsList.size(); i++) {
                mHandler.addSalesmanStation(salesmanStationsList.get(i));
            }

            return "Finish Store";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            pb.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context , s , Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }
}
