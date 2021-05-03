package com.andrii.broker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private Button btnCreateUser;
    public static String namePreference = "broker";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(namePreference,MODE_PRIVATE);
        String nameUser = sharedPreferences.getString("name", null);
        if(!nameUser.isEmpty()){
            Intent i = new Intent(this, BrokerActivity.class);
            startActivity(i);
        }
        setContentView(R.layout.activity_main);

        name =(EditText)findViewById(R.id.main_name);
        btnCreateUser = (Button)findViewById(R.id.main_btn_enter);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()){
                    Toast t = Toast.makeText(getApplicationContext(), R.string.error_name, Toast.LENGTH_SHORT);
                    t.show();
                }else {
                    createUser();
                }
            }
        });
    }

    private void createUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(namePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name.getText().toString());
        editor.apply();
        Intent i = new Intent(this, BrokerActivity.class);
        startActivity(i);
    }
}