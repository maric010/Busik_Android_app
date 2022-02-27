package ru.db.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_reys_request extends Fragment {
    View root;
    TextView people_count;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reys_apply, container, false);



        TextView reys_day_month = root.findViewById(R.id.reys_day_month);

        String start_date = my.current_order.getValue().get("start_date").toString().split(" ")[0];



       // 27.02.2022
        //Пн 13 Декабря
        String[] split = start_date.split("\\.");
        reys_day_month.setText("К рейсу "+split[0]+"."+split[1]);

        TextView week_day_month = root.findViewById(R.id.week_day_month);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf.parse(my.current_order.getValue().get("start_date").toString().split(" ")[0]);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf2.parse(my.current_order.getValue().get("stop_date").toString().split(" ")[0]);
            c2.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView start_week_hour = root.findViewById(R.id.start_week_hour);
        start_week_hour.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+" "+my.current_order.getValue().get("start_date").toString().split(" ")[1]);
        TextView stop_week_hour = root.findViewById(R.id.stop_week_hour);
        stop_week_hour.setText(my.get_week()[c2.get(Calendar.DAY_OF_WEEK)-1]+" "+my.current_order.getValue().get("stop_date").toString().split(" ")[1]);


        String month = my.getMonths()[Integer.valueOf(my.current_order.getValue().get("start_date").toString().split("\\.")[1])-1];
        week_day_month.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+c.get(Calendar.DAY_OF_MONTH-1)+" "+month);


        int people_cost = Integer.parseInt(my.current_order.getValue().get("passenger_cost").toString());
        int gruz_cost = Integer.parseInt(my.current_order.getValue().get("gruz_cost").toString());
        TextView rate_ratecount = root.findViewById(R.id.rate_ratecount);
        rate_ratecount.setText(my.current_order.getValue().get("owner_rate").toString());
        TextView people_itog = root.findViewById(R.id.calculated_people_cost);

        TextView itog = root.findViewById(R.id.in_total);
        TextView gruz = root.findViewById(R.id.gruz);

        people_count = root.findViewById(R.id.people);
        System.out.println("a1a1a1");
        Button people_up = root.findViewById(R.id.people_up);
        people_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    people_count.setText((Integer.parseInt(people_count.getText().toString()) + 1) +"");
                    int a = Integer.parseInt(people_count.getText().toString());
                    people_itog.setText(a+" человека "+a*people_cost+" €");
                    int b = Integer.parseInt(gruz.getText().toString());
                    itog.setText("Итого: €"+((b*gruz_cost) + a*people_cost));

            }
        });
        System.out.println("a2a2a2");

        Button gruz_up = root.findViewById(R.id.gruz_up);
        gruz_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gruz.setText((Integer.parseInt(gruz.getText().toString()) + 1) +"");
                    int a = Integer.parseInt(people_count.getText().toString());
                    int b = Integer.parseInt(gruz.getText().toString());
                    itog.setText("Итого: €"+((b*gruz_cost) + a*people_cost));

            }
        });

        Button people_down = root.findViewById(R.id.people_down);
        people_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!people_count.getText().toString().equals("0")) {
                    people_count.setText((Integer.parseInt(people_count.getText().toString()) - 1) + "");
                    int a = Integer.parseInt(people_count.getText().toString());
                    people_itog.setText(a+" человека "+a*people_cost+" €");
                    int b = Integer.parseInt(gruz.getText().toString());
                    itog.setText("Итого: €"+((b*gruz_cost) + a*people_cost));
                }
            }
        });

        Button gruz_down = root.findViewById(R.id.gruz_down);
        gruz_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gruz.getText().toString().equals("0")){
                    gruz.setText((Integer.parseInt(gruz.getText().toString()) - 1) +"");
                    int a = Integer.parseInt(people_count.getText().toString());
                    int b = Integer.parseInt(gruz.getText().toString());
                    itog.setText("Итого: €"+((b*gruz_cost) + a*people_cost));

                }
            }
        });



        TextView name = root.findViewById(R.id.name);
        name.setText(my.current_order.getValue().get("owner_name").toString());
        TextView start_country_city = root.findViewById(R.id.start_country_city);
        TextView stop_country_city = root.findViewById(R.id.stop_country_city);

        start_country_city.setText(my.current_order.getValue().get("otkuda").toString());
        stop_country_city.setText(my.current_order.getValue().get("kuda").toString());



        Button request = root.findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Сообщение
                HashMap<String,Object> new_message=new HashMap<>();
                new_message.put("title","Новый пасажир");
                new_message.put("text","На рейсе "+my.current_order.getValue().get("otkuda")+" -> "
                        +my.current_order.getValue().get("kuda")+" "+split[0]+"."+split[1]+"("+my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+")");
                new_message.put("order",my.current_order.getKey());
                my.dbmessages.child(my.current_order.getValue().get("owner").toString()).push().setValue(new_message);
                //Сообщение end


                HashMap<String,Object> user=new HashMap<>();

                user.put("name",my.name);
                user.put("avatar",my.avatar);
                user.put("phone",my.phone);
                user.put("peoples",people_count.getText().toString());
                user.put("gruz",gruz.getText().toString());
                my.dborders.child(my.current_order.getKey()).child("passengers_request").child(my.id).setValue(user);

                MainActivity.th.switch_fragment(new Fragment_zapros_ready());

            }
        });
                if(my.current_order.getValue().get("owner_avatar")!=null){
                    Glide.with(root.getContext())
                            .load(my.gen_avatar(my.current_order.getValue().get("owner_avatar").toString()))
                            .error(R.drawable.ellipse_1)
                            .placeholder(R.drawable.ellipse_1)
                            .into((CircleImageView)root.findViewById(R.id.avatar));

                }

        return root;
    }

}