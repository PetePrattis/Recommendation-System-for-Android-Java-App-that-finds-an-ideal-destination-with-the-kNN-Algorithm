package com.unipi.p15013p15120.kastropoliteiesv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Questionnaire extends AppCompatActivity {
    private static final String TAG = "Questionnaire";

    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;
    Button submit;

    CheckBox cb1,cb2,cb3,cb4,cb5,cb6;

    String cuid;

    ArrayList<String> locations = new ArrayList<String>();
    boolean answered = false;

    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire);
        setTheme(R.style.SplashScreenAndLoginTheme);


        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);
        cb6 = findViewById(R.id.checkBox6);

        submit = findViewById(R.id.submit);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        cuid = user.getUid();

        //listener to check if auth user is signed in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:");

                } else {
                    // User is signed out go back at LoginSignUpPage class
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(getApplicationContext(), LoginSignUpPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        };


        //Read from the database the locations and add them to arraylist
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("topothesies").getChildren()) {
                    locations.add(ds.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //listener to check if questionnaire is answered by user
        listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("questionnaire").getChildren()) {
                    if (String.valueOf(ds.getKey()).equals(cuid)) {//if there is an entry with user's id in questionnaire table
                        //this means he has answered questionnaire
                        answered = true;
                        submit.setVisibility(View.GONE);
                        break;
                    }
                }
                //if he hasn't answered
                if (!answered) {
                    submit.setClickable(true);
                }
                else {
                    //if he has answered we create intent for HomePage class
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    //we put an extra variable 'welcome' to know that we came from Questionnaire class
                    intent.putExtra("welcome_message","welcome");
                    myRef.removeEventListener(listener);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //when submit is clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers();
            }
        });

    }


    //we check answers and we make records in questionnaire table
    private void checkAnswers() {
        if(!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()){//we have to at least chooce one of the first three options
            Toast.makeText(getApplicationContext(),"Πρέπει να επιλέξετε τουλάχιστον μια από τις τρείς πρώτες επιλογές!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseUser currentUser = mAuth.getCurrentUser();
            cuid = currentUser.getUid();

            //we set values of user's answers in questionnaire with one if option check or else with zero
            if(cb1.isChecked())
                myRef.child("questionnaire").child(cuid).child("thalassa").setValue(1);
            else
                myRef.child("questionnaire").child(cuid).child("thalassa").setValue(0);

            if(cb2.isChecked())
                myRef.child("questionnaire").child(cuid).child("vouno").setValue(1);
            else
                myRef.child("questionnaire").child(cuid).child("vouno").setValue(0);

            if(cb3.isChecked())
                myRef.child("questionnaire").child(cuid).child("pediada").setValue(1);
            else
                myRef.child("questionnaire").child(cuid).child("pediada").setValue(0);

            if(cb4.isChecked())
                myRef.child("questionnaire").child(cuid).child("istoria").setValue(1);
            else
                myRef.child("questionnaire").child(cuid).child("istoria").setValue(0);

            if(cb5.isChecked())
                myRef.child("questionnaire").child(cuid).child("en_tour").setValue(1);
            else
                myRef.child("questionnaire").child(cuid).child("en_tour").setValue(0);

            if(cb6.isChecked())
                myRef.child("questionnaire").child(cuid).child("nyxt_zwh").setValue(1);
            else
                myRef.child("questionnaire").child(cuid).child("nyxt_zwh").setValue(0);

            //methods that make records for user's ratings and visitation of home page
            addZeroRatings();
            addZeroEpiskepsimotitaHomePage();
        }
    }

    private void addZeroRatings(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        cuid = currentUser.getUid();

        //for every location we make a record for the user
        for (int i = 0; i <= locations.size() - 1; i++) {
            myRef.child("ratings").child(cuid).child(String.valueOf(i)).child("episkepsimotita").setValue(0);
            myRef.child("ratings").child(cuid).child(String.valueOf(i)).child("rating").setValue(0);
            myRef.child("ratings").child(cuid).child(String.valueOf(i)).child("topothesies").setValue(locations.get(i));

        }

    }
    private void addZeroEpiskepsimotitaHomePage()
    {
        FirebaseUser curUser = mAuth.getCurrentUser();
        cuid = curUser.getUid();

        myRef.child("users_episkepsimotita").child(cuid).child("episk_homepage").setValue(0);
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            finish();
        }
    }

}