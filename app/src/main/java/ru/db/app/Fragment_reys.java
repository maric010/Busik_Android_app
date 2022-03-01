package ru.db.app;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        return root;
    }
}