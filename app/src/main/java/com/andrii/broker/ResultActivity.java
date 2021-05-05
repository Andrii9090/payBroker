package com.andrii.broker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    float total;
    float totalOld;
    SharedPreferences sharedPreferences;

    TextView title;
    TextView mainText;
    Button btnPlay;
    Button btnExit;
    ImageView image;

    public static String KEY_SHARED_PREFERENCE_TOTAL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.namePreference, MODE_PRIVATE);
        if (sharedPreferences.getBoolean("theme_night", false)) {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }else{
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        }
        setContentView(R.layout.activity_result);
        title = (TextView)findViewById(R.id.result_title);
        mainText = (TextView)findViewById(R.id.result_main_text);
        btnPlay = (Button)findViewById(R.id.result_btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, BrokerActivity.class);
                startActivity(i);
            }
        });

        btnExit = (Button)findViewById(R.id.result_btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        image = (ImageView)findViewById(R.id.result_image);

        Intent i = getIntent();
        total = i.getFloatExtra(BrokerActivity.KEY_TOTAL, BrokerActivity.CAPITAL_DEFAULT);

        sharedPreferences = getSharedPreferences(MainActivity.namePreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        totalOld = sharedPreferences.getFloat(BrokerActivity.KEY_TOTAL, BrokerActivity.CAPITAL_DEFAULT);

        if(total>totalOld){
            editor.putFloat(BrokerActivity.KEY_TOTAL, total);
            editor.apply();
            mainText.setText(getString(R.string.new_record)+" "+total);
        }else {
            editor.putFloat(BrokerActivity.KEY_TOTAL, BrokerActivity.CAPITAL_DEFAULT);
            editor.apply();
            Log.e("TOTAL", String.valueOf(BrokerActivity.CAPITAL_DEFAULT));
            title.setText(getString(R.string.record_not_broken));
            if(totalOld>1000.0f)
                mainText.setText(getString(R.string.last_record)+" "+totalOld);
            else{
                mainText.setText("");
            }
            image.setImageDrawable(getDrawable(R.drawable.medalla));
        }
    }
}