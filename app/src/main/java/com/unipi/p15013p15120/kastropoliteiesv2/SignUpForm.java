package com.unipi.p15013p15120.kastropoliteiesv2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;


//form gia eggrafi xristi
public class SignUpForm extends AppCompatActivity {

    LoginSignUpPage helper;
    ImageButton close;
    EditText email, pass, usern;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashScreenAndLoginTheme);
        setContentView(R.layout.sign_up_form);
        //setSpinnerCalendar();

        //panw deksia yparxei koumpi X ws epistrofi sthn prohgoumenh selida (auti tis syndeshs)
        close = findViewById(R.id.closeBtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper = new LoginSignUpPage();

                email = findViewById(R.id.email_prompt);
                pass = findViewById(R.id.password_prompt);
                usern = findViewById(R.id.username_prompt);


                if (!email.getText().toString().equals("") &&!pass.getText().toString().equals("") && !(pass.getText().toString().length() <6)) {
                    //an to email einai valid se morfi kane tin eggrafi
                    if (helper.isEmailValid(email.getText().toString()))
                        helper.firebaseAuthEmailSignUp(email.getText().toString(), pass.getText().toString(),usern.getText().toString());//helper.makeToast(getApplicationContext(),helper.firebaseAuthEmailSignUp(email.getText().toString(), pass.getText().toString(),usern.getText().toString()));
                    else
                        helper.makeToast(getApplicationContext(),"Το email που εισάγατε δεν είναι σε σωστή μορφή");
                }
                //an o password einai mikroteros apo 6 psifia den ginetai
                else if (pass.getText().toString().length() <6)
                    helper.makeToast(getApplicationContext(),"Ο κωδικός πρέπει να είναι μεγαλύτερος ή ίσος με 6 ψηφία.");
                else
                    helper.makeToast(getApplicationContext(), "Παρακαλώ εισάγετε τα στοιχεία σας.");
            }
        });


    }


    //itan gia na yparxei imerologio gia hmeromhnia gennhshs
    private void setSpinnerCalendar()
    {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, years);//was this

        //Spinner spinYear = findViewById(R.id.spinnerCalendar);
        //spinYear.setAdapter(adapter);
    }

}
