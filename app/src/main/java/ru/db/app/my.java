package ru.db.app;

import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;

public class my {
    static SharedPreferences settings;
    static String name="",phone,email,id,city,status;

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference dborders = database.getReference("рейсы");

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    static void reg(String name,String password,String phone,String mail,String city,
               boolean is_admin,boolean is_carrier,boolean is_passenger,boolean auth_google,boolean auth_facebook,
               boolean auth_telegram,int facebook_id,int telegram_id,int google_id){
        try {
            HashMap<String,Object> new_user=new HashMap<>();
            new_user.put("name",name);
            new_user.put("password",SHA1(SHA1(password)));
            new_user.put("phone",phone);
            new_user.put("city",city);
            if(is_admin)
                new_user.put("is_admin",true);
            if(is_carrier)
                new_user.put("is_carrier",true);
            if(is_passenger)
                new_user.put("is_passenger",true);
            if(auth_facebook)
                new_user.put("facebook_id",facebook_id);
            if(auth_google)
            {
                new_user.put("e-mail",mail);
                new_user.put("google_id",google_id);
            }

            if(auth_telegram)
                new_user.put("telegram_id",telegram_id);
            DocumentReference user = my.db.collection("users").document();
            user.set(new_user);
            final String PREF_id = "id";
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.putString(PREF_id,user.getId());
            prefEditor.apply();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    static void reg_order(String otkuda,String kuda,String start_date,String stop_date,String description,
                          String is_passenger,String passenger_cost,String gruz_cost,String is_gruz){
            HashMap<String,Object> new_order=new HashMap<>();
            new_order.put("otkuda",otkuda);
            new_order.put("kuda",kuda);
            new_order.put("start_date",start_date);
            new_order.put("stop_date",stop_date);
            new_order.put("description",description);
            new_order.put("is_passenger",is_passenger);
            new_order.put("passenger_cost",passenger_cost);
            new_order.put("gruz_cost",gruz_cost);
            new_order.put("is_gruz",is_gruz);
            dborders.push().setValue(new_order);
    }


    private void getListItems(String phone) {
        DocumentReference docRef = db.collection("phone").document(phone);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                    } else {
                        // Not found
                    }
                } else {
                    // Can'nt find
                }
            }
        });
    }

}
