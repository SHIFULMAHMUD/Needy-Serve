package shiful.android.needyserve.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.Constant;
import shiful.android.needyserve.R;

public class DonateFoodActivity extends AppCompatActivity {
    EditText quantityEt,nameEt,phoneEt,addressEt;
    RadioGroup radioGroup;
    ProgressDialog loading;
    Button submitbtn;
    String string_rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_food);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Needy Serve");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button


        quantityEt=findViewById(R.id.quantityEdittext);
        nameEt=findViewById(R.id.nameEdittext);
        phoneEt=findViewById(R.id.mobileEdittext);
        addressEt=findViewById(R.id.addressEdittext);
        radioGroup=findViewById(R.id.radiogroup_food);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                string_rb = rb.getText().toString().trim();
            }
        });

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
        final String cell = phoneEt.getText().toString().trim();
        final String quantity = quantityEt.getText().toString().trim();
        final String address = addressEt.getText().toString().trim();
        final String delivery = string_rb;


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
                    params.put(Constant.KEY_DELIVERY, delivery);

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


