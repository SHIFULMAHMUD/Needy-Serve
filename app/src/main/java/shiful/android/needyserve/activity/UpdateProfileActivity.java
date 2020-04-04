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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class UpdateProfileActivity extends AppCompatActivity {
    String getCell,getName,getDivision,getAddress;
    EditText update_NameEt,update_divisionET,update_AddressEt;
    private ProgressDialog loading;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        getName = getIntent().getExtras().getString("name");
        getDivision = getIntent().getExtras().getString("division");
        getAddress = getIntent().getExtras().getString("address");

        update_NameEt=findViewById(R.id.editTextUpdateName);
        update_divisionET=findViewById(R.id.editTextUpdateDivName);
        update_AddressEt=findViewById(R.id.editTextUpdateAddress);
        updateBtn=findViewById(R.id.update_profile_button);

        update_NameEt.setText(getName);
        update_divisionET.setText(getDivision);
        update_AddressEt.setText(getAddress);
        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //For choosing division and open alert dialog
        update_divisionET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] divisionList = {"Dhaka", "Chittagong","Barisal","Khulna","Mymensingh","Rajshahi","Sylhet","Rangpur"};

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                builder.setTitle("Select Division");
                builder.setCancelable(false);
                builder.setItems(divisionList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                update_divisionET.setText(divisionList[position]);
                                break;

                            case 1:
                                update_divisionET.setText(divisionList[position]);
                                break;
                            case 2:
                                update_divisionET.setText(divisionList[position]);
                                break;

                            case 3:
                                update_divisionET.setText(divisionList[position]);
                                break;
                            case 4:
                                update_divisionET.setText(divisionList[position]);
                                break;

                            case 5:
                                update_divisionET.setText(divisionList[position]);
                                break;
                            case 6:
                                update_divisionET.setText(divisionList[position]);
                                break;

                            case 7:
                                update_divisionET.setText(divisionList[position]);
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });
    }
    public void  UpdateProfile()
    {
        //Getting values from edit texts
        final String name = update_NameEt.getText().toString().trim();
        final String division = update_divisionET.getText().toString().trim();
        final String address = update_AddressEt.getText().toString().trim();


        //Checking  field/validation
        if (name.isEmpty()) {
            update_NameEt.setError("Please enter name !");
            requestFocus(update_NameEt);
        }

        else if (division.isEmpty()) {

            update_divisionET.setError("Please select division !");
            requestFocus(update_divisionET);
            Toasty.error(this, "Please select division !", Toast.LENGTH_SHORT).show();
        }
        else if (address.isEmpty()) {

            update_AddressEt.setError("Please enter full address !");
            requestFocus(update_AddressEt);
        }
        else
        {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Updating Profile");
            loading.setMessage("Please wait....");
            loading.show();


            StringRequest stringRequest=new StringRequest(Request.Method.POST, Constant.UPDATE_PROFILE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //for track response in logcat
                    Log.d("RESPONSE", response);

                    if (response.equals("success")) {
                        loading.dismiss();
                        Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                        Toasty.success(UpdateProfileActivity.this, "Information Updated", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }

                    else if (response.equals("failure")) {

                        Toasty.error(UpdateProfileActivity.this, "Profile update Failed!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toasty.error(UpdateProfileActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(Constant.KEY_UPDATE_NAME, name);
                    params.put(Constant.KEY_UPDATE_CELL, getCell);
                    params.put(Constant.KEY_UPDATE_DIV, division);
                    params.put(Constant.KEY_UPDATE_ADDRESS, address);

                    Log.d("url_info",Constant.UPDATE_PROFILE_URL);

                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
    //for request focus
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
