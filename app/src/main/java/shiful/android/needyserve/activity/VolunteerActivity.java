package shiful.android.needyserve.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.R;

public class VolunteerActivity extends AppCompatActivity {
    CardView taskcv,profilecv,eventcv,logoutcv;
    private static long back_pressed;
    private static final int TIME_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        taskcv=findViewById(R.id.task_cv);
        profilecv=findViewById(R.id.profile_cv);
        eventcv=findViewById(R.id.event_cv);
        logoutcv=findViewById(R.id.logoutcv);
        taskcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VolunteerActivity.this,TaskActivity.class);
                startActivity(intent);
            }
        });
        profilecv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VolunteerActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        eventcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VolunteerActivity.this,EventsActivity.class);
                startActivity(intent);
            }
        });
        logoutcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VolunteerActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
