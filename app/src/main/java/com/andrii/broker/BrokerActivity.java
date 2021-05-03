package com.andrii.broker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class BrokerActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView name;
    TextView sharePrice;
    TextView shares;
    TextView sharesPrice;
    TextView capitalAmount;
    Button btnBuy;
    Button btnSell;
    SeekBar seekBar;
    String nameUser;
    TextView result;

    int DELAY_TIME = 450;
    public static float CAPITAL_DEFAULT = 1000.0f;

    long start;
    long end;

    Float capital;
    Float priceShares;
    int quantityShares;

    public static String KEY_TOTAL = "total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(MainActivity.namePreference, MODE_PRIVATE);

        nameUser = sharedPreferences.getString("name", null);
        if(nameUser.isEmpty()){
            Intent i = new Intent(this, MainActivity.class);
            Toast t = Toast.makeText(getApplicationContext(), R.string.error_undefined_user, Toast.LENGTH_SHORT);
            t.show();
            startActivity(i);
        }
        setContentView(R.layout.activity_broker);
        name = (TextView)findViewById(R.id.broker_name);
        name.setText(nameUser);
        sharePrice = (TextView)findViewById(R.id.broker_price);
        shares = (TextView)findViewById(R.id.broker_text_shares);
        result = (TextView)findViewById(R.id.broker_text_result);
        sharesPrice = (TextView)findViewById(R.id.broker_text_shares_price);
        capitalAmount = (TextView)findViewById(R.id.broker_text_capital_amount);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnBuy = (Button) findViewById(R.id.broker_btn_buy);
        btnSell = (Button)findViewById(R.id.broker_btn_sell);

        capital = sharedPreferences.getFloat(KEY_TOTAL, CAPITAL_DEFAULT);
        Log.e("TOTAL", String.valueOf(sharedPreferences.getFloat(KEY_TOTAL, CAPITAL_DEFAULT)));
        result.setText(String.valueOf(sharedPreferences.getFloat(KEY_TOTAL, CAPITAL_DEFAULT)));
        capitalAmount.setText(String.valueOf(sharedPreferences.getFloat(KEY_TOTAL, CAPITAL_DEFAULT)));

        priceShares = 0.0f;
        quantityShares = 0;

        start = System.currentTimeMillis();
        end = start + getTimeRun(40);
        seekBar.setProgress(0);
        seekBar.setMax(Integer.parseInt(String.valueOf(end-start)));
        btnBuy.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (capital - Float.parseFloat(sharePrice.getText().toString()) > 0.0f) {
                    capital -= Float.parseFloat(sharePrice.getText().toString());
                    quantityShares++;
                    priceShares += Float.parseFloat(sharePrice.getText().toString());
                    shares.setText(getString(R.string.broker_text_shares).replace("0", String.valueOf(quantityShares)));
                    sharesPrice.setText(String.format("%.2f",priceShares));
                    result.setText(String.format("%.2f",capital));
                }else {
                    Toast t = Toast.makeText(BrokerActivity.this,getString(R.string.error_money), Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        btnSell.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                float price = Float.parseFloat(sharePrice.getText().toString());
                if(quantityShares>0) {
                    capital += price;
                    quantityShares--;
                    shares.setText(getString(R.string.broker_text_shares).replace("0", String.valueOf(quantityShares)));
                    result.setText(String.format("%.2f",capital));
                }
            }
        });

        r.run();
    }

    Handler handler = new Handler();

    final Runnable r = new Runnable() {
        public void run() {
            Float price = (float) (Math.random()*(999-1)+1);
            sharePrice.setText(String.format("%.2f", price));
            if(System.currentTimeMillis()<end){
                handler.postDelayed(this, DELAY_TIME);
                setProgress();
            }else {
                Intent i = new Intent(BrokerActivity.this, ResultActivity.class);
                i.putExtra(KEY_TOTAL, Float.parseFloat(result.getText().toString()));
                startActivity(i);
            }
        }
    };

    private void setProgress() {
        long progress = seekBar.getProgress()+DELAY_TIME;
        seekBar.setProgress((int)progress);
    }

    private int getTimeRun(int time){
        return time * 1000;
    }

}