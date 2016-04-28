package com.example.akhil.mecg;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends Activity {

    // Declare Variable
    Button logout,btn;
    String role;
    TextView txtuser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String struser = currentUser.getUsername();
        role =(String) currentUser.get("role");
        txtuser = (TextView) findViewById(R.id.txtuser);
        txtuser.setText("You are logged in as " + struser + " \n Hello, "+ role);
        btn = (Button) findViewById(R.id.btnOp);
        logout = (Button) findViewById(R.id.logout);
        if(role.equals("Doctor")) {
            btn.setText("Check Patient");
        }
        else{
            btn.setText("Upload File");
        }
        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if(role.equals("Doctor")) {
                    Intent i = new Intent(Welcome.this,DownloadFile.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(Welcome.this,UploadFile.class);
                    startActivity(i);
                }
            }
        });
        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(Welcome.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}