package com.example.app.userScreen.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.app.R;
import com.example.app.userScreen.Evenimente;
import com.example.app.userScreen.MainScreen;
import com.example.app.userScreen.MapActivity;
import com.example.app.userScreen.Poze;

import org.w3c.dom.Text;

public class Profile extends Fragment {


    private static Profile INSTANCE = null;

    private static int ID;
    private Dialog eDialog;
    private static String username, nume, prenume, password, mail, ziuaDeNastere, sex, nrtel;
    private ImageButton toEdit, toEvents, more;
    private CardView option1,option2, option3,option4,option5;
    private LinearLayout menu;
    private Animation anim, anim2,anim3,anim4,anim5,fadein,fadeout,popin_top, popout_top;
    private TextView do4;
    private boolean opened = false, colapedHeader = false;
    private ConstraintLayout header,contentMenu;
    private ConstraintSet constraintSet;

    private View view;

    public Profile(){
    }

    public static Profile getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new Profile();
        return INSTANCE;
    }

    public static void resetINSTANCE(){
        INSTANCE = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_screen_dashboard,container,false);
        init();
        TextView usernameTV = view.findViewById(R.id.username_T);
        usernameTV.setText(username);

        if(MainScreen.getMail().equals("-null-"))
            do4.setText("Confirm mail");
        else
            do4.setText("Confirm phone");

        ConstraintLayout aggroZone = view.findViewById(R.id.profile);
        aggroZone.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(true);
                return true;
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eDialog.setContentView(R.layout.edit_profile);

                ImageButton closeBtn = eDialog.findViewById(R.id.closeProfile);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eDialog.dismiss();
                    }
                });


                EditText usernameT = eDialog.findViewById(R.id.username);
                usernameT.setText(username);

                eDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                eDialog.show();
            }
        });


        toEdit = view.findViewById(R.id.toEdit);
        toEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!opened)
                {
                    opened = true;
                    menu.setVisibility(View.VISIBLE);
                    contentMenu.startAnimation(fadein);
//                    circularRevealCard(header);
                    header.startAnimation(popin_top);
                    option1.startAnimation(anim);
                    option2.startAnimation(anim2);
                    option3.startAnimation(anim3);
                    option4.startAnimation(anim4);
                    option5.startAnimation(anim5);
                    toEdit.setImageResource(R.drawable.ic_close_black_24dp);
                }
                else{
                    opened = false;
//                    circularCloseCard(header);
                    header.startAnimation(popout_top);
                    contentMenu.startAnimation(fadeout);
                    toEdit.setImageResource(R.drawable.ic_menu_black_24dp);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menu.setVisibility(View.INVISIBLE);
                        }
                    },300);
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!colapedHeader){
                    colapedHeader = true;
                    more.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    constraintSet.constrainPercentHeight(header.getId(),1.0f);
                    header.setConstraintSet(constraintSet);
                    header.setBackgroundResource(R.drawable.full_screen_bg);
                }
                else
                {
                    colapedHeader = false;
                    more.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    constraintSet.constrainPercentHeight(header.getId(),0.4f);
                    header.setConstraintSet(constraintSet);
                    header.setBackgroundResource(R.drawable.circle_profile);

                }
                Toast.makeText(getContext(), "" + colapedHeader, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void circularRevealCard(View view){
        float finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, 80, 80, 0, finalRadius * 1.1f);

        circularReveal.setDuration(300);

        circularReveal.start();
    }

    private void circularCloseCard(final View view){
        float finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, 80, 80, finalRadius * 1.1f, 0);

        circularReveal.setDuration(300);

        circularReveal.start();
    }

    private void init(){
        ID = MainScreen.getUserID();
        username = MainScreen.getUsername();
        nume = MainScreen.getNume();
        prenume = MainScreen.getPrenume();
        password = MainScreen.getPassword();
        mail = MainScreen.getMail();
        ziuaDeNastere = MainScreen.getZiuaDeNastere();
        sex = MainScreen.getSex();
        nrtel = MainScreen.getNrtel();
        menu = view.findViewById(R.id.profileMenu);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);
        option5 = view.findViewById(R.id.option5);
        more = view.findViewById(R.id.more);
        constraintSet = new ConstraintSet();

        anim = AnimationUtils.loadAnimation(getContext(),R.anim.popin);
        anim.setDuration(200);

        anim2 = AnimationUtils.loadAnimation(getContext(),R.anim.popin);
        anim2.setDuration(200);
        anim2.setStartOffset(100);

        anim3 = AnimationUtils.loadAnimation(getContext(),R.anim.popin);
        anim3.setDuration(200);
        anim3.setStartOffset(200);

        anim4 = AnimationUtils.loadAnimation(getContext(),R.anim.popin);
        anim4.setDuration(200);
        anim4.setStartOffset(300);

        anim5 = AnimationUtils.loadAnimation(getContext(),R.anim.popin);
        anim5.setDuration(200);
        anim5.setStartOffset(400);

        fadein = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        fadein.setDuration(300);

        fadeout = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
        fadeout.setDuration(300);

        popin_top = AnimationUtils.loadAnimation(getContext(),R.anim.popin_top);
        popin_top.setDuration(300);

        popout_top = AnimationUtils.loadAnimation(getContext(),R.anim.popout_top);
        popout_top.setDuration(300);

        do4 = view.findViewById(R.id.do4);

        eDialog = new Dialog(getContext());

        header = view.findViewById(R.id.headerMenu);
        contentMenu = view.findViewById(R.id.menuContent);
    }
}