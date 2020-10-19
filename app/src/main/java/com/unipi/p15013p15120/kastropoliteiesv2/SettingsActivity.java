package com.unipi.p15013p15120.kastropoliteiesv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//rythmiseis efarmoghs
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    private SharedPreferences sp;
    private LoginSignUpPage helper = new LoginSignUpPage();
    private HomePage helper2 = new HomePage();
    private Dialog2 dialog;

    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getSupportActionBar().setSubtitle("Ρυθμίσεις");
        //These features will belong to whatever account
        Button signout = findViewById(R.id.signout2);
        Button delete_account = findViewById(R.id.delete_account);
        Button about = findViewById(R.id.show_about);
        Button history = findViewById(R.id.show_history);
        Button change_quest = findViewById(R.id.change_quest);
        Button show_help = findViewById(R.id.show_help);


        //These features will belong only to email

        Button change_email = findViewById(R.id.ch_email);
        Button change_pass = findViewById(R.id.ch_pass);



        sp = getSharedPreferences("account_google", MODE_PRIVATE);
        //an eimai syndedemenos mesw GOOGLE ACCOUNT, den mporw na allaksw mail, kwdiko
        if (sp.getBoolean("account_google", false)) {
            change_email.setVisibility(View.GONE);
            change_pass.setVisibility(View.GONE);

        }

        //logged in user
        user = FirebaseAuth.getInstance().getCurrentUser();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginSignUpPage.class);
                i.putExtra("where", "homepage_signout");
                startActivity(i);
            }
        });

        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog2("Εισάγετε e-mail", "change_email");
                dialog.show(getSupportFragmentManager(), "change_email");
            }
        });


        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog2("Εισάγετε κωδικό", "change_pass");
                dialog.show(getSupportFragmentManager(), "change_pass");
            }
        });

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog2("Διαγραφή Λογαριασμού","delete_account","Είστε σίγουρος οτι θέλετε να διαγράψετε τον λογαριασμό σας από τις Καστροπολιτείες;");
                dialog.show(getSupportFragmentManager(), "delete_account");

            }
        });

        //an o xristis ehei kanei toulaxiston 1 tote emfanise to istoriko
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper2.actualratings>0 && helper2.user_history_locations_id != null && helper2.user_history_locations_name != null){
                    i = new Intent(getApplicationContext(), LocationsList.class);
                    i.putExtra("where", "user_ratings");
                    startActivity(i);
                }
                else
                    helper.makeToast(getApplicationContext(),"Δεν έχεις βαθμολογήσει ακόμη κάποια τοποθεσία...");
            }

        });

        //allages stis apantiseis erwthmatologiou
        change_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangeQuestionnaire.class);
                startActivity(intent);
            }

        });


        show_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog2("Βοήθεια","about",getResources().getString(R.string.help_text));
                dialog.show(getSupportFragmentManager(),"help");
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog2("Σχετικά με εμάς","about",getResources().getString(R.string.about));
                dialog.show(getSupportFragmentManager(), "about");
            }
        });


    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    public void changeEmail(String email) {


        auth.getCurrentUser().updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Log.d(TAG, "User email address updated.");
                        else
                            Log.d(TAG,"yparxei idi tetoios xristis");
                    }
                });

    }

    public void changePass(String pass) {
        auth.getCurrentUser().updatePassword(pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }
}
