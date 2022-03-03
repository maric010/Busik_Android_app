package ru.db.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class search extends AppCompatActivity {
EditText start_country_city,stop_country_city,total_count_1,total_count_2,total_cargo_1,total_cargo_2;
TextView date_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        start_country_city = findViewById(R.id.start_country_city);
        stop_country_city = findViewById(R.id.stop_country_city);
        date_search = findViewById(R.id.date_search);
        total_count_1 = findViewById(R.id.total_count_1);
        total_count_2 = findViewById(R.id.total_count_2);
        total_cargo_1 = findViewById(R.id.total_cargo_1);
        total_cargo_2 = findViewById(R.id.total_cargo_2);


    }


    public void back(View view) {
        finish();
    }

    public void search_onClick(View view) {

    }
}