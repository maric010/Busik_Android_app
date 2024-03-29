package ru.db.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_reys_passengers extends Fragment {
    View root;
    static LinearLayout scrollView_request,scrollView_accept;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reys_passengers, container, false);
        scrollView_request=root.findViewById(R.id.scrollView_request);
        scrollView_accept=root.findViewById(R.id.scrollView_accept);

        TextView week_day_month = root.findViewById(R.id.week_day_month);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf.parse(my.current_order.getValue().get("start_date").toString().split(" ")[0]);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String month = my.getMonths()[Integer.valueOf(my.current_order.getValue().get("start_date").toString().split("\\.")[1])-1];
        week_day_month.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+c.get(Calendar.DAY_OF_MONTH)+" "+month);

if(my.current_order.getValue().get("passengers_request")!=null){
    HashMap<String, HashMap> request = (HashMap<String, HashMap>) my.current_order.getValue().get("passengers_request");
    if(request!=null){
        for(Map.Entry<String, HashMap> entry : (request).entrySet()) {
            System.out.println(entry);
            add_request(entry);
        }
    }
}
        int gruz_count = 0;
        HashMap<String, HashMap> accepted=null;
        if( my.current_order.getValue().get("passengers_accepted")!=null){
        accepted = (HashMap<String, HashMap>) my.current_order.getValue().get("passengers_accepted");
        if(accepted!=null){
            for(Map.Entry<String, HashMap> entry : (accepted).entrySet()) {
                add_accepted(entry);
                if(entry.getValue().get("gruz")!=null)
                    gruz_count+=Integer.parseInt(entry.getValue().get("gruz").toString());
            }
    }
}


        int accepted_count = 0;
        if(accepted!=null)
            accepted_count=accepted.size();

        TextView accepted_gruz = root.findViewById(R.id.accepted_gruz);
        accepted_gruz.setText("Подтверждено груза: "+gruz_count+" кг");
        TextView accepted_count_t = root.findViewById(R.id.accepted_passengers_count);
        accepted_count_t.setText("Подтверждено мест: "+accepted_count+" человека");
        TextView status = root.findViewById(R.id.status);
        status.setText("    "+my.current_order.getValue().get("status").toString()+"    ");

        HashMap<String, HashMap> requested = (HashMap<String, HashMap>) my.current_order.getValue().get("passengers_request");

        TextView request_count = root.findViewById(R.id.request_count);
        if(requested!=null)
            request_count.setText("Запросы ("+requested.size()+")");
        TextView accepted_count_a = root.findViewById(R.id.accepted_count);
        accepted_count_a.setText("Подтвержденные ("+accepted_count+")");

        return root;
    }
    void add_request(Map.Entry<String, HashMap> entry){
        LinearLayout gl = new LinearLayout(root.getContext());
        gl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gllayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gllayoutParams.setMargins(0,20,0,0);
        gl.setLayoutParams(gllayoutParams);

        LinearLayout line1 = new LinearLayout(root.getContext());
        line1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams line1p = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        line1p.setMargins(0,20,0,0);
        line1.setLayoutParams(line1p);

        CircleImageView avatar = new CircleImageView(root.getContext());
        LinearLayout.LayoutParams avatarp = new LinearLayout.LayoutParams
                (100, ViewGroup.LayoutParams.MATCH_PARENT);
        if(entry.getValue().get("avatar")!=null){
            Glide.with(root.getContext())
                    .load(my.gen_avatar(entry.getValue().get("avatar").toString()))
                    .placeholder(R.drawable.ellipse_3)
                    .into(avatar);
        }else
            avatar.setImageResource(R.drawable.ellipse_3);
        avatar.setLayoutParams(avatarp);

        line1.addView(avatar);

        LinearLayout v = new LinearLayout(root.getContext());
        v.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //vp.gravity = Gravity.CENTER_VERTICAL;
        vp.setMargins(-30,0,0,0);
        v.setLayoutParams(vp);

        TextView t = new TextView(root.getContext());
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        t.setText(entry.getValue().get("name").toString());
        t.setTextColor(Color.BLACK);
        t.setTextSize(18f);
        t.setLayoutParams(tp);

        v.addView(t);

        t=new TextView(root.getContext());
        t.setText("+"+entry.getValue().get("phone").toString());
        t.setTextSize(16f);
        t.setTextColor(Color.GRAY);
        t.setLayoutParams(tp);

        v.addView(t);
        line1.addView(v);

        v = new LinearLayout(root.getContext());
        v.setOrientation(LinearLayout.VERTICAL);
        vp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(vp);

        Button button = new Button(root.getContext());
        LinearLayout.LayoutParams buttonp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonp.gravity = Gravity.RIGHT;
        button.setBackgroundResource(R.drawable.button_2);
        button.setText("\uD83D\uDCDE");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(16f);
        button.setLayoutParams(buttonp);
        v.addView(button);
        line1.addView(v);

        gl.addView(line1);
        LinearLayout line2 = new LinearLayout(root.getContext());
        line2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams line2p = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        line2p.setMargins(25,10,0,0);
        line2.setLayoutParams(line2p);

        t = new TextView(root.getContext());
        t.setText("Человек: "+entry.getValue().get("peoples").toString());
        t.setTextColor(Color.BLACK);
        t.setTextSize(16f);
        t.setLayoutParams(tp);
        line2.addView(t);

        t = new TextView(root.getContext());
        tp.setMargins(50,0,0,0);
        t.setText("Груза: "+entry.getValue().get("gruz").toString()+" кг");
        t.setTextColor(Color.BLACK);
        t.setTextSize(16f);
        t.setLayoutParams(tp);
        line2.addView(t);

        gl.addView(line2);
        v = new LinearLayout(root.getContext());
        v.setOrientation(LinearLayout.HORIZONTAL);
        vp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp.setMargins(0,10,0,0);
        vp.gravity = Gravity.CENTER_HORIZONTAL;
        v.setLayoutParams(vp);

        button = new Button(root.getContext());
        buttonp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonp.setMargins(0,10,0,0);
        button.setBackgroundResource(R.drawable.button_2);
        button.setText("Принять");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(15f);
        button.setLayoutParams(buttonp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference field1 = my.dborders.child(my.current_order.getKey()).child("passengers_accepted");
                DatabaseReference field2 = field1.child(entry.getKey());
                field2.setValue(entry.getValue());
                my.dborders.child(my.current_order.getKey()).child("passengers_request").child(entry.getKey()).removeValue();
                scrollView_request.removeView(gl);
                add_accepted(entry);

                String start_date = my.current_order.getValue().get("start_date").toString().split(" ")[0];
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    Date date = sdf.parse(start_date);
                    c.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String[] split = start_date.split("\\.");
                HashMap<String,Object> new_message=new HashMap<>();
                new_message.put("title","Вы приняты");
                new_message.put("text","На рейсе "+my.current_order.getValue().get("otkuda").toString().split(" ")[1]+" -> "
                        +my.current_order.getValue().get("kuda").toString().split(" ")[1]+" "+split[0]+"."+split[1]+"("+my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+")");
                new_message.put("order",my.current_order.getKey());
                my.dbmessages.child(entry.getKey()).push().setValue(new_message);

            }
        });

        v.addView(button);

        button = new Button(root.getContext());
        buttonp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonp.setMargins(15,10,0,0);
        button.setBackgroundResource(R.drawable.button_2);
        button.setText("Отклонить");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(15f);
        button.setLayoutParams(buttonp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my.dborders.child(my.current_order.getKey()).child("passengers_request").child(entry.getKey()).removeValue();
                scrollView_request.removeView(gl);
            }
        });
        v.addView(button);

        button = new Button(root.getContext());
        button.setBackgroundResource(R.drawable.button_2);
        button.setText("Отзывы");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(15f);
        button.setLayoutParams(buttonp);

        v.addView(button);

        gl.addView(v);
        scrollView_request.addView(gl);
    }
    void add_accepted(Map.Entry<String, HashMap> entry){
        LinearLayout gl = new LinearLayout(root.getContext());
        gl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gllayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gllayoutParams.setMargins(0,20,0,0);
        gl.setLayoutParams(gllayoutParams);

        LinearLayout line1 = new LinearLayout(root.getContext());
        line1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams line1p = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        line1p.setMargins(0,20,0,0);
        line1.setLayoutParams(line1p);

        CircleImageView avatar = new CircleImageView(root.getContext());
        LinearLayout.LayoutParams avatarp = new LinearLayout.LayoutParams
                (100, ViewGroup.LayoutParams.MATCH_PARENT);
        if(entry.getValue().get("avatar")!=null){
            Glide.with(root.getContext())
                    .load(my.gen_avatar(entry.getValue().get("avatar").toString()))
                    .placeholder(R.drawable.ellipse_3)
                    .into(avatar);
        }else
            avatar.setImageResource(R.drawable.ellipse_3);
        avatar.setLayoutParams(avatarp);

        line1.addView(avatar);

        LinearLayout v = new LinearLayout(root.getContext());
        v.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //vp.gravity = Gravity.CENTER_VERTICAL;
        vp.setMargins(-30,0,0,0);
        v.setLayoutParams(vp);

        TextView t = new TextView(root.getContext());
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        t.setText(entry.getValue().get("name").toString());
        t.setTextColor(Color.BLACK);
        t.setTextSize(18f);
        t.setLayoutParams(tp);
        v.addView(t);

        t=new TextView(root.getContext());
        t.setText("+"+entry.getValue().get("phone").toString());
        t.setTextSize(16f);
        t.setTextColor(Color.GRAY);
        t.setLayoutParams(tp);

        v.addView(t);
        line1.addView(v);

        v = new LinearLayout(root.getContext());
        v.setOrientation(LinearLayout.VERTICAL);
        vp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(vp);

        Button button = new Button(root.getContext());
        LinearLayout.LayoutParams buttonp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonp.gravity = Gravity.RIGHT;
        button.setBackgroundResource(R.drawable.button_2);
        button.setText("\uD83D\uDCDE");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(16f);
        button.setLayoutParams(buttonp);
        v.addView(button);
        line1.addView(v);

        gl.addView(line1);
        LinearLayout line2 = new LinearLayout(root.getContext());
        line2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams line2p = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        line2p.setMargins(25,10,0,0);
        line2.setLayoutParams(line2p);

        t = new TextView(root.getContext());
        t.setText("Человек: "+entry.getValue().get("peoples").toString());
        t.setTextColor(Color.BLACK);
        t.setTextSize(16f);
        t.setLayoutParams(tp);
        line2.addView(t);

        t = new TextView(root.getContext());
        tp.setMargins(50,0,0,0);
        t.setText("Груза: "+entry.getValue().get("gruz").toString()+" кг");
        t.setTextColor(Color.BLACK);
        t.setTextSize(16f);
        t.setLayoutParams(tp);
        line2.addView(t);

        gl.addView(line2);
        v = new LinearLayout(root.getContext());
        v.setOrientation(LinearLayout.HORIZONTAL);
        vp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp.setMargins(0,10,0,0);
        vp.gravity = Gravity.CENTER_HORIZONTAL;
        v.setLayoutParams(vp);

        button = new Button(root.getContext());
        buttonp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonp.setMargins(0,10,20,0);
        button.setBackgroundResource(R.drawable.button_2);
        button.setText("Удалить");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(15f);
        button.setLayoutParams(buttonp);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        my.dborders.child(my.current_order.getKey()).child("passengers_accepted").child(entry.getKey()).removeValue();
        scrollView_accept.removeView(gl);
    }
});
        v.addView(button);


        button = new Button(root.getContext());
        button.setBackgroundResource(R.drawable.button_2);
        button.setText("Отзывы");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(15f);
        button.setLayoutParams(buttonp);

        v.addView(button);

        gl.addView(v);
        scrollView_accept.addView(gl);
    }
}