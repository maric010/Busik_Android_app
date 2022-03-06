package ru.db.app;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_orders extends Fragment {
    static View root;
    static LinearLayout scrollView;
    static String odate="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_orders, container, false);
odate="";

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
            add_order(entry);
        }
        return root;
    }
    static boolean x=false;
    static void add_order(Map.Entry<String, HashMap> entry){



        HashMap h = entry.getValue();
        TextView summa;
        LinearLayout.LayoutParams summap;

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
System.out.println(order_date);
        if(!order_date.equals(odate)){
            odate=order_date;
            summa = new TextView(root.getContext());
            summap = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            summap.gravity = Gravity.CENTER;
            summap.setMargins(0,10,0,10);
            summa.setText(order_date);
            summa.setTextSize(20);
            summa.setTextColor(Color.DKGRAY);
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

        start_date_textview.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+" "+h.get("start_date").toString().split(" ")[1]);
        start_date_textview.setTextSize(15);
        start_date_textview.setTextColor(Color.WHITE);
        start_date_textview.setLayoutParams(starttextviewparams);
        linearLayout2.addView(start_date_textview);

        TextView stop_date_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams stoptextviewparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stop_date_textview.setText(my.get_week()[c2.get(Calendar.DAY_OF_WEEK)-1]+" "+h.get("stop_date").toString().split(" ")[1]);
        stop_date_textview.setTextSize(15);
        stop_date_textview.setTextColor(Color.WHITE);
        stop_date_textview.setLayoutParams(stoptextviewparams);
        linearLayout2.addView(stop_date_textview);

        linearLayout1.addView(linearLayout2);

        ImageView arrow = new ImageView(root.getContext());
        LinearLayout.LayoutParams arrowp = new LinearLayout.LayoutParams
                (70, 160);
        arrowp.setMargins(0,-10,0,0);
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
        start_adress_textview.setTextColor(Color.WHITE);
        start_adress_textview.setTextSize(15);
        start_adress_textview.setLayoutParams(startparams);
        linearLayout3.addView(start_adress_textview);

        TextView stop_adress_textview = new TextView(root.getContext());
        LinearLayout.LayoutParams stopparams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stop_adress_textview.setText(h.get("kuda").toString());
        stop_adress_textview.setTextSize(15);
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
        summa.setTextSize(15);
        summa.setTextColor(Color.WHITE);
        summa.setTypeface(null, Typeface.BOLD);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);

        summa = new TextView(root.getContext());
        summa.setText("/ чел");
        summa.setTextSize(15);
        summa.setTextColor(Color.WHITE);
        summa.setLayoutParams(summap);
        linearLayout1.addView(summa);


        if(!h.get("gruz_cost").toString().equals("0") && !h.get("gruz_cost").toString().equals("")){
            arrow = new ImageView(root.getContext());
            arrowp.setMargins(25,0,5,0);
            arrow.setImageResource(R.drawable.ic_luggage_svgrepo_com);
            arrow.setLayoutParams(arrowp);
            linearLayout1.addView(arrow);

            summa = new TextView(root.getContext());
            summa.setText("€ "+h.get("gruz_cost").toString()+" ");
            summa.setTextSize(15);
            summa.setTextColor(Color.WHITE);
            summa.setTypeface(null, Typeface.BOLD);
            summa.setLayoutParams(summap);
            linearLayout1.addView(summa);

            summa = new TextView(root.getContext());
            summa.setText("/ кг");
            summa.setTextColor(Color.WHITE);
            summa.setTextSize(15);
            summa.setLayoutParams(summap);
            linearLayout1.addView(summa);
        }






        gl.addView(linearLayout1);

        linearLayout1 = new LinearLayout(root.getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        l1params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l1params.setMargins(20,30,0,30);
        linearLayout1.setLayoutParams(l1params);

        gl.addView(linearLayout1);

        CircleImageView avatar = new CircleImageView(root.getContext());
        arrowp = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        arrowp.setMargins(20,0,10,0);
        avatar.setLayoutParams(arrowp);

        avatar.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        linearLayout1.addView(avatar);


        Drawable color = new ColorDrawable(Color.WHITE);
        Drawable image = MainActivity.th.getResources().getDrawable(R.drawable.ic_user_121);

        LayerDrawable ld = new LayerDrawable(new Drawable[]{color, image});


        if(h.get("owner_avatar")!=null){
            Glide.with(root.getContext())
                    .load(my.gen_avatar(h.get("owner_avatar").toString()))
                    .placeholder(R.drawable.ic_user_121)
                    .into(avatar);
        }else avatar.setImageDrawable(ld);



        linearLayout2 = new LinearLayout(root.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        l2params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout2.setLayoutParams(l2params);

        summa = new TextView(root.getContext());
        summa.setText(h.get("owner_name").toString().split(" ")[0]);
        summa.setTextSize(22);
        summa.setTextColor(Color.WHITE);
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
        arrow.setImageResource(R.drawable.ic_star_svgrepo_com__1_);
        arrow.setLayoutParams(arrowp);
        linearLayout3.addView(arrow);

        summa = new TextView(root.getContext());
        summa.setText(h.get("owner_rate").toString());
        summa.setTextSize(15);
        summa.setTextColor(Color.WHITE);
        summa.setLayoutParams(summap);
        linearLayout3.addView(summa);

        linearLayout2.addView(linearLayout3);
        linearLayout1.addView(linearLayout2);


        linearLayout3 = new LinearLayout(root.getContext());
        linearLayout3.setOrientation(LinearLayout.VERTICAL);
        l3params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        linearLayout3.setLayoutParams(l3params);


        summa = new TextView(root.getContext());
        summa.setText("   "+h.get("status").toString()+"   ");
        summa.setTextSize(18);
        summa.setTextColor(Color.WHITE);
        summa.setBackgroundResource(R.drawable.status_border);
        summap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summap.setMargins(0,45,40,0);
        summap.gravity = Gravity.END;
        summa.setLayoutParams(summap);
        linearLayout3.addView(summa);


        linearLayout1.addView(linearLayout3);


        gl.setBackgroundResource(R.drawable.border_for_reys);
        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fr=2;
                MainActivity.th.switch_fragment(new Fragment_reys());
                my.current_order = entry;
            }
        });
        scrollView.addView(gl);



    }

}