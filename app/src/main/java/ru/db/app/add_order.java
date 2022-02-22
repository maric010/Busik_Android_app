package ru.db.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class add_order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
    }

    public void back(View view) {
        finish();
    }

    public void create_order(View view) {
        my.reg_order(((EditText)findViewById(R.id.new_order_otkuda)).getText().toString(),
                ((EditText)findViewById(R.id.new_order_kuda)).getText().toString(),
                ((EditText)findViewById(R.id.new_order_start_date)).getText().toString(),
                ((EditText)findViewById(R.id.new_order_stop_date)).getText().toString(),
                ((EditText)findViewById(R.id.new_order_description)).getText().toString(),
                ((CheckBox)findViewById(R.id.new_order_peoples)).isChecked()+"",
                ((EditText)findViewById(R.id.new_order_people_cost_text)).getText().toString(),
                ((EditText)findViewById(R.id.new_order_gruz_cost)).getText().toString(),
                ((CheckBox)findViewById(R.id.new_order_gruz)).isChecked()+"");
        finish();
    }
}