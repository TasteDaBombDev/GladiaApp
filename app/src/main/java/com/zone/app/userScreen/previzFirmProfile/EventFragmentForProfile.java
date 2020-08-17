package com.zone.app.userScreen.previzFirmProfile;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zone.app.R;
import com.zone.app.utils.EventsDetails;

public class EventFragmentForProfile extends Fragment {

    private EventsDetails event;
    private View home;

    public EventFragmentForProfile(EventsDetails event){
        this.event = event;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        home = inflater.inflate(R.layout.event_for_profile_firm, container, false);

        TextView name = home.findViewById(R.id.eventName1), type1 = home.findViewById(R.id.type1), location = home.findViewById(R.id.location1), hour1 = home.findViewById(R.id.hour1);
        ImageView pic = home.findViewById(R.id.coverPic1);

        name.setText(event.getName());
        type1.setText(event.getType());
        hour1.setText(event.getOra());
        location.setText(event.getLocation());
        Picasso.get().load(event.getPoza()).placeholder(R.drawable.nopic).into(pic, new Callback() {
            @Override
            public void onSuccess() {
//                setImageRounded(pic);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        return home;
    }

    private void setImageRounded(ImageView pic){
        Bitmap bitmap = ((BitmapDrawable)pic.getDrawable()).getBitmap();
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        pic.setImageDrawable(roundedBitmapDrawable);
    }
}
