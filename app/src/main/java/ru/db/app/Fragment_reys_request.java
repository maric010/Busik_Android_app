package ru.db.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class Fragment_reys_request extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reys_apply, container, false);


        Button request = root.findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> new_message=new HashMap<>();
                new_message.put("title","Новый пасажир");
                new_message.put("text","На рейсе "+my.current_order.getValue().get("otkuda")+" -> "
                        +my.current_order.getValue().get("kuda")+" "+my.current_order.getValue().get("start_date").toString().split(" ")[0]
                        +" (Пн)");
                new_message.put("order",my.current_order.getKey());

                my.dbmessages.child(my.current_order.getValue().get("owner").toString()).push().setValue(new_message);



                HashMap<String,Object> user=new HashMap<>();

                user.put("name",my.name);
                user.put("avatar",my.avatar);
                user.put("phone",my.phone);
                user.put("peoples","1");
                user.put("gruz","1");
                my.dborders.child(my.current_order.getKey()).child("passengers_request").child(my.id).setValue(user);
            }
        });


        return root;
    }
}