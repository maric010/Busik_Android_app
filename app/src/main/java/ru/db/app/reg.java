package ru.db.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class reg extends AppCompatActivity {
Boolean is_carrier = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        Button reg_button = findViewById(R.id.reg_button);
        my.effect(reg_button);
        Button outdown_button = findViewById(R.id.outdown_button);
        my.effect(outdown_button);

        if(!my.name.equals("")){
            ((FrameLayout)findViewById(R.id.edit4)).setVisibility(View.GONE);
            ((FrameLayout)findViewById(R.id.edit)).setVisibility(View.GONE);
            ((FrameLayout)findViewById(R.id.edit5)).setVisibility(View.GONE);
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
        String password_again = ((EditText)findViewById(R.id.password)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();

        if(name.equals("") || password.equals("") || city.equals("") || phone.equals("")){
            alert("Ошибка","Заполните все поля!");
            return;
        }
        if(password.length()<8){
            alert("Ошибка","Пароль должен быть минимум 8 символов");
            return;
        }
        if(!password_again.equals(password)){
            alert("Ошибка","Пароли не совпадают");
            return;
        }
        my.name=name;
        my.phone=phone;
        my.city=city;
        if(my.email.equals("")){
            my.email=email;
            my.mAuth.createUserWithEmailAndPassword(my.email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        my.reg(name,password,phone,city,is_carrier,!is_carrier);
                        Intent intent = new Intent(reg.this, MainActivity.class);
                        startActivity(intent);
                        if(auth.th!=null)
                            auth.th.finish();
                        finish();
                    }
                    else{
                        my.email="";
                        my.name="";
                        my.phone="";
                        my.city="";
                        alert("Ошибка","Пользователь с таким e-mailом уже существует");
                    }

                }
            });
        }
        else{
            my.reg(name,password,phone,city,is_carrier,!is_carrier);
            Intent intent = new Intent(reg.this, MainActivity.class);
            startActivity(intent);
            if(auth.th!=null)
                auth.th.finish();
            finish();
        }



    }

    void alert(String title,String text){
        AlertDialog alertDialog = (new AlertDialog.Builder(reg.this)).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}