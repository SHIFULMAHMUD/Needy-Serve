package shiful.android.needyserve.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

public class ViewAllReviewActivity extends AppCompatActivity {
    ListView CustomList;
    private ProgressDialog loading;
    int MAX_SIZE=999;


    public String userName[]=new String[MAX_SIZE];
    public String userRating[]=new String[MAX_SIZE];
    public String userRecommend[]=new String[MAX_SIZE];
    public String userReview[]=new String[MAX_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Reviews");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        CustomList=(ListView)findViewById(R.id.reviews_list);
        //call function to get data
        getData();

    }


    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(ViewAllReviewActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Constant.VIEW_ALL_REVIEWS_URL;

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
                        Toasty.error(ViewAllReviewActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ViewAllReviewActivity.this);
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
                Toasty.info(ViewAllReviewActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();
            }

            else {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);


                    String name = jo.getString(Constant.KEY_VIEW_USERNAME);
                    String rating = jo.getString(Constant.KEY_VIEW_RATING);
                    String recommend = jo.getString(Constant.KEY_VIEW_RECOMMEND);
                    String review = jo.getString(Constant.KEY_VIEW_REVIEW);

                    //insert data into array for put extra

                    userName[i] = name;
                    userRating[i] = rating;
                    userRecommend[i] = recommend;
                    userReview[i] = review;

                    //put value into Hashmap
                    HashMap<String, String> reviews_data = new HashMap<>();
                    reviews_data.put(Constant.KEY_VIEW_USERNAME, name);
                    reviews_data.put(Constant.KEY_VIEW_RATING, rating);
                    reviews_data.put(Constant.KEY_VIEW_RECOMMEND, recommend);
                    reviews_data.put(Constant.KEY_VIEW_REVIEW, review);
                    list.add(reviews_data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ViewAllReviewActivity.this, list, R.layout.reviews_list_items,
                new String[]{Constant.KEY_VIEW_USERNAME,Constant.KEY_VIEW_RATING,Constant.KEY_VIEW_REVIEW}, new int[]{R.id.reviewer_name_tv,R.id.rating_tv,R.id.review_tv});
        CustomList.setAdapter(adapter);

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

