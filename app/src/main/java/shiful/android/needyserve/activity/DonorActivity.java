package shiful.android.needyserve.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;
import es.dmoral.toasty.Toasty;
import shiful.android.needyserve.R;

public class DonorActivity extends AppCompatActivity {
    CardView profilecv,fooddonatecv,donate_moneycv,reviewcv,logoutcv,eventscv,fooddonatehistorycv,moneydonatehistorycv;
    private static long back_pressed;
    private static final int TIME_DELAY = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        profilecv=findViewById(R.id.profile_cv);
        fooddonatecv=findViewById(R.id.food_donate_cv);
        donate_moneycv=findViewById(R.id.donate_money_cv);
        reviewcv=findViewById(R.id.review_cv);
        logoutcv=findViewById(R.id.logout_cv);
        eventscv=findViewById(R.id.events_cv);
        fooddonatehistorycv=findViewById(R.id.food_donate_history_cv);
        moneydonatehistorycv=findViewById(R.id.Money_donate_history_cv);
        fooddonatehistorycv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,FoodDonateHistoryActivity.class);
                startActivity(intent);
            }
        });
        moneydonatehistorycv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,MoneyDonateHistoryActivity.class);
                startActivity(intent);
            }
        });
        eventscv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,EventsActivity.class);
                startActivity(intent);
            }
        });
        logoutcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        reviewcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,ReviewActivity.class);
                startActivity(intent);
            }
        });
        donate_moneycv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,DonateMoneyActivity.class);
                startActivity(intent);
            }
        });
        profilecv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        fooddonatecv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity.this,DonateFoodActivity.class);
                startActivity(intent);
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
