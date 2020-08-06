package com.zone.app.userScreen.profile.friends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zone.app.R;
import com.zone.app.userScreen.profile.Profile;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class FriendsActivity extends AppCompatActivity {

    ListView friendsListing;
    private Adapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);
        final TextInputLayout textInputLayout = findViewById(R.id.outlineSearch);
        textInputLayout.setAlpha(0.0f);

        AppBarLayout appbar = findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float m = Math.abs((229 + verticalOffset)/229.0f);
                textInputLayout.setAlpha(m);
            }
        });


        ImageButton back = findViewById(R.id.backToMainScreen);
        final EditText search = findViewById(R.id.searchBar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        friendsListing = findViewById(R.id.listview);
        adapter = new Adapter(FriendsActivity.this, Profile.getNames(),Profile.getPaths());
        friendsListing.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (FriendsActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class Adapter extends ArrayAdapter<String>{

        Context context;
        String[] names;
        String[] paths;

        Adapter(Context c, String[] names, String[] paths){
            super(c, R.layout.firend_item,R.id.friendName, names);
            this.context = c;
            this.names = names;
            this.paths = paths;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            @SuppressLint("ViewHolder") View item = layoutInflater.inflate(R.layout.firend_item,parent,false);
            TextView name = item.findViewById(R.id.friendName);
            ImageView image = item.findViewById(R.id.friendPic);
            name.setText(names[position]);
            Picasso.get().load(paths[position]).into(image);

            return item;
        }
    }
}
