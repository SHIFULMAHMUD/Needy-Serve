package shiful.android.needyserve.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.Constant;
import shiful.android.needyserve.R;

public class DonateFoodActivity extends AppCompatActivity {
    EditText quantityEt,nameEt,phoneEt,addressEt, txtDate, txtTime;
    RadioGroup radioGroup;
    RadioButton radioButtonOne;
    ProgressDialog loading;
    Button submitbtn;
    String string_rb, getCell, time;
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_food);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Needy Serve");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        quantityEt=findViewById(R.id.quantityEdittext);
        nameEt=findViewById(R.id.nameEdittext);
        phoneEt=findViewById(R.id.mobileEdittext);
        addressEt=findViewById(R.id.addressEdittext);
        radioButtonOne=findViewById(R.id.rb1);
        radioButtonOne.setChecked(true);
        string_rb = radioButtonOne.getText().toString().trim();
        radioGroup=findViewById(R.id.radiogroup_food);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                string_rb = rb.getText().toString().trim();
            }
        });

        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog datePickerDialog = new DatePickerDialog(DonateFoodActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(DonateFoodActivity.this,
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


        phoneEt.setText(getCell);
        submitbtn=findViewById(R.id.cirSubmitButton);
        quantityEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] peopleList = {"1 Person", "2 People", "3 People", "5 People", "10 People", "20 People", "50 People", "100+ People"};

                AlertDialog.Builder builder = new AlertDialog.Builder(DonateFoodActivity.this);
                builder.setTitle("Choose Account Type");
                builder.setCancelable(false);
                builder.setItems(peopleList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                quantityEt.setText(peopleList[position]);
                                break;

                            case 1:
                                quantityEt.setText(peopleList[position]);
                                break;
                            case 2:
                                quantityEt.setText(peopleList[position]);
                                break;

                            case 3:
                                quantityEt.setText(peopleList[position]);
                                break;
                            case 4:
                                quantityEt.setText(peopleList[position]);
                                break;

                            case 5:
                                quantityEt.setText(peopleList[position]);
                                break;
                            case 6:
                                quantityEt.setText(peopleList[position]);
                                break;

                            case 7:
                                quantityEt.setText(peopleList[position]);
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

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }
    private void submit() {

        //Getting values from edit texts
        final String name = nameEt.getText().toString().trim();
        final String cell = getCell;
        final String quantity = quantityEt.getText().toString().trim();
        final String address = addressEt.getText().toString().trim();
        final String date = txtDate.getText().toString().trim();
        final String time = txtTime.getText().toString().trim();
        final String delivery = string_rb;
        final String status="Pending";


        //Checking  field/validation
        if (name.isEmpty()) {
            nameEt.setError("Please enter donor name !");
            requestFocus(nameEt);
        }
        else if (cell.length()!=11) {

            phoneEt.setError("Please enter valid phone number !");
            requestFocus(phoneEt);

        }

        else if (quantity.isEmpty()) {

            quantityEt.setError("Please choose quantity !");
            requestFocus(quantityEt);
            Toasty.error(this, "Please choose quantity !", Toast.LENGTH_SHORT).show();
        }
        else if (address.isEmpty()) {

            addressEt.setError("Please enter full address !");
            requestFocus(addressEt);
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


            String URL = Constant.FOOD_DONATION_URL;


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

                                Toasty.success(DonateFoodActivity.this, " Successfully Submitted!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DonateFoodActivity.this,FoodDonateHistoryActivity.class);
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                //Intent intent = new Intent(AddContactsActivity.this, HomeActivity.class);
                                Toasty.error(DonateFoodActivity.this, " Submission Failed!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toasty.error(DonateFoodActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toasty.error(DonateFoodActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(Constant.KEY_DONOR_NAME, name);
                    params.put(Constant.KEY_DONOR_MOBILE, cell);
                    params.put(Constant.KEY_QUANTITY, quantity);
                    params.put(Constant.KEY_DONOR_ADDRESS, address);
                    params.put(Constant.KEY_DATE, date);
                    params.put(Constant.KEY_TIME, time);
                    params.put(Constant.KEY_DELIVERY, delivery);
                    params.put(Constant.KEY_STATUS_FOOD_DONATE, status);

                    Log.d("url_info",Constant.FOOD_DONATION_URL);

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


