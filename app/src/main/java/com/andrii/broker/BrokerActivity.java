package com.andrii.broker;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class BrokerActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView name;
    TextView sharePrice;
    TextView shares;
    TextView sharesPrice;
    TextView capitalAmount;
    Button btnBuy;
    Button btnSell;
    ImageView brokerArrow;
    SeekBar seekBar;
    String nameUser;
    TextView result;

    long durations;
    int DELAY_TIME = 500;
    public static float CAPITAL_DEFAULT = 1000.0f;

    long start;
    long end;
    int animProgress;
    int animSaveProgress;
    long fracAnim;

    boolean animStart;

    float priceTmp;

    Float capital;
    Float priceShares;
    int quantityShares;

    public ValueAnimator anim;
    public Context context;

    public static String KEY_TOTAL = "total";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(MainActivity.namePreference, MODE_PRIVATE);
        nameUser = sharedPreferences.getString("name", null);

        if (nameUser.isEmpty()) {
            Intent i = new Intent(this, MainActivity.class);
            Toast t = Toast.makeText(getApplicationContext(), R.string.error_undefined_user, Toast.LENGTH_SHORT);
            t.show();
            startActivity(i);
        }
        if (sharedPreferences.getBoolean("theme_night", false)) {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }else{
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        }
        setContentView(R.layout.activity_broker);

        name = (TextView) findViewById(R.id.broker_name);
        name.setText(nameUser);
        sharePrice = (TextView) findViewById(R.id.broker_price);
        shares = (TextView) findViewById(R.id.broker_text_shares);
        result = (TextView) findViewById(R.id.broker_text_result);
        sharesPrice = (TextView) findViewById(R.id.broker_text_shares_price);
        capitalAmount = (TextView) findViewById(R.id.broker_text_capital_amount);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnBuy = (Button) findViewById(R.id.broker_btn_buy);
        btnSell = (Button) findViewById(R.id.broker_btn_sell);
        brokerArrow = (ImageView) findViewById(R.id.broker_arrow);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(savedInstanceState != null){
            quantityShares = savedInstanceState.getInt("quantityShares");
            priceShares = savedInstanceState.getFloat("priceShares");
            capital = savedInstanceState.getFloat("capital");
            start = savedInstanceState.getLong("start");
            end = savedInstanceState.getLong("end");
            fracAnim = savedInstanceState.getLong("fracAnim");


            result.setText(String.format("%.2f", capital));
            shares.setText(getString(R.string.broker_text_shares).replace("0", String.valueOf(quantityShares)));
            sharesPrice.setText(String.format("%.2f", priceShares));
            animStart = true;
        }else{
            start = System.currentTimeMillis();
            end = start + getTimeRun(60);
            priceShares = 0.0f;
            quantityShares = 0;
            capital = sharedPreferences.getFloat(KEY_TOTAL, CAPITAL_DEFAULT);
            result.setText(String.valueOf(sharedPreferences.getFloat(KEY_TOTAL, CAPITAL_DEFAULT)));
            capitalAmount.setText(String.valueOf(sharedPreferences.getFloat(KEY_TOTAL, CAPITAL_DEFAULT)));
            priceTmp = 0.0f;
            seekBar.setProgress(0);
            animStart = false;
        }

        seekBar.setMax(Integer.parseInt(String.valueOf(end - start)));

        btnBuy.setOnClickListener(new OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (capital - Float.parseFloat(sharePrice.getText().toString().replace(",", ".")) > 0.0f) {
                    capital -= Float.parseFloat(sharePrice.getText().toString().replace(",", "."));
                    quantityShares++;
                    priceShares += Float.parseFloat(sharePrice.getText().toString().replace(",", "."));
                    shares.setText(getString(R.string.broker_text_shares).replace("0", String.valueOf(quantityShares)));
                    sharesPrice.setText(String.format("%.2f", priceShares));
                    result.setText(String.format("%.2f", capital));
                } else {
                    Toast t = Toast.makeText(BrokerActivity.this, getString(R.string.error_money), Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        btnSell.setOnClickListener(new OnClickListener() {

            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                float price = Float.parseFloat(sharePrice.getText().toString().replace(",", "."));
                if (quantityShares > 0) {
                    capital += price;
                    quantityShares--;
                    shares.setText(getString(R.string.broker_text_shares).replace("0", String.valueOf(quantityShares)));
                    result.setText(String.format("%.2f", capital));
                }
            }
        });

        anim = ValueAnimator.ofInt(0, seekBar.getMax());
        durations = end - start - 800;
        anim.setDuration(durations);
        if(animStart){
            anim.setCurrentPlayTime(fracAnim);
            animStart = false;
        }
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fracAnim = animation.getCurrentPlayTime();
                animProgress = (Integer) animation.getAnimatedValue();

                if (durations - 10000 < animProgress) {
                    seekBar.setBackgroundColor(getResources().getColor(R.color.red));
                }
                seekBar.setProgress(animProgress);
            }
        });
        anim.start();
        r.run();
    }

    Handler handler = new Handler();

    final Runnable r = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            float price = (float) (Math.random() * (999 - 1) + 1);
            if (price > priceTmp) {
                brokerArrow.setImageDrawable(getDrawable(R.drawable.ic_arrow_up));
            } else {
                brokerArrow.setImageDrawable(getDrawable(R.drawable.ic_arrow_down));
            }
            priceTmp = price;
            sharePrice.setText(String.format("%.2f", price));
            if (System.currentTimeMillis() < end) {
                handler.postDelayed(this, DELAY_TIME);
            } else {
                Intent i = new Intent(BrokerActivity.this, ResultActivity.class);
                i.putExtra(KEY_TOTAL, Float.parseFloat(result.getText().toString().replace(",", ".")));
                startActivity(i);
            }
        }
    };

    private int getTimeRun(int time) {
        return time * 1000;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(sharedPreferences.getBoolean("theme_night", false)){
            menu.findItem(R.id.day_night).setIcon(getResources().getDrawable(R.drawable.ic_day));
        }else{
            menu.findItem(R.id.day_night).setIcon(getResources().getDrawable(R.drawable.ic_copa));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editor = sharedPreferences.edit();
        if (item.getItemId() == R.id.day_night) {
            if (sharedPreferences.getBoolean("theme_night", false)) {
                editor.putBoolean("theme_night", false);
            } else {
                editor.putBoolean("theme_night", true);
            }
            editor.apply();
            editor.commit();
            handler.removeCallbacks(r);
            anim.cancel();
            recreate();
            return true;
        }
        return true;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("quantityShares", quantityShares);
        outState.putFloat("priceShares", priceShares);
        outState.putFloat("capital", capital);
        outState.putLong("start", start);
        outState.putLong("end", end);
        outState.putInt("progress", seekBar.getProgress());
        outState.putInt("animProgress", animProgress);
        outState.putLong("fracAnim", fracAnim);
        super.onSaveInstanceState(outState);
    }

}