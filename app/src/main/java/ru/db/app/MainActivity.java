package ru.db.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
    //region cache

    //region cache end
    static int fr;
    SharedPreferences settings;
    static MainActivity th;
    private static final String PREFS_FILE = "Account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        th=this;
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
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
        my.logout();
        Intent intent = new Intent(MainActivity.this, auth.class);
        startActivity(intent);
        finish();

    }

    public void cabinet_onClick(View view) {
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
    }

    public void all(View view) {
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
                fr=0;
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

    public void profile_edit_save_onClick(View view) {


    }

    public void set_image_onClick(View view) {
        CropImage.activity().setAspectRatio(1,1).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            my.result = CropImage.getActivityResult(data);
            System.out.println("PPP1");
            //Fragment_cabinet_edit.profileImageView.setImageURI(my.result.getUri());
            System.out.println("PPP2");


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
        MainActivity.fr = 3;
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
        switch_fragment(new Fragment_rates());
    }

    public void activity_search_onClick(View view) {
        Intent intent = new Intent(MainActivity.this, search.class);
        startActivity(intent);
    }
}