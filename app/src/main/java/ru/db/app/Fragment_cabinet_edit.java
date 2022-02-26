package ru.db.app;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_cabinet_edit extends Fragment {
    View root;
    EditText name,phone,city,email;
    static CircleImageView profileImageView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cabinet_edit, container, false);
        name = root.findViewById(R.id.name);
        phone=root.findViewById(R.id.phone);
        city = root.findViewById(R.id.city);
        email = root.findViewById(R.id.email);
        name.setText(my.name);
        phone.setText("+"+my.phone);
        city.setText(my.city);
        email.setText(my.email);
        profileImageView=root.findViewById(R.id.avatar);
        root.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.th);
                progressDialog.setTitle("Инфо");
                progressDialog.setMessage("Пожалуйста, подождите, пока мы обновляем ваши данные.");
                progressDialog.show();


                my.name = name.getText().toString();
                my.city = city.getText().toString();
                my.email = email.getText().toString();

                DocumentReference user = my.db.collection("users").document(my.id);
                user.update("name",my.name);
                user.update("city",my.city);
                user.update("e-mail",my.email);

                if(my.result!=null){
                    StorageReference storageRef = my.fm.getReference();
                    StorageReference mountainsRef = storageRef.child("avatars/"+my.id+".jpg");
                    UploadTask uploadTask = mountainsRef.putFile(my.result.getUri());
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.cancel();
                            AlertDialog alertDialog = (new AlertDialog.Builder(MainActivity.th)).create();
                            alertDialog.setTitle("Инфо");
                            alertDialog.setMessage("Данные успешно сохранены.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                }
                else {
                    progressDialog.cancel();
                    AlertDialog alertDialog = (new AlertDialog.Builder(MainActivity.th)).create();
                    alertDialog.setTitle("Инфо");
                    alertDialog.setMessage("Данные успешно сохранены.");
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
        //if(my.avatar!=null)
        //    profileImageView.setImageBitmap(my.avatar);
        Glide.with(root.getContext())
                .load(my.fm.getReferenceFromUrl("gs://test-535c2.appspot.com/avatars/"+my.id+".jpg"))
                .into(profileImageView);
        return root;
    }


}