package ru.db.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
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
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        HashMap<String,String> t=new HashMap<>();
        t.put("adress_start","msk0");
        t.put("adress_stop","spb");
        t.put("cost","500");
        t.put("cost_baggage","100");
        t.put("description","data");
        t.put("date_start","Mon");
        t.put("date_stop","Thu");
        t.put("status","wait");
        //my.dborders.push().setValue(t);
        //String id = my.reg("Марик","test","79967855023","smaricpb@gmail.com","Россия","Спб",false,
          //      false,true,false,false,false,0,0,0);
        //System.out.println(id);
        String id = settings.getString(PREF_id,"");
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
                            my.name = (String) doc.get("name");
                            my.city=(String)doc.get("city");
                            my.email= (String)doc.get("e-mail");
                            my.phone=(String)doc.get("phone");
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
                            else

                            System.out.println(my.name);
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