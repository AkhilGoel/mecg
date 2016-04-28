package com.example.akhil.mecg;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Akhil on 22-03-2016.
 */

public class SignupActivity extends Activity implements OnItemSelectedListener {

    EditText username;
    Button btnSubmit;
    ParseUser user;
    String rolename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);

        user = ParseUser.getCurrentUser();

        String textUser = user.getUsername();
        username = (EditText) findViewById(R.id.editUsername);
        username.setText(textUser);

        Spinner role = (Spinner) findViewById(R.id.spinnerRole);

        role.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();

        categories.add("Doctor");
        categories.add("Medical Centre");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(dataAdapter);

        btnSubmit = (Button) findViewById(R.id.btnSub);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.put("role",rolename);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null ) {
                            Intent intent = new Intent(
                                    SignupActivity.this,
                                    Welcome.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Role set to "+rolename,
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rolename = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
