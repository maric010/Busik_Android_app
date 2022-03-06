package ru.db.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class auth_main extends AppCompatActivity {
    SharedPreferences settings;
    Boolean is_carrier=false;
    static auth_main th;
    private static final String PREFS_FILE = "Account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main);

        my.effect(findViewById(R.id.button5));
        my.effect(findViewById(R.id.button2));

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        th=this;
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
    public void auth_onClick(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Авторизация");
        progressDialog.setMessage("Пожалуйста, подождите, пока мы проверяем.");
        progressDialog.show();
        String field = "is_passenger";
        if(is_carrier)
            field="is_carrier";

        CollectionReference docRef = my.db.collection("users");
        String finalField = field;
        my.mAuth.signInWithEmailAndPassword(((EditText)findViewById(R.id.phone)).getText().toString(),((EditText)findViewById(R.id.password)).getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  docRef.whereEqualTo("e-mail", ((EditText)findViewById(R.id.phone)).getText().toString())
                          .whereEqualTo(finalField,true).get()
                          .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                              @Override
                              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                  if(queryDocumentSnapshots.getDocuments().size()>0){
                                      DocumentSnapshot user = queryDocumentSnapshots.getDocuments().get(0);
                                      my.id=user.getId();
                                      my.name=user.get("name").toString();
                                      my.phone=user.get("phone").toString();
                                      my.city=user.get("city").toString();
                                      if(user.get("avatar")!=null)
                                          my.avatar=user.get("avatar").toString();
                                      if(user.get("is_carrier")!=null)
                                      {
                                          if(((Boolean) user.get("is_carrier")))
                                              my.status="Перевозчик";
                                      }
                                      else if(user.get("is_passenger")!=null){
                                          if(((Boolean) user.get("is_passenger")))
                                              my.status="Пасажир";
                                      }
                                      else if(user.get("is_admin")!=null){
                                          if(((Boolean) user.get("is_admin")))
                                              my.status="Администратор";
                                      }
                                      if(my.status.equals("Перевозчик"))
                                          my.get_arxiv_carrier();

                                      progressDialog.cancel();
                                      Intent intent = new Intent(auth_main.this, MainActivity.class);
                                      startActivity(intent);
                                      finish();
                                  }
                                  else{
                                      progressDialog.cancel();
                                      AlertDialog alertDialog = (new AlertDialog.Builder(auth_main.th)).create();
                                      alertDialog.setTitle("Ошибка");
                                      alertDialog.setMessage("Неправильный логин или пароль!");
                                      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                                              new DialogInterface.OnClickListener() {
                                                  public void onClick(DialogInterface dialog, int which) {
                                                      alertDialog.dismiss();
                                                  }
                                              });
                                      alertDialog.show();
                                  }

                              }
                          });
              }
              else {
                  progressDialog.cancel();
                  AlertDialog alertDialog = (new AlertDialog.Builder(auth_main.th)).create();
                  alertDialog.setTitle("Ошибка");
                  alertDialog.setMessage("Неправильный логин или пароль!");
                  alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                          new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                                  alertDialog.dismiss();
                              }
                          });
                  alertDialog.show();
              }
            }
        });

    }

    public void back(View view) {
        finish();
    }

    public void reg_onclick(View view) {
        Intent intent = new Intent(auth_main.this, reg.class);
        startActivity(intent);
    }

    public void reset_password_onClick(View view) {
        Intent intent = new Intent(auth_main.this, reset_password.class);
        startActivity(intent);
    }
}