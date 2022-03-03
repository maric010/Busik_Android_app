package ru.db.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class reg extends AppCompatActivity {
Boolean is_carrier = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        if(!my.name.equals("")){
            ((FrameLayout)findViewById(R.id.edit4)).setVisibility(View.INVISIBLE);
            ((FrameLayout)findViewById(R.id.edit5)).setVisibility(View.INVISIBLE);
            ((EditText)findViewById(R.id.name)).setText(my.name);
        }
    }

    public void back(View view) {
        finish();
    }

    public void check_on_passenger(View view) {
        is_carrier=false;
        TextView passenger_text = findViewById(R.id.passenger_textview);
        passenger_text.setTextColor(Color.BLACK);
        View passenger = findViewById(R.id.passenger_check);
        passenger.setVisibility(View.VISIBLE);
        View carrier = findViewById(R.id.carrier_check);
        carrier.setVisibility(View.GONE);
        TextView carrier_text = findViewById(R.id.carrier_textview);
        carrier_text.setTextColor(Color.GRAY);
    }
    public void check_on_carrier(View view) {
        is_carrier=true;
        TextView passenger_text = findViewById(R.id.passenger_textview);
        passenger_text.setTextColor(Color.GRAY);
        View passenger = findViewById(R.id.passenger_check);
        passenger.setVisibility(View.GONE);
        View carrier = findViewById(R.id.carrier_check);
        carrier.setVisibility(View.VISIBLE);
        TextView carrier_text = findViewById(R.id.carrier_textview);
        carrier_text.setTextColor(Color.BLACK);
    }

    public void reg_onclick(View view) {
        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        String phone = ((EditText)findViewById(R.id.phone)).getText().toString().replace("+","");
        String city = ((EditText)findViewById(R.id.city)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        my.reg(name,password,phone,city,false,is_carrier,!is_carrier);
        my.name=name;
        my.phone=phone;
        my.city=city;
        Intent intent = new Intent(reg.this, MainActivity.class);
        startActivity(intent);
        if(auth.th!=null)
            auth.th.finish();
        finish();
    }
}