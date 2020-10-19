package com.unipi.p15013p15120.kastropoliteiesv2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

//class pou dhmiourgei dialogs.
//ta dialogs pou ehoume stin efarmogi einai:
//about, help, change email, change pass, delete account, profile, rate, help(location_page), rate of Home page
public class Dialog2 extends DialogFragment {

    private String title,email_txt,tel_txt,name_txt;
    private static String where;
    private String msg_or_id = null;
    private String imgDr;
    private String id = null;

    private EditText input;
    private ImageView img;
    private TextView name,email,tel, thalassa,vouno,pediada,istoria,en_tour,nyxt_zwh;
    View view;
    private RatingBar rt;



    private LoginSignUpPage helper;
    private SettingsActivity helper2;
    static HomePage helper3;
    LocationPage helper4;
    private SharedPreferences sp;



    FirebaseAuth mAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    ValueEventListener listener;


    public Dialog2() {}

    //for input only
    @SuppressLint("ValidFragment")
    public Dialog2(String title, String where) {
        this.title = title;
        this.where = where;
    }

    //for text only
    @SuppressLint("ValidFragment")
    public Dialog2 (String title, String where, String msg_or_id)
    {
        this.title = title;
        this.where = where;
        this.msg_or_id = msg_or_id;
    }

    public Dialog2 (String title, String where, String name_txt, String email_txt, String tel_txt)
    {
        this.title = title;
        this.where = where;
        this.name_txt = name_txt;
        this.email_txt = email_txt;
        this.tel_txt = tel_txt;
    }

