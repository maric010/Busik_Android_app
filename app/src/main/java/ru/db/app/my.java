package ru.db.app;

import android.content.SharedPreferences;
import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class my {
    public static Map.Entry<String, HashMap> current_order;
    static SharedPreferences settings;
    static String name="",phone,email="",id,city,status,google_id,telegram_id,facebook_id;
    static float rate=0.0f;
    static Boolean auth_google=false;
    static Boolean auth_facebook=false;
    static Boolean auth_telegram=false;
    static CropImage.ActivityResult result;
    static String avatar;

    private static final String PREFS_FILE = "Account";
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference dborders = database.getReference("рейсы");

    static DatabaseReference dbmessages = database.getReference("сообщения");
    static FirebaseStorage fm = FirebaseStorage.getInstance();


    static HashMap<String, HashMap> Orders = new HashMap<String, HashMap>();

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

    static void reg(String name,String password,String phone,String city,
               boolean is_admin,boolean is_carrier,boolean is_passenger){
        try {
            HashMap<String,Object> new_user=new HashMap<>();
            new_user.put("name",name);

            new_user.put("phone",phone);
            new_user.put("city",city);
            if(is_admin)
                new_user.put("is_admin",true);
            if(is_carrier)
                new_user.put("is_carrier",true);
            if(is_passenger)
                new_user.put("is_passenger",true);

            if(auth_google)
            {
                new_user.put("e-mail",email);
                new_user.put("google_id",google_id);
            }
            else if(auth_facebook)
                new_user.put("facebook_id",facebook_id);
            else
                new_user.put("password",SHA1(SHA1(password)));

            if(auth_telegram)
                new_user.put("telegram_id",telegram_id);
            DocumentReference user = my.db.collection("users").document();
            user.set(new_user);
            my.id = user.getId();
            System.out.println(my.id+";"+user.getId());
            if(is_carrier)
            {
                my.status="Перевозчик";
            }
            else if(is_passenger){
                my.status="Пасажир";
            }
            else if(is_admin){
                my.status="Администратор";
            }

            final String PREF_id = "id";
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.putString(PREF_id,my.id);
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
            new_order.put("owner",my.id);
            new_order.put("owner_rate",my.rate);
            new_order.put("owner_name",my.name);
            new_order.put("owner_avatar",my.avatar);

        new_order.put("status","В ожидании");


        dborders.push().setValue(new_order);
    }
    static void change_order(String otkuda,String kuda,String start_date,String stop_date,String description,
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
        new_order.put("owner",my.id);
        new_order.put("owner_rate",my.rate);
        new_order.put("owner_name",my.name);
        new_order.put("owner_avatar",my.avatar);
        new_order.put("status","В ожидании");
        dborders.child(current_order.getKey()).setValue(new_order);
    }
static String[] get_week(){
    return new String[]{"Вс.", "Пн.", "Вт.", "Ср.", "Чт.", "Пт.","Сб."};
}

    static String[] getMonths() {
        return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    }

    public static String gen_avatar(String iid) {
        return  "https://firebasestorage.googleapis.com/v0/b/test-535c2.appspot.com/o/avatars%2F"+iid+".jpg?alt=media";
    }

static void fill_fragment(View root){
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    try {
        Date date = sdf.parse(my.current_order.getValue().get("start_date").toString().split(" ")[0]);
        c.setTime(date);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    Calendar c2 = Calendar.getInstance();
    SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
    try {
        Date date = sdf2.parse(my.current_order.getValue().get("stop_date").toString().split(" ")[0]);
        c2.setTime(date);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    TextView week_day_month = root.findViewById(R.id.week_day_month);
    System.out.println(my.current_order.getValue().get("stop_date").toString());
    String month = my.getMonths()[Integer.valueOf(my.current_order.getValue().get("start_date").toString().split("\\.")[1])-1];
    week_day_month.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+c.get(Calendar.DAY_OF_MONTH-1)+" "+month);
    TextView description = root.findViewById(R.id.description);
    description.setText(my.current_order.getValue().get("description").toString());
    TextView start_country_city = root.findViewById(R.id.start_country_city);
    start_country_city.setText(my.current_order.getValue().get("otkuda").toString());
    TextView stop_country_city  = root.findViewById(R.id.stop_country_city);
    stop_country_city.setText(my.current_order.getValue().get("kuda").toString());
    TextView start_week_hour = root.findViewById(R.id.start_week_hour);
    start_week_hour.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+" "+my.current_order.getValue().get("start_date").toString().split(" ")[1]);
    TextView stop_week_hour = root.findViewById(R.id.stop_week_hour);
    stop_week_hour.setText(my.get_week()[c2.get(Calendar.DAY_OF_WEEK)-1]+" "+my.current_order.getValue().get("stop_date").toString().split(" ")[1]);
    TextView passenger_cost = root.findViewById(R.id.passenger_cost);
    passenger_cost.setText(my.current_order.getValue().get("passenger_cost").toString());
    TextView gruz_cost = root.findViewById(R.id.gruz_cost);
    gruz_cost.setText(my.current_order.getValue().get("gruz_cost").toString());
}

}



