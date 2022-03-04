package ru.db.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class my {

    public static boolean is_arxiv=false,is_search=false;
    public static Uri google_photo;
    static ChildEventListener Messages_Listener;
    public static Map.Entry<String, HashMap> current_order;
    static SharedPreferences settings;
    static String name="",phone,email="",id,city,status,google_id,telegram_id,facebook_id;
    static float rate=0.0f;
    static Boolean auth_google=false;
    static Boolean auth_facebook=false;
    static Boolean auth_telegram=false;
    static CropImage.ActivityResult result;
    static String avatar;
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final String PREFS_FILE = "Account";
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference dborders = database.getReference("рейсы");

    static DatabaseReference dbmessages = database.getReference("сообщения");
    static FirebaseStorage fm = FirebaseStorage.getInstance();


    static LinkedHashMap<String, HashMap> Orders = new LinkedHashMap<String, HashMap>();
    static LinkedHashMap<String, HashMap> Orders_arxiv = new LinkedHashMap<String, HashMap>();
    static HashMap<String, HashMap> Messages = new HashMap<String, HashMap>();

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

            mAuth.createUserWithEmailAndPassword(my.email,"asd12345");

            /*
            if(auth_google){
                my.avatar=my.id+"_"+(System.currentTimeMillis());
                StorageReference storageRef = my.fm.getReference();
                StorageReference mountainsRef = storageRef.child("avatars/"+my.avatar+".jpg");
                InputStream stream = new FileInputStream(new File(String.valueOf(my.google_photo)));
                UploadTask uploadTask = mountainsRef.putFile(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        user.update("avatar",my.avatar);
                    }
                });
            }
            */
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    static void finish_order(){
        DocumentReference order = my.db.collection("orders").document(current_order.getKey());
        order.set(current_order.getValue());
        my.dborders.child(current_order.getKey()).removeValue();

        String start_date = my.current_order.getValue().get("start_date").toString().split(" ")[0];
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf.parse(start_date);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] split = start_date.split("\\.");
        HashMap<String,Object> new_message=new HashMap<>();
        new_message.put("title","Рейс завершен");
        new_message.put("text","Поставьте оценку вашему недавнему рейсу\n"+my.current_order.getValue().get("otkuda")+" -> "
                +my.current_order.getValue().get("kuda")+" "+split[0]+"."+split[1]+"("+my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+")");
        new_message.put("rate",my.current_order.getValue().get("owner"));

        if(my.current_order.getValue().get("passengers_accepted")!=null){
            HashMap<String, Object> v = (HashMap<String, Object>) (my.current_order.getValue().get("passengers_accepted"));

            for(String k : v.keySet()) {
                my.dbmessages.child(k).push().setValue(new_message);
            }
        }


    }
    static void cancel_order(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            my.current_order.getValue().replace("status","Отменен");
        }
        DocumentReference order = my.db.collection("orders").document(current_order.getKey());
        order.set(current_order.getValue());
        my.dborders.child(current_order.getKey()).removeValue();

        String start_date = my.current_order.getValue().get("start_date").toString().split(" ")[0];
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf.parse(start_date);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] split = start_date.split("\\.");
        HashMap<String,Object> new_message=new HashMap<>();
        new_message.put("title","Рейс отменен");
        new_message.put("text","Поставьте оценку вашему недавнему рейсу\n"+my.current_order.getValue().get("otkuda")+" -> "
                +my.current_order.getValue().get("kuda")+" "+split[0]+"."+split[1]+"("+my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+")");
        new_message.put("rate",my.current_order.getValue().get("owner"));

        if(my.current_order.getValue().get("passengers_accepted")!=null){
            HashMap<String, Object> v = (HashMap<String, Object>) (my.current_order.getValue().get("passengers_accepted"));

            for(String k : v.keySet()) {
                my.dbmessages.child(k).push().setValue(new_message);
            }
        }
    }
    static void reg_order(String otkuda,String kuda,String start_date,String stop_date,String description,
                          String is_passenger,String passenger_cost,String gruz_cost,String is_gruz,String max_peoples,String max_gruz){
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
            new_order.put("max_peoples",max_peoples);
            new_order.put("max_gruz",max_gruz);
            new_order.put("status","В ожидании");


        dborders.push().setValue(new_order);
    }
    static void change_order(String otkuda,String kuda,String start_date,String stop_date,String description,
                          String is_passenger,String passenger_cost,String gruz_cost,String is_gruz,String max_peoples,String max_gruz){
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
        new_order.put("max_peoples",max_peoples);
        new_order.put("max_gruz",max_gruz);
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
    public static String format (long ms) {// Преобразует количество миллисекунд в x дней x часов x минут x секунд x миллисекунд
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / dd;
        String strDay = day < 10 ? "0" + day : "" + day;
        return strDay ;
    }

static void fill_fragment(View root) {
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
    String month = my.getMonths()[Integer.valueOf(my.current_order.getValue().get("start_date").toString().split("\\.")[1])-1];
    week_day_month.setText(my.get_week()[c.get(Calendar.DAY_OF_WEEK)-1]+c.get(Calendar.DAY_OF_MONTH)+" "+month);
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
    gruz_cost.setText("€ "+my.current_order.getValue().get("gruz_cost").toString()+" ");
    if(current_order.getValue().get("gruz_cost").toString().equals("0")){
        gruz_cost.setVisibility(View.INVISIBLE);
        root.findViewById(R.id.imageView3).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.gruz_cost_2).setVisibility(View.INVISIBLE);
    }

    SimpleDateFormat sm=new SimpleDateFormat("dd.MM.yyyy HH:mm");
    Date date1 = null;
    try {
        date1 = sm.parse(my.current_order.getValue().get("start_date").toString());
    } catch (ParseException e) {
        e.printStackTrace();
    }
    Date date2 = null;
    try {
        date2 = sm.parse(my.current_order.getValue().get("stop_date").toString());
    } catch (ParseException e) {
        e.printStackTrace();
    }
    TextView v_puti = root.findViewById(R.id.textView8);
    if(v_puti!=null){
        long different = date2.getTime()-date1.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;

        String day_hour = "";
        if(elapsedDays!=0){
            day_hour+= elapsedDays;
            if(elapsedDays>1 && elapsedDays<5)
                day_hour+=" дня ";
            if(elapsedDays>1 && elapsedDays<5)
                day_hour+=" дней ";
            else
                day_hour+=" день ";
        }
        if(elapsedHours!=0){
            day_hour+= elapsedHours;
            if((elapsedHours>1 && elapsedHours<5) || elapsedHours>20)
                day_hour+=" часа";
            else if (elapsedHours>4)
                day_hour+=" часов";
            else
                day_hour+=" час";
        }
        v_puti.setText("В пути "+day_hour);
    }
if(current_order.getValue().get("status").toString().equals("Завершен") || current_order.getValue().get("status").toString().equals("Отменен")){
    if(my.status.equals("Перевозчик")){
        Button b1 = root.findViewById(R.id.button11);
        b1.setEnabled(false);
        b1.setBackgroundColor(Color.GRAY);
        b1.setBackgroundColor(Color.GRAY);
        root.findViewById(R.id.button12).setEnabled(false);
        root.findViewById(R.id.editText2).setEnabled(false);
    }
}





}
static void sort_orders(){
        LinkedHashMap<String, HashMap> sorted_Orders = new LinkedHashMap<>();
        int a = my.Orders.keySet().size();
        System.out.println("sizze "+a);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        for(int i=0;i<a;i++) {
            Date max_date = null;
            String max_date_key=null;
            HashMap max_date_value = null;
            for (Map.Entry<String, HashMap> entry : my.Orders.entrySet()) {
                try {
                    Date date = sdf.parse(entry.getValue().get("start_date").toString().split(" ")[0]);
                    if (max_date == null)
                    {
                        max_date = date;
                        max_date_key=entry.getKey();
                        max_date_value=entry.getValue();
                    }
                    else if (max_date.getTime() > date.getTime())
                    {
                        max_date = date;
                        max_date_key=entry.getKey();
                        max_date_value=entry.getValue();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            sorted_Orders.put(max_date_key,max_date_value);
            my.Orders.remove(max_date_key);
        }
        my.Orders=sorted_Orders;
    }
    static void logout(){
        DatabaseReference field = my.dbmessages.child(my.id);
        field.removeEventListener(my.Messages_Listener);
        Messages.clear();
        final String PREF_id = "id";
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(PREF_id,"");
        prefEditor.apply();
        my.id="";
        my.name="";
        my.phone="";
        my.city="";
        my.status="";

    }

static void effect(View button){
    button.setOnTouchListener(new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                    v.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.getBackground().clearColorFilter();
                    v.invalidate();
                    break;
                }
            }
            return false;
        }
    });
}
   static void get_arxiv_carrier(){
        Orders_arxiv.clear();
        CollectionReference docRef = my.db.collection("orders");
        docRef.whereEqualTo("owner",my.id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    List<DocumentSnapshot> orders = queryDocumentSnapshots.getDocuments();
                    for(int i=0;i<orders.size();i++){
                        DocumentSnapshot order = orders.get(i);
                        Orders_arxiv.put(order.getId(),(HashMap)order.getData());
                    }
                }
            }});
    }

    public static void search() {

    }
}



