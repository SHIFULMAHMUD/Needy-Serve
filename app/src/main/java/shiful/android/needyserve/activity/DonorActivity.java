package shiful.android.needyserve.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import shiful.android.needyserve.R;

public class DonorActivity extends AppCompatActivity {
    CardView profilecv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        profilecv=findViewById(R.id.profile_cv);
        profilecv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
