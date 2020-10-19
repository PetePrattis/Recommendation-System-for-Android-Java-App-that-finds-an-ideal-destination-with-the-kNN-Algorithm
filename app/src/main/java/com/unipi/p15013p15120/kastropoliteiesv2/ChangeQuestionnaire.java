package com.unipi.p15013p15120.kastropoliteiesv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeQuestionnaire extends AppCompatActivity {
    private static final String TAG = "ChangeQuestionnaire";

    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    Button submit;

    CheckBox cb1,cb2,cb3,cb4,cb5,cb6;

    String cuid;

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

        //button on click listener
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswers();
            }
        });


    }

    private void checkAnswers() {//method that checks if answers given are correct
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

            //we initialize the intent for the HomePage class
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            //we put an extra variable 'changes' to know that when we are at HomePage that the ChangeQuestionnaire was before
            intent.putExtra("changedq","changedq");
            startActivity(intent);

        }
    }

}