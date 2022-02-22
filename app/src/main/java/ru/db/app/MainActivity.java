package ru.db.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;

    private static final String PREFS_FILE = "Account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        if(my.status.equalsIgnoreCase("Пасажир"))
            switch_fragment(new Fragment_orders());
        else
            switch_fragment(new Fragment_orders_carrier());



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
}