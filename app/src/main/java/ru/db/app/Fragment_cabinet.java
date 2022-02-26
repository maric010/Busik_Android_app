package ru.db.app;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_cabinet extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.user_cabinet, container, false);
        ((TextView)root.findViewById(R.id.name)).setText(my.name);
        ((TextView)root.findViewById(R.id.status)).setText(my.status);
        ((TextView)root.findViewById(R.id.city)).setText(my.city);
/*
        my.fm.getReference().child("avatars/"+my.id+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(root.getContext())
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into((CircleImageView)root.findViewById(R.id.ellipse_1));
                System.out.println("uri "+uri.getEncodedPath());
            }
        });


 */

        Glide.with(root.getContext())
                .load(my.fm.getReference().child("avatars/"+my.id))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into((CircleImageView)root.findViewById(R.id.ellipse_1));





        //if(my.avatar!=null)
        //    ((CircleImageView)root.findViewById(R.id.ellipse_1)).setImageBitmap(my.avatar);
        return root;
    }
}