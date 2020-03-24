package shiful.android.needyserve;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {
    EditText accouttypeET, divisionET,nameET,cellET,addressEt,passwordET;
    Button registerBtn;
    ProgressDialog loading;
    private static long back_pressed;
    private static final int TIME_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        accouttypeET =findViewById(R.id.editTextAccountType);
        divisionET =findViewById(R.id.editTextDivName);
        nameET=findViewById(R.id.editTextName);
        cellET=findViewById(R.id.editTextMobile);
        addressEt=findViewById(R.id.editTextAddress);
        passwordET=findViewById(R.id.editTextPassword);
        registerBtn=findViewById(R.id.cirRegisterButton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sign_up();

            }
        });
        //For choosing account type and open alert dialog
        accouttypeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] accountList = {"Donor", "Volunteer"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Choose Account Type");
                builder.setCancelable(false);
                builder.setItems(accountList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                accouttypeET.setText(accountList[position]);
                                break;

                            case 1:
                                accouttypeET.setText(accountList[position]);
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

        //For choosing division and open alert dialog
        divisionET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] divisionList = {"Dhaka", "Chittagong","Barisal","Khulna","Mymensingh","Rajshahi","Sylhet","Rangpur"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Select Division");
                builder.setCancelable(false);
                builder.setItems(divisionList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                divisionET.setText(divisionList[position]);
                                break;

                            case 1:
                                divisionET.setText(divisionList[position]);
                                break;
                            case 2:
                                divisionET.setText(divisionList[position]);
                                break;

                            case 3:
                                divisionET.setText(divisionList[position]);
                                break;
                            case 4:
                                divisionET.setText(divisionList[position]);
                                break;

                            case 5:
                                divisionET.setText(divisionList[position]);
                                break;
                            case 6:
                                divisionET.setText(divisionList[position]);
                                break;

                            case 7:
                                divisionET.setText(divisionList[position]);
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
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }
    private void sign_up() {

        //Getting values from edit texts
        final String name = nameET.getText().toString().trim();
        final String cell = cellET.getText().toString().trim();
        final String division = divisionET.getText().toString().trim();
        final String address = addressEt.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        final String account_type = accouttypeET.getText().toString().trim();


        //Checking  field/validation
        if (name.isEmpty()) {
            nameET.setError("Please enter name !");
            requestFocus(nameET);
        }
        else if (cell.length()!=11) {

            cellET.setError("Please enter valid phone number !");
            requestFocus(cellET);

        }

        else if (division.isEmpty()) {

            divisionET.setError("Please select division !");
            requestFocus(divisionET);
            Toasty.error(this, "Please select division !", Toast.LENGTH_SHORT).show();
        }
        else if (address.isEmpty()) {

            addressEt.setError("Please enter full address !");
            requestFocus(addressEt);
        }
        else if (account_type.isEmpty()) {

            accouttypeET.setError("Please select account type !");
            requestFocus(accouttypeET);
            Toasty.error(this, "Please select account type !", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty()) {

            passwordET.setError("Please enter password !");
            requestFocus(passwordET);
        }
        else if (password.length()!=5) {

            passwordET.setError("Password should contain minimum 5 characters!");
            requestFocus(passwordET);
        }

        else
        {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Sign up");
            loading.setMessage("Please wait....");
            loading.show();


            StringRequest stringRequest=new StringRequest(Request.Method.POST, Constant.SIGNUP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //for track response in logcat
                    Log.d("RESPONSE", response);

                    if (response.equals("success")) {
                        loading.dismiss();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        Toasty.success(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    } else if (response.equals("exists")) {

                        Toasty.warning(RegisterActivity.this, "User already exists!", Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }

                    else if (response.equals("failure")) {

                        Toasty.error(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toasty.error(RegisterActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(Constant.KEY_NAME, name);
                    params.put(Constant.KEY_CELL, cell);
                    params.put(Constant.KEY_DIV, division);
                    params.put(Constant.KEY_ADDRESS, address);
                    params.put(Constant.KEY_AC_TYPE, account_type);
                    params.put(Constant.KEY_PASSWORD, password);

                    Log.d("url_info",Constant.SIGNUP_URL);

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
}
