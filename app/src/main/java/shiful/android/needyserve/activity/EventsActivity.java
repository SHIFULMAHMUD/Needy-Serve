package shiful.android.needyserve.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class EventsActivity extends AppCompatActivity {
    ListView CustomList;
    private ProgressDialog loading;
    Button btnSearch;
    EditText txtSearch;
    int MAX_SIZE=999;

    public String eventName[]=new String[MAX_SIZE];
    public String eventDetails[]=new String[MAX_SIZE];
    public String startTime[]=new String[MAX_SIZE];
    public String endTime[]=new String[MAX_SIZE];
    public String eventPlace[]=new String[MAX_SIZE];
    public String eventPhone[]=new String[MAX_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Events");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        btnSearch=findViewById(R.id.button_search);
        txtSearch=findViewById(R.id.text_search);
        CustomList=(ListView)findViewById(R.id.event_list);
        //call function to get data
        getData("");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String search=txtSearch.getText().toString();

                if (search.isEmpty())
                {
                    Toasty.info(EventsActivity.this, "Please enter event name!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getData(search);
                }

            }
        });

    }


    private void getData(String text) {

        //for showing progress dialog
        loading = new ProgressDialog(EventsActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.EVENT_URL+"&text="+text;

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
                        Toasty.error(EventsActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(EventsActivity.this);
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
                Toasty.info(EventsActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();
            }

            else {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);

                    String name = jo.getString(Constant.KEY_EVENT_NAME);
                    String details = jo.getString(Constant.KEY_EVENT_DETAILS);
                    String start_time = jo.getString(Constant.KEY_START_TIME);
                    String end_time = jo.getString(Constant.KEY_END_TIME);
                    String location = jo.getString(Constant.KEY_PLACE);
                    String phone = jo.getString(Constant.KEY_PHONE);

                    //insert data into array for put extra

                    eventName[i] = name;
                    eventDetails[i] = details;
                    startTime[i] = start_time;
                    endTime[i] = end_time;
                    eventPlace[i] = location;
                    eventPhone[i] = phone;

                    //put value into Hashmap
                    HashMap<String, String> event_data = new HashMap<>();
                    event_data.put(Constant.KEY_EVENT_NAME, name);
                    event_data.put(Constant.KEY_EVENT_DETAILS, details);
                    event_data.put(Constant.KEY_START_TIME, start_time);
                    event_data.put(Constant.KEY_END_TIME, end_time);
                    event_data.put(Constant.KEY_PLACE, location);
                    event_data.put(Constant.KEY_PHONE, phone);

                    list.add(event_data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                EventsActivity.this, list, R.layout.event_list_items,
                new String[]{Constant.KEY_EVENT_NAME, Constant.KEY_START_TIME},
                new int[]{R.id.txt_event_name, R.id.txt_start_time});
        CustomList.setAdapter(adapter);

        CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Intent intent=new Intent(EventsActivity.this, EventDetailsActivity.class);
                intent.putExtra("name",eventName[position]);
                intent.putExtra("details",eventDetails[position]);
                intent.putExtra("starttime",startTime[position]);
                intent.putExtra("endtime",endTime[position]);
                intent.putExtra("place",eventPlace[position]);
                intent.putExtra("phone",eventPhone[position]);

                startActivity(intent);

            }
        });

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
