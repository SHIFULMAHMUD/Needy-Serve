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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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

public class UpdateReviewActivity extends AppCompatActivity {
    RatingBar ratingbar;
    Button reviewBtn;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    ProgressDialog loading;
    EditText name_Et,review_Et;
    String string_rb,string_rating,getCell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Needy Serve");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        name_Et=findViewById(R.id.update_username_Edittext);
        review_Et=findViewById(R.id.update_review_Edittext);
        //binding MainActivity.java with activity_main.xml file
        ratingbar = findViewById(R.id.update_ratingBar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
                string_rating = String.valueOf(rateValue);
            }
        });


        reviewBtn=findViewById(R.id.cirUpdate_Review_Button);
        radioButton1=findViewById(R.id.rb_update);
        radioButton1.setChecked(true);
        string_rb = radioButton1.getText().toString().trim();

        radioGroup=findViewById(R.id.update_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                string_rb = rb.getText().toString().trim();
            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }
    private void submit() {

        //Getting values from edit texts
        final String name = name_Et.getText().toString().trim();
        final String cell = getCell;
        final String rating = string_rating;
        final String recommend = string_rb;
        final String review = review_Et.getText().toString().trim();



        //Checking  field/validation
        if (name.isEmpty()) {
            name_Et.setError("Please give your name !");
            requestFocus(name_Et);
        }
        else if (review.isEmpty()) {
            review_Et.setError("Please give your review !");
            requestFocus(review_Et);
        }
        else
        {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Submitting Review");
            loading.setMessage("Please wait....");
            loading.show();


            String URL = Constant.UPDATE_REVIEW_URL;


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

                                Toasty.success(UpdateReviewActivity.this, " Review Updated!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(UpdateReviewActivity.this,ViewAllReviewActivity.class);
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                //Intent intent = new Intent(AddContactsActivity.this, HomeActivity.class);
                                Toasty.error(UpdateReviewActivity.this, " Submission Failed!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toasty.error(UpdateReviewActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toasty.error(UpdateReviewActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(Constant.KEY_UPDATE_USERNAME, name);
                    params.put(Constant.KEY__UPDATE_USERCELL, cell);
                    params.put(Constant.KEY_UPDATE_RATING, rating);
                    params.put(Constant.KEY_UPDATE_RECOMMEND, recommend);
                    params.put(Constant.KEY_UPDATE_REVIEW, review);

                    Log.d("url_info",Constant.UPDATE_REVIEW_URL);

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