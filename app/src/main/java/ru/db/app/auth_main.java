package ru.db.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class auth_main extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    Boolean is_carrier=false;
    static auth_main th;
    AccessToken accessToken;

    GoogleSignInClient mGoogleSignInClient;

    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        my.effect(findViewById(R.id.button5));
        my.effect(findViewById(R.id.button2));

        th=this;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        callbackManager = CallbackManager.Factory.create();
    }
    public void check_on_passenger(View view) {
        is_carrier=false;
        TextView passenger_text = findViewById(R.id.passenger_textview);
        passenger_text.setTextColor(Color.BLACK);
        View passenger = findViewById(R.id.passenger_check);
        passenger.setVisibility(View.VISIBLE);
        View carrier = findViewById(R.id.carrier_check);
        carrier.setVisibility(View.GONE);
        TextView carrier_text = findViewById(R.id.carrier_textview);
        carrier_text.setTextColor(Color.GRAY);
    }
    public void check_on_carrier(View view) {
        is_carrier=true;
        TextView passenger_text = findViewById(R.id.passenger_textview);
        passenger_text.setTextColor(Color.GRAY);
        View passenger = findViewById(R.id.passenger_check);
        passenger.setVisibility(View.GONE);
        View carrier = findViewById(R.id.carrier_check);
        carrier.setVisibility(View.VISIBLE);
        TextView carrier_text = findViewById(R.id.carrier_textview);
        carrier_text.setTextColor(Color.BLACK);
    }
    public void auth_onClick(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Авторизация");
        progressDialog.setMessage("Пожалуйста, подождите, пока мы проверяем.");
        progressDialog.show();
        String field = "is_passenger";
        if(is_carrier)
            field="is_carrier";

        CollectionReference docRef = my.db.collection("users");
        String finalField = field;
        my.mAuth.signInWithEmailAndPassword(((EditText)findViewById(R.id.phone)).getText().toString(),((EditText)findViewById(R.id.password)).getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  docRef.whereEqualTo("e-mail", ((EditText)findViewById(R.id.phone)).getText().toString())
                          .whereEqualTo(finalField,true).get()
                          .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                              @Override
                              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                  if(queryDocumentSnapshots.getDocuments().size()>0){
                                      DocumentSnapshot user = queryDocumentSnapshots.getDocuments().get(0);
                                      my.id=user.getId();
                                      my.name=user.get("name").toString();
                                      my.phone=user.get("phone").toString();
                                      my.city=user.get("city").toString();
                                      if(user.get("avatar")!=null)
                                          my.avatar=user.get("avatar").toString();
                                      if(user.get("is_carrier")!=null)
                                      {
                                          if(((Boolean) user.get("is_carrier")))
                                              my.status="Перевозчик";
                                      }
                                      else if(user.get("is_passenger")!=null){
                                          if(((Boolean) user.get("is_passenger")))
                                              my.status="Пасажир";
                                      }
                                      else if(user.get("is_admin")!=null){
                                          if(((Boolean) user.get("is_admin")))
                                              my.status="Администратор";
                                      }
                                      if(my.status.equals("Перевозчик"))
                                          my.get_arxiv_carrier();
                                      my.calculate_rate();
                                      progressDialog.cancel();
                                      Intent intent = new Intent(auth_main.this, MainActivity.class);
                                      startActivity(intent);
                                      finish();
                                  }
                                  else{
                                      progressDialog.cancel();
                                      AlertDialog alertDialog = (new AlertDialog.Builder(auth_main.th)).create();
                                      alertDialog.setTitle("Ошибка");
                                      alertDialog.setMessage("Неправильный логин или пароль!");
                                      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                                              new DialogInterface.OnClickListener() {
                                                  public void onClick(DialogInterface dialog, int which) {
                                                      alertDialog.dismiss();
                                                  }
                                              });
                                      alertDialog.show();
                                  }

                              }
                          });
              }
              else {
                  progressDialog.cancel();
                  AlertDialog alertDialog = (new AlertDialog.Builder(auth_main.th)).create();
                  alertDialog.setTitle("Ошибка");
                  alertDialog.setMessage("Неправильный логин или пароль!");
                  alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                          new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                                  alertDialog.dismiss();
                              }
                          });
                  alertDialog.show();
              }
            }
        });

    }

    public void back(View view) {
        finish();
    }

    public void reg_onclick(View view) {
        Intent intent = new Intent(auth_main.this, reg.class);
        startActivity(intent);
    }

    public void reset_password_onClick(View view) {
        Intent intent = new Intent(auth_main.this, reset_password.class);
        startActivity(intent);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                my.google_id=account.getId();
                my.name=account.getDisplayName();
                my.phone="";
                my.email=account.getEmail();
                my.city="";
                my.status="";
                my.auth_google=true;
                my.google_photo=account.getPhotoUrl();
                firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {
            }
        }
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void auth_google_onClick(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void auth_facebook_onClick(View view) {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if(isLoggedIn){
                    System.out.println("Авторизация с фейсбук успешно");
                    handleFacebookAccessToken(accessToken);

                }
                System.out.println(accessToken.getUserId());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        my.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            CollectionReference docRef = my.db.collection("users");
                            docRef.document(my.mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot user) {
                                    if(user.exists()){
                                        my.id=user.getId();
                                        my.name=user.get("name").toString();
                                        my.phone=user.get("phone").toString();
                                        my.city=user.get("city").toString();
                                        if(user.get("avatar")!=null)
                                            my.avatar=user.get("avatar").toString();
                                        if(user.get("is_carrier")!=null)
                                        {
                                            if(((Boolean) user.get("is_carrier")))
                                                my.status="Перевозчик";
                                        }
                                        else if(user.get("is_passenger")!=null){
                                            if(((Boolean) user.get("is_passenger")))
                                                my.status="Пасажир";
                                        }
                                        else if(user.get("is_admin")!=null){
                                            if(((Boolean) user.get("is_admin")))
                                                my.status="Администратор";
                                        }
                                        if(my.status.equals("Перевозчик"))
                                            my.get_arxiv_carrier();
                                        my.calculate_rate();
                                        Intent intent = new Intent(auth_main.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(auth_main.this, reg.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else {
                        }
                    }
                });

    }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        my.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GraphRequest request =GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {
                                        my.facebook_id=object.getString("id");
                                        my.name=object.getString("first_name")+object.getString("last_name");
                                        my.phone="";
                                        my.email=object.getString("email");
                                        my.city="";
                                        my.status="";
                                        my.auth_facebook=true;

                                        CollectionReference docRef = my.db.collection("users");
                                        docRef.document(my.mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot user) {
                                                if(user.exists()){
                                                    my.id=user.getId();
                                                    my.name=user.get("name").toString();
                                                    my.phone=user.get("phone").toString();
                                                    my.city=user.get("city").toString();
                                                    if(user.get("avatar")!=null)
                                                        my.avatar=user.get("avatar").toString();
                                                    if(user.get("is_carrier")!=null)
                                                    {
                                                        if(((Boolean) user.get("is_carrier")))
                                                            my.status="Перевозчик";
                                                    }
                                                    else if(user.get("is_passenger")!=null){
                                                        if(((Boolean) user.get("is_passenger")))
                                                            my.status="Пасажир";
                                                    }
                                                    else if(user.get("is_admin")!=null){
                                                        if(((Boolean) user.get("is_admin")))
                                                            my.status="Администратор";
                                                    }
                                                    if(my.status.equals("Перевозчик"))
                                                        my.get_arxiv_carrier();
                                                    my.calculate_rate();
                                                    Intent intent = new Intent(auth_main.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else {
                                                    Intent intent = new Intent(auth_main.this, reg.class);
                                                    startActivity(intent);
                                                }

                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,email,first_name,last_name");
                            request.setParameters(parameters);
                            request.executeAsync();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(auth_main.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}