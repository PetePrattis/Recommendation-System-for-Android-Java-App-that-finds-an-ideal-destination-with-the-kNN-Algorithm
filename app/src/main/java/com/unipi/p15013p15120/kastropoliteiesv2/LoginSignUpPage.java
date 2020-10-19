package com.unipi.p15013p15120.kastropoliteiesv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//othoni syndesis
public class LoginSignUpPage extends AppCompatActivity {

    //oncreate
    GoogleSignInClient mGoogleSignInClient;
    ImageButton signinGoogle;
    private FirebaseAuth auth;
    private SharedPreferences sp;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final String TAG = "LoginSignUpPage";
    Intent i;

    // on create + emailVer
    String where, answer, answer2;
    EditText email, pass;

    //checkQ variables
    String cuid;
    FirebaseDatabase mFirebaseDatabase;
    ValueEventListener listener;
    //deleteAccount variable too
    DatabaseReference myRef;

    //Am I loggen in? NO, if i'm here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashScreenAndLoginTheme);
        setContentView(R.layout.login_sign_up_page);


        //den eimai se account google akomi
        sp = getSharedPreferences("account_google", MODE_PRIVATE);
        sp.edit().putBoolean("account_google", false).apply();

        //Where would I been logged in? In Email if i do not click Google Sign In Button
        where = "email";

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Google Button for Sign In
        signinGoogle = findViewById(R.id.signingoogle);

