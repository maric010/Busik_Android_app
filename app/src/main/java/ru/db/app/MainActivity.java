package ru.db.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.LruCache;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //region cache

    //region cache end
    SharedPreferences settings;
    static MainActivity th;
    private static final String PREFS_FILE = "Account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        th=this;



        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        if(my.status.equalsIgnoreCase("Пасажир"))
            switch_fragment(new Fragment_orders());
        else
            switch_fragment(new Fragment_orders_carrier());


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        my.dbmessages.child(my.id).addChildEventListener(childEventListener);

    }
    void switch_fragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    public void fragment_message_onClick(View view) {
        switch_fragment(new Fragment_message());
    }

    public void fragment_orders_onClick(View view) {
        if(my.status.equalsIgnoreCase("Пасажир"))
            switch_fragment(new Fragment_orders());
        else
            switch_fragment(new Fragment_orders_carrier());
    }

    public void activity_add_order(View view) {
        Intent intent = new Intent(MainActivity.this, add_order.class);
        startActivity(intent);
    }

    public void logout_onClick(View view) {
        final String PREF_id = "id";
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(PREF_id,"");
        prefEditor.apply();
        my.id="";
        my.name="";
        my.phone="";
        my.city="";
        my.status="";
        Intent intent = new Intent(MainActivity.this, auth.class);
        startActivity(intent);
        finish();

    }

    public void cabinet_onClick(View view) {
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
        switch_fragment(new Fragment_cabinet_edit());
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

    }
}