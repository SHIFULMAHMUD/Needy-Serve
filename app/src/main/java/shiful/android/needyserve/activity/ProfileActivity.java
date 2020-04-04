package shiful.android.needyserve.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.Constant;
import shiful.android.needyserve.R;

public class ProfileActivity extends AppCompatActivity {
    TextView nametv, divisiontv, celltv, addresstv,accounttypetv,heading_name,heading_ac_type,backtv;
    ImageView profileiv;
    private ProgressDialog loading;
    String getCell;
    int MAX_SIZE=999;

    public String userName[]=new String[MAX_SIZE];
    public String userCell[]=new String[MAX_SIZE];
    public String userDivision[]=new String[MAX_SIZE];
    public String userAddress[]=new String[MAX_SIZE];
    public String userAccounttype[]=new String[MAX_SIZE];
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateBtn=findViewById(R.id.update_profile_btn);
        heading_name=findViewById(R.id.headingname);
        heading_ac_type=findViewById(R.id.heading_accounttype);
        nametv=findViewById(R.id.nameTextView);
        divisiontv=findViewById(R.id.divisionTextview);
        celltv=findViewById(R.id.mobileTextview);
        addresstv=findViewById(R.id.addressTextview);
        accounttypetv=findViewById(R.id.accounttypeTextview);
        profileiv=findViewById(R.id.profile_image);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //Log
        Log.d("SP_CELL",getCell);


        //call function to get data
        getData("");
    }


    private void getData(String text) {


        //for showing progress dialog
        loading = new ProgressDialog(ProfileActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.USER_VIEW_URL+getCell;
        Log.d("SP_URL",URL);
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
                        Toasty.error(ProfileActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);
    }



    private void showJSON(String response) {

        //Create json object for receiving json data
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);


            if (result.length()==0)
            {
                Toasty.info(ProfileActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfileActivity.this, DonorActivity.class);

                startActivity(intent);
                //imgNoData.setImageResource(R.drawable.nodata);
            }

            else {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    final String name = jo.getString(Constant.KEY_NAME);
                    final String cell = jo.getString(Constant.KEY_CELL);
                    final String division = jo.getString(Constant.KEY_DIV);
                    final String address = jo.getString(Constant.KEY_ADDRESS);
                    String account_type = jo.getString(Constant.KEY_AC_TYPE);

                    //insert data into array for put extra

                    if (account_type.equals("Donor"))
                    {
                        backtv=findViewById(R.id.backTextview);
                        backtv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ProfileActivity.this, DonorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    if (account_type.equals("Volunteer"))
                    {
                        backtv=findViewById(R.id.backTextview);
                        backtv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ProfileActivity.this, VolunteerActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                    userName[i] = name;
                    userCell[i] = cell;
                    userDivision[i] = division;
                    userAddress[i] = address;
                    userAccounttype[i] = account_type;

                    nametv.setText(name);
                    celltv.setText(cell);
                    divisiontv.setText(division);
                    addresstv.setText(address);
                    accounttypetv.setText(account_type);
                    heading_name.setText(name);
                    heading_ac_type.setText(account_type);

                    Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.male);
                    ImageView imageView = (ImageView) findViewById(R.id.profile_image);
                    imageView.setImageBitmap(getRoundedBitmap(picture));

                    updateBtn=findViewById(R.id.update_profile_btn);
                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(ProfileActivity.this,UpdateProfileActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("division",division);
                            intent.putExtra("address",address);
                            startActivity(intent);
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public Bitmap getRoundedBitmap(Bitmap bitmap){
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        return circleBitmap;
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