        //Configurations for Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(default_web_client_id)
                .requestIdToken("894264305491-2hgvfhruqtnmsp6f9sefcbmdt97n8lpo.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        //elegxw an o xristis ehei apantisei sto erwthmatologio
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                checkForUser();
            }

        };

        //Google Sign In
        signinGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Edit texts and Buttons for Email
        email = findViewById(R.id.email_prompt);
        pass = findViewById(R.id.password_prompt);
        Button signin_email = findViewById(R.id.login);
        Button signup_email = findViewById(R.id.signupWelcome);
        Button forgot = findViewById(R.id.forgotpass);

        //Sign in using email and password
        signin_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //an ehw pliktrologhsei stoixeia tote na ginei i syndesh
                if (!email.getText().toString().equals("") && !pass.getText().toString().equals(""))
                {
                    firebaseAuthEmailSignIn(email.getText().toString(), pass.getText().toString());
                    //na elegxthei o xristis ws pros to erwthmatologio
                    checkForUser();
                }
                else
                    Toast.makeText(getApplicationContext(), "Παρακαλώ εισάγετε τα στοιχεία σας", Toast.LENGTH_SHORT).show();
            }
        });

        //Sign Up using email and password
        signup_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), SignUpForm.class);
                startActivity(i);

            }
        });

        //Forgot Password for Email
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog2 dialog = new Dialog2("Εισάγετε e-mail","forgot_pass");
                dialog.show(getSupportFragmentManager(),"forgot_pass");
            }
        });

    }


    //den hrisimopoieitai i methodos email verification
    public void emailVerification() {

        try {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                               }
                        }
                    });
        } catch (NullPointerException ex) {
        }
    }

    //rename DATA se pinaka questionnaire
    public void renameData()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        FirebaseUser currentUser = auth.getCurrentUser();
        cuid = currentUser.getUid();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("questionnaire").getChildren()) {
                    if (cuid.equals(ds.getKey()))
                        myRef.child("questionnaire").child(cuid).setValue("new");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //se kathe periptwsh den tha thelame na paei pisw o xristis apo tin login-sign up selida
    //auto giati synithws i prohgoumeni activity apo tin login einai kati pou sxetizetai me kapoion xristi pou htan syndedemenos.
    //an xreiastei o xristis na epistrepsei sthn arxiki tou kinhtou tou, apla pataei to mesaio koubi!
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }


    //den diagrafoume dedomena
    public void deleteAccount() {

        cuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseAuth.getInstance().getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted." + cuid);
                        }
                    }
                });

        /*if (FirebaseAuth.getInstance().getCurrentUser()!= null)
        {
            //You need to get here the token you saved at logging-in time.
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginSignUpPage.this,  new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    Log.e("newToken",newToken);
                    token = newToken;
                }
            });

            AuthCredential credential;

            //This means you didn't have the token because user used like Facebook Sign-in method.
            if (token == null) {
                Log.e("token","null");
                credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), pass);
            } else {
                Log.e("token","not null");
                //Doesn't matter if it was Facebook Sign-in or others. It will always work using GoogleAuthProvider for whatever the provider.
                credential = GoogleAuthProvider.getCredential(token, null);
            }


            //We have to reauthenticate user because we don't know how long
            //it was the sign-in. Calling reauthenticate, will update the
            //user login and prevent FirebaseException (CREDENTIAL_TOO_OLD_LOGIN_AGAIN) on user.delete()
            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().getCurrentUser().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");

                                                signOutFirebase();
                                                signOutGoogle();
                                                //makeToast(getApplicationContext(), "Ο λογαριασμός σου διαγράφτηκε οριστικά. Ελπίζουμε να επιστρέψετε κάποια στιγμή.");
                                                //FirebaseDatabase.getInstance().getReference().child("questionnaire").child(email_delete).re
                                                //FirebaseDatabase.getInstance().getReference().child("ratings").child(email_delete).removeValue();
                                                //myRef.child("questionnaire").child("alexelement22@gmailcom").removeValue();
                                            }
                                        }
                                    });

                        }
                    });
        }*/
    }




    public void forgotPass(final String email) {
        //auth.setLanguageCode("en"); orismos glwssas analoga th glwssa efarmoghs
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            //Toast.makeText(MainActivity.this,"Ο κωδικός πρόσβασης άλλαξε επιτυχώς για τον " + email.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, task.getException().toString());
                            //Toast.makeText(MainActivity.this,"Ο κωδικός πρόσβασης δεν άλλαξε. Παρακαλώ ξαναπροσπάθησε", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //methodos eggrafis xristis stin FIREBASE AUTHENTICATION
    public void firebaseAuthEmailSignUp(String email, String password, final String name) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            if (!name.equals("")) {
                                UserProfileChangeRequest profUpd = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profUpd)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                          }
                    }
                });
    }

    private void firebaseAuthEmailSignIn(String email, String password) {
        if (!email.equals("") && !password.equals("") && !(password.length()<6)) {
            if (isEmailValid(email)) {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    makeToast(getApplicationContext(), "Ο κωδικός πρόσβασής σας είναι λάθος ή δεν υπάρχει λογαριασμός με αυτό το email. Παρακαλώ ξαναπροσπαθήστε. Σε περίπτωση που ξεχάσατε τον κωδικό σας, πατήστε στο 'Ξέχασες τον κωδικό;'. Αν το πρόβλημα επιμένει, παρακαλώ κάντε επανεκκίνηση την εφαρμογή.");

                                }

                                // ...
                            }
                        });
            }
            else
                makeToast(getApplicationContext(),"Το email που εισάγατε δεν είναι σε σωστή μορφή");
        }
        else if (password.length()<6)
            makeToast(getApplicationContext(),"Ο κωδικός πρόσβασης πρέπει να είναι μεγαλύτερος ή ίσος με 6 ψηφία");
        else
            makeToast(getApplicationContext(),"Παρακαλώ εισάγετε τα στοιχεία σας");
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly. GOOGLE
        auth.addAuthStateListener(mAuthStateListener);

        if (getIntent() != null) { //signout
            if (getIntent().hasExtra("where")) {
                if (getIntent().getStringExtra("where").equals("homepage_signout")) {
                    if (auth.getCurrentUser() != null) {
                        signOutFirebase();//
                        signOutGoogle();//google

                    }
                }
                if (getIntent().getStringExtra("where").equals("homepage_delete_account")) {
                    if (auth.getCurrentUser() != null) {
                        deleteAccount();
                        signOutFirebase();
                        signOutGoogle();//google
                    }
                }
            }
        }//from TOPOTHESIES CLASS

        checkForUser();
        // updateUI(auth.getCurrentUser());

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 9001);
    }

    private void signOutFirebase() {
        FirebaseAuth.getInstance().signOut();
        //makeToast(LoginSignUpPage.this, "Αποσύνδεση χρήστη επιτυχής!");
    }

    //check if i logged out from google
    private void signOutGoogle() {
        // Firebase sign out
        //auth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                        makeToast(getApplicationContext(), "Αποσύνδεση χρήστη επιτυχής!");

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                //updateUI(null);
                makeToast(getApplicationContext(), "Κάτι πήγε λάθος, σας παρακαλώ ξαναπροσπαθήστε. Σε περίπτωση που το πρόβλημα επιμένει, παρακαλώ κάντε επανεκκίνηση την εφαρμογή - GOOGLE SIGN IN FAILED");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Log.w(TAG, "signInWithCredential:success", task.getException());
                            sp.edit().putBoolean("account_google", true).apply();
                            where = "google";

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            makeToast(getApplicationContext(), "Δεν ήταν δυνατή η σύνδεση στον Google λογαριασμό σας, παρακαλώ ξαναπροσπαθήστε");
                        }

                    }
                });
    }

    private void checkForUser() {
        if (auth.getCurrentUser() != null) {
            checkQ(auth.getCurrentUser());

        }
    }

    private void checkQ(FirebaseUser currentUser){

        cuid = currentUser.getUid();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        //an o xristis ehei apantisei sto questionnaire, tote phgaine ton stin homepage alliws sto erwthmatologio
        listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot ds : dataSnapshot.child("questionnaire").getChildren()){
                    if (String.valueOf(ds.getKey()).equals(cuid)){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Log.v(TAG,"User has not answered questionnaire!");
                    Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
                    //efoson ton steilame ekei pou prepei (questionnaire) analambavei allos tin douleia
                    myRef.removeEventListener(listener);
                    startActivity(intent);

                }
                else{
                    Log.v(TAG,"User has answered questionnaire!");
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    //efoson to ehei apantisei den mas noiazei kati allo
                    myRef.removeEventListener(listener);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });

    }

    public void makeToast (Context c, String t)
    {
        Toast.makeText(c, t, Toast.LENGTH_LONG).show();
    }
}