    public Dialog2 (String title, String where, String[] msg_or_id, String imgDr)
    {
        this.title = title;
        this.where = where;
        this.msg_or_id = msg_or_id[1];
        this.id = msg_or_id[0];
        this.imgDr = imgDr;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        sp = getActivity().getSharedPreferences("account_google", Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Layout of Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //declare view and content
        if (where.equals("about") || where.equals("splash")||
                where.equals("delete_account")  || where.equals("welcome_message")){
            view = inflater.inflate(R.layout.alert_dialog_textonly,null);
            //text = (TextView) view.findViewById(R.id.textView);
        }
        else if (where.equals("profile")) {
            view = inflater.inflate(R.layout.alert_dialog_profile,null);
            img = view.findViewById(R.id.photo_profile);
            name= view.findViewById(R.id.text_profile_name);
            email = view.findViewById(R.id.text_profile_email);
            tel = view.findViewById(R.id.text_profile_tel);
            thalassa = view.findViewById(R.id.thalassa);
            vouno = view.findViewById(R.id.vouno);
            pediada = view.findViewById(R.id.pediada);
            istoria = view.findViewById(R.id.history);
            en_tour = view.findViewById(R.id.alternative_tourism);
            nyxt_zwh = view.findViewById(R.id.night_life);
        }
        else if (where.equals("rate"))
        {
            view = inflater.inflate(R.layout.alert_dialog_rating_bar,null);
            rt = view.findViewById(R.id.rtbar);

        }
        else if (where.equals("rateHomepage"))
        {
            view = inflater.inflate(R.layout.alert_dialog_rating_bar_img,null);
            rt = view.findViewById(R.id.rtbar);
            img = view.findViewById(R.id.img);


            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), LocationPage.class);

                    i.putExtra("whereID", id);
                    i.putExtra("whereName", msg_or_id);
                    startActivity(i);
                }
            });
            int i = getResources().getIdentifier(imgDr , "drawable", getActivity().getPackageName());
            Bitmap b = BitmapFactory.decodeResource(getContext().getResources(), i);

            float scaleFactor = 0.50f;
            int sizeX = Math.round(b.getWidth() * scaleFactor);
            int sizeY = Math.round(b.getHeight() * scaleFactor);

            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

            Drawable im = new BitmapDrawable(getContext().getResources(), bitmapResized);

            img.setImageDrawable(im);
            //img.setImageResource(getResources().getIdentifier(imgDr , "drawable", getActivity().getPackageName()));
        }
        else{//change email
            view = inflater.inflate(R.layout.alert_dialog_email, null);
            input = (EditText) view.findViewById(R.id.input);
        }

        //set view and title (if exists)
        if (!where.equals("profile"))
            builder.setView(view).setTitle(title);
        else
            builder.setView(view);


        helper = new LoginSignUpPage();
        helper2 = new SettingsActivity();
        helper3 = new HomePage();
        helper4 = new LocationPage();

        //set message if exists  in RATE
        if (msg_or_id !=null && where.equals("rate"))
            builder.setMessage(helper4.whereName);

        //set message if exists wherever except profile
        if (msg_or_id !=null && !where.equals("profile") && !where.equals("rate")) //&& !where.equals("rate")) an pernaw to ID kai den thelw na to vlepw
            builder.setMessage(msg_or_id);


        if (where.equals("profile"))
        {
            //download image and make it rounded in profile
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null)
                new DownLoadImageTask(img,getContext()).execute(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
            else {
                img.setImageResource(R.drawable.ic_profile_black_24dp);
            }

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();


            //get QUESTIONNAIRE ANSWERS
            //listener =
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.child("questionnaire").getChildren()) {
                        UserQuestionnaire user = ds.getValue(UserQuestionnaire.class);

                        if (String.valueOf(ds.getKey()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            if (user.getThalassa() == 0)
                                thalassa.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (user.getVouno() == 0)
                                vouno.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (user.getPediada() == 0)
                                pediada.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (user.getIstoria() == 0)
                                istoria.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (user.getEn_tour() == 0)
                                en_tour.setBackgroundResource(R.drawable.roundedfull_decline);
                            if (user.getNyxt_zwh() == 0)
                                nyxt_zwh.setBackgroundResource(R.drawable.roundedfull_decline);


                        }
                    }
                    //myRef.removeEventListener(listener);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //an yparxei onoma, emfanise to
            if (name_txt != null)
                name.setText(name_txt);

            email.setText(email_txt);

            //an yparxei telephone emfanise to
            if (tel_txt != null)
                tel.setText(tel_txt);

            //new HomePage.DownLoadImageTask(img).execute(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        }


        //set ok and cancel button directs to what we want
        if (!where.equals("top_rated")) {
            if (!where.equals("profile")) {
                if (!where.equals("predicted")) {
                    if (!where.equals("history")) {
                        if (!where.equals("splash")) {
                            if (!where.equals("about")) {

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (where.equals("forgot_pass")) {
                                            if (!input.getText().toString().equals("")) {
                                                if (helper.isEmailValid(input.getText().toString())) {
                                                    helper.forgotPass(input.getText().toString());
                                                    helper.makeToast(getActivity().getApplicationContext(), "Το email στάλθηκε επιτυχώς στη " + input.getText().toString());
                                                } else
                                                    helper.makeToast(getActivity().getApplicationContext(), "Το email που εισάγατε δεν είναι σε σωστή μορφή");
                                            } else
                                                helper.makeToast(getActivity().getApplicationContext(), "Παρακαλώ εισάγετε το email σας");
                                        }

                                        if (where.equals("change_email")) {
                                            if (!input.getText().toString().equals(""))
                                                if (helper.isEmailValid(input.getText().toString())) {
                                                    helper2.changeEmail(input.getText().toString());
                                                } else
                                                    helper.makeToast(getActivity().getApplicationContext(), "Το email που εισάγατε δεν είναι σε σωστή μορφή");

                                        }

                                        if (where.equals("change_pass")) {
                                            if (!input.getText().toString().equals("") && !(input.getText().toString().length() < 6))
                                                helper2.changePass(input.getText().toString());
                                            else if (input.getText().toString().length() < 6)
                                                helper.makeToast(getActivity().getApplicationContext(), "Παρακαλώ ο κωδικός να είναι περισσότερος ή ισος με 6 ψηφία");
                                        }

                                        if (where.equals("delete_account")) {

                                            Intent i = new Intent(getActivity().getApplicationContext(), LoginSignUpPage.class);
                                            i.putExtra("where", "homepage_delete_account");
                                            startActivity(i);

                                            helper.makeToast(getActivity().getApplicationContext(), FirebaseAuth.getInstance().getCurrentUser().getUid());//helper.makeToast(getContext(),FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",""));
                                    /*Intent i = new Intent(getActivity().getApplicationContext(), LoginSignUpPage.class);
                                    i.putExtra("where", "homepage_delete_account");
                                    i.putExtra("who", FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ""));
                                    startActivity(i);*/
                                        }

                                        if (where.equals("rate")) {
                                            mAuth = FirebaseAuth.getInstance();
                                            checkRate(mAuth.getCurrentUser().getUid(), msg_or_id);
                                        }

                                        if (where.equals("rateHomepage")) {
                                            mAuth = FirebaseAuth.getInstance();
                                            //checkRate(mAuth.getCurrentUser().getEmail().replace(".","_"),msg_or_id);
                                            checkRate(mAuth.getCurrentUser().getUid(), id);
                                        }

                                        if (where.equals("welcome_message"))
                                            helper3.welcome = false;


                                    }
                                }).setNegativeButton("Ακύρωση", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (where.equals("welcome_message"))
                                            helper3.welcome = false;

                                        if (where.equals("rate") || where.equals("rateHomepage")) {
                                            helper.makeToast(getActivity().getApplicationContext(), "Aγαπητέ χρήστη, η γνώμη σου μας βοηθά ώστε να σου προσφέρουμε καλύτερες προτάσεις προορισμών!");
                                        }
                                    }
                                });

                            }
                        }
                    }
                }
            }
        }



        //Create Dialog
        AlertDialog dialog = builder.create();
        return dialog;
    }


    //elegxos vathomologias xristi
    public void checkRate(final String uid, final String whereID){

        final float r = rt.getRating();
        System.out.println("inside checkRate");

        //an i vathmologia einai egyri (1-5) tote apothikeuse tin ston pinaka ratings gia ton sugkekrimeno xristi me uid ...
        if (r > 0){

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();
            listener = myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds2 : dataSnapshot.child("ratings").child(uid).getChildren())
                    {
                        try {
                            if (ds2.child("topothesies").getValue().toString().equals(whereID)) {
                                myRef.child("ratings").child(uid).child(ds2.getKey()).child("rating").setValue(r);
                            }
                        }
                        catch (NullPointerException e){
                            Log.w("Dialog2",e.getMessage());
                        }
                    }
                    myRef.removeEventListener(listener);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            helper.makeToast(getActivity().getApplicationContext(), "Σε ευχαριστούμε πολύ! Η γνώμη σου μας βοηθά ώστε να σου προσφέρουμε καλύτερες προτάσεις προορισμών");
        }
        else
            helper.makeToast(getActivity().getApplicationContext(),"Δεν έδωσες σωστή βαθμολογία :( ");


    }

}


