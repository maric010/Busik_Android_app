package ru.db.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class auth extends AppCompatActivity {
    private static final String TAG = "xz";
    private static final int RC_SIGN_IN = 1;
    CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    static auth th;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        th=this;
        FacebookSdk.setApplicationId("468922064606903");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_auth);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//fb();

    }
void fb(){
    mCallbackManager = CallbackManager.Factory.create();
    LoginButton loginButton = findViewById(R.id.button_sign_in);
    loginButton.setReadPermissions("public_profile");
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
        }
    });
    /*
    loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "facebook:onSuccess:" + loginResult);
            handleFacebookAccessToken(loginResult.getAccessToken());
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "facebook:onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d(TAG, "facebook:onError", error);
        }
    });
*/
    }
    public void reg_onclick(View view) {
        Intent intent = new Intent(auth.this, reg.class);
        startActivity(intent);
    }

    public void auth_onClick(View view) {
        Intent intent = new Intent(auth.this, auth_main.class);
        startActivity(intent);
    }
    private FirebaseAuth mAuth;

    public void fbLogin(View view)
    {
        // Before Edit:

         //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));


        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
        //LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        /*
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel()
                    {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {
                        // App code
                    }
                });
        */

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(auth.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
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

                CollectionReference docRef = my.db.collection("users");

                    docRef.whereEqualTo("google_id",my.google_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(queryDocumentSnapshots.getDocuments().size()>0){
                                        DocumentSnapshot user = queryDocumentSnapshots.getDocuments().get(0);
                                        final String PREF_id = "id";
                                        SharedPreferences.Editor prefEditor = my.settings.edit();
                                        prefEditor.putString(PREF_id,user.getId());
                                        prefEditor.apply();

                                        my.name=user.get("name").toString();
                                        my.phone=user.get("phone").toString();
                                        my.city=user.get("city").toString();
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
    }



}