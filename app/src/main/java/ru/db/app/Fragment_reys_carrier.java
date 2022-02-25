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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fragment_reys_carrier extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reys_carrier, container, false);


        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf.parse(my.current_order.get("start_date").toString().split(" ")[0]);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c2 = Calendar.getInstance();

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf2.parse(my.current_order.get("stop_date").toString().split(" ")[0]);
            c2.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView week_day_month = root.findViewById(R.id.week_day_month);
        System.out.println(my.current_order.get("stop_date").toString());
        String month = my.getMonths()[Integer.valueOf(my.current_order.get("start_date").toString().split("\\.")[1])-1];
        week_day_month.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)]+c.get(Calendar.DAY_OF_MONTH)+" "+month);
        TextView description = root.findViewById(R.id.description);
        description.setText(my.current_order.get("description").toString());
        TextView start_country_city = root.findViewById(R.id.start_country_city);
        start_country_city.setText(my.current_order.get("otkuda").toString());
        TextView stop_country_city  = root.findViewById(R.id.stop_country_city);
        stop_country_city.setText(my.current_order.get("kuda").toString());
        TextView start_week_hour = root.findViewById(R.id.start_week_hour);

        start_week_hour.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+" "+my.current_order.get("start_date").toString().split(" ")[1]);

        TextView stop_week_hour = root.findViewById(R.id.stop_week_hour);
        stop_week_hour.setText(my.get_week()[c2.get(Calendar.DAY_OF_WEEK)-1]+" "+my.current_order.get("stop_date").toString().split(" ")[1]);

        TextView passenger_cost = root.findViewById(R.id.passenger_cost);
        passenger_cost.setText(my.current_order.get("passenger_cost").toString());

        TextView gruz_cost = root.findViewById(R.id.gruz_cost);
        gruz_cost.setText(my.current_order.get("gruz_cost").toString());


        return root;
    }
}