package ru.db.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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

        my.settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        String id = settings.getString(PREF_id,"");

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
                else if(Fragment_orders_carrier.root!=null){
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






        //id = "gqxbnYBJI9mijfK1yOpA";

        if(!id.equalsIgnoreCase("")){
            DocumentReference docRef = my.db.collection("users").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                            //my.download_my_avatar();
                            Intent intent = new Intent(start.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                        }
                        else{
                          //auth
                            Intent intent = new Intent(start.this, auth.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {

                    }
                }
            });
        }
        else{
            Intent intent = new Intent(start.this, auth.class);
            startActivity(intent);
            finish();
        }







    }

}