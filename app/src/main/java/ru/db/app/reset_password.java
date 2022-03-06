package ru.db.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class reset_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }

    public void back(View view) {
        finish();
    }

    public void send_reset_password(View view) {
        my.mAuth.sendPasswordResetEmail(((EditText)findViewById(R.id.email)).getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    AlertDialog alertDialog = (new AlertDialog.Builder(reset_password.this)).create();
                    alertDialog.setTitle("Инфо");
                    alertDialog.setMessage("Ссылка на восстановление пароля успешно отправлено.");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                    finish();
                                }
                            });
                    alertDialog.show();

                }
                else{
                    AlertDialog alertDialog = (new AlertDialog.Builder(reset_password.this)).create();
                    alertDialog.setTitle("Ошибка");
                    alertDialog.setMessage("Что то пошло не так.");
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
}