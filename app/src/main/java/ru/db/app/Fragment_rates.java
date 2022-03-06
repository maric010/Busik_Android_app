package ru.db.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_rates extends Fragment {
    View root;
    LinearLayout scroll;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_rates, container, false);

        scroll = root.findViewById(R.id.scrollView);


        my.db.collection("users").document(my.id).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                float rate=0;
                if(docs.size()>0){
                    for(int i=0;i<docs.size();i++){
                        DocumentSnapshot doc = docs.get(i);
                        Map<String, Object> h = doc.getData();
                        rate += Integer.parseInt(h.get("rate").toString());
                        add_rate(h);
                    }

                }
                TextView rate_count = root.findViewById(R.id.rate_count);
                rate_count.setText("У вас "+docs.size()+" отзывов");
                TextView sredniy = root.findViewById(R.id.sredniy);
                sredniy.setText("Средний "+(rate/docs.size())+"");
            }
        });
        return root;
    }
    void add_rate(Map<String, Object> h){


        LinearLayout gl = new LinearLayout(root.getContext());
        gl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gllayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gllayoutParams.setMargins(20,20,20,0);
        gl.setLayoutParams(gllayoutParams);

        LinearLayout linearLayout1 = new LinearLayout(root.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams l1params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        l1params.setMargins(20,20,20,20);
        linearLayout1.setLayoutParams(l1params);

        CircleImageView arrow = new CircleImageView(root.getContext());
        LinearLayout.LayoutParams arrowp = new LinearLayout.LayoutParams
                (80, ViewGroup.LayoutParams.MATCH_PARENT);
        arrowp.gravity = Gravity.CENTER;
        arrow.setLayoutParams(arrowp);

        if(h.get("avatar")!=null){
            Glide.with(root.getContext())
                    .load(my.gen_avatar(h.get("avatar").toString()))
                    .placeholder(R.drawable.main_logo)
                    .into(arrow);
        }else arrow.setBackgroundResource(R.drawable.main_logo);

        linearLayout1.addView(arrow);



        LinearLayout linearLayout2 = new LinearLayout(root.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams l2params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout2.setLayoutParams(l2params);

        TextView start_adress_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startparams.setMargins(10,5,0,0);
        start_adress_textview.setText(h.get("name").toString());

        start_adress_textview.setTextColor(Color.BLACK);
        start_adress_textview.setTextSize(20);
        start_adress_textview.setLayoutParams(startparams);
        linearLayout2.addView(start_adress_textview);
        start_adress_textview = new TextView(root.getContext());
        startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startparams.setMargins(10,0,0,0);
        start_adress_textview.setText(h.get("date").toString());

        start_adress_textview.setTextColor(Color.BLACK);
        start_adress_textview.setTextSize(18);
        start_adress_textview.setLayoutParams(startparams);
        linearLayout2.addView(start_adress_textview);
        linearLayout1.addView(linearLayout2);

        linearLayout2 = new LinearLayout(root.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        l2params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout2.setLayoutParams(l2params);


        LinearLayout linearLayout3 = new LinearLayout(root.getContext());
        linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams l3params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l3params.gravity = Gravity.RIGHT;
        linearLayout3.setLayoutParams(l3params);

        start_adress_textview = new TextView(root.getContext());
        startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startparams.setMargins(0,0,0,0);
        startparams.gravity = Gravity.CENTER;
        start_adress_textview.setText(h.get("rate").toString());
        start_adress_textview.setTextColor(Color.BLACK);
        start_adress_textview.setTextSize(20);
        start_adress_textview.setLayoutParams(startparams);
        linearLayout3.addView(start_adress_textview);

        arrow = new CircleImageView(root.getContext());
        arrowp = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, 40);
        arrowp.gravity = Gravity.CENTER;
        arrowp.setMargins(5,0,0,0);
        arrow.setImageResource(R.drawable.star);
        arrow.setLayoutParams(arrowp);
        linearLayout3.addView(arrow);

        linearLayout2.addView(linearLayout3);
        linearLayout1.addView(linearLayout2);
        gl.addView(linearLayout1);
        start_adress_textview = new TextView(root.getContext());
        startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startparams.setMargins(15,0,15,0);
        startparams.gravity = Gravity.CENTER;

        start_adress_textview.setText(h.get("text").toString());
        start_adress_textview.setTextColor(Color.BLACK);
        start_adress_textview.setTextSize(18);
        start_adress_textview.setBackgroundResource(R.drawable.rectangle_3);
        start_adress_textview.setLayoutParams(startparams);
        gl.addView(start_adress_textview);

scroll.addView(gl);

    }
}