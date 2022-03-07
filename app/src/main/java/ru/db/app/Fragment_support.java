package ru.db.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fragment_support extends Fragment {
    static View root;
    static ScrollView scrollView;
    static LinearLayout scroll;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_support, container, false);
        scroll = root.findViewById(R.id.scrollView);
        scrollView = root.findViewById(R.id.scroll);

        ImageView send = root.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = root.findViewById(R.id.text);
                if(!text.getText().toString().equals("")){
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    HashMap<String,Object> new_user=new HashMap<>();
                    new_user.put("id",my.id);
                    new_user.put("date",df.format(c));
                    new_user.put("text",text.getText().toString());
                    new_user.put("name",my.name.split(" ")[0]);
                    new_user.put("id",my.id);
                    my.dbmessages2.child(my.id).push().setValue(new_user);
                    text.setText("");
                }
            }
        });

        for(Map.Entry<String, HashMap> entry : my.Messages2.entrySet()) {
            add_message(entry);
        }
        return root;
    }
    static void add_message(Map.Entry<String, HashMap> entry){

        LinearLayout gl = new LinearLayout(root.getContext());
        gl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gllayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(my.id.equals(entry.getValue().get("id"))){
            gllayoutParams.setMargins(50,15,30,0);
            gllayoutParams.gravity = Gravity.RIGHT;
        }
        else{
            gllayoutParams.setMargins(50,15,0,0);

        }
        gl.setLayoutParams(gllayoutParams);
        gl.setBackgroundResource(R.drawable.rectangle_3);

        TextView start_adress_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startparams.setMargins(10,10,10,0);

        start_adress_textview.setText(entry.getValue().get("text").toString());
        start_adress_textview.setTextColor(Color.BLACK);
        start_adress_textview.setTextSize(20);
        start_adress_textview.setLayoutParams(startparams);
        gl.addView(start_adress_textview);

        start_adress_textview = new TextView(root.getContext());
        startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startparams.setMargins(40,0,10,0);
        start_adress_textview.setText(entry.getValue().get("date").toString().split(" ")[1]);

        start_adress_textview.setTextColor(Color.GRAY);
        start_adress_textview.setTextSize(18);
        start_adress_textview.setLayoutParams(startparams);
        gl.addView(start_adress_textview);

        scroll.addView(gl);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);

    }
}