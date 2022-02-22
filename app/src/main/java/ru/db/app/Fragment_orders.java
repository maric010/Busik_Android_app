package ru.db.app;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

public class Fragment_orders extends Fragment {
    static View root;
    static LinearLayout scrollView;
    static String odate="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_orders, container, false);


        scrollView = root.findViewById(R.id.scroll_orders);
        if(my.name.equalsIgnoreCase("")){
            LinearLayout my_trips_layout = root.findViewById(R.id.my_trips_layout);
            my_trips_layout.setVisibility(View.GONE);
            LinearLayout all_trips_layout = root.findViewById(R.id.all_trips_layout);
            all_trips_layout.setVisibility(View.GONE);
            LinearLayout visitor = root.findViewById(R.id.visitor);
            visitor.setVisibility(View.VISIBLE);
        }

        for(Map.Entry<String, HashMap> entry : my.Orders.entrySet()) {
            HashMap h = entry.getValue();
            add_order(h.get("start_date").toString(),h.get("stop_date").toString(),h.get("otkuda").toString(),
                    h.get("kuda").toString(),h.get("passenger_cost").toString()
                    ,h.get("gruz_cost").toString(),"Станислав","5.0","В ожидании","Пн 13.12 (сегодня)");
        }
        return root;
    }
    static void add_order(String order_start_date,String order_stop_date,String order_otkuda,
                   String order_kuda,String cel_summ,String gruz_kg_sum,String order_owner,
                   String order_owner_rate,String order_status,String order_date){
        TextView summa;
        LinearLayout.LayoutParams summap;
        if(!order_date.equals(odate)){
            odate=order_date;
            summa = new TextView(root.getContext());
            summap = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            summap.gravity = Gravity.CENTER;
            summap.setMargins(0,10,0,10);
            summa.setText(order_date);
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
        l2params.setMargins(20,15,0,0);
        linearLayout2.setLayoutParams(l2params);

        TextView start_date_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams starttextviewparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        starttextviewparams.setMargins(0,0,0,3);
        start_date_textview.setText(order_start_date);
        start_date_textview.setTextSize(18);
        start_date_textview.setLayoutParams(starttextviewparams);
        linearLayout2.addView(start_date_textview);

        TextView stop_date_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams stoptextviewparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stop_date_textview.setText(order_stop_date);
        stop_date_textview.setTextSize(18);
        stop_date_textview.setLayoutParams(stoptextviewparams);
        linearLayout2.addView(stop_date_textview);

        linearLayout1.addView(linearLayout2);

        ImageView arrow = new ImageView(root.getContext());
        LinearLayout.LayoutParams arrowp = new LinearLayout.LayoutParams
                (80, 120);
        arrow.setImageResource(R.drawable.strelka2);
        arrow.setLayoutParams(arrowp);
        linearLayout1.addView(arrow);

        LinearLayout linearLayout3 = new LinearLayout(root.getContext());
        linearLayout3.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams l3params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l3params.setMargins(5,15,0,0);
        linearLayout3.setLayoutParams(l3params);

        TextView start_adress_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams startparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        starttextviewparams.setMargins(0,0,0,3);
        start_adress_textview.setText(order_otkuda);
        start_adress_textview.setTextSize(18);
        start_adress_textview.setLayoutParams(startparams);
        linearLayout3.addView(start_adress_textview);

        TextView stop_adress_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams stopparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stop_adress_textview.setText(order_kuda);
        stop_adress_textview.setTextSize(18);
        stop_adress_textview.setLayoutParams(stopparams);
        linearLayout3.addView(stop_adress_textview);

        linearLayout1.addView(linearLayout3);
        gl.addView(linearLayout1);

        linearLayout1 = new LinearLayout(root.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        l1params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(l1params);

        arrow = new ImageView(root.getContext());
        arrowp = new LinearLayout.LayoutParams
                (50, ViewGroup.LayoutParams.MATCH_PARENT);
        arrowp.setMargins(20,0,5,0);
        arrow.setImageResource(R.drawable.people);
        arrow.setLayoutParams(arrowp);
        linearLayout1.addView(arrow);

        summa = new TextView(root.getContext());
        summap = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summa.setText("€ "+cel_summ+" ");
        summa.setTextSize(24);
        summa.setTypeface(null, Typeface.BOLD);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);

        summa = new TextView(root.getContext());
        summa.setText("/ чел");
        summa.setTextSize(22);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);

        arrow = new ImageView(root.getContext());
        arrowp.setMargins(25,0,5,0);
        arrow.setImageResource(R.drawable.suitcase);
        arrow.setLayoutParams(arrowp);
        linearLayout1.addView(arrow);

        summa = new TextView(root.getContext());
        summa.setText("€ "+gruz_kg_sum+" ");
        summa.setTextSize(24);
        summa.setTypeface(null, Typeface.BOLD);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);

        summa = new TextView(root.getContext());
        summa.setText("/ кг");
        summa.setTextSize(22);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);



        gl.addView(linearLayout1);

        linearLayout1 = new LinearLayout(root.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        l1params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linearLayout1.setLayoutParams(l1params);

        gl.addView(linearLayout1);

        arrow = new ImageView(root.getContext());
        arrowp = new LinearLayout.LayoutParams
                (80, ViewGroup.LayoutParams.MATCH_PARENT);
        arrowp.setMargins(20,0,10,0);
        arrow.setImageResource(R.drawable.ellipse_2);
        arrow.setLayoutParams(arrowp);
        linearLayout1.addView(arrow);

        linearLayout2 = new LinearLayout(root.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        l2params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout2.setLayoutParams(l2params);

        summa = new TextView(root.getContext());
        summa.setText(order_owner);
        summa.setTextSize(22);
        summa.setLayoutParams(summap);
        linearLayout2.addView(summa);

        linearLayout3 = new LinearLayout(root.getContext());
        linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        l3params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout3.setLayoutParams(l3params);

        arrow = new ImageView(root.getContext());
        arrowp = new LinearLayout.LayoutParams
                (20, ViewGroup.LayoutParams.MATCH_PARENT);
        arrowp.setMargins(0,0,5,0);
        arrow.setImageResource(R.drawable.star);
        arrow.setLayoutParams(arrowp);
        linearLayout3.addView(arrow);

        summa = new TextView(root.getContext());
        summa.setText(order_owner_rate);
        summa.setTextSize(20);
        summa.setLayoutParams(summap);
        linearLayout3.addView(summa);

        linearLayout2.addView(linearLayout3);
        linearLayout1.addView(linearLayout2);


        linearLayout3 = new LinearLayout(root.getContext());
        linearLayout3.setOrientation(LinearLayout.VERTICAL);
        l3params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        linearLayout3.setLayoutParams(l3params);


        summa = new TextView(root.getContext());
        summa.setText("   "+order_status+"   ");
        summa.setTextSize(18);
        summa.setBackgroundResource(R.drawable.border2);
        summap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summap.setMargins(0,45,40,10);
        summap.gravity = Gravity.END;
        summa.setLayoutParams(summap);
        linearLayout3.addView(summa);


        linearLayout1.addView(linearLayout3);


        gl.setBackgroundResource(R.drawable.border);
        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.th.switch_fragment(new Fragment_reys());
            }
        });
        scrollView.addView(gl);
    }

}