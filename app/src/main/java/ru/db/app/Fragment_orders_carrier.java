package ru.db.app;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fragment_orders_carrier extends Fragment {
    static View root;
    static LinearLayout scrollView;
    static String odate="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_orders_carrier, container, false);
        scrollView = root.findViewById(R.id.scroll_orders);
        odate="";
        //my.sort_orders();
        for(Map.Entry<String, HashMap> entry : my.Orders.entrySet()) {
                add_carrier_order(entry);
        }

        return root;
    }


    static void add_carrier_order(Map.Entry<String, HashMap> entry){
        TextView summa;
        LinearLayout.LayoutParams summap;
        HashMap h = entry.getValue();
        if (!h.get("owner").toString().equalsIgnoreCase(my.id))
           return;
        String order_date = h.get("start_date").toString();


        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateText = dateFormat.format(new Date());
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date()); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 1);
        Date newDate = instance.getTime();
        String newdateText = dateFormat.format(newDate);
        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf.parse(h.get("start_date").toString().split(" ")[0]);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c2 = Calendar.getInstance();

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf2.parse(h.get("stop_date").toString().split(" ")[0]);
            c2.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(dateText.equalsIgnoreCase(order_date.split(" ")[0])){
            order_date="Сегодня";
        }
        else if(newdateText.equalsIgnoreCase(order_date.split(" ")[0])){
            order_date="Завтра";
        }
        order_date = order_date.split(" ")[0];

        if(!order_date.equals(odate)){
            odate=order_date;
            summa = new TextView(root.getContext());
            summap = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            summap.gravity = Gravity.CENTER;
            summap.setMargins(0,10,0,10);
            summa.setText(odate);
            summa.setTextSize(20);
            summa.setLayoutParams(summap);
            scrollView.addView(summa);
        }

        LinearLayout gl = new LinearLayout(root.getContext());
        gl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gllayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gllayoutParams.setMargins(20,20,20,10);
        gl.setLayoutParams(gllayoutParams);
        LinearLayout linearLayout1 = new LinearLayout(root.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams l1params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(l1params);

        LinearLayout linearLayout2 = new LinearLayout(root.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams l2params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l2params.setMargins(50,35,0,40);
        linearLayout2.setLayoutParams(l2params);

        TextView start_date_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams starttextviewparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        starttextviewparams.setMargins(0,0,0,3);
        start_date_textview.setText(h.get("start_date").toString());
        start_date_textview.setTextSize(18);
        start_date_textview.setTextColor(Color.WHITE);
        start_date_textview.setLayoutParams(starttextviewparams);
        linearLayout2.addView(start_date_textview);

        TextView stop_date_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams stoptextviewparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stop_date_textview.setText(h.get("stop_date").toString());
        stop_date_textview.setTextSize(18);
        stop_date_textview.setTextColor(Color.WHITE);
        stop_date_textview.setLayoutParams(stoptextviewparams);
        linearLayout2.addView(stop_date_textview);

        linearLayout1.addView(linearLayout2);

        ImageView arrow = new ImageView(root.getContext());
        LinearLayout.LayoutParams arrowp = new LinearLayout.LayoutParams
                (90, 160);
        arrowp.setMargins(0,0,0,0);
        arrow.setImageResource(R.drawable.ic_strelka);
        arrow.setLayoutParams(arrowp);
        linearLayout1.addView(arrow);

        LinearLayout linearLayout3 = new LinearLayout(root.getContext());
        linearLayout3.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams l3params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l3params.setMargins(5,35,0,0);
        linearLayout3.setLayoutParams(l3params);

        TextView start_adress_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        starttextviewparams.setMargins(0,0,0,3);
        start_adress_textview.setText(h.get("otkuda").toString());
        start_adress_textview.setTextSize(18);
        start_adress_textview.setTextColor(Color.WHITE);
        start_adress_textview.setLayoutParams(startparams);
        linearLayout3.addView(start_adress_textview);

        TextView stop_adress_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams stopparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stop_adress_textview.setText(h.get("kuda").toString());
        stop_adress_textview.setTextSize(18);
        stop_adress_textview.setTextColor(Color.WHITE);
        stop_adress_textview.setLayoutParams(stopparams);
        linearLayout3.addView(stop_adress_textview);

        linearLayout1.addView(linearLayout3);
        gl.addView(linearLayout1);

        linearLayout1 = new LinearLayout(root.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        l1params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l1params.setMargins(20,0,0,0);
        linearLayout1.setLayoutParams(l1params);

        arrow = new ImageView(root.getContext());
        arrowp = new LinearLayout.LayoutParams
                (50, ViewGroup.LayoutParams.MATCH_PARENT);
        arrowp.setMargins(20,0,5,0);
        arrow.setImageResource(R.drawable.ic_user_svgrepo_com);
        arrow.setLayoutParams(arrowp);
        linearLayout1.addView(arrow);

        summa = new TextView(root.getContext());
        summap = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summa.setText("€ "+h.get("passenger_cost").toString()+" ");
        summa.setTextSize(24);
        summa.setTextColor(Color.WHITE);
        summa.setTypeface(null, Typeface.BOLD);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);

        summa = new TextView(root.getContext());
        summa.setText("/ чел");
        summa.setTextSize(22);
        summa.setTextColor(Color.WHITE);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);
        if(!h.get("gruz_cost").toString().equals("0")) {
            arrow = new ImageView(root.getContext());
            arrowp.setMargins(25, 0, 5, 0);
            arrow.setImageResource(R.drawable.ic_luggage_svgrepo_com);
            arrow.setLayoutParams(arrowp);
            linearLayout1.addView(arrow);

            summa = new TextView(root.getContext());
            summa.setText("€ " + h.get("gruz_cost").toString() + " ");
            summa.setTextSize(24);
            summa.setTextColor(Color.WHITE);
            summa.setTypeface(null, Typeface.BOLD);
            summa.setLayoutParams(summap);
            linearLayout1.addView(summa);

            summa = new TextView(root.getContext());
            summa.setText("/ кг");
            summa.setTextColor(Color.WHITE);
            summa.setTextSize(22);
            summa.setLayoutParams(summap);
            linearLayout1.addView(summa);

        }

        gl.addView(linearLayout1);

        linearLayout1 = new LinearLayout(root.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        l1params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l1params.setMargins(45,30,0,40);

        linearLayout1.setLayoutParams(l1params);



        summa = new TextView(root.getContext());
        summa.setText("  "+h.get("status").toString()+"  ");
        summa.setTextSize(18);
        summa.setTextColor(Color.WHITE);
        summap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //summap.gravity = Gravity.END;
        summa.setLayoutParams(summap);

        linearLayout1.addView(summa);

        summa.setBackgroundResource(R.drawable.status_border);
        gl.addView(linearLayout1);

        gl.setBackgroundResource(R.drawable.border_for_reys);
        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my.current_order = entry;
                MainActivity.fr=4;
                MainActivity.th.switch_fragment(new Fragment_reys_carrier());
            }
        });
        scrollView.addView(gl);

    }
}