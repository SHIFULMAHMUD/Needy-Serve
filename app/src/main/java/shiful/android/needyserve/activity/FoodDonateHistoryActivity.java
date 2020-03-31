package shiful.android.needyserve.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class FoodDonateHistoryActivity extends AppCompatActivity {
    ListView CustomList;
    private ProgressDialog loading;
    int MAX_SIZE=999;
    String getCell;
    public String donorName[]=new String[MAX_SIZE];
    public String donorMobile[]=new String[MAX_SIZE];
    public String quantityPerson[]=new String[MAX_SIZE];
    public String donorAddress[]=new String[MAX_SIZE];
    public String deliveryDate[]=new String[MAX_SIZE];
    public String deliveryTime[]=new String[MAX_SIZE];
    public String deliveryOption[]=new String[MAX_SIZE];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donate_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Food Donation History");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        CustomList=(ListView)findViewById(R.id.donate_food_history_list);
        //call function to get data
        getData();

    }


    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(FoodDonateHistoryActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.VIEW_FOOD_DONATION_URL+getCell;
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
                        Toasty.error(FoodDonateHistoryActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(FoodDonateHistoryActivity.this);
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
                Toasty.info(FoodDonateHistoryActivity.this, "No Data Available!", Toast.LENGTH_LONG).show();
            }

            else {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String donor_name = jo.getString(Constant.KEY_FOOD_DONOR_NAME);
                    String donor_mobile = jo.getString(Constant.KEY_FOOD_DONOR_MOBILE);
                    String quantity_food = jo.getString(Constant.KEY_QUANTITY_FOOD);
                    String address = jo.getString(Constant.KEY_USER_ADDRESS);
                    String donation_date = jo.getString(Constant.KEY_DONATION_DATE);
                    String donation_time = jo.getString(Constant.KEY_DONATION_TIME);
                    String delivery = jo.getString(Constant.KEY_DELIVERY_OPTION);

                    //insert data into array for put extra
                    donorName[i] = donor_name;
                    donorMobile[i] = donor_mobile;
                    quantityPerson[i] = quantity_food;
                    donorAddress[i] = address;
                    deliveryDate[i] = donation_date;
                    deliveryTime[i] = donation_time;
                    deliveryOption[i] = delivery;

                    //put value into Hashmap
                    HashMap<String, String> food_donation_data = new HashMap<>();
                    food_donation_data.put(Constant.KEY_FOOD_DONOR_NAME, donor_name);
                    food_donation_data.put(Constant.KEY_FOOD_DONOR_MOBILE, donor_mobile);
                    food_donation_data.put(Constant.KEY_QUANTITY_FOOD, quantity_food);
                    food_donation_data.put(Constant.KEY_USER_ADDRESS, address);
                    food_donation_data.put(Constant.KEY_DONATION_DATE, donation_date);
                    food_donation_data.put(Constant.KEY_DONATION_TIME, donation_time);
                    food_donation_data.put(Constant.KEY_DELIVERY_OPTION, delivery);

                    list.add(food_donation_data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                FoodDonateHistoryActivity.this, list, R.layout.foodhistory_items,
                new String[]{Constant.KEY_QUANTITY_FOOD, Constant.KEY_DONATION_DATE, Constant.KEY_DONATION_TIME},
                new int[]{R.id.food_quantity_tv, R.id.delivery_date_tv, R.id.delivery_time_tv});
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
