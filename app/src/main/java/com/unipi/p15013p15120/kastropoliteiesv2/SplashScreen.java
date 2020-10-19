package com.unipi.p15013p15120.kastropoliteiesv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

//arxiki othoni. edw elegxoume an o xristis einai syndedemenos sto diadiktyo
// kai an einai syndedemenos me ton logariasmo tou apo prin stin efarmogi
public class SplashScreen extends AppCompatActivity {

    //gia metavliti GOOGLE ACOUNT
    private SharedPreferences sp;

    Intent i;

    //checkQ variables
    String cuid;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth auth;
    ValueEventListener listener;

    //gia emfanisi DIALOG
    Dialog2 dialog;

    //gia elegxo internet
    ConnectivityManager cm;
    static boolean resumed = false;

    private static final String TAG = "SplashScreen";

    //otan px den exoume arxika internet kai epistrefoume sthn arxiki othoni kinhtou kai
    //ksananoigoume tin efarmogi KASTROPOLITEIES tote elegxetai ksana an exoume syndesi sto internet
    @Override
    public void onRestart() {
        super.onRestart();

        //metabliti elegxou an proerxomaste apo RESTART efarmogis
        resumed = true;
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        checkForConnection(cm);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashScreenAndLoginTheme);
        setContentView(R.layout.splash_screen);

        //orismos metavlitis shared preferences gia to an eimaste syndedemenoi me logariasmo GOOGLE
        sp = getSharedPreferences("account_google", MODE_PRIVATE);

        //logo splash screen
        ImageView logo = findViewById(R.id.logo);
        makeRound(R.drawable.logo,logo);

        //elegxos internet
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        checkForConnection(cm);

    }


    //dhmiourgia Toast mhnymatos
    private void makeToast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
    }

    //methodos elegxou an i syndesh pou yparxei sti syskeui ehei prosbasi sto internet
    /*private boolean isOnline(){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProc = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitVal = ipProc.waitFor();
            return (exitVal == 0);
        }
        catch(IOException e) {Log.w(TAG,e.getMessage());}
        catch(InterruptedException e ) {Log.w(TAG,e.getMessage());}
        return false;
    }*/

    private void checkForConnection(ConnectivityManager cm)
    {
        //an yparxei syndesh sto diadiktyo
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {

            // if (isOnline()) {
            auth = FirebaseAuth.getInstance();
            //an yparxei xristis syndedemenos elegkse an ehei apantisei sto erwthmatologio
            if (auth.getCurrentUser() != null)
                checkQ(auth.getCurrentUser());
            //an den yparxei xristis syndedemenos, phgaine ton stin othony syndesis
            else {
                i = new Intent(this, LoginSignUpPage.class);
                sp.edit().putBoolean("account_google",false).apply();
                startActivity(i);
                finish();
            }
        }//an den yparxei syndesi sto diadiktyo
        else {
            Log.w(TAG,"No Network");
            //an den proerxomaste apo restart tis activity emfanise auto to mhnyma
            if (!resumed) {
                dialog = new Dialog2("No Internet", "splash", "Οι Καστροπολιτείες χρειάζονται σύνδεση στο δίκτυο για να λειτουργήσουν. Παρακαλώ συνδέσου και ξαναπροσπάθησε!");
                dialog.show(getSupportFragmentManager(), "splash");
            }//an ehouem kanei restart tine farmogi emfanise toast mhnuma gia apofysh EXCEPTION
            else
                makeToast(getApplicationContext(), "Οι Καστροπολιτείες χρειάζονται σύνδεση στο δίκτυο για να λειτουργήσουν. Παρακαλώ συνδέσου και ξαναπροσπάθησε!");
        }
    }

    //round image
    private void makeRound(int img, ImageView logo)
    {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),img);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),icon);
        dr.setCornerRadius(Math.max(icon.getWidth(), icon.getHeight()) / 2.0f);
        logo.setBackground(dr);

    }

    //elegxos erwthmatologiou
    private void checkQ(FirebaseUser currentUser){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        cuid = currentUser.getUid();

        //synexhs elegxos an o xristis pou einai syndedemenos ehei apanthsei sto erwthmatologio
        //ara yparxei i eggrafi tou ston pinaka QUESTIONNAIRE
        listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot ds : dataSnapshot.child("questionnaire").getChildren()){
                    if (String.valueOf(ds.getKey()).equals(cuid)){
                        found = true;
                        //den theloume na elegxoume pia an ehei apadisei o xristis sto questionnaire giati vrehtike
                        myRef.removeEventListener(listener);
                        break;
                    }
                }

                //an den vrethei, phgaine ton stin selida tou erwthmatologiou
                if(!found){
                    Log.v(TAG,"User has not answered questionnaire!");
                    Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                    startActivity(intent);
                    finish();

                }
                //alliws phgaine ton sthn arxiki selida
                else{
                    Log.v(TAG,"User has answered questionnaire!");
                    //den theloume na elegxoume pia an ehei apadisei o xristis sto questionnaire giati ehei vrethei
                    myRef.removeEventListener(listener);
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
                makeToast(getApplicationContext(), "Παρακαλώ κάντε επανεκκίνηση την εφαρμογή");
            }
        });
    }
}
