package shiful.android.needyserve.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.Constant;
import shiful.android.needyserve.R;

public class DonateMoneyActivity extends AppCompatActivity {
    EditText donor_name_Et,mobile_Et,address_Et,amount_Et,trx_id_Et,comment_Et,txtDate, txtTime;
    Button submit_button;
    ProgressDialog loading;
    String getCell, time;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_money);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Needy Serve");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        donor_name_Et=findViewById(R.id.donor_nameEdittext);
        mobile_Et=findViewById(R.id.mobile_noEdittext);
        address_Et=findViewById(R.id.full_addressEdittext);
        amount_Et=findViewById(R.id.amountEdittext);
        trx_id_Et=findViewById(R.id.trx_id_Edittext);
        comment_Et=findViewById(R.id.commentEdittext);
        submit_button=findViewById(R.id.cirSubmitBtn);
        mobile_Et.setText(getCell);
        txtDate=(EditText)findViewById(R.id.in_date_et);
        txtTime=(EditText)findViewById(R.id.in_time_et);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog datePickerDialog = new DatePickerDialog(DonateMoneyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(DonateMoneyActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                if(hourOfDay>=0 && hourOfDay<12){
                                    time = hourOfDay + " : " + minute + " AM";
                                } else {
                                    if(hourOfDay == 12){
                                        time = hourOfDay + " : " + minute + " PM";
                                    } else{
                                        hourOfDay = hourOfDay -12;
                                        time = hourOfDay + " : " + minute + " PM";
                                    }
                                }

                                txtTime.setText(time);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }

        });


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }
    private void submit() {

        //Getting values from edit texts
        final String name = donor_name_Et.getText().toString().trim();
        final String cell = getCell;
        final String address = address_Et.getText().toString().trim();
        final String amount = amount_Et.getText().toString().trim();
        final String trx_id = trx_id_Et.getText().toString().trim();
        final String date = txtDate.getText().toString().trim();
        final String time = txtTime.getText().toString().trim();
        final String comment = comment_Et.getText().toString().trim();
        final String status = "Pending";


        //Checking  field/validation
        if (name.isEmpty()) {
            donor_name_Et.setError("Please enter donor name !");
            requestFocus(donor_name_Et);
        }
        else if (cell.length()!=11) {

            mobile_Et.setError("Please enter valid phone number !");
            requestFocus(mobile_Et);

        }

        else if (address.isEmpty()) {

            address_Et.setError("Please enter full address !");
            requestFocus(address_Et);
        }
        else if (amount.isEmpty()) {

            amount_Et.setError("Please enter donation amount !");
            requestFocus(amount_Et);
        }
        else if (trx_id.isEmpty()) {

            trx_id_Et.setError("Please enter bKash transaction id !");
            requestFocus(trx_id_Et);
        }
        else if (date.isEmpty()) {

            txtDate.setError("Please select date !");
            requestFocus(txtDate);
            Toasty.error(this, "Please select date !", Toast.LENGTH_SHORT).show();
        }
        else if (time.isEmpty()) {

            txtTime.setError("Please select time !");
            requestFocus(txtTime);
            Toasty.error(this, "Please select time !", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Submitting Request");
            loading.setMessage("Please wait....");
            loading.show();

            String URL = Constant.MONEY_DONATION_URL;

            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            //for track response in logcat
                            Log.d("RESPONSE", response);
                            // Log.d("RESPONSE", userCell);


                            //If we are getting success from server
                            if (response.equals("success")) {

                                loading.dismiss();
                                //Starting profile activity

                                Toasty.success(DonateMoneyActivity.this, " Successfully Submitted!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DonateMoneyActivity.this,MoneyDonateHistoryActivity.class);
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                //Intent intent = new Intent(AddContactsActivity.this, HomeActivity.class);
                                Toasty.error(DonateMoneyActivity.this, " Submission Failed!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toasty.error(DonateMoneyActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toasty.error(DonateMoneyActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(Constant.KEY_MONEY_DONOR_NAME, name);
                    params.put(Constant.KEY_DONOR_CELL, cell);
                    params.put(Constant.KEY_MONEY_DONOR_ADDRESS, address);
                    params.put(Constant.KEY_AMOUNT, amount);
                    params.put(Constant.KEY_TRX_ID, trx_id);
                    params.put(Constant.KEY_MD_DATE, date);
                    params.put(Constant.KEY_MD_TIME, time);
                    params.put(Constant.KEY_COMMENT, comment);
                    params.put(Constant.KEY_STATUS_MONEY_DONATE, status);

                    Log.d("url_info",Constant.MONEY_DONATION_URL);

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



