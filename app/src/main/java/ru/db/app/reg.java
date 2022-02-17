package ru.db.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class reg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
    }

    public void back(View view) {
        finish();
    }

    public void check_on_passenger(View view) {
        View passenger = findViewById(R.id.passenger_check)
    }
}