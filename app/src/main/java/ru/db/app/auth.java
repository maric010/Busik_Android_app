package ru.db.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class auth extends AppCompatActivity {
    private static final String TAG = "xz";
    private static final int RC_SIGN_IN = 1;

    GoogleSignInClient mGoogleSignInClient;
    static auth th;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        th=this;
        setContentView(R.layout.activity_auth);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        callbackManager = CallbackManager.Factory.create();





    }



    public void reg_onclick(View view) {
        Intent intent = new Intent(auth.this, reg.class);
        startActivity(intent);
    }

    public void auth_onClick(View view) {
        view.startAnimation(new AlphaAnimation(1F, 0.8F));
        Intent intent = new Intent(auth.this, auth_main.class);
        startActivity(intent);

    }
    private FirebaseAuth mAuth;


    public void auth_google_onClick(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                my.google_id=account.getId();
                System.out.println("GOOGLE ID "+account.getId());
                my.name=account.getDisplayName();
                my.phone="";
                my.email=account.getEmail();
                my.city="";
                my.status="";
                my.auth_google=true;
                my.google_photo=account.getPhotoUrl();
                CollectionReference docRef = my.db.collection("users");

                    docRef.whereEqualTo("google_id",my.google_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(queryDocumentSnapshots.getDocuments().size()>0){
                                        DocumentSnapshot user = queryDocumentSnapshots.getDocuments().get(0);
                                        my.id=user.getId();
                                        final String PREF_id = "id";
                                        SharedPreferences.Editor prefEditor = my.settings.edit();
                                        prefEditor.putString(PREF_id,user.getId());
                                        prefEditor.apply();

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
                                        System.out.println("NAME "+my.name);
                                        Intent intent = new Intent(auth.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(auth.this, reg.class);
                                        startActivity(intent);
                                    }

                                }
                            });



            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void guest(View view) {
        my.status="Гость";
        Intent intent = new Intent(auth.this, MainActivity.class);
        startActivity(intent);

    }

    public void auth_facebook_onClick(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if(isLoggedIn){
                    System.out.println("Авторизация с фейсбук успешно");
                    GraphRequest request =GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                System.out.println();

                                my.facebook_id=object.getString("id");
                                my.name=object.getString("first_name")+object.getString("last_name");
                                my.phone="";
                                my.email=object.getString("email");
                                my.city="";
                                my.status="";
                                my.auth_facebook=true;
                                CollectionReference docRef = my.db.collection("users");
                                docRef.whereEqualTo("facebook_id",my.facebook_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots.getDocuments().size()>0){
                                            DocumentSnapshot user = queryDocumentSnapshots.getDocuments().get(0);
                                            my.id=user.getId();
                                            final String PREF_id = "id";
                                            SharedPreferences.Editor prefEditor = my.settings.edit();
                                            prefEditor.putString(PREF_id,user.getId());
                                            prefEditor.apply();

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
                                            System.out.println("NAME "+my.name);
                                            Intent intent = new Intent(auth.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Intent intent = new Intent(auth.this, reg.class);
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
}