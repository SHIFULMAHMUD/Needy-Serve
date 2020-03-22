package shiful.android.needyserve;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


public class RegisterActivity extends AppCompatActivity {
    EditText accouttypeEdit,divisionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        accouttypeEdit=findViewById(R.id.editTextAccountType);
        divisionEdit=findViewById(R.id.editTextDivName);
        //For choosing account type and open alert dialog
        accouttypeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] accountList = {"Food Donor", "Volunteer"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Choose Account Type");
                builder.setCancelable(false);
                builder.setItems(accountList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                accouttypeEdit.setText(accountList[position]);
                                break;

                            case 1:
                                accouttypeEdit.setText(accountList[position]);
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
        divisionEdit.setOnClickListener(new View.OnClickListener() {
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
                                divisionEdit.setText(divisionList[position]);
                                break;

                            case 1:
                                divisionEdit.setText(divisionList[position]);
                                break;
                            case 2:
                                divisionEdit.setText(divisionList[position]);
                                break;

                            case 3:
                                divisionEdit.setText(divisionList[position]);
                                break;
                            case 4:
                                divisionEdit.setText(divisionList[position]);
                                break;

                            case 5:
                                divisionEdit.setText(divisionList[position]);
                                break;
                            case 6:
                                divisionEdit.setText(divisionList[position]);
                                break;

                            case 7:
                                divisionEdit.setText(divisionList[position]);
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



}
