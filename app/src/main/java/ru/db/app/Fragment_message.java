package ru.db.app;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class Fragment_message extends Fragment {
    static View root;
    static LinearLayout scrollView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_message, container, false);
        scrollView = root.findViewById(R.id.scrollView);
        for(Map.Entry<String, HashMap> entry : my.Messages.entrySet()) {
            new_passenger(entry);
        }
        return root;
    }
    static void new_passenger(Map.Entry<String, HashMap> entry){
        HashMap h = entry.getValue();
        LinearLayout gl = new LinearLayout(root.getContext());
        gl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gllayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gllayoutParams.setMargins(30,35,30,15);
        gl.setLayoutParams(gllayoutParams);

        gl.setBackgroundResource(R.drawable.border);

        LinearLayout tx = new LinearLayout(root.getContext());
        tx.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams txp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txp.setMargins(0,0,0,0);
        tx.setLayoutParams(txp);


        TextView title = new TextView(root.getContext());
        LinearLayout.LayoutParams titlep = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlep.setMargins(30,20,30,0);
        title.setText(h.get("title").toString());
        title.setTextSize(18);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);
        title.setLayoutParams(titlep);
        tx.addView(title);

        LinearLayout lx = new LinearLayout(root.getContext());
        lx.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lxp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lx.setLayoutParams(lxp);

        ImageView x = new ImageView(root.getContext());
        LinearLayout.LayoutParams xp = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        xp.gravity = Gravity.RIGHT;
        xp.setMargins(0,0,20,0);
        x.setLayoutParams(xp);
        x.setImageResource(R.drawable.ic_vector_12);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my.dbmessages.child(my.id).child(entry.getKey()).removeValue();
                scrollView.removeView(gl);
            }
        });
        lx.addView(x);

tx.addView(lx);
gl.addView(tx);

        title = new TextView(root.getContext());
        title.setText(h.get("text").toString());
        title.setTextSize(18);
        title.setTextColor(Color.DKGRAY);
        title.setLayoutParams(titlep);
        gl.addView(title);

        Button button = new Button(root.getContext());
        LinearLayout.LayoutParams buttonp = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonp.setMargins(30,20,30,20);
        button.setTextSize(15f);
        if(h.get("order")!=null)
            button.setText("К рейсу");
        else if (h.get("rate")!=null)
            button.setText("    Поставить отзыв    ");
        button.setTextColor(Color.WHITE);
        button.setLayoutParams(buttonp);
        button.setAllCaps(false);
        button.setBackgroundResource(R.drawable.button_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(h.get("order")!=null){
                    Map.Entry<String, HashMap> entry2 = new Map.Entry<String, HashMap>() {
                        @Override
                        public String getKey() {
                            return h.get("order").toString();
                        }

                        @Override
                        public HashMap getValue() {
                            if(my.Orders.get(h.get("order"))!=null)
                                return my.Orders.get(h.get("order").toString());
                            else
                                return my.Orders_arxiv.get(h.get("order").toString());
                        }

                        @Override
                        public HashMap setValue(HashMap hashMap) {
                            return null;
                        }
                    };

                    my.current_order = entry2;
                    if(my.status.equals("Пасажир"))
                        MainActivity.th.switch_fragment(new Fragment_reys());
                    else if(my.status.equals("Перевозчик"))
                        MainActivity.th.switch_fragment(new Fragment_reys_carrier());
                }
                else if(h.get("rate")!=null){
                    my.current_rate_owner=h.get("rate").toString();
                    my.current_rate_reys=h.get("text").toString().split("\n")[1];
                    my.current_rate_owner_name=h.get("rate_owner").toString();
                    my.current_rate_message = entry.getKey();
                    MainActivity.th.switch_fragment(new Fragment_add_rate());
                }





            }
        });
        gl.addView(button);
        scrollView.addView(gl);
    }
}