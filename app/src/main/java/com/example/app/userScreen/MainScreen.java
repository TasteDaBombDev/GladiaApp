package com.example.app.userScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.app.R;
import com.example.app.userScreen.map.MapActivity;

public class MainScreen extends AppCompatActivity {

    private static String username;
    private ViewPager viewPager;

    private static int userID, afaceri, events, friends;
    private static boolean tutorial;
    private static String nume;
    private static String prenume;
    private static String password;
    private static String mail;
    private static String ziuaDeNastere;
    private static String sex;
    private static String nrtel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_screen);
        Bundle extras = getIntent().getExtras();
        userID = extras.getInt("userID");
        username = extras.getString("username");
        nume = extras.getString("nume");
        prenume = extras.getString("prenume");
        password = extras.getString("password");
        mail = extras.getString("mail");
        ziuaDeNastere = extras.getString("ziuaDeNastere");
        sex = extras.getString("sex");
        afaceri = extras.getInt("nrAfaceri");
        events = extras.getInt("nrEvents");
        friends = extras.getInt("friends");
        nrtel = extras.getString("nrtel");
        tutorial = extras.getBoolean("fromRegister");


        viewPager = findViewById(R.id.mainSlider);

        EnumFragments enumFragments = new EnumFragments(getSupportFragmentManager(),this);
        viewPager.setAdapter(enumFragments);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean scrollStarted = false;


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(false);
                } else {
                    scrollStarted = false;
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(true);
                }
            }
        });
    }

    public static int getUserID() {
        return userID;
    }

    public static String getUsername() {
        return username;
    }

    public static String getNume() {
        return nume;
    }

    public static String getPrenume() {
        return prenume;
    }

    public static String getPassword() {
        return password;
    }

    public static String getMail() {
        return mail;
    }

    public static String getZiuaDeNastere() {
        return ziuaDeNastere;
    }

    public static String getSex() {
        return sex;
    }

    public static String getNrtel() {
        return nrtel;
    }

    public static boolean startTutorial(){
        return tutorial;
    }

    public static int getAfaceri(){
        return afaceri;
    }

    public static int getEvents(){
        return events;
    }

    public static int getFriends(){
        return friends;
    }

    @Override
    public void onBackPressed() {
        //Nothing
    }
}
