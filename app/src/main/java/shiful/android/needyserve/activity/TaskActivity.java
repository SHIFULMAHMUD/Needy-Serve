package shiful.android.needyserve.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
import java.util.Map;

import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.Constant;
import shiful.android.needyserve.R;

public class TaskActivity extends AppCompatActivity {
    ListView CustomList;
    private ProgressDialog loading;
    int MAX_SIZE=999;
    String getCell;


    public String taskDetails[]=new String[MAX_SIZE];
    public String taskStatus[]=new String[MAX_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Task");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        CustomList=(ListView)findViewById(R.id.task_list);
        //call function to get data
        getData("");

    }


    private void getData(String text) {

        //for showing progress dialog
        loading = new ProgressDialog(TaskActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.TASK_URL+getCell;

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
                        Toasty.error(TaskActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(TaskActivity.this);
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
                Toasty.info(TaskActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();
            }

            else {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);


                    String task = jo.getString(Constant.KEY_TASK);
                    String status = jo.getString(Constant.KEY_TASK_STATAS);

                    //insert data into array for put extra

                    taskDetails[i] = task;
                    taskStatus[i] = status;

                    //put value into Hashmap
                    HashMap<String, String> task_data = new HashMap<>();
                    task_data.put(Constant.KEY_TASK, task);
                    task_data.put(Constant.KEY_TASK_STATAS, status);
                    list.add(task_data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                TaskActivity.this, list, R.layout.task_list_items,
                new String[]{Constant.KEY_TASK,Constant.KEY_TASK_STATAS}, new int[]{R.id.txt_task_name,R.id.txt_task_status});
        CustomList.setAdapter(adapter);
        CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String[] taskList = {"Completed", "Not Completed"};

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setTitle("Task Complete?");


                builder.setCancelable(false);
                builder.setItems(taskList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                submit();
                                break;

                            case 1:
                                submittwo();
                                break;


                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });


                AlertDialog accountTypeDialog = builder.create();

                accountTypeDialog.show();
            }

        });

    }
    public void  submit()
    {
        //Getting values from edit texts
        final String status = "Completed";

            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Submitting Request");
            loading.setMessage("Please wait....");
            loading.show();


            StringRequest stringRequest=new StringRequest(Request.Method.POST, Constant.TASK_STATUS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //for track response in logcat
                    Log.d("RESPONSE", response);

                    if (response.equals("success")) {
                        loading.dismiss();

                        Toasty.success(TaskActivity.this, "Request Submitted", Toast.LENGTH_SHORT).show();

                    }

                    else if (response.equals("failure")) {

                        Toasty.error(TaskActivity.this, "Submission Failed!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toasty.error(TaskActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request


                    params.put(Constant.KEY_TASK_CELL, getCell);
                    params.put(Constant.KEY_TASK_STATUS, status);

                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    public void  submittwo()
    {
        //Getting values from edit texts
        final String status = "Not Completed";

        loading = new ProgressDialog(this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Submitting Request");
        loading.setMessage("Please wait....");
        loading.show();


        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constant.TASK_STATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //for track response in logcat
                Log.d("RESPONSE", response);

                if (response.equals("success")) {
                    loading.dismiss();

                    Toasty.success(TaskActivity.this, "Request Submitted", Toast.LENGTH_SHORT).show();

                }

                else if (response.equals("failure")) {

                    Toasty.error(TaskActivity.this, "Submission Failed!", Toast.LENGTH_SHORT).show();
                    loading.dismiss();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toasty.error(TaskActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request


                params.put(Constant.KEY_TASK_CELL, getCell);
                params.put(Constant.KEY_TASK_STATUS, status);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

