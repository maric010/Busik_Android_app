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

public class Fragment_reys_carrier extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reys_carrier, container, false);

        TextView week_day_month = root.findViewById(R.id.week_day_month);
        week_day_month.setText(my.current_order.get("start_date").toString());
        TextView description = root.findViewById(R.id.description);
        description.setText(my.current_order.get("description").toString());
        TextView start_country_city = root.findViewById(R.id.start_country_city);
        start_country_city.setText(my.current_order.get("otkuda").toString());
        TextView stop_country_city  = root.findViewById(R.id.stop_country_city);
        stop_country_city.setText(my.current_order.get("kuda").toString());
        TextView start_week_hour = root.findViewById(R.id.start_week_hour);
        start_week_hour.setText(my.current_order.get("start_date").toString());

        TextView stop_week_hour = root.findViewById(R.id.stop_week_hour);
        stop_week_hour.setText(my.current_order.get("stop_date").toString());


        return root;
    }
}