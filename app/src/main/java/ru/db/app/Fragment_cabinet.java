package ru.db.app;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_cabinet extends Fragment {
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.user_cabinet, container, false);
        ((TextView)root.findViewById(R.id.name)).setText(my.name);
        ((TextView)root.findViewById(R.id.status)).setText(my.status);
        ((TextView)root.findViewById(R.id.city)).setText(my.city);

        Glide.with(root.getContext())
                .load(my.gen_avatar(my.avatar))
                .error(R.drawable.main_logo)
                .placeholder(R.drawable.main_logo)
                .skipMemoryCache(true)
                .into((CircleImageView)root.findViewById(R.id.ellipse_1));

        return root;
    }
}