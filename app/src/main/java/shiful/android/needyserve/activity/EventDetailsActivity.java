package shiful.android.needyserve.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import shiful.android.needyserve.R;

public class EventDetailsActivity extends AppCompatActivity {
    TextView txtName, txtCell, txtDetails,txtPlace, txtStarttime,txtEndtime;
    String getName, getCell, getDetails,getPlace,getStarttime,getEndtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Needy Serve");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        txtName=findViewById(R.id.event_name_tv);
        txtDetails=findViewById(R.id.event_details_tv);
        txtStarttime=findViewById(R.id.start_time_tv);
        txtEndtime=findViewById(R.id.end_time_tv);
        txtPlace=findViewById(R.id.event_location);
        txtCell=findViewById(R.id.event_phone);

        getName = getIntent().getExtras().getString("name");
        getDetails = getIntent().getExtras().getString("details");
        getStarttime = getIntent().getExtras().getString("starttime");
        getEndtime = getIntent().getExtras().getString("endtime");
        getPlace = getIntent().getExtras().getString("place");
        getCell = getIntent().getExtras().getString("phone");

        txtName.setText(getName);
        txtDetails.setText(getDetails);
        txtStarttime.setText(getStarttime);
        txtEndtime.setText(getEndtime);
        txtPlace.setText(getPlace);
        txtCell.setText(getCell);
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