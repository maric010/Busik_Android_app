package ru.db.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Fragment_add_rate extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_rate, container, false);
        TextView title = root.findViewById(R.id.ost_otziv);
        title.setText("Оставить отзыв "+my.current_rate_owner_name);
        TextView date = root.findViewById(R.id.otkuda_kuda_date);
        date.setText("К поездке "+my.current_rate_reys);
        EditText editText = root.findViewById(R.id.new_order_description);
        Button create_button = root.findViewById(R.id.create);

        CheckBox c1 = root.findViewById(R.id.check_1);
        CheckBox c2 = root.findViewById(R.id.check_2);
        CheckBox c3 = root.findViewById(R.id.check_3);
        CheckBox c4 = root.findViewById(R.id.check_4);
        CheckBox c5 = root.findViewById(R.id.check_5);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.setChecked(true);
                c2.setChecked(false);
                c3.setChecked(false);
                c4.setChecked(false);
                c5.setChecked(false);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(false);
                c4.setChecked(false);
                c5.setChecked(false);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(true);
                c4.setChecked(false);
                c5.setChecked(false);
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(true);
                c4.setChecked(true);
                c5.setChecked(false);
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(true);
                c4.setChecked(true);
                c5.setChecked(true);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 5;
                if(c5.isChecked())
                    rate=5;
                else if(c4.isChecked())
                    rate=4;
                else if(c3.isChecked())
                    rate=3;
                else if(c2.isChecked())
                    rate=2;
                else if(c1.isChecked())
                    rate=1;
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                HashMap<String,Object> new_message=new HashMap<>();
                new_message.put("name",my.name);
                new_message.put("avatar",my.avatar);
                new_message.put("text",editText.getText().toString());
                new_message.put("date", df.format(c));
                new_message.put("rate",rate);
                my.db.collection("users").document(my.current_rate_owner).collection("comments").document().set(new_message);
                my.dbmessages.child(my.id).child(my.current_rate_message).removeValue();
                AlertDialog alertDialog = (new AlertDialog.Builder(MainActivity.th)).create();
                alertDialog.setTitle("Инфо");
                alertDialog.setMessage("Отзыв успешно добавлен.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.show();
                if(my.status.equals("Пасажир"))
                    MainActivity.th.switch_fragment(new Fragment_orders());
                else
                    MainActivity.th.switch_fragment(new Fragment_orders_carrier());
            }
        });
        return root;
    }



}