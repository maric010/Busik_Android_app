package ru.db.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static int fr;
    static MainActivity th;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        th=this;
        if(my.status.equalsIgnoreCase("Перевозчик")){
            fr=0;
            switch_fragment(new Fragment_orders_carrier());
        }

        else if(my.status.equalsIgnoreCase("Пасажир") || my.status.equalsIgnoreCase("Гость"))
        {
            fr=0;
            switch_fragment(new Fragment_orders());
        }

        if(my.status.equals("Перевозчик") || my.status.equals("Пасажир")){
            my.Messages_Listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    HashMap<String, String> doc = (HashMap<String, String>) dataSnapshot.getValue();
                    my.Messages.put(dataSnapshot.getKey(),doc);
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    HashMap<String, String> doc = (HashMap<String, String>) dataSnapshot.getValue();
                    my.Messages.replace(dataSnapshot.getKey(),doc);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    my.Messages.remove(dataSnapshot.getKey());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            DatabaseReference field = my.dbmessages.child(my.id);
            field.addChildEventListener(my.Messages_Listener);

            my.Messages_Listener2 = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    HashMap<String, String> doc = (HashMap<String, String>) dataSnapshot.getValue();
                    my.Messages2.put(dataSnapshot.getKey(),doc);
                    if(Fragment_support.scroll!=null){
                        Map.Entry<String, HashMap> entry = new Map.Entry<String, HashMap>() {
                            @Override
                            public String getKey() {
                                return dataSnapshot.getKey();
                            }

                            @Override
                            public HashMap getValue() {
                                return doc;
                            }

                            @Override
                            public HashMap setValue(HashMap hashMap) {
                                return null;
                            }
                        };
                        Fragment_support.add_message(entry);
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    HashMap<String, String> doc = (HashMap<String, String>) dataSnapshot.getValue();
                    my.Messages2.replace(dataSnapshot.getKey(),doc);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    my.Messages2.remove(dataSnapshot.getKey());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            DatabaseReference field2 = my.dbmessages2.child(my.id);
            field2.addChildEventListener(my.Messages_Listener2);

        }

    }
    void switch_fragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    public void fragment_message_onClick(View view) {
        if(my.status.equals("Гость"))
            switch_fragment(new Fragment_messages_guest());
        else
            switch_fragment(new Fragment_message());
    }

    public void fragment_orders_onClick(View view) {
        if(my.status.equalsIgnoreCase("Пасажир") || my.status.equals("Гость"))
            switch_fragment(new Fragment_orders());
        else
            switch_fragment(new Fragment_orders_carrier());
    }

    public void activity_add_order(View view) {
        Intent intent = new Intent(MainActivity.this, add_order.class);
        startActivity(intent);
    }

    public void logout_onClick(View view) {
        my.mAuth.signOut();
        my.logout();
        Intent intent = new Intent(MainActivity.this, auth.class);
        startActivity(intent);
        finish();
    }

    public void cabinet_onClick(View view) {
        if(my.status.equals("Перевозчик"))
            fr=4;
        else
            fr=2;
        if(my.status.equals("Гость"))
            switch_fragment(new Fragment_cabinet_guest());
        else
            switch_fragment(new Fragment_cabinet());
    }

    public void my(View view) {
        ((View)Fragment_orders.root.findViewById(R.id.my_trips)).setVisibility(View.VISIBLE);
        ((View)Fragment_orders.root.findViewById(R.id.all_trips)).setVisibility(View.INVISIBLE);
        ((TextView)Fragment_orders.root.findViewById(R.id.all_aviable_trips)).setVisibility(View.INVISIBLE);
        Fragment_orders.scrollView.removeAllViewsInLayout();
        TextView summa = new TextView(Fragment_orders.root.getContext());
        LinearLayout.LayoutParams summap = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summap.gravity = Gravity.CENTER;
        summap.setMargins(0,10,0,10);
        summa.setText("Активные поездки");
        summa.setTextSize(22);
        summa.setTextColor(Color.BLACK);
        summa.setLayoutParams(summap);
        Fragment_orders.scrollView.addView(summa);

        for(Map.Entry<String, HashMap> entry : my.Orders.entrySet()) {
            Object accepted = entry.getValue().get("passengers_accepted");
            if(accepted!=null){
                if(((HashMap)accepted).containsKey(my.id))
                    Fragment_orders.add_order(entry);
            }

        }
        for(Map.Entry<String, HashMap> entry : my.Orders.entrySet()) {
            Object accepted = entry.getValue().get("passengers_request");
            if(accepted!=null)
                if(((HashMap)accepted).containsKey(my.id))
                    Fragment_orders.add_order(entry);
        }
        summa = new TextView(Fragment_orders.root.getContext());
        summap = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        summap.gravity = Gravity.CENTER;
        summap.setMargins(0,10,0,10);
        summa.setText("Архивные поездки");
        summa.setTextSize(22);
        summa.setTextColor(Color.BLACK);
        summa.setLayoutParams(summap);
        Fragment_orders.scrollView.addView(summa);
        for(Map.Entry<String, HashMap> entry : my.Orders_arxiv.entrySet()) {
            Object accepted = entry.getValue().get("passengers_accepted");
            if(accepted!=null)
                if(((HashMap)accepted).containsKey(my.id))
                    Fragment_orders.add_order(entry);
        }
        //arxiv


    }

    public void all(View view) {
        Fragment_orders.scrollView.removeAllViews();
        ((View)Fragment_orders.root.findViewById(R.id.my_trips)).setVisibility(View.INVISIBLE);
        ((View)Fragment_orders.root.findViewById(R.id.all_trips)).setVisibility(View.VISIBLE);
        ((TextView)Fragment_orders.root.findViewById(R.id.all_aviable_trips)).setVisibility(View.VISIBLE);


        for(Map.Entry<String, HashMap> entry : my.Orders.entrySet()) {
            Fragment_orders.add_order(entry);
        }
    }

    public void back_to_orders(View view) {
        fr=0;
        switch_fragment(new Fragment_orders());
    }

    public void back_to_orders_carrier(View view) {
        fr=0;
        switch_fragment(new Fragment_orders_carrier());
    }

    public void change_order_onClick(View view) {

        Intent intent = new Intent(MainActivity.this, edit_order.class);
        startActivity(intent);
    }

    public void profile_edit(View view) {
        fr=1;
        switch_fragment(new Fragment_cabinet_edit());
    }

    @Override
    public void onBackPressed() {
        switch(fr) {
            case 0:
                super.onBackPressed();
                break;
            case 1:
                if(my.status.equals("Перевозчик"))
                    fr=4;
                else
                    fr=2;
                switch_fragment(new Fragment_cabinet());
                break;
            case 2:
                fr=0;
                switch_fragment(new Fragment_orders());
                break;
            case 3:
                fr=2;
                switch_fragment(new Fragment_reys());
                break;
            case 4:
                fr=0;
                switch_fragment(new Fragment_orders_carrier());
                break;
        }
    }

    public void set_image_onClick(View view) {
        CropImage.activity().setAspectRatio(1,1).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            my.result = CropImage.getActivityResult(data);
            Glide.with(Fragment_cabinet_edit.root.getContext())
                    .load(my.result.getUri())
                    .error(R.drawable.ellipse_1)
                    .into(Fragment_cabinet_edit.profileImageView);

        }
    }

    public void back_to_cabinet(View view) {
        switch_fragment(new Fragment_cabinet());
    }

    public void apply(View view) {
        fr = 3;
        switch_fragment(new Fragment_reys_request());
    }

    public void finish(View view) {
        finish();
    }

    public void back_to_order(View view) {
        if(my.status.equals("Пасажир"))
            switch_fragment(new Fragment_reys());
        if(my.status.equals("Перевозчик"))
            switch_fragment(new Fragment_reys_carrier());
    }

    public void reys_passengers_onClick(View view) {
        switch_fragment(new Fragment_reys_passengers());
    }

    public void finish_order(View view) {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.th);
        progressDialog.setTitle("Завершение рейса");
        progressDialog.setMessage("Пожалуйста, подождите.");
        progressDialog.show();
        my.finish_order();

        AlertDialog alertDialog = (new AlertDialog.Builder(MainActivity.th)).create();
        alertDialog.setTitle("Инфо");
        alertDialog.setMessage("Рейс успешно завершен.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        progressDialog.cancel();
        alertDialog.show();

        switch_fragment(new Fragment_orders_carrier());
    }

    public void cancel_order(View view) {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.th);
        progressDialog.setTitle("Отменение рейса");
        progressDialog.setMessage("Пожалуйста, подождите.");
        progressDialog.show();
        my.cancel_order();

        AlertDialog alertDialog = (new AlertDialog.Builder(MainActivity.th)).create();
        alertDialog.setTitle("Инфо");
        alertDialog.setMessage("Рейс успешно отменен.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        progressDialog.cancel();
        alertDialog.show();
        switch_fragment(new Fragment_orders_carrier());
    }

    public void fragment_rates_onClick(View view) {
        fr=1;
        switch_fragment(new Fragment_rates());
    }

    public void activity_search_onClick(View view) {
        Intent intent = new Intent(MainActivity.this, search.class);
        startActivity(intent);
    }

    public void back_to_messages(View view) {
        switch_fragment(new Fragment_message());
    }

    public void support_onClick(View view) {
        fr=1;
        switch_fragment(new Fragment_support());
    }

    public void search_disable(View view) {
        my.is_search=false;
        switch_fragment(new Fragment_orders());
        Fragment_orders.root.findViewById(R.id.search_layout).setVisibility(View.INVISIBLE);
        Fragment_orders.root.findViewById(R.id.all_trips_layout).setVisibility(View.VISIBLE);
        Fragment_orders.root.findViewById(R.id.my_trips_layout).setVisibility(View.VISIBLE);
    }
}