package ru.db.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class country_city extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_city);
        List<String> countries = new ArrayList<>();
        for(String s : my.countries.keySet()){
            countries.add(s);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, countries);



        country_edit textView = findViewById(R.id.acType);
        textView.setAdapter(adapter);
    }

    public void back(View view) {
        finish();
    }
    void test(){

    }
}