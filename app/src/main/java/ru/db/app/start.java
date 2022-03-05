package ru.db.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class start extends AppCompatActivity {
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;
    private static final String PREFS_FILE = "Account";
    private static final String PREF_id = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

                    ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                            HashMap<String, String> h = (HashMap<String, String>) dataSnapshot.getValue();
                            my.Orders.put(dataSnapshot.getKey(),h);

                            Map.Entry<String, HashMap> entry = new Map.Entry<String, HashMap>() {
                                @Override
                                public String getKey() {
                                    return dataSnapshot.getKey();
                                }

                                @Override
                                public HashMap getValue() {
                                    return h;
                                }

                                @Override
                                public HashMap setValue(HashMap hashMap) {
                                    return null;
                                }
                            };
                            my.sort_orders();
                            if(Fragment_orders.root!=null){
                                Fragment_orders.add_order(entry);
                            }
                            else if(Fragment_orders_carrier.root!=null && !my.is_arxiv){

                                if(h.get("owner").toString().equalsIgnoreCase(my.id))
                                    Fragment_orders_carrier.add_carrier_order(entry);
                            }

                        }

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                            HashMap<String, String> doc = (HashMap<String, String>) dataSnapshot.getValue();
                            my.Orders.replace(dataSnapshot.getKey(),doc);
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            my.Orders.remove(dataSnapshot.getKey());
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    my.dborders.addChildEventListener(childEventListener);

        if(my.mAuth.getCurrentUser()!=null){
            my.db.collection("users").document(my.mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> doc = document.getData();
                            my.id = document.getId();
                            my.name = (String) doc.get("name");
                            my.city=(String)doc.get("city");
                            my.email= (String)doc.get("e-mail");

                            my.phone=(String)doc.get("phone");
                            if(doc.get("avatar")!=null)
                                my.avatar=doc.get("avatar").toString();
                            if(doc.get("is_carrier")!=null)
                            {
                                if(((Boolean) doc.get("is_carrier")))
                                    my.status="Перевозчик";
                            }
                            else if(doc.get("is_passenger")!=null){
                                if(((Boolean) doc.get("is_passenger")))
                                    my.status="Пасажир";
                            }
                            else if(doc.get("is_admin")!=null){
                                if(((Boolean) doc.get("is_admin")))
                                    my.status="Администратор";
                            }
                            if(my.status.equals("Перевозчик"))
                                my.get_arxiv_carrier();
                            Intent intent = new Intent(start.this, MainActivity.class);
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    my.get_country();
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);

                        }
                        else{
                          //auth
                            Intent intent = new Intent(start.this, auth.class);
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    my.get_country();
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);

                        }
                    } else {

                    }
                }
            });
        }
        else{
            Intent intent = new Intent(start.this, auth.class);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    my.get_country();
                    startActivity(intent);
                    finish();
                }
            }, 1000);

        }
    }

}