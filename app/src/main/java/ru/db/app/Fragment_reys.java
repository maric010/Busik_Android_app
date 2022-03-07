package ru.db.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_reys extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reys, container, false);
        CircleImageView circleImageView = root.findViewById(R.id.avatar);
        if(my.current_order.getValue().get("owner_avatar")!=null){
            Glide.with(root.getContext())
                    .load(my.gen_avatar(my.current_order.getValue().get("owner_avatar").toString()))
                    .placeholder(R.drawable.ellipse_1)
                    .into(circleImageView);
        }
        TextView owner_name = root.findViewById(R.id.owner_name);
        owner_name.setText(my.current_order.getValue().get("owner_name").toString());
        my.fill_fragment(root);
        if(my.current_order.getValue().get("passengers_accepted")!=null){
            HashMap<String, Object> v = (HashMap<String, Object>) (my.current_order.getValue().get("passengers_accepted"));
            if(v.containsKey(my.id)){
                Button b = root.findViewById(R.id.editText2);
                b.setEnabled(false);
                b.setBackgroundResource(R.drawable.button_disabled);
            }
        }
        if(my.current_order.getValue().get("passengers_request")!=null){
            HashMap<String, Object> v = (HashMap<String, Object>) (my.current_order.getValue().get("passengers_request"));
            if(v.containsKey(my.id)){
                Button b = root.findViewById(R.id.editText2);
                b.setEnabled(false);
                b.setBackgroundResource(R.drawable.button_disabled);
            }
        }
        return root;
    }
}