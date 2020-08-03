package com.example.app.userScreen.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.R;
import com.example.app.userScreen.MainScreen;
import com.example.app.userScreen.map.MapActivity;
import com.example.app.userScreen.profile.friends.FriendsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {


    private static Profile INSTANCE = null;


    //BUSSINESS DATA
    private ImageView bussinesPic1, bussinesPic2, bussinesPic3;
    private LinearLayout jobLayout1, jobLayout2, jobLayout3;
    private TextView bussinesText1, bussinesText2, bussinesText3;
    private ImageButton createBussines;


    // USER DATA
    private static int ID, afaceri;
    private static String username, nume, prenume, password, mail, ziuaDeNastere, sex, nrtel;

    private ImageView profileImg;
    private Dialog eDialog;
    private LinearLayout headerProfile;
    private ImageButton toEdit, toEvents, more;
    private CardView option1,option2, option3,option4,option5;
    private LinearLayout menu;
    private Animation anim, anim2,anim3,anim4,anim5,fadein,fadeout,popin_top, popout_top;
    private TextView usernameProfile;
    private boolean opened = false, colapedHeader = false;
    private ConstraintLayout header,contentMenu,fadeLayer;
    private ConstraintSet constraintSet;
    private int timesPressed = 0;
    private static String[] names = new String[MainScreen.getFriends()];
    private static String[] paths = new String[MainScreen.getFriends()];

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

    public static String[] getNames(){
        return names;
    }

    public static String[] getPaths(){
        return paths;
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
        serverRun();


        jobLayout1 = new LinearLayout(getContext());
        bussinesPic1 = new ImageView(getContext());
        bussinesText1 = new TextView(getContext());
        createBussines = view.findViewById(R.id.addBussiness);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!colapedHeader)
                {
                    colapedHeader = true;
                    addToLayout();
                }
                else
                {
                    colapedHeader = false;
                    removeFromLayout();
                }
            }
        });

        usernameProfile.setText(MainScreen.getUsername());


        ConstraintLayout aggroZone = view.findViewById(R.id.profile);
        aggroZone.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    MapActivity.getMap().getUiSettings().setScrollGesturesEnabled(true);
                return true;
            }
        });

        editProfile();

        toMenu();

        return view;
    }

    private void editProfile(){
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
    }

    private void toMenu(){
        toEdit = view.findViewById(R.id.toEdit);
        toEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!opened)
                    openMenu();
                else
                {
                    closeMenu();
                }
            }
        });
    }




    private void openMenu(){
        opened = true;
        menu.setVisibility(View.VISIBLE);
        contentMenu.startAnimation(fadein);
        header.startAnimation(popin_top);
        option1.startAnimation(anim);
        option2.startAnimation(anim2);
        option3.startAnimation(anim3);
        option4.startAnimation(anim4);
        option5.startAnimation(anim5);
        toEdit.setImageResource(R.drawable.ic_close_black_24dp);
    }

    private void closeMenu(){
        opened = false;
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

    private void openSelector(ConstraintLayout master){
        colapedHeader = true;
        more.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        constraintSet.clone(master);
        constraintSet.constrainPercentHeight(R.id.headerMenu,1.0f);
        constraintSet.applyTo(master);
        header.setBackgroundResource(R.drawable.full_screen_bg);
    }

    private void closeSelector(ConstraintLayout master){
        colapedHeader = false;
        more.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        constraintSet.clone(master);
        constraintSet.constrainPercentHeight(R.id.headerMenu,0.4f);
        constraintSet.applyTo(master);
        header.setBackgroundResource(R.drawable.circle_profile);
    }




    /**
     * THIS IS THE LAYOUT_ADDER
     */
    private void addToLayout(){

        LinearLayout rootLayout = view.findViewById(R.id.selector);

        final ConstraintLayout head = view.findViewById(R.id.profile_selector);
        TransitionManager.beginDelayedTransition(head);

        final ConstraintLayout selectorMaster = view.findViewById(R.id.selectorMaster);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) selectorMaster.getLayoutParams();
        params.topMargin = 0;

//        selectorMaster.setBackgroundResource(R.drawable.top_bottom_border);

        TransitionManager.beginDelayedTransition(fadeLayer);
        fadeLayer.setVisibility(View.VISIBLE);


        switch (MainScreen.getAfaceri()){
            case 0:
                createBussines.setVisibility(View.VISIBLE);
                selectorMaster.setMinHeight(20);
                break;
            case 1:
                createBussines.setVisibility(View.VISIBLE);
                buildBussines1(rootLayout);
                break;
            case 2:
                createBussines.setVisibility(View.VISIBLE);
                buildBussines1(rootLayout);
                buildBussines2(rootLayout);
                break;
            case 3:
                createBussines.setVisibility(View.INVISIBLE);
                buildBussines1(rootLayout);
                buildBussines2(rootLayout);
                buildBussines3(rootLayout);
                break;
        }
    }

    private void createAddingButton(LinearLayout rootLayout){
        float height = getResources().getDimension(R.dimen.add_height);
        float width = getResources().getDimension(R.dimen.add_height);
        LinearLayout.LayoutParams set = new LinearLayout.LayoutParams((int) height, (int) width);
        set.gravity = Gravity.CENTER;
        createBussines.setScaleType(ImageView.ScaleType.FIT_XY);
        createBussines.setImageResource(R.drawable.plus);
        createBussines.setLayoutParams(set);
        createBussines.setBackgroundResource(R.drawable.around_add_button);

        rootLayout.addView(createBussines);
    }

    private void buildBussines1(LinearLayout rootLayout){
        //The Layout
        LinearLayout.LayoutParams set = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        set.setMargins(0,20,0,20);
        jobLayout1.setLayoutParams(set);

        //The pic
        bussinesPic1.setImageResource(R.drawable.default_pic_foirma);
        float height = getResources().getDimension(R.dimen.pic_height);
        float width = getResources().getDimension(R.dimen.pic_height);
        bussinesPic1.setLayoutParams(new LinearLayout.LayoutParams((int) width,(int) height));
        bussinesPic1.setAdjustViewBounds(true);
        bussinesPic1.setScaleType(ImageView.ScaleType.FIT_CENTER);


        //The text
        bussinesText1.setText("First Bussiness");
        bussinesText1.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(20,0,0,0);
        bussinesText1.setLayoutParams(params);


        rootLayout.addView(jobLayout1);
        jobLayout1.addView(bussinesPic1);
        jobLayout1.addView(bussinesText1);
    }

    private void buildBussines2(LinearLayout rootLayout){}

    private void buildBussines3(LinearLayout rootLayout){}


    /**
     * THIS IS THE LAYOUT_REMOVER
     */
    private void removeFromLayout(){

        final ConstraintLayout head = view.findViewById(R.id.profile_selector);
        TransitionManager.beginDelayedTransition(head);

        final ConstraintLayout selectorMaster = view.findViewById(R.id.selectorMaster);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) selectorMaster.getLayoutParams();
        float topMinus = getResources().getDimension(R.dimen.initial_pos);
        params.topMargin = (int) topMinus;

        TransitionManager.beginDelayedTransition(fadeLayer);
        fadeLayer.setVisibility(View.INVISIBLE);


        switch (MainScreen.getAfaceri()){
            case 0:
                selectorMaster.setMinHeight(0);
                break;
            case 1:
                removeBussines1();
                break;
            case 2:
                removeBussines1();
                removeBussines2();
                break;
            case 3:
                removeBussines1();
                removeBussines2();
                removeBussines3();
                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selectorMaster.setMinHeight(0);
//                selectorMaster.setBackgroundResource(R.drawable.aggro_zone);
            }
        },650);
    }

    private void removeAddingButton(){
        LinearLayout rootLayout = view.findViewById(R.id.selector);
        headerProfile.removeView(createBussines);
    }

    private void removeBussines1(){
        LinearLayout rootLayout = view.findViewById(R.id.selector);

        rootLayout.removeView(jobLayout1);
        jobLayout1.removeView(bussinesPic1);
        jobLayout1.removeView(bussinesText1);

    }

    private void removeBussines2(){
        LinearLayout rootLayout = view.findViewById(R.id.selector);

        rootLayout.removeView(jobLayout2);
        jobLayout2.removeView(bussinesPic2);
        jobLayout2.removeView(bussinesText2);
    }

    private void removeBussines3(){
        LinearLayout rootLayout = view.findViewById(R.id.selector);

        rootLayout.removeView(jobLayout3);
        jobLayout3.removeView(bussinesPic3);
        jobLayout3.removeView(bussinesText3);
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
        usernameProfile = view.findViewById(R.id.username_profile_header);
        profileImg = view.findViewById(R.id.profilePicHeader);
        fadeLayer = view.findViewById(R.id.fadeLayer);

        TextView events = view.findViewById(R.id.nrEvents);
        String text = "" + MainScreen.getEvents() + "\nEvents";
        events.setText(text);

        TextView nothing = view.findViewById(R.id.nothingHere);
        text = "" + MainScreen.getFriends() + "\nFriends";
        nothing.setText(text);
        nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendsActivity.class);
                timesPressed++;
                if(timesPressed == 1)
                    intent.putExtra("firstTime", true);
                else
                    intent.putExtra("firstTime", false);
                startActivity(intent);
            }
        });

        afaceri = MainScreen.getAfaceri();

        constraintSet = new ConstraintSet();

        TextView usernameTV = view.findViewById(R.id.username_T);
        usernameTV.setText(username);


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

        eDialog = new Dialog(getContext());

        headerProfile = view.findViewById(R.id.headerProfile);

        Toast.makeText(getContext(), "" + headerProfile.getHeight(),Toast.LENGTH_SHORT).show();

        header = view.findViewById(R.id.headerMenu);
        contentMenu = view.findViewById(R.id.menuContent);
    }

    private void serverRun(){

        String urlUpload = "http://gladiaholdings.com/PHP/countFriends.php";

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for (int i = 0; i < jsonObject.getInt("noOfFriends"); i++) {
                        names[i] = jsonObject.getString("nume" + (i + 1));
                        paths[i] = jsonObject.getString("poza" + (i + 1));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error loading your friends", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID","" + MainScreen.getUserID());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }
}
