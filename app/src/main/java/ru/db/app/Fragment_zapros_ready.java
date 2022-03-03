package ru.db.app;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class Fragment_zapros_ready extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_zapros_ready, container, false);
        TextView reys_day_month = root.findViewById(R.id.reys_day_month);
        String[] sp = my.current_order.getValue().get("start_date").toString().split(" ")[0].split("\\.");
        reys_day_month.setText("К рейсу "+sp[0]+"."+sp[1]);
        return root;
    }
}