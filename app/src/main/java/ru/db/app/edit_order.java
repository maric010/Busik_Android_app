package ru.db.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class edit_order extends AppCompatActivity {
    Dialog dialog;
    CalendarView calendarView;
    TimePicker timePicker;
    Boolean start=true;
    String start_date,stop_date;
    static TextView otkuda,kuda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
        my.effect(findViewById(R.id.edit));
        otkuda = findViewById(R.id.new_order_otkuda);
        kuda = findViewById(R.id.new_order_kuda);

        ((TextView)findViewById(R.id.new_order_otkuda)).setText(my.current_order.getValue().get("otkuda").toString());
        ((TextView)findViewById(R.id.new_order_kuda)).setText(my.current_order.getValue().get("kuda").toString());
        ((TextView)findViewById(R.id.new_order_start_date)).setText(my.current_order.getValue().get("start_date").toString());
        ((TextView)findViewById(R.id.new_order_stop_date)).setText(my.current_order.getValue().get("stop_date").toString());
        ((EditText)findViewById(R.id.new_order_description)).setText(my.current_order.getValue().get("description").toString());
        ((CheckBox)findViewById(R.id.new_order_peoples)).setChecked(Boolean.parseBoolean(my.current_order.getValue().get("is_passenger").toString()));
        ((EditText)findViewById(R.id.new_order_people_cost_text)).setText(my.current_order.getValue().get("passenger_cost").toString());
        ((EditText)findViewById(R.id.new_order_gruz_cost)).setText(my.current_order.getValue().get("gruz_cost").toString());
        ((CheckBox)findViewById(R.id.new_order_gruz)).setChecked(Boolean.parseBoolean(my.current_order.getValue().get("is_gruz").toString()));
        ((EditText)findViewById(R.id.peoples_max)).setText(my.current_order.getValue().get("max_peoples").toString());
        ((EditText)findViewById(R.id.gruz_max)).setText(my.current_order.getValue().get("max_gruz").toString());
    }

    public void change_order_onClick(View view) {
        my.change_order(((TextView)findViewById(R.id.new_order_otkuda)).getText().toString(),
                ((TextView)findViewById(R.id.new_order_kuda)).getText().toString(),
                ((TextView)findViewById(R.id.new_order_start_date)).getText().toString(),
                ((TextView)findViewById(R.id.new_order_stop_date)).getText().toString(),
                ((EditText)findViewById(R.id.new_order_description)).getText().toString(),
                ((CheckBox)findViewById(R.id.new_order_peoples)).isChecked()+"",
                ((EditText)findViewById(R.id.new_order_people_cost_text)).getText().toString(),
                ((EditText)findViewById(R.id.new_order_gruz_cost)).getText().toString(),
                ((CheckBox)findViewById(R.id.new_order_gruz)).isChecked()+"",
                ((EditText)findViewById(R.id.peoples_max)).getText().toString(),
                ((EditText)findViewById(R.id.gruz_max)).getText().toString());
        finish();
    }

    public void pick_stop_date(View view) {
        start=false;
        pic();
    }

    public void pick_date(View view) {
        start=true;
        pic();
    }

    void pic(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calendar);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        calendarView = dialog.findViewById(R.id.calendarView);
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

        timePicker = dialog.findViewById(R.id.timepicker);
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

                if(start){
                    start_date=sDate+" "+hour+":"+minute;
                    ((TextView)findViewById(R.id.new_order_start_date)).setText(start_date);
                }
                else{
                    stop_date=sDate+" "+hour+":"+minute;
                    ((TextView)findViewById(R.id.new_order_stop_date)).setText(stop_date);
                }
                dialog.cancel();
            }
        });
    }

    public void back(View view) {
        finish();
    }
    public void otkuda_onClick(View view) {
        my.is_otkuda=true;
        Intent intent = new Intent(edit_order.this, country_city.class);
        startActivity(intent);
    }

    public void kuda_onClick(View view) {
        my.is_otkuda=false;
        Intent intent = new Intent(edit_order.this, country_city.class);
        startActivity(intent);
    }
}