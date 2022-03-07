package ru.db.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class country_city extends AppCompatActivity {
    country_edit textView,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_city);
        if(my.is_otkuda){
            ((TextView)findViewById(R.id.title)).setText("Откуда");
        }
        else
            ((TextView)findViewById(R.id.title)).setText("Куда");

        List<String> countries = new ArrayList<>();
        for(String s : my.countries.keySet()){
            countries.add(s);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, countries);

        textView2 = findViewById(R.id.acType2);
        textView = findViewById(R.id.acType);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                on();
            }
        });
    }

    public void back(View view) {
        finish();
    }
    public void otkuda_text(View view) {
        if(my.is_otkuda){
            my.otkuda= textView.getText().toString()+", "+textView2.getText();
            if(add_order.otkuda!=null)
                add_order.otkuda.setText(my.otkuda);
            if(edit_order.otkuda!=null)
                edit_order.otkuda.setText(my.otkuda);
            if(search.start_country_city!=null)
                search.start_country_city.setText(my.otkuda);
        }
        else{
            my.kuda= textView.getText().toString()+", "+textView2.getText();
            if(add_order.kuda!=null)
                add_order.kuda.setText(my.kuda);
            if(edit_order.kuda!=null)
                edit_order.kuda.setText(my.kuda);
            if(search.stop_country_city!=null)
                search.stop_country_city.setText(my.kuda);
        }
        finish();

    }
void on(){
    ArrayList<String> cs = my.countries.get(textView.getText().toString());
    if(cs!=null){
        List<String> cities = new ArrayList<>();
        for(String s : cs){
            cities.add(s);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, cities);

        textView2.setAdapter(adapter);
    }
}
    public void city(View view) {


    }
}