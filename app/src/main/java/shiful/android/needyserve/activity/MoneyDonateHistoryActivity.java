package shiful.android.needyserve.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.Constant;
import shiful.android.needyserve.R;

public class MoneyDonateHistoryActivity extends AppCompatActivity {
    ListView CustomList;
    private ProgressDialog loading;
    int MAX_SIZE=999;
    String getCell;
    public String donorName[]=new String[MAX_SIZE];
    public String donorMobile[]=new String[MAX_SIZE];
    public String donorAddress[]=new String[MAX_SIZE];
    public String donationAmount[]=new String[MAX_SIZE];
    public String bkashTrxID[]=new String[MAX_SIZE];
    public String deliveryDate[]=new String[MAX_SIZE];
    public String deliveryTime[]=new String[MAX_SIZE];
    public String donorComment[]=new String[MAX_SIZE];
    public String donationConfirmation[]=new String[MAX_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_donate_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Money Donation History");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        CustomList=(ListView)findViewById(R.id.donate_money_history_list);
        //call function to get data
        getData();

    }


    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(MoneyDonateHistoryActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.VIEW_MONEY_DONATION_URL+getCell;
        Log.d("url_food",URL);

        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toasty.error(MoneyDonateHistoryActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MoneyDonateHistoryActivity.this);
        requestQueue.add(stringRequest);

    }



    private void showJSON(String response) {

        //Create json object for receiving jason data
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);


            if (result.length()==0)
            {
                Toasty.info(MoneyDonateHistoryActivity.this, "No Data Available!", Toast.LENGTH_LONG).show();
            }

            else {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String donor_name = jo.getString(Constant.KEY_VIEW_MONEY_DONOR_NAME);
                    String donor_mobile = jo.getString(Constant.KEY_VIEW_DONOR_CELL);
                    String address = jo.getString(Constant.KEY_VIEW_MONEY_DONOR_ADDRESS);
                    String amount = jo.getString(Constant.KEY_VIEW_AMOUNT);
                    String bkash_trx_id = jo.getString(Constant.KEY_VIEW_TRX_ID);
                    String donation_date = jo.getString(Constant.KEY_VIEW_MD_DATE);
                    String donation_time = jo.getString(Constant.KEY_VIEW_MD_TIME);
                    String comment = jo.getString(Constant.KEY_VIEW_COMMENT);
                    String confirmation = jo.getString(Constant.KEY_MONEY_DONATION_CONFIRMATION);

                    //insert data into array for put extra
                    donorName[i] = donor_name;
                    donorMobile[i] = donor_mobile;
                    donorAddress[i] = address;
                    donationAmount[i] = amount;
                    bkashTrxID[i] = bkash_trx_id;
                    deliveryDate[i] = donation_date;
                    deliveryTime[i] = donation_time;
                    donorComment[i] = comment;
                    donationConfirmation[i] = confirmation;

                    //put value into Hashmap
                    HashMap<String, String> money_donation_data = new HashMap<>();
                    money_donation_data.put(Constant.KEY_VIEW_MONEY_DONOR_NAME, donor_name);
                    money_donation_data.put(Constant.KEY_VIEW_DONOR_CELL, donor_mobile);
                    money_donation_data.put(Constant.KEY_VIEW_MONEY_DONOR_ADDRESS, address);
                    money_donation_data.put(Constant.KEY_VIEW_AMOUNT, amount);
                    money_donation_data.put(Constant.KEY_VIEW_TRX_ID, bkash_trx_id);
                    money_donation_data.put(Constant.KEY_VIEW_MD_DATE, donation_date);
                    money_donation_data.put(Constant.KEY_VIEW_MD_TIME, donation_time);
                    money_donation_data.put(Constant.KEY_VIEW_COMMENT, comment);
                    money_donation_data.put(Constant.KEY_MONEY_DONATION_CONFIRMATION, confirmation);

                    list.add(money_donation_data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                MoneyDonateHistoryActivity.this, list, R.layout.moneydonationhistory_items,
                new String[]{Constant.KEY_VIEW_AMOUNT, Constant.KEY_VIEW_TRX_ID, Constant.KEY_VIEW_MD_DATE,Constant.KEY_VIEW_MD_TIME,Constant.KEY_VIEW_COMMENT,Constant.KEY_MONEY_DONATION_CONFIRMATION},
                new int[]{R.id.donation_amount_tv, R.id.trx_id_tv, R.id.user_delivery_date_tv, R.id.user_delivery_time_tv, R.id.comment_tv,R.id.money_confirmation_tv});
        CustomList.setAdapter(adapter);
    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
