package ru.db.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.type.DateTime;

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
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTime current_date = DateTime.getDefaultInstance();
                HashMap<String,Object> new_message=new HashMap<>();
                new_message.put("name",my.name);
                new_message.put("date", current_date.getDay()+"."+current_date.getMonth()+"."+current_date.getYear());



                my.db.collection("users").document(my.current_rate_owner).collection("comments").document().setValue(new_comment);
            }
        });
        return root;
    }
}