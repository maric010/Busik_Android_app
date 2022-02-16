package ru.db.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.annotations.Nullable;

import java.util.HashMap;

public class start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        my.dborders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //System.out.println(previousChildName);
                System.out.println(snapshot.child("adress_start").getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HashMap<String,String> t=new HashMap<>();
        t.put("adress_start","msk");
        t.put("adress_stop","spb");
        t.put("cost","500");
        t.put("cost_baggage","100");
        t.put("description","data");
        t.put("date_start","Mon");
        t.put("date_stop","Thu");
        t.put("status","wait");
        my.dborders.push().setValue(t);

    }
}