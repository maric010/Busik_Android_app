package ru.db.app;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
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
            add_order(entry);
        }

        return root;
    }
    static void add_order(Map.Entry<String, HashMap> entry){



        HashMap h = entry.getValue();
        LinearLayout gl = new LinearLayout(root.getContext());
        gl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gllayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gllayoutParams.setMargins(30,15,30,15);
        gl.setLayoutParams(gllayoutParams);

        gl.setBackgroundResource(R.drawable.border);
  /*
        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.th.switch_fragment(new Fragment_reys());

                Map.Entry<String, HashMap> entry2 = new Map.Entry<String, HashMap>() {
                    @Override
                    public String getKey() {
                        return h.get("order").toString();
                    }

                    @Override
                    public HashMap getValue() {
                        return my.Orders.get(h.get("order").toString());
                    }

                    @Override
                    public HashMap setValue(HashMap hashMap) {
                        return null;
                    }
                };

                my.current_order = entry2;

            }
        });
*/





        TextView title = new TextView(root.getContext());
        LinearLayout.LayoutParams titlep = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlep.setMargins(20,20,20,0);
        title.setText(h.get("title").toString());
        title.setTextSize(18);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);
        title.setLayoutParams(titlep);
        gl.addView(title);

        title = new TextView(root.getContext());
        title.setText(h.get("text").toString());
        title.setTextSize(18);
        title.setTextColor(Color.DKGRAY);
        title.setLayoutParams(titlep);
        gl.addView(title);

        Button button = new Button(root.getContext());
        LinearLayout.LayoutParams buttonp = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonp.setMargins(20,20,20,20);

        button.setText("К рейсу");

        button.setBackgroundResource(R.drawable.button_1);

        scrollView.addView(gl);




    }
}