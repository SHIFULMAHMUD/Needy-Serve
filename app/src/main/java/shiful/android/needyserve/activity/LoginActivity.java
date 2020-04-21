package shiful.android.needyserve.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.Constant;
import shiful.android.needyserve.R;


public class LoginActivity extends AppCompatActivity implements TextWatcher,
        CompoundButton.OnCheckedChangeListener{
    CircularProgressButton loginBtn;
    //EditText object declaration
    EditText etxtCell,etxtPassword,accouttypeEdt;
    TextView hidetv;
    String text,checkusertext;
    String getCell;
    //ProgressDialog object declaration
    private ProgressDialog loading;
    private static long back_pressed;
    private static final int TIME_DELAY = 2000;
    int MAX_SIZE=999;
    public String userAccountStatus[]=new String[MAX_SIZE];
    private CheckBox rem_userpass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERCELL = "usercell";
    private static final String KEY_PASS = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        etxtCell=findViewById(R.id.editTextMobile);
        etxtPassword=findViewById(R.id.editTextPassword);
        rem_userpass = findViewById(R.id.ch_rememberme);
        loginBtn=findViewById(R.id.cirLoginButton);

        accouttypeEdt=findViewById(R.id.editTextAccountName);
//For choosing account type and open alert dialog
        accouttypeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] accountList = {"Donor", "Volunteer"};

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Choose Account Type");
                builder.setCancelable(false);
                builder.setItems(accountList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                accouttypeEdt.setText(accountList[position]);
                                text=accountList[position];
                                break;

                            case 1:
                                accouttypeEdt.setText(accountList[position]);
                                text=accountList[position];
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

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false))
            rem_userpass.setChecked(true);
        else
            rem_userpass.setChecked(false);

        etxtCell.setText(sharedPreferences.getString(KEY_USERCELL, ""));
        etxtPassword.setText(sharedPreferences.getString(KEY_PASS, ""));

        etxtCell.addTextChangedListener(this);
        etxtPassword.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);


        //Click listener in LoginActivity Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Call AppSingleton function
                AppSingleton();

            }
        });

    }


    //LoginActivity function
    private void AppSingleton() {
        //Getting values from edit texts
        final String cell = etxtCell.getText().toString().trim();
        final String password = etxtPassword.getText().toString().trim();
        final String ac_type = accouttypeEdt.getText().toString().trim();


        //Checking usercell field/validation
        if (cell.isEmpty()) {
            etxtCell.setError("Please enter cell !");
            requestFocus(etxtCell);
        }

        //Checking password field/validation
        else if (password.isEmpty()) {
            etxtPassword.setError("Password can't be empty!");
            requestFocus(etxtPassword);
        }
        else if (ac_type.isEmpty()) {
            accouttypeEdt.setError("Account type can't be empty!");
            requestFocus(accouttypeEdt);
            Toasty.error(this, "Please select account type !", Toast.LENGTH_SHORT).show();
        }
        //showing progress dialog

        else {

            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Logging in");
            loading.setMessage("Please wait....");
            loading.show();

            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("response",""+response);
                            //If we are getting success from server
                            if (response.equals("accept")) {
                                //Creating a shared preference

                                SharedPreferences sp = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sp.edit();
                                //Adding values to editor
                                editor.putString(Constant.CELL_SHARED_PREF, cell);

                                //Saving values to editor
                                editor.commit();
                                //Starting Home activity

                                if (text.equals("Donor"))
                                {
                                        Intent intent = new Intent(LoginActivity.this, DonorActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toasty.success(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                }
                                if (text.equals("Volunteer"))
                                {
                                        Intent intent = new Intent(LoginActivity.this, VolunteerActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toasty.success(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                }

                                loading.dismiss();
                            }
                            else if (response.equals("reject")) {
                                //Creating a shared preference

                                SharedPreferences sp = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sp.edit();
                                //Adding values to editor
                                editor.putString(Constant.CELL_SHARED_PREF, cell);

                                //Saving values to editor
                                editor.commit();
                                //Starting Home activity

                                if (text.equals("Donor"))
                                {
                                    Toasty.error(LoginActivity.this, "User Blocked!\nPlease contact with Service Provider", Toast.LENGTH_LONG).show();


                                }
                                if (text.equals("Volunteer"))
                                {
                                    Toasty.error(LoginActivity.this, "User Blocked!\nPlease contact with Service Provider", Toast.LENGTH_LONG).show();

                                }

                                loading.dismiss();
                            }
                            else if (response.equals("pending")) {
                                //Creating a shared preference

                                SharedPreferences sp = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sp.edit();
                                //Adding values to editor
                                editor.putString(Constant.CELL_SHARED_PREF, cell);

                                //Saving values to editor
                                editor.commit();
                                //Starting Home activity

                                if (text.equals("Donor"))
                                {
                                    Toasty.warning(LoginActivity.this, "User approval still pending!\nPlease contact with Service Provider", Toast.LENGTH_LONG).show();


                                }
                                if (text.equals("Volunteer"))
                                {
                                    Toasty.warning(LoginActivity.this, "User approval still pending!\nPlease contact with Service Provider", Toast.LENGTH_LONG).show();

                                }

                                loading.dismiss();
                            }
                            else if(response.equals("failure")) {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toasty.error(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }

                            else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toasty.error(LoginActivity.this, "Invalid user cell or password", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toasty.error(LoginActivity.this, "There is an error !!!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(Constant.KEY_CELL, cell);
                    params.put(Constant.KEY_PASSWORD, password);
                    params.put(Constant.KEY_AC_TYPE,ac_type);

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


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs(){
        if(rem_userpass.isChecked()){
            editor.putString(KEY_USERCELL, etxtCell.getText().toString().trim());
            editor.putString(KEY_PASS, etxtPassword.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERCELL);//editor.putString(KEY_USERCELL, "");
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toasty.info(getBaseContext(), R.string.press_once_again_to_exit,
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }


    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }
}
