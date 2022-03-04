package ru.db.app;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class search extends AppCompatActivity {
EditText start_country_city,stop_country_city,total_count_1,total_count_2,total_cargo_1,total_cargo_2;
TextView date_search;
Dialog dialog;
static String otkuda="",kuda="",kogda="",c1="",c2="",g1="",g2="";

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
        start_country_city.setText(otkuda);
        stop_country_city.setText(kuda);
        date_search.setText(kogda);
        total_count_1.setText(c1);
        total_count_2.setText(c2);
        total_cargo_1.setText(g1);
        total_cargo_2.setText(g2);


    }


    public void back(View view) {
        finish();
    }

    public void search_onClick(View view) {
        otkuda = start_country_city.getText().toString();
        kuda = stop_country_city.getText().toString();
        kogda = date_search.getText().toString();
        c1=total_count_1.getText().toString();
        c2=total_count_2.getText().toString();
        g1=total_cargo_1.getText().toString();
        g2=total_cargo_2.getText().toString();
        my.search();
    }

    public void search_kogda_onClick(View view) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calendar);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        CalendarView calendarView = dialog.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    Date date = sdf.parse(dayOfMonth+"."+(month+1)+"."+year);
                    calendarView.setDate(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        TimePicker timePicker = dialog.findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);
        Button button=dialog.findViewById(R.id.accept);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String sDate = sdf.format(calendarView.getDate());
                String hour = String.valueOf(timePicker.getCurrentHour());
                String minute = String.valueOf(timePicker.getCurrentMinute());
                if(minute.length()==1)
                    minute = "0"+minute;

                kogda=sDate+" "+hour+":"+minute;
                date_search.setText(kogda);

                dialog.cancel();
            }
        });
    }


